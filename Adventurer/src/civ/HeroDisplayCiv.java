/**
 * HeroDisplayCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;


import hic.HeroDisplay;

import java.util.EnumMap;

import chronos.civ.PersonKeys;
import chronos.pdc.Item.ItemCategory;
import chronos.pdc.character.Hero;
import chronos.pdc.character.Inventory;
import chronos.pdc.registry.HeroRegistry;


/**
 * Output Civ: Creates the GUI widget <code>HeroDisplay</code>, passing output data to it from a new
 * or existing Hero.
 * 
 * @author Alan Cline
 * @version May 31 2010 // original <br>
 *          Jul 4 2010 // segregated HIC from PDC <br>
 *          Jan 4 2011 // removed Observer MVP model approach <br>
 *          Oct 1 2015 // revised for new Hero generation rules <br>
 *          Nov 6, 2015 // revised to be called by NewHeroCiv <br>
 *          Nov 21, 2015 // updated JPanel to ChronosPanel and edited accordingly <br>
 */
public class HeroDisplayCiv extends BaseCiv
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
    doConstructorWork();
  }

  // Override for testing to avoid GUI
  protected void doConstructorWork()
  {
    _heroDisp = new HeroDisplay(this);
  }

  /** Restore the mainframe panels to their previous state */
  public void back()
  {
    _mfCiv.back();
  }

  /** Restore the mainframe panels to their previous state */
  public void backToMain(String newFrameTitle)
  {
    _mfCiv.backToMain(newFrameTitle);
  }

  /**
   * Display the Hero the HeroDisplay widget.
   * 
   * @param firstTime Heroes disable Delete button
   * @param hero to display
   */
  public void displayHero(Hero hero, boolean firstTime)
  {
    _hero = hero;

    EnumMap<PersonKeys, String> _outputMap = hero.loadAttributes();
    addAdditionalHeroStuff(hero);
    _heroDisp.displayHero(_outputMap, firstTime);

    _mfCiv.replaceLeftPanel(_heroDisp);
  }

  private void addAdditionalHeroStuff(Hero hero)
  {
    Inventory inventory = hero.getInventory();
    _heroDisp.addSkills(_hero.getOcpSkills(), _hero.getRaceSkills(), _hero.getKlassSkills());
    _heroDisp.addInventory(inventory);
    _heroDisp.addMagicItem(inventory.getNameList(ItemCategory.MAGIC));
    if (_hero.canUseMagic()) {
      _heroDisp.addMaterials(inventory.getNameList(ItemCategory.SPELL_MATERIAL));
      _heroDisp.addSpell(_hero.getSpellBook());
    }
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
   * Rename the Hero to the name selected
   * 
   * @param name the new name for the character
   * @return true if the rename worked correctly; else false
   */
  public void renamePerson(String name)
  {
    _hero.setName(name);
  }

  public boolean overwritePerson()
  {
    return _dorm.update(_hero);
  }

  public boolean createPerson()
  {
    return _dorm.add(_hero);
  }
} // end of HeroDisplayCiv class

