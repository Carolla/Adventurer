/**
 * HelpContentHandler.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package mylib.dmc;

import java.io.IOException;

import mylib.MsgCtrl;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Implements the<code> SAXStream's ContentHandler</code> interface, and 
 * contains a sequence of application-independent but help-based <i>callback</i> methods
 * for the SAXStream parser-validator that reads Help XML data file and the Help XSD 
 * schema file. The text in the help file is application-<i>dependent</i>.   
 * <P>
 * The format for the Help XML file is in four parts:
 * <OL>
 * <LI> Total number of entries in this file, which dictates how large to make the index in
 * the <code>Help Reader</code>. </LI>
 * <LI> The helpID is the key used in the index and precedes the text entry as a check on
 * reading the correct file position. The helpID or key must match the tags in the XML file
 * and the labels in the widgets. </LI>
 * <LI> An optional <i>label</i> string, enclosed within pipe ("|") delimiters, is at the front of 
 * the text entry. The label string is presented and set as the title of the 
 * <code>HelpDialog</code>.</LI>
 * <LI> The actual text of the help message, merely a text block. All newlines, tabs, and 
 * multiple white spaces are stripped from the text block when it is stored in the file. If a
 * a tilde (~) is encountered, then it is assumed that the user specifically wants a newline
 * at that position; the tilde is replaced with a newline character before it is displayed. </LI>
 * </OL>
 *
 * @see SAXStream
 * @see HelpReader
 * 
 *	@author Alan Cline 
 * @version <DL>
 * <DT> 1.1 	Nov 22 2008 		// Specifics revised for this program<DD>
 * <DT> 1.1 	Dec 16 2008 		// Made less application-specific and the 
 * 				<code>HelpEngine</code> more reusable. <DD>
 * <DT> 1.2 	Jan 19 2009  		// Added <code>MsgCtrl</code> messaging and passed 
 * 				errorMsgs to the SAXException mechanism to be caught by callers. <DD>
 * <DT> 1.3 	Jul 21 2009  		// Minor realignment for repackaging done for other	
 * 				classes. <DD>
 * <DT> 2.0 	Dec 28 2009  		// Replaced messaging with MsgtCtrl statements <DD>
 * </DL>
 */
public class HelpContentHandler implements ContentHandler
{
	/** The following constants map the XML tags to the widgets in the GUI.
	 * Verify that the XML and XSD tags match these constant values.
	 */
	private  final String HELPGROUPS = "help_groups";			// Help file group tag
	private  final String HELPTEXT = "helptext";					// Help text block tag

	/** Internal attribute: Holds text from input parsing */
    private StringBuilder _text;

	/** Internal attributeL Name of the textblock being written */
	private String _helpID = null;

    /** XML text input file to binary output file converter, encapsulates file operations */
    private HelpReader _helpRdr = null;
    

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** Create the object that processes the XML input file
     * @param callbackOwner		HelpReader object that contains the callback methods
     */
    public HelpContentHandler(HelpReader callbackOwner) 
    {
    	MsgCtrl.msgln("HelpContentHandler()");
    	_helpRdr = callbackOwner;
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */      
        
    /**
     * Creates a <code>HelpReader</code> which saves the help text to a file using helper
     * callbacks methods. 
     *  
     * @throws SAXException found by SAX parser 
    */
    public void startDocument() throws SAXException 
    {
     	MsgCtrl.msgln("\tstartDocument(): Creating the help file.");
    }

    
    /**
     * Implements <code>SAX.ContentHandler.startElement()</code>,
     * which creates the binary RandomAccessFile via the <code>HelpReader</code>. 
     * The <code>startElement()</code> attributes specify the max number of entries 
     * allowed in the text file, and the random file creates an empty index with that many 
     * help entry slots.  
     * <p>
     * This method is an elaborate switch-case based on XML tags.
     *  
     * @param uri       universal resource locator -- not used
     * @param localName often the same as the element tag name -- not used
     * @param qName     the name of the element tag 
     * @param atts      the attributes defined and found for the element
     * @throws SAXException on invalid format: either not-well-formed XML file or 
     * 			noncompliant with the XSD Schema file. 
     */
    public void startElement(String uri, String localName, String qName, Attributes atts) 
                throws SAXException 
    {
        // Clear _text from previous element
        _text = null;
        
        // START THE HELP GROUP
        if (HELPGROUPS.equals(qName)) {
        	MsgCtrl.msgln("\tstartElement():" + "Start <" + HELPGROUPS + "> ... ");
        	// Open a random binary file to capture the helptext
            // Create the help file with the maxEntries number of indices given in the XML file
        	long maxEntries = Long.parseLong(atts.getValue(0));
        	if (_helpRdr.createIndex(maxEntries) <= 0L) {
       	       	MsgCtrl.errMsgln("\tstartElement(): Couldn't create the help index.");
       		}
        }
        else if (HELPTEXT.equals(qName)) {
   	       	// Save for writing text block later
   	       	_helpID = atts.getValue(0);
   	       	MsgCtrl.msgln("\tstartElement(): " + _helpID);
        }
        else {
        	String errMsg = "Unknown start tag found: ";
   	       	MsgCtrl.errMsgln("\tstartElement(): " + errMsg + qName);
      	  	throw new SAXException(errMsg);
        }
    }		// end of startElement method

    
    /**
     * Implements <code>SAX.ContentHandler.endElement()</code>,
     * which processes an object when its data block <code>endElement</code> tag is found.
     * In general, each text block is written to the HelpReader (random access file) and the 
     * file index entry is updated.
     * <p>
     * This method is an elaborate switch-case base based on XML tags.
     * 
     * @param uri       			universal resource locater -- not used
     * @param localName 	often the same as the element tag name -- not used
     * @param qName     	the name of the element tag
     * @throws	SAXException found by SAX parser 
     */
    public void endElement(String uri, String localName, String qName) throws SAXException 
    {
        // END THE HELP GROUP
        // Help group end tag ends the document file  
        if (HELPGROUPS.equals(qName.toLowerCase())) {
        	MsgCtrl.msgln("\tendElement(): End </" + HELPGROUPS + ">\t ");
        	try {
            	MsgCtrl.msgln("\tendElement(): " + _helpRdr.length() + " bytes written");
        	} catch (IOException ex) {
            	MsgCtrl.errMsgln("\tendElement(): " + ex.getMessage());
        		throw new SAXException(ex.getMessage());
        	}
        }
        else if (HELPTEXT.equals(qName)) {
        	// Write the text buffer as a string to the binary output file
   	       	try {
   	       		// Remove whitespace from help text after text block is read
   	       		String textblock = joinLines(_text);
   	   	       	if (_helpRdr.writeEntry(_helpID, textblock) == false) {
   	   	       		String errMsg = "Error writing text to help file.";
   	   	       		MsgCtrl.errMsgln("\tendElement(): " + errMsg); 
   	   	       		throw new SAXException(errMsg);
   	   	       	}
   	       	} catch (IOException ex) {
   	       		String expMsg = "Trouble writing text block to binary file";
   	       		MsgCtrl.errMsgln("\tendElement(): " + expMsg); 
   	       		throw new SAXException(expMsg);
   	       	}
   	       	MsgCtrl.msgln("\tendElement(): End </" + HELPTEXT + ">");
        }
        else {
        	String expMsg = "\nUnknown end tag found: " + qName;
   	       	MsgCtrl.msgln("\tendElement(): " + expMsg);
   	       	throw new SAXException(expMsg);
          }

        // Clear any _text fields for next use
        _text = null;
    }	// end of endElement method

    
    /** 
     * Invoked when the XML file is completed, and allows private final states to be closed.
     * <p>
     * Warning: Due to <code> SAXParser</code> specification ambiguities, this method will 
     * not be called if an errorMsg occurs while reading the XML file. 
     * 
     * @throws SAXException found by SAX parser 
     */ 
    public void endDocument() throws SAXException 
    {
    	MsgCtrl.msgln("\tendDocument(): End of XML document parsing reached.");
    	// Do not close the file because other operations are needed first in HelpEngine
    }

    
    /** 
     * Implements <code>SAX.ContentHandler.characters()</code>.
     * Builds multiline text (character arrays) between a start and end tag, removes the word-
     * wrap substring, and trims the text. 
     * 
     * @param ch        byte array of characters read
     * @param start     first byte of char array
     * @param length    length of the character array
     */
    public void characters(char[] ch, int start, int length)  
    {
        if (_text == null) {
            _text = new StringBuilder(new String(ch,start,length));
        } else {
            _text.append(new String(ch,start,length));
        }
    }

    
    /** Copies non-white space characters of the input into a buffer and ensures that only
      * a single white space character remains there. White space is defined as
      * newlines, tab characters, and multiple space characters. 
      * <P>
      * This method is good for creating a single long-line of text (for binary output operations)
      * and for writing to GUI widgets that expect long lines, and do their own word-wrapping. 
      *  
      * @param rawText	the input text block to be cleansed of white space
      * @return the cleansed string
      */
    private String joinLines(StringBuilder rawText)
    {
    	final char NEWLINE = '\n';
    	final char TABCHAR = '\t';
    	final char SPACE = ' ';
    	
    	int len = rawText.length();
    	StringBuilder src = new StringBuilder(rawText);
    	StringBuilder dest = new StringBuilder(len);
    	
    	// Prime the dest with a leading space
    	int destPos = 0;
    	dest.append(SPACE);
    	
    	for (int index=0; index < len; index++) {
    		// Walk the text string char by char
    		char c = src.charAt(index);
    		if (c == NEWLINE) {
    			c = SPACE;
    		}
    		else if (c == TABCHAR) {
    			// Convert it to a space char for convenience
    			c = SPACE;
    		}
    		
    		// Now we only deal with SPACE and non-white space
    		try {
	    		if ((c == SPACE) && (dest.charAt(destPos) == SPACE))      {
	    				continue;
	    		}
				else {
	    				dest.append(c);
	    				destPos++;				// last place written into dest
//	    				System.err.println("\t" + dest.charAt(destPos));
	    		}
    		}  catch (IndexOutOfBoundsException ex) {
    			System.err.println(ex.getMessage());
    			break;
    		}
    			
        }	// end of loop 
    	// Convert destination to String to return
    	return new String(dest);
    }

    
    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
	* @param prefix unused 
    * @throws SAXException found by SAX parser 
    */
    public void endPrefixMapping(String prefix) throws SAXException {
//      NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
    * @param ch	unused 
    * @param start unused 
    * @param length unused 
     * @throws SAXException found by SAX parser 
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
//      NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
	* @param target unused 
	* @param data unused 
     * @throws SAXException found by SAX parser 
     */
    public void processingInstruction(String target, String data) throws SAXException {
        //NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @param locator unused 
     */
    public void setDocumentLocator(Locator locator) {
        //NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @param name unused
    * @throws SAXException found by SAX parser 
     */
    public void skippedEntity(String name) throws SAXException {
        //NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
    * @param prefix unused 
    * @param uri unused 
    * @throws SAXException found by SAX parser 
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        //NOOP
    }    
        
}		// end of HelpContentLoader class
