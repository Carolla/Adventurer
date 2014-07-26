/**
 * Occupation.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import chronos.civ.OccupationKeys;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

import mylib.ApplicationException;
import mylib.civ.DataShuttle;
import mylib.dmc.RegistryElement;

/**
 * An occupation gives a person some skill from their previous experience.
 * Occupations are one-to-many to Skills: each Occupation has one Skill, but a
 * Skill might be
 * associated with more than one Occupations.
 * 
 * @author Timothy Armstrong
 * @version <DL>
 *          <DT>Build 1.0 Feb 5 2011 // original
 *          <DD>
 *          <DT>Build 2.0 June 13, 2011 // TAA updated for registries
 *          <DD>
 *          <DT>Build 2.1 Feb 29, 2012 // ABC updated to add Skill object
 *          <DD>
 *          </DL>
 */
public class Occupation extends RegistryElement
{
    // Serializable because it is part of the serialized Person object
    static final long serialVersionUID = 20110205L;		// creation date

    /** The name of the occupation the player selected */
    private String _name = null;

    /**
     * A name of the Skill assocated with the occupationl and how the Hero may
     * use it.
     */
    private String _skillName = null;

    /** The length of the name field in a record */
    public static final int OCC_NAME_LIMIT = 35;

    /**
     * Maximum length of the skill description (used for GUI limitations and
     * records)
     */
    public static final int OCC_SKILL_LIMIT = 35;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Default constructor */
    public Occupation() {}

    /**
     * Construct an Occupationl from its components
     * 
     * @param name of the occupation; cannot be null
     * @param skillname name of the associated skill
     * @throws ApplicationException if the description is too long
     * @throws NullPointerException if the parms are null
     */
    public Occupation(String name, String skillname)
            throws ApplicationException, NullPointerException
    {
        // GUARDS
        // Name cannot be null
        if (name == null) {
            throw new NullPointerException(name + ": Occupation must have a name; received null");
        }
        // Description cannot be null
        if (skillname == null) {
            throw new NullPointerException(name + ": Occupation must have a skill; received null");
        }
        // Do not create an Occupation if the skill is too long
        if (skillname.length() > OCC_SKILL_LIMIT) {
            throw new ApplicationException(skillname
                    + ": Skill description is too long by "
                    + (skillname.length() - OCC_SKILL_LIMIT));
        }
        // Do not create an Occupation if the name is too long
        if (name.length() > OCC_NAME_LIMIT) {
            throw new ApplicationException(name
                    + ": Occupation name is too long by "
                    + (name.length() - OCC_NAME_LIMIT));
        }
        // End Guards

        // Verify that the skill name given exists in the Skill Registry
        SkillRegistry skReg = (SkillRegistry) RegistryFactory.getInstance().getRegistry(RegKey.SKILL);
        Skill s = skReg.getSkill(skillname);
        if (s != null) {
            _name = name;
            _skillName = skillname;
        }
        else {
            throw new ApplicationException(skillname + " does not exist in the SkillRegistry");
        }
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * ACCESSORS FOR DISPLAYING THE PARTS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    // /** Two Occupations are considered equal if their names and associated
    // Skills are the same.
    // * This is a required implementation for the <code>ArrayList
    // contains()</code> method,
    // * and overrides the <code>Object.equals()</code> method, so must have
    // * exactly this signature, and cast to the target Class within.
    // *
    // * @param otherThing the Occupation to be considered
    // * @return true if the Occupation has the same name (or phrase) and Skill,
    // or memory address
    // */
    // @Override
    // public boolean equals(Object otherThing)
    // {
    // // Check that the parameter exists
    // if (otherThing == null) {
    // return false;
    // }
    //
    // // A quick test to see if objects are identical
    // if (this == otherThing) {
    // return true;
    // }
    //
    // // Check that a match occurs at least at the Class level
    // if (getClass() != otherThing.getClass()) {
    // return false;
    // }

    // /* (non-Javadoc)
    // * @see mylib.dmc.IRegistryElement#getPredicate()
    // */
    // @Override
    // public Predicate<IRegistryElement> getPredicate()
    // {
    // // TODO Auto-generated method stub
    // return null;
    // }

    /*
     * Two Occupations are considered equal if their name and associated Skill
     * are equal
     * 
     * @param otherThing the Skill to be considered
     * 
     * @return true if the Skill has the same name and description as this
     * object
     * 
     * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
     */
    @Override
    public boolean equals(Object otherThing)
    {
        // Guard against null input
        if (otherThing == null) {
            return false;
        }
        Occupation ocp = (Occupation) otherThing;
        boolean bName = _name.equals(ocp._name);
        boolean bSkill = _skillName.equals(ocp._skillName);
        return (bName);
    }

    // /** Two Occupations are considered equal if their names and associated
    // Skills are the same.
    // * This is a required implementation for the <code>ArrayList
    // contains()</code> method,
    // * and overrides the <code>Object.equals()</code> method, so must have
    // * exactly this signature, and cast to the target Class within.
    // *
    // * @param otherThing the Occupation to be considered
    // * @return true if the Occupation has the same name (or phrase) and Skill,
    // or memory address
    // */
    // @Override
    // public boolean equals(Object otherThing)
    // {
    // // Call the Object class's <code>equal()</code> method first
    // if (super.equals(otherThing) == false) {
    // return false;
    // }
    // // Now we know otherThing is equal to far
    // Occupation whatsIt = (Occupation) otherThing;
    // // Check for name and skill
    // return (this._name.equalsIgnoreCase(whatsIt._name)
    // && this._skillName.equalsIgnoreCase(whatsIt._skillName));
    // }

    // /* (non-Javadoc)
    // * @see mylib.dmc.IRegistryElement#getPredicate()
    // */
    // @Override
    // public Predicate<IRegistryElement> getPredicate()
    // {
    // // TODO Auto-generated method stub
    // return null;
    // }

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

    /**
     * Get the description of the skill that corresponds to this occupation
     * 
     * @return the name of the skill
     */
    public String getSkillName()
    {
        return _skillName;
    }

    // /** Find the Occupation by its name, and return the Skill associated with
    // it
    // * @param ocpName name of the occupation
    // * @return the Skill object, else null
    // * @throws ApplicationException if Skill cannot be found or created
    // */
    // public Skill getOccupationSkill(String ocpName) throws
    // ApplicationException
    // {
    // Skill sk = null;
    // try {
    // Occupation ocp = OccupTable.createOccupation(ocpName);
    // sk = ocp.getSkill();
    // } catch (ApplicationException ex) {
    // sk = null;
    // }
    // return sk;
    // }

    /**
     * Convert a occupation object into a data shuttle to be passed back
     * to the HIC
     * 
     * @param occ The occupation to be converted to a shuttle
     * @return shuttle packed with data
     */
    public DataShuttle<OccupationKeys> loadShuttle(
            DataShuttle<OccupationKeys> ds)
    {
        if (ds != null)
        {
            // Load up the shuttle
            ds.putField(OccupationKeys.NAME, _name);
            ds.putField(OccupationKeys.DESC, "Not implemented yet");
            ds.putField(OccupationKeys.SKILL, _skillName);
        }
        return ds;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * ACCESSORS FOR DISPLAYING THE PARTS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Return a string of the Occupatio's name and skill */
    public String toString()
    {
        return _name;
    }

    // /** Convert a shuttle object into a Occupation to be passed back
    // * @param shuttle The data shuttle loaded with data
    // * @return Occupation created using the data passed
    // */
    // public Occupation unloadShuttle(DataShuttle<OccupationKeys> shuttle)
    // {
    // String name = (String) shuttle.getField(OccupationKeys.NAME);
    // String skill = (String) shuttle.getField(OccupationKeys.SKILL);
    //
    // Occupation occ = null;
    // try
    // {
    // occ = new Occupation(name, skill);
    // } catch (ApplicationException e)
    // {
    // MsgCtrl.errMsgln(this, "Error creating skill.  " + e.getMessage());
    // }
    // return occ;
    // }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * INNER CLASS MockOccupation
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    public class MockOccupation
    {
        /** Default ctor */
        public MockOccupation() {}

        /** Set a new name into the Occupation */
        public void setName(String newName)
        {
            _name = newName;
        }

    }   // mock of MockOccupation inner class

}		// end of Occupation class
