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
 * Creates singleton registries of various kinds
 * 
 * @author Alan Cline
 * @version Jan 1, 2014 // ABC original <br>
 *          July 19, 2014 // ABC added method to return already created Registry <br>
 */
public class RegistryFactory
{
  static private RegistryFactory _rf = null;

  static private HashMap<RegKey, Registry> _regMap = null;

  // ============================================================
  // Public list of all possible registries subclasses
  // ============================================================
  public enum RegKey {
    ADV("Adventure"), BLDG("Building"), ITEM("Item"), NPC("NPC"), OCP("Occupation"),
    SKILL("Skill"), TOWN("Town"); // HELP("AdvHelp");

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


  static private RegistryFactory getInstance()
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
   * Return the requested regsistry, or null if the registry is null or closed
   * 
   * @param regtype one of the canonical immutable Registries defined in <code>enum RegKey</code>
   */
  static public Registry getRegistry(RegKey regtype)
  {
    RegistryFactory factory = RegistryFactory.getInstance();
    return factory.findRegistry(regtype);
  }

  /**
   * Get an existing Registry, else return null; do not create it
   * 
   * @param regtype one of the Registries defined in <code>enum RegKey</code>
   * @return the registry of the specified type, else null if not found
   */
  static public Registry getExisting(RegKey regtype)
  {
    RegistryFactory.getInstance();
    return _regMap.get(regtype);
  }

  /** @return the number of Registries currently created by the {@code RegistryFactory} */
  static public int getNumberOfRegistries()
  {
    return _regMap.size();
  }

  // ============================================================
  // Private Methods
  // ============================================================

  private Registry createRegistry(RegKey regtype)
  {
    Registry reg = null;
    String regName = Chronos.REGISTRY_CLASSPKG + regtype + "Registry";
    try {
      reg = (Registry) Class.forName(regName).newInstance();
      _regMap.put(regtype, reg);

    } catch (ClassNotFoundException ex) {
      System.err.println("createRegistry(): cannot find specified registry: " + ex.getMessage());
    } catch (IllegalAccessException ex) {
      System.err.println("createRegistry(): cannot access specified method: " + ex.getMessage());
    } catch (IllegalArgumentException ex) {
      System.err.println("createRegistry(): found unexpected argument: " + ex.getMessage());
    } catch (NullPointerException ex) {
      System.err.println("createRegistry(): null pointer exception thrown: " + ex.getMessage());
    } catch (Exception ex) {
      System.err.println("createRegistry(): unexpected exception thrown: " + ex.getMessage());
    }
    return reg;
  }


  private Registry findRegistry(RegKey regtype)
  {
    Registry reg = _regMap.get(regtype);
    if (isValidRegistry(reg)) {
      return reg;
    } else {
      return createRegistry(regtype);
    }
  }


  private boolean isValidRegistry(Registry reg)
  {
    if ((reg == null) || (reg.isClosed())) {
      _regMap.remove(reg);
      return false;
    }
    return true;
  }


} // end of RegistryFactory class
