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

import test.integ.TA00a_Initialize;
import test.integ.TA00b_Exit;


/**
 * Regression test suite for all the <code>JUnit</code> test cases for the source code packages of
 * the <i>Adventurer</i> program. See the Testing Guidelines in
 * {@code ChronosLib/src/test/ChronosSuite.java}.
 * 
 * <P>
 * <H4>Limitations</H4>
 * The derived class constructors for Race and Klass, e.g., Human and Fighter, have public
 * visibility instead of protected for test purposes. The Person class factory that legitimately
 * instantiates those objects cannot be cast to the derived class object. To test the methods of the
 * derived classes, the objects are created by a direct call to the class's constructor. A mock
 * approach will not work because the object must be created before the mock can be created.
 * <p>
 * Perhaps the solution to this problem is to make Person abstract, as the rules of instantiation
 * demand: a Person object itself can never be instantiated, but always is created as a composite
 * object of a specific Race and Klass.
 * 
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jul 23 2014 // refactored tests with new code refactoring <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // CIV test files
    // TestBuildingDisplayCiv.class,
    // TestMainFrameCiv.class,
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

    // Use Case Integration Tests
    TA00a_Initialize.class,
    TA00b_Exit.class
// TestHumanPeasant.class
// TestDwarfPeasant.class
// TestElfPeasant.class
// TestGnomePeasant.class
// TestHalfElfPeasant.class
// TestHalfOrcPeasant.class
// TestHobbitPeasant.class
})

public class AdventurerSuite
{
  /**
   * Builds the accumulated test suite of all test cases for the <i>Adventurer</i> program. These
   * comments show the list of test files in the order in which to perform the testing, according to
   * the test rules mentioned above, and serves as a guide for unit testers.
   * <P>
   * Actually, the suite is not arraanged in the order below, but is arranged alphabetically within
   * each package, and executed in that order for regression testing.
   * <P>
   * Each time a <code>Class</code> source file is written, the <code>TestClass</code> test file
   * must be added to these comments, and to this <code>suite()</code> in the appropriate (and
   * different) position in their respective lists.
   * 
   * @return Test type for the JUnit framework
   */


} // end of AdventurerSuite class

