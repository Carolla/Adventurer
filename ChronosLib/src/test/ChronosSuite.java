/**
 * ChronosSuite.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test;

import mylib.test.MyLibraryTestSuite;
import mylib.test.pdc.TestUtilities;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.TestItem;
import test.pdc.TestNPC;
import test.pdc.TestOccupation;
import test.pdc.TestSkill;
import test.pdc.buildings.BuildingsSuite;
import test.pdc.registry.RegistrySuite;

/**
 * Regression test suite for all the <code>JUnit</code> test cases for the
 * source code packages of the <i>Chronos</i> library.
 * 
 * <H4>Testing Guidelines</H4> Each test case (<code>JUnit</code> file) is run
 * individually and independently before being added. Test cases and the
 * subsequent test suite were built according to the following basic rules:
 * <OL>
 * <LI><i>Context-Free Unit-Testing:</i> Write <code>JUnit</code> tests for the
 * PDC, CIV, DMC, and SIC architectural components as appropriate. Test the
 * methods context-free. The methods are alphabetized in the class files (within
 * scope visibility). Not only can they be found easier, but by testing them in
 * that order, the testing is more likely to be context-free. <br>
 * <i>Author's Comment</i> It is my conjecture that if the public API methods
 * are throughly tested, you will not need to write test cases for the private
 * (helper) methods--they will have been tested as part of that class's API.
 * However, this heuristic can be abused: under this logic, one may say that
 * thorough integration tests are all that are needed because they will test
 * each class's public API too. Theoretically and empirically, such testing
 * strategy is not likely to succeed because, at the least, integration tests
 * can require an infinite number of test paths, whereas the more fine-grained
 * methods of a class's API do not.</LI>
 * <LI><i>Tests before Code:</i> Write the test before coding the methods as
 * much as possible. The thought processes are very different and offer a
 * different (cross-checking) perspective.</LI>
 * <LI><i>Components before Composites:</i> Test component classes before the
 * composite classes that contain those components to localize errors better.</LI>
 * <LI><i>Base Classes before Derived Classes:</i> Test base and abstract
 * classes first which will minimize the amount of functionality being tested
 * localize errors better. See <code>suite</code> constructor for more specific
 * details.</LI>
 * <LI><i>MockClasses before Accessors: </i>Use a Mock inner class to get to
 * private members of a class under test, instead of exposing the internals with
 * getter and setter methods that break encapsulation. Most class files
 * <code>ClassName</code> use a <code>MockClassName</code> inner class. Abstract
 * classes cannot have mock inner classes, so have a subclass
 * <code>MockClassName</code> in the test package of the corresponding component
 * package instead. </LI>
 * <LI><i> Integration Tests before GUI Tests:</i> <code>JUnit</code> does not
 * support GUI testing, so defer GUI testing until after integration testing.
 * Those tests will require manual inspection anyway. Automated GUI testing (HIC
 * package) is supported by the CIV package, which contains the logic behind the
 * GUI widgets to minimize the amount of code that cannot be automated.</LI>
 * <LI><i>Context-Dependent Integration-Testing:</i> Write integration tests in
 * execution order (context-dependent), as opposed to the alphabetical order of
 * unit testing. This approach offers yet another cross-check to testing
 * functionality. Integration tests exercise a particular test scenario through all the previously tested 
 * classes. About 85% of the code can be automated and verified with <code>JUnit</code>; the  
 * remaining 15% of the code resides in the GUI, and must be checked by manual inspections.</LI>
 * </OL>
 * <P>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT> Build 1.0 Jun 4 2009 // original <DD> 
 *          <DT> Build 1.1 Jan 18 2010 // add Occupation and Skill class testing <DD> 
 *          <DT> Build 1.2 Jul 11 2010 // updated for tests with CIV support <DD>
 *          <DT> Build 2.0 Jan 14 2013 // updated for db4o database changes
 *          <DT> Build 2.1 Jan 26 2013 // ensure all integration testing works for complete suite <DD>
 *          <DT> Build 3.0 Apr 14, 2013 // converted all tests to JUnit 4 <DD>
 *          </DL>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( 
{
    // MyLibrary test suit
    MyLibraryTestSuite.class,
   
    // Chronos PDC Building test suit
    BuildingsSuite.class,

    // Chronos PDC Registry test suit
    RegistrySuite.class,
    
    // PDC test files:
//    TestAge.class,
//    TestArena.class,
//    TestAttributeList.class,
    TestItem.class,
    TestNPC.class,
    TestOccupation.class,
//    TestRace.class,
    TestSkill.class,
//    TestTown.class,               // something left in wrong state in RegistrySuite.class test
    TestUtilities.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class ChronosSuite { } 

//          end of ChronosSuite class

