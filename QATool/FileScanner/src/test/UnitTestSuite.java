/**
 * UnitTestSuite.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.TestFileScanner;
import test.pdc.TestQAUtils;
import test.pdc.TestTestWriter;
import test.pdc.TestTripleMap;

/**
 * Run all unit tests for the project.
 * 
 * @author Al Cline
 * @version May 3, 2016 // original <br>
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({

    // All classes are in PDC folder
    TestFileScanner.class,
    TestQAUtils.class,
    TestTestWriter.class,
    TestTripleMap.class

})

public class UnitTestSuite
{
}

// end of UnitTestSuite class
