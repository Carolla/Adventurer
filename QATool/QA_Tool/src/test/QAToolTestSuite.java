/**


 * QAToolTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Run all unit tests for the QA Tool.
 * 
 * @author Alan Cline
 * @version Mar 7 2016 // original <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

    /** PDC test files */
    TestPrototype.class,
    TestQATool.class

  })

public class QAToolTestSuite
{

} // end of QAToolTestSuite class

