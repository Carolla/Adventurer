/**
 * TestClericsGuild.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc.buildings;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *    Tests the various Guild-specific methods.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       April 16, 2013   // original <DD>
 * </DL>
 */
public class TestClericsGuild
{
    
    /**
     * Creates the test Store, but many tests in this class create their own different stores
     */
    @Before
    public void setUp() 
    {
    }

    @After
    public void tearDown() 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
        
    /** Tests that are not implemented either because tests are not needed
     *  @Not_Needed getKey()                            // getter
     */
    @Test
    public void NotNeeded() { }
    
}       // end of TestStore class
