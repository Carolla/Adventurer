/**
 * ChronosSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import mylib.test.MyLibraryTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.buildings.BuildingsSuite;
import test.pdc.registry.RegistrySuite;


/**
 * Regression test suite for all the {@code JUnit} test cases for the source code packages of
 * the {@code Chronos} library.
 * 
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jan 14 2013 // updated for db4o database changes <br>
 *          Jan 26 2013 // ensure all integration testing works for complete suite <br>
 *          Apr 14, 2013 // converted all tests to JUnit 4 <br>
 *          July 23, 2014 // refactored file with new unit and integration tests <br>
 *          July 26, 2014 // {@code UC00a. Initialization} classes added: {@code RegistryFactory} <br>
 *          Sep 20, 2014 // Organized test file list <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{

    /** CIV test files */
    // TestDefaultLists.class,
    // TestHeroDefaults
    // TestMiscKeys.class,
    // TestOccupationsKeys.class,
    // TestSkillKeys.class,

    /* DMC test files */
    // None
    
    /** chronos.pdc.buildings test files */
    BuildingsSuite.class,   // all test files in the registry subfolder

    /** chronos.pdc.registry test files */
    RegistrySuite.class,    // all test files in the registry subfolder

    /* Chronos PDC test files */
    // TestAdventure.class,
    // TestAge.class,
    // TestArena.class,
    // TestAttributeList.class,
    // TestItem.class,
    // TestNPC.class,
    // TestNullNPC.class,
    // TestOccupation.class,
    // TestRace.class,
    // TestSkill.class,
    // TestTown.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class ChronosSuite
{
}

// end of ChronosSuite class

