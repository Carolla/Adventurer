/**
 * GCD.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;


/**
 * @author Alan Cline
 * @version Mar 30, 2017 // original <br>
 */
public class GCD
{
   /**
    * Greatest common divisor (GCD) is defined as the <i>largest postive integer</i> that divides
    * two numbers evenly; negative numbers as input will produce a negative GCD, and are therefore
    * invalid.
    * 
    * @param num1 any non-negative integer
    * @param num2 any non-negative integer
    * @returns the gcd of the two numbers
    * @throws IllegalArgumentException for negative input
    */
   static public int gcd(int num1, int num2) throws IllegalArgumentException
   {
      int result = 0;
      // Sort to find the smallest number
      int low = Math.min(Math.abs(num1), Math.abs(num2));
      int high = Math.max(Math.abs(num1), Math.abs(num2));

      // By definition
      if (low == 0) {
         return high;
      }
      for (int k = low; k > 0; k--) {
         if ((high % k == 0) && (low % k == 0)) {
            result = k;
            break;
         }
      }
      return result;
   }


   /**
    * Same as {@code gcd()} but using recursion instead. Uses Euclid's algorithm: if m = 0 then
    * GCD(n, m) = n else GCD(n, m) = GCD(m, n mod m)
    * 
    * @param num1 any non-negative integer
    * @param num2 any non-negative integer
    * @returns the gcd of the two numbers
    * @throws IllegalArgumentException for negative input
    */
   static public int gcdRecurse(int n, int m)
   {
      int result = 0;
      if (m == 0) {
         return n;
      } else {
         result = gcdRecurse(m, n % m);
      }
      return Math.abs(result);
   }


}  // end of GCD class.
