/**
 * Registry.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.pdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mylib.dmc.IRegistryElement;

/**
 * The base class for all Registries.
 * <p>
 * WARNING: All Registry elements must implement the interface {@code IRegistryElement}. Do not use
 * the default {@code boolean Object.equals} method because it compares objects independent of the
 * field data within (an instantiation level compare) and will not work as expected.
 * 
 * @author Alan Cline
 * @version Aug 6, 2012 // original <br>
 *          Sept 13, 2014 // removed need for closeflag <br>
 *          Sept 27, 2014 // removed ctor used only for testing <br>
 *          Dec 19 2015 // removed the <IRegistryElement> generic <br>
 *          Dec 25 2015 // refactored for the new DbReadWriter class <br>
 *          Mar 28 2016 // ABC refactored to in-memory List instead of dbReadWriter <br>
 *          July 30, 2017 // revised per QATool <br>
 *          Aug 1, 2017 // added HeroRegistry to cadre of Registry subclasses <br>
 */
public abstract class Registry<E extends IRegistryElement> implements Serializable
{
  // Required for serialization
  static final long serialVersionUID = 20170804530L;

  /** Registries are in-memory object structures except for HeroRegistry. */
  protected ArrayList<E> _list;

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
   * Base constructor for in-memory registries creates a read-only {@code Registry}. Required for
   * serialization.
   */
  public Registry()
  {
    _list = new ArrayList<E>();
    initialize();
  }


  /**
   * Creates a for persistent registry. Currently, {@code HeroRegistry} is the only one.
   * 
   * @param filename path filename designated the save file
   */
  public Registry(String filename)
  {
    _list = new ArrayList<E>();
    initialize();
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
    // Ensure that a null or an empty key is not being added
    if ((obj == null) || (obj.getKey().trim().length() == 0)) {
      return false;
    }
    // Add only entries unique to the list
    if (!contains(obj)) {
      return _list.add(obj);
    }
    return false;
  }


  /**
   * Verifies if the given object exists in the registry. The object's equal() method is called.
   * 
   * @param target object to match against for comparison
   * @return true if the registry contains the element, else false
   */
  public boolean contains(E target)
  {
    if (target == null)
      return false;

    for (E elem : _list) {
      if (elem.getKey().equals(target.getKey())) {
        return true;
      }
    }
    return false;
  }


  /**
   * Delete an object from the registry
   * 
   * @param obj object to delete
   * @throws NullPointerException if the obj is null
   */
  public void delete(E obj)
  {
    for (Iterator<E> it = _list.iterator(); it.hasNext();) {
      E elem = it.next();
      if (elem.getKey().equals(obj.getKey())) {
        it.remove();
      }
    }
  }


  /**
   * Retrieve one or more objects by key, which is more than the name. The object's {@code getKey()}
   * method is called.
   * 
   * @param key of the target object to match against for comparison
   * @return the element object that matches the name
   */
  // public E get(String name)
  public E get(String key)
  {
    for (E elem : _list) {
      if (elem.getKey().equalsIgnoreCase(key)) {
        return elem;
      }
    }
    return null;
  }


  /**
   * Gets all the elements of the Registry
   * 
   * @return one or more registry elements that match the Predicate, else returns null.
   */
  public List<E> getAll()
  {
    return _list;
  }

  /**
   * Retrieve elements in the particular Registry that match the type of the object passed in.
   * 
   * @return list of elements
   */
  public List<Object> getElementsByType(Object obj)
  {
    List<Object> selectedElements = new ArrayList<Object>();
    for (IRegistryElement elem : getAll()) {
      if (elem.getClass().equals(obj.getClass())) {
        selectedElements.add(elem);
      }
    }
    return selectedElements;
  }

  /**
   * Get the number of elements currently in the registry
   * 
   * @return then number of objects in the register
   */
  public int size()
  {
    return _list.size();
  }

  /**
   * Update an existing object in the registry. The existing object must already be in the database,
   * and will be replaced with the first one it finds that matches it. The element's getKey() method
   * will be called, so the key field cannot be changed (updated). <br>
   * Note: db4o provides a field-replacement based update, but this is a delete-add method.
   * 
   * @param target replacement for modified object
   * @return false if the update failed because original was not found; else true
   */
  public boolean update(final E target)
  {
    if (target == null) {
      return false;
    }

    if (contains(target)) {
      delete(target); // by name
    }
    add(target);
    return true;
  }


} // end of Registry base class

