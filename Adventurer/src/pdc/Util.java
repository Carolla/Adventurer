/**
 * Util.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.awt.Font;
import java.io.File;

import mylib.MsgCtrl;
import chronos.Chronos;

/**
 * Collection of static utility routines
 * 
 * @author alancline
 * @version Aug 3, 2014 // original <br>
 */
public class Util
{
  /** Non-instantiable utility class */
  private Util()
  {}

  /**
   * Create a Runic font that simulates English letters. <br>
   * Warning: Be careful of character selection and float size; round-up errors for {@code float} 
   * sizes can cause overruns on displayed Components.
   * 
   * @param height of the font
   * @return the Font class
   */
  static public Font makeRunicFont(float fontHt)
  {
    Font font = null;
    try {
      Font newFont =
          Font.createFont(Font.TRUETYPE_FONT, new File(Chronos.RUNIC_ENGLISH2_FONT_FILE));
      font = newFont.deriveFont(fontHt);
    } catch (Exception e) {
      MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
    }
    return font;
  }

} // end of Util class
