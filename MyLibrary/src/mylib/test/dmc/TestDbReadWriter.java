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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;
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
public class TestDbReadWriter
{
    /** Object under test */
    private DbReadWriter _regRW = null;
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
    }

    /**
     * Closes db, delete it and its mock to null state, db file used for test is deleted
     * 
     * @throws java.lang.Exception to catch unexcepted things
     */
    @After
    public void tearDown()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        
        _regRW.dbClear();
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
        try {
            new DbReadWriter(null);
            fail("No exception thrown");
        } catch (NullPointerException ex) {

        }
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
        assertNotNull(_regRW);
        assertTrue(_regFile.exists());
        // Display message that db was created in setUp()
        MsgCtrl.msgln("\tClosed database " + _regRW.getClass().getCanonicalName());
        // Confirm that file is now in place
        long fileLen = _regFile.length();
        MsgCtrl.msgln("\tWrote file " + _regRW.getPath() + "\t:" + fileLen + " bytes.");
        assertTrue(_regFile.exists());

        // Now try to create DBRW and confirm that it reused existing one, and that same file is
        // used
        // Create new registry, open database and read-write file (default config)
        _regRW = new DbReadWriter(REG_PATH);
        assertFalse(oldRW == _regRW);
        fileLen = _regFile.length();
        MsgCtrl.msgln("\tNewly loaded file " + _regRW.getPath() + "\t:" + fileLen + " bytes.");
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
    public void addObjectIncreasesSize()
    {
        MsgCtrl.where(this);

        int beforeNbr = _regRW.size();
        SomeObject defaultSO = new SomeObject(11, "default");
        SomeObject setSO = new SomeObject(42, "second object");

        _regRW.addElement(defaultSO);
        _regRW.addElement(setSO);
        assertEquals(beforeNbr + 2, _regRW.size());
    }


    /**
     * @Error.Test force a NullPointerException object cannot be null
     */
    @Test
    public void addingNullObjectThrowsError()
    {
        MsgCtrl.where(this);
        try {
            _regRW.addElement(null);
            fail("No exception thrown when adding null element");
        } catch (NullPointerException ex) {
            // Succeed
        }
    }

    /**
     * @Normal.Test Add a normal object to a db
     */
    @Test
    public void addingObjectSucceeds()
    {
        MsgCtrl.where(this);
        
        SomeObject so = new SomeObject(-1, "negative");
        _regRW.addElement(so);
    }
    
    /**
     * boolean mylib.dmc.DbReadWriter.dbContains(IRegistryElement) throws DatabaseClosedException
     * 
     * @Normal.Test objects in the db are identified
     */
    @Test
    public void insertedObjectsAreFoundInTheDatabase()
    {
        MsgCtrl.where(this);

        // Normal Add a few objects and check that they are known
        SomeObject so1 = new SomeObject(1, "one");
        SomeObject so2 = new SomeObject(2, "two");
        SomeObject so3 = new SomeObject(3, "three");
        assertFalse(_regRW.containsElement(so2));
        assertFalse(_regRW.containsElement(so1));
        assertFalse(_regRW.containsElement(so3));

        _regRW.addElement(so1);
        _regRW.addElement(so2);
        _regRW.addElement(so3);

        assertTrue(_regRW.containsElement(so2));
        assertTrue(_regRW.containsElement(so1));
        assertTrue(_regRW.containsElement(so3));
    }
    
    @Test
    public void objectsAddedMoreThanOnceOnlyAppearOnce()
    {
        MsgCtrl.where(this);
        int nbrBefore = _regRW.size();
        
        // Error Try for variations on this object
        SomeObject so4 = new SomeObject(4, "four");
        assertFalse(_regRW.containsElement(so4));

        // Add another one
        _regRW.addElement(so4);
        assertEquals(nbrBefore + 1, _regRW.size());
        _regRW.addElement(so4);
        assertEquals(nbrBefore + 1, _regRW.size());
        assertTrue(_regRW.containsElement(so4));
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
        final SomeObject t1 = new SomeObject(212, "degrees");
        final SomeObject t2 = new SomeObject(32, "degrees");
        final SomeObject t3 = new SomeObject(0, "radians");
        _regRW.addElement(t1);
        _regRW.addElement(t2);
        _regRW.addElement(t3);

        // NORMAL Retrieve using a matching object predicate
        Predicate<IRegistryElement> objPred = new Predicate<IRegistryElement>() {
            public boolean match(IRegistryElement candidate)
            {
                return candidate.equals(t2);
            }
        };
        List<IRegistryElement> soList = _regRW.query(objPred); // input the predicate
        assertEquals(1, soList.size());

        // NORMAL Retrieve using a key matching predicate
        Predicate<IRegistryElement> keyPred = new Predicate<IRegistryElement>() {
            public boolean match(IRegistryElement candidate)
            {
                return candidate.getKey().equals(t2.getKey());
            }
        };
        soList = _regRW.query(keyPred); // new predicate
        assertEquals(2, soList.size());

        // NORMAL Retrieve using a predicate that matches everything
        Predicate<IRegistryElement> allPred = new Predicate<IRegistryElement>() {
            public boolean match(IRegistryElement candidate)
            {
                return true;
            }
        };
        soList = _regRW.query(allPred); // new predicate
        assertEquals(3, soList.size());
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
        soList = _regRW.query(nonePred); // new predicate
        assertEquals(0, soList.size());
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

        SomeObject so1 = new SomeObject(12, "soon to be dead");
        SomeObject so2 = new SomeObject(24, "tried to be RO read");

        // Normal: Delete an existing object from the db
        _regRW.addElement(so1);
        MsgCtrl.msgln("\tDb contains " + _regRW.size() + " elements");
        assertEquals(1, _regRW.size());
        _regRW.deleteElement(so1);
        MsgCtrl.msgln("\tDb contains " + _regRW.size() + " elements");
        assertEquals(0, _regRW.size());

        // Normal Try to delete the same object twice: silent fail
        _regRW.deleteElement(so1);
        MsgCtrl.msgln("\tDb contains " + _regRW.size() + " elements");
        assertEquals(0, _regRW.size());

        // Error: Close the database and try again
        try {
            _regRW.deleteElement(so1);
        } catch (DatabaseClosedException ex) {
            MsgCtrl.errMsgln("\tExpected exception: " + "Closed (null) database");
        }

        // Error: Set the database to readOnly and try again
        // Create new registry, open database and read-write file (default config)
        _regRW = new DbReadWriter(REG_PATH);
        assertNotNull(_regRW);
        _regRW.addElement(so1);
        _regRW.addElement(so2);
        _regRW.setReadOnly(false);
        MsgCtrl.msgln("\tDb contains " + _regRW.size() + " elements");
        assertEquals(2, _regRW.size());
        try {
            _regRW.deleteElement(so1);
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
        SomeObject so = new SomeObject(1, "test reliving through dbClose()");

        // NORMAL: Write to file, close db, then re-read previously written object
        _regRW.addElement(so);
        assertTrue(_regRW.containsElement(so));
    }

    /**
     * @Error.Test force a DatabaseReadOnlyException Change the config to RO and try to add
     */
    @Test
    public void testDbReadOnly()
    {
        MsgCtrl.where(this);

        _regRW.setReadOnly(true);

        try {
            _regRW.addElement(new SomeObject(111, "ones"));
            fail("No ReadOnlyException thrown");
        } catch (DatabaseReadOnlyException ex) {
            
        }
        
        _regRW.setReadOnly(false);
    }

    /**
     * @Normal.Test Ensure that the correct number of objects stored in the db is returned
     */
    @Test
    public void sizeIsNumberOfElements()
    {
        MsgCtrl.where(this);

        int nbrObjs = _regRW.size();
        SomeObject so1 = new SomeObject(1, "first object saved");
        SomeObject so2 = new SomeObject(2, "second object saved");
        SomeObject so3 = new SomeObject(3, "third object saved");

        _regRW.addElement(so1);
        assertEquals(nbrObjs + 1, _regRW.size());
 
        _regRW.addElement(so2);
        assertEquals(nbrObjs + 2, _regRW.size());
        
        _regRW.addElement(so3);
        assertEquals(nbrObjs + 3, _regRW.size());

        // Now delete two and try again
        _regRW.deleteElement(so3);
        assertEquals(nbrObjs + 2, _regRW.size());

        _regRW.deleteElement(so2);
        assertEquals(nbrObjs + 1, _regRW.size());

        _regRW.deleteElement(so1);
        assertEquals(nbrObjs, _regRW.size());
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
