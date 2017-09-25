/**
 * RaceTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc.race;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * {@code pdc.race.Test*.java} regression test suite
 * 
 * @author Alan Cline
 * @version Mar 29 2016 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /* PDC.Race test files */
    TestDwarf.class,
    TestElf.class,
    TestGnome.class,
    // TestHalfElf.class,
    // TestHalfOrc.class,
    // TestHobbit.class,
    // TestHuman.class,
    TestRace.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class RaceTestSuite
{
}

// end of CommandTestSuite class

