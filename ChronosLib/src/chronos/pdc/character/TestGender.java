
package chronos.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Trait.PrimeTraits;

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
    int[] traits = {10, 10, 10, 10, 10, 10};
    female.adjustTraitsForGender(traits);
    assertEquals(9, traits[PrimeTraits.STR.ordinal()]);
  }

  @Test
  public void adjustingTraitsIncreasesConstitutionAndCharismaInWomen()
  {
    int[] traits = {10, 10, 10, 10, 10, 10};
    female.adjustTraitsForGender(traits);
    assertEquals(11, traits[PrimeTraits.CHR.ordinal()]);
    assertEquals(11, traits[PrimeTraits.CON.ordinal()]);
  }
}
