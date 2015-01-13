/**
 * SkillDisplayCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.civ.BaseCiv;
import mylib.civ.DataShuttle;
import chronos.civ.SkillKeys;
import chronos.pdc.Skill;
import chronos.pdc.registry.SkillRegistry;

// TODO: Move this to DungeonWizard. It is not part of Adventurer

/**
 * Output Civ: Will create a GUI widget <code>SkillDisplay</code>, passing output data to it, which
 * then formats and displays the data because the GUI has no knowledge of PDC objects.
 * 
 * @author Timothy Armstrong
 * @version June 21 2011 // original <br>
 *          August 7, 2011 // changed validate to submit <br>
 *          Sept 1, 2011 // Numerous changes to work with display <br>
 */
public class SkillDisplayCiv extends BaseCiv<SkillKeys, SkillKeys> implements Observer
{
  /** Associated Inventory of the Person */
  private SkillRegistry _skreg = null;

  static public final int ASSOCATION_ERROR = -10;

  /**
   * Define the kinds of classes a Hero might be (first one is default): Human, Dwarf, Elf, Gnome,
   * Half-Elf, Half-Orc, and Hobbit.
   */
  static private final String[] _klassList = {"Peasant", "Cleric",
      "Fighter", "Magic User", "Thief"};

  /**
   * Define the kinds of races a Hero might be (first one is default): Human, Dwarf, Elf, Gnome,
   * Half-Elf, Half-Orc, and Hobbit.
   */
  static private final String[] _raceList = {"Human", "Dwarf", "Elf",
      "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  /** Error type for validating input for Skills */
  static public final int BAD_NAME_FIELD = -1;

  /** Error type for validating input for Skills */
  static public final int BAD_DESC_FIELD = -2;

  /** Error type for validating input for Skills */
  static public final int INVALID_WHITESPACE = -3;

  /** Data shuttle */
  private DataShuttle<SkillKeys> _ds = null;

  /** Widget shuttle */
  private DataShuttle<SkillKeys> _ws = null;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  // /** Default constructor */
  // public SkillDisplayCiv()
  // {
  // _ds = new DataShuttle<SkillKeys>(SkillKeys.class);
  // _ws = new DataShuttle<SkillKeys>(SkillKeys.class);
  // // This should be _model
  // _skreg = (SkillRegistry) RegistryFactory.getInstance().getRegistry(RegKey.SKILL);
  // // _skreg.load(); //TODO: Fix this line.
  // // _skreg.addObserver(this);
  // }

  /**
   * Deletes the skill with the name passed
   * 
   * @param skillName the name of the skill to be deleted
   * @return returns number of skills in registry if successful, -1 otherwise
   */
  public int delete(String skillName)
  {
    Skill skill = null;
    skill = _skreg.getSkill(skillName);
    _skreg.delete(skill);
    return _skreg.getNbrElements();
  }

  /**
   * Implemented method to return data shuttle with SkillKeys
   * 
   * @return Returns shuttle with default data for display
   */
  public DataShuttle<SkillKeys> getDefaults()
  {
    if (_ds == null) {
      return null;
    }

    ArrayList<String> skills = getSkills();
    skills.add(0, "NEW");

    String[] races = getRaces();
    String[] klasses = getKlasses();

    ArrayList<String> racelist = new ArrayList<String>();
    for (int i = 0; i < races.length; i++) {
      racelist.add(races[i]);
    }
    racelist.add(0, "All");

    ArrayList<String> klasslist = new ArrayList<String>();
    for (int i = 0; i < klasses.length; i++) {
      klasslist.add(klasses[i]);
    }
    klasslist.add(0, "All");

    // Build shuttle of default data
    _ds.putField(SkillKeys.NAME, SkillKeys.NAME.getDefault());
    _ds.putField(SkillKeys.ACTION, SkillKeys.ACTION.getDefault());
    _ds.putField(SkillKeys.DESC, SkillKeys.DESC.getDefault());
    _ds.putField(SkillKeys.RACE, SkillKeys.RACE.getDefault());
    _ds.putField(SkillKeys.KLASS, SkillKeys.KLASS.getDefault());
    _ds.putField(SkillKeys.RACELIST, racelist);
    _ds.putField(SkillKeys.KLASSLIST, klasslist);
    _ds.putField(SkillKeys.SKILLLIST, skills);

    _ws = convertToDisplay(_ds);
    return _ws;
  }

  // /**
  // * Loads the skill with the given name into the shuttle and passes back
  // *
  // * @param skillName name of the skill to be populated
  // * @return data shuttle packed with information of skill named in parameter
  // */
  // public DataShuttle<SkillKeys> getSkill(String skillName)
  // {
  // // Create the skill and the data shuttle
  // Skill skill = null;
  // skill = _skreg.getSkill(skillName);
  //
  // if (skill == null) {
  // return null;
  // } else {
  // return skill.loadShuttle(_ws);
  // }
  // }

  /**
   * Gets the list of skills for display
   * 
   * @return the names of all the Skills in the registry
   */
  public ArrayList<String> getSkills()
  {
    ArrayList<String> skillList = new ArrayList<String>();
    for (Skill s : _skreg.getSkillList()) {
      skillList.add(s.getName());
    }
    return skillList;
  }

  /**
   * Gets the list of races for display
   * 
   * @return the names of all the Races
   */
  public String[] getRaces()
  {
    return _raceList;
  }

  /**
   * Gets the list of Klasses for display
   * 
   * @return the names of all the Klasses
   */
  public String[] getKlasses()
  {
    return _klassList;
  }

  // /**
  // * Check if the input passed is valid for a skill
  // *
  // * @param ds Shuttle packed with data for validation
  // * @return True if the values are acceptable
  // */
  // @Override
  // public DataShuttle<SkillKeys> isValid(DataShuttle<SkillKeys> ws)
  // {
  // _ds = convertToModel(ws);
  //
  // String name = (String) _ds.getField(SkillKeys.NAME);
  // String desc = (String) _ds.getField(SkillKeys.DESC);
  //
  // // Check for empty name
  // if ((name == null) || (name.trim().length() == 0)) {
  // MsgCtrl.errMsgln("Bad string passed: name");
  // _ds.setErrorType(ErrorType.FIELD_INVALID);
  // _ds.setErrorSource(SkillKeys.NAME);
  // _ds.setErrorMessage("Bad string passed: Name");
  // }
  //
  // // Check for empty description
  // else if (desc == null || (desc.trim().length() == 0)) {
  // MsgCtrl.errMsgln("Bad string passed: description");
  // _ds.setErrorType(ErrorType.FIELD_INVALID);
  // _ds.setErrorSource(SkillKeys.DESC);
  // _ds.setErrorMessage("Bad string passed: Description");
  // }
  //
  // // Check for illegal characters in name
  // else {
  // for (int i = 0; i < name.length(); i++) {
  // Character check = name.charAt(i);
  // if (check == '\t' || check == '\n' || check == '\b') {
  // MsgCtrl.errMsgln("Invalid whitespace in skill name");
  // _ds.setErrorType(ErrorType.FIELD_INVALID);
  // _ds.setErrorSource(SkillKeys.NAME);
  // _ds.setErrorMessage("Invalid whitespace in Name");
  // }
  // }
  // }
  // return _ds;
  // }

  /**
   * Check if the input passed is valid for a skill
   * 
   * @param name Name of the skill to be created
   * @param desc Description for the skill
   * @return True if the values are acceptable
   */
  public int isValid(String name, String desc)
  {
    int retVal = Constants.OK;
    // Check for empty name
    if ((name == null) || (name.trim().length() == 0)) {
      MsgCtrl.errMsgln("Bad string passed: name");
      retVal = BAD_NAME_FIELD; // -1;
    }

    // Check for empty description
    else if (desc == null || (desc.trim().length() == 0)) {
      MsgCtrl.errMsgln("Bad string passed: description");
      retVal = BAD_DESC_FIELD; // -2;
    }

    // Check for illegal characters in name
    else {
      for (int i = 0; i < name.length(); i++) {
        Character check = name.charAt(i);
        if (check == '\t' || check == '\n' || check == '\b') {
          MsgCtrl.errMsgln("Invalid whitespace in skill name");
          retVal = INVALID_WHITESPACE; // -3;
        }
      }
    }
    return retVal;
  }

  /**
   * Convert all SkillRegistry data to Strings for the widget
   * 
   * @param shuttle in/out shuttle containing the model/widget data
   * @return shuttle containing widget String data
   */
  public DataShuttle<SkillKeys> convertToDisplay(DataShuttle<SkillKeys> shuttle)
  {
    // Guard: if shuttle is null, return false immediately
    if (shuttle == null) {
      return null;
    }
    // Explicit cast for unchecked exception
    DataShuttle<SkillKeys> ds = shuttle;
    ds.putField(SkillKeys.ACTION, "Not yet implemented");
    // case NAME: // no change to this key
    // break;
    // case DESC: // no change to this key
    // break;
    // case KLASSLIST: // no change to this key
    // break;
    // case SKILLLIST: // no change to this key
    // break;
    // case RACELIST: // no change to this key
    // break;
    // default:
    // ds.setErrorType(ErrorType.NULL_KEY);
    // ds.setErrorMessage(List.MISSING_KEY_MSG + key);
    // break;
    // }
    // }
    // // In case the shuttle is of the wrong type
    // } catch (ClassCastException ex) {
    // return null;
    // }
    return ds;
  }

  /*
   * PRIVATE METHODS
   */

  /**
   * Convert all SkillRegistry data from Strings for the model
   * 
   * @param shuttle in/out shuttle containing the model/widget data
   * @return shuttle containing widget String data
   */
  public DataShuttle<SkillKeys> convertToModel(DataShuttle<SkillKeys> shuttle)
  {
    // Guard: if shuttle is null, return false immediately
    if (shuttle == null) {
      return null;
    }
    // Explicit cast for unchecked exception
    DataShuttle<SkillKeys> ds = shuttle;
    // try {
    // for (SkillKeys key : ds.getKeys()) {
    // switch (key) {
    // case ACTION:
    ds.putField(SkillKeys.ACTION, "None");
    // break;
    // case NAME: // no change to this key
    // break;
    // case DESC: // no change to this key
    // break;
    // case KLASSLIST: // no change to this key
    // break;
    // case RACELIST: // no change to this key
    // break;
    // default:
    // ds.setErrorType(ErrorType.NULL_KEY);
    // ds.setErrorMessage(List.MISSING_KEY_MSG + key);
    // break;
    // }
    // }
    // // In case the shuttle is of the wrong type
    // } catch (ClassCastException ex) {
    // return null;
    // }

    return ds;
  }

  /**
   * Accept data in a shuttle, verify that it is correct, and create a new skill from the data
   * input. Add the skill into the SkillRegistry
   * 
   * @return true if the save worked correctly
   */
  public int save(DataShuttle<SkillKeys> ws)
  {
    MsgCtrl.msgln(this, "\tsave(): ");

    DataShuttle<SkillKeys> ds = convertToModel(ws);

    String name = (String) ds.getField(SkillKeys.NAME);
    String desc = (String) ds.getField(SkillKeys.DESC);

    Skill newSkill = null;
    try {
      newSkill = new Skill(name, desc); // , "Not implemented yet", race, klass);
    } catch (ApplicationException e) {
      MsgCtrl.errMsgln("Error creating skill: " + e.getMessage());
    }

    _skreg.add(newSkill);
    return _skreg.getNbrElements();
  }

  public DataShuttle<SkillKeys> getSkill(String _skillSelected)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update(Observable o, Object arg)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected DataShuttle<SkillKeys> isValid(DataShuttle<SkillKeys> shuttle)
  {
    // TODO Auto-generated method stub
    return null;
  }

  // /**
  // * Wrapper for SkillRegistry save method
  // *
  // * @return Returns number of skill saved
  // */
  //
  // public int saveRegistry() {
  // int numBefore = _skreg.getNbrElements();
  // // if (_skreg.saveOut() == numBefore)
  // if (_skreg.save() == numBefore) {
  // return _skreg.getNbrElements();
  // } else
  // return Constants.ERROR;
  // }

  // /**
  // * Accept data in a shuttle, verify that it is correct, and create a new skill from the data
  // * input. Add the skill into the SkillRegistry
  // *
  // * @param _ds Shuttle containing the fields for skill to be validated
  // * @return data (widget) shuttle with error modes set
  // */
  // @Override
  // // public DataShuttle<SkillKeys> submit (List ds)
  // public DataShuttle<SkillKeys> submit(DataShuttle<SkillKeys> ws)
  // {
  // _ds = convertToModel(ws);
  // MsgCtrl.msgln(this, "\tsubmit(): ");
  // // Guard against null
  // if (_ds == null) {
  // return _ds;
  // }
  //
  // _ds.clearErrors();
  // isValid(_ds);
  //
  // if (_ds.getErrorType() == ErrorType.OK) {
  // // First check if the skill exists, return overwrite prompt
  // Skill skill = _skreg.getSkill((String) _ds.getField(SkillKeys.NAME));
  // if (skill != null) {
  // _ds.setErrorType(ErrorType.CREATION_EXCEPTION);
  // _ds.setErrorSource(SkillKeys.NAME);
  // _ds.setErrorMessage("Skill already exists");
  // }
  // else
  // {
  // if (save (ds) != Chronos.ERROR)
  // {
  // ds.clearErrors();
  // ds.setErrorType(ErrorType.OK);
  // ds.putField (SkillKeys.SKILLLIST, getSkills());
  // retval = true;
  // }
  // else
  // {
  // ds.setErrorType (ErrorType.CREATION_EXCEPTION);
  // ds.setErrorSource (SkillKeys.NAME);
  // ds.setErrorMessage("Error creating skill");
  // retval = false;
  // }
  // }
  // }
  // // return retval;
  // _ws = convertToDisplay(_ds);
  // return _ws;
  // }

  // /*
  // * Update the widget from the update SkillRegistry model
  // *
  // * @param myObs the object being watched for changes
  // *
  // * @param myObject arbitary object that the observable model passes to this observer
  // */
  // public void update(Observable myObs, Object myObject)
  // {
  // MsgCtrl.errMsgln("Update message received");
  // // Guard: if either argument is null, return immediately
  // if ((myObs == null) || (myObject == null)) {
  // return;
  // } else {
  // _ws = getDefaults();
  // convertToDisplay(_ws);
  // }
  // // Explicit cast of data shuttle received to shuttle of right type
  // // @SuppressWarnings("unchecked")
  // // DataShuttle<SkillKeys> ds = (DataShuttle<SkillKeys>) myObject;
  // //
  // // try {
  // // for (SkillKeys key : ds.getKeys()) {
  // // switch (key) {
  // // case ACTION:
  // }

} // end of SkillDisplayCiv class

