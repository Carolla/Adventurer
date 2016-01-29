package chronos.pdc.character;

import static chronos.pdc.character.TraitList.PrimeTraits.CON;
import static chronos.pdc.character.TraitList.PrimeTraits.STR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestTraitList
{

  @Test
  public void primeTraitIsLargestAfterSwap()
  {
    TraitList traits = new TraitList(new int[] { 15, 10, 10, 10, 10, 10 });
    
    traits.swapPrime(CON, STR);
    assertEquals(traits.getTrait(STR), 10);
    assertEquals(traits.getTrait(CON), 15);
  }

}
