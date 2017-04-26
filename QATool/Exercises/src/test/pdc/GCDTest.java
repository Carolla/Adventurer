
package test.pdc;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.GCD;


/**
 * @author Alan Cline
 * @version Apr 1, 2017 // original <br>
 */
public class GCDTest
{
   private int[][] _input = {{21, 9}, {9, 21}, {1, 1}, {7, 12}, {7, 13}, {225, 15}, {125, 25},
         {10, 10}, {22, 0}, {0, 0}};
   private int[] _exp = {3, 3, 1, 1, 1, 15, 25, 10, 22, 0};

   
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


   // -------------------------------------------------------
   // BEGIN TESTS
   // -------------------------------------------------------

   @Test
   public void testGcd()
   {
      System.out.println("\n");
      for (int k = 0; k < _exp.length; k++) {
         int result = GCD.gcd(_input[k][0], _input[k][1]);
         System.out.println("GCD(" + _input[k][0] + ", " + _input[k][1] + ") = " + result);
         assertEquals(_exp[k], result);
      }
   }

   
   @Test
   public void testGcdRecurse()
   {
      System.out.println("\n");
      for (int k = 0; k < _exp.length; k++) {
         int result = GCD.gcdRecurse(_input[k][0], _input[k][1]);
         System.out.println("GCDRecurse(" + _input[k][0] + ", " + _input[k][1] + ") = " + result);
         assertEquals(_exp[k], result);
      }
   }

   @Test
   public void testNegGcd()
   {
      // Test the brute force method
      System.out.println("");
      assertEquals(3, GCD.gcd(12, -3));
      assertEquals(3, GCD.gcd(-12, 3));
      assertEquals(3, GCD.gcd(-12, -3));

      // Test the recusive method
      System.out.println("");
      assertEquals(3, GCD.gcdRecurse(12, -3));
      assertEquals(3, GCD.gcdRecurse(-12, 3));
      assertEquals(3, GCD.gcdRecurse(-12, -3));
   }


}  // end of GCDTest
