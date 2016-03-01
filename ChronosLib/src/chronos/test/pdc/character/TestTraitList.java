
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;

public class TestTraitList
{

  @Test
  public void primeTraitIsLargestAfterSwap()
  {
    for (int i = 0; i < 100; i++) {
      TraitList traits = new TraitList();

      for (PrimeTraits trait : PrimeTraits.values()) {
        traits.swapPrime(trait);
        assertTrue(trait == traits.findLargestTrait() || traits.isLargestTrait(trait));
      }
    }
  }

  @Test
  public void largestTraitIsReturnedWhenLargest()
  {
    int[][] differentTraits = {
        {11, 10, 10, 10, 10, 10},
        {10, 11, 10, 10, 10, 10},
        {10, 10, 11, 10, 10, 10},
        {10, 10, 10, 11, 10, 10},
        {10, 10, 10, 10, 11, 10},
        {10, 10, 10, 10, 10, 11}
    };

    for (int i = 0; i < differentTraits.length; i++) {
      TraitList traits = new TraitList(differentTraits[i]);
      PrimeTraits trait = PrimeTraits.values()[i];
      assertEquals(trait, traits.findLargestTrait());
    }
  }

  @Test
  public void largestTraitIsReturnsWhenMultipleSame()
  {
    TraitList traits = new TraitList(TraitList.DEFAULT_TRAITS);
    for (PrimeTraits trait : PrimeTraits.values()) {
      assertEquals(traits.getTrait(traits.findLargestTrait()), traits.getTrait(trait));
    }
  }
}
