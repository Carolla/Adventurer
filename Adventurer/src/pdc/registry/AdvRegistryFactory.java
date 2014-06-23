/**
 * AdvRegistryFactory.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package pdc.registry;

import mylib.pdc.Registry;
import chronos.pdc.registry.RegistryFactory;

/**
 * Create the registry that contains all the adventures for Adventurer. 
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
  public AdvRegistryFactory()
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


  // ============================================================
  // Public Methods
  // ============================================================


  // ============================================================
  // Private Methods
  // ============================================================

}
// end of AdvHelpRegistryFactory class
