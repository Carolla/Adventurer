/**
 * Filename: DungeonContentLoader.java
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

import rumorMillStuff.src.hic.Dgn;
import rumorMillStuff.src.pdc.Inn;
import rumorMillStuff.src.pdc.Innkeeper;
import rumorMillStuff.src.pdc.Patron;
import rumorMillStuff.src.pdc.PatronRegistry;
import rumorMillStuff.src.pdc.Person;
import rumorMillStuff.src.pdc.Room;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


/**
 * Contains a sequence of application-specific callback methods for the SAXStream 
 * parser-validator that reads the Dungeon XML data file and XSD schema file. 
 * <P>
 * General rules for parsing and object construction used in this application:
 * <OL>
 * <LI> Arguments to object constructors are defined in attributes.</LI> 
 * <LI> Collection objects (e.g., <code>Inn, PatronRegistry</code>) are created as found, 
 * and component objects are added as created. See <code>startElement()</code> 
 * 		method.</LI> 
 * <LI> Integral (non-collection) objects (e.g. <code>Innkeeper, Person</code>) are 
 * 		retained until all data are parsed, then added as a whole to their owner object. See 
 * 		<code>endElement()</code> method.</LI> 
 * <LI> In the exceptional case that an integral object has attributes and many text elements, 
 * 		the object is created as found, and the elements added through setter methods.</LI> 
 * </OL>
 * XML parsing format:
 * <OL>
 * <LI>The file defines a Dungeon group (the <code>Inn</code>), containing a name and 
 * 		introduction.</LI> 
 * <LI>Normally an <code>Inn Dungeon</code> contains a sequence of <code>Rooms</code>, 
 * 		an <code>Innkeeper</code>, and a collection of <code>Patrons</code>, but in this.  
 * 		case, the <I>Rumor Mill</I> is a one-room  dungeon. </LI> 
 * <LI> Each <code>Room</code> contains a name and description. </LI>
 * <LI>Each <code>Patron</code> has a name, far and near descriptions (for LOOK and 
 * 		EXAMINE commands), and (usually, as default) 3 positive and 3 negative messages. 
 * 		The third positive message is called a 'rumor'; the third negative message is a 'retort'.
 * 		The <code>Patron</code> leaves the Inn after uttering their rumor or retort.</LI> 
 * <LI> <code>Patron</code> and <code>Innkeeper</code> are subclasses of 
 * 		<code>Person</code>. The <code>Innkeeper </code>cannot leave the Inn but has 
 * 		multiple rumors and retorts instead of one (and recycles messages). 
 * </OL>
 * @author Al Cline, based on a skeleton prototype by Jack Summanen, August, 2006
 * @version <DL>
 * <DT> 1.0 	Aug 17 2006		// SAX Parser operates on single file containing Inn, Innkeeper 
 * 						and Patrons <DD>
 * <DT> 2.0	Sep 29 2007		// Minor modifications when merged into single builder and 
 * 						runner program. <DD>
 * <DT> 2.1	Feb 23 2008		// Removed message attributes <code>rank, rumor, retort</code>
 * 						 and	added <code>Patron's affinity</code> attribute. <DD>
 * <DT> 2.2 	Jun 29 2008 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class DungeonContentLoader implements ContentHandler
{
    // The following constants are keys for the XML tags.
	/** <dungeon> HTML group tag */
    private final String DUNGEON_KEY = "dungeon";   	// group element
	/** <intro> HTML element text tag */
    private final String DUNGEON_INTRO = "intro";     		// element key, _text value
	/** <room> HTML group tag */
    private final String ROOM_KEY = "room";           			// group element
    
    // Persons can be of Innkeeper or Patron types
	/** <people> HTML group tag */
    private final String PEOPLE_KEY = "people";       			// group element
	/** <innkeeper> HTML element tag */
    private final String INNKEEPER_KEY = "innkeeper"; 		// attribute value[0]
	/** <patron> HTML element tag */
    private final String PATRON_KEY = "patron";        			// element key

    // Person descriptions can be of type far or close, then followed by _text 
	/** <description> HTML group tag */
    private final String DESCRIPTION_KEY = "description"; 		// group element
	/** <far> HTML element text tag */
    private final String DESCRIPTION_FAR = "far";     				// map key only, _text value
	/** <near> HTML gelement text tag */
    private final String DESCRIPTION_NEAR = "near";   			// map key only, _text value

    /** TODO Are these two flags needed for parsing? */
    /** designates how to handle near and far descriptions for later */
    private final boolean DESC_NEAR_FLAG = false;
    /** true if description is far */
    private final boolean DESC_FAR_FLAG = true;
	/** <message> HTML group tag */
    private final String MESSAGE_KEY    = "message";    // group element
    
    /** Either a far or near description of the Person */
    private String _descType;								// either a far or near description of the Person
	/** far (true) or near (false) description of the Person */
    private boolean _descFlag;							// far = true; near = false
    

    /** Problem domain objects created or used by this SAX Parser */
    private Inn _thisInn;
    /** Problem domain objects created or used by this SAX Parser */
    private Room _room;					// Create a room and add to dungeon (Inn); later the Inn will contain multiple rooms.
    /** Problem domain objects created or used by this SAX Parser */
    private PatronRegistry _people;	// Create a despository for all the Patrons
    /** Problem domain objects created or used by this SAX Parser */
    private Person _person;				// Holding reference to keep current Person; needed for desc to Innkeeper or Patrons
    /** Problem domain objects created or used by this SAX Parser */
    private Patron _patron;
    /** Problem domain objects created or used by this SAX Parser */
    private Innkeeper _owner; 

    /** Internal attribute: Holds text from input parsing */
    private StringBuffer _text;
    /** Internal attribute: Retention attributes to bridge between startElement and endElement processing */
    private String _curMsgType;      		// string polarity for positive & negative messages
    /** Internal attribute: Retention attributes to bridge between startElement and endElement processing */
    private boolean _curMsgFlag;			// true (positive) or false (negative) messages


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** Default constructor */
    DungeonContentLoader() { }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /** 
     * Implements <code>SAX.ContentHandler.characters(),</code>
     * which builds multiline text (character arrays) between a start and end tag, and trims the 
     * text.
     * 
     * @param ch        byte array of characters read
     * @param start     first byte of char array
     * @param length    length of the character array
     */
    public void characters(char[] ch, int start, int length)  
    {
        if (_text == null) {
            _text = new StringBuffer(new String(ch,start,length));
        } else {
            _text.append(new String(ch,start,length));
       }
    }

    
    /** 
     * Invoked when the XML file is completed, and allows private final states to be closed.
     * <p>
     * Warning: Due to<code> SAXParser</code> specification ambiguities, this method will 
     * not be called if an error occurs while reading the XML file. 
     * 
     * @throws SAXException found by SAX parser 
     */ 
    public void endDocument() throws SAXException 
    {
    	Dgn.auditMsg("End of XML document parsing reached.");
    }

    
    /**
     * Implements <code>SAX.ContentHandler.endElement()</code>
     * which processes an object when its data block <code>endElement</code> tag is found.
     * In general, XML attribute values are passed to the object's constructor from 
     * <code>startElement()</code>; 
     * element data, such as text blocks, are added to the object later by end tag methods.
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
        // END THE DUNGEON GROUP
        // Dungeon group contains 0 _text block  
        if (DUNGEON_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("End </" + DUNGEON_KEY + ">\n");
            // no op processing 
        }
        // End dungeon Intro element with 1 _text block
        else if (DUNGEON_INTRO.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\tEnd </" + DUNGEON_INTRO + ">");
            // Add the Intro to the Inn
            _thisInn.setIntro(_text.toString().trim());
         }
        // End Room, contains 1 _text block = description
        else if (ROOM_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\tEnd </" + ROOM_KEY + ">");
            // Add the room description to the room
            _room.setDescription(_text.toString().trim());
            // Add current room into the Inn
            if ((_thisInn != null) && (_room != null)) {
           		_thisInn.addRoom(_room);      											
            }
            else {
            	Dgn.debugMsg("Inn or Room is set to null.");
                throw new SAXException();
            }
        }
        
        // END THE PEOPLE GROUP
        else if (PEOPLE_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("End </" + PEOPLE_KEY + "> ");
            if (_owner == null) {
                	Dgn.debugMsg("Innkeeper unexpectedly set to null");
                	throw new SAXException();
            }
            if (_people != null) {
   	            Dgn.auditMsg("\tPatrons completed and added to Registry = " + _people.size());
            }
            else {
            	Dgn.debugMsg("_people (PatronRegistry) unexpectedly set to null");
            	throw new SAXException();
            }
        } 
        // Add the MsgTable to the Innkeeper and add the completed Innkeeper to the Inn
        else if (INNKEEPER_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\tEnd </" + INNKEEPER_KEY + "> ");
	          if ((_owner != null) && (_room != null)) {
	        	  // Add the Innkeeper to the Room of the Inn; he does not "enter".
	        	  _room.add(_owner);								
	          }
	          else {
	        	  Dgn.debugMsg("Innkeeper or Room is unexpectedly to null");
	        	  throw new SAXException();
	          }
        }

	        // Create the Patron and descriptions, and save
	        else if (PATRON_KEY.equals(qName.toLowerCase())) {
//	            Dgn.auditMsg("\tEnd </" + PATRON_KEY + "> ");
	            if ((_patron != null) && (_people != null)) {
	            	// Add current patron into the container
	                _people.register(_patron);						
//	                Dgn.auditMsg("\t" + _patron.getName() + " read.");
	            }
	            else {
	                Dgn.debugMsg("Patron or PatronRegistry set to null");
	                throw new SAXException();
	            }
	        } 
	        // End Person Description, contains 1 _text block for either near or far type
	        else if (DESCRIPTION_KEY.equals(qName.toLowerCase())) {
//	            Dgn.auditMsg("\tEnd <" + DESCRIPTION_KEY + "> ");
//	            Dgn.auditMsg("\n<" + _desctype + " " + DESCRIPTION_KEY + "> ");
	        	// Add far or near description to either Innkeeper or Patron (polymorphic)
	        	_person.setDescription(_text.toString().trim(), _descFlag);
	            }

	        // END THE MESSAGE GROUP
	        // End Message, which contains 1 _text block for positive/negative and rank 1 and 2
	        else if (MESSAGE_KEY.equals(qName.toLowerCase())) {
//	        	Dgn.auditMsg("\t\tEnd <" + _curMsgType + " " + MESSAGE_KEY + "> ");
//	        	Dgn.auditMsg("\n<" + _curMsgType + " " + MESSAGE_KEY + "> ");
	        	// Set the value for the correct Message type
	        	String curMsg = _text.toString().trim();
	        	_person.addMsg(curMsg, _curMsgFlag);
	        }

        // Clear any _text fields for next use
        _text = null;

    }	// end of endElement method

    
    /**
     * Sets up certain states before the document is parsed,  
     * or creates various internal state variables.
     * Currently, it does nothing but display an audit message.
     *  
     * @throws SAXException found by SAX parser 
    */
    public void startDocument() throws SAXException 
    {
    	Dgn.auditMsg("Starting to read XML document with SAX-validating parser.");
    }

    
    /**
     * Implements <code>SAX.ContentHandler.startElement()</code>
     * which processes an object when its <code>startElement </code> tag is found; 
     * usually, the object is created. In general, XML attribute values are passed to the 
     * object's constructor from <code>startElement()</code>; element data, such as text 
     * blocks, are added to 	the object later by end tag methods.
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
        
        // START THE DUNGEON GROUP
        // Dungeon element has 1 attribute = dungeon name (plus intro _text)
        if (DUNGEON_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("Start <" + DUNGEON_KEY + "> ... ");
            //  Create Inn singleton
            _thisInn = Inn.createInstance(atts.getValue(0));			
        }
        // Intro has 0 attributes 
        else if (DUNGEON_INTRO.equals(qName.toLowerCase())) {
//          Dgn.auditMsg("\tStart <" + DUNGEON_INTRO + "> ... ");
            // no op until _text block is complete
        }
        // Room element has 1 attribute = room name (plus description _text)
        else if (ROOM_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\tStart <" + ROOM_KEY + "> ... ");
            // Create a new Room for adding to the Inn
            _room = new Room(atts.getValue(0));
        }
 
        // START THE PEOPLE GROUP
        // People is a group element with 0 attributes, containing a list of Patron groups
        else if (PEOPLE_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\nStart <" + PEOPLE_KEY + "> ");
            // Create a container to hold the Patrons and add Patrons later as they are found
            _people = PatronRegistry.getInstance();
        }
        // Create the InnKeeper Person
        else if (INNKEEPER_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\tStart <" + INNKEEPER_KEY + "> ");
            _owner = new Innkeeper(atts.getValue(0), 0);
//            Dgn.auditMsg("\nInnkeeper's name is " + _owner.getName());
            // Current Person is the Innkeeper; all following elements are applied to this Person
            _person = _owner;			
        }
        // Create a Patron Person
        else if (PATRON_KEY.equals(qName.toLowerCase())) {
//            Dgn.auditMsg("\n\tStart <" + PATRON_KEY + "> ");
        	// First attribute = Patron's name; second attribute is the Person's affinity
            _patron = new Patron(atts.getValue(0), new Integer(atts.getValue(1)));
            _person = _patron;		// set the base class to this instantiation
//        	Dgn.auditMsg("\n\t" + atts.getValue(0) + " has affnity = " + atts.getValue(1));
        }
        // Description has 1 attribute = type (near or far) followed by _text block
        else if (DESCRIPTION_KEY.equals(qName.toLowerCase())) {
            _descType = atts.getValue(0);
        	// Convert XML tag (String value) to boolean flag to keep the called method ignorant of the XML tag names
            if (_descType.equalsIgnoreCase(DESCRIPTION_FAR)) {
            	_descFlag = DESC_FAR_FLAG;
            }
            else if (_descType.equalsIgnoreCase(DESCRIPTION_NEAR)) {
            	_descFlag = DESC_NEAR_FLAG;
            }
            else {
                System.err.println("ERROR: Illegal Description type found.");
                throw new SAXException();
            }
        }
        
        // START A SERIES OF MESSAGES
        // Messages have 2 attributes, type and rank; rank is currently ignored
        else if (MESSAGE_KEY.equals(qName.toLowerCase())) {
        	_curMsgType = atts.getValue(0);
           	// Convert XML tag (String value) to boolean flag to keep the called method ignorant of the XML tag names
        	if (_curMsgType.equalsIgnoreCase(Dgn.STR_POSITIVE)) {
            	_curMsgFlag = Dgn.POSITIVE;
            }
            else if (_curMsgType.equalsIgnoreCase(Dgn.STR_NEGATIVE)) {
            	_curMsgFlag = Dgn.NEGATIVE;
            }
            else {
//                System.err.println("ERROR: Illegal Message type or rank found.");
                System.err.println("ERROR: Illegal Message type found.");
                throw new SAXException();
            }
        }

    }		// end of startElement method

    
    
    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
    * @throws SAXException found by SAX parser 
    */
    public void endPrefixMapping(String prefix) throws SAXException {
//      NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @throws SAXException found by SAX parser 
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
//      NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @throws SAXException found by SAX parser 
     */
    public void processingInstruction(String target, String data) throws SAXException {
        //NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @throws SAXException found by SAX parser 
     */
    public void setDocumentLocator(Locator locator) {
        //NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @throws SAXException found by SAX parser 
     */
    public void skippedEntity(String name) throws SAXException {
        //NOOP
    }

    /** Not implemented from interface <code>SAX.ContentHandler.</code>.
     * @throws SAXException found by SAX parser 
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        //NOOP
    }
    
    
}		// end of DungeonContentLoader class
