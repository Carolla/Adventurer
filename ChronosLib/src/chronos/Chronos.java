/**
 * Chronos.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package chronos;

import mylib.Constants;


/**
 * Contains the global constants and methods that all objects require for the Chronos applications..
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 12, 2010 // original                                                      <DD>
 *          <DT>Build 1.1 May 16, 2011 // TAA: added Skill Registry keys                 <DD>
 *          <DT>Build 1.2 Jun 13, 2011 // TAA: added Occupation Registry keys       <DD>
 *          <DT>Build 2.0 Aug 5, 2012 // ABC: Moved universal constants to MyLibrary.Constants <DD>
 *          <DT>Build 2.1 April 20, 2013 // ABC: Reorganized registry path locations <DD>
 *          <DT>Build 2.2 May 3, 2013 // ABC: Added Arena locations, one per db file <DD>
 *          </DL>
 */
public class Chronos 
{
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

	/** Private constructor since this file is mostly constants and an enum */
	private Chronos() { };

  /**
   * Chronos directory for shared resources between Quest Master and Adventurer
   */
  public static final String CHRONOS_RESOURCES = Constants.HOME_DIR + Constants.FILE_SEPARATOR
          + "workspace" + Constants.FILE_SEPARATOR + "Chronos" + Constants.FILE_SEPARATOR
          + "resources" + Constants.FILE_SEPARATOR;

    /** Chronos directory for shared resources between Quest Master and Adventurer */
    public static final String WORKSPACE = Constants.WORKSPACE;
    /** Platform-agnostic character for file separator */
    public static final String FSEP = Constants.FILE_SEPARATOR;

    /** Absolute root path to all resources, containing images, characters, and other non-source files */
	public static final String RESOURCES = WORKSPACE + Constants.FILE_SEPARATOR
			+ "ChronosLib" + Constants.FILE_SEPARATOR + "resources" + Constants.FILE_SEPARATOR;
	/** All registries are located in the same resource/registries folder */
	public static final String REGISTRIES = 
	                RESOURCES + "registries" + Constants.FILE_SEPARATOR;
    /** All registries classes are located in the same source package */
    public static final String REGISTRY_CLASSPKG = "chronos.pdc.registry.";

    /** Fake Registry location for testing purposes. */
    public static String FakeRegPath = Chronos.REGISTRIES + "FakeStuff.reg";            
    /** Building Registry location. This location can change for test purposes. */
    public static String BuildingRegPath = Chronos.REGISTRIES + "Buildings.reg"; 
    /** Item location. This location can change for test purposes. */
    public static String ItemRegPath = Chronos.REGISTRIES + "Items.reg";
    /** Patron Registry location. This location can change for test purposes. */
    public static String NPCRegPath = Chronos.REGISTRIES + "NPCs.reg";
    /** Occupation Registry location. This location can change for test purposes. */
    public static String OcpRegPath = Chronos.REGISTRIES + "Occupations.reg";
    /** Person Registry location. This location can change for test purposes. */
    public static String PersonRegPath = Chronos.REGISTRIES + "Dormitory.reg";
    /** Skill Registry location. This location can change for test purposes. */
    public static String SkillRegPath = Chronos.REGISTRIES + "Skills.reg";
    /** Town Registry location. This location can change for test purposes. */
    public static String TownRegPath = Chronos.REGISTRIES + "Towns.reg";            
    /** Location of AdventureRegistry */
    public static String AdventureRegPath = Chronos.REGISTRIES + "Adventures.reg";

    /** Current user working directory as relative file system reference.
     * It will refer to the Adventurer or QuestMaster folder. */
    public static String USER_DIR = System.getProperty("user.dir");
    /** Location of HelpRegistry */
    public static String AdventureHelpRegPath = USER_DIR + FSEP + "resources" + FSEP +
                    "help" + FSEP + "AdventureHelp.reg";

    /** Location of Arenas (dungeons), one each in their own db file  */
    public static final String ArenaPath = RESOURCES + "data" + Constants.FILE_SEPARATOR;
    /** Extension to all Arena filenames */
    public static final String ARENA_EXT = ".dgn";

	/** Extension path to user-generating resources, such as the user characters */
	public static final String USER_RESOURCES = 
	                RESOURCES + "user"+ Constants.FILE_SEPARATOR;


	/**
	 * Default package name for class files. Package names differ from the
	 * directories in that they have a dot (.) separator instead of a slash (/)
	 * separator
	 */
	public static String DEFAULT_PKG = "pdc.";


    /** Location of font file used for name display */
    static public final String RUNIC_FONT_FILE = 
            RESOURCES + Constants.FILE_SEPARATOR + "fonts" + Constants.FILE_SEPARATOR + "RUNE_A.ttf";
    
    /** Location of font file used for English-runic display */
    static public final String RUNIC_ENGLISH_FONT_FILE = 
            RESOURCES + Constants.FILE_SEPARATOR + "fonts" + Constants.FILE_SEPARATOR + "RUNENG1.ttf";
    
    /** Location of font file used for English-runic, mixed-case display */
    static public final String RUNIC_ENGLISH2_FONT_FILE = 
            RESOURCES + Constants.FILE_SEPARATOR + "fonts" + Constants.FILE_SEPARATOR + "RUNENG2.ttf";

	/** Define half standard deviation range boundary for normal distribution */
	static public final double HALF_SIGMA = (1.0 / 6.0) * 100.0;
	/** Weight and height minimums fall about 84% of average (-half-sigma). */
	static public final double MIN_MULTIPLIER = 5.0 / 6.0;
	/** Weight and height maximums rise about 116% of average (+half-sigma). */
	static public final double MAX_MULTIPLIER = 7.0 / 6.0;

	/** Java lib search constant */
	/** 1 gold piece weight = 2 ounces */
	static public final int GPW = 2;
	/** 1 silver piece weight = 1 ounce */
	static public final int SPW = GPW / 2;
    /**
	 * Age and Race and Person constants that should not be defined in one
	 * project when needed in a library
	 */
	public static enum ATTRIBUTE {
		/**
		 * Strength: Physical muscle for fighting and lifting; STR is the prime
		 * requisite for a Fighter.
		 */
		STR(),

		/**
		 * Dexterity: Ability to pick locks, detect and set traps, climb walls,
		 * and gives advantage for quiet attacks in battle; DEX is the prime
		 * requisite for a Rogue.
		 */
		DEX(),

		/**
		 * Intelligence: Ability to identify items, magical or otherwise, learn
		 * and cast arcane spells; INT is the prime requsite for a Wizard.
		 */
		INT(),

		/**
		 * Wisdom: Application of knowledge, ability to cast clerical spells,
		 * works against undead creatures and to protect against magical
		 * attacks; WIS is the prime requisite for a Cleric.
		 */
		WIS(),

		/**
		 * Constitution: Physical stamina and ability to resist phsycial trauma,
		 * such as resisting poison, or cramping up ice cold water.
		 */
		CON(),

		/**
		 * Charisma: Ability to work with people, negotiate better deals, and
		 * partially reflects the appearance of the Person.
		 */
		CHR();
	}

	/**
	 * Allow the base location of class files to be defined or redirected for
	 * testing
	 * 
	 * @return the current base directory, typically the source directory for
	 *         the project
	 */
	static public String getPackageName() {
		return DEFAULT_PKG;
	}

	/**
	 * Allow the base location of class files to be defined or redirected for
	 * testing
	 * 
	 * @param pkgName
	 *            location of file wrto the Chronos root directory
	 */
	static public void setPackageName(String pkgName) {
		DEFAULT_PKG = pkgName;
	}

} // end of Chronos global class

