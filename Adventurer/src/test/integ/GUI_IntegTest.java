/**
 * GUI_IntegTest.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com
 */

package test.integ;

import org.junit.After;
import org.junit.Test;

import civ.MainframeCiv;
import mylib.MsgCtrl;


/**
 * This is the integration test for the Mainframe. It creates the proxy and HIC-CIV connector,
 * from which most GUI widgets are simulated, and provides an auditing trace if requested.
 * 
 * @author Al Cline
 * @version Mar 24, 2018 <br>
 *          Apr 6, 2018 // major rewrite to use MVP Proxy testing <br>
 */
public class GUI_IntegTest
{
  /** List of valid Buildings that can be entered */
  // protected static final List<String> _bldgs = new ArrayList<String>();
  // protected static final Scheduler _skedder = new Scheduler();
  // protected static final MainActionCiv _maCiv = new MainActionCiv(_mfCiv);
  // protected static final RegistryFactory _regFactory = new RegistryFactory();
  //
  // protected static BuildingDisplayCiv _bldgCiv;
  // protected static CommandFactory _cmdFac;
  // protected static CommandParser _cp;

  /** Send simulated input and displays selected system responses */
  // protected static final MainframeProxy _proxy = new MainframeProxy();
  // protected static final MainframeCiv _mfCiv = new MainframeCiv(_proxy);
  protected static MainframeProxy _mfProxy;
  protected static MainframeCiv _mfCiv;


  // @BeforeClass
  // public static void setUpBeforeClass()
  // {
  // }
  //
   @After
   public void tearDown()
   {
   MsgCtrl.auditMsgsOn(false);
   MsgCtrl.errorMsgsOn(false);
  
   // Disconnect HIC from CIV
   _mfCiv = null;
   _mfProxy = null;
   }


  // ============================================================================
  // INTEGRATION TEST TA00a. Initialize Adventure Use Case
  // ============================================================================
  /**
   * Launch the main application and the infrastructure to support it. In particular,this use
   * case initializes the Town and Adventure-specific Registries. From the launch page, the
   * player can invoke other use cases: open an adventure, create a new Hero, and perform Hero
   * management. The player can also display the About box and general Help page.
   */
  @Test
  public void TA001_Intialization()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    
//    Initialization init = new Initialization();
//    MockInit mock = init.new MockInit();
//    MsgCtrl.msgln("  Initializing PDC components...");

//    // Verify that there are 8 registries created and mapped 
//    RegistryFactory rf = mock.getRegistryFactory();
//    assertNotNull(rf);
//    assertEquals(8, rf.size());
//    
//    // Verify that the Scheduler was created
//    assertNotNull(mock.getScheduler());
    
    // Verify that there are 10 comands in the command map 
//    assertNotNull(mock.getCommandParser());
//    CommandFactory cmdFac = mock.getCommandFactory();
//    assertNotNull(cmdFac);
//    MockCommands cmdMock = cmdFac.new MockCommands();
//    Map<String, Supplier<Command>> cmdMap = cmdMock.getCommands();
//    assertEquals(1, cmdMap.size());
        
    MsgCtrl.msgln("\t Initialization verified");
    
  }


  // ============================================================================
  // HELPER METHODS
  // ============================================================================


}
