/**
 * Filename:	MetaDie.java
 * Copyright (c) 2003, Carolla Development, All Rights Reserved
 */
package rumorMillStuff.src.myLibrary;

import rumorMillStuff.src.hic.Dgn;

import java.util.Random;

/**
 *	This class rolls groups of dice of various sides, various number of times, 
 *	and adds them up. <code>MetaDie</code> can be created either in debug mode (repeatable numeric 
 *	sequences) or with standard randomizer (non-repeatable).
 *	@author Alan Cline 
 *  @version Build 6.0  Dec 10 2006		 // re-used from previous programs
 */
public class MetaDie 
{
    private Random _generator;
    private int NOT_FOUND = -1;		// some String method returns

	/**
	*	Create a Random class for single die throws, using time as the seed.
	*	This object pre-defines a sequence of random numbers.
	*/
    public MetaDie() 
	{
		 _generator = new Random();
	}


	/**
	*	Create a Random class using an input value as a seed. This object 
	*	pre-defines a sequence of random numbers, but the same seed  
	*	generates the same sequence of numbers, so this version is useful for debugging.
	*	@param	seed	a number that determines the number sequence generated. 
	*/
	public MetaDie(long seed) 
	{
		 _generator = new Random(seed);
	}

	
	/**
 	*	Roll the dice to generate random numbers within a given range. 
	*	@param	minRange	the smallest positive number requested >= 0
	*	@param	maxRange	the largest positive number requested
	*	@return the random number within the range
    *   @throws IllegalArgument Exception if the input parms are negative or out of range
	*/
	public int roll(int minRange, int maxRange) throws IllegalArgumentException
	{
	    // Guards against bad input parms
	    if (minRange >= maxRange) {
	        throw new IllegalArgumentException("Min range larger than max range");
	    }
	    if ((minRange < 0) || (maxRange < 0)) {
	        throw new IllegalArgumentException("Ranges cannot be negative");
	    }
        int sum = -1;
		while (sum < minRange) {
		    sum = _generator.nextInt(maxRange);
		}
		return(sum);
	}

	
	/**
 	*	Roll the dice as indicated with randomly generated numbers.
	*	The sum of nbrDice, each of nbrSides, typically creates a normal (binomial) bell curve. 
	*	The generator uses zero-based numbers, so is offset by 1 for the caller.
	*  If nbrDice is 1, then <code>sumRoll()/code> returns a number from 1 to nbrSides.
	*	@param	nbrDice		number of rolls to sum
	*	@param	nbrSides		max range of each roll (number of sides of die)
	*	@return the sum of the rolled dice. 
	*/
	public int sumRoll(int nbrDice, int nbrSides)
	{
	    int sum = 0;
		for (int k=0; k < nbrDice; k++) {
			sum += _generator.nextInt(nbrSides) + 1;
		}
		return(sum);
	}


	/**
	* 	Convert a dice notation string to two numbers, then roll the dice as indicated by the string.
	*	@param notation	format of {[n][n] 'd' n[n][n] ['+' | '-'] n]}, that is, 1-100
	*		required letter 'd', and 1-100, e.g. 2d10, 1d8+1, or 2d4-1.
	*		Optional +n to provide minimums 
	*	@throws NumberFormatException if notation length out of range
	*	@return the sum of the rolled dice. 
	*/
	public int sumRoll(String notation) throws NumberFormatException
	{
		// Guard condition
		int len = notation.length();
		if ((len < 2) || (len > 8)) {
			throw new NumberFormatException();
		}
		try {
			// Default to no addon minimum values
			int addon = 0;
			int minVal =0;
			
			StringBuffer sb = new StringBuffer(notation);
			int delim = sb.indexOf("d");
			int plusIndex = sb.indexOf("+-");

			// If addon value given, parse it first and adjust length
			if (plusIndex != NOT_FOUND){
				addon = Integer.parseInt(sb.substring(plusIndex+1,len));
				len -= 2;		// DO NOT LET THIS MAGIC NUMBER SURVIVE!
			}
			// Default coefficient of notation is 1, e.g. "d8" = "1d8"
			int nbrDice = 1;
			if (delim != 0) {
			    nbrDice = Integer.parseInt(sb.substring(0,delim));
			}
			int nbrSides = Integer.parseInt(sb.substring(delim+1,len));
			int sum = roll (nbrDice, nbrSides);
			minVal = addon * nbrDice;
			return sum + minVal;
		}
		catch (NullPointerException e) {
			System.out.println("Cannot find the delimiter 'd'");
			throw new NumberFormatException();
		}   
		catch (StringIndexOutOfBoundsException e) {
			System.out.println("Index inconsistent with size");
			throw new NumberFormatException();
		}   
	}
	
}	// end MetaDie class

