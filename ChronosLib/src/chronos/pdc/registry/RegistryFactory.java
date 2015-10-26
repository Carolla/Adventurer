/**
 * RegistryFactory.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.registry;

import java.util.HashMap;

import mylib.pdc.Registry;
import chronos.pdc.Command.Scheduler;

/**
 * Creates singleton registries of various kinds and keeps count of existing registries
 * 
 * @author Alan Cline
 * @version Jan 1, 2014 // ABC original <br>
 *          July 19, 2014 // ABC added method to return already created Registry <br>
 *          July 25, 2014 // ABC added collection funtionality <br>
 */
public class RegistryFactory
{
    private HashMap<RegKey, Registry> _regMap = null;

    /** Public list of all possible registries subclasses, in rough dependency order. */
    public enum RegKey
    {
        SKILL("Skill", 35), // default: 8 racial, and 27 general Skills
        OCP("Occupation", 27), // default: 26 Occupations plus "None"
        ITEM("Item", 39), // default 14 Hero, 6 Bank, 11 Inn menu, 5 Rogue, 3 Store
        BLDG("Building", 8), // default: 4 Guilds, Inn, Store, Jail, Bank
        NPC("NPC", 16), // Default: 8 building masters and 8 Inn patrons
        TOWN("Town", 1), // default: Biljur'Baz
        ADV("Adventure", 1); // default: "The Quest for Rogahn and Zelligar" (Arena = Quasqueton)

        private String _name;
        private int _defSize;

        private RegKey(String nm, int size)
        {
            _name = nm;
            _defSize = size;
        }

        @Override
        public String toString()
        {
            return _name;
        }

        /** Return the number of default entries in the corresponding Registry */
        public int getDefaultSize()
        {
            return _defSize;
        }

    }

    /** For creating certain registries */
    private Scheduler _skedder;


    // ============================================================
    // Constructor(s) and Related Methods
    // ============================================================

    public RegistryFactory(Scheduler skedder)
    {
        _skedder = skedder;
        _regMap = new HashMap<RegKey, Registry>();
    }


    public void initRegistries()
    {
        _regMap.put(RegKey.ITEM, new ItemRegistry());
        _regMap.put(RegKey.SKILL, new SkillRegistry());
        _regMap.put(RegKey.OCP, new OccupationRegistry((SkillRegistry) _regMap.get(RegKey.SKILL)));
        _regMap.put(RegKey.NPC, new NPCRegistry());
        _regMap.put(RegKey.BLDG,
                new BuildingRegistry(_skedder, (NPCRegistry) _regMap.get(RegKey.NPC)));
        _regMap.put(RegKey.TOWN, new TownRegistry((BuildingRegistry) _regMap.get(RegKey.BLDG)));
        _regMap.put(RegKey.ADV, new AdventureRegistry());
    }

    // ============================================================
    // Public Methods
    // ============================================================

    /**
     * Close all registries currently open, and remove them from the {@code RegistryFactory}
     * collection
     */
    public void closeAllRegistries()
    {
        for (RegKey key : RegKey.values()) {
            Registry reg = _regMap.get(key);
            if (reg != null) {
                reg.closeRegistry();
            }
        }
    }


    /**
     * Close a registry and remove it from the factory collection
     * 
     * @param key one of the specified registry keys
     */
    public void closeRegistry(RegKey key)
    {
        Registry reg = _regMap.get(key); // get returns null if not found
        if (reg != null) {
            reg.closeRegistry(); // this is the registry's method, not this method it's in
        }
    }


    /**
     * Get the number of registries created by this {@code RegistryFactory}
     * 
     * @return the number of current Registries
     */
    public int getNumberOfRegistries()
    {
        return _regMap.size();
    }


    /**
     * Return the requested regsistry, or null if the registry is null or closed
     * 
     * @param regtype one of the canonical immutable Registries defined in <code>enum RegKey</code>
     * @return an existing registry of the requested type, or null if it doesn't exist or can't be
     *         found
     */
    public Registry getRegistry(RegKey regtype)
    {
        return _regMap.get(regtype);
    }

} // end of RegistryFactory class
