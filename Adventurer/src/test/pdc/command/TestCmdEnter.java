/**
 * TestCmdEnter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdEnter;
import chronos.pdc.registry.TownRegistry;

/**
 * @author Al Cline
 * @version May 4, 2015 // original <br>
 */
public class TestCmdEnter
{
    private static FakeBuildingDisplayCiv _bdciv = null;

    private CmdEnter _cmdEnter = null;

    private static List<String> _bList;

    @BeforeClass
    public static void doOne()
    {
        _bList = Arrays.asList(TownRegistry.DEF_BUILDING_LIST);
    }
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _bdciv = new FakeBuildingDisplayCiv();
        _cmdEnter = new CmdEnter(_bdciv);
        _bdciv.setBuildingName("");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    // =================================================
    // BEGIN TESTING
    // =================================================

    /** Normal CmdEnter given building(s) */
    @Test
    public void initValidBuilding()
    {
        MsgCtrl.where(this);

        List<String> bNames = new ArrayList<String>();

        // For each building, enter it and check its attributes
        for (int k = 0; k < _bList.size(); k++) {
            String name = _bList.get(k);
            bNames.add(0, name);
            MsgCtrl.msgln("\tEntering Building:\t" + name);
            assertTrue(_cmdEnter.init(bNames));
        }
        MsgCtrl.msgln("\tAll buildings entered successfully.");
    }


    /** Normal CmdEnter default (current) building */
    @Test
    public void initWithoutParms()
    {
        MsgCtrl.where(this);

        List<String> bNames = new ArrayList<String>();

        // Set first building in registry to the current building (in context object)
        String name = _bList.get(0);
        _bdciv.enterBuilding(name);

        // Now try to enter current Building without a parm
        MsgCtrl.msgln("\tEntering current Building (target Building) " + name);
        assertTrue(_cmdEnter.init(bNames));
        
        // Verify
        assertTrue(_cmdEnter.init(bNames));
    }


    /** Error: Enter invalid building for ERR_NOBLDG */
    @Test
    public void initInvalidBuilding()
    {
        MsgCtrl.where(this);

        List<String> bNames = new ArrayList<String>();
        // Place an invalid building as the parm
        bNames.add("Winery");

        _bdciv._canEnter = false;
        assertFalse(_cmdEnter.init(bNames));
    }

    /** Error: Trying to enter one building from inside another gives ERRMSG_JUMPBLDG */
    @Test
    public void initJumpBuilding()
    {
        MsgCtrl.where(this);
        
        CheckingBuildingDisplayCiv bdCiv = new CheckingBuildingDisplayCiv();
        bdCiv.enterBuilding("NotTheJail");
        
        _cmdEnter = new CmdEnter(bdCiv);

        // Set context to be inside valid building: Jail
        List<String> bNames = new ArrayList<String>();
        bNames.add("Jail");

        assertFalse(_cmdEnter.init(bNames));

    }
    
    @Test
    public void execWithBuildingParamSet()
    {
        // Set context to be inside valid building: Jail
        List<String> bNames = new ArrayList<String>();
        bNames.add("Jail");
        _cmdEnter.init(bNames);
        
        assertTrue(_cmdEnter.exec());
    }
    
    @Test
    public void execWithoutParamAndCurrentBuildingSet()
    {
        // Set context to be inside valid building: Jail
        List<String> bNames = new ArrayList<String>();
        _cmdEnter.init(bNames);
        _bdciv.setBuildingName("Arcaneum");
        
        assertTrue(_cmdEnter.exec());
    }
} // end of TestCmdEnter class

