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
  protected DbReadWriter<E> _regRW;
  protected boolean _shouldInitialize;

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
    init(filename);
  }


  protected void init(String filename)
  {
    // Creates registry file and reloads it (new registry will be empty)
    _regRW = new DbReadWriter<E>(filename);

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

    // Ensure that a null or an empty key is not being added
    if ((obj == null) || (obj.getKey().trim().length() == 0)) {
      return retval;
    }
    // Ensure that only unique objects are added
    if (contains(obj) == false) {
      _regRW.addElement(obj);
      retval = true;
    } else {
      System.out.println("Tried to add " + obj.getKey() + " to registry, but failed\n");
    }

    return retval;
  }

  public boolean forceAdd(E obj)
  {
    delete(obj);
    return add(obj);
  }

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
  }

  /**
   * Retrieve one or more objects by name. The object's {@code getKey()} method is called.
   * 
   * @param name of the target object to match against for comparison
   * @return the list of all elements that match the name
   */
  public E get(String name)
  {
    E element = _regRW.get(name);
    return element;
  }

  /**
   * Gets all the elements of the Registry
   * 
   * @return one or more registry elements that match the Predicate, else returns null.
   */
  public List<E> getAll()
  {
    return _regRW.getAll();
  }
  
	/**
	 * Retrieve element names in the particular Registry that match the type of
	 * the object passed in. Uses the getKey() method of IRegistryElement, and
	 * assumes the key is the object's name field.
	 * 
	 * @return the list of names
	 */
	public List<String> getNamesByType(E elem) {
		return _regRW.getAllNames(elem);
//		List<String> names = new ArrayList<String>();
//		for (IRegistryElement elem : getAll()) {
//			if (elem.getClass().equals(obj.getClass())) {
//				names.add(elem.getKey());
////				System.out.println(elem.getClass().getName().toString() +
////						": " + elem.getKey().toString());
//			}
//			
//		}
//		return names;
	}
	
	/**
	 * Retrieve elements in the particular Registry that match the type of
	 * the object passed in.
	 * 
	 * @return list of elements
	 */
	public List<Object> getElementsByType(Object obj) {
		List<Object> selectedElements = new ArrayList<Object>();
		for (IRegistryElement elem : getAll()) {
			if (elem.getClass().equals(obj.getClass())) {
				selectedElements.add(elem);
//				System.out.println(elem.getClass().getName().toString() +
//						": " + elem.getKey().toString());
			}
			
		}
		return selectedElements;
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
    E obj = _regRW.containsElement(target);
    if (obj != null) {
      // Retrieve the target element and overwrite it
      _regRW.deleteElement(target);
      _regRW.addElement(target);
      retval = true;
    }
    return retval;
  }
} // end of Registry base class


