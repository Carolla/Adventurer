/**
 * FileMap.java
 */


package pdc;

import java.io.File;
import java.util.List;
import java.util.Vector;



/**
 * Target file for QA Tool
 */
public class SrcMissingAllTests
{
  private static final int INITIAL_DEPTH = 1;
  private static final String DEFAULT_SEP_WIDTH = "    ";
  private static final String FILE_PREFIX = "---";

  
  public static void main(String args[]) throws Exception
  {
     System.out.println("Running SrcMissingAllTests.java");
  } 

  
  public void showFiles(File file)
  {
    System.out.print(file);
    List<Integer> depthList = new Vector<Integer>();
    depthList.add(new Integer(INITIAL_DEPTH));
    displayFiles(file.listFiles(), INITIAL_DEPTH, depthList);
  }

  public void displayFiles(File[] files, int depth, List<Integer> parentDepths)
  {
    Integer maxDepth = (Integer) parentDepths.get(parentDepths.size() - 1);
    String hline = "";
    for (int i = INITIAL_DEPTH; i <= maxDepth; i++) {
      boolean found = false;
      for (int j = 0; j < parentDepths.size(); j++) {
        Integer curDepth = (Integer) parentDepths.get(j);
        if (i == curDepth.intValue()) {
          found = true;
        }
      }
      if (found == true) {
        hline += DEFAULT_SEP_WIDTH + "|";
      } else {
        hline += DEFAULT_SEP_WIDTH;
      }
    }

    // Required format : | |-- File name
    // hline = "\n" + hline + "\n" + hline;
    hline = "\n" + hline;
    hline += FILE_PREFIX;
    for (int i = 0; i < files.length; i++) {
      if (files[i].isFile()) {
        // print file
        System.out.print(hline + files[i].getName());
      } else {
        // print directory
        System.out.print(hline + File.separator + files[i].getName());

        // If this is last file in this directory then no need of showing parent chain further.
        // Remove parent depth from depth list
        List<Integer> depthList = new Vector<Integer>(parentDepths);
        Integer curDepth = new Integer(depth);
        Integer nxtLevelDepth = new Integer(depth + 1);

        // last file in this directory
        if (i == files.length - 1) {
          if (depthList.contains(curDepth)) {
            depthList.remove(curDepth);
          }
        }

        // add next level depth
        depthList.add(nxtLevelDepth);

        if (files[i].listFiles() != null) {
          displayFiles(files[i].listFiles(), nxtLevelDepth, depthList);
        }
      }
    }

  } /* end of displayFiles method */


}/* end of SrcMissingAllTest class */
