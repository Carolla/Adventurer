/**
 * ConcreteBuilding.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import chronos.pdc.buildings.Building;

/**
 *    Enables testing of the abstract base class building
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Apr 5, 2013   // original <DD>
 * </DL>
 */
public class ConcreteBuilding extends Building
{

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Create the base building; called by the concrete Building object
     * @param name      of this building
     * @param master    owner or manager of the building
     * @param hoverText     quick phrase for purpose of building
     * @param intro        first glance of building
     * @param desc       detailed description of building, usually once inside
     * @throws ApplicationException if NPC cannot be found
     **/
    public ConcreteBuilding(String name, String master, String hoverText, String intro, String desc) 
                        throws ApplicationException
    {
        super(name, master, hoverText, intro, desc);
    }


    /* A Building is equal if its key (name) and Building Master is equal
     * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
     */
    @Override
    public boolean equals(IRegistryElement target)
    {
        Building bldg = (Building) target;
        boolean bName = (this._name).equals(bldg.getKey());
        boolean bMaster = (this._buildingMaster).equals(bldg.getMaster());
        return (bName && bMaster);
    }

    
    /* Gets the key, the name of this Building
     * @returns the name of this building
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    @Override
    public String getKey()
    {
        return _name;
    }


    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
 
    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  MockConcreteBuilding inner class 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
   
public class MockConcreteBuilding
{
    /** Constructor */
    public MockConcreteBuilding()  { }
        
    /** Return the closed message for comparisons */
    public String getClosedMsg()
    {
        return String.format(BUILDING_CLOSED, _name, 
                        getMeridianTime(_openTime), getMeridianTime(_closingTime));
    }

}       // end of MockConcreteBuilding inner class
    
    
}           // end of ConcreteBuilding class
