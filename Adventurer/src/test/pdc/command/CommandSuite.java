
package test.pdc.command;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  TestCmdApproach.class,
  TestCmdEnter.class,
  TestCmdInspect.class,
  TestCmdLook.class,
  TestCmdQuit.class,
  TestCmdReturn.class,
  TestCmdTalk.class,
  TestCmdWait.class,
  TestCommandFactory.class,
  TestCommandParser.class,
  TestEvent.class,
  TestScheduler.class
})

public class CommandSuite
{

}
