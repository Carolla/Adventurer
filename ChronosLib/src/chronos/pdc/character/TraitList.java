
package chronos.pdc.character;

import java.util.EnumMap;

import mylib.pdc.MetaDie;

public class TraitList
{
  /** Indices into the Hero's prime traits */
  public enum PrimeTraits {
    STR, INT, WIS, DEX, CON, CHR
  };

  private EnumMap<PrimeTraits, Integer> _traits;
  private static final MetaDie _md = new MetaDie();
  
  public TraitList()
  {
    _traits = new EnumMap<PrimeTraits, Integer>(PrimeTraits.class);
    for (PrimeTraits trait : PrimeTraits.values()) {
      _traits.put(trait, _md.rollTrait());
    }
  }

  public TraitList(int[] traits)
  {
    _traits = new EnumMap<PrimeTraits, Integer>(PrimeTraits.class);
    for (PrimeTraits t : PrimeTraits.values()) {
      _traits.put(t, traits[t.ordinal()]);
    }
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
   * @param prime the trait that should be the largest trait 
   * @param largest the index of the largest trait to swap
   */
  public void swapPrime(PrimeTraits primeTrait, PrimeTraits largestTrait)
  {
      int oldPrime = getTrait(primeTrait);
      int largest = getTrait(largestTrait);
      _traits.put(primeTrait, largest);
      _traits.put(largestTrait, oldPrime);
  }
}
