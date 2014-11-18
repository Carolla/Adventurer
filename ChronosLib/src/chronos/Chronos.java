/**
 * Chronos.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos;

import mylib.Constants;


/**
 * Contains the global constants and methods that all objects require for the Chronos applications..
 * 
 * @author Alan Cline
 * @version Jun 12, 2010 // original <br>
 *          May 16, 2011 // TAA: added Skill Registry keys <br>
 *          Jun 13, 2011 // TAA: added Occupation Registry keys <br>
 *          Aug 5, 2012 // ABC: Moved universal constants to MyLibrary.Constants <br>
 *          April 20, 2013 // ABC: Reorganized registry path locations <br>
 *          May 3, 2013 // ABC: Added Arena locations, one per db file <br>
 *          June 21, 2014 // ABC: Used CHRONOS_ROOT and added syntactic synonyms <br>
 *          July 19, 2014 // ABC: Refactored after architectural changes to source <br>
 *          July 25, 2014 // ABC: Moved AdvHelpRegistry from Adventurer to shared registry area<br>
 */
public class Chronos
{
  /** Private constructor since this file is mostly constants and an enum */
  private Chronos()
  {};

  /** Synonyms */
  private static final String FS = Constants.FILE_SEPARATOR;

  /**
   * Chronos install directory environment variable for resource root between Quest Master and
   * Adventurer. This is mutable so that an Installer can change its value. <br>
   * Implementation Note: Due to our EGit environment, the project source files and the data files
   * (resources) are in the actual git repository folder, even though Eclipse shows them in the
   * workspace.
   */

  /** Base environment variable for shared file resources path */
  public static String CHRONOS_ROOT = System.getProperty("user.dir"); // + "/eChronos";
  /** Relative path location of all shared resources */
//  public static String RESOURCES_PATH = CHRONOS_ROOT + "/ChronosLib/resources";
  public static String RESOURCES_PATH = CHRONOS_ROOT + "/resources";
  /** Relative path location of Adventurer-specific resources */
  public static String ADV_RESOURCES_PATH = CHRONOS_ROOT + "/Adventurer/resources";

  /** Most registry Classes are located in the ChronosLib shared source package (ChronosLib) */
  public static final String REGISTRY_CLASSPKG = "chronos.pdc.registry.";
  // /** Adventurer-specific registry Classes are located in the an alternate location */
  // public static final String ALT_REGISTRY_CLASSPKG = "adventurer.pdc.registry.";

  /** Path for all data Registries */
  public static final String REGISTRY_PATH = RESOURCES_PATH + FS + "registries" + FS;

  /** These images are in the Adventurer resources directory */
  public static final String ADV_IMAGE_PATH = ADV_RESOURCES_PATH + FS + "images" + FS;
  /** Chared images are in the ChronosLib resources directory */
  public static final String IMAGE_PATH = RESOURCES_PATH + FS + "images" + FS;

  // LOG FOR TESTING
  // static {
  // System.out.println("Chronos Log: ");
  // System.out.println("RESOURCES_PATH = " + RESOURCES_PATH);
  // System.out.println("IMAGE_PATH = " + IMAGE_PATH);
  // System.out.println("ADV_RESOURCES_PATH = " + ADV_RESOURCES_PATH);
  // System.out.println("ADV_IMAGE_PATH = " + ADV_IMAGE_PATH);
  // }

  /** Location of AdventureRegistry */
  public static String AdventureRegPath = REGISTRY_PATH + "Adventures.reg";
  /** Building Registry location. This location can change for test purposes. */
  public static String BuildingRegPath = REGISTRY_PATH + "Buildings.reg";
  /** Item location. This location can change for test purposes. */
  public static String ItemRegPath = REGISTRY_PATH + "Items.reg";
  /** Patron Registry location. This location can change for test purposes. */
  public static String NPCRegPath = REGISTRY_PATH + "NPCs.reg";
  /** Occupation Registry location. This location can change for test purposes. */
  public static String OcpRegPath = REGISTRY_PATH + "Occupations.reg";
  /** Person Registry location. This location can change for test purposes. */
  public static String PersonRegPath = REGISTRY_PATH + "Dormitory.reg";
  /** Skill Registry location. This location can change for test purposes. */
  public static String SkillRegPath = REGISTRY_PATH + "Skills.reg";
  /** Town Registry location. This location can change for test purposes. */
  public static String TownRegPath = REGISTRY_PATH + "Towns.reg";
  /** Adventure Help Registry location. This location can change for test purposes. */
  public static String AdvHelpRegPath = REGISTRY_PATH + "AdvHelpRegistry.reg";

  // LOG FOR TESTING
  static {
    // System.out.println("Chronos Log: ");
    // System.out.println("AdventureRegPath = " + AdventureRegPath);
    // System.out.println("BuildingRegPath = " + BuildingRegPath);
    // System.out.println("ItemRegPath = " + ItemRegPath);
    // System.out.println("NPCRegPath = " + NPCRegPath);
    // System.out.println("OcpRegPath = " + OcpRegPath);
    // System.out.println("PersonRegPath = " + PersonRegPath);
    // System.out.println("SkillRegPath = " + SkillRegPath);
    // System.out.println("TownRegPath = " + TownRegPath);
    // System.out.println("AdvHelpRegPath = " + AdvHelpRegPath);
  }

  /** Fake Registry location for testing purposes. */
  public static String FakeRegPath = REGISTRY_PATH + "FakeStuff.reg";

  /** Location of Arenas (dungeons), one each in their own db file */
  public static final String ArenaPath = RESOURCES_PATH + "data" + FS;
  /** Extension to all Arena filenames */
  public static final String ARENA_EXT = ".dgn";

  /** Extension path to user-generating resources, such as the user characters */
  public static final String USER_RESOURCES = RESOURCES_PATH + "user" + FS;


  /**
   * Default package name for class files. Package names differ from the directories in that they
   * have a dot (.) separator instead of a slash (/) separator
   */
  public static String DEFAULT_PKG = "pdc.";


  /** Location of font file used for name display */
  static public final String RUNIC_FONT_FILE = RESOURCES_PATH + Constants.FILE_SEPARATOR + "fonts"
      + Constants.FILE_SEPARATOR + "RUNE_A.ttf";

  /** Location of font file used for English-runic display */
  static public final String RUNIC_ENGLISH_FONT_FILE = RESOURCES_PATH + Constants.FILE_SEPARATOR
      + "fonts"
      + Constants.FILE_SEPARATOR + "RUNENG1.ttf";

  /** Location of font file used for English-runic, mixed-case display */
  static public final String RUNIC_ENGLISH2_FONT_FILE = RESOURCES_PATH + Constants.FILE_SEPARATOR
      + "fonts"
      + Constants.FILE_SEPARATOR + "RUNENG2.ttf";

  /** Define half standard deviation range boundary for normal distribution */
  static public final double HALF_SIGMA = (1.0 / 6.0) * 100.0;
  /** Weight and height minimums fall about 84% of average (-half-sigma). */
  static public final double MIN_MULTIPLIER = 5.0 / 6.0;
  /** Weight and height maximums rise about 116% of average (+half-sigma). */
  static public final double MAX_MULTIPLIER = 7.0 / 6.0;

  /** No normal character can be below this low range */
  static public int LOW_TRAIT = 8;
  /** No normal character can be above this high range */
  static public int HIGH_TRAIT = 19;
  /** If Person reaches this value for any trait, he is unconscious */
  static public int COMA_TRAIT_VALUE = 3;
  /** Breakpoint trait for positive mods */
  static public int MID_TRAIT = 14;
  /** Average Trait for a Person (using 4d6-d6 rule), used for calculations */
  static public final double AVERAGE_TRAIT = 11.5;


  /** Java lib search constant */
  /** 1 gold piece weight = 2 ounces */
  static public final int GPW = 2;
  /** 1 silver piece weight = 1 ounce */
  static public final int SPW = GPW / 2;

  /**
   * Age and Race and Person constants that should not be defined in one project when needed in a
   * library
   */
  public static enum ATTRIBUTE {
    /**
     * Strength: Physical muscle for fighting and lifting; STR is the prime requisite for a Fighter.
     */
    STR(),

    /**
     * Dexterity: Ability to pick locks, detect and set traps, climb walls, and gives advantage for
     * quiet attacks in battle; DEX is the prime requisite for a Rogue.
     */
    DEX(),

    /**
     * Intelligence: Ability to identify items, magical or otherwise, learn and cast arcane spells;
     * INT is the prime requsite for a Wizard.
     */
    INT(),

    /**
     * Wisdom: Application of knowledge, ability to cast clerical spells, works against undead
     * creatures and to protect against magical attacks; WIS is the prime requisite for a Cleric.
     */
    WIS(),

    /**
     * Constitution: Physical stamina and ability to resist phsycial trauma, such as resisting
     * poison, or cramping up ice cold water.
     */
    CON(),

    /**
     * Charisma: Ability to work with people, negotiate better deals, and partially reflects the
     * appearance of the Person.
     */
    CHR();
  }

  /**
   * Allow the base location of class files to be defined or redirected for testing
   * 
   * @return the current base directory, typically the source directory for the project
   */
  static public String getPackageName()
  {
    return DEFAULT_PKG;
  }

  /**
   * Allow the base location of class files to be defined or redirected for testing
   * 
   * @param pkgName location of file wrto the Chronos root directory
   */
  static public void setPackageName(String pkgName)
  {
    DEFAULT_PKG = pkgName;
  }

} // end of Chronos global class

