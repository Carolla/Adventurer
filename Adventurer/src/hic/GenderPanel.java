
package hic;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import mylib.Constants;

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
     * p389) radioButton,actionCommand is always null, so can be used as a default value setting for
     * retrieving actual user selection
     */
    _maleButt = makeRadioButton(MALE, true);
    _femaleButt = makeRadioButton(FEMALE, false);

    // Buttons must be added to BOTH the group and the panel
    _groupSex.add(_maleButt);
    _groupSex.add(_femaleButt);
    _radioPanel.add(_maleButt);
    _radioPanel.add(_femaleButt);
  }

  private JRadioButton makeRadioButton(String label, boolean enabled)
  {
    JRadioButton button = new JRadioButton(label, enabled);
    button.setActionCommand(label);
    button.setBackground(Constants.MY_BROWN);
    button.setEnabled(enabled);
    return button;
  }

  public String getSelectedGender()
  {
    String cmdValue = _groupSex.getSelection().getActionCommand();
    return (cmdValue.equals(MALE)) ? MALE : FEMALE;
  }

  public Component toJPanel()
  {
    return _radioPanel;
  }
}
