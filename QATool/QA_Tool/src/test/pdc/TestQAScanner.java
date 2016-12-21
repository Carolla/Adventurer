/** 
 * TestQAScanner.java Copyright (c) 2016, Alan Cline. All Rights Reserved. 
 * 
 * Permission to make digital or hard copies of all or parts of this work for 
 * commercial use is prohibited. To republish, to post on servers, to reuse, 
 * or to redistribute to lists, requires prior specific permission and/or a fee. 
 * Request permission to use from acline@carolla.com. 
 */

package test.pdc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;

/** 
 * @author Al Cline
 * @version December 20, 2016    // original <br>
 */
public class TestQAScanner
{
	/** 
	 * @throws java.lang.Exception
 	 */ 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{ }

	/** 
	 * @throws java.lang.Exception
 	 */ 
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{ }

	/** 
	 * @throws java.lang.Exception
 	 */ 
	@Before
	public void setUp() throws Exception
	{ }

	/** 
	 * @throws java.lang.Exception
 	 */ 
	@After
	public void tearDown() throws Exception
	{ 
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
	}


// ===============================================================================
//		 BEGIN TESTING
// ===============================================================================
	/**
 	 * @NORMAL_TEST void qaReport()
	 */
	@Test
	public void testQaReport()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * @NORMAL_TEST void treeScan()
	 */
	@Test
	public void testTreeScan()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}


} 	// end of TestQAScanner.java class
