/**
 * TestRoot.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.Root;

/**
 * @author Alan Cline
 * @version Feb 10, 2016 // original <br>
 */
public class TestRoot
{

  private final double TOLERANCE = 0.01;

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
  {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {}

  @Test
  public void testAlllZeroes()
  {
    Root root = new Root();
    Double[] result = root.getRoot(0.0, 0.0, 0.0);
    assertEquals(0.0, result[0], TOLERANCE);
    assertEquals(0.0, result[1], TOLERANCE);

  }

  @Test
  public void testConstant()
  {
    Root root = new Root();
    Double[] result = root.getRoot(0.0, 0.0, 1.0);
    assertNull(result[0]);
    assertNull(result[1]);

  }

  @Test
  public void testLinear()
  {
    Root root = new Root();
    Double[] result = root.getRoot(0.0, 1.0, 0.0);
    assertEquals(0.0, result[0], TOLERANCE);
    assertNull(result[1]);

    root = new Root();
    result = root.getRoot(0.0, 3.0, 9.0);
    assertEquals(-3.0, result[0], TOLERANCE);
    assertNull(result[1]);

  }

  @Test
  public void testQuadratic()
  {
    Root root = new Root();
    Double[] result = root.getRoot(1.0, 0.0, 0.0);
    assertEquals(0.0, result[0], TOLERANCE);
    assertEquals(0.0, result[1], TOLERANCE);
    
    result = root.getRoot(1.0, 4.0, 2.0);
    assertEquals(1.42, result[0], TOLERANCE);
    assertEquals(-.585, result[1], TOLERANCE);
    

  }

  
  
  
}   // end of TestRoot class
