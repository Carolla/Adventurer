/** Demonstrate Test-Driven Development (TDD) with this class */

package pdc;

/**
 * @author Al Cline
 * @version 1.0	// original
 */
public class MathMod {

	public MathMod() {}	
	
	  public double sqrt(double t) throws IllegalArgumentException
	   {
	      // Guard against zero case
	      if (t > -0.0001 && t < 0.0001) {
	         return 0.0;
	      }
	      if (t < 0.0) {
	         throw new IllegalArgumentException("sqrt() only accepts non-negative real numbers");
	      }

	      double r = t/2.0;
	      for (int k=0; k < 10; k++) {
	            r = (r + t/r)/ 2.0;
	      }
	      return r;
	   }


	
} // end of MathMod class
