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

import test.pdc.TestQATool;

/**
 * Run all unit tests for the project.
 * 
 * @author Al Cline
 * @version Dec 20, 2016 // original <br>
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({

      /** Base Test Files */

      /** PDC Test Files */
      //TestPrototype.class,
      TestQATool.class, // no need to test this launcher class 
      // TestQAUtils.class,
      // TestSingleFileScan.class,
      // TestSrcReader.class,
      // TestSuiteBuilder.class,
      // TestTestWriter.class

      /** CIV Test Files */
      // None
      
      /** DMC Test Files */
      // None
})

public class UnitTestSuite
{
}

// end of UnitTestSuite class
