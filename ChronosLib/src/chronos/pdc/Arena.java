/**
 * Arena.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.pdc;

import chronos.Chronos;
import mylib.dmc.DbReadWriter;


/**
 *  Singleton containing the actual "dungeon", whether indoors or outdoors. 
 *  Unlike other Adventure components, an Arena is not a registry but is a single db file. 
 *  An Arena is a collection of Rooms that the Hero explores, and each Room may contain Items
 *  monsters, NPCs, or a combination thereof.  
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		June 8, 2013   // original <DD>
 * </DL>
 */
public class Arena 
{
    /** Reference to this singleton */
    static private Arena _thisArena = null;
    /** Name of this Arena */
    private String _name = null;
    /** Description of the Arena when Hero first sees it */
    private String _intro = null;
    /** All the rooms of the dungeon, and everything each room contains */
 //   ArrayList<Room> _rooms = null;
    
    /** Default description if the Arena is never assigned one */
    private final String DEF_INTRO = "This is the dungeon %s";

    /** Error message if the name is null */
    private final String nullMsg = "Arena cannot have a null name";

    // Create the db interface to store the Arena, but do not store the read writer
    // It is massive large ~27K, because it pulls in db40.
    private transient DbReadWriter _dbi = null;
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** Create this singleton and open the db for it
     * @param name of the arena being created, and the name of the db file
     * @throws NullPointerException if the Arena name is null
     */
    private Arena(String name)
    {
        // Guard against non-existant file
        if ((name == null) || (name.length() == 0)) {
            throw new NullPointerException(nullMsg);
        }
        _name = name;
        _intro = String.format(DEF_INTRO, name);
        // Create the db file to store the Arena
        _dbi = new DbReadWriter(Chronos.ArenaPath + name + Chronos.ARENA_EXT);
    }

    
    /**
     * Get the singleton if it exists. Call createArena() is the caller should create a new Arena file
     * This method intentionally does not create a new arena if it does not exist
     * @param   name of the arena to open
     * @return the initialized Arena
     * */
    static public Arena getInstance(String name)
    {
        // Check if arena already exists
        if (_thisArena == null)  {
            _thisArena = new Arena(name);
        }
        return _thisArena;
    }


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Close down this Arena, file and object
     * @see mylib.pdc.Registry#closeRegistry(boolean)
     */
    public void close()
    {
        _dbi.close();
        _thisArena = null;
    }

    
//    /** Delete the arena file
//    * @see mylib.pdc.Registry#deleteRegistry(boolean)
//    */
//    public void delete()
//    {
//        _dbi.dbDelete();
//        _thisArena = null;
//    }

    
    /** 
     * @return the arena introduction text
     */
    public String getIntro()
    {
        return _intro;
    }


    /** 
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    public String getName()
    {
        return _name;
    }


    /** 
     * @return the number of Rooms in this Arena
     */
    public int getNbrRooms()
    {
        // For now, until Rooms are added, this method always return 0
        return 0;
    }

//    /** Save this arena into the db 
//     * @see mylib.dmc.IRegistryElement#getKey()
//     */
//    public void save()
//    {
//        _dbi.dbSave(this);
//    }

    
    /** 
     * Set the intro text into the Arena object
     * @param introText     a description of what one sees on first appearance
     */
    public void setIntro(String introText)
    {        
        _intro = introText;
    }


    /** 
     * @return the display name of the Arena
     */
    public String toString()
    {        
        return _name;
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    
    
}       // end of Arena class
