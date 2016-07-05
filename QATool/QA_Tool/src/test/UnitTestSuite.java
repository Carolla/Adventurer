/** 
 * UnitTestSuite.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved 
 * 
 * Permission to make digital or hard copies of all or parts of this work for 
 * commercial use is prohibited. To republish, to post on servers, to reuse, 
 * or to redistribute to lists, requires prior specific permission and/or a fee. 
 * Request permission to use from Carolla Development, Inc. by email: 
 * acline@carolla.com 
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.TestQAScanner;
import test.pdc.TestTestTemplate;
import test.pdc.subDir.TestSubDirSource;


/**
 * Run all unit tests for the project.
 * 
 * @author --generated by QA Tool--
 * @version June 30, 2016 	// original <br>
 */

@RunWith(Suite.class)
@Suite.SuiteClasses( {

		/** Base Test Files */
		TestAugmentTFile.class,
		TestPrototype.class,
		TestQATool.class,

		/** PDC Test Files */
		TestSubDirSource.class,
		TestAugmentTFile.class,
		TestPrototype.class,
		TestQAScanner.class,
		TestQATool.class,
		TestTestTemplate.class,

		/** CIV Test Files */

		/** DMC Test Files */

}) 

 public class UnitTestSuite { } 
 // end of class
