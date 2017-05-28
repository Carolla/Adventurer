/**
 * 
 * Fighter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version May 21, 2017 // original <br>
 */
public class Peasant extends Klass {

	/** Starting die and initial free HP for klass */
  //Peasants never get promoted, so this die is not needed 
	private static final String _hitDie = "d0";  
	// All peasants start with a fixed amount
  private static final int _startingHP = 10;  
	private static final String _startingGold = "1d4"; // multiplied by 10
	// There are no specialty items for peasants
	private String[] _peasantItems = {};

	/**
	 * Default constructor, called reflectively by Klass
	 * 
	 * @param traits
	 */
	public Peasant(TraitList traits) {
    super(traits, PEASANT_CLASS_NAME, PrimeTraits.STR, _hitDie, _startingGold);
	}

	// Peasant has no specific inventory
	@Override
	public void addKlassItems(Inventory inven) {
//		for (String itemName : _peasantItems) {
//			inven.addItem(itemName);
//		}
	}
	
	/** Peasants get a fixed number of HP + their CON mod */
	@Override
	public int rollHP()
	{
    return _startingHP + _traits.calcMod(PrimeTraits.CON);
	}
	
	
} // end of Peasant class
