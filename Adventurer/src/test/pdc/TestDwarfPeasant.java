/**
 * TestHumanPeasant.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import pdc.character.Person;

import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * Tests the Peasant object of all Races. Private methods of Peasant are tested
 * through a MockPerson inner class that acts as a public wrapper for the
 * private methods. The public interface is tested through an instantiation of
 * the Person class.
 * 
 * @author Alan Cline
 * @author Timothy Armstrong
 * @version <DL>
 *          <DT>Build 1.0 Feb 13 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Feb 28 2009 // revised for Race and Klass re-design
 *          <DD>
 *          <DT>Build 1.2 Apr 29 2009 // add MockPerson private testing
 *          <DD>
 *          <DT>Build 1.3 May 11 2009 // added Age calcs, which affects traits
 *          and most secondary calculations
 *          <DD>
 *          <DT>Build 2.0 Feb 20 2011 // redisgned to fit new use case
 *          <DT>Build 2.1 May 15 2011 // TAA fixed lots of errors
 *          </DL>
 */
public class TestDwarfPeasant extends TestCase
{
    /* GENERIC METADATA CONSTANTS */
    // Specific independent Person constants; ratios have large precisions
    private final int STARTING_HP = 10; // peasant's starting hit points
    // private final double STARTING_GOLD = 15.8; // peasant's starting gold
    //
    // // Limit ranges for movement calc
    // private final int DEFAULT_MVMT = 4;
    // private final int PENALTY_HEIGHT = 48;
    // private final int BONUS_HEIGHT = 78;
    // private final double AVG_TRAIT = 11.5;
    // private final int STR = 0;
    // private final int INT = 1;
    // private final int WIS = 2;
    // private final int CON = 3;
    // private final int DEX = 4;
    // private final int CHR = 5;

    /* Dwarf METADATA CONSTANTS */
    // Specific Dwarf constants
    private final int AVG_MALE_HEIGHT = 48; // inches
    private final int AVG_MALE_WEIGHT = 150; // pounds

    private final int AVG_FEMALE_HEIGHT = 46; // inches
    private final int AVG_FEMALE_WEIGHT = 120; // pounds

    /* SPECIFIC PEASANT ATTRIBUTES TO COMPARE WHEN TESTING */
    // Dwarf Peasant attributes for Male
    private final String _hisName = "Clyde P. Falsoon";
    private final String _hisGender = "Male";
    private final String _hisOccup = "Gambler";
    private final String _hisHair = "no"; // bald

    private AttributeList _traits = null;
    // private int[] _defaultTrait = {11, 11, 11, 11, 11, 11};
    // after gender & age adjustment, -1 WIS, +2 CON, -1 CHR
    private int[] _guyTrait = { 11, 11, 10, 13, 12, 10 };

    // private int _hisAP = _guyTrait[STR] + _guyTrait[DEX]; // for STR + DEX =
    // 22
    // private int _hisWtAllow = 2249; // oz
    private double _startingAge = 42.5; // Dwarfs start at 85% of YoungAdult

    // private int _hisMvmt = 3; // 3 plus height bonus
    // private double mOVERBEARING = 66.0;

    // Dwarf Peasant attributes for Female
    private final String _herName = "Ann Pennington";
    private final String _herGender = "Female";
    private final String _herOccup = "Weaver";
    private final String _herHair = "blonde";
    // After gender adjustment: -1 STR, +1 CON, +1 CHR
    // After age adjustment: -1 WIS, +1 CON
    // After race adjustment: -1 CHR, +1 CON
    private int[] _galTrait = { 10, 11, 10, 14, 11, 11 }; // female-adjusted
                                                          // traits
                                                          // private int _herAP
                                                          // = _galTrait[STR] +
                                                          // _galTrait[DEX]; //
                                                          // for STR + DEX = 21
    //
    // private int _herMvmt = 3;
    // private int fOVERBEARING = 66;

    // General expected values for all Clyde and Ann in this test class
    private final String _raceName = "Dwarf";
    private final String _klassName = "Peasant";
    private int _maxLangs = 2; // Dwarfs can know Common and one other
    private int _langsKnown = 2; // Dwarfs start with knowing Common only
    private int _magicAttackAdj = 3; // for Dwarf Peasant
    private int _hitPointAdj = 0; // for a CON = 10
    private int _toHitAdjMissile = 0; // for a DEX = 10
    private int _acAdj = 0; // for a DEX = 10

    // private int _herWtAllow = 1516; // oz
    private int _inventoryOzLoad = 464;

    Person _pGuy = null;
    Person _pGal = null;
    Person.MockPerson _mock = null; // accesses both genders

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ LET THE TESTS
     * BEGIN! ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Create the objects needed for testing; called before each test */
    protected void setUp()
    {
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.auditMsgsOn(false);
        try {
            // Pass both objects to Person constructor to create beginning
            // Kharacter
            _pGuy = new Person(_hisName, Race.MALE, _hisOccup, _hisHair,
                    _raceName, _klassName);
            // Pass both objects to Person constructor to create beginning
            // Kharacter
            _pGal = new Person(_herName, Race.FEMALE, _herOccup, _herHair,
                    _raceName, _klassName);
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        // Create the mock object for testing Person's private methods
        _mock = _pGuy.new MockPerson();
    }

    /** Release the objects created during setup; called after each test */
    protected void tearDown()
    {
        // Clear out Person and MockPerson
        _pGuy = null;
        _pGal = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
    }

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * SPECIFIC PERSON TEST: Male Dwarf Peasant
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Male Dwarf Peasant was created, test for proper attributes Setup creates
     * the Person and MockPerson objects
     */
    public void testMaleDwarfPeasant()
    {
        MsgCtrl.auditMsgsOn(true);
        // Person is already created; dump internals as a check
        // _pGuy.dump();
        _traits = _pGuy.getTraits();

        // 1. Profile Data
        assertTrue(_pGuy.getName().equalsIgnoreCase(_hisName));
        assertTrue(_mock.getHairColor().equalsIgnoreCase(_hisHair)); // no
                                                                     // getter
        assertTrue(_pGuy.getGender().equalsIgnoreCase(_hisGender));
        assertEquals(_pGuy.getOccupation().getName(), _hisOccup);
        assertTrue(_pGuy.getRaceName().equalsIgnoreCase(_raceName));
        assertTrue(_pGuy.getKlassName().equalsIgnoreCase(_klassName));

        // -- Calculate Person's Age and Bracket: Race Dependent
        // All Person's start at 17 years of age, Young Adult
        assertTrue(_pGuy.getAge() == _startingAge);

        // 2. Person Traits: all young males should be 11 except for WIS (10)
        // and CON (12)
        assertEquals(new AttributeList(_guyTrait), _pGuy.getTraits());

        // 3. Languages (INT): For a Dwarf, language list contains only Common,
        // and one other learnable
        assertTrue(_pGuy.knowsLanguage("Common") == true);
        assertTrue(_pGuy.getMaxLangs() == _maxLangs);
        assertTrue(_pGuy.getLangsKnown().size() == _langsKnown);

        // 4. WIS modifier: Magical Attack Adjusment
        assertTrue(_pGuy.getMagicAttackMod() == _magicAttackAdj);

        // 5. CON Modifers: HitPointAdj
        assertTrue(_pGuy.getHitPointAdj() == _hitPointAdj);

        // 6. DEX Modifiers: ToHitAdjMissile and ArmorClassAdj
        assertTrue(_pGuy.getToHitMissileMod() == _toHitAdjMissile);
        assertTrue(_pGuy.getACMod() == _acAdj);

        // 7. HEIGHT and WEIGHT (unchanging attributes, but have random
        // calculation)
        // Check limits of 59" (4'11") to 81" (6'11") (Average = 70" = 5'10")
        int lowHt = (int) Math.round(AVG_MALE_HEIGHT
                * (1.0 - MetaDie.HALF_SIGMA));
        int highHt = (int) Math.round(AVG_MALE_HEIGHT
                * (1.0 + MetaDie.HALF_SIGMA));
        int ht = _pGuy.getHeight();
        assertTrue((ht >= lowHt) && (ht <= highHt));

        // Check limits of 147# to 203# (Average = 175#)
        int lowWt = (int) Math.round(AVG_MALE_WEIGHT
                * (1.0 - MetaDie.HALF_SIGMA));
        int highWt = (int) Math.round(AVG_MALE_WEIGHT
                * (1.0 + MetaDie.HALF_SIGMA));
        int wt = _pGuy.getWeight();
        assertTrue((wt >= lowWt) && (wt <= highWt));

        // 8. STR Modifiers: toHitAdjMelee, DamageAdj, and WeightAllowance
        assertTrue(_pGuy.getToHitMissileMod() == 0);
        // assertTrue(_pGuy.getToHitMelee()== 0);
        // STR / AVG_STR = 11/11.5 = .957 of Gal's weight
        int getWtAllow = _pGuy.getWtAllowance();
        int calcWtAllow = (int) ((_traits.get(ATTRIBUTE.STR) / 11.5)
                * _pGuy.getWeight() * Constants.OUNCES_PER_POUND);
        assertEquals(getWtAllow, calcWtAllow, 1.0);

        // 8. HUNGER
        // All Characters are initialized as FULL -- nohunger
        // MsgCtrl.msgln("HungerState is: " + _pGuy.findHungerState());
        assertTrue(_pGuy.findHungerState().toString().equals("FULL"));

        // 9. EXPERIENCE
        // Experience is measured in XP and grouped by Level; all start at 0
        assertTrue((_pGuy.getLevel() == 0) && (_pGuy.getXP() == 0));

        // 10. HIT POINTS: All Peasants start with same value
        // Hit Points are Klass-dependent values; for Dwarf Peasant, set to a
        // fixed value
        assertTrue(_pGuy.getHP() == STARTING_HP);

        // 12. GOLD: The Peasant gets the default minimum, and adds to his load.
        // Inventory test to check weight for inventory load
        assertTrue(_pGuy.getInventoryWeight() == _inventoryOzLoad);
        // Test for proper starting gold, and a new load once he has it
        // assertEquals(_pGuy.getInventory().getCash(), STARTING_GOLD, 0.2);
        assertEquals(_pGuy.getInventory().getGoldBanked(), 0.0, 0.05);
        assertTrue(_pGuy.calcLoad() == _inventoryOzLoad);

        // 13. ACTION POINTS: Calc AP for movement, overbearing, grappling, and
        // pummeling
        // int ap = _pGuy.calcAP();
        // assertTrue(ap == _hisAP);
        // double speed = _pGuy.getSpeed();
        // assertEquals(speed, _hisMvmt, .1);
        // assertEquals(_pGuy.calcOverbearing(_hisAP, _hisWtAllow,
        // _inventoryOzLoad),
        // mOVERBEARING, .1);
        // assertTrue(_pGuy.calcGrappling(_hisAP) == _hisAP);
        // assertTrue(_pGuy.calcPummeling(_hisAP) == _hisAP);

        // 14. SPECIAL ABILITIES are compiled from occupation, Race, and Klass
        // abilities
        // This Dwarf has only the Luck skill of a gambler
        assertTrue(_pGuy.hasSkill("Luck"));
        assertTrue(_pGuy.hasSkill("Infravision 60'"));
        assertTrue(_pGuy.hasSkill("Geasing"));
        ArrayList<Skill> skills = _pGuy.getSkills(); // Luck plus readOnly skill
        assertTrue(skills.size() == 4);
        // Although all Persons have this attribute, Dwarfs have it for 0 feet
        // assertEquals(_pGuy.getInfraDistance(), 0);

        // 15. BUILD DESCRIPTION: built from many other attributes
        // The method was throoughyl tested for various values elsewhere
        // String desc =
        // "A tall male with no hair. He is weather-beaten and tough.";
        // assertTrue(_pGuy.getDescription().equals(desc));

        MsgCtrl.auditMsgsOn(true);
    }

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * SPECIFIC PERSON TEST: Female Dwarf Peasant
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Test for Female Dwarf Peasant Setup creates the Person and MockPerson
     * objects
     */
    public void testFemaleDwarfPeasant()
    {
        MsgCtrl.auditMsgsOn(true);
        // Person is already created; dump internals as a check
        // _pGal.dump();

        // 1. Profile Data
        assertTrue(_pGal.getName().equalsIgnoreCase(_herName));
        assertTrue(_pGal.getGender().equalsIgnoreCase(_herGender));
        assertEquals(_pGal.getOccupation().getName(), _herOccup);
        assertTrue(_pGal.getRaceName().equalsIgnoreCase(_raceName));
        assertTrue(_pGal.getKlassName().equalsIgnoreCase(_klassName));
        _traits = _pGal.getTraits();

        // -- Calculate Person's Age and Bracket: Race Dependent
        // All Person's start at 17 years of age, Young Adult
        assertTrue(_pGal.getAge() == _startingAge);
        // TODO: Move test to CIV
        // assertTrue(_pGal.getAgeCategory().equals("Young Adult"));

        // 2. Person Traits: all young females should be 11 except for -1 STR,
        // -1 WIS,
        // +1 CON. +1 CHR = {10, 11, 10, 13, 11, 12}; // female- and
        // age-adjusted traits
        assertEquals(new AttributeList(_galTrait), _pGal.getTraits());

        // 3. Languages (INT): For a Dwarf, language list contains only Common,
        // and one other learnable
        assertTrue(_pGal.knowsLanguage("Common") == true);
        assertTrue(_pGal.getMaxLangs() == _maxLangs);
        assertTrue(_pGal.getLangsKnown().size() == _langsKnown);

        // 4. WIS modifier: Magical Attack Adjusment
        assertTrue(_pGal.getMagicAttackMod() == _magicAttackAdj);

        // 5. CON Modifers: HitPointAdj
        assertTrue(_pGal.getHitPointAdj() == _hitPointAdj);

        // 6. DEX Modifiers: ToHitAdjMissile and ArmorClassAdj
        // assertTrue(_pGal.getToHitMissileMod() == _toHitAdjMissile);
        assertTrue(_pGal.getACMod() == _acAdj);

        // 7. HEIGHT and WEIGHT (unchanging attributes, but have random
        // calculation)
        // Check limits of 54" (4'6") to 74" (6'2") (Average = 64" = 5'4")
        int lowHt = (int) Math.round(AVG_FEMALE_HEIGHT * (1.0 - MetaDie.SIGMA));
        int highHt = (int) Math
                .round(AVG_FEMALE_HEIGHT * (1.0 + MetaDie.SIGMA));
        int ht = _pGal.getHeight();
        assertTrue((ht >= lowHt) && (ht <= highHt));

        // Check limits of 109# to 151# (Average = 130#)
        int lowWt = (int) Math.round(AVG_FEMALE_WEIGHT * (1.0 - MetaDie.SIGMA));
        int highWt = (int) Math
                .round(AVG_FEMALE_WEIGHT * (1.0 + MetaDie.SIGMA));
        int wt = _pGal.getWeight();
        assertTrue((wt >= lowWt) && (wt <= highWt));

        // 8. HUNGER
        // All Characters are initialized as FULL -- nohunger
        assertTrue(_pGuy.findHungerState().toString().equals("FULL"));

        // 9. STR Modifiers: toHitAdjMelee, DamageAdj, and WeightAllowance
        assertTrue(_pGal.getToHitMissileMod() == 0);
        // assertTrue(_pGal.getToHitMelee()== 0);
        // STR / AVG_STR = 10/11.5 = .870 of Gal's weight
        // assertEqual(_pGal.getWtAllowance() == _herWtAllow);
        int getWtAllow = _pGal.getWtAllowance();
        int calcWtAllow = (int) ((_traits.get(ATTRIBUTE.STR) / 11.5)
                * _pGal.getWeight() * Constants.OUNCES_PER_POUND);
        assertEquals(getWtAllow, calcWtAllow, 1.0);

        // 10. EXPERIENCE
        // Experience is measured in XP and grouped by Level; all start at 0
        assertTrue((_pGal.getLevel() == 0) && (_pGal.getXP() == 0));

        // 11. HIT POINTS: All Peasants start with same value
        // Hit Points are Klass-dependent values; for Dwarf Peasant, set to a
        // fixed value
        assertTrue(_pGal.getHP() == STARTING_HP);

        // 12. GOLD: The Peasant gets the default minimum, and adds to his load.
        // Inventory test to check weight for inventory load
        assertTrue(_pGal.getInventoryWeight() == _inventoryOzLoad);
        // Test for proper starting gold, and a new load once he has it
        // assertEquals(_pGal.getInventory().getCash(), STARTING_GOLD, 0.05);
        assertEquals(_pGal.getInventory().getGoldBanked(), 0.0, 0.05);
        assertTrue(_pGal.calcLoad() == _inventoryOzLoad);

        // 13. ACTION POINTS: Calc AP for movement, overbearing, grappling, and
        // pummeling
        // int ap = _pGal.calcAP();
        // assertTrue(ap == _herAP);
        // double speed = _pGal.getSpeed();
        // assertEquals(speed, _herMvmt, .1);
        // assertEquals(_pGal.calcOverbearing(_hisAP, _herWtAllow,
        // _inventoryOzLoad),
        // fOVERBEARING, .1);
        // assertTrue(_pGal.calcGrappling(_herAP) == _herAP);
        // assertTrue(_pGal.calcPummeling(_herAP) == _herAP);

        // 14. SPECIAL ABILITIES are compiled from occupation, Race, and Klass
        // abilities
        // This Dwarf has only the Luck skill of a gambler
        assertTrue(_pGal.hasSkill("Appraise Tapestries"));
        assertTrue(_pGal.hasSkill("Infravision 60'"));
        assertTrue(_pGal.hasSkill("Geasing"));
        ArrayList<Skill> skills = _pGal.getSkills(); // Appraise plus readOnly
                                                     // skill
        assertTrue(skills.size() == 4);
        // Although all Persons have this attribute, Dwarfs have it for 0 feet
        // assertEquals(_pGal.getInfraDistance(), 0);

        // 15. BUILD DESCRIPTION: built from many other attributes
        // The method was throughll tested for various values elsewhere
        // String desc =
        // "A thin female with blonde hair. She is nothing special to look at.";
        // assertTrue(_pGal.getDescription().equals(desc));

        MsgCtrl.auditMsgsOn(true);
    }

    // /** Test the total number of languages the Person can know. Common is
    // always known,
    // * and a racial language is always known if non-Dwarf, so maxLangs is a
    // minimum of 1 or 2.
    // * In some cases, half-Elves and half-Orcs do not know their racial
    // language.*/
    // public void testInitMaxLangs()
    // {
    // // This method is tested, can turn off message now
    // MsgCtrl.auditMsgsOn(true);
    // MsgCtrl.errorMsgsOn(true);
    // MsgCtrl.msgln(this, "\ttestInitMaxLangs()");
    //
    // int minInt = 10;
    // // NORMAL -- walk through intelligence values for Dwarf
    // // int intelligence, ArrayList<String> langs
    // for (int k=6; k < 20; k++) {
    // int maxLangs = _mock.testInitMaxLangs(k);
    // MsgCtrl.msgln("INT = " + k +": " + maxLangs + ";\t");
    // if (k <= minInt) {
    // assertEquals(maxLangs, 1);
    // }
    // else if (k > minInt)
    // assertEquals(maxLangs, k/2-3);
    // }
    //
    // // ERROR -- invalid intelligence
    // assertEquals(_mock.testInitMaxLangs(7), 1);
    //
    // }

    /** Test for saving a Person */
    public void testSavePerson()
    {
        MsgCtrl.auditMsgsOn(true);
        // Pass both objects to Person constructor to create beginning Kharacter
        Person pGal = null;
        try {
            pGal = new Person(_herName, Race.FEMALE, _herOccup, _herHair,
                    "Dwarf", "Peasant");
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        // pGal.dump();
        // Call the save service
        assertTrue(pGal.save());
        MsgCtrl.auditMsgsOn(true);
    }

    // /**
    // * Compare the prime traits against expected values
    // *
    // * @param traits
    // * to be verifued
    // * @param expTraits
    // * to compare againt
    // * @return true if all trait values match, else false
    // */
    // private boolean equalArrays(int[] traits, int[] expTraits) {
    // MsgCtrl.auditMsgsOn(false);
    //
    // boolean retval = true;
    // for (int k = 0; k < traits.length; k++) {
    // if (traits[k] != expTraits[k]) {
    // retval = false;
    // break;
    // }
    // }
    // return retval;
    // }
    //
    // /**
    // * Compare the prime traits against expected values
    // *
    // * @param traits
    // * to be verifued
    // * @param expTraits
    // * to compare againt
    // * @return true if all trait values match, else false
    // */
    // private boolean equalTraitMap(int[] traits,
    // Map<ATTRIBUTE, Integer> expTraits) {
    // MsgCtrl.auditMsgsOn(false);
    // Integer[] arrTraits = (Integer[]) expTraits.values().toArray();
    //
    // boolean retval = true;
    // for (int k = 0; k < traits.length; k++) {
    // if (traits[k] != arrTraits[k]) {
    // retval = false;
    // break;
    // }
    // }
    // return retval;
    // }

} // end of TestPeasant test class
