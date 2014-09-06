/**
 * Skill.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import mylib.ApplicationException;
import mylib.civ.DataShuttle;
import mylib.dmc.RegistryElement;
import chronos.civ.SkillKeys;

/**
 * Each Skill contains a name, description, and an associated action. Skills are inherent to certain
 * races (e.g. Elf's tingle) and klass (e.g. Rogue's Detect Traps skil). Skills are also associated
 * with certain occupations, e.g., a Jeweler can "Appraise". The entire set of skills are stored in
 * the SkillRegistry for assigning to the Hero. Later, the corresponding skill actions will be
 * implemented so the Hero can use them.
 * 
 * @author Alan Cline
 * @version Jan 5 2010 // original <br>
 *          May 15 2011 // TAA Added in skill2Rec and rec2Skill <br>
 *          May 22 2011 // TAA Udpated methods for testing <br>
 *          Jun 13 2011 // TAA changed to RegObject <br>
 *          2.3 FebJ10 2013 // ABC removed RegObject for db4o <br>
 *          2.4 Mar 25 2013 // ABC added in IRegistryElement <br>
 */
public class Skill extends RegistryElement
{
  // Serializable because it is part of the serialized Person object
  static final long serialVersionUID = 101005L; // creation date

  /** The name of the skill the player uses to select */
  private String _name = null;
  /** A description of the skill and how the Hero may use it. */
  private String _description = null;
  /**
   * The action that corresponds to the skill, currently implemented as a String
   */
  private String _action = null;

  /** Errors message for overly long field data */
  private final String FLDERR_OVERLONG = "Field is overly long by %d characters";
  /** Errors message for missing field */
  private final String FLDERR_MISSING = "%s field is requied to have data";

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public Skill()
  {}

  /**
   * Construct an skill from its components
   * 
   * @param name of the skill; cannot be null
   * @param desc description of the skill, and how it might be used; cannot be null.
   * @throws ApplicationException if the parms are null, or are too long
   */
  public Skill(String name, String desc) throws ApplicationException
  {
    // Name cannot be null
    if (name == null) {
      throw new ApplicationException(name + String.format(FLDERR_MISSING, "Name"));
    }
    // Description cannot be null
    if (desc == null) {
      throw new ApplicationException(name + String.format(FLDERR_MISSING, "Description"));
    }

    // Do not create a Skill if the description is too long
    if (desc.length() > SkillKeys.DESC.maxLength()) {
      throw new ApplicationException(name + String.format(FLDERR_OVERLONG,
          (name.length() - SkillKeys.DESC.maxLength())));
    }

    // Do not create a Skill if the name is too long
    if (name.length() > SkillKeys.NAME.maxLength()) {
      throw new ApplicationException(name + String.format(FLDERR_OVERLONG,
          (name.length() - SkillKeys.NAME.maxLength())));
    }

    // Build the skill as specifics
    _name = name;
    _description = desc;
    _action = null;
  }

  /*
   * ACCESSORS FOR DISPLAYING THE PARTS
   */

  /*
   * ACCESSORS FOR DISPLAYING THE PARTS
   */

  /**
   * Two Skills are considered equal if their names are equal. This is a required implementation for
   * the IRegistryElement interface.
   * 
   * @param otherThing the Skill to be considered
   * @return true if the Skill has the same name as this object
   */
  public boolean equals(Object otherThing)
  {
    // Guard against null input
    if (otherThing == null) {
      return false;
    }
    Skill target = (Skill) otherThing;
    boolean bName = _name.equals(target._name);
    return bName;
  }

  /**
   * Get the description of the skill
   * 
   * @return the description of the skill
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Get the key field for this Skill; default is name
   * 
   * @return the description of the skill
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

  /**
   * Convert a skill object into a data shuttle to be passed back to the HIC
   * 
   * @param skill The skill to be converted to a shuttle
   * @return shuttle packed with data
   */
  public DataShuttle<SkillKeys> loadShuttle(DataShuttle<SkillKeys> ds)
  {
    if (ds != null) {
      // Load up the shuttle
      ds.putField(SkillKeys.NAME, _name);
      ds.putField(SkillKeys.ACTION, _action);
      ds.putField(SkillKeys.DESC, _description);
      // ds.putField(SkillKeys.RACE, _race);
      // ds.putField(SkillKeys.KLASS, _klass);
    }
    return ds;
  }

  /**
   * Convert Skill to string
   * 
   * @param skill that is converted to a record for saving
   * @return record of the skill passed in
   */
  @Override
  public String toString()
  {
    return (_name);
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ Inner Class: MockSkill
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockSkill
  {
    /** Default ctor */
    public MockSkill()
    {}

    /** Set a new name into the Skill */
    public void setName(String newName)
    {
      _name = newName;
    }

    /** Set a new desc into the Skill */
    public void setDesc(String newDesc)
    {
      _description = newDesc;
    }

  } // end of MockSkill inner class
} // end of Skill outer class
