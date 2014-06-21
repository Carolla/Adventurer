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

import test.civ.TestBuildingDisplayCiv;
import test.civ.TestMainFrameCiv;


/**
 * Regression test suite for all the <code>JUnit</code> test cases for the source code packages of
 * the <i>Adventurer</i> program.
 * 
 * <H4>Testing Guidelines</H4> Each test case (<code>JUnit</code> file) is run individually and
 * independently before being added. Test cases and the subsequent test suite were built according
 * to the following basic rules:
 * <OL>
 * <LI><i>Context-Free Unit-Testing:</i> Write <code>JUnit</code> tests for the PDC, CIV, DMC, and
 * SIC architectural components as appropriate. Test the methods context-free. The methods are
 * alphabetized in the class files (within scope visibility). Not only can they be found easier, but
 * by testing them in that order, the testing is more likely to be context-free. <br>
 * <i>Author's Comment</i> It is my conjecture that if the public API methods are throughly tested,
 * you will not need to write test cases for the private (helper) methods--they will have been
 * tested as part of that class's API. However, this heuristic can be abused: under this logic, one
 * may say that thorough integration tests are all that are needed because they will test each
 * class's public API too. Theoretically and empirically, such testing strategy is not likely to
 * succeed because, at the least, integration tests can require an infinite number of test paths,
 * whereas the more fine-grained methods of a class's API do not.</LI>
 * <LI><i>Tests before Code:</i> Write the test before coding the methods as much as possible. The
 * thought processes are very different and offer a different (cross-checking) perspective.</LI>
 * <LI><i>Components before Composites:</i> Test component classes before the composite classes that
 * contain those components to localize errors better. For example, the <code>Person</code> object
 * contains <code>Race, Klass</code> , and <code>Inventory</code> components. Each component was
 * tested before the <code>Person</code> class.</LI>
 * <LI><i>Base Classes before Derived Classes:</i> Test base and abstract classes first which will
 * minimize the amount of functionality being tested localize errors better. For example, a
 * <code>MockRace</code> concrete class was used to test the methods implemented in the abstract
 * <code>Race</code> class before testing the <code>Human</code> class, which is a subclass of
 * <code>Race</code>. See <code>suite</code> constructor for more specific details.</LI>
 * <LI><i>MockClasses before Accessors: </i>Use a Mock inner class to get to private members of a
 * class under test, instead of exposing the internals with getter and setter methods that break
 * encapsulation. Most class files <code>ClassName</code> use a <code>MockClassName</code> inner
 * class. Abstract classes cannot have mock inner classes, so have a subclass
 * <code>MockClassName</code> in the test package of the corresponding component package instead.
 * For example, <code>Human</code> has a <code>MockHuman</code> inner class, both in
 * <code>package pdc</code> (because they are the same file), but <code>Race</code> in
 * <code>package pdc</code> has a <code>MockRace</code> class in the package <code>test.pdc.</code></LI>
 * <LI><i> Integration Tests before GUI Tests:</i> <code>JUnit</code> does not support GUI testing,
 * so defer GUI testing until after integration testing. Those tests will require manual inspection
 * anyway. Automated GUI testing (HIC package) is supported by the CIV package, which contains the
 * logic behind the GUI widgets to minimize the amount of code that cannot be automated.</LI>
 * <LI><i>Context-Dependent Integration-Testing:</i> Write integration tests in execution order
 * (context-dependent), as oppsoed to the alphabetical order of unit testing. This approach offers
 * yet another cross-check to testing functionality. Integration tests exercise a particular test
 * scenario (use case) through all the previously tested classes. About 85% of the code can be
 * automated and verified with <code>JUnit</code>; the remaining 15% of the code resides in the GUI,
 * and must be checked by manual inspections.
 * </OL>
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
 * @version <DL>
 *          <DT> Build 1.0 Jun 4 2009 // original <DD> <DT> Build 1.1 Jan 18 2010 // add Occupation
 *          and Skill class testing <DD> <DT> Build 1.2 Jul 11 2010 // updated for tests with CIV
 *          support <DD>
 *          </DL>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // CIV test files
    TestBuildingDisplayCiv.class,
    TestMainFrameCiv.class,
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

// Integration Tests
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
     * comments show the list of test files in the order in which to perform the testing, according
     * to the test rules mentioned above, and serves as a guide for unit testers.
     * <P>
     * Actually, the suite is not arraanged in the order below, but is arranged alphabetically
     * within each package, and executed in that order for regression testing.
     * <P>
     * Each time a <code>Class</code> source file is written, the <code>TestClass</code> test file
     * must be added to these comments, and to this <code>suite()</code> in the appropriate (and
     * different) position in their respective lists.
     * 
     * @return Test type for the JUnit framework
     */


} // end of AdventurerSuite class

