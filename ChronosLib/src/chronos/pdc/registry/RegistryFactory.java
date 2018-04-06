/**
 * RegistryFactory.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */


package chronos.pdc.registry;

import java.util.HashMap;

import mylib.pdc.Registry;


/**
 * Creates registries of various kinds and keeps count of existing registries
 * 
 * @author Alan Cline
 * @version Jan 1, 2014 // original <br>
 *          July 19, 2014 // added method to return already created Registry <br>
 *          July 25, 2014 // added collection functionality <br>
 *          July 15, 2017 // slight modifications to support test class <br>
 *          July 31, 2017 // update per QATool <br>
 *          Marh 19, 2018 // trying to get this working after a long delay <br>
 */
public class RegistryFactory
{
  private HashMap<RegKey, Registry<?>> _regMap = null;

  /** Public list of all possible registries subclasses, in rough dependency order. */
  public enum RegKey {
    HERO("Hero"), // default: No Heroes stored by default
    SKILL("Skill"), // default: 8 racial, and 54 general Skills
    OCP("Occupation"), // default: 26 Occupations plus "None"
    ITEM("Item"), // default 14 Hero, 6 Bank, 11 Inn menu, 5 Rogue, 3 Store
    BLDG("Building"), // default: 4 Guilds, Inn, Jail, Store, Bank, Jewelry, Stable
    NPC("NPC"), // Default: 8 Building Masters and 8 Inn patrons
    TOWN("Town"), // default: Biljur'Baz
    ADV("Adventure"); // default: "The Quest for Rogahn and Zelligar" (Arena = Quasqueton)

    private String _name;

    private RegKey(String nm)
    {
      _name = nm;
    }

    @Override
    public String toString()
    {
      return _name;
    }
  }


  // ============================================================
  // Constructor(s) and Related Methods
  // ============================================================

  // This map is needed by other classes; do not move
  public RegistryFactory()
  {
    _regMap = new HashMap<RegKey, Registry<?>>();
  }

  public void initRegistries()
  {
    _regMap.put(RegKey.SKILL, new SkillRegistry());
    System.err.println("\t Registry Skill");

    _regMap.put(RegKey.OCP, new OccupationRegistry());
    System.err.println("\t Registry Occupation");

    _regMap.put(RegKey.ITEM, new ItemRegistry());
    System.err.println("\t Registry Item");

    _regMap.put(RegKey.BLDG, new BuildingRegistry());
    System.err.println("\t Registry Building");

    _regMap.put(RegKey.TOWN, new TownRegistry());
    System.err.println("\t Registry Town");

    _regMap.put(RegKey.NPC, new NPCRegistry());
    System.err.println("\t Registry NPC");

    _regMap.put(RegKey.ADV, new AdventureRegistry());
    System.err.println("\t Registry Adventure");

    _regMap.put(RegKey.HERO, new HeroRegistry());
    System.err.println("\t Registry Hero");
  }


  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * Get the number of registries created by this {@code RegistryFactory}
   * 
   * @return the number of current Registries
   */
  public int size()
  {
    return _regMap.size();
  }


  /**
   * Return the requested registry, or null if the registry is null or closed
   * 
   * @param regtype one of the canonical immutable Registries defined in
   *        <code>enum RegKey</code>
   * @return an existing registry of the requested type, or null if it doesn't exist or can't
   *         be found
   */
  public Registry<?> getRegistry(RegKey regtype)
  {
    return _regMap.get(regtype);
  }


} // end of RegistryFactory class
