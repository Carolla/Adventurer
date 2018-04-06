
package chronos.pdc.character;

import java.io.Serializable;

import chronos.pdc.character.TraitList.PrimeTraits;


public class Gender implements Serializable
{
  // Required for serialization
  static final long serialVersionUID = 201708040451L;

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

  /** Females are given more CON and CHR but less STR */
  public TraitList adjustTraitsForGender(TraitList traits)
  {
    if (_gender == Gender_e.FEMALE) {
      traits.adjust(PrimeTraits.STR, -1);
      traits.adjust(PrimeTraits.CON, 1);
      traits.adjust(PrimeTraits.CHR, 1);
    }
    return traits;
  }

  public boolean isFemale()
  {
    return !isMale();
  }

  public boolean isMale()
  {
    return _gender == Gender_e.MALE;
  }

  public String pronoun()
  {
    return (_gender == Gender_e.FEMALE) ? "She" : "He";
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_gender == null) ? 0 : _gender.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Gender other = (Gender) obj;
    if (_gender != other._gender)
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return isMale() ? MALE_STRING : FEMALE_STRING;
  }

  private Gender_e byName(String name)
  {
    return (name.equalsIgnoreCase("Male")) ? Gender_e.MALE : Gender_e.FEMALE;
  }
}
