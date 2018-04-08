/**
 * ApplicationException.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */


package mylib;

/**
 * An exception class that is specific to Chronos, and derived from class Exception
 * 
 * @author Alan Cline
 * @version Apr 21, 2010 // original <br>
 *          July 19, 2017 // minor comment changes when examining for bugs elsewhere <br>
 */
public class ApplicationException extends RuntimeException
{
  /** Default constructor */
  public ApplicationException()
  {}

  /**
   * Typical constructor
   * 
   * @param msg user-supplied message for annotating problem
   */
  public ApplicationException(String msg)
  {
    super(msg);
  }


} // end of ChronosException class

