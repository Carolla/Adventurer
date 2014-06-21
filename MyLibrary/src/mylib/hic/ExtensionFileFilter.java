/**
 * ExtensionFileFilter.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.util.*;
import javax.swing.filechooser.FileFilter;

import java.io.*;


/**
 * Screens out files so that only the particular files are shown to the user in the 
 * <code>JFileChooser</code> dialog, for saving or opening. This class is a 
 * Helper class for MyFileChooser.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jan 10 2009 	 // refactored from multiple subclasses <DD>
 * </DL>
 */
public class ExtensionFileFilter extends FileFilter
{	
    /** Description of the kinds of file; shown in the select button */
	private String _description = null;

	/** Possible file extension in the filter */
	private ArrayList<String> _extensionList = null;
	
	/** Unused default constructor */
	public ExtensionFileFilter() { }
	
	/** Creates the file filter with given extension and options 
	 * @param ext      legal file name extension (after the period) as selectable 
	 * @param desc   the file chooser label over the accept button 
	 */
	public ExtensionFileFilter(String ext, String desc) 
	{ 
		_extensionList = new ArrayList<String>();
		// View on files with the proper extension
	    addExtension(ext);
	    // Set the button to Save or Open files
    	setDescription(desc);
	}

	
	/** Add a file extension to check before selecting a dungeon file
	 * @param ext	add a filename extension to the list of valid files
	 */
	public void addExtension(String ext)
	{
		_extensionList.add(ext);
	}
	

	/** Get the file extension list for these kinds of files. 
	 * @param index into the extension list to retrieve
	 * @return	the filename extension
	 */
	public String getExtension(int index)
	{
		if (index > _extensionList.size() ) {
			return null;
		}
		else {
			return _extensionList.get(index);
		}
	}

	
	/** Add a description for the category of file for this filter
	 * @param desc 	add a description for the spinner in JFileChooser
	 */
	public void setDescription(String desc)
	{
		_description = desc;
	}
	
	
	/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
	 * 							REQUIRED Implementations for FileFilter Abstract class
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
	/** Required implementation: Accept only files with FILE_EXT in this instantiation
	 * @param f	the file to verify for acceptance
	 * @return true if the file is accepted
	 */
	public boolean accept(File f) 
	{
		// Always allow directories...
		if (f.isDirectory()) {
			return true;
		}
		// ...otherwise, check for proper file extensions
		for (int k=0; k < _extensionList.size(); k++) {
			if (f.getName().toLowerCase().endsWith(_extensionList.get(k)))  {
				return true;
			}
		}
		return false;
	}
				
		
    /**
     * Required implementation: Get the description of the file extension.
     * @return the file extension for this filter
     */  
     public String getDescription() 
     {
    	 return _description;
     }

          
} 	// end of ExtensionFileFilter class

