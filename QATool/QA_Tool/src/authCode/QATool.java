/**
 * QATool.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

/**
 * This collection of QA tools is intended to enforce the various standards
 * defined by the <i>eChronos</i> team, and to make testing and development more
 * automated. Violations of standards are detected independently of human
 * inspection, and thus should be more thorough and objective.
 * <P>
 * STRUCTURE CHECKS
 * <ol>
 * <li>The user invokes the <i>QATool</i> to start its work at the project's
 * source directory {@code root/src}, henceforward referred to as the
 * {@root} directory. All other directories that contain source code (
 * {@code .java} files) are assumed to be relative to {@code root}, and
 * organized according to the <i>eChronos</i> programming standards.</li>
 * <li>The root directory {@code root/src} is organized into
 * {@code civ, dmc, hic, pdc}, and {@code util} subdirectories. , dmc,hic,
 * sic</code>, and <code>test</code> packages (directories). The test package is
 * further subdivided into <code>civ, pdc, util, dmc,</code> and
 * <code>sic</code> packages. Note that tests are not expected for the
 * <code>hic</code> package and that some packages may be optional.</li>
 * <li>All files in the <code>src</code> directories have correlates in the test
 * directory: each source <code>Classname.java</code> file has a corresponding
 * <code>Test[Classname].java</code> file in the <code>src/test</code> package,
 * and both <code>.class</code> files are contained together in the
 * <code>root/bin</code> directory. File names that start with <code>Mock</code>
 * are excluded.</li>
 * </ol>
 * <P>
 * FILE CHECKS (<code>import</code> statements)
 * <ol>
 * <li>There must not be any <code>import</code> statements in the
 * <code>hic</code> source files except for <code>civ</code> classes (and
 * standard libraries); and there must not be any <code>hic</code> imports in
 * any non-<code>civ</code> files.</li>
 * <li>There must not be any <code>import</code> statements in the
 * <code>dmc</code> source files except for <code>pdc</code> classes (and
 * standard libraries); and there must not be any <code>dmc</code> imports in
 * any non-<code>pdc</code> files.</li>
 * </ol>
 * <P>
 * FILE CHECKS (QA Tags)
 * <ol>
 * <li>All methods in each class have a corresponding set of tests in the test
 * package class, as indicated by the Javadoc QA tags. <code>@Normal</code>,
 * <code>@Error</code> and <code>@Null</code> QA tags are required unless the
 * source method is included in a <code>@NotNeeded</code> block. Each method in
 * each file must be accounted for by one set of QA tags or the other. The name
 * and number of unit test methods without proper QA tags are reported.</li>
 * </ol>
 * <P>
 * REPORTS (Test File Matching)
 * <ol>
 * <li>All methods in each class have a corresponding set of tests in the test
 * package and generates a report that matches each
 * <code>class.methodName</code> with its <code>testClass.methodName</code>(s).
 * All class files for source code and test code are together in the
 * <code>root/bin</code> directory. A violation report is generated for
 * unmatched source and/or test methods. The name and number of unmatched
 * methods are reported.</li>
 * <li>All methods in each package are displayed so that each source method name
 * can be matched by the user against one or more test method names.</li>
 * </ol>
 * <P>
 * OUTPUT FILES The following output files apply to whatever JUnit test source
 * files are available, and are best requested by the user after the matching
 * process is completed.
 * <ol>
 * <li>Generates a unit test suite containing all available unit tests.</li>
 * <li>Generates an integration test suite for each iteration. All integration
 * test files, prefixed with a <code>TC[nn]</code> filename that represents a
 * use case being tested, are organized into a JUnit test suite source file
 * <code>IntegrationSuite.java</code>. One such suite is placed in each
 * <code>testIter[NN]</code> directory.</li>
 * <li>Generates a full regression suite for each iteration. This suite is
 * called <code>RegressionSuite.java</code> and it contains all integration
 * tests from previous iterations. One such suite is placed in each
 * <code>testIter[NN]</code> directory.</li>
 * </ol>
 *
 * @author Alan Cline
 * @author Cheyney Loffing
 * @version
 * 			<ul>
 *          <li>Build 1.0 Apr 29, 2010 // Original</li>
 *          <li>Build 2.0 Mar 15, 2011 // Overhaul for DaVinci 1.0 release</li>
 *          </ul>
 */
public class QATool {
	/** Minimum number of command line arguments */
	private static final int MIN_NUM_ARGS = 2;
	/** Maximum number of command line arguments */
	private static final int MAX_NUM_ARGS = 3;
	/** Index in command line argument list for specification file */
	private static final int ARG_SPEC_FILE = 0;
	/** Index in command line argument list for export file suffix */
	private static final int ARG_EXPORT_SUFFIX = 1;
	/**
	 * Index in command line argument list for additional library directories
	 */
	private static final int ARG_LIB_DIRS = 2;

	// ________________________________________________________________
	//
	// CONSTRUCTOR(S) AND RELATED METHODS
	// ________________________________________________________________

	/** Private utility constructor */
	private QATool() {
	}

	/**
	 * Entry point for QA tools suite. Accepts arguments from the command line:
	 * <ul>
	 * <li>project's QA specification</li>
	 * <li>export file suffix</li>
	 * <li>additional library directories</li>
	 * </ul>
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		String className = QATool.class.getSimpleName();

		// Verify correct usage
		if ((args == null) || (args.length < MIN_NUM_ARGS) || (args.length > MAX_NUM_ARGS)) {
			MsgCtrl.errMsgln("USAGE: " + className + " <specFileName> <exportFileSuffix> [<additionalLibDirs>]");
			MsgCtrl.errMsgln("\t<specFileName> - name (not path) of the XML specification file");
			MsgCtrl.errMsgln("\t<exportFileSuffix> - name appended to export files");
			MsgCtrl.errMsgln("\t[<additionalLibDirs>] - comma-separated list of directories that "
					+ " contain classes and JARs that must be included for source code checking" + " (optional)");
			MsgCtrl.errMsgln("Results will be found in the export folder of the QATool project.");
		} else {
			// Turn on audit messages
			MsgCtrl.setSuppression(false);

			MsgCtrl.msgln("Running " + className + "...");

			// Read the specification
			String libDirs = (args.length > ARG_LIB_DIRS) ? args[ARG_LIB_DIRS] : null;
			QASpec spec = QASpec.getInstance();
			if (spec.readSpecification(args[ARG_SPEC_FILE], libDirs)) {
				// Verify directory structure
				DirectoryChecker dirCheck = new DirectoryChecker();
				if (dirCheck.checkDirectories()) {
					MsgCtrl.msgln("Directory checking complete");

					// Export directory checking results
					if (dirCheck.exportResults(args[ARG_EXPORT_SUFFIX])) {
						MsgCtrl.msgln("Directory export complete");
					} else {
						MsgCtrl.errMsgln("Directory export failed");
					}
				} else {
					MsgCtrl.errMsgln("Directory checking failed");
				}

				// Scan test directory and build method reference table
				UnitTestChecker scanner = new UnitTestChecker();
				if (scanner.scanTestFiles()) {
					MsgCtrl.msgln("Test checking complete");

					// Export test checking results
					MethodReferenceTable testResults = scanner.getResults();
					if (scanner.exportResults(args[ARG_EXPORT_SUFFIX])) {
						MsgCtrl.msgln("Test export complete");
					} else {
						MsgCtrl.errMsgln("Test export failed");
					}

					// Check for source/test method pairing
					SourceChecker srcChecker = new SourceChecker(testResults);
					if (srcChecker.checkSource()) {
						MsgCtrl.msgln("Source checking complete");

						// Export source checking results
						if (srcChecker.exportResults(args[ARG_EXPORT_SUFFIX])) {
							MsgCtrl.msgln("Source export complete");
						} else {
							MsgCtrl.errMsgln("Source export failed");
						}
					} else {
						MsgCtrl.errMsgln("Source checking failed");
					}
				} else {
					MsgCtrl.errMsgln("Test checking failed");
				}
			} else {
				MsgCtrl.errMsgln("Unable to read XML specification: " + args[ARG_SPEC_FILE]);
			}

			MsgCtrl.msgln(className + " complete");
		}
	}

} // end of QATool class
