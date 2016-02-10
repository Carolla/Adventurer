/**
 * Wizard.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import static chronos.pdc.character.TraitList.PrimeTraits.*;

import java.util.EnumMap;
import java.util.List;

import chronos.civ.PersonKeys;
import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 *          Oct 17, 2015 // added klass-specific inventory items <br>
 */
public class Wizard extends Klass
{
  /** Starting die and initial free HP for klass */
  private String _hitDie = "d4";
  private String _startingGold = "2d4";

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
    _klassName = "Wizard";
    _primeTrait = PrimeTraits.INT;
    _hpDie = _hitDie;
    _goldDice = _startingGold;
    calcWizardMods(traits.getTrait(INT));
    _MSPs = _MSPsPerLevel; // for first level
    _traits = traits;
  }

  private void calcWizardMods(int intell)
  {
    _MSPsPerLevel = intell / 2 - 3;
    _percentToKnow = intell * 5;
  }

  @Override
  /** Assign initial inventory to Wizard (8 gpw = 1 lb) */
  public Inventory addKlassItems(Inventory inven)
  {
    // Basic inventory Items: category, name, quantity, weight (each in fractional lb)
    inven.addItem(new Item(ItemCategory.ARMS, "Walking stick (dmg=d6T, attack=1)", 1, 3.0));
    inven.addItem(new Item(ItemCategory.MAGIC, "Spell book", 1, 5.0));
    inven.addItem(new Item(ItemCategory.MAGIC, "Magic bag", 1, 0.25));
    // for testing
    inven.addItem(new Item(ItemCategory.SPELL_MATERIAL, "Live spider", 1, 0.125));
    return inven;
  }

  @Override
  public List<String> addKlassSpells(List<String> spellbook)
  {
    spellbook.add("Read Magic");
    return spellbook;
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
