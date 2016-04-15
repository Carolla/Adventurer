/**
 * TA00a_Initalize.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Integration test for initializing the application class: ensure that all Registries are created.
 * 
 * @author Al Cline
 * @version July 19, 2014 // ABC original <br>
 *          April 5 2016 // updated for HeoRegistry as only persistent registry <br>
 */
public class TA00a_Initialize extends IntegrationTest
{
  /**
   * INFO ONLY: Keys used by RegistryFactory public enum RegKey { ADV("Adventure"),
   * BLDG("Building"), HERO ("Hero"), ITEM("Item"), NPC("NPC"), OCP("Occupation"), SKILL("Skill"),
   * TOWN("Town");
   */

  // ============================================================
  // Integration Test
  // ============================================================

  @Test
  public void testInitRegs()
  {
    assertEquals(RegKey.values().length, _regFactory.getNumberOfRegistries());
  }
} // end of TA00a_Initialize class


