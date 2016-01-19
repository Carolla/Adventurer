/**
 * oldSomeObject.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.dmc;

import mylib.dmc.IRegistryElement;

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
  private int _num = 9;
  /** Test field, also used as key */
  private String _word = "test";


  /**
   * Constructor with specified field values.
   * 
   * @param value any float number
   * @param text any text string
   */
  public SomeObject(int value, String text) throws IllegalArgumentException
  {
    _num = value;
    _word = text;
    _key = _word;
  }


  public SomeObject(String string)
  {
    this(0, string);
  }


  @Override
  public boolean equals(IRegistryElement target)
  {
    return this.equals((Object) target);
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + _num;
    result = prime * result + ((_word == null) ? 0 : _word.hashCode());
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
    SomeObject other = (SomeObject) obj;
    if (_num != other._num)
      return false;
    if (_word == null) {
      if (other._word != null)
        return false;
    } else if (!_word.equals(other._word))
      return false;
    return true;
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
  public int getNum()
  {
    return _num;
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
  public void setNum(int value)
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
