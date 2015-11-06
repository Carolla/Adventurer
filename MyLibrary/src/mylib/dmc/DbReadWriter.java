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

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.query.Predicate;

/**
 * Creates and opens the db4o database for object persistence. The database can be set for
 * read-write or read-only mode after it is opened. Handles all object persistence operations and
 * database management. If the database is closed, it will be nulled out, but the
 * DBRegistryReadWriter will still exist.
 * 
 * @author Alan Cline
 * @version Aug 12, 2012 // original <br>
 *          Dec 16, 2012 // updated with more robust testing <br>
 *          Feb 25 2013 // replaced queryByExample with native queries <br>
 *          Mar 18, 2013 // revised after adding IRegistryElement <br>
 *          Dec 7, 2013 // changed dbOpen signature <br>
 */
public class DbReadWriter<E extends IRegistryElement>
{
  /** The path of the database file */
  private final String _regPath;

  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Creates the read writer for a particular registry and opens the database
   * 
   * @param filepath absolute path name for the file associated with the db
   */
  public DbReadWriter(String filepath)
  {
    if (filepath == null) {
      throw new NullPointerException();
    }

    _regPath = filepath;
  }

  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * Add a new object into the database. All abnormal cases that cause exceptions to be thrown must
   * be handled by the caller
   * 
   * @param obj object to add
   */
  public void addElement(IRegistryElement obj)
  {
    deleteElement(obj);
    if (obj == null) {
      throw new NullPointerException("Object added was null");
    } else {
      ObjectContainer objectContainer = open(_regPath);
      try {
        objectContainer.store(obj);
        objectContainer.commit();
      } finally {
        objectContainer.close();
      }
    }
  }

  /**
   * Deletes all elements in the registry. This method is only used for testing.
   * <p>
   * WARNING: This method is for testing only. An application should never have a need to clear the
   * database, file, and DbReadWriter.
   */
  @SuppressWarnings("serial")
  public void dbClear()
  {
    ObjectContainer objectContanier = open(_regPath);

    ObjectSet<IRegistryElement> obSet =
        objectContanier.query(new Predicate<IRegistryElement>() {
          public boolean match(IRegistryElement candidate)
          {
            return true;
          }
        });

    try {
      for (IRegistryElement elem : obSet) {
        objectContanier.delete(elem);
      }
    } finally {
      objectContanier.close();
    }
  }

  /**
   * Verify if a particular object exists, found by calling that object's {@code equals} method
   * 
   * @param target name of the object with specific fields to find
   * @return true if it exists in the db, else false
   * 
   */
  @SuppressWarnings("serial")
  public boolean containsElement(final IRegistryElement target)
  {
    ObjectContainer objectContanier = open(_regPath);

    // Run the query using the equals method
    List<IRegistryElement> obSet = objectContanier.query(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return target.equals(candidate);
      }
    });

    boolean contains = obSet.size() > 0;
    objectContanier.close();
    return contains;
  }

  /**
   * Delete an object from the database. The object must be retrieved before being deleted. The
   * caller must handle exceptions if the database is closed or Read-Only.
   * 
   * @param target object to delete
   * @return true if delete was successful, else false
   */
  @SuppressWarnings("serial")
  public boolean deleteElement(final IRegistryElement target)
  {
    // Guards: Illegal to delete via null: entire database content would be
    // deleted
    if (target == null) {
      return true;
    }

    ObjectContainer objectContanier = open(_regPath);
    ObjectSet<IRegistryElement> obSet =
        objectContanier.query(new Predicate<IRegistryElement>() {
          public boolean match(IRegistryElement candidate)
          {
            return candidate.equals(target);
          }
        });

    boolean retval = false;
    try {
      if (obSet.size() != 0) {
        IRegistryElement found = obSet.next(); // get first object in result
                                               // set
        objectContanier.delete(found);
        objectContanier.commit();
        retval = true;
      }
    } finally {
      objectContanier.close();
    }
    // ...else if object was not found, there is nothing to do
    return retval;
  }

  /**
   * Get the physical file location of the registry
   * 
   * @return absolute path name
   */
  public String getPath()
  {
    return _regPath;
  }

  /**
   * Create a new db only if it doesn't exist; else db4o will throw an exception. Create the object
   * container for transaction processing with the default configuration. Javadoc Tutorial says, "If
   * the file with this name already exists, it will be opened as db4o database, otherwise a new
   * db4o database will be created."
   * <P>
   * NOTE: The folder structure must exist before a db file within it can be created. db4o will not
   * create folders: db4o will throw an enigmatic System IO error.
   */
  public EmbeddedObjectContainer open(String filepath)
  {
    EmbeddedObjectContainer container = null;
    try {
      container = Db4oEmbedded.openFile(filepath);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
    return container;
  }

  /**
   * Gets one or more of elements that match the predicate provided.
   * 
   * @param pred predicate objet containing the comparison function to match for retrieval
   * @return the list of elements that match the predicate provided; else returns null.
   */
  public List<E> query(Predicate<E> pred)
  {

    List<E> elementList = new ArrayList<E>();
    // Guards: db and predicate must exist
    if (pred == null) {
      return elementList;
    }

    ObjectContainer objectContanier = open(_regPath);
    // Use predicate to call match() method to select element
    try {
      elementList.addAll(objectContanier.query(pred));
      return elementList;
    } finally {
      objectContanier.close();
    }
  }

  /**
   * Set the database to ReadOnly (true) or ReadWrite (false), depending on the parm. Reset the
   * current configuration as indicated, close the database, and open it with the new requested
   * configuration.
   * 
   * @param roFlag true for readOnly, false for the default ReadWrite
   */
  public void setReadOnly(boolean roFlag)
  {
    ExtObjectContainer objectContanier = (ExtObjectContainer) open(_regPath);

    // Get the current configuration; needs Extended services for this
    Configuration config = objectContanier.configure();
    // Set the configuration to the desired state
    config.readOnly(roFlag);

    objectContanier.close();
  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

  // ================================================================================
  // Inner Class: MockDBRW
  // ================================================================================

  /** Finds all elements in the given Registry ReadWriter */
  @SuppressWarnings("serial")
  public int size()
  {
    ObjectContainer objectContanier = open(_regPath);
    ObjectSet<IRegistryElement> obSet =
        objectContanier.query(new Predicate<IRegistryElement>() {
          public boolean match(IRegistryElement candidate)
          {
            return true;
          }
        });
    int size = obSet.size();

    objectContanier.close();
    return size;
  }

} // end of RegistryReadWriter class

