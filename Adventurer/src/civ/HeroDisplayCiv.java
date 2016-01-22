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
import chronos.pdc.MiscKeys.ItemCategory;
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
  private HeroDisplay _widget = null;

  /** Hero data are converted and sent to the GUI in this EnumMap */
  private EnumMap<PersonKeys, String> _outputMap = new EnumMap<PersonKeys, String>(PersonKeys.class);;

  /** Reference to parent civ */
  private final MainframeCiv _mfCiv;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Displays a newly created Hero before it is saved in the Dormitory
   * 
   * @param mf mainframe connection for displaying widgets
   * @param regFact to access the Dormitory, where Heroes are stoed
   */
  public HeroDisplayCiv(MainframeCiv mfCiv)
  { 
    _mfCiv = mfCiv;
  }

  /** Restore the mainframe panels to their previous state */
  public void backToMain()
  {
    _mfCiv.backToMain();
  }

  /** Restore the mainframe panels to their previous state */
  public void back()
  {
    _mfCiv.back();
  }

  /**
   * Delete the Person
   * 
   * @return true if the delete worked correctly; else false
   */
  public boolean deletePerson()
  {
    System.err.println("Can't delete hero right now");
    System.exit(-1);
    return false;
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

  /**
   * Save the Hero into the Dormitory, adding a new Hero or overwriting an old one
   * 
   * @param overwrite if true, then will overwrite an existing Hero
   * @return true if all save operations worked as expected
   */
  public boolean savePerson(boolean overwrite)
  {
    boolean retflag = false;
    HeroRegistry dorm = new HeroRegistry();
    
    // Save when NOT in overwrite mode
    if (overwrite == false) {
      retflag = dorm.add(_hero);
    } else {
      retflag = dorm.update(_hero);
    }
    return retflag;
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
    Inventory inventory = _hero.getInventory();
    
    _outputMap = hero.loadAttributes(_outputMap);
    _widget = new HeroDisplay(this, _outputMap);
    
    _widget.addSkills(_hero.getOcpSkills(), _hero.getRaceSkills(), _hero.getKlassSkills());
    _widget.addInventory(inventory);
    _widget.addMagicItem(inventory.getNameList(ItemCategory.MAGIC));
    if (_hero.canUseMagic()) {
      _widget.addMaterials(inventory.getNameList(ItemCategory.SPELL_MATERIAL));
      _widget.addSpell(_hero.getSpellBook());
    }

    _mfCiv.replaceLeftPanel(_widget);
  }
} // end of HeroDisplayCiv class

