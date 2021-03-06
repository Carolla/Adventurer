/**
 * TA14_TalkToPatron.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static chronos.pdc.buildings.Building.DEFAULT_BUILDINGS;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Inn;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Format: {@code TALK (TO)} <target> starts a conversation with the target.
 * 
 * @author Tim Armstrong
 * @version 1 // original <br>
 */
public class TA14_TalkToPatron extends IntegrationTest
{
  @Test
  public void talkToAllBuildingMasters()
  {
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String buildingName = DEFAULT_BUILDINGS[i][0];
      String buildingMaster = DEFAULT_BUILDINGS[i][1];

      _cp.receiveCommand("Enter " + buildingName);
      assertTrue("Couldn't talk to " + buildingMaster + " in " + buildingName,
          _cp.receiveCommand("Talk to " + buildingMaster));

      resetBuildingState();
    }
  }

  @Test
  public void canTalkToPatronsInInn()
  {
    BuildingRegistry bReg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);
    Inn inn = (Inn) bReg.getBuilding("Ugly Ogre Inn");

    _cp.receiveCommand("Enter Ugly Ogre Inn");

    for (NPC npc : inn.getPatrons()) {
      assertTrue("Couldn't talk to " + npc.getName(), _cp.receiveCommand("Talk to " + npc.getName()));
    }
  }
}
