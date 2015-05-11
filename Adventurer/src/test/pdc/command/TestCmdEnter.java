/**
 * TestCmdEnter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.command;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdEnter;

/**
 * @author Al Cline
 * @version May 4, 2015 // original <br>
 */
public class TestCmdEnter
{

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {}

  
  
  
  //=================================================
  //  BEGIN TESTING
  //=================================================
      
  /** Normal CmdEnter <bldgName> */
  @Test
  public void test()
  {
    CmdEnter ce = new CmdEnter();
    assertNotNull(ce);
//    CommandRegistry cmdReg = fail("Not yet implemented");
  }

}
