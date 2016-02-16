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
import chronos.pdc.Item.ItemCategory;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 *          Oct 17, 2015 // added klass-specific inventory items <br>
 */
public class Fighter extends Klass
{
  
  /** Starting die and initial free HP for klass */
  private String _hitDie = "d10";
  private String _startingGold = "5d4";


  /**
   * Default constructor, called reflectively by Klass
   * @param traits 
   */
  public Fighter(TraitList traits)
  {
    _traits = traits;
    _klassName = "Fighter";
    _primeTrait = PrimeTraits.STR;
    _hpDie = _hitDie;
    _goldDice = _startingGold;
  }

  @Override
  /** Assign initial inventory to Fighte (8 gpw = 1 lb) */
  public Inventory addKlassItems(Inventory inven)
  {
    // Basic inventory Items: category, name, quantity, weight (each in fractional lb)
    inven.addItem(new Item(ItemCategory.ARMS, "Sword, short, w/scabbard (dmg=d6, attack=1)", 1, 7.0));
    inven.addItem(new Item(ItemCategory.ARMOR, "Leather (AC=12)", 1, 10.0));
    return inven;
  }


} // end of Fighter class
