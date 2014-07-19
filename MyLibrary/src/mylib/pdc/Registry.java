/**
 * Registry.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.pdc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.dmc.DbReadWriter;
import mylib.dmc.IRegistryElement;

import com.db4o.query.Predicate;

/**
 * The base class for all Registries, and contains a component to <code>DbReadWriter</code> All
 * derived registries wll become persistent singleton containers for unique homogeneous objects. The
 * singleton will reload the file when created, or intialize from static tables if the file doesn't
 * exist.
 * <p>
 * All concrete classes derived from this abstact class works with a data management component class
 * <code>DbReadWriter</code> to handle the actual database read and write operations.
 * <p>
 * All Registry elements must implement the interface <code>IRegistryElement</code>. Do not use the
 * default <code>boolean Object.equals()</code> method because it compares objects independent of
 * the field data within (an instantiation level compare) and will not work as expected with db4o.
 * 
 * @author Alan Cline
 * @version Aug 6, 2012 // original
 */
public abstract class Registry
{

  // TODO Remove the need for this flag
  /** Flag to indicate when the db is closed or not */
  protected boolean _isClosed = true;

  /**
   * Initialize registry with beginning data from static tables, called when the registry file does
   * not exist. Method abstract because each derived registry has its own type-specific init data
   * table.
   */
  protected abstract void initialize();

  /**
   * The DMC registry class for handling persistence. Each derived-class Registry has its own
   * ReadWriter
   */
  protected DbReadWriter _regRW = null;

  /** Number of elements in the Registry collection */
  private int _nbrElements = 0;

  /** Warning message for a non-unique object found in the database */
  static public final String DBREG_NOT_UNIQUE = "Non-unique object found while trying to retrieve";

  // public static final String TEST_MODE = "Allows construction of Registry without calling DB";

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Creates a Registry (read-write) and its DbReadWriter component. If the Registry exists, its
   * database is reloaded from the db file. if the Registry is created new, initializes the database
   * with starting hard-coded data in the sublcass. A new Registry can only be created and
   * initialized if the initFlag is true
   * 
   * @param filename relative path filename going to the db
   */
  public Registry(String filename)
  {
    // Creates registry file and reloads it (new registry will be empty)
    if (filename != null) {
      _regRW = new DbReadWriter(filename);
      _isClosed = false;
      // Set the persistence number of elements in the Registry
      _nbrElements = getAll().size();
      if (_nbrElements == 0) {
        initialize();
      }
    }
  }

  /**
   * Cannot be called by any class other than Registry subclass, allows for testing without touching
   * the database directly.
   */
  protected Registry()
  {}

  /**
   * Add a new unique element into the database. Object must not be null.
   * 
   * @param obj object to add to database
   * @return true if the add was successful, else false (as with duplicate attempts)
   */
  public boolean add(IRegistryElement obj)
  {
    boolean retval = false;
    // Ensure that a null isn't being added
    if (obj == null) {
      return retval;
    }
    // Add object only if it does not already exist
    if (contains(obj) == false) {
      _regRW.dbAdd(obj);
      _nbrElements++;
      retval = true;
    }
    return retval;
  }

  public boolean isClosed()
  {
    return _isClosed;
  }

  /** Close the registry and remove the reference */
  public void closeRegistry()
  {
    _regRW.dbClose();
    _isClosed = true;
  }

  /** Close the registry and delete its file */
  public void deleteRegistry()
  {
    _regRW.dbDelete();
    _isClosed = true;
  }

  /**
   * Verifies if the given objects exists in the registry. The object's equal() method is called.
   * 
   * @param target object to match against for comparison
   * @return true if the registry contains the element, else false
   */
  public boolean contains(final IRegistryElement target)
  {
    // Run the query using the equals method
    List<IRegistryElement> obSet = _regRW.dbQuery(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return target.equals(candidate);
      }
    });
    return (obSet.size() > 0) ? true : false;
  }

  /**
   * Delete an object from the registry
   * 
   * @param obj object to delete
   * @throws NullPointerException if the obj is null
   */
  public void delete(IRegistryElement obj) throws NullPointerException
  {
    // Reduce the nuber of elements only if the delete worked
    if (_regRW.dbDelete(obj) == true) {
      _nbrElements--;
    }
  }

  public void eraseDbFiles(String regPath)
  {
    File regfile = new File(regPath);
    regfile.delete();
    // _regRW = null;
  }

  /**
   * Retrieve one or more objects by name. The object's <code>getKey()</code> method is called.
   * 
   * @param name of the target object to match against for comparison
   * @return the list of all elements that match the name
   */
  public List<IRegistryElement> get(final String name)
  {
    // Run the query using the getKey method
    Predicate<IRegistryElement> pred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return candidate.getKey().equalsIgnoreCase(name);
      }
    };
    List<IRegistryElement> elementList = get(pred);
    return elementList;
  }

  /** @return the concrete type of registry */
  public Registry getReference()
  {
    // return _thisReg;
    return null;
  }

  /** @return the db read-writer object reference for this registry */
  public DbReadWriter getDBRW()
  {
    return _regRW;
  }

  /**
   * Gets the requested object from the database using the predicate from the same element type as
   * that being searched for.
   * 
   * @param pred object containing the element's match() method for comparison
   * @return one or more registry elements that match the Predicate, else returns null.
   */
  public List<IRegistryElement> get(Predicate<IRegistryElement> pred)
  {
    List<IRegistryElement> elementList = _regRW.dbQuery(pred);
    return elementList;
  }

  /**
   * Gets all the elements of the Registry
   * 
   * @return one or more registry elements that match the Predicate, else returns null.
   */
  protected List<IRegistryElement> getAll()
  {
    List<IRegistryElement> elementList = get(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return true;
      }
    });
    return elementList;
  }

  /**
   * Get the number of elements currently in the registry
   * 
   * @return then number of objects in the register
   */
  public int getNbrElements()
  {
    return _nbrElements;
  }

  /**
   * Retrieves a list of all names of the elements in the Registry
   * 
   * @return a list of all names of the elements in the Registry
   */
  public List<String> getElementNames()
  {
    List<IRegistryElement> elem = getAll();
    List<String> names = new ArrayList<String>(elem.size());
    // Convert the name of the town to a string
    for (IRegistryElement e : elem) {
      String key = e.getKey();
      names.add(key);
    }
    return names;
  }

  /**
   * Retrieves a unique Registry element using that entry's equal() method
   * 
   * @param name of a unique object in the registry to be retrieved; cannot be null
   * @return the particular matching object; or null if not found or name was null
   * @throws ApplicationException if more than one (non-unique) match was found
   */
  public IRegistryElement getUnique(final String name) throws ApplicationException
  {
    // Guard
    if ((name == null) || (name.trim().length() == 0)) {
      return null;
    }

    IRegistryElement regElem = null;
    List<IRegistryElement> elementList = get(name);
    int nbrFound = elementList.size();
    // If single element found, return it
    if (nbrFound == 1) {
      regElem = elementList.get(0);
    }
    // Registry should contain only unique elements. Throw exception is
    // multiples are found
    else if (nbrFound > 1) {
      throw new ApplicationException(DBREG_NOT_UNIQUE);
    }
    // If no element found, return null
    return regElem;
  }

  /**
   * Verifes if the string (key) is unique to the Registry Calls getUnique() to return a boolean
   * instead of an object
   * 
   * @name to check for uniqueness
   * @returns true if name is unique in the registry, else false
   */
  public boolean isUnique(String name)
  {
    boolean retval = false;
    // Return true if single element found
    try {
      retval = (getUnique(name) != null) ? true : false;
    } catch (ApplicationException ex) {
      // Return false for multiple elements found
      return false;
    }
    return retval;
  }

  /**
   * Retrieves a list of Registry element based on the Predicate from the caller.
   * 
   * @param pred the object that contains the boolean match() method for extraction
   * @return a list of all elements in which match() returns true
   * @throws NullPointerException if the target name is null or empty
   */
  public List<IRegistryElement> queryByPredicate(Predicate<IRegistryElement> pred)
      throws NullPointerException
  {
    // Guard
    if (pred == null) {
      throw new NullPointerException("Predicate cannot be null");
    }
    List<IRegistryElement> elementList = _regRW.dbQuery(pred);
    return elementList;
  }

  /**
   * Update an existing object in the registry. The existing object must already be in the database,
   * and will be replaced with the first one it finds that matches it. The element's getKey() method
   * will be called, so the key field cannot be changed (updated). <br>
   * Note: db4o provides a field-replacedment based update, but this is a delete-add method.
   * 
   * @param target replacement for modified object
   * @return false if the update failed becuase original was not found; else true
   */
  public boolean update(final IRegistryElement target) // throws
  // ApplicationException
  {
    boolean retval = false;
    // Guard against null replacements or missing elements
    if (target == null) {
      return false;
    }
    // Guard: if target is not in the registry, return immediately.
    if (contains(target) == true) {
      // Retrieve the target element and overwrite it
      _regRW.dbDelete(target);
      _regRW.dbAdd(target);
      retval = true;
    }
    return retval;
  }

  // // Create a predicate to match against the target's key
  // Predicate<IRegistryElement> pred = new Predicate<IRegistryElement>() {
  // public boolean match(IRegistryElement candidate)
  // {
  // return candidate.getKey().equals(target.getKey());
  // }
  // };
  //
  // // Run the query using the equals method
  // List<IRegistryElement> obSet = _regRW.dbQuery(pred);
  // boolean retval = false;
  // if (obSet.size() == 1) {
  // retval = add(target);
  // }
  // return retval;
  // }

} // end of Registry class
