/**
 * Klass.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.character;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import mylib.pdc.MetaDie;
import chronos.civ.PersonKeys;
import chronos.pdc.character.TraitList.PrimeTraits;

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
  protected String _klassName;
  protected PrimeTraits _primeTrait;
  protected String _hpDie;
  protected String _goldDice;
  protected TraitList _traits;
  private int _HP;

  private static final MetaDie _md = new MetaDie();

  /**
   * Create a specific subclass of Klass based on its klass name. <br>
   * NOTE: The subclass must be in the same package as the Klass class.
   *
   * @param klassName the name of the subclass to be created
   * @param traits 
   * @return Klass, the subclass created, but referenced polymorphically; else null
   */
  static public Klass createKlass(String klassName, TraitList traits)
  {
    Klass klass = null;
    switch (klassName) {
      case FIGHTER_CLASS_NAME:
        klass = new Fighter(traits);
      case CLERIC_CLASS_NAME:
        klass =  new Cleric(traits);
      case WIZARD_CLASS_NAME:
        klass =  new Wizard(traits);
      case THIEF_CLASS_NAME:
        klass =  new Thief(traits);
    }
    klass.rollHP();

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
  public TraitList adjustTraitsForKlass(TraitList traits)
  {
    // Walk the list and find the largest trait
    int largest = -1;
    PrimeTraits largestTrait = PrimeTraits.STR;
    
    for (PrimeTraits trait : PrimeTraits.class.getEnumConstants()) {
      int traitVal = traits.getTrait(trait);
      if (largest < traitVal) {
        largest = traitVal;
        largestTrait = trait;
      }
    }
    
    // Swap the prime trait
    traits.swapPrime(_primeTrait, largestTrait);
    return traits;
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
  protected void rollHP()
  {
    _HP = _md.roll(_hpDie) + _traits.getHpMod();
  }


  public boolean canUseMagic()
  {
    return false;
  }


  public List<String> getSkills()
  {
    return new ArrayList<String>();
  }

  public List<String> getSpells()
  {
    return new ArrayList<String>();
  }


  public void loadKlassTraits(EnumMap<PersonKeys, String> map)
  {
    map.put(PersonKeys.KLASSNAME, _klassName);
    map.put(PersonKeys.HP, "" + _HP);
    map.put(PersonKeys.HP_MAX, "" + _HP);
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_klassName == null) ? 0 : _klassName.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Klass other = (Klass) obj;
    if (_klassName == null) {
      if (other._klassName != null)
        return false;
    } else if (!_klassName.equals(other._klassName))
      return false;
    return true;
  }
} // end of abstract Klass class
