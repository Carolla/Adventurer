/**
 * Cleric.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import java.util.EnumMap;

import chronos.civ.PersonKeys;
import chronos.pdc.Item;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 *          Oct 17, 2015 // added klass-specific inventory items <br>
 */
public class Cleric extends Klass {

	/** Starting die and initial free HP for klass */
	private static final String _hitDie = "d8";
	private static final String _startingGold = "3d6";

	// Clerical mods
	private int _CSPsPerLevel = 0;
	private int _CSPs = 0;
	private int _turnUndead = 0;

	private final String[] _clericSpells = { "Bless", "Command",
			"Create Water", "Cure Light Wounds", "Detect Evil", "Detect Magic",
			"Light", "Protection from Evil", "Purify Food and Drink",
			"Remove Fear", "Resist Cold", "Sanctuary" };

	private final String[] _clericItems = { "Quarterstaff", "Sacred Satchel",
			"Holy symbol, wooden" };

	/**
	 * Default constructor, called reflectively by Klass
	 * 
	 * @param traits
	 */
	public Cleric(TraitList traits) {
		super(traits, CLERIC_CLASS_NAME, PrimeTraits.WIS, _hitDie,
				_startingGold);
	}

	// 8b. FOR CLERICS ONLY: CSPs/Level, CSPS, Turn Undead
	public void calcClassMods(int wisdom) {
		_CSPsPerLevel = wisdom / 2;
		_CSPs = _CSPsPerLevel; // for level 1
		_turnUndead = wisdom;
	}

	@Override
	public void addKlassSpells() {
		for (String s : _clericSpells) {
			_spellBook.add(s);
		}
	}

	@Override
	/** Assign initial inventory to Cleric (8 gpw = 1 lb) */
	public void addKlassItems(Inventory inven) {
		for (String itemName : _clericItems) {
			inven.addItem(Item.getItem(itemName));
		}
	}

	@Override
	public boolean canUseMagic() {
		return true;
	}

	@Override
	public void loadKlassKeys(EnumMap<PersonKeys, String> map) {
		super.loadKlassKeys(map);
		map.put(PersonKeys.CURRENT_CSP, "" + _CSPs);
		map.put(PersonKeys.MAX_CSP, "" + _CSPs);
		map.put(PersonKeys.CSP_PER_LEVEL, "" + _CSPsPerLevel);
		map.put(PersonKeys.TURN_UNDEAD, "" + _turnUndead);
	}
} // end of Cleric class

