/**
 * MockMainframe.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import hic.BuildingRectangle;
import hic.ChronosPanel;
import hic.MainframeInterface;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JComponent;

import civ.MainActionCiv;



/**
 * Facade for GUI Mainframe object to send and receive messages for testing.
 * 
 * @author Al Cline
 * @version Feb 7, 2015 // original <br>
 */
public class MainframeProxy implements MainframeInterface
{
    /** Buffer for holding messages for auditing */
    private String _msg;
    private String _errMsg;


    /** Default constructor */
    public MainframeProxy()
    {
        // MsgCtrl.auditMsgsOn(false);
        // MsgCtrl.errorMsgsOn(false);
    }


    /** Replace the button panel with the final IOPanel */
    public void addIOPanel()
    {
        // MsgCtrl.where(this);
    }

    @Override
    public void addPanel(JComponent comonent)
    {
        // MsgCtrl.where(this);
    }

    /**
     * Display error text into the output panel
     * 
     * @param imagePath text description of image location
     */
    public void displayErrorText(String errText)
    {
        // MsgCtrl.where(this);
        // MsgCtrl.errMsgln("\t" + errText);
        _errMsg = errText;
    }

    /**
     * Display a prompt for user to asnwer YES or NO
     * 
     * @param text to be displayed
     */
    @Override
    public boolean displayPrompt(String text)
    {
        // MsgCtrl.where(this);
        return true;
    }

    /**
     * Display the text into the output panel
     * 
     * @param text to be displayed
     */
    public void displayText(String text)
    {
        // MsgCtrl.where(this);
        _msg = text;
    }

    public String errMsgOut()
    {
        // MsgCtrl.where(this);
        return _errMsg;
    }

    /** Return the size of the image space; set here temporarily */
    public Dimension getImagePanelSize()
    {
        // MsgCtrl.where(this);
        return new Dimension(800, 600);
    }

    /**
     * Return last message out and clear buffer
     * 
     * @return whatever message was last intended for the GUI
     */
    public String msgOut()
    {
        // MsgCtrl.where(this);
        return _msg;
    }

    /**
     * Display a prompt message asking for confirmation
     * 
     * @param mesg question to ask for confirmation
     * @return true if the user selected YES
     */
    public boolean msgPrompt(String msg)
    {
        // MsgCtrl.where(this);
        return true;
    }

    /*
     * Misplaced redraw command in BuildingDisplayCiv
     */
    @Override
    public void redraw()
    {
        // MsgCtrl.where(this);
    }

    /**
     * Define a bounding rectangle to highlight a building
     * 
     * @param rect rectangle around the building
     */
    public void setBuilding(BuildingRectangle rect)
    {
        // MsgCtrl.where(this);
    }


    /*
     * (non-Javadoc)
     * 
     * @see hic.IOPanelInterface#setImage(java.lang.String)
     */
    @Override
    public void setImage(Image image)
    {
        // MsgCtrl.where(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hic.IOPanelInterface#setImageTitle(java.lang.String)
     */
    @Override
    public void setImageTitle(String bldgName)
    {
        // MsgCtrl.where(this);
        // MsgCtrl.msgln("\tbuilding name = " + bldgName + "\n");
    }


    @Override
    public void repaint()
    {}


    @Override
    public void setTitle(String title)
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void showHelp()
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void addIOPanel(MainActionCiv mac)
    {
      // TODO Auto-generated method stub
      
    }


    @Override
    public void back()
    {
      // TODO Auto-generated method stub
      
    }


    @Override
    public void replaceLeftPanel(ChronosPanel leftPanel)
    {
      // TODO Auto-generated method stub
      
    }


    @Override
    public void setLeftPanelTitle(String title)
    {
      // TODO Auto-generated method stub
      
    }

    public String getMsg()
    {
      return _msg;
    }

} // end of MainframeProxy class

