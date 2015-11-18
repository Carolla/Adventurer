/**
 * PersonKeys.java Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.civ;


/**
 * List of field names (keys) for all Person fields, including keys for the Race, Klass, and
 * Inventory fields displayed on the Hero Panel
 * 
 * @author Alan Cline
 * @version Sep 14, 2011 // original <br>
 *          Oct 10, 2015 // added Skill enums <br>
 */
public enum PersonKeys {
  // First group contains the input data when a Person is created
  NAME,
  GENDER,
  HAIR_COLOR,
  KLASSNAME,
  OCCUPATION,
  RACENAME,
  ABILITY_SCORES,

  // Prime traits and their mods
  STR,
  TO_HIT_MELEE,
  DAMAGE,
  WT_ALLOW,
  LOAD,
  INT,
  TO_KNOW,
  CURRENT_MSP,
  MAX_MSP,
  MSP_PER_LEVEL,
  SPELLS_KNOWN,
  MAX_LANGS,
  WIS,
  MAM,
  CURRENT_CSP,
  MAX_CSP,
  CSP_PER_LEVEL,
  TURN_UNDEAD,
  CON,
  HP_MOD,
  RMR,
  DEX,
  TO_HIT_MISSLE,
  AC_MOD,
  CHR,

  // This group contains the remaining non-lethal combat values
  AP,
  OVERBEARING,
  GRAPPLING,
  PUMMELING,
  SHIELD_BASH,

  // This group contains the remaining displayable set of key values for the Person
  AC,
  AC_MAGIC,
  DESCRIPTION,
  GOLD,
  GOLD_BANKED,
  HEIGHT,
  HP,
  HP_MAX,
  HUNGER,
  INVENTORY,
  LANGUAGES,
  LITERACY,
  LEVEL,
  SILVER,
  SPEED,
  WEIGHT,
  XP;

}; // end of PersonKeys enum class

