/**
 * HeroDisplayCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@carolla.com
 */

package civ;


import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.character.Hero;
import chronos.pdc.character.Inventory;
import chronos.pdc.registry.HeroRegistry;
import hic.HeroDisplay;


/**
 * Output Civ: Creates the GUI widget <code>HeroDisplay</code>, passing output data to it from
 * a new or existing Hero.
 * 
 * @author Alan Cline
 * @version May 31 2010 // original <br>
 *          Jul 4 2010 // segregated HIC from PDC <br>
 *          Jan 4 2011 // removed Observer MVP model approach <br>
 *          Oct 1 2015 // revised for new Hero generation rules <br>
 *          Nov 6, 2015 // revised to be called by NewHeroCiv <br>
 *          Nov 21, 2015 // updated JPanel to ChronosPanel and edited accordingly <br>
 */
public class HeroDisplayCiv
{
  /** Associated Hero */
  private Hero _hero = null;
  /** Associated GUI */
  private HeroDisplay _heroDisp = null;

  /** Reference to parent civ */
  private final MainframeCiv _mfCiv;
  private final HeroRegistry _dorm;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  public HeroDisplayCiv(MainframeCiv mfCiv, HeroRegistry dorm)
  {
    _mfCiv = mfCiv;
    _dorm = dorm;
  }

  /** Restore the mainframe panels to their previous state */
  public void back()
  {
    _mfCiv.back();
  }

  public void backToMain()
  {
    _mfCiv.backToMain(null);
  }

  public boolean createPerson()
  {
    return _dorm.add(_hero);
  }

  /**
   * Delete the Person
   * 
   * @return true if the delete worked correctly; else false
   */
  public void deletePerson()
  {
    _dorm.delete(_hero);
  }

  /**
   * Display the Hero in the HeroDisplay tab pane.
   * 
   * @param firstTime Hero has disabled Delete button
   * @param hero to display
   */
  public void displayHero(Hero hero, boolean firstTime)
  {
    _hero = hero;

    Map<PersonKeys, String> _outputMap = hero.loadAttributes();
    _heroDisp = new HeroDisplay(this);
    _heroDisp.displayHero(_outputMap, firstTime);
    _heroDisp.setNameplate(_hero.toNamePlate());
    addAdditionalHeroStuff(hero);

    _mfCiv.replaceLeftPanel(_heroDisp);
  }

  public boolean overwritePerson()
  {
    return _dorm.update(_hero);
  }

  /**
   * Rename the Hero to the name selected
   * 
   * @param name the new name for the character
   * @return true if the rename worked correctly; else false
   */
  public void renamePerson(String name)
  {
    _hero.setName(name);
  }

  private void addAdditionalHeroStuff(Hero hero)
  {
    Inventory inventory = hero.getInventory();
    // Build a list of the Hero's various skills
    _heroDisp.addSkills(_hero.getOcpSkills(), _hero.getRaceSkills(), _hero.getKlassSkills());
    // Build an inventory list of regular items
    _heroDisp.addInventory(inventory);
    // Build an inventory list of magical items
    _heroDisp.addMagic(inventory);

    // Only clerics and wizards have spell materials
    // if (_hero.canUseMagic()) {
    // _heroDisp.addMaterials(inventory.getNameList(ItemCategory.SPELL_MATERIAL));
    // _heroDisp.addSpell(_hero.getSpellBook());
    // }
  }
} // end of HeroDisplayCiv class

