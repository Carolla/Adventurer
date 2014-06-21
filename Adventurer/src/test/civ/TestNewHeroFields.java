/**
 * TestNewHeroFields.java
 * Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.civ;


import civ.NewHeroFields;

import mylib.MsgCtrl;
import mylib.civ.DataShuttle;
import mylib.civ.DataShuttle.ErrorType;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Dec 10, 2011   // original <DD>
 * </DL>
 */
public class TestNewHeroFields extends TestCase
{
    /** DataShuttle for these keys */
    DataShuttle<NewHeroFields> _ws = null;

    /** Default data for the fields */
    private final String IP_NAME = "Xandra the Red";
    private final String IP_GENDER = "Female";
    private final String IP_HAIRCOLOR = "red";
    private final String IP_KLASSNAME = "Peasant";
    private final String IP_RACENAME = "Human";
    private final String IP_OCCUPATION = "Acrobat";

    /** Invalid constants for various keys */
    private final String EMPTY_STRING = " ";
    private final String LONG_NAME = "This is an overly long name to trigger an error";
    private final String WRONG_GENDER = "NotMale";
    private final String UNLISTED_COLOR = "maroon";
    private final String ILLEGAL_KLASSNAME = "Paladin";
    private final String GUILD_KLASSNAME = "Fighter";
    private final String UNLISTED_OCCUPATION = "carpet-cleaner";
    private final String ILLEGAL_RACENAME = "Ogre";
    
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(true);
        _ws = new DataShuttle<NewHeroFields>(NewHeroFields.class);
        populate(_ws);
        assertNotNull(_ws);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _ws = null;
        MsgCtrl.errorMsgsOn(false);
    }

   
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *          BEGIN TESTING
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
 
    /** Test method for civ.NewHeroCiv.isValid(EnumMap&ltPersonKeys, String&gt)
     *            
     * @Normal validation checks for each key            
     * @Error     empty shuttle, empty string checks; unlisted values from options lists;
     *                  superfluous keys that are option list keys
     * @Null       null value checks for each key  
     */
    public void testIsValid()
    {
         MsgCtrl.auditMsgsOn(false);
         MsgCtrl.errorMsgsOn(false);
         MsgCtrl.msgln(this, "\ttestIsValid(): ");

         // ERROR Call the isValid method on an empty shuttle
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         
         // NORMAL: Pack a valid data shuttle to test the method, and confirm no error handling
         // Clear errors
         _ws.clearErrors();
         _ws = packFields(_ws);
         _ws = NewHeroFields.isValid(_ws);
         assertFalse(DataShuttle.hasErrors(_ws));
         
         // ERROR Empty required fields
         // First remove the non-String keys
         _ws.removeKey(NewHeroFields.HAIR_COLOR_OPTIONS);
         _ws.removeKey(NewHeroFields.OCCUPATION_OPTIONS);
         _ws.removeKey(NewHeroFields.RACE_OPTIONS);
         
         // ERROR Name empty and null
         _ws.clearErrors();
         _ws.putField(NewHeroFields.NAME, EMPTY_STRING);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.NAME);
         
         _ws.clearErrors();
         _ws.putField(NewHeroFields.NAME, null);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.NULL_FIELD);
         assertEquals(_ws.getErrorSource(), NewHeroFields.NAME);
         
         // ERROR Overly long Name field
         _ws.clearErrors();
         _ws.putField(NewHeroFields.NAME, LONG_NAME);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.NAME);

         // Put  good value back in for Name
         _ws.clearErrors();
         _ws.putField(NewHeroFields.NAME, IP_NAME);

         // ERROR Gender empty and null
         _ws.clearErrors();
         _ws.putField(NewHeroFields.GENDER, EMPTY_STRING);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.GENDER);
         
         _ws.clearErrors();
         _ws.putField(NewHeroFields.GENDER, null);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.NULL_FIELD);
         assertEquals(_ws.getErrorSource(), NewHeroFields.GENDER);
         
         // ERROR Gender neither male nor female
         _ws.clearErrors();
         _ws.putField(NewHeroFields.GENDER, WRONG_GENDER);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.GENDER);

         // Put  good value back in for Gender
         _ws.clearErrors();
         _ws.putField(NewHeroFields.GENDER, IP_GENDER);

         // ERROR Hair color empty and null
         _ws.clearErrors();
         _ws.putField(NewHeroFields.HAIR_COLOR, EMPTY_STRING);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.HAIR_COLOR);
         
         _ws.clearErrors();
         _ws.putField(NewHeroFields.HAIR_COLOR, null);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.NULL_FIELD);
         assertEquals(_ws.getErrorSource(), NewHeroFields.HAIR_COLOR);
         
         // ERROR Hair color is not listed in the available options
         _ws.clearErrors();
         _ws.putField(NewHeroFields.HAIR_COLOR, UNLISTED_COLOR);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.HAIR_COLOR);

         // Put  good value back in for Hair Color
         _ws.clearErrors();
         _ws.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
         
         // ERROR Klassname empty and null
         _ws.clearErrors();
         _ws.putField(NewHeroFields.KLASSNAME, EMPTY_STRING);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.KLASSNAME);
         
         _ws.clearErrors();
         _ws.putField(NewHeroFields.KLASSNAME, null);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.NULL_FIELD);
         assertEquals(_ws.getErrorSource(), NewHeroFields.KLASSNAME);
         
         // ERROR Klass Name is not listed in the available options
         _ws.clearErrors();
         _ws.putField(NewHeroFields.KLASSNAME, ILLEGAL_KLASSNAME);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.KLASSNAME);

         // ERROR Klass Name is not a Peasant, so is not yet implemented
         _ws.clearErrors();
         _ws.putField(NewHeroFields.KLASSNAME, GUILD_KLASSNAME);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.KLASSNAME);

         // Put  good value back in for Klass Name
         _ws.clearErrors();
         _ws.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);

         // ERROR Occupation empty and null
         _ws.clearErrors();
         _ws.putField(NewHeroFields.OCCUPATION, EMPTY_STRING);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.OCCUPATION);
         
         _ws.clearErrors();
         _ws.putField(NewHeroFields.OCCUPATION, null);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.NULL_FIELD);
         assertEquals(_ws.getErrorSource(), NewHeroFields.OCCUPATION);
         
         // ERROR OCCUPATION is not listed in the available options
         _ws.clearErrors();
         _ws.putField(NewHeroFields.OCCUPATION, UNLISTED_OCCUPATION);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.OCCUPATION);

         // Put  good value back in for Occupation
         _ws.clearErrors();
         _ws.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);

         // ERROR Race Name empty and null
         _ws.clearErrors();
         _ws.putField(NewHeroFields.RACENAME, EMPTY_STRING);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.RACENAME);
         
         _ws.clearErrors();
         _ws.putField(NewHeroFields.RACENAME, null);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.NULL_FIELD);
         assertEquals(_ws.getErrorSource(), NewHeroFields.RACENAME);
         
         // ERROR Race Name is not listed in the available options
         _ws.clearErrors();
         _ws.putField(NewHeroFields.RACENAME, ILLEGAL_RACENAME);
         _ws = NewHeroFields.isValid(_ws);
         assertTrue(DataShuttle.hasErrors(_ws));
         assertEquals(_ws.getErrorType(), ErrorType.FIELD_INVALID);
         assertEquals(_ws.getErrorSource(), NewHeroFields.RACENAME);

         // Put  good value back in for Klass Name
         _ws.clearErrors();
         _ws.putField(NewHeroFields.RACENAME, IP_RACENAME);

         // ERROR Hair Color Options is superfluous key; shuttle should still be ok
         _ws.clearErrors();
         _ws.putField(NewHeroFields.HAIR_COLOR_OPTIONS, null);
         _ws = NewHeroFields.isValid(_ws);
         assertFalse(DataShuttle.hasErrors(_ws));
     }

    
    /** 
     * List of methods not necessary to test <br>
     *  <code>getDefault()</code>  -- getter for constructor value <br>
     */
    public void notNeeded() { }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *          Private helper methods
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /** Fill the keys with default data values
     * @param ds empty shuttle to load
     * @return shuttle loaded with all data values
     */
    private DataShuttle<NewHeroFields> packFields(DataShuttle<NewHeroFields> ds)
    {
        _ws.putField(NewHeroFields.NAME, IP_NAME);
        _ws.putField(NewHeroFields.GENDER, IP_GENDER);
        _ws.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
        _ws.putField(NewHeroFields.RACENAME, IP_RACENAME);
        _ws.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
        _ws.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
        return ds;
    }

    
    /** Fill the shuttle with all the keys of the enum class
     * @param ts empty shuttle to load
     * @return ts empty shuttle to load
     */
    private DataShuttle<NewHeroFields> populate(DataShuttle<NewHeroFields> ts)
    {
        // Build data shuttle with all the desired keys
        for (NewHeroFields key : NewHeroFields.values()) {
            ts.assignKey(key);
        }
        return ts;
    }


}
    		// end of TestNewHeroFields class
