/**
 * Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.test.dmc;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;
import mylib.dmc.DbReadWriter.MockDBRW;
import mylib.dmc.IRegistryElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.query.Predicate;

/**
 * Test the database read/writer interface methods
 * 
 * @author Alan Cline
 * @version Aug 12, 2012 // original <br>
 *          Dec 3, 2012 // updates to better distinguish openDB and closeDB <br>
 *          Feb 25, 2013 // replaced queryByExample with native queries <br>
 *          Mar 18, 2013 // revised after adding IRegistryElement <br>
 */
public class TestDbReadWriter extends TestCase
{
  /** Object under test */
  private DbReadWriter _regRW = null;
  /** MockObject for target object */
  private MockDBRW _mock = null;
  // /** Name of the persistence file */
  // private final String REG_NAME = "Test.reg";
  /** File separator for current platform */
  private final String FILE_SEPARATOR = System.getProperty("file.separator");
  /** Place temporary test files in current directory */
  private final String REG_PATH = System.getProperty("user.dir") + FILE_SEPARATOR
      + "resources" + FILE_SEPARATOR + "Test.reg";
  /** File for db persistence */
  private final File _regFile = new File(REG_PATH);

  
  // ====================================================================
  // Fixtures
  // ====================================================================


  /**
   * Create database and associated file, and mock DBRW for testing.
   * 
   * @throws java.lang.Exception to catch unexcepted things
   */
  @Before
  public void setUp() throws Exception
  {
    MsgCtrl.errorMsgsOn(true);
    // Create new registry, open database and read-write file (default config)
    _regRW = new DbReadWriter(REG_PATH);
    assertNotNull(_regRW);
    _mock = _regRW.new MockDBRW();
    assertNotNull(_mock);
    assertNotNull(_mock.getContainer());
  }


  /**
   * Returns db and mock to null state, db file used for test is deleted
   * 
   * @throws java.lang.Exception to catch unexcepted things
   */
  @After
  public void tearDown() throws Exception
  {
    _regRW.dbDelete();
    _regRW = null;
    _mock = null;
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  // ====================================================================
  // BEGIN TESTS
  // ====================================================================

  /**
   * @Not_Implemented DbOpenError() -- don't know how to trigger the db errors
   * @Not_implemented dbSave(Object obj)
   */


  /**
   * @Error Null filename for constructor; force null pointer exception
   */
  @Test
  public void testConstructorError()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Delete the database created by setUp() and its ObjectContainer
    _regRW.dbDelete();

    // Confirm that DBRW and file does not exist before call...
    assertFalse(_regFile.exists());

    // Exception for null filename expected
    try {
      _regRW = new DbReadWriter(null);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\tException expected: " + ex.getMessage());
    }
    assertFalse(_regFile.exists());
  }


  /**
   * @Normal Confirm that an existing file will reload for new database
   */
  @Test
  public void testConstructorReload()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // NORMAL: Setup has created db, container, and file
    DbReadWriter oldRW = _regRW;
    ObjectContainer oldContainer = _mock.getContainer();
    File oldFile = _regFile;

    // Close down the file and recreate the database with the same file; DBRW still exists
    _regRW.dbClose();
    assertNotNull(_regRW);
    assertTrue(_regFile.exists());
    // Display message that db was created in setUp()
    MsgCtrl.msgln("\tClosed database " + _regRW.getClass().getCanonicalName());
    // Confirm that file is now in place
    long fileLen = _regFile.length();
    MsgCtrl.msgln("\tWrote file " + _mock.getPath() + "\t:" + fileLen + " bytes.");
    assertTrue(_regFile.exists());

    // Now try to create DBRW and confirm that it reused existing one, and that same file is used
    // Create new registry, open database and read-write file (default config)
    _regRW = new DbReadWriter(REG_PATH);
    assertFalse(oldRW == _regRW);
    _mock = _regRW.new MockDBRW();
    assertNotNull(_mock);
    assertTrue(oldContainer != _mock.getContainer());
    fileLen = _regFile.length();
    MsgCtrl.msgln("\tNewly loaded file " + _mock.getPath() + "\t:" + fileLen + " bytes.");
    assertEquals(oldFile, _regFile);
  }


  /**
   * @Normal Verify that objects are added to the db correctly
   * @Normal Verify that objects are updated to the db correctly
   */
  @Test
  public void testAdd()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Get current number of elements in db, even if 0
    assertNotNull(_regRW);
    int beforeNbr = getNbrElements();

    // NORMAL Add two objects and verify that count increased
    SomeObject defaultSO = new SomeObject(11, "default");
    SomeObject setSO = new SomeObject(42.0, "second object");
    MsgCtrl.msgln("\tDB size before adding:\t" + beforeNbr);
    MsgCtrl.msgln("\t\tdefaultSO: " + defaultSO.toString());
    MsgCtrl.msgln("\t\tsetSO: \t" + setSO.toString());
    _regRW.dbAdd(defaultSO);
    _regRW.dbAdd(setSO);

    // Check the db size after adding
    int afterNbr = getNbrElements();
    MsgCtrl.msg("\tDB size after adding: \t\t" + afterNbr);
    assertEquals(beforeNbr + 2, afterNbr);

    // NORMAL Update an object in the database
    defaultSO.setNum(-99.9);
    _regRW.dbAdd(defaultSO);

    // Ensure that the same number of objects are in the database
    afterNbr = getNbrElements();
    MsgCtrl.msgln("DB size after updating: \t\t" + afterNbr);
    MsgCtrl.msgln("\t\tdefaultSO: " + defaultSO.toString());
    assertEquals(beforeNbr + 2, afterNbr);
  }


  /**
   * @Error force a NullPointerException object cannot be null
   * @Error force a DatabaseClosedException db cannot be null (closed)
   * @Error force a ObjectNotStorableException try to store a String
   */
  @Test
  public void testAddError()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create a target object to save
    SomeObject so = new SomeObject(-1.0, "negative");

    // Error: Try to store a null
    try {
      _regRW.dbAdd(null);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\t Expected exception: " + ex.getMessage());
    }

    // Error: Try to write to db after it is closed, and set to null
    try {
      _regRW.dbClose();
      _regRW.dbAdd(so);
    } catch (DatabaseClosedException ex) {
      MsgCtrl.errMsgln("\t Expected exception: " + "db closed (null)");
    }
    // Reopen DbReadWriter so that tearDown() will not fail
    _regRW.dbOpen();
  }


  /**
   * @Normal case works because it is part of tearDown(), and runs repeatedly
   * @Error close db and try to write to it
   * @Error try to close an already closed db
   * @Error cannot force a Db4oIOException for a db4o-specific IO exception
   */
  @Test
  public void testDbClose()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create a target object to save
    SomeObject so = new SomeObject(12.34, "numbers");
    _regRW.dbClose();

    // ERROR Write to closed db
    try {
      _regRW.dbAdd(so);
    } catch (DatabaseClosedException ex) {
      MsgCtrl.errMsgln("\t Expected exception: " + "db closed (null)");
    }

    // ERROR: Try to close an already closed db
    try {
      _regRW.dbClose();
    } catch (DatabaseClosedException ex) {
      MsgCtrl.errMsgln("\t Expected exception: " + "db closed (null)");
    }
    // Reopen DbReadWriter so that tearDown() will not fail
    _regRW.dbOpen();
  }


  /**
   * @Normal objects in the db are identified
   * @Error
   * @Error
   */
  @Test
  public void testDbContains()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal Add a few objects and check that they are known
    SomeObject so1 = new SomeObject(1.0, "one");
    SomeObject so2 = new SomeObject(2.0, "two");
    SomeObject so3 = new SomeObject(3.0, "three");
    _regRW.dbAdd(so1);
    _regRW.dbAdd(so2);
    _regRW.dbAdd(so3);
    // Verify results
    assertEquals(3, getNbrElements());
    assertTrue(_regRW.dbContains(so2));
    assertTrue(_regRW.dbContains(so1));
    assertTrue(_regRW.dbContains(so3));

    // Error Try for variations on this object
    SomeObject so4 = new SomeObject(4.0, "four");
    assertFalse(_regRW.dbContains(so4));
    SomeObject soNull = new SomeObject(0.0, null);
    assertFalse(_regRW.dbContains(soNull));
    // Add another one
    _regRW.dbAdd(so4);
    assertEquals(4, getNbrElements());
    assertTrue(_regRW.dbContains(so4));
  }


  // /** Chronos.dmc.DbReadWriter
  // * Cannot think how to force these exceptions for this private method.
  // * Will write these tests as these exceptions appear
  // * @Error: force a DatabaseFileLockedException
  // * @Error: force a Db4oIOException
  // * @Error: force a IncompatibleFileFormatException
  // * @Error: force a OldFormatException
  // */
  // @Test
  // public void testDbOpenError()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.msgln(this, "\t testDbOpenError()");
  //
  // // Confirm file exists and database is open
  // assertTrue(_regFile.exists());
  // assertNotNull(_mock.getContainer());
  //
  // // ERROR: Force a locked file exception, which occurs when the database is already in use
  // // Create a second DBRW for the same file
  // try {
  // ObjectContainer oc = _mock.openDB();
  // assertNotNull(oc);
  // // Read from the test file from two processes concurrently without an intervening commit
  // oc.queryByExample(null);
  // _regRW.getAllElements();
  // } catch (DatabaseFileLockedException ex) {
  // MsgCtrl.errMsgln("\tExpected exception: " + ex.getMessage());
  // }
  // }


  /**
   * @Normal delete a unique object from the database
   * @Normal attempt to delete the same object twice
   * @Error force DatabaseClosedException attempt to delete from a closed database
   * @Error force DatabaseReadOnlyException attempt to delete from a RO database
   */
  @Test
  public void testDbDelete()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    SomeObject so1 = new SomeObject(12.2, "soon to be dead");
    SomeObject so2 = new SomeObject(24.4, "tried to be RO read");

    // Normal: Delete an existing object from the db
    _regRW.dbAdd(so1);
    MsgCtrl.msgln("\tDb contains " + getNbrElements() + " elements");
    assertEquals(1, getNbrElements());
    _regRW.dbDelete(so1);
    MsgCtrl.msgln("\tDb contains " + getNbrElements() + " elements");
    assertEquals(0, getNbrElements());

    // Normal Try to delete the same object twice: silent fail
    _regRW.dbDelete(so1);
    MsgCtrl.msgln("\tDb contains " + getNbrElements() + " elements");
    assertEquals(0, getNbrElements());

    // Error: Close the database and try again
    _regRW.dbClose();
    try {
      _regRW.dbDelete(so1);
    } catch (DatabaseClosedException ex) {
      MsgCtrl.errMsgln("\tExpected exception: " + "Closed (null) database");
    }

    // Error: Set the database to readOnly and try again
    // Create new registry, open database and read-write file (default config)
    _regRW = new DbReadWriter(REG_PATH);
    assertNotNull(_regRW);
    _regRW.dbAdd(so1);
    _regRW.dbAdd(so2);
    _regRW.dbReadOnly(false);
    MsgCtrl.msgln("\tDb contains " + getNbrElements() + " elements");
    assertEquals(2, getNbrElements());
    try {
      _regRW.dbDelete(so1);
    } catch (DatabaseReadOnlyException ex) {
      MsgCtrl.errMsgln("\tExpected exception: " + "Attempting to delete from RO db");
    }
  }


  /**
   * @Normal: Write to file, close db, then re-read previously written object
   */
  @Test
  public void testDbOpenExistingFile()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Create object to write
    SomeObject so = new SomeObject(1.0, "test reliving through dbClose()");
    MsgCtrl.msgln("\tobject created = " + so.toString());

    // NORMAL: Write to file, close db, then re-read previously written object
    _regRW.dbAdd(so);
    _regRW.dbClose();
    // reopen the file
    _regRW = new DbReadWriter(REG_PATH);
    assertNotNull(_regRW);
    // TODO db doesn't seem to read object from previous session
    assertTrue(_regRW.dbContains(so));
    MsgCtrl.msgln("\tobject added = " + so.toString());
  }


  /**
   * @Normal: Ensure that database is already open and try to open it
   * @Normal: Ensure that database is closed but file exists
   */
  @Test
  public void testDbOpenFileExists()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // NORMAL: Ensure that database is already open and try to open it; it should reload from file
    assertTrue(_regFile.exists());
    assertNotNull(_mock.getContainer());
    assertNotNull(_mock.openDB()); // open database...
    assertTrue(_regFile.exists()); // ...and check that file still exists

    // NORMAL: Ensure that database is closed but file exists
    // Same file will be reopened
    assertTrue(_regFile.exists());
    _regRW.dbClose(); 
    assertTrue(_regFile.exists());
  }


  /**
   * @Error force a DatabaseReadOnlyException Change the config to RO and try to add
   */
  @Test
  public void testDbReadOnly()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create a target object to save
    SomeObject so = new SomeObject(111.1, "ones");
    // Set the database to readOnly
    _regRW.dbReadOnly(false);
    // Try to write to this db
    try {
      _regRW.dbAdd(so);
    } catch (DatabaseReadOnlyException ex) {
      MsgCtrl.errMsgln("\t Expected exception: " + "Trying to write to read-only database");
    }
    // Now change it back so it succeeds
    _regRW.dbReadOnly(false);
    _regRW.dbAdd(so);
    MsgCtrl.msgln("\t Succeeded in changing from RO to RW and adding object");
  }


  /**
   * @Normal extract element lists using different kinds of Predicates
   */
  @Test
  public void testDbQuery()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Populate the database with some objects
    final SomeObject t1 = new SomeObject(212.0, "degrees");
    final SomeObject t2 = new SomeObject(32.0, "degrees");
    final SomeObject t3 = new SomeObject(0.0, "radians");
    _regRW.dbAdd(t1);
    _regRW.dbAdd(t2);
    _regRW.dbAdd(t3);

    // NORMAL Retrieve using a matching object predicate
    Predicate<IRegistryElement> objPred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return candidate.equals(t2);
      }
    };
    List<IRegistryElement> soList = _regRW.dbQuery(objPred); // input the predicate
    assertNotNull(soList);
    int size = soList.size();
    MsgCtrl
        .msgln("\tTest 1: Object match: Found " + size + " element: " + soList.get(0).toString());
    assertEquals(1, size);

    // NORMAL Retrieve using a key matching predicate
    Predicate<IRegistryElement> keyPred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return candidate.getKey().equals(t2.getKey());
      }
    };
    soList = _regRW.dbQuery(keyPred); // new predicate
    assertNotNull(soList);
    size = soList.size();
    MsgCtrl.msgln("\tTest 2: Key match: Found " + size + " elements, first of which is: "
        + soList.get(0).toString());
    assertEquals(2, size);

    // NORMAL Retrieve using a predicate that matches everything
    Predicate<IRegistryElement> allPred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return true;
      }
    };
    soList = _regRW.dbQuery(allPred); // new predicate
    assertNotNull(soList);
    size = soList.size();
    MsgCtrl.msgln("\tTest 3: All match  (Found " + size + " elements): ");
    assertEquals(3, size);
    for (int k = 0; k < soList.size(); k++) {
      MsgCtrl.msgln("\t\t" + soList.get(k).toString());
    }

    // NORMAL Retrieve using a predicate that matches nothing
    Predicate<IRegistryElement> nonePred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return candidate.getKey().equals("nothing");
      }
    };
    soList = _regRW.dbQuery(nonePred); // new predicate
    assertNotNull(soList);
    size = soList.size();
    MsgCtrl.msgln("\tTest 4: No match  (Found " + size
        + " elements that compares with \"Nothng\"): ");
    assertEquals(0, size);

    // ERROR Try to use a null Predicate
    MsgCtrl.msgln("\tTest 5: Null predicate: expected error message:");
    MsgCtrl.msg("\t\t\t");
    try {
      soList = _regRW.dbQuery(null); // new predicate
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\tExpected exception: " + ex.getMessage());
    }

  }


  /**
   * @Normal Ensure that the correct number of objects stored in the db is returned
   */
  @Test
  public void testGetNbrElements()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create objects to write
    SomeObject so1 = new SomeObject(1.0, "first object saved");
    SomeObject so2 = new SomeObject(2.0, "second object saved");
    SomeObject so3 = new SomeObject(3.0, "third object saved");
    SomeObject so4 = new SomeObject(4.0, "fourth object saved");

    // NORMAL: Write to file, then get db size
    _regRW.dbAdd(so1);
    _regRW.dbAdd(so2);
    _regRW.dbAdd(so3);
    int nbrObjs = getNbrElements();
    MsgCtrl.msgln("\tNumber of objects retrieved after adding three objects = " + nbrObjs);
    assertEquals(3, nbrObjs);

    // Close, reopen, add one more, then try again
    _regRW.dbClose();
    _regRW = new DbReadWriter(REG_PATH);
    MsgCtrl.msgln("\tNumber of objects after closing and opening db = " + nbrObjs);
    _regRW.dbAdd(so4);
    nbrObjs = getNbrElements();
    MsgCtrl.msgln("\tNumber of objects after adding a new object = " + nbrObjs);
    assertEquals(4, nbrObjs);

    // Now delete two and try again
    MsgCtrl.msg("\n\tDb closed and reopened then...");
    _regRW.dbDelete(so2);
    _regRW.dbDelete(so1);
    MsgCtrl.msgln("\tso2 then so1 deleted.");
    assertEquals(2, getNbrElements());

    // Now delete last two and try again
    _regRW.dbDelete(so4);
    _regRW.dbDelete(so3);
    MsgCtrl.msgln("\tso4 then so3 deleted.");
    assertEquals(0, getNbrElements());

    // Normal Delete an object that is not in the db
    _regRW.dbDelete(so4); // it has been deleted already
    nbrObjs = getNbrElements();
    MsgCtrl.msgln("\tAttempted to retrieve non-stored object after second delete: " + nbrObjs);
    assertEquals(0, nbrObjs);
  }


  // ====================================================================
  // PRIVATE HELPER METHODS
  // ====================================================================

  /**
   * Get the number of elements in the db using a Predicate
   * 
   * @return number of elements
   */
  private int getNbrElements()
  {
    List<IRegistryElement> elementList = _regRW.dbQuery(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return true;
      }
    });
    return elementList.size();
  }


  /**
   * 1
   * 
   * @NotNeeded getDB() -- getter
   */
  void _testsNotNeeded()
  {}



} // end of TestDBRegistryRW class
