/**
 * ChronosSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.battle.BattleUseCase;
import test.battle.TestBattle;
import test.battle.TestCombatant;


/**
 * Regression test suite for all the {@code JUnit} test cases for the source code packages of
 * the {@code BattleMode} library.
 * 
 * @author Tim Armstrong
 */

@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    BattleUseCase.class,
    TestBattle.class,
    TestCombatant.class

})

/** Compilation of all unit tests for regression and integration testing. */
public class BattleSuite
{
}
