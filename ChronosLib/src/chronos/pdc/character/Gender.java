
package chronos.pdc.character;

import chronos.pdc.character.Hero.PrimeTraits;


public class Gender
{
  private static enum Gender_e {
    MALE, FEMALE
  }

  private Gender_e _gender;;
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
    if (name.equals("Male")) {
      return Gender_e.MALE;
    } else {
      return Gender_e.FEMALE;
    }
  }

  public String pronoun()
  {
    return (_gender == Gender_e.FEMALE) ? "She" : "He";
  }


  /** Females are given more CON and CHR but less STR */
  public int[] adjustTraitsForGender(int[] traits)
  {
    traits[PrimeTraits.STR.ordinal()] -= 1;
    traits[PrimeTraits.CON.ordinal()] += 1;
    traits[PrimeTraits.CHR.ordinal()] += 1;
    return traits;
  }
}
