/**
 * AdventureUnitTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.civ.CivTestSuite;
import test.dmc.DmcTestSuite;
import test.pdc.PdcTestSuite;

/**
 * Run all unit tests for {@code Adventurer} components. Unit tests are grouped by their components
 * for {@code PDC, CIV}, and {@code DMC}.<br>
 * The collective unit test suites for {@code ChronosLib} and {@code MyLibrary} are not included
 * here.
 * 
 * @see test.AdventureSuite
 * 
 * @author Alan Cline
 * @version Sept 7 2014 // original <br>
 *          Mar 29 2016 // Reviewed and tested for overall QA <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

  // All MVP Component unit test suites
  CivTestSuite.class,
  PdcTestSuite.class,
  DmcTestSuite.class,
  

})


public class AdventurerUnitTestSuite
{

} // end of AdventureUnitTestSuite

