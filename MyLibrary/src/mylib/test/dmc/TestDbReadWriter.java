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
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;
import mylib.dmc.DbReadWriter.MockDBRW;
import mylib.dmc.IRegistryElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
 *          Nov 1, 2014 // removed dbSize to mock, and fixed tests messing new mock with new regRW <br>
 *          Nov 9, 2014 // moved dbDelete into mock and refactored tests <br>
 *          Dec 7, 2014 // revised dbOpen(String) signature and associated tests <br>
 */
public class TestDbReadWriter extends TestCase
{
  /** Object under test */
  private DbReadWriter _regRW = null;
  /** MockObject for target object */
  private MockDBRW _mock = null;
  /** Place temporary test files in resource directory */
  private final String REG_PATH = Constants.MYLIB_RESOURCES + "Test.reg";
  /** File for db persistence */
  private final File _regFile = new File(REG_PATH);


  // ====================================================================
  // Fixtures
  // ====================================================================

  /**
   * Create database and associated file, and mock DBRW for testing.
   * 
   * @throws java.lang.Exception to catch unexpected things
   */
  @Before
  public void setUp()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    // Create new registry, open database and read-write file (default config)
    _regRW = new DbReadWriter(REG_PATH);
    assertNotNull(_regRW);
    _mock = _regRW.new MockDBRW();
    assertNotNull(_mock);
  }

  /**
   * Closes db, delete it and its mock to null state, db file used for test is deleted
   * 
   * @throws java.lang.Exception to catch unexcepted things
   */
  @After
  public void tearDown()
  {
    _mock.dbDelete(); // closes the db and deletes its filer
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ====================================================================
  // BEGIN TESTS
  // ====================================================================


  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   * 
   * @Error.Test Null filename for constructor; force null pointer exception
   */
  @Test
  public void testConstructorError()
  {
    MsgCtrl.where(this);

    // Delete the database created by setUp()
    _mock.dbDelete();

    // Confirm that DBRW and file does not exist before call...
    assertFalse(_regFile.exists());

    // Exception for null filename expected
    _regRW = new DbReadWriter(null);

    assertFalse(_regRW.isOpen());
    assertFalse(_regFile.exists());
  }


  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   * 
   * @Normal.Test Confirm that an existing file will reload for new database
   */
  @Test
  public void testConstructorReload()
  {
    MsgCtrl.where(this);

    // NORMAL: Setup has created db, container, and file
    DbReadWriter oldRW = _regRW;
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
    fileLen = _regFile.length();
    MsgCtrl.msgln("\tNewly loaded file " + _mock.getPath() + "\t:" + fileLen + " bytes.");
    assertEquals(oldFile, _regFile);
  }


  /**
   * void mylib.dmc.DbReadWriter.add(IRegistryElement) throws NullPointerException,
   * DatabaseClosedException, DatabaseReadOnlyException, ObjectNotStorableException
   * 
   * @Normal.Test Verify that objects are added to the db correctly
   * @Normal.Test Verify that objects are updated to the db correctly
   */
  @Test
  public void testAdd()
  {
    MsgCtrl.where(this);

    // Get current number of elements in db, even if 0
    assertNotNull(_regRW);
    int beforeNbr = _mock.dbSize();

    // NORMAL Add two objects and verify that count increased
    SomeObject defaultSO = new SomeObject(11, "default");
    SomeObject setSO = new SomeObject(42.0, "second object");
    MsgCtrl.msgln("\tDB size before adding:\t" + beforeNbr);
    MsgCtrl.msgln("\t\tdefaultSO: " + defaultSO.toString());
    MsgCtrl.msgln("\t\tsetSO: \t" + setSO.toString());
    _regRW.dbAdd(defaultSO);
    _regRW.dbAdd(setSO);

    // Check the db size after adding
    int afterNbr = _mock.dbSize();
    MsgCtrl.msg("\tDB size after adding: \t\t" + afterNbr);
    assertEquals(beforeNbr + 2, afterNbr);

    // NORMAL Update an object in the database
    defaultSO.setNum(-99.9);
    _regRW.dbAdd(defaultSO);

    // Ensure that the same number of objects are in the database
    afterNbr = _mock.dbSize();
    MsgCtrl.msgln("DB size after updating: \t\t" + afterNbr);
    MsgCtrl.msgln("\t\tdefaultSO: " + defaultSO.toString());
    assertEquals(beforeNbr + 2, afterNbr);
  }


  /**
   * void mylib.dmc.DbReadWriter.add(IRegistryElement) throws NullPointerException,
   * DatabaseClosedException, DatabaseReadOnlyException, ObjectNotStorableException
   * 
   * @Error.Test force a NullPointerException object cannot be null
   * @Error.Test force a DatabaseClosedException db cannot be null (closed)
   * @Error.Test force a ObjectNotStorableException try to store a String
   */
  @Test
  public void testAddError()
  {
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
    _regRW.dbOpen(REG_PATH);
  }


  /**
   * void mylib.dmc.DbReadWriter.dbClose()
   * 
   * @Normal.Test case works because it is part of tearDown(), and runs repeatedly
   * @Error.Test close db and try to write to it
   * @Error.Test try to close an already closed db
   * @Error.Test cannot force a Db4oIOException for a db4o-specific IO exception
   */
  @Test
  public void testDbClose()
  {
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
    _regRW.dbOpen(REG_PATH);
  }


  /**
   * boolean mylib.dmc.DbReadWriter.dbContains(IRegistryElement) throws DatabaseClosedException
   * 
   * @Normal.Test objects in the db are identified
   */
  @Test
  public void testDbContains()
  {
    MsgCtrl.where(this);

    // Normal Add a few objects and check that they are known
    SomeObject so1 = new SomeObject(1.0, "one");
    SomeObject so2 = new SomeObject(2.0, "two");
    SomeObject so3 = new SomeObject(3.0, "three");
    _regRW.dbAdd(so1);
    _regRW.dbAdd(so2);
    _regRW.dbAdd(so3);
    // Verify results
    assertEquals(3, _mock.dbSize());
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
    assertEquals(4, _mock.dbSize());
    assertTrue(_regRW.dbContains(so4));
  }


  /**
   * boolean mylib.dmc.DbReadWriter.dbDelete(IRegistryElement)
   * 
   * @Normal.Test delete a unique object from the database
   * @Normal.Test attempt to delete the same object twice
   * @Error.Test force DatabaseClosedException attempt to delete from a closed database
   * @Error.Test force DatabaseReadOnlyException attempt to delete from a RO database
   */
  @Test
  public void testDbDelete()
  {
    MsgCtrl.where(this);

    SomeObject so1 = new SomeObject(12.2, "soon to be dead");
    SomeObject so2 = new SomeObject(24.4, "tried to be RO read");

    // Normal: Delete an existing object from the db
    _regRW.dbAdd(so1);
    MsgCtrl.msgln("\tDb contains " + _mock.dbSize() + " elements");
    assertEquals(1, _mock.dbSize());
    _regRW.dbDelete(so1);
    MsgCtrl.msgln("\tDb contains " + _mock.dbSize() + " elements");
    assertEquals(0, _mock.dbSize());

    // Normal Try to delete the same object twice: silent fail
    _regRW.dbDelete(so1);
    MsgCtrl.msgln("\tDb contains " + _mock.dbSize() + " elements");
    assertEquals(0, _mock.dbSize());

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
    _mock = _regRW.new MockDBRW();
    assertNotNull(_mock);
    _regRW.dbAdd(so1);
    _regRW.dbAdd(so2);
    _regRW.dbReadOnly(false);
    MsgCtrl.msgln("\tDb contains " + _mock.dbSize() + " elements");
    assertEquals(2, _mock.dbSize());
    try {
      _regRW.dbDelete(so1);
    } catch (DatabaseReadOnlyException ex) {
      MsgCtrl.errMsgln("\tExpected exception: " + "Attempting to delete from RO db");
    }
  }


  /**
   * DB_ERROR mylib.dmc.DbReadWriter(String)
   * 
   * @Normal.Test Write to file, close db, then re-read previously written object
   */
  @Test
  public void testDbOpenExistingFile()
  {
    MsgCtrl.where(this);

    // Create object to write
    SomeObject so = new SomeObject(1.0, "test reliving through dbClose()");
    MsgCtrl.msgln("\tobject created = " + so.toString());
    // MsgCtrl.msgln("\tdb created at " + REG_PATH);

    // NORMAL: Write to file, close db, then re-read previously written object
    MsgCtrl.msgln("\tBefore adding, db contains \t\t" + _mock.dbSize() + " elements.");
    _regRW.dbAdd(so);
    MsgCtrl.msgln("\tAfter adding, db contains \t\t" + _mock.dbSize() + " elements.");
    _regRW.dbClose();
    // reopen the file
    _regRW = new DbReadWriter(REG_PATH);
    assertNotNull(_regRW);
    _mock = _regRW.new MockDBRW();
    assertNotNull(_mock);
    MsgCtrl.msgln("\tBefore checking, db contains contains \t" + _mock.dbSize() + " elements.");
    assertTrue(_regRW.dbContains(so));
    MsgCtrl.msgln("\tAfter checking, db contains contains \t" + _mock.dbSize() + " elements.");
    MsgCtrl.msgln("\tobject retreived = " + so.toString());
  }


  /**
   * DB_ERROR mylib.dmc.DbReadWriter.open(String)
   * 
   * @Error.Test Ensure that database is already open and try to open it
   * @Normal.Test Ensure that database is closed but file exists
   */
  @Test
  public void testDbOpenFileExists()
  {
    MsgCtrl.where(this);

    // // ERROR: Reopening an open db throws a db locked exception
    // assertTrue(_regFile.exists());
    // assertNotNull(_mock.openDB()); // open database...
    // assertTrue(_regFile.exists()); // ...and check that file still exists

    // NORMAL: Ensure that database is closed but file exists
    // Same file will be reopened
    assertTrue(_regFile.exists());
    _regRW.dbClose();
    assertTrue(_regFile.exists());
  }


  /**
   * @Error.Test force a DatabaseReadOnlyException Change the config to RO and try to add
   */
  @Test
  public void testDbReadOnly()
  {
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
   * List<IRegistryElement> dbQuery(Predicate<IRegistryElement>) throws Db4oIOException,
   * DatabaseClosedException, NullPointerException
   * 
   * @Normal.Test extract element lists using different kinds of Predicates
   */
  @Test
  @SuppressWarnings("serial")
  public void testDbQuery()
  {
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
   * @Normal.Test Ensure that the correct number of objects stored in the db is returned
   */
  @Test
  public void testdbSize()
  {
    MsgCtrl.where(this);

    // Create objects to write
    SomeObject so1 = new SomeObject(1.0, "first object saved");
    SomeObject so2 = new SomeObject(2.0, "second object saved");
    SomeObject so3 = new SomeObject(3.0, "third object saved");

    // NORMAL: Write to file, then get db size
    int nbrObjs = _mock.dbSize();
    _regRW.dbAdd(so1);
    _regRW.dbAdd(so2);
    _regRW.dbAdd(so3);
    int nbrObjsAfter = _mock.dbSize();
    MsgCtrl.msgln("\tNumber of objects retrieved after adding three objects = " + nbrObjsAfter);
    assertEquals(nbrObjsAfter, nbrObjs + 3);

    // Close, reopen, add one more, then try again
    nbrObjs = _mock.dbSize();
    _regRW.dbClose();
    _regRW.dbOpen(REG_PATH);
    nbrObjsAfter = _mock.dbSize();
    MsgCtrl.msgln("\tNumber of objects after closing and opening db = " + nbrObjs);
    assertEquals(nbrObjs, nbrObjsAfter);

    // Now delete two and try again
    MsgCtrl.msg("\n\tDb closed and reopened then...");
    nbrObjs = _mock.dbSize();
    _regRW.dbDelete(so2);
    _regRW.dbDelete(so1);
    nbrObjsAfter = _mock.dbSize();
    MsgCtrl.msgln("\tso2 then so1 deleted.");
    assertEquals(nbrObjsAfter, nbrObjs - 2);

    // Now delete last one and try again
    nbrObjs = _mock.dbSize();
    _regRW.dbDelete(so3);
    nbrObjsAfter = _mock.dbSize();
    MsgCtrl.msgln("\tso3 deleted.");
    assertEquals(nbrObjsAfter, nbrObjs - 1);
  }


  // ====================================================================
  // PRIVATE HELPER METHODS
  // ====================================================================


  /**
   * 4 There are four methods that do not need to be tested due to their simplistic nature.
   * 
   * @Not.Needed getDB() -- getter
   * @Not.Needed dbIsClosed() -- wrapper
   * @Not.Needed DbOpenError() -- don't know how to trigger the db errors
   * @Not.Needed dbSave(Object) 
   */
  void NotNeeded()
  {}



} // end of TestDBRegistryRW class
