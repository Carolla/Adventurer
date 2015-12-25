/**
 * ConcreteRegistry.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;

import mylib.ApplicationException;
import mylib.pdc.Registry;
import mylib.test.dmc.oldSomeObject;


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
 */
public class ConcreteRegistry extends Registry<oldSomeObject>
{

  // ============================================================
  // CONSTRUCTOR AND RELATED METHODS
  // ============================================================

  /**
   * Default constructor
   * 
   * @param filename of the file to act as db repository
   * @throws ApplicationException if the constructor fails
   */
  public ConcreteRegistry(String filename) throws ApplicationException
  {
    super(filename);
  }


  // ============================================================
  // Required implementations of abstract METHODS
  // ============================================================

  /*
   * Sets some predetermined data into the registry to act as default
   */
  @Override
  protected void initialize()
  {
    // Create three objects to initialize database
    oldSomeObject obj1 = new oldSomeObject(1, "one");
    oldSomeObject obj2 = new oldSomeObject(2, "two");
    oldSomeObject obj3 = new oldSomeObject(3, "three");
    add(obj1);
    add(obj2);
    add(obj3);
  }

} // end of ConcreteRegistry outer class

