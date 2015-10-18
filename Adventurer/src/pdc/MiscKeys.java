/**
 * MiscKeys.java Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package pdc;


/**
 *
 * @author Alan Cline
 * @version Oct 1, 2011 // original <br>
 *          Oct 13, 2015 // revised to expand ItemCategory enum keys <br>
 */
public class MiscKeys
{

  /** List of labels for the Hero default lists in the defaultsRegistry */
  public enum HeroOptions {
    HAIR_COLORS, RACES, ITEMS, QUANTITIES;
  };

  /** List of field names for Skill fields displayed on the Hero Panel. */
  public enum SkillFields {
    NAME, DESC, ACTION, RACE, KLASS;
  };

  /** Enum of various category of Inventory Items */
  public enum ItemCategory {
    ARMS, ARMOR, CLOTHING, EQUIPMENT, MAGIC, PROVISION, SPELL_MATERIAL, VALUABLES;
  };


  // TODO: These are the fields for the ArrayList passed to the GUI; enum no longer needed
  /** List of field names for Item fields displayed on the Hero Panel. */
  public enum ItemFields {
    CATEGORY, NAME, QTY, LBWT, OZWT;
  };


  /** List of names (keys) for file data related to the Person */
  public enum FileData {
    PERSON_EXT, RESOURCE_DIR;
  };


  /**
   * Contains the keys to the display fields for the Hero widget
   */
  public enum PersonFileData {
    RESOURCE_DIR, PERSON_EXT, DEFAULT_FILENAME, CHOOSER_LABEL;
  };


  /** Constants used in several literacy references */
  public enum Literacy {
    ILLITERATE, READING, WRITING
  };


} // end of MiscKeys class
