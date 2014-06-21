/**
 * Shuttle.java
 * Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.civ;

import java.util.EnumMap;
import java.util.Set;

import mylib.Constants;
import mylib.MsgCtrl;


/**
 * Collects enum keys in a named key-value pair to pass between HIC, CIV, and PDC object.
 * The HIC works with the <code>DataShuttle</code> at the field level (Strings), 
 * the PDC object works with it at the native attribute level,  
 * and the CIV translates between the two.  
 * A <code>DataShuttle</code> negates the need (at least partially) to bloviate the PDC object with 
 * accessors, and it provides a consistent way (protocol) to communicate between the HIC and
 * PDC via the CIV socket. 
 * <P>
 * In general, enum <code>Fields</code> are keys for the widget, and are always String data type;
 * enum <code>Keys</code> are keys for the model, and may be of any data type.
 * <code>Fields</code> have <code>getDefault()</code> and <code>isValid()</code> methods
 * that the model <code>Keys</code>  lack.
 * <P>
 *  This is a particular implementation of Fowler's <i>Introduce Parameter Object</i> refactoring 
 *  pattern.
 * See "Refactoring: Improving the Design of Existing Code" by Martin Fowler, 
 * Addison-Wesley, 2000.
 * <P>
 * <P>
 * The PDC object should extend the <code>ObservableModel</code> class, and the CIV object
 * should extend the <code>BaseCiv</code> class, which implements the <code>Observer</code> 
 * interface. Both of these work to keep the observer in sync with recent changes to the 
 * GUI widget or the PDC object, and support validation to keep the data correct.
 *
 * @see @link java.util.Observable  
 * @see @link mylib.pdc.ObservableModel   
 * @see @link java.util.Observer 
 * @see @link mylib.mylib.mylib.civ.BaseCiv 
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Jun 19, 2011   // original <DD>
 * <DT> Build 2.0       Jul 3, 2011   // revised to support generics <DD>
 * </DL>
 */
public class DataShuttle<E extends Enum<E>>
{
    /** List of error types to trigger error message in the various Hero displays. */
    public enum ErrorType
    {
        OK,  CLASS_CAST_EXCEPTION, CREATION_EXCEPTION,  DATA_SHUTTLE,  
        EMPTY_SHUTTLE,  FIELD_INVALID,  INVALID_SIZE, MISSING_FIELD, MISSING_KEY, 
        NULL_FIELD, NULL_KEY, UNKNOWN;        
    };           

    /** Exception message if a null key is attempted */
    static public final String NULL_KEY_MSG = "Attempting to put null key into DataShuttle";
    /** Exception message if a shuttle and keyset is wrong size */
    static public final String MISSING_KEY_MSG = "Expected shuttle key not found";
    /** Exception message if a shuttle and keyset is wrong size */
    static public final String EMPTY_SHUTTLE_MSG = "There are no keys in this shuttle.";
    /** Exception message if a cast is attempted on an instance of which it is not a subclass */
    static public final String CAST_EXCEPTION_MSG = 
                    "Attempted to save using an invalid class cast";

      /** Define the source of invalid data */
      private E _errorSource = null;
      /** Define the source of invalid data */
      private ErrorType _errorType = null;
      /** Define a general purpose error message */
      private String _errorMsg = null;
      
      /** Implementation class for DataShuttle<key, value> pair, where the key is an enum,
       * and the value is a String */ 
      private EnumMap<E, Object> _eMap = null;
      
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

      /** Creates a data shuttle that contains the enum map designated.
       * Caller must pass in the class name of the enum, such has
       * <code>DataShuttle/<E/> ds = DataShuttle/<E/>(Items.class). 
       * The enum is then used to create the internal enumMap that <code>DataShuttle</code> 
       * uses for implementation. 
       *
       * @param enumClass     .class name for the enum used to key the shuttle
       * @return the shuttle created
       */
      public DataShuttle(Class<E> enumClass) 
      {
          try {
              // Subclass Commands must have empty constructors (no formal input arguments)
              _eMap = new EnumMap<E, Object>(enumClass);
              clearErrors();
          } catch (Exception e) {
              MsgCtrl.errMsgln(this, "Cannot create request class name " + enumClass); 
              MsgCtrl.errMsgln(e.getMessage());
          }
      }

          
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

      /** Standard routine to check for shuttle errors, and print any that are found
     *  @return false if shuttle has no errors
     */
    @SuppressWarnings("rawtypes")
    static public boolean hasErrors(DataShuttle shuttle) 
    {
        boolean retval = true;
        // Guard: against of null shuttle
        if (shuttle == null) {
            retval = true;
        }
        // Guard: against empty shuttle
        else if (shuttle.size() == 0) {
            shuttle.setErrorType(ErrorType.EMPTY_SHUTTLE);
            retval = true;
        }
        // Check for any error states in the shuttle
        else {
            ErrorType err = shuttle.getErrorType();
            if (err == DataShuttle.ErrorType.OK) {
                if ((shuttle.getErrorSource() == null)  && (shuttle.getErrorMessage() == null)) {
                    retval = false;
                }
            }
        }
        return retval;
    }


    /** Set a key into the shuttle to receive a value mapping, else the
       * shuttle will be empty and unassignable. Each key is assigned to null
       * until application sets a value.
       * 
       * @param key the enum element to set in the shuttle
       */
      public void assignKey(E key)
      {
          _eMap.put(key, null);
      }
     
      
      /** Clears all values and error flags from the keyset, but retains the keys */
      public void clear()
      {
          _eMap.clear();
          clearErrors();
      }

      
      /** Reset all errors to null or empty  */
    public void clearErrors()
    {
        _errorType = ErrorType.OK;
        _errorSource = null;
        _errorMsg = null;
    }

    
    /** Display the contents of this shuttle
     * @parma showNull if true, will show keys with null values; if false, will skip the null-value keys
     */
    public void display(boolean showNull)
    {
        for (E key : _eMap.keySet()) {
            if (_eMap.get(key) == null) {
                continue;
            }
            // Show everything is requested
            if (showNull == true) {
                    MsgCtrl.msgln("\t" + key + " = " + _eMap.get(key));
            }
            else {
                // Show only non null keys
                if (_eMap.get(key) != null) {
                    MsgCtrl.msgln("\t" + key + " = " + _eMap.get(key));
                }
            }
        }
    }

    
    /** Get the exception message if the Person aborted and threw an exception
     * @return String containing some info about the exception  
     */
    public String getErrorMessage()
    {
        return _errorMsg;
    }

    
    /** Get the source of invalid data being input defined by the enumMap specific field keys.
     * Both ErrorType or type E may be returned, so called may need to downcase return result.
     * @return the enum field containing the invalid data
     */
    public E getErrorSource()
    {
        return _errorSource;
    }

    
    /** Get the type of error
     * @return the type of error that has occurred  
     */
    public ErrorType getErrorType()
    {
        return _errorType;
    }

    
    /** Get the value for the given key
     * @param key   for retrieveing the value
     * @return the value field from the shuttle, else returns null and sets the shuttle's error flag if the
     *      key is not in the map
     */
    public Object getField(E key)
    {
        Object obj = _eMap.get(key);
        if (obj == null) {
            // Set error flags
            setErrorType(ErrorType.MISSING_KEY);
            setErrorSource((E) key);
            setErrorMessage(MISSING_KEY_MSG);
        }
        return obj;
    }

    
    /** Return the enum keys within this shuttle 
     * @return the list of the keys for this shuttle. 
     */
    public Set<E> getKeys()
    {
        return _eMap.keySet();
    }

    
    /** Set the given value for the given key. Null keys are not allowed. Error flags are set if an
     * error occurs.
     * 
     * @param key   for retrieving the value
     * @param value   to be stored with the key; can be null
     * @return the number of keys currently in the shuttle, 
     *                  or Chronos.ERROR if a null key is attempted.
     */
    @SuppressWarnings("unchecked")
    public int putField(E key, Object value)
    {
        // Guard against illegal null keys
        try {
            _eMap.put(key, value);
        } catch (NullPointerException ex) {
            // Set error flags
            setErrorType(ErrorType.NULL_KEY);
            setErrorSource((E) ErrorType.DATA_SHUTTLE);
            setErrorMessage(NULL_KEY_MSG);
            return Constants.ERROR;
        }
        return size();
    }

    
    
    /** Removes the key and value for the specified key. The shuttle's size will be reduced accordingly.
     * This clears the shuttle of the mapping, whereas setting the value to null does not.
     * @param key   the enum (and value) to be removed from the shuttle
     */
      public void removeKey(E key)
      {
          _eMap.remove(key);
      }


    /** Set the exception message. If none is defined, <code>DataShuttle</code> provides a default
     * @param errMsg to describe the message to the user
     */
    public void setErrorMessage(String errMsg)
    {
        _errorMsg = errMsg;
    }

    
    /** Set the source of invalid data being input 
     * @param sourceField that is not valid
     */
    public void setErrorSource(E sourceField)
    {
        _errorSource = sourceField;
    }

    
    /** Get the type of error
     * @param  error   the type of error that has occurred  
     */
    public void setErrorType(ErrorType error)
    {
        _errorType = error;
    }

    
    /** Get the number of seats on the shuttle; i.e., how many keys are expected
     * @return the size of the shuttle  
     */
    public int size()
    {
        return _eMap.size();
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

//    /** Sets the error flags to state of original empty constructor */
//    private void reset() 
//    {
//        _errorType = ErrorType.EMPTY_SHUTTLE;
//        _errorSource = (E) ErrorType.DATA_SHUTTLE;
//        _errorMsg = EMPTY_SHUTTLE_MSG;
//    }

    
    /** Once a DataShuttle is created with an enum mapping, the keys cannot be changed */
//  /** Removes all mappings from the shuttle */
//  public void clear()
//  {
//      _eMap.clear();
//  }
      
    /** Once a DataShuttle is created with an enum mapping, the keys cannot be changed */
//  /** Remove an entry from the shuttle, both key and value
//   * @param key of item to remove
//   * @return the number of assigned keys currently in the shuttle
//   */
//  public int remove(E key)
//  {
//      _eMap.remove(key);
//      return size();
//  }



}   // end of Shuttle class
