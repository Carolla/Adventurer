
package chronos.pdc.character;

import static chronos.pdc.character.TraitList.PrimeTraits.CHR;
import static chronos.pdc.character.TraitList.PrimeTraits.CON;
import static chronos.pdc.character.TraitList.PrimeTraits.DEX;
import static chronos.pdc.character.TraitList.PrimeTraits.INT;
import static chronos.pdc.character.TraitList.PrimeTraits.STR;
import static chronos.pdc.character.TraitList.PrimeTraits.WIS;

import java.util.Map;
import java.util.TreeMap;

import mylib.pdc.MetaDie;
import chronos.civ.PersonKeys;

public class TraitList
{
  /** Indices into the Hero's prime traits */
  public static enum PrimeTraits {
    STR, INT, WIS, DEX, CON, CHR
  };

  // STR values 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21
  final int[] toHitTbl = {-3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3};
  final int[] dmgTbl = {-3, -3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5};
  final int[] wtTbl = {80, 120, 160, 200, 280, 360, 440, 520, 600, 700, 800, 900, 1000, 1200, 1500,
      1800, 2100, 2300, 2600};

  final String ILLITERATE = "ILLITERATE: Cannot read or write";
  final String LITERATE = "LITERATE: Can read and write";
  final String PART_LITERATE = "PARTIALLY LITERATE: Can read but cannot write";


  // Non-lethal combat stats
  int OVERBEAR = 0;
  int GRAPPLE = 1;
  int PUMMEL = 2;
  int BASH = 3;
  int[] _apMods;

  private int _AP;
  int _speed;

  private final Map<Integer, Integer> _traits = new TreeMap<Integer, Integer>();
  private static final MetaDie _md = new MetaDie();
  public static final int DEFTRAIT_VAL = 10;
  public static final int[] DEFAULT_TRAITS = {DEFTRAIT_VAL,DEFTRAIT_VAL,DEFTRAIT_VAL,DEFTRAIT_VAL,DEFTRAIT_VAL,DEFTRAIT_VAL };

  public TraitList()
  {
    this(_md.rollTraits());
  }

  public TraitList(int[] traits)
  {
    for (PrimeTraits t : PrimeTraits.values()) {
      _traits.put(t.ordinal(), traits[t.ordinal()]);
    }

    _AP = getTrait(STR) + getTrait(DEX);
    _apMods = calcAPMods(0, getTrait(STR));
    _speed = calcSpeed();
  }

  public int getTrait(PrimeTraits trait)
  {
    return _traits.get(trait.ordinal());
  }

  public int adjust(PrimeTraits t, int i)
  {
    if (_traits.containsKey(t.ordinal())) {
      return _traits.put(t.ordinal(), _traits.get(t.ordinal()) + i);
    }
    return 0;
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
    int largest = getTrait(largestTrait );
    _traits.put(primeTrait.ordinal(), largest);
    _traits.put(largestTrait.ordinal(), oldPrime);
  }

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

  public int getStrDmgBonus()
  {
    int ndx = getTrait(STR) - 3; // read from the table 3 places to the left
    return dmgTbl[ndx];
  }

  public int getWeightAllowance()
  {
    int ndx = getTrait(STR) - 3; // read from the table 3 places to the left
    return wtTbl[ndx];
  }

  /** Calculate the non-lethal combat stats: overbearing, grappling, pummeling, and shield bash */
  public int[] calcAPMods(int weight, int damage)
  {
    int[] mods = new int[4];
    mods[OVERBEAR] = _AP + (weight / 25);
    mods[GRAPPLE] = _AP + damage;
    mods[PUMMEL] = _AP + damage + getACMod();
    mods[BASH] = 0;
    return mods;
  }

  /** Lookup the speed of the character, factoring in height */
  public int calcSpeed()
  {
    if (_AP < 16) {
      return 2;
    } else if (_AP < 23) {
      return 3;
    } else if (_AP < 33) {
      return 4;
    } else {
      return 5;
    }
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

  public int getMagicAttackMod()
  {
    return findInRange(WIS);
  }

  public int getACMod()
  {
    return getToHitMissleBonus();
  }

  public int getToHitStr()
  {
    return findInRange(STR);
  }

  public int getToHitMissleBonus()
  {
    return findInRange(DEX);
  }

  public int getHpMod()
  {
    return findInRange(CON);
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

  public void loadTraitKeys(Map<PersonKeys, String> map)
  {
    map.put(PersonKeys.STR, "" + getTrait(STR));
    map.put(PersonKeys.DEX, "" + getTrait(DEX));
    map.put(PersonKeys.CON, "" + getTrait(CON));
    map.put(PersonKeys.INT, "" + getTrait(INT));
    map.put(PersonKeys.WIS, "" + getTrait(WIS));
    map.put(PersonKeys.CHR, "" + getTrait(CHR));

    map.put(PersonKeys.DAMAGE, "" + getStrDmgBonus());
    map.put(PersonKeys.TO_HIT_MELEE, "" + getToHitStr());
    map.put(PersonKeys.WT_ALLOW, "" + getWeightAllowance());

    map.put(PersonKeys.MAM, "" + getMagicAttackMod());
    map.put(PersonKeys.HP_MOD, "" + getHpMod());

    map.put(PersonKeys.TO_HIT_MISSLE, "" + getToHitMissleBonus());
    map.put(PersonKeys.AC_MOD, "" + getACMod());

    map.put(PersonKeys.MAX_LANGS, "" + getMaxLangs());
    map.put(PersonKeys.LITERACY, getLiteracy());

    map.put(PersonKeys.SPEED, "" + _speed);
    map.put(PersonKeys.AP, "" + _AP);
    map.put(PersonKeys.OVERBEARING, "" + _apMods[OVERBEAR]);
    map.put(PersonKeys.PUMMELING, "" + _apMods[PUMMEL]);
    map.put(PersonKeys.GRAPPLING, "" + _apMods[GRAPPLE]);
    map.put(PersonKeys.SHIELD_BASH, "" + _apMods[BASH]);
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
    return getTrait(INT) / 2 - 3;
  }

  public boolean isLargestTrait(PrimeTraits trait)
  { 
    return getTrait(findLargestTrait()) == getTrait(trait);
  }
  
  @Override
  public String toString()
  {
    return "STR: " + getTrait(PrimeTraits.STR) + ", " +
        "INT: " + getTrait(PrimeTraits.INT) + ", " +
        "WIS: " + getTrait(PrimeTraits.WIS) + ", " +
        "DEX: " + getTrait(PrimeTraits.DEX) + ", " +
        "CON: " + getTrait(PrimeTraits.CON) + ", " +
        "CHR: " + getTrait(PrimeTraits.CHR);
  }
}
