/**
 * ConcreteRegistry.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.dmc;

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;


/**
 * A concrete Registry object used for testing. It is named {@code ConcreteRegistry} to indicate
 * that it is merely used for testing, and to distinguish it from the inner class
 * {@code MockRegistry} to access private methods.
 * <p>
 * The concrete class derived from this abstact class works with a data management component class
 * {@code DBReadWriter} to handle the actual database read and write operations.
 * 
 * @author Alan Cline
 * @version May 8, 2010 // original <br>
 *          January 22, 2011 // working to implement registry <br>
 *          May 15 2011 // TAA: cleared out errors <br>
 *          May 23 2011 // TAA: added getKey method <br>
 *          Jun 13 2011 // TAA: updated/deprecated methods <br>
 *          Sep 27 2014 // ABC removed unneeded methods and encapsulated DBRW better <br>
 *          Aug 1, 2017 // replaced db4o with a HaspMap<><br>
 */
public class ConcreteRegistry<E extends IRegistryElement> extends Registry<E>
{

  // ============================================================
  // CONSTRUCTOR AND RELATED METHODS
  // ============================================================

  // protected DbReadWriter<E> _regRW;

  /**
   * Default constructor
   * 
   * @param filename of the file to act as db repository
   * @throws ApplicationException if the constructor fails
   */
  public ConcreteRegistry(String filename)
  {
    super(filename);
  }

  // public void setDbReadWriter(DbReadWriter<E> regRW)
  // {
  // _regRW = regRW;
  // }


  @Override
  protected void initialize()
  {
    // _regRW = new DbReadWriter<E>(_filename);
    _list = (ArrayList<E>) getAll(); // generic using subclass to avoid type erasure(?) issue
  }


  @Override
  public boolean add(E obj)
  {
    if (super.add(obj)) {
      // _regRW.addElement(obj);
      return true;
    }
    return false;
  }


  @Override
  public void delete(E obj)
  {
    super.delete(obj);
    // _regRW.deleteElement(obj);
  }


  /**
   * Retrieve element names in the particular Registry that match the type of the object passed in.
   * Uses the getKey() method of IRegistryElement, and assumes the key is the object's name field.
   * 
   * @return the list of names
   */
  public List<E> getNamesByType(E elem)
  {
//    return _regRW.getAllNames(elem);
    return (List<E>) new ArrayList<E>();
  }

  
} // end of ConcreteRegistry outer class

