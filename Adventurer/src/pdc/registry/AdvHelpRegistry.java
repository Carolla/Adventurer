/**
 * AdvHelpRegistry.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package pdc.registry;

import civ.HelpTextObject;

import chronos.Chronos;

import mylib.pdc.Registry;

/**
 *    All Help text for Adventurer contained here.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Nov 14, 2013   // original <DD>
 * <DT> Build 1.1       Nov 23, 2013   // abc: added generic help and hooked it in <DD>
 * </DL>
 */
public class AdvHelpRegistry extends Registry
{
    /** Internal reference to this registry for singleton purposes */
//    static private AdvHelpRegistry _thisReg = null;
    
    /** Generic help ID for the mainframe help window */
    static public final String GENERIC_ID =  "INSTRUCTIONS";
    /** Generic help to initialize the AdvHelpRegistry */
    static private final String GENERIC_TEXT =  
                    "Goal: Explore the Arena in the Town to achieve your quest, which is given to you by "+
                    "the Guild that you join when you have enough experience points. "+
                    "Your Hero will gain experience by finding loot, fighting monsters, and solving puzzles. "+
                    "You will become more powerful as you grow in experience.\n"+ 
                    "\n"+
                    "Create your Hero, select an Adventure, and enter the Town. "+
                    "Enter any of the Town's buildings to prepare yourself before entering the Arena.\n"+
                    "--Buy and sell supplies at the General Store;\n"+
                    "--Talk to patrons in the Inn for information to prepare yourself;\n"+
                    "--Rent a room and buy food in the Inn to refresh yourself;\n"+
                    "--Earn enough experience to join a Guild for better weapons or magic spells;\n"+
                    "--Deposit money and make wills at the Bank to save your wealth.\n" +
                    "\n\n"+
                    "Images will show on right pane, descriptions and actions show on left pane.\n" +
                    "Click on images to get more information or to proceed into town.\n" +                
                    "Hit the F1 key if you need help at any time.\n\n";
    
    /* Analogy for the prime character traits */
    private String TRAIT_ANALOGY = 
                    "Strength is being able to crush a tomato.\n"+     
                    "Dexterity is being able to dodge a tomato.\n" +
                    "Constitution is being able to eat a bad tomato.\n" +
                    "Intelligence is knowing a tomato is a fruit.\n" +
                    "Wisdom is knowing not to put a tomato in a fruit salad.\n" +
                    "Charisma is being able to sell a tomato-based fruit salad.\n";

    // _____________________________________________________________________
    //
    //      CONSTRUCTOR(S) AND RELATED METHODS
    // _____________________________________________________________________

    /**
     * Return the Registry if there is one; if not, create it. 
     * 
     * @return the reference to the HelpRegistry
     */
//    protected AdvHelpRegistry getInstance()
    public AdvHelpRegistry getInstance()
    {
        return (AdvHelpRegistry) new AdvHelpRegistry();
    }

    
    /**
     * Private ctor because this singleton is called from getInstance().
     * Registry filename is used for database filepath
     */
    protected AdvHelpRegistry() 
    {
        super(Chronos.AdventureHelpRegPath); 
    }
    

    /** Define the initial data that goes into the HelpRegistry when it is created */
    @Override
    protected void initialize()
    {
        // Create the generic help object and load the registry 
        HelpTextObject myHelp = new HelpTextObject(GENERIC_ID, GENERIC_TEXT);
        super.add(myHelp);  
    }

    
    // _____________________________________________________________________
    //
    //      PUBLIC METHODS
    // _____________________________________________________________________


//    /** Close down the Registry without deleting the db files
//     * @see mylib.pdc.Registry#closeRegistry()
//     */
//    @Override
//    public void closeRegistry()
//    {
//        close();
////        _thisReg = null;
//    } 
//
//    /** Delete the Registry and its file
//     * @see mylib.pdc.Registry#closeRegistry()
//     */
//    @Override
//    public void deleteRegistry()
//    {
//        delete();  
////        _thisReg = null;
//    } 
//
    
    // _____________________________________________________________________
    //
    //      PRIVATE METHODS
    // _____________________________________________________________________


    // _____________________________________________________________________
    //
    //      MOCK AdvHelpRegistry
    // _____________________________________________________________________

public class MockAdvHelpRegistry
{
    public MockAdvHelpRegistry() { }
    
    
//    public AdvHelpRegistry getInternalReference()
//    {
//        return _thisReg;
//    }
//    
//    
//    public DbReadWriter getDBWriter()
//    {
//        return _regRW;
//    }
    
    
}
    
    
    
    
}       // end of HelpRegistry class
