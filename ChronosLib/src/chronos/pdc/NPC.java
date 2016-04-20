/**
 * Patron.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.util.ArrayList;
import java.util.List;

import mylib.dmc.IRegistryElement;

/**
 * A Patron frequents the Inn of the town. He or she will reveal information about the Arena to the
 * Hero if friendly enough, or rebuff the Hero if not. The Patron has a description when seen from
 * afar, and one when seen much closer up. Some Patrons will try to keep the peace if the Hero
 * starts a bar brawl. By default, each patron has three positive and negative mesages, and the
 * Buildmasters, like the Innkeeper, have more.
 * 
 * @author Alan Cline
 * @version Jan 21, 2013 // original <br>
 */
public class NPC implements IRegistryElement
{
  /** Name of the Patron */
  private String _name;

  /** Description when the Patron is at a distance of about 20-50 ft. */
  private String _farDescription;

  /**
   * Description when the Patron is up close and personal (less than 10'), and could include smells
   */
  private String _nearDescription;

  /** Affinity, a adjustment from -25% to +25% likely to befriend Hero. */
  private int _affinity = 0;

  /** If Peacekeeper flag is set, then patron will attack Hero in a bar brawl once. */
  private boolean _peacekeeper;

  /** Word or phrase about the profession of this NPC, used as note to the author */
  private String _note;

  /** List of helpful friendly responses to the Hero (rumors) */
  private List<String> _rumors = new ArrayList<String>();

  /** List of rebuffs and negative responses to the Hero (retorts) */
  private List<String> _retorts = new ArrayList<String>();

  /** Min range for affinity; each point represents 5% less chance of success */
  private final int MIN_AFFINITY = -5;
  /** Max range for affinity; each point represents 5% more less chance of success */
  private final int MAX_AFFINITY = 5;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public NPC()
  {
    _name = "NPC";
    _note = "new";
  }

  /**
   * Normal constructor: no parm can be empty or null except the note
   * 
   * @param name of this Patron
   * @param farDesc appearance when far from Patron; cannot be empty or null
   * @param nearDesc appearance when up close and personal, can include smells; cannot be empty or
   *        null
   * @param affinity must be within range [-5,+5]
   * @param peaceflag true if Patron will fight in bar brawl
   * @param note profession of the NPC, as a note (unused in game)
   */
  public NPC(String name, String note, int affinity, String farDesc, String nearDesc, List<String> replies, List<String> retorts) throws NullPointerException
  {
    // Guard conditions

    if ((name == null) || (name.trim().length() == 0)) {
      throw new NullPointerException("Patron(): Name cannot be null or empty");
    }
    if ((farDesc == null) || (farDesc.trim().length() == 0)) {
      throw new NullPointerException("Patron(): Far Description cannot be null or empty");
    }
    if ((nearDesc == null) || (nearDesc.trim().length() == 0)) {
      throw new NullPointerException("Patron(): Near Description cannot be null or empty");
    }
    
    if (affinity < MIN_AFFINITY) {
      affinity = MIN_AFFINITY;
    } else if (affinity > MAX_AFFINITY) {
      affinity = MAX_AFFINITY;
    }
    
    _name = name;
    _farDescription = farDesc;
    _nearDescription = nearDesc;
    _affinity = affinity;
    _note = note;
    _rumors.addAll(replies);
    _retorts.addAll(retorts);
  }

  public String getDescription()
  {
    return _farDescription;
  }

  public String getNearDescription()
  {
    return _nearDescription;
  }

  /*
   * PUBLIC METHODS
   */

  /*
   * NPCs are equals if they have the same name, near- and far-descriptions, affinity, and
   * peacekeeper status
   * 
   * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
   */
  @Override
  public boolean equals(IRegistryElement target)
  {
    NPC p = (NPC) target;
    boolean bName = _name.equals(p._name);
    boolean bFarDesc = _farDescription.equals(p._farDescription);
    boolean bNearDesc = _nearDescription.equals(p._nearDescription);
    boolean bfriend = (_affinity == p._affinity) && (_peacekeeper == p._peacekeeper);
    return (bName && bFarDesc && bNearDesc && bfriend);
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
    return equals((IRegistryElement) obj);
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_farDescription == null) ? 0 : _farDescription.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_nearDescription == null) ? 0 : _nearDescription.hashCode());
    result = prime * result + (_peacekeeper ? 1231 : 1237);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
  }

  /**
   * Getter for name
   * 
   * @return name
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Convert the NPC attributes to a string for display in library tree
   * 
   * @return single string for NPC
   */
  public String toString()
  {
    return _name + " (" + _note + ")";
  }
} // end of NPC class
