/**
 * FightersGuild.java
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
 *    Fighters' Guild, for practice, quests, and lodging
 *    The default constructor creates the default "Stadium".     
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jan 28, 2013   // original <DD>
 * </DL>
 */
public class FightersGuild extends Building
{
    // Data to initialize the default Store; must be static because it is used in constructor
    /** Name of this fine establishment */
    static private final String GUILD_NAME = "Stadium";
    /** Owner of this fine establishment */
    static private final String OWNER = "Aragon";
    /** Fighter's Guild */
    static private final String HOVERTEXT = "Fighters' Guild, for martial training and weaponry";
    /** What appears as one enters the building */
    static private final String EXTERIOR = "You stand outside a large coliseum-style building. "+
                    "You can see a grassy courtyard through the entranceway, and hear the clashing "+
                    "of sword practice inside.";
    /** What one senses when looking around the inside of the Inn when few patrons are here. */
    static private final String INTERIOR = "The Guildmaster meets you at the archway into the Guild. "+
                    "Tiers of doors lead to the squire and knight lodgings. Brawny men and women are " +
                    "in the middle of heavy exercise, some armed with wooden swords, others with halberds, " +
                    "pikes, axes, shields and armor.";

    /** Paths to the images for this building **/    
    static private final String EXTERIOR_IMAGE = "raw_ext_Stadium.JPG";
    static private final String INTERIOR_IMAGE = "raw_ext_Stadium.JPG";
    
    
    /** The Store opens at 6am and closes at 6pm */
    private int OPENTIME = 500;
    private int CLOSETIME = 1600;


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Default Constructor, create Inn with default data 
     * @throws ApplicationException if the ctor fails
     */
    public FightersGuild() throws ApplicationException
    {
        super(GUILD_NAME, OWNER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
        setBusinessHours(OPENTIME, CLOSETIME);
    }

    
    /** Constructor for typical general store with default business hours  
     * @param name          of this building
     * @param master        who runs this building
     * @param hoverText     quick phrase for purpose of building
     * @param intro         first glance outside, or when entering
     * @param desc         detailed look of building, inside or out
     * @throws ApplicationException if the ctor fails
     */
    public FightersGuild(String name, String master, String hoverText, String intro, String desc) 
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
        FightersGuild fg = (FightersGuild) target;
        boolean bName = this.getKey().equals(fg.getKey());
        boolean bMaster= this.getMaster().getName().equals(fg.getMaster().getName());
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
    
public class MockFightersGuild
{
    /** default ctor */
    public MockFightersGuild() {}

    public String getDescrption() 
    {
        return _intDesc;
     }
    
    public String getIntro() 
    {
        return _extDesc;
     }


}   // end of MockStore inner class


}           // end of Inn class
