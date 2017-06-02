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

package chronos.test.pdc.race;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.race.Race;

/**
 * Tests the abstract Race class by implementing a concrete subclass from which
 * the base class methods are called.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 4, 2009 // original
 *          <DT>Build 2.0 Jan 18, 2010 // add in non-Human racial tests
 *          </DL>
 */
public class TestRace  
{

//	@Test
//	public void testCreateRace() 
//	{
//		Race aRace = null;
//		for (int k = 0; k < Race.RACE_LIST.length; k++) {
//			aRace = Race.createRace(Race.RACE_LIST[k], new Gender("Male"));
//			assertTrue(aRace.getName().equals(Race.RACE_LIST[k]));
//		}
//	}
	
	@Test
	public void weightIsCalculatedByCategory()
	{
	  
	}

} // end of TestRace class

