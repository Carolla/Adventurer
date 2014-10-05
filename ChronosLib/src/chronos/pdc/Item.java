/**
 * Item.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.util.EnumMap;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import chronos.civ.MiscKeys;
import chronos.civ.MiscKeys.ItemCategory;
import chronos.civ.MiscKeys.ItemFields;


/**
 * Contains the attributes of a generic Item. For now, that is name and weight.
 * 
 * @author Alan Cline
 * @version Jun 8, 2009 // original <br>
 *          Oct 21, 2009 // changed ctor to take int weights instead of codes <br>
 *          Apr 11 2011 // TAA changed visibiltiy of attributes to protected <br>
 */
public class Item implements IRegistryElement
{
  // /** METADATA CONSTANTS */
  // /** Recommended serialization constant */
  // static final long serialVersionUID = 1002L;

  /** Weight of Item to nearest lb */
  public final int LBWT = 0;
  /** Weight of Item in ounces of remaining fraction lb */
  public final int OZWT = 1;

  /** Name of this Item */
  private String _name;
  /** One of the many kinds of Item */
  private MiscKeys.ItemCategory _category;
  /** Weight of a single Item in ounces (qty = 1) */
  private int _weight;
  /** Number of the kinds of items (quantity) */
  private int _qty;

  /** Standard weight (oz) for a gold piece */
  static public final int GOLD_WEIGHT = 2;
  /** Standard weight (oz) for a silver piece */
  static public final int SILVER_WEIGHT = 1;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public Item()
  {}

  /**
   * Construct an Item from its name and weight descriptor
   * 
   * @param type one of General, Weapon, Armor, Valueable, or Key
   * @param name of the Item
   * @param ozWeight weight with Item in ounces
   * @param initialQty the number of these Items being constructed
   * @throws ApplicationException if any of the parms are null or illegal
   */
  public Item(ItemCategory type, String name, int ozWeight, int initialQty)
      throws ApplicationException
  {
    if (type == null) {
      throw new ApplicationException(
          "Item(): Could not create Item. Null ItemCategory passed to Item ctor");
    }
    // Trim down the name to ensure that no empty string was input
    if ((name == null) || (name.trim().length() == 0)) {
      throw new ApplicationException(
          "Item(): Could not create Item. Empty name passed to Item ctor");
    }
    if (ozWeight <= 0) {
      throw new ApplicationException(
          "Item(): Could not create Item. : Illegal (negative) weight for Item");
    }
    if (initialQty <= 0) {
      throw new ApplicationException(
          "Item(): Could not create Item: Illegal (negative) quantity for Item");
    }
    _category = type;
    _name = name;
    _weight = ozWeight;
    _qty = initialQty;
  }

  /*
   * PUBLIC METHODS
   */

  /**
   * Increment the number of these kinds of Items in addition to it's current quanity. Negative
   * values are allowed for dropping one or more of these Items
   * 
   * @param delta the <i>increase or decrease</i> in quantity for this Item
   * @return the final quantity for the Item
   * @throws ApplicationException if an attempted to bring an Item to below zero quantity
   */
  public int adjustQuantity(int delta) throws ApplicationException
  {
    // Guard against dropping more Items than in inventory
    if ((delta <= 0) && (Math.abs(delta) > _qty)) {
      throw new ApplicationException("Don't have that many " + getName()
          + "'s to drop.");
    }
    // Change the Item quantiy for small enough decrements, or any increment
    else {
      _qty += delta;
    }
    return _qty; // either to updated value or the original
  }

  /**
   * Copies like the standard <code>Object.clone()</code> method, but without all the baggage of the
   * <code>Cloneable interface</code>. Used mostly to support the Inventory copies that may damage
   * originals added or dropped to it. Will catch exceptions from the Item constructor and return a
   * null.
   * 
   * @returns the copied version of the input Item; or null if copying doesn't work
   */
  public Item copy()
  {
    try {
      Item mimic = new Item(_category, _name, _weight, _qty);
      return mimic;
    } catch (ApplicationException ex) {
      return null;
    }
  }

  /**
   * Two Items are considered equal if their class, names and categories are equal. This is a
   * required implementation for the <code>ArrayList contains()</code> method, and overrides the
   * <code>Object.equals()</code> method, so must have exactly this signature, and cast to the
   * target Class within.
   * 
   * @param otherThing the Item to be considered
   * @return true if the Item has the same name (or phrase), or memory address
   */
  @Override
  public boolean equals(IRegistryElement otherThing)
  {
    // Check that the parameter exists
    if (otherThing == null) {
      return false;
    }

    // A quick test to see if objects are identical
    if (this == otherThing) {
      return true;
    }

    // Check that a match occurs at least at the Class level
    if (getClass() != otherThing.getClass()) {
      return false;
    }

    // Now we know otherThing is a non-null Item
    Item whatsIt = (Item) otherThing;
    // Check for name, category, and weight
    boolean sameName = this._name.equalsIgnoreCase(whatsIt.getName());
    boolean sameCat = this._category == whatsIt.getCategory();
    boolean sameWeight = this._weight == whatsIt.getWeight();
    return (sameName && sameCat && sameWeight);
  }

  // @Override
  // public Predicate<IRegistryElement> getPredicate()
  // {
  // // TODO Auto-generated method stub
  // return null;
  // }

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
  public int getWeight()
  {
    return _weight;
  }

  /**
   * Pack the Item-specific fields into a data shuttle of Strings (EnumMaps must be homogeneous).
   * Note that any existing data will be overwritten by new values of the same key.
   * 
   * @param fields of the Item: category, name, quantity, weight (oz)
   * @return the data shuttle
   */
  public EnumMap<ItemFields, String> packShuttle(
      EnumMap<ItemFields, String> fields)
  {
    fields.put(ItemFields.NAME, _name);
    fields.put(ItemFields.CATEGORY, _category.name());
    fields.put(ItemFields.QTY, String.valueOf(_qty));
    fields.put(ItemFields.OZWT, String.valueOf(_weight));
    return fields;
  }

  /**
   * Set the name of this Item; used for making query templates
   * 
   * @param name new name of this Item
   * @return the Item
   */
  public Item setName(String name)
  {
    _name = name;
    return this;
  }

  /**
   * Convert Item to string: name (category), qty = ?, weight = ?
   * 
   * @param item that is converted to a record for saving
   * @return record of the item passed in
   */
  @Override
  public String toString()
  {
    String catStr = null;
    if (_category != null) {
      catStr = _category.toString();
    }
    // return (_name + " (" + catStr + ")\tQty = " + _qty + "\t weight = " + _weight + " oz.");
    return (_name + " (" + catStr + ")");
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockItem
  {
    /** Default ctor */
    public MockItem()
    {}

    /** Set a new name into the Item */
    public void setName(String newName)
    {
      _name = newName;
    }

  } // end of MockItem inner class

} // end of Item class

