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
import java.util.List;

import mylib.MsgCtrl;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.ObjectNotStorableException;
import com.db4o.ext.OldFormatException;
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
 */
public class DbReadWriter
{
  /** The instance of the database in memory */
  private ExtObjectContainer _db;
  /** The path of the database file */
  private String _regPath = null;

  /** Database file locked error */
  private final String DBERR_FILE_LOCKED = "Database file locked (already open)";
  /** DB Closed error */
  private final String DBERR_CLOSED = "Tried to read from a closed database";
  /** DB ReadOnly error */
  private final String DBERR_RO_DB = "Database in readOnly mode";
  /** Attempted to delete a null object from the db */
  private final String DBERR_NULL_PREDICATE = "dbQuery must have a non-null Predicate object";
  /** Attempted to delete a null object from the db */
  private final String DBERR_NULL_OBJECT = "Attempted to delete a null object from the db";
  /** Invalid filename error message */
  private final String DBERR_FILENAME = "Invalid filename given for Registry resources";
  /** Incompatible File format error */
  private final String DBERR_FILE_FORMAT = "Incompatible file format encountered";
  /** Old format File error */
  private final String DBERR_OLD_FORMAT = "Old file format encountered by db";

  /** Error code for db4o possibilities */
  static public enum DB_ERROR
  {
    OK, FILE_LOCKED, CLOSED, RO_DB, NULL_PREDICATE, NULL_OBJECT, FILENAME, DB4OIO, FILE_FORMAT, OLD_FORMAT
  }

  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Creates the read writer for a particular registry and opens the database
   * 
   * @param filepath absolute path name for the file associated with the db
   * @throws NullPointerException if the filename is null
   */
  public DbReadWriter(String filepath) throws NullPointerException
  {
    if (filepath == null) {
      throw new NullPointerException("DbReadWriter(): " + DBERR_FILENAME + " set to null");
    }
    _regPath = filepath;
    dbOpen();
  }


  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * Add a new object into the database. All abnormal cases that cause exceptions to be thrown must
   * be handled by the caller
   * 
   * @param obj object to add
   * @throws NullPointerException registry should not try to save a null
   * @throws DatabaseClosedException db needs to be in correct state
   * @throws DatabaseReadOnlyException DBregistry does not use RO state in QM
   * @throws ObjectNotStorableException strange objects should be screened by DBRegistry
   */
  public void dbAdd(IRegistryElement obj) throws NullPointerException,
      DatabaseClosedException, DatabaseReadOnlyException, ObjectNotStorableException
  {
    // Do not allow null objects to be stored
    if (obj == null) {
      throw new NullPointerException("DbReadWriter(): " + DBERR_FILE_FORMAT);
    }
    // // if (_db == null) {
    // if (_db.isClosed()) {
    // throw new DatabaseClosedException();
    // }
    else {
      _db.store(obj);
      _db.commit();
    }
  }


  /**
   * Closes down the database and its file, sets 
   * 
   * @throws Db4oIOException on a db4o-specific IO exception
   */
  public void dbClose() // throws Db4oIOException
  {
    try {
      // Close the db file and remove the object container
      if (!_db.isClosed()) {
        _db.close();
      }
    } catch (Db4oIOException ex) {
      MsgCtrl.where(this);
      System.err.println("Cannot close database " + ex.getMessage());
    }
  }


  /**
     * Verify if a particular object exists, found by calling that objects <code>equals()</code>
     * method
     * 
     * @param target name of the object with specific fields to find
     * @return true if it exists in the db, else false
     * 
     * @throws DatabaseClosedException trying to delete from a closed (null) db
     */
    public boolean dbContains(final IRegistryElement target) throws DatabaseClosedException
    {
  //    return _db.isStored(target);  // this db4o call doesn't seem to work
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


  


  // /**
  // * Verify if a particular object exists, found by calling that objects <code>equals()</code>
  // * method
  // *
  // * @param target name of the object with specific fields to find
  // * @return true if it exists in the db, else false
  // *
  // * @throws DatabaseClosedException trying to delete from a closed (null) db
  // */
  // @SuppressWarnings("serial")
  // public boolean dbContains(final IRegistryElement target) throws DatabaseClosedException
  // {
  // List<IRegistryElement> obSet = null;
  // // if (_db == null) {
  // // if (_db.isClosed()) {
  // // throw new DatabaseClosedException();
  // // }
  // try {
  // obSet = _db.query(new Predicate<IRegistryElement>() {
  // public boolean match(IRegistryElement candidate)
  // {
  // return candidate.equals(target);
  // }
  // });
  // } catch (DatabaseClosedException ex) {
  // System.err.println("\tDbReadWriter.contains(): " + DBERR_CLOSED + ex.getMessage());
  // }
  // // Return true if any of the target objects are found
  // return (obSet.size() > 0) ? true : false;
  // }

  /**
   * Delete an object from the database. The object must be retrieved before being deleted. The
   * caller must handle exceptions if the database is closed or Read-Only.
   * 
   * @param target object to delete
   * @return true if delete was successful, else false
   * @throws DatabaseClosedException if trying to delete from a closed (null) db
   * @throws DatabaseReadOnlyException if trying to delete from a RO db
   * @throws NullPointerException if target object is null
   */
  public boolean dbDelete(final IRegistryElement target)
      throws DatabaseClosedException, DatabaseReadOnlyException, NullPointerException
  {
    // Guards: Illegal to delete via null: entire database content would be deleted
    if (target == null) {
      throw new NullPointerException(DBERR_NULL_OBJECT);
    }
    if (_db.isClosed()) {
      throw new DatabaseClosedException();
    }
    boolean retval = false;
    ObjectSet<IRegistryElement> obSet = null;
    try {
      obSet = _db.query(new Predicate<IRegistryElement>() {
        public boolean match(IRegistryElement candidate)
        {
          return candidate.equals(target);
        }
      });
    } catch (DatabaseClosedException ex1) {
      System.err.println("\tDbReadWriter.contains(): " + DBERR_CLOSED + ex1.getMessage());
    }
    // If object was found, delete it...
    if (obSet.size() != 0) {
      IRegistryElement found = obSet.next(); // get first object in result set
      _db.delete(found);
      _db.commit();
      retval = true;
    }
    // ...else if object was not found, there is nothing to do
    return retval;
  }

  
  /** Is the database closed to transactions?
   * @return true if db is closed
   */
  public boolean dbIsClosed() 
  {
    return _db.isClosed();
  }
  
  
  /**
   * Create the object container for transaction processing with the default configuration; set the
   * db to open status. This is used when the DBRW is created.
   * <P>
   * NOTE: The folder structure must exist before a db file within it can be created. db4o will not
   * create folders: db4o will throw an enigmatic System IO error.
   * 
   * @return enum error code (OK) if all worked, or some other error code
   */
  public DB_ERROR dbOpen()
  {
    try {
      // Open the db only if it is not already open. The file is created or reloads the
      // ObjectContainer
      if ((_db == null) || (_db.isClosed())) {
        _db = (ExtObjectContainer) Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), _regPath);
      }
    } catch (Db4oIOException ex) {
      System.err.println("DbReadWriter ctor: " + ex.getMessage());
      return DB_ERROR.DB4OIO;
    } catch (DatabaseFileLockedException ex) {
      System.err.print("DbReadWriter ctor: " + DBERR_FILE_LOCKED + ": " + _regPath);
      return DB_ERROR.FILE_LOCKED;
    } catch (IncompatibleFileFormatException ex) {
      System.err.println("DbReadWriter ctor: " + DBERR_FILE_FORMAT + ": " + _regPath);
      return DB_ERROR.FILE_FORMAT;
    } catch (OldFormatException ex) {
      System.err.println("DbReadWriter ctor: " + DBERR_OLD_FORMAT);
      return DB_ERROR.OLD_FORMAT;
    } catch (DatabaseReadOnlyException ex) {
      System.err.println("DbReadWriter ctor: " + DBERR_RO_DB);
      return DB_ERROR.RO_DB;
    }
    return DB_ERROR.OK;
  }


  /**
   * Gets one or more of elements that match the predicate provided.
   * 
   * @param pred predicate objet containing the comparison function to match for retrieval
   * @return the list of elements that match the predicate provided; else returns null.
   * @throws Db4oIOException for internal db problem
   * @throws DatabaseClosedException is the database does not exist or is closed
   * @throws NullPointerException if the predicate is null
   */
  public List<IRegistryElement> dbQuery(Predicate<IRegistryElement> pred)
      throws Db4oIOException, DatabaseClosedException, NullPointerException
  {
    List<IRegistryElement> elementList = null;
    // Guards: db and predicate must exist
    // if (_db == null) {
    if (_db.isClosed()) {
      throw new DatabaseClosedException();
    }
    if (pred == null) {
      throw new NullPointerException("DbReadWriter.query(): " + DBERR_NULL_PREDICATE);
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


//  /**
//   * Add a new object into the database. All abnormal cases that cause exceptions to be thrown must
//   * be handled by the caller. This is same as <code>dbAdd()</code> except that it takes a generic
//   * Object instead of a <code>IRegistryElement</code> object.
//   * 
//   * @param obj object to add
//   * @throws NullPointerException registry should not try to save a null
//   * @throws DatabaseClosedException db needs to be in correct state
//   * @throws DatabaseReadOnlyException DBregistry does not use RO state in QM
//   * @throws ObjectNotStorableException strange objects should be screened by DBRegistry
//   */
//  public void dbSave(Object obj) throws NullPointerException,
//      DatabaseClosedException, DatabaseReadOnlyException, ObjectNotStorableException
//  {
//    // Do not allow null objects to be stored
//    if (obj == null) {
//      throw new NullPointerException("DbReadWriter(): " + DBERR_FILE_FORMAT);
//    }
//    if (_db.isClosed()) {
//      throw new DatabaseClosedException();
//    }
//    else {
//      _db.store(obj);
//      _db.commit();
//    }
//  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================



  // ================================================================================
  // Innet Class: MockDBRW
  // ================================================================================

  /** Mock registry read writer to access private methods */
  public class MockDBRW
  {
    public MockDBRW()
    {}

    /**
     * Deletes all elements in the registry, its ObjectContainer, and deletes the associated db
     * file. This method is only used for testing.
     * <p>
     * WARNING: This method is for testing only. An application should never have a need to delete
     * the database, file, and DbReadWriter.
     */
    public void dbErase()
    {
      // Delete the associated file
      File regfile = new File(_regPath);
      regfile.delete();
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

    /** Set the container to the db field */
    public void setContainer(ExtObjectContainer oc)
    {
      _db = oc;
    }

    /** Wraps outer method, returns the database container */
    public ObjectContainer openDB()
    {
      DbReadWriter.this.dbOpen();
      return _db;
    }

    /** Retruns returns the database container */
    public ExtObjectContainer getContainer()
    {
      return _db;
    }

  } // end of MockDbReadWriter inner class

} // end of RegistryReadWriter class

