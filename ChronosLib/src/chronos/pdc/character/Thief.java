/**
 * Thief.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import java.util.ArrayList;

import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 *          Oct 17, 2015 // added klass-specific inventory items <br>
 */
public class Thief extends Klass
{
  /** Starting die and initial free HP for klass */
  private static final String _hitDie = "d6";
  private static final String _startingGold = "2d6";

  /** Indices into the Hero's prime traits */
  public enum TSKILL {
    OPEN_SECRET_DOORS,
    PICK_POCKETS,
    OPEN_LOCKS,
    FIND_REMOVE_TRAPS,
    MOVE_SILENTLY,
    HIDE_IN_SHADOWS,
    LISTENING,
    CLIMB_WALLS,
    BACK_ATTACK
  };

  /** Keys in the thief skills */
  public String[] _skillName = {"Find/Open Secret Doors", "Pick Pockets",
      "Open Locks", "Find/Remove/Make Traps", "Move Silently",
      "Hide in Shadows", "Listening", "Climb Walls",
      "Back Attack +4 (if Move Silently roll succeeds)"};

  private String[] _thiefItems = {"Thief's Kit", "Dagger"};

  private final int NBR_SKILLS = _skillName.length;
  private String[][] _thiefSkills = new String[NBR_SKILLS][2];

  /**
   * Default constructor, called reflectively by Klass
   * 
   * @param traits
   */
  public Thief(TraitList traits)
  {
    super(traits, ROGUE_CLASS_NAME, PrimeTraits.DEX);
  }

  /** Converts from Skill name, description, and percent change into a string */
  public static ArrayList<String> assignKlassSkills(String[][] thiefSkills)
  {
    ArrayList<String> sk = new ArrayList<String>();

    for (int k = 0; k < thiefSkills.length; k++) {
      sk.add(thiefSkills[k][0] + ": " + thiefSkills[k][1]);
    }
    return sk;
  }

  @Override
  public void addKlassItems(Inventory inven)
  {
    for (String itemName : _thiefItems) {
      inven.addItem(itemName);
    }
  }

  /**
   * Assign the percentages for thief skills for level 1 by dex
   * 
   * @param dex of the Hero
   * @return the list of thief skills (index = 0) with the chance of success (index = 1)
   */
  protected void calcClassMods(int dex)
  {
    // Basic chance per skill for level 1 thief; must be in order of global
    // index constants
    int[] chanceList = {30, 30, 25, 20, 21, 11, 15, 82, 21};

    // Assign the names into the list in corresponding order
    for (int k = 0; k < NBR_SKILLS; k++) {
      _thiefSkills[k][0] = _skillName[k];
      _thiefSkills[k][1] = String.format("%s", chanceList[k]);
    }

    // Adjust skills for dex
    _thiefSkills = adjThiefSkillByDex(_thiefSkills, dex);
  }


  /** Peasants get a fixed 10 pts, augmented later when they join a guild */
  @Override
  public int rollHP()
  {
    return rollHP(_hitDie);
  }

  /** Peasants get a starting number of gold */
  @Override
  public double rollStartingGold()
  {
    return rollStartingGold(_startingGold);
  }


  /** Adjust the basic skills percents by the Hero's DEX value */
  private String[][] adjThiefSkillByDex(String[][] skills, int dex)
  {
    // Adjustments by dex for DEX [9,18]
    int[] dex9 = {0, -15, -10, -10, -20, -10, 0, 0, -20};
    int[] dex10 = {0, -10, -5, -10, -15, -5, 0, 0, -5};
    int[] dex11 = {0, -5, 0, -5, -10, 0, 0, 0, -10};
    int[] dex12 = {0, 0, 0, 0, -5, 0, 0, 0, -5};
    int[] dex16 = {0, 0, 5, 0, 0, 0, 0, 0, 0};
    int[] dex17 = {0, 5, 10, 0, 5, 5, 0, 0, 5};
    int[] dex18 = {0, 10, 15, 5, 10, 10, 0, 0, 10};

    // For purposes of thief skill chances, set thief skills to boundary
    // limits if they exceed them
    dex = (dex < 9) ? 9 : dex;
    dex = (dex > 18) ? 18 : dex;

    // Make adjustments to base chances by DEX value
    if ((dex < 13) || (dex > 15)) {
      switch (dex)
      {
        case (9):
          adjThiefMods(skills, dex9);
          break;
        case (10):
          adjThiefMods(skills, dex10);
          break;
        case (11):
          adjThiefMods(skills, dex11);
          break;
        case (12):
          adjThiefMods(skills, dex12);
          break;
        case (16):
          adjThiefMods(skills, dex16);
          break;
        case (17):
          adjThiefMods(skills, dex17);
          break;
        case (18):
          adjThiefMods(skills, dex18);
          break;
        default:
          System.err
              .println("Unknown DEX found while assigning thief skills: "
                  + dex);
          break;
      }
    }
    // Set back attack skill chance equal to adjusted 'Move Silently' skill
    skills[TSKILL.BACK_ATTACK.ordinal()][1] = skills[TSKILL.MOVE_SILENTLY
        .ordinal()][1];

    return skills;
  }

  // Adjust the thief skills by dex
  private String[][] adjThiefMods(String[][] skills, int[] adjTable)
  {
    for (int k = 0; k < skills.length; k++) {
      int oldChance = Integer.parseInt(skills[k][1]);
      int newChance = oldChance + adjTable[k];
      skills[k][1] = String.format("%s", newChance);
    }
    return skills;
  }

} // end of Thief class
