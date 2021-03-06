/**
 * Item.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import mylib.dmc.IRegistryElement;

/**
 * Contains the attributes of a generic Item. For now, that is name and weight.
 * 
 * @author Alan Cline
 * @version Jun 8, 2009 // original <br>
 *          Oct 21, 2009 // changed ctor to take int weights instead of codes <br>
 *          Apr 11 2011 // TAA changed visibiltiy of attributes to protected <br>
 *          Oct 13, 2015 // revised for new Hero gerneration rules <br>
 *          May 27 2017 // converted weights to lbs as standard measure, instead of gpw <br>
 *          July 29, 2017 // refactored per QATool <br>
 */
public class Item implements IRegistryElement
{
  /** Enum of various category of Inventory Items */
  public enum ItemCategory {
    ARMS, ARMOR, CLOTHING, EQUIPMENT, MAGIC, PROVISION, SPELL_MATERIAL, VALUABLES;
  }

  /** Category for easier grouping */
  private ItemCategory _category;
  /** Name of this Item */
  private String _name;
  /** Weight of a single Item in ounces (qty = 1) */
  private double _weight;
  /** Number of the kinds of items (quantity) */
  private int _qty;


  // ==========================================================================================
  // CONSTRUCTOR AND RELATED METHODS
  // ==========================================================================================

  /**
   * Construct an Item from its name and weight descriptor
   * 
   * @param cat one of enum ItemCategory
   * @param name of the Item
   * @param weight weight of item in lbs.ounces
   */
  public Item(ItemCategory cat, String name, double weight)
  {
    if ((cat == null) || (name == null) || (name.isEmpty())) {
      throw new NullPointerException("Constructor parms cannot be null");
    }
    // Negative weights are converted to 0
    weight = (weight <= 0.0) ? 0.0 : weight;
    _category = cat;
    _name = name;
    _weight = weight;
    _qty = 1;
  }


  // ==========================================================================================
  // PUBLIC METHODS
  // ==========================================================================================

  /**
   * Increment the number of these kinds of Items in addition to it's current quantity. Negative
   * values are allowed for dropping one or more of these Items. If more items are attempted to be
   * dropped than exist in the inventory, then no change is made and the current quantity is
   * returned
   * 
   * @param delta the <i>increase or decrease</i> in quantity for this Item
   * @return the final quantity for the Item
   */
  public int adjustQuantity(int delta)
  {
    _qty = (delta + _qty >= 0) ? _qty + delta : _qty;
    return _qty;
  }


  @Override
  public String getKey()
  {
    return _name;
  }

  /**
   * Get the category of the particular item
   * 
   * @return one of the Categories
   */
  public ItemCategory getCategory()
  {
    return _category;
  }

  /**
   * Get the name of this Item
   * 
   * @return the Item name
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the quantity attribute of all Items of this type
   * 
   * @return the Item name
   */
  public int getQuantity()
  {
    return _qty;
  }

  /**
   * Get the weight of this Item
   * 
   * @return the weight in ounces
   */
  public double getWeight()
  {
    return _weight;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((_category == null) ? 0 : _category.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + _qty;
    long temp;
    temp = Double.doubleToLongBits(_weight);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Item other = (Item) obj;
    if (_category != other._category)
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_qty != other._qty)
      return false;
    if (Double.doubleToLongBits(_weight) != Double
        .doubleToLongBits(other._weight))
      return false;
    return true;
  }

  /**
   * Converts this Item to string: name (category): qty : weight 
   * 
   * @return record of the item passed in
   */
  @Override
  public String toString()
  {
    String COLON = " : ";
    String catStr = null;
    if (_category != null) {
      catStr = _category.toString();
    }
    StringBuilder sb = new StringBuilder();
    sb.append(_name + " (" + catStr + ")" + COLON + _qty + COLON + _weight);
    return sb.toString();
  }


} // end of Item class

