/**
 * AdventurerIntegTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.integ.TA00a_Initialize;
import test.integ.TA00b_Quit;
import test.integ.TA03a_SummonHeroes;
import test.integ.TA04_DeleteHeroes;
import test.integ.TA08_CmdEnter;
import test.integ.TA09_CmdLeave;
import test.integ.TA10_ReturnToTown;
import test.integ.TA14_TalkToPatron;

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
 *          Mar 29 2016 // Reviewed and tested for overall QA <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    // All integration tests for each use case
    TA00a_Initialize.class,
    TA00b_Quit.class,
    TA03a_SummonHeroes.class,
    TA04_DeleteHeroes.class,
    TA08_CmdEnter.class,
    TA09_CmdLeave.class,
    TA10_ReturnToTown.class,
    TA14_TalkToPatron.class,
})
public class AdventurerIntegTestSuite
{

} // end of IntegrationSuite class

