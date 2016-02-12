
package chronos.pdc.character;

import static chronos.pdc.character.TraitList.PrimeTraits.CHR;
import static chronos.pdc.character.TraitList.PrimeTraits.CON;
import static chronos.pdc.character.TraitList.PrimeTraits.DEX;
import static chronos.pdc.character.TraitList.PrimeTraits.INT;
import static chronos.pdc.character.TraitList.PrimeTraits.STR;
import static chronos.pdc.character.TraitList.PrimeTraits.WIS;

import java.util.EnumMap;

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

  private final EnumMap<PrimeTraits, Integer> _traits = new EnumMap<PrimeTraits, Integer>(
      PrimeTraits.class);;
  private static final MetaDie _md = new MetaDie();
  public static final int DEFAULT_TRAIT_VALUE = 10;
  public static final int[] DEFAULT_TRAITS = {DEFAULT_TRAIT_VALUE,DEFAULT_TRAIT_VALUE,DEFAULT_TRAIT_VALUE,DEFAULT_TRAIT_VALUE,DEFAULT_TRAIT_VALUE,DEFAULT_TRAIT_VALUE };

  public TraitList()
  {
    this(_md.rollTraits());
  }

  public TraitList(int[] traits)
  {
    for (PrimeTraits t : PrimeTraits.values()) {
      _traits.put(t, traits[t.ordinal()]);
    }

    _AP = getTrait(STR) + getTrait(DEX);
    _apMods = calcAPMods(0, getTrait(STR));
    _speed = calcSpeed();
  }

  public int getTrait(PrimeTraits trait)
  {
    return _traits.get(trait);
  }

  public int adjust(PrimeTraits con, int i)
  {
    if (_traits.containsKey(con)) {
      return _traits.put(con, _traits.get(con) + i);
    }
    return 0;
  }

  /**
   * Swap the largest raw trait for the prime trait with the specific klass
   * 
   * @param primeTrait the trait that should be the largest trait 
   * @param largestTrait the index of the largest trait to swap
   */
  public void swapPrime(PrimeTraits primeTrait, PrimeTraits largestTrait)
  {
    int oldPrime = getTrait(primeTrait);
    int largest = getTrait(largestTrait);
    _traits.put(primeTrait, largest);
    _traits.put(largestTrait, oldPrime);
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
    // Adjust for height
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

  public void loadTraitKeys(EnumMap<PersonKeys, String> map)
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
}
