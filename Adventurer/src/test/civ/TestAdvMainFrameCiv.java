/**
 * TestAdvMainFrameCiv.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.civ;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;

import civ.MainframeCiv;

/**
 * Test the various methods in AdvMainframeCiv
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Dec 21, 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestAdvMainFrameCiv
{
    static private MainframeCiv _aciv = null;

    /*
     * ++++++++++++++++++++++++ FIXTURES ++++++++++++++++++++++++++++++++
     */

//    /**
//     * @throws java.lang.Exception
//     */
//    @Before
//    public void setUp() throws Exception
//    {
//        MsgCtrl.errorMsgsOn(true);
//        _aciv = new MainframeCiv(null);
//    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
    }


    /*
     * ++++++++++++++++++++++++PRIVATE METHODS+++++++++++++++++++++++++++++++
     */
}
// end of TestAdvMainFrameCiv class
