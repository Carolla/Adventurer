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
    System.out.println("SubDirSource has compiled and created a .class file");
  }
  
  public SubDirSource()
  {}

  public String getTestFilename(String s1, String s2)
  {
    return "x";
  }

  public void m(File f)
  {}

  public File m(String s, int x)
  {
    return null;
  }

  public String m(String s, int x, long k)
  {
    return "x";
  }

  protected File createFile(File x, File y, String s)
  {
    return null;
  }

  protected File writeFile(File x, String s)
  {
    return null;
  }

}
