/**
 * MyLibraryTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import mylib.test.dmc.TestRegistry;
import mylib.test.pdc.TestMetaDie;
import mylib.test.pdc.TestUtilities;

/**
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jul 26 2014 // {@code UC00a. Initialization} classes added: {@code Registry} <br>
 *          Sep 20, 2014 // removed unneeded test files and uncommented needed ones <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /** HIC Test file */
    // TestHelpDialog // TODO: Needs to be implemented

    /** DMC Test files */
    // TestDbReadWriter.class, // Db4o dropped from the program
    TestRegistry.class, // base class

    /** PDC Test files */
    TestMetaDie.class,
    TestUtilities.class,

})
public class MyLibraryUnitTestSuite
{
}

// end of MyLibraryTestSuite class

