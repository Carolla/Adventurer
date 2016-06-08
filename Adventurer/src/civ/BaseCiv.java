/**
 * BaseCiv.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Base class for all Civs that monitor and manage the HIC widgets
 * 
 * @author Al Cline
 * @version Nov 18, 2015 // original <br>
 */
public class BaseCiv implements MouseListener, MouseMotionListener
{

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  public BaseCiv()
  {}
  
  // ============================================================
  // Mouse handler methods
  // ============================================================

  @Override
  public void mouseClicked(MouseEvent e)
  {}

  @Override
  public void mouseDragged(MouseEvent e)
  {}

  @Override
  public void mouseEntered(MouseEvent e)
  {}

  @Override
  public void mouseExited(MouseEvent e)
  {}

  @Override
  public void mouseMoved(MouseEvent e)
  {}

  @Override
  public void mousePressed(MouseEvent e)
  {}

  @Override
  public void mouseReleased(MouseEvent e)
  {}


} // end of BaseCiv class

