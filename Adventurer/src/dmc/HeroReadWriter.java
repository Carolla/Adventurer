/**
 * HeroReadWriter.java Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package dmc;

import java.util.ArrayList;

import mylib.MsgCtrl;
import chronos.Chronos;
import chronos.pdc.character.Hero;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

/**
 * Handles Hero serializations from Hero files.
 * 
 * @author Alan Cline
 * @version Build 1.0 Oct 5 2008 // original <br>
 *          Nov 27 2008 // removed intermediate class and inserted SAXStream directly <br>
 *          Feb 22 2009 // updated for Adventurer module and Hero class <br>
 *          Mar 5 2009 // revised for serialization instead of XML file <br>
 */
public class HeroReadWriter
{
  /** XML Tag names to be used by various objects */
  /** Hero file start tag */
  static public final String Hero_GROUP = "Hero";
  /** Hero name is an attribute tag */
  static public final String Hero_ATTRIB = "name";
  /** Gender element is a single line tag */
  static public final String GENDER_ELEMENT = "Gender";
  static public final String GENDER_ATTRIB = "gender";
  /** Occupation element is a single line tag */
  static public final String OCCUP_ELEMENT = "Occupation";
  static public final String OCCUP_ATTRIB = "occupation";
  /** Description element is a multi-line text block */
  static public final String DESC_ELEMENT = "Description";
  /** Race name is a single line tag that evokes a constructor */
  static public final String RACE_ELEMENT = "Race";
  static public final String RACENAME_ATTRIB = "racename";
  /** Klass name is a single line tag that evokes a constructor */
  static public final String KLASS_ELEMENT = "Klass";
  static public final String KLASSNAME_ATTRIB = "klassname";

  /**
   * Receive Hero PDC object so that this loader has back-reference to its owner.
   */
  public HeroReadWriter()
  {}

  public boolean delete(Hero p)
  {
    // Guards against bad input
    if (p == null) {
      MsgCtrl.errMsgln(this, "There  is no Hero to remove. Null received");
      return false;
    }

    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      String pName = p.getName();
      Hero pToDel = null;

      // Now retrieve that Hero from the database
      Query query = db.query();
      query.constrain(Hero.class);
      // TODO: remove hard-coded reference to _name field
      query.descend("_name").constrain(pName);
      ObjectSet<Hero> result = query.execute();
      if (result.size() > 0) {
        pToDel = result.get(0);
      }

      db.delete(pToDel);
      MsgCtrl.msgln(this, "Deleted " + p.getName() + " from the Dormitory");
    } finally {
      db.close();
    }
    return true;
  }

  public void dumpDB()
  {
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      // Make a new query
      Query query = db.query();

      // Restrict it to the Hero class
      query.constrain(Hero.class);
      ObjectSet<Hero> result = query.execute();

      // Get all the names of the people in the database
      System.out.println("Dormitory contains");
      if (result.size() > 0) {
        for (Hero p : result) {
          System.out.println("\t" + p.getName() + ", " + p.getOccupationName());
        }
      }
    } finally {
      db.close();
    }
  }

  /**
   * De-serialize a Hero object and repopulate it. It will need to have the transient values
   * recalculated before it can be used. It is assumed to be stored in the resource directory with a
   * Hero_EXT file extension.
   * 
   * @param HeroName of the Hero object to deserialize
   * @return the Hero read in, else null
   */
  public Hero load(String HeroName)
  {
    if (HeroName == null) {
      MsgCtrl.errMsgln(this, "Null Hero name given");
      return null;
    }

    Hero p = null;

    // db4o code inserted here
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      // retrieveComplexSODA
      Query query = db.query();
      query.constrain(Hero.class);
      // TODO: remove hard-coded reference to _name field
      query.descend("_name").constrain(HeroName);
      ObjectSet<Hero> result = query.execute();
      if (result.size() > 0) {
        p = result.get(0);
      }
    } finally {
      db.close();
    }
    return p;
  }

  /**
   * Serialize the Hero to a file, using his/her name. A Hero_EXT suffix is added when saving to the
   * file system The Klass and Race will be stored with it as components. The Inventory for the Hero
   * is saved with the Hero.
   * 
   * @param p Hero object to serialize
   * @param HeroName filename only; path will be expanded
   * @return false if the save goes awry, else return true
   */
  public boolean overwrite(Hero p, String pName)
  {
    // Guards against bad input
    if (p == null) {
      return false;
    }
    if ((pName == null) || (pName.trim().length() == 0)) {
      return false;
    }

    // Open db4o for transaction
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    // retrieveComplexSODA
    Query query = db.query();
    query.constrain(Hero.class);
    // TODO: remove hard-coded reference to _name field
    query.descend("_name").constrain(pName);
    ObjectSet<Hero> result = query.execute();
    if (result.size() > 0) {
      Hero oldHero = result.get(0);
      db.delete(oldHero);
    }
    try {
      db.store(p);
    } finally {
      db.close();
    }
    return true;
  }

  /**
   * Serialize the Hero to a file, using his/her name. A Hero_EXT suffix is added when saving to the
   * file system The Klass and Race will be stored with it as components. The Inventory for the Hero
   * is saved with the Hero.
   * 
   * @param p Hero object to serialize
   * @param pName filename only; path will be expanded
   * @return false if the Hero exists, else return true for no errors
   */
  public boolean save(Hero p, String pName)
  {
    // Guards against bad input
    if (p == null) {
      return false;
    }
    if ((pName == null) || (pName.trim().length() == 0)) {
      return false;
    }

    // Check if the Hero exists
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      // Now retrieve that Hero from the database
      Query query = db.query();
      query.constrain(Hero.class);
      // TODO: remove hard-coded reference to _name field
      query.descend("_name").constrain(pName);
      ObjectSet<Hero> result = query.execute();

      // Return false, prompting for overwrite if Hero exists
      if (result.size() > 0) {
        return false;
      } else {
        db.store(p);
      }
    } finally {
      db.close();
    }
    return true;
  }

  /**
   * Return a list of all the names of people saved in the dormitory
   * 
   * @return a list of the names of all the characters in the dormitory
   */
  public ArrayList<String> wakePeople()
  {
    ArrayList<String> sleepers = new ArrayList<String>();
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      // Make a new query
      Query query = db.query();

      // Restrict it to the Hero class
      query.constrain(Hero.class);
      ObjectSet<Hero> result = query.execute();

      // Get all the names of the people in the database
      if (result.size() > 0) {
        for (Hero p : result) {
          sleepers.add(p.getName());
        }
      }
    } finally {
      db.close();
    }
    return sleepers;
  }

} // end of HeroReadWriter class

