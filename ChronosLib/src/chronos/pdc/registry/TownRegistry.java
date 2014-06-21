/**
 * TownRegistry.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import chronos.Chronos;
import chronos.pdc.Town;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 *    Contains a collection of all Town objects
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Feb 6, 2013   // original <DD>
 * </DL>
 */
public class TownRegistry extends Registry
{
    /** List of 8 buildings to add to default Town (Guilds listed first) */
    static private final String[] DEF_BUILDING_LIST = { "Arcaneum",  "Jail", "Monastery", 
            "Rat's Pack General Store", "Rogues' Den", "Stadium",  "The Bank", "Ugly Ogre Inn" };

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /**
     * Private ctor because this singleton is called from getInstance().
     * Registry filename is used for database
     * @param   init    flag to initialize registry for default data if true
     */
    protected TownRegistry() 
    {
        super(Chronos.TownRegPath);
    }

    
    /**
     * Populate the TownRegistry with the default Town, and the names of the buildings and arena.
     * Confirm that the building names represent Registry elements, and that the arena is in its own
     * .dgn file.
     */
    @Override
    public void initialize() 
    {
        // Create the default town and add it to the TownRegistry
        Town town = new Town();
        // Add the default buildings to it
        try {
            town.addBuildings(DEF_BUILDING_LIST);
        } catch (ApplicationException ex) {
            System.err.println(ex.getMessage());
        }
    }


//    /** Close db, destroy the dbReadWriter and set this registry to null
//     */
//    public void closeRegistry()
//    {
//        super.close();
////        _thisReg = null;
//    }
//
//    public void deleteRegistry()
//    {
//        super.delete();
////        _thisReg = null;
//    }
    
    /** Retrieve all Towns from the Town Registry
     * @return the list of Towns
     */
    public ArrayList<Town> getTownList()
    {
        // Run the query to retrieve all buildings from the registry
        List<IRegistryElement> result = super.getAll();
        ArrayList<Town>  townList = new ArrayList<Town>(result.size());
        for (IRegistryElement e : result) {
            townList.add((Town) e);
        }
        return townList;
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}       // end of TownRegistry class

///** Name of default town */
//private final String TOWN_NAME = "Biljur'Baz";
///** Description of town when entered in the daytime */
//private final String DESC_DAY= "A country road divides a few dilapidated buildings from the "+
//                "apple treees and grain fields on the other side. One of the larger buildings catches "+
//                "your glance in the center of the town--the Inn.  You can also see a Bank, a Store, and "+
//                "a few others. Larger, more foreboding buildings decorate the slopes around the town. "+
//                "At the edge of town, nestled among a mountain foothill, is a huge and ominious "+
//                "fortress of dark rock. Even by day, it says, \"Stay away.\"";
//
///** Description of town when entered in the evening */
//private final String DESC_NIGHT =  "Yellow lights burn in a line along a country road, casting "+
//                "more shadow than illumination. You see small squares of light from the windows of "+
//                "largest grey building near the center of town--probably the town's Inn. ";
//
///** Buildings of the default town */
//private final String[] DEF_BLDGS = {"Ugly Ogre Inn", "The Bank", "Jail", "Rat's Pack", 
//                "Monastery", "Arcaneum", "Stadium", "Rogues' Den"};
///** The Dungeon associated with this town */
//private final String DEF_DGN_NAME = "Quasqueton";
///** The Dungeon's overview */
//private final String DEF_DGN_INTRO = "This is the fortress of the mighty warrior Rogahn and the " +
//        "genius mage Zeliigar. Nestled among the escarpment outside of town, it has become the " +
//        "lair to bandits looking for loot, vile creatures looking for shelter, and horrible monsters that "+
//        "have found a good supply of food--human or otherwise." ;

