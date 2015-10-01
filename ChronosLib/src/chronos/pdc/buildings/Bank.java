/**
 * Bank.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;


import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;


/**
 * The Bank is a location for making wills, buying and selling valuables, and depositing wealth for
 * later.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Apr 8, 2013 // original
 *          <DD>
 *          </DL>
 */
public class Bank extends Building
{
    // Data to initialize the default Bank; must be static because it is used in constructor
    /** Name of this fine establishment */
    static private final String BANK_NAME = "The Bank";
    /** Owner of this fine establishment */
    static private final String OWNER = "J.P. Pennypacker";
    /** Town Bank */
    static private final String HOVERTEXT = "The Bank, for deposits, wills, and loans";
    /** What appears as one enters the building */
    static private final String EXTERIOR =
            "The Bank is more stylish than the other buildings in this town.";
    /** What one senses when looking around the inside of the Inn when few patrons are here. */
    static private final String INTERIOR =
            "You face a baldish middle-aged man on the other "
                    +
                    "side of an old counter about four feet high. A dark metal grill partitions his side of the "
                    +
                    "counter from your side. A large man with a large axe stands silently in the corner, "
                    +
                    "eyeing you suspiciously.";

    /** Paths to the images for this building **/
    static private final String EXTERIOR_IMAGE = "ext_Bank.JPG";
    static private final String INTERIOR_IMAGE = "raw_int_Bank.jpg";


    /** The bank opens at 9am and closes at 3pm */
    private int OPENTIME = 900;
    private int CLOSETIME = 1500;


    /*
     * CONSTRUCTOR(S) AND RELATED METHODS
     */

    /**
     * Default Constructor creates the default Bank
     * 
     * @throws ApplicationException if the ctor fails
     */
    public Bank() throws ApplicationException
    {
        super(BANK_NAME, OWNER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
        setBusinessHours(OPENTIME, CLOSETIME);
    }


    /**
     * Constructor for typical inn with default business hours and no busy description
     * 
     * @param name of this building
     * @param master who runs this building
     * @param hoverText small phrase for purpose of the Buidling
     * @param exterior first glance of outside of Bank
     * @param interior detailed look of building inside
     * @throws ApplicationException if the ctor fails
     */
    public Bank(String name, String master, String hoverText, String exterior, String interior)
            throws ApplicationException
    {
        super(name, master, hoverText, exterior, interior);
    }


    /*
     * Two Banks are considerd equal if their name and building masters are equal
     * 
     * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
     */
    @Override
    public boolean equals(IRegistryElement target)
    {
        if (target == null) {
            return false;
        }
        Bank bank = (Bank) target;
        boolean bName = this.getKey().equals(bank.getKey());
        boolean bMaster = this.getMaster().getName().equals(bank.getMaster().getName());
        return (bName && bMaster);
    }


    /*
     * Get the key, which is the name of the Building
     * 
     * @see mylib.dmc.IRegistryElement#getKey()
     */
    @Override
    public String getKey()
    {
        return _name;
    }


    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */


    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockBank
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    public class MockBank
    {
        /** default ctor */
        public MockBank()
        {}

        /** Get the building's name */
        public String getName()
        {
            return _name;
        }


        /** Get the intro and description statements */
        public String[] getDescs()
        {
            String[] s = new String[3];
            s[0] = _extDesc;
            s[1] = _intDesc;
            return s;
        }

    } // end of MockBank inner class


} // end of Bank class

