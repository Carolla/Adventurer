/**
 * TestPeasant.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc;


import junit.framework.TestCase;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;

import pdc.Peasant;
import pdc.Peasant.MockPeasant;

/**
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Jun 21, 2009   // original <DD>
 * <DT> Build 1.1       Aug 29 2010     // add QA tags and support <DD>
 * <DT> Build 1.2       Apr 20 2011     // TAA checked tags, changed initCash to all doubles
 * </DL>
 */
public class TestPeasant extends TestCase
{

    // Frequently used objects under test
	Peasant _worker = null;
	MockPeasant _mock = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
        // Error messages are ON at start of each test 
        MsgCtrl.errorMsgsOn(true);
        // Audit messages are OFF at start of each test
        MsgCtrl.auditMsgsOn(false);      
		_worker = new Peasant();
		_mock = _worker.new MockPeasant();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		_worker = null;
		_mock = null;
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
	}

    
    //-------------------------------------------------------------------------
    //         BEGIN TESTING
    //-------------------------------------------------------------------------

    /** Test the Peasant constructor, which doesnothing but set the Klass name 
     * @Normal Peasant.Peasant()                                    used in setUp()
     * @Null      Peasant.Peasant()                                    N/A  
     * @Error    Peasant.Peasant()                                    N/A  
     */
    public void testCtor()
    {
        // Audit messages are OFF at start of each test
        MsgCtrl.auditMsgsOn(false);      
        MsgCtrl.msgln(this, "\ttestCtor()");      
        assertTrue(_worker.getKlassName().equals("Peasant"));
    }

    
    /** Test the calculation of HP for the Peasant. Simple getter but confirm for Peasant  
     * @Normal  Peasant.calcHP()                ok
     * @Null       Peasant.calcHP()           N/A
     * @Error     Peasant.calcHP()            min HP w/adj => 1
     */
    public void testCalcHP()
    {
        MsgCtrl.auditMsgsOn(false);      
        MsgCtrl.msgln(this, "\ttestCalcHP()");
        // HP = MIN_HP and HP Adj, which depends on CON
        int minHP = _mock.getMinHP();
        MsgCtrl.msgln("\tThe minimum HP for all Klasses = " + minHP);
        for (int k=-3; k < 3; k++) {
            _mock.calcHP(k);
            assertEquals(_worker.getHP(), minHP + k);
        }
        
        // ERROR 
        // Negative HP Adj cannot be greater than MIN_HP;  Lowest HP bump during
        // promotion is always 1 
        int negAdj1 = -minHP;
        _mock.calcHP(negAdj1);
        MsgCtrl.msgln("\tNegative Adj equal to MIN_HP: HP  = " + _worker.getHP());
        assertEquals(_worker.getHP(), 1);
        
        _mock.calcHP(negAdj1-2);
        MsgCtrl.msgln("\tNegative Adj greater than MIN_HP:  HP = " + _worker.getHP());
        assertEquals(_worker.getHP(), 1);
    }


    /** Test the calculation of HP for the Peasant. Simple getter but confirm for Peasant  
     * @Normal Peasant.initCash()         convert ints into combined double
     * @Null       Peasant.initCash()                   N/A
     * @Error    Peasant.initCash()                    N/A
     */
    public void testInitCash()
    {
        MsgCtrl.auditMsgsOn(false);      
        MsgCtrl.msgln(this, "\ttestInitCash()");

        // Cash = MIN_GOLD + MIN_SILVER / 10.0;
        double gold = _mock.getMinGold();                      // 15 gp to start
        double silver = _mock.getMinSilver();                   //   8 sp to start       
        MsgCtrl.msg("\tInitial cash for all Klasses: " + gold + " gp + " + silver + " sp" );
        MsgCtrl.msgln("\t= " + _mock.initCash() + " gp/sp");
        double cash = gold + (silver/10.0);
        assertEquals(_mock.initCash(), cash, 0.1);
    }

    
    //-------------------------------------------------------------------------
    //         QA SUPPORT
    //-------------------------------------------------------------------------

    /** Tests that are not needed for various reasons, mostly setters and getters
     * @Not_Needed Peasant.calcLevel()                     getter
     * @Not_Needed Peasant.rollInitialTraits()              getter
     */
    public void testNotNeeded() {}


    /** Tests that are not yet implemented  */
    public void testNotImplemented() { }  

	
}   		// end of TestPeasant class

