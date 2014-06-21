/**
 * Peasant.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package pdc;

import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.AttributeList;
import chronos.pdc.Skill;

import mylib.pdc.MetaDie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Peasants are default-Klass Persons, not associated with a Guild, and are
 * severely restricted. Although Peasants can explore dungeons, get rumors from
 * the tavern, and buy supplies from the store, they cannot get promoted or get
 * other services from the Guilds. They must join a Guild to become a Fighter,
 * Wizard, Cleric or Rogue of any level, then they are Heroes, and their prime
 * traits are recalculated.
 * <P>
 * Peasants are constructed by passing a pre-defined Race and Klass object to
 * the Person constructor.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT> Build 1.0 Feb 28, 2009 // original <DD> <DT> Build 1.1 Apr 20,
 *          2011 // TAA changed initCash() to all doubles
 *          </DL>
 */
@SuppressWarnings("serial")
public class Peasant extends Klass implements Serializable {
	// PEASANT-SPECIFIC ATTRIBTUES
	private final double START_GOLD = 15;
	private final double START_SILVER = 8;

	/**
	 * Basic Peasant traits mus tlie somewhere between 8 and 18, but can be
	 * Race-adjusted
	 */
	private final int MIN_TRAIT = 8;
	private final int DEF_TRAIT = 11;
	private final int MAX_TRAIT = 18;

	/**
	 * Peasants do not roll traits randomly, but are assigned a fixed mediocre
	 * set, which are adjusted later for racial charactieristics and Guilds
	 * Traits = STR, DEX, INT, WIS, CON, CHR
	 */
	private int[] _defValue = { DEF_TRAIT, DEF_TRAIT, DEF_TRAIT, DEF_TRAIT,
			DEF_TRAIT, DEF_TRAIT };

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
	 * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Constructor, in which the class name is assigned It is protected so it
	 * can only be called with the PDC, but SHOULD only be called by the Klass
	 * factory <code>Klass.createKlass(klassName)</code>.
	 */
	public Peasant() {
		super._klassName = "Peasant";
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PROTECTED METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Peasants have no special Klass skills.
	 * 
	 * @param skillList
	 *            target list on which this method will add new skills
	 * @return the original list since Peasant adds no new skill
	 */
	@Override
	public ArrayList<Skill> assignSkills(ArrayList<Skill> skillList)
	{
		return skillList;
	}

	/**
	 * Sets the minimum HP for the Person, but because Peasants have no prime
	 * trait, they get the default, to be incremented by a Guild on promotion.
	 * This method is used for promotions or initializing the Person. For the
	 * Peasant, there are no promotions, so HP is initialized with the Minimum
	 * and the HP Adj. However, any Character gets at least 1 HP per promotion
	 * 
	 * @return the HP + HP Adj value for the Peasant
	 */
	protected int calcHP() 
	{
		int calc = MIN_HP + super._hitPointAdj;
		super._hp = Math.max(calc, MIN_HP_BUMP);
		return super._hp;
	}

	/**
	 * Peasants are set at Level 0 until they join a Guild, at which time they
	 * are no longer Peasant Klass. Calculating Level is then a method for that
	 * Guild class.
	 * 
	 * @return the level for the number of XP attained, for Peasant, Level = 0
	 *         always.
	 */
	public int calcLevel() 
	{
		return 0;
	}

	/**
	 * TODO: Integer attributes for gold and silver pieces are set now, but the
	 * weight must be factored in from the Inventory Registry later
	 */
	/**
	 * Set the minimum money for the Peasant, which must be lower than any
	 * starting Guild member
	 * 
	 * @return amount of gold and silver in gp.sp format
	 */
	protected double initCash() 
	{
		return (START_GOLD + START_SILVER / Inventory.SILVER_PER_GOLD);
	}

	/**
	 * Guilds set the prime traits of STR, DEX, INT, and WIS, so these are
	 * filled in temporarily with mediocre default values. The traits of CON and
	 * CHR are rolled now because they are not affected by Guild membership.
	 * 
	 * @return array of six traits defined
	 */
	public AttributeList rollInitialTraits() 
	{
		MetaDie md = new MetaDie();
		// Assign the first few from the default values
		AttributeList traits = new AttributeList(_defValue);

		// Roll for CON
		traits.put(ATTRIBUTE.CON, md.rollTrait());
		while ((traits.get(ATTRIBUTE.CON) < MIN_TRAIT)
				|| traits.get(ATTRIBUTE.CON) > MAX_TRAIT) {
			traits.put(ATTRIBUTE.CON, md.rollTrait());
		}
		// Roll for CHR
		traits.put(ATTRIBUTE.CHR, md.rollTrait());
		while ((traits.get(ATTRIBUTE.CHR) < MIN_TRAIT)
				|| traits.get(ATTRIBUTE.CHR) > MAX_TRAIT) {
			traits.put(ATTRIBUTE.CHR, md.rollTrait());
		}
		return traits;
	}

	
	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
	 * INNER CLASS USED FOR TESTING
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Accesses and tests the private methods of the Peasant object. */
	public class MockPeasant 
	{
		public MockPeasant() {}

		/** Return the Klass name */
		public String getKlassName() 
		{
			return Peasant.this._klassName;
		}

		/**
		 * Wrapper for protected method
		 * 
		 * @param adj
		 *            bonus/penalty from normal HP
		 */
		public int calcHP(int adj) 
		{
			Peasant.this._hitPointAdj = adj;
			return Peasant.this.calcHP();
		}

		/** Return the minimum HP for all Klasses */
		public int getMinHP() 
		{
			return Klass.MIN_HP;
		}

		/**
		 * Get the initial gold for all Klasses
		 * 
		 * @return the fixed initial gold amount
		 */
		public double getMinGold() 
		{
			return START_GOLD;
		}

		/**
		 * Get the initial silver for all Klasses
		 * 
		 * @return the fixed initial silver amount
		 */
		public double getMinSilver() 
		{
			return START_SILVER;
		}

		/**
		 * Wrap the protected initCash() method
		 * 
		 * @return the mixed gp.sp value
		 */
		public double initCash() 
		{
			return Peasant.this.initCash();
		}

	}  // end of Mockpeasant inner class

}   // end of Peasant class

