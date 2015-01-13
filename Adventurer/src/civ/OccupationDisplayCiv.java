/**
 * OccupationDisplayCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.civ.BaseCiv;
import mylib.civ.DataShuttle;
import chronos.civ.OccupationKeys;
import chronos.pdc.Occupation;
import chronos.pdc.Skill;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

// TODO: Move this to {@code Quest Master}. It is not part of {@code Adventurer}.

/**
 * Output Civ: Will create a GUI widget <code>SkillDisplay</code>, passing output data to it, which
 * then formats and displays the data because the GUI has no knowledge of PDC objects.
 * 
 * @author Timothy Armstrong
 * @version Sept 12, 2011 // original <br>
 */
public class OccupationDisplayCiv extends BaseCiv<OccupationKeys, OccupationKeys> // implements
                                                                                  // Observer
{
  /** Occupation Registry */
  private OccupationRegistry _occreg = null;

  /** Skill Registry */
  private SkillRegistry _skreg = null;

  // TODO: Convert these int constants to enums
  /** Error type for validating input for Skills */
  static public final int BAD_NAME_FIELD = -1;

  /** Error type for validating input for Skills */
  static public final int BAD_DESC_FIELD = -2;

  /** Error type for validating input for Skills */
  static public final int INVALID_WHITESPACE = -3;

  private DataShuttle<OccupationKeys> _ds = null;
  private DataShuttle<OccupationKeys> _ws = null;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public OccupationDisplayCiv()
  {
    _ds = new DataShuttle<OccupationKeys>(OccupationKeys.class);
    _ws = new DataShuttle<OccupationKeys>(OccupationKeys.class);
    _occreg = (OccupationRegistry) RegistryFactory.getInstance().getRegistry(RegKey.OCP);
    _skreg = (SkillRegistry) RegistryFactory.getInstance().getRegistry(RegKey.SKILL);
  }

  /**
   * Deletes the occupation with the name passed
   * 
   * @param occName the name of the occupation to be deleted
   * @return returns number of occs in registry if successful, -1 otherwise
   */
  public int delete(String occName)
  {
    _occreg.delete(_occreg.getOccupation(occName));

    return _occreg.getNbrElements();
  }

  /**
   * Implemented method to return data shuttle with SkillKeys
   * 
   * @return Returns shuttle with default data for display
   */
  public DataShuttle<OccupationKeys> getDefaults()
  {
    if (_ds == null) {
      return null;
    }
    List<String> skills = getSkills();
    List<String> occlist = new ArrayList<String>();

    for (Occupation o : _occreg.getOccupationList()) {
      occlist.add(o.getName());
    }
    occlist.add(0, "NEW");

    // Build shuttle of default data
    _ds.putField(OccupationKeys.NAME, OccupationKeys.NAME.getDefault());
    _ds.putField(OccupationKeys.DESC, OccupationKeys.DESC.getDefault());
    _ds.putField(OccupationKeys.OCCLIST, occlist);
    _ds.putField(OccupationKeys.SKILLLIST, skills);

    _ws = convertToDisplay(_ds);
    return _ws;
  }

  // /**
  // * Loads the occupation with the given name into the shuttle and passes back
  // *
  // * @param occName name of the occupation to be populated
  // * @return data shuttle packed with information of occupation named in parameter
  // */
  // public List<OccupationKeys> getOccupation(String occName)
  // {
  // // Create the skill and the data shuttle
  // Occupation occ = _occreg.getOccupation(occName);
  //
  // if (occ == null) {
  // return null;
  // } else {
  // return occ.loadShuttle(_ws);
  // }
  // }

  /**
   * Gets the list of skills for display
   * 
   * @return the names of all the Skills in the registry
   */
  public List<String> getSkills()
  {
    List<String> skillList = new ArrayList<String>();
    for (Skill s : _skreg.getSkillList()) {
      skillList.add(s.getName());
    }
    return skillList;
  }

  // /**
  // * Check if the input passed is valid for a skill
  // *
  // * @param ds Shuttle packed with data for validation
  // * @return True if the values are acceptable
  // */
  // @Override
  // public List<OccupationKeys> isValid(List<OccupationKeys> ws)
  // {
  // _ds = convertToModel(ws);
  //
  // String name = (String) _ds.getField(OccupationKeys.NAME);
  // String desc = (String) _ds.getField(OccupationKeys.DESC);
  //
  // // Check for empty name
  // if ((name == null) || (name.trim().length() == 0)) {
  // MsgCtrl.errMsgln("Bad string passed: name");
  // _ds.setErrorType(ErrorType.FIELD_INVALID);
  // _ds.setErrorSource(OccupationKeys.NAME);
  // _ds.setErrorMessage("Bad string passed: Name");
  // }
  //
  // // Check for empty description
  // else if (desc == null || (desc.trim().length() == 0)) {
  // MsgCtrl.errMsgln("Bad string passed: description");
  // _ds.setErrorType(ErrorType.FIELD_INVALID);
  // _ds.setErrorSource(OccupationKeys.DESC);
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
  // _ds.setErrorSource(OccupationKeys.NAME);
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
  public DataShuttle<OccupationKeys> convertToDisplay(DataShuttle<OccupationKeys> shuttle)
  {
    // Guard: if shuttle is null, return false immediately
    if (shuttle == null) {
      return null;
    }
    // Explicit cast for unchecked exception
    DataShuttle<OccupationKeys> ds = shuttle;
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
  public DataShuttle<OccupationKeys> convertToModel(DataShuttle<OccupationKeys> shuttle)
  {
    // Guard: if shuttle is null, return false immediately
    if (shuttle == null) {
      return null;
    }
    // Explicit cast for unchecked exception
    DataShuttle<OccupationKeys> ds = shuttle;
    return ds;
  }

  /**
   * Accept data in a shuttle, verify that it is correct, and create a new occupation from the data
   * input. Add into the OccupationRegistry
   * 
   * @return true if the save worked correctly
   */
  public int save(DataShuttle<OccupationKeys> ws)
  {
    MsgCtrl.msgln(this, "\tsave(): ");

    DataShuttle<OccupationKeys> ds = convertToModel(ws);

    String name = (String) ds.getField(OccupationKeys.NAME);
    // String desc = (String) ds.getField(OccupationKeys.DESC);
    String skill = (String) ds.getField(OccupationKeys.SKILL);

    Occupation newOcc = null;
    try {
      newOcc = new Occupation(name, skill);
      _occreg.add(newOcc);
    } catch (ApplicationException e) {
      MsgCtrl.errMsgln("Error creating skill: " + e.getMessage());
    }

    return _occreg.getNbrElements();

  }

  @Override
  protected DataShuttle<OccupationKeys> isValid(DataShuttle<OccupationKeys> shuttle)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getOccupation(String _occSelected)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public int getSize()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  // /**
  // * Wrapper for OccupationRegistry save method
  // *
  // * @return Returns number of skill saved
  // */
  // public int saveRegistry() {
  // int numBefore = _occreg.getNbrOccupations();
  // // if (_skreg.saveOut() == numBefore)
  // if (_occreg.save() == numBefore) {
  // return _occreg.getNbrOccupations();
  // } else
  // return Constants.ERROR;
  // }

  // /**
  // * Accept data in a shuttle, verify that it is correct, and create a new occupation from the
  // data
  // * input. Add the occupation into the ORegistry
  // *
  // * @param _ds Shuttle containing the fields for occupation to be validated
  // * @return data (widget) shuttle with error modes set
  // */
  // @Override
  // public List<OccupationKeys> submit(List<OccupationKeys> ws)
  // {
  // _ds = convertToModel(ws);
  // MsgCtrl.msgln(this, "\tsubmit(): ");
  // // Guard against null
  // if (_ds == null) {
  // return _ds;
  // }
  // _ds.clearErrors();
  // isValid(_ds);
  // if (_ds.getErrorType() == ErrorType.OK) {
  // // First check if the skill exists, return overwrite prompt
  // if (_occreg.getOccupation((String) _ds
  // .getField(OccupationKeys.NAME)) != null) {
  // _ds.setErrorType(ErrorType.CREATION_EXCEPTION);
  // _ds.setErrorSource(OccupationKeys.NAME);
  // _ds.setErrorMessage("Occupation already exists");
  // // retval = false;
  // }
  // }
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
  // }
  //
  // public int getSize()
  // {
  // _occreg.getNbrElements();
  // return 0;
  // }

} // end of OccupationDisplayCiv class

