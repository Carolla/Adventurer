/**
 * 
 * Fighter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import chronos.pdc.Item;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 *          Oct 17, 2015 // added klass-specific inventory items <br>
 */
public class Fighter extends Klass {

	/** Starting die and initial free HP for klass */
	private static final String _hitDie = "d10";
	private static final String _startingGold = "5d4";
	private String[] _fighterItems = { "Short sword", "Leather Armor" };

	/**
	 * Default constructor, called reflectively by Klass
	 * 
	 * @param traits
	 */
	public Fighter(TraitList traits) {
		super(traits, FIGHTER_CLASS_NAME, PrimeTraits.STR, _hitDie,
				_startingGold);
	}

	@Override
	public void addKlassItems(Inventory inven) {
		for (String itemName : _fighterItems) {
			inven.addItem(Item.getItem(itemName));
		}

	}

} // end of Fighter class
