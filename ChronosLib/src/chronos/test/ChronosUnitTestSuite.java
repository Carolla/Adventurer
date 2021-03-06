/**
 * ChronosSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */

package chronos.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chronos.test.pdc.TestAdventure;
import chronos.test.pdc.TestGameClock;
import chronos.test.pdc.TestItem;
import chronos.test.pdc.TestNPC;
import chronos.test.pdc.TestOccupation;
import chronos.test.pdc.TestSkill;
import chronos.test.pdc.TestTown;
import chronos.test.pdc.command.CommandTestSuite;
import chronos.test.pdc.race.RaceTestSuite;
import chronos.test.pdc.registry.RegistryTestSuite;


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
 *          Mar 29 2016 // reviewed and updated all test classes <br>
 *          July 17, 2017 // cleaned up tests as informed by QATool (FileScanner) <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /* CIV test files */
    // PersonKeys.class, // only an enum definition
    // UserMsgInterface.class, // interface definition only
    // UserMsgProxy.class, // wrapper methods only

    // TODO Verify that all classes below have tests that pass, or don't need them

    /* Test suites from PDC.subdirectories */
    // BuildingsTestSuite.class,
    // CharacterTestSuite.class,
    // CommandTestSuite.class,
    RaceTestSuite.class,
    RegistryTestSuite.class,

    /* PDC test files */
    // Chronos.class, // no tests: collection of constants and statics
    TestAdventure.class,
    TestGameClock.class,
    TestItem.class,
    TestNPC.class,
    TestOccupation.class,
    TestSkill.class,
    TestTown.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class ChronosUnitTestSuite
{
}

// end of ChronosSuite class

