/**
 * BuildingsTestSuite.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * {@code chronos.pdc.buildings.Test*.java} regression test suite
 *
 * @author Alan Cline
 * @version May 15, 2013 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // PDC.buildings test files:
    TestArena.class,
    TestBank.class, // Need NPC
    TestBuilding.class, // all Buildings need NPCs
    TestClericsGuild.class, // Needs NPC
    TestFightersGuild.class, // Needs NPC
    TestInn.class, // need NPCs
    TestJail.class, // Need NPC and Items
    TestRoguesGuild.class, // Needs NPC
    TestStore.class, // Need NPC and Items
    TestWizardsGuild.class, // Needs NPC

})
/** Compilation of all unit tests for regression and integration testing. */
public class BuildingsTestSuite
{
}

// end of BuildingsTestSuite

