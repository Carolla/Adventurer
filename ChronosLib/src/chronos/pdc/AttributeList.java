package chronos.pdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import chronos.Chronos.ATTRIBUTE;

public class AttributeList {
	/** The attributes for a character */
	private Map<ATTRIBUTE, Integer> atts;

	/** All lists of traits should include 6 traits */
	public static final int NUM_ATTRIBUTES = 6;

	/** The order of attributes that will always be used */
	public static ATTRIBUTE[] attOrder = { ATTRIBUTE.STR, ATTRIBUTE.INT,
			ATTRIBUTE.WIS, ATTRIBUTE.DEX, ATTRIBUTE.CON, ATTRIBUTE.CHR };

	/**
	 * Create a new empty attribute list with no values in it
	 * 
	 */
	public AttributeList() {
		atts = new TreeMap<ATTRIBUTE, Integer>();
	}

	/**
	 * Create an attribute list and fill it according to the passed array
	 * 
	 * @param attList
	 *            the attributes for the list
	 */
	public AttributeList(int[] attList) {
		assert (attList.length == NUM_ATTRIBUTES) : "Invalid attribute list: "
				+ attList;
		atts = new TreeMap<ATTRIBUTE, Integer>();

		for (int i = 0; i < attOrder.length; i++) {
			atts.put(attOrder[i], attList[i]);
		}
	}

	/**
	 * Add an attribute to an attribute list
	 * 
	 * @param att
	 *            the attribute being added/updated
	 * @param i
	 *            the value to put into the list
	 * @return true if the value is in the attribute list
	 */
	public boolean put(ATTRIBUTE att, Integer i) {
		atts.put(att, i);
		return atts.containsKey(att);
	}

	/**
	 * Get the value of an attribute from the list
	 * 
	 * @param att
	 *            the attribute in question
	 * @return the value of the attribute in the list
	 */
	public int get(ATTRIBUTE att) {
		return atts.get(att);
	}

	/**
	 * Based on the ordering specified for attributes, returns the value of an
	 * attribute from the list
	 * 
	 * @param i
	 *            position in the list to return
	 * @return the value at that position
	 */
	public int get(int i) {
		return atts.get(attOrder[i]);
	}
	
	/**
	 * Provides a List of all attributes
	 * 
	 * @return the List of all attributes as integers in attribute order
	 */
	public List<Integer> getList() {
		List<Integer> retval = new ArrayList<Integer>();
		for(int i = 0; i < attOrder.length; i++){
			retval.add(atts.get(attOrder[i]));
		}
		return retval;
	}

	/**
	 * Based on the ordering specified for attributes, puts a value into a list
	 * 
	 * @param pos
	 *            the position to insert at
	 * @param val
	 *            the value to put into the list
	 * @return true if the list contains a value for that position
	 */
	public boolean put(int pos, int val) {
		atts.put(attOrder[pos], val);
		return atts.containsKey(attOrder[pos]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atts == null) ? 0 : atts.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeList other = (AttributeList) obj;
		if (atts == null) {
			if (other.atts != null)
				return false;
		} else if (!atts.equals(other.atts))
			return false;
		return true;
	}

	public int size() {
		return atts.size();
	}

}
