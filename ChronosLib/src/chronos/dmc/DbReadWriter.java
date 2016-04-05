/**
 * DbReadWriter.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.dmc;

import java.util.ArrayList;
import java.util.List;

import mylib.dmc.IRegistryElement;
import chronos.pdc.character.Hero;

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
  public void addElement(E obj)
  {
    // Guard: null object not permitted
    if (obj == null) {
      throw new NullPointerException("Cannot add null Object");
    }
    _db = open();
    try {
      if (containsElement(obj) == null) {
        _db.store(obj);
        _db.commit();
      }
    } catch (DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    } finally {
      close();
    }
  }


  /**
   * Deletes all elements in the registry. Each item is removed from the database so db4o's OID can
   * be used to delete it.
   * <p>
   * WARNING: This method is for testing only. An application should never have a need to clear the
   * database, file, and DbReadWriter.
   */
  @SuppressWarnings("serial")
  public void clear()
  {
    List<E> alist = new ArrayList<E>();
    _db = open();
    try {
      alist = _db.query(new Predicate<E>() {
        public boolean match(E candidate)
        {
          return true;
        }
      });
      // There is no clear() for the object set in the db, so each must be removed individually
      for (E elem : alist) {
        _db.delete(elem);
      }
    } catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    } finally {
      close();
    }
  }


  /**
   * Verify if a particular object exists, found by calling that object's {@code equals} method
   * 
   * @param target name of the object with specific fields to find
   * @return the object found, else null
   */
  private E containsElement(final E target)
  {
    for (E elem : getAllList()) {
      if (elem.getKey().equals(target.getKey())) {
        return elem;
      }
    }
    return null;
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
    // Guards: Illegal to delete via null: entire database content would be deleted
    if (target == null) {
      return;
    }
    // Object must be retrieved before it can be deleted
    _db = open();
    try {
      E obj = containsElement(target);
      if (obj != null) {
        _db.delete(obj);
      }
    } catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
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
    // Guard: name must not be null or empty
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
      return null;
    } finally {
      close();
    }
  }


  public List<E> getAll()
  {
    _db = open();
    List<E> list = getAllList();
    close();
    return list;
  }

  /** Finds all elements in the given Registry ReadWriter */
  public int size()
  {
    _db = open();
    List<E> alist = getAllList();
    close();
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
        while (!_db.close()) {
        }
        _open = false;
      }
    } catch (Db4oIOException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
      _open = true;
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
        config.common().objectClass(Hero.class).cascadeOnActivate(true);
        config.common().objectClass(Hero.class).cascadeOnDelete(true);
        config.common().objectClass(Hero.class).cascadeOnUpdate(true);

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
