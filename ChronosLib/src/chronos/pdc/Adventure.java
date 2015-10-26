/**
 * Adventure.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc;

import java.io.File;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import chronos.Chronos;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.TownRegistry;

/**
 * Singleton containing a reference to a Town and an Arena, two required elements of all Adventures.
 * 
 * @author Alan Cline
 * @version June 8, 2013 // original <br>
 */
public class Adventure implements IRegistryElement
{
  /** Name of this Adventure */
  private String _name;
  /** Overview of the adventure */
  private String _overview;
  /** Name of the Town for this Adventure */
  private String _townName;
  /** Name of the Arena for this Adventure */
  private String _arenaName;


  // ============================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ============================================================

  /** Create the default Adventure */
  public Adventure()
  {}



  /**
   * Creates the default {@code Adventure}. Only the names of the elements are included after
   * verifying that they are in their respective registries. The {@code Arena} will be verified to
   * be in its own {@code .dgn} file (not a Registry).
   * 
   * @param advName this adventure
   * @param townName the primary town in the adventure
   * @param arenaName arena in the adventure
   * @param overview description and background story of town, arena, and situation
   * @throws throws ApplicationException if any of the parms are null, or the Town cannot be found
   *         in the {@code TownRegistry}
   */
  public Adventure(String advName, String townName, String arenaName, String overview)
      throws ApplicationException
  {
    // Guard against an empty key
    if ((advName == null) || (townName == null) || (arenaName == null) || (overview == null)) {
      throw new ApplicationException("Adventure cannot have null parms");
    }

    // Set the parms
    else {
      _name = advName;
      _townName = townName;
      _arenaName = arenaName;
      _overview = overview;
    }
  }

  // ============================================================
  // PUBLIC METHODS
  // ============================================================

  // /** Close an Adventure */
  // public void close()
  // {
  // _open = false;
  // }


  // /** Dump the Adventure internals */
  // public void dump()
  // {
  // System.out.println("\tAdventure " + getKey());
  // System.out.println("\thas Town " + _townName);
  // System.out.println("\thas Arena " + _arenaName);
  // System.out.println("\tOverview: \t" + getOverview());
  // }
  
  /**
   * Two Adventures are equal if all the adventure name, Town name, and Arena name are equal
   * 
   * @param otherThing the Skill to be considered
   * @return true if all elements are equal
   */
  @Override
  public boolean equals(IRegistryElement otherThing)
  {
    // Guard against null input
    if (otherThing == null) {
      return false;
    }
    Adventure adv = (Adventure) otherThing;
    boolean bName = _name.equals(adv.getName());
    boolean bTown = _townName.equals(adv.getTownName());
    boolean bArena = _arenaName.equals(adv.getArenaName());
    return (bName && bTown && bArena);
  }


  /**
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
  }

  /**
   * @return the name of the adventure
   */
  public String getName()
  {
    return _name;
  }


  /**
   * @return the name of the adventure's town
   */
  public String getTownName()
  {
    return _townName;
  }

  /**
   * @return the name of the adventure's arena
   */
  public String getArenaName()
  {
    return _arenaName;
  }


  /**
   * @return the introductory description of the arena
   */
  public String getOverview()
  {
    return _overview;
  }


  /**
   * If the Adventure has an Arena, get it from the arena folder
   * 
   * @return the Arena object or null
   */
  public Arena getArena()
  {
    return (_arenaName == null) ? null : Arena.getInstance(_arenaName);
  }



  // /** Return true if the Adventure is open */
  // public boolean isOpen()
  // {
  // return _open;
  // }
  //
  //
  // /** Open an Adventure */
  // public void open()
  // {
  // _open = true;
  // }


  /** @return the display name of the Adventure */
  public String toString()
  {
    return _name;
  }

  // ============================================================
  // PUBLIC METHODS
  // ============================================================

  // ============================================================
  // PRIVATE METHODS
  // ============================================================

  // ============================================================
  // INNER CLASS MockAdventure
  // ============================================================

  /** Inner class for testing Person */
  public class MockAdventure
  {
    /** Default constructor */
    public MockAdventure()
    {}

    /** Get all Adventure parms */
    public String[] getParms()
    {
      String[] parms = new String[4];
      parms[0] = _name;
      parms[1] = _townName;
      parms[2] = _arenaName;
      parms[3] = _overview;
      return parms;
    }
  }


} // end of Adventure class
