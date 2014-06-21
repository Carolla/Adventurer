/**
 * Suite_TestDMC.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.test.dmc;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Aug 8, 2009   // original <DD>
 * </DL>
 */
public class Suite_TestDMC
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for mylib.mylib.test.dmc");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestHelpReader.class);
		//$JUnit-END$
		return suite;
	}

}		// end of DMCSuite class

