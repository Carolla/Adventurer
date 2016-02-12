package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.HalfOrc;

public class TestHalfOrc
{
  @Test
  public void traitsAreAdjustStrAndConPlus1ChrMinus2()
  {
     TraitList traits = new TraitList(TraitList.DEFAULT_TRAITS);
     HalfOrc ho = new HalfOrc(new Gender("Male"));
     ho.adjustTraitsForRace(traits);

     assertEquals(8, traits.getTrait(PrimeTraits.CHR));
     assertEquals(11, traits.getTrait(PrimeTraits.CON));
     assertEquals(11, traits.getTrait(PrimeTraits.STR));
  }
}
