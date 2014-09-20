/**
 * ObservableModel.java
 * Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.pdc;

import java.util.Observable;

import mylib.civ.DataShuttle;
import mylib.civ.DataShuttle.ErrorType;


/**
 *  Extends the <code>Observable</code> abstract class to include DataShuttle behavior. 
 *  The derived class plays the role of model in the model-view pattern. Observable base class 
 *  behavior notifies of data changes so that the corresponding Civ object observer(s), 
 *  which implements the </code>Observer<code> interface, can update the data with a data shuttle. 
 *  @see java.util.Observable
 *  @see java.util.Observer
 *  @see mylib.civ.DataShuttle
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jul 3, 2011   // original <DD>
 * </DL>
 */
public abstract class ObservableModel<E extends Enum<E>> extends Observable
{
    /** Wrapper to load values into the concrete observable, and handle the exception */
    abstract protected DataShuttle<E> load(DataShuttle<E> ds) throws ClassCastException;

    /** Wrapper to unload values into the concrete observable, and handle the exception */
    abstract protected void unload(DataShuttle<E> ds) throws ClassCastException;

    
    /** Place the object's attributes into the shuttle.
     * The shuttle must contain the keys for the data to be loaded. A superfluous key requested
     * will cause an error.
     *  
     * @param shuttle containing enum key-value pairs
     * @return updated shuttle, or null if there are no keys.
     *      Data shuttle error flags will be set to reflect success or failure
     */
    public DataShuttle<E> loadShuttle(DataShuttle<E> shuttle)
    {
        // Guard: if shuttle is null, return immediately
        if (shuttle == null) {
            return null;
        }
//        // Guard: if shuttle contains no keys, return empty shuttle with error flags
//        if (shuttle.size() == 0) {
//            shuttle.setErrorType(ErrorType.EMPTY_SHUTTLE);
//            shuttle.setErrorMessage(DataShuttle.EMPTY_SHUTTLE_MSG);
//            return shuttle;
//        }
        // Load a value into the shuttle for each slot requested; ignore others
        // Return immediately if the requested key is not found
        try {

            // Call the application-specific subclass for unloading and saving the shuttle values;
            // shuttle is updated on error
            shuttle = load(shuttle);
            
        // In case the shuttle is of the wrong type
        } catch (ClassCastException ex) {
            shuttle.setErrorType(ErrorType.CLASS_CAST_EXCEPTION);
            shuttle.setErrorMessage(DataShuttle.CAST_EXCEPTION_MSG);
            return shuttle;
        }
        return shuttle;
    }
   
    
    /** Checks for general shuttle problems, then calls the subclass unload() to save the shuttle
     * data, determined by the keys of the shuttle, into the object. 
     * 
     * @param shuttle   map of enum keys and string values
     * @return same input shuttle, but updated with error messages if there are problems.
     */
    public DataShuttle<E> unloadShuttle(DataShuttle<E> shuttle)
    {
        // Guard: if shuttle is null, or contains no keys, return immediately
        if (shuttle == null) {
            return null;
        }
        if (shuttle.size() == 0) {
            shuttle.setErrorType(ErrorType.EMPTY_SHUTTLE);
            shuttle.setErrorMessage(DataShuttle.EMPTY_SHUTTLE_MSG);
            return shuttle;
        }
        
        // Load a value into the shuttle for each slot requested; ignore others
        try {
            
            // Call the application-specific subclass for loading the model values;
            unload(shuttle);
            
            } catch (ClassCastException ex) {
                shuttle.setErrorType(ErrorType.CLASS_CAST_EXCEPTION);
                shuttle.setErrorMessage(DataShuttle.CAST_EXCEPTION_MSG);
                return shuttle;
            }
        // Set the change flag so that notifyObservers() works 
        setChanged();
        notifyObservers(shuttle);
        return shuttle;
    }

    
}   // end of ObservableModel abstract class
