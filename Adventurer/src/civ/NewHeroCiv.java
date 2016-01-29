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
import chronos.pdc.character.Hero.HeroInput;
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
  public final static int MAX_NAMELEN = 45;

  /** String arrays are used instead of Lists because it is final data and easier to work with */
  public static final String[] HAIR_COLOR_LIST =
  {"bald", "black", "blonde", "brown", "gray", "red", "silver", "streaked", "white"};

  public static final String[] KLASS_LIST =
  {"Fighter", "Cleric", "Thief", "Wizard"};


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
    Hero myHero = new Hero(inputMap);
    return myHero;
  }
} // end of NewHeroCiv class
