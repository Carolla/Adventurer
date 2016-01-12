/**
 * QAParser.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package dmc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mylib.MsgCtrl;

import pdc.ResultsTable;

/**
 * Read a JUnit test case file and search for which methods have which QA tag.
 * Test case methods may have multiple QA tokens, which occur before the test
 * method name, so the QA token collection must be kept while reading the file.
 * 
 * Also searches source files for illegal imports.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0   Jul 6, 2010 // original <DD>
 * </DL>
 */
public class QAParser
{
    /** Standard return value for many of the string searching methods */
    private static final int NOT_FOUND = -1;

    private static final String SPACE_CHAR = " ";
    private static final String OPEN_PAREN = "(";
    private static final String CLOSE_PAREN = ")";
    private static final String SEMI_COLON = ";";
    private static final String PKG = "package ";
    private static final String PKG_SEPARATOR = ".";
    private static final String IMPORT = "import ";

    /** Package prefix for tests */
    private static final String TEST_PKG_PREFIX = "test.";
    /** Target token is always found inside Javadoc comment.  */
    private static final String QA_TAG = "* @";
    /** Target method must always public void test<someMethod>  */
    private static final String METHOD_KEY = "public void test";
    /** Prefix before a test method name  */
    private static final String METHOD_PREFIX = "public void";
    /** Max number of lines a method name can be broken over */
    private static final int METHOD_LINES = 3;
    /** Line start between opening and closing comment identifiers */
    private static final String COMMENT_PREFIX = " *";

    // Indices into the token list
    private static final int QA_TOKEN = 0;
    private static final int METHOD_NAME = 1;
    
    /** Enum list of possible error values */
    private static enum Error
    {
        UNASSIGNED, UNNEEDED_TOKEN, MISSING_TOKEN, MISSING_METHODNAME,
        MISSING_ENDPAREN;
    }
    
    /** Enum list of QA tokens */
    public static enum Token
    {
        /** Boundary test tag */
        BOUNDARY,
        /** Error test tag */
        ERROR,
        /** Normal test tag */
        NORMAL,
        /** Not implemented test tag */
        NOT_IMPLEMENTED,
        /** Not needed test tag */
        NOT_NEEDED,
        /** Null test tag */
        NULL,
        /** Special test tag */
        SPECIAL,
        /** Deprecated test tag */
        DEPRECATED;
    }
    
    /** Enum map relating tokens to their textual representations */
    private static Map<Token, String> TOKEN_MAP = null;
    
    /** Error flag containing the line the error was discovered */
    private int _errorLine = NOT_FOUND;
    /** Error code describing the error type */
    private Error _errorCode = Error.UNASSIGNED;
    
    
    // ________________________________________________________________
    //
    //  CONSTRUCTOR(S) AND RELATED METHODS
    // ________________________________________________________________
 
    /** Default constructor */
    public QAParser() 
    {
        TOKEN_MAP = new EnumMap<Token, String>(Token.class);
        TOKEN_MAP.put(Token.BOUNDARY, "Boundary");
        TOKEN_MAP.put(Token.ERROR, "Error");
        TOKEN_MAP.put(Token.NORMAL, "Normal");
        TOKEN_MAP.put(Token.NOT_IMPLEMENTED, "NotImplemented");
        TOKEN_MAP.put(Token.NOT_NEEDED, "NotNeeded");
        TOKEN_MAP.put(Token.NULL, "Null");
        TOKEN_MAP.put(Token.SPECIAL, "Special");
        TOKEN_MAP.put(Token.DEPRECATED, "Deprecated");
    }


    // ________________________________________________________________
    //
    //  PUBLIC METHODS
    // ________________________________________________________________

    /**
     * Scans a source file for illegal imports
     * 
     * @param targetFile    file to scan
     * @param illegal       set of packages that are illegal to import
     * @return  line number of first illegal import or NOT_FOUND if not found
     */
    public int scanSourceImports(File targetFile, Set<String> illegal)
    {
        int retVal = NOT_FOUND;
        int lineCount = 1;
        
        try {
            // Read a line of text from the file
            BufferedReader  in = new BufferedReader(new FileReader(targetFile));
            String line = null;
            boolean doneReading = false;
            while (!doneReading && (retVal == NOT_FOUND)) {
                line = in.readLine();
                if (line == null) {
                    doneReading = true;
                }
                else {
                    // Check for import statement
                    for (String i : illegal) {
                        if (line.indexOf(IMPORT + i) != NOT_FOUND) {
                            // Illegal import found
                            retVal = lineCount;
                            break;
                        }
                    }
                    lineCount++;
                }
            }
        }
        catch (IOException ex) {
            MsgCtrl.errMsg(this, "Error reading source file");
        }
        
        return retVal;
    }
    
    
    /**
     * Read the test file and look for QA tags. Build a table of the resulting
     * source and test methods along with their QA tags. 
     * 
     * @param targetFile    to search
     * @return table of results
     */
    public ResultsTable scanTestFile(File targetFile) 
    {
        int lineCount = 0;
        ResultsTable table = new ResultsTable();
        Object[] qaTags = new String[2];
        HashMap<String, HashSet<Token>> methodTags =
            new HashMap<String, HashSet<Token>>();
        String pkg = null;
        // Variables for collecting method names that are broken across lines
        int methodBreaks = 0;
        String methodPartial = "";
        Token currentToken = null;
        try {
            // Read a line of text from the file
            BufferedReader  in = new BufferedReader(
                    new FileReader(targetFile));
            String line = null;
            boolean doneReading = false;
            while (!doneReading) {
                line = in.readLine();
                if (line == null) {
                    doneReading = true;
                    break;
                }
                lineCount++;
                // Check for partial method name
                if (methodBreaks > 0) {
                    if (methodBreaks == METHOD_LINES) {
                        // Too many line breaks for method name, ignore method
                        _errorLine = lineCount;
                        String msg = targetFile.getName() + ":" + _errorLine
                            + " Incomplete method signature";
                        MsgCtrl.errMsgln(this, msg);
                        // Clear method info
                        methodBreaks = 0;
                        methodPartial = "";
                        currentToken = null;
                    }
                    else {
                        // Get next part of method name
                        methodPartial += getPartialSourceMethodName(line);
                        methodBreaks++;
                        
                        // Check for completion
                        if (methodPartial.endsWith(CLOSE_PAREN)) {
                            // Add completed source method
                            String source = getCanonicalMethodName(pkg
                                    + PKG_SEPARATOR + methodPartial);
                            
                            // If the method signature has generics, remove them
                            // (e.g. List<String> becomes List)
                            if (source.indexOf('<') != NOT_FOUND) {
                                source = stripGenerics(source);
                            }
                            
                            if (methodTags.containsKey(source)) {
                                // Source method already exists
                                methodTags.get(source).add(currentToken);
                            }
                            else {
                                // New source method
                                HashSet<Token> set = new HashSet<Token>();
                                set.add(currentToken);
                                methodTags.put(source, set);
                            }
                            methodBreaks = 0;
                            methodPartial = "";
                            currentToken = null;
                        }
                    }
                    
                }
                // Check for package line
                else if (line.indexOf(PKG + TEST_PKG_PREFIX) != NOT_FOUND) {
                    int start = line.indexOf(PKG + TEST_PKG_PREFIX)
                        + PKG.length() + TEST_PKG_PREFIX.length();
                    int end = line.indexOf(SEMI_COLON);
                    if (end == NOT_FOUND) {
                        MsgCtrl.errMsgln(this, "Unexpected package statement");
                        continue;
                    }
                    pkg = line.substring(start, end).trim();
                }
                // Check if the line starts with a QA_TAG
                else if (line.indexOf(QA_TAG) != NOT_FOUND) {
                    // Package should have already been set
                    if (pkg == null) {
                        MsgCtrl.errMsgln(this, "Package not found");
                        break;
                    }
                    
                    // If so, get and save the QA token and method name until we
                    // read to the test method. Error Code is set by this method
                    qaTags = getTaggedSourceMethod(line);
                    // Add another token pair to the QA list for the upcoming
                    // method
                    if (qaTags != null) {
                        if (_errorCode == Error.MISSING_ENDPAREN) {
                            // Method signature may be broken across lines
                            methodPartial += qaTags[METHOD_NAME];
                            currentToken = (Token) qaTags[QA_TOKEN];
                            methodBreaks++;
                        }
                        else {
                            Token token = (Token) qaTags[QA_TOKEN];
                            String source = getCanonicalMethodName(pkg
                                    + PKG_SEPARATOR + qaTags[METHOD_NAME]);
                            
                            // If the method signature has generics, remove
                            // them (e.g. List<String> becomes List)
                            if (source.indexOf('<') != NOT_FOUND) {
                                source = stripGenerics(source);
                            }
                            
                            // Add tag to collection for the current method
                            if (methodTags.containsKey(source)) {
                                // Source method already exists
                                methodTags.get(source).add(token);
                            }
                            else {
                                // New source method
                                HashSet<Token> set = new HashSet<Token>();
                                set.add(token);
                                methodTags.put(source, set);
                            }
                        }
                        continue;
                    }
                    // If the QA token is not relevant, continue reading. If it
                    // is, then a method name is required to follow it
                    if (_errorCode == Error.UNNEEDED_TOKEN) {
                        continue;
                    }
                    // Otherwise, we have an error in the QA token line
                    _errorLine = lineCount;
                    String msg = targetFile.getName() + ":" + _errorLine
                        + " Parse error (" + _errorCode + ")";
                    MsgCtrl.errMsgln(this, msg);
                    // Ignore this file by clearing out table
                    table.clear();
                    break;
                }
                // Check for test method signature
                else if (line.indexOf(METHOD_KEY) != NOT_FOUND) {
                    // All tags for this method have been collected
                    String testFile = targetFile.getName();
                    int stop = testFile.lastIndexOf(".java");
                    if (stop == NOT_FOUND) {
                        MsgCtrl.errMsgln(this, "Non-Java file extension");
                    }
                    testFile = testFile.substring(0, stop);  
                    String test = TEST_PKG_PREFIX + pkg + PKG_SEPARATOR
                        + testFile + PKG_SEPARATOR  + getTestMethodName(line);
                    if (methodTags.size() == 0) {
                        // Method without tags
                        table.addUntaggedTest(test);
                    }
                    for (String source : methodTags.keySet()) {
                        table.addTaggedTest(source, test,
                                methodTags.get(source));
                    }
                    // Reset tag collection
                    methodTags.clear();
                }
            } // end of file-reading loop
            in.close();
        }
        catch (FileNotFoundException ex) {
            MsgCtrl.errMsgln(this, "Can't find file to read: "
                    + targetFile.getAbsolutePath());
        }
        catch (IOException ex) {
            MsgCtrl.errMsgln(this, ex.getMessage());
        }
        return table;
    }
    
        
    // ________________________________________________________________
    //
    //  PRIVATE METHODS
    // ________________________________________________________________
    
    /**
     * Converts a signature to a canonical method name be removing parameter
     * names.
     * 
     * @param signature method signature to convert
     * @return  canonical method name
     */
    private String getCanonicalMethodName(String signature)
    {
        // Trim leading/trailing white space
        String sig = signature.trim();
        
        // Get everything before parameter list
        int openParen = sig.indexOf(OPEN_PAREN);
        String methodPrefix = sig.substring(0, openParen + 1);
        
        // Clean parameter list
        int closeParen = sig.indexOf(CLOSE_PAREN);
        String paramList = sig.substring(openParen + 1, closeParen);
        String cleanList = "";
        String[] params = paramList.split(",");
        for (int k = 0; k < params.length; k++) {
            // Trim leading and trailing white space
            params[k] = params[k].trim();
            
            // Keep parameter type, discard name
            int end = params[k].indexOf(" ");
            if (end == NOT_FOUND) {
                cleanList += params[k].substring(0);
            }
            else {
                cleanList += params[k].substring(0, end);
            }
            
            // Add commas between parameters
            if (k < params.length - 1) {
                cleanList += ",";
            }
        }
        cleanList += CLOSE_PAREN;
        
        // Remove all spaces
        String finalSignature = (methodPrefix + cleanList).replace(" ", "");
        
        return finalSignature;
    }
    
    
    /**
     * Gets the a partial source method name from a line.  Used when a source
     * method name is broken across multiple lines.
     * 
     * @param line  text to parse
     * @return  part of source method name on this line
     */
    private String getPartialSourceMethodName(String line)
    {
        String testMethod = null;
        // Move the line pointer past the comment 
        int ptr = line.indexOf(COMMENT_PREFIX) + COMMENT_PREFIX.length();
        // Find the white space following the comment prefix
        int spacePos = line.indexOf(SPACE_CHAR, ptr);
        if (spacePos == NOT_FOUND) {
            return null;
        }
        // Find closing parenthesis or end of line
        int parenPos = line.indexOf(CLOSE_PAREN, ptr);
        if (parenPos == NOT_FOUND) {
            // Get rest of line and trim white space
            testMethod = line.substring(spacePos, line.length()).trim();
        }
        else {
            // Extract the method name and trim white space
            testMethod = line.substring(spacePos, parenPos + 1).trim();
        }        
        
        return testMethod;
    }


    /** 
     * A QA Tag has been found at the front of the line. 
     * Now search through the text line looking for a QA tag of interest
     * instead of skippable tokens (e.g. irrelevant Javadoc tags, annotations).
     * 
     * @param line          text string to examine for unwanted QA tags
     * @return  the QA token in the first element, the class.methodName 
     *              in the second element; else returns null 
     */
    private Object[] getTaggedSourceMethod(String line)
    {
        Object[] qaTags = new Object[2];
        Token token = null;
        String tokenText = null;
        // Init the errorCode
        _errorCode = Error.UNASSIGNED;
        // Move the line pointer to the first QA Token
        int ptr = line.indexOf(QA_TAG) + QA_TAG.length();
        // Find the white space following the QA token
        int spacePos = line.indexOf(SPACE_CHAR, ptr);
        if (spacePos == NOT_FOUND) {
            token = getToken(line);
            if (token == null) {
                _errorCode = Error.UNNEEDED_TOKEN;
                return null;
            }
            _errorCode = Error.MISSING_TOKEN;
            return null;
        }
        // Extract the token, and ensure that it exists and is a QA token
        tokenText = line.substring(ptr, spacePos);
        if (tokenText == null) {
            _errorCode = Error.MISSING_TOKEN;
            return null;
        }
        token = getToken(tokenText);
        if (token == null) {
            _errorCode = Error.UNNEEDED_TOKEN;
            return null;
        }
    
        // Extract the method name following the QA token, using the closing
        // paren of the method signature
        int endSig =  line.indexOf(CLOSE_PAREN) + 1;  // Include parenthesis
        if (endSig == NOT_FOUND + 1) {
            // Signature may be broken across multiple lines, return partial
            _errorCode = Error.MISSING_ENDPAREN;
            endSig = line.length();
        }
        String methodName = line.substring(ptr + tokenText.length(),
                endSig).trim();
        
        // We now have a good token and a good method name
        // Convert the String token into an enum
        qaTags[QA_TOKEN] = token;
        qaTags[METHOD_NAME] = methodName;
        
        // If there is no method name following a valid QA Token, the pair is
        // invalid
        if (qaTags[METHOD_NAME] == null) {
            _errorCode = Error.MISSING_METHODNAME;
            return null;
        }
        return qaTags;
    }


    /**
     * Gets the test name from a line beginning with METHOD_KEY
     * 
     * @param line  text to parse
     * @return  test method name or null if not found
     */
    private String getTestMethodName(String line)
    {
        String testMethod = null;
        // Move the line pointer to start of method name 
        int ptr = line.indexOf(METHOD_PREFIX) + METHOD_PREFIX.length();
        // Find the white space following the method prefix
        int spacePos = line.indexOf(SPACE_CHAR, ptr);
        // Find closing parenthesis
        int parenPos = line.indexOf(CLOSE_PAREN, ptr);
        if ((spacePos == NOT_FOUND) || (parenPos == NOT_FOUND)) {
            return null;
        }
        // Extract the method name and trim white space
        testMethod = line.substring(spacePos, parenPos + 1).trim();
        
        return testMethod;
    }


    /**
     * Search the map of QA tokens for the token with the specified text.
     * 
     * @param token     to match against list
     * @return  token or null if not found
     */
    private Token getToken(String token) 
    {
        for (Token t : TOKEN_MAP.keySet()) {
            if (TOKEN_MAP.get(t).equals(token)) {
                return t;
            }
        }
        return null;
    }
    
    
    /**
     * Strips generics information from a method name to allow better matching
     * (e.g. "test(List<String> var)" becomes "test(List var)").
     * 
     * @param method    method signature
     * @return  method signature without generics information
     */
    private String stripGenerics(String method)
    {
        return method.replaceAll("<[a-zA-Z\\?,\\w]+>", "");
    }
    
    
    // ________________________________________________________________
    //
    //  INNER CLASSES
    // ________________________________________________________________
    
    /**
     * Mock class for testing the QA Parser itself
     */    
    public class MockQAParser
    {
        /**
         * Empty constructor
         */
        public MockQAParser()
        {
        }
        
        
        /**
         * Return the error code to find the problem
         * 
         * @return  the Errorcode flagged during parsing
         */
        public Error getErrorCode()
        {
            return _errorCode;
        }

        
        /**
         * Return canonical method name
         * 
         * @param name  method signature to be converted
         * @return  canonical name
         */
        public String getCanonicalMethodName(String name)
        {
            return QAParser.this.getCanonicalMethodName(name);
        }
        
        
        /**
         * Wrapper for private getPartialSourceMethodName
         *  
         * @param line     text line to scan for part of method name
         * @return  part of method name
         */
        public String getPartialSourceMethodName(String line)
        {
            return QAParser.this.getPartialSourceMethodName(line);
        }
        
        
        /**
         * Wrapper for private getTaggedSourceMethod
         *  
         * @param line     text line to scan for tags
         * @return  QA token and methodName in array; else null
         */
        public Object[] getTaggedSourceMethod(String line)
        {
            return QAParser.this.getTaggedSourceMethod(line);
        }
        
        
        /**
         * Wrapper for private getTestMethodName
         *  
         * @param line     text line to scan for test method name
         * @return  test method name
         */
        public String getTestMethodName(String line)
        {
            return QAParser.this.getTestMethodName(line);
        }
        
        
        /**
         * Wrapper for private getToken
         * 
         * @param token text of token to be found
         * @return  token or null if not found
         */
        public Token getToken(String token)
        {
            return QAParser.this.getToken(token);
        }

    }   // end of MockQAParser inner class

}   // end of QAParser class

