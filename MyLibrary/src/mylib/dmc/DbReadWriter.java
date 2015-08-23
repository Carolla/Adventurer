/**
 * DbReadWriter.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.dmc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.ext.Db4oIOException;
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
public class DbReadWriter
{
  /** The instance of the database in memory */
  private ExtObjectContainer _db;
  /** The path of the database file */
  private String _regPath = null;

  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Creates the read writer for a particular registry and opens the database
   * 
   * @param filepath absolute path name for the file associated with the db
   */
  public DbReadWriter(String filepath) throws NullPointerException
  {
      dbOpen(filepath);
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
  public void dbAdd(IRegistryElement obj) 
  {
    if (obj == null) {
        // Do not allow null objects to be stored
        return;
    } else {
      _db.store(obj);
      _db.commit();
    }
  }

  /**
   * Closes down the database but keeps its file
   */
  public void dbClose()
  {
      // Close the db file and remove the object container
      if (!_db.isClosed()) {
        _db.close();
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
public boolean dbContains(final IRegistryElement target)
  {
    // return _db.isStored(target); // this db4o call doesn't seem to work
    // Run the query using the equals method
    List<IRegistryElement> obSet = _db.query(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return target.equals(candidate);
      }
    });
    return (obSet.size() > 0) ? true : false;
  }

  /**
   * Delete an object from the database. The object must be retrieved before being deleted. The
   * caller must handle exceptions if the database is closed or Read-Only.
   * 
   * @param target object to delete
   * @return true if delete was successful, else false
   */
  @SuppressWarnings("serial")
  public boolean dbDelete(final IRegistryElement target)
  {
    // Guards: Illegal to delete via null: entire database content would be
    // deleted
    if (target == null) {
      return true;
    }

    boolean retval = false;
    ObjectSet<IRegistryElement> obSet = null;
      obSet = _db.query(new Predicate<IRegistryElement>() {
        public boolean match(IRegistryElement candidate)
        {
          return candidate.equals(target);
        }
      });
      
    // If object was found, delete it...
    if (obSet.size() != 0) {
      IRegistryElement found = obSet.next(); // get first object in result
                                             // set
      _db.delete(found);
      _db.commit();
      retval = true;
    }
    // ...else if object was not found, there is nothing to do
    return retval;
  }


  /**
   * Is the database closed to transactions?
   * 
   * @return true if db is closed
   */
  public boolean dbIsClosed()
  {
    return _db.isClosed();
  }

  /**
   * Create a new db only if it doesn't exist; else db4o will throw an exception. Create the object
   * container for transaction processing with the default configuration. Javadoc Tutorial says, "If
   * the file with this name already exists, it will be opened as db4o database, otherwise a new
   * db4o database will be created."
   * <P>
   * NOTE: The folder structure must exist before a db file within it can be created. db4o will not
   * create folders: db4o will throw an enigmatic System IO error.
   * 
   * @return enum error code (OK) if all worked, else some other error code
   */
  public void dbOpen(String filepath)
  {
      _db = (ExtObjectContainer) Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), filepath);
  }

  /**
   * Gets one or more of elements that match the predicate provided.
   * 
   * @param pred predicate objet containing the comparison function to match for retrieval
   * @return the list of elements that match the predicate provided; else returns null.
   */
  public List<IRegistryElement> dbQuery(Predicate<IRegistryElement> pred)
  {
    List<IRegistryElement> elementList = new ArrayList<IRegistryElement>();
    // Guards: db and predicate must exist
    // if (_db == null) {
    if (_db.isClosed() || pred == null) {
      return elementList;
    }
    
    // Use predicate to call match() method to select element
    elementList = _db.query(pred);
    return elementList;
  }

  /**
   * Set the database to ReadOnly (true) or ReadWrite (false), depending on the parm. Reset the
   * current configuration as indicated, close the database, and open it with the new requested
   * configuration.
   * 
   * @param roFlag true for readOnly, false for the default ReadWrite
   */
  public void dbReadOnly(boolean roFlag)
  {
    // Get the current configuration; needs Extended services for this
    Configuration config = _db.configure();
    // Set the configuration to the desired state
    config.readOnly(roFlag);
  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

  // ================================================================================
  // Inner Class: MockDBRW
  // ================================================================================

  /** Mock registry read writer to access private methods */
  public class MockDBRW
  {
    public MockDBRW()
    {}

    /**
     * Close down the database and delete its file
     * 
     * @throws Db4oIOException on a db4o-specific IO exception
     */
    public void dbDelete() throws Db4oIOException
    {
      dbClose();
      File regfile = new File(_regPath);
      regfile.delete();
    }


    /**
     * Deletes all elements in the registry. This method is only used for testing.
     * <p>
     * WARNING: This method is for testing only. An application should never have a need to clear
     * the database, file, and DbReadWriter.
     */
    @SuppressWarnings("serial")
    public void dbClear()
    {
      ObjectSet<IRegistryElement> obSet = _db.query(new Predicate<IRegistryElement>() {
        public boolean match(IRegistryElement candidate)
        {
          return true;
        }
      });
      for (IRegistryElement elem : obSet) {
        _db.delete(elem);
      }
    }


    /** Finds all elements in the given Registry ReadWriter */
    @SuppressWarnings("serial")
    public int dbSize()
    {
      ObjectSet<IRegistryElement> obSet = _db.query(new Predicate<IRegistryElement>() {
        public boolean match(IRegistryElement candidate)
        {
          return true;
        }
      });
      return obSet.size();
    }

    /** Returns returns the database container */
    public ExtObjectContainer getContainer()
    {
      return _db;
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

    /** Wraps outer method, returns an existing database container */
    public ObjectContainer openDB()
    {
      DbReadWriter.this.dbOpen(_regPath);
      return _db;
    }

    /** Set the container to the db field */
    public void setContainer(ExtObjectContainer oc)
    {
      _db = oc;
    }

  } // end of MockDbReadWriter inner class

} // end of RegistryReadWriter class

