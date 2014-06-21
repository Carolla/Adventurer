/**
 * Age.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package chronos.pdc;

import mylib.Constants;
import mylib.MsgCtrl;
import chronos.Chronos.ATTRIBUTE;

/**
 * Handles all age-related behavior: initialization and aging and trait
 * adjustment. This class is a utililty class of age-related behaviors.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Nov 13, 2010 // original
 *          <DD>
 *          </DL>
 */
public class Age {
	/** The minimum age that a Person can be for this age (for error checking). */
	static public long MINIMUM_AGE = 16L;

	/**
	 * Arbitrarily, start the new Hero at 85% from the top of the youngest age
	 * category. This provides some plausibility for an Adventurer not being too
	 * young, and allows a maturity trait adjustment to be seen relatively soon.
	 */
	static public final double STARTING_AGE_ADJ = 0.85;

	/**
	 * The categories for all races, which correspond with the Race-specific age
	 * brackets.
	 */
	static private final String[] _ageCats = { "Young Adult", "Mature",
			"Middle-Aged", "Old", "Venerable" };

	/**
	 * The categories for all races, which correspond with the Race-specific age
	 * brackets.
	 */
	public enum CATEGORY {
		YOUNG_ADULT, MATURE, MIDDLE_AGE, OLD, VENERABLE
	};

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
	 * CONSTRUCTOR(S) AND RELATED METHODS 
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Unneeded constructor for all static methods */
	private Age() {
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
	 * PUBLIC METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Adjust the prime traits as the person ages. <br>
	 * Algorithm: 
	 * <BL> 
	 * <LI>Young Adult: -1WIS, +1 CON</LI> 
	 * <LI>Mature: +1 STR, +1WIS</LI> 
	 * <LI>Middle-Aged: -1 STR, -1 CON, +1 INT, +1 WIS</LI> 
	 * <LI>Old: -2 STR, -2 DEX, -1 CON, +1 WIS</LI> 
	 * <LI>Venerable: -1 STR, -1 DEX, -1 CON, +1 INT, +1 WIS</LI> 
	 * </BL> 
	 * Net effects over a lifetime (which affects prime trait generation) are: <br>
	 * STR = -3; INT = +2; WIS = +3; CON = -2; DEX = -3; CHR = 0 <br>
	 * NOTE: When the Person's age is initialized, no trait can be below 8, and
	 * no trait can be above 19. Once this is done, all derivative values must
	 * be recalculated. Traits can fall outside the range of [8,19] during a
	 * Person's lifetime of play due to age, magic, race, or other factors.
	 * 
	 * @param ageCat
	 *            the name of the category for a particular Race-dependent age
	 * @param traitList
	 *            the six traits will be overwritten with the new ones
	 * @return new trait list; or null if age category can't be found or input
	 *         missing
	 */
	static public AttributeList adjTraitsForAge(CATEGORY ageCat,
			AttributeList traitList) {
		// Guard against traits not derived yet
		if (traitList == null) {
			MsgCtrl.errMsgln("Prime Traits not yet defined");
			return traitList;
		}
		switch (ageCat) {
		case YOUNG_ADULT:
			traitList.put(ATTRIBUTE.WIS, traitList.get(ATTRIBUTE.WIS) - 1);
			traitList.put(ATTRIBUTE.CON, traitList.get(ATTRIBUTE.CON) + 1);
			break;
		case MATURE:
			traitList.put(ATTRIBUTE.STR, traitList.get(ATTRIBUTE.STR) + 1);
			traitList.put(ATTRIBUTE.WIS, traitList.get(ATTRIBUTE.WIS) + 1);
			break;
		case MIDDLE_AGE:
			traitList.put(ATTRIBUTE.STR, traitList.get(ATTRIBUTE.STR) - 1);
			traitList.put(ATTRIBUTE.CON, traitList.get(ATTRIBUTE.CON) - 1);
			traitList.put(ATTRIBUTE.INT, traitList.get(ATTRIBUTE.INT) + 1);
			traitList.put(ATTRIBUTE.WIS, traitList.get(ATTRIBUTE.WIS) + 1);
			break;
		case OLD:
			traitList.put(ATTRIBUTE.STR, traitList.get(ATTRIBUTE.STR) - 2);
			traitList.put(ATTRIBUTE.DEX, traitList.get(ATTRIBUTE.DEX) - 2);
			traitList.put(ATTRIBUTE.CON, traitList.get(ATTRIBUTE.CON) - 1);
			traitList.put(ATTRIBUTE.WIS, traitList.get(ATTRIBUTE.WIS) + 1);
			break;
		case VENERABLE:
			traitList.put(ATTRIBUTE.STR, traitList.get(ATTRIBUTE.STR) - 1);
			traitList.put(ATTRIBUTE.DEX, traitList.get(ATTRIBUTE.DEX) - 1);
			traitList.put(ATTRIBUTE.CON, traitList.get(ATTRIBUTE.CON) - 1);
			traitList.put(ATTRIBUTE.INT, traitList.get(ATTRIBUTE.INT) + 1);
			traitList.put(ATTRIBUTE.WIS, traitList.get(ATTRIBUTE.WIS) + 1);
			break;
		default:
			MsgCtrl.errMsgln("Can't find the age category to adjust");
			break;
		}
		return traitList;
	}

	/**
	 * Find the age bracket (array of age limits) into which the Person's age
	 * fits for a particular Race. As the Person ages, and changes brackets, the
	 * Person's age descriptor will change, and the Person's traits will
	 * permanently change. The age category is a descriptor of the bracket. Age
	 * categories are "Young Adult", "Mature", Middle-Aged", "Old", and
	 * "Venerable", but at what ages those apply depends on the Race. <br>
	 * Note: "Venerable" has no upper limit, so any age greater than the
	 * venerable age is still "venerable". <br>
	 * This method is a generalized method for all Races, which pass their
	 * specific data to it. Age brackets are taken from <i>Dungeon Master
	 * Guide</i>, by Gary Gygax, (c) 1979, TSR Games, p13.
	 * 
	 * @param curAge
	 *            the current age of the Person in seconds
	 * @param ageBrackets
	 *            defines the age category for the Person
	 * @return age category of Person's age, or null if category not found or
	 *         person is below minimum age
	 */
	static public CATEGORY calcAgeCategory(long curAge, int[] ageBrackets) {
		// Place age category here if it is found
		Age.CATEGORY retCat = null;
		// Convert age in seconds to age in years
		double age = ((double) curAge) / Constants.SECS_PER_YEAR;

		// Guard against null input or too young Characters
		if ((ageBrackets == null) || (age < MINIMUM_AGE)) {
			return null;
		}

		// Check for oldest category: anything largest than the oldest age
		// bracket
		int highestCat = ageBrackets.length;
		if (age > ageBrackets[highestCat - 1]) {
			retCat = Age.CATEGORY.VENERABLE;
		}
		// Check for youngest category
		else if ((age >= MINIMUM_AGE) && (age <= ageBrackets[0])) {
			retCat = Age.CATEGORY.YOUNG_ADULT;
		}
		// Check for intervals within the brackets
		// then return that same index into the age categories
		else {
			CATEGORY[] eList = Age.CATEGORY.values();
			for (int k = 1; k < ageBrackets.length; k++) {
				if ((age > ageBrackets[k - 1]) && (age <= ageBrackets[k])) {
					retCat = eList[k];
					break;
				}
			}
		}
		// Return latest value, or null
		return retCat;
	}

	/** Get the specific string names of the age categories */
	static public String[] getAgeCategories() {
		return _ageCats;
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
	 * PRIVATE METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

}        // end of Age class
