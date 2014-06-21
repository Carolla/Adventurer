/**
 * GBC.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * Simplifies the use of <code>GridBagConstraints</code> class, which is
 * used extensively for Swing's <code>GridBagLayout Manager</code>.
 * This class is adapted from <i>Core Java: Volume 1 - Fundamentals, 8/e</i> by 
 * Cay S. Horstmann and Gary Cornell, p433-435, Sun Microsystems Press, (c) 2008
 * 
 * @author Cay Horstmann, Al Cline
 * @version <DL>
 * <DT> Build 1.01 	May 6 2004	// Cay's code adapted for Carolla's conventions <DD>
 * </DL>
 */
@SuppressWarnings("serial")
public class GBC extends GridBagConstraints
{
	// Internal constrants
//	/** The gridx position (0-based column number) */
//	protected int _gridx = 0;
//	/** The gridy position (0-based row number) */
//	private int _gridy = 0;
//	/** Cell-span in the x-direction (how many columns this cell uses) */
//	private int _gridwidth = 0;
//	/** Cell-span in the y-direction (how many rows this cell uses) */
//	private int _gridheight = 0;
//	/** The compassed-based direction of where the cell should start in a component 
//	 * area: e.g. GridBagConstraints.CENTER (Default), or GridBagConstraints.NORTHEAST. */
//	private int _anchor = GridBagConstraints.CENTER;
//	/** Location where the cell will be extended from, used when filling the entire component 
//	 * area is not wanted; e.g. GridBagConstraints.NONE (Default), or 
//	 * GridBagConstraints.VERTICAL.*/ 
//	private int _fill = GridBagConstraints.NONE;
//	/** Sets the cell weights: the amount of flexibility allowed when resizing a cell when the
//	 * component resizes. A value of 0 means the size remains fixde; a size of 100 is a
//	 * good default choice.  

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
	/** Constructs a GBC with a given gridx and grid y positiojn and all other grid bag
	 * constraint values set to the default.
	 * @param gridx	the gridx position (0-based column number)
	 * @param gridy	the gridy position (0-based row number)
	 */
	public GBC(int gridx, int gridy)
	{
		this.gridx = gridx;
		this.gridy = gridy;
	}

	
	/** Constructs a GBC with a given gridx, gridy, gridwith, and grideight and all other
	 * grid bag constraint values set to the default.
	 * 
	 * @param gridx	the gridx position (0-based column number)
	 * @param gridy	the gridy position (0-based row number)
	 * @param gridwidth 	the cell-span in the x-direction (how many columns this cell uses)
	 * @param gridheight 	the cell-span in the y-direction (how many rows this cell uses)
	 */
	public GBC(int gridx, int gridy, int gridwidth, int gridheight)
	{
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
	}

	
	/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
	 * 					PUBLIC METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** Sets the anchor: the compassed-based direction of where the cell should start in a
	 * component area: e.g. GridBagConstraints.CENTER (Default).
	 * 
	 * @param anchor 	anchor value
	 * @return this object for further modification
	 */
	public GBC setAnchor(int anchor)
	{
		this.anchor = anchor;
		return this;
	}
	
	
	/** Sets the fill direction: the location that the cell will be extended from, used when
	 * the compnent size is less than the display size 
	 * e.g. GridBagConstraints.NONE (Default), or GridBagConstraints.HORIZONTAL. 
	 * 
	 * @param fill	the fill direction 
	 * @return this object for further modification
	 */
	public GBC setFill(int fill)
	{
		this.fill = fill;
		return this;
	}
	
	
	/** Sets the Insets of this cell, that is, the blank padding around each cell
	 * @param distance the spacing to use in all directions
	 * @return this object for further modifications
	 */
	public GBC setInsets(int distance)
	{
		this.insets = new Insets(distance, distance, distance, distance);
		return this;
	}
	

	/** Sets the Insets of this cell, that is, the blank padding around each cell
	 * @param top		the spacing to use above the cell
	 * @param left		the spacing to use to the left the cell
	 * @param bottom		the spacing to use below the cell
	 * @param right	the spacing to use to the right of cell
	 * @return this object for further modifications
	 */
	public GBC setInsets(int top, int left, int bottom, int right)
	{
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}
	

	/** Sets the internal padding: the spacing within the cell to force it to maintain no less
	 * than its minimum size.
	 * 
	 * @param ipadx		the internal padding in the x (column) direction
	 * @param ipady		the internal padding in the y (row) direction
	 * @return this object for further modifications
	 */
	public GBC setIpad(int ipadx, int ipady)
	{
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}


	/** Sets cell row and column on the grid
	 * 
	 * @param col				the 0-based column number to place the cell
	 * @param row			the 0-based row number to place the cell
	 * @param colSpan		the number of columns the cell will span
	 * @param rowSpan	the number of rows the cell will span
	 * @return this object for further modifications
	 */
	public GBC setPos(int col, int row, int colSpan, int rowSpan)
	{
		this.gridx = col;
		this.gridy = row;
		this.gridwidth = colSpan;
		this.gridheight = rowSpan;
		return this;
	}

	
	/** Sets the cell weights: the amount of flexibility allowed when resizing a cell when the
	 * component resizes. A value of 0 means the size remains fixed; a size of 100 is a
	 * good default choice.  
	 * 
	 * @param weightx	the cell weight in the x (column) direction
	 * @param weighty	the cell weight in the y (row) direction
	 * @return this object for further modification
	 */
	public GBC setWeights(double weightx, double weighty)
	{
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}
		

} // end of GBC class

