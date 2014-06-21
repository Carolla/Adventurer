/**
 * MockKlass.java Copyright (c) 2009, Carolla Development, Inc. All Rights
 * Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse, or
 * to redistribute to lists, requires prior specific permission and/or a fee.
 * Request permission to use from Carolla Development, Inc. by email:
 * acline@carolla.com
 */

package test.pdc;

import pdc.Klass;

import chronos.pdc.AttributeList;
import chronos.pdc.Skill;

import java.util.ArrayList;

/**
 * Fake class to test the base class methods of the Klass superclass.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 4, 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Aug 28 2010 // minor revisions to support QA testing
 *          <DD>
 *          </DL>
 */

@SuppressWarnings("serial")
public class MockKlass extends Klass {
	/** Keep the concrete subclass for testing base class methods */
	MockKlass _mk = null;

	/** Default ctor to allow createKlass to work */
	public MockKlass() {
		_klassName = "MockKlass";
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * METHODS REQUIRED TO BE IMPLEMENTED BY ABSTRACT BASE CLASS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	@Override
	public ArrayList<Skill> assignSkills(ArrayList<Skill> skillList) {
		return skillList;
	}

	@Override
	protected double initCash() {
		return 15.7;
	}

	@Override
	protected int calcHP() {
		return _hp;
	}

	@Override
	public int calcLevel() {
		return _level;
	}

	@Override
	public AttributeList rollInitialTraits() {
		return null;
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ TEST
	 * KLASS METHODS THAT ARE IMPLEMENTED ONLY IN BASE CLASS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Get the AC value for no armor at all */
	public int getNoArmor() {
		return NO_ARMOR;
	}

	/**
	 * Set internal data so that the map will be filled with predictable data
	 * during test
	 * 
	 * @param klassname
	 *            armor class
	 * @param hp
	 *            hit points
	 * @param level
	 *            experience level
	 * @param xp
	 *            experience points
	 */
	public void setInternalData(String klassname, int hp, int level, int xp) {
		_hp = hp;
		_level = level;
		_xp = xp;
		_klassName = klassname;
	}

} // end of MockKlass class

