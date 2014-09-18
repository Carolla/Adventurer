/**
 * Adventure.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.pdc;

import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.TownRegistry;

import mylib.dmc.RegistryElement;

/**
 *  Singleton containing a refernce to a Town and an Arena, two required elements of all Adventures.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		June 8, 2013   // original <DD>
 * </DL>
 */
public class Adventure extends RegistryElement
{
    /** Name of this Adventure */
    private String _name = null;
    /** Overview of the adventure */
    private String _overview = null;

    /** Name of the Town for this Adventure */
    private String _townName = null;
    /** Name of the Arena for this Adventure */
    private String _arenaName = null;

    /** If an Adventure is open, then all elements are expanded in the tree;
     * if closed, then only the Adventure's name is shown and the tree is not expandable */
    private boolean _open = true;
    
    /** Error message for any null input parm to constructor */
    private String nullMsg = "%s cannot be null";
    /** Error message if given town or arena does not exist */
    private String cannotFindMsg = "Cannot find %s in registry";

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** Create the default adventurer
     * @param advName       this adventure
     * @param townName     the primary town in the adventure (homebase)
     * @param arenaName   arena in the adventure
     * @param overview        description and background story of town, arena, and situation
     */
    public Adventure(String advName, String townName, String arenaName, String overview) 
    {
        _name = advName;
        _townName = townName;
        _arenaName = arenaName;
        _overview = overview;
    }
    

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** Close an Adventure */
    public void close()
    {
        _open = false;
    }

    /** Dump the Adventure internals */
    public void dump()
    {
        System.out.println("\tAdventure " + getKey());
        System.out.println("\thas Town " +  _townName);
        System.out.println("\thas Arena " + _arenaName);
        System.out.println("\tOverview: \t" + getOverview());
    }

    /** Check if two Adventures are equal or not. The name is sufficient to confirm uniqueness
     * @param otherThing    the Skill to be considered
     * @return true if the Skill has the same name and description as this object
     */
    @Override
   public boolean equals(Object otherThing)
   {
       // Guard against null input
       if (otherThing == null) {
           return false;
       }
       Adventure adv = (Adventure) otherThing;
       boolean bName = _name.equals(adv._name);
       return bName;
    }

    
    /** 
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    @Override
    public String getKey()
    {
        return _name;
    }

    /** 
     * @return the name of the adventure
     */
    public String getName()
    {
        return _name;
    }


    /** 
     * @return the name of the adventure's town
     */
    public String getTownName()
    {
        return _townName;
    }

    /** 
     * @return the name of the adventure's arena
     */
    public String getArenaName()
    {
        return _arenaName;
    }

    
    /** 
     * @return the introductory description of the arena
     */
    public String getOverview()
    {
        return _overview;
    }

    
    /** If the Adventure has an Arena, get it from the arena folder
     * @return the Arena object or null
     */
    public Arena getArena()
    {
        return (_arenaName == null) ? null : Arena.getInstance(_arenaName);
    }

    
    /** If the Adventure has a Town, get it from the TownRegistry
     * @return the Town object or null
     */
    public Town getTown()
    {
        TownRegistry townReg = (TownRegistry) RegistryFactory.getInstance().getRegistry(RegKey.TOWN);
        return (_townName == null) ? null : (Town) townReg.getUnique(_townName);
    }
  
    
    /** Return true if the Adventure is open */
    public boolean isOpen()
    {
        return _open;
    }


    /** Open an Adventure */
    public void open()
    {
        _open = true;
    }


    /**  @return the display name of the Adventure */
    public String toString()
    {
        return _name;
    }
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    
    
}       // end of Adventure class
