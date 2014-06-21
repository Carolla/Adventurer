/**
 * TestOccupation.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Occupation;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;


/**
 *    Test all methods in the Occupation class. An Occupations associated Skill must be in the
 *    SkillRegistry before the Occupation can be created.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Feb 11, 2013   // original <DD>
 * </DL>
 */
public class TestOccupation 
{
    /** Target object */
    private Occupation  _ocp = null;
    /** Test name for target */
    private final String OCPNAME = "Gambler";
    /** Test skill for target */
    private final String SKILLNAME = "Luck";
    /** Overly long Test name for target */
    private final String LONGNAME = "All names are required to be within " 
                + Occupation.OCC_NAME_LIMIT + "  characters";
    /** Test skill for target */
    private final String LONGSKILLNAME = "All skill names are required to be within "
                + Occupation.OCC_NAME_LIMIT + "  characters";
    /** Skill missing from Skill registry and associated with target */
    private final String ERRANT_SKILLNAME = "Bugalooing";


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  SETUP / TEARDOWN 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Create the secondary registries needed */
    @BeforeClass
    public static void runOnce()
    {
        // Create a Registry object, which will be initialized if new
        assertNotNull((SkillRegistry) RegistryFactory.getRegistry(RegKey.SKILL));
    }
    
    /** Close down all the secondary registries needed */
    @AfterClass
    public static void cleanUp() 
    {
        // Close the secondary Registry but do not delete the file
        ((SkillRegistry) RegistryFactory.getRegistry(RegKey.SKILL)).closeRegistry();
    }

    
    /**
     * @throws java.lang.Exception for unexpected exception
     */
    @Before
    public void setUp() throws Exception
    {
        _ocp = new Occupation(OCPNAME, SKILLNAME);
        assertNotNull(_ocp);
    }

    /**
     * @throws java.lang.Exception for unexpected exception
     */
    @After
    public void tearDown() throws Exception
    {
        _ocp = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  TEST METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Test that the constructor is build correctly
     * @Normal  verify the fields are properly assigned
     * @Error           parms are null                  forces NullPointerException
     * @Error           parms are overlimit          forces ApplicationException
     * @Error           occupation is missing a skill
     */
    @Test
    public void testCtor() 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testCtor()");
        
        // Normal  verify the fields are properly assigned
        MsgCtrl.msgln("\t Occupation " + _ocp.getName() + " created with skill " + _ocp.getSkillName());
        assertEquals(OCPNAME, _ocp.getName());
        assertEquals(SKILLNAME, _ocp.getSkillName());
        
        // Error  skill parm is null
        try {
            _ocp = new Occupation(OCPNAME, null);
        } catch (NullPointerException ex1) {
            MsgCtrl.msgln("\t Expected exception: " + ex1.getMessage());
        } catch (ApplicationException ex2) {
            MsgCtrl.errMsgln("\t Unexpected exception: " + ex2.getMessage());
        }
        // Error  name parm is null
        try {
            _ocp = new Occupation(null, SKILLNAME);
        } catch (NullPointerException ex1) {
            MsgCtrl.msgln("\t Expected exception: " + ex1.getMessage());
        } catch (ApplicationException ex2) {
            MsgCtrl.errMsgln("\t UnExpected exception: " + ex2.getMessage());
        }
        // Error  name parm is overly long
        try {
            _ocp = new Occupation(LONGNAME, SKILLNAME);
        } catch (ApplicationException ex2) {
            MsgCtrl.msgln("\t Expected exception: " + ex2.getMessage());
        }
        // Error  skillname parm is overly long
        try {
            _ocp = new Occupation(OCPNAME, LONGSKILLNAME);
        } catch (ApplicationException ex2) {
            MsgCtrl.msgln("\t Expected exception: " + ex2.getMessage());
        }
        // Error  skillname given is not part of the Registry
        try {
            _ocp = new Occupation(OCPNAME, ERRANT_SKILLNAME);
        } catch (ApplicationException ex2) {
            MsgCtrl.msgln("\t Expected exception: " + ex2.getMessage());
        }
    }

    
    /** chronos.pdc.Occupation
     * @Normal Verify same name and description return true
     * @Error   Verify different name returns false
     * @Error   Verify different skillNames returns false
     * @Error   Verify different both name and skillname returns false
     * @Error  Pass null object; expect false
     * @throws ApplicationException unexpected exception found
     */
    @Test
    public void testEquals() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);   
        String ocp2Name = "DM'ing";
        String ocp2Skill = "No Occupational Skills";
        
        // Normal   Verify same class, name and description returns true
        Occupation ocpSame = new Occupation(OCPNAME, SKILLNAME);
        assertTrue(_ocp.equals(ocpSame));
        
        // Error   Verify different names returns false
        Occupation ocpDiffName= new Occupation(ocp2Name, SKILLNAME);
        assertFalse(_ocp.equals(ocpDiffName));
        
        // Error   Verify different skillNames return false
        Occupation ocpDiffSkill = new Occupation(OCPNAME, ocp2Skill);
        assertFalse(_ocp.equals(ocpDiffSkill));

        Occupation ocpAll= new Occupation(ocp2Name, ocp2Skill);
        assertFalse(_ocp.equals(ocpAll));
        
        // Error  Pass null object; expect false
        assertFalse(_ocp.equals(null));
    }

    
    /** Tests that are not implemented either because tests are not needed, or they haven't
     * been determined to be needed yet
     *  @Not_Needed Occupation()                        unused ctor and simple      <br>
     *  @Not_Needed getName()                           simple wrapper                  <br>
     *  @Not_Needed getSkillName()                     simple wrapper             <br>
     *  @Not_Needed toString()                              simple print debugging statement  <br>
     */
    @Test
    public void notNeeded() { }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}
    		// end of TestOccupation class
