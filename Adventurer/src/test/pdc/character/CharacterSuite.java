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

import test.pdc.TestRace;


/**
 * Chronos PDC.buildings test files
 *
 * @author Alan Cline
 * @version Dec 25, 2015 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // PDC.buildings test files:
    TestRace.class,
    //    TestDwarf.class,
    //    TestElf.class,
    //    TestGnome.class,
    //    TestHalfElf.class,
    //    TestHalfOrc.class,
    //    TestHobbit.class,
    //    TestHuman.class,
    TestHunger.class,
//    TestPatron.class
})
/** Compilation of all unit tests for regression and integration testing. */
public class CharacterSuite
{
}

// end of BuildingsSuite test class

