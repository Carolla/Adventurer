/** 
 * TestQAUtils.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved 
 * 
 * Permission to make digital or hard copies of all or parts of this work for 
 * commercial use is prohibited. To republish, to post on servers, to reuse, 
 * or to redistribute to lists, requires prior specific permission and/or a fee. 
 * Request permission to use from Carolla Development, Inc. by email: 
 * acline@carolla.com 
 */


package test.pdc;

import static org.junit.Assert.*; 
import org.junit.After; 
import org.junit.AfterClass; 
import org.junit.Before; 
import org.junit.BeforeClass; 
import org.junit.Test; 

import mylib.MsgCtrl;

public class TestQAUtils
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

	/**
 	 * @NORMAL_TEST static convertSourceToClass(String)
	 */
	@Test
	public void testConvertSourceToClass()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("\t\tNot yet implemented");
	}


	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}

	/**
 	 * @NORMAL_TEST static simplifyDeclaration(String)
	 */
	@Test
	public void testSimplifyDeclaration()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}

	/**
 	 * @NORMAL_TEST static simplifyReturnType(String)
	 */
	@Test
	public void testSimplifyReturnType()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}

	/**
 	 * @NORMAL_TEST static sortSignatures(ArrayList)
	 */
	@Test
	public void testSortSignatures()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}

} 	// end of TestQAUtils.java class
