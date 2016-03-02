/**
 * TestJail.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the Jail methods
 *
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Apr 8, 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestJail
{

    /**
     * Creates the test Jail, but many tests in this class create their own different banks
     */
    @Before
    public void setUp()
    {}

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ TEST METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    @Test
    public void notNeeded()
    {
        
    }
} // end of TestJail class
