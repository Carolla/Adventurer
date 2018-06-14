/**
 * RegistryTestSuite.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@wowway.com
 */


package chronos.test.pdc.command;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * * {@code pdc.registry.Test*.java} regression test suite
 *
 * @author Alan Cline
 * @version July 15, 2017 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    // TODO Verify that all classes below have tests that pass, or don't need them

    /* chronos.pdc.registry test files */
    TestCommand.class,
    // TestDeltaCmdList.class,
    // TestEvent.class,
    // TestEventTest.class,
    // TestintCmdPatronEnter.class,
    // TestintCmdPatronLeave.class,
    // TestNullCommand.class,
    // TestScheduler.class,

})

/** Compilation of all unit tests for regression and integration testing. */
public class CommandTestSuite
{
}

// end of ResgistryTestSuite test class

