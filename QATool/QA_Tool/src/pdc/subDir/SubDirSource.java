/**
 * SubDirSource.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.subDir;

import java.io.File;

/**
 * Dummy file for testing the QA Tool
 * 
 * @author Alan Cline
 * @version Feb 5, 2016 // original <br>
 */
public class SubDirSource
{
  
  static public void main(String[] args)
  {
    System.out.println("SubDirSource running to create .class file");
  }
  
  public SubDirSource()
  {}

  public void m1()
  {}

  public File m2()
  {
    return null;
  }

  protected String m3()
  {
    return "x";
  }

  File createFile(File x, File y, String s)
  {
    return null;
  }

  String getTestFilename(String s1, String s2)
  {
    return "x";
  }

  File writeFile(File x, String s)
  {
    return null;
  }

}
