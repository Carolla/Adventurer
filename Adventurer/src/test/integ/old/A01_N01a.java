/**
 * A01_N01a.java
 * Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.integ.A01_CreateHero.old;

import civ.NewHeroFields;

import chronos.pdc.AttributeList;

import mylib.MsgCtrl;
import mylib.civ.DataShuttle;

/**
 * Testing UC A01 Create New Hero, using female, with last-listed hair color and
 * last-listed Occupation. All common tests are derived from the basic HumanHero
 * test class.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 May 28, 2012 // original
 *          <DD>
 *          </DL>
 */
public class A01_N01a extends A01_N01 {
	// Constants for testing submit(), using middle-list items
	private final String IP_NAME = "Seraphim";
	private final String IP_GENDER = "Female";
	private final String IP_HAIRCOLOR = "red"; // 6th in list of 9
	private final String IP_RACENAME = "Human";
	private final String IP_KLASSNAME = "Peasant";
	private final String IP_OCCUPATION = "Gambler"; // 13th of 27 (with
													// Occupation "Kit")

	// Model output values in order built
	// Fixed prime traits; CON and CHR are random
	// STR, DEX, INT, WIS, CON (adj), CHR (adj)
	private int[] _expTraits = { 10, 11, 11, 10, -1, +1 };
	// Traits will be reset after the Person is created
	private int[] _resultTraits = { 0, 0, 0, 0, 0, 0 };
	private final int FEMALE_TRAITMIN = 9; // for CON and CHR
	private final int FEMALE_TRAITMAX = 19; // for CON and CHR
	// private int T_STR = 10; // starts at 11, downgraded for gender
	// private int T_INT = 11;
	// private int T_WIS = 10; // starts at 11, downgraded for Age (Young Adult)
	// private int T_DEX = 11;
	// private int T_CON = Chronos.UNASSIGNED; // will get upgraded +1 for
	// gender
	// private int T_CHR = Chronos.UNASSIGNED; // will get updgraded +1 for
	// gender

	// Expected value in order of creation
	private final double OP_AGE = 17.0;
	private final double OP_WEIGHT = 130; // female
	private final int OP_HEIGHT = 66; // female
	private final String OP_DESCRIPTION = null;
	private final int OP_TOHITMELEE_MOD = 0; // STR modifed for STR = 10
	private final int OP_DAMAGE_MOD = 0; // STR modifed for STR = 10
	private final String OP_LANGUAGE = "Commmon"; // Only Common

	// NOT INTEGRATED YET
	private final int OP_XP = 0;
	private final int OP_LEVEL = 0;
	private final int OP_HP = 10;
	private final String OP_HUNGER = "FULL";

	private final int OP_AC = 10;
	private final double OP_SPEED = 3.0; // AP = 11+11 => BM of 3.0

	private final int OP_GOLD = 15;
	private final int OP_SILVER = 8;
	private final double OP_GOLD_BANKED = 0.0;
	private final int OP_LOAD = 464; // ounces + 38 ounces in cash;

	private final int OP_MAX_LANGS = 1; // for INT = 11

	// /**
	// * @throws java.lang.Exception
	// */
	// @Before
	// public void setUp() throws Exception
	// {
	// }
	//
	// /**
	// * @throws java.lang.Exception
	// */
	// @After
	// public void tearDown() throws Exception
	// {
	// }

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ DERIVED OVERRIDE
	 * METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Pack the internal fields then send the new Hero data to the Civ to be
	 * validated. Scavanged method from NewHeroDisplay
	 * 
	 * @param inShuttle
	 *            Data shuttle with default data from NHCiv
	 * @return data shuttle of fields for this widget
	 */
	@Override
	protected DataShuttle<NewHeroFields> packFields(
			DataShuttle<NewHeroFields> inShuttle) {
		// Package each of these into the data shuttle
		inShuttle.putField(NewHeroFields.NAME, IP_NAME);
		inShuttle.putField(NewHeroFields.GENDER, IP_GENDER);
		inShuttle.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
		inShuttle.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
		inShuttle.putField(NewHeroFields.RACENAME, IP_RACENAME);
		inShuttle.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
		return inShuttle;
	}

	/**
	 * Verify that the Person's fixed traits are as expected. CON and CHR are
	 * not included in this test because they are random.
	 * 
	 * @See verifyConChrTraits(int, int)
	 * 
	 * @param expTraits
	 *            the expected values for the six prime trait for every hero;
	 * @param resultTraits
	 *            the actual results for the six prime traits
	 */
	@Override
	protected void verifyTraits(AttributeList expTraits,
			AttributeList resultTraits) {
		MsgCtrl.msgln("A01_N01a.verifyTraits()");
		super.verifyTraits(expTraits, resultTraits);
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

} // end of A01_N01a class
