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
import chronos.Chronos;

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
  static private RegistryFactory _rf = null;

  private HashMap<RegKey, Registry> _regMap = null;

  /** Public list of all possible registries subclasses */
  public enum RegKey {
    ADV("Adventure"), BLDG("Building"), ITEM("Item"), NPC("NPC"), OCP("Occupation"),
    SKILL("Skill"), TOWN("Town");

    private RegKey(String nm)
    {
      _name = nm;
    }

    @Override
    public String toString()
    {
      return _name;
    }

    private String _name = null;
  }


  // ============================================================
  // Constructor(s) and Related Methods
  // ============================================================

  private RegistryFactory()
  {
    _regMap = new HashMap<RegKey, Registry>();
  }


  /** Retrieve or create the factory class, a collection of all Registries */
  static public RegistryFactory getInstance()
  {
    if (_rf == null) {
      _rf = new RegistryFactory();
    }
    return _rf;
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
        _regMap.remove(key);
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
      _regMap.remove(key);
    }
  }


  /**
   * Creates a registry of the given type. Registry location defaults to ChronosLib resources.
   * Registry is stored for quick access in this factory
   * 
   * @param regtype defined in {@code RegistryFactory.RegKey enum}
   * @return the requested Registry
   */
  public Registry createRegistry(RegKey regtype)
  {
    Registry reg = null;
    String regName = Chronos.REGISTRY_CLASSPKG + regtype + "Registry";
    try {
      reg = (Registry) Class.forName(regName).newInstance();
      // System.err.println("RegistryFactory.createRegistry() created = " + reg.toString());
      _regMap.put(regtype, reg);

      // TODO Figure out how to suppress these exception messages during testing
    } catch (ClassNotFoundException ex) {
      System.err.println("createRegistry(): Class.forName() cannot find specified registry: "
          + ex.getMessage());
    } catch (IllegalAccessException ex) {
      System.err.println("createRegistry(): cannot access specified method: " + ex.getMessage());
    } catch (IllegalArgumentException ex) {
      System.err.println("createRegistry(): found unexpected argument: " + ex.getMessage());
    } catch (NullPointerException ex) {
      System.err.println("createRegistry(): null pointer exception thrown: " + ex.getMessage());
    } catch (InstantiationException ex) {
      System.err.println("createRegistry(): class cannot be created with newInstance(): "
          + ex.getMessage());
    } catch (Exception ex) {
      System.err.println("createRegistry(): possible database closed exception thrown: "
          + ex.getMessage());
    }
    return reg;
  }


  /**
   * Close all registries and delete their .reg files
   */
  public void deleteAllRegistries()
  {
    for (RegKey key : RegKey.values()) {
      Registry reg = _regMap.get(key);
      reg.closeRegistry();
      _regMap.remove(reg);
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
    Registry reg = getExisting(regtype);
    if (reg == null) {
      reg = createRegistry(regtype);
    }
    // System.err.println("RegistryFactory.getRegistry() = " + reg.toString());
    return reg;
  }


  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Get an existing Registry, else return null; do not create it
   * 
   * @param regtype one of the Registries defined in <code>enum RegKey</code>
   * @return the registry of the specified type, else null if not found
   */
  private Registry getExisting(RegKey regtype)
  {
    return _regMap.get(regtype);
  }


  // private Registry findRegistry(RegKey regtype)
  // {
  // Registry reg = _regMap.get(regtype);
  // if (isValidRegistry(reg)) {
  // return reg;
  // } else {
  // return createRegistry(regtype);
  // }
  // }


  // private boolean isValidRegistry(Registry reg)
  // {
  // if ((reg == null) || (reg.isClosed())) {
  // _regMap.remove(reg);
  // return false;
  // }
  // return true;
  // }


} // end of RegistryFactory class
