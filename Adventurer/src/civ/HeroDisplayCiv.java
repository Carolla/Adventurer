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
import java.util.List;

import chronos.civ.PersonKeys;
import chronos.pdc.AttributeList;
import chronos.pdc.Item;
import chronos.pdc.MiscKeys.ItemCategory;
import chronos.pdc.Skill;
import chronos.pdc.character.Hero;
import chronos.pdc.character.Inventory;
import dmc.HeroReadWriter;


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

  /** The categories for hunger, to convert Satiety points into a hunger state */
  enum hungerStage {
    FULL, NOT_HUNGRY, HUNGRY, WEAK, FAINT, STARVED
  };

  /** Whether character is being loaded. */
  public static boolean LOADING_CHAR = false;
  /** Whether character is being created. */
  public static boolean NEW_CHAR = false;

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
    if (!HeroDisplayCiv.LOADING_CHAR) {
      HeroDisplayCiv.NEW_CHAR = true;
    }
    
    _mfCiv = mfCiv;
  }

  /** Restore the mainframe panels to their previous state */
  public void backToMain()
  {
    _mfCiv.backToMain();
  }

  /**
   * Delete the Person
   * 
   * @return true if the delete worked correctly; else false
   */
  public boolean deletePerson()
  {
    // return _hero.delete();
    return false;
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
    _outputMap = hero.loadAttributes(_outputMap);
    _widget = new HeroDisplay(this, firstTime);

    _mfCiv.replaceLeftPanel(_widget);
  }


  public EnumMap<PersonKeys, String> getAttributes()
  {
    return _outputMap;
  }

  /** Restore the mainframe panels to their previous state */
  public void back()
  {
    _mfCiv.back();
  }

  public List<String> getKlassSkills()
  {
    return _hero.getKlassSkills();
  }

  public List<String> getOcpSkills()
  {
    return _hero.getOcpSkills();
  }

  public List<String> getRaceSkills()
  {
    return _hero.getRaceSkills();
  }

  /**
   * @return the collection of Items
   */
  public Inventory getInventory()
  {
    return _hero.getInventory();
  }


  /**
   * Retrieve a list of all items in the given invenotry by name
   * 
   * @param cat category of item to build a subset from
   * @return the list of names for the subset inventory
   */
  public List<String> getInventoryNames(ItemCategory cat)
  {
    return getInventory().getNameList(cat);
  }


  /*
   * @return the length of the inventory (number of Items)
   */
  public int getInventorySize()
  {
    return getInventory().getNbrItems();
  }


  /**
   * @return the list of spells known
   */
  public List<String> getSpellBook()
  {
    return _hero.getSpellBook();
  }

  public boolean populateAbilityScores(AttributeList attribs)
  {
    return true;
  }

  /**
   * Format the inventory data and tell the widget to display it
   * 
   * @param itemList list of Items to display
   * @return false is an error occurs
   */
  public boolean populateInventory(List<Item> itemList)
  {
    // Create a shuttle to contain the data and convert to widget String
    // format
    // List<String> items = convertItems(itemList);
    // _widget.displayInventory(items);
    return true;
  }

  /**
   * Format the Skill data and tell the widget to display it
   * 
   * @param _skills list of Hero's skills to display
   * @return false is an error occurs
   */
  public boolean populateSkills(List<Skill> _skills)
  {
    // Create a shuttle to contain the data and convert to widget String
    // format
    // List<String> skillList = convertSkills(_skills);
    // if (!Constants.IN_TEST) {
    // _widget.displaySkills(skillList);
    // }
    return true;
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
    HeroReadWriter dorm = new HeroReadWriter();
    if (overwrite == false) {
      retflag = dorm.save(_hero, _hero.getName());
    } else {
      retflag = dorm.overwrite(_hero, _hero.getName());
    }
    return retflag;
  }

} // end of HeroDisplayCiv class

