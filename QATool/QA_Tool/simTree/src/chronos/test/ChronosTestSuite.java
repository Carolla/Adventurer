/**
 * ChronosSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Regression test suite for all the {@code JUnit} test cases for the source code packages of the
 * {@code Chronos} library.
 * 
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jan 14 2013 // updated for db4o database changes <br>
 *          Jan 26 2013 // ensure all integration testing works for complete suite <br>
 *          Apr 14, 2013 // converted all tests to JUnit 4 <br>
 *          July 23, 2014 // refactored file with new unit and integration tests <br>
 *          July 26, 2014 // {@code UC00a. Initialization} added: {@code RegistryFactory} <br>
 *          Sep 20, 2014 // Organized test file list <br>
 *          Dec 25, 2015 // verify testfile names against prod files names <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /* CIV test files */
    // TestDefaultLists.class,
    // TestDefaultUserMsg.class,
    // TestHeroDefaults,
    // TestMiscKeys.class,
    // TestOccupationsKeys.class,
    // TestSkillKeys.class,
    // TestUserMsg.class,

    /* DMC test files */
    // None

    /* PDC subfolder test suites */
//     BuildingsSuite.class,
    // RegistrySuite.class,
  
    /* PDC test files */
    // TestAdventure.class,
    // TestArena.class,
    // TestAttributeList.class,
    // TestGameClock.class,
    // TestItem.class,
    // TestMiscKeys.class,
    // TestNPC.class,
    // TestNullNPC.class,
    // TestOccupation.class,
    // TestRace.class,
    // TestSkill.class,
    // TestTown.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class ChronosTestSuite
{
}

// end of ChronosSuite class

