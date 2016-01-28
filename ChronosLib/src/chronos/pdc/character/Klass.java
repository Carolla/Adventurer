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

import mylib.pdc.MetaDie;
import chronos.pdc.Item;
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
  /** All Heroes get a free max value of hit points on their on hit die */
  protected int _initialHP = 0;
  /** Starting gold is rolled from klass-specific dice notation */
  protected String _goldDice = null;

  /** Special klass-specific table of items */
  protected String[] _klassItems = null;


  public String[][] assignKlassSkills()
  {
    return new String[0][0];
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


  // Assign initial klass-specific inventory
  public Inventory assignKlassInventory(Inventory inven, ArrayList<Item> items)
  {
    return inven;
  }

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

  // // Add an element to an array
  // protected ArrayList<String> addItem(inven, item) {
  // inven.add(item);
  // return inven;
  // }

  // // Now klass specific items
  // switch(klass) {
  // case("Cleric"):
  // addItem(inven, "holy symbol, wooden | 4"); // 0.50 lb
  // addItem(inven, "sacred satchel | 2"); // 0.25 lb
  // addItem(inven, "quarterstaff | 24"); // 3.00 lb
  // break;
  // case("Wizard"):
  // addItem(inven, "magic spell book | 32"); // 4.00 lb
  // addItem(inven, "magic bag | 2"); // 0.25 lb
  // addItem(inven, "walking stick | 24"); // 3.00 lb
  // break;
  // case("Thief"):
  // addItem(inven, "thieves' kit | 8"); // 1.00 lb
  // addItem(inven, "dagger | 24"); // 3.00 lb
  // break;
  // case("Fighter"):
  // addItem(inven, "short sword w/scabberd | 80"); // 10 lb
  // break;
  // default:
  // alert("Could not find klass when assigning inventory: " + klass);
  // break;
  // }
  // return inven;
  // }

  /**
   * Roll the klass-specific money dice
   * 
   * @return the starting gold
   */
  public int rollGold()
  {
    MetaDie md = new MetaDie();
    int gold = md.roll(_goldDice) * 10;
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
    MetaDie md = new MetaDie();
    int HP = md.roll(_hpDie) + mod + _initialHP;
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


  // ====================================================
  // OLD CODE TO BE REFACTORED IN LATER
  // ====================================================

  // // METADATA TO SUPPORT CALCULATIONS AND METHODS
  // /** Starting Hit Points for all Klasses */
  // static public final int MIN_HP = 10;
  //
  // /** Base ac for all character with no armor is 10 */
  // protected static final int NO_ARMOR = 10;
  //
  // /** Any Character gets at least 1 HP per promotion */
  // protected final int MIN_HP_BUMP = 1;
  //
  // /** Hit Points (life force) of the Person */
  // protected int _hp = Constants.UNASSIGNED;
  // /**
  // * Experience points (XP) can increase but Peasants cannot get promoted to a higher level w/o a
  // * Guild (updated as player plays)
  // */
  // protected int _xp = Constants.UNASSIGNED;
  // /**
  // * Every Person starts at level 0, but with enough XP, the Person can be promoted by a Guild to
  // a
  // * higher level
  // */
  // protected int _level = Constants.UNASSIGNED;
  // // /** Money in gp.sp notation (10 sp = 1gp); Klass-specific when starting
  // // */
  // // protected double _gold = Person.UNASSIGNED;
  // /** Armor class depending on Dex and armor */
  // protected int _ac = NO_ARMOR;
  // /**
  // * Ajustment to the number of Hit Points (HP) Person gets on promotions; due to CON but only
  // used
  // * by the Klass class.
  // */
  // protected int _hitPointAdj = Constants.UNASSIGNED;
  //
  // /**
  // * Define the kinds of Klasses a Hero might be (first one is required for new Persons)
  // */
  // static private final String[] _klassList = {"Peasant", "Cleric",
  // "Fighter", "MagicUser", "Rogue"};
  //
  /*
   * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ABSTRACT METHODS,
   * IMPLEMENTED IN SUBCLASSES ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */
  //
  // /**
  // * An unsused default constructor because Klass is an abstract class, and all subclasses must
  // * extend Klass. The planned subclasses are: Peasant, Fighter, Wizard, Cleric, and Rogue.
  // */
  // public Klass()
  // {}
  //
  // /** Set the Klass-specific special abilities. Peasants have none. */
  // // public abstract ArrayList<Skill> assignSkills(ArrayList<Skill> skillList);
  //
  // /**
  // * Each class, except Peasant, sets their subclass's characteristic Hit Point value. The Peasant
  // * is assigned a fixed value of HP that is incremented when they join a Guild.
  // */
  // abstract protected int calcHP();
  //
  // /**
  // * All Persons starts with 0 XP, but at promotion, the Guild checks to see if a certain number
  // * have been attained to raise the Person's level (which is Klass-specific). On a successful
  // * promotion to a higher level, the Person becomes more powerful.
  // */
  // public abstract int calcLevel();
  //
  // /**
  // * Each class sets their subclass's characteristic (random) starting money. The Peasant is
  // * assigned the lowest value of gp.sp which is then incremented when they join a Guild.
  // */
  // abstract protected double initCash();
  //
  // /**
  // * Each class, except Peasant, randomly generates the six traits characterizing the Person based
  // * on that Klass's prime trait. The Peasant is assigned a fixed set of mediocre traits until
  // they
  // * join a Guild, except CON and CHR which are not related to Guild traits .
  // */
  // // abstract public AttributeList rollInitialTraits();
  //
  // /*
  // * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ METHODS TO SUPPORT
  // CONCRETE
  // * SUBCLASSES ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  // */
  //
  // /**
  // * Checks if a passed string is contained in the klass list
  // *
  // * @return Is true if the string is found in the klass list
  // */
  // public static boolean isRace(String name)
  // {
  // boolean isRace = false;
  // for (int i = 0; i < _klassList.length; i++) {
  // if (name == _klassList[i]) {
  // isRace = true;
  // break;
  // }
  // }
  // return isRace;
  // }
  //
  // /**
  // * Get the possible list of Klass subclasses the user can play
  // *
  // * @return Klass list
  // */
  // static public String[] getRaceTypes()
  // {
  // return _klassList;
  // }
  //
  // /**
  // * Get the possible list of Klasses the user can play
  // *
  // * @return Klass names
  // */
  // static public String[] getKlassTypes()
  // {
  // return _klassList;
  // }
  //
  // /*
  // * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ GETTERS AND SETTERS
  // * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
  // */
  // /**
  // * Return the name of the Klass subclass, e.g. Peasant or Cleric
  // *
  // * @return the Klass's subclass name
  // */
  // public String getKlassName()
  // {
  // return _klassName;
  // }
  //
  // /**
  // * Return the life force (Hit Points) of the Person; default to MIN_HP unadjusted
  // *
  // * @return the Person's HP
  // */
  // public int getHP()
  // {
  // if (_hp == Constants.UNASSIGNED) {
  // _hp = MIN_HP;
  // }
  // return _hp;
  // }
  //
  // /**
  // * Return the life force (Hit Points) of the Person; default to MIN_HP unadjusted
  // *
  // * @return the Person's HP
  // */
  // public int getHitPointAdj()
  // {
  // return _hitPointAdj;
  // }
  //
  // /**
  // * Base method to to return the language for the Klass. By default, there are no Klass-based
  // * langauges except "Thieves' Cant", so the Rogue class must override this method
  // *
  // * @return null unless overridden
  // */
  // public String getLanguage()
  // {
  // // return "Klassiness"; // for testing
  // return null;
  // }
  //
  // /**
  // * Return the total experience points for the person
  // *
  // * @return experience points (XP)
  // */
  // public int getXP()
  // {
  // return _xp;
  // }
  //
  // /** Initialize the Person's experience: sets XP and Level to 0 */
  // public void initExperience()
  // {
  // _xp = 0;
  // _level = 0;
  // }
  //
  // /** Initialize all Person's Hit Points to same value */
  // public void initHitPoints()
  // {
  // if (_hitPointAdj == Constants.UNASSIGNED) {
  // MsgCtrl.errMsg(this,
  // "Cannot assign HP. Hit Point Adjustment is not calculated yet.");
  // } else {
  // _hp = MIN_HP + _hitPointAdj;
  // _hp = Math.max(_hp, MIN_HP_BUMP);
  // }
  // }
  //
  // /**
  // * Pack the Klass-specific fields into a data shuttle for display *
  // *
  // * @param shuttle enum values, as Strings, to hold the data
  // * @return the data shuttle
  // */
  // public EnumMap<PersonKeys, String> packShuttle(
  // EnumMap<PersonKeys, String> fields)
  // {
  // // Guard against null pointer exceptions
  // if (fields == null) {
  // return null;
  // }
  // // Load'er up!
  // // fields.put(PersonKeys.AC, String.valueOf(_ac));
  // // fields.put(PersonKeys.HP, String.valueOf(_hp));
  // // fields.put(PersonKeys.KLASSNAME, _klassName);
  // // fields.put(PersonKeys.LEVEL, String.valueOf(_level));
  // // fields.put(PersonKeys.XP, String.valueOf(_xp));
  // return fields;
  // }
  //
  // /**
  // * Update the base HP Adjustment and save the final in this class, but there are no updates from
  // * the default unless CON changes
  // *
  // * @param defAdj the default value to be updated
  // * @return the new value HP Adj value, which is the default for new Persons
  // */
  // public int updateHPAdj(int defAdj)
  // {
  // return _hitPointAdj = defAdj;
  // }
  //
  // public int calcAC(int _acMod)
  // {
  // return 10 + _acMod;
  // }

} // end of abstract Klass class
