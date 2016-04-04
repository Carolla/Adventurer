/**
 * CharacterTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.civ;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * {@code pdc.command.Test*.java} regression test suite 
 * 
 * @author Alan Cline
 * @version Mar 29 2016 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /* Civ test files */
    TestBuildingDisplayCiv.class,
    TestHeroDisplayCiv.class,
    TestMainframeCiv.class,
    TestNewHeroCiv.class,
    TestSummonHeroes.class,
})

public class CivTestSuite
{
}

// end of CivTestSuite class

