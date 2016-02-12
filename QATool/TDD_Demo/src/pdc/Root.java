/**
 * Root.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

/**
 * @author Alan Cline
 * @version Feb 10, 2016 // original <br>
 */
public class Root
{
  public Root()
  {}


  public Double[] getRoot(Double A, Double B, Double C)
  {
    Double[] result = new Double[2];
    result[0] = 0.0;
    result[1] = 0.0;

    // Guard for no coefficients
    if ((A == 0.0) && (B == 0.0) && (C == 0.0)) {
      result[0] = 0.0;
      result[1] = 0.0;
    }
    // Linear case
    else if ((A == 0.0) && (B != 0.0)) {
      result[0] = -C / B;
      result[1] = null;
    }
    // Quadratic case
    else if ((A != 0) && (B == 0.0) && (C == 0.0)) {
      result[0] = 0.0;
      result[1] = 0.0;
    }
    Double radical = B*B - 4 * A *C;
    // Solve real case first
    if (radical > 0) {
      if (result[0] > result[1]) {
        Double tmp = result[0];
        result[1] = result[0];
        result[0] = tmp;
      }
    }

    return result;
  }

}
