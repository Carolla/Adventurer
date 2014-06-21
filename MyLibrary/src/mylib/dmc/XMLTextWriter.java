/**
 * XMLTextWriter.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.dmc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Writes the Person data to an XML output file for later.
 * <p>
 * Implementation Note: This class uses a direct file write approach instead of using the 
 * StAX package. The StAX package is preferable because it is a direct non-memory-intensive 
 * package, but it is available only in Java SDK 6.0, which is not yet available for the Mac 
 * OS X platform.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Sep 1   2008   // original <DD>
 * <DT> Build 1.1		Feb 23 2009   // adapted for Adventurer and put into library <DD>
 * </DL>
 */
public class XMLTextWriter
{	
	// Internal constants, mostly collected here to fix them to avoid typos
	final private String XML_HEADER = "<?xml version=\"1.0\"?> ";
	final private String START_TAG = "<";
	final private String END_TAG = ">";
	final private String END_ELEMENT = "</";
	final private String QUOTE = "\"";
	final private String IMPLICIT_CLOSE = QUOTE + "/>";		// used for closing single-line elements
	final private String START_ASSIGN = "=\"";
	final private String END_ASSIGN = QUOTE+END_TAG;
	
	// Other constants
	/** The length of a human-readable block of text in the XML output file. */
	static final private int LINE_LENGTH = 80;
	static final private int LENGTH_ERROR = -1;
	static final private int NOT_FOUND = -1;
	static final private String EOL = "\n";
	/** The character that denotes a word-wrapped newline. The writer adds it on when
	 * it breaks the line by adding a newline character; the ContentHandler takes it back
	 * off. Text is wrapped so the XML file can be human-read. */
	static final private String WORD_WRAP = "~";		
	
	// Internal references
	private PrintWriter _pw = null;

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

	/** Default constructor */
	public XMLTextWriter() { }
	
	/** Create a PrintWriter at the specified filepath (autoflush mode) 
	 * @param longFileName		the filename prepended with the directory path
	 */
	public XMLTextWriter(String longFileName)
	{
		try {
			_pw= new PrintWriter(new FileWriter(longFileName), true);	
			_pw.println(XML_HEADER);
		}
		catch (IOException ex) {
			System.err.println("XMLTextWriter ctor exception: " + ex.getMessage());
		}
	}

	
	/** Verify that the print writer is still not null after ctor is called
	 * @return null if printWriter not created.
	 */
	public PrintWriter getPrintWriter()
	{
		return _pw;
	}


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 									PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	

	/** Write an XML element to specify a group forthcoming. This is merely a wrapper for
	 * <code>startElement()</code> method with a more readable name.
	 * This group tag does NOT contain an attribute.
	 * 
	 * @param level		of indentation for readability
	 * @param tagName	the name to be enclosed in angle brackets
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 * @param attName	name of tag attribute to include with tag
	 * @param attValue	name of tag attribute to include with tag
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 */
	public void startGroup(int level, String tagName, String attName, String attValue) 
					throws IOException
	{
		startElement(level, tagName, attName, attValue);
	}

	
	/** Write an XML end-element tag for a group. This is merely a wrapper for
	 * <code>endElement()</code> method with a more readable name. 
	 * 
	 * @param level		of indentation for readability
	 * @param tagName	the name to be enclosed in angle brackets
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 */
	public void endGroup(int level, String tagName) throws IOException
	{
		endElement(level, tagName);
	}
	
	
	/** Write an XML element without a following attribute to the output file. 
	 * The string will be enclosed in tag and element syntax.
	 * This (overloaded) method overrides <code>writeStartElement</code method with 
	 * an attribute.
	 * 
	 * @param level		of indentation for readability
	 * @param tagName	the name to be enclosed in angle brackets
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 */
	public void startElement(int level, String tagName) throws IOException
	{
		indent(level);
		_pw.println(START_TAG + tagName + END_TAG);
	}


	/** Write an XML element with a following attribute to the output file. 
	 * The string will be enclosed in tag and element syntax.
	 * This (overloaded) method overrides <code>writeStartElement</code method without
	 * an attribute.
	 * 
	 * @param level		of indentation for readability
	 * @param tagName	the name to be enclosed in angle brackets
	 * @param attName	name of tag attribute to include with tag
	 * @param attValue	name of tag attribute to include with tag
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 */
	public void startElement(int level, String tagName, String attName, String attValue) 
				throws IOException
	{
		indent(level);
		_pw.println(START_TAG + tagName + " " + attName + START_ASSIGN 
							+ attValue + END_ASSIGN);
	}
	
	
	/** Write an XML element with a following attribute and its end tag to the output file. 
	 * The string will be enclosed in tag and element syntax.
	 * This method overrides <code>writeStartElement</code method without an attribute.
	 * 
	 * @param level		of indentation for readability
	 * @param tagName	the name to be enclosed in angle brackets
	 * @param attName	name of tag attribute to include with tag
	 * @param attValue	value to include with tag
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 */
	public void singleLine(int level, String tagName, String attName, String attValue) 
				throws IOException
	{
		indent(level);
		_pw.println(START_TAG + tagName + " " + attName + START_ASSIGN 
							+ attValue + IMPLICIT_CLOSE);
	}
	
	
	/** Write an XML end-element tag 
	 * 
	 * @param level		of indentation for readability
	 * @param tagName	the name to be enclosed in angle brackets
	 * @throws IOException which should pass through the PDC and be caught at the HIC
	 */
	public void endElement(int level, String tagName) throws IOException
	{
		indent(level);
		_pw.println(END_ELEMENT + tagName + END_TAG);
	}
	
	
	/** Write one or more lines of text without tags.
	 * The text is not indented but is word-wrapped at a word-boundary if it exceeds 
	 * the LINE_LENGTH. Word-wrapping assumes that there are no newlines in the text;
	 * any newlines found will wrap the text at that position. 
	 * 
	 * @param textOut  actual text to write to the line output
	 * @return number of bytes written
	 */
	public int writeText(String textOut)
	{
		int bytesOut = 0;
		
		// Long lines are converted to elements of a String array where user-entered newlines
		// (EOL)  acted as delimiter. 
		// The <code>split()</code> method breaks these into individual strings; 
		// 0 arg means repeat the split process for entire textOut, creating an indefinite list of
		// Strings of indefinite length.
		String[] lines = textOut.split(EOL, 0);
		for (int k=0; k < lines.length; k++) {
			// Each line now contains no EOL characters
			// If short enough, write the line out
			if (lines[k].length() < LINE_LENGTH) {
				bytesOut += oneOut(lines[k]);
			}
			// Case of long lines with no EOL
			else {
				// Loop until the white space found exceeds the LINE_LENGTH
				bytesOut += multiOut(lines[k]);
			}	
		}	// end lines[k] loop
			return bytesOut;
	}


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 									PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */	

	/** Write a single line of text out
	 * @param line of text to write out
	 * @return the number of bytes written
	 */
	private int oneOut(String line) 
	{
		int len = line.length();
		if (len > LINE_LENGTH) {
			System.err.println("Text out exceeds Line Length: " + line);
			return LENGTH_ERROR;
		}
		else {
			_pw.println(line);
//			System.out.println("[" + len + "] " + line);
			return len;
		}
	}

	
	/** Copy a line of text, word by word, into a receiving string until it is just inside the allowed 
	 * LINE_LENGTH, then add a WORD_WRAP sequence and write it out.
	 * 
	 * @param line of text to write out, which is overly long and must be word-wrapped; 
	 * 			it should not contain any newline characters
	 * @return the number of bytes written
	 */
	private int multiOut(String line) 
	{
		StringBuilder recvr = new StringBuilder();
		int outlen = 0;
		int startPos = 0;
		int wrapNdx = 0;
		// Loop through long line, searching for legal-length wrap points
		// Move the immutable source string to a StringBuilder for easier manipulation
		String src = line;
		while (true) {
			// Look for last white space before the end of line, allowing for wrapping characters
//			System.err.println("src =  " + src);
			// Reset the cursor to beginning of line
			startPos = 0;
			wrapNdx = src.lastIndexOf(" ", LINE_LENGTH-3);
			if ((wrapNdx == NOT_FOUND) || (src.length() < LINE_LENGTH)) {
				// Write the remnant of the line
				outlen += oneOut(src.substring(startPos));
				break;
			}
			// Add the text before that point into a receiving buffer
			recvr.append(src.substring(startPos, wrapNdx));
			// Add on the word wrap sequence and write it out
			recvr.append(WORD_WRAP);
			outlen += oneOut(new String(recvr));
			// Shove remaining text in source test to front and clear backend
			src = src.substring(wrapNdx, src.length());
			// Clear receiving buffer
			recvr.delete(0, recvr.length());
		}
		return outlen;
	}

	
	/** Indent the XML tags the designated level by repeated tab characters
	 * @param level 	the number of tab characters to indent
	 */
	private void indent(int level)
	{
		for (int k=0; k < level; k++) {
			_pw.print("\t");
		}
	}

	
}	// end of XMLTextWriter class
