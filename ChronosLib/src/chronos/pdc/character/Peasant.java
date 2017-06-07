/**
 * 
 * Fighter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved Permission to make
 * digital or hard copies of all or parts of this work for commercial use is prohibited. To
 * republish, to post on servers, to reuse, or to redistribute to lists, requires prior specific
 * permission and/or a fee. Request permission to use from Carolla Development, Inc. by email:
 * acline@carolla.com
 */

package chronos.pdc.character;

/**
 * @author Al Cline
 * @version May 21, 2017 // original <br>
 *          June 2 2017 // modified to support hero peasant refactoring <br>
 */
public class Peasant extends Klass
{

  /** Starting die and initial free HP for klass */
  // Starting HP is fixed at 10, but string notation is required
  private static final String _hitDie = "d1+9";
  private static final String _startingGold = "1d4"; // multiplied by 10

  /**
   * Default constructor, called reflectively by Klass Peasant klass has no prime trait
   * 
   * @param traits six prime traits of any Hero
   */
  public Peasant(TraitList traits)
  {
    super(traits, PEASANT_CLASS_NAME, null);
  }

  /**
   * Peasant has no additional inventory than basic default inventory items
   * 
   * @param inven basic inventory is not augmented
   */
  @Override
  public void addKlassItems(Inventory inven)
  {}



  /** Peasants get a fixed 10 pts, augmented later when they join a guild */
  @Override
  public int rollHP()
  {
    return rollHP(_hitDie);
  }

  
  /** Peasants get a starting number of gold */
  @Override
  public double rollStartingGold()
  {
    return rollStartingGold(_startingGold);
  }

  
} // end of Peasant class
