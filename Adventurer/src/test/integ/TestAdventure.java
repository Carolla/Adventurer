/**
 * TestAdventure.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.integ;

import mylib.MsgCtrl;

import junit.framework.TestCase;

/**
 * The Adventure object is a container for all the bits and pieces of an adventure.  Normally, an Adventure
 * will contain Heroes, Buildings, Quests, and NPCs.
 * 
 * @author Tim Armstrong
 * @version <DL>
 *          <DT>Build 1.0 Mar, 10 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestAdventure extends TestCase
{

    /**
     * @throws java.lang.Exception
     */
    public void setUp() throws Exception
    {}

    /**
     * @throws java.lang.Exception
     */
    public void tearDown() throws Exception
    {}

    /**
     * Creation of an Adventure
     * 
     * @Normal
     * @Null 
     * @Error 
     */
    public void shouldHaveAnAdventure()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "shouldHaveAnAdventure()");

    }

} // end of TestAdventure class
