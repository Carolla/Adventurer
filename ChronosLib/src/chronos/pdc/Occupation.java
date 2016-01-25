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
  private static final String[] _ocpTable = {
      "Academic", "Acrobat", "Alchemist", "Apothecary", "Armorer", "Banker", "Bowyer",
      "Carpenter", "Farmer", "Fisher", "Forester", "Freighter", "Gambler", "Hunter",
      "Husbandman", "Innkeeper", "Jeweler", "Leatherworker", "Painter", "Mason",
      "Miner", "Navigator", "Sailor", "Shipwright", "Tailor", "Trader", "Trapper",
      "Weaponsmith", "Weaver", "Woodworker", "Drifter"};

  private static OccupationRegistry _ocpreg;

  /** The name of the occupation the player selected */
  private String _name = null;

  // TODO Should Occupation be saved with the Skill object, or just its name?
  /** A name of the Skill assocated with the occupationl and how the Hero may use it. */
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

  public static void setOccupationRegistry(OccupationRegistry ocpreg)
  {
    _ocpreg = ocpreg;
  }

  public static Occupation getOccupation(String occup)
  {
    return _ocpreg.getOccupation(occup);
  }

  public static Occupation getRandomOccupation()
  {
    MetaDie md = new MetaDie();
    int maxLimit = _ocpTable.length;
    // ndx is index into table, so ranges between 0 and length of table - 1
    int ndx = md.getRandom(1, maxLimit) - 1; // range must be between 1 and maxLimit
    return getOccupation(_ocpTable[ndx]);
  }

  /**
   * Construct an Occupation from its components
   * 
   * @param name of the occupation; cannot be null
   * @param skillname name of the associated skill
   * @throws ApplicationException if the description is too long
   * @throws NullPointerException if the parms are null
   */
  public Occupation(String name, List<String> skills)
  {
    // GUARDS
    // Name cannot be null
    if (name == null || skills == null) {
      throw new NullPointerException(name + ": Occupation must have a name; received null");
    }

    // Do not create an Occupation if its name is too long
    if (name.length() > OCC_NAME_LIMIT) {
      throw new ApplicationException(name
          + ": Occupation name is too long by "
          + (name.length() - OCC_NAME_LIMIT));
    }
    // End Guards

    _name = name;
    for (String skill : skills) {
      _skills.add(Skill.getSkill(skill));
    }
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
    // Guard against null input
    if (otherThing == null) {
      return false;
    }
    Occupation ocp = (Occupation) otherThing;
    boolean bName = _name.equals(ocp._name);
    boolean bSkill = _skills.equals(ocp._skills);
    return (bName || bSkill);
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

  /**
   * Get the name of the skill
   * 
   * @return the name of the skill
   */
  public String getName()
  {
    return _name;
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
  public List<String> getSkillName()
  {
    //return _skill.getName();
    return null;
  }

  /** Return a string of the Occupatio's name and skill */
  public String toString()
  {
    return _name;
  }

} // end of Occupation class
