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
import chronos.pdc.character.Klass;

/**
 * Input CIV: Allows the user to enter input data, validates it and creates a new Hero.
 * 
 * @author Al Cline
 * @version Sep 20, 2015 // rewrite of more complicated {@code NewHeroCiv} class <br>
 */
public class NewHeroCiv
{
  /** Max length of name for Hero */
  public final static int MAX_NAMELEN = 45;

  /** String arrays are used instead of Lists because it is final data and easier to work with */
  public static final String[] HAIR_COLOR_LIST =
      {"bald", "black", "blonde", "brown", "gray", "red", "silver", "streaked", "white"};

  // TODO: IS this duplication necessary?
  public static final String[] KLASS_LIST = Klass.KLASS_LIST;

  // ===========================================================================
  // CONSTRUCTOR
  // ===========================================================================

  // Default constructor
  public NewHeroCiv()
  {}


  /**
   * Create the new Hero from the user's input data
   * 
   * @param inputMap of user fields: name, gender, hairColor, race, and Klass
   */
  public Hero createHero(EnumMap<HeroInput, String> inputMap)
  {
    Hero myHero = new Hero(inputMap.get(HeroInput.NAME),
        inputMap.get(HeroInput.GENDER),
        inputMap.get(HeroInput.HAIR),
        inputMap.get(HeroInput.RACE),
        inputMap.get(HeroInput.KLASS));
    return myHero;
  }

} // end of NewHeroCiv class
