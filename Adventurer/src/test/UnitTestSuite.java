/**
 * UnitTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.pdc.command.TestCmdApproach;
import test.pdc.command.TestCmdEnter;
import test.pdc.command.TestCmdQuit;
import test.pdc.command.TestDeltaCmdList;
import test.pdc.command.TestEvent;
import test.pdc.registry.TestAdventureRegistry;

/**
 * Run all unit tests for {@code Adventurer} components. Unit tests are grouped by their components
 * for {@code PDC, CIV}, and {@code DMC}.<br>
 * The collective unit test suites for {@code ChronosLib} and {@code MyLibrary} are not included
 * here.
 * 
 * @see test.AdventureSuite
 * 
 * @author Alan Cline
 * @version Sept 7 2014 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

/** CIV test files */
// TestMainFrameCiv.class,
// TestBuildingDisplayCiv.class,
// TestHeroDisplayCiv.class,
// TestNewHeroCiv.class,
// TestNewHeroFields.class,

/** PDC test files */
  TestAdventureRegistry.class,
  TestCmdApproach.class,
  TestCmdEnter.class,
  TestCmdQuit.class,
  TestDeltaCmdList.class,
  TestEvent.class
// TestDwarf.class
// TestHuman.class
// TestHunger.class
// TestInventory.class
// TestKlass.class
// TestPeasant.class
// TestPerson.class
// TestRace.class

/** DMC test files */
// TestPersonReadWriter.class


})
public class UnitTestSuite
{

} // end of UnitTestSuite class

