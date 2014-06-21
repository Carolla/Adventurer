/**
 * TestArena.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc.buildings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Arena;

/**
 *    Verify that the Arena Class works as expected 
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Feb 6, 2013   // original <DD>
 * </DL>
 */
public class TestArena
{
    /** Target object */
    static private Arena _arena = null;
    /** Expected value for name */
    static private String NAME = "Test Dungeon";

    /** Expected value for first impression description */
    private String INTRO = "Of all the dungeons in all the world, she had to kill me in mine.";


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  STATE HANDLING METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** The target and file is created before any test */
    @BeforeClass
    public static void runOnce()
    {
    }
    
    /** The target file is deleted after all tests */
    @AfterClass
    public static void cleanUp()
    {
    }
    
    /**
     * Each test starts with a target Arena in place
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _arena = Arena.getInstance(NAME);
        assertNotNull(_arena);
        _arena.setIntro(INTRO);
    }

    /**
     * The target is closed after each test (but its file remains)
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _arena.close();
        _arena = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  BEGIN TESTING 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Test constructor to create an arena only if it does not already exist
     * @Normal  Create an arena only if it does not exist
     * @Normal  Return null for non-existing file without creating one
     * @Error      Null name parm
     * @throws Exception for anything else unexpected
     */
    @Test
    public void testArena() throws Exception
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msgln(this, "\t testArena()");

        // Get the arena from the "registry" and check it for integrity
        _arena = Arena.getInstance(NAME);
        assertNotNull(_arena);
        
        String dgnName = _arena.getName();
        String intro  = _arena.getIntro();
        int nbrRooms = _arena.getNbrRooms();
        MsgCtrl.msgln("\t This dungeon is called " + dgnName);
        MsgCtrl.msgln("\t " + intro);
        MsgCtrl.msgln("\t It contains " + nbrRooms + " Rooms.");
        assertNotNull(dgnName);
        assertNotNull(intro);
        assertEquals("Zero rooms expected, but not found", 0, nbrRooms);
    }


    /** Test that Arena will not take a null name */ 
    @Test
    public void testNullArena() throws Exception
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msgln(this, "\t testNullArena()");

        // ERROR null name parm; current arena must be closed already
        try {
            Arena.getInstance(null);
        } catch (NullPointerException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
    }
    
//
//    /** Verify that the target object is saved to the file
//     * @Normal  save the object to the file and check file size
//     * @throws Exception for anything unexpected
//     */
//    @Test
//    public void testSave() throws Exception
//    {
//        MsgCtrl.auditMsgsOn(true);
//        MsgCtrl.errorMsgsOn(true);
//        MsgCtrl.msgln(this, "\t testSave()");
//
//        // NORMAL Check file size of arena before and after save
//        // Ensure that there is no arena file
//        _arena.close(true);
//        File dgn = new File(NAME_PATH);
//        assertEquals(dgn.exists(), false);
//        MsgCtrl.msgln("\t Arena size before saving = " + dgn.length());
//        // Now create a new Arena
//        _arena = Arena.getInstance(NAME);
//        // Save arena and check filesize
//        try {
//            _arena.save();
//            _arena.close(false);
//            assertEquals(dgn.exists(), true);
//            MsgCtrl.msgln("\t Arena size after saving = " + dgn.length());
//        } catch (Exception ex) {
//            MsgCtrl.errMsg("\t " + ex.getMessage());
//        }
//    }
    
    
    /** Chronos.pdc.Arena
     * @Not_Needed  getName()                             // getter
     * @Not_Needed  close(boolean)                      // support method, tested in state prep/cleanup
     */
    public void not_Needed() { }
    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}           // end of TestArena class
