package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Elf;

public class TestElf
{
  @Test
  public void traitsAreAdjustDexPlus1ConMinus1()
  {
     TraitList traits = new TraitList(TraitList.DEFAULT_TRAITS);
     Elf e = new Elf(new Gender("Male"));
     e.adjustTraitsForRace(traits);

     assertEquals(11, traits.getTrait(PrimeTraits.DEX));
     assertEquals(9, traits.getTrait(PrimeTraits.CON));
  }
}
