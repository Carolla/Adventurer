/**
 * Occupation.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.MetaDie;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.SkillRegistry;

/**
 * An occupation gives a person some skill from their previous experience. Occupations are
 * one-to-many to Skills: each Occupation has one Skill, but a Skill might be associated with more
 * than one Occupations.
 * 
 * @author Timothy Armstrong
 * @version Feb 5 2011 // original <br>
 *          June 13, 2011 // TAA updated for registries <br>
 *          Feb 29, 2012 // ABC updated to add Skill object <br>
 */
public class Occupation implements IRegistryElement
{

  // Various occupations (31) for random selection
  public static final String[] _ocpTable = {
      "Academic", "Acrobat", "Alchemist", "Apothecary", "Armorer", "Banker", "Bowyer",
      "Carpenter", "Farmer", "Fisher", "Forester", "Freighter", "Gambler", "Hunter",
      "Husbandman", "Innkeeper", "Jeweler", "Leatherworker", "Painter", "Mason",
      "Miner", "Navigator", "Sailor", "Shipwright", "Tailor", "Trader", "Trapper",
      "Weaponsmith", "Weaver", "Woodworker", "Drifter"};

  private static final MetaDie md = new MetaDie();

  /** The name of the occupation the player selected */
  private String _name = null;
  private String _description;
  private String _trait;
  private List<Skill> _skills = new ArrayList<Skill>();

  /** The length of the name field in a record */
  public static final int OCC_NAME_LIMIT = 35;

  /**
   * Maximum length of the skill description (used for GUI limitations and records)
   */
  public static final int OCC_SKILL_LIMIT = 35;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */
  public static Occupation getRandomOccupation()
  {
    int ndx = md.getRandom(1, _ocpTable.length) - 1; // range must be between 1 and maxLimit
    return new OccupationRegistry().getOccupation(_ocpTable[ndx]);
  }

  /**
   * Construct an Occupation from its components
   * 
   * @param name of the occupation; cannot be null
   * @param skillname name of the associated skill
   * @throws ApplicationException if the description is too long
   * @throws NullPointerException if the parms are null
   */
  public Occupation(String name, String description, String trait, List<String> arrayList)
  {
    // GUARDS
    // Name cannot be null
    if (name == null || description == null || trait == null || arrayList == null) {
      throw new NullPointerException(name
          + ": Occupation must have a name, description, trait and skills; received null");
    }

    // Do not create an Occupation if its name is too long
    if (name.length() > OCC_NAME_LIMIT) {
      throw new ApplicationException(name
          + ": Occupation name is too long by "
          + (name.length() - OCC_NAME_LIMIT));
    }
    // End Guards

    _name = name;
    _description = description;
    _trait = trait;

    SkillRegistry skReg = new SkillRegistry();
    for (String skill : arrayList) {
      _skills.add(skReg.getSkill(skill));
    }
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
    Occupation other = (Occupation) obj;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    return true;
  }

  /*
   * Two Occupations are considered equal if their name and associated Skill are equal
   * 
   * @param otherThing the Skill to be considered
   * 
   * @return true if the Skill has the same name and description as this object
   * 
   * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
   */
  @Override
  public boolean equals(IRegistryElement otherThing)
  {
    return equals((Object) otherThing);
  }


  /*
   * Returns the field used for registry retrieval
   * 
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
  }

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getTrait()
  {
    return _trait;
  }

  public List<Skill> getSkill()
  {
    return _skills;
  }

  /**
   * Get the description of the skill that corresponds to this occupation
   * 
   * @return the name of the skill
   */
  public List<String> getSkillNames()
  {
    List<String> names = new ArrayList<String>();
    for (Skill s : _skills) {
      names.add(s.getName());
    }
    return names;
  }

  /** Return a string of the Occupatio's name and skill */
  public String toString()
  {
    return _name;
  }

} // end of Occupation class
