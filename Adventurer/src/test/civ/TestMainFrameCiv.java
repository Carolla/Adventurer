/**
 * TestAdvMainFrameCiv.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.civ;

import org.junit.Before;

import civ.MainframeCiv;
import mylib.MsgCtrl;

/**
 * Test the various methods in AdvMainframeCiv
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Dec 21, 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestMainFrameCiv
{
    private static final String QUASQUETON_JPG = "ext_Quasqueton.JPG";
    private static final String SILLY_ADVENTURE = "Silly Adventure";
    static private MainframeCiv _mainciv = null;
//    private MockMainframe _mf;
//    private MockPersonRW _prw;
//    private MockAdventureRegistry _areg;
//    private MockBuildingRegistry _breg;

    /*
     * ++++++++++++++++++++++++ FIXTURES ++++++++++++++++++++++++++++++++
     */

    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        MsgCtrl.errorMsgsOn(true);
//        AdvObjectMother mother = new AdvObjectMother();
//        _mf = mother.getMockMainframe();
//        _prw = mother.getMockPersonRW();
//        _areg = mother.getMockAdventureRegistry();
//        _breg = mother.getMockBuildingRegistry();
//        AdvRegistryFactory.addRegistry(RegKey.BLDG, _breg);
//        _mainciv = new MainframeCiv(_mf, _prw, _areg);
    }

//    /**
//     * @throws java.lang.Exception
//     */
//    @After
//    public void tearDown() throws Exception
//    {
//        MsgCtrl.auditMsgsOn(false);
//    }
//
//    private void loadFakeAdventure()
//    {
//        _mainciv.loadAdventure(SILLY_ADVENTURE);
//    }
//
//    @Test
//    public void itShouldGetStoredAdventures()
//    {
//        List<String> adventures = _mainciv.getAdventures();
//        assertTrue("Should return at least 1 adventure", adventures.size() > 0);
//    }
//
//    @Test
//    public void itShouldGetStoredHeroes()
//    {
//        List<String> heroes = _mainciv.openDormitory();
//        assertTrue("Should return at least 1 hero", heroes.size() > 0);
//    }
//
//    @Test
//    public void itShouldLoadAdventure()
//    {
//        loadFakeAdventure();
//        assertAdventureLoaded(SILLY_ADVENTURE);
//        assertTownIsDisplayed();
//    }
//
//    @Test
//    public void itShouldOpenTown()
//    {
//        loadFakeAdventure();
//        _mainciv.enterBuilding("The Jail");
//        _mainciv.openTown();
//        assertTownIsDisplayed();
//    }
//
//    @Test
//    public void itShouldEnterBuildingWhenOnTown()
//    {
//        loadFakeAdventure();
//        _mainciv.enterBuilding("The Jail");
//        assertBuildingIsDisplayed();
//    }
//
//    @Test
//    public void itShouldEnterBuildingWhenOnBuilding()
//    {
//        loadFakeAdventure();
//        _mainciv.enterBuilding("The Jail");
//        _mainciv.enterBuilding("The Jail");
//        assertInnerBuildingIsDisplayed();
//    }
//
//    private void assertInnerBuildingIsDisplayed()
//    {
//        assertTrue(_mf._displayed);
//        assertTrue("Should display internal image, instead displayed " + _mf._lastImage,
//                _mf._lastImage.contains("int"));
//    }
//
//    private void assertAdventureLoaded(String advName)
//    {
//        assertTrue(_areg.loadedAdventure().equals(advName));
//    }
//
//    private void assertTownIsDisplayed()
//    {
//        assertTrue(_mf._displayed);
//        assertTrue("Image displayed was " + _mf._lastImage,
//                _mf._lastImage.equals(QUASQUETON_JPG));
//    }
//
//    @Test
//    public void itShouldHandleClicksIfNotOnBuilding()
//    {
//        loadFakeAdventure();
//        _mainciv.handleClick(new Point(0, 0));
//        assertBuildingNotClicked();
//    }
//
//
//    private void assertBuildingNotClicked()
//    {
//        assertTrue(_mf._displayed);
//        assertTrue("Image displayed was " + _mf._lastImage,
//                _mf._lastImage.equals(QUASQUETON_JPG));
//    }
//
//    @Test
//    public void itShouldHandleClicksIfOnBuilding()
//    {
//        loadFakeAdventure();
//        _mainciv.handleClick(makePointThatIsOverABuilding());
//        assertBuildingIsClicked();
//    }
//
//    private void assertBuildingIsClicked()
//    {
//        assertTrue(_mf._displayed);
//        assertFalse("Image displayed was " + _mf._lastImage,
//                _mf._lastImage.equals(QUASQUETON_JPG));
//    }
//
//    @Test
//    public void itShouldHandleClicksIfOnReturnButton()
//    {
//        loadFakeAdventure();
//        _mainciv.enterBuilding("The Jail");
//        _mainciv.handleClick(makePointThatIsOverAReturnButton());
//        assertTownIsDisplayed();
//    }
//
//    private Point makePointThatIsOverAReturnButton()
//    {
//        return new Point(1, 1);
//    }
//
//    private Point makePointThatIsOverABuilding()
//    {
//        return new Point(420, 750);
//    }
//
//    @Test
//    public void itShouldHandleMouseMovementWhenNotOverBuilding()
//    {
//        loadFakeAdventure();
//        _mainciv.handleMouseMovement(new Point(0, 0));
//        assertNoBuildingDisplayed();
//    }
//
//    private void assertNoBuildingDisplayed()
//    {
//
//    }
//
//    @Test
//    public void itShouldHandleMouseMovementWhenOverBuilding()
//    {
//        loadFakeAdventure();
//        _mainciv.handleMouseMovement(makePointThatIsOverABuilding());
//        assertBuildingIsDisplayed();
//    }
//
//    private void assertBuildingIsDisplayed()
//    {
//        assertTrue(_mf._displayed);
//        assertTrue("Should display external image, instead displayed " + _mf._lastImage,
//                _mf._lastImage.contains("ext"));
//    }
//
//    
}       // end of TestAdvMainFrameCiv class
