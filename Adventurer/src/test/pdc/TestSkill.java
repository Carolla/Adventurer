/**
 * TestSkill.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import chronos.pdc.Skill;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *	Test the simple Skill class that comprises the SkillRegistry collection
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Jan 5 2010   // original <DD>
 * <DT> Build 1.1       Aug 22 2010   // added QA tags <DD>
 * <DT> Build 1.2       Apr 20 2011  // TAA function/tags confirmed <DD>
 * <DT> Build 1.3       May 15 2011  // TAA function confirmed <DD>
 * <DT> Build 1.4       May 22 2011 // TAA added toObject and toRec tests <DD>
 * <DT> Build 1.5       Jun 13 2011 // TAA tweaked for integration tests <DD>
 * <DT> Build 2.0       Jun 30 2011 // TAA added unpackShuttle and loadShuttle <DD>
 * </DL>
 */
public class TestSkill extends TestCase
{
    private Skill _skill = null;

    final int NOT_FOUND = -1;

    // Test skill to use for default
    private final String _skName = "DM'ing";
    private final String _skDesc = "Able to create mazes and challenging adventures";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // Error messages are ON at start of each test 
        MsgCtrl.errorMsgsOn(true);
        // Audit messages are OFF at start of each test
        MsgCtrl.auditMsgsOn(false);
        // Create an Occupation object, and its mock
        _skill = new Skill(_skName, _skDesc);
    }

    @After
    public void tearDown()
    {
        _skill = null;
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 				BEGIN TESTING
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Test the Occupation(String name, String weightCode) ctor
     * @Normal Skill.Skill(String name, String desc, String action)     used in setUp()
     * @Error    Skill.Skill(String name, String desc, String action)      long desc
     */
    public void testCtorNormalError()
    {
        MsgCtrl.auditMsgsOn(false);
        // turn these off for expected errors now
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "testCtorNormalError(): ");
        // Create an Occupation and confirm its attributes

        // Normal
        MsgCtrl.msgln("\t" + _skill.getName() + " = " + _skill.getDescription());
        assertTrue(_skill.getName().equals(_skName));
        assertTrue(_skill.getDescription().equals(_skDesc));

        // ERROR
        Skill aSkill = null;
        // Expected exception for overly long description (len = 70)
        String longDesc = "This is a very long description that is testing the Chronos"
                +
                " Exception handling facility of the Skill constructor.";
        try {
            aSkill = new Skill("Skillname", longDesc);
            MsgCtrl.msgln("\t" + aSkill.getName() + " = "
                    + aSkill.getDescription());
            MsgCtrl.errMsgln("\tException not thrown");
            fail();
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln("\tExpected Exception: " + ex.getMessage());
        }
    }

    /** Test the Occupation(String name, String weightCode) ctor
     * @Null      Skill.Skill(String name, String desc, String action)      
     */
    public void testCtorNull() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);          // turn these off for expected errors now
        Skill aSkill = null;

        // NULL name test
        try {
            aSkill = new Skill(null, "Some Desc");
            assertNull(aSkill);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, "\tExpected Exception: " + ex.getMessage());
        }
        // NULL Description test
        try {
            aSkill = new Skill("Skillname", null);
            assertNull(aSkill);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, "\tExpected Exception: " + ex.getMessage());
        }
        //        // NULL action can be null
        //        aSkill = new Skill("Skillname", "Skil Description", null);
        //        assertTrue(aSkill != null);
        //        MsgCtrl.msgln("\t" + aSkill.getName() + " = " + aSkill.getDescription()
        //                + " : "
        //                + aSkill.getAction());
    }

    @Test
    public void testEquals()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);          // turn these off for expected errors now
        String ts1Name = "DM'ing";
        String ts1Desc = "Able to create mazes and challenging adventures";
        Skill tSkill_1 = null;
        try {
            tSkill_1 = new Skill(ts1Name, ts1Desc);
        } catch (ApplicationException e) {
            MsgCtrl.errMsg(e.getMessage());
            System.exit(0);
        }

        //Normal Test
        assertTrue(_skill.equals(tSkill_1));
    }

    //    /** Test the conversion to a Record from a skill Object
    //     * @throws ChronosException 
    //     * @Normal      Skill.toRecord(Object skill)                  ok
    //     * @Error       Skill.toRecord(Object skill)                  N/A
    //     * @Null        Skill.toRecord(Object skill)                  ok 
    //     */ 
    //     public void testToRec() throws ChronosException
    //     {
    //         MsgCtrl.auditMsgsOn(false);
    //         MsgCtrl.errorMsgsOn(false);
    //         MsgCtrl.msgln(this, "testToRec()");
    //
    //         // NORMAL testing
    //         // Convert the test skill and make sure that it converted properly
    //         String record = _skill.toRec(_skill);
    //         MsgCtrl.msgln("\t" + record);
    //       
    //         Skill emptySkill = new Skill("","","");
    //         record = emptySkill.toRec(emptySkill);
    //         MsgCtrl.msgln("\t" + record);
    //         
    //         // ERROR
    //         // Cannot come up with a test for this
    //         
    //         // NULL 
    //         // Pass a null skill
    //         emptySkill = null;
    //         try
    //         {
    //             record = _skill.toRec(emptySkill);
    //         }
    //         finally
    //         {
    //             MsgCtrl.errMsgln("\tError expected.\n");
    //         }
    //     }
    //     
    //     /** Test the conversion from a record to a skill object
    //      * @Normal      Skill.toObject(String[] record)                  ok
    //      * @Error       Skill.toObject(String[] record)                  ok
    //      * @Null        Skill.toObject(String[] record)                  N/A
    //      */ 
    //      public void testToObject()
    //      {
    //          MsgCtrl.auditMsgsOn(false);
    //          MsgCtrl.msgln(this, "\ttestToObject()");
    //
    //          // NORMAL testing
    //          // Convert the test skill and make sure that it converted properly
    //          String[] record = {_skName, _skDesc, _skAction};
    //          Skill skill = new Skill();
    //          skill = (Skill) skill.toObject(record);
    //          assertEquals (skill.getName(), record[0]);
    //          assertEquals (skill.getDescription(), record[1]);
    //          assertEquals (skill.getAction(), record[2]);
    //          
    //          String[] record2 = {"","",""};
    //          skill = (Skill) skill.toObject(record2);
    //          assertEquals (skill.getName(), record2[0]);
    //          assertEquals (skill.getDescription(), record2[1]);
    //          assertEquals (skill.getAction(), record2[2]);
    //          
    //          // ERROR
    //          // Pass an object of the wrong length
    //          String[] badRec = {"First string","No third string"};
    //          skill = (Skill) skill.toObject(badRec);
    //          assertNull(skill);
    //          
    //          // NULL - N/A
    //
    //      }

    /** Test the conversion from a shuttle to a skill object
     * @Normal      Skill.unpackShuttle (Skill skill)             ok
     * @Error       Skill.unpackShuttle (Skill skill)             ok
     * @Null        Skill.unpackShuttle (Skill skill)             N/A
     */
    public void testUnpackShuttle()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestUnpackShuttle()");

        // NORMAL testing

        // ERROR

        // NULL - N/A

    }

    /** Tests that are not needed for various reasons, mostly setters and getters
     * @Not_Needed Skill.display()              audit method   <br>
     * @Not_Needed Skill.new()                  with 5 parameters, wrapper for constructor <br>   
     * @Not_Needed Skill.getAction()            simpler getter <br>
     * @Not_Needed Skill.getDescription()       simple getter <br>
     * @Not_Needed Skill.getName()              simple getter <br>
     * @Not_Needed Skill.getKey()               simple getter <br>
     * @Not_Needed Skill.loadShuttle()          wrapper for methods tested elsewhere <br>
     * @Not_Needed Skill.unpackShuttle()        wrapper for methods tested elsewhere <br>
     */
    public void testNotNeeded() {}

    //
    //    /** Tests that are not yet implemented */
    //    public void testNotImplemented()  
    //    { 
    //        MsgCtrl.errorMsgsOn(false);
    //        MsgCtrl.errMsgln(this, "testNotImplemented");
    //    }

}		// end of TestSkill class

