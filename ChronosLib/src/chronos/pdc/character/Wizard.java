/**
 * Wizard.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
public class Wizard extends Klass
{
  /** Starting die and initial free HP for klass */
  private static final String _hitDie = "d4";
  private static final String _startingGold = "2d4";

  private int _MSPs = 0;
  private int _MSPsPerLevel = 0;
  private int _percentToKnow = 0;
  private int _spellsKnown = 0;

  /**
   * Default constructor, called reflectively by Klass
   * @param traits 
   */
  public Wizard(TraitList traits)
  {
    super(traits, WIZARD_CLASS_NAME, PrimeTraits.INT, _hitDie, _startingGold);
  }

  protected void calcClassMods(int intell)
  {
    _MSPsPerLevel = intell / 2 - 3;
    _percentToKnow = intell * 5;
    _MSPs = _MSPsPerLevel; // for first level
  }

  @Override
  /** Assign initial inventory to Wizard (8 gpw = 1 lb) */
  public void addKlassItems(Inventory inven)
  {
    inven.addItem(Item.getItem("Walking stick"));
    inven.addItem(Item.getItem("Spell book"));
    inven.addItem(Item.getItem("Magic bag"));
    inven.addItem(Item.getItem("Live spider"));
  }

  @Override
  public void addKlassSpells()
  {
    _spellBook.add("Read Magic");
  }

  @Override
  public boolean canUseMagic()
  {
    return true;
  }


  @Override
  public void loadKlassKeys(EnumMap<PersonKeys, String> map)
  {
    super.loadKlassKeys(map);
    map.put(PersonKeys.TO_KNOW, "" + _percentToKnow);
    map.put(PersonKeys.CURRENT_MSP, "" + _MSPs);
    map.put(PersonKeys.MAX_MSP, "" + _MSPs);
    map.put(PersonKeys.MSP_PER_LEVEL, "" + _MSPsPerLevel);
    map.put(PersonKeys.SPELLS_KNOWN, "" + _spellsKnown);
  }

} // end of Wizard class
