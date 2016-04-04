/**
 * SkillRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import mylib.pdc.Registry;
import chronos.pdc.Chronos;
import chronos.pdc.Skill;


/**
 * Contains a set of skills that a player may assign to his Hero. {@code SkillRegistry} is a
 * read-only singleton and is only initialized once.
 * 
 * @author Alan Cline
 * @version Jan 1 2010 // original <br>
 */
public class SkillRegistry extends Registry<Skill>
{
  /** 8 Skills that are defined for particular races: Name, Description. */
  @SuppressWarnings("serial")
  private static final List<Skill> _racialSkillTable = new ArrayList<Skill>() {{
      add(new Skill("Archery", "Extra skillful with a short or long bow."));
      add(new Skill("Geasing", "Able to detect undeground direction and stone-based traps."));
      add(new Skill("Infravision", "Can see warm bodies in the dark."));
      add(new Skill("Move Silently", "Chance of sneaking about undetected."));
      add(new Skill("Resistance to Charm", "Charm spells rarely effect you."));
      add(new Skill("Resistance to Poison", "Most poisons only moderately effect you."));
      add(new Skill("Resistance to Sleep", "Sleep spells rarely effect you."));
      add(new Skill("Tingling", "Ability to find hidden doors, secret panels, or concealed openings."));
  }};

  // Occupational skills and descriptions for defined occupations
  @SuppressWarnings("serial")
  private static final List<Skill> _occupSkillTable = new ArrayList<Skill>() {{
      add(new Skill("Animal Empathy", "Communicate emotionally with animals"));
      add(new Skill("Appraise Jewelry", "Estimate selling value of gems and jewelry"));
      add(new Skill("Appraise Tapestries", "Estimate selling value of tapestries"));
      add(new Skill("Arcane Knowledge", "+1 INT to recognize things, substances, and potions  "
          + "\n\t --Identify substances (2gp, 1hr)  "
          + "\n\t --Identify potions (2gp 1hr)  "
          + "\n\t --Make acid: half-pint, d4 dmg or dissolve metal (15gp, 1 hr in town only) "
          + "\n\t --Make weak explosive (2d6 dmg (5gp, 1hr) "
          + "\n\t --Make medium explosive (3d8 dmg (20gp, 4hr in town only) "));
      add(new Skill("Balance", "+1 DEX for balancing tasks and saves"));
      add(new Skill("Bluff", "+2 CHR if the lie is only a matter of luck that the listener believes you"));
      add(new Skill("Bowmaking", "in the field if proper materials available:"
          + "\n\t --Make/repair short bow (-1 to hit) (20 gp, 3 days) "
          + "\n\t --Make arrows (-1 damage), need 1 bird for feathers "
          + "(1 gp per 3d4+2 per day) "));
      add(new Skill("Cargo Transport", "knows tack, harness, and transport equipment"
          + "\n\t --Repair wagons"));
      add(new Skill("Cavern Lore", "+1 WIS to guide party through caverns without getting lost"
          + "\n\t --Avoid natural cavern hazards"
          + "\n\t --Identify most kinds of rock ores, +1 INT on rarer ores"
          + "\n\t --Use picks and shovels as +1, +1 weapons"));
      add(new Skill("Climb Walls", "as a Level 1 Thief (%)"));
      add(new Skill("Concentration",
          "+1 Save vs INT to avoid distraction (and spell interruption if spell caster)"));
      add(new Skill("Diplomacy", "+1 CHR for all political negotiating"));
      add(new Skill("Disable Device Skill", "same as Remove Traps as Level 1 Thief (%)"
          + "\n\t --Undo or jam wooden devices or traps at +1"));
      add(new Skill("Escape Artist", "+1 DEX to slip from manacles, ropes, or through tight spaces"));
      add(new Skill("Fast Swim", "Gains +1 BM when moving in water or underwater (normal water penalty = BM/2) "
          + "\n\t --Gets +4 Save when falling into water due to diving "));
      add(new Skill("Financial Brokering", "+1 CHR when negotiating money deals "
          + "\n\t --No fee banking "
          + "\n\t --Gets 10% discount on all transactions in town "));
      add(new Skill("Find Secrets in Woodwork",
          "+10% chance to find secret doors in wall panels, cabinets, etc."));
      add(new Skill("Find Secrets in Stonework", "+10% chance to find openings in stone construction, "
          + "e.g. cavern walls, stone floors, fireplaces "));
      add(new Skill("Find/Set Traps", "for simple traps like snares and deadweights, as a Level 1 Thief (%)"));
      add(new Skill("Gather Information", "+2 CHR to hear rumor when in an inn or similarly crowded place"
          + "\n\t -- Gets +2 CHR to find contact information for a key person"));
      add(new Skill("General Knowledge", "+1 INT on any general question on specific topic "));
      add(new Skill("Hide in Shadows", "as a Level 1 Thief (%)"));
      add(new Skill("Hunting", "20% chance of finding wild game"));
      add(new Skill("Husbandry", "10% chance of catching live animals"
          + "\n\t --From vet skills, ca heal d3 HP human dmg or Slow Poison. Needs herbs"));
      add(new Skill("Identify Plants", "+1 INT on rarer items"));
      add(new Skill("Intimidate", "+1 CHR to get info from a prison or backdown a bully"));
      add(new Skill("Intuit Outdoor Direction", "+1 WIS to know direction of travel when outside"));
      add(new Skill("Intuit Underground Direction", "+1 WIS to know direction when underground"));
      add(new Skill("Jump", "+2 AP for leaping chasms or reaching lower tree branches"));
      add(new Skill("Leatherworking", "Make/repair leather armor (10gp, 3 days)"
          + "\n\t --Make/repair boots or gloves (5gp, 1 day)"));
      add(new Skill("Listening", "as a Level 1 Thief (%)"));
      add(new Skill("Luck", "Gets +1 on all Saves involving luck and risk taking"
          + "\n\t --Gets +2 on all throws involving gaming luck"));
      add(new Skill("Make Raft", "Make a sailing raft. Needs hand axe (3 days)"));
      add(new Skill("Make Weapons", "Make small metal shield (12gp, 4hr)"
          + "\n\t --Make/repair small melee weapons (all at -1 to Hit, -1 dmg):"
          + "\n\t . . dagger (5gp, 2hr); battle axe (5gp, 2hr, need hand axe); spear (5gp, 2hr); "
          + "short sword (no scabbard) (10gp, 1 day)"
          + "\n\t --Make/repair small missile weapons (all at -1 to Hit, -1 dmg): "
          + "throwing axe (5gp, 2hr, need hand axe); "
          + "\n\t . . light xbow bolts (10sp, 15 min); heavy bolts (1gp, 30 min); "
          + "sling bullet or dart (1 gp per 3d6 bullets/4hrs)"));
      add(new Skill("Move Silently", "as a Level 1 Thief (%)"));
      add(new Skill("Natural Knowledge", "+1 WIS for biological or chemical question "
          + "\n\t --Identify substance (3gp, 1hr) "
          + "\n\t --Identify potion (3gp, 1hr) "
          + "\n\t --Detect poison in bottle or by symptoms of person (10gp, 1 hr) "
          + "\n\t --Detect potency and kind of poison after detection (20gp, 1 hr) "
          + "\n\t --Make weak medicinal potions (d4 healing) (10gp, 1 hr) "
          + "\n\t --Make medium medicinal potions (2d6 healing) (20gp, 4 hr in town only) "
          + "\n\t --Make weak poison (L1 poison)( (5gp, 1hr) "
          + "\n\t --Make medium poison (L2 poison)(40gp, 4hr in town only) "));
      add(new Skill("Negotiations", "+1 CHR when negotiating money deals"));
      add(new Skill("Netmaking", "Make/repair 10'x10' net that can provide 10d4 fish per day for food. "
          + " Needs 50' rope (2 days) "));
      add(new Skill("Open Locks", "as a Level 1 Thief (%)"));
      add(new Skill("Painting", "Paint buildings, mix paint. (yep, that's it)."));
      add(new Skill("Pick Pockets", "as a Level 1 Thief (%)"));
      add(new Skill("Predict Weather", "Predict next day weather at +2 WIS"));
      add(new Skill("Read Lips", "Understand about 1 minute of speaker's speech if within 30' "
          + "and knows the speaker's language"));
      add(new Skill("Repair Armor", "in the field if proper materials available:"
          + "\n\t --Make shield, small metal (10gp, 1 day) or small wooden (2 gp, 4 hr) "
          + "\n\t --Convert lantern from open (hooded) to bulls-eye lantern (10gp, 4hr) "
          + "\n\t --Make caltrop from 4 spikes (1gp, 1hr) "));
      add(new Skill("Sense Motive", "+1 WIS to determine if person is lying or bluffing"));
      add(new Skill("Sewing",
          "Make/repair belt (1gp, 1hr), boots (5gp, 1 day), cloak (1gp, 1hr), hat (1gp, 1hr)"));
      add(new Skill("Spot Details", "+2 WIS to notice details such as obscure items in a dim room, "
          + "\n\t or centipedes in a pile of trash"));
      add(new Skill("Train Animals", "Train animals or work teams"));
      add(new Skill("Trapping", "Catch animals alive (20%)"));
      add(new Skill("Tumble", "land softer when falling (reduce dmg by d3) "
          + "\n\t--Dive tumble over opponents at +2 AC "));
      add(new Skill("Water Lore", "+1 WIS to guide party through water areas and avoid natural hazards"));
      add(new Skill("Wilderness Lore", "Guide party through badlands and avoid natural hazards "
          + "\n\t --Navigate outdoor course without getting lost "
          + "\n\t --Survive off the land"));
      add(new Skill("Woodworking", "Make/repair wooden items, e.g. repair xbows (not bows),"
          + "\n\t --Add secret compartments to chests"));
  }};

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used for
   * database
   * 
   * @param init flag to initialize registry for default data if true
   */
  public SkillRegistry()
  {
    super(Chronos.SkillRegPath);
  }

  /**
   * Create the Skill Registry with the tables given, converting each element to a Skill object and
   * saving it in the database.
   */
  @Override
  protected void initialize()
  {
	  _regRW.addAll(_occupSkillTable);
	  _regRW.addAll(_racialSkillTable);
  }


  /**
   * Get a particlar Skill by name
   * 
   * @param name of the Item
   * @return the Item
   */
  public Skill getSkill(String name)
  {
    return get(name);
  }


  /**
   * Retrieve all Skills in the SkillRegistry
   * 
   * @return the skillList
   */
  public List<Skill> getSkillList()
  {
    return getAll();
  }
} // end of SkillRegistry class

