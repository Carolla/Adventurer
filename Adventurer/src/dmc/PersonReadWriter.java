/**
 * PersonReadWriter.java Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package dmc;

import java.util.ArrayList;

import mylib.MsgCtrl;
import pdc.character.Person;
import chronos.Chronos;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

/**
 * Handles Person serializations from Hero files.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Oct 5 2008 // original
 *          <DD>
 *          <DT>Build 1.1 Nov 27 2008 // removed intermediate class and inserted SAXStream directly
 *          <DD>
 *          <DT>Build 2.0 Feb 22 2009 // updated for Adventurer module and Person class
 *          <DD>
 *          <DT>Build 2.1 Mar 5 2009 // revised for serialization instead of XML file
 *          <DD>
 *          </DL>
 */
public class PersonReadWriter
{
  /** XML Tag names to be used by various objects */
  /** Person file start tag */
  static public final String PERSON_GROUP = "Person";
  /** Person name is an attribute tag */
  static public final String PERSON_ATTRIB = "name";
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
   * Receive Person PDC object so that this loader has back-reference to its owner.
   */
  public PersonReadWriter()
  {}

  /**
   * De-serialize a Person object and repopulate it. It will need to have the transient values
   * recalculated before it can be used. It is assumed to be stored in the resource directory with a
   * PERSON_EXT file extension.
   * 
   * @param personName of the Person object to deserialize
   * @return the Person read in, else null
   */
  public Person load(String personName)
  {
    if (personName == null) {
      MsgCtrl.errMsgln(this, "Null Person name given");
      return null;
    }

    Person p = null;

    // db4o code inserted here
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      // retrieveComplexSODA
      Query query = db.query();
      query.constrain(Person.class);
      // TODO: remove hard-coded reference to _name field
      query.descend("_name").constrain(personName);
      ObjectSet<Person> result = query.execute();
      if (result.size() > 0) {
        p = result.get(0);
      }
    } finally {
      db.close();
    }
    return p;
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

      // Restrict it to the Person class
      query.constrain(Person.class);
      ObjectSet<Person> result = query.execute();

      // Get all the names of the people in the database
      if (result.size() > 0) {
        for (Person p : result) {
          sleepers.add(p.getName());
        }
      }
    } finally {
      db.close();
    }
    return sleepers;
  }

  /**
   * Serialize the Person to a file, using his/her name. A PERSON_EXT suffix is added when saving to
   * the file system The Klass and Race will be stored with it as components. The Inventory for the
   * Person is saved with the Person.
   * 
   * @param p Person object to serialize
   * @param pName filename only; path will be expanded
   * @return false if the person exists, else return true for no errors
   */
  public boolean save(Person p, String pName)
  {
    // Guards against bad input
    if (p == null) {
      MsgCtrl.errMsgln(this, "There  is no Person to save. Null received");
      return false;
    }
    if ((pName == null) || (pName.trim().length() == 0)) {
      MsgCtrl.errMsgln(this, "This Person has no name!. Cannot save him or her.");
      return false;
    }

    // Check if the person exists
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.PersonRegPath);
    try {
      // Now retrieve that person from the database
      Query query = db.query();
      query.constrain(Person.class);
      // TODO: remove hard-coded reference to _name field
      query.descend("_name").constrain(pName);
      ObjectSet<Person> result = query.execute();

      // Return false, prompting for overwrite if person exists
      if (result.size() > 0) {
        return false;
      } else {
        db.store(p);
        MsgCtrl.msgln(this, "Saved " + pName + " to the Dormitory");
      }
    } finally {
      db.close();
    }
    return true;
  }

  /**
   * Serialize the Person to a file, using his/her name. A PERSON_EXT suffix is added when saving to
   * the file system The Klass and Race will be stored with it as components. The Inventory for the
   * Person is saved with the Person.
   * 
   * @param p Person object to serialize
   * @param personName filename only; path will be expanded
   * @return false if the save goes awry, else return true
   */
  public boolean overwrite(Person p, String pName)
  {
    // Guards against bad input
    if (p == null) {
      MsgCtrl.errMsgln(this, "There  is no Person to save. Null received");
      return false;
    }
    if ((pName == null) || (pName.trim().length() == 0)) {
      MsgCtrl.errMsgln(this, "This Person has no name!. Cannot save him or her.");
      return false;
    }

    // db4o code inserted here
    // accessDb4o
    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.REGISTRY_PATH
            + Chronos.PersonRegPath);
    try {
      this.delete(p);
      db.store(p);
      MsgCtrl.msgln(this, "Saved " + pName + " to the Dormitory");
    } finally {
      db.close();
    }
    return true;
  }

  public boolean delete(Person p)
  {
    // Guards against bad input
    if (p == null) {
      MsgCtrl.errMsgln(this, "There  is no Person to remove. Null received");
      return false;
    }

    ObjectContainer db =
        Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Chronos.REGISTRY_PATH
            + Chronos.PersonRegPath);
    try {
      String pName = p.getName();
      Person pToDel = null;

      // Now retrieve that person from the database
      Query query = db.query();
      query.constrain(Person.class);
      // TODO: remove hard-coded reference to _name field
      query.descend("_name").constrain(pName);
      ObjectSet<Person> result = query.execute();
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

} // end of PersonReadWriter class

