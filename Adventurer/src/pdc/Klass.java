/**
 * Klass.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;

import mylib.Constants;
import mylib.MsgCtrl;
import chronos.Chronos;
import chronos.pdc.AttributeList;
import chronos.pdc.Skill;
import civ.PersonKeys;

/**
 * Defines the common methods and attributes for all Klasses. Peasant is the
 * default Klass.
 * <P>
 * NOTE: This class is serializable, so contains transients that will not be
 * stored.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT> Build 1.0 Feb 14 2009 // original <DD> <DT> Build 1.1 Feb 28
 *          2009 // modifed after Race and Klass re-design <DD> <DT> Build 1.2
 *          Mar 5 2009 // modifed for serialization <DD> <DT> Build 1.3 Apr 19
 *          2011 // TAA Removed acAdj, it is in Person <DD>
 *          </DL>
 */
public abstract class Klass implements Serializable
{
    // Statics and transients that are not serialized with the Race class
    // heirarchy
    /** Recommended serialization constant. */
    static final long serialVersionUID = 1100L;

    // METADATA TO SUPPORT CALCULATIONS AND METHODS
    /** Starting Hit Points for all Klasses */
    static public final int MIN_HP = 10;

    /** Base ac for all character with no armor is 10 */
    protected static final int NO_ARMOR = 10;

    /** Any Character gets at least 1 HP per promotion */
    protected final int MIN_HP_BUMP = 1;

    // KLASS-SPECIFIC ATTRIBUTES
    /** Name of the subclass of Klass, e.g, Peasant or Fighter */
    protected String _klassName = null;
    /** Hit Points (life force) of the Person */
    protected int _hp = Constants.UNASSIGNED;
    /**
     * Experience points (XP) can increase but Peasants cannot get promoted to a
     * higher level w/o a Guild (updated as player plays)
     */
    protected int _xp = Constants.UNASSIGNED;
    /**
     * Every Person starts at level 0, but with enough XP, the Person can be
     * promoted by a Guild to a higher level
     */
    protected int _level = Constants.UNASSIGNED;
    // /** Money in gp.sp notation (10 sp = 1gp); Klass-specific when starting
    // */
    // protected double _gold = Person.UNASSIGNED;
    /** Armor class depending on Dex and armor */
    protected int _ac = NO_ARMOR;
    /**
     * Ajustment to the number of Hit Points (HP) Person gets on promotions; due
     * to CON but only used by the Klass class.
     */
    protected int _hitPointAdj = Constants.UNASSIGNED;

    /**
     * Define the kinds of Klasses a Hero might be (first one is required for
     * new Persons)
     */
    static private final String[] _klassList = { "Peasant", "Cleric",
            "Fighter", "MagicUser", "Rogue" };

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * ABSTRACT METHODS, IMPLEMENTED IN SUBCLASSES
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * An unsused default constructor because Klass is an abstract class, and
     * all subclasses must extend Klass. The planned subclasses are: Peasant,
     * Fighter, Wizard, Cleric, and Rogue.
     */
    public Klass() {}

    /** Set the Klass-specific special abilities. Peasants have none. */
    public abstract List<Skill> assignSkills(List<Skill> _skills);

    /**
     * Each class, except Peasant, sets their subclass's characteristic Hit
     * Point value. The Peasant is assigned a fixed value of HP that is
     * incremented when they join a Guild.
     */
    abstract protected int calcHP();

    /**
     * All Persons starts with 0 XP, but at promotion, the Guild checks to see
     * if a certain number have been attained to raise the Person's level (which
     * is Klass-specific). On a successful promotion to a higher level, the
     * Person becomes more powerful.
     */
    public abstract int calcLevel();

    /**
     * Each class sets their subclass's characteristic (random) starting money.
     * The Peasant is assigned the lowest value of gp.sp which is then
     * incremented when they join a Guild.
     */
    abstract protected double initCash();

    /**
     * Each class, except Peasant, randomly generates the six traits
     * characterizing the Person based on that Klass's prime trait. The Peasant
     * is assigned a fixed set of mediocre traits until they join a Guild,
     * except CON and CHR which are not related to Guild traits .
     */
    abstract public AttributeList rollInitialTraits();

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * METHODS TO SUPPORT CONCRETE SUBCLASSES
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Create a specific subclass of Klass based on its klass name. <br>
     * NOTE: The subclass must be in the same package as the Klass class.
     * 
     * @param klassName
     *            the name of the subclass to be created
     * @return Klass, the subclass created, but referenced polymorphically; else
     *         null
     */
    static public Klass createKlass(String klassName)
    {
        Klass newKlass = null;
        try {
            // Class Commands must have empty constructors (no formal input
            // arguments)
            String klassPath = Chronos.getPackageName() + klassName;
            newKlass = (Klass) Class.forName(klassPath).newInstance();
        } catch (Exception e) {
            MsgCtrl.errMsgln("Klass.createKlass(): Cannot find class requested: "
                    + e.getMessage());
        }
        return newKlass;
    }

    /**
     * Checks if a passed string is contained in the klass list
     * 
     * @return Is true if the string is found in the klass list
     */
    public static boolean isRace(String name)
    {
        boolean isRace = false;
        for (int i = 0; i < _klassList.length; i++) {
            if (name == _klassList[i]) {
                isRace = true;
                break;
            }
        }
        return isRace;
    }

    /**
     * Get the possible list of Klass subclasses the user can play
     * 
     * @return Klass list
     */
    static public String[] getRaceTypes()
    {
        return _klassList;
    }

    /**
     * Get the possible list of Klasses the user can play
     * 
     * @return Klass names
     */
    static public String[] getKlassTypes()
    {
        return _klassList;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ GETTERS AND
     * SETTERS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    /**
     * Return the name of the Klass subclass, e.g. Peasant or Cleric
     * 
     * @return the Klass's subclass name
     */
    public String getKlassName()
    {
        return _klassName;
    }

    /**
     * Return the life force (Hit Points) of the Person; default to MIN_HP
     * unadjusted
     * 
     * @return the Person's HP
     */
    public int getHP()
    {
        if (_hp == Constants.UNASSIGNED) {
            _hp = MIN_HP;
        }
        return _hp;
    }

    /**
     * Return the life force (Hit Points) of the Person; default to MIN_HP
     * unadjusted
     * 
     * @return the Person's HP
     */
    public int getHitPointAdj()
    {
        return _hitPointAdj;
    }

    /**
     * Base method to to return the language for the Klass. By default, there
     * are no Klass-based langauges except "Thieves' Cant", so the Rogue class
     * must override this method
     * 
     * @return null unless overridden
     */
    public String getLanguage()
    {
        // return "Klassiness"; // for testing
        return null;
    }

    /**
     * Return the total experience points for the person
     * 
     * @return exerpeince points (XP)
     */
    public int getXP()
    {
        return _xp;
    }

    /** Initialize the Person's experience: sets XP and Level to 0 */
    public void initExperience()
    {
        _xp = 0;
        _level = 0;
    }

    /** Initialize all Person's Hit Points to same value */
    public void initHitPoints()
    {
        if (_hitPointAdj == Constants.UNASSIGNED) {
            MsgCtrl.errMsg(this,
                    "Cannot assign HP. Hit Point Adjustment is not calculated yet.");
        } else {
            _hp = MIN_HP + _hitPointAdj;
            _hp = Math.max(_hp, MIN_HP_BUMP);
        }
    }

    /**
     * Pack the Klass-specific fields into a data shuttle for display *
     * 
     * @param shuttle
     *            enum values, as Strings, to hold the data
     * @return the data shuttle
     */
    public EnumMap<PersonKeys, String> packShuttle(
            EnumMap<PersonKeys, String> fields)
    {
        // Guard against null pointer exceptions
        if (fields == null) {
            return null;
        }
        // Load'er up!
        // fields.put(PersonKeys.AC, String.valueOf(_ac));
        // fields.put(PersonKeys.HP, String.valueOf(_hp));
        // fields.put(PersonKeys.KLASSNAME, _klassName);
        // fields.put(PersonKeys.LEVEL, String.valueOf(_level));
        // fields.put(PersonKeys.XP, String.valueOf(_xp));
        return fields;
    }

    /**
     * Update the base HP Adjustment and save the final in this class, but there
     * are no updates from the default unless CON changes
     * 
     * @param defAdj
     *            the default value to be updated
     * @return the new value HP Adj value, which is the default for new Persons
     */
    public int updateHPAdj(int defAdj)
    {
        return _hitPointAdj = defAdj;
    }

    public int calcAC(int _acMod)
    {
        return 10 + _acMod;
    }

} // end of abstract Klass class
