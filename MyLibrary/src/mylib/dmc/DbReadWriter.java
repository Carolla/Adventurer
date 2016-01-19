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
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Predicate;

/**
 * Handles all object persistence operations and database management with a db4o database, and
 * encapsulates all operations for the Registries.
 * <P>
 * Implementation Note: db4o queries always return a {@code List<?>} of some kind instead of
 * retrieving individual elements. {@code DbReadWriter} requires all contained elements (objects) to
 * implement the {@code IRegistryElement} interface. Therefore, the private helper method
 * {@code getAllList()} is usually called before any other action is taken, and the in-memory
 * {@code List<IRegistryElement>} is used.
 * <P>
 * WARNING: Do not call the {@code ObjectContainer}'s {@code open()} and {@code close()} methods
 * directly; use this class's {@code open()} and {@code close()} methods.
 * 
 * @author Alan Cline
 * @version Aug 12, 2012 // original <br>
 *          Dec 16, 2012 // updated with more robust testing <br>
 *          Feb 25 2013 // replaced queryByExample with native queries <br>
 *          Mar 18, 2013 // revised after adding IRegistryElement <br>
 *          Dec 7, 2013 // changed dbOpen signature <br>
 *          Dec 23, 2015 // refactored for better encapsulation <br>
 */
public class DbReadWriter
{
  /** The path of the database file */
  private final String _regPath;

  /** Db is open or closed */
  private boolean _open = false;

  /** actual database */
  EmbeddedObjectContainer _db;

  /** Exception message for null argument */
  static private final String NULL_ARG_MSG = "Argument cannot be null or empty";


  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Creates the read writer for a particular registry and opens the database
   * 
   * @param filepath absolute path name for the file associated with the db
   * @throws NullPointerException if the file path is null or empty
   */
  public DbReadWriter(String filepath) throws NullPointerException
  {
    if (!exists(filepath)) {
      throw new NullPointerException(NULL_ARG_MSG);
    }
    _regPath = filepath;
  }


  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * Add a new object into the database and guarantee it is unique.
   * 
   * @param obj object to add
   */
  public void addElement(IRegistryElement obj)
  {
    // Guard: null object not permitted
    if (obj == null) {
      throw new NullPointerException("Cannot add null Object");
    }
    _db = open();
    // Add element only if it is unique
    try {
      if (containsElement(obj) == null) {
        _db.store(obj);
        _db.commit();
      }
      // Catch exceptions thrown by store() or commit()
    } catch (DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
    close();
  }


  /**
   * Deletes all elements in the registry. Each item is removed from the database so db4o's OID can
   * be used to delete it.
   * <p>
   * WARNING: This method is for testing only. An application should never have a need to clear the
   * database, file, and DbReadWriter.
   */
  @SuppressWarnings("serial")
  public void dbClear()
  {
    List<IRegistryElement> alist = new ArrayList<IRegistryElement>();
    _db = open();
    try {
      alist = _db.query(new Predicate<IRegistryElement>() {
        public boolean match(IRegistryElement candidate)
        {
          return true;
        }
      });
      // There is no clear() for the object set in the db, so each must be removed individually
      for (IRegistryElement elem : alist) {
        _db.delete(elem);
      }
    } catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
    close();
  }


  /**
   * Verify if a particular object exists, found by calling that object's {@code equals} method
   * 
   * @param target name of the object with specific fields to find
   * @return the object found, else null
   */
  public Object containsElement(final IRegistryElement target)
  {
    List<IRegistryElement> alist = getAllList();
    // Search for target
    Object obj = null;
    for (IRegistryElement elem : alist) {
      if (elem.getKey().equals(target.getKey())) {
        obj = elem;
        break;
      }
    }
    return obj;
  }


  /**
   * Delete an object from the database. The object must be retrieved before being deleted. The
   * caller must handle exceptions if the database is closed or Read-Only.
   * 
   * @param target object to delete
   * @return true if delete was successful, else false
   */
  public void deleteElement(IRegistryElement target)
  {
    // Guards: Illegal to delete via null: entire database content would be deleted
    if (target == null) {
      return;
    }
    // Object must be retrieved before it can be deleted
    try {
      Object obj = containsElement(target);
      if (obj != null) {
        _db.delete(obj);
      }
    } catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
  }


  /**
   * Finds all elements in the given Registry
   * 
   * @return the list found
   */
  public List<IRegistryElement> getAll()
  {
    return getAllList();
  }


  /**
   * Retrieve the first element that matches the name. The object's {@code getKey} method is called.
   * 
   * @param name key of the target object to match against for comparison
   * @return the object with matching name; else null if not found
   */
  public Object get(String name)
  {
    // Guard: name must not be null or empty
    if (!exists(name)) {
      return null;
    }
    List<IRegistryElement> elementList = getAllList();
    IRegistryElement obj = null;
    for (int k = 0; k < elementList.size(); k++) {
      obj = elementList.get(k);
      if (obj.getKey().equals(name)) {
        break;
      }
    }
    // Return first element in list
    return obj;
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

  // TODO: Might be needed later, but not now
  // /**
  // * Set the database to ReadOnly (true) or ReadWrite (false), depending on the parm. Reset the
  // * current configuration as indicated, close the database, and open it with the new requested
  // * configuration.
  // *
  // * @param roFlag true for readOnly, false for the default ReadWrite
  // */
  // public void setReadOnly(boolean roFlag)
  // {
  // ExtObjectContainer objectContainer = (ExtObjectContainer) open(_regPath);
  //
  // // Get the current configuration; needs Extended services for this
  // Configuration config = objectContainer.configure();
  // // Set the configuration to the desired state
  // config.readOnly(roFlag);
  //
  // objectContainer.close();
  // }


  /** Finds all elements in the given Registry ReadWriter */
  public int size()
  {
    List<IRegistryElement> alist = getAllList();
    return alist.size();
  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

  /**
   * Close the open database (and resets the open/close flag).
   */
  private void close()
  {
    try {
      if (_open) {
        _db.close();
        _open = false;
      }
    } catch (Db4oIOException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
  }


  /**
   * Verify that argument is neither null nor empty (white space only)
   * 
   * @return true if arg contains data
   */
  private boolean exists(String arg)
  {
    return ((arg != null) && (arg.trim().length() > 0));
  }


  /**
   * Helper method to get all the elements in the registry. It opens the db and returns all
   * elements, leaving the db open so that the resulting List is valid. This method avoids
   * duplicating open/close code in other methods.
   * <P>
   * Warning: The List returned is an ObjectSetFacade, and is only available when the db is open.
   * Trying to use the List after the db is closed will throw a DatabaseClosedException.
   * 
   * @return the list for further action, leaving the db open
   */
  @SuppressWarnings("serial")
  private List<IRegistryElement> getAllList()
  {
    List<IRegistryElement> alist = new ArrayList<IRegistryElement>();
    _db = open();
    alist = _db.query(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return true;
      }
    });
    close();
    return alist;
  }


  /**
   * Create a new db only if it doesn't exist; else db4o will throw an exception. Create the object
   * container for transaction processing with the default configuration. db4o tutorial says, "If
   * the file with this name already exists, it will be opened as db4o database, otherwise a new
   * db4o database will be created."
   * <P>
   * NOTE: The folder structure must exist before a db file within it can be created. db4o will not
   * create folders: db4o will throw an enigmatic System IO error.
   */
  private EmbeddedObjectContainer open()
  {
    try {
      if (_open == false) {
//    	  EmbeddedConfiguration eCon = EmbeddedConfiguration.newConfiguration();
        _db = Db4oEmbedded.openFile( _regPath);
        _open = true;
      }
    } catch (Db4oIOException | DatabaseFileLockedException | IncompatibleFileFormatException
        | OldFormatException | DatabaseReadOnlyException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
    return _db;
  }


} // end of RegistryReadWriter class

