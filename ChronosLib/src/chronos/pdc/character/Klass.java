/**
 * Klass.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.character;

import java.util.List;

import mylib.pdc.MetaDie;
import chronos.pdc.character.Trait.PrimeTraits;

/**
 * Defines the common methods and attributes for all Klasses. Peasant is the default Klass.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 */
public abstract class Klass
{
  /** Assign klass specific inventory items */
  public abstract Inventory addKlassItems(Inventory inventory);

  public static final String FIGHTER_CLASS_NAME = "Fighter";
  public static final String CLERIC_CLASS_NAME = "Cleric";
  public static final String WIZARD_CLASS_NAME = "Wizard";
  public static final String THIEF_CLASS_NAME = "Thief";

  // KLASS-SPECIFIC ATTRIBUTES and METHODS
  /** Name of the subclass of Klass, e.g, Peasant or Fighter */
  protected String _klassName = null;
  /** The specific prime trait for the sub-klass */
  protected PrimeTraits _primeTrait;

  /** Each klass has a specific amount of HP they start with */
  protected String _hpDie = null;
  /** Starting gold is rolled from klass-specific dice notation */
  protected String _goldDice = null;

  private static final MetaDie _md = new MetaDie();


  /**
   * Create a specific subclass of Klass based on its klass name. <br>
   * NOTE: The subclass must be in the same package as the Klass class.
   *
   * @param klassName the name of the subclass to be created
   * @return Klass, the subclass created, but referenced polymorphically; else null
   */
  static public Klass createKlass(String klassName)
  {
    switch (klassName) {
      case FIGHTER_CLASS_NAME:
        return new Fighter();
      case CLERIC_CLASS_NAME:
        return new Cleric();
      case WIZARD_CLASS_NAME:
        return new Wizard();
      case THIEF_CLASS_NAME:
        return new Thief();
    }

    throw new NullPointerException("Klass.createKlass(): Cannot find class requested " + klassName);
  }


  public List<String> addKlassSpells(List<String> spellbook)
  {
    return spellbook;
  }


  /**
   * Swap the largest trait for the prime trait of the klass: <br>
   * Fighter (STR), Cleric (WIS), Wizard (INT), and Thief (DEX)
   * 
   * @param _traits raw traits to rearrange
   * @return traits after klass adjusted
   */
  public int[] adjustTraitsForKlass(int[] _traits)
  {
    // Walk the list and find the largest trait
    int largest = -1;
    int ndx = -1;
    for (int k = 0; k < _traits.length; k++) {
      if (largest < _traits[k]) {
        largest = _traits[k];
        ndx = k;
      }
    }
    // Swap the prime trait
    _traits = swapPrime(_traits, ndx, _primeTrait.ordinal());
    return _traits;
  }

  /**
   * Roll the klass-specific money dice
   * 
   * @return the starting gold
   */
  public int rollGold()
  {
    int gold = _md.roll(_goldDice) * 10;
    return gold;
  }

  /**
   * Roll the HP Die for the specific Klass, plus the mod, plus an initial number for all Level 1
   * Heroes
   * 
   * @param mod the HP mod for the Hero, a CON-based attribute
   * @return the initial Hit Points
   */
  public int rollHP(int mod)
  {
    int HP = _md.roll(_hpDie) + mod;
    return HP;
  }

  /**
   * Swap the largest raw trait for the prime trait with the specific klass
   * 
   * @param traits the list of traits for the Hero
   * @param largest the index of the largest trait to swap
   * @param primeNdx the index of the klass-specific trait to receive the largest trait
   */
  private int[] swapPrime(int[] traits, int largest, int primeNdx)
  {
    int tmp = traits[primeNdx];
    traits[primeNdx] = traits[largest];
    traits[largest] = tmp;
    return traits;
  }

} // end of abstract Klass class
