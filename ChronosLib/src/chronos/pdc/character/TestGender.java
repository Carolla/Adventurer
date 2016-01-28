
package chronos.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.TraitList.PrimeTraits;

public class TestGender
{
  private Gender female;

  @Before
  public void setup()
  {
    female = new Gender(Gender.FEMALE_STRING);
  }

  @Test
  public void aManIsMale()
  {
    assertTrue(new Gender(Gender.MALE_STRING).isMale());
  }

  @Test
  public void aWomanIsFemale()
  {
    assertTrue(new Gender(Gender.FEMALE_STRING).isFemale());
  }

  @Test
  public void nonMaleMeansFemae()
  {
    assertTrue(new Gender("CONFUSED").isFemale());
  }

  @Test
  public void adjustingTraitsReducesStrengthInWomen()
  {
    int[] intTraits = {10, 10, 10, 10, 10, 10};
    TraitList traits = new TraitList(intTraits);
    female.adjustTraitsForGender(traits);
    assertEquals(9, traits.getTrait(PrimeTraits.STR));
  }

  @Test
  public void adjustingTraitsIncreasesConstitutionAndCharismaInWomen()
  {
    int[] intTraits = {10, 10, 10, 10, 10, 10};
    TraitList traits = new TraitList(intTraits);
    female.adjustTraitsForGender(traits);
    assertEquals(11, traits.getTrait(PrimeTraits.CON));
    assertEquals(11, traits.getTrait(PrimeTraits.CHR));
  }
}
