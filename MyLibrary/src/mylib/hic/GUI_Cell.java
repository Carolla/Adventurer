/**
 * GUI_Cell.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *	Helper class to create a standard panel or cell (for GridBagLayout). 
 * Some getters are available to change the default settings.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Apr 4, 2009   	// original <DD>
 * <DT> Build 2.0		Apr 16 2009   	// revised to proper class with default settings <DD>
 * </DL>
 */
public class GUI_Cell
{	
	// Set Defaults for repeated usage
	private Color _bkColor = null;
	private int _cellWidth = 0;
	private int _cellHeight = 0;
	private int _pad = 0;

	/** Default constructor for individual adjustments */
	public GUI_Cell() { }

	
	/** Constructor initializes to constants in the MainFrame class
	 * @param width			of the cell to contain the data
	 * @param height 		of the cell (for various fontsizes)
	 * @param kolor			of the background of the cell
	 * @param padding 	space around the cell as an Inset
	 */
	public GUI_Cell(int width, int height, Color kolor, int padding)
	{
		_bkColor = kolor;
		_cellWidth = width;
		_cellHeight = height;
		_pad =padding;
	}
	
	
	/** 
	 * Create a subpanel with an equal-spaced border for adding a Person attribute widgets.
	 * @return the subpanel with proper spacing
	 */
	public JPanel makeSubpanel()
	{
		// Define centered empty borders for spacing esthetic
		Border spacing = BorderFactory.createMatteBorder(_pad, _pad, _pad, _pad, _bkColor);
		JPanel subpanel = new JPanel();		// FlowLayout by default 
		subpanel.setBackground(_bkColor);
		subpanel.setBorder(spacing);
		return subpanel;
	}				

	
	/** Build a cell (a bordered panel) containing a label and data text.
	 * 
	 * @param cellLbl		the text of the label
	 * @param dataText		the data to show in the cell
	 * @return a subpanel containing both the label and text JLabels
	 */
	public JPanel buildCell(String cellLbl, String dataText)
	{
//		final int CELL_HEIGHT = 50;
		String textline = null;
		// Check for non-label situation
		JLabel lbl = null;
		if (cellLbl == "") {
			textline = dataText;
		}
		else {
			textline = cellLbl + " " + dataText;
		}
		lbl = new JLabel(textline);
		// Create panel as cell
		setPad(_pad);
		setBackColor(_bkColor);
	    JPanel panel = makeSubpanel();
	    panel.setFocusable(true);
	    // Fix the width and height of the cell (in pixels)
	    Dimension cellSize =  new Dimension(_cellWidth, _cellHeight);
	    panel.setPreferredSize(cellSize);
	    Border cellOutline = BorderFactory.createLineBorder(Color.BLACK, 1);
	    panel.setBorder(cellOutline);
	    panel.add(lbl);
		return panel;
	}
	
	
	/** Set the default background Color for multiple use
	 * @param backColor	background color to for all subsequent cell creations 
	 */
	public void setBackColor(Color backColor)
	{
		_bkColor = backColor;
	}

	
	/** Set the default background Color for multiple use
	 * @param width		width of the cell or panel
	 */
	public void setCellWidth(int width)
	{
		_cellWidth = width;
	}
	
	/** Set the default background Color for multiple use
	 * @param height		height of the cell or panel
	 */
	public void setCellHeight(int height)
	{
		_cellHeight = height;
	}

	/** Set the default background Color for multiple use
	 * @param pad	the spacing around the component within the cell or panel
	 */
	public void setPad(int pad)
	{
		_pad = pad;
	}
	
	
}	// end of GUI_Utilities class

