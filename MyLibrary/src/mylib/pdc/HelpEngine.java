/**
 * HelpEngine.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.pdc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import mylib.MsgCtrl;
import mylib.dmc.HelpReader;


/**
 * <code>HelpEngine</code>, a PDC class, maps the HIC class <code>HelpDialog</code> 
 * to its DMC implementation class, <code>HelpReader</code>, the binary help file handler. 
 * <code>HelpReader</code> also contains callback methods for the
 * <code>HelpContentHandler</code>, which is the SAXParser's 
 * <code>ContentHandler</code> for parsing and building the binary help file. 
 * <p> 
 * <code>HelpEngine</code> was built intentionally to be reusable for any 
 * application, and provides a default XML Schema for parsing validation.
 * It is called by the <code>HelpDialog</code> class in the library.
 * The application-specific XML file should contain all the help text for the application, but  
 * the XML help tags must match the String help IDs (or keys) in <code>HelpDialog</code>. 
 * <p>
 * Implementation Note: Because the <code>HelpEngine</code> and support classes are 
 * contained in the library, the location of the application-specific help text file and directory 
 * must be passed through the <code>HelpDialog</code> object when it is created, which 
 * is then passed to the <code>HelpReader</code> and other DMC objects. This class 
 * uses a default library folder for the location of library resources, such as the XML Schema file. 
 * 
 * @see mylib.dmc.HelpReader
 * @see mylib.dmc.HelpContentHandler
 * @see mylib.hic.HelpDialog
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Dec 16, 2008   // original <DD>
 * <DT> Build 1.1		Dec 23, 2008   // moved file implementation to its own
 * 			IndexedRandomFile class, and reset calling links in HelpContentHandler <DD>
 * <DT> Build 1.2		July 9  2009   	// minor cleanup when using it for <i>Fortune</i> <DD>
 * <DT> Build 2.0		July 16  2009  	// allowed more app control with file names <DD>
 * <DT> Build 2.1		July 21  2009  	// repackaged it to be more app-independent<DD>
 * <DT> Build 2.2		Aug 9 	 2009  	// revised for creating and loading functionality <DD>
 * <DT> Build 2.3		Aug 14  2009  	// revised for retrieving help records <DD>
 * </DL>
 */
public class HelpEngine
{
	/** Text help file (input file) extension */
	private final String HELP_EXT = ".hlp";
	/** Binary help file (output) extension */
	private final String BIN_EXT = ".hlp";

	/** Text help file path set by application */
	private String _xmlInputPath = null;
	/** Binary help file path built on xml file name */
	private String _binOutputPath = null;

	/** Mode when opening, and will create a new one */
	private final String READWRITE = "rw";
	/** Mode when getting help text */
	private final String READONLY = "r";

	/** Reference to indexed random access file component */
	private HelpReader _helpRdr = null;
	/** Internal reference to HelpEngine singleton */
	static private HelpEngine _helpEngine = null;

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 						CONSTRUCTOR AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
	/** 
	 * Create the singleton <code>HelpEngine</code> if it doesn't exist
	 * 
	 * @param appHelpRegPath   path and filename for help registry
	 * @return existing or newly created HelpEngine
	 */
    public static HelpEngine createInstance(String appHelpRegPath)
    {
//		MsgCtrl.msgln("\tHelpEngine.createInstance(): ");
        if (_helpEngine == null) {
        	// Create the HelpEngine and the HelpReader DMC component
            _helpEngine = new HelpEngine(appHelpRegPath);

            // Re-open the binary help file. If it is out-of-date or does not exist, then convert 
            // and load the XML input Data into a HelpReader binary file; or, do nothing 
//            if (_helpEngine != null) {
//            _helpEngine.loadHelpData();
//            }
        }
        return _helpEngine;
    }

    
    /** Massage the caller's input file information to create a <code>HelpEngine</code> 
     * and its file handling component, <code>HelpReader</code>.  
     * The <code>HelpEngine</code> then calls a wrapper method to the 
     *  <code>HelpReader</code> to build the binary file and be ready for help requests.
     * 
     * @param appHelpRegPath		path and filename of application-specific help registry
     * @param appHelpFname		file name containing XML text help info
     */
	private HelpEngine(String appHelpRegPath) 
	{
//		MsgCtrl.msgln("\tHelpEngine.constructor: ");
		// Convert the dir and help fname to a bin filename, else catch exception if
		// files cannot be found
//	    try {
//	    	// This method sets the input and output file paths
//	    	buildPath(appHelpRegPath);
//	    } catch (FileNotFoundException fnfex) {
//	    	MsgCtrl.errMsgln("\tFileNotFoundException = " + fnfex.getMessage());
//	    } 
	}	

	
//	/** Determines whether to open an existing helpfile (calls <code>xmlUpdated()</code>), 
//	 * or create a new one (calls <code>createHelpfile()</code> if necessary).
//	 *  In either case, this method creates the HelpReader in either RW or RO mode.
//	 *  
//	 * @return true if the binary file is ready to be read, whether a load was necessary or not
//	 * @see #xmlUpdated()
//	 * @see #createHelpfile(String)
//	 */ 
//	public boolean loadHelpData()
//	{
//		MsgCtrl.msgln(this, "\tHelpEngine.loadHelpData(): "); 
//		try {
//			// Check if xml file exists and is newer than than the binary output file
//			if ((xmlUpdated()) == true ) {
//				// Binary helpfile needs to be created
//		    	_helpRdr = new HelpReader(_binOutputPath, READWRITE);
//				// Create the HelpReader to parse the XML input file and create the binary helpfile
//				if (createHelpfile(_xmlInputPath) == 0L) {
//					return false;
//				}
//				// Close and verify the RW file before re-opening
//				if (_helpRdr.closeFile() == false) {
//					return false;
//				}
//			}
//			
//			// Binary helpfile does not need to be created
//			_helpRdr = new HelpReader(_binOutputPath, READONLY);
////			MsgCtrl.msgln(this, "\tSuccesfully re-opened the help file = " + 
////			                getLength() + " bytes.");
//		} catch (IOException ex) {
//			MsgCtrl.errMsgln("\tException = " + ex.getMessage());
//			return false;
//		}
//		// Leave the help file open for queries: do not close
//		return true;
//	}

	
	/** Creates the <code>HelpReader</code> in ReadWrite mode to parse the XML input file
	 * and create the binary helpfile. Calls the SAX Parser to create the binary help file
	 * 
	 * @param xmlPath		long pathname to the XML input file
	 * @return size of binary file (in bytes) else 0
	 */
	private long createHelpfile(String xmlPath)
	{
		long len = 0L;
		try {
	    	// Truncate existing file because RAFile loader always appends 
	    	_helpRdr.setLength(0L);
			// Reads the xmlinput file, creates the binary random access file, 
	    	// then verifies the output file (an internal structure check)
			boolean loadDone = _helpRdr.load(xmlPath);
			if (loadDone == false) {
				MsgCtrl.errMsgln("\tcreateHelpFile(): Load failure; aborting");
			}
			else { 
				// HelpReader must be open for this to work
				len = getLength();
				MsgCtrl.msgln("HelpEngine.createHelpFile(): Load complete. Binary help file created: " 
								+ len + " bytes");
			}
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\tException = " + ex.getMessage());
		}
		return len;
	}

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** This is the liaison method to the application after the <code>HelpEngine</code> is
	 * created. It supports the application's help queries for GUI widgets through the help ID
	 * associated with the widget and its help text description in the XML input file. 
	 * Most of the low-level work is done by the <code>HelpReader</code> object. 
	 * 
	 * @see HelpReader
	 * 
	 * @param key	the reference id for retrieving the text
	 * @return 	[0] the short text label, or null if not found; and <br>
	 * 					[1] the help text line without the label, or null if not found. 
	 * <br>
	 *  Method is quiet on exceptions, and does nothing. Null labels or text is most likely
	 *  returned to GUI.
	 */

	public String[] getHelp(String key) 
	{
		String[] helpEntry = new String[2];
		try {
			helpEntry = _helpRdr.getHelpRecord(key);
		} catch (IOException ex) {
			MsgCtrl.msgln("\nHelpEngine.getHelp(): " + ex.getMessage());
		}
    	return helpEntry;
	}


	/** Get the file length of the binary helpReader file
	 * @return file length, in bytes
	 */
	public long getLength()
	{
		long bytelen = 0L;
		try {
			bytelen = _helpRdr.length();
		} catch (IOException ex) {
			MsgCtrl.errMsgln("HelpEngine.getLength():  Can't get length from the file");
		}
		return bytelen;
	}
		
	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
	/** Build and assign the file path needed by the SAXParser. 
	 * The XML extension is stripped off, and the bin ext is concatenated to the xml filepath. 
	 * All three names of the XML input file (<i>.xml</i>), XML Schema file  (<i>.xsd</i>), and
	 * the <code>HelpReader</code>, a binary indexed random access output file  
	 * (<i>.hlp</i>) are all needed by the SAXStream parser. Each of these three attributes 
	 * are set here.
	 * 
	 *  @param userHelpDir the directory in which the XML input text is located, and
	 *  		to which the binary help file will be written
	 *  @param userHelpFname	the filename of the XML input text, with .xml extension)
	 *  @throws FileNotFoundException if xml file or directory cannot be found
	 */
	private void buildPath(String userHelpDir, String userHelpFname) 
							throws FileNotFoundException
	{
		// Guard to verify XML extension
		if (userHelpFname.endsWith(HELP_EXT) == false) {
			throw new FileNotFoundException(
					"HelpEngine.buildPath(): Cannot find xml help file specified for input: "
					+ userHelpFname);
		}
		// Guard to verify that that both dir and input file exist
		File hdir = new File(userHelpDir);
		if (hdir.exists() == false) {
			throw new FileNotFoundException(
					"HelpEngine.buildPath(): Cannot find help directory specified: " 	+ userHelpDir);
		}
		String path = userHelpDir + userHelpFname;
		File infile = new File(path);
		if (infile.exists() == false) {
			throw new FileNotFoundException(path);
		}
		
		// Extract the extension to create the bin output file of the same name,  but with
		// a different extension
		int extStart = userHelpFname.lastIndexOf(".");
		String binBase = userHelpFname.substring(0, extStart);
		// Rebuild with proper extensions
		_binOutputPath = userHelpDir + binBase + BIN_EXT;
    	_xmlInputPath = userHelpDir + userHelpFname;
	}


//	/** Compare status of the xml input file and binary output file for existence, and last
//	 * update, in case the binary help file does not need to be re-created. This helper
//	 * returns true if [1] the XML is available for conversion, and [2] the binary file either
//	 * does not exist or is older than the XML file.   
//	 * 
//	 *  @return true if conversion should proceed, else false if not
//	*/
//	private boolean xmlUpdated()
//	{
//		// CASE 1: XML input file does not exist
//		// Check for existence of the XML file and get its last modifed date
//		long xmlTouch = 0L;
//		if (_xmlInputPath == null) {
//			return false;
//		}
//		File xml = new File(_xmlInputPath);
//		if (xml.exists()) {
//			xmlTouch = xml.lastModified();
//		}
//		else {
//			// XML file does not exist, so cancel creating the output
//			return false;
//		}
//		
//		// CASE 2: Binary output file does not exist
//		// If binary o/p filepath doesn't exist, then proceed with creating it
//		File bin = null;
//		if (_binOutputPath == null) {
//			return true;
//		}
//		// If binary o/p file is incorrect, and doesn't exist, then proceed with creating it
//		else {
//			bin = new File(_binOutputPath);
//			// If the binary file does not exist, proceed with creating it
//			if (bin.exists() == false) {
//				return true;
//			}
//		}
//		// CASE 3: Both xml i/p and bin o/p files exist
//		// Check for existence of the binary help file, and compare its last modifled date
//		// with that of the xml input file
//		long binTouch = bin.lastModified();
//		// If the xml file is newer than the binary file, then re-create the binary output file;
//		// else return false
//		return (binTouch <= xmlTouch);
//	}

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 						INNER CLASS: MockHelpEnginer
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

/** This class is called by JUnit to check lower level private methods;
 * most of these methods are public wrappers.*/
public class MockHelpEngine
{
	/** Unused constructor */
	public MockHelpEngine() { }
	
	/** Wrap the private method buildPath and return the two filepaths
	 * @param dir	contains XML input file and destination for binary output file
	 * @param inFile		XML help text file 
	 * @return two elements: full path for xml file and binary helpfile
	 */
	public String[] buildPath(String dir, String inFile)
	{
		MsgCtrl.msgln("\tHelpEngine.buildPath(): ");
		String[] helpPath = new String[2];
		try {
			HelpEngine.this.buildPath(dir, inFile);
		} catch (FileNotFoundException ex) {
			MsgCtrl.errMsgln("\tException = " + ex.getMessage());
			return null;
		}
		// Results are placed in object attributes
		helpPath[0] = _xmlInputPath;
		helpPath[1] = _binOutputPath;
		return helpPath;
	}


//	/** Dump the index of the binary help file, which is in the MockHelpReader */
//	public void dumpIndex()
//	{
//		MockHelpReader mock = _helpRdr.new MockHelpReader();
//		mock.dumpIndex();
//	}


	/** Get the HelpReader file for internal testing
	 * @returns reference to HelpReader RandomAccessFile
	 */
	public HelpReader getHelpReader()
	{
		return _helpRdr;
	}
	
	
	/** Get the last time the HelpReader was modified 
	 * @return time in milliseconds
	 */
	public long lastModified()
	{
		File tmp = new File(_binOutputPath);
		return tmp.lastModified();
	}


//	/** Create the binary IRAF with index and data entry section. 
//	 * 
//	 * @param xmlfile  can be null to signify a missing input file
//	 * @param binfile  to test against timestamps for modification comparisons
//	 * @return true if SAX parsing is to proceed
//	 */
//	public boolean loadHelpData(String xmlfile, String binfile)
//	{
//		// Set the internal parameters
//		_xmlInputPath = xmlfile;
//		_binOutputPath = binfile;
//		return HelpEngine.this.loadHelpData();
//	}

	
//	/** Verify XML updated checks for all file conditions correctly
//	 * 
//	 * @param xmlfile  can be null to signify a missing input file
//	 * @param binfile  to test against timestamps for modification comparisons
//	 * @return true if SAX parsing is to proceed
//	 */
//	public boolean xmlUpdated(String xmlfile, String binfile)
//	{
//		// Set the internal parameters
//		_xmlInputPath = xmlfile;
//		_binOutputPath = binfile;
//		return HelpEngine.this.xmlUpdated();
//	}


}	// end of MockHelpEngine inner class
	

}	// end of HelpEngine class
