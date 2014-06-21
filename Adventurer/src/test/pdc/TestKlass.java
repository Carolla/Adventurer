/**
 * TestKlass.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import pdc.Klass;

import civ.PersonKeys;

import chronos.Chronos;

import mylib.MsgCtrl;

import java.util.EnumMap;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

/**
 * Tests the abstract Klass class by implementing a concrete subclass from which the
 * base class methods are called. 
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Jun 4, 2009   // original <DD>
 * <DT> Build 1.1       Aug 28 2010   // updated with QA tags <DD>
 * <DT> Build 1.2       Apr 19 2011   // TAA updated/ensured function, removed 
 * calcAC() test which is done by Inventory, added HP/exp tests<DD>
 * </DL>
 */

public class TestKlass extends TestCase
{
	/** Original package name for class definiton */
	private static String _originalPkgName = null;
	/** Redirected package name for Mock classes for testing */
	private static final String _mockPkgName = "test.pdc.";
	/** MockRace object to which the methods are called */
	MockKlass _mock = null;
	/** Klasses to be tested as they are implemented */
	final String[] _klasses = {"Peasant", "Fighter", "Rogue", "Wizard", "Cleric"};

	
	/**
	 * Redirect the class loader to look in the test directory for Mock class,
	 * then create the MockKlass in the test directory
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
        // Error messages are ON at start of each test 
        MsgCtrl.errorMsgsOn(true);
        // Audit messages are OFF at start of each test
        MsgCtrl.auditMsgsOn(true);      
        // Save default package name for later restore
        _originalPkgName = Chronos.getPackageName();
        Chronos.setPackageName(_mockPkgName);
        // Redirect the mock class location for testing purposes */
		_mock = (MockKlass) Klass.createKlass("MockKlass");
		assertNotNull(_mock);
	}

	/**	Redirect the class loader back to its original class directory, and delete the 
	 * MockKlass object
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		// Delete the MockRace object
		_mock = null;
		// Restore original package name (non-testing version)
        Chronos.setPackageName(_originalPkgName);
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
	}

	
    //-------------------------------------------------------------------------
    //         BEGIN TESTING
    //-------------------------------------------------------------------------
    
	/** Test that the MockKlass can be created so that the base Klass class methods
	 * can be tested.  
     * @Normal Klass.createKlass(String klassName)         used in setUp()
     * @Null       Klass.createKlass(String klassName)      exceptions caught  
     * @Error    Klass.createKlass(String klassName)       exceptions caught  
	 */
	public void testMockKlass()
	{
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestMockKlass()");

        // NORMAL Test that setUp() worked correctly
		// Check aspects of the MockKlass to confirm it
		assertNotNull(_mock);
        assertEquals(_mock.getKlassName(), "MockKlass");

        // Turn off expected error messages
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.errMsgln("Expected error messages: ");

        // NULL  Exceptions return null
        MsgCtrl.errMsg("\tNull class requested: \t");
        assertNull(Klass.createKlass(null));

        // ERROR  Exceptions return null 
        MsgCtrl.errMsg("\tAbstract class requested: \t");
        assertNull(Klass.createKlass("Klass"));         // abstract Klass

        MsgCtrl.errMsg("\tnon-Klass class requested: \t");
        assertNull(Klass.createKlass("Human"));      // non-Klass
        MsgCtrl.errMsg("\tNot implemented class requested: \t");
        assertNull(Klass.createKlass("Fighter"));      // not implemented Klass
        MsgCtrl.errMsgln("");
	}

	
	/** Test the various Klasses that can be created, both implemented and not 
	 * @Normal Klass.createKlass(String klassName)        
     * @Null       Klass.createKlass(String klassName)      exceptions caught  
     * @Error    Klass.createKlass(String klassName)       exceptions caught  
     */
    public void testCreateKlass()
	{
		MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestCreateKlass()");
        // No mocks needed so restore original package name (non-testing version)
        Chronos.setPackageName(_originalPkgName);
        // The setUp() method following will restore to the testing package again
        
		// These classes are implemented
		Klass aKlass = Klass.createKlass("Peasant");
		assertNotNull(aKlass);
		assertEquals(aKlass.getKlassName(), "Peasant");

        // Reset packageName with the new Mock class location
		// These classes are not implemented yet
		for (int k=1; k < _klasses.length; k++) {
	        // Turn off expected error messages
	        MsgCtrl.errorMsgsOn(false);
			aKlass = Klass.createKlass(_klasses[k]);
			assertNull(aKlass);
    	}
	}	
 
    public void testInitExperience()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestInitExperience()");
        
        //Normal
        _mock.setInternalData("I have 400 experience", 8, 4, 400);
        assertEquals(_mock.getXP(), 400);
        assertEquals(_mock.calcLevel(), 4);
        
        _mock.initExperience();
        assertEquals(_mock.getXP(), 0);
        assertEquals(_mock.calcLevel(), 0);
    }
    
    /** Test that hit points are initialized properly and hp adjustments are handled 
     * @Normal      Klass.testInitHP()          ok
     * @Null        Klass.testInitHP()          Null pointer
     * @Error       Klass.testInitHP()          ok
     */
    public void testInitHP()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.msgln(this, "\ttestInitHP()");
        
        _mock.setInternalData("I have 400 HP", 400, 400, 400);
        assertEquals(_mock.getHP(), 400);
        
        //Normal
        _mock.updateHPAdj(10);
        _mock.initHitPoints();
        assertEquals(_mock.getHP(), 20);
        
        //Error
        _mock.updateHPAdj(-10);
        _mock.initHitPoints();
        assertEquals(_mock.getHP(), 1);
        MsgCtrl.msgln("HP is " + _mock.getHP());
        // This should be 1 hp because hpAdj is negative
    }
    
    /** Test that the data shuttle is properly packed for transport 
     * @Normal  Klass.packShuttle(EnumMap<PersonKeys, String> fields)     ok
     * @Null       Klass.packShuttle(EnumMap<PersonKeys, String> fields)  n/a
     * @Error     Klass.packShuttle(EnumMap<PersonKeys, String> fields)   ok
     */
    public void testPackDataShuttle()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestPackDataShuttle()");

        // Create the data shuttle to populate
        EnumMap<PersonKeys, String> shuttle = 
                    new EnumMap<PersonKeys, String>(PersonKeys.class); 
        
        // NULL test with a null shuttle
        assertNull(_mock.packShuttle(null));

        // NORMAL Get the normal Klass fields for the MockKlass
        // AC, HP, KLASSNAME, LEVEL, XP
        MsgCtrl.msgln("\tKlass-oriented data shuttle fields...");
        _mock.packShuttle(shuttle);
        MsgCtrl.msg("\tBEFORE: ");
//        MsgCtrl.msg("\tAC=" + shuttle.get(PersonKeys.AC));
//        MsgCtrl.msg("\tHP=" + shuttle.get(PersonKeys.HP));
//        MsgCtrl.msg("\t\tKlass name=" + shuttle.get(PersonKeys.KLASSNAME));
//        MsgCtrl.msg("\tLevel=" + shuttle.get(PersonKeys.LEVEL));
//        MsgCtrl.msg("\tXP=" + shuttle.get(PersonKeys.XP));
        MsgCtrl.msgln("");
        // Now load the object with internal data to be read back from the shuttle
        // klassname, hit points, level, XP, and AC
        _mock.setInternalData("Test Klass", 12, 1, 2100);
        shuttle = _mock.packShuttle(shuttle);
        
        MsgCtrl.msg("\t\tAFTER: ");
//        MsgCtrl.msg("\tAC=" + shuttle.get(PersonKeys.AC));
//        MsgCtrl.msg("\tHP=" + shuttle.get(PersonKeys.HP));
//        MsgCtrl.msg("\t\tKlass name=" + shuttle.get(PersonKeys.KLASSNAME));
//        MsgCtrl.msg("\tLevel=" + shuttle.get(PersonKeys.LEVEL));
//        MsgCtrl.msg("\tXP=" + shuttle.get(PersonKeys.XP));
        MsgCtrl.msgln("");
        // Convert the strings returned against the primitive ints they are
//        assertEquals(shuttle.get(PersonKeys.KLASSNAME), "Test Klass");
//        assertEquals(Integer.parseInt(shuttle.get(PersonKeys.HP)), 12);
//        assertEquals(Integer.parseInt(shuttle.get(PersonKeys.LEVEL)), 1);
//        assertEquals(Integer.parseInt(shuttle.get(PersonKeys.XP)), 2100);
        //assertEquals(Integer.parseInt(shuttle.get(PersonKeys.AC)), 15);
        
        // ERROR  What if I try to retrieve the wrong key?
        // There is no way to block if the value actually is there since the key is in the enum
        assertNotNull(shuttle);
        shuttle = _mock.packShuttle(shuttle);
//        assertNull(shuttle.get(PersonKeys.GOLD));
    }
    
    
    /** Tests that are not needed for various reasons, mostly setters and getters
     * @Not_Needed  Klass.assignSkills(ArrayList<String> skillList)     getter
     * @Not_Needed  Klass.initExperience()                    setter
     * @Not_Needed  Klass.initHitPoints()                        setter
     * @Not_Needed  Klass.updateHPAdj(int defAdj)        setter
     * @Not_Needed  Klass.getAC()                                 getter
     * @Not_Needed  Klass.getKlassName()                    getter
     * @Not_Needed  Klass.getHP()                                 getter  
     * @Not_Needed  Klass.getHitPointAdj()                     getter   
     * @Not_Needed  Klass.getLanguage()                     getter
     * @Not_Needed  Klass.getXP()                                 getter
     * @Not_Needed  Klass.calcLevel()                   implemented elsewhere
     * @Not_Needed  Klass.calcAC()                      implemented elsewhere
     * @Not_Needed  Klass.initCash()                    implemented elsewhere
     * @Not_Needed  Klass.rollInitialTraits()           implemented elsewhere
     */
    public void testNotNeeded() {}


    /** Tests that are not yet implemented  */
    public void testNotImplemented() { }  

	
}		// end of TestKlass class

