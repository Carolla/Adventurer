/**
 * RegistrySuite.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.test.pdc.registry;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


 /**    Chronos PDC.Registrys test files
 *
 * @author Alan Cline
 * @version Jan 17, 2015    // original <br> 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( 
{
    /** chronos.pdc.registry test files */
    // TestArena.class
    TestAdventureRegistry.class,
    // TestBuildingRegistry.class,
    // TestHelpTextObject.class,
    // TestItemRegistry.class,
    // TestNPCRegistry.class,
    // TestOccupationRegistry.class,
    TestRegistryFactory.class,
    // TestRegistry.class
    // TestSkillRegistry.class,
    // TestTownRegistry.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class RegistrySuite { } 

//          end of BuildingsSuite test class

