/**
 * QASpec.java
 *
 * Distribution Statement C: Distribution authorized to U.S. Government Agencies and their
 * contractors due to Administrative or Operation Use as determined on 24 February 2010. Other
 * requests for this document shall be referred to ASC/XRE, 1970 Monahan Way, WPAFB, OH 45433-7902.
 *
 * ITAR Statement: WARNING: This document contains technical data whose export is restricted by the
 * Arms Export Control Act (Title 22, U.S.C., sec 2751, et seq.) or the Export Administration Act of
 * 1979, as amended, Title 50, U.S.C., App. 2401 et seq. Violation of these export laws are subject
 * to severe criminal penalties. Disseminate in accordance with the provisions of DoD Directive
 * 5230.25.
 */


package davinci.qatool.pdc;


import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import davinci.qatool.dmc.XmlSpecReader;
import davinci.qatool.util.DirType;
import davinci.qatool.util.MsgCtrl;


/**
 * Singleton that contains the specification of the project being checked
 *
 * @author Cheyney Loffing
 * @version
 *  <ul>
 *      <li>Build 1.0   Mar 15, 2011    // Original</li>
 *  </ul>
 */
public class QASpec
{
    /** Separator for library directories specified at command line */
    private static final String LIB_DIR_SEPARATOR = ",";

    /** Name attribute for root directory */
    private static final String ROOT_DIR_TYPE = "root";
    /** Name attribute for source directory */
    private static final String SRC_DIR_TYPE = "src";
    /** Name attribute for test directory */
    private static final String TEST_DIR_TYPE = "test";
    /** Name attribute for bin (output) directory */
    private static final String BIN_DIR_TYPE = "bin";
    /** Name attribute for lib directory */
    private static final String LIB_DIR_TYPE = "lib";
    
    /** Error message to log for unrecognized directory type */
    private static final String DIR_TYPE_ERR = "Unexpected directory type: %1$s";
    
    /** Singleton reference */
    private static QASpec _spec = null;

    /** Path to root directory */
    private String _rootDirPath = null;
    /** Path to source directory */
    private String _srcDirPath = null;
    /** Path to test directory */
    private String _testDirPath = null;
    /** Path to bin (output) directory */
    private String _binDirPath = null;
    /** Paths to supplemental library directories and whether they're relative to project root */
    private Map<String, Boolean> _libDirPaths = null;
    
    /** Package prefix for main source packages */
    private String _srcPackage = null;
    /** Package prefix for main unit test packages */
    private String _testPackage = null;
    
    /** Collection of required source directories */
    private Set<String> _srcReqDirs = null;
    /** Collection of optional source directories */
    private Set<String> _srcOptDirs = null;
    /** Collection of source directories to be ignored */
    private Set<String> _srcIgnDirs = null;
    /** Collection of required test directories */
    private Set<String> _testReqDirs = null;
    /** Collection of optional test directories */
    private Set<String> _testOptDirs = null;
    /** Collection of test directories to be ignored */
    private Set<String> _testIgnDirs = null;

    /** Map containing illegal imports based on package */
    private Map<String, Set<String>> _illegalImports = null;
    
    /** Set of prefixes used by unit test classes */
    private Set<String> _unitTestPrefixes = null;
    /** Name of unit test suites */
    private String _unitSuiteName = null;

    /** Set of prefix used by integration test classes */
    private Set<String> _integTestPrefixes = null;
    /** Prefix used by all integration test directories */
    private String _integTestDirPrefix = null;
    /** Name of integration test suites */
    private String _integSuiteName = null;
    
    /** Name of regression test suites */
    private String _regSuiteName = null;
    
    
    // ________________________________________________________________
    //
    //  CONSTRUCTOR(S) AND RELATED METHODS
    // ________________________________________________________________

    /**
     * Private constructor
     */
    private QASpec()
    {
        _libDirPaths = new HashMap<String, Boolean>();
        
        _srcReqDirs = new HashSet<String>();
        _srcOptDirs = new HashSet<String>();
        _srcIgnDirs = new HashSet<String>();
        _testReqDirs = new HashSet<String>();
        _testOptDirs = new HashSet<String>();
        _testIgnDirs = new HashSet<String>();

        _illegalImports = new HashMap<String, Set<String>>();

        _unitTestPrefixes = new HashSet<String>();

        _integTestPrefixes = new HashSet<String>();
    }
    
    
    /**
     * Gets the QASpec singleton
     * 
     * @return  singleton specification
     */
    public static QASpec getInstance()
    {
        if (_spec == null) {
            _spec = new QASpec();
        }
        return _spec;
    }


    // ________________________________________________________________
    //
    //  PUBLIC METHODS
    // ________________________________________________________________
    
    /**
     * Adds a path to a project directory
     * 
     * @param type          type of directory (source, test, lib, etc.)
     * @param path          path to directory
     * @param isRelative    whether path is relative to project root (<code>true</code>) or absolute
     *                      (<code>false</code>); applies to lib directories only
     * @param pkg           package associated with directory; applies to source and test
     *                      directories only
     * @return  <code>true</code> if name and path are valid, <code>false</code> otherwise
     */
    public boolean addStructurePath(String type, String path, boolean isRelative, String pkg)
    {
        boolean set = false;
        
        if (type != null) {
            if (ROOT_DIR_TYPE.equals(type)) {
                _rootDirPath = path;
                set = true;
            }
            else if (SRC_DIR_TYPE.equals(type)) {
                _srcDirPath = path;
                _srcPackage = pkg;
                set = true;
            }
            else if (TEST_DIR_TYPE.equals(type)) {
                _testPackage = pkg;
                _testDirPath = path;
                set = true;
            }
            else if (BIN_DIR_TYPE.equals(type)) {
                _binDirPath = path;
                set = true;
            }
            else if (LIB_DIR_TYPE.equals(type)) {
                _libDirPaths.put(path, isRelative);
                set = true;
            }
        }
        
        return set;
    }
    
    
    /**
     * Indicates whether the specified directory is in the specified set of source directories
     * 
     * @param dirName   name of directory to be searched for
     * @param dirType   type of source directories to look through
     * @return  <code>true</code> if dirName is included in the specified source directory set,
     *          <code>false</code> otherwise
     */
    public boolean containsSourceDir(String dirName, DirType dirType)
    {
        boolean contained = false;
        
        Set<String> srcDirs = getSourceDirs(dirType);
        if (srcDirs != null) {
            contained = srcDirs.contains(dirName);
        }
        
        return contained;
    }
    
    
    /**
     * Indicates whether the specified directory is in the specified set of test directories
     * 
     * @param dirName   name of directory to be searched for
     * @param dirType   type of test directories to look through
     * @return  <code>true</code> if dirName is included in the specified test directory set,
     *          <code>false</code> otherwise
     */
    public boolean containsTestDir(String dirName, DirType dirType)
    {
        boolean contained = false;
        
        Set<String> testDirs = getTestDirs(dirType);
        if (testDirs != null) {
            contained = testDirs.contains(dirName);
        }
        
        return contained;
    }
    
    
    /**
     * Get path to bin directory
     * 
     * @return  bin directory path
     */
    public String getBinPath()
    {
        return _binDirPath;
    }
    
    
    /**
     * Gets the set of illegal imports
     * 
     * @return  map of package name to set of illegal imports
     */
    public Map<String, Set<String>> getIllegalImports()
    {
        return new HashMap<String, Set<String>>(_illegalImports);
    }
    
    
    /**
     * Gets the prefix used by integration tests
     * 
     * @return  integration test prefix
     */
    public String getIntegrationDirPrefix()
    {
        return _integTestDirPrefix;
    }
    
    
    /**
     * Gets the name to be used for generated integration test suite files
     * 
     * @return  name to be used for integration test suite files
     */
    public String getIntegrationSuiteName()
    {
        return _integSuiteName;
    }
    
    
    /**
     * Gets the set of prefixes used by integration tests
     * 
     * @return  set of integration test prefixes
     */
    public Set<String> getIntegrationTestPrefixes()
    {
        return new HashSet<String>(_integTestPrefixes);
    }
    
    
    /**
     * Get paths to supplemental library directories and whether they're relative or absolute
     * 
     * @return  map of paths to directories containing supplemental libraries and whether they are
     *          relative to project root
     */
    public Map<String, Boolean> getLibPaths()
    {
        return _libDirPaths;
    }
    
    
    /**
     * Gets the name to be used for generated regression test suite files
     * 
     * @return  name to be used for regression test suite files
     */
    public String getRegressionSuiteName()
    {
        return _regSuiteName;
    }
    
    
    /**
     * Get path to root directory
     * 
     * @return  root directory path
     */
    public String getRootPath()
    {
        return _rootDirPath.endsWith(File.separator) ? _rootDirPath : _rootDirPath + File.separator;
    }
    
    
    /**
     * Gets the set of source directories of the specified type
     * 
     * @param dirType   type of source directories to be returned
     * @return  directory set or null if dirType is invalid or specification has not yet been loaded
     */
    public Set<String> getSourceDirs(DirType dirType)
    {
        Set<String> srcDirs = null;
        
        switch (dirType) {
            case IGNORED:
                srcDirs = new HashSet<String>(_srcIgnDirs);
                break;

            case OPTIONAL:
                srcDirs = new HashSet<String>(_srcOptDirs);
                break;

            case REQUIRED:
                srcDirs = new HashSet<String>(_srcReqDirs);
                break;
                
            default:
                MsgCtrl.errMsgln(this, String.format(DIR_TYPE_ERR, dirType));
        }
        
        return srcDirs;
    }
    
    
    /**
     * Get name of source package
     * 
     * @return  test source name
     */
    public String getSourcePackage()
    {
        return _srcPackage;
    }
    
    
    /**
     * Get path to source directory
     * 
     * @return  source directory path
     */
    public String getSourcePath()
    {
        String srcPath = null;
        
        if (_srcDirPath != null) {
            srcPath = _srcDirPath.endsWith(File.separator)
                    ? _srcDirPath : _srcDirPath + File.separator;
        }
        
        return srcPath;
    }
    
    
    /**
     * Gets the set of test directories of the specified type
     * 
     * @param dirType   type of test directories to be returned
     * @return  directory set or null if dirType is invalid or specification has not yet been loaded
     */
    public Set<String> getTestDirs(DirType dirType)
    {
        Set<String> testDirs = null;
        
        switch (dirType) {
            case IGNORED:
                testDirs = new HashSet<String>(_testIgnDirs);
                break;

            case OPTIONAL:
                testDirs = new HashSet<String>(_testOptDirs);
                break;

            case REQUIRED:
                testDirs = new HashSet<String>(_testReqDirs);
                break;
                
            default:
                MsgCtrl.errMsgln(this, String.format(DIR_TYPE_ERR, dirType));
        }
        
        return testDirs;
    }
    
    
    /**
     * Get name of test package
     * 
     * @return  test package name
     */
    public String getTestPackage()
    {
        return _testPackage;
    }
    
    
    /**
     * Get path to test directory
     * 
     * @return  test directory path
     */
    public String getTestPath()
    {
        return _testDirPath.endsWith(File.separator) ? _testDirPath : _testDirPath + File.separator;
    }
    
    
    /**
     * Gets the set of prefixes used by unit tests
     * 
     * @return  set of unit test prefixes
     */
    public Set<String> getUnitTestPrefixes()
    {
        return new HashSet<String>(_unitTestPrefixes);
    }
    
    
    /**
     * Gets the name to be used for generated unit test suite files
     * 
     * @return  name to be used for unit test suite files
     */
    public String getUnitSuiteName()
    {
        return _unitSuiteName;
    }
    
    
    /**
     * Indicates whether the integration test suite specification has been loaded
     * 
     * @return  <code>true</code> if the specification of integration test suites has been loaded,
     *          <code>false</code> otherwise
     */
    public boolean isIntegrationSuiteSpecLoaded()
    {
        return (_integTestPrefixes != null) && (_integTestDirPrefix != null)
                && (_integSuiteName != null);
    }
    
    
    /**
     * Indicates whether the regression test suite specification has been loaded
     * 
     * @return  <code>true</code> if the specification of regression test suites has been loaded,
     *          <code>false</code> otherwise
     */
    public boolean isRegressionSuiteSpecLoaded()
    {
        return (_regSuiteName != null);
    }
    
    
    /**
     * Indicates whether the unit test suite specification has been loaded
     * 
     * @return  <code>true</code> if the specification of unit test suites has been loaded,
     *          <code>false</code> otherwise
     */
    public boolean isUnitSuiteSpecLoaded()
    {
        return (_unitTestPrefixes != null) && (_unitSuiteName != null);
    }
    
    
    /**
     * Sets specification attributes according to the XML specification
     * 
     * @param specName  name of specification file
     * @param libDirs   comma-separated list of library directories to include in source checking
     * @return  true if specification was read successfully, false otherwise
     */
    public boolean readSpecification(String specName, String libDirs)
    {
        MsgCtrl.msgln("Reading specification (" + specName + ")");
        
        // Process additional library directories, if any
        boolean specRead = processLibraryDirectories(libDirs);
        
        if (specRead) {
            // Get specification from XML
            XmlSpecReader specReader = new XmlSpecReader();
            specRead = specReader.loadSpecification(specName);
        }
        
        return specRead;
    }
    
    
    /**
     * Sets the set of illegal imports
     * 
     * @param illegalImports    map of package to set of illegal imports for that package
     */
    public void setIllegalImports(Map<String, Set<String>> illegalImports)
    {
        _illegalImports = new HashMap<String, Set<String>>(illegalImports);
    }
    
    
    /**
     * Sets the set of source directories of the specified type
     * 
     * @param dirType   type of source directories to be set
     * @param dirSet    set of directories
     * @return  <code>true</code> if directory set was set, <code>false</code> if an error occurred
     */
    public boolean setSourceDirs(DirType dirType, Set<String> dirSet)
    {
        boolean set = false;
        
        switch (dirType) {
            case IGNORED:
                _srcIgnDirs = new HashSet<String>(dirSet);
                set = true;
                break;

            case OPTIONAL:
                _srcOptDirs = new HashSet<String>(dirSet);
                set = true;
                break;

            case REQUIRED:
                _srcReqDirs = new HashSet<String>(dirSet);
                set = true;
                break;
                
            default:
                MsgCtrl.errMsgln(this, String.format(DIR_TYPE_ERR, dirType));
        }
        
        return set;
    }
    
    
    /**
     * Sets the attributes related to unit test suite generation
     * 
     * @param testPrefixes      set of prefixes used by integration test classes
     * @param testDirPrefix     prefix used by integration test directories
     * @param integSuiteName    name to be used for generated integration test suite files
     * @param regSuiteName      name to be used for generated regression test suite files
     */
    public void setRegressionSuiteAttributes(Set<String> testPrefixes, String testDirPrefix,
            String integSuiteName, String regSuiteName)
    {
        _integTestPrefixes = (testPrefixes == null) ? null : new HashSet<String>(testPrefixes);
        _integTestDirPrefix = testDirPrefix;
        _integSuiteName = integSuiteName;
        _regSuiteName = regSuiteName;
    }
    
    
    /**
     * Sets the set of test directories of the specified type
     * 
     * @param dirType   type of test directories to be set
     * @param dirSet    set of directories
     * @return  <code>true</code> if directory set was set, <code>false</code> if an error occurred
     */
    public boolean setTestDirs(DirType dirType, Set<String> dirSet)
    {
        boolean set = false;
        
        switch (dirType) {
            case IGNORED:
                _testIgnDirs = new HashSet<String>(dirSet);
                set = true;
                break;

            case OPTIONAL:
                _testOptDirs = new HashSet<String>(dirSet);
                set = true;
                break;

            case REQUIRED:
                _testReqDirs = new HashSet<String>(dirSet);
                set = true;
                break;
                
            default:
                MsgCtrl.errMsgln(this, String.format(DIR_TYPE_ERR, dirType));
        }
        
        return set;
    }
    
    
    /**
     * Sets the attributes related to unit test suite generation
     * 
     * @param testPrefixes  set of prefixes used by unit test classes
     * @param suiteName     name to be used for generated unit test suite files
     */
    public void setUnitSuiteAttributes(Set<String> testPrefixes, String suiteName)
    {
        _unitTestPrefixes = (testPrefixes == null) ? null : new HashSet<String>(testPrefixes);
        _unitSuiteName = suiteName;
    }
    
    
    /**
     * Processes additional library directories specified on the command line
     * 
     * @param libDirs   comma-separated list of library directories
     * @return  <code>true</code> if all directories were processed, <code>false</code> if one or
     *          more directories was invalid
     */
    private boolean processLibraryDirectories(String libDirs)
    {
        boolean libDirsProcessed = true;
        
        // If null, no library directories to process
        if (libDirs != null) {
            for (String dir : libDirs.split(LIB_DIR_SEPARATOR)) {
                libDirsProcessed = libDirsProcessed
                    && addStructurePath(LIB_DIR_TYPE, dir, false, null);
            }
        }
        
        return libDirsProcessed;
    }
    
}   // end ofQASpec class
