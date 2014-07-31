/**
 * ChronosSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.registry.TestRegistryFactory;

/**
 * Regression test suite for all the <code>JUnit</code> test cases for the source code packages of
 * the <i>Chronos</i> library.
 * <P>
 * <H4>Testing Guidelines</H4> Each test case (<code>JUnit</code> file) is run individually and
 * independently before being added. Test cases and the subsequent test suite were built according
 * to the following basic rules:
 * <P>
 * <OL>
 * <LI><i>Context-Free Unit-Testing:</i> Write <code>JUnit</code> tests for the PDC, CIV, DMC, and
 * SIC architectural components as appropriate. Test the methods context-free. The methods are
 * alphabetized in the class files (within scope visibility). Not only can they be found easier, but
 * by testing them in that order, the testing is more likely to be context-free.</LI>
 * <P>
 * <LI><i>Tests before Code:</i> Write the test before coding the methods as much as possible. The
 * thought processes are very different and offer a different (cross-checking) perspective.</LI>
 * <P>
 * <LI><i>Components before Composites:</i> Test component classes before the composite classes that
 * contain those components to localize errors better.</LI>
 * <P>
 * <LI><i>Base Classes before Derived Classes:</i> Test base and abstract classes first which will
 * minimize the amount of functionality being tested localize errors better. See <code>suite</code>
 * constructor for more specific details.</LI>
 * <P>
 * <LI><i>MockClasses instead of Accessors: </i>Use a Mock inner class to get to private members of
 * a class under test, instead of exposing the internals with getter and setter methods that break
 * encapsulation. Most class files <code>ClassName</code> use a <code>MockClassName</code> inner
 * class. Abstract classes cannot have mock inner classes, so have a subclass
 * <code>MockClassName</code> in the test package of the corresponding component package instead.</LI>
 * <P>
 * <LI><i> Integration Tests before GUI Tests:</i> <code>JUnit</code> does not support GUI testing,
 * so defer GUI testing until after integration testing. Those tests will require manual inspection
 * anyway. Automated GUI testing (HIC package) is supported by the CIV package, which contains the
 * logic behind the GUI widgets to minimize the amount of code that cannot be automated.</LI>
 * <P>
 * <LI><i>Context-Dependent Integration-Testing:</i> Write integration tests in execution order
 * (context-dependent), as opposed to the alphabetical order of unit testing. This approach offers
 * yet another cross-check to testing functionality. Integration tests exercise a particular test
 * scenario through all the previously tested classes. About 85% of the code can be automated and
 * verified with <code>JUnit</code>; the remaining 15% of the code resides in the GUI, and must be
 * checked by manual inspections.</LI>
 * </OL>
 * 
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jan 14 2013 // updated for db4o database changes <br>
 *          Jan 26 2013 // ensure all integration testing works for complete suite <br>
 *          Apr 14, 2013 // converted all tests to JUnit 4 <br>
 *          July 23, 2014 // refactored file with new unit and integration tests <br>
 *          July 26, 2014 // {@code UC00a. Initialization} classes added: {@code RegistryFactory}
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
/**
 *     // MyLibrary test suit
    // MyLibraryTestSuite.class,
*/
  
    // Chronos PDC Building test suit
    // BuildingsSuite.class,

    // Chronos PDC Registry test suit
    // RegistrySuite.class,

    // PDC test files:
    TestRegistryFactory.class
// TestAge.class,
// TestArena.class,
// TestAttributeList.class,
// TestItem.class,
// TestNPC.class,
// TestOccupation.class,
// TestRace.class,
// TestSkill.class,
// TestTown.class, // something left in wrong state in RegistrySuite.class test
// TestUtilities.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class ChronosSuite
{
}

// end of ChronosSuite class

