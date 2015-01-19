/**
 * Basemylib.mylib.civ.java Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.civ;

import java.awt.Container;
import java.util.ArrayList;

import mylib.Constants;
import mylib.civ.DataShuttle.ErrorType;

/**
 * An abstract base class that contains common functions for all Civ classes, and supports the MVP
 * (Model-View-Presenter) Architecture. This class takes a DataShuttle of ModelKeys M, and a
 * DataShuttle of WidgetFields W.
 * 
 * @author Alan Cline
 * @version May 28, 2011 // original <br>
 *          Jul 29 2011 // update after generics working <br>
 */
public abstract class BaseCiv<M extends Enum<M>, W extends Enum<W>> // implements Observer
{
  // Used for converting internal format to delimited strings
  static public final String DELIM = "|";

  /** Every Civ is associated with a shuttle for model data. */
  protected DataShuttle<M> _ds = null;
  /** Every Civ is associated with a shuttle for display data. */
  protected DataShuttle<W> _ws = null;
  /** Every Civ is associated with a specific model */
  // protected ObservableModel<M> _model = null;

  /**
   * Every Civ is associated with its specific widget, whether JDialog, JPanel, JComponent, or
   * Window
   */
  protected Container _widget = null;

  /**
   * Converts shuttle data from model data to output widget data.
   * 
   * @param ds contains model key data of any data type
   * @return shuttle containing String data data for the model keys; shuttle's error flags are set
   *         on error
   */
  abstract protected DataShuttle<M> convertToDisplay(DataShuttle<M> ds);

  /**
   * Converts shuttle data from input widget data to model data
   * 
   * @param ws contains widget key data
   * @return shuttle containing model key data; shuttle's error flags are set on error
   */
  abstract protected DataShuttle<M> convertToModel(DataShuttle<W> ws);

  /**
   * Creates and loads a shuttle with the widget's default field values. The default data can come
   * from fields in the Civ, or from the model, or from another class that has default data to
   * display.
   * 
   * @return DataShuttle of the given widget enum type
   */
  abstract public DataShuttle<W> getDefaults();

  /**
   * Require the civs to ask the keys to check their own validity
   * 
   * @parm shuttle containing the keys to check for validity
   * @return true if shuttle data is valid, else sets shuttle's error flags
   */
  abstract protected DataShuttle<W> isValid(DataShuttle<W> shuttle);


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** General default constructor */
  public BaseCiv()
  {}


  /*
   * PUBLIC METHODS
   */

  /**
   * Formats a String representing height in feet to one representing feet and inches
   * 
   * @param strHeight to convert into feet and inches
   * @return the converted input; else null if empty
   */
  static public String formatHeight(String strHeight)
  {
    // Guard: verify inut parm has data in it
    if (isEmptyString(strHeight)) {
      return null;
    }

    // Get integer and ensure that it is a positive number
    int total = Integer.parseInt(strHeight);
    if (total < 0) {
      return null;
    }
    int feet = total / Constants.INCHES_PER_FOOT;
    int inches = total % Constants.INCHES_PER_FOOT;
    String fullWt = String.format("%s' %s\"", feet, inches);
    return fullWt;
  }


  /**
   * Formats a String representing inches to one representing feet and inches
   * 
   * @param strInches to convert into feet and inches
   * @return the converted input
   */
  static public String formatInches(String strInches)
  {
    // Guard: input parm exists
    if (isEmptyString(strInches) == true) {
      return null;
    }

    // Get integer and ensure that it is a positive number
    int total = Integer.parseInt(strInches);
    if (total < 0) {
      return null;
    }
    // Format line into something presentable
    int feet = total / Constants.INCHES_PER_FOOT;
    int inches = total % Constants.INCHES_PER_FOOT;
    String fullSize = String.format("%s' %s\" ", feet, inches);
    return fullSize;
  }


  /**
   * Formats a String representing ounces to one representing pounds and ounces
   * 
   * @param strOunces to convert into pounds and ounces
   * @return the converted input; else null if empty
   */
  static public String formatOunces(String strOunces)
  {
    // Guard: verify inut parm has data in it
    if (isEmptyString(strOunces)) {
      return null;
    }

    // Get integer and ensure that it is a positive number
    double total = Double.parseDouble(strOunces);
    if (total < 0) {
      return null;
    }
    int pounds = (int) total / Constants.OUNCES_PER_POUND;
    int ounces = (int) total % Constants.OUNCES_PER_POUND;
    String fullWt = String.format("%s lb. %s oz.", pounds, ounces);
    return fullWt;
  }


  /**
   * Format a String representing seconds into years and fractional years
   * 
   * @param strSecs to convert into years
   * @return the converted input
   */
  static public String formatSeconds(String strSecs)
  {
    // Guard against empty string
    if (isEmptyString(strSecs)) {
      return null;
    }

    // Format line into something presentable
    float total = Float.parseFloat(strSecs) / Constants.SECS_PER_YEAR;
    // Only positive numbers are returned
    if (total < 0) {
      return null;
    }
    String age = String.format("%2.1f yrs.", total);
    return age;
  }


  /**
   * Checks that a string exists (not null) and contains more than white space
   * 
   * @param target to verify for existence
   * @return true if target is null or contains only white space
   */
  static public boolean isEmptyString(String target)
  {
    // String is assumed not empty
    boolean retval = false;

    // Guard against null target
    if (target == null) {
      return true;
    }
    // Remove any white space
    String s = target.trim();
    // Check for existence, including no characters in string
    if (s.length() == 0) {
      retval = true;
    }
    return retval;
  }


  // /** Ask the keys to check their own validity
  // * @parm shuttle containing the keys to check for validity
  // * @return true if shuttle datais valid, else set shuttle's error flags
  // */
  // private DataShuttle<W> isValid(DataShuttle<W> shuttle)
  // {
  // // Load a value into the shuttle for each slot requested; ignore others
  // for (W key : shuttle.getKeys())
  // {
  // Object obj = shuttle.getField(key);
  // if (((Object) key).isValid(obj) == false) {
  // // Every key requested must have data to go with it, else error
  // shuttle.setErrorType(ErrorType.FIELD_INVALID);
  // shuttle.setErrorMessage(key.name());
  // shuttle.setErrorSource(key);
  // break;
  // }
  // return shuttle;
  // }
  // }


  /**
   * Converts an object to a string, else returns null
   * 
   * @param obj value to convert
   * @return object converted
   */
  static public String toString(Object obj)
  {
    final int BRACKET_SIZE = 1;
    if (obj == null) {
      return null;
    }
    String str = obj.toString().trim();
    // If the object is an arraylist, remove the brackets put on by the obj.toString() method
    if ((obj instanceof ArrayList) && (str.charAt(0) == '[')) {
      str = str.substring(BRACKET_SIZE, str.length() - BRACKET_SIZE);
    }
    return (BaseCiv.isEmptyString(str) == false) ? str : null;
  }

  /**
   * Default method to be overwritten if application needs to do anything. Called by submit() method
   * after validation and conversion of widget to data shutte, but before the model unpacks the data
   * shuttle. This method can be used to create the model if it doesn't already exist.
   * 
   * @param ws widget shuttle coming in from the widget
   */
  protected void localAction(DataShuttle<W> ws)
  {}


  /**
   * Validates the data and reformats it for the PDC object. This method validates the data in the
   * widget shuttle, converts it to model shuttle data, and calls the model's
   * <code>unloadShuttle()</code> method to store the data. Occasionally, the application-level Civ
   * must perform some action on the data shuttle before it is sent to the model, so
   * <code>localAction()</code> method is called. In most cases, <code>localAction()</code> defaults
   * to the do-noting method in this class.
   * 
   * @param ws contains the field keys from the widget
   * @return ws; if errors, the shuttle's error flags will be set
   */
  public DataShuttle<W> submit(DataShuttle<W> ws)
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
      // Validate the data before proceeding; bad data is returned with error flags.
      if (isValid(ws).getErrorType() != ErrorType.OK) {
        return ws;
      }
      // Convert the widget shuttle to a model shuttle
      _ds = convertToModel(ws);

      // Allow the Civ to perform other actions on the data shuttle before unloading it
      localAction(ws);

      // Local action must create a model if it doesn't exist
      // if (_model == null) {
      // ws.setErrorType(ErrorType.CREATION_EXCEPTION);
      // ws.setErrorMessage("Can't find model to update");
      // return ws;
      // }

      // Set the values from the shuttle into the model using the given keys
      // If error, the shuttle will contain the error flags
      // _ds = _model.unloadShuttle(_ds);

      // In case the shuttle is of the wrong type
    } catch (ClassCastException ex) {
      ws.setErrorType(ErrorType.CLASS_CAST_EXCEPTION);
      ws.setErrorMessage(DataShuttle.CAST_EXCEPTION_MSG);
      return ws;
    }
    return ws;
  }


  // /** Converts the model's data shuttle to a widget shuttle whenever it is updated, and sends it
  // to
  // * the widget to refresh the GUI.
  // *
  // * @param model the object being watched for changes
  // * @param objModel arbitary object (should be a DataShuttle<M>) that the model passes to this
  // observer
  // */
  // @Override
  // public void update(Observable model, Object objModel)
  // {
  // // Guard: if shuttle is null, return immediately
  // if (objModel == null) {
  // return;
  // }
  // MsgCtrl.msgln(this, "Observable model " + objModel.toString());
  // MsgCtrl.msgln("\t updating Civ Observer" + this.toString() );
  //
  // // Guard: if shuttle contains no keys, return empty shuttle with error flags
  // @SuppressWarnings("unchecked")
  // DataShuttle<M> ds = (DataShuttle<M>) objModel;
  // if (ds.size() == 0) {
  // return;
  // }
  // // Load a value into the shuttle for each slot requested; ignore others
  // // Return immediately if the requested key is not found
  // DataShuttle<W> ws = null;
  // try {
  // // Verify correct widget input data
  // ws = packDisplay(ds);
  // // TODO Implement the refresh() method to display data from the widget shuttle
  // // _widget.refresh(ws);
  // // In case the shuttle is of the wrong type
  // } catch (ClassCastException ex) {
  // ws.setErrorType(ErrorType.CLASS_CAST_EXCEPTION);
  // ws.setErrorMessage(DataShuttle.CAST_EXCEPTION_MSG);
  // }
  // }
  //

} // end of BaseCiv class
