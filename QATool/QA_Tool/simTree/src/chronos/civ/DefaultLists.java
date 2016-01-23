/**
 * DefaultLists.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.civ;

import java.util.ArrayList;

/**
 * Contains the default options list and behavior for creating the Hero
 *
 * @author Alan Cline
 * @version Feb 14, 2013 // original <br>
 */
public class DefaultLists
{
  /** Enum value to identify the list */
//  private HeroOptions _label;
  /** List of options to display for choosing */
  private ArrayList<String> _option;
  /** Quantity associated with an options list, such as default inventory Items */
  private ArrayList<Integer> _quantity;

  /*
   * CONSTRUCTORS
   */


  /**
   * Constructor to create template for searching
   * 
   * @parm label that identifies the options list in the object
   * @return DefaultList that can be stored in the registry and db
   */
//  public DefaultLists(HeroOptions label)
//  {
//    _label = label;
//    _option = null;
//    _quantity = null;
//  }


//  /**
//   * Constructor for non-object options (Strings) that have no quantity
//   * 
//   * @parm label that identifies the options list in the object
//   * @parm opts the options for the Hero, which may be Strings or Items
//   * @return DefaultList that can be stored in the registry and db
//   */
//  public DefaultLists(HeroOptions label, String[] opts)
//  {
//    _label = label;
//    _option = new ArrayList<String>(opts.length);
//    for (String s : opts) {
//      _option.add(s);
//    }
//    _quantity = null;
//  }


//  /**
//   * Constructor for non-object options (Strings) that have no quantity
//   * 
//   * @parm label that identifies the options list in the object
//   * @parm opts the options for the Hero, which may be Strings or Items
//   * @return DefaultList that can be stored in the registry and db
//   */
//  public DefaultLists(HeroOptions label, ArrayList<String> opts, ArrayList<Integer> qty)
//  {
//    _label = label;
//    // Convert String[] to ArrayList<String> for storing
//    _option = opts;
//    // Convert string to ArrayList<Integer> for storing
//    _quantity = qty;
//  }


  /*
   * PUBLIC METHODS
   */

  /**
   * @return the number of elements in the options list
   */
  public int getElements()
  {
    return _option.size();
  }

//  /**
//   * @return the label of the label list
//   */
//  public String getLabel()
//  {
//    return _label.toString();
//  }

  /**
   * @return the options
   */
  public ArrayList<String> getOptions()
  {
    return _option;
  }

  /**
   * @return the quantities
   */
  public ArrayList<Integer> getQuantities()
  {
    return _quantity;
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */


} // end of DefaultLists
