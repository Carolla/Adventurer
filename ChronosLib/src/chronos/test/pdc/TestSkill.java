/**
 * TestSkill.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Skill;

/**
 *    Test the Skill class
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Feb 10, 2013   // original <DD>
 * </DL>
 */
public class TestSkill // extends TestCase
{
    /** target object */
    private Skill _skill = null;
    
    /** Test skill */
    private final String NAME = "--TestSkill";
    private final String DESC = "--Test description";
    private final String LONGNAME = "This Skill name exceeds the required limits of 35 characters";
    private final String LONGDESC = "This description exceeds the required limits of 70 characters"+
                    " in order to force an ApplicationException to be thrown.";
    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  SETUP / TEARDOWN 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Nothing to do */
    @BeforeClass
    public static void runOnce() { }
    
    /** Nothing to undo */
    @AfterClass
    public static void cleanUp() { } 

    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _skill = new Skill(NAME,DESC);
        assertNotNull(_skill);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _skill = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  BEGIN TESTS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** chronos.pdc.Skill()
     * @Normal  two-parm constructor
     * @Error null for either or both parms 
     * @Error force exception for description too long 
     * @Error force exception for name too long 
     */
    @Test
    public void testCtor()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t Skill(name, desc)");
        
        // NORMAL  confirm Skill createdin setup()
        MsgCtrl.msgln("\t " + _skill.toString());
        assertEquals(NAME, _skill.getName());
        assertEquals(DESC, _skill.getDescription());
        
        // ERROR set parms to null to force exception
        Skill errSkill = null;
        try {
            errSkill = new Skill(null, DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln("\t Expected exception: " + ex.getMessage());
            assertNull(errSkill);
        }
        try {
            errSkill = new Skill(NAME, null);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln("\t Expected exception: " + ex.getMessage());
            assertNull(errSkill);
        }
        try {
            errSkill = new Skill(null, null);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln("\t Expected exception: " + ex.getMessage());
            assertNull(errSkill);
        }
        
        // ERROR force exception for description too long 
        try {
            errSkill = new Skill(NAME, LONGDESC);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln("\t Expected exception: " + ex.getMessage());
            assertTrue(Skill.MAX_DESC_LEN < LONGDESC.length());
        }
        try {
            errSkill = new Skill(LONGNAME, DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln("\t Expected exception: " + ex.getMessage());
            assertTrue(Skill.MAX_NAME_LEN < LONGNAME.length());
        }
    }

    
    /** chronos.pdc.Skill()
     * @Normal Verify same class, name and description return true
     * @Error   Verify different names returns false
     * @Error   Verify different descriptions return false
     * @Error  Verify different classes return false
     * @Error  Verify different objects return false
     * @Error  Pass null object; expect false
     * @throws ApplicationException unexpected throw from Skill() ctor
     */
    @Test
    public void testEquals() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);          // turn these off for expected errors now
        String ts1Name = "DM'ing";
        String ts1Desc = "Able to create mazes and challenging adventures";
        
        // Normal   Verify same class, name and description returns true
        Skill tSkill_1 = new Skill(NAME, DESC);
        assertTrue(_skill.equals(tSkill_1));
        
        // Error   Verify different names returns false
        tSkill_1 = new Skill(ts1Name, DESC);
        assertFalse(_skill.equals(tSkill_1));
        
        // Error   Verify different descriptions return false
        tSkill_1 = new Skill(NAME, ts1Desc);
        assertFalse(_skill.equals(tSkill_1));
        
        // Error  Verify different classes return false
        assertFalse(_skill.equals(ts1Name));
        // how would this test be possible? - all Skills are of Skill class
        
        // Error  Verify different objects return false
        tSkill_1 = _skill;
        assertTrue(_skill.equals(tSkill_1));
        
        // Error  Pass null object; expect false
        assertFalse(_skill.equals(null));

    }

    
    // TODO Test Skill.equals method
    /** List of Skill methods not yet implemented in this test class
     * @Not_Needed Skill.getDescription()             simple getter
     * @Not_Needed Skill.getName()                     simple getter
     * @Not_Needed Skill.toString()                       simple diagnostic message
     */
    public void Not_Needed() { } 

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}           // end of TestSkill class
