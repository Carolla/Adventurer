/**
 * MyFileChooser.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
// base class


/** Creates a specialized instance of JFileChooser to open a file of a given type 
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Oct 4 2008		// original <DD>
 * <DT> Build 1.1		Oct 18 2008   // make a singleton to avoid folder overhead
 *          <DD>
 * <DT> Build 1.2		Nov 1 2008  	 // remove singleton to put onus on caller 
 *          <DD>
 * <DT> Build 1.3		Jan 10 2009  	 // removed all application-specific references 
 *           <DD>
 * <DT> Build 1.4      Jul 5 2010      // support stand-along action for multiple GUI
 *           widgets <DD>
 * </DL>
 */
@SuppressWarnings("serial")
public class MyFileChooser extends JFileChooser
{	
	// Define error messages and title of error dialogs
	static private final String OPEN_ERROR_MSG =  "Error! Cannot open file.";
	static private final String OPEN_ERROR_TITLE =  "FILE OPEN ERROR";
	static private final String MISSING_FILE_MSG =  "Error! Cannot find file ";
	static private final String MISSING_FILE_TITLE =  "MISSING FILE ERROR";

	/** Defines whether to display Save Dialog or Open Dialog */
	static public String NO_DEFAULT_NAME = null;

	/** Keep track of whether to Open or Save files */
	private boolean _saveFlag = false;

			
	/** Constructor called by buttons and menu items
	 * 
	 * @param startPath	default directory to Open or Save files
	 * @param dialogType		SaveDialog or OpenDialog
	 * @param defaultName	filename to place in dialog window for saving or 
	 *          opening; can be null
	 * @param clientFilter 	passed in from caller and provides file type callback 
	 *         methods; the extension and description properties should already be 
	 *         added to the filter
	 */
	public MyFileChooser(String startPath, int dialogType, String defaultName,
						ExtensionFileFilter clientFilter) 
	{
		super(startPath);
	    setFileFilter(clientFilter);
		
		// Bring up JFileChooser so user can select an existing file (starts in default directory).
		setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    if (defaultName != null) {
	    	// Search out if one of the extension is ok; use first extension as default
	    	setSelectedFile(new File(defaultName + clientFilter.getExtension(0)));
	    }
		// View on files with the proper extension
//	    _filter.addExtension(ext);
	    // Set the button to Save or Open files
//    	_filter.setDescription(ext + " Files ");
//	    _setFileFilter(_filter);
	    // Set flag depending on what type of dialog was requested
//	    _saveFlag = (dialogType == JFileChooser.SAVE_DIALOG) ? true : false;
	    _saveFlag = (dialogType == SAVE_DIALOG) ? true : false;
	}

	
	/** Perform the selecting and opening of the file
	 * @return the File object opened, or null if cancelled or an error
	 */
	public File getFile()
	{
		// The file selected and to be returned
		File selectedFile = null;
		
		// Open the chooser without a parent so that it displays center screen
		// It will return one of three results
		int result = JFileChooser.CANCEL_OPTION;		// init to safe value
		if (_saveFlag == true) {
			result = showSaveDialog(null);
		}
		else {
			result = showOpenDialog(null);
		}

		// Skip opening if user selects cancel
		if (result == JFileChooser.CANCEL_OPTION)	{
			return null;
		} 

		// Can't open if an IO error occurs
		if (result == JFileChooser.ERROR_OPTION)	
		{
			Toolkit kit = Toolkit.getDefaultToolkit();
			kit.beep();		
			JOptionPane.showMessageDialog(null, OPEN_ERROR_MSG,
							OPEN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return null;
		} 

		// Call the dungeon open method to read from the file.
		if (result == JFileChooser.APPROVE_OPTION)	
		{
			// Accept only dungeon file extensions
			selectedFile = getSelectedFile();
		}
		return selectedFile;
	}

	/** Set the starting directory for the dialog window to the directory given
	 * @param startDir		the directory (absolute path) in which to start the file list display
	 */
	public void setStartDirectory(String startDir)
	{
	    File start = null;
	    try {
	        start = new File(startDir);
	    } catch (NullPointerException e) {
	        // Check if startDir is a legitimate directory or filename
			JOptionPane.showMessageDialog(null, MISSING_FILE_MSG + startDir,
							MISSING_FILE_TITLE, JOptionPane.ERROR_MESSAGE);
		}
		setCurrentDirectory(start);
	}

	/** Find the extension for a particlar file; that is, confirm that the last three characters
	 * of the filename are preceded by a period. This is a general utility method so is made
	 * static so it can be called without creating a MyFileChooser object.
	 * @param filename the name of the file
	 * @return the last three characters of the filename, else null
	 */
	static public String getExtension(String filename)
	{
		int len = filename.length();					// 1-based counting
		int k = filename.lastIndexOf(".")+1;			// 0-based counting
		if (k != len-3) {
			return null;
		}
		String ext = filename.substring(len-3, len);
		return ext;
	}
		
		
}	// end of MyFileChooser class
