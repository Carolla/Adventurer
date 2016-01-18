/**
 * CharacterSuite.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.pdc.character;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.buildings.TestArena;
import test.pdc.buildings.TestBank;
import test.pdc.buildings.TestBuilding;
import test.pdc.buildings.TestClericsGuild;
import test.pdc.buildings.TestFightersGuild;
import test.pdc.buildings.TestInn;
import test.pdc.buildings.TestJail;
import test.pdc.buildings.TestRoguesGuild;
import test.pdc.buildings.TestStore;
import test.pdc.buildings.TestWizardsGuild;


/**
 * Chronos PDC.buildings test files
 *
 * @author Alan Cline
 * @version Dec 25, 2015 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
     // PDC.buildings test files:
     TestArena.class,
     TestBank.class, // Need NPC
     TestBuilding.class, // all Buildings need NPCs
     TestClericsGuild.class, // Needs NPC
     TestFightersGuild.class, // Needs NPC
     TestJail.class, // Need NPC and Items
     TestInn.class, // need NPCs
     TestRoguesGuild.class, // Needs NPC
     TestStore.class, // Need NPC and Items
     TestWizardsGuild.class, // Needs NPC
})
/** Compilation of all unit tests for regression and integration testing. */
public class BuildingSuite
{
}

// end of BuildingsSuite test class

