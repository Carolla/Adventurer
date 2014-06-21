/**
 * HelpTextObject.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package civ;

import mylib.dmc.IRegistryElement;

/**
 *    <Class Description>
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Nov 14, 2013   // original <DD>
 * </DL>
 */
public class HelpTextObject implements IRegistryElement
{
    /** Unique identifier for this object */
    private String _helpID = null;
    /** Actual text to provide the help */
    private String _helpText = null;
    
    // _____________________________________________________________________
    //
    //      CONSTRUCTOR(S) AND RELATED METHODS
    // _____________________________________________________________________

    /** Public constructor for all Help objects */
    public HelpTextObject() { }
    
    /** Public constructor for all Help objects
     * 
     * @param key       primary reference for saving and retrieving help from registry
     * @param txt       the text value for this help object
     */
    public HelpTextObject(String key, String txt) 
    { 
        _helpID = key;
        _helpText = txt;
    }
    

    // _____________________________________________________________________
    //
    //      PUBLIC METHODS
    // _____________________________________________________________________

    /** Pull out the text part of this object for display */
    public String extractText()
    {
        return _helpText;
    }


    /* 
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    public String getKey()
    {
        return _helpID;
    }
    
    
    // _____________________________________________________________________
    //
    //      PRIVATE METHODS
    // _____________________________________________________________________


}           // end of HelpTextObject class
