/**
 * Occupation.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.SkillRegistry;
import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.MetaDie;

/**
 * Each Occupation is associated with one Skill, but a Skill might be associated with more than one
 * Occupation.
 * 
 * @see Skill
 * 
 * @author Timothy Armstrong
 * @version Feb 5 2011 // original <br>
 *          June 13, 2011 // TAA updated for registries <br>
 *          Feb 29, 2012 // ABC updated to add Skill object <br>
 *          June 3 2017 // ABC updated to display Skill actions <br>
 */
public class Occupation implements IRegistryElement, Serializable
{
  // Required for serialization
  static final long serialVersionUID = 201708040446L;

  /** MetDie for randomizing Occupations */
  private static final MetaDie _md = new MetaDie();

  /** The name of this Occupation */
  private String _name = null;
  /** What this Occupation is or can do */
  private String _description;
  /** A list of skills associated with this Occupation */
  private List<Skill> _skills = new ArrayList<Skill>();

  /** Max length of the Occupation name field. */
  public static final int OCC_NAME_LIMIT = 35;


  // ========================================================================
  // CONSTRUCTOR AND RELATED METHODS
  // ========================================================================

  /**
   * Construct an Occupation from its components: each Occupation has a name, description, and a set
   * of Skills. No parm may be null.
   *
   * @param name of the occupation
   * @param description of the occupation
   * @param skillNames names for a set of Skills associated with this Occupation
   * @throws NullPointerException any of the parms are null
   */

  public Occupation(String name, String description, List<String> skillNames)
  {
    // GUARDS
    // Name cannot be null
    if (name == null || description == null || skillNames == null) {
      throw new NullPointerException(name
          + ": Occupation must have a name, description, and skills; received null");
    }
    // Do not create an Occupation if its name is too long
    if (name.length() > OCC_NAME_LIMIT) {
      throw new ApplicationException(name + ": Occupation name is too long by "
          + (name.length() - OCC_NAME_LIMIT));
    }

    _name = name;
    _description = description;
    // Add the Skills to the Occupation from the given skill names
    _skills = addSkills(skillNames);
  }


  /**
   * Converts each Skill name into a list of Skill objects. If a Skill is not found in the
   * SkillRegistry, it is not added to the Occupation
   * 
   * @param list of skill names associated with this Occupation
   * @return list of Skill objects for the given skill names
   */
  private List<Skill> addSkills(List<String> skillNames)
  {
    SkillRegistry skReg = new SkillRegistry();
    for (String skill : skillNames) {
      Skill regSkill = skReg.getSkill(skill);
      if (!(regSkill == null)) {
        _skills.add(regSkill);
      } else {
        System.err.println("Skill " + skill + " not recognized in SkillRegistry; will not be added"
            + "to Occupation");
      }
    }
    return _skills;
  }


  // ========================================================================
  // PUBLIC METHODS
  // ========================================================================

  /**
   * Required support primitive: two Occupations are equal when their names are equal
   * 
   * @return true if {@code Occupation.equals(otherOccupation)}
   **/
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

  /** Get the Occupation's description */
  public String getDescription()
  {
    return _description;
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
   * Get the Occupation's name. If retrieving an Occupation from the OccupationRegistry, use
   * {@code getKey()}.
   */
  public String getName()
  {
    return _name;
  }


  /** Get a random Occupation from the OccupationRegistry */
  public static Occupation getRandomOccupation()
  {
//    int ndx = _md.getRandom(1, _ocpTable.length) - 1;
//    int ndx = _md.getRandom(1, ocpReg.getNbrElements() - 1);
//    return new OccupationRegistry().getOccupation(_ocpTable[ndx]);
    OccupationRegistry ocpReg = new OccupationRegistry();
    List<Occupation> ocps = ocpReg.getAll();
    int ndx = _md.getRandom(1, ocps.size() - 1);
    return ocps.get(ndx);
  }


  /** Return a list of Skill objects for this Occupation */
  public List<Skill> getSkills()
  {
    return _skills;
  }


  /**
   * Returns a list of skill names associated with this Occupation
   */
  public List<String> getSkillNames()
  {
    List<String> names = new ArrayList<String>();
    for (Skill s : _skills) {
      names.add(s.getName());
    }
    return names;
  }


  /**
   * Required support primitive: an Occupation is encrypted into a comparable int code
   * 
   * @return true if {@code Occupation.equals(otherOccupation)}
   **/
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    return result;
  }


  /**
   * Required support primitive: an Occupation is converted to its name when called by the system's
   * print methods.
   **/
  @Override
  public String toString()
  {
    return _name;
  }


} // end of Occupation class
