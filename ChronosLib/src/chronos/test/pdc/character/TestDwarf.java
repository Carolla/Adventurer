package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Dwarf;

public class TestDwarf
{
  @Test
  public void traitsAreAdjustConPlus1ChrMinus1()
  {
     TraitList traits = new TraitList(TraitList.DEFAULT_TRAITS);
     Dwarf d = new Dwarf();
     d.adjustTraitsForRace(traits);

     assertEquals(11, traits.getTrait(PrimeTraits.CON));
     assertEquals(9, traits.getTrait(PrimeTraits.CHR));
  }
}
