/**
 * TA01_CreateHero.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.civ.PersonKeys;
import chronos.pdc.character.Hero;
import civ.NewHeroCiv;
import mylib.MsgCtrl;

/**
 * Creates a Peasant Hero, and contains all the common tests that are not race or gender specific.
 * The specific tests are placed into subclasses of this one, whose {@code setUp()} methods define
 * the inputs that create the specific gender- and race-dependent Heros. This structure is called
 * the {@code configurator} pattern.
 * 
 * @author Al Cline
 * @version May 21, 2017 // original <br>
 *          June 2 2017 // revised to support refactored male Peasant Hero <br>
 *          June 9 2017 // refactored to split gender tests from others via the inheritance
 *          Configurator pattern <br>
 */
public class TA01_CreateHero
{
  // Keys into the traits (count the AP as an additional trait)
  protected final int STR = 0;
  protected final int INT = 1;
  protected final int WIS = 2;
  protected final int CON = 3;
  protected final int DEX = 4;
  protected final int CHR = 5;

  /** Convenience array for easier access to prime traits */
  protected int[] _traits;
  private final int MAX_TRAITS = 6;

  // Specific constants
  protected final int MIN_HP = 10;

  /** Interface to application on the other side of the CIV */
  static protected NewHeroCiv _nhCiv;
  /** Data to the Hero constructor */
  static protected EnumMap<PersonKeys, String> _inputMap;
  /** Map of all fields and attributes for the generated Hero */
  static protected Map<PersonKeys, String> _opMap;

  /** Hero character to be verified */
  protected Hero _myHero;


  // ============================================================
  // Prep methods
  // ============================================================

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _nhCiv = new NewHeroCiv();
    _inputMap = new EnumMap<PersonKeys, String>(PersonKeys.class);
    _opMap = new EnumMap<PersonKeys, String>(PersonKeys.class);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    // Containers get cleared, objects get nulled
    _opMap.clear();
    _inputMap.clear();
    _nhCiv = null;
  }

  @Before
  public void setUp() throws Exception
  {
    _traits = new int[MAX_TRAITS];
  }

  @After
  public void tearDown() throws Exception
  {
    // System.out.println("Class tearDown()");
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _traits = null;
  }


  // ============================================================
  // Constructor for Subclass
  // ============================================================

  /**
   * Set up subclass specific data to generate the hero. If the echoCheck within fails, no further
   * tests should be called
   */
  protected Hero createHero(String name, String gender, String hairColor, String raceName,
      String klassName)
  {
    MsgCtrl.where(this);

    _inputMap = loadInput(name, gender, hairColor, raceName, klassName);
    _myHero = _nhCiv.createHero(_inputMap);
    _opMap = _myHero.loadAttributes();
    // Echo-Check
    assertTrue(echoCheck(_inputMap, _opMap));
    // Load convenience array with the Hero's prime traits
    _traits = loadTraits(_opMap);
    return _myHero;
  }


  /**
   * Verify that initialization values are set as expected: Level, Gold Banked, Gold in Hand,
   * Hunger, Level, Load, Klass, XP
   */
  
  public void verifyGenericAttributes()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Peasant klass
    MsgCtrl.msg("   Klass = " + _opMap.get(PersonKeys.KLASSNAME));
    assertTrue(_opMap.get(PersonKeys.KLASSNAME).equals("Peasant"));

    // Description: Almost anything can go in here.
    String desc = _opMap.get(PersonKeys.DESCRIPTION);
    MsgCtrl.msg("\n\t Description: " + desc);
    assertNotNull(desc);

    // Occupation: almost anything can be in here
    String ocp = _opMap.get(PersonKeys.OCCUPATION);
    MsgCtrl.msg("\n\t Occupation: " + ocp);
    assertNotNull(ocp);

    // HUNGER
    MsgCtrl.msgln("   Hunger = " + _opMap.get(PersonKeys.HUNGER));
    assertTrue(_opMap.get(PersonKeys.HUNGER).equals("Full"));

    // LEVEL
    MsgCtrl.msgln("   Level = " + _opMap.get(PersonKeys.LEVEL));
    assertTrue(_opMap.get(PersonKeys.LEVEL).equals("0"));

    // XP
    MsgCtrl.msgln("   XP = " + _opMap.get(PersonKeys.XP));
    assertTrue(_opMap.get(PersonKeys.XP).equals("0"));

    /** STR Attributes: To Hit, Damage, Wt Allowance */
    int str = Integer.parseInt(_opMap.get(PersonKeys.STR));
    int expToHit = getMod(str);
    int expDmg = getMod(str);
    int toHit = Integer.parseInt(_opMap.get(PersonKeys.TO_HIT_MELEE));
    int dmg = Integer.parseInt(_opMap.get(PersonKeys.DAMAGE));
    MsgCtrl.msg("\n\t STR = " + str + " To Hit = " + toHit + " Damage= " + dmg);
    assertEquals(expToHit, toHit);
    assertEquals(expDmg, dmg);

    /** Weight allowance; STR * STR (in lbs) */
    int expWtAllow = _traits[STR] * _traits[STR];
    int wtAllow = Integer.parseInt(_opMap.get(PersonKeys.WT_ALLOW));
    MsgCtrl.msg(" Wt Allowance = " + wtAllow + " lb.");
    assertEquals(expWtAllow, wtAllow);

    /** INT has no attributes */
    MsgCtrl.msg("\n\t INT = " + _opMap.get(PersonKeys.INT));

    /** WIS and Magic Defense Mod (= WIS mod) */
    int wis = Integer.parseInt(_opMap.get(PersonKeys.WIS));
    int expWisMod = getMod(wis);
    int wisMod = Integer.parseInt(_opMap.get(PersonKeys.MDM));
    MsgCtrl.msg("\n\t WIS = " + wis + " Magic Defense Mod = " + wisMod);
    assertEquals(expWisMod, wisMod);

    /** CON and HP Mod (CON mod) */
    int con = Integer.parseInt(_opMap.get(PersonKeys.CON));
    int expConMod = getMod(con);
    int conMod = Integer.parseInt(_opMap.get(PersonKeys.HP_MOD));
    MsgCtrl.msg("\n\t CON = " + con + " HP Mod = " + conMod);
    assertEquals(expConMod, conMod);

    /** DEX attributes: ToHit Mod and AC Mod */
    int dex = Integer.parseInt(_opMap.get(PersonKeys.DEX));
    int expDexMod = getMod(dex);
    int toHitMod = Integer.parseInt(_opMap.get(PersonKeys.TO_HIT_MISSLE));
    int acMod = Integer.parseInt(_opMap.get(PersonKeys.AC_MOD));
    MsgCtrl.msg("\n\t DEX = " + dex + " ToHit Mod = " + toHitMod + " AC Mod = " + acMod);
    assertEquals(expDexMod, toHitMod);
    assertEquals(expDexMod, acMod);

    /** AC depends on AC Mod (DEX) */
    int expAC_Mod = getMod(_traits[DEX]); // expected value
    int expAC = MIN_HP + expAC_Mod;
    int acmod = Integer.parseInt(_opMap.get(PersonKeys.AC_MOD));
    int AC = Integer.parseInt(_opMap.get(PersonKeys.AC));
    MsgCtrl.msg("; AC = " + AC + " (" + acmod + ")");
    assertEquals(expAC_Mod, acmod);
    assertEquals(expAC, AC);

    /** AP = STR + DEX */
    int expAP = _traits[STR] + _traits[DEX];
    int AP = Integer.parseInt(_opMap.get(PersonKeys.AP));
    MsgCtrl.msg("\n\t AP = " + AP);
    assertEquals(expAP, AP);

    /** Speed, depends on height and AP */
    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    int expSpeed = getExpSpeed(height, AP);
    MsgCtrl.msg(" Speed = " + _opMap.get(PersonKeys.SPEED) + " (" + height + "\")");
    int speed = Integer.parseInt(_opMap.get(PersonKeys.SPEED));
    assertEquals(expSpeed, speed);

    /** Overbearing = AP + (1 per 25 lb weight) */
    int expOverbear = AP + Integer.parseInt(_opMap.get(PersonKeys.WEIGHT)) / 25;
    int overbear = Integer.parseInt(_opMap.get(PersonKeys.OVERBEARING));
    MsgCtrl.msg(" Overbear = " + overbear);
    assertEquals(expOverbear, overbear);

    /** Grappling = AP + StrMod */
    int expGrapple = AP + getMod(_traits[STR]);
    int grapple = Integer.parseInt(_opMap.get(PersonKeys.GRAPPLING));
    MsgCtrl.msg(" Grappling = " + grapple);
    assertEquals(expGrapple, grapple);

    /** Pummeling = AP + StrMod + DexMod */
    int expPummel = AP + getMod(_traits[STR]) + getMod(_traits[DEX]);
    int pummel = Integer.parseInt(_opMap.get(PersonKeys.PUMMELING));
    MsgCtrl.msg(" Pummeling = " + pummel);
    assertEquals(expPummel, pummel);

    /** Shield bash = 0 until Hero wields a shield */
    int expBash = 0;
    int bash = Integer.parseInt(_opMap.get(PersonKeys.SHIELD_BASH));
    MsgCtrl.msg(" Shield Bash = " + bash);
    assertEquals(expBash, bash);

    /** GOLD BANKED: Peasants get 0.0 gold banked */
    double goldBanked = Double.parseDouble(_opMap.get(PersonKeys.GOLD_BANKED));
    MsgCtrl.msg("   Gold banked = " + goldBanked);
    assertTrue(goldBanked < 0.05);

    /** GOLD IN HAND: All peasants get 10*d4 gold + d10 silver */
    double gold = Double.parseDouble(_opMap.get(PersonKeys.GOLD));
    double silver = Double.parseDouble(_opMap.get(PersonKeys.SILVER));
    double goldInHand = gold + silver / 10.0;
    MsgCtrl.msg("   Gold/silver in hand = " + gold + " gp/ " + silver + " sp = " + goldInHand);
    assertTrue(checkRangeValue(10.1, 40.9, goldInHand, 0.05));
    
    // All Peasants start with 10 HP 
     int expHP_Mod = getMod(_traits[CON]); // expected value
     int expHP = MIN_HP + expHP_Mod;
     int hpmod = Integer.parseInt(_opMap.get(PersonKeys.HP_MOD));
     int HP = Integer.parseInt(_opMap.get(PersonKeys.HP));
     int HP_Max = Integer.parseInt(_opMap.get(PersonKeys.HP_MAX));
     MsgCtrl.msg("; HP / Max HP = " + HP + "/" + HP_Max + " (" + hpmod + ")");
     assertEquals(expHP_Mod, hpmod);
     assertEquals(expHP, HP);
     assertEquals(expHP, HP_Max);

  }

  
  /** Verify inventory attributes, but for now, only its load */
  public void testInventoryAttributes()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // LOAD: weight carried depends on standard basic inventory items
    double expLoad = 23.375; // lbs. weight of starting inventory
    // double load = _myHero.getInventory().calcInventoryWeight() / 16.0; // in lb
    double load = Double.parseDouble(_opMap.get(PersonKeys.LOAD)) / 16.0; // in lb
    MsgCtrl.msgln("   Load = " + load + " lb");
    assertEquals(expLoad, load, 0.1);
  }


  // ============================================================
  // Protected Methods
  // ============================================================

  /** Verify that an double value falls with a certain boundary */
  protected boolean checkRangeValue(double low, double high, double value, double tolerance)
  {
    double tLow = low - tolerance;
    double tHigh = high + tolerance;
    return ((tLow <= value) && (value <= tHigh));
  }


  /** Verify that an int value falls with a certain boundary */
  protected boolean checkRangeValue(int low, int high, int value)
  {
    return ((low <= value) && (value <= high));
  }


  /** All prime traits have the same mod */
  protected int getMod(int trait)
  {
    return (trait - 10) / 2;
  }

  // ============================================================
  // Private helper Methods
  // ============================================================

  /**
   * Convert the hero's traits to an int[] field. A convenience method for easier access to the
   * Hero's prime traits.
   * 
   * @param traits map of Hero's labels to trait values
   * @return int array of all six traits
   */
  protected int[] loadTraits(Map<PersonKeys, String> opMap)
  {
    _traits[STR] = Integer.parseInt(_opMap.get(PersonKeys.STR));
    _traits[INT] = Integer.parseInt(_opMap.get(PersonKeys.INT));
    _traits[WIS] = Integer.parseInt(_opMap.get(PersonKeys.WIS));
    _traits[CON] = Integer.parseInt(_opMap.get(PersonKeys.CON));
    _traits[DEX] = Integer.parseInt(_opMap.get(PersonKeys.DEX));
    _traits[CHR] = Integer.parseInt(_opMap.get(PersonKeys.CHR));
    return _traits;
  }

  /**
   * Convert the hero's traits to a set of labeled strings, e.g., STR = 12
   * 
   * @param traits map of Hero's labels to trait values
   * @return string of all six traits
   */
  protected String loadTraitStrings(Map<PersonKeys, String> traits)
  {
    String[] trNames = {"STR", "INT", "WIS", "CON", "DEX", "CHR", "AP"};
    String[] trValues = {traits.get(PersonKeys.STR), traits.get(PersonKeys.INT),
        traits.get(PersonKeys.WIS), traits.get(PersonKeys.CON), traits.get(PersonKeys.DEX),
        traits.get(PersonKeys.CHR), traits.get(PersonKeys.AP)};

    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < trNames.length; k++) {
      sb.append(trNames[k]);
      sb.append("=");
      sb.append(trValues[k]);
      if (k != trNames.length - 1) {
        sb.append("; ");
      }
    }
    return sb.toString();
  }

  /** Verify that the HP and HP Mods are correct */
  protected void verifyHPMods()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /** HP and Max HP depends on Klass */
    int con = Integer.parseInt(_opMap.get(PersonKeys.CON));
    int expHP_Mod = getMod(con); // expected value
    int expHP = MIN_HP + expHP_Mod;
    int hpmod = Integer.parseInt(_opMap.get(PersonKeys.HP_MOD));
    int HP = Integer.parseInt(_opMap.get(PersonKeys.HP));
    int HP_Max = Integer.parseInt(_opMap.get(PersonKeys.HP_MAX));
    MsgCtrl.msg("; HP / Max HP = " + HP + "/" + HP_Max + " (" + hpmod + ")");
    assertEquals(expHP_Mod, hpmod);
    assertEquals(expHP, HP);
    assertEquals(expHP, HP_Max);
  }

  /** Compare the hero input data against the output data */
  // private boolean echoCheck(String[] indata, Map<PersonKeys, String> outdata)
  private boolean echoCheck(EnumMap<PersonKeys, String> inData, Map<PersonKeys, String> outdata)
  {
    MsgCtrl.msgln("\tHero " + _opMap.get(PersonKeys.NAME) + ": " + _opMap.get(PersonKeys.RACENAME)
        + " " + _opMap.get(PersonKeys.GENDER) + " " + _opMap.get(PersonKeys.KLASSNAME)
        + " with " + _opMap.get(PersonKeys.HAIR_COLOR) + " hair.");
    MsgCtrl.msgln("\t" + _opMap.get(PersonKeys.DESCRIPTION));
    boolean bName = inData.get(PersonKeys.NAME).equals(outdata.get(PersonKeys.NAME));
    boolean bGender = inData.get(PersonKeys.GENDER).equals(outdata.get(PersonKeys.GENDER));
    boolean bHair = inData.get(PersonKeys.HAIR_COLOR).equals(outdata.get(PersonKeys.HAIR_COLOR));
    boolean bRace = inData.get(PersonKeys.RACENAME).equals(outdata.get(PersonKeys.RACENAME));
    boolean bKlass = inData.get(PersonKeys.KLASSNAME).equals(outdata.get(PersonKeys.KLASSNAME));

    return bName && bGender && bHair && bRace && bKlass;
  }


  /** Get the speed in ft/round */
  private int getExpSpeed(int ht, int ap)
  {
    int BLOCK_ADJ = 5;
    int speed = -1;
    int slow = (ht < 48) ? -BLOCK_ADJ : 0;
    int fast = (ht > 74) ? BLOCK_ADJ : 0;
    speed = ((1 <= ap) && (ap <= 15)) ? 10 : speed;
    speed = ((16 <= ap) && (ap <= 23)) ? 15 : speed;
    speed = ((24 <= ap) && (ap <= 32)) ? 20 : speed;
    speed = (33 <= ap) ? 25 : speed;
    speed = speed + slow + fast;
    return speed;
  }


  /**
   * Load the input map with new Hero data
   * 
   * @param indataame, gender, hair color, race,
   * @return the input map but is also a class field
   */
  private EnumMap<PersonKeys, String> loadInput(String name, String gender,
      String hairColor, String raceName, String klassName)
  {
    _inputMap.put(PersonKeys.NAME, name);
    _inputMap.put(PersonKeys.GENDER, gender);
    _inputMap.put(PersonKeys.HAIR_COLOR, hairColor);
    _inputMap.put(PersonKeys.RACENAME, raceName);
    _inputMap.put(PersonKeys.KLASSNAME, klassName);
    return _inputMap;
  }



  // ============================================================
  // BEGIN VERIFICATION
  // ============================================================
  
  private void stockChecks()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
  
  
    /** HP and Max HP depends on Klass */
    // int expHP_Mod = getMod(trait[CON]); // expected value
    // int expHP = MIN_HP + expHP_Mod;
    // int hpmod = Integer.parseInt(_opMap.get(PersonKeys.HP_MOD));
    // int HP = Integer.parseInt(_opMap.get(PersonKeys.HP));
    // int HP_Max = Integer.parseInt(_opMap.get(PersonKeys.HP_MAX));
    // MsgCtrl.msg("; HP / Max HP = " + HP + "/" + HP_Max + " (" + hpmod + ")");
    // assertEquals(expHP_Mod, hpmod);
    // assertEquals(expHP, HP);
    // assertEquals(expHP, HP_Max);
  
    /** Languages and max languages knowable depend on INT (and Race) */
    // Max new learnable languages
    // int intel = Integer.parseInt(_opMap.get(PersonKeys.INT));
    // int expNbrLangs = intel / 2 - 4;
    // int maxLangs = Integer.parseInt(_opMap.get(PersonKeys.MAX_LANGS));
    // MsgCtrl.msg("\n\tNew Languages Knowable = " + maxLangs);
    // assertEquals(expNbrLangs, maxLangs);
    //
    // Languages known
    // String expLang = "Common";
    // String lang = _opMap.get(PersonKeys.LANGUAGES);
    // MsgCtrl.msg(" Languages known: " + lang);
    // assertEquals(expLang, lang);
    //
  
  }

  // ============================================================
  // Protected Methods
  // ============================================================

  /** Confirm that all PersonsKeys have a value */
  private void verifyPersonKeys()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP for male human peasant
    _inputMap = loadInput("Falsoon", "Male", "brown", "Human", "Peasant");

    // Some keys do not apply to Peasants, so load an exception list
    PersonKeys[] pk = {PersonKeys.CURRENT_MSP, PersonKeys.MAX_MSP, PersonKeys.MSP_PER_LEVEL,
        PersonKeys.SPELLS_KNOWN, PersonKeys.CURRENT_CSP, PersonKeys.MAX_CSP,
        PersonKeys.CSP_PER_LEVEL, PersonKeys.TURN_UNDEAD};

    ArrayList<PersonKeys> ignoreKeys = new ArrayList<>();
    for (int k = 0; k < pk.length; k++) {
      ignoreKeys.add(pk[k]);
    }

    for (PersonKeys key : PersonKeys.values()) {
      String s = _opMap.get(key);
      if (s == null) {
        if (!ignoreKeys.contains(key)) {
          MsgCtrl.msgln("\tPersonKey value " + key + " = null");
          assertNotNull(s);
        }
      }
    }
  }


} // end of TA01_CreateHero class
