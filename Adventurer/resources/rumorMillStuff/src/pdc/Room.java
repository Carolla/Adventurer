/*
 * Room.java
 *
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package rumorMillStuff.src.pdc;

import rumorMillStuff.src.hic.Dgn;

import java.util.ArrayList;

/**
 * The <code>Room</code> is the basic unit of the <code>Inn</code> (dungeon object container). In
 * this case, there is only one <code>Room</code> (the "main" Room) in the <code>Inn</code>.
 * <p>
 * Each (the) <code>Room</code> contains a description, and a collection of <code>Persons</code>--
 * <code>Patron</code>s and the <code>Innkeeper</code>. The <code>Innkeeper</code> is always in the
 * main <code>Room </code> of the <code>Inn</code>.
 * 
 * @author Alan Cline
 * @version
 *          <DL>
 *          <DT>1.0 Aug 30 2006 // Original version
 *          <DD>
 *          <DT>2.0 Mar 25 2007 // Added serialization
 *          <DD>
 *          <DT>3.0 May 20 2007 // Moved to common package for DgnBuild and DgnRun
 *          <DD>
 *          <DT>3.1 May 26 2007 // Added non-default serialization for better flexibility in writing
 *          attributes separately, expecting to encrypt long text blocks to keep user from reading
 *          answers in the XML datafile.
 *          <DD>
 *          <DT>4.0 Sep 29 2007 // Revised for DgnBuild and DgnRunner merged program.
 *          <DD>
 *          <DT>4.1 Feb 16 2008 // Added Affinity methods
 *          <DD>
 *          <DT>4.2 Jul 3 2008 // Final commenting for Javadoc compliance
 *          <DD>
 *          </DL>
 */
public class Room
{
  /** Name of the <code>Room</code> */
  private String _rmName = null;

  /** Description of <code>Room</code> (for the Look command). */
  private String _rmDesc = null;

  /** <code>Room</code> counts start at 1. There is currently only 1 <code>Room</code> */
  private int _rmNbr = 1;

  /** This array may include the <code>Innkeeper</code>. */
  private ArrayList<Person> _persons; // init in constructor only

  /** Only one <code>Person</code> at a time can be busy in this Room. */
  private Person _busyBody = null;


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Default unused constructor */
  public Room()
  {}

  /**
   * Constructor for SAX parser's <code>DungeonContentLoader</code>.
   * 
   * @param name of the room
   * @see dmc.DungeonContentLoader
   */
  public Room(String name)
  {
    _rmName = name;
    _persons = new ArrayList<Person>();
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Add a <code>Person</code> into the <code>Room</code>. Called by SAX Parser.
   * 
   * @param npc <code>Person</code> to add into the <code>Room</code>; can even be the
   *        <code>Innkeeper</code>.
   */
  public void add(Person npc)
  {
    _persons.add(npc);
    System.out.println(npc.getName() + " has entered the room.");
  }


  /**
   * <code>Room</code> conntains all <code>Persons</code>, so it is responsible to loop through all
   * <code>Patrons</code> in the <code>Room</code>, and adjust their affinities by the amount given.
   * This method does not adjust the <code>Innkeeper</code>'s affinity; <code>Patron</code> affinity
   * only.
   * 
   * @param affin amount to adjust the affinity of each <code>person</code>; if affin is negative,
   *        <code>Patron</code>'s affinity is reduced
   */
  public void adjustAllAffinities(int affin)
  {
    for (int k = 0; k < _persons.size(); k++) {
      Person p = (Person) _persons.get(k);
      if (p instanceof Innkeeper) {
        continue; // do not increment the Innkeeper's affinity here
      } else {
        p.adjustAffinity(affin); // adjust only the Patrons in the room
      }
    }
  }


  /**
   * Checks if the <code>Innkeeper</code>, a special non-<code>Patron Person</code>, is in the
   * <code>Room</code>. He should always be (for now).
   * 
   * @return <code>Innkeeper</code> if found, else return null
   */
  public Innkeeper getInnkeeper()
  {
    // Describe the appearance of each Patron in the Inn.
    for (int k = 0; k < _persons.size(); k++) {
      Person p = (Person) _persons.get(k);
      if (p instanceof Innkeeper) {
        return (Innkeeper) p;
      }
    }
    // If Innkeeper is not found...
    return null;
  }


  /**
   * Gets the total number of <code>Patron</code>s in the <code>Room</code> (excluding
   * <code>Innkeeper</code>).
   * 
   * @return the number of <code>Patron</code>s
   */
  public int getNumberPatrons()
  {
    return _persons.size() - 1;
  }


  /**
   * Retrieves a <code>Person</code> from the <code>Room</code> by the <code>Patron</code>
   * (internal) number.
   * 
   * @param nbr of the <code>Patron</code>, between 0 and number of <code>Persons</code> in
   *        <code>Room</code> (including <code>Innkeeper</code>).
   * @return <code>Person</code> requested; if not found, returns null
   */
  public Person getPerson(int nbr)
  {
    Person p;
    Dgn.auditMsg("Checking for Person of specified number.");
    try {
      p = (Person) _persons.get(nbr);
    } catch (Exception e) {
      System.err.println("Illegal patron number specified.");
      return null;
    }
    return p;
  }


  /**
   * Gets a reference to a <code>Person</code> specified by his/her name; can be the
   * <code>innkeeper</code> if his name is given.
   * 
   * @param nameRequested of Person wanted
   * @return Person if found, else return null
   */
  public Person getPerson(String nameRequested)
  {
    // Search for the Person requested
    for (int k = 0; k < _persons.size(); k++) {
      Person p = (Person) _persons.get(k);
      if (p.getName().equalsIgnoreCase(nameRequested) == true) {
        return (Person) p;
      }
    }
    return null;
  }


  /**
   * Checks if a particular <code>Person</code> is busy or not.
   * 
   * @param p the <code>Person</code> being checked for busyness.
   * @return true if busy; else return false
   */
  public boolean isBusy(Person p)
  {
    return (p == _busyBody);
  }


  /**
   * Gives the <code>Room</code> description, and the far description of any <code>Person</code> in
   * the <code>Room</code>.
   */
  public void look()
  {
    System.out.println("\nYou are in Room " + _rmNbr + ": " + _rmName);
    System.out.println(_rmDesc);

    // Describe the appearance of each Patron in the Inn
    for (int k = 0; k < _persons.size(); k++) {
      Person p = (Person) _persons.get(k);
      System.out.println("(" + p.getName() + "): " + p.getFarDesc());
    }
  }


  /**
   * Removes a <code>Patron</code> from the <code>Room </code>. A <code>Patron</code> who is busy
   * talking will not be removed.
   * 
   * @param p the <code>Patron </code>to remove
   * @return false if the <code>Patron</code> does not exist, or cannot be removed.
   */
  public boolean remove(Patron p)
  {
    int index = _persons.indexOf(p);
    // Confirm that Patron is in the Room
    if (index < 0) {
      System.err.println("Patron " + p.getName() + " not found.");
      return false;
    }
    // Confirm that Patron is not busy, and eligible to leave
    if (_busyBody == p) {
      Dgn.auditMsg("Time for " + p.getName() + " to leave, but Patron is busy.");
      return false;
    }
    // If Patron is rebuking the Hero, increment the rebuke count
    if (p.hasRebuke() == true) {
      System.out.println(p.getName() + " stomps out of the building in annoyance.");
    }
    // If Patron is leaving normally, do not increment the rebuke count
    else {
      System.out.println(p.getName() + " has left the building.");
    }
    // In all cases, remove the Patron from those present in the Room
    _persons.remove(index);
    return true;
  }


  /**
   * Sets a busy flag to show who is engaged in some activity
   * 
   * @param person who is busy; only one can be busy at a time
   */
  public void setBusy(Person person)
  {
    _busyBody = person;
  }


  /**
   * Sets the XML Room description to this <code>Room</code>. Called by SAX Parser.
   * 
   * @param desc what the <code>Hero </code>sees when he LOOKs around the <code>Room</code>.
   */
  public void setDescription(String desc)
  {
    _rmDesc = desc;
  }


  // /**
  // * For DEBUGGING: Dump the attributes of the room: Name, Number, Description, and name of people
  // within.
  // */
  // public void dump()
  // {
  // // Currently, look() provides all the necessary data needed.
  // // If the Patrons' details are needed, dump the PatronRegistry
  // look();
  // Innkeeper ik = getInnkeeper();
  // ik.dump();
  //// p.dumpMsgs();
  // }


} // end of Room class

