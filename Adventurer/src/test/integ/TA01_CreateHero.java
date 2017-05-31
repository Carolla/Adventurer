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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.civ.PersonKeys;
import chronos.pdc.Item;
import chronos.pdc.character.Hero;
import chronos.pdc.character.Hero.HeroInput;
import chronos.pdc.character.Inventory;
import chronos.pdc.character.TraitList;
import civ.NewHeroCiv;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version May 21, 2017 // original <br>
 */
public class TA01_CreateHero
{
  /** Hero input: name, gender, hair color, race, klass = peasant (only Peasants can be created) */
  private String[] _heroData = {"Falsoon", "Male", "brown", "Human", "Peasant"};

  // Keys into the traits (count the AP as an additional trait)
  private final int MAX_TRAITS = 6;
  private final int STR = 0;
  private final int INT = 1;
  private final int WIS = 2;
  private final int CON = 3;
  private final int DEX = 4;
  private final int CHR = 5;

  // Specific constants
  private final int MIN_HP = 10;
  private Hero _myHero;
  private Map<PersonKeys, String> _opMap;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    // Setup: Define simulated data from the input panel to create hero
    NewHeroCiv nhCiv = new NewHeroCiv();
    EnumMap<HeroInput, String> inputMap = new EnumMap<HeroInput, String>(HeroInput.class);
    // TODO Re-use PersonKeys instead of requiring a new HeroInput map
    inputMap.put(HeroInput.NAME, _heroData[0]);
    inputMap.put(HeroInput.GENDER, _heroData[1]);
    inputMap.put(HeroInput.HAIR, _heroData[2]);
    inputMap.put(HeroInput.RACE, _heroData[3]);
    inputMap.put(HeroInput.KLASS, _heroData[4]);

    // Create a Peasant
    // TODO: Use TestProxy with Civs to ensure same interface to Civ and GUI
    _myHero = nhCiv.createHero(inputMap);
    _opMap = _myHero.loadAttributes();

  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /**
   * @NORMAL Create a random human peasant Hero
   */
  @Test
  public void testCreateHero()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Setup: The Hero is created in the setUp() method, then verified here

    // VERIFY echo-Check
    MsgCtrl.msgln("\tHero " + _opMap.get(PersonKeys.NAME) + ": " + _opMap.get(PersonKeys.RACENAME)
        + " " + _opMap.get(PersonKeys.GENDER) + " " + _opMap.get(PersonKeys.KLASSNAME)
        + " with " + _opMap.get(PersonKeys.HAIR_COLOR) + " hair.");
    MsgCtrl.msgln("\t" + _opMap.get(PersonKeys.DESCRIPTION));
    assertEquals(_heroData[0], _opMap.get(PersonKeys.NAME));
    assertEquals(_heroData[1], _opMap.get(PersonKeys.GENDER));
    assertEquals(_heroData[2], _opMap.get(PersonKeys.HAIR_COLOR));
    assertEquals(_heroData[3], _opMap.get(PersonKeys.RACENAME));
    assertEquals(_heroData[4], _opMap.get(PersonKeys.KLASSNAME));

    // All core traits must be within [8, 18]
    String traitStrings = loadTraitStrings(_opMap);
    MsgCtrl.msgln("\tTraits: \t" + traitStrings);
    int[] trait = loadTraits(_opMap);
    for (int k = 0; k < trait.length - 1; k++) {
      assertTrue((trait[k] >= TraitList.MIN_TRAIT) && (trait[k] <= 18));
    }

    /** TAB 1 OF THE HERO OUTPANEL: HERO ATTRIBUTES */
    /** LINE 1: Level, HP/ Max HP, AC, Hunger */
    // Level
    MsgCtrl.msg("\tLevel = " + _opMap.get(PersonKeys.LEVEL));
    assertEquals("0", _opMap.get(PersonKeys.LEVEL));

    // HP and Max HP
    int expHP_Mod = getMod(trait[CON]); // expected value
    int expHP = MIN_HP + expHP_Mod;
    int hpmod = Integer.parseInt(_opMap.get(PersonKeys.HP_MOD));
    int HP = Integer.parseInt(_opMap.get(PersonKeys.HP));
    int HP_Max = Integer.parseInt(_opMap.get(PersonKeys.HP_MAX));
    MsgCtrl.msg(";   HP / Max HP = " + HP + "/" + HP_Max + " (" + hpmod + ")");
    assertEquals(expHP_Mod, hpmod);
    assertEquals(expHP, HP);
    assertEquals(expHP, HP_Max);

    // AC
    int expAC_Mod = getMod(trait[DEX]); // expected value
    int expAC = MIN_HP + expAC_Mod;
    int acmod = Integer.parseInt(_opMap.get(PersonKeys.AC_MOD));
    int AC = Integer.parseInt(_opMap.get(PersonKeys.AC));
    MsgCtrl.msg(";   AC = " + AC + " (" + acmod + ")");
    assertEquals(expAC_Mod, acmod);
    assertEquals(expAC, AC);

    // Hunger
    String hunger = _opMap.get(PersonKeys.HUNGER);
    MsgCtrl.msg("   Hunger = " + hunger);
    assertEquals("Full", hunger);

    /** LINE 2: XP, Speed, Gold Banked, Gold in Hand */
    // XP
    MsgCtrl.msg("\n\tXP = " + _opMap.get(PersonKeys.XP));
    assertEquals("0", _opMap.get(PersonKeys.XP));

    // Speed, depends on height and AP
    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    int expSpeed = getExpSpeed(height, trait[STR] + trait[DEX]);
    MsgCtrl.msg("   Speed = " + _opMap.get(PersonKeys.SPEED) + " (" + height + "\")");
    int speed = Integer.parseInt(_opMap.get(PersonKeys.SPEED));
    assertEquals(expSpeed, speed);

    // Gold.silver in hand; gold banked
    // Peasants get 10.1-40.9 gold
    double expGoldMin = 10.1;
    double expGoldMax = 40.9;
    double goldHand = Double.parseDouble(_opMap.get(PersonKeys.GOLD));
    double goldBanked = Double.parseDouble(_opMap.get(PersonKeys.GOLD_BANKED));
    MsgCtrl.msg("   Gold in hand = " + goldHand);
    MsgCtrl.msg("   Gold banked = " + goldBanked);
    boolean goldRange = ((expGoldMin <= goldHand) && (goldHand <= expGoldMax));
    assertTrue(goldRange);
    assertEquals(0.0, Double.parseDouble(_opMap.get(PersonKeys.GOLD_BANKED)), 0.05);

    /** LINE 3: Description */
    // Almost anything can go in here.

    /** LINE 4: STR, To Hit, Damage, Wt Allowance, Load carried */
    // STR, To Hit, Damage
    int str = Integer.parseInt(_opMap.get(PersonKeys.STR));
    int expToHit = getMod(str);
    int expDmg = getMod(str);
    int toHit = Integer.parseInt(_opMap.get(PersonKeys.TO_HIT_MELEE));
    int dmg = Integer.parseInt(_opMap.get(PersonKeys.DAMAGE));
    MsgCtrl.msg("\n\t   STR = " + str + "   To Hit = " + toHit + "   Damage= " + dmg);
    assertEquals(expToHit, toHit);
    assertEquals(expDmg, dmg);

    // Weight allowance; STR * STR (in lbs)
    int expWtAllow = trait[STR] * trait[STR];
    int wtAllow = Integer.parseInt(_opMap.get(PersonKeys.WT_ALLOW));
    MsgCtrl.msg("   Wt Allowance = " + wtAllow + " lb.");
    assertEquals(expWtAllow, wtAllow);

    // Load: weight carried
    double expLoad = 23.375; // lbs. weight of starting inventory
    Inventory pack = _myHero.getInventory();
    double load = pack.calcInventoryWeight(); // in oz
    MsgCtrl.msg("\t   Load = " + load + " oz = " + load / 16.0 + " lb");
    assertEquals(expLoad, load / 16.0, .05);

    /** LINE 5: INT trait only */
    MsgCtrl.msg("\n\t   INT = " + _opMap.get(PersonKeys.INT));

    /** LINE 6: WIS and Magic Attack Mod (= WIS mod) */
    int wis = Integer.parseInt(_opMap.get(PersonKeys.WIS));
    int expWisMod = getMod(wis);
    int wisMod = Integer.parseInt(_opMap.get(PersonKeys.MDM));
    MsgCtrl.msg("\n\t   WIS = " + wis + "   Magic Defense Mod = " + wisMod); 
    assertEquals(expWisMod, wisMod);

    /** LINE 7: CON and HP Mod (= CON mod) */
    int con = Integer.parseInt(_opMap.get(PersonKeys.CON));
    int expConMod = getMod(con);
    int conMod = Integer.parseInt(_opMap.get(PersonKeys.HP_MOD));
    MsgCtrl.msg("\n\t   CON = " + con + "   HP Mod = " + conMod); 
    assertEquals(expConMod, conMod);

    /** LINE 8: DEX and ToHit Mod and AC Mod */
    int dex = Integer.parseInt(_opMap.get(PersonKeys.DEX));
    int expDexMod = getMod(dex);
    int toHitMod = Integer.parseInt(_opMap.get(PersonKeys.TO_HIT_MISSLE));
    int acMod = Integer.parseInt(_opMap.get(PersonKeys.AC_MOD));
    MsgCtrl.msg("\n\t   DEX = " + dex + "   ToHit Mod = " + toHitMod + "   AC Mod = " + acMod); 
    assertEquals(expDexMod, toHitMod);
    assertEquals(expDexMod, acMod);

    /** LINE 9: CHR, Weight, and Height */
    // CHR
    int chr = Integer.parseInt(_opMap.get(PersonKeys.CHR));
    // Weight: human male avg = 180, range = 130 - 230 [130 + (2d6-2 * 10) lb]
    int expWtLow = 130;
    int expWtHigh = 230;
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    MsgCtrl.msg("\n\t   CHR = " + chr + "   Weight = " + weight + " lbs."); 
    assertTrue((expWtLow <= weight) && (weight <= expWtHigh));

    // Height: human male avg = 69"; range = 60 - 78 [60 + (2d10-2) in.]
    int expHtLow = 60;
    int expHtHigh = 78;
    // height used in speed calc
    height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    MsgCtrl.msg("   Height = " + height + " in."); 
    assertTrue((expHtLow <= height) && (height <= expHtHigh));

    /** LINE 10: AP, Mods for Overbearing, Grappling, Pummeling, and Shield Bash */
    // AP = STR + DEX
    int expAP = str + dex;
    int AP = Integer.parseInt(_opMap.get(PersonKeys.AP));
    MsgCtrl.msg("\n\t    AP = " + AP); 
    assertEquals(expAP, AP);
    
    // Overbearing = AP + (1 per 25 lb weight)
    int expOverbear = AP + weight/25;
    int overbear = Integer.parseInt(_opMap.get(PersonKeys.OVERBEARING));
    MsgCtrl.msg("   Overbear = " + overbear); 
    assertEquals(expOverbear, overbear);
    
    // Grappling = AP + StrMod
    int expGrapple = AP + getMod(str);
    int grapple = Integer.parseInt(_opMap.get(PersonKeys.GRAPPLING));
    MsgCtrl.msg("   Grappling = " + grapple); 
    assertEquals(expGrapple, grapple);

    // Pummeling = AP + StrMod + DexMod
    int expPummel = AP + getMod(str) + getMod(dex);
    int pummel = Integer.parseInt(_opMap.get(PersonKeys.PUMMELING));
    MsgCtrl.msg("   Pummeling = " + pummel); 
    assertEquals(expPummel, pummel);

    // Sheild bash = 0 until Hero wields a shield
    int expBash = 0;
    int bash = Integer.parseInt(_opMap.get(PersonKeys.SHIELD_BASH));
    MsgCtrl.msg("   Shield Bash = " + bash); 
    assertEquals(expBash, bash);

    /** LINE 11: Languages and max languages knowable */
    // Max new learnable languages
    int intel = Integer.parseInt(_opMap.get(PersonKeys.INT));
    int expNbrLangs = intel/2 - 4;
    int maxLangs = Integer.parseInt(_opMap.get(PersonKeys.MAX_LANGS));
    MsgCtrl.msg("\n\tNew Languages Knowable = " + maxLangs); 
    assertEquals(expNbrLangs, maxLangs);
    
    // Languages known
    String expLang = "Common"; 
    String lang = _opMap.get(PersonKeys.LANGUAGES);
    MsgCtrl.msg("   Languages known: " + lang); 
    assertEquals(expLang, lang);
    
    /** LINE 12: Occupation */
    String ocp = _opMap.get(PersonKeys.OCCUPATION);
    MsgCtrl.msg("\n\t   Occupation: " + ocp); 
    assertNotNull(ocp);
    
  }


  // ============================================================
  // Private Helper Methods
  // ============================================================

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


  /** All prime traits have the same mod */
  private int getMod(int trait)
  {
    return (trait - 10) / 2;
  }

  
  /**
   * Convert the hero's traits to int[]
   * 
   * @param traits map of Hero's labels to trait values
   * @return int array of all six traits
   */
  private int[] loadTraits(Map<PersonKeys, String> traits)
  {
    int[] trValue = new int[MAX_TRAITS];
    trValue[STR] = Integer.parseInt(traits.get(PersonKeys.STR));
    trValue[INT] = Integer.parseInt(traits.get(PersonKeys.INT));
    trValue[WIS] = Integer.parseInt(traits.get(PersonKeys.WIS));
    trValue[CON] = Integer.parseInt(traits.get(PersonKeys.CON));
    trValue[DEX] = Integer.parseInt(traits.get(PersonKeys.DEX));
    trValue[CHR] = Integer.parseInt(traits.get(PersonKeys.CHR));
    return trValue;
  }


  /**
   * Convert the hero's traits to a set of labeled strings, e.g., STR = 12
   * 
   * @param traits map of Hero's labels to trait values
   * @return string of all six traits
   */
  private String loadTraitStrings(Map<PersonKeys, String> traits)
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

  /** Print the Hero's inventory */
  private void printInventory(Inventory inv)
  {
    List<Item> invList = inv.getAll();
    MsgCtrl.msgln("\n");
    for (Item item : invList) {
      MsgCtrl.msgln("\t\t\t" + item.toString() + "\t\t\t" + item.getWeight() + " oz.");
    }
  }



} // end of TA01_CreateHero class
