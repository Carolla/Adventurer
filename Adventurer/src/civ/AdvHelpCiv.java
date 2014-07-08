/**
 * AdvHelpCiv.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package civ;

import java.util.ArrayList;

import mylib.hic.HelpDialog;
import pdc.registry.AdvHelpRegistry;

/**
 *   Displays the HelpDialog window with text retrieved from the HelpRegistry
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Nov 23, 2013   // original <DD>
 * </DL>
 */
public class AdvHelpCiv
{
    /** Reference to this singleton */
    static private AdvHelpCiv _helpCiv = null;
    
    /** Reference to the PDC HepRegistry for data retrieval */
    private AdvHelpRegistry _helpReg = null;
    /** Reference to the HIC HelpDialog for display */
    private HelpDialog _helpDialog = null;

    /** Display this message if the helpID cannot be found */
    private final String NON_MSG = "Called for Help...\n\t...but help text not found!";
    /** Display this title for the not found help message */
    private final String MSG_NOTFOUND = "Help Text Not Found";

    // _____________________________________________________________________
    //
    //      CONSTRUCTOR(S) AND RELATED METHODS
    // _____________________________________________________________________

    /** Retrieve the HelpRegistry. */
    private AdvHelpCiv() 
    {
//        _helpReg = (AdvHelpRegistry) AdvRegistryFactory.getRegistry(RegKey.HELP);
//        _helpDialog = HelpDialog.getInstance(Mainframe.getInstance());
    }

    /**
     * Return the Registry if there is one; if not, create it. 
     * 
     * @return the reference to the HelpRegistry
     */
    static public AdvHelpCiv getInstance()
    {
        if  (_helpCiv == null)  {
            _helpCiv = new AdvHelpCiv();
        }
        return _helpCiv;
    }

    // _____________________________________________________________________
    //
    //      PUBLIC METHODS
    // _____________________________________________________________________

    /** Retrievethe help text from the registry and displayit
     * @param helpID        key for the appropriate Text
     */
    public void showHelp(String helpID)
    {
        HelpTextObject helpObj = (HelpTextObject) _helpReg.getUnique(helpID);
        if (helpObj == null) {
            _helpDialog.showHelp(NON_MSG,  MSG_NOTFOUND);
        }
        else {
            String helptext = helpObj.extractText();
            _helpDialog.showHelp(helptext, helpID);
        }
    }
   
    
    // _____________________________________________________________________
    //
    //      PRIVATE METHODS
    // _____________________________________________________________________

    
}           // end of HelpCiv class
