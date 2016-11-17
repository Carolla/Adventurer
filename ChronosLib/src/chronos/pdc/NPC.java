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
  public static final String NO_MORE_RESPONSES = "No more responses";

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

  /** List of helpful friendly responses to the Hero (rumors) */
  private List<String> _rumors = new ArrayList<String>();

  /** List of rebuffs and negative responses to the Hero (retorts) */
  private List<String> _retorts = new ArrayList<String>();

  /** Min range for affinity; each point represents 5% less chance of success */
  private final int MIN_AFFINITY = -5;
  /** Max range for affinity; each point represents 5% more less chance of success */
  private final int MAX_AFFINITY = 5;

  private int _rumorIndex = 0;
  private int _retortIndex = 0;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Normal constructor: no parm can be empty or null except the note
   * 
   * @param name of this Patron
   * @param farDesc appearance when far from Patron; cannot be empty or null
   * @param nearDesc appearance when up close and personal, can include smells; cannot be empty or
   *          null
   * @param affinity must be within range [-5,+5]
   * @param peaceflag true if Patron will fight in bar brawl
   */
  public NPC(String name, int affinity, String farDesc, String nearDesc,
      List<String> replies, List<String> retorts) throws NullPointerException
  {
    if (affinity < MIN_AFFINITY) {
      affinity = MIN_AFFINITY;
    } else if (affinity > MAX_AFFINITY) {
      affinity = MAX_AFFINITY;
    }

    _name = name;
    _farDescription = farDesc;
    _nearDescription = nearDesc;
    _affinity = affinity;
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
   * (non-Javadoc)
   * 
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
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

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NPC other = (NPC) obj;
    if (_farDescription == null) {
      if (other._farDescription != null)
        return false;
    } else if (!_farDescription.equals(other._farDescription))
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_nearDescription == null) {
      if (other._nearDescription != null)
        return false;
    } else if (!_nearDescription.equals(other._nearDescription))
      return false;
    if (_peacekeeper != other._peacekeeper)
      return false;
    return true;
  }

  public String getName()
  {
    return _name;
  }

  public String toString()
  {
    return _name;
  }

  public String talk()
  {
    String result = NO_MORE_RESPONSES;
    if (_affinity > 0) {
      if (_rumorIndex < _rumors.size()) {
        result = _rumors.get(_rumorIndex);
        _rumorIndex++;
      }
    } else {
      if (_retortIndex < _retorts.size()) {
        result = _retorts.get(_retortIndex);
        _retortIndex++;
      }
    }

    return result;
  }
} // end of NPC class
