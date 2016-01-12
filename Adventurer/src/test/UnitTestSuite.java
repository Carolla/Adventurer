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

import test.pdc.command.CommandSuite;

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
    // TestAdventurer.class,
    // TestBaseCiv.class,
    // TestBuildingDisplayCiv.class,
    // TestCommandParser.class,
    // TestDocumentSizeFiler.class,
    // TestHeroDisplayCiv.class,
    // TestMainActionCiv.class,
    // TestMainframeCiv.class,
    // TestNewHeroCiv.class,
    // TestOccupationDisplay.class,
    // TestOccupationKeys.class,
    // TestSkillDisplayCiv.class,
    // TestSkillKeys.class,

    /* PDC subfolder test suites */
    // CharacterSuite.class
    CommandSuite.class

    /** PDC test files */
    // Util.class
    // Comment

    // TestAdventureRegistry.class,
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

