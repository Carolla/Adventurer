/**
 * BuildingsSuite.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


 /**    Chronos PDC.buildings test files
 *
 * @author Alan Cline
 * @version <DL>
 *          <DT> Build 1.May 15, 2013 // split off from ChronosSuite with new folders <DD> 
 *          </DL>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( 
{
    // PDC.buildings test files:
    // TestArena.class
    TestBank.class,                        // Need NPC
    TestBuilding.class,                     // all Buildings need NPCs
    TestClericsGuild.class,              // Needs NPC 
    TestFightersGuild.class,                  // Needs NPC
    TestJail.class,                      // Need NPC and Items
    TestInn.class,                          // need NPCs
    TestRoguesGuild.class,            // Needs NPC
    TestStore.class,                      // Need NPC and Items
    TestWizardsGuild.class,               // Needs NPC    

})
/** Compilation of all unit tests for regression and integration testing. */
public class BuildingsSuite { } 

//          end of BuildingsSuite test class

