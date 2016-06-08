/**
 * CmdInspect.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.command;

import java.util.ArrayList;
import java.util.List;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Building;
import chronos.pdc.command.Command;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;


/**
 * @author Al Cline
 * @version Apr 29, 2016 // original <br>
 */
public class CmdInspect extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the {@code help()} method. */
  static private final String CMD_DESCRIPTION = "Get a near description of a requested NPC.";
  /** Format for this command */
  static private final String CMDFMT = "INPSECT <NPC Name>";
  /** This command starts immediately, requiring no delay. */
  static private final int DELAY = 0;
  /** This command takes so many seconds on the game clock. */
  static private final int DURATION = 5;

  /** Building accesses and displays are controlled by the BuildingDisplayCiv */
  private BuildingDisplayCiv _bldgCiv;
  /** MainframeCiv controls outputs to the IOPanel */
  private MainframeCiv _mfCiv;

  /** Requested name of NPC to Inspect */
  private String _npcName;

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /** Constructor called by the CommandFactory. */
  public CmdInspect(BuildingDisplayCiv bdCiv)
  {
    super("CmdInspect", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    _bldgCiv = bdCiv;
    // _mfCiv = mfciv;
  }


  /*
   * @see chronos.pdc.command.Command#init(java.util.List)
   */
  @Override
  public boolean init(List<String> args)
  {
    // Stub for testing
    _npcName = convertArgsToString(args);
    _isInitialized = true;
    return _isInitialized;
  }

  /*
   * @see chronos.pdc.command.Command#exec()
   */
  @Override
  public boolean exec()
  {
    Building curBldg = _bldgCiv.getBuildingObject();
    ArrayList<NPC> npcList = (ArrayList<NPC>) curBldg.getPatrons();
    for (NPC person : npcList) {
      String personName = person.getName();
      if (personName.equals(_npcName)) {
        _output.displayText(person.getNearDescription());
        break;
      }
    }

    return true;
  }

}
