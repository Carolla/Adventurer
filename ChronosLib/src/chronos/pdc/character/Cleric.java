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
public class Cleric extends Klass
{

  /** Starting die and initial free HP for klass */
  private String _hitDie = "d8";
  private String _startingGold = "3d6";
  // Clerical mods
  private int _CSPsPerLevel = 0;
  private int _CSPs = 0;
  private int _turnUndead = 0;

  private final String[] _clericSpells = {
      "Bless", "Command", "Create Water", "Cure Light Wounds", "Detect Evil", "Detect Magic",
      "Light", "Protection from Evil", "Purify Food and Drink", "Remove Fear", "Resist Cold",
      "Sanctuary"};


  /**
   * Default constructor, called reflectively by Klass
   * @param traits 
   */
  public Cleric(TraitList traits)
  {
    _klassName = "Cleric";
    _primeTrait = PrimeTraits.WIS;
    _hpDie = _hitDie;
    _goldDice = _startingGold;
    calcClericMods(traits.getTrait(_primeTrait));
    _traits = traits;
  }

  // 8b. FOR CLERICS ONLY: CSPs/Level, CSPS, Turn Undead
  private void calcClericMods(int wisdom)
  {
    _CSPsPerLevel = wisdom / 2;
    _CSPs = _CSPsPerLevel; // for level 1
    _turnUndead = wisdom;
  }

  @Override
  public List<String> addKlassSpells(List<String> spellbook)
  {
    for (String s : _clericSpells) {
      spellbook.add(s);
    }
    return spellbook;
  }


  @Override
  /** Assign initial inventory to Cleric (8 gpw = 1 lb) */
  public Inventory addKlassItems(Inventory inven)
  {
    // Basic inventory Items: category, name, quantity, weight (each in fractional lb)
    inven.addItem(new Item(ItemCategory.ARMS, "Quarterstaff (dmg=d6T, attack=1)", 1, 3.0));
    inven.addItem(new Item(ItemCategory.MAGIC, "Sacred Satchel", 1, 0.25));
    inven.addItem(new Item(ItemCategory.MAGIC, "Holy symbol, wooden", 1, 0.50));
    // for testing
    inven.addItem(new Item(ItemCategory.SPELL_MATERIAL, "Rosemary sprig", 1, 0.125));
    inven.addItem(new Item(ItemCategory.SPELL_MATERIAL, "Wolfsbane", 2, 0.25));
    return inven;
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
    map.put(PersonKeys.CURRENT_CSP, "" + _CSPs);
    map.put(PersonKeys.MAX_CSP, "" + _CSPs);
    map.put(PersonKeys.CSP_PER_LEVEL, "" + _CSPsPerLevel);
    map.put(PersonKeys.TURN_UNDEAD, "" + _turnUndead);
  }
} // end of Cleric class


