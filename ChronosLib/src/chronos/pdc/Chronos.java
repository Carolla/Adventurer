/**
 * Chronos.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@carolla.com
 */

package chronos.pdc;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import mylib.Constants;
import mylib.MsgCtrl;


/**
 * Contains the global constants and methods that all objects require for the Chronos
 * applications..
 * 
 * @author Alan Cline
 * @version Jun 12, 2010 // original <br>
 *          May 16, 2011 // TAA: added Skill Registry keys <br>
 *          Jun 13, 2011 // TAA: added Occupation Registry keys <br>
 *          Aug 5, 2012 // Moved universal constants to MyLibrary.Constants <br>
 *          April 20, 2013 // Reorganized registry path locations <br>
 *          May 3, 2013 // Added Arena locations, one per db file <br>
 *          June 21, 2014 // Used CHRONOS_ROOT and added syntactic synonyms <br>
 *          July 19, 2014 // Refactored after architectural changes to source <br>
 *          July 25, 2014 // Moved AdvHelpRegistry from Adventurer to shared registry area <br>
 *          Dec 7, 2014 // Moved platform-independent directory variables to Contants.java <br>
 *          Oct 28, 2015 // Added Hero registry path <br>
 *          Aug 5, 2017 // revised to point to directory of Hero files <br>
 *          May 12, 2018 // Added Gussian tolerance constant for stat comparisons <br>
 */
public class Chronos
{
  /** Private constructor since this file is mostly constants and an enum */
  private Chronos()
  {};


  /** Synonyms */
  public static final String FS = Constants.FS;

  /**
   * Chronos install directory environment variable for resource root between Quest Master and
   * Adventurer. This is mutable so that an Installer can change its value. <br>
   * Implementation Note: Due to our EGit environment, the project source files and the data
   * files (resources) are in the actual git repository folder, even though Eclipse shows them
   * in the workspace.
   */

  /** Initializer for CHRONOS_ROOT static variable */
  private static String find_eChronosRootDir()
  {
    String fileName = null;
    try {
      fileName = new File("..").getCanonicalPath();
    } catch (IOException e) {
      System.err.println("Unable to locate CHRONOS_ROOT");
      System.exit(-1);
    }
    return fileName;
  }

  /** Base environment variable for shared file resources path */
  public static String ECHRONOS_ROOT = find_eChronosRootDir() + FS;

  /** Relative path location of all shared resources */
  public static String RESOURCES_PATH = ECHRONOS_ROOT + "resources";

  public static String CHRONOS_LIB_RESOURCES_PATH = ECHRONOS_ROOT + "ChronosLib" + FS + "resources";
  /** Relative path location of Adventurer-specific resources */
  public static String ADV_RESOURCES_PATH = ECHRONOS_ROOT + "Adventurer" + FS + "resources";
  /** Most registry Classes are located in the ChronosLib shared source package (ChronosLib) */
  public static final String REGISTRY_CLASSPKG = "chronos.pdc.registry.";

  /** Path for all data Registries */
  public static final String ARENA_PATH = CHRONOS_LIB_RESOURCES_PATH + FS + "data" + FS;

  public static final String REGISTRY_PATH = CHRONOS_LIB_RESOURCES_PATH + FS + "registries" + FS;
  /** These images are in the Adventurer resources directory */
  public static final String ADV_IMAGE_PATH = ADV_RESOURCES_PATH + FS + "images" + FS;

  // LOG FOR TESTING
  static {
    // System.out.println("Chronos Log: ");
    // System.out.println("CHRONOS_LIB_RESOURCES_PATH = " + CHRONOS_LIB_RESOURCES_PATH);
    // System.out.println("ADV_RESOURCES_PATH = " + ADV_RESOURCES_PATH);
    // System.out.println("ADV_IMAGE_PATH = " + ADV_IMAGE_PATH);
  }

  // public static String HeroRegPath = REGISTRY_PATH + "Dormitory.reg";
  public static String HeroRegPath = REGISTRY_PATH + "Dormitory/";
  public static String AdventureRegPath = REGISTRY_PATH + "Adventures.reg";
  public static String BuildingRegPath = REGISTRY_PATH + "Buildings.reg";
  public static String ItemRegPath = REGISTRY_PATH + "Items.reg";
  public static String NPCRegPath = REGISTRY_PATH + "NPCs.reg";
  public static String OcpRegPath = REGISTRY_PATH + "Occupations.reg";
  // public static String PersonRegPath = REGISTRY_PATH + "Dormitory.reg";
  public static String SkillRegPath = REGISTRY_PATH + "Skills.reg";
  public static String TownRegPath = REGISTRY_PATH + "Towns.reg";
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

  /**
   * Default package name for class files. Package names differ from the directories in that
   * they have a dot (.) separator instead of a slash (/) separator
   */
  public static String DEFAULT_PKG = "pdc.character.";

  /** Generic font folder */
  public static final String FONT_PATH = CHRONOS_LIB_RESOURCES_PATH + FS + "fonts" + FS;
  /** Location of font file used for name display */
  static public final String RUNIC_FONT_FILE = RESOURCES_PATH + FS + "fonts" + FS + "RUNE_A.ttf";
  /** Location of font file used for English-runic display */
  static public final String RUNIC_ENGLISH_FONT_FILE = FONT_PATH + "RUNENG1.ttf";
  /** Location of font file used for English-runic, mixed-case display */
  static public final String RUNIC_ENGLISH2_FONT_FILE = FONT_PATH + "RUNENG2.ttf";
  /** Global standard font for buttons and widgets */
  static public final Font STANDARD_FONT = new Font("Tahoma", Font.PLAIN, 24);
  /** Global Runic font for user interactions */

  static public final Font RUNIC_FONT = makeRunicFont(14f);

  /**
   * Create a Runic font that simulates English letters. <br>
   * Warning: Be careful of character selection and float size; round-up errors for
   * {@code float} sizes can cause overruns on displayed Components.
   * 
   * @return the Font class
   */
  static public Font makeRunicFont(float fontHt)
  {
    Font font = null;
    try {
      Font newFont =
          Font.createFont(Font.TRUETYPE_FONT, new File(Chronos.RUNIC_ENGLISH2_FONT_FILE));
      font = newFont.deriveFont(fontHt);
    } catch (Exception e) {
      MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
    }
    return font;
  }

  /** Define half standard deviation range boundary for normal distribution */
  static public final double HALF_SIGMA = (1.0 / 6.0) * 100.0;
  /** Weight and height minimums fall about 84% of average (-half-sigma). */
  static public final double MIN_MULTIPLIER = 5.0 / 6.0;
  /** Weight and height maximums rise about 116% of average (+half-sigma). */
  static public final double MAX_MULTIPLIER = 7.0 / 6.0;
  /** Allowing a 2% tolerance about the mean for Gaussian populations. */
  static public final double TOLERANCE = 0.02;

  /** Average Trait for a Person (using 4d6-d6 rule), used for calculations */
  static public final int AVERAGE_TRAIT = 11;

  /**
   * Some things apply across all Races, e.g. Body Type descriptors. The following height and
   * weight ranges are dubbed "standard" (human) because what is "short" and "tall" is a human
   * perspective.
   */
  public static final int STD_MIN_HEIGHT = 54;
  public static final int STD_MAX_HEIGHT = 70;
  public static final int STD_MIN_WEIGHT = 110;
  public static final int STD_MAX_WEIGHT = 175;

  public static final int LOW_TRAIT = 8;
  public static final int HIGH_TRAIT = 18;

  /** Java lib search constant */
  /** 1 gold piece weight = 2 ounces */
  static public final int GPW = 2;
  /** 1 silver piece weight = 1 ounce */
  static public final int SPW = GPW / 2;

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

