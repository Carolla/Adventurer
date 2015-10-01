/**
 * Fighter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
public class Fighter extends Klass
{
  /** Starting die and initial free HP for klass */
  private String _hitDie = "d10";
  private int _freeHP = 10;
  private String _startingGold = "5d4";

  protected final String[] _fighterStuff = {"short sword w/scabberd | 80"};

  
  /**
   * Default constructor, called reflectively by Klass
   */
  public Fighter() 
  {
    _klassName = "Fighter";
    _primeNdx = PrimeTraits.STR.ordinal();
    _hpDie = _hitDie;
    _initialHP = _freeHP;
    _goldDice = _startingGold;
    _klassItems = _fighterStuff;
  } 


}
