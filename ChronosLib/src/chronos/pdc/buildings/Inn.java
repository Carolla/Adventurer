/**
 * Inn.java
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

/**
 *    Main building in town for rest, food, conversation, and sometimes even a bar brawl.
 *    Heroes can be temporarily banned from the Inn for fighting.
 *    The default constructor creates "Ugly Ogre Inn".     
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jan 28, 2013   // original <DD>
 * </DL>
 */
public class Inn extends Building
{
    // Data to initialize the default Inn; must be static because it is used in constructor
    /** Name of this fine establishment */
    static private final String INN_NAME = "Ugly Ogre Inn";
    /** Owner of this fine establishment */
    static private final String INNKEEPER = "Bork";
    /** Inn */
    static private final String HOVERTEXT = "Center for social events, food, and lodging";
    /** What appears as one enters the building */
    static private final String EXTERIOR = "You stand outside the door beneath the old "+ 
                    "weather-beaten sign, with words barely readable:  " + INN_NAME;
    /** What one senses when looking around the inside of the Inn when few patrons are here. */
    static private final String INTERIOR = "You find yourself inside a darkened and smokey "
            +  "room. As your eyes adjust to the dimness, you see a burly bartender filling mugs from "
            +  "behind a solid oak bar along the north wall. ";
    /** What one senses when looking around the inside of the Inn when many patrons are here. */
    static private final String BUSY_DESC = "Serving women are delivering food to the patrons at the " +
            "various tables scattered about. The clatter of crockery and the bustling of dinner " +
            "activity is loud, enough to make attempts at conversation difficult. " +
            "The patrons notice you, and suddenly it becomes much quieter. ";
    
    /** Paths to the images for this building **/    
    static private final String EXTERIOR_IMAGE = "raw_ext_Ugly Ogre Inn.JPG";
    static private final String INTERIOR_IMAGE = "int_inn.jpg";
    
    /** The standard description is saved in the base class, but the busy description is stored here */
    private String _busyDescription = null;

    /** The Inn opens at 6am and closes at midnight */
    private int OPENTIME = 600;
    private int CLOSETIME = 2400;

    /** Minimum number of patrons that indicate if the Inn is busy or not */
    private int NBR_PATRONS_TO_BE_BUSY = 4;

    /** Current number of patrons in the Inn plus the Innkeeper */
    private int _patronsNow = 1;

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Default Constructor, create Inn with default data 
     * @throws ApplicationException if the ctor fails
     */
    public Inn() throws ApplicationException
    {
        super(INN_NAME, INNKEEPER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
        _busyDescription = BUSY_DESC;
        setBusinessHours(OPENTIME, CLOSETIME);
    }

    
    /** Constructor for typical inn with default business hours and no busy description 
     * @param name          of this building
     * @param master        who runs this building
     * @param hoverText     quick phrase for purpose of building
     * @param intro         first glance outside, or when entering
     * @param desc         detailed look of building, inside or out
     * @throws ApplicationException if the ctor fails
     */
    public Inn(String name, String master, String hoverText, String intro, String desc) 
                    throws ApplicationException
    {
        super(name, master, hoverText, intro, desc, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    }


    /** Overloaded constructor for an extra "busy description".  
     * @param name          of this building
     * @param master        who runs this building
     * @param hoverText     quick phrase for purpose of building
     * @param intro         first glance outside, or when entering
     * @param desc         detailed look of building, inside or out
     * @param busyDesc         internal description when the Inn is very busy
     * @throws ApplicationException if the ctor fails
     */
    public Inn(String name, String master, String hoverText, String intro, String desc, String busyDesc) 
                    throws ApplicationException
    {
        super(name, master, hoverText, intro, desc, EXTERIOR_IMAGE, INTERIOR_IMAGE);
        _busyDescription = busyDesc;
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /*  Two Inns are considerd equal if their name and building masters are equal
     * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
     */
    @Override
    public boolean equals(Object target)
    {
        if (target == null) {
            return false;
        }
        Inn inn = (Inn) target;
        boolean bName = this.getKey().equals(inn.getKey());
        boolean bMaster= this.getMaster().getName().equals(inn.getMaster().getName());
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


    /**When the Hero enters the building, he or she sees what the inside looks like. For the Inn,
     * the description changes depending on whether it is busy or not. This conditional method 
     * overrides the base class method.
     * 
     * @return the standard description or the busy description 
     */
    @Override
    public String getInteriorDescription()
    {
        return (_patronsNow >= NBR_PATRONS_TO_BE_BUSY) ? _busyDescription : _intDesc;
    }
    
      /** Set the description seen when the Inn is busy
     * @param   bdesc   description of a busy Inn
     */
    public void setBusyDescription(String bdesc)
    {
        _busyDescription = bdesc;
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  INNER CLASS: MockInn 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
public class MockInn
    {
        /** default ctor */
        public MockInn() {}

        /** Get the Inn's name */
        public String getName()
        {
            return _name;
        }
        
        /** Get the Innkeeper's name */
        public String getMasterName()
        {
            return _buildingMaster.getName();
        }
        
        /** Get the description statements: intro, standard desc; busy desc */
        public String[] getDescs()
        {
            String[] s = new String[3];
            s[0] = _extDesc;
            s[1] = _intDesc;
            s[2] = _busyDescription;
            return s;
        }
        
        /** Get the current number of patrons, making the Inn busy or notbusy
         * @param nbrPatrons    number of patrons in the Inn, not counting staff
         */
        public void setCurrentPatrons(int nbrPatrons)
        {
            _patronsNow = nbrPatrons;
        }

        /** Set the standard description */
        public void setDesc(String desc)
        {
            _intDesc = desc;
        }
        
    }   // end of MockInn inner class


}           // end of Inn class
