
package hic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory.RegKey;
import net.miginfocom.swing.MigLayout;

/**
 * ShuttleList is a pop-up dialog for selecting party members when the Summon
 * Heroes Button is activated.
 * 
 * @author unknown
 *
 */
@SuppressWarnings("serial")
public class ShuttleList extends JDialog {
	// /* Code in main is for testing only */
	// public static void main(String[] args)
	// {
	// try {
	// List<String> l1 = new ArrayList<String>()
	// {
	// {
	// add("string 1");
	// add("abcdefg 2");
	// add("3 bbf4a");
	// }
	// };
	// ShuttleList dialog = new ShuttleList(l1);
	// dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	// dialog.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private final JPanel _contentPanel = new JPanel();

	// List of Hero names that will appear on the left side of the dialog
	@SuppressWarnings("rawtypes")
	private final JList _leftList = new JList();

	// List of Hero names that will appear on the right side of the dialog
	@SuppressWarnings("rawtypes")
	private final JList _rightList = new JList();

	// Indicates the currently selected list between the "left" and "right"
	// lists
	@SuppressWarnings("rawtypes")
	private JList _selectedList;

	// User buttons that appear on the dialog
	private JButton _okButton = new JButton("OK");
	private JButton _xferLeft = new JButton("<");
	private JButton _xferRight = new JButton(">");

	private List<String> _summonableHeroNames;

	/**
	 * Create a ShuttleList with a single list. When this dialog is displayed,
	 * user can manipulate the selected items by placing them in the right side
	 * list.
	 * 
	 * @param summonableHeroes
	 *            the list of Hero names
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ShuttleList(List<Hero> summonableHeroes) {
		// Get String names from Hero List
		_summonableHeroNames = translateHeroesToNames(summonableHeroes);

		// Set "left" list as currently selected
		_selectedList = _leftList;

		/*
		 * Display and operation settings for dialog
		 */
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 525, 400);
		getContentPane().setLayout(new BorderLayout());
		_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(_contentPanel, BorderLayout.CENTER);
		_contentPanel.setLayout(
				new MigLayout("", "[grow,sizegroup lists,center][][grow,sizegroup lists,center]", "[grow][]"));

		// TODO Dave split up sections and add comments below as makes sense to
		// you

		// Add the left-side selection list (list of summonable heros) to the
		// dialog
		{
			JPanel panel = new JPanel();
			_contentPanel.add(panel, "cell 0 0,grow");
			panel.setLayout(new BorderLayout());
			{
				DefaultListModel model = new DefaultListModel();
				for (String hName : _summonableHeroNames) {
					model.addElement(hName);
				}
				_leftList.setModel(model);
				_leftList.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent arg0) {
					}

					@Override
					public void focusGained(FocusEvent arg0) {
						_selectedList = _leftList;
						_rightList.clearSelection();
					}
				});
				_leftList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				panel.add(_leftList, BorderLayout.CENTER);
			}
		}

		// Add the transfer left ("<") and transfer right (">") buttons to the
		// dialog
		{
			JPanel panel = new JPanel();
			_contentPanel.add(panel, "cell 1 0");
			panel.setLayout(new MigLayout("", "[]", "[][][]"));
			{
				// JButton button = new JButton(">");
				// _xferRight = new JButton(">");
				_xferRight.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DefaultListModel model = (DefaultListModel) _leftList.getModel();
						int selIdx = _leftList.getSelectedIndex();
						if (selIdx >= 0) {
							Object selectedItem = model.getElementAt(selIdx);
							model.removeElementAt(selIdx);
							// Prohibit more than one selected item
							DefaultListModel rightSelection = (DefaultListModel) _rightList.getModel();
							if (rightSelection.getSize() < 1) {
								rightSelection.addElement(selectedItem.toString());
								// ((DefaultListModel)
								// _rightList.getModel()).addElement(selectedItem.toString());
							}
						}
					}
				});
				panel.add(_xferRight, "cell 0 1");
			}
			{
				// JButton button = new JButton("<");
				// _xferLeft = new JButton("<");
				_xferLeft.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DefaultListModel model = (DefaultListModel) _rightList.getModel();
						int selIdx = _rightList.getSelectedIndex();
						if (selIdx >= 0) {
							Object selectedItem = model.getElementAt(selIdx);
							model.removeElementAt(selIdx);
							((DefaultListModel) _leftList.getModel()).addElement(selectedItem.toString());
						}
					}
				});
				panel.add(_xferLeft, "cell 0 2");
			}

		}

		// Add right-side selection list (list of heros that will be summoned on
		// "OK") to the dialog
		{
			JPanel panel = new JPanel();
			_contentPanel.add(panel, "cell 2 0,grow");
			panel.setLayout(new BorderLayout());
			{
				DefaultListModel model = new DefaultListModel();
				_rightList.setModel(model);
				_rightList.addFocusListener(new FocusListener() {
					@Override
					public void focusLost(FocusEvent arg0) {
					}

					@Override
					public void focusGained(FocusEvent arg0) {
						ShuttleList.this._selectedList = _rightList;
						_leftList.clearSelection();
					}
				});
				_rightList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				panel.add(_rightList, BorderLayout.CENTER);
			}
		}

		// Add the "Up" button to the dialog
		{
			JPanel panel = new JPanel();
			_contentPanel.add(panel, "cell 2 1");
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btnUp = new JButton("Up");
				btnUp.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DefaultListModel model = (DefaultListModel) _selectedList.getModel();
						int selIdx = _selectedList.getSelectedIndex();
						boolean canMove = (selIdx > 0);
						if (canMove) {
							Object selItem = model.getElementAt(selIdx);
							model.remove(selIdx);
							model.add(selIdx - 1, selItem.toString());
						}
						_selectedList.setSelectedIndex(canMove ? selIdx - 1 : selIdx);
					}
				});
				panel.add(btnUp);
			}
			{
				JButton btnDown = new JButton("Down");
				btnDown.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DefaultListModel model = (DefaultListModel) _selectedList.getModel();
						int selIdx = _selectedList.getSelectedIndex();
						boolean canMove = selIdx < (model.getSize() - 1);
						if (canMove) {
							Object selItem = model.getElementAt(selIdx);
							model.remove(selIdx);
							model.add(selIdx + 1, selItem.toString());
						}
						_selectedList.setSelectedIndex(canMove ? selIdx + 1 : selIdx);
					}
				});
				panel.add(btnDown);
			}
		}

		// Create the "OK" and "Cancel" button section at the bottom of the
		// dialog
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				_okButton.setActionCommand("OK");
				_okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						ShuttleList.this.setVisible(false);
						List<String> hNames = ShuttleList.this.getSelectedHeroNames();
						System.out.print("\nElements selected: ");
						for (String name : hNames) {
							System.out.print(name + " ");
						}
					}
				});
				buttonPane.add(_okButton);
				getRootPane().setDefaultButton(_okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						ShuttleList.this.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	// Gets the names from the "selected" (right) side of the hero selection list
	private List<String> getSelectedHeroNames() {
		int numElts = _rightList.getModel().getSize();
		List<String> selectedHeroNames = new ArrayList<String>();
		for (int i = 0; i < numElts; i++) {
			selectedHeroNames.add((String) _rightList.getModel().getElementAt(i));
		}
		
		return selectedHeroNames;
	}

	private List<String> translateHeroesToNames(List<Hero> heroes) {
		List<String> heroNames = new ArrayList<String>();
		for (Hero h : heroes) {
			heroNames.add(h.getName());
		}
		return heroNames;
	}

	/**
	 * Create a ShuttleList with some items already selected (in the right-hand
	 * list)
	 * 
	 * @param _summonableHeroes
	 *            the items that can be selected from
	 * @param _partyHeros
	 *            the items already selected
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ShuttleList(List<Hero> summonableHeroes, List<Hero> partyHeros) {
		this(summonableHeroes);
		// Get names of partyHeros
		List<String> partyHeroNames = translateHeroesToNames(partyHeros);
		
		
		for (String name : partyHeroNames) {
			((DefaultListModel) _leftList.getModel()).removeElement(name);
			((DefaultListModel) _rightList.getModel()).addElement(name);
		}
	}

	/**
	 * Add the ActionListener that will take an action when the Dialog closes
	 * 
	 * @param action
	 *            Action for OK button
	 */
	public void addActionListener(final ActionListener action) {
		_okButton.addActionListener(action);
	}

	/**
	 * Get the list of Items selected while the dialog was being displayed. If
	 * no items were selected, then an empty list is returned
	 * 
	 * @return the selected list
	 */
	public List<Hero> getSelectedHeroes(HeroRegistry hReg) {
		List<Hero> selectedHeroes = new ArrayList<Hero>();
		for(String hName : this.getSelectedHeroNames()) {
			selectedHeroes.add(hReg.getHero(hName));
		}
		
		return selectedHeroes;
	}

}
