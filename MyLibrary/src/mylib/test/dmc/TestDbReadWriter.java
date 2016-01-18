/**
 * Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.test.dmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;
import mylib.dmc.IRegistryElement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the database read/writer interface methods
 * 
 * @author Alan Cline
 * @version Aug 12, 2012 // original <br>
 *          Dec 3, 2012 // updates to better distinguish openDB and closeDB <br>
 *          Feb 25, 2013 // replaced queryByExample with native queries <br>
 *          Mar 18, 2013 // revised after adding IRegistryElement <br>
 *          Nov 1, 2014 // removed dbSize to mock, and fixed tests messing new mock with new regRW
 *          <br>
 *          Nov 9, 2014 // moved dbDelete into mock and refactored tests <br>
 *          Dec 7, 2014 // revised dbOpen(String) signature and associated tests <br>
 *          Dec 23, 2015 // test new frefactoring for better encapsulation <br>
 */
public class TestDbReadWriter
{
  /** Object under test */
  private DbReadWriter<SomeObject> _regRW;

  /** Place temporary test files in resource directory */
  static private final String REG_PATH = Constants.MYLIB_RESOURCES + "Test.reg";

  @AfterClass
  static public void tearDownAfterClass()
  {
    File testFile = new File(REG_PATH);
    assertTrue(testFile.exists());
    testFile.delete(); // remove the test file
  }

  @Before
  public void setUp()
  {
    _regRW = new DbReadWriter<SomeObject>(REG_PATH);
    assertNotNull(_regRW);
  }

  /**
   * Closes db, delete it and its mock to null state, db file used for test is deleted
   * 
   * @throws java.lang.Exception to catch unexpected events
   */
  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    // Clear the database and remove the file
    _regRW.dbClear();

  }

  // ====================================================================
  // BEGIN NORMAL TESTS
  // ====================================================================

  /**
   * @Normal.Test Add a normal object to a db
   */
  @Test
  public void testAddElement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int nbr = _regRW.size();
    SomeObject so = new SomeObject("SO object");
    _regRW.addElement(so);
    assertEquals(nbr + 1, _regRW.size());

    SomeObject so2 = new SomeObject("Second S");
    _regRW.addElement(so2);
    assertEquals(nbr + 2, _regRW.size());
  }


  /**
   * @Normal.Test Ensure that the correct number of objects stored in the db is returned
   */
  @Test
  public void testContainsElement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    SomeObject so1 = new SomeObject("first object saved");
    SomeObject so2 = new SomeObject("second object saved");
    SomeObject so3 = new SomeObject("third object saved");

    // Add these three and then retrieve two of them
    _regRW.addElement(so1);
    _regRW.addElement(so2);
    _regRW.addElement(so3);

    // Check that they are there
    assertNotNull(_regRW.containsElement(so2));
    assertNotNull(_regRW.containsElement(so1));

    // Check that they are not removed
    assertNotNull(_regRW.containsElement(so2));
  }


  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   *
   * @Normal.Test Remove all elements from the db: a test-only method
   */
  @Test
  public void testDbClear()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    SomeObject so1 = new SomeObject("first object saved");
    SomeObject so2 = new SomeObject("second object saved");
    SomeObject so3 = new SomeObject("third object saved");

    // Add these three and check the db size
    int nbrElems = _regRW.size();
    _regRW.addElement(so1);
    _regRW.addElement(so2);
    _regRW.addElement(so3);

    // Check that they are there
    assertEquals(nbrElems + 3, _regRW.size());

    // Clear them all
    _regRW.dbClear();
    assertEquals(0, _regRW.size());
    assertNull(_regRW.containsElement(so2));
  }


  /**
   * @Normal.Test Delete a selected object
   */
  @Test
  public void testDeleteElement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int nbrElems = _regRW.size();
    SomeObject so = new SomeObject("SO Key");

    _regRW.addElement(so);
    assertEquals(nbrElems + 1, _regRW.size());

    // Verify its gone
    _regRW.deleteElement(so);
    assertEquals(nbrElems, _regRW.size());
    assertNull(_regRW.containsElement(so));
  }


  /**
   * @Normal.Test Get an element by its key, typically its name
   */
  @Test
  public void testGet()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Clear the database
    _regRW.dbClear();

    // Add some elements
    _regRW.addElement(new SomeObject("first object saved"));
    _regRW.addElement(new SomeObject("second object saved"));
    _regRW.addElement(new SomeObject("third object saved"));
    _regRW.addElement(new SomeObject("next object"));
    _regRW.addElement(new SomeObject("and the next"));
    _regRW.addElement(new SomeObject("last object"));
    assertEquals(6, _regRW.size());

    SomeObject so = (SomeObject) _regRW.get("last object");
    assertNotNull(so);
    MsgCtrl.msgln("so received named " + so.getKey());
    
    SomeObject so2 = (SomeObject) _regRW.get("second object saved");
    assertNotNull(so2);
    MsgCtrl.msgln("so2 received named " + so2.getKey());
  }



  /**
   * @Normal.Test Get all elements in the database
   */
  @Test
  public void testGetAllList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Clear the database
    _regRW.dbClear();

    // Add some elements
    _regRW.addElement(new SomeObject("first object saved"));
    _regRW.addElement(new SomeObject("second object saved"));
    _regRW.addElement(new SomeObject("third object saved"));
    _regRW.addElement(new SomeObject("next object"));
    _regRW.addElement(new SomeObject("and the next"));
    _regRW.addElement(new SomeObject("last object"));
    assertEquals(6, _regRW.size());

    List<SomeObject> slist = _regRW.getAll();
    assertNotNull(slist);
    assertEquals(6, slist.size());

    // Clear the database and try again
    _regRW.dbClear();
    slist = _regRW.getAll();
    assertNotNull(slist);
    assertEquals(0, slist.size());
  }


  /**
   * @Normal.Test Ensure that the correct number of objects stored in the db is returned
   */
  @Test
  public void testSize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int nbrObjs = _regRW.size();
    SomeObject so1 = new SomeObject("first object saved");
    SomeObject so2 = new SomeObject("second object saved");
    SomeObject so3 = new SomeObject("third object saved");

    // Add one element
    _regRW.addElement(so1);
    assertEquals(nbrObjs + 1, _regRW.size());

    // Add second element
    _regRW.addElement(so2);
    assertEquals(nbrObjs + 2, _regRW.size());

    // Add third element
    _regRW.addElement(so3);
    assertEquals(nbrObjs + 3, _regRW.size());

    // Remove two elements
    _regRW.deleteElement(so1);
    _regRW.deleteElement(so3);
    assertEquals(nbrObjs + 1, _regRW.size());

    // Remove remaining last element
    _regRW.deleteElement(so2);
    assertEquals(nbrObjs, _regRW.size());
  }


  /**
   * @Normal.Test Add the same object multiple times to ensure that uniqueness if enforced
   */
  @Test
  public void testUniqueness()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int nbrBefore = _regRW.size();

    // Test object not within the db
    SomeObject so = new SomeObject("four");
    assertNull(_regRW.containsElement(so));

    // Add a test object
    _regRW.addElement(so);
    assertEquals(nbrBefore + 1, _regRW.size());
    assertNotNull(_regRW.containsElement(so));

    // Fail when trying to add it again
    _regRW.addElement(so);
    assertEquals(nbrBefore + 1, _regRW.size());
  }


  // ====================================================================
  // BEGIN ERROR TESTS
  // ====================================================================

  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   *
   * @Error.Test Null filename for constructor; force null pointer exception
   */
  @Test
  public void testErrorConstructorNullArg()
  {
    try {
      new DbReadWriter<SomeObject>(null);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\tExpected NullPointerException: " + ex.getMessage());
    }
  }

  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   *
   * @Error.Test Empty filename for constructor; force null pointer exception
   */
  @Test
  public void testErrorConstructorEmptyArg()
  {
    try {
      new DbReadWriter<SomeObject>("  ");
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\tExpected NullPointerException: " + ex.getMessage());
    }
  }

  
  /**
   * @Error.Test force a NullPointerException object cannot be null
   */
  @Test
  public void testErrorAddNullObject()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    try {
      _regRW.addElement(null);
      fail("No exception thrown when adding null element");
    } catch (NullPointerException ex) {
      MsgCtrl
          .errMsgln("\tExpected NullPointerException for adding null object: " + ex.getMessage());
    }
  }

  /**
   * @Normal.Test Delete a nonexisting object
   */
  @Test
  public void testErrorDeleteNonexistingElement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    SomeObject so9 = new SomeObject("object not in db");
    // Check for unadded element
    assertNull(_regRW.containsElement(so9));
    _regRW.deleteElement(so9);
  }


  // ====================================================================
  // PRIVATE HELPER METHODS
  // ====================================================================

  /**
   * DbReadWrite methods that are not tested because of their simplistic nature.
   * 
   * @Not.Needed getPath() -- simple getter
   * @Not.Needed getAll() -- simple wrapper
   * @Not.Needed close() -- tested as part of tearDown
   * @Not.Needed open() -- tested as part of setUp
   */
  void notNeeded()
  {}

  // ====================================================================
  // Inner SomeObject Test Class
  // ====================================================================

  // Internal test class
  class SomeObject implements IRegistryElement
  {
    private String _key;

    public SomeObject(String key)
    {
      _key = key;
    }

    public boolean equals(IRegistryElement target)
    {
      return (_key.equals(target.getKey()));
    }

    public String getKey()
    {
      return _key;
    }

  } // end of SomeObject test class


} // end of TestDBRegistryRW class
