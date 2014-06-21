/**
 * AdvRegistryFactory.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is prohibited. To
 * republish, to post on servers, to reuse, or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. by email: acline@carolla.com
 */


package pdc.registry;

import chronos.pdc.registry.RegistryFactory;

import mylib.pdc.Registry;

/**
 * <Class Description>
 * 
 * @author Alan Cline
 * @version Feb 16, 2014 // original
 */
public class AdvRegistryFactory extends RegistryFactory
{

  static private AdvRegistryFactory _arf = null;


  /**
   * Constructor(s) and Related Methods
   */

  protected AdvRegistryFactory()
  {}


  static public Registry getRegistry(RegKey regtype)
  {
    return AdvRegistryFactory.getInstance().findRegistry(regtype);
  }

  static public AdvRegistryFactory getInstance()
  {
    if (_arf == null) {
      _arf = new AdvRegistryFactory();
    }
    return _arf;
  }


//  protected Registry createRegistry(RegKey regtype)
//  {
//    Registry reg = null;
//    // String regName = "pdc.registry." + regtype + "Registry";
//    String regName = "chronos.pdc.registry." + regtype + "Registry";
//    try {
//      reg = (Registry) Class.forName(regName).newInstance();
//      _regMap.put(regtype, reg);
//
//    } catch (ClassNotFoundException ex) {
//      // Recovery: checking chronos registries
//      reg = RegistryFactory.getRegistry(regtype);
//
//    } catch (IllegalAccessException ex) {
//      System.err.println(" createRegistry(): cannot access specified method: " + ex.getMessage());
//    } catch (IllegalArgumentException ex) {
//      System.err.println(" createRegistry(): found unexpected argument: " + ex.getMessage());
//    } catch (NullPointerException ex) {
//      System.err.println(" createRegistry(): null pointer exception thrown: " + ex.getMessage());
//    } catch (Exception ex) {
//      System.err.println(" createRegistry(): unexpected exception thrown: " + ex.getMessage());
//    }
//    return reg;
//  }

  // ============================================================
  // Public Methods
  // ============================================================


  // ============================================================
  // Private Methods
  // ============================================================
}
// end of AdvHelpRegistryFactory class
