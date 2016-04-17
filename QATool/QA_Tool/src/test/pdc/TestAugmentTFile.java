/** 
 * TestAugmentTFile.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved 
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

/** 
 * @author --generated by QA Tool--
 * @version April 17, 2016 	// original <br>
 */
public class TestAugmentTFile
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
	// TESTS FOR PUBLIC METHODS
	// ===============================================================================

	/**
 	 * @NORMAL_TEST void augment(String, String)
	 */
	@Test
	public void testAugment()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("\t\tNot yet implemented");
	}

} 	// end of TestAugmentTFile.java class
