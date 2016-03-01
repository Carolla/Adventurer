
package chronos.pdc.character;

import chronos.pdc.character.TraitList.PrimeTraits;


public class Gender
{
  public static final String MALE_STRING = "Male";
  public static final String FEMALE_STRING = "Female";

  private static enum Gender_e {
    MALE, FEMALE
  }

  private Gender_e _gender;

  public Gender(String genderString)
  {
    _gender = byName(genderString);
  }

  public boolean isMale()
  {
    return _gender == Gender_e.MALE;
  }

  public boolean isFemale()
  {
    return !isMale();
  }

  private Gender_e byName(String name)
  {
    if (name.equalsIgnoreCase("Male")) {
      return Gender_e.MALE;
    } else {
      return Gender_e.FEMALE;
    }
  }
  
  public int adjustHeightForGender(int height)
  {
    return isMale() ? height *= 1.1 : height;
  }
  
  public int adjustWeightForGender(int weight)
  {
    return isMale() ? weight *= 1.3 : weight;
  }

  public String pronoun()
  {
    return (_gender == Gender_e.FEMALE) ? "She" : "He";
  }


  /** Females are given more CON and CHR but less STR */
  public TraitList adjustTraitsForGender(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, -1);
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.CHR, 1);
    return traits;
  }
  
  @Override
  public String toString()
  {
    if (isMale()) {
      return MALE_STRING;
    } else {
      return FEMALE_STRING;
    }
  }
}
