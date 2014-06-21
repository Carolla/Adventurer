/**
 * Town.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.pdc;

import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.dmc.RegistryElement;

import java.util.ArrayList;
import java.util.List;
/**
 *    A Town is the thin container for all Buildings, and some global settings.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Feb 6, 2013   // original <DD>
 * <DT> Build 110      May 4, 2013  // updated and augmented <DD>
 * </DL>
 */
public class Town extends RegistryElement
{
    /** Name of the town */
    private String _name = null;

    /** Description of the town when first entererd during the day */
    private String _descDay = null;
    /** Description of the town when first entererd during the night */
    private String _descNight = null;
    /** Cost of living factor. All prices in the town are multiplied by this number: 1.0 is standard. */
    private double _costOfLiving = 1.0;         
    
    /** Buildings in town */
    private List<Building> _buildings = new ArrayList<Building>();
    
    /** Name of default town */
    private final static String TOWN_NAME = "Biljur'Baz";
    /** Description of town when entered in the daytime */
    private final static String DESC_DAY= "A country road divides a few dilapidated buildings from the "+
                  "apple treees and grain fields on the other side. One of the larger buildings catches "+
                  "your glance in the center of the town--the Inn.  You can also see a Bank, a Store, and "+
                  "a few others. Larger, more foreboding buildings decorate the slopes around the town. "+
                  "At the edge of town, nestled among a mountain foothill, is a huge and ominious "+
                  "fortress of dark rock. Even by day, it says, \"Stay away.\"";

    /** Description of town when entered in the evening */
    private final static String DESC_NIGHT =  "Yellow lights burn in a line along a country road, casting "+
                  "more shadow than illumination. You see small squares of light from the windows of "+
                  "largest grey building near the center of town--probably the town's Inn. ";

//    /** Buildings of the default town */
//    private final String[] DEF_BLDGS = {"Ugly Ogre Inn", "The Bank", "Jail", "Rat's Pack", 
//            "Monastery", "Arcaneum", "Stadium", "Rogues' Den"};

    /** The Dungeon associated with this town */
    public static final String DEF_DGN_NAME = "Quasqueton";

    /** Global clock time, in seconds, for the states of the city, arena, buildings, and Hero. */
    // TODO: Make this clock an object later
    /* TODO: The clock is triggered to start based on the Inn, which must always be open when the 
     * Hero starts. The clock will start randomly between 1 hr after the Inn opens, and 1 hr before the
     * Inn closes. */
    private long _gameClock = 0L;
    /** Fixed start time when Town awakes: 7am */
    private int INIT_TIME = 7;
    

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** Default constructor that creates the default Town */
    public Town()
    {
        // Initalize the default town
        this(TOWN_NAME, DESC_DAY, DESC_NIGHT);        
//        for (String s : DEF_BLDGS) {
//            if (buildingRegContainsName(s)) {
//                _buildingNames.add(s);
//            }
//            else {
//                String errmsg = String.format("\nTown(): Invalid building name in default town list. " +
//                                "Building %s not found in BuildingRegistry", s);
//                System.err.println(errmsg);
//            }
//        }
    }
    
    
    /** Constructor. If the descNight is null, the descDay value is used. Some rare towns do not have
     * night time descriptions or Arenas associated with them, so null values are ok.
     * @param name          of the town
     * @param descDay       appearance of the town when it is first entered in day  
     * @param descNight     appearance of the town when it is first entered in evening; can be null
     * @throws NullPointerException if name or descDay is null
     */
    public Town(String name, String descDay, String descNight)  throws NullPointerException
    {
        if ((name == null) || (descDay == null)) {
                throw new NullPointerException("Name or daytime description cannot be null");
        }
        _name = name;
        _descDay = descDay;
        _descNight = (descNight == null) ? descDay : descNight;
        // Initialize running clock to INIT_TIME
        _gameClock = INIT_TIME * Constants.SECS_PER_HOUR;
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** Add a list of Buildings to the Town, verifying each one from the BuildingRegistry
     * @param bldgList      list of building names to add
     * @throws ApplicationException if building cannot be found
     */
    public void addBuildings(String[] bldgList) throws ApplicationException
    {
        BuildingRegistry bReg = (BuildingRegistry) RegistryFactory.getRegistry(RegKey.BLDG);
        for (String bName : bldgList) {
            Building b = bReg.getBuilding(bName);
            if (b == null) {
                throw new ApplicationException("Cannot find " + bName + "in BuildingRegistry.");
            }
            else {
                _buildings.add(b);
            }
        }
    }
    
    
    /** Add a building to the Town list for retrieval for the Building Registry after verifying
     * that the Building exists in the Building registry
     * 
     * @param bldgName  of the building to add
     * @return true if the add was successful
     */
    public boolean buildingRegContainsName(String bldgName) 
    {
        BuildingRegistry bReg = (BuildingRegistry) RegistryFactory.getRegistry(RegKey.BLDG);
        Building b = bReg.getBuilding(bldgName);
        return b != null;
    }
    

    /** Two Towns are equal if their names are equal */
    @Override
    public boolean equals(Object targetTown)
    {
        Town target = (Town) targetTown;
        return _name.equalsIgnoreCase(target.getName());
    }

    
    /** Get a list of all the Buildings in the town */
    public List<Building> getAllBuildings()
    {
        return _buildings;
    }

    
    /** Get a Building object from the Building Registry*/
    public Building getBuilding(String name)
    {
        BuildingRegistry bldgReg = (BuildingRegistry) RegistryFactory.getRegistry(RegKey.BLDG);
        Building bldg = (Building) bldgReg.getUnique(name);
        return bldg;
    }

    /** Get the cost of living (price adjustment) for this town */
    public double getCostOfLiving()
    {
        return _costOfLiving;
    }

    
    /** Get the appearance of this town during the day */
    public String getDayDescription()
    {
        return _descDay;
    }

    
    /* (non-Javadoc)
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    @Override
    public String getKey()
    {
        return _name;
    }


    /** Get the name of this town */
    public String getName()
    {
        return _name;
    }


    /** Get the appearance of this town at night */
    public String getNightDescription()
    {
        return _descNight;
    }


    /** Set the standard of living (price factor) for the town
     * @param col   multiplier for price adjustment, but at least 0.1
     */
    public void setCostOfLiving(double col)
    {
        _costOfLiving = col;
    }

    /** Get the name of the town     */
    public String toString()
    {
        return _name;
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}           // end of Town class



///** The Dungeon's overview */
//private final String DEF_DGN_INTRO = "This is the fortress of the mighty warrior Rogahn and the " +
//    "genius mage Zeliigar. Nestled among the escarpment outside of town, it has become the " +
//      "lair to bandits looking for loot, vile creatures looking for shelter, and horrible monsters that "+
//    "have found a good supply of food--human or otherwise." ;
