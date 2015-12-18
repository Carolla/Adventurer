/**
 * NewHeroCiv.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.util.EnumMap;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;

/**
 * Input CIV: Allows the user to enter input data, validates it and creates a new Hero.
 * 
 * @author Al Cline
 * @version Sep 20, 2015 // rewrite of more complicated {@code NewHeroCiv} class <br>
 */
public class NewHeroCiv extends BaseCiv
{
  /** Max length of name for Hero */
  public final int MAX_NAMELEN = 45;

  /** String arrays are used instead of Lists because it is final data and easier to work with */
  private final String[] _hairColorList =
      {"bald", "black", "blonde", "brown", "gray", "red", "silver", "streaked", "white"};

  private final String[] _klassList =
      {"Fighter", "Cleric", "Rogue", "Wizard"};

  private final String[] _raceList =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  /** Input data fields to create a new hero */
  public enum HeroInput {
    NAME, GENDER, HAIR, RACE, KLASS
  };

  /** ErrorCode for type of error encountered on input */
  public enum ErrorCode {
    NO_ERROR, NAME_MISSING, NAME_TOO_LONG, NAME_NOT_UNIQUE, HERO_NOT_CREATED
  };

  private MainframeCiv _mfCiv;

  // ===========================================================================
  // CONSTRUCTOR
  // ===========================================================================

  /**
   * Controls the creation of the new Hero input panel, data validation, and displaying the new Hero
   * stats
   * 
   * @param mf to callback for placing created panels, and relinquishing final control
   */
  public NewHeroCiv(MainframeCiv mfCiv, HeroRegistry heroReg)
  {
    _mfCiv = mfCiv;
  }

  // ===========================================================================
  // PUBLIC METHODS
  // ===========================================================================

  // Return the current state to the previous state
  public void back()
  {
    _mfCiv.back();
  }

  /**
   * Create the new Hero from the user's input data
   * 
   * @param inputMap of user fields: name, gender, hairColor, race, and Klass
   * @param ErrorCode of NO_ERROR, or some ErrorCode value if a problem occured
   */
  public Hero createHero(EnumMap<HeroInput, String> inputMap)
  {
    String name = inputMap.get(HeroInput.NAME);
    String gender = inputMap.get(HeroInput.GENDER);
    String hairColor = inputMap.get(HeroInput.HAIR);
    String raceName = inputMap.get(HeroInput.RACE);
    String klassName = inputMap.get(HeroInput.KLASS);

    Hero myHero = null;

    try {
      myHero = new Hero(name, gender, hairColor, raceName, klassName);
    } catch (InstantiationException ex) {
      System.err.println("NewHeroCiv.createHero(): " + ex.getMessage());
    }
    return myHero;
  }

  // Getters for the input data options
  public String[] getHairColors()
  {
    return _hairColorList;
  }

  public String[] getKlasses()
  {
    return _klassList;
  }

  public String[] getRaces()
  {
    return _raceList;
  }


  /**
   * Retrieve the new Hero input data for validation or Hero creation. Biggest check is if the hero
   * name is unique (does not exist in the Hero registry)
   * 
   * @param heroInput contains the five input fields: name, gender, hair color, race, and klass (in
   *        that order)
   * @return false if error widget needs to be displayed
   */
  public ErrorCode validate(EnumMap<HeroInput, String> inputMap)
  {
    String name = inputMap.get(HeroInput.NAME);
    ErrorCode err = isValid(name);
    return err;
  }

  /**
   * Verify the name is valid, which means <br>
   * (1) it must be within valid field length, and <br>
   * (2) it must be unique to names in the Dormitory.
   * 
   * @param name of the Hero
   * @return true if name is valid and unique
   */
  private ErrorCode isValid(String name)
  {
    // TODO Overlong name size should not be possible if Input widget defined properly
    ErrorCode retflag = ErrorCode.NO_ERROR;
    if (name == null) {
      retflag = ErrorCode.NAME_MISSING;
    } else if (name.length() > MAX_NAMELEN) {
      retflag = ErrorCode.NAME_TOO_LONG;
    }
    return retflag;
  }


} // end of NewHeroCiv class
