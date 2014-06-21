/**
 * Filename: DgnBuild.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package rumorMillStuff.src.dmc;

import rumorMillStuff.src.hic.CommandParser;
import rumorMillStuff.src.hic.Dgn;
import rumorMillStuff.src.pdc.PatronRegistry;
import rumorMillStuff.src.pdc.Scheduler;

import mylib.pdc.MetaDie;


/**
 *	Initializes the data conversion process of reading xml data into objects, 
 * creates and inits a SAX validating parser to read XML files.  
 * The SAX Parser and helper methods call callback methods for the <code>Inn, Innkeeper, 
 * Patron</code>, and <code>PatronRegistry</code>. 
 *  
 *  @author Alan Cline
 * @version <DL>
 * <DT> 1.0	Dec 3 2005			// Split application into HIC, PDC, DMC components <DD>
 * <DT> 1.1	Jan 22 2006		// Remove XML Loader for proprietary parser-loader. <DD>
 * <DT> 1.2	Dec 10 2006    	// Merge SAX XML loader with Command Parser. <DD>
 * <DT> 2.0	Mar 22 2007		// Split single program into Run and   components with serialization. <DD>
 * <DT> 2.1	May 15 2007		// Write objects after parsing to output file without serialization. <DD>
 * <DT> 2.2	May 20 2007		// Break common objects into base classes and subclass loaders. <DD>
 * <DT> 2.3	Jul 4 2007			// Add the PatronRegistry and Patrons to serializaton  <DD>
 * <DT> 3.0	Sep 29 2007		// Read xml data directly into memory objects; there is no output file.  <DD>
 * <DT> 3.1	Feb 29 2008		// Move the file reading and parsing into this class, from the Dgn class; and redirect the output streams. <DD>
 * <DT> 4.0	Mar 29 2008		// Move most init processing here when multi-threading added (splash screen display.) <DD>
 * <DT> 4.1 	Jun 23 2008 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see SAXStream
 * @see hic.Dgn
 */
public class DgnBuild
{

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** SAX Stream Parser that includes the XML Schema validator. */
    private SAXStream _sst = null;	
	
    /** 
     * Creates the validating parser and implements the required methods from the SAX parser
     * base class. Picks up the file names from the static <code>hic.Dgn</code> repository 
     * (they are not passed from the windowing system).
     */
	public DgnBuild()
	{
        // Create a SAX Stream Parser, which includes the XML Schema validator
        _sst = new SAXStream(Dgn.dataFile, Dgn.schemaFile);	
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
    /**
     * Initializes the system and starts the <code>Scheduler</code> and 
     * <code>CommandParser</code>. Creates the randomizer, and  initializes the 
     * <code PatronRegistry</code> to schedule the Patrons to enter (and leave) the Inn.
     */
    public void init()
    {	    	
		// Get a generic randomizer, the MetaDie (seed it for debug stability); remove later
    	MetaDie _md = new MetaDie(1007L);  // for repeatable testing
//    	MetaDie _md = new MetaDie();
    	
    	// Get a copy of the CommandParser singleton for the Scheduler.
    	CommandParser cp = CommandParser.createInstance();
    	
        // Get a copy of the Scheduler singleton and pass it to the PatronRegistry so that the PatronRegistry
        // can schedule the Patrons to enter and leave the inn at the proper time.
        Scheduler skedder = Scheduler.createInstance(cp);

    	// Get a copy of the existing PatronRegistry singleton and initialize it
        PatronRegistry pReg = PatronRegistry.getInstance();
    	pReg.initPatrons(_md, skedder);
    		
    	Dgn.auditMsg("Dungeon Inn initialized.");
    }

	/**
	 * Reads, parses, validates, and loads the xml file. 
	 */
	public void load() 
    {
        Dgn.auditMsg("SAX Parsing dungeon data from XML files.");
      
        // Read the XML data files and parse data into objects
        _sst.parseSAXStream();
        Dgn.auditMsg("SAXStream validated and parsed.");
    }

	
} 	// end of the DgnBuild class

