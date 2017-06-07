
package hic;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import mylib.Constants;

/**
 * Creates a generic button set to enable the user to select a gender: Male (default) or Female.
 * <P>
 * <b> NOTE: Java 8 changed the way JRadioButtons are created. The second parm in the constructor
 * {@code JRadioButton(String label, boolean selectable)} means that the button is active, not that
 * it is selected by default; which was the case in the now-deprecated Java 7 functionality.</b>
 * 
 * @author Al Cline
 * @version Jun 7, 2017 // updated from a much older class to make it active again <br>
 */
public class GenderPanel
{
  private JRadioButton _maleButt;
  private JRadioButton _femaleButt;
  private ButtonGroup _groupSex;

  private final String MALE = "Male";
  private final String FEMALE = "Female";
  private final JPanel _radioPanel;

  public GenderPanel()
  {
    _radioPanel = new JPanel();
    Border border = BorderFactory.createLineBorder(Color.WHITE);
    _radioPanel.setBorder(border);
    _radioPanel.setBackground(Constants.MY_BROWN);

    _groupSex = new ButtonGroup();

    /*
     * Define the male radio button (default to Male button selected) Per Cay Horstman (Volume 1,
     * p389) radioButton, actionCommand is always null, so can be used as a default value setting
     * for retrieving actual user selection
     */
    // The second arg does not set the default, but makes the buttons settable (selectable).
    // If set to 'false', the button and label is disabled, and unselectable to the user. 
    _maleButt = makeRadioButton(MALE, true);
    _femaleButt = makeRadioButton(FEMALE, true);

    // Buttons must be added to BOTH the group and the panel
    _groupSex.add(_maleButt);
    _groupSex.add(_femaleButt);
    _radioPanel.add(_maleButt);
    _radioPanel.add(_femaleButt);
  }


  /** Return the gender selected */
  public String getSelectedGender()
  {
    return _groupSex.getSelection().getActionCommand();
  }


  /** Identify this radio button JPanel */
  public Component toJPanel()
  {
    return _radioPanel;
  }

  /**
   * Make a radioButton with label.
   * 
   * @param label to identify selection to be made
   * @param enabled allow the button to be selected (this is not the default setting)
   * @return the actual Button object
   */
  private JRadioButton makeRadioButton(String label, boolean enabled)
  {
    JRadioButton button = new JRadioButton(label, enabled);
    button.setActionCommand(label);
    button.setBackground(Constants.MY_BROWN);
    button.setEnabled(enabled);
    return button;
  }

} // end of GenderPanel class
