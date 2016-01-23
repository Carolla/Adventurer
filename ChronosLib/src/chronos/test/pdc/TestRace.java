/**
 * TestRace.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package chronos.test.pdc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.Before;

import chronos.pdc.race.Race;

/**
 * Tests the abstract Race class by implementing a concrete subclass from which
 * the base class methods are called.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 4, 2009 // original
 *          <DD>
 *          <DT>Build 2.0 Jan 18, 2010 // add in non-Human racial tests
 *          <DD>
 *          </DL>
 */
public class TestRace  {

	private final String[] _races = { "Human", "Dwarf", "Elf", "Gnome",
			"Half-Elf", "Half-Orc", "Hobbit" };
  private Race _race;

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  STATE HANDLING METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
	/**
	 * Redirect the class loader to look in the test directory for Mock class,
	 * then create the MockRace in the test directory
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() 
	{
		_race = Race.createRace("Elf");
	}

	// -------------------------------------------------------------------------
	// BEGIN TESTING
	// -------------------------------------------------------------------------

	/**
	 * Test the various Races that can be created
	 * 
	 * @Normal Race.createRace(String ) implemented classes checked
	 * @Null Race.createRace(String ) N/A
	 * @Error Race.createRace(String ) unimplemented classes checked
	 */
	public void testCreateRace() 
	{
		// NORMAL
		// Human and Dwarf are only classes implemented now
		Race aRace = null;
		for (int k = 0; k < 2; k++) {
			aRace = Race.createRace(_races[k]);
			assertNotNull(aRace);
			assertTrue(aRace.getRaceName().equals(_races[k]));
		}
		// ERROR
		// These classes are not implemented yet
		for (int k = 2; k < _races.length; k++) {
			MsgCtrl.errMsg("\tExpected error: ");
			aRace = Race.createRace(_races[k]);
			assertNull(aRace);
		}
	}

	
	// -------------------------------------------------------------------------
	// QA SUPPORT
	// -------------------------------------------------------------------------
	/**
	 * Tests that are not needed for various reasons, mostly setters and getters
	 * 
	 * @Not_Needed Race.Race() simple random call
	 * @Not_Needed Race.adjTraitsForRace(int[] defValues) wrapper
	 * @Not_Needed Race.calcHeight(int maleHt, int femaleHt) wrapper
	 * @Not_Needed Race.calcWeight(int maleWt, int femaleWt) wrapper
	 * @Not_Needed Race.calcMaxSatiety(int satietyRate)
	 * @Not_Needed Race.getAge() getter
	 * @Not_Needed Race.getAgeCategory() getter
	 * @Not_Needed Race.getGender() getter
	 * @Not_Needed Race.getHeight() getter
	 * @Not_Needed Race.getHungerState() getter
	 * @Not_Needed Race.getMagicAttackAdj() getter
	 * @Not_Needed Race.getMvmt() getter
	 * @Not_Needed Race.getRaceName() getter
	 * @Not_Needed Race.getRaceTypes() getter
	 * @Not_Needed Race.getToHitMissile() getter
	 * @Not_Needed Race.getWeight() getter
	 * @Not_Needed Race.setGender(String gender) setter
	 * @Not_Needed Race.updateToHitAdjMissile(int defAdj, int dex) setter
	 */
	public void testNotNeeded() {
	}

	public void testNotImplemented() {
	}
	/**
	 * Tests that are not yet implemented Public and protected methods
	 * 
	 * @Not_Implemented Race.initAge(int topYoungster)
	 * @Not_Implemented Race.findRangeDescriptor(double value, double minValue,
	 *                  double maxValue, String[] descriptors)
	 * @Not_Implemented Race.packShuttle(EnumMap<PersonFields, String> fields)
	 * @Not_Implemented Race.verifyTraits(int[] traits, int[] minList, int[]
	 *                  maxList)
	 */

} // end of TestRace class

