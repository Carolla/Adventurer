package chronos.pdc.race;

/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

import static chronos.pdc.character.TraitList.PrimeTraits.CON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import mylib.pdc.MetaDie;
import chronos.civ.PersonKeys;
import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 */
public abstract class Race
{
  
  public static final String[] RACE_LIST =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  protected String _raceName;
  protected String _raceLang;
  
  protected static MetaDie _md = new MetaDie();
  
  /** Hero male and female weight ranges */
  protected int _weightMedValue;
  /** Hero male and female height ranges */
  protected int _heightMedValue;
  
  /** Racial limits for a each subclass for the traits */
  protected int[] _minLimit;
  protected int[] _maxLimit;
  
  protected String _racialLang;
  protected String _descriptor;
  protected int[] _racialThiefMods;
  protected String[] _raceSkills;
  protected int _racialPoisonResist = 0;

  public abstract int calcWeight();
  public abstract int calcHeight();

  /**
   * Create a specific subclass of Race based on the Race name. <br>
   * NOTE: The subclass must be in the same package as the Race class.
   *
   * @param RaceName the name of the subclass to be created
   */
  static public Race createRace(String raceName, Gender gender)
  {
    Race race = null;
    if (raceName.equals("Dwarf")) {
      race =  new Dwarf(gender);
    } else if (raceName.equals("Elf")) {
      race =  new Elf(gender);
    } else if (raceName.equals("Gnome")) {
      race =  new Gnome(gender);
    } else if (raceName.equals("Half-Elf")) {
      race =  new HalfElf(gender);
    } else if (raceName.equals("Half-Orc")) {
      race =  new HalfOrc(gender);
    } else if (raceName.equals("Hobbit")) {
      race =  new Hobbit(gender);
    } else {
      race =  new Human(gender);      
    }
    return race;
  }
  
  /** Races have different advantages and disadvantages */
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    addRacialPoisonResist(traits.getTrait(CON), traits.getMagicAttackMod());
    return traits;
  }

  private void addRacialPoisonResist(int constitution, int magicAttackMod)
  {
    if ((_raceName.equalsIgnoreCase("Dwarf")) ||
        (_raceName.equalsIgnoreCase("Gnome")) ||
        (_raceName.equalsIgnoreCase("Hobbit"))) {
      _racialPoisonResist = (int) Math.round((float) constitution / 3.5);
      magicAttackMod += _racialPoisonResist;
    }
  }
  
  // Assign the chances for thief skills for level 1 by race
  public String[][] adjustRacialThiefSkills(String[][] skills) 
  {
    // Adjust the thief skills by the racial mods
    for (int k=0; k < skills.length; k++) {
      int oldChance = Integer.parseInt(skills[k][1]);
      int newChance = oldChance + _racialThiefMods[k];
      skills[k][1] = String.format("%s",  newChance);     
    }
    return skills;
  }

  /**
   * Return the language specific to the race, or null. Half-breed races have a 50% chance of
   * knowing their race language.
   * 
   * @return the race language
   */
  public String getRacialLanguage()
  {
    return _raceLang;
  }

  public String getName()
  {
    return _raceName;
  }

  public String getRaceDescriptor()
  {
    return _descriptor;
  }
  
  public List<String> getSkills()
  {
    return new ArrayList<String>(Arrays.asList(_raceSkills));
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_raceName == null) ? 0 : _raceName.hashCode());
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
    Race other = (Race) obj;
    if (_raceName == null) {
      if (other._raceName != null)
        return false;
    } else if (!_raceName.equals(other._raceName))
      return false;
    return true;
  }
  
  public void loadRaceKeys(EnumMap<PersonKeys, String> map)
  {
    map.put(PersonKeys.RACENAME, _raceName);
    map.put(PersonKeys.RMR, "" + _racialPoisonResist); 
  }


} // end of abstract Race class
