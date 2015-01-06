/**
 * MockRace.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import java.util.ArrayList;

import mylib.ApplicationException;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;

/**
 * Fake class to test the base class methods of the Race superclass.
 * 
 * @author Alan Cline
 * @version Jun 4, 2009 // original <br>
 */
@SuppressWarnings("serial")
public class MockRace extends Race
{
  // Keep the concrete subclass for testing base class methods
  MockRace _tr = null;

  // Default ctor to allow createRace to work;
  public MockRace()
  {
    super._raceName = "MockRace";
  }

  // Default ctor to allow createRace to work;
  public String initRaceDescriptor()
  {
    return super._descriptor;
  }

  /*
   * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ STUB METHODS REQUIRED BY
   * ABSTRACT BASE CLASS ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Return min values for traits */
  public int[] getTraitMin()
  {
    return new int[] {8, 8, 8, 8, 8, 8};
  }

  /** Return max values for traits */
  public int[] getTraitMax()
  {
    return new int[] {18, 18, 18, 18, 18, 18};
  }

  @Override
  public AttributeList adjTraitsForRace(AttributeList unadjustedTraits)
  {
    return unadjustedTraits;
  }

  /** Set the Race subclass skills of the Person */
  public ArrayList<Skill> assignRacialSkills(ArrayList<Skill> skillList)
  {
    return skillList;
  };

  /** Set the Race subclass skills of the Person */
  public ArrayList<Skill> assignSkills(ArrayList<Skill> skillList)
  {
    return null;
  }

  /** Add the specific race language to the Hero's list */
  public String getLanguage()
  {
    return null;
  }

  /**
   * Each subclass Person starts at the same Race-dependent age. Age changes with time in the
   * Dungeon, particularly after Guild graduation and during promotion.
   */
  public long initAge()
  {
    return -1;
  }

  /** Calculate the height of the Person, depending on specific Race. */
  public int initHeight()
  {
    return -1;
  }

  /**
   * Each subclass prepares a description based on height ranges, then calls a generic
   * rangeDescriptor method implemented in this base class.
   */
  public String initHeightDescriptor(double height)
  {
    return null;
  }

  /** Calculate the weight of the Person, depending on specific Race. */
  public int initWeight()
  {
    return -1;
  }

  /**
   * Each subclass prepares a description based on weight ranges, then calls a generic
   * rangeDescriptor method implemented in this base class.
   */
  public String initWeightDescriptor(double weight)
  {
    return null;
  }

  /** Adjust the default magic attack value for racial factors */
  public int updateMagicAttackAdj(int defMagicAttackAdj, int con)
  {
    return -1;
  }

  @Override
  public AttributeList verifyTraits(AttributeList _trait)
      throws ApplicationException
  {
    return null;
  }

  /*
   * TEST RACE METHODS THAT ARE IMPLEMENTED ONLY IN BASE CLASS
   */

  // TODO: Defer this method to playing mode; it is not part of Character
  // intializaton
  // /** Run the given traits through the various age brackers
  // * @param traitList simulated traits
  // * @param newCat that drives the trait adjustment
  // * @return given traits adjusted by age category
  // */
  // public int[] adjTraitsForAge(int[] traitList, String newCat)
  // {
  // // REPLACE the original category with the given one
  // String originalCat = super._ageCategory;
  // super._ageCategory = newCat;
  // // TEST
  // int[] resultSet = super.adjTraitsForAge(traitList);
  // // RESET the category back to its original
  // super._ageCategory = originalCat;
  // return resultSet;
  // }

  /**
   * Run the given traits through the various starting sets for male and female
   * 
   * @param traitList simulated traits
   * @param gender male or female (male is not adjusted)
   * @return given traits adjusted by age category
   */
  public AttributeList adjTraitsForGender(AttributeList traitList,
      String gender)
  {
    // REPLACE the original category with the given one
    String originalGender = super._gender;
    super._gender = gender;
    // TEST
    AttributeList resultSet = super.adjTraitsForGender(traitList);
    // RESET the category back to its original
    super._gender = originalGender;
    return resultSet;
  }

  // /** Accesor to the protected method in the base class. All tests are run
  // against an initial Person,
  // * who by definition is a young adult Peasant. <br>
  // * Traits are adjusted by Racial characteristics of age, gender, then
  // verified against permitted
  // * Racial limits.
  // *
  // * @param defValues the initial Race-based, pre-Guild traits to adjust for
  // Race
  // * @return the modifed trait list
  // */
  // public int[] adjTraitsForRace(int[] defValues)
  // {
  // return super. adjTraitsForRace(defValues);
  // }
  //
  // /**
  // * Match a set of ages against a meaningless age bracket to confirm that
  // the
  // * method works properly.
  // *
  // * @param age
  // * the yearly age of the Person, converted to seconds for testing
  // * @param ageBrackets
  // * defines the age category for the Person
  // * @return age category corresponding to the Person's age
  // * @throws ChronosException
  // * is the Person is below minimum age
  // */
  // public Age.CATEGORY calcAgeCategory(long age, int[] ageBrackets)
  // throws ApplicationException {
  // return Age.calcAgeCategory(age, ageBrackets);
  // }

  /**
   * Pass AP and height adjusters into base movement calculations
   * 
   * @param ap Action Points (AP) determine Person's speed
   * @param height gives bonus or penalty adjustment
   * @return base movement (before weight carried is considered)
   */
  public int calcBaseMovement(int ap, int height)
  {
    // REPLACE the original category with the given one
    int originalHeight = super._height;
    super._height = height;
    // TEST
    int bMvmt = super.calcBaseMovement(ap);
    // RESET the category back to its original
    super._height = originalHeight;

    return bMvmt;
  }

  // TODO: Move this method to the CIV package
  // /** Test the permutations of the CHR descriptions.
  // * @param charisma the Person prime trait for making friends
  // * @return the description for the given charisma
  // */
  // public String initChrDescriptor(int charisma)
  // {
  // return super.initCharismaDescriptor(charisma);
  // }

  // TODO: Move this test to the CIV
  // /** Wrapper for the table lookup method. This test will cycle through
  // various heights
  // * weights, and charisma to determine the permutations.
  // *
  // * @param height of the Person; key values chosen to test bracket
  // boundaries
  // * @param weight of the Person; key values chosen to test bracket
  // boundaries
  // * @param charisma of the Person; key values chosen to test bracket
  // boundaries
  // * @return the word that describes the Person's body type
  // */
  // public String testBodyType(int height, int weight, int charisma)
  // {
  // // Save values that initBodyType uses internally for swapping out and
  // replacing after test
  // int originalWeight = _weight;
  // int originalHeight = _height;
  // _weight = weight;
  // _height = height;
  // // Get body type for internal height and weight, and given charisma
  // String bodyType = initBodyType(charisma);
  // // Restore original values
  // _weight = originalWeight;
  // _height = originalHeight;
  //
  // return bodyType;
  // }

  /**
   * Test the percentile finder.
   * 
   * @param value the Person's specific value to be applied within the range
   * @param minValue the smallest possible value for the range of the population
   * @param maxValue the largest possible value for the range of the population
   * @param descriptors the population of specific descriptions to use for range; The number of the
   *        descriptor array determines the N to calculate the N-tile.
   * @return the string that corresponds to the value within the segmented range specified by
   *         maxValue and minValue
   */
  public String testRangeDescriptor(double value, double minValue, double maxValue, 
      String[] descriptors)
  {
    return super.findRangeDescriptor(value, minValue, maxValue, descriptors);
  }

  
} // end of MockRace class

