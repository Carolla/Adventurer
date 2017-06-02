package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.HalfElf;

public class TestHalfElf
{
  @Test
  public void traitsAreNotAdjusted()
  {
     TraitList traits = new TraitList(TraitList.DEFAULT_TRAITS);
     HalfElf he = new HalfElf();
     he.adjustTraitsForRace(traits);

     for (PrimeTraits trait : PrimeTraits.values()) {
       assertEquals(10, traits.getTrait(trait)); 
     }
  }
}
