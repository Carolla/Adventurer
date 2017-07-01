
package chronos.pdc.character;

import static chronos.pdc.character.TraitList.PrimeTraits.CHR;
import static chronos.pdc.character.TraitList.PrimeTraits.CON;
import static chronos.pdc.character.TraitList.PrimeTraits.DEX;
import static chronos.pdc.character.TraitList.PrimeTraits.INT;
import static chronos.pdc.character.TraitList.PrimeTraits.STR;
import static chronos.pdc.character.TraitList.PrimeTraits.WIS;

import java.util.Map;
import java.util.TreeMap;

import chronos.civ.PersonKeys;
import mylib.pdc.MetaDie;

/**
 * Handles all calculations around the Hero's prime traits
 * 
 * @author Al Cline
 * @version May 23, 2017 // revised from original (unknown date) <br>
 */
public class TraitList
{
  /** Indices into the Hero's prime traits */
  public static enum PrimeTraits {
    STR, INT, WIS, CON, DEX, CHR
  };

  /** Max number of traits */
  static public final int NUMBER_OF_TRAITS = 6;

  /** Indices into the non-lethal stats */
  public static enum NonLethal {
    OVERBEAR, GRAPPLE, PUMMEL, BASH
  };

  // Non-lethal combat stats
  // TODO: Remove this and use the NonLethal enum above
  private int OVERBEAR = 0;
  private int GRAPPLE = 1;
  private int PUMMEL = 2;
  private int BASH = 3;
  private int[] _apMods;

  private final String ILLITERATE = "ILLITERATE: Cannot read or write";
  private final String LITERATE = "LITERATE: Can read and write";
  private final String PART_LITERATE = "PARTIALLY LITERATE: Can read but cannot write";

  private int _AP;
  private int _speed;

  // No trait, raw of modified, may be less than MIN_TRAIT at start
  // Make this static to be available by other classes
//  static public final int MIN_TRAIT = 8;
//  static public final int MAX_TRAIT = 18;
  
  // Height indicators for speed mod
  private final int HT_TALL = 74;
  private final int HT_SHORT = 48;
  private final int SPEED_INC = 5;

  private final Map<Integer, Integer> _traits = new TreeMap<Integer, Integer>();
  private static final MetaDie _md = new MetaDie();
  public static final int DEFTRAIT_VAL = 10;
  public static final int[] DEFAULT_TRAITS =
      {DEFTRAIT_VAL, DEFTRAIT_VAL, DEFTRAIT_VAL, DEFTRAIT_VAL, DEFTRAIT_VAL, DEFTRAIT_VAL};



  /** Roll the Hero's traits, constrained to [8, 18] before adjustments */
  public TraitList()
  {
    this(_md.rollTraits());
  }

  /**
   * Remap the int traits randomly rolled, then calc AP
   * 
   * @param traits for Hero
   */
  public TraitList(int[] traits)
  {
    for (PrimeTraits t : PrimeTraits.values()) {
      _traits.put(t.ordinal(), traits[t.ordinal()]);
    }
  }


  public int adjust(PrimeTraits t, int i)
  {
    if (_traits.containsKey(t.ordinal())) {
      return _traits.put(t.ordinal(), _traits.get(t.ordinal()) + i);
    }
    return 0;
  }


  /**
   * Calculate the non-lethal combat stats: overbearing, grappling, pummeling, and shield bash.
   * 
   * @param weight affects overbearing
   * @return all four mods
   */
  public int[] calcAPMods(int weight)
  {
    _apMods = new int[4];
    _AP = getTrait(STR) + getTrait(DEX);

    int damageMod = calcMod(STR);
    _apMods[OVERBEAR] = _AP + (weight / 25);
    _apMods[PUMMEL] = _AP + damageMod + calcMod(PrimeTraits.DEX);
    _apMods[GRAPPLE] = _AP + damageMod;
    _apMods[BASH] = 0;
    return _apMods;
  }


  /**
   * All mods to prime traits are calculated the same: (Trait - 10) / 2
   * 
   * @param key one of the six prime traits
   * @return the mod for that trait
   */
  public int calcMod(PrimeTraits key)
  {
    return (getTrait(key) - 10) / 2;
  }


  /**
   * Lookup the speed of the character, factoring in height. Tall people run faster; shorter people
   * run slower; average people run 20. Speed is adjusted by a block rate of SPEED_INC = 5
   */
  public int calcSpeed(int height)
  {
    // Calc base speed
    _speed = 0;
    if ((1 <= _AP) && (_AP <= 15)) {
      _speed = 10;
    } else if ((16 <= _AP) && (_AP <= 23)) {
      _speed = 15;
    } else if ((24 <= _AP) && (_AP <= 32)) {
      _speed = 20;
    } else {
      _speed = 25;
    }
    // Adjust speed by height
    if (height > HT_TALL) {
      _speed += SPEED_INC;
    } else if (height < HT_SHORT) {
      _speed -= SPEED_INC;
    }
    return _speed;

  }

//  /**
//   * Ensure that no trait, raw or modified, is below MI_TRAIT. Modified traits be exceed MAX_TRAIT
//   * 
//   * @return nothing: internal fields modified to be within proper range
//   */
//  public void ensureTraitConstraints()
//  {
//    for (PrimeTraits t : PrimeTraits.values()) {
//      if (getTrait(t) < MIN_TRAIT) {
//        _traits.put(t.ordinal(), MIN_TRAIT);
//      }
//    }
//  }


  public PrimeTraits findLargestTrait()
  {
    // Walk the list and find the largest trait
    int largest = -1;
    PrimeTraits largestTrait = PrimeTraits.STR;

    for (PrimeTraits trait : PrimeTraits.values()) {
      int traitVal = getTrait(trait);
      if (largest < traitVal) {
        largest = traitVal;
        largestTrait = trait;
      }
    }
    return largestTrait;
  }

  // public int getACMod()
  // {
  //// return getToHitMissleBonus();
  // return calcMod(getTrait(PrimeTraits.DEX));
  // }

  // public int getHpMod()
  // {
  // return findInRange(CON);
  // }

  public int getMagicDefenseMod()
  {
    return findInRange(WIS);
  }

  // public int getStrDmgBonus()
  // {
  //// int ndx = getTrait(STR) - 3; // read from the table 3 places to the left
  //// return dmgTbl[ndx];
  // }

  // public int getToHitMissleBonus()
  // {
  // return findInRange(DEX);
  // }

  // public int getToHitStr()
  // {
  // return findInRange(STR);
  // }

  public int getTrait(PrimeTraits trait)
  {
    return _traits.get(trait.ordinal());
  }

  // A person can carry their STR value squared (STR * STR), measured in lbs.
  public int getWeightAllowance()
  {
    return getTrait(STR) * getTrait(STR);
  }

  /** Get the literacy based on intelligence; Spell casters are always literate */
  public String getLiteracy()
  {
    int intel = getTrait(INT);

    if (intel > 11) {
      return LITERATE;
    } else if (intel == 11) {
      return PART_LITERATE;
    } else {
      return ILLITERATE;
    }
  }

  public int getMaxLangs()
  {
    return getTrait(INT) / 2 - 4;
  }


  public boolean isLargestTrait(PrimeTraits trait)
  {
    return getTrait(findLargestTrait()) == getTrait(trait);
  }


  /**
   * Put the prime traits and their associated modifiers into the map
   * 
   * @param map all Hero's attributes
   */
  public void loadTraitKeys(Map<PersonKeys, String> map)
  {
    // Prime traits
    map.put(PersonKeys.STR, "" + getTrait(STR));
    map.put(PersonKeys.DEX, "" + getTrait(DEX));
    map.put(PersonKeys.CON, "" + getTrait(CON));
    map.put(PersonKeys.INT, "" + getTrait(INT));
    map.put(PersonKeys.WIS, "" + getTrait(WIS));
    map.put(PersonKeys.CHR, "" + getTrait(CHR));

    // STR
    int strMod = calcMod(PrimeTraits.STR);
    map.put(PersonKeys.DAMAGE, addPolarity(strMod));
    map.put(PersonKeys.TO_HIT_MELEE, addPolarity(strMod));
    map.put(PersonKeys.WT_ALLOW, "" + getWeightAllowance());

    // INT
    map.put(PersonKeys.MAX_LANGS, "" + getMaxLangs());
    map.put(PersonKeys.LITERACY, getLiteracy());

    // WIS
    int wisMod = calcMod(PrimeTraits.WIS);
    map.put(PersonKeys.MDM, "" + addPolarity(wisMod));

    // CON
    int conMod = calcMod(PrimeTraits.CON);
    map.put(PersonKeys.HP_MOD, addPolarity(conMod));

    // DEX
    int dexMod = calcMod(PrimeTraits.DEX);
    map.put(PersonKeys.TO_HIT_MISSLE, addPolarity(dexMod));
    map.put(PersonKeys.AC_MOD, addPolarity(dexMod));

    // AP mods
    map.put(PersonKeys.SPEED, "" + _speed);
    map.put(PersonKeys.AP, "" + _AP);
    map.put(PersonKeys.OVERBEARING, "" + _apMods[OVERBEAR]);
    map.put(PersonKeys.PUMMELING, "" + _apMods[PUMMEL]);
    map.put(PersonKeys.GRAPPLING, "" + _apMods[GRAPPLE]);
    map.put(PersonKeys.SHIELD_BASH, "" + _apMods[BASH]);
  }


  private String addPolarity(int mod) 
  {
    String modStr = (mod > 0) ? "+" + mod : Integer.toString(mod);
    return modStr;
  }
  
  
  public void recalcSpeed(int height)
  {
    if (height < 49) {
      _speed -= 1;
    }
    if (height > 73) {
      _speed += 1;
    }
  }

  /**
   * Swap the largest raw trait for the prime trait with the specific klass
   * 
   * @param primeTrait the trait that should be the largest trait
   */
  public void swapPrime(PrimeTraits primeTrait)
  {
    int oldPrime = getTrait(primeTrait);
    PrimeTraits largestTrait = findLargestTrait();
    int largest = getTrait(largestTrait);
    _traits.put(primeTrait.ordinal(), largest);
    _traits.put(largestTrait.ordinal(), oldPrime);
  }

  /**
   * Dump the trait map into an int[]
   *
   * @return the traits in PrimeTrait order
   */
  public int[] toArray()
  {
    int[] trAry = new int[NUMBER_OF_TRAITS];
    trAry[0] = getTrait(PrimeTraits.STR);
    trAry[1] = getTrait(PrimeTraits.INT);
    trAry[2] = getTrait(PrimeTraits.WIS);
    trAry[3] = getTrait(PrimeTraits.CON);
    trAry[4] = getTrait(PrimeTraits.DEX);
    trAry[5] = getTrait(PrimeTraits.CHR);
    return trAry;
  }

  
  @Override
  public String toString()
  {
    return "STR: " + getTrait(PrimeTraits.STR) + ", " +
        "INT: " + getTrait(PrimeTraits.INT) + ", " +
        "WIS: " + getTrait(PrimeTraits.WIS) + ", " +
        "CON: " + getTrait(PrimeTraits.CON) + ", " +
        "DEX: " + getTrait(PrimeTraits.DEX) + ", " +
        "CHR: " + getTrait(PrimeTraits.CHR);
  }

  // Find a number in one of three ranges: low, medium, high
  private int findInRange(PrimeTraits trait)
  {
    int value = getTrait(trait);
    final int HI_GATE = 14;
    final int LO_GATE = 9;
    if (value > HI_GATE)
      return (value - HI_GATE);
    if (value < LO_GATE)
      return (value - LO_GATE);
    return 0;
  }
}
