/** 
 * TestQATool.java Copyright (c) 2016, Alan Cline. All Rights Reserved. 
 * 
 * Permission to make digital or hard copies of all or parts of this work for 
 * commercial use is prohibited. To republish, to post on servers, to reuse, 
 * or to redistribute to lists, requires prior specific permission and/or a fee. 
 * Request permission to use from acine@carolla.com 
 */


package test.pdc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import mylib.MsgCtrl;

/** 
 * @author Al Cline
 * @version July 21, 2016 	// original <br>
 *          July 26, 2016 	// autogen: QA Tool added missing test methods <br>
 *          August 1, 2016 	// autogen: QA Tool added missing test methods <br>
 *          August 3, 2016 	// autogen: QA Tool added missing test methods <br>
 */
public class TestQATool
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

} 	// end of TestQATool.java class
