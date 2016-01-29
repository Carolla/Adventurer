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


/**
 * Regression test suite for all the {@code JUnit} unit and integration test cases for
 * {@code Adventurer}. This suite includes the {@code ChronosLib} application-specific library,
 * which indirectly calls the {@code MyLibrary} generic library. After the two libraries test suites
 * are called, then the {@code UnitTestSuite} class is called, then finally all the integration
 * tests, which are collected individually here.
 * 
 * @author Alan Cline
 * @version Jun 4 2009 // original <br>
 *          Jan 18 2010 // add Occupation and Skill class testing <br>
 *          Jul 11 2010 // updated for tests with CIV support <br>
 *          Jul 26 2014 // {@code UC00a. Initilization} classes added: {@code Adventurer} (the app
 *          Launcher), {@code MainframeCiv}; <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    UnitTestSuite.class,

    /* Adventurer integration test suite */
    IntegrationSuite.class,

})
public class AdventurerTestSuite
{

} // end of AdventurerSuite class

