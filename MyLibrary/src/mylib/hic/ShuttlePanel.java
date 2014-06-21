
package mylib.hic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class ShuttlePanel extends JPanel
{
    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        try {
            List<String> l1 = new ArrayList<String>()
            {
                {
                    add("string 1");
                    add("abcdefg 2");
                    add("3 bbf4a");
                }
            };
            JFrame frame = new JFrame();
            ShuttlePanel dialog = new ShuttlePanel(l1);
            ShuttlePanel dialog2 = new ShuttlePanel(l1);
            frame.add(dialog, BorderLayout.EAST);
            frame.add(dialog2, BorderLayout.WEST);
            frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final JList<Object> _leftList = new JList<Object>();
    private final JList<Object> _rightList = new JList<Object>();
    private JList<Object> _selectedList;
    private JButton _okButton = new JButton("OK");


    /**
     * Create a ShuttleList with a single list. When this dialog is displayed, user can manipulate
     * the selected items by placing them in the right side list.
     * 
     * @param list the list of items to display
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ShuttlePanel(List<String> list)
    {
        _selectedList = _leftList;
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new MigLayout("",
                "[grow,sizegroup lists,center][][grow,sizegroup lists,center]",
                "[grow][]"));
        {
            JPanel panel = new JPanel();
            this.add(panel, "cell 0 0,grow");
            panel.setLayout(new BorderLayout());
            {
                DefaultListModel model = new DefaultListModel();
                for (String s : list) {
                    model.addElement(s);
                }
                _leftList.setModel(model);
                _leftList.addFocusListener(new FocusListener()
                {
                    @Override
                    public void focusLost(FocusEvent arg0)
                    {}


                    @Override
                    public void focusGained(FocusEvent arg0)
                    {
                        _selectedList = _leftList;
                        _rightList.clearSelection();
                    }
                });
                _leftList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                panel.add(_leftList, BorderLayout.CENTER);
            }
        }
        {
            JPanel panel = new JPanel();
            this.add(panel, "cell 1 0");
            panel.setLayout(new MigLayout("", "[]", "[][][]"));
            {
                JButton button = new JButton(">");
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent arg0)
                    {
                        DefaultListModel model = (DefaultListModel) _leftList.getModel();
                        int selIdx = _leftList.getSelectedIndex();
                        if (selIdx >= 0) {
                            Object selectedItem = model.getElementAt(selIdx);
                            model.removeElementAt(selIdx);
                            ((DefaultListModel) _rightList.getModel())
                                    .addElement(selectedItem.toString());
                        }
                    }
                });
                panel.add(button, "cell 0 1");
            }
            {
                JButton button = new JButton("<");
                button.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent arg0)
                    {
                        DefaultListModel model = (DefaultListModel) _rightList.getModel();
                        int selIdx = _rightList.getSelectedIndex();
                        if (selIdx >= 0) {
                            Object selectedItem = model.getElementAt(selIdx);
                            model.removeElementAt(selIdx);
                            ((DefaultListModel) _leftList.getModel())
                                    .addElement(selectedItem.toString());
                        }
                    }
                });
                panel.add(button, "cell 0 2");
            }

        }
        {
            JPanel panel = new JPanel();
            this.add(panel, "cell 2 0,grow");
            panel.setLayout(new BorderLayout());
            {
                DefaultListModel model = new DefaultListModel();
                _rightList.setModel(model);
                _rightList.addFocusListener(new FocusListener()
                {
                    @Override
                    public void focusLost(FocusEvent arg0)
                    {}


                    @Override
                    public void focusGained(FocusEvent arg0)
                    {
                        ShuttlePanel.this._selectedList = _rightList;
                        _leftList.clearSelection();
                    }
                });
                _rightList
                        .setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                panel.add(_rightList, BorderLayout.CENTER);
            }
        }
        {
            JPanel panel = new JPanel();
            this.add(panel, "cell 2 1");
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            {
                JButton btnUp = new JButton("Up");
                btnUp.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent arg0)
                    {
                        DefaultListModel model = (DefaultListModel) _selectedList
                                .getModel();
                        int selIdx = _selectedList.getSelectedIndex();
                        boolean canMove = (selIdx > 0);
                        if (canMove) {
                            Object selItem = model.getElementAt(selIdx);
                            model.remove(selIdx);
                            model.add(selIdx - 1, selItem.toString());
                        }
                        _selectedList.setSelectedIndex(canMove ? selIdx - 1
                                : selIdx);
                    }
                });
                panel.add(btnUp);
            }
            {
                JButton btnDown = new JButton("Down");
                btnDown.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent arg0)
                    {
                        DefaultListModel model = (DefaultListModel) _selectedList
                                .getModel();
                        int selIdx = _selectedList.getSelectedIndex();
                        boolean canMove = selIdx < (model.getSize() - 1);
                        if (canMove) {
                            Object selItem = model.getElementAt(selIdx);
                            model.remove(selIdx);
                            model.add(selIdx + 1, selItem.toString());
                        }
                        _selectedList.setSelectedIndex(canMove ? selIdx + 1
                                : selIdx);
                    }
                });
                panel.add(btnDown);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            add(buttonPane, BorderLayout.SOUTH);
            {
                _okButton.setActionCommand("OK");
                _okButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent arg0)
                    {
                        ShuttlePanel.this.setVisible(false);
                        List<Object> selItems = ShuttlePanel.this
                                .getSelectedItems();
                        System.out.print("\nElements selected: ");
                        for (Object o : selItems) {
                            System.out.print(o + " ");
                        }
                    }
                });
                buttonPane.add(_okButton);
                // setDefaultButton(_okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent arg0)
                    {
                        System.out.println("Cancelling");
                    }
                });
                buttonPane.add(cancelButton);
            }
        }
    }


    /**
     * Create a ShuttleList with some items already selected (in the right-hand list)
     * 
     * @param initialItems the items that can be selected from
     * @param selectedItems the items already selected
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ShuttlePanel(List<String> initialItems, List<String> selectedItems)
    {
        this(initialItems);
        for (String s : selectedItems) {
            ((DefaultListModel) _leftList.getModel()).removeElement(s);
            ((DefaultListModel) _rightList.getModel()).addElement(s);
        }
    }


    /**
     * Add the ActionListener that will take an action when the Dialog closes
     * 
     * @param action Action for OK button
     */
    public void addActionListener(final ActionListener action)
    {
        _okButton.addActionListener(action);
    }


    /**
     * Get the list of Items selected while the dialog was being displayed. If no items were
     * selected, then an empty list is returned
     * 
     * @return the selected list
     */
    public List<Object> getSelectedItems()
    {
        int numElts = _rightList.getModel().getSize();
        List<Object> list = new ArrayList<Object>(numElts);
        for (int i = 0; i < numElts; i++) {
            list.add(_rightList.getModel().getElementAt(i));
        }
        return list;
    }

}
