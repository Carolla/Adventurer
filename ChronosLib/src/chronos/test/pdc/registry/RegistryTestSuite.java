/**
 * RegistryTestSuite.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.registry;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * * {@code pdc.registry.Test*.java} regression test suite
 *
 * @author Alan Cline
 * @version Jan 17, 2015 // original <br>
 *          Mar 29 2016 // reviewed and updated for overall QA (<br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
  
    /* chronos.pdc.registry test files */
    TestAdventureRegistry.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class RegistryTestSuite
{
}

// end of ResgistryTestSuite test class

