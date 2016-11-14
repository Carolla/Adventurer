/** Test file to demonstrate Test-Driven Development */

package test.pdc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.MathMod;

/**
 * @author Al Cline
 * @version 1.0 // original
 */
public class TestMathMod
{
   private MathMod _mm = null;

   /**
    * @throws java.lang.Exception
    */
   @Before
   public void setUp() throws Exception
   {
      _mm = new MathMod();
      assertNotNull(_mm);
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception
   {
      _mm = null;
   }

   
   /**
    * NORMAL double sqrt(double)
    */
   @Test
   public void normalTest()
   {
      System.out.println("normalTest()");
      double result = _mm.sqrt(25.0);
      System.out.println("\t" + result);
      assertEquals(5.0, result, 0.001);
   }

   /**
    * NORMAL double sqrt(double)
    */
   @Test
   public void normalManyTest()
   {
      System.out.println("normalManyTest()");
      double[] input = {1.0, 4.0, 9.0, 121.0, 2.0, 75.5, 999.9999}; 
      double[] exp = {1.0, 2.0, 3.0, 11.0, 1.4142, 8.689, 31.6228}; 
      double result = -1;
      
      for (int k=0; k < input.length; k++) {
         result = _mm.sqrt(input[k]);
         System.out.println("\t" + result);
         assertEquals(exp[k], result, 0.001);
      }
   }

   /**
    * SPECIAL double sqrt(0.0)
    */
   @Test
   public void zeroTest()
   {
      System.out.println("zeroTest()");
      double result = _mm.sqrt(0.0);
      System.out.println("\t" + result);
      assertEquals(0.0, result, 0.001);

      result = _mm.sqrt(-0.0);
      System.out.println("\t" + result);
      assertEquals(0.0, result, 0.001);
   }

   /**
    * ERROR double sqrt(double)  negative numbers throw exception
    */
   @Test
   public void negativeNumbersTest()
   {
      System.out.println("negativeNumberTest()");
      try {
         double result = _mm.sqrt(-25.0);
         fail();
      } catch (IllegalArgumentException iax) {
         System.err.println("Expected exception: " + iax.getMessage());
      }
   }

}     // end of TestMathMod class
