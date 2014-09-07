/**
 * UnitTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
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
 * Run all unit tests for {@code Adventurer} components. Unit tests are collected in their own
 * respective suites for {@code PDC, CIV}, and {@code DMC}.<br>
 * The collective unit test suites for {@code ChronosLib} and {@code MyLibrary} are included here
 * and run first because they support the {@code Adventurer}-specific unit tests. <br>
 * Integration tests for each use case are run from {@code AdventureSuite}, which also runs this
 * suite.
 * 
 * @see test.AdventureSuite 
 * 
 * @author Alan Cline
 * @version Sept 7 2014 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

/** MyLibary unit test suite */
// MyLibraryTestSuite.class,

/** ChronosLib unit test suite */
// ChronosSuite.class

/** CIV test files */
// TestMainFrameCiv.class,
// TestBuildingDisplayCiv.class,
// TestHeroDisplayCiv.class,
// TestNewHeroCiv.class,
// TestNewHeroFields.class,

/** PDC test files */
// TestDwarf.class
// TestHuman.class
// TestHunger.class
// TestInventory.class
// TestKlass.class
// TestPeasant.class
// TestPerson.class
// TestRace.class

/** DMC test files */
// TestPersonReadWriter.class


})
public class UnitTestSuite
{

} // end of UnitTestSuite class

