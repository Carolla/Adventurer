/**
 * ClericsGuild.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;

/**
 *    Clerics' Guild for spells, magical items, quests, and lodging
 *    The default constructor creates the default "Monastery".     
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		April 17, 2013   // original <DD>
 * </DL>
 */
public class ClericsGuild extends Building
{
    // Data to initialize the default Store; must be static because it is used in constructor
    /** Name of this fine establishment */
    static private final String GUILD_NAME = "Monastery";
    /** Owner of this fine establishment */
    static private final String OWNER = "Balthazar";
    /** Monastery */
    static private final String HOVERTEXT =  "Clerical Guild for spiritual guidance and powers";
    /** What appears as one enters the building */
    static private final String EXTERIOR = "A tall wooden fence encloses a thick stone building with "+
                    "many rooms and smaller buildings. Figures in brown clerical robes roam about the."+
                    "garden you can see through the fence.";
    /** For this case, a non-Guild member cannot enter */
    static private final String INTERIOR = "A cheerful looking man in a brown robe greets you. "+
                    "His hood lays back on shoulders. \"How can I serve you? \" he asks. ";

    /** Paths to the images for this building **/    
    static private final String EXTERIOR_IMAGE = "ext_Monastery.JPG";
    static private final String INTERIOR_IMAGE = "ext_Monastery.JPG";
    
    
    /** The Arcaneum opens at dawn (6am) and closes at dusk (7pm) */
    private int OPENTIME = 600;
    private int CLOSETIME = 1900;


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Default Constructor, create Inn with default data 
     * @throws ApplicationException if the ctor fails
     */
    public ClericsGuild() throws ApplicationException
    {
        super(GUILD_NAME, OWNER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
        setBusinessHours(OPENTIME, CLOSETIME);
    }

    
    /** Constructor for typical general store with default business hours  
     * @param name          of this building
     * @param master        who runs this building
     * @param hoverText     quick phrase for purpose of the building
     * @param intro         first glance outside, or when entering
     * @param desc         detailed look of building, inside or out
     * @throws ApplicationException if the ctor fails
     */
    public ClericsGuild(String name, String master, String hoverText, String intro, String desc) 
                    throws ApplicationException
    {
        super(name, master, hoverText, intro, desc);
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /*  Two Guilds are considerd equal if their name and building masters are equal
     * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
     */
    @Override
    public boolean equals(IRegistryElement target)
    {
        // Guards against null target
        if (target == null) {
            return false;
        }
        ClericsGuild wizG = (ClericsGuild) target;
        boolean bName = this.getKey().equals(wizG.getKey());
        boolean bMaster= this.getMaster().getName().equals(wizG.getMaster().getName());
        return (bName && bMaster);
    }


    /* Get the key, which is the name of the Building
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    @Override
    public String getKey()
    {
        return _name;
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  INNER CLASS: MockStore 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
public class MockClericsGuild
{
    /** default ctor */
    public MockClericsGuild() {}

    public String getDescrption() 
    {
        return _intDesc;
     }
    
    public String getIntro() 
    {
        return _extDesc;
     }


}   // end of MockClericsGuild inner class


}           // end of Wizards Guild class
