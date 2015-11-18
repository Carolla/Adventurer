/**
 * TestRegistry.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;
import mylib.dmc.IRegistryElement;
import mylib.test.dmc.SomeObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.db4o.query.Predicate;


/**
 * Tests the base class <code>Registry</code> using <code>ConcreteRegistry</code> as its concrete
 * subclass, and <code>SomeObject</code> as its <code>IRegistryElement</code>
 * 
 * @author Timothy Armstrong
 * @author Al Cline
 * @version Jan 23, 2011 // original <br>
 *          May 16, 2011 // TAA fixed errors <br>
 *          May 23, 2011 // TAA ensured all methods included <br>
 *          Dec 24, 2011 // ABC revamped for new Registry base clase <br>
 *          Feb 25, 2013 // ABC revamped for native queries in Registry base clase <br>
 *          Sep 13, 2014 // tested for closeFlag removal <br>
 *          Sep 27, 2014 // pruned again, looking for missed bug <br>
 *          Dec 7, 2014 // added specific getUnique() tests<br>
 */
public class TestRegistry
{
    public class FakeDbReadWriter extends DbReadWriter<SomeObject>
    {
        private List<SomeObject> _objects;

        public FakeDbReadWriter(String filepath)
        {
            super(filepath);
            _objects = new ArrayList<SomeObject>();
        }
        
        @Override
        public void addElement(IRegistryElement obj)
        {
            _objects.add((SomeObject) obj);
        }
        
        @Override
        public boolean containsElement(IRegistryElement obj)
        {
            return _objects.contains((SomeObject) obj);
        }
        
        @Override
        public boolean deleteElement(IRegistryElement obj)
        {
            return _objects.remove(((SomeObject) obj));
        }

        @Override
        public List<SomeObject> query(Predicate<SomeObject> pred)
        {
            List<SomeObject> list = new ArrayList<SomeObject>();
            for (SomeObject so : _objects) {
                if (pred.match(so)) {
                    list.add(so);
                }
            }
            return list;
        }
    }

    /** Concrete derived class of Registry */
    private ConcreteRegistry _testReg = null;
    /** Location of test file */
    private static final String TEST_FILEPATH = Constants.MYLIB_RESOURCES + "Test.reg";
    /** Temporary Test file */
    private static File _testfile = null;

    /** A predicate for retrieving objects by name */
    Predicate<IRegistryElement> _pred = null;


    // ============================================================
    // Fixtures
    // ============================================================

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {}


    /** Remove the leftover test file */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        _testfile.delete();
        assertFalse(_testfile.exists());
    }


    @Before
    public void setUp() throws Exception
    {
        // Create a Registry object, which will be initialized if one doesn't exist
        _testReg = new ConcreteRegistry(TEST_FILEPATH);
        
        // Insert the test double DbReadWriter
        _testReg.setDbReadWriter(new FakeDbReadWriter("Doesn't matter"));

        // Ensure that registry exists with no elements
        _testfile = new File(TEST_FILEPATH);
        assertTrue(_testfile.exists());
        assertEquals(0, _testReg.getNbrElements());
    }

    @After
    public void tearDown() throws Exception
    {
        _testReg.closeRegistry();
        // All messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


    // ============================================================
    // BEGIN TESTS
    // ============================================================

    /**
     * mylib.test.pdc.add(IRegistryElement)
     * 
     * @Normal.Test Add one or more objects and retrieve them again <br>
     * @Normal.Test Add two identical objects and verify if both are added, or one overwrites the
     *              first <br>
     * @Error.Test Verify that null cannot be saved in the db <br>
     */
    @Test
    public void testAdd()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.where(this);

        // Normal Create an object to add and verify
        SomeObject so1 = new SomeObject(4, "object one");
        _testReg.add(so1); // object 1 into db
        assertEquals(1, _testReg.getNbrElements());
        assertEquals(so1, _testReg.getUnique("object one"));

        // Normal Add a second object
        SomeObject so2 = new SomeObject(100, "object two");
        _testReg.add(so2); // object 2 into db
        assertEquals(2, _testReg.getNbrElements());
        assertEquals(so2, _testReg.getUnique("object two"));

        // Normal Add two identical objects and verify that the second is not added
        SomeObject so1Copy = new SomeObject(4, "object one");
        assertTrue(so1Copy.equals(so1));
        // The copy is rejected because it matches an original
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
     * mylib.test.pdc.contains(IRegistryElement)
     * 
     * @Normal.Test Add one or more objects and verify they are in the registry <br>
     * @Normal.Test Verify that a deleted object is no longer contained in the registry <br>
     * @Normal.Test Verify that an updated object is still contained in the registry <br>
     * @Error.Test Verify that null cannot be checked to be in the registry <br>
     */
    @Test
    public void testContains()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        // Normal Create an object to add and verify
        SomeObject so1 = new SomeObject(4, "object one");
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
        so3.setNum(-42);
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
     * mylib.test.pdc.delete(IRegistryElement)
     * 
     * @Normal.Test Delete objects and verify their removal <br>
     * @Error.Test Delete a null object <br>
     * @Error.Test Delete the same object twice <br>
     */
    @Test
    public void testDelete()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        // Normal Create an object to add and verify
        SomeObject so = new SomeObject(4, "perfect");
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
     * mylib.test.pdc.get(String)
     * 
     * @Normal.Test Get an element by its key <br>
     * @Normal.Test Change key and try to get it again <br>
     * @Normal.Test Get a list of elements that have the same key <br>
     * @Normal.Test Get an element with an empty (whitespace) key <br>
     * @Error.Test Get an element with an null key <br>
     */
    @Test
    public void testGet_ByString()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        // Prepare: Add a few objects to the db
        SomeObject so1 = new SomeObject(1, "one");
        SomeObject so2 = new SomeObject(2, "two");
        SomeObject so3 = new SomeObject(3, "three");
        _testReg.add(so3);
        _testReg.add(so1);
        _testReg.add(so2);
        assertEquals(3, _testReg.getNbrElements());

        // Normal Get an element by its key...
        List<SomeObject> elist = _testReg.get(so2.getKey());
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
        SomeObject so11 = new SomeObject(1, "eleventy");
        SomeObject so12 = new SomeObject(2, "eleventy");
        SomeObject so13 = new SomeObject(3, "eleventy");
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
     * mylib.test.pdc.get(String)
     * 
     * @Error.Test No element should exist with an empty (whitespace) key <br>
     * @Error.Test Get an element with an null key <br>
     */
    @Test
    public void testGet_ByEmptyKey()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        /* Previously commented code */
        // Normal get an object with an empty string name
        List<SomeObject> elist = _testReg.get(" ");
        assertEquals(0, elist.size());
        // ... and is same as getUnique()
        assertNull(_testReg.getUnique(" "));
    }


    /**
     * mylib.test.pdc.getAll()
     * 
     * @Normal.Test Get all elements in the registry <br>
     * @Error.Test Get an element with an null key <br>
     */
    @Test
    public void testGetAll()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        // Normal dump all the objects in the registry
        // Add some elements first because setup() cleared the registry
        _testReg.initialize();
        List<SomeObject> list = _testReg.getAll();
        assertEquals(3, list.size());
        for (int k = 0; k < list.size(); k++) {
            MsgCtrl.msgln("\t" + list.get(k).toString());
        }
        MsgCtrl.msg("\n");

        // Add two and try again
        SomeObject s1 = new SomeObject(1, "supplement A");
        SomeObject s2 = new SomeObject(2, "supplement B");
        _testReg.add(s1);
        _testReg.add(s2);
        list = _testReg.getAll();
        assertEquals(5, list.size());
        for (int k = 0; k < list.size(); k++) {
            MsgCtrl.msgln("\t" + list.get(k).toString());
        }
    }


    /**
     * mylib.test.pdc.getUnique(String)
     * 
     * @Normal.Test Get an element by its name <br>
     * @Normal.Test Get the same element again <br>
     * @Error.Test Attempt an element that is not stored <br>
     * @Error.Test Attempt an element by its non-key value <br>
     * @Null.Test Attempt to retrieve with null key <br>
     */
    @Test
    public void testGetUnique()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        // Concrete Registry already created with 0 elements by setup()
        assertEquals(_testReg.getNbrElements(), 0);

        // Add get a unique object from the list
        SomeObject obj = new SomeObject(11, "key");
        assertNotNull(obj);
        _testReg.add(obj);
        assertEquals(_testReg.getNbrElements(), 1);

        // Retrieve the object by name
        IRegistryElement elem = _testReg.getUnique("key");
        assertEquals("key", obj.getKey());
        assertEquals("key", elem.getKey());

        // Retrieve the same object by name again
        IRegistryElement elem2 = _testReg.getUnique("key");
        assertEquals("key", obj.getKey());
        assertEquals("key", elem2.getKey());
        assertEquals(elem, elem2);

        // Attempt to get an object by its non-key value
        IRegistryElement nonKeyElem = _testReg.getUnique("11.2");
        assertNull(nonKeyElem);

        // Attempt to get a null value
        IRegistryElement elem3 = _testReg.getUnique(null);
        assertNull(elem3);
    }


    /**
     * mylib.test.pdc.update(RegistryElement)
     * 
     * @Normal.Test Ensure that one object is swapped for a newer one <br>
     * @Normal.Test Ensure that one object is swapped for the itself (same incident) without
     *              incident <br>
     * @Normal.Test Ensure that one object is swapped for different one but of the same field values <br>
     * @Normal.Test Try to replace an object when more than one is in the registry <br>
     * @Error.Test Try to replace an object that does not exist in the registry <br>
     * @Null.Test Replace an object with a null object <br>
     */
    @Test
    public void testUpdate()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);

        // Normal Ensure that one object is swapped for a newer one
        // First, place an object in the registry
        SomeObject so1 = new SomeObject(1, "one");
        assertTrue(_testReg.add(so1));
        MsgCtrl.msg("\tObject stored: " + so1.toString());
        MsgCtrl.msgln("\tRegistry contains " + _testReg.getNbrElements() + " elements.");
        assertEquals(1, _testReg.getNbrElements());

        // Normal update it with a different object but attempt should fail
        SomeObject so2 = new SomeObject(2, "two");
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
        SomeObject so1Copy = new SomeObject(1, "one");
        // This update will try to get around the uniqueness issue
        assertTrue(_testReg.update(so1Copy));
        MsgCtrl.msgln("\tObject tried to update to make a duplicate...");
        MsgCtrl.msgln("\t...but update bypassed, so Registry still contains "
                + _testReg.getNbrElements() + " elements.");

        // Error Try to replace an object that does not exist in the registry
        SomeObject soNone = new SomeObject(-99, "None");
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
     * 3 methods
     * 
     * @Not.Needed initialize() -- abstract method <br>
     * @Not.Needed getDBRW() -- getter <br>
     * @Not.Needed getNbrElements() -- getter <br>
     * @Not.Needed Registry(String) -- abstract base class <br>
     */
    void _testsNotNeeded()
    {}

    /**
     * 5 methods
     * 
     * @Not.Implemented getElementNames() <br>
     * @Not.Implemented isClosed() -- can this method be removed? <br>
     * @Not.Implemented isUnique(String) -- is this method actually needed? <br>
     */
    void _testsToBeImplemented()
    {}

} // end of TestRegistry class

