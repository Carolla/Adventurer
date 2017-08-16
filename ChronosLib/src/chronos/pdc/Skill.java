/**
 * Skill.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.io.Serializable;

import chronos.pdc.registry.SkillRegistry;
import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;

/**
 * Each Skill contains a name, description, and an associated action. Skills are inherent to certain
 * races (e.g. Elf's <i>Tingle</i>) and klass (e.g. Rogue's <i>Detect Traps</i>). Skills are also
 * associated with certain occupations, e.g., a Jeweler can <i>Appraise</i>. The entire set of
 * skills are stored in the {@code SkillRegistry} for assigning to the Hero. Later, the
 * corresponding skill actions will be implemented so the Hero can use them.
 * 
 * @author Alan Cline
 * @version Jan 5 2010 // original <br>
 *          May 15 2011 // TAA Added in skill2Rec and rec2Skill <br>
 *          May 22 2011 // TAA Udpated methods for testing <br>
 *          Jun 13 2011 // TAA changed to RegObject <br>
 *          Feb 10 2013 // ABC removed RegObject for db4o <br>
 *          Mar 25 2013 // ABC added in IRegistryElement <br>
 *          July 17 2017 // ABC slight modifications per QATool <br>
 */
public class Skill implements IRegistryElement, Serializable
{

  // Serializable because it is part of the serialized Person object
  static final long serialVersionUID = 101005L; // creation date

  private static SkillRegistry _skreg;

  public static final int MAX_NAME_LEN = 35;
  public static final int MAX_DESC_LEN = 70;
  protected final String _name;
  protected final String _description;

  /** Errors message for missing field */
  private final String FLDERR_MISSING = " %s field is required to have data";
  private final String TOOLONG_INPUT = " %s field is too long for valid input";

  
  // =============================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // =============================================================================

  /**
   * Construct a skill from its components
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
    // Description must be within valid length
//    if (desc.length() > MAX_DESC_LEN) {
//      throw new ApplicationException(String.format(TOOLONG_INPUT, "Description"));
//    }
    // Name must be within valid length
    if (name.length() > MAX_NAME_LEN) {
      throw new ApplicationException(String.format(TOOLONG_INPUT, "Name"));
    }
    // Build the skill as specifics
    _name = name;
    _description = desc;
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
    Skill other = (Skill) obj;
    if (!_description.equals(other._description))
      return false;
    if (!_name.equals(other._name))
      return false;
    return true;
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

  public static Skill getSkill(String string)
  {
    return _skreg.getSkill(string);
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_description == null) ? 0 : _description.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    return result;
  }

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  public static void setSkillRegistry(SkillRegistry skreg)
  {
    _skreg = skreg;
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
    return (_name + ": " + _description);
  }
} // end of Skill outer class
