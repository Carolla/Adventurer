/**
 * MainActionCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import hic.ChronosPanel;
import hic.IOPanel;
import hic.Mainframe;
import hic.NewHeroIPPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import mylib.Constants;
import hic.ShuttleList;
import net.miginfocom.swing.MigLayout;
import pdc.command.CommandFactory;
import chronos.Chronos;
import chronos.pdc.Adventure;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.character.Hero;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * The main civ behind the Mainframe screen. It creates the MainActionPanel
 * consisting of the primary buttons: Select an Adventure, Summon Heroes, and
 * Create a New Hero
 * 
 * @author Alan Cline
 * @version Nov 7, 2015 // original <br>
 */
public class MainActionCiv extends BaseCiv {
	private AdventureRegistry _advReg;
	private MainframeCiv _mfCiv;
	private RegistryFactory _rf;

	private List<Hero> _partyHeros;

	/** Controls left side and right side panels */
	private ChronosPanel _actionPanel;

	/** Amount of space in pixels around the frame and image of aesthetics */
	public static final int FRAME_PADDING = 90;

	/** Title of the initial three-button panel on left side */
	private final String INITIAL_OPENING_TITLE = " Select Your Action ";

	private final String REGISTRAR_IMAGE = "raw_Register.jpg";
	private final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
	private final String ADV_IMAGE = "icn_Town.jpg";

	private final String INITIAL_IMAGE = "ChronosLogo.jpg";
	private final String INITIAL_IMAGE_TITLE = "Chronos Logo";
	private Scheduler _skedder;

	// ============================================================
	// Constructors and constructor helpers
	// ============================================================

	/**
	 * Create the Civ associated with the mainframe and main button panel
	 * 
	 * @param frame
	 *            owner of the widget for which this civ applies
	 * @param personRW
	 *            supports the Summon Hero and Create Hero buttons
	 */
	public MainActionCiv(MainframeCiv mfciv) {
		_mfCiv = mfciv;
		constructMembers();

		createActionPanel();
		setActivePanel();
	}

	protected void constructMembers() {
		_skedder = new Scheduler(); // Skedder first for injection

		_rf = new RegistryFactory(_skedder);
		_rf.initRegistries();

		_advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
	}

	/**
	 * Create the Adventure, Heroes, and Create-Hero buttons, and button panel
	 * for them
	 */
	protected ChronosPanel createActionPanel() {
		JButton adventureButton = createAdventureButton();
		JButton summonButton = createSummonHeroesButton();
		JButton creationButton = createNewHeroButton();

		_actionPanel = new ChronosPanel(INITIAL_OPENING_TITLE);

		// Align all buttons in a single column
		_actionPanel.setLayout(new MigLayout("wrap 1"));
		Dimension frame = Mainframe.getWindowSize();
		_actionPanel
				.setPreferredSize(new Dimension((int) (frame.width - FRAME_PADDING) / 2, frame.height - FRAME_PADDING));
		_actionPanel.setBackground(Constants.MY_BROWN);

		/** Buttons are at 25% to allow space for Command Line later */
		_actionPanel.add(adventureButton, "hmax 25%, grow");
		_actionPanel.add(summonButton, "hmax 25%, grow");
		_actionPanel.add(creationButton, "hmax 25%, grow");

		return _actionPanel;
	}

	protected void setActivePanel() {
		_mfCiv.replaceLeftPanel(_actionPanel);
		_mfCiv.displayImage(INITIAL_IMAGE_TITLE, INITIAL_IMAGE);
		loadSelectedAdventure("The Quest for Rogahn and Zelligar");
	}

	// ============================================================
	// Private methods
	// ============================================================

	private void createHero() {
		NewHeroCiv nhCiv = new NewHeroCiv(_mfCiv, (HeroRegistry) _rf.getRegistry(RegKey.HERO));
		NewHeroIPPanel ipPanel = new NewHeroIPPanel(nhCiv, _mfCiv);
		_mfCiv.replaceLeftPanel(ipPanel);
	}

	/**
	 * Load the selected adventure from the Adventure registry. Replace the
	 * opening button panel with the IOPanel (text and command line)
	 * 
	 * @param adventureName
	 *            selected from the Adventure by the user
	 */
	private void loadSelectedAdventure(String adventureName) {
		Adventure adv = _advReg.getAdventure(adventureName);

		// Create all the objects used in town
		BuildingDisplayCiv bldgCiv = new BuildingDisplayCiv(_mfCiv, adv,
				(BuildingRegistry) _rf.getRegistry(RegKey.BLDG));

		CommandFactory cmdFac = new CommandFactory(_mfCiv, bldgCiv);
		cmdFac.initMap();
		CommandParser parser = new CommandParser(_skedder, cmdFac);

		IOPanel iop = new IOPanel(parser);
		_mfCiv.replaceLeftPanel(iop);
		iop.requestFocusInWindow();

		// Wait until everything created to finally display the town
		bldgCiv.openTown();
	}

	// ============================================================
	// Private methods
	// ============================================================

	/**
	 * Create the behavior for selecting an adventure, which drives the frame
	 * update. <br>
	 * Warning: Known bug with MigLayout in that {@code float} font sizes can
	 * cause overruns on round-up calculations. "Choose your Adventure" overruns
	 * the button length, but "Select your Adventure" does not, despite being
	 * the same number of characters!
	 * 
	 * @return the button created
	 */
	private JButton createAdventureButton() {
		JButton button = createButtonWithTextAndIcon(ADV_IMAGE, "Select your Adventure ");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Adventure> adventures = _advReg.getAdventureList();
				Object[] adventuresArr = adventures.toArray();
				Object selectedValue = JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
						JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
				if (selectedValue != null) {
					loadSelectedAdventure(selectedValue.toString());
				}
			}
		});
		return button;
	}

	// ============================================================
	// Public methods:
	// ============================================================

	private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText) {
		JButton button = new JButton(buttonText);
		button.setBackground(Constants.MY_BROWN);

		button.setFont(Chronos.STANDARD_FONT);
		button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
		button.setIconTextGap(40);
		return button;
	}

	// ============================================================
	// Public methods:
	// ============================================================

	/**
	 * Create the button to call the NewHeroCiv, which will control the
	 * NewHeroIOPanel that collects the new Hero data, and calls HeroDisplayCiv
	 * that displays the Hero's stats panel
	 *
	 * @return the button
	 */
	private JButton createNewHeroButton() {
		JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, "Create New Heroes");
		button.addActionListener(action -> createHero());
		return button;
	}

	// ============================================================
	// Public methods:
	// ============================================================

	/* This button code is followed by a series of inner methods */
	private JButton createSummonHeroesButton() {
		JButton button = createButtonWithTextAndIcon(HALL_IMAGE, "Summon Heroes");
		button.addActionListener(new ActionListener() {
			HeroRegistry heroReg = (HeroRegistry) _rf.getRegistry(RegKey.HERO);
			private List<Hero> summonableHeroes;

			// This happens when SummonHeros is clicked
			public void actionPerformed(ActionEvent e) {
				summonableHeroes = heroReg.getAll();

				if (_partyHeros.size() == 0) {
					showPartyPickerWhenPartyEmpty();
				} else {
					showPartyPickerWhenMembersAlreadySelected();
				}
			}

			private void showPartyPickerWhenPartyEmpty() {
				// padHeroes(_summonableHeroes);
				final ShuttleList slist = new ShuttleList(summonableHeroes);
				setPropsForShuttleList(slist);
			}

			private void showPartyPickerWhenMembersAlreadySelected() {
				final ShuttleList slist = new ShuttleList(summonableHeroes, _partyHeros);
				setPropsForShuttleList(slist);
			}
			
			// Additional settings for the ShuttleList
			private void setPropsForShuttleList(final ShuttleList slist) {
				slist.setTitle("Choose your Adventurers!");
				slist.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						List<Hero> list = new ArrayList<Hero>();
						for (Hero s : slist.getSelectedHeroes(heroReg)) {
							list.add(s);
						}
						_partyHeros = list;
						slist.dispose();
					}
				});
				slist.setVisible(true);
			}

			// private void padHeroes(List<String> list)
			// {
			// if (list.size() < 3) {
			// list.add("Gronkhar the Smelly");
			// list.add("Siobhan the Obsiquious");
			// list.add("Sir Will-not-be-appearing-in-this-movie");
			// }
			// }
		});
		return button;
	}
} // end of MainActionCiv class
