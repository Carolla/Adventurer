/**
 * SomeObject.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.dmc;

import mylib.dmc.IRegistryElement;

import com.db4o.query.Predicate;

/**
 * This object is used to test various db4o input/output methods. By default, the word field is used
 * as the key field, but can be changed by the <code>setKey()</code> method.
 * 
 * @author Alan Cline
 * @version Dec 3, 2012 // original <br>
 *          Mar 13, 2013 // adapted to implement IRegistryElement <br>
 */
public class SomeObject implements IRegistryElement
{
  /** Field to use as search key */
  private String _key;
  /** Test field */
  private double _num = 9.1;
  /** Test field, also used as key */
  private String _word = "test";


  /**
   * Constructor with specified field values.
   * 
   * @param value any float number
   * @param text any text string
   */
  public SomeObject(double value, String text) throws IllegalArgumentException
  {
    _num = value;
    _word = text;
    _key = _word;
  }


  /**
   * Match two objects of this type comparing their two fields
   * 
   * @param target object to match against
   * @return true if both fields match, else false
   */
  public boolean equals(IRegistryElement object)
  {
    SomeObject target = (SomeObject) object;
    return ((this._num == target.getNum()) && (this._word.equals(target.getWord())));
  }


  /**
   * Get a unique field to use as a key. Default is the word field, but that can be changed.
   * 
   * @return the field key
   */
  @Override
  public String getKey()
  {
    return _key;
  }


  /**
   * @return the numerical field of this object
   */
  public double getNum()
  {
    return _num;
  }


  /**
   * Set the predicate to match objects; uses the equals method
   * 
   * @return the Predicate object
   */
  public Predicate<IRegistryElement> getPredicate()
  {
    Predicate<IRegistryElement> pred = new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return this.equals(candidate);
      }
    };
    return pred;
  }


  /**
   * Get the word field
   * 
   * @return word field key
   */
  public String getWord()
  {
    return _word;
  }


  /**
   * Set the unique key to a different field, in this case, the number
   * 
   * @return the text field of this object to use as a key
   */
  public void setKey(String field)
  {
    _key = field;
  }


  /**
   * Setter for testing
   * 
   * @param value new value to store
   */
  public void setNum(double value)
  {
    _num = value;
  }

  /**
   * Dump the object contents as one String
   * 
   * @return elements as a single string
   */
  public String toString()
  {
    String s = "num = " + _num;
    s += "\t word = " + _word;
    return s;
  }


} // end of SomeObject class
