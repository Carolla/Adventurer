/**
 * TestOccupation.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Occupation;
import chronos.pdc.registry.OccupationRegistry;
import mylib.ApplicationException;
import mylib.MsgCtrl;


/**
 * Test all methods in the Occupation class. An Occupations associated Skill must be in the
 * SkillRegistry before the Occupation can be created.
 *
 * @author Alan Cline
 * @version Feb 11, 2013 // original <br>
 *          July 17, 2017 // updated as informed by QATool <br>
 *          July 21, 2017 // per QATool <br>
 */
public class TestOccupation
{
  private static final String OCPNAME = "Gambler";
  private static final String DESCNAME = "Gambles a lot";
  // private static final String TRAITSTRING = "";
  private static final String SKILLNAME1 = "Luck";
  private static final String SKILLNAME2 = "Cheating";

  private List<String> _skillList = new ArrayList<>();
  private Occupation _ocp;


  // =============================================================================
  // SETUP/TEARDOWN
  // =============================================================================

  /**
   * @throws java.lang.Exception for unexpected exception
   */
  @Before
  public void setUp() throws Exception
  {
    _skillList.add(SKILLNAME1);
    _skillList.add(SKILLNAME2);
    _ocp = new Occupation(OCPNAME, DESCNAME, _skillList);
    assertNotNull(_ocp);
  }

  /**
   * @throws java.lang.Exception for unexpected exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _ocp = null;
    _skillList.clear();
  }


  // =============================================================================
  // BEGIN TESTS
  // =============================================================================

  /**
   * @Normal.Test {@code Occupation(String name, String description, List<String> skillNameList)}
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal verify the fields are properly assigned
    MsgCtrl.msgln("\t Occupation " + _ocp.getName() + " created with skills "
        + _ocp.getSkillNames());
    assertEquals(OCPNAME, _ocp.getName());
    assertEquals(2, _ocp.getSkillNames().size());
    assertEquals(SKILLNAME1, _ocp.getSkillNames().get(0));
    assertEquals(SKILLNAME2, _ocp.getSkillNames().get(1));
  }


  /**
   * @Error.Test {@code Occupation(String, String, List<String>)} -- null input parms <br>
   * @Error.Test {@code Occupation(String, String, List<String>)} -- Name parm is overly long <br>
   */
  @Test
  public void testCtor_NullParms()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    String LONGNAME = "All names are required to be within "
        + Occupation.OCC_NAME_LIMIT + "  characters";

    // Error name parm is null
    try {
      _ocp = new Occupation(null, SKILLNAME1, new ArrayList<String>());
    } catch (NullPointerException ex1) {
      MsgCtrl.msgln("\t Expected exception: " + ex1.getMessage());
    } catch (ApplicationException ex2) {
      MsgCtrl.errMsgln("\t UnExpected exception: " + ex2.getMessage());
    }
    // Error Description is null
    try {
      _ocp = new Occupation(OCPNAME, null, new ArrayList<String>());
    } catch (NullPointerException ex1) {
      MsgCtrl.msgln("\t Expected exception: " + ex1.getMessage());
    } catch (ApplicationException ex2) {
      MsgCtrl.errMsgln("\t Unexpected exception: " + ex2.getMessage());
    }
    // Error Skill List is null
    try {
      _ocp = new Occupation(OCPNAME, SKILLNAME2, null);
    } catch (NullPointerException ex1) {
      MsgCtrl.msgln("\t Expected exception: " + ex1.getMessage());
    } catch (ApplicationException ex2) {
      MsgCtrl.errMsgln("\t Unexpected exception: " + ex2.getMessage());
    }
    // Error name parm is overly long
    try {
      _ocp = new Occupation(LONGNAME, SKILLNAME2, new ArrayList<String>());
    } catch (ApplicationException ex2) {
      MsgCtrl.msgln("\t Expected exception: " + ex2.getMessage());
    }
  }


  /**
   * chronos.pdc.Occupation(String, String, String, String) -- test series
   * 
   * @Normal.Test Verify same name and description return true <br>
   * @Error.Test Verify different name returns false <br>
   * @Error.Test Verify different skillNames returns false <br>
   * @Error.Test Verify different both name and skillname returns false <br>
   * @Error.Test Pass null object; expect false <br>
   * @throws ApplicationException unexpected exception found <br>
   */
  @Test
  public void testEquals() throws ApplicationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String ocp2Name = "DM'ing";
    String ocp2Skill = "No Occupational Skills";
    List<String> ocpSkillList = new ArrayList<String>();
    ocpSkillList.add(ocp2Skill);

    // Normal Verify same class, name and description returns true
    Occupation ocpSame = new Occupation(OCPNAME, DESCNAME, _skillList);
    assertTrue(_ocp.equals(ocpSame));

    // Error Verify different names returns false
    Occupation ocpDiffName = new Occupation(ocp2Name, DESCNAME, _skillList);
    assertFalse(_ocp.equals(ocpDiffName));

    // Error Pass null object; expect false
    assertFalse(_ocp.equals(null));
  }


  /**
   * @Not.Needed String getDescription() -- getter
   */
  public void testGetDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);;
  }


  /**
   * @Not.Needed String getKey() -- getter
   */
  public void testGetKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);;
  }


  /**
   * @Not.Needed String getName() -- getter
   */
  public void testGetName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Normal.Test Occupation getRandomOccupation() -- return a unique set of Occupations from the
   *              OccupationRegistry <br>
   */
  @Test
  public void testGetRandomOccupation()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP a collection that allows no duplicates
    OccupationRegistry ocpReg = new OccupationRegistry();
    boolean isUnique = false;

    // Take 20% of the Registry as a sample
    int sampleSize = ocpReg.size() / 5;
    // Try five times to get unique names before failing the test
    for (int k = 0; k < 5; k++) {
      // A unique set will be full; dups (unadded) will cause the set to be smaller
      if (loadOcpNames(sampleSize).size() == sampleSize) {
        isUnique = true;
        break;
      }
    }
    assertTrue(isUnique);
  }


  /**
   * @Not.Needed {@code List<String> getSkillNames()}
   */
  public void testGetSkillNames()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);;
  }


  /**
   * @Not.Needed List getSkills() -- getter
   */
  public void testGetSkills()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);;
  }


  /**
   * @Not.Needed String getTrait() -- getter
   */
  public void testGetTrait()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);;
  }


  /**
   * @Not.Needed int hashCode() -- primitive support
   */
  public void testHashCode()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Not.Needed String toString() -- gets occupation name
   */
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  // =============================================================================
  // Private Helper Methods
  // =============================================================================

  /**
   * Randomly count unique occupation names added to a Set. Duplicates will be dropped so the set
   * will not be full.
   * 
   * @return the unique set of names
   */
  private Set<String> loadOcpNames(int sampleSize)
  {
    Set<String> foundOcps = new HashSet<>();
    for (int k = 0; k < sampleSize; k++) {
      String name = Occupation.getRandomOccupation().getName();
      assertNotNull(name);
      foundOcps.add(name);
    }
    return foundOcps;
  }


} // end of TestOccupation class
