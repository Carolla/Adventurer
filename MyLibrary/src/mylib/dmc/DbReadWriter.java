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
public class DbReadWriter<E extends IRegistryElement>
{
  /** The path of the database file */
  protected final String _regPath;

  /** Db is open or closed */
  private boolean _open = false;

  /** actual database */
  private EmbeddedObjectContainer _db = null;

  /** Exception message for null argument */
  static private final String ERR_NULL_ARG = "Argument cannot be null or empty";

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
      throw new NullPointerException(ERR_NULL_ARG);
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
  public boolean addElement(E obj)
  {
    // Guard: null object not permitted
    if (obj == null) {
      throw new NullPointerException("Cannot add null Object");
    }
    try {
      _db = open();
      if (!containsElement(obj.getKey())) {
        _db.store(obj);
        _db.commit();
        return true;
      }
    } catch (DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    } finally {
      close();
    }
    return false;
  }


  /**
   * Deletes all elements in the registry. Each item is removed from the database so db4o's OID can
   * be used to delete it.
   * <p>
   * WARNING: This method is for testing only. An application should never have a need to clear the
   * database, file, and DbReadWriter.
   */
  public void clear()
  {
    try {
      open();
      for (E elem : getAllList()) {
        _db.delete(elem);
      }
      _db.commit();
    } catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException ex) {
      handleDbException(ex);
    } finally {
      close();
    }
  }

  public boolean contains(final E target)
  {
    try {
      open();
      return containsElement(target.getKey());
    } finally {
      close();
    }
  }

  /**
   * Delete an object from the database. The object must be retrieved before being deleted. The
   * caller must handle exceptions if the database is closed or Read-Only.
   * 
   * @param target object to delete
   * @return true if delete was successful, else false
   */
  public void deleteElement(E target)
  {
    if (target == null) {
      return;
    }

    try {
      _db = open();
      for (E el : getAllList()) {
        if (el.getKey().equalsIgnoreCase(target.getKey())) {
          _db.delete(el);
        }
      }
      _db.commit();
    } catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException ex) {
      handleDbException(ex);
    } finally {
      close();
    }
  }


  /**
   * Retrieve the first element that matches the name. The object's {@code getKey} method is called.
   * 
   * @param name key of the target object to match against for comparison
   * @return the object with matching name; else null if not found
   */
  public E get(String name)
  {
    if (!exists(name)) {
      return null;
    }

    _db = open();
    try {
      List<E> elementList = getAllList();
      for (E obj : elementList) {
        if (obj.getKey().equalsIgnoreCase(name)) {
          return obj;
        }
      }
    } finally {
      close();
    }
    return null;
  }


  public List<E> getAll()
  {
    try {
      open();
      List<E> list = getAllList();
      return list;
    } finally {
      close();
    }
  }

  public List<E> query(Predicate<E> pred)
  {
    List<E> alist = new ArrayList<E>();
    try {
      open();
      alist.addAll(_db.query(pred));
      return alist;
    } finally {
      close();
    }
  }


  /** Finds all elements in the given Registry ReadWriter */
  public int size()
  {
    try {
      _db = open();
      List<E> alist = getAllList();
      return alist.size();
    } finally {
      close();
    }
  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

  /**
   * Close the open database (and resets the open/close flag).
   */
  private void close()
  {
    if (_open) {
      try {
        _db.close();
        _open = false;
      } catch (Db4oIOException ex) {
        handleDbException(ex);
      }
    }
  }


  /**
   * Verify if a particular object exists, found by calling that object's {@code equals} method
   * 
   * @param target name of the object with specific fields to find
   * @return the object found, else null
   */
  private boolean containsElement(final String target)
  {
    if (target == null)
      return false;

    for (E elem : getAllList()) {
      if (elem.getKey().equals(target)) {
        return true;
      }
    }
    return false;
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
   * elements, leaving the db open so that the resulting List is valid.
   * <P>
   * Warning: The List returned is an ObjectSetFacade, and is only available when the db is open.
   * Trying to use the List after the db is closed will throw a DatabaseClosedException.
   * 
   * @return the list for further action, leaving the db open
   */
  @SuppressWarnings("serial")
  private List<E> getAllList()
  {
    List<E> alist = new ArrayList<E>();
    alist.addAll(_db.query(new Predicate<E>() {
      @Override
      public boolean match(E candidate)
      {
        return true;
      }
    }));
    return alist;
  }


  private void handleDbException(Exception ex)
  {
    System.err.println(ex.getClass() + ": " + ex.getMessage());
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
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().activationDepth(255);
        config.common().updateDepth(255);

        _db = Db4oEmbedded.openFile(config, _regPath);
        _open = true;
      }
    } catch (Db4oIOException | DatabaseFileLockedException | IncompatibleFileFormatException
        | OldFormatException | DatabaseReadOnlyException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
      System.exit(-1);
    }
    return _db;
  }
} // end of RegistryReadWriter class
