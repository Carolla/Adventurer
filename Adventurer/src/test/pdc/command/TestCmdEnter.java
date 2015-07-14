/**
 * TestCmdEnter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdEnter;
import pdc.command.CmdEnter.MockCmdEnter;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.TownRegistry;
import civ.BuildingDisplayCiv;

/**
 * @author Al Cline
 * @version May 4, 2015 // original <br>
 */
public class TestCmdEnter
{
    public class CheckingBuildingDisplayCiv extends BuildingDisplayCiv
    {
        private String _currentBuilding;

        @Override
        public void setCurrentBuilding(Building b)
        {
            _currentBuilding = b.getName();
        }
        
        @Override
        public boolean canEnter(String bldgName)
        {
            return _currentBuilding.equals(bldgName);
        }
    }

    public class FakeBuilding extends Building
    {

        public FakeBuilding(String name)
        {
            super(name, "", "", "", "");
        }

        @Override
        public boolean equals(IRegistryElement target)
        {
            return false;
        }

        @Override
        public String getName()
        {
            return _name;
        }
    }

    private static FakeBuildingDisplayCiv _bdciv = null;

    private CmdEnter _cmdEnter = null;
    private MockCmdEnter _mock = null;

    private static List<String> _bList = Arrays.asList(TownRegistry.DEF_BUILDING_LIST);

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _bdciv = new FakeBuildingDisplayCiv();
        _cmdEnter = new CmdEnter(_bdciv);
        _mock = _cmdEnter.new MockCmdEnter();

        // Ensure that current building is null to start
        _bdciv.setCurrentBuilding(new FakeBuilding(""));
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

    /** Normal verify CmdEnter constructor */
    @Test
    public void ctorVerified()
    {
        MsgCtrl.where(this);

        int delay = 0;
        int duration = 10;
        String cmdfmt = "ENTER [Building Name]";
        MsgCtrl.msgln("\tCommand format: " + _mock.getCmdFormat());

        assertEquals(delay, _mock.getDelay());
        assertEquals(duration, _mock.getDuration());
        assertEquals(cmdfmt, _mock.getCmdFormat());
    }


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

            // Verify target building
            String tBldg = _mock.getTargetBldg();
            assertEquals(tBldg, name);
            // Clear out arglist
            bNames.remove(0);
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
        Building b = new FakeBuilding(_bList.get(0));
        assertNotNull(b);
        _bdciv.setCurrentBuilding(b);

        // Now try to enter current Building without a parm
        bNames.clear();
        MsgCtrl.msgln("\tEntering current Building (target Building) " + b.getName());
        assertTrue(_cmdEnter.init(bNames));
        // Verify
        String  tBldg = _mock.getTargetBldg();
        assertTrue(_cmdEnter.init(bNames));
        assertTrue(tBldg.isEmpty());
    }


    /** Error: Enter invalid building for ERR_NOBLDG */
    @Test
    public void initInvalidBuilding()
    {
        MsgCtrl.where(this);

        List<String> bNames = new ArrayList<String>();
        // Place an invalid building as the parm
        bNames.add("Winery");
        MsgCtrl.errMsg("\tExpected error: ");
        _bdciv._canEnter = false;
        assertFalse(_cmdEnter.init(bNames));
    }

    /** Error: Trying to enter one building from inside another gives ERRMSG_JUMPBLDG */
    @Test
    public void initJumpBuilding()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.where(this);
        
        CheckingBuildingDisplayCiv bdCiv = new CheckingBuildingDisplayCiv();
        _cmdEnter = new CmdEnter(bdCiv);

        // Set context to be inside valid building: Jail
        List<String> bNames = new ArrayList<String>();
        bNames.add("Jail");

        bdCiv.setCurrentBuilding(new FakeBuilding("NotTheJail"));
        assertFalse(_cmdEnter.init(bNames));

    }


} // end of TestCmdEnter class

