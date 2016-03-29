/**
 * Hunger.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import java.io.Serializable;

import mylib.Constants;
import mylib.MsgCtrl;

/**
 * Hunger determines if the Person needs to eat or not, measured in satiety points to represent
 * calories. A person's initial maximum satiety points are based on the nutritional rule of 15
 * calories per pound times their weight. This class forces persons to risk eating things in the
 * dungeon, such as food found, potions and poisons. As they burn calories, they will move from a
 * FULL state to a STARVING state. If the person has no satiety points, he will begin losing STR
 * points until dead (or until he eats something.)
 * <p>
 * For example, for an average person, max satiety points = 150 lb * 15 sp/lb * 3 days = 6750 sp
 * (which is 2250 per day, or 0.026 sp/sec for calculating calories burned during time-based
 * activities).
 * <p>
 * Different foods have different sp's, and each edible item will have a satiety amount. Standard
 * rations contain 2000 sp, enough for about a day (24 hours) of activity. Water has 0 sp, an apple
 * = 20 sp, roast beef with goat's milk = 3000 sp. <br>
 * Different activities burn calories at different rates. <BL>
 * <LI>Fighting burns calories at triple rate;</LI>
 * <LI>Moving under encumbrance burns calories at double rate;</LI>
 * <LI>Sleeping, resting, or waiting burns calories at half rate.</LI> </BL>
 * 
 * @author Alan Cline
 * @version
 *          <DL>
 *          <DT>Build 1.0 Oct 30, 2010 // original
 *          <DD>
 *          <DT>Build 1.1 April 10 2011 // TAA added check for invalid weight
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class Hunger implements Serializable
{
  /** The satiety points needed per day for the Person */
  private double _maxSatiety = Constants.UNASSIGNED;
  /** The current satiety points for the Person after activity */
  private double _curSatiety = Constants.UNASSIGNED;
  /**
   * The satiety points burned per second for the Person after activity; a convenience calculation
   */
  private double _personalBurnRate = Constants.UNASSIGNED;
  /**
   * STR interval set so that 18 STR points are lost in 72 hours, which is also the amount of time
   * curSatiety goes from maxSatiety goes to 0
   */
  private double _strDropInterval = Constants.UNASSIGNED;
  /**
   * The Person-specific satiety increment by which STR is lost during starvation
   */
  private State _hunger = State.FULL;
  /** The current temporary STR adjustment due to Hunger */
  private int _strHungerAdj = 0;
  //
  // /** The satiety the last time STR was dropped */
  // private static double _lastChange = Chronos.UNASSIGNED;

  // Support data constants for the calculations
  /** Hunger is gauged against 10% drops in full satiety points */
  // transient private final double HUNGER_INCREMENT = 0.10;
  /**
   * Amount of satiety points per pound required for a Hero's weight: 15 calories per lb
   */
  transient private final double SATIETY_RATE = 15.0;
  /** Number of days person can be active without eating */
  transient private final int ACTIVITY_BURNDAYS = 3;
  /**
   * The max STR value for purposes of Hunger calculations, starving, and recovery
   */
  transient private final int MAX_STR = 18;

  /** The states of Hunger; return null if invalid State */
  public enum State {
    FULL, NOT_HUNGRY, HUNGRY, WEAK, FAINT, STARVED
  };

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Unused default constructor */
  public Hunger()
  {}

  /**
   * Sets the maximum satiety (FULL) at the start, using the person's weight: each person consumes
   * his energy at 15 cal/lb, so a person has 3 days worth of calories as a maximum. The current
   * satiety points are calculated with each activity the Person does. The person starts losing STR
   * after all satiety points are exhausted.
   * 
   * @param weight the number of lbs the person weights (excluding inventory)
   * @return satiety of Person in satiety points
   * @throws IllegalArgumentException if the weight is non-positive
   */
  public Hunger(double weight) throws IllegalArgumentException
  {
    if (weight <= 0) {
      throw new IllegalArgumentException("Weight cannot be negative");
    }
    // INIT PERSONAL CONSTANTS
    // Find the daily caloric intake needed by weight
    double dailyEnergy = SATIETY_RATE * weight;
    // Find the satiety points needed for three days at the expected
    // burn rate
    _maxSatiety = dailyEnergy * ACTIVITY_BURNDAYS;
    // Str drops (or recovers) 18 pts over the time it takes to exhaust
    // maxSatiety (3 days)
    _strDropInterval = _maxSatiety / MAX_STR;

    // Calc burn rate per second for the individual character (a
    // convenience attribute)
    _personalBurnRate = dailyEnergy / Constants.SECS_PER_DAY;

    // The current SP is equal to the max at this point
    _curSatiety = _maxSatiety;
  }

  /*
   * PUBLIC METHODS
   */

  /**
   * Calculate the hunger of the Person as they perform certain activities. The current and max
   * satiety values are calculated, along with a decrement to indicate a particular hunger state.
   * The hunger state is used to determine if the Person is hungry enough to start losing STR
   * points. <br>
   * Later, commands will be modified to allow extra higher (e.g., fighting) or lower (e.g.,
   * resting) burn rates.
   * 
   * @param burnTime the time the burnrate is applied; typically, the time a user-command takes to
   *        execute
   * @return the current Satiety after the elapsed time
   */
  public double calcHunger(long burnTime)
  {
    // Guard against wasting time calculating with zero times
    if (burnTime <= 0L) {
      return _curSatiety;
    }
    // Decrement the current satiety by the amount of SP's burned: burn rate
    // per sec * nbr seconds
    double delta = _personalBurnRate * (double) burnTime;
    _curSatiety = _curSatiety - delta;
    if (_curSatiety <= 0) {
      calcStrHungerDelta();
    }
    return _curSatiety;
  }

  // TODO: Modify this method to handle regaining STR after eating
  /**
   * Calculate any strength penalties or recovery due to changes in hunger over time. Compare the
   * drop in satieties at the time Strength change with the satiety now. Algorithm: 18 STR points
   * are lost in 72 hours, which is the StrDropInterval based on the maxSatiety of the person.
   * 
   * After 72 hours, STR is 18/72 pts/hr = 0.25 pts per hour. Person will be WEAK for 0 - 11 hours
   * (incusive), resulting in STR reduction of -4. Person will be FAINT for 11- 48 hours, resulting
   * in STR reduction of -5 to -13. Person will be STARVED after any time after 48 hours (STR-12)
   * resulting in 18 points after 72 hrs.
   * 
   * @return new strength adjustment
   */
  public int calcStrHungerDelta()
  {
    int strAdj = 0;
    // Remove 1 STR point when current satiety goes to 0
    if (_curSatiety <= 0) {
      // How many drop interval is the current satiety?
      strAdj = (int) (_curSatiety / _strDropInterval - 1);
      // _lastChange = _curSatiety;
    }
    return strAdj;
  }

  // TODO: This belongs with some display CIV.
  /**
   * Algorithm: If the Person's current satiety points are within a certain range, Person will
   * traverse through the following stages. <br>
   * When current satiety reaches 0, Person loses 1 STR point. As satiety decreases, Person loses
   * STR at a rate such that after 72 hours, STR -= -19. All STR dependent attributes are
   * recalculated, notably AP, speed, and weight allowance each time Person loses STR. At STR = 3,
   * Person will become unconscious; speed probably has gone to 0 before that.</LI>
   * 
   * To find these states of hunger, the maxSatiety is broken into brackets of percent. In the list
   * below, values in square brackets are inclusive; values in parenthesis are. <BL>
   * <LI>FULL: Current satiety [90, 100]%. Person cannot eat when FULL, and cannot eat more than
   * 100% of their satiety points.</LI>
   * <LI>NOT_HUNGRY: [30, 90) %</LI>
   * <LI>HUNGRY: (30, 0] %.</LI>
   * <LI>WEAK: [-40, 0] %</LI>
   * <LI>FAINT: [-100, -40)</LI>
   * <LI>STARVED: < -100 Hero is dead</LI> </BL>
   * 
   * @return one of the 6 Hunger States (enum)
   */
  public State findHungerState()
  {
    // Note that the ranges are inclusive, or exclusive;
    // FULL, and WEAK ranges are bounded on both sides intentionally
    final int SP_FULL = 90; // [90. 100]
    final int SP_NOT_HUNGRY = 30; // (30, 90)
    final int SP_HUNGRY = 0; // (0, 30]
    final int SP_WEAK = -40; // [-40. 0]
    final int SP_FAINT = -100; // [-100, -40)
    // Starved is any percential greater than FAINT

    // Convert the current satiety value into a hunger percentile
    int hungryRatio = (int) ((_curSatiety / _maxSatiety) * 100.0);

    // Guard against invalid satieties
    if (hungryRatio > 100) {
      return null;
    }

    // Convert the point value to a hunger flag [90, 100]
    // FULL -- range within [90, 100]
    if (hungryRatio >= SP_FULL) {
      _hunger = State.FULL;
    }
    // NOT HUNGRY -- range within [30, 90)
    else if ((hungryRatio > SP_NOT_HUNGRY) && (hungryRatio < SP_FULL)) {
      _hunger = State.NOT_HUNGRY;
    }
    // HUNGRY -- range (0, 30)
    else if ((hungryRatio > SP_HUNGRY) && (hungryRatio <= SP_NOT_HUNGRY)) {
      _hunger = State.HUNGRY;
    }
    // WEAK -- range within [-40, 0]
    else if ((hungryRatio >= SP_WEAK) && (hungryRatio <= SP_HUNGRY)) {
      _hunger = State.WEAK;
    }
    // FAINT -- range [-100, -40)
    else if ((hungryRatio >= SP_FAINT) && (hungryRatio < SP_WEAK)) {
      _hunger = State.FAINT;
    }
    // STARVED -- range (-300, -100)
    else if (hungryRatio < SP_FAINT) {
      _hunger = State.STARVED;
    }
    return _hunger;
  }

  /**
   * Convert the hunger value to a displayable String
   * 
   * @return the string for the name of the State enum
   */
  public String findHungerString()
  {
    State h = findHungerState();
    return h.toString();
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS FOR TESTING: MockHunger
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockHunger
  {
    /** Default constructor */
    public MockHunger()
    {}

    /** Display the Hunger attributes */
    public void display()
    {
      MsgCtrl.msgln("\tMax satiety = \t\t\t\t\t" + getMaxSatiety());
      MsgCtrl.msgln("\tCurrent satiety = \t\t\t\t" + getCurrentSatiety());
      MsgCtrl.msgln("\tPersonalized burn rate/sec = " + getBurnRate());
    }

    /** Satiety burn rate per second */
    public double getBurnRate()
    {
      return Hunger.this._personalBurnRate;
    }

    /** Wrapper for getter of private attribute */
    public double getCurrentSatiety()
    {
      return Hunger.this._curSatiety;
    }

    /** Wrapper for getter of private attribute */
    public State getHungerState()
    {
      return Hunger.this._hunger;
    }

    /** Wrapper for getter of private attribute */
    public double getMaxSatiety()
    {
      return Hunger.this._maxSatiety;
    }

    /** Wrapper for getter of private attribute */
    public int getStrAdj()
    {
      return Hunger.this._strHungerAdj;
    }

    /** Wrapper for getter of private attribute */
    public double getStrDrop()
    {
      return Hunger.this._strDropInterval;
    }

    /**
     * Wrapper for setter of private attribute
     * 
     * @param newSat the value to set the current satiety points to
     */
    public void setCurrentSatiety(double newSat)
    {
      Hunger.this._curSatiety = newSat;
    }

    /**
     * Wrapper for setter of private attribute
     * 
     * @param newStrAdj the value to set the current STR adjustment for Hunger
     */
    public void setStrAdj(int newStrAdj)
    {
      Hunger.this._strHungerAdj = newStrAdj;
    }

  } // end MockHunger inner class

} // end of Hunger class
