/**
 * AdventurerSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Regression test suite for all the {@code JUnit} test cases for the source code packages of
 * {@code ChronosLib} application-specific library and the {@code MyLibrary} generic library. Test
 * class are added for unit test and integration tests as each class is built for its first use case
 * implementation. See the class diagrams for each use case respectively.
 * 
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jul 26 2014 // {@code UC00a. Initilization} classes added: {@code Adventurer} (the app
 *          Launcher), {@code MainframeCiv}; <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    // ChronosLib test suite (includes MyLibaryTestSuite.class)
    ChronosSuite.class

// CIV test files
// TestMainFrameCiv.class,
// TestBuildingDisplayCiv.class,
// TestHeroDisplayCiv.class,
// TestNewHeroCiv.class,
// TestNewHeroFields.class,

// PDC test files:
// TestDwarf.class
// TestHuman.class
// TestHunger.class
// TestInventory.class
// TestKlass.class
// TestPeasant.class
// TestPerson.class
// TestRace.class

// DMC test files
// TestPersonReadWriter.class


})
public class AdventurerSuite
{

} // end of AdventurerSuite class

