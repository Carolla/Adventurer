/**
 * HelpReader.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.dmc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import mylib.MsgCtrl;
import mylib.pdc.HelpEngine;


/**
 * Reads and writes random records to a binary file using an index for faster access and
 * to support variable-length text. The file is formatted into three sections:
 * <OL>
 * <LI> <i>MaxEntries</i>: The total number of entries possible in the binary file, which is 
 * 			equal to the <i>capacity</i> of the index. 
 * 			Field size: <code>long</code> datatype. </LI>
 * <LI> <i>Index</i>: A fixed-length table, with each <i>slot</i> (element) containing a 
 * 			fixed-length key (<code>String</code>) and a fixed-length file position 
 * 			(<code>long</code>) of that key's <i>text entry</i>. </LI>
 * <LI> <i>Text entry</i>: A fixed-length key equal to that of the index (for verification),  
 * 			a variable-length and optional <code>String</code> <i>label</i>, followed by a
 * 			variable-length <code>String</code> that contains the actual text for the file. </LI>
 * </OL>
 * Implementation Note:  For simplicity, many methods assume that the text entries are 
 * appended to the end of the file so that the index position can be written before the text 
 * entry is written. The entry position and the index position value will always equal the file 
 * length before writing. <br>
 * The lower level methods (private scope) throw IOExceptions that must be caught by the 
 * callers; most (but not all) public methods handle their own Exceptions. 
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Dec 23, 2008   // original <DD>
 * <DT> Build 1.1		Dec 29, 2008   //refactored slightly and added verify methods and 
 * 				override <code>close()</code> method.<DD>
 * <DT> Build 1.2		Jul 21 2009 		// repackaged to be more app-independent<DD> 
 * <DT> Build 2.0		Jul 31 2009 		// streamlined methods for cleaner design <DD> 
 * </DL>
 */
public class HelpReader extends RandomAccessFile
{
//    // Standard directories and filename extensions 
//    /** Default Help directory is in the common library resources folder */
//    private final String LIBHELP_DIRECTORY = 
//                "/Projects/workspace/MyLibrary/src/mylib/libResources/";
    // Standard directories and filename extensions 
    /** Default Help directory is in the common library resources folder */
    private final String LIBHELP_DIRECTORY = 
                "mylib/libResources/";

	/** All Help files have the same XSD Schema for Help validation, so the schema
	 * is placed in the common library resources folder */
	private final String HELP_SCHEMA = LIBHELP_DIRECTORY + "help.xsd";

	/** Internal constants for readability */
	private final long NOT_FOUND = -1L;
	/** Internal constants for readability */
	private final long SIZEOF_LONG = 8L;
	/** Internal constants for readability */
	private final long SIZEOF_BYTE = 1L;
	/** Arbitrary terminator character */
	private final char TERMINATOR = '\n';
	/** Arbitrary fill char for short datas */
	private final byte FILL_BYTE = 0x00;
	/** Internal constants for readability */
	private final long INDEX_FULL = -2L;

	/** Placeholder byte positions in index */
	private final long EMPTY_SLOT = -1L;
	/** Placeholder character used to create empty index slot key */
	private final String EMPTY_KEY= "..........";								
	/** Size of key field in chars, equal to twice that in zero-prefixed bytes*/
	private final int KEY_SIZE = 10;
	/** Size of repeated index fields:  */
//	private final long INDEX_REC_SIZE = KEY_SIZE * SIZEOF_BYTE + SIZEOF_LONG;
	private final long INDEX_REC_SIZE = KEY_SIZE + SIZEOF_LONG;
	
	/** Mode when creating help file to output binary */
	static public final String READWRITE = "rw";
	/** Mode when getting help from binary help file */
	static public final String READONLY = "r";

	/** Internal reference for client use */
	static private HelpReader _helpRdr = null; 
	
	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 					CONSTRUCTOR AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** Creates a binary indexed random access output file in ReadWrite mode. (Later it is 
	 * opened for random access reading in ReadOnly mode.) The file will automatically 
	 * extend as text is appended (but never go past the fixed-size index). <br>  
	 * Provides all callbacks for the <code>HelpContentHandler</code>, an implementation 
	 * of the SAXParser's <code>ContentHandler interface</code>. 
	 * <p>
	 * Implementation Note: The <code>RandomAccessFile</code> constructor that takes 
	 * a <code>File</code> object is used instead of the <code>String</code>-version 
	 * constructor because various file management operations are required that are not 
	 * available with the String version of the constructor. 
 	 *
	 * @param pathName	full path file name of the binary help file to be created
	 * @param mode			read/write or read-only mode for the file
	 * @throws FileNotFoundException if file passed to the base class constructor cannot
	 * 				 be created at the given pathName
	 */
	public HelpReader(String pathName, String mode) throws FileNotFoundException
	{ 
		super(new File(pathName), mode);			// super is required to be first statement
		MsgCtrl.msgln("\tHelpReader ctor: ");
		// Reset file pointer to top of file
		try {
			// Reset the file pointer to the top
			seek(0L);
			// Exception thrown if length is invalid
		} catch (FileNotFoundException fnfex) {
			MsgCtrl.errMsgln("\tHelpReader FileNotFoundException: " + fnfex.getMessage());
		} catch (IllegalArgumentException iaex) {
			MsgCtrl.errMsgln("\tHelpReader IllegalArgumentException: " + iaex.getMessage());
		} catch (IOException ex) {
			MsgCtrl.errMsg("\tHelpReader IOException: " + ex.getMessage());
			try {
				MsgCtrl.errMsgln("\t" + length() + " bytes");
			} catch(IOException ex2) {
				// Do nothing 
			}
		}
		_helpRdr = this;
	}
		
	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 					CALLBACK METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
			
	/**
	 * Callback method for <code>HelpContentHandler</code>. 
	 * <p> 
	 * Creates an index at the start of a random access file with all slots for entries to be 
	 * inserted later. Each index record contains a <i>slot</i> for each help record. 
	 * A <i>slot</i> is a pair of fields containing the help key and the file position of the text
	 * written for that key at the end of the file.
	 * The total number of slots is the index <i>capacity</i>, and is  defined by the 
	 * <code>maxEntries</code> attribute of the <code>authorhelp</code> tag in the XML
	 * file. 
	 * <p>
	 * Implementation Note: This method is NOT part of the constructor, but a callback 
	 * method intended for and called by the SAX parser's <code>HelpContentHandler</code>.
	 * 
	 * @param maxEntries	the total number of records permitted, sets the fixed-size index
	 * @return the number of bytes written to the file
	 * 
	 * @see HelpContentHandler
	 * @see #getCapacity() 
	 */
	public long createIndex(long maxEntries)
	{
		MsgCtrl.msgln("HelpReader.createIndex(): ");
		// Guard against zero or negative entries
		if (maxEntries <= 0) {
			return 0;
		}
		long bytesOut = 0L;
		// Write table size record and index table to the top of the binary file
		try {
			// Set the index table size as first record 
			seek(0L);
			writeLong(maxEntries);
			// Write two fixed-fields as slot placeholder in the index 
			for (int k=0; k < maxEntries; k++) {
				writeFixedField(EMPTY_KEY, KEY_SIZE);		
				// Second index field contains byte position of text in file, for now, a placeholder
				writeLong(EMPTY_SLOT);				
			}
			bytesOut = getFilePointer();				
		} catch (IOException ex) {
				MsgCtrl.errMsg("HelpReader.createIndex(): " + ex.getMessage());
				return EMPTY_SLOT;
		}
		return bytesOut;
	}


	/** Creates a SAXStream parser and loads the contents of the XML file into a binary
	 * indexed random access file. 
 	 *
	 * @param xmlFile	(.xml) contains the input text data in XML format for the help text
	 * @return true for a successful load
	 * @throws IOException if SAXStream could not be created
	 */
	public boolean load(String xmlFile) throws IOException
	{
		MsgCtrl.msgln("HelpReader.load(): Creating SAX Stream Parser.");
		// Create the application-specific XML Content Handler, and pass this object because 
		//		HelpReader contains the callback methods
		HelpContentHandler hch = new HelpContentHandler(_helpRdr); 
		// Create a SAX Stream Parser, which includes the XML Schema validator
		SAXStream sst = new SAXStream(xmlFile, HELP_SCHEMA, hch);	
        // if (sst == null) { IMPORTANT: removed "dead code" TAA (03/17/2013)
        //     throw new IOException("\tHelpReader.load(): SAX Stream Parser creation failed.");
        // }
	    // Read the XML data files and parse data into objects
       boolean parseDone = sst.parseSAXStream();
       if (parseDone == true) {
    	   MsgCtrl.msgln("HelpReader.load(): SAXStream validated and parsed.");
       }
       else {
    	   MsgCtrl.errMsgln("HelpReader.load(): Problem parsing or validating SAXStream.");
       }
       return parseDone;
	}

	
	/** 
	 * Callback method for <code>HelpContentHandler</code>. 
	 * <p> 
	 * Writes a fixed-field text entry to the binary file, in two steps.
	 * <OL>
	 * <LI> Search the index for the next empty slot; then put the given key and position of 
	 * the text entry into the index (overwrite the fixed-length placeholder). This method 
	 * assumes, and depends on, the text entry being appended to the end of the file. </LI>
	 * <LI> Append both the fixed-length key and the variable-length text entry to the end of 
	 * 	the file. During later retrieval, the key at the front of the entry is used to verify that the
	 * correct record was found. </LI>
	 * </OL>
	 * The append assumption allows the index position to be written equal to the file length 
	 * before the actual text record is written; i.e. the file pointer does not have to calculate 
	 * the forward and backward positions for each write, and subsequently saves an extra
	 * reverse <i>seek</i> operation.
	 * <p>
	 * This method is a wrapper for the <code>writeToIndex()</code> and 
	 * <code>writeRecord()</code> methods.  
	 * 
	 * @param key	the KEY_SIZE identifier for the text entry, written into the index and to the
	 * 			front of the text entry
	 * @param text	the entry of variable length
	 * @return the file pointer, reset to the end of the last text entry (ready to write next entry)
	 * @throws IOException, passed from RandomAccessFile back to caller
	 */
	public boolean writeEntry(String key, String text) throws IOException
	{
		MsgCtrl.msgln("HelpReader.writeEntry(): ");
		// Guard against invalid key length
		if (key.length() > KEY_SIZE) {
			MsgCtrl.errMsgln("\twriteEntry(): Oversized help key ID encountered.");
			return false;
		}
		// Find the max table size of the index
		int maxEntries = getCapacity();
		if (maxEntries <= 0) {
			return false;
		}

		// Step 1:  Write helpID and file position of the text record (which is the current file 
		// length) into first open slot in index
		if (writeToIndex(key) == false) {
			MsgCtrl.errMsgln("\twriteEntry(): Cannot update the file index.");
			return false;
		}
		// Step 2:  Append helpID + text entry record to file
		if (writeRecord(key, text) == false) {
			MsgCtrl.errMsgln("\twriteEntry(): Cannot append the text to the file.");
			// Write failed, so remove the last, now erroneous key, from the index
			removeLastKey(maxEntries * INDEX_REC_SIZE);			
			return false;
		}
		// Both index and text record was written, return all-ok
		return true;
	}


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *		PUBLIC METHODS (not Callbacks)
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** 
	 * Closes the file after verifying the file's internal structure and completeness;
	 * calls <code>finalVerify()</code>.
	 * If the verification fails, a message is displayed for informational purposes.
	 * Regardless of verification results, the file will be closed.
	 * 
	 * @return false	if the internal structure or completeness check failed.
	 * @see #finalVerify()
	 */
	public boolean closeFile()
	{
		MsgCtrl.msgln("\tHelpReader.closeFile(): ");
		boolean status = true;
		// Guard: against file already being closed
		// Try reading from it first; it will throw an exception if it closed
		try {
			seek(0L);
			readLong();
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\tHelpReader.closeFile(): File already closed or not available: " 
							+ ex.getMessage());
			return false;
		}
		// Proceed with verification before really closing this HelpReader
		if (finalVerify() == false) {
			MsgCtrl.errMsgln("\tInternal structure or completeness check failed");
			status = false;
		}
		try {
			super.close();
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\tHelpReader.closeFile(): Could not close the HelpReader file: " 
							+ ex.getMessage());
		}
		return status;
	}


	/** Dump the index of the binary file (for debugging) */
	public void dumpIndex()
	{
		MsgCtrl.msgln("\tHelpReader.dumpIndex(): ");
		try {
			// Move to front of index
			seek(0L);
			long maxEntries = _helpRdr.readLong();
			long ndxSize = getIndexSize() + SIZEOF_LONG;
			long filelen = length();
			
			MsgCtrl.msg("\t\tIndex size (entries) = " + maxEntries + " entries; ");
			MsgCtrl.msg(ndxSize + " (bytes)");
			MsgCtrl.msgln("\tFilesize = " + filelen + " bytes");

			byte[] key = new byte[KEY_SIZE]; 
			long recAddress = 0; 
			for (int k=0; k < maxEntries; k++) {
				_helpRdr.read(key);
				String keyStr = new String(key);
				recAddress = _helpRdr.readLong();
				MsgCtrl.msgln("\t\tIndex slot = " + keyStr + " | " + recAddress);
			}
		} catch (Exception ex) {
			MsgCtrl.errMsgln("\tHelpReader.dumpIndex(): " + ex.getMessage());
		}
	}


	/** This is the supervisor for three primary work methods. It returns the textual help 
	 * description and label for the requested Help ID. It does this in three steps.
	 * <OL>
	 * <LI> Search for the Help ID in the index and retrieve the position of the text entry
	 * associated with that ID. (The Help ID of the GUI widget is mapped to the help text 
	 * description and label in the XML file.) </LI>
	 * <LI> Retrieve the help entry at the specified position after confirming that the help ID
	 * in the record matches that requested (and that of the index).</LI>
	 * <LI> Pull off the delimited label field, which acts as a temporary title for the help
	 * window, and return both the label and the help text in a String array.</LI>
	 * </OL>
	 * Implementation Note: If the user required hardcoded newlines in the XML text, they 
	 * were encoded as tildes ('~') in the binary help file, and are now put back in as 
	 * newlines. 
	 * 
	 * @see HelpEngine
	 * 
	 * @param key	the reference id for retrieving the text
	 * @return 	[0] the short text label, or null if not found; and <br>
	 * 					[1] the help text line without the label, or null if not found.
	 * @throws IOException if the key cannot be found, or the text cannot be returned
	 */
	public String[] getHelpRecord(String key) throws IOException
	{
		MsgCtrl.msgln("\tHelpReader.getHelpRecord(): ");
		// Step 1. Search the index for the help ID and return its associated record position
		long entryPos = getEntryPos(key);
		// Check if the record position was found or somewhat valid
		if ((entryPos == NOT_FOUND) || (entryPos == 0L)) {
			throw new IOException("\tHelpReader.getHelpRecord(): Valid help key not found");
		}
		// Step 2. Confirm the record's help ID matches the index, then return the help 
		//		record, which consists of a label and help text
		String textRec = readTextRecord(key, entryPos);
		if (textRec == null) {
			throw new IOException("\tHelpReader.getHelpRecord(): Text record not found");
		}
		// Step 3. Pull off the label and return it and the text description in a String array
		String[] helpRecord = splitRecord(textRec);
		return helpRecord;
	}

    	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
				
	/** Verifies the internal structure of the output file. It must be true that
	 * <OL>
	 * <LI> The first field, <code>maxEntries</code>, must contain the max number of 
	 * 			entries in the index; that is, must equal <code>getCapacity()</code>. </LI>
	 * <LI> The total number of keys in the index must imply the correct space size of
	 * 			the index; that is, the index must imply <code>getIndexSize()</code>. </LI>
	 * <LI> The key field of the text entry matches the key in the index, and can be found at 
	 * 			the positions indicated in the index </LI>
	 * <LI> The file length in bytes must equal the total bytes for <code>maxEntries</code>, 
	 * 			size of the index, and total size of all text entries. </LI> 
	 * </OL>
	 * This class catches all of its IOExceptions.
	 *  
	 * @return false if any internal inconsistencies are found
	 */
	private boolean finalVerify()
	{
		MsgCtrl.msgln("\tHelpReader.verify(): ");
		// Accumulator for text entry lengths
		long totalTextLength = 0L;
		try {
			// Step 1. The number of entries must reflect the index size
			seek(0L);
			long maxEntries = readLong();
			String keyfield = null;
			int keyCounter = 0;
			do { 
				keyfield = readFixedString(KEY_SIZE);	
				seek(getFilePointer() + SIZEOF_LONG);					//skip the position pointer
				keyCounter++;
			} while ((!keyfield.equals(EMPTY_KEY) && keyCounter < maxEntries));
			// The count should match the value in the maxEntries field
			if (keyCounter != getCapacity()) {
				return false;
			}
			// Step 2: The index's byte size must reflect the number of entries
			long calcNdxSize = maxEntries * INDEX_REC_SIZE + SIZEOF_LONG;
			// Compensate by nbrOfEntries Field, which is not included in the index count
			if (calcNdxSize != getIndexSize() + SIZEOF_LONG) {		
				return false;							
			}
			// Step 3.Traverses the index with field one offset, verifying all text entries and
			// verifying that the index key matches the text record key
			long k = SIZEOF_LONG;
			for (; k < calcNdxSize; k+=INDEX_REC_SIZE) {
				// Advance file ptr one index record
				seek(k);
				// Get the key
				String key = readFixedString(KEY_SIZE);
				// Get the location of that key's position
				long pos = readLong();
				// Get the text entry for that key at the current position; keys are compared
				String entry = readTextRecord(key, pos);			
				if (entry == null) {
					return false;
				}
				else {
					// Accumulate length of text entries
					totalTextLength += entry.length() + 1;			// EOL char included
				}
			}	// end of for loop to traverse index
			// Check that loop was exhausted and ended at the proper boundary
			if (k > calcNdxSize) {
				return false;
			}
			// Step 4. Verifies the file length is as expected by tallying file parts
			long calcLen = calcNdxSize;						// size of index and size field in bytes
			calcLen += maxEntries * KEY_SIZE;				// size of populated text headers
			calcLen += totalTextLength;							// total text length in bytes
			if (calcLen != length()) { 								// file length as written
				return false;
			}
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\tverify(): IOException = " + ex.getMessage());
			return false;
		}
		return true;
	}

	
	/** Returns the maximum number of entries the index can hold (not the <i>size</i> 
	 * of the index, which is the number of  populated entries). <br>
	 * Note: Increments the file poiner.
	 * 
	 * @return one or more entries in the index, or 0 if there is no index
	 */
	private int getCapacity() 
	{
//		MsgCtrl.msgln("\t\tgetCapacity(): ");
		int max = 0;
		try {
			seek(0L);	
			max = (int) readLong();
		} catch (IOException ex) {
			MsgCtrl.errMsgln("\tHelpReader.getCapacity(): " + ex.getMessage());
		}
		return max;
	}

	
	/** Searches the index for a given key and returns the position of the entry with that key.
	 * 
	 * @param key		the identifier, of KEY_SIZE length, used to find the text
	 * @return the file position of a text entry that matches the given key, 
	 * 			else -1 if key not found
	 * @throws IOException if cannot read from the index
	 */
	private long getEntryPos(String key)  throws IOException
	{
		MsgCtrl.msgln("\t\tgetEntryPos(): ");
		// Default return in case of error
		long entryPos = NOT_FOUND;
		// Find the max table size of the index (inc file ptr)
		int maxEntries = getCapacity();							
		
		// Traverse index looking for first empty slot
		long kmax = maxEntries * INDEX_REC_SIZE + SIZEOF_LONG;
		for (long k = SIZEOF_LONG; k < kmax; k+=INDEX_REC_SIZE) {
			// Start at first key field, later advance file ptr one index record
			seek(k);
			String rec = readFixedString(KEY_SIZE);	
 			// If the key and help ID in the index match...
			if (rec.equalsIgnoreCase(key)) {
				// .. read the file pointer position for that record in the next field
				entryPos = readLong();
				break;
			}
		}
		// Record position remains NOT_FOUND if loop exits
		return entryPos;
	}
		
	
	/** Get the size of the index in bytes (do not include the size field in front)
	 * @return the index size in bytes, or 0 
	 */
	private long getIndexSize() 
	{
		MsgCtrl.msgln("\t\tgetIndexSize(): ");
		long ndxSize = NOT_FOUND;
		try {
			// Move to front of file
			seek(0L);
			long maxEntries = readLong();
			// If there are no entries in the index, it probably doesn't yet exist
			ndxSize = (maxEntries == 0) ? NOT_FOUND : maxEntries * INDEX_REC_SIZE;
		} catch (IOException ex) {
				MsgCtrl.errMsgln("\tgetIndexSize(): Problem reading file" + ex.getMessage());
				return NOT_FOUND;
		}
		return ndxSize;
	}
	

	/** Return the first part of the text string, throwing away the delimiters. 
	 * The label must be the first substring of the text, and is delimited in front and back by 
	 * the pipe char ('|'). It will be extracted and serve as the title for the help window. 
	 * 
	 * @param longtext	the text from which to strip the label
	 * @return the label-title, or null if text not found
	 * @throws IOException of the key or label cannot be returned
	 */
	private String getLabel(String longtext) throws IOException
	{
		MsgCtrl.msgln("\t\tgetLabel(): ");
		int start = (int) NOT_FOUND;
		int end = (int) NOT_FOUND;
		final String PIPE = "|";			// indexOf() requires a String arg
		
		// Guard condition against no input parm
		if (longtext == null) {
			return null;
		}
		// Check for existence of label
		start = longtext.indexOf(PIPE);
		end = longtext.indexOf(PIPE, start+1);	
		if ((start == NOT_FOUND) || (end == NOT_FOUND)) {	// could not find delimiter
			return null;
		}
		// If all went well...
		return longtext.substring(start+1, end);	// exclude bracketing delimiters
	}

	
	/** Search the index for the first empty slot, and return the file ptr position of that field.
	 * If there at no more slots available, will return INDEX_FULL
	 * 
	 * @return file pointer position of next empty slot, or INDEX_FULL if none are empty.
	 * 		If there is trouble reading the data, or index has no header entry, returns 
	 * 		NOT_FOUND
	 * @throws IOException if some lower file-level problem occurred
	 */
	private long getNextEmptySlot() throws IOException
	{
		MsgCtrl.msgln("\t\tHelpReader.getNextEmptySlot(): ");
		// Guard: If there is no index, then next empty slot cannot be found
		long ptrPos = NOT_FOUND;
		long maxBytes = getIndexSize();	
		if ((maxBytes == 0) || (maxBytes == NOT_FOUND)) {
			return NOT_FOUND;
		}
		
		// Destination for file data read
		byte[] slot = new byte[KEY_SIZE];
		// Verify that the index contains any records at all
		long k = SIZEOF_LONG;
		// Traverse the index looking for empty slots
		for (; k < maxBytes; k +=INDEX_REC_SIZE) {
			// Advance fileptr one key field and read the next slot...
			seek(k);
			if (read(slot, 0, slot.length) == NOT_FOUND) {
				return NOT_FOUND;
			}
			// Verify if field contains an empty slot (convert to String for the comparison) 
			String slotData = new String(slot);
			if (slotData.equalsIgnoreCase(EMPTY_KEY)) {
				// Step back one key field, and...
				ptrPos = getFilePointer() - (long) (KEY_SIZE * SIZEOF_BYTE);
				return ptrPos;
			}
		}
		// Dropped out of search loop
		ptrPos = (k > 0) ? INDEX_FULL : NOT_FOUND;
		return ptrPos;
	}

	
	/** Reads a fixed-length char field from a random access file.
	 * The index and text entry header are fixed-length fields. Fill bytes are discarded
	 * to reproduce the variable-lengths <code>Strings</code> that were originally written.
	 * 
	 * @param fieldSize 	reads this many chars or until a zero byte is read
	 * @return the text read from the file
	 * @throws IOException if cannot read characters from the file
	 */
	private String readFixedString(int fieldSize) throws IOException
	{
		MsgCtrl.msgln("\t\treadFixedString(): ");
		StringBuilder b = new StringBuilder(fieldSize);
		int k=0;
		boolean more = true;
		// Read while max size not exceeded; break when fill byte encountered
		while (more && k < fieldSize) {
			char ch = (char) readByte();
			k++;
			if (ch == FILL_BYTE) {
				// Advance file ptr to end of field and return
				long inc = KEY_SIZE - b.length() - 1L;
				seek(getFilePointer() + inc);
				more = false;
			}
			else {
				b.append(ch);
			}
		}
		String str = b.toString();
		return str;
	}

	
	/** Retrieves a text entry for a given file pointer position; confirms that the record key
	 * matches the index key given.
	 *  
	* @param key		help ID used for verifying the index with the text record found
	* @param pos		the file pointer position of where the record sits in the file
	* @return	text associated with the key given, else null if not found; can be used
	* 		to compare with correct key when returned 
	* @throws IOException if cannot read from the file
	*/ 
	private String readTextRecord(String key, long pos) throws IOException
	{
		MsgCtrl.msgln("\t\t readTextRecord(): ");
		String textline = null;
		// At the given position is a fixed length field (10 bytes)...
		// ...followed by a delimited label field and an terminated text string
		seek(pos);
		// Retrieve the fixed field text from the found position
		String keyfield = readFixedString(KEY_SIZE);
		// Confirm index and record match
		if (keyfield.equalsIgnoreCase(key)) {
			// Read the entire line (up to newline), which includes the delimited label field
			textline = readLine();
		}
		else {
			MsgCtrl.errMsgln("\t\t\t Text record key does not match given key");
		}
		// No more entries in index
		return textline;
	}

	
	/** Find the last non-empty slot and replace it with an empty slot. 
	 * (This method is usually called to undo an erroneous write statement).
	 * If this operation fails, the integrity of the help file is broken, so an exception is
	 * thrown to be handled by the caller, and a <code>finalVerify()</code> on the file is 
	 * recommended.
	 * <p>
	 * If this method fails, the binary help file probably needs to be recreated by re-reading
	 * the XML input again.
	 * 
	 * @param ndxSize	the size of the index in bytes
	 * @throws IOException
	 */
	private void removeLastKey(long ndxSize) throws IOException 
	{
		MsgCtrl.msgln("\t\tremoveLastKey(): ");
		// Get the first empty field position 
		long curPos = getNextEmptySlot();
		// If the index can't be found, throw an exception
		if (curPos == NOT_FOUND) {
			throw new IOException("\tHelpReader.removeLastKey(): " 
							+ "Cannot undo last erroneous write.");
		}
		// If there are no empty slots, set file pointer to last (populated) position
		if (curPos == INDEX_FULL) {
			curPos = getCapacity() - INDEX_REC_SIZE;
		}
		// Back up one index record and write over the previous slot
		else {
			curPos -= INDEX_REC_SIZE;
		}
		// Overwrite the populated slot with the empty one
		// Overwrite the key and address with the Empty slot placeholder
		writeFixedField(EMPTY_KEY, KEY_SIZE);	
		writeLong(EMPTY_SLOT);			 
	}

	
	/** Extracts the label from the help text record and return it and the text in a String array
	 * @parm textRec	the combined record of label and text
	 * @return 	[0] the short text label, or null if not found; and <br>
	 * 					[1] the help text line without the label, or null if not found.
	 * @throws IOException is something goes wrong
	 */
	private String[] splitRecord(String textRec) throws IOException
	{
		MsgCtrl.msgln("\t\t splitRecord(): ");
		final char TILDE = '~';
		final char NEWLINE = '\n';
		final String PIPE = "|";			// indexOf() requires a String arg

		StringBuilder dest = new StringBuilder();
		String[] helpEntry = new String[2];
		// Extract the label first
		helpEntry[0] = getLabel(textRec);
		MsgCtrl.msgln("\t\t\tLabel = " + helpEntry[0]);
		
		// Get the traversal start and ends
		int start = textRec.lastIndexOf(PIPE)+1 ;			// to account for delimiters
		int len = textRec.length();
		
		// Replace each tilde with a newline into a new array positon
    	for (int index=start; index < len; index++) {
    		// Walk the text string char by char
    		char c = textRec.charAt(index);
    		// If a tilde is found, replace it with a newline
    		if (c == TILDE) {
    			dest.append(NEWLINE);
    		}
    		// else copy the char to the destination 
    		else {
    			dest.append(c);
    		}
		}
    	// Return the two strings in the string array
    	helpEntry[1] = new String(dest);
		return helpEntry;
	}
	
	
	/** Writes a string to a random access file as a fixed-length byte field.
	 * The index and first field in the text entry are fixed-length.<br>
	 * 
	 * @param src 			the string to be written
	 * @param fieldLen 	if the src is too small, zero padding is used
	 * @throws IOException if cannot read the file length or write a character
	 */
	private void writeFixedField(String src, int fieldLen) 
					throws IOException, IndexOutOfBoundsException 
	{
		MsgCtrl.msgln("\t\twriteFixedField(): ");
		// Guard against too large src
		if (src.length() > fieldLen) {
			throw new IndexOutOfBoundsException ("\tHelpReader.writeFixedField: " +
							"src larger than fieldlen specifed");
		}
		// Prepare destination buffers
		byte[] b = new byte[fieldLen];
		// Copy the src into the buffer
		for (int k=0; k < src.length(); k++) {
			b[k] = (byte) src.charAt(k);
		}
		// If the field too short, fill in with fill bytes
		for (int k=src.length(); k < fieldLen; k++) {
			b[k] = FILL_BYTE;
		}
		
		// Write the fixed byte array to the current position
		write(b);
	}
	
		
	/** Write text record into the bottom part of the file (index has already been updated)
	 * Move the file pointer to end of file; then write the help key and text into the record
	 * 
	 * @param key		the helpID used to identify the text (also in index)
	 * @param helpText  the text data to be written
	 * @return true if help succeeded
	 * @throws IOException
	 */
	private boolean writeRecord(String key, String helpText) throws IOException
	{
		MsgCtrl.msgln("\t\twriteRecord(): ");
		long ptrPos = length();
		seek(ptrPos);								// move to the same position as just put in index
		writeFixedField(key, KEY_SIZE);
		writeBytes(helpText);				// write the indefinite-length text entry 
		writeByte(TERMINATOR);			// add the terminator character
		return true;
	}
	

	/** Search the index for the next empty slot; then put the given key and position of 
	 * the text entry into the index (overwrite the placeholder). This method assumes, and 
	 * depends on, the next entry being appended to the end of the file.
	 *  
	 * @param helpID	the key that associates the index slot with the text entry in the file
	 * @return true of all writes succeeded
	 * @throws IOException
	 */
	private boolean writeToIndex(String helpID) throws IOException
	{	
		MsgCtrl.msgln("\t\twriteToIndex(): ");
		// Set the current file ptr postion to the first empty slot
		long ptrPos = getNextEmptySlot();
		// If the index can't be found, throw an exception
		if (ptrPos == INDEX_FULL) {
			throw new IOException("HelpReader.writeToIndex(): Cannot write to full index");
		}
		// OK to proceed, and write the key field and data record position
		if (ptrPos != NOT_FOUND) {
			// Step back one key field, and...
			seek(getFilePointer() - (long) (KEY_SIZE * SIZEOF_BYTE));
			// ...write the fixed-field key into the index as the key...
			writeFixedField(helpID, KEY_SIZE);
			// ...and then write the position of the text entry about to be written
			ptrPos = length();				// save this index value for the text entry
			writeLong(ptrPos);			
		}
		// Index succesfully updated	
		return true;
	}

	
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 				INNER CLASS FOR TESTING: MockHelpReader
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

/** This class is called by JUnit to check lower level private methods;
 * each of these methods are only public wrappers.*/
public class MockHelpReader
{
	/** Default constructor */
	public MockHelpReader() { }

	
	/** Delete the requested file from storage
	 * @param fname name of file to delete
	 * @return true if it is gone; else false
	 */
	public boolean delete(String fname)
	{
		File ser = new File(fname);
		return ser.delete();
	}
	
	/** Wrapper for HelpReader's private method 
	 * @return the number of max number of entries in the help file */
	public long getCapacity()
	{
		return HelpReader.this.getCapacity();
	}
	
	/** Get the size of the requested file, in bytes
	 * @param filename of target 
	 * @return number of bytes of file
	 */
	public long getFilesize(String filename)
	{
		File ser = new File(filename);
		return ser.length();
	}
	
	/** Get the index record fixed constant 
	 * @return the size of an individual index record (in bytes)
	 */
	public long getIndexRecSize()
	{
		return HelpReader.this.INDEX_REC_SIZE;
	}

	/** Get the number of bytes used for the index, populated or not
	 * @return the size of the index (in bytes) 
	 * @throws IOException on error 
	 */
	public long getIndexSize() throws IOException
	{
		return HelpReader.this.getIndexSize();
	}

	/** Test the index search method
	 * @return file position of first empty slot (or NOT_FOUND)
	 */
	public long getNextEmptySlot()
	{
		long pos = NOT_FOUND;
		try {
			pos = HelpReader.this.getNextEmptySlot();
		} catch (IOException ex) {
			MsgCtrl.errMsgln("MockHelpReader.searchIndex(): " + ex.getMessage());
		}
		return pos;
	}

	/** Check if the file exists or not
	 * @param filename of file to check for existence
	 * @return true if it does exist; else false
	 */
	public boolean isExists(String filename)
	{
		File ser = new File(filename);
		return ser.exists();
	}
	
	/** Remove the last populated entry of the index by overwriting it with an empty slot	
	 * @throws IOException on error
	 */
	public void removeLastKey() throws IOException
	{
		HelpReader.this.removeLastKey(getIndexSize());
	}
	
	
}	// end of MockHelpReader inner class

	
}	// end of HelpReader class

