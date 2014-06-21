/*
 * Filename: MyErrorHandler.java
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


/**
 * Prints any Warning , Error or Fatal Error from the SAX parser.
 * This generic implementation should suffice for most applications.
 * @author Alan Cline
 * @version
 * <DL>
 * <DT> 1.0 Mar 14 2007 // Original version based on Jack Summanen's methods. <DD>
 * <DT> 1.1 Jun 23 2008 // Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see SAXStream 
 */
public class MyErrorHandler implements org.xml.sax.ErrorHandler
{
    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
    /** Creates a new instance of MyErrorHandler */
    public MyErrorHandler() { }
    

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** 
     * Required implementation method for catching general error Exceptions.
     * Catches and displays SAX error exceptions it receives from <code>SAXParser</code>. 
     * It works like an XML exception version of a <code>println</code> call.
     * 
     * @param  e	<code>SAXParseException</code> from the <code>SAXParser</code>
     */
    public void error(org.xml.sax.SAXParseException e) throws org.xml.sax.SAXException 
    {
        System.err.println(e.getMessage() +" at line "+ e.getLineNumber() 
        		+", column "+ e.getColumnNumber());
    }

    /** 
     * Required implementation method for catching fatal error Exceptions.
     * It catches and displays SAX fatal error exceptions it receives from <code>SAXParser</code>. 
     * It works like an XML exception version of a <code>println</code> call.
     * 
     * @param  e	<code>SAXParseException</code> from the <code>SAXParser</code>
     */
    public void fatalError(org.xml.sax.SAXParseException e) throws org.xml.sax.SAXException 
    {
        System.err.println("FATAL: "+e.getMessage() +" at line "
        		+ e.getLineNumber() +", column "+ e.getColumnNumber());
    }

    /** 
     * Required implementation method for catching warning Exceptions.
     * It catches and displays SAX warning exceptions it receives from <code>SAXParser</code>. 
     * It works like an XML exception version of a <code>println</code> call.
     * 
     * @param  e	<code>SAXParseException</code> from the <code>SAXParser</code>
     */
    public void warning(org.xml.sax.SAXParseException e) throws org.xml.sax.SAXException 
    {
        System.out.println("WARNING: "+e.getMessage()
                +" at line "+e.getLineNumber() +", column "+e.getColumnNumber());
    }
    
    
}	// end MyErrorHandler class
