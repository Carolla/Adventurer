/**
 * IntegrationSuite_A01.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.integ.A01_CreateHero.old;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Regression test suite for all the <code>Integration</code> test cases for the source code packages of the 
 * <i>Registry</i> suite. 
 * 
 * @author Timothy Armstrong
 * @version <DL>
 * <DT> Build 1.0		November 22, 2011   // original <DD>
 * </DL>
 */
public class IntegrationSuite_A01
{
    /** Compilation of all unit tests for regression and integration testing.  
     * @see <code>suite()</code>
     */
    public IntegrationSuite_A01() {}

    /** Builds the accumulated test suite of all test cases for the Test Cases pertaining to 
     * registries. These comments show the list of test files in the order in which to perform
     * the testing, according to the test rules mentioned above, and serves as a guide for 
     * unit testers. 
     *
     * <OL>
     * <LI> A01 
     *      <UL>
     *      <LI> A01_N001</LI>
     *      <LI> A01_N002</LI>
     */
    public static Test suite()
    {
        String caption = "INTEGRATION TEST SUITE FOR UC A01. CREATE NEW HERO";
        TestSuite suite = new TestSuite(caption);

        //$JUnit-BEGIN$

        // Integration test files: 
//        suite.addTestSuite(A01_N01.class);  //Human female; special case options
        //        suite.addTestSuite(A01_N001a.class); //Human male
        //        suite.addTestSuite(A01_N002.class);  //Dwarf female
        //        suite.addTestSuite(A01_N002a.class); //Dwarf male
        //        suite.addTestSuite(A01_N003.class);  //Elf female
        //        suite.addTestSuite(A01_N003a.class); //Elf male
        //        suite.addTestSuite(A01_N004.class);  //Gnome female
        //        suite.addTestSuite(A01_N004a.class); //Gnome male
        //        suite.addTestSuite(A01_N005.class);  //Half-Elf female
        //        suite.addTestSuite(A01_N005a.class); //Half-Elf male
        //        suite.addTestSuite(A01_N006.class);  //Half-Orc female
        //        suite.addTestSuite(A01_N006a.class); //Half-Orc male
        //        suite.addTestSuite(A01_N007.class);  //Hobbit female
        //        suite.addTestSuite(A01_N007a.class); //Hobbit male
        //$JUnit-END$
        return suite;
    }

} 		// end of IntegrationSuite_A01 class

