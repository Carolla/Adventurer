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
import java.util.List;
import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.character.TraitList.PrimeTraits;
import mylib.pdc.MetaDie;

/**
 * Defines the common methods and attributes for all Klasses. Peasant is the default Klass.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 *          June 2 2017 // refactored for clearer organization <br>
 */
public abstract class Klass
{
  // Spells in the Cleric or Wizard's spell book
  List<String> _spellBook = new ArrayList<String>();

  public static final String PEASANT_CLASS_NAME = "Peasant";
  public static final String FIGHTER_CLASS_NAME = "Fighter";
  public static final String CLERIC_CLASS_NAME = "Cleric";
  public static final String WIZARD_CLASS_NAME = "Wizard";
  public static final String ROGUE_CLASS_NAME = "Rogue";
  public static final String[] KLASS_LIST = {PEASANT_CLASS_NAME, FIGHTER_CLASS_NAME,
      CLERIC_CLASS_NAME, WIZARD_CLASS_NAME, ROGUE_CLASS_NAME};

  // KLASS-SPECIFIC ATTRIBUTES and METHODS
  protected String _klassName;
  protected int _level;
  protected int _XP;
  protected int _HP_Max;
  protected int _currentHP;
  protected PrimeTraits _primeTrait;
  protected TraitList _traits;
  private static final MetaDie _md = new MetaDie();


  // ====================================================
  // ABSTRACT METHODS FOR SUBCLASSES
  // ====================================================

  /** Each klass has a unique amount of starting gold */
  protected abstract double rollStartingGold();

  /** Each klass has a unique amount of starting HO */
  protected abstract int rollHP();


  // ====================================================
  // CONSTRUCTORS AND RELATED METHODS
  // ====================================================

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
    switch (klassName)
    {
      case PEASANT_CLASS_NAME:
        klass = new Peasant(traits);
        break;
      case FIGHTER_CLASS_NAME:
        klass = new Fighter(traits);
        break;
      case CLERIC_CLASS_NAME:
        klass = new Cleric(traits);
        break;
      case WIZARD_CLASS_NAME:
        klass = new Wizard(traits);
        break;
      case ROGUE_CLASS_NAME:
        klass = new Thief(traits);
        break;
      default:
        throw new NullPointerException(
            "Klass.createKlass(): Cannot find class requested " + klassName);
    }
    return klass;
  }


  /**
   * Method called by each subsclass with its own specific data
   * 
   * @param traits prime traits of Hero
   * @param klassName Peasant, Fighter, Cleric, Rogue, Wizard
   * @param trait except for Peasant, the trait set to max for the klass
   * @param hitdie starting hit points die to be rolled
   * @param startinggold starting gold for specific klass
   */
  protected Klass(TraitList traits, String klassName, PrimeTraits trait)
  {
    _traits = traits;
    _klassName = klassName;
    _primeTrait = trait;
    _level = 0;
    _XP = 0;
    _HP_Max = rollHP();
    _currentHP = _HP_Max;
  }


  public void addKlassSpells()
  {
    // Override
  }

  /** Assign klass specific inventory items */
  public void addKlassItems(Inventory inventory)
  {
    // Override
  }


  /**
   * Swap the largest trait for the prime trait of the klass: <br>
   * Fighter (STR), Cleric (WIS), Wizard (INT), and Thief (DEX)
   * 
   * @param _traits raw traits to rearrange
   * @return traits after klass adjusted
   */
  public void adjustTraitsForKlass(TraitList traits)
  {
    traits.swapPrime(_primeTrait);
  }


  public boolean canUseMagic()
  {
    return false;
  }


  /** Gets the name of this klass */
  public String className()
  {
    return _klassName;
  }


  public List<String> getSkills()
  {
    return new ArrayList<String>();
  }


  public double getStartingGold()
  {
    return rollStartingGold();
  }


  public List<String> getSpells()
  {
    return _spellBook;
  }

  public void loadKlassKeys(Map<PersonKeys, String> map)
  {
    map.put(PersonKeys.KLASSNAME, _klassName);
    map.put(PersonKeys.HP, Integer.toString(_currentHP));
    map.put(PersonKeys.HP_MAX, Integer.toString(_HP_Max));
    map.put(PersonKeys.LEVEL, Integer.toString(_level));
    map.put(PersonKeys.XP, Integer.toString(_XP));
  }

  public int getCurrentHP()
  {
    return _currentHP;
  }

  public int getHP_Max()
  {
    return _HP_Max;
  }

  /**
   * All Peasants start at 10 hp, plus HP modifier. Further HP rolls by other Klasses depend on the
   * klass-specific HP die
   * 
   * @param the starting dice for a klass's Hit Points
   * @return the initial modified Hit Points
   */
  protected int rollHP(String hpDie)
  {
    return _md.roll(hpDie) + _traits.calcMod(PrimeTraits.CON);
  }


  /**
   * Roll the klass-specific money dice plus a handful of silver pieces
   * 
   * @param dice string for how much klass-specific gold Hero starts with
   * @return the starting gold
   */
  protected double rollStartingGold(String startingGoldDice)
  {
    // Gold pieces
    double gold = _md.roll(startingGoldDice) * 10.0;
    // Add silver pieces as fractional gold
    gold += (_md.roll("d10") / 10.0);
    return gold;
  }


  /*************************
   ** OVERRIDDEN METHODS *
   *************************/

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
