package test.dmc;

import pdc.character.Person;

import dmc.PersonReadWriter;

import chronos.Chronos;

import mylib.MsgCtrl;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

/**
 * Test that the read/writer component of the SkillRegistry class works
 * 
 * @author Alan Cline
 * @author Timothy Armstrong
 * @version <DL>
 *          <DT>Build 1.0 January 14, 2010 // abc: original
 *          <DD>
 *          <DT>Build 1.1 January 16, 2010 // taa: added QA tags and more tests
 *          <DD>
 *          </DL>
 */
public class TestPersonReadWriter extends TestCase {
	// Create ReadWriters for the tests
	private PersonReadWriter _persRW = null;

	@Before
	public void setUp() throws Exception {
		MsgCtrl.auditMsgsOn(false);
		// Create PersonReadWriter
		_persRW = new PersonReadWriter();
		assertNotNull(_persRW);
	}

	@After
	public void tearDown() throws Exception {
		_persRW = null;
	}

	/*********************************************************************************************************
	 * BEGIN THE TESTS
	 **********************************************************************************************************/

	/**
	 * Create a SkillRegistry object by loading it from the data file.
	 * 
	 * @Normal PersonRW.Load() OK
	 * @Error PersonRW.Load() OK
	 * @Null PersonRW.Load() N/A
	 */
	public void testLoad() throws IOException {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.msgln("\ntestLoad():");

		// ERROR -- Test 1: Start with no file to read
		MsgCtrl.msgln("\tTest 1: No data file exists, should see exception message");
		String pers = "TestBob";
		MsgCtrl.msgln("\tNo person file to load...exception expected:");
		assertNull(_persRW.load(pers));

		// NORMAL -- Test 2: Load a test person file.
		MsgCtrl.msgln("\tTest 2: Load a Person file for test purposes");
		pers = "TestClyde";
		Person p = _persRW.load(pers);
		assertNotNull(p);
	}

	/**
	 * Load a Person object, write it to a new file, then by replacing it. Not
	 * sure how to compare Object values.
	 * 
	 * @Normal PersonRW.Save() OK
	 * @Error PersonRW.Save() TODO with file locking(?)
	 * @Null PersonRW.Save() N/A
	 */
	public void testSave() throws IOException {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.msgln("\ntestSave():");
		MsgCtrl.msgln("\tLoading TestClyde for test purposes");

		String pers1 = "TestClyde";
		String pers2 = "TestBob";

		// Ensure that testBob file doesn't exist
		String fileStr = Chronos.CHRONOS_LIB_RESOURCES_PATH + pers2 + ".chr";
		File toDel = new File(fileStr);
		if (toDel.exists()) {
			assertTrue(toDel.delete());
		}
		assertNull(_persRW.load(pers2));

		// NORMAL -- Load person and try to save normally (beware of load()
		// dependencies)
		Person p = _persRW.load(pers1);
		Person q = _persRW.load(pers1);
		assertNotNull(p);
		MsgCtrl.msgln("\tTest 1: Writing a loaded person to a test file");
		assertTrue(_persRW.save(p, pers2));

		// Verify that the test person was loaded identically to the saved
		// person
		MsgCtrl.msgln("\tTest 2: Comparing test person with original person");
		assertEquals(p, q);

		// NULL -- Now blank the test person, write out, and verify the test
		// file
		// is different than the original
		MsgCtrl.msgln("\tTest 3: Replacing the test file and compare with original");
		q = null;
		assertFalse(_persRW.save(q, pers2));

		Person r = _persRW.load(pers2);
		assertTrue((q == r) && (r != p));

		// Debugging
		fileStr = Chronos.CHRONOS_LIB_RESOURCES_PATH + pers2 + ".chr";
		toDel = new File(fileStr);
		assertTrue(toDel.delete());
		assertNull(_persRW.load(pers2));

		MsgCtrl.auditMsgsOn(true);

	}

} // end of PersonReadWriter class

