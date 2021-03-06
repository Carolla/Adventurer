/**
 * PdcTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.character.TestHunger;
import test.pdc.command.CommandSuite;


/**
 * {@code pdc.command.Test*.java} regression test suite
 * 
 * @author Alan Cline
 * @version Mar 29 2016 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /* PDC Base test files */
    TestInventory.class,
    TestSkill.class,

    /* PDC.Character test files */
    TestHunger.class,

    /* PDC.Command test files */
    CommandSuite.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class PdcTestSuite
{
}

// end of CharacterTestSuite class

