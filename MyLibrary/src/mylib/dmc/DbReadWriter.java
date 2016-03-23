/**
 * DbReadWriter.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.dmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Handles all object persistence operations and database management with a db4o
 * database, and encapsulates all operations for the Registries.
 * <P>
 * Implementation Note: db4o queries always return a {@code List<?>} of some
 * kind instead of retrieving individual elements. {@code DbReadWriter} requires
 * all contained elements (objects) to implement the {@code IRegistryElement}
 * interface. Therefore, the private helper method {@code getAllList()} is
 * usually called before any other action is taken, and the in-memory
 * {@code List<IRegistryElement>} is used.
 * <P>
 * WARNING: Do not call the {@code ObjectContainer}'s {@code open()} and
 * {@code close()} methods directly; use this class's {@code open()} and
 * {@code close()} methods.
 * 
 * @author Alan Cline
 * @version Aug 12, 2012 // original <br>
 *          Dec 16, 2012 // updated with more robust testing <br>
 *          Feb 25 2013 // replaced queryByExample with native queries <br>
 *          Mar 18, 2013 // revised after adding IRegistryElement <br>
 *          Dec 7, 2013 // changed dbOpen signature <br>
 *          Dec 23, 2015 // refactored for better encapsulation <br>
 */
public class DbReadWriter<E extends IRegistryElement> {

	private Set<E> _elements;

	// ================================================================================
	// CONSTRUCTOR(S) AND RELATED METHODS
	// ================================================================================

	/**
	 * Creates the read writer for a particular registry and opens the database
	 */
	public DbReadWriter(String filepath) {
		_elements = new TreeSet<E>();
	}

	// ================================================================================
	// PUBLIC METHODS
	// ================================================================================

	/**
	 * Add a new object into the database and guarantee it is unique.
	 * 
	 * @param obj
	 *            object to add
	 */
	public void addElement(E obj) {
		if (obj == null) {
			throw new NullPointerException("Cannot add null Object");
		}

		_elements.add(obj);
	}

	/**
	 * Verify if a particular object exists, found by calling that object's
	 * {@code equals} method
	 * 
	 * @param target
	 *            name of the object with specific fields to find
	 * @return the object found, else null
	 */
	public boolean containsElement(final E target) {
		return _elements.contains(target);
	}

	/**
	 * Delete an object from the database. The object must be retrieved before
	 * being deleted. The caller must handle exceptions if the database is
	 * closed or Read-Only.
	 * 
	 * @param target
	 *            object to delete
	 * @return true if delete was successful, else false
	 */
	public void deleteElement(E target) {
		_elements.remove(target);
	}

	public List<E> getAll() {
		List<E> list = new ArrayList<E>();
		list.addAll(_elements);
		return list;
	}

	/**
	 * Retrieve the first element that matches the name. The object's
	 * {@code getKey} method is called.
	 * 
	 * @param name
	 *            key of the target object to match against for comparison
	 * @return the object with matching name; else null if not found
	 */
	public E get(String name) {
		for (E item : _elements) {
			if (item.getKey().equals(name)) {
				return item;
			}
		}
		return null;
	}

	/** Finds all elements in the given Registry ReadWriter */
	public int size() {
		return _elements.size();
	}
} // end of RegistryReadWriter class

