/**
 * TestSkillRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc.registry;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import chronos.pdc.registry.SkillRegistry;


/**
 * Make sure all the right skills are there.
 */
public class TestSkillRegistry
{
  private static final String[] skills = {"General Knowledge", "Concentration", "Diplomacy",
      "Climb Walls", "Balance", "Escape Artist", "Jump", "Tumble", "Arcane Knowledge",
      "Natural Knowledge", "Repair Armor", "Financial Brokering", "Appraise Jewelry", "Bowmaking",
      "Find Secrets in Woodwork", "Identify Plants", "Predict Weather", "Netmaking", "Fast Swim",
      "Hide in Shadows", "Move Silently", "Wilderness Lore", "Intuit Outdoor Direction",
      "Spot Details", "Negotiations", "Cargo Transport", "Train Animals", "Luck", "Pick Pockets",
      "Open Locks", "Bluff", "Sense Motive", "Hunting", "Find/Set Traps", "Move Silently",
      "Hide in Shadows", "Spot Details", "Intimidate", "Listening", "Husbandry", "Animal Empathy",
      "Train Animals", "Negotiations", "Sense Motive", "Gather Information", "Read Lips",
      "Appraise Jewelry", "Open Locks", "Leatherworking", "Painting", "Gather Information",
      "Find Secrets in Stonework", "Intuit Underground Direction", "Cavern Lore",
      "Find Secrets in Stonework", "Predict Weather", "Water Lore", "Intuit Outdoor Direction",
      "Spot Details", "Fast Swim", "Make Raft", "Fast Swim", "Make Raft", "Fast Swim", "Sewing",
      "Gather Information", "Financial Brokering", "Sense Motive", "Diplomacy", "Trapping",
      "Find/Set Traps", "Move Silently", "Open Locks", "Hide in Shadows", "Spot Details",
      "Wilderness Lore", "Disable Device Skill", "Make Weapons", "Appraise Tapestries",
      "Woodworking", "Find Secrets in Woodwork", "Disable Device Skill" }; //, "No special skills"};

  @Test
  public void validateSkills()
  {
    SkillRegistry reg = new SkillRegistry();
    for (String s : skills) {
      assertNotNull(s + " was null", reg.getSkill(s));
    }
  }

} // end of TestSkillRegistry
