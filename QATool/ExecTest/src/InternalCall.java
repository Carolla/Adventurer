/**
 * InternalCall.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


/**
 * @author Alan Cline
 * @version Sep 14, 2016 // original <br>
 */
public class InternalCall
{
   public static void main(String[] args) {
      try {
        runProcess("javac " + args[0]);
        runProcess("java HelloWorld");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

   private static void runProcess(String command) throws Exception {
      Process pro = Runtime.getRuntime().exec(command);
      pro.waitFor();
      System.out.println(command + " exitValue() " + pro.exitValue());
    }

   
   
   
}
