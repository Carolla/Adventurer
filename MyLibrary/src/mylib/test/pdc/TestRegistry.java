/**
 * TestRegistry.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;


import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;
import mylib.test.dmc.SomeObject;
import mylib.test.pdc.ConcreteRegistry.MockRegistry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.db4o.query.Predicate;


/**
 * Tests the base class <code>Registry</code> using <code>ConcreteRegistry</code> as its concrete
 * subclass, and <code>SomeObject</code> as its <code>IRegistryElement</code>
 * 
 * @author Timothy Armstrong
 * @author Al Cline
 * @version Jan 23 2011 // original <br>
 *          May 16 2011 // TAA fixed errors <br>
 *          May 23 2011 // TAA ensured all methods included <br>
 *          Dec 24 2011 // ABC revamped for new Registry base clase <br>
 *          Feb 25, 2013 // ABC revamped for native queries in Registry base clase <br>
 *          Sep 13, 2014 // tested fro closeFlag removal <br>
 */
public class TestRegistry extends TestCase
{
  /** Concrete derived class of Registry */
  private ConcreteRegistry _testReg = null;
  /** MockDBRW to access the internal DbReadWriter methods */
  private MockRegistry _mock = null;
  /** Location of test file */
  private final String TEST_FILEPATH = Constants.RESOURCES + "Test.reg";
  /** Temp storage to return Registry back to normal non-test location */
  private String _regLoc = null;

  /** A predicate for retrieving objects by name */
  Predicate<IRegistryElement> _pred = null;

  // ============================================================
  // Fixtures
  // ============================================================

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  @Before
  public void setUp() throws Exception
  {
    // Change Registry location to test registry file
    _regLoc = TEST_FILEPATH;
    // Create a Registry object, which will be initialized if one doesn't exist
    _testReg = new ConcreteRegistry(_regLoc);
    assertNotNull(_testReg);
    _mock = _testReg.new MockRegistry();
    assertNotNull(_mock);

    // Ensure that registry exists with no elements
    File handle = new File(TEST_FILEPATH);
    assertTrue(handle.exists());
    _mock.clearElements();
    assertEquals(0, _testReg.getNbrElements());
  }

  @After
  public void tearDown() throws Exception
  {
    _testReg.deleteRegistry();
    _testReg = null;
    _mock = null;
    // Al messages are OFF after each test
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // BEGIN TESTS
  // ============================================================


  /**
   * @NotImplemented eraseDbFiles(String)
   * @NotImplemented getElementNames()
   * @NotImplemented getString(String)
   * @NotImplemented getUnique(String)
   * @NotImplemented isUnique(String)
   * @NotImplemented queryByPredicate(Predicate<IRegistryElement>)
   */


  /**
   * @Normal Add one or more objects and retrieve them again <br>
   * @Normal Add two identical objects and verify if both are added, or one overwrites the first <br>
   * @Error Verify that null cannot be saved in the db <br>
   */
  public void testAdd()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal Create an object to add and verify
    SomeObject so1 = new SomeObject(4.2, "object one");
    _testReg.add(so1); // object 1 into db
    assertEquals(1, _testReg.getNbrElements());
    assertEquals(so1, _testReg.getUnique("object one"));

    // Normal Add a second object
    SomeObject so2 = new SomeObject(100, "object two");
    _testReg.add(so2); // object 2 into db
    assertEquals(2, _testReg.getNbrElements());
    assertEquals(so2, _testReg.getUnique("object two"));

    // Normal Add two identical objects and verify that the second is not added
    SomeObject so1Copy = new SomeObject(4.2, "object one");
    assertTrue(so1Copy.equals(so1));
    // The copies is rejected because it matches an original
    assertFalse(_testReg.add(so1Copy));
    assertEquals(2, _testReg.getNbrElements());
    assertTrue(_testReg.contains(so2));
    assertTrue(_testReg.contains(so1));
    // Although so1Copy does not exist in the db, a field-level equivalent does
    assertTrue(_testReg.contains(so1Copy));

    // Error Verify that null cannot be saved in the db
    SomeObject empty = null;
    assertFalse(_testReg.add(empty));
    assertEquals(2, _testReg.getNbrElements());
  }


  /**
   * @Normal Add one or more objects and verify they are in the registry <br>
   * @Normal Verify that a deleted object is no longer contained in the registry <br>
   * @Normal Verify that an updated object is still contained in the registry <br>
   * @Error Verify that null cannot be checked to be in the registry <br>
   */
  public void testContains()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal Create an object to add and verify
    SomeObject so1 = new SomeObject(4.2, "object one");
    _testReg.add(so1); // object 1 into db
    assertEquals(1, _testReg.getNbrElements());
    assertTrue(_testReg.contains(so1));

    // Normal Add a second object
    SomeObject so2 = new SomeObject(100, "object two");
    _testReg.add(so2); // object 2 into db
    assertEquals(2, _testReg.getNbrElements());
    assertTrue(_testReg.contains(so2));

    // Normal Verify that a deleted object is no longer contained
    SomeObject so3 = new SomeObject(42, "object three");
    _testReg.add(so3); // object 3 into db
    assertEquals(3, _testReg.getNbrElements());
    _testReg.delete(so1);
    assertEquals(2, _testReg.getNbrElements());
    assertFalse(_testReg.contains(so1));
    assertTrue(_testReg.contains(so2));
    assertTrue(_testReg.contains(so3));

    // Normal Verify that an updated object is still contained
    so3.setNum(-42.0);
    _testReg.update(so3); // object 3 into db
    assertEquals(2, _testReg.getNbrElements());
    assertFalse(_testReg.contains(so1));
    assertTrue(_testReg.contains(so2));
    assertTrue(_testReg.contains(so3));
    SomeObject so42 = (SomeObject) _testReg.getUnique(so3.getKey());
    assertEquals(-42.0, so42.getNum(), .00001);

    // Error Verify that null cannot be saved in the db
    assertFalse(_testReg.contains(null));
    assertEquals(2, _testReg.getNbrElements());
  }


  /**
   * @Normal Delete objects and verify their removal <br>
   * @Error Delete a null object <br>
   * @Error Delete the same object twice <br>
   */
  public void testDelete()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal Create an object to add and verify
    SomeObject so = new SomeObject(4.2, "perfect");
    _testReg.add(so);
    assertEquals(1, _testReg.getNbrElements());
    assertEquals(so, _testReg.getUnique("perfect"));

    // Add a second object
    SomeObject so2 = new SomeObject(100, "one hundred");
    _testReg.add(so2);
    assertEquals(2, _testReg.getNbrElements());
    assertEquals(so2, _testReg.getUnique("one hundred"));

    // Remove the first object and verify the number of elements is reduced...
    _testReg.delete(so);
    assertEquals(1, _testReg.getNbrElements());
    // ... and the second object still exists
    assertTrue(_testReg.contains(so2));

    // Test that both objects no longer exists
    assertFalse(_testReg.contains(so));
    _testReg.delete(so2);
    assertFalse(_testReg.contains(so2));

    // Error No response will be received if trying to deleted a non-existent object
    _testReg.delete(so2);

    // Error delete null object
    try {
      _testReg.delete(null);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsg("\tExpected exception: " + ex.getMessage());
    }
    assertEquals(0, _testReg.getNbrElements());
  }


  /**
   * @Normal Get all elements in the registry <br>
   * @Error Get an element with an null key <br>
   */
  public void testGetAll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal dump all the objects in the registry
    // Add some elements first because setup() cleared the registry
    _mock.init();
    List<IRegistryElement> list = _mock.getAll();
    assertEquals(3, list.size());
    for (int k = 0; k < list.size(); k++) {
      MsgCtrl.msgln("\t" + list.get(k).toString());
    }
    MsgCtrl.msg("\n");

    // Add two and try again
    SomeObject s1 = new SomeObject(1.5, "supplement A");
    SomeObject s2 = new SomeObject(2.5, "supplement B");
    _testReg.add(s1);
    _testReg.add(s2);
    list = _mock.getAll();
    assertEquals(5, list.size());
    for (int k = 0; k < list.size(); k++) {
      MsgCtrl.msgln("\t" + list.get(k).toString());
    }
  }


  /**
   * @Normal Get an element by its key <br>
   * @Normal Change key and try to get it again <br>
   * @Normal Get a list of elements that have the same key <br>
   * @Normal Get an element with an empty (whitespace) key <br>
   * @Error Get an element with an null key <br>
   */
  public void testGetByKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Prepare: Add a few objects to the db
    SomeObject so1 = new SomeObject(1.0, "one");
    SomeObject so2 = new SomeObject(2.0, "two");
    SomeObject so3 = new SomeObject(3.0, "three");
    _testReg.add(so3);
    _testReg.add(so1);
    _testReg.add(so2);
    assertEquals(3, _testReg.getNbrElements());

    // Normal Get an element by its key...
    List<IRegistryElement> elist = _testReg.get(so2.getKey());
    assertEquals(so2, elist.get(0));
    // ... and is same as getUnique()
    assertEquals(so2, _testReg.getUnique(so2.getKey()));

    // Normal Change key and try to get it again
    so3.setKey("threeCopy");
    _testReg.update(so3);
    elist = _testReg.get(so3.getKey());
    assertEquals(so3, elist.get(0));
    // ... and is same as getUnique()
    assertEquals(so3, _testReg.getUnique(so3.getKey()));
    // Ensure that original is not in registry
    assertEquals(0, _testReg.get("three").size());
    assertEquals(1, _testReg.get("threeCopy").size());

    // Normal Get a list of elements that have the same key
    // Add some same non-key values for different keys to get a list
    SomeObject so11 = new SomeObject(1.0, "eleventy");
    SomeObject so12 = new SomeObject(2.0, "eleventy");
    SomeObject so13 = new SomeObject(3.0, "eleventy");
    _testReg.add(so11);
    _testReg.add(so12);
    _testReg.add(so13);
    assertEquals(6, _testReg.getNbrElements());
    // Retrieve the three objects with the same 'eleventy' keyword
    elist = _testReg.get("eleventy");
    assertEquals(3, elist.size());
    for (IRegistryElement s : elist) {
      MsgCtrl.msgln("\t" + s);
    }
  }


  /**
   * @Normal Get an element with an empty (whitespace) key <br>
   * @Error Get an element with an null key <br>
   */
  public void testGetByEmptyKey()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /* Previously commented code */
    // Normal get an object with an empty string name
    List<IRegistryElement> elist = _testReg.get(" ");
    assertEquals(0, elist.size());
    // ... and is same as getUnique()
    assertNull(_testReg.getUnique(" "));
  }


  /**
   * @Normal Get an element by its key <br>
   * @Normal Change key and try to get it again <br>
   * @Normal Get a list of elements that have the same key <br>
   */
  public void testGetByPredicate()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Prepare: Add a few objects to the db
    SomeObject so1 = new SomeObject(1.0, "one");
    SomeObject so2 = new SomeObject(2.0, "two");
    SomeObject so3 = new SomeObject(3.0, "three");
    _testReg.add(so3);
    _testReg.add(so1);
    _testReg.add(so2);
    assertEquals(3, _testReg.getNbrElements());

    // Normal Get an element by its key...
    Predicate<IRegistryElement> pred = keyPredicate(so2);
    List<IRegistryElement> elist = _testReg.get(pred);
    assertEquals(so2, elist.get(0));
    // ... and is same as getUnique()
    assertEquals(so2, _testReg.getUnique(so2.getKey()));

    // Normal Change key and try to get it again
    so3.setKey("threeCopy");
    _testReg.update(so3);
    elist = _testReg.get(keyPredicate(so3));
    assertEquals(so3, elist.get(0));
    // ... and is same as getUnique()
    assertEquals(so3, _testReg.getUnique(so3.getKey()));
    // Ensure that original is not in registry
    assertEquals(0, _testReg.get("three").size());
    assertEquals(1, _testReg.get("threeCopy").size());

    // Normal Get a list of elements that have the same key
    // Add some same non-key values for different keys to get a list
    SomeObject so11 = new SomeObject(1.0, "eleventy");
    SomeObject so12 = new SomeObject(2.0, "eleventy");
    SomeObject so13 = new SomeObject(3.0, "eleventy");
    _testReg.add(so11);
    _testReg.add(so12);
    _testReg.add(so13);
    assertEquals(6, _testReg.getNbrElements());
    // Retrieve the three objects with the same 'eleventy' keyword (that matches so11)
    elist = _testReg.get(keyPredicate(so11));
    assertEquals(3, elist.size());
    for (IRegistryElement s : elist) {
      MsgCtrl.msgln("\t" + s);
    }

    // Error get an object with an empty string name
    SomeObject emptySO = new SomeObject(-99.0, " ");
    assertFalse(_testReg.add(emptySO));
  }


  /**
   * @Normal Ensure that one object is swapped for a newer one <br>
   * @Normal Ensure that one object is swapped for the itself (same incident) without incident <br>
   * @Normal Ensure that one object is swapped for different one but of the same field values <br>
   * @Normal Try to replace an object when more than one is in the registry <br>
   * @Error Try to replace an object that does not exist in the registry <br>
   * @Null Replace an object with a null object <br>
   */
  public void testUpdate()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal Ensure that one object is swapped for a newer one
    // First, place an object in the registry
    SomeObject so1 = new SomeObject(1.0, "one");
    assertTrue(_testReg.add(so1));
    MsgCtrl.msg("\tObject stored: " + so1.toString());
    MsgCtrl.msgln("\tRegistry contains " + _testReg.getNbrElements() + " elements.");
    assertEquals(1, _testReg.getNbrElements());

    // Normal update it with a different object but attempt should fail
    SomeObject so2 = new SomeObject(2.0, "two");
    assertFalse(_testReg.update(so2));
    assertEquals(1, _testReg.getNbrElements());

    // Normal Ensure that one object is swapped for the itself (same incident) without incident
    assertTrue(_testReg.add(so2));
    assertTrue(_testReg.contains(so2));
    assertTrue(_testReg.update(so2));
    MsgCtrl.msgln("\tObject swapped with itself: " + _testReg.get(so2.getKey()));
    MsgCtrl.msgln("\tRegistry still contains " + _testReg.getNbrElements() + " elements.");

    // Normal Ensure that one object is swapped for different one but of the same field values
    // Now try the update: same fields but different instance
    SomeObject so1Copy = new SomeObject(1.0, "one");
    // This update will try to get around the uniqueness issue
    assertTrue(_testReg.update(so1Copy));
    MsgCtrl.msgln("\tObject tried to update to make a duplicate...");
    MsgCtrl.msgln("\t...but update bypassed, so Registry still contains "
        + _testReg.getNbrElements() + " elements.");

    // Error Try to replace an object that does not exist in the registry
    SomeObject soNone = new SomeObject(-99.0, "None");
    assertFalse(_testReg.update(soNone));
    assertEquals(2, _testReg.getNbrElements());
    MsgCtrl.msgln("\tOriginal not found, so registry still contains "
        + _testReg.getNbrElements() + " elements.");

    // Null Replace an object with a null object
    assertFalse(_testReg.update(null));
  }


  /*********************************************************************************************************
   * PRIVATE METHODS
   **********************************************************************************************************/

  /**
   * Create a target predicate to match against the db candidate's key
   * 
   * @param target object to find
   * @return the Predicate with the proper match() method
   */
  private Predicate<IRegistryElement> keyPredicate(final SomeObject target)
  {
    // Suppression needed for the annoymous inner class
    @SuppressWarnings("serial")
    Predicate<IRegistryElement> pred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return target.getKey().equals(candidate.getKey());
      }
    };
    return pred;
  }


  /**
   * 9 methods
   * 
   * @NotNeeded Registry() -- empty constructor for testing <br>
   * @NotNeeded Registry(String) -- abstract constructo
   * @NotNeeded initialize() -- abstract method <br>
   * @NotNeeded isClosed() -- getter (perhaps not needed at all) <br>
   * @NotNeeded closeRegistry() -- wrapper for DBReadWriter <br>
   * @NotNeeded deleteRegistry() -- wrapper for DBReadWriter <br>
   * @NotNeeded getReference() -- method to be removed; does nothing <br>
   * @NotNeeded getDBRW() -- getter <br>
   * @NotNeeded getNbrElements() -- getter <br>
   */
  void _testsNotNeeded()
  {}


} // end of TestRegistry class

