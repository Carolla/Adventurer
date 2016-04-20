package chronos.pdc.character;

/**
 * Description.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.Chronos;

public class Description
{
  // Possible descriptors for charismas in increasing order. 
  // Ranges from CHR=8 to CHR=18 are normal; CHR=7 and CHR=19 are exceptional and rarely occur
  private static final String[] _chrDescs = {
      "ugly",                         // < 8
      "scarred",                      // 8
      "scarred from war or fire",     // 9
      "the result of years of misery",// 10 
      "weather-beaten and tough",     // 11
      "nothing special to look at",   // 12
      "clear-eyed and rugged",        // 13
      "attractive if one could scrape off the years of wear and tear", //14
      "a handsome adventurer",        // 15
      "gorgeous",                     // 16 
      "very attactive",               // 17
      "stunningly beautiful",         // 18
      "mesmerizing",                  // > 18
  };

  // Possible descriptors for positive charismas in a Height x Weight matrix,
  // must be in increasing order to call findRangeDescriptor
  private static  final String[][] posBody = {
      // Light   Average    Heavy
      {"petite", "compact", "b`urly"},     // Short height
      {"lithe", "athletic", "muscular"}, // Average height
      {"thin", "tall", "towering"},  // Tall height
  };

  // Possible descriptors for negative charismas in a Height x Weight matrix,
  // must be in increasing order when calling findRangeDescriptor()
  private static final String[][] negBody = {
      {"puny", "pudgy", "squat"}, // Short height
      {"slinky", "average-size", "heavy"}, // Average height
      {"skinny", "tall", "giant"} // Tall height
  };

  private String _description;
  private int _chr;
  private String _raceDescriptor;
  private int _height;
  private int _weight;
  private String _hairColor;
  private Gender _gender;

  public Description(int charisma, String raceDescriptor, String hairColor, Gender gender,
      int height, int weight)
  {
    _chr = charisma;
    _raceDescriptor = raceDescriptor;
    _hairColor = hairColor;
    _gender = gender;
    _weight = weight;
    _height = height;
    _description = initDescription();
  }


  /**
   * Template for the attributes in description: \n\t
   * "A [height_descriptor] and [weight_descriptor]" + "[gender] with [color] hair" +
   * "and [racial note]". [She | He] is [CHR reflection]". \n\t
   * 
   * @return a string that describes the Person's body-type (a Race function).
   */
  public String initDescription()
  {
    String bodyType = bodyType(_chr, _height, _weight);
    return article(bodyType) + bodyType + " " + _gender.toString().toLowerCase() + " with "
        + hairDescription() + " and " + _raceDescriptor + ". " + _gender.pronoun() + " is "
        + initCharismaDescriptor(_chr) + ".";
  }

  public String hairDescription()
  {
    return (_hairColor.equalsIgnoreCase("bald")) ? "a bald head" : _hairColor + " hair";
  }

  public static String article(String string)
  {
    return (beginsWithVowel(string)) ? "An " : "A ";
  }

  public static boolean beginsWithVowel(String target)
  {
    final char[] vowels = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};

    for (char c : vowels) {
      if (target.charAt(0) == c) {
        return true;
      }
    }
    return false;
  }

  public void loadKeys(Map<PersonKeys, String> map)
  {
    map.put(PersonKeys.DESCRIPTION, _description);
    map.put(PersonKeys.WEIGHT, "" + _weight);
    map.put(PersonKeys.HEIGHT, "" + _height);
  }

  /**
   * Associate the height and weight of the character with their Charisma to get a body type
   * descriptor. For this implementation, height and weight are broken into only three cartegories.
   * 
   * @param charisma body types are perceived as favorable or unfavorable depending on the Person's
   *        CHR
   * @return what the person looks like for their Charisma trait
   */
  public String bodyType(int charisma, int height, int weight)
  {
    String[][] descrChoice = (charisma >= Chronos.AVERAGE_TRAIT) ? posBody : negBody;
    int rowNbr = findBucket(height, Chronos.STD_MAX_HEIGHT, Chronos.STD_MIN_HEIGHT);
    int colNbr = findBucket(weight, Chronos.STD_MAX_WEIGHT, Chronos.STD_MIN_WEIGHT);
    return descrChoice[rowNbr][colNbr];
  }

  private int findBucket(int value, int highValue, int lowValue)
  {
    if (value < lowValue) {
      return 0;
    } else if (value > highValue) {
      return 2;
    } else {
      return 1;
    }
  }

  /**
   * Associate the Charisma of the character with their attractiveness, a simple string matching
   * algorithm to a description table.
   * 
   * @param charisma the Person's prime trait for making friends and negotiating
   * @return what the person looks like for their Charisma trait
   */
  public String initCharismaDescriptor(int charisma)
  {
    int pos = charisma - Chronos.LOW_TRAIT + 1;
    if (charisma < Chronos.LOW_TRAIT) {
      pos = 0;
    } else if (charisma > Chronos.HIGH_TRAIT) {
      pos = _chrDescs.length - 1;
    }
    return _chrDescs[pos];
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_description == null) ? 0 : _description.hashCode());
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
    Description other = (Description) obj;
    if (_description == null) {
      if (other._description != null)
        return false;
    } else if (!_description.equals(other._description))
      return false;
    return true;
  }
  
  @Override
  public String toString()
  {
    return _description;
  }
}
