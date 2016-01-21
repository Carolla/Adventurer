/**
 * TestHeroDisplayCiv.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.civ;

import junit.framework.TestCase;
import mylib.MsgCtrl;
import mylib.civ.DataShuttle;

import org.junit.Before;

import chronos.civ.PersonKeys;
import chronos.pdc.character.Hero;
import civ.HeroDisplayCiv;

/**
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jul 31, 2010 // original
 *          <DD>
 *          </DL>
 */
public class TestHeroDisplayCiv extends TestCase
{

    // Person to use for Testing
    private final String NAME = "Clyde";
    private final String GENDER = "Male";
    private final String OCCUP = "Gambler";
    private final String HAIRCOLOR = "brown";
    private final String RACENAME = "Human";
    
    private Hero _p = null;
    private HeroDisplayCiv _civ = null;
    DataShuttle<PersonKeys> _ds = null;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        _p = new Hero(NAME, GENDER, OCCUP, HAIRCOLOR, RACENAME);
        _civ = new HeroDisplayCiv(null);
    }


    /**
     * List of methods that do not need JUnit test because they are too trivial,
     * or some other test method tests them equally well. <br>
     * <code>heroDisplayCiv()</code>: -- Unused default contructor<br>
     * <code>convertToModel(DataShuttle&ltNewHeroFields&gt)</code>: -- unused
     * override <br>
     * <code>getDefaults()</code>: -- unused override <br>
     * <code>getFieldData()</code>: -- <br>
     * <code>getInventorySize()</code>: -- wrapper <br>
     * <code>isValid(DataShuttle&ltNewHeroFields&gt)</code>: -- unused override <br>
     * <code>populateAttributes(DataShuttle&ltPersonKeys&gt ds)</code>-- calls
     * GUI <code>populateInventory(ArrayList&ltItem&gt)</code> -- calls GUI
     * <code>populateSkills(ArrayList&ltSkill&gt)</code> -- calls GUI
     */
    public void notNeeded()
    {}

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * PRIVATE HELPER METHODS!
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

} // end of TestHeroDisplayCiv class
