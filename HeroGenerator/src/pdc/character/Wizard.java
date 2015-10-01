/**
 * Wizard.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import pdc.character.Hero.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 */
public class Wizard extends Klass
{
  /** Starting die and initial free HP for klass */
  private String _hitDie = "d4";
  private int _freeHP = 4;
  private String _startingGold = "2d4";
  
  protected final String[] _wizardStuff = {
      "magic spell book | 32",  // 4.00 lb
      "magic bag | 2",          // 0.25 lb
      "walking stick | 24",     // 3.00 lb
  };

  
  /**
   * Default constructor, called reflectively by Klass
   */
  public Wizard() 
  {
    _klassName = "Wizard";
    _primeNdx = PrimeTraits.INT.ordinal();
    _hpDie = _hitDie;
    _initialHP = _freeHP;
    _goldDice = _startingGold;
    _klassItems = _wizardStuff;
  } 

}
