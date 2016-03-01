
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;

public class TestGender
{
  private Gender female;
  private Gender male;

  @Before
  public void setup()
  {
    female = new Gender(Gender.FEMALE_STRING);
    male = new Gender(Gender.MALE_STRING);
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
  public void menShouldWeighMoreThanWomen()
  {
    int weight = 100;
    assertTrue(male.adjustWeightForGender(weight) > female.adjustWeightForGender(weight));
  }
  
  @Test
  public void menShouldBeTallerThanWomen()
  {
    int height = 60;
    assertTrue(male.adjustHeightForGender(height) > female.adjustHeightForGender(height));
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
