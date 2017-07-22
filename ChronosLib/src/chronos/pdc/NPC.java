/**
 * Patron.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */

package chronos.pdc;

import java.util.ArrayList;
import java.util.List;

import mylib.dmc.IRegistryElement;

/**
 * An NPC (non-player character) represents a person with which the Hero may interact. Typically,
 * NPCs are townspeople, 'Patrons' frequent the Inn of the town, 'BuildMasters' own or run one of
 * the town establishments. Any NPC may reveal information about the Arena to the Hero if friendly
 * enough, or rebuff the Hero if not. Each NPC has a description when seen from afar, and one when
 * seen closer up. Some NPC's will try to keep the peace if the Hero starts a brawl. By default,
 * each NPC has three positive messages (rumors) and three negative messages (retorts); some, like
 * the BuildingMasters, have more.
 * 
 * @author Alan Cline
 * @version Jan 21, 2013 // original <br>
 *          July 22, 2017 // minor cleanup per QATool <br>
 */
public class NPC implements IRegistryElement
{
  /** Displayed when NPC has given all his/her messages */
  public static final String NO_MORE_RESPONSES = "No more responses";

  /** Name of the Patron */
  private String _name;

  /** Description when the Patron is at a distance of 10-50 ft. */
  private String _farDescription;

  /** Description when the Patron is less than 10' away, and could include smells */
  private String _nearDescription;

  /** Affinity, an adjustment from -25% to +25% likely to befriend Hero. */
  private int _affinity;

  /** If true, indicates NPC will attack or resist Hero if Hero starts a brawl. */
  private boolean _peacekeeper;

  /** List of helpful friendly responses to the Hero (rumors) */
  private List<String> _rumors = new ArrayList<String>();

  /** List of rebuffs and negative responses to the Hero (retorts) */
  private List<String> _retorts = new ArrayList<String>();

  /** Min range for affinity; each point represents 5% less chance of success */
  private final int MIN_AFFINITY = -5;
  /** Max range for affinity; each point represents 5% more chance of success */
  private final int MAX_AFFINITY = 5;

  private int _rumorIndex = 0;
  private int _retortIndex = 0;


  // ========================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ========================================================================

  /**
   * Create a character who can converse with the Hero (or in a brawl, attack the Hero if the
   * peacekeeper flag is set). No parm can be empty or null; affinity is set to max (or min) value
   * if it is out of valid range. Each NPC must have at least three messages of both types: Patrons
   * typically have 3 each; BuildingMasters have at least 6 of each. <br>
   * Note: An NPC defaults to non-peacekeeper unless they are BuildingMasters or are set
   * deliberately.
   * 
   * @param name of this Patron
   * @param affine friendliness of NPC, must be within range [-5,+5]
   * @param farDesc appearance when far from Patron
   * @param nearDesc appearance when up close and personal, can include smells
   * @param replies a list of messages,perhaps helpful rumors, that are returned for favorable
   *        affinity checks
   * @param retorts a list of unhelpful messages that are returned for unfavorable affinity checks
   */
  public NPC(String name, int affine, String farDesc, String nearDesc, List<String> replies,
      List<String> retorts)
  {
    // Guard against null parms
    if ((name == null) || (farDesc == null) || (nearDesc == null)
        || (replies == null) || (retorts == null)) {
      throw new NullPointerException();
    }
    // Ensure that affinity stays within range
    if (affine < MIN_AFFINITY) {
      _affinity = MIN_AFFINITY;
    } else if (affine > MAX_AFFINITY) {
      _affinity = MAX_AFFINITY;
    } else {
      _affinity = affine;
    }
    _name = name;
    _farDescription = farDesc;
    _nearDescription = nearDesc;
    _rumors.addAll(replies);
    _retorts.addAll(retorts);
    _peacekeeper = false;
  }

  /**
   * Required primitive support: An NPC is equal to another if his name, peacekeeper status, and
   * descriptions are equal. Tested in order of most likely to be false for performance.
   * 
   * @return false if the {@code this} object and the parm object are not equal
   */
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

    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;

    if (_peacekeeper != other._peacekeeper)
      return false;

    if (_farDescription == null) {
      if (other._farDescription != null)
        return false;
    } else if (!_farDescription.equals(other._farDescription))
      return false;

    if (_nearDescription == null) {
      if (other._nearDescription != null)
        return false;
    } else if (!_nearDescription.equals(other._nearDescription))
      return false;
    return true;
  }

  /**
   * Key needed to retrieve an NPC from the {@code NPCRegistry}. The NPC's name is used as the key.
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

  /** An NPC is converted to his or her name */
  @Override
  public String toString()
  {
    return _name;
  }


  // ========================================================================
  // PUBLIC METHODS
  // ========================================================================

  public String getFarDescription()
  {
    return _farDescription;
  }

  public String getName()
  {
    return _name;
  }

  public String getNearDescription()
  {
    return _nearDescription;
  }


  public boolean isPeacekeeper()
  {
    return _peacekeeper;
  }

  /**
   * The peacekeeper status is rare enough for its own set method. BuildingMasters are always
   * peacekeepers by default, and special NPCs, but otherwise, the peacekeeper flag defaults to
   * false.
   * 
   * @param peaceFlag if true, sets NPC to peacekeeper
   */
  public void setPeacekeeper(boolean peaceFlag)
  {
    _peacekeeper = peaceFlag;
  }


  /**
   * Returns the next message in one of the NPC's rumor or retort message queues, depending on an
   * Hero's charisma + affinity check. (Charisma values swing from 3 to 20, so NPC will give a rumor
   * if sum of charisma and affinity greater than 9 (at least a 55% chance of rumor).
   * 
   * @param charisma modifying value to affinity; Hero's charisma trait
   * @return a retort or a rumor
   */
  public String talk(int charisma)
  {
    int POSITIVE = 10;
    int talkative = _affinity + charisma;
    String result = NO_MORE_RESPONSES;
    if (talkative >= POSITIVE) {
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


  // ========================================================================
  // MOCK INNER CLASS FOR TESTING
  // ========================================================================

  public class MockNPC
  {
    public MockNPC()
    {}

    public int getAffinity()
    {
      return _affinity;
    }

    public List<String> getRetorts()
    {
      return _retorts;
    }

    public List<String> getRumors()
    {
      return _rumors;
    }

  }


} // end of NPC class
