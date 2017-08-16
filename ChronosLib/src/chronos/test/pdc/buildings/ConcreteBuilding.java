/**
 * ConcreteBuilding.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import chronos.pdc.buildings.Building;
import mylib.ApplicationException;

/**
 * Enables testing of the abstract base class building
 *
 * @author Alan Cline
 * @version Apr 5, 2013 // original <br>
 *          Aug 9, 2017 // updated per QATool <br>
 */
public class ConcreteBuilding extends Building
{

  // ========================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ========================================================================

  /**
   * Create the base building; called by the concrete Building object
   * 
   * @param name of this building
   * @param master owner or manager of the building
   * @param openTime when (military time of day) the building opens for business
   * @param closeTime when (military time of day) the building closes for business
   * @param hoverText quick phrase for purpose of building
   * @param exterior first glance of building
   * @param interior detailed description of building, usually once inside
   * @throws ApplicationException if NPC cannot be found
   **/
  public ConcreteBuilding(String name, String master, int openTime, int closeTime, String hoverText,
      String exterior, String interior) throws ApplicationException
  {
    super(name, master, openTime, closeTime, hoverText, exterior, interior, null, null);
  }


  /*
   * Gets the key, the name of this Building
   * 
   * @returns the name of this building
   * 
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ MockConcreteBuilding inner class
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockConcreteBuilding
  {
    /** Constructor */
    public MockConcreteBuilding()
    {}

    /** Return the closed message for comparisons */
    public String getClosedMsg()
    {
      return String.format(BUILDING_CLOSED, _name,
          getMeridianTime(_openTime), getMeridianTime(_closingTime));
    }

  } // end of MockConcreteBuilding inner class


} // end of ConcreteBuilding class
