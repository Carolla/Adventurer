/*
 * Filename: SAXStream.java 
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

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Creates the SAX parsing and validating environment to 
 * parse the user's XML data file, according to the semantics defined in
 * the user's XML Schema file. This class does not implement or inherit from any
 * other classes, but creates XML and SAX objects that require callbacks
 * (implementation objects and methods) for application-specific parsing and
 * validating. 
 * <p>
 * The programmer needs to define two application-specific objects. The first implements the
 * SAX ContentHandler interface for defining callback functions, such as
 * <code>startDocument()</code>, <code>endDocument()</code> and tag
 * processing. In this application, that class is <code>DungeonContentLoader</code>.
 * The second object is an error handler to recieve and process parsing and validatiing
 * errors; in this case, <code>MyErrorHandler</code> performs that service.
 * <p>
 * Imports many onjects from the <code>javax.xml.validation</code> and 
 * <code>org.xml.sax</code> packages.
 * <P>
 * @author Alan Cline
 * @version
 * <DL>
 * <DT> 1.0 Mar 14 2007 // Original version based on Jack Summanen's methods. <DD>
 * <DT> 2.0 Apr 15 2007 // Put all application-independent action into single ctor. <DD>       
 * <DT> 3.0 Sep 29 2007 // Moved this class, with only admin changes, 
 * 				into the combined Dgn program. <DD>
 * <DT> 3.1 Jun 23 2008 // Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see DungeonContentLoader 
 * @see MyErrorHandler 
 */
public class SAXStream
{
	/** The compiled version of the XML Schema from the .xsd file. */
	private Schema _compiledSchema = null;

	/** The handler containing the .xsd validation processing. */
	private ValidatorHandler _vh = null;

	/** The XML file reader with parser for the .xml data file. */
	private XMLReader _reader = null;

	/**
	 * The .xml data file containing the data to be parsed and mapped into 
	 * app-specific objects.
	 */
	private String _dataFile = null;

	/**
	 * The .xsd schema file containing the semantic rules by which the XML data
	 * must be validated.
	 */
	private String _schemaFile = null;

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
			
	/** Unused */
	public SAXStream() { }

	/**
	 * Takes the user-defined XML data file and XSD schema file and 
	 * creates the application-independent SAX parsing/validating environment.
	 * After this object is created and returned, the caller then calls
	 * <code>parseSAXStream()</code> to evoke the application-specific callback methods.
	 * <P>
	 * The specific steps to create a SAXStream validating parser follows.
	 * <OL>
	 * <LI> Create a <code>SchemaFactory </code>to create a <code>Schema</code>
	 * object, then read the schema file, which is compiled immediately .
	 * </LI>
	 * <LI> Create a <code>ValidatorHandler</code>from the compiled Schema to handle 
	 * SAX events. </LI>
	 * <LI> Assign an instance of <code>MyErrorHandler</code> to the Validator Handler.</LI>
	 * <LI> Assign an instance of the document content and tag handler to the Validator
	 * Handler. In this case, it is <code>DungeonContentLoader</code>.</LI>
	 * <LI> Create a SAX Parser Factory to return an instance of an <code>XML Reader</code>,
	 * and then assign the now-initiated <code>ValidatorHandler</code> as the 
	 * ContentHandler for the XML Reader.</LI>
	 * </OL>
	 * Catches and handles <code>SAXParseException, SAXException</code>, 
	 * and <code>ParserConfigurationException</code>.
     *
	 * @param xmlFile contains the data in XML format
	 * @param xsdFile contains the XML Schema grammer for validating the data file
	 */
	public SAXStream(String xmlFile, String xsdFile) 
	{
		_dataFile = xmlFile;
		_schemaFile = xsdFile;

		try {
			// Create a Schema object, then read and compile the schema file
			SchemaFactory sf = SchemaFactory.newInstance(
					XMLConstants.W3C_XML_SCHEMA_NS_URI);
			_compiledSchema = sf.newSchema(new File(_schemaFile));

			// Create ValidatorHandler from the compiled Schema to handle SAX events
			_vh = _compiledSchema.newValidatorHandler();

			// Assign the generic error handler to the validator handler
			_vh.setErrorHandler(new MyErrorHandler());
			// Set the application implementation of the parsing object
			_vh.setContentHandler(new DungeonContentLoader());

			// Create an XML Reader which contains a parser
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			_reader = spf.newSAXParser().getXMLReader();

			// Associate the validating handler to the parser
			_reader.setContentHandler(_vh);

		}
		catch (SAXParseException e) {
			System.err.println(_schemaFile + " is not well formed.");
			System.err.println(e.getMessage() + " at line "
						+ e.getLineNumber() + ", column " + e.getColumnNumber());
		}
		catch (SAXException e) {
			System.err.println(e.getMessage());
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	} // end of SAXStream constructor

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
	/**
	 * Assigns an application-specific <code>SAXStream</code> to parse the XML file.
	 * Calls the XMLReader's parser to throw events at it.
	 * Catches <code>SAXParseException, SAXException</code>, and <code>IOException.</code> 
	 */
	public void parseSAXStream()
	{
		// All systems go -- parse the data file while validating
		try {
			_reader.parse(_dataFile);

		}
		catch (SAXParseException e) {
			System.err.println(_schemaFile + " is not well formed.");
			System.err.println(e.getMessage() + " at line "
						+ e.getLineNumber() + ", column " 	+ e.getColumnNumber());
		}
		catch (SAXException e) {
			System.err.println(e.getMessage());
		}
		catch (IOException e) {
			System.err.println("Could not report on "
						+ _dataFile + " because of the IOException "	+ e);
		}
	}

} // end of SAXStream class

