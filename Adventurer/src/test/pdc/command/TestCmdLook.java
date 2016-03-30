/**
 * TestCmdLook.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdLook;
import test.pdc.FakeNPC;
import chronos.pdc.buildings.Building;
import chronos.test.pdc.buildings.FakeBuilding;

public class TestCmdLook
{
  private static final String EXAMPLE_DESC = "A fat man";
  private static final String EXAMPLE_NAME = "Bob";
  private static final FakeNPC BOB = new FakeNPC(EXAMPLE_NAME, EXAMPLE_DESC);
  private static final String BUILDING_DESC = "Building Desc";
  private static final String FAKE_BUILDING = "FakeBuilding";
  private static final FakeBuilding BUILDING = new FakeBuilding(FAKE_BUILDING, BUILDING_DESC);
  private static final FakeBuilding BOBLESS_BUILDING =
      new FakeBuilding(FAKE_BUILDING, BUILDING_DESC);

  private CmdLook _cmdLook;
  private FakeBuildingDisplayCiv _bdciv = new FakeBuildingDisplayCiv();
  private final List<String> bobList = new ArrayList<String>();
  private final List<String> fredList = new ArrayList<String>();

  @Before
  public void setUp() throws Exception
  {
    BUILDING.add(BOB);
    _bdciv.setBuilding(BUILDING);
    _bdciv.enterBuilding(FAKE_BUILDING);
    _cmdLook = new CmdLook(_bdciv);
    bobList.add(EXAMPLE_NAME);
    fredList.add("Fred");
  }

  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  @Test
  public void whenNoTargetProvidedGenericLooksIsPerformed()
  {
    _cmdLook.init(new ArrayList<String>());
    _bdciv.setBuilding(BOBLESS_BUILDING);

    _cmdLook.exec();
    assertEquals(BUILDING_DESC, _bdciv._displayedText);
  }

  @Test
  public void whenTargetNotFoundGetTargetNotFoundMessage()
  {
    _cmdLook.init(fredList);
    _bdciv.enterBuilding(FAKE_BUILDING);

    _cmdLook.exec();
    assertEquals(Building.MISSING_PERSON, _bdciv._displayedText);
  }

  @Test
  public void whenPeopleAreInsideBuildingThenTheirNamesAreGiven()
  {
    _cmdLook.init(new ArrayList<String>());

    _cmdLook.exec();
    assertTrue("Expected \"" + _bdciv._displayedText + "\" to contain " + EXAMPLE_NAME,
        _bdciv._displayedText.contains(EXAMPLE_NAME));
  }

  // @Test
  public void whenPersonIsTargetAndInsideBuildingThenDescriptionGiven()
  {
    _cmdLook.init(bobList);

    _cmdLook.exec();
    assertEquals(EXAMPLE_DESC, _bdciv._displayedText);
  }
}
