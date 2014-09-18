/**
 * PersonRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import pdc.character.Person;
import chronos.Chronos;

import com.db4o.query.Predicate;

/**
 * Contains all Persons in the game. {@code PersonRegistry} is a singleton and is only
 * initialized once.
 * 
 * @author Tim Armstrong
 * @version Mar 13, 2013 // original <br>
 */
public class PersonRegistry extends Registry
{


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Init this Person Registry
   * 
   * @throws IOException if the Registry is not on the disk
   */
  protected PersonRegistry() throws IOException
  {
    super(Chronos.PersonRegPath);
  }


  /**
   * Create the Person Registry with the tables given (none), converting each element to a Person
   * object and saving it in the database.
   */
  @Override
  public void initialize()
  {
    MsgCtrl.msgln("PersonRegistry.initialize() was called.");
  }


  /*
   * PUBLIC METHODS
   */

  /**
   * Retrieves the Person with the requested unique name
   * 
   * @param name name of the Person to retrieve
   * @return the Person object; or null if not unique
   * @throws ApplicationException if trying to retrieve non-unique object
   */
  public Person getPerson(final String name)
  {
    // ExtObjectContainer db = _regRW.getDB();
    // Retrieve all skills that match the skillname; should be only one
    // List<Person> list = db.query(new PersonPredicate(name));

    List<IRegistryElement> elist = get(name);

    // Ensure uniqueness
    if (elist.size() == 1) {
      return (Person) elist.get(0);
    } else {
      return null;
    }
  }

  /**
   * Retrieve multiple Persons
   * 
   * @param target the object to find in the database
   * @return the Person List
   */
  public ArrayList<Person> getPersonList(Person target)
  {
    List<IRegistryElement> PersonSet = getAll();
    ArrayList<Person> personList = new ArrayList<Person>(PersonSet.size());
    for (Object o : PersonSet) {
      personList.add((Person) o);
    }
    return personList;
  }


  /*
   * PRIVATE METHODS
   */


  @SuppressWarnings("serial")
  private final class PersonPredicate extends Predicate<Person>
  {
    private final String name;

    private PersonPredicate(String name)
    {
      this.name = name;
    }

    public boolean match(Person candidate)
    {
      return candidate.getName().equals(name);
    }
  }

  // /** Load a table of Persons into the PersonRegistry
  // *
  // * @param table the initial Persons to load
  // * @return false if a problem occurs, else true
  // *@throw ApplicationException if the Person could not be added to the db
  // */
  // private void loadTable(String[][] table) throws ApplicationException
  // {
  // // Save the Persons required for the new Hero's inventory
  // for (int k = 0; k < table.length; k++) {
  // PersonCategory cat = PersonCategory.valueOf(table[k][0]);
  // String name = table[k][1];
  // int weight = Integer.valueOf(table[k][2]);
  // int qty = Integer.valueOf(table[k][3]);
  // Person Person = new Person(cat, name, weight, qty);
  // if (super.add(Person) == false) {
  // throw new ApplicationException("loadTable() error while adding to db " +
  // Person.getName());
  // }
  // }
  // }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockPersonRegistry for
   * Testing ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Inner class for testing Person */
  public class MockPersonRegistry
  {
    /** Default constructor */
    public MockPersonRegistry()
    {}
    //
    // /** Total number of objects expected to be stored in the db */
    // public long getOverhead()
    // {
    // return PersonRegistry.this.getOverhead();
    // }

  } // end of MockPersonRegistry inner class


  // /** Close db, destroy the dbReadWriter and set this registry to null
  // * @param eraseFile if true, erase registry file; else not
  // */
  // public void closeRegistry()
  // {
  // super.close();
  // // _thisReg = null;
  // }
  //
  // public void deleteRegistry()
  // {
  // super.delete();
  // // _thisReg = null;
  // }

} // end of PersonRegistry class

