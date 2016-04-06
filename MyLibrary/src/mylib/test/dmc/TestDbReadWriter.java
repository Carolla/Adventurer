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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;

import org.junit.After;
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
 *          Dec 23, 2015 // test new refactoring for better encapsulation <br>
 *          Mar 29 2016 // reviewed tests after refactoring it out of {@code Registry} <br>
 */
public class TestDbReadWriter
{
  /** Object under test */
  private DbReadWriter<SomeObject> _dbrw;

  /** Place temporary test files in resource directory */
  static private final String REG_PATH = Constants.MYLIB_RESOURCES + "Test.reg";

  @Before
  public void setUp()
  {
    _dbrw = new DbReadWriter<SomeObject>(REG_PATH);
  }

  /**
   * @throws java.lang.Exception to catch unexpected events
   */
  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _dbrw.clear();
  }


  // ====================================================================
  // BEGIN TESTS
  // ====================================================================

  /**
   * @Normal.Test void addElement(E obj)
   */
  @Test
  public void testAddElement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    SomeObject so = new SomeObject("SO object");
    _dbrw.addElement(so);
    assertEquals(1, _dbrw.size());

    SomeObject so2 = new SomeObject("Second S");
    _dbrw.addElement(so2);
    assertEquals(2, _dbrw.size());
  }


  /**
   * @Normal.Test void clear()
   */
  @Test
  public void testClear()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    // Add three objects to be cleared
    SomeObject so1 = new SomeObject("First S");
    SomeObject so2 = new SomeObject("Second S");
    SomeObject so3 = new SomeObject("Third S");
    _dbrw.addElement(so1);
    _dbrw.addElement(so2);
    _dbrw.addElement(so3);
    assertEquals(3, _dbrw.size());

    // RUN
    _dbrw.clear();

    // VERIFY
    assertEquals(0, _dbrw.size());
  }

  /**
   * @Normal.Test boolean containsElement(final E target)
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
    _dbrw.addElement(so1);
    _dbrw.addElement(so2);
    _dbrw.addElement(so3);

    // Check that they are there
    assertTrue(_dbrw.contains(so2));
    assertTrue(_dbrw.contains(so1));

    // Check that they are not removed
    assertNotNull(_dbrw.contains(so1));
    assertNotNull(_dbrw.contains(so2));
  }


  /**
   * @Normal.Test void deleteElement(E target)
   */
  @Test
  public void testDeleteElement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    SomeObject so1 = new SomeObject("SO Key 1");
    SomeObject so2 = new SomeObject("SO Key 2");

    _dbrw.addElement(so1);
    _dbrw.addElement(so2);
    assertEquals(2, _dbrw.size());

    // Verify its gone
    _dbrw.deleteElement(so1);
    assertFalse(_dbrw.contains(so1));
//    assertEquals(1, _dbrw.size());

    _dbrw.deleteElement(so2);
    assertEquals(0, _dbrw.size());
    assertFalse(_dbrw.contains(so2));
  }


  /**
   * @Normal.Test E get(String name)
   */
  @Test
  public void testGet()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Add some elements
    _dbrw.addElement(new SomeObject("first object saved"));
    _dbrw.addElement(new SomeObject("second object saved"));
    SomeObject so3 = new SomeObject("third object saved");
    _dbrw.addElement(so3);
    _dbrw.addElement(new SomeObject("next object"));
    _dbrw.addElement(new SomeObject("and the next"));
    SomeObject so6 = new SomeObject("last object");
    _dbrw.addElement(so6);
    assertEquals(6, _dbrw.size());

    // RUN Get does not change db size
    SomeObject f3 = _dbrw.get("third object saved");
    assertNotNull(f3);
    MsgCtrl.msgln("so received named " + f3.getKey());
    assertEquals(f3.toString(), so3.toString());
    assertEquals(6, _dbrw.size());

    // RUN with object having extra space
    SomeObject f6 = _dbrw.get("last object    ");
    assertNull(f6);

    // Delete and try to get again
    _dbrw.deleteElement(so6);
    assertEquals(5, _dbrw.size());
    assertNull(_dbrw.get("last object"));
  }


  /**
   * @Normal.Test List<E> getAll()
   */
  @Test
  public void testGetAll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Add some elements
    _dbrw.addElement(new SomeObject("first object saved"));
    _dbrw.addElement(new SomeObject("second object saved"));
    SomeObject so3 = new SomeObject("third object saved");
    _dbrw.addElement(so3);
    _dbrw.addElement(new SomeObject("next object"));
    _dbrw.addElement(new SomeObject("and the next"));
    _dbrw.addElement(new SomeObject("last object"));
    assertEquals(6, _dbrw.size());

    // RUN
    List<SomeObject> slist = _dbrw.getAll();

    // VERIFY
    assertNotNull(slist);
    assertEquals(6, slist.size());
    assertEquals(6, _dbrw.size());

    _dbrw.deleteElement(so3);
    assertEquals(5, _dbrw.size());
  }


  /**
   * @Normal.Test int size()
   */
  @Test
  public void testSize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int nbrObjs = _dbrw.size();
    SomeObject so1 = new SomeObject("first object saved");
    SomeObject so2 = new SomeObject("second object saved");
    SomeObject so3 = new SomeObject("third object saved");

    // Add one element
    _dbrw.addElement(so1);
    assertEquals(nbrObjs + 1, _dbrw.size());

    // Add second element
    _dbrw.addElement(so2);
    assertEquals(nbrObjs + 2, _dbrw.size());

    // Add third element
    _dbrw.addElement(so3);
    assertEquals(nbrObjs + 3, _dbrw.size());

    // Remove two elements
    _dbrw.deleteElement(so1);
    _dbrw.deleteElement(so3);
    assertEquals(nbrObjs + 1, _dbrw.size());

    // Remove remaining last element
    _dbrw.deleteElement(so2);
    assertEquals(nbrObjs, _dbrw.size());
  }


  // ====================================================================
  // SUPPLEMENTAL TESTS
  // ====================================================================

  /**
   * @Normal.Test Supplemental test for {@code get()}
   */
  @Test
  public void testGetCaseInsensitive()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    _dbrw.addElement(new SomeObject("UPPER"));
    assertNotNull(_dbrw.get("upper"));
  }


  /**
   * @Normal.Test Supplemental test for {@code addElement()}
   */
  @Test
  public void testUniqueness()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int nbrBefore = _dbrw.size();

    // Test object not within the db
    SomeObject so = new SomeObject("four");
    assertFalse(_dbrw.contains(so));

    // Add a test object
    _dbrw.addElement(so);
    assertEquals(nbrBefore + 1, _dbrw.size());

    // Fail when trying to add it again
    _dbrw.addElement(so);
    assertEquals(nbrBefore + 1, _dbrw.size());
  }


  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   *
   * @Error.Test Null filename for constructor; force null pointer exception
   */
  @Test
  public void testErrorConstructorNullArg()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    DbReadWriter<SomeObject> newDb = null;
    try {
      newDb = new DbReadWriter<SomeObject>(null);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\tExpected NullPointerException: " + ex.getMessage());
    }
    assertNull(newDb);
  }

  
  /**
   * mylib.dmc.DbReadWriter(String) throws NullPointerException
   *
   * @Error.Test Empty filename for constructor; force null pointer exception
   */
  @Test
  public void testErrorConstructorEmptyArg()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    DbReadWriter<SomeObject> newDb = null;
    try {
      newDb = new DbReadWriter<SomeObject>("  ");
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("\tExpected NullPointerException: " + ex.getMessage());
    }
    assertNull(newDb);
  }


  /**
   * @Error.Test force a NullPointerException object cannot be null
   */
  @Test(expected=NullPointerException.class)
  public void testErrorAddNullObject()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    assertFalse(_dbrw.addElement(null));
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
    assertFalse(_dbrw.contains(so9));
    _dbrw.deleteElement(so9);
  }


  // ====================================================================
  // SUPPLEMENTAL TESTS
  // ====================================================================

  /**
   * DbReadWrite public methods that are not tested because of their simplistic nature.
   * 
   * @Not.Needed isOpen() -- getter
   */
  void notNeeded()
  {}


} // end of TestDbReadWriter class
