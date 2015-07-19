package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdApproach;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.TownRegistry;

public class TestCmdApproach
{
    // iVars
    private CmdApproach _cmdApproach;
    
    private static FakeBuildingDisplayCiv _bdciv = null;
    private static List<String> _bList = Arrays.asList(TownRegistry.DEF_BUILDING_LIST);

    @Before
    public void setUp() throws Exception
    {
        _bdciv = new FakeBuildingDisplayCiv();
        _cmdApproach = new CmdApproach(_bdciv);
    }

    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    
    /** Normal CmdApproach given building(s) */
    @Test
    public void testInitValidBuilding()
    {
      MsgCtrl.where(this);

      List<String> bNames = new ArrayList<String>();

      // For each building, approach it and check its attributes
      for (int k = 0; k < _bList.size(); k++) {
        String name = _bList.get(k);
        bNames.add(0, name);
        assertTrue(_cmdApproach.init(bNames));

        // Clear out arglist
        bNames.clear();
      }
      MsgCtrl.msgln("\tAll buildings approached successfully.");
    }
    
    /** Error: Approach invalid building for ERRMSG_WRONG_BLDG */
    @Test
    public void testInitInvalidBuilding()
    {
      MsgCtrl.where(this);

      List<String> bNames = new ArrayList<String>();
      // Place an invalid building as the parm
      bNames.add("Winery");
      _bdciv._canApproach = false;
      assertFalse(_cmdApproach.init(bNames));
    } 
    
    /** Error CmdApproach no building specified */
    @Test
    public void testInitWithoutParms()
    {
      MsgCtrl.where(this);

      List<String> bNames = new ArrayList<String>();

      // Now try to approach current Building without a parm
      bNames.clear();
      _bdciv._canApproach = false;
      assertFalse(_cmdApproach.init(bNames));
    }
    
    /** Error: Trying to approach one building from inside another gives ERRMSG_JUMPBLDG */
    @Test
    public void initJumpBuilding()
    {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);
      
      CheckingBuildingDisplayCiv bdCiv = new CheckingBuildingDisplayCiv();
      _cmdApproach = new CmdApproach(bdCiv);
      Building b = new FakeBuilding("Jail");
      bdCiv.setCurrentBuilding(b);

      // Set context to be inside valid building: Jail
      List<String> bNames = new ArrayList<String>();
      bNames.add("Jail");
      
      assertFalse(_cmdApproach.init(bNames));
    } 
    

    @Test
    public void execWithBuildingParamSet()
    {
        // Set context to be inside valid building: Jail
        List<String> bNames = new ArrayList<String>();
        bNames.add("Jail");
        _cmdApproach.init(bNames);
        
        assertTrue(_cmdApproach.exec());
    }
}
