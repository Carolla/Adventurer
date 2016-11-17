
package test.pdc.command;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdWait;

public class TestCmdWait
{
  private CmdWait _cmdWait;

  @Before
  public void setUp()
  {
    _cmdWait = new CmdWait();
  }

  @Test
  public void testInit()
  {
    assertTrue(_cmdWait.init(new ArrayList<String>()));
  }

  @Test
  public void testExec()
  {
    assertTrue(_cmdWait.exec());
  }

  @Test
  public void testWaitParsing()
  {
    String[][] testStrings = {{"5", "m"}, {"10", "h"}, {"5", "hr"}, {"2", "hours"},
        {"1", "min"}, {"2", "minutes"}};

    for (int i = 0; i < testStrings.length; i++) {
      assertTrue("Failed to parse " + i, _cmdWait.init(Arrays.asList(testStrings[i])));
    }
  }

}
