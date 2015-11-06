/**
 * FakeRegistry.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc.registry;

import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Skill;

/**
 * Target for testing the RegistryFactory
 * 
 * @author Alan Cline
 * @version <DL>
 *  <DT>Build 1.0 May 18, 2013 // original <DD>
 *  </DL>
 */
public class FakeRegistry extends Registry<IRegistryElement>
{
    
    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     *         CONSTRUCTOR(S) AND RELATED METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /**
     * Private ctor because this singleton is called from getInstance().
     * Registry filename is used for database
     * 
     * @param   createFlag   if true, will create and initialize the registry; if false, will reload existing one
     */
//    private FakeRegistry(boolean createFlag) 
    private FakeRegistry() 
    {
//        super(Chronos.FakeRegPath, createFlag);
        super(Chronos.FakeRegPath);
    }

    
    /** ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
    *         PUBLIC METHODS
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

//    /** Close db, destroy the dbReadWriter and set this registry to null
//     * @param eraseFile     if true, erase registry file; else not
//     */
//    public void closeRegistry()
//    {
//        super.close();
////        _thisReg = null;
//    }
//    
//
//    public void deleteRegistry()
//    {
//        super.delete();
////        _thisReg = null;
//    }

    
//    /** Get the create flag status which indicates whether registry can overwrite the existing db file 
//     * @return create Flag as false (create locked) or true (ok to create a new registry)
//     */
//    static public boolean getCreateStatus()
//    {
//        return _createLock;
//    }

    
    /** Get the filename to be used to store the registry db file data 
     * @return the reg file name
     */
    static public String getRegFileName()
    {
        System.out.println("\tFakeRegistry.getRegFileName() called!");
        String s = Chronos.FakeRegPath;
        return s;
    }

    
     /** Stub to check that this method was called */
    @Override
    protected void initialize()
    {
        // Indicate the this method was called
        System.out.println("\tFakeRegistry.initialize() called!.");
        // Place Skills into FakeRegistry merely as testable placeholders
        for (int k=0; k < 10; k++) {
            add(new Skill());
        }
    }
    
    
//    /** Set the create flag to allow the registry to be created and initialied with default data
//     * @status  is true to block file re-initialization (and overwriting)
//     */
//    static public void setCreateLock(boolean status)
//    {
//        _createLock = status;
//    }
    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     *      INNER CLASS: MockFakeRegistry for Testing
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Inner class for testing */
    public class MockFakeRegistry
    {
        /** Default constructor */
        public MockFakeRegistry() {}

        
        /** Return the path for the registry file */
        public String getPath()
        {
            return Chronos.FakeRegPath;
        }

        /** Return the path for the registry file */
        public void setPath(String testPath)
        {
            Chronos.FakeRegPath = testPath;
        }

    } // end of MockFakeRegistry class


}   // end of FakeRegistry class

