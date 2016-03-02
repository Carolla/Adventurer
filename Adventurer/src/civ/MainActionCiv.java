/**
 * MainActionCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import chronos.Chronos;
import chronos.pdc.Adventure;
import chronos.pdc.character.Hero;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import hic.ChronosPanel;
import hic.IOPanel;
import hic.Mainframe;
import hic.NewHeroIPPanel;
import hic.ShuttleList;
import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import pdc.command.CommandFactory;

/**
 * The main civ behind the Mainframe screen. It creates the MainActionPanel consisting of the
 * primary buttons: Select an Adventure, Summon Heroes, and Create a New Hero
 * 
 * @author Alan Cline
 * @version Nov 7, 2015 // original <br>
 */

public class MainActionCiv extends BaseCiv
{
    private AdventureRegistry _advReg;
    private HeroRegistry _dorm;
    private MainframeCiv _mfCiv;
    private HeroDisplayCiv _hdCiv;
    private RegistryFactory _rf;
    private Scheduler _skedder;

    private Hero _activeHero;

    /** Amount of space in pixels around the frame and image of aesthetics */
    public static final int FRAME_PADDING = 90;

    /** Title of the initial three-button panel on left side */
    private final String INITIAL_OPENING_TITLE = " Select Your Action ";

    /** Title of the three buttons */
    private final String LOAD_ADVENTURE_TITLE = " Select Your Adventure ";
    private final String SUMMON_HERO_TITLE = " Summon Heroes ";
    private final String CREATE_HERO_TITLE = " Create a New Hero ";

    private final String REGISTRAR_IMAGE = "raw_Register.jpg";
    private final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
    private final String ADV_IMAGE = "icn_Town.jpg";
    
    private ChronosPanel _actionPanel;
    private JButton _summonButton;
    
    // Button ToolTips
    private static final String SUMMON_DISABLED_HOVER = "Create heroes to enable summoning";
    private static final String SUMMON_ENABLED_HOVER = " Heroes waiting to be summoned";
    private static final String SMN_ENBLD_SINGL_HOVER = " Hero waiting to be summoned";

    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /**
     * Create the Civ to display and handle the MainActionPanel of buttons
     * 
     * @param mfCiv handler for the mainframe
     */
    public MainActionCiv(MainframeCiv mfciv)
    {
        _mfCiv = mfciv;
        _hdCiv = new HeroDisplayCiv(_mfCiv, this);
        constructCoreMembers();
        _mfCiv.replaceLeftPanel(createActionPanel());
    }

    private void constructCoreMembers()
    {
        _skedder = new Scheduler(_mfCiv); // Skedder first for injection

        _rf = new RegistryFactory();
        _rf.initRegistries(_skedder);

        _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
        _dorm = (HeroRegistry) _rf.getRegistry(RegKey.HERO);
    }
    
    private void summonHero(String heroName)
    {
        boolean firstSummon = false;
        Hero summonee = _dorm.getHero(heroName);
        if (summonee != null) {
            _hdCiv.displayHero(summonee, firstSummon);
        } else {
            System.err.println("Hero was not summoned.");
        }
    }

    // ============================================================
    // Public methods
    // ============================================================

    public void createHero()
    {
        NewHeroCiv nhCiv = new NewHeroCiv(_mfCiv, _dorm);
        NewHeroIPPanel ipPanel = new NewHeroIPPanel(nhCiv, _mfCiv, this);
        _mfCiv.replaceLeftPanel(ipPanel);
        ipPanel.setDefaultFocus(); // default focus only works after panel is
        // displayed
    }

    public List<String> getAdventureList()
    {
        List<String> adventures = _advReg.getAdventureList();
        return adventures;
    }
    
    /**
     * Sets Summon Heroes button to enabled or disabled based on the presence of saved heroes in the
     * dormitory.
     */
    public void toggleSummonEnabled()
    {
        List<String> hNames = _dorm.getHeroNames();
        if ((hNames.isEmpty()) == false) {
            _summonButton.setEnabled(true);
            if (hNames.size() == 1)
            {
                _summonButton.setToolTipText(hNames.size() + SMN_ENBLD_SINGL_HOVER);
            } else {
                _summonButton.setToolTipText(hNames.size() + SUMMON_ENABLED_HOVER);
            }
        } else {
            _summonButton.setEnabled(false);
            _summonButton.setToolTipText(SUMMON_DISABLED_HOVER);
        }
    }

    // ============================================================
    // Private methods
    // ============================================================

    public HeroRegistry getDormitory()
    {
        return _dorm;
    }

    /**
     * Load the selected adventure from the Adventure registry. Replace the opening button panel
     * with the IOPanel (text and command line)
     * 
     * @param adventureName selected from the Adventure by the user
     */
    public void loadSelectedAdventure(String adventureName)
    {
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
     * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
     */
    private ChronosPanel createActionPanel()
    {
        JButton adventureButton = createAdventureButton();
        _summonButton = createSummonHeroesButton();
        JButton creationButton = createNewHeroButton();
        
        // Set status of Summon Button
        toggleSummonEnabled();

        _actionPanel = new ChronosPanel(INITIAL_OPENING_TITLE);

        // Align all buttons in a single column
        _actionPanel.setLayout(new MigLayout("wrap 1"));
        Dimension frame = Mainframe.getWindowSize();
        _actionPanel
                .setPreferredSize(new Dimension((int) (frame.width - FRAME_PADDING) / 2,
                        frame.height - FRAME_PADDING));
        _actionPanel.setBackground(Constants.MY_BROWN);

        /** Buttons are at 25% to allow space for Command Line later */
        _actionPanel.add(adventureButton, "hmax 25%, grow");
        _actionPanel.add(_summonButton, "hmax 25%, grow");
        _actionPanel.add(creationButton, "hmax 25%, grow");

        return _actionPanel;
    }

    /**
     * Create the behavior for selecting an adventure, which drives the frame update. <br>
     * Warning: Known bug with MigLayout in that {@code float} font sizes can cause overruns on
     * round-up calculations. "Choose your Adventure" overruns the button length, but
     * "Select your Adventure" does not, despite being the same number of characters!
     * 
     * @return the button created
     */
    private JButton createAdventureButton()
    {
        JButton button = createButtonWithTextAndIcon(ADV_IMAGE, LOAD_ADVENTURE_TITLE);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                List<String> adventures = _advReg.getAdventureList();
                Object[] adventuresArr = adventures.toArray();
                Object selectedValue =
                        JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
                                JOptionPane.INFORMATION_MESSAGE, null, adventuresArr,
                                adventuresArr[0]);
                if (selectedValue != null) {
                    loadSelectedAdventure((String) selectedValue);
                }
            }
        });
        return button;
    }

    // ============================================================
    // Public methods:
    // ============================================================

    private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
    {
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
     * Create the button to call the NewHeroCiv, which will control the NewHeroIOPanel that collects
     * the new Hero data, and calls HeroDisplayCiv that displays the Hero's stats panel
     *
     * @return the button
     */
    private JButton createNewHeroButton()
    {
        JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, CREATE_HERO_TITLE);
        button.addActionListener(action -> createHero());
        return button;
    }

    // ============================================================
    // Public methods:
    // ============================================================

    /* This button code is followed by a series of inner methods */
    private JButton createSummonHeroesButton()
    {
        JButton button = createButtonWithTextAndIcon(HALL_IMAGE, SUMMON_HERO_TITLE);

        button.addActionListener(new ActionListener() {

            // This happens when SummonHeroes is clicked
            public void actionPerformed(ActionEvent e)
            {
                // Show Hero chooser
                List<String> heroList = _dorm.getHeroNames();
                List<String> plateList = new ArrayList<String>(heroList.size());
                for (String name : heroList) {
                    plateList.add(_hdCiv.getNamePlate(name));
                }
                
                // Convert to "plain" array
                Object[] plateArray = plateList.toArray();
                // Capture selection
                String selectedPlate = null;
                selectedPlate =
                        (String) JOptionPane.showInputDialog(null, "Select your Hero", "Heroes",
                                JOptionPane.PLAIN_MESSAGE, null, plateArray, plateArray[0]);
                
                // Get matching name from hero list
                String selectedName = heroList.get(plateList.indexOf(selectedPlate));
//                System.out.println("Selected plate : " + selectedPlate);
//                System.out.println("Selected name : " + selectedName);
                
//                 Make sure HeroName has been assigned a non-zero value
                if ((selectedName != null) && (selectedName.length() > 0)) {
                    // Get hero info and display it
                    summonHero(selectedName);
                }

            }

            // Additional setup of hero ShuttleList
//            private void setUpShuttleList(final ShuttleList slist)
//            {
//                slist.setTitle("Choose your Adventurers!");
//                slist.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent arg0)
//                    {
//                        List<Hero> list = new ArrayList<>();
//                        for (Hero s : slist.getSelectedHeroes(_dorm)) {
//                            list.add(s);
//                        }
//                        _activeHero = list.get(0);
//                        slist.dispose();
//                    }
//                });
//                slist.setVisible(true);
//            }

        });
        return button;
    }

} // end of MainActionCiv class
