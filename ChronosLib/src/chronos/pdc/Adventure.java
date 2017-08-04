/**
 * Adventure.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc;

import mylib.dmc.IRegistryElement;

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

  /**
   * Creates the default {@code Adventure}. Only the names of the elements are included after
   * verifying that they are in their respective registries. The {@code Arena} will be verified to
   * be in its own {@code .dgn} file (not a Registry).
   * 
   * @param advName this adventure
   * @param townName the primary town in the adventure
   * @param arenaName arena in the adventure
   * @param overview description and background story of town, arena, and situation
   * @throws NullPointerException if any of the parms are null, or the Town cannot be found in the
   *         {@code TownRegistry}
   */
  public Adventure(String advName, String townName, String arenaName, String overview)
      throws NullPointerException
  {
    // Guard against an empty key
    if ((advName == null) || (townName == null) || (arenaName == null) || (overview == null)) {
      throw new NullPointerException("Adventure cannot have null parms");
    }
    // Set the parms
    _name = advName;
    _townName = townName;
    _arenaName = arenaName;
    _overview = overview;
  }

  // ============================================================
  // PUBLIC METHODS
  // ============================================================

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Adventure other = (Adventure) obj;
    if (!_arenaName.equals(other._arenaName))
      return false;
    if (!_name.equals(other._name))
      return false;
    if (!_townName.equals(other._townName))
      return false;
    return true;
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
   * @return the introductory description of the arena
   */
  public String getOverview()
  {
    return _overview;
  }


  /**
   * @return the name of the adventure's town
   */
  public String getTownName()
  {
    return _townName;
  }

  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_arenaName == null) ? 0 : _arenaName.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_townName == null) ? 0 : _townName.hashCode());
    return result;
  }

  /** @return the display name of the Adventure */
  @Override
  public String toString()
  {
    return _name;
  }
  
  
} // end of Adventure class
