/**
 * Registry.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.pdc;

import java.util.ArrayList;
import java.util.List;

import mylib.dmc.DbReadWriter;
import mylib.dmc.IRegistryElement;

/**
 * The base class for all Registries, contains component {@code DbReadWriter}. All derived
 * registries will become persistent singleton containers for unique homogeneous objects. The
 * singleton will reload the file when created, or initialize from static tables containing default
 * data if the file doesn't exist.
 * <p>
 * All concrete classes derived from this abstract class works with a data management component
 * class {@code DbReadWriter} to handle the actual database read and write operations.
 * <p>
 * WARNING: All Registry elements must implement the interface {@code IRegistryElement}. Do not use
 * the default {@code boolean Object.equals} method because it compares objects independent of the
 * field data within (an instantiation level compare) and will not work as expected with db4o.
 * 
 * @author Alan Cline
 * @version Aug 6, 2012 // original <br>
 *          Sept 13, 2014 // removed need for closeflag <br>
 *          Sept 27, 2014 // removed ctor used only for testing <br>
 *          Dec 19 2015 // removed the <IRegistryElement> generic <br>
 *          Dec 25 2015 // refactored for the new DbReadWriter class <br>
 */
public abstract class Registry<E extends IRegistryElement>
{
  /**
   * The DMC registry class for handling persistence. Each derived-class {@code Registry} has its
   * own ReadWriter
   */
  protected DbReadWriter _regRW = null;
  // protected List<IRegistryElement> _inMemoryList = new ArrayList<IRegistryElement>();

  // /** Number of elements in the Registry collection */
  // private int _nbrElements = 0;

  protected boolean _shouldInitialize;

//  /** Warning message for a non-unique object found in the database */
//  static public final String DBREG_NOT_UNIQUE =
//      "Non-unique object found while trying to retrieve";

  /**
   * Initialize registry with beginning data from static tables, called when the registry file does
   * not exist. Method abstract because each derived registry has its own type-specific init data
   * table.
   */
  protected abstract void initialize();


  // ============================================================
  // CONSTRUCTOR AND RELATED METHODS
  // ============================================================

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
    _regRW = new DbReadWriter(filename);
    // // Set the persistence number of elements in the Registry
    // _nbrElements = getAll().size();

    if (getAll().size() == 0) {
      _shouldInitialize = true;
    }
  }



  // ============================================================
  // PUBLIC METHODS
  // ============================================================


  /**
   * Add a new unique element into the database. Object cannot be null, or have an empty (white
   * space only) key.
   * 
   * @param obj object to add to database
   * @return true if the add was successful, else false (as with duplicate attempts)
   */
  public boolean add(E obj)
  {
    boolean retval = false;

    // _inMemoryList.add(obj);
    // Ensure that a null or an empty key is not being added
    if ((obj == null) || (obj.getKey().trim().length() == 0)) {
      return retval;
    }
    // Ensure that only unique objects are added
    if (contains(obj) == false) {
      _regRW.addElement(obj);
      // _nbrElements++;
      retval = true;
    }
    return retval;
  }


  /** Close the given registry */
  public void closeRegistry()
  {}


  /**
   * Verifies if the given object exists in the registry. The object's equal() method is called.
   * 
   * @param target object to match against for comparison
   * @return true if the registry contains the element, else false
   */
  public boolean contains(E target)
  {
    return (_regRW.containsElement(target) != null);
  }

  /**
   * Delete an object from the registry
   * 
   * @param obj object to delete
   * @throws NullPointerException if the obj is null
   */
  public void delete(E obj) throws NullPointerException
  {
    _regRW.deleteElement(obj);
    // _nbrElements--;
  }

  // public void deleteAll()
  // {
  // _regRW.dbClear();
  // }

  /**
   * Retrieve one or more objects by name. The object's {@code getKey()} method is called.
   * 
   * @param name of the target object to match against for comparison
   * @return the list of all elements that match the name
   */
  @SuppressWarnings("serial")
  public Object get(String name)
  {
    Object elementList = _regRW.get(name);
    return elementList;
  }

  // /**
  // * Gets the requested object from the database using the predicate from the same element type as
  // * that being searched for.
  // *
  // * @param pred object containing the element's match() method for comparison
  // * @return one or more registry elements that match the Predicate, else returns null.
  // */
  // public List<IRegistryElement> get(Predicate<IRegistryElement> pred)
  // {
  // List<IRegistryElement> elementList = _regRW.query(pred);
  // return elementList;
  // }


  /**
   * Gets all the elements of the Registry
   * 
   * @return one or more registry elements that match the Predicate, else returns null.
   */
  public List<IRegistryElement> getAll()
  {
    return _regRW.getAll();
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
    // Convert the name of the element to a string
    for (int k = 0; k < elem.size(); k++) {
      String key = elem.get(k).getKey();
      names.add(k, key);
    }
    return names;
  }


  /**
   * Get the number of elements currently in the registry
   * 
   * @return then number of objects in the register
   */
  public int getNbrElements()
  {
    return _regRW.size();
  }



  // /**
  // * Retrieves a unique Registry element using that entry's equal() method
  // *
  // * @param name of a unique object in the registry to be retrieved; cannot be null
  // * @return the particular matching object; or null if not found or name was null
  // * @throws ApplicationException if more than one (non-unique) match was found
  // */
  // public E getUnique(String name) throws ApplicationException
  // {
  // // Guard
  // if ((name == null) || (name.trim().length() == 0)) {
  // return null;
  // }
  // E regElem = getFromMemory(name);
  // if (regElem != null) {
  // return regElem;
  // }
  //
  // List<IRegistryElement> elementList = get(name);
  // int nbrFound = elementList.size();
  // // If single element found, return it
  // if (nbrFound == 1) {
  // regElem = elementList.get(0);
  // }
  // // Registry should contain only unique elements. Throw exception is
  // // multiples are found
  // else if (nbrFound > 1) {
  // throw new ApplicationException(DBREG_NOT_UNIQUE);
  // }
  // // If no element found, return null
  // return regElem;
  // }

  // private E getFromMemory(String name)
  // {
  // for (E element : _inMemoryList) {
  // if (element.getKey().equalsIgnoreCase(name)) {
  // return element;
  // }
  // }
  // return null;
  // }


//  // TODO: This method should never be needed
//  /**
//   * Verifies if the string (key) is unique to the Registry Calls getUnique() to return a boolean
//   * instead of an object. The db4o doc promises not to store non-unique objects into db4o, and
//   * DbReadWriter ensures that is stored only unique objects, so this method should never be needed.
//   * 
//   * @param name to check for uniqueness
//   * @return true if name is unique in the registry, else false
//   */
//  public boolean isUnique(String name)
//  {
//    return (_regRW.get(name) == null) ? true : false;
//  }

  // TODO: Why is this needed?
//  public void setDbReadWriter(DbReadWriter regRW)
//  {
//    _regRW = regRW;
//  }


  /**
   * Update an existing object in the registry. The existing object must already be in the database,
   * and will be replaced with the first one it finds that matches it. The element's getKey() method
   * will be called, so the key field cannot be changed (updated). <br>
   * Note: db4o provides a field-replacement based update, but this is a delete-add method.
   * 
   * @param target replacement for modified object
   * @return false if the update failed becuase original was not found; else true
   */
  public boolean update(final E target)
  {
    boolean retval = false;
    // Guard against null replacements or missing elements
    if (target == null) {
      return false;
    }
    // Guard: if target is not in the registry, return immediately.
    IRegistryElement obj = (IRegistryElement) _regRW.containsElement(target);
    if (obj != null) {
      // Retrieve the target element and overwrite it
      _regRW.deleteElement(target);
      _regRW.addElement(target);
      retval = true;
    }
    return retval;
  }


} // end of Registry base class


