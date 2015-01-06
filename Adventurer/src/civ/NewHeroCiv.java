/**
 * NewHeroCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mylib.civ.DataShuttle;
import mylib.civ.DataShuttle.ErrorType;
import pdc.character.Person;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;

/**
 * Negotiates data between the input widget and the backend PDC object, using two different
 * List&ltPersonKeys&gt objects. Two shuttles are created to keep internally formatted data of the
 * model distinct from String data of the widget. This input civ takes the input data, creates the
 * Person, and creates the output HeroDisplayCiv, which gains controls when the Person is created.
 * <P>
 * Input data [default]: <BL>
 * <LI>Hero name [blank]</LI>
 * <LI>Gender [male]</LI>
 * <LI>Race [Human]</LI>
 * <LI>Occupation [None]</LI>
 * <LI>Hair color [bald]</LI> </BL>
 * 
 * @author Alan Cline
 * @version May 27 2010 // original <br>
 *          Jul 3, 2010 // support input CIV and EnumMap <br>
 *          Sep 6 2011 // refactored for MVP Stack <br>
 *          Jan 4 2012 // refactored Observer pattern back out <br>
 */
public class NewHeroCiv
{
  private Person _person = null;
  private DataShuttle<PersonKeys> _ds = null;
  private DataShuttle<NewHeroFields> _ws = null;

  private String _selectedRace = null;
  private String _selectedGender = null;

  private final String STR = "Strength";
  private final String DEX = "Dexterity";
  private final String INT = "Intelligence";
  private final String WIS = "Wisdom";
  private final String CON = "Constitution";
  private final String CHA = "Charisma";

  /** List of attributes */
  private List<String> _attributes = Arrays.asList(STR, INT, WIS, DEX, CON, CHA);

  /** key: attribute, value: AbilityScoreSelector */
  private Map _scoreSelectors = new HashMap(6);

  private int _totalPointsSpent = 0;
  private final int MAX_POINTS = 24;
  /** List of hair colors available for editing */
  static public final String[] _hairColorList =
  {"bald", "black", "blonde", "brown", "gray", "red", "silver", "streaked", "white"};


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Creates and saves the widget for reference, and shuttles for the two transport directions.
   */
  // public NewHeroCiv() {
  // // Create the shuttles, references, and model for this civ
  // _ds = new List<PersonKeys>(PersonKeys.class);
  // _ws = new List<NewHeroFields>(NewHeroFields.class);
  // }

  /**
   * Called by widget to get a widget shuttle with key data for display. Default keys needed on
   * initial display are NAME, GENDER, HAIRCOLOR (list), OCCUPATION (list), and RACE (list).
   * 
   * @return the widget shuttle for display
   */
  public DataShuttle<NewHeroFields> getDefaults()
  {
    // Put the single-valued defaults into the widget shuttle
    _ws.putField(NewHeroFields.NAME, NewHeroFields.NAME.getDefault());
    _ws.putField(NewHeroFields.GENDER, NewHeroFields.GENDER.getDefault());
    _ws.putField(NewHeroFields.KLASSNAME,
        NewHeroFields.KLASSNAME.getDefault());
    _ws.putField(NewHeroFields.RACE_OPTIONS,
        NewHeroFields.RACE_OPTIONS.getDefault());
    _ws.putField(NewHeroFields.HAIR_COLOR_OPTIONS,
        NewHeroFields.HAIR_COLOR_OPTIONS.getDefault());
    _ws.putField(NewHeroFields.OCCUPATION_OPTIONS,
        NewHeroFields.OCCUPATION_OPTIONS.getDefault());

    // Return the shuttle for display
    return _ws;
  }

  /**
   * Calls each key and confirms that the widget data is valid
   * 
   * @param ws data in widget format
   * @return widget shuttle with ErrorType.OK. If an invalid key occurs, the error methods can be
   *         checked for which key was invalid.
   */
  private DataShuttle<NewHeroFields> isValid(DataShuttle<NewHeroFields> ws)
  {
    // NewHeroFields.isValid(ws);
    return ws;
  }

  /**
   * Validates the data and reformats it for the PDC object. This method validates the data in the
   * widget shuttle, converts it to model shuttle data, and calls the model's
   * <code>unloadShuttle()</code> method to store the data.
   * 
   * @param ws contains the field keys from the widget
   * @return ws; if errors, the shuttle's error flags will be set
   */
  public DataShuttle<NewHeroFields> submit(DataShuttle<NewHeroFields> ws)
  {
    // Guard: if shuttle is null, return immediately
    if (ws == null) {
      return ws;
    }
    // Guard: if shuttle contains no keys, return empty shuttle with error flags
    if (ws.size() == 0) {
      ws.setErrorType(ErrorType.EMPTY_SHUTTLE);
      ws.setErrorMessage(DataShuttle.EMPTY_SHUTTLE_MSG);
      return ws;
    }
    // Load a value into the shuttle for each slot requested; ignore others
    // Return immediately if the requested key is not found
    try {
      // Validate the data before proceeding; bad data is returned with
      // error flags.
      if (isValid(ws).getErrorType() != ErrorType.OK) {
        return ws;
      }
      // Convert the widget shuttle to a model shuttle
      _ds = convertToModel(ws);

      // Allow the Civ to perform other actions on the data shuttle before
      // unloading it
      createPerson(_ds);

      // Local action must create a model if it doesn't exist
      if (_person == null) {
        // ws.setErrorType(ErrorType.CREATION_EXCEPTION);
        // ws.setErrorMessage("Can't create that kind of Person");
        return ws;
      }

      // Set the values from the shuttle into the model using the given
      // keys
      // If error, the shuttle will contain the error flags
      // _ds = _person.unload(_ds);

      // In case the shuttle is of the wrong type
    } catch (ClassCastException ex) {
      // ws.setErrorType(ErrorType.CLASS_CAST_EXCEPTION);
      // ws.setErrorMessage(List.CAST_EXCEPTION_MSG);
      return ws;
    }
    return ws;
  }

  /**
   * Decrease the attribute's score by one.
   * 
   * @param attrib - the attribute to decrease.
   */
  public void decreaseScore(String attrib)
  {
    AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(attrib);
    ss.decreaseScore();
  }

  public Map getAllScores()
  {

    Map retval = new HashMap(6);
    for (String attrib : _attributes) {
      AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(attrib);
      int total = ss.minValue + ss.pointsSpent;
      retval.put(attrib, total);
    }

    return retval;
  }

  /**
   * Convert the attribute Map into an AttributeList.
   * 
   * @param attribMap - the Map to convert
   * @return an AttributeList
   */
  public AttributeList getAttributeList()
  {

    int[] attribArray = new int[6];

    for (int i = 0; i < attribArray.length; i++) {
      AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(_attributes.get(i));

      attribArray[i] = ss.minValue + ss.pointsSpent;
    }

    return new AttributeList(attribArray);
  }

  /**
   * Provides the attributes to the UI to create the controls.
   * 
   * @return the List of attributes as strings.
   */
  public List<String> getAttributes()
  {
    return _attributes;
  }

  /**
   * Returns the current number of spent points in the provided attribute.
   * 
   * @param attrib - the attribute to check
   * @return the current points spent
   */
  public int getCurrent(String attrib)
  {
    AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(attrib);
    return ss.pointsSpent;
  }

  /**
   * Returns the minimum value for the provided attribute.
   * 
   * @param attrib - the attribute to check
   * @return the minimum value (as int)
   */
  public int getMin(String attrib)
  {
    AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(attrib);
    return ss.minValue;
  }

  /**
   * Returns the maximum value for the provided attribute.
   * 
   * @param attrib - the attribute to check
   * @return the maximum value (as int)
   */
  public int getMax(String attrib)
  {
    AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(attrib);
    return ss.maxValue;
  }

  /**
   * Returns the number of remaining points to spend in ability scores.
   * 
   * @return the number of unspent points.
   */
  public int getTotalRemaining()
  {
    return MAX_POINTS - _totalPointsSpent;
  }

  /**
   * Increment the attribute's score by one.
   * 
   * @param attrib - the attribute to increase.
   */
  public void increaseScore(String attrib)
  {
    AbilityScoreSelector ss = (AbilityScoreSelector) _scoreSelectors.get(attrib);
    ss.increaseScore();
  }

  /**
   * Create an instance of the Ability Score Selector class to associte with an ability
   * 
   * @param attrib - the ability to associate the control with.
   */
  public void makeAbilityScoreSelector(String attrib)
  {
    new AbilityScoreSelector(attrib);
  }


  /*
   * Private methods
   */

  /**
   * Iterates through the score controls and updates each one.
   * 
   * @param race - the selected race
   * @param gender - the selected gender
   */
  public void updateScores(String race, String gender)
  {
    _selectedGender = gender;
    _selectedRace = race;

    for (String attrib : _attributes) {
      AbilityScoreSelector scoreControl = (AbilityScoreSelector) _scoreSelectors.get(attrib);
      scoreControl.setMinMax();
    }
  }

  /**
   * Transfer the input data from the widget shuttle to the model shuttle. For new Heroes, there are
   * no keys that need to go back to the model; they are provided in the Person constructor.
   * 
   * @param ws shuttle containing widget string data
   * @return the input shuttle with model data, with error flags set if a problem occurs.
   */
  private DataShuttle<PersonKeys> convertToModel(DataShuttle<NewHeroFields> ws)
  {
    // Guard against no shuttle or empty shuttle
    if (ws == null || ws.size() == 0) {
      return null;
    }
    // Map the widget values to the data model key values
    _ds.putField(PersonKeys.NAME, ws.getField(NewHeroFields.NAME));
    _ds.putField(PersonKeys.GENDER, ws.getField(NewHeroFields.GENDER));
    _ds.putField(PersonKeys.OCCUPATION,
        ws.getField(NewHeroFields.OCCUPATION));
    _ds.putField(PersonKeys.HAIR_COLOR,
        ws.getField(NewHeroFields.HAIR_COLOR));
    _ds.putField(PersonKeys.KLASSNAME, ws.getField(NewHeroFields.KLASSNAME));
    // If the race name contains "Half-", replace the hyphen:
    // classes cannot have hyphenated names
    String sRace = new String((String) ws.getField(NewHeroFields.RACENAME));
    sRace = Race.trueRace(sRace);
    _ds.putField(PersonKeys.RACENAME, sRace);
    return _ds;
  }

  /**
   * Overrides the base class's do-nothing localAction() to create a new model. The output observer
   * Civ is created to receive the update data from the model later.
   * 
   * @param inMap key-value pair of person fields needed to create the Person object
   */
  private void createPerson(DataShuttle<PersonKeys> inMap)
  {
    // Null Guard
    if (DataShuttle.hasErrors(inMap)) {
      return;
    }
    try {
      // Create the AttributeList
      AttributeList attribList = getAttributeList();

      // If any of these keys have null values, the exception is thrown
      // and the error flags are set
      _person = new Person((String) inMap.getField(PersonKeys.NAME),
          (String) inMap.getField(PersonKeys.GENDER),
          (String) inMap.getField(PersonKeys.OCCUPATION),
          (String) inMap.getField(PersonKeys.HAIR_COLOR),
          (String) inMap.getField(PersonKeys.RACENAME),
          (String) inMap.getField(PersonKeys.KLASSNAME),
          attribList);
      // Ask the model to display itself -- IS THIS THE WRONG PLACE FOR
      // THIS CALL?
    } catch (InstantiationException ex) {
      inMap.setErrorType(ErrorType.CREATION_EXCEPTION);
      inMap.setErrorMessage("NewHeroCiv.localAction(): Person constructor failed");
    }
  }


  /*
   * Inner Class-- MockNewHerCiv
   */

  /** Inner class used for testing only */
  public class MockNewHeroCiv
  {
    /** Default constructor */
    public MockNewHeroCiv()
    {}

    public void clearPerson()
    {
      NewHeroCiv.this._person = null;

    }

    public DataShuttle<PersonKeys> getDS()
    {
      return _ds;
    }

    public Person getPerson()
    {
      return _person;
    }

    public AttributeList getTraits()
    {
      return _person.getTraits();
    }

    public DataShuttle<NewHeroFields> getWS()
    {
      return _ws;
    }

    /**
     * Transfer the widget shuttle data to the model shuttle
     * 
     * @param ws widget shuttle
     * @return model shuttle containing wigdet shuttle data
     */
    public DataShuttle<PersonKeys> convertToModel(DataShuttle<NewHeroFields> ws)
    {
      return NewHeroCiv.this.convertToModel(ws);
    }

    // /**
    // * Call the createPerson method with known user options
    // *
    // * @param ws
    // * shuttle containing NewHeroFields
    // * @return model shuttle containing wigdet shuttle data
    // */
    // public List<PersonKeys> createPerson(
    // List<NewHeroFields> ws) {
    // // NewHeroCiv.this.createPerson(ws);
    // // Null Guard
    // if (List.hasErrors(ws)) {
    // return null;
    // }
    // try {
    // // If any of these keys have null values, the exception is
    // // thrown and the error flags are set
    // _person = new Person((String) ws.getField(NewHeroFields.NAME),
    // (String) ws.getField(NewHeroFields.GENDER),
    // (String) ws.getField(NewHeroFields.OCCUPATION),
    // (String) ws.getField(NewHeroFields.HAIR_COLOR),
    // (String) ws.getField(NewHeroFields.RACENAME),
    // (String) ws.getField(NewHeroFields.KLASSNAME));
    //
    // } catch (InstantiationException ex) {
    // // ws.setErrorType(ErrorType.CREATION_EXCEPTION);
    // ws.setErrorMessage("NewHeroCiv.localAction(): Person constructor failed");
    // }
    //
    // // If person is created with an error, then load the attributes
    // // if (ws.getErrorType() == ErrorType.OK) {
    // // _person.loadPersonData(_ds);
    // }
    // // return _ds;
    // }

  } // end of MockNewHeroCiv class

  /**
   * Groups information associated with an ability Score selector into a class.
   */
  private class AbilityScoreSelector
  {

    private String _attribute;
    private int maxValue;
    private int minValue;
    private int pointsSpent;

    /**
     * Constructor requires a string to associate the class with an attribute.
     * 
     * @param attrib - the attribute to associate the control with.
     */
    public AbilityScoreSelector(String attrib)
    {
      _attribute = attrib;
      _scoreSelectors.put(_attribute, this);
    }

    /**
     * Raise the score, called by the UI via the CIV.
     */
    public void increaseScore()
    {
      pointsSpent++;
      _totalPointsSpent++;
    }

    /**
     * Decrease the score, called by the via the CIV
     */
    public void decreaseScore()
    {
      pointsSpent--;
      _totalPointsSpent--;
    }

    /**
     * Set Max and Min values for the control. Called from the constructor and from the UI when race
     * or gender changes.
     */
    public void setMinMax()
    {
      minValue = 8;
      maxValue = 18;

      Race race = Race.createRace(Race.trueRace(_selectedRace));

      int[] mins = race.getTraitMin();
      int[] maxs = race.getTraitMax();

      for (int i = 0; i < _attributes.size(); i++) {
        if (_attributes.get(i).equals(_attribute)) {
          minValue = mins[i];
          maxValue = maxs[i];
          break;
        }
      }

      if (_selectedGender.equalsIgnoreCase(Race.FEMALE)) {
        minValue = Person.adjustTraitsForFemale(minValue, _attribute);
        maxValue = Person.adjustTraitsForFemale(maxValue, _attribute);
      }
    }

    // TODO: Replace this switch case with polymorphic function
    // switch(_selectedRace){
    // case "Dwarf":
    // if(_attribute == CHA){
    // minValue--;
    // maxValue--;
    // }
    // if(_attribute == CON){
    // minValue++;
    // maxValue++;
    // }
    // break;
    // case "Elf":
    // if(_attribute == CON){
    // minValue--;
    // maxValue--;
    // }
    // if(_attribute == DEX){
    // minValue++;
    // maxValue++;
    // }
    // break;
    // case "Gnome":
    // if(_attribute == STR){
    // minValue--;
    // maxValue--;
    // }
    // if(_attribute == CON){
    // minValue++;
    // maxValue++;
    // }
    // break;
    // case "Half-Orc":
    // if(_attribute == CHA){
    // minValue -= 2;
    // maxValue -= 2;
    // }
    // if(_attribute == CON || _attribute == STR){
    // minValue++;
    // maxValue++;
    // }
    // break;
    // case "Hobbit":
    // if(_attribute == STR){
    // minValue--;
    // maxValue--;
    // }
    // if(_attribute == DEX){
    // minValue++;
    // maxValue++;
    // }
    // break;
    // }
    //
    // if(_selectedGender.equalsIgnoreCase("female")){
    // if(_attribute == STR){
    // minValue--;
    // maxValue--;
    // }
    // if(_attribute == CHA){
    // minValue++;
    // maxValue = Math.max(19, maxValue + 1);
    // }
  } // End of AbilityScoreSelector class
} // end of NewHeroCiv class
