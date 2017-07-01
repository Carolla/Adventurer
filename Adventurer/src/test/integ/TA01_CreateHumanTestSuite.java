/**
 * TA01_CreateHumanTestSuite Copyright (c) 2017, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Collective test suite for use case TA01_CreateHero
 * 
 * @author Alan Cline
 * @version June 30, 2017  // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    TestCreateHumanPeasant.class,
    TestCreateGnomePeasant.class,
})
public class TA01_CreateHumanTestSuite
{

} // end of TA01_CreateHumanTestSuite class

