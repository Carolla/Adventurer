/**
 * TmpItem.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.util.ArrayList;

/**
 * An TmpItem is an element of the Inventory. TmpItems may also contain other TmpItems.
 * 
 * @author Al Cline
 * @version Oct 5, 2015 // original <br>
 */
public class TmpItem
{
  private String _name;
  private double _weight;
  private double _quantity;

  // TODO: Move the quantity to add(), which gets incremented each time duplicate TmpItem is added
  // Constructor for name and weight.
  public TmpItem(String name, int qty, double wt)
  {
    _name = name;
    _quantity = qty;
    _weight = wt * qty;
  }


} // end of TmpItem class
