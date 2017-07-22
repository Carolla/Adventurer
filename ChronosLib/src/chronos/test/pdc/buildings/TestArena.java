/**
 * TestArena.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.buildings.Arena;
import mylib.MsgCtrl;

/**
 *    Verify that the Arena Class works as expected 
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Feb 6, 2013   // original <DD>
 * </DL>
 */
public class TestArena
{
  /** Target object */
  static private Arena _arena = null;
  /** Expected value for name */
  static private String NAME = "Test Dungeon";

  /** Expected value for first impression description */
  private String INTRO = "Of all the dungeons in all the world, she had to kill me in mine.";


  /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
   *                  STATE HANDLING METHODS 
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  /**
   * Each test starts with a target Arena in place
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _arena = Arena.getInstance(NAME);
    assertNotNull(_arena);
    _arena.setIntro(INTRO);
  }

  /**
   * The target is closed after each test (but its file remains)
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _arena.close();
    _arena = null;
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
   *                  BEGIN TESTING 
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  /** Test constructor to create an arena only if it does not already exist
   * @Normal  Create an arena only if it does not exist
   * @Normal  Return null for non-existing file without creating one
   * @Error      Null name parm
   * @throws Exception for anything else unexpected
   */
  @Test
  public void testArena() throws Exception
  {
    // Get the arena from the "registry" and check it for integrity
    _arena = Arena.getInstance(NAME);
    assertNotNull(_arena);

    String dgnName = _arena.getName();
    String intro = _arena.getIntro();
    int nbrRooms = _arena.getNbrRooms();
    MsgCtrl.msgln("\t This dungeon is called " + dgnName);
    MsgCtrl.msgln("\t " + intro);
    MsgCtrl.msgln("\t It contains " + nbrRooms + " Rooms.");
    assertNotNull(dgnName);
    assertNotNull(intro);
    assertEquals("Zero rooms expected, but not found", 0, nbrRooms);
  }


  /** Test that Arena will not take a null name */
  @Test
  public void testNullArena() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\t testNullArena()");

    // ERROR null name parm; current arena must be closed already
    try {
      Arena.getInstance(null);
    } catch (NullPointerException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
  }


  /** Chronos.pdc.Arena
   * @Not_Needed  getName()       // getter
   * @Not_Needed  close(boolean)  // support method, tested in state prep/cleanup
   */
  public void not_Needed()
  {}


  /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
   * 					PRIVATE METHODS 
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}           // end of TestArena class
