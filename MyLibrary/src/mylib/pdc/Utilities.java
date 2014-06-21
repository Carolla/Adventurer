/**
 * Utilities.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.pdc;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import mylib.Constants;

/**
 * Collection of miscellaneous static methods to support the application classes.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0   Dec 28, 2010    // original <DD>
 *          <DT>Build 1.1   Jan 29, 2013    // moved from Chronos to MyLibary <DD>
 *          </DL>
 */
public class Utilities {
	

//	/**
//	 * Checks if two int arrays are equals, element by element compare. Arrays
//	 * must be of equal length.
//	 * 
//	 * @param results
//	 *            array of 6 traits to compare
//	 * @param minTraits
//	 *            array of 6 traits to compare
//	 * @return true if all match; else false
//	 */
//	static public boolean isTraitsMapEqual(Map<ATTRIBUTE, Integer> results,
//			Map<ATTRIBUTE, Integer> minTraits) {
//		// Guard against non-existent trait array
//		if (results == null) {
//			return false;
//		}
//		// Guard against overrunning the array
//		if (results.values().size() != minTraits.values().size()) {
//			return false;
//		}
//		// Compare each element of the two arrays
//		boolean retval = false;
//		// Traverse both arrays for equality
//		for (ATTRIBUTE att : ATTRIBUTE.values()) {
//			if (!minTraits.get(att).equals(results.get(att))) {
//				retval = false;
//			}
//		}
//		return retval;
//	}
	

    /** Helper method to replace the last white space character with a newline character within
     * the designated limit
     *  
     * @param msg         the string to insert the newline
     * @param width  the limit to crop the string; the remnant is not returned
     * @return the newly cropped string  
     */
    static public String cropLine(String msg, int width)
    {
        // Ensure that no msg needs to be cropped
        if (msg.length() <= width) {
            return msg;
        }
        // Ensure that no newlines are already in the msg
        if ((msg.contains(Constants.NEWLINE)) && (msg.indexOf(Constants.NEWLINE) < width)) {
            return msg;
        }
        Character SPACE =  ' ';
        int pos = msg.lastIndexOf(SPACE, width);
        StringBuilder sb = new StringBuilder(width+1);
        // Copy the substring found into the retaining buffer without the Space char
        sb.append(msg.substring(0, pos));
        // Add the newline
        sb.append(Constants.NEWLINE);
        // Conver the buffer back to a string
        String result = new String(sb); 
        return result;
    }

    /**
     * Checks if two int arrays are equals, element by element compare. Arrays
     * must be of equal length.
     * 
     * @param expValue
     *            array of 6 traits to compare
     * @param testValue
     *            array of 6 traits to compare
     * @return true if all match; else false
     */
    static public boolean isTraitsEqual(int[] expValue, int[] testValue) {
    	// Guard against non-existent trait array
    	if (expValue == null) {
    		return false;
    	}
    	// Guard against overrunning the array
    	if (expValue.length != testValue.length) {
    		return false;
    	}
    	// Compare each element of the two arrays
    	boolean retval = false;
    	// Traverse both arrays for equality
    	for (int k = 0; k < testValue.length; k++) {
    		if (testValue[k] != expValue[k]) {
    			retval = false;
    			break;
    		} else {
    			retval = true;
    		}
    	}
    	return retval;
    }

    /** Formats a long text line into a left-aligned, ragged-right paragraph of a given width.
     *  
     * @param msg         the string to insert the newline
     * @param width  the limit to crop the string; the remnant is not returned
     * @return the new paragraph as a single string, but with inserted newlines  
     */
    static public String wordWrap(String msg, int width)
    {
        // Normal single overly long line without internal newline, results in three formatted lines
        StringBuilder finalPara = new StringBuilder(msg.length());
        String result = Utilities.cropLine(msg, width);
        String remStr = msg.substring(result.length());
        finalPara.append(result);

        // Put the remaining end part of the original string back in to crop more off
        while (remStr.length() > 0) {
            result = Utilities.cropLine(remStr, width);
            finalPara.append(result);
            remStr = remStr.substring(result.length());
        }
        // Conver the buffer back to a string
        String finalString = new String(finalPara); 
        return finalString;
    }

    
    /** Sort an ArrayList<String> alphabetically, which then can be accessed by the get()
     * method sequentially. The sort algorithm uses a TreeSet because it automatically uses
     * an insert-sort algorithm with no duplicates.
     * @param target        unsorted list
     * @return the arraylist sorted, accessible sequentially by the get() method.
     */
    static public ArrayList<String> sort(ArrayList<String> target)
    {
        // Use the TreeSet collection and allow the insert-sort to work
        Collection<String> tree = new TreeSet<String>(Collator.getInstance());
        tree.addAll(target);
        
        // Now convert the TreeSet back to an ArrayList t
        ArrayList<String> result = new ArrayList<String>(target.size());
        for (String s : tree) {
            result.add(s);
        }

        return result;
    }

    
    
}       // end of Utilities class
