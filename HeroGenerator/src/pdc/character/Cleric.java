/**
 * Cleric.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import java.util.ArrayList;

import pdc.character.Hero.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 */
public class Cleric extends Klass
{  
  
  /** Starting die and initial free HP for klass */
  private String _hitDie = "d8";
  private int _freeHP = 8;
  private String _startingGold = "3d6";

  protected final String[] _clericStuff = {
      "holy symbol, wooden | 4", // 0.50 lb
      "sacred satchel | 2",      // 0.25 lb
      "quarterstaff | 24",      // 3.00 lb
  };
  
  private final String[] _clericSpells = {
      "Bless", "Command", "Create Water", "Cure Light Wounds","Detect Evil", "Detect Magic",
      "Light", "Protection from Evil", "Purify Food and Drink", "Remove Fear", "Resist Cold", 
      "Sanctuary" };

  
  /**
   * Default constructor, called reflectively by Klass
   */
  public Cleric() 
  {
    _klassName = "Cleric";
    _primeNdx = PrimeTraits.WIS.ordinal();
    _hpDie = _hitDie;
    _initialHP = _freeHP;
    _goldDice = _startingGold;
    _klassItems = _clericStuff;
  } 

  public ArrayList<String> addClericalSpells(ArrayList<String> spellList) 
  {
    for (String s : _clericSpells) {
      spellList.add(s);
    }
    return spellList;
  }
  
  
} // end of Cleric class




