/**
 * Wizard.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 *          Oct 17, 2015 // added klass-specific inventory items <br>
 */
public class Wizard extends Klass {
	/** Starting die and initial free HP for klass */
	private static final String _hitDie = "d4";
	private static final String _startingGold = "2d4";

	private int _MSPs = 0;
	private int _MSPsPerLevel = 0;
	private int _spellsKnown = 0;
	private String[] _wizardItems = { "Walking stick", "Spell book", "Magic bag" };

	/**
	 * Default constructor, called reflectively by Klass
	 * 
	 * @param traits
	 */
	public Wizard(TraitList traits) {
		super(traits, WIZARD_CLASS_NAME, PrimeTraits.INT);
	}

	protected void calcClassMods(int intell) {
		_MSPsPerLevel = intell / 2 - 3;
		_MSPs = _MSPsPerLevel; // for first level
	}

	@Override
	public void addKlassItems(Inventory inven)
	{
		for (String itemName : _wizardItems ) {
			inven.addItem(itemName);
		}
	}

	@Override
	public void addKlassSpells() {
		_spellBook.add("Read Magic");
	}

	@Override
	public boolean canUseMagic() {
		return true;
	}

  /** Each klass has a unique amount of starting gold */
  @Override
  public double rollStartingGold()
  {
    return rollStartingGold(_startingGold);
  }

  /** Each klass has a unique amount of starting HO */
  @Override
  public int rollHP()
  {
    return rollHP(_hitDie);
  }
  
  
	@Override
	public void loadKlassKeys(Map<PersonKeys, String> map) {
		super.loadKlassKeys(map);
		map.put(PersonKeys.CURRENT_MSP, "" + _MSPs);
		map.put(PersonKeys.MAX_MSP, "" + _MSPs);
		map.put(PersonKeys.MSP_PER_LEVEL, "" + _MSPsPerLevel);
		map.put(PersonKeys.SPELLS_KNOWN, "" + _spellsKnown);
	}

} // end of Wizard class
