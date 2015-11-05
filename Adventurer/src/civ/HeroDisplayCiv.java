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
import hic.Mainframe;

import java.util.EnumMap;
import java.util.List;

import pdc.Inventory;
import pdc.character.Hero;
import chronos.pdc.AttributeList;
import chronos.pdc.Item;
import chronos.pdc.MiscKeys.ItemCategory;
import chronos.pdc.Skill;

/**
 * Output Civ: Creates the GUI widget <code>HeroDisplay</code>, passing output data to it, which
 * then formats and displays the Hero because the GUI has no knowledge of PDC objects.
 * 
 * @author Alan Cline
 * @version May 31 2010 // original <br>
 *          Jul 4 2010 // segregated HIC from PDC <br>
 *          Jan 4 2011 // removed Observer MVP model approach <br>
 *          Oct 1 2015 // revised for new Hero generation rules <br>
 */
public class HeroDisplayCiv
{
  /** Associated Hero */
  private Hero _hero = null;
  /** Associated GUI */
  private HeroDisplay _widget = null;

  /** Associated Inventory of the Person */
  private Inventory _inventory = null;

  /** The categories for hunger, to convert Satiety points into a hunger state */
  enum hungerStage {
    FULL, NOT_HUNGRY, HUNGRY, WEAK, FAINT, STARVED
  };

  /** Whether character is being loaded. */
  public static boolean LOADING_CHAR = false;
  /** Whether character is being created. */
  public static boolean NEW_CHAR = false;

  /** Hero data are converted and sent to the GUI in this EnumMap */
  private EnumMap<PersonKeys, String> _outputMap;

  private Mainframe _mf;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Displays a newly created Hero before it is saved in the Dormitory
   * 
   * @param hero the model object from which to get the display data
   */
  public HeroDisplayCiv(Mainframe mf)
  {
    if (!HeroDisplayCiv.LOADING_CHAR) {
      HeroDisplayCiv.NEW_CHAR = true;
    }
    _mf = mf;
    _outputMap = new EnumMap<PersonKeys, String>(PersonKeys.class);
  }


  // /**
  // * Load all person names from the database Method made static because in order to create a
  // * HeroDisplayCiv, a person or person name is needed, which is currently being loaded
  // *
  // * @return list of people in the dorimtory
  // */
  // public static List<String> openDormitory()
  // {
  // Person tmp = new Person();
  // return tmp.wake();
  // }



  // /**
  // * Creates the Civ and its associated output HeroDisplay widget. Do not display the GUI if this
  // is
  // * in JUnit test
  // *
  // * @param name the name of the person to be summoned
  // */
  // public HeroDisplayCiv(String name)
  // {
  // HeroDisplayCiv.LOADING_CHAR = true;
  //
  // // Make a null person to run the summon method
  // _hero = new Hero();
  //
  // // Now load the person from the registry and display it
  // _hero = _hero.load(name);
  // _hero.display();
  // this.resetLoadState();
  // }


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
   * Display the Hero the HeroDisplay widget
   * 
   * @param hero to display
   */
  public void displayHero(Hero hero)
  {
    _hero = hero;
    _outputMap = hero.loadAttributes(_outputMap);
    _widget = new HeroDisplay(this, _mf);

    _mf.addPanel(_widget);
  }


  public EnumMap<PersonKeys, String> getAttributes()
  {
    return _outputMap;
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
    _inventory = _hero.getInventory();
    return _inventory;
  }


  /** Retrieve a list of all items in the given invenotry by name
   * 
   * @param cat     category of item to build a subset from
   * @return the list of names for the subset inventory
   */
  public List<String> getInventoryNames(ItemCategory cat)
  {
    return _inventory.getNameList(cat);
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
    //List<String> skillList = convertSkills(_skills);
    // if (!Constants.IN_TEST) {
    //_widget.displaySkills(skillList);
    // }
    return true;
  }
    
  /*
   * @return the length of the inventory (number of Items)
   */
  public int getInventorySize()
  {
    return _inventory.getNbrItems();
  }

  /**
   * @return the list of spells known
   */
  public List<String> getSpellBook()
  {
    return _hero.getSpellBook();
  }

  
  // /**
  // * Save the Person to a new file. This method pops up a file chooser so the user can select a
  // * filename; else the Hero's name is used. If the Person is newly created, then the Person is
  // * saved to a new file. If the Person already existed, then no file writes are written because
  // the
  // * <code>HeroDislay</code> panel does not allow edits; Save As options are disabled.
  // *
  // * @return true if the save worked correctly
  // */
  // public List<PersonFileData> getPersonFileData()
  // {
  // MsgCtrl.msgln(this, "\tsavePerson(): ");
  //
  // //Get the requested key data map needed for the chooser from the CIV
  // _fileData = new List<PersonFileData>();
  // _fileData.assignKey(PersonFileData.RESOURCE_DIR);
  // _fileData.assignKey(PersonFileData.DEFAULT_FILENAME);
  // _fileData.assignKey(PersonFileData.PERSON_EXT);
  // _fileData.assignKey(PersonFileData.CHOOSER_LABEL);
  // return _person.getFileData(_fileData);
  // }

  public boolean populateAbilityScores(AttributeList attribs)
  {
    return true;
  }

  // /**
  // * Format the model data and tell the widget to display it
  // *
  // * @param ds map containing internally formatted key values
  // * @return false is an error occurs
  // */
  // public boolean populateAttributes(EnumMap<PersonKeys, String> ds)
  // {
  // // Create a shuttle to contain the data and convert to widget String format
  // _outputMap = convertAttributes(ds);
  // _widget.displayAttributes(_outputMap);
  // return true;
  // }

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
    //List<String> items = convertItems(itemList);
    //_widget.displayInventory(items);
    return true;
  }

  // /**
  // * Format the Skill data and tell the widget to display it
  // *
  // * @param skills list of Hero's skills to display
  // * @return false is an error occurs
  // */
  // public boolean populateSkills(ArrayList<Skill> skills)
  // {
  // // Create a shuttle to contain the data and convert to widget String
  // // format
  // ArrayList<String> skillList = convertSkills(skills);
  // // if (!Constants.IN_TEST) {
  // _widget.displaySkills(skillList);
  // // }
  // return true;
  // }

  /**
   * Rename the Hero to the name selected
   * 
   * @param name the new name for the character
   * @return true if the rename worked correctly; else false
   */
  public boolean renamePerson(String name)
  {
    // return _hero.rename(name);
    return false;
  }

  /**
   * Sets both load/new fields to false
   */
  public void resetLoadState()
  {
    HeroDisplayCiv.LOADING_CHAR = false;
    HeroDisplayCiv.NEW_CHAR = false;
  }

  /**
   * Save the Persona to the filename selected
   * 
   * @param saveName filename to use for saving the Person
   * @return true if the save worked correctly; else false
   */
  public boolean savePerson(boolean overwrite)
  {
    // return _hero.save(overwrite);
    return false;
  }

//  /**
//   * Convert the Skill object into string fields for list display. All Item fields are concatenated
//   * into a single delimited string.
//   * 
//   * @param _skills list of Skills objects to convert
//   * @return the string list of output data
//   */
//  private List<String> convertSkills(List<Skill> _skills)
//  {
//    List<String> skillList = new ArrayList<String>(_skills.size());
//    for (Skill skill : _skills) {
//      // Each Skill consists of: name, description, race, klass, and
//      // action (excluded)
//      String name = skill.getName();
//      // String race = skill.getRace();
//      // String klass = skill.getKlass();
//      String description = skill.getDescription();
//      String skillStr = name + BaseCiv.DELIM + description; // race + BaseCiv.DELIM + klass + BaseCiv.DELIM + description;
//      skillList.add(skillStr);
//    }
//    return skillList;
//  }

  /*
   * PRIVATE METHODS
   */

  // TODO: Save for runtime version, not Hero initialization
  // /** Convert the satiety point value and current satiety points into a
  // label that
  // * describes the Person's hunger state.
  // * To find these states of hunger, the maxSatiety is broken into 10%
  // increments
  // * <BL>
  // * <LI> FULL: Current satiety > 90%. </LI>
  // * <LI> NOT_HUNGRY: Current satiety is between 30% and 90% of satiety
  // value. </LI>
  // * <LI> HUNGRY: Current satiety is between 0 and 30%. </LI>
  // * <LI> WEAK: Current satiety is between -20 and 0% </LI>
  // * <LI> FAINT: Current satiety is between -20 and -200% </LI>
  // * <LI> STARVED: Current satiety is less than -200% </LI>
  // * </BL>
  // *
  // * @return One of the hunger states as a String value for display
  // */
  // public String getHungerFlag()
  // {
  // // String[] hungerStage =
  // // {"FULL", "NOT HUNGRY", "HUNGRY", "WEAK", "FAINT", "STARVED"};
  // // // Percent ranges for hunger stages; STR is lost as {0, 0, 0, -1, -1
  // per 20};
  // // long[] hunger = {90, 20, 0, -20, -200};
  // int deltaStr = 0;
  //
  // // Decrement the current satiety by the amount of SP's burned
  // double deltaSP = ( _personalBurnRate * (double) burnTime);
  // _curSatiety = _curSatiety - deltaSP;
  //
  // // // Find the burnrate of the person in seconds
  // // double burnRate = _maxSatiety / Constants.SECS_PER_DAY;
  // // // How many notches did this drop?
  // // int nbrNotches = (int) (_maxSatiety % _hungerNotch);
  //
  // // Now categorize the hunger flag into its categories
  // // Calculate the satiety points as a consume-ratio in percent
  // double hungryRatio = (_curSatiety / _maxSatiety ) * 100.0;
  // // Convert the point value to a hunger flag
  // // FULL -- cannot eat any more; set flag only
  // if (hungryRatio >= _hunger[0]) {
  // _hungerFlag = _hungerStage[0];
  // }
  // // NOT HUNGRY -- set flag only (or turn off other flags)
  // else if ((hungryRatio < _hunger[0]) && (hungryRatio >= _hunger[1])) {
  // _hungerFlag = _hungerStage[1];
  // }
  // // HUNGRY -- set flag only
  // else if ((hungryRatio < _hunger[1]) && (hungryRatio >= _hunger[2])) {
  // _hungerFlag = _hungerStage[2];
  // }
  // // WEAK -- set flag, and deduct 1 pt of STR
  // else if ((hungryRatio < _hunger[2]) && (hungryRatio >= _hunger[3])) {
  // _hungerFlag = _hungerStage[3];
  // // Find the multiples of Str loss due to hunger
  // deltaStr = (int)( Math.round(hungryRatio)) / HUNGER_STR_LOSS;
  // deltaStr = Math.abs(deltaStr);
  // }
  // // FAINT -- set flag, and deduct 1 pt of STR per -20%
  // else if ((hungryRatio < _hunger[3]) && (hungryRatio >= _hunger[4])) {
  // _hungerFlag = _hungerStage[4];
  // // Find the multiples of Str loss due to hunger
  // deltaStr = (int)( Math.round(hungryRatio)) / HUNGER_STR_LOSS;
  // deltaStr = Math.abs(deltaStr);
  // }
  // // STARVED -- set flag and kill Person
  // else {
  // _hungerFlag = _hungerStage[5];
  // }
  // return deltaStr;
  // }


  // ==================================================================
  // INNER CLASS: MockHeroDisplayCiv
  // ==================================================================

  // /**
  // * Convert the data in the Hero object to stringified data fields for widget display
  // *
  // * @param hero to display
  // * @return the output map of all hero data fields
  // */
  // private EnumMap<PersonKeys, String> getAttributes(Hero hero)
  // {
  // _outputMap = hero.getAttributes(_outputMap);
  // return _outputMap;
  //
  // }

  // /**
  // * Load all person names from the database Method made static because in order to create a
  // * HeroDisplayCiv, a person or person name is needed, which is currently being loaded
  // *
  // * @return list of people in the dorimtory
  // */
  // public static List<String> openDormitory()
  // {
  // Person tmp = new Person();
  // return tmp.wake();
  // }


//  /**
//   * Convert the Item objects into string fields for list display. All Item fields are concatenated
//   * into a single delimited string.
//   * 
//   * @param items list of Item object to convert
//   * @return the string list of output data
//   */
//  private List<String> convertItems(ArrayList<Item> items)
//  {
//    List<String> itemList = new ArrayList<String>(items.size());
//    for (Item item : items) {
//      // Each item consists of: Inventory category, name, quantity, and
//      // weight (each)
//      String cat = item.getCategory().toString();
//      String name = item.getName();
//      String qty = String.valueOf(item.getQuantity());
//      // Convert weight from ounces to lbs and ozs
//      // int weight = item.getWeight();
//      // int lbs = weight / Constants.OUNCES_PER_POUND;
//      // int ozs = weight % Constants.OUNCES_PER_POUND;
//      // String lbWt = String.valueOf(lbs);
//      // String ozWt = String.valueOf(ozs);
//      // Build displayable string
//      String itemStr = cat + BaseCiv.DELIM + name + BaseCiv.DELIM + qty; // + BaseCiv.DELIM + lbWt
//      // + BaseCiv.DELIM + ozWt;
//      itemList.add(itemStr);
//    }
//    return itemList;
//  }


  // /**
  // * Creates the Civ and its associated output HeroDisplay widget. Do not display the GUI if this
  // is
  // * in JUnit test
  // *
  // * @param name the name of the person to be summoned
  // */
  // public HeroDisplayCiv(String name)
  // {
  // HeroDisplayCiv.LOADING_CHAR = true;
  //
  // // Make a null person to run the summon method
  // _hero = new Hero();
  //
  // // Now load the person from the registry and display it
  // _hero = _hero.load(name);
  // _hero.display();
  // this.resetLoadState();
  // }


  // /**
  // * Convert the Skill object into string fields for list display. All Item fields are
  // concatenated
  // * into a single delimited string.
  // *
  // * @param skills list of Skills objects to convert
  // * @return the string list of output data
  // */
  // private ArrayList<String> convertSkills(ArrayList<Skill> skills)
  // {
  // ArrayList<String> skillList = new ArrayList<String>(skills.size());
  // for (int k = 0; k < skills.size(); k++) {
  // // Each Skill consists of: name, description, race, klass, and
  // // action (excluded)
  // Skill skill = skills.get(k);
  // String name = skill.getName();
  // // String race = skill.getRace();
  // // String klass = skill.getKlass();
  // String description = skill.getDescription();
  // String skillStr = name + BaseCiv.DELIM + description; // race + BaseCiv.DELIM + klass +
  // // BaseCiv.DELIM + description;
  // skillList.add(k, skillStr);
  // }
  // return skillList;
  // }



  /** Inner class used for testing private methods */
  public class MockHeroDisplayCiv
  {
    /** Default ctor */
    public MockHeroDisplayCiv()
    {}

    // /**
    // * Get the data shuttle for this civ
    // *
    // * @return data shutle
    // */
    // public List<PersonKeys> getList()
    // {
    // return HeroDisplayCiv.this._ds;
    // }

    // // TODO: Move to MockCiv
    // /* Verify correct adjective for given height
    // * @param height of the Person
    // * @param gender of the person to return different values for testing
    // * @return word that describes the person in terms of height
    // */
    // public String initHeightDescriptor(double height, String gender)
    // {
    // setGender(gender);
    // return Human.this.initHeightDescriptor(height);
    // }
    //
    //
    // // TODO: Move to MockCiv
    // /* Verify correct adjective for given weight
    // * @param weight of the Person
    // * @param gender of the person to return different values for testing
    // * @return word that describes the person in terms of height
    // */
    // public String initWeightDescriptor(double weight, String gender)
    // {
    // setGender(gender);
    // return Human.this.initWeightDescriptor(weight);
    // }

  } // end of MockHeroDisplayClass

} // end of HeroDisplayCiv class

