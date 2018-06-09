/**
 * TestCmdLook.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@carolla.com
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

import pdc.command.CmdTalk;
import test.pdc.FakeNPC;
import chronos.test.pdc.buildings.FakeBuilding;

public class TestCmdTalk
{
  private static final FakeNPC BOB = new FakeNPC("Bob", "A fat man");
  private static final FakeBuilding BUILDING =
      new FakeBuilding("FakeBuilding", "Building Description");

  private CmdTalk _cmdTalk;
  private FakeBuildingDisplayCiv _bdciv = new FakeBuildingDisplayCiv();
  private final List<String> bobList = new ArrayList<String>();

  @Before
  public void setUp() throws Exception
  {
    BUILDING.add(BOB);
    _bdciv.setBuilding(BUILDING);
    _cmdTalk = new CmdTalk(_bdciv);
    bobList.add("Bob");
  }

  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  @Test
  public void whenNoTargetNoResult()
  {
    _cmdTalk.init(new ArrayList<String>());

    _cmdTalk.exec();
    assertEquals("", _bdciv._displayedText);
  }

  @Test
  public void whenTargetNotFoundGetTargetNotFoundMessageEmpty()
  {
    List<String> list = new ArrayList<String>();
    list.add("Fred");
    _cmdTalk.init(list);

    _cmdTalk.exec();
    assertEquals("", _bdciv._displayedText);
  }

  @Test
  public void whenTargetFoundResponseIsGiven()
  {
    _cmdTalk.init(bobList);

    _cmdTalk.exec();
    assertTrue("Saw \"" + _bdciv._displayedText + "\" instead of text",
        _bdciv._displayedText.length() > 0);
  }
}
