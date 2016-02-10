/**
 * Quadratic.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

/**
 * Class to demonstrate Test-Driven Development (TDD) using the quadratic equation
 * 
 * @author Alan Cline
 * @version Feb 9, 2016 // original <br>
 */
public class Quadratic
{
  private boolean _imaginary = false;
  private boolean _complex = false;


  // Constructor
  public Quadratic()
  {}


  /**
   * Return the solution to the quadratic equation Ax^2 + Bx + C = 0
   * 
   * @param A quadratic coefficient
   * @param B linear coefficient
   * @param C constant
   * @return the roots of the equation
   */
  public Double[] calcRoots(double A, double B, double C)
  {
    Double[] root = new Double[2];
    return root;
  }


  /**
   * Return the solution to the quadratic equation Ax^2 + Bx + C = 0. Only numbers of the Real and
   * Imaginary number system are returned; Complex root are returned as Double.NaN. The method
   * {@code isImaginary} or (@code isComplex} can be used to indicate imaginary or complex roots.
   * If A and B coefficients are 0, then returns null, regardless of value of C.
   * 
   * @param A quadratic coefficient
   * @param B linear coefficient
   * @param C constant
   * @return the roots of the equation. The Real part in the first element, and the Imaginary root
   *         in the second element.
   */
  public Double[] solve(double A, double B, double C)
  {
    Double[] sol = new Double[2];
    _imaginary = false;
    _complex = false;

    // Guard against all zeroes; return null solutions
    if ((A == 0) && (B == 0)) {
      sol = null;
    }
    // Linear case
    else if ((A == 0) && (B != 0)) {
      sol[0] = -C / B;
      sol[1] = Double.NaN;
    }
    // Quadratic case
    else if (A != 0) {
      double radical = B * B - 4 * A * C;
      // Imaginary roots
      if ((B == 0) && (radical < 0)) {
        _imaginary = true;
        radical = -radical;
        sol[0] = 0.0;
        sol[1] = Math.sqrt(radical) / (2 * A);
      }
      // Complex roots
      else if ((B != 0) && (radical < 0)) {
        _complex = true;
        radical = -radical;
        sol[0] = -B / (2 * A);
        sol[1] = Math.sqrt(radical) / (2 * A);
      }
      // Real roots
      else {
        sol[0] = (-B - Math.sqrt(radical)) / (2 * A);
        sol[1] = (-B + Math.sqrt(radical)) / (2 * A);
        // Place largest (positive) number first
        if (sol[1] > sol[0]) {
          Double tmp = sol[0];
          sol[0] = sol[1];
          sol[1] = tmp;
        }
      }
    }
    return sol;
  }


  public boolean isImaginary()
  {
    return _imaginary;
  }

  public boolean isComplex()
  {
    return _complex;
  }


} // end of QuadraticEquation class
