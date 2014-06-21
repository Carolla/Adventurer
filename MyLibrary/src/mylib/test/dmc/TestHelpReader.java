/**
 * TestHelpReader.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.test.dmc;


import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;
import mylib.MsgCtrl;
import mylib.dmc.HelpReader;
import mylib.dmc.HelpReader.MockHelpReader;

/**
 * Tests <code>HelpReader</code>, an indexed random file component of 
 * <code>HelpEngine</code>. <code>HelpReader</code> also creates the Java SAX  
 * Parser, part of the XML SAX library.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jul 25, 2009   // original: output side only <DD>
 * <DT> Build 1.1		Aug 13, 2009   // adjusted to use a 
 * 			<code>RandomAccessFile(File, String)</code> constructor to allow <code>File</code>
 * 			operations (instead of the <code>RandomAccessFile(String, String) constructor). <DD>
 * </DL>
 */

public class TestHelpReader extends TestCase
{

	/** Normally, these constants are passed in from the caller, but they are used
	* 	here for testing */
	private final String TEST_DIR = "src/mylib/resources/"; 
	/** The app's input help file name */
//	private final String XML_FNAME = "mylibTestHelp.xml";
	/** The output binary (IRF) help file created */
	private final String BIN_FNAME = "mylibTestHelp.hlp";

	/** The output binary (IRF) help file created in the Test space  */
	private final String BIN_TESTPATH = TEST_DIR+ BIN_FNAME;

	/** Size of long for counting tests */
	private final long SIZEOF_LONG = 8L;
	/** Size of fixed filed containing the hel keys */
	private final long KEY_SIZE = 10L;
	// Internal constant for checking
	private final long INDEX_FULL = -2L;
	// Internal constant for checking
	private final long NOT_FOUND = -1L;
	
	/** Reusable reference to the HelpReader under test */
	private HelpReader _hRdr = null;
	/** MockHelpReader for accessing internal data */
	private MockHelpReader _mock = null;
	
	
	/**
	 * Create the HelpReader and MockHelpReader
	 * @throws java.lang.Exception
	 * 	@Before
	 */

	public void setUp() throws Exception
	{
	    MsgCtrl.errorMsgsOn(true);
        MsgCtrl.auditMsgsOn(false);
		// A new HelpReader is created with an empty index (no text entries)
		_hRdr = createNewReader(TEST_DIR);
		assertNotNull(_hRdr);
		// Truncate the file to force no index
		_hRdr.setLength(0L);
		// Create the MockHelpReader
		_mock = _hRdr.new MockHelpReader();
	}

	/**
	 * Delete the common HelpReader under test
	 * @throws java.lang.Exception
	 * @After
	 */
	public void tearDown() throws Exception
	{
		_hRdr = null;
		_mock = null;
	}
	

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 													PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */		

	 /** Tests the primary low lever helper that looks for an EMPTY Slot, and subsequently,
	  * tests a lot of minor secondary methods too */
	public void testGetNextEmptySlot()
	{
        MsgCtrl.auditMsgsOn(false);

		// Get the record size (constant) for reuse
		long indexRecLen = _mock.getIndexRecSize();
		// This should force an empty index
		long indexSize = populate(0, 0);
		// Show an empty index
		_hRdr.dumpIndex();
		assertTrue(_mock.getNextEmptySlot() == NOT_FOUND);
		
		// Create a small empty index for testing
		int nbrEntries = 5;
		indexSize = populate(nbrEntries, 0);
		long expSize = indexRecLen * nbrEntries + SIZEOF_LONG;		
		// Check index visually -- should be empty
		_hRdr.dumpIndex();
		assertTrue(indexSize == expSize);
		// First empty slot should be the first slot (just past nbrEntries record-field)
		assertTrue(_mock.getNextEmptySlot() == 8L);

		// Create a small partially-populated index for testing
		indexSize = populate(nbrEntries, 1);
		expSize = indexRecLen * nbrEntries + SIZEOF_LONG;		
		// Check index visually -- should be empty
		_hRdr.dumpIndex();
		assertTrue(indexSize == expSize);
		// Next empty slot should be the second 
		assertTrue(_mock.getNextEmptySlot() == 26L);

		// Create a small populated index for testing
		indexSize = populate(nbrEntries, nbrEntries);
		expSize = indexRecLen * nbrEntries + SIZEOF_LONG;		
		// Check index visually -- should be empty
		_hRdr.dumpIndex();
		assertTrue(indexSize == expSize);
		// There should not be any empty slots now 
		assertTrue(_mock.getNextEmptySlot() == INDEX_FULL);
	}
		
		
	/** 
	 * Tests the index creation of the bin help file. The index contains as many records
	  * as specified, with each record a fixed length. <br>
	  * NOTE: Since <code>load()</code> is not called, there is no reading of the XML file.
	  * 
	  * <code>HelpContentHandler</code> callback.
	  */
	public void testCreateIndex()
	{
        MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln("testCreateIndex(): ");
		long idRecSize = 8L;

		// Create a new file with 1 index entry
		long nbrEntries = 1L;
		_hRdr = createNewReader(TEST_DIR);
		long indexSize = _hRdr.createIndex(nbrEntries);
		long expSize = _mock.getIndexRecSize() * nbrEntries + idRecSize;		
		assertTrue(indexSize == expSize);
		// Test that the file was created
		assertTrue(_mock.isExists(BIN_TESTPATH));
		
		// Create a file with 5 index entries
		nbrEntries = 5L;
		_hRdr = createNewReader(TEST_DIR);
		indexSize = _hRdr.createIndex(nbrEntries);
		expSize = _mock.getIndexRecSize() * nbrEntries + idRecSize;		
		assertTrue(indexSize == expSize);
		// Test that the file was created, then remove it for next test
		assertTrue(_mock.isExists(BIN_TESTPATH));
		
		// Create a file with 0 index entries (none); check that guard catches it
		nbrEntries = 0L;
		_hRdr = createNewReader(TEST_DIR);
		indexSize = _hRdr.createIndex(nbrEntries);
		assertTrue(indexSize == 0L);

		// Create a file with -1 index entries; check that guard catches it
		nbrEntries = -1L;
		_hRdr = createNewReader(TEST_DIR);
		indexSize = _hRdr.createIndex(nbrEntries);
		assertTrue(indexSize == 0L);

		// Test that the file was created
		assertTrue(_mock.isExists(BIN_TESTPATH));
	}
	
	
	 /** Tests the creation of the bin help file data entries, and their relation with the index. 
	  * NOTE: Since load() is not called, there is no reading of the XML file.
	  * 
	  * <code>HelpContentHandler</code> callback.
	  */
	public void testWriteEntry()
	{
        MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln("testWriteEntry(): ");

		// Remove any old help files
		_mock.delete(BIN_TESTPATH);
		
		// Test that an empty file will abort
		_hRdr = createNewReader(TEST_DIR);
		long len = _hRdr.createIndex(0L);
		assertTrue(len ==0);
		try {
			assertTrue(_hRdr.writeEntry("Empty", "Should Abort") == false);
		} catch (IOException ex) {
			MsgCtrl.msgln("\ttestWriteEntry(): valid exception thrown");
		}
		
		// Write a few strings to the help file; create the file to hold 3 entries
		_hRdr = createNewReader(TEST_DIR);
		long nbrRecs = 3L;
		long ndxSize = 18L;
		long longSize = 8L;
		String rec1 = "TEXT";															// len = 4
		String rec2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";		// len = 26;
		String rec3 = "0123456789";												// len = 10;
		
		// After the file delete, need to create a new HelpReader
		len = _hRdr.createIndex(nbrRecs);							// 3 index slots + 8L =  62
		// Each record is 18 bytes long + 8 byte header record
		assertTrue(len == nbrRecs * ndxSize + longSize);
		try {
			// Entry 1
			assertTrue(_hRdr.writeEntry("KEY", rec1));		
			// Entry 2
			assertTrue(_hRdr.writeEntry("Alphabet", rec2));
			// Entry 3
			assertTrue(_hRdr.writeEntry("Numbers", rec3));
			// Check that all data was written into file: index size (62) plus 3 key fields in
			// data section (30) + 3 data records (40) + 3 terminators (3) = 
			long datalen = len + rec1.length() + rec2.length() + 	rec3.length() + 
						3 * KEY_SIZE + 3;		
			_hRdr.closeFile();		// so we can get the filelength
			long filesize = _mock.getFilesize(BIN_TESTPATH);
			MsgCtrl.msgln("\ttestWriteEntry(): datalen = " + datalen + "\t filesize = " + filesize);
			assertTrue(filesize == datalen);
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\ttestWriteEntry: " + ex.getMessage());
		}

		// Test that the file was created, then remove it for next test
		assertTrue(_mock.isExists(BIN_TESTPATH));
	}	

	
	/** Tests the closeFile and verify methods
	 * 
	 * <code>HelpContentHandler</code> callback.
	 */
	public void testCloseFile()
	{
        MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln("\ntestCloseFile(): ");
		// Case: Close an empty file normally (the one setup() created)
		MsgCtrl.msg("\tClosing 1");
		MsgCtrl.msgln("\tExpected error message... ");
		assertFalse(_hRdr.closeFile());
		
		// Case: Close the file after it is already closed
		MsgCtrl.msg("\tClosing 2");
		MsgCtrl.msgln("\tExpected error message... ");
		assertFalse(_hRdr.closeFile());
		
		// Case: Write a few entries, then close the file
		_hRdr = createNewReader(TEST_DIR);
		long indexLen = _hRdr.createIndex(3);
		assertTrue(indexLen == 62L);
		String rec1 = "TEXT";															// len = 4
		String rec2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";		// len = 26;
		String rec3 = "0123456789";												// len = 10;
		try {
			_hRdr.writeEntry("Key1", rec1);
			_hRdr.writeEntry("Key2", rec2);
			_hRdr.writeEntry("Key3", rec3);
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\ttestCloseFile(): " + ex.getMessage());
		}
		MsgCtrl.msg("\tClosing 3");
		assertTrue(_hRdr.closeFile());
	}
	
	
//	/** Parse and load the xml file into a binary help file, using the standard SAX parser */
//	public void testLoad()
//	{
//		MsgCtrl.setSuppression(false);
//		MsgCtrl.msgln("testLoad(): ");
//		// Load the XML test file into a new bin help file
//		try {
//			_hRdr.load(XML_PATH, BIN_PATH);
//			long len = _mock.getFilesize(BIN_PATH);
//			MsgCtrl.msgln("\ttestLoad(): bin help file created with size " + len);
//			// Exception occurs if file cannot be created
//		} catch (IOException ex) {
//			MsgCtrl.errMsgln("\tIOException thrown trying to load HelpReader:  " 
//						+ ex.getMessage());
//			fail();
//		}
//		MsgCtrl.setSuppression(true);
//	}
	
	
	/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
	 * 														PRIVATE METHODS 
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

	/** Create another HelpReader file; useful for <code>setUp()</code>
	 * @param dir		folder containing fname and binary o/p file (.hlp).
	 */
	private HelpReader createNewReader(String dir)
	{
		HelpReader reader = null;
		// Creating the HelpReader can throw a size-related IOException
		try {
			reader = new HelpReader(BIN_TESTPATH, HelpReader.READWRITE);
			assertNotNull(reader);
			// Exception occurs if file cannot be created
		} catch (FileNotFoundException ex) {
			MsgCtrl.errMsgln("\tFileNotFoundException thrown trying to create HelpReader:  " 
							+ ex.getMessage());
			fail();
		}
		return reader;
	}


	/** Populate the index and text record section with test data to setup for testing.
	 * Exercises the callback method <code>createIndex()</code>. 
	 * 
	 * @param maxEntries		the max allowed number of records (index capacity)
	 * @param nbrEntries		the number of test records written
	 * @return the number of bytes in the index
	 */
	private long populate(int maxEntries, int nbrEntries)
	{
		// Repeated test string, appended with record number later
		final String  testString = "Test string to put its position into index slot ";
		long indexSize = NOT_FOUND;
		try {
			// Clear out the index by truncating the existing file
			_hRdr.setLength(0L);
			// Create the index for writing
			indexSize = _hRdr.createIndex(maxEntries);
			
			// Add all records sequentially and verify that the next slot is empty
			for (int k=0; k < nbrEntries; k++) {
				String testKey = "Test " + k;
				String testEntry = testString + k;
				_hRdr.writeEntry(testKey, testEntry);
				// Stop when index is full
				if (_mock.getNextEmptySlot() == INDEX_FULL) {
					break;
				}
			}
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\ttestgetNextEmptySlot(): " + ex.getMessage());
			fail();
		}
		// Get the number of bytes used for index space
		return indexSize;
	}
	
	
}		// end of TestHelpReader class

