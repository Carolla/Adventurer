/**
 * MyLibraryTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.test;

import mylib.test.dmc.TestDbReadWriter;
import mylib.test.pdc.TestMetaDie;
import mylib.test.pdc.TestRegistry;
import mylib.test.pdc.TestUtilities;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 * Jan 18 2010 // add Occupation and Skill class testing  <br>
 * Jul 11 2010 // updated for tests with CIV support  <br>
 * Jul 26 2014 // {@code UC00a. Initialization} classes added: {@code Registry}

 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    // PDC Test files
    TestMetaDie.class,
    TestRegistry.class,
    TestUtilities.class,

    // CIV Test files
//    TestDataShuttle.class,

    // DMC Test files
    TestDbReadWriter.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class MyLibraryTestSuite
{
}

// end of MyLibraryTestSuite class


// public class MyLibraryTestSuite{
// {
// /** Compilation of all unit tests for regression and integration testing. */
// public MyLibraryTestSuite() {}
//
// public static Test suite()
// {
// String caption = "Test Suite for MyLibrary";
// TestSuite suite = new TestSuite(caption);
//
// //$JUnit-BEGIN$
//
// // PDC test files:
// // suite.addTestSuite(TestHelpEngine.class); // not yet written
// suite.addTestSuite(TestMetaDie.class);
// // suite.addTestSuite(TestObservableModel.class); // not yet written
// suite.addTestSuite(TestRegistry.class);
// suite.addTestSuite(TestUtilities.class);
//
// // CIV test files
// // suite.addTestSuite(TestBaseCiv.class); // not yet written
// suite.addTestSuite(TestDataShuttle.class);
//
// // DMC test files
// suite.addTestSuite(TestDbReadWriter.class);
// // suite.addTestSuite(TestHelpContentHandler.class); // not yet written
// // TODO doesn't pass yet
// // suite.addTestSuite(TestHelpReader.class);
//
// //$JUnit-END$
// return suite;
// }

