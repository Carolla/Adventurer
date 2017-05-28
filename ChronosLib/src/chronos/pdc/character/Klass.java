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
 */
public class Klass
{
  // Spells in the Cleric or Wizard's spell book
  List<String> _spellBook = new ArrayList<String>();

  public static final String PEASANT_CLASS_NAME = "Peasant";
  public static final String FIGHTER_CLASS_NAME = "Fighter";
  public static final String CLERIC_CLASS_NAME = "Cleric";
  public static final String WIZARD_CLASS_NAME = "Wizard";
  public static final String THIEF_CLASS_NAME = "Rogue";
  public static final String[] KLASS_LIST =
  {PEASANT_CLASS_NAME, FIGHTER_CLASS_NAME, CLERIC_CLASS_NAME, WIZARD_CLASS_NAME, THIEF_CLASS_NAME};

  // KLASS-SPECIFIC ATTRIBUTES and METHODS
  protected String _klassName;
  protected PrimeTraits _primeTrait;
  protected String _hpDie;
  protected String _goldDice;
  protected TraitList _traits;
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
      case THIEF_CLASS_NAME:
        klass = new Thief(traits);
        break;
      default:
        throw new NullPointerException("Klass.createKlass(): Cannot find class requested " + klassName);
    }
    return klass;
  }
  
  public Klass(TraitList traits, String klassName, PrimeTraits trait, String hitdie, String startinggold)
  {
    _traits = traits;
    _klassName = klassName;
    _primeTrait = trait;
    _hpDie = hitdie;
    _goldDice = startinggold;
  }


  /** Gets the name of this klass */
  public String className()
  {
    return _klassName;
  }

  /**
   * Roll the klass-specific money dice plus a handful of silver pieces
   * 
   * @return the starting gold
   */
  public double rollGold()
  {
    // gold pieces
    double gold = _md.roll(_goldDice) * 10.0;
    // add silver pieces as fractional gold
    gold += _md.roll("d10")/ 10.0;
    return gold;
  }

  /**
   * Roll the HP Die for the specific Klass, plus the mod
   * 
   * @return the initial modified Hit Points
   */
  public int rollHP()
  {
    return _md.roll(_hpDie) + _traits.calcMod(PrimeTraits.CON);
  }

  
  public void loadKlassKeys(Map<PersonKeys, String> map)
  {
    map.put(PersonKeys.KLASSNAME, _klassName);
  }

  
  /*************************
   ** OVERRIDEABLE METHODS *
   *************************/
  public void addKlassSpells()
  {
    //Override
  }

  /** Assign klass specific inventory items */
  public void addKlassItems(Inventory inventory)
  {
    //Override
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

//  public void calcClassMods()
//  {
//    calcClassMods(_traits.getTrait(_primeTrait));
//  }


//  protected void calcClassMods(int trait)
//  {
//    //Override
//  }


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
    return _spellBook;
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
