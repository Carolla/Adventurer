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

import mylib.ApplicationException;
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
  private String _name = null;

  /** Description when the Patron is at a distance of about 20-50 ft. */
  private String _farDescription = null;

  /** Description when the Patron is up close and personal (less than 10'), and could include smells */
  private String _nearDescription = null;

  /** Affinity, a adjustment from -25% to +25% likely to befriend Hero. */
  private int _affinity = 0;

  /** If Peacekeeper flag is set, then patron will attack Hero in a bar brawl once. */
  private boolean _peacekeeper = false;

  /** Word or phrase about the profession of this NPC, used as note to the author */
  private String _note = null;

  /** List of helpful friendly responses to the Hero (rumors) */
  private ArrayList<String> _rumors = null;

  /** List of rebuffs and negative responses to the Hero (retorts) */
  private ArrayList<String> _retorts = null;

  /** Min range for affinity; each point represents 5% less chance of success */
  private final int MIN_AFFINITY = -5;
  /** Max range for affinity; each point represents 5% more less chance of success */
  private final int MAX_AFFINITY = 5;

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Default constructor */
  public NPC()
  {
    _name = "NPC";
    _note = "new";
  }

  /**
   * Normal contructor:
   * 
   * @param name of this Patron
   * @param affinity must be within range else throws exception
   * @param peaceflag true if Patron will fight in bar brawl
   * @param note profession of the NPC, as a note (unused in game)
   * @param farDesc appearance when far from Patron
   * @param nearDesc appearance when up close and personal, can include smells
   * @throws ApplicationException if null entries or invalid affinity value
   */
  public NPC(String name, String note, int affinity, boolean peaceFlag, String farDesc,
      String nearDesc)
      throws ApplicationException
  {
    // Guard conditions

    // if ((name == null) || (name.trim().length() == 0)) {
    // throw new ApplicationException(
    // "Patron(): Name cannot be null or empty");
    // }
    // if ((farDesc == null) || (farDesc.trim().length() == 0)) {
    // throw new ApplicationException(
    // "Patron(): Far Description cannot be null or empty");
    // }
    // if ((nearDesc == null) || (nearDesc.trim().length() == 0)) {
    // throw new ApplicationException(
    // "Patron(): Near Description cannot be null or empty");
    // }

    if ((affinity < MIN_AFFINITY) || (affinity > MAX_AFFINITY)) {
      throw new ApplicationException(
          "Patron(): Affinity value out of range; cannot be "
              + affinity);
    }
    _name = name;
    _farDescription = farDesc;
    _nearDescription = nearDesc;
    _affinity = affinity;
    _peacekeeper = peaceFlag;
    _note = note;
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
  public boolean equals(Object target)
  {
    NPC p = (NPC) target;
    boolean bName = _name.equals(p._name);
    boolean bFarDesc = _farDescription.equals(p._farDescription);
    boolean bNearDesc = _nearDescription.equals(p._nearDescription);
    boolean bfriend = (_affinity == p._affinity) && (_peacekeeper == p._peacekeeper);
    return (bName && bFarDesc && bNearDesc && bfriend);
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
   * Set the positive and negative messages into the Patron for conversation
   * 
   * @param posMsgs the list of helpful friendly responses to the Hero (rumors)
   * @param negMsgs the list of rebuffs and negative responses to the Hero (retorts)
   * @return true if lists are not null and not empty
   */
  public boolean setMessages(ArrayList<String> posMsgs,
      ArrayList<String> negMsgs)
  {
    if ((posMsgs == null) || (posMsgs.size() == 0)) {
      return false;
    }
    if ((negMsgs == null) || (negMsgs.size() == 0)) {
      return false;
    }
    _rumors = posMsgs;
    _retorts = negMsgs;
    return true;
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



  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ MockPatron INNER CLASS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockPatron
  {
    /** Mock contructor */
    public MockPatron()
    {}

    /**
     * Get the number of messages (rumors or retorts) this NPC has
     * 
     * @param pos true to get rumors, else gets retorts
     * @return the number of positive or negative messages
     */
    public int getNbrMessages(boolean pos)
    {
      return (pos == true) ? _rumors.size() : _retorts.size();
    }


    /**
     * Return positive or negative messages
     * 
     * @param pos if true, returns rumors; else retors
     * @param idx which of the messages to return
     * @return the requested message
     */
    public String getMsg(boolean pos, int idx)
    {
      return (pos == true) ? _rumors.get(idx) : _retorts.get(idx);
    }


    /** Return the positive messages */
    public ArrayList<String> getRumors()
    {
      return _rumors;
    }

    /** Return the negative messages */
    public ArrayList<String> getRetorts()
    {
      return _retorts;
    }

  } // end of MockPatron inner class


} // end of NPC class
