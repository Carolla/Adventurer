/**
 * TestQroot.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.Quadratic;

/**
 * @author Alan Cline
 * @version Feb 9, 2016 // original <br>
 */
public class TestQuadratic
{
  static private Quadratic _quad;

  private final Double TOLERANCE = 0.01;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _quad = new Quadratic();
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _quad = null;
  }

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

  // BEGIN TESTS


  @Test
  public void testAllZeroes()
  {
    Double[] solution = new Double[2];
    solution = _quad.solve(0, 0, 0);
    assertNull(solution);
  }


  @Test
  public void testConstant()
  {
    Double[] solution = new Double[2];
    solution = _quad.solve(0, 0, 9);
    assertNull(solution);
  }


  @Test
  public void testLinear()
  {
    Double[] solution = new Double[2];

    solution = _quad.solve(0, 3, 0);
    assertEquals(0.0, solution[0], TOLERANCE);
    assertEquals(Double.NaN, solution[1], TOLERANCE);

    solution = _quad.solve(0, 3, 9);
    assertEquals(-3.0, solution[0], TOLERANCE);
    assertEquals(Double.NaN, solution[1], TOLERANCE);

  }


  @Test
  public void testQuadraticReal()
  {
    Double[] solution = new Double[2];

    solution = _quad.solve(4, 0, 0);
    assertEquals(0.0, solution[0], TOLERANCE);
    assertEquals(0.0, solution[1], TOLERANCE);

    solution = _quad.solve(1, 0, -4);
    assertEquals(2.0, solution[0], TOLERANCE);
    assertEquals(-2.0, solution[1], TOLERANCE);

    solution = _quad.solve(1, -4, 4);
    assertEquals(2.0, solution[0], TOLERANCE);
    assertEquals(2.0, solution[1], TOLERANCE);

    solution = _quad.solve(2, -5, -3);
    assertEquals(3.0, solution[0], TOLERANCE);
    assertEquals(-0.5, solution[1], TOLERANCE);
  
  }

  @Test
  public void testQuadraticImaginary()
  {
    Double[] solution = new Double[2];

    solution = _quad.solve(1, 0, 0);
    assertEquals(0.0, solution[0], TOLERANCE);
    assertEquals(0.0, solution[1], TOLERANCE);
    assertFalse(_quad.isImaginary());
    assertFalse(_quad.isComplex());

    solution = _quad.solve(1, 0, 1);
    assertEquals(0.0, solution[0], TOLERANCE);
    assertEquals(1.0, solution[1], TOLERANCE);
    assertTrue(_quad.isImaginary());
    assertFalse(_quad.isComplex());
    
    solution = _quad.solve(1, 2, 4);
    assertEquals(-1.0, solution[0], TOLERANCE);
    assertEquals(1.73, solution[1], TOLERANCE);
    assertFalse(_quad.isImaginary());
    assertTrue(_quad.isComplex());

  }
  
  
} // end of TestQuadratic
