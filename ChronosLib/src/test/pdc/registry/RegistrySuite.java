/**
 * RegistrySuite.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.pdc.registry;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Chronos PDC.registry test files
 * 
 * @author Alan Cline
 * @version May 15, 2013 // split off from ChronosSuite with new folders <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    // PDC test files:
    TestRegistryFactory.class,

})
/** Compilation of all unit tests for regression and integration testing. */
public class RegistrySuite
{
}

// end of RegistrySuite test class

