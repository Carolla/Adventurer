package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Hobbit;

public class TestHobbit
{
  @Test
  public void traitsAreAdjustDexPlus1StrMinus1()
  {
     TraitList traits = new TraitList(TraitList.DEFAULT_TRAITS);
     Hobbit h = new Hobbit(new Gender("Male"));
     h.adjustTraitsForRace(traits);

     assertEquals(11, traits.getTrait(PrimeTraits.DEX));
     assertEquals(9, traits.getTrait(PrimeTraits.STR));
  }
}
