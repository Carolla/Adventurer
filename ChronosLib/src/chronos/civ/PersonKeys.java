/**
 * PersonKeys.java Copyright (c) 2011, Alan Cline. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */


package chronos.civ;


/**
 * List of field names (keys) for all Person fields, including keys for the Race, Klass, and
 * Inventory fields displayed on the Hero Panel
 * 
 * @author Alan Cline
 * @version Sep 14, 2011 // original <br>
 *          Oct 10, 2015 // added Skill enums <br>
 *          June 2 2017 // minor revisions to support refactoring Peasant Hero <br>
 */
public enum PersonKeys {
  // First group contains the input data when a Person is created
  NAME,
  GENDER,
  HAIR_COLOR,
  RACENAME,
  // Hero is always created as a Peasant first
  KLASSNAME,        // Klass-specific

  // Prime traits and their mods (race-specific)
  STR,
  TO_HIT_MELEE,
  DAMAGE,
  WT_ALLOW,
  LOAD,
  
  INT,
  CURRENT_MSP,    // Klass specific
  MAX_MSP,        // Klass specific
  MSP_PER_LEVEL,  // Klass specific
  SPELLS_KNOWN,   // Klass specific
  MAX_LANGS,

  WIS,
  MDM,
  CURRENT_CSP,    // Klass specific
  MAX_CSP,        // Klass specific
  CSP_PER_LEVEL,  // Klass specific
  TURN_UNDEAD,    // Klass specific
  
  CON,
  HP_MOD,
  RMR,
  
  DEX,
  TO_HIT_MISSLE,  // Klass specific
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
  DESCRIPTION,
  GOLD,         // Klass specific
  GOLD_BANKED,
  HEIGHT,       // gender-specific
  HP,           
  HP_MAX,       // Klass specific
  HUNGER,
  INVENTORY,
  LANGUAGES,    // Race-specific
  LITERACY,
  LEVEL,
  OCCUPATION,
  OCC_DESCRIPTOR,
  SILVER,       // Klass specific
  SKILL,
  SPEED,      // AP and height dependent
  WEIGHT,     // gender-specific
  XP;

}; // end of PersonKeys enum class


