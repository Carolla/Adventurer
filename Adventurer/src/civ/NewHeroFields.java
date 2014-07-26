/**
 * NewHeroFields.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mylib.civ.DataShuttle;
import mylib.civ.DataShuttle.ErrorType;
import chronos.pdc.Occupation;
import chronos.pdc.Race;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Contains the widget keys to the display fields for the new Hero widget, and
 * is reponsible for defining default data values in the widget, and validating
 * the input data from the user. The enum values are 'fields' because they
 * contain only String data; enum values for the model are 'keys' because they
 * contain data of any type.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 23, 2010 // original
 *          <DD>
 *          <DT>Build 1.1 Sep 30 2010 // refactored for new revised MVP Stack
 *          modules
 *          <DD>
 *          </DL>
 */
public enum NewHeroFields {
    // Empty default name
    NAME("") {
        /** Max length of Person name */
        private final int MAX_NAMELEN = 30;

        /** Verify that the name is non-empty and within the length limit */
        public boolean isValid(Object obj)
        {
            String s = obj.toString().trim();
            return ((s.length() <= MAX_NAMELEN) && (s.length() > 0));
        }
    }, // end of Name key
    

    GENDER("Male") {
        private final String MALE = "Male";
        private final String FEMALE = "Female";

        /** Verify that the gender is either male or female */
        public boolean isValid(Object obj)
        {
            String s = obj.toString().trim();
            if ((s.equalsIgnoreCase(MALE)) || (s.equalsIgnoreCase(FEMALE))) {
                return true;
            } else {
                return false;
            }
        }
    }, // end of Gender key

    HAIR_COLOR("bald") {
        // Legal value must be one of the options in the options list
        @SuppressWarnings("unchecked")
        public boolean isValid(Object obj)
        {
            String s = obj.toString().trim();
            List<String> optionList = (List<String>) HAIR_COLOR_OPTIONS.getDefault();
            return (optionList.contains(s));
        }
    }, // end of Hair Color key

    KLASSNAME("Peasant") {
        /** New Heroes may only be Peasant klass */
        public boolean isValid(Object obj)
        {
            String s = KLASSNAME.getDefault().toString();
            return (obj.toString().equalsIgnoreCase(s));
        }
    }, // end of Klassname key

    OCCUPATION("None") {
        // Legal value must be one of the options in the options list
        @SuppressWarnings("unchecked")
        public boolean isValid(Object obj)
        {
            String s = obj.toString().trim();
            ArrayList<String> optionList = (ArrayList<String>) OCCUPATION_OPTIONS
                    .getDefault();
            return (optionList.contains(s));
        }
    }, // end of Occupation key

    RACENAME("Human") {
        // Legal value must be one of the options in the options list
        @SuppressWarnings("unchecked")
        public boolean isValid(Object obj)
        {
            String s = obj.toString().trim();
            ArrayList<String> optionList = (ArrayList<String>) RACE_OPTIONS
                    .getDefault();
            return (optionList.contains(s)) ? true : false;
        }
    }, // end of Racename key
    
    ABILITY_SCORES("") {
    	/** Max Number of Spent Points */
    	private final int MAX_POINTS = 24;
    	
    	/** Verify that all points have been spent */
    	public boolean isValid(Object obj)
    	{
    		return true;
    	}
    },

    HAIR_COLOR_OPTIONS(null) {
        /** List of hair colors available for selection; default is always first */
        private final String[] _hairColorList = { "bald", "black", "blonde",
                "brown", "gray", "red", "silver", "streaked", "white" };

        /** Returns all hair colors in a list */
        public List<String> getDefault()
        {
            return Arrays.asList(_hairColorList);
        }

        /** Hair color options are output data, so are always true */
        public boolean isValid(Object obj)
        {
            return true;
        }
    }, // end of Hair Color options list key

    OCCUPATION_OPTIONS(null) {
        /** Initialize the list for the options */
        private ArrayList<String> _list;

        /** Occupation options are output data, so are always true */
        public boolean isValid(Object obj)
        {
            return true;
        }

        /** Returns all Occupations in a list */
        public ArrayList<String> getDefault()
        {
            _list = new ArrayList<String>();
            for (Occupation o : ((OccupationRegistry) RegistryFactory.getInstance().getRegistry(RegKey.OCP))
                    .getOccupationList()) {
                _list.add(o.getName());
            }
            String defName = (String) OCCUPATION.getDefault();
            _list.remove(defName);
            _list.add(0, (String) OCCUPATION.getDefault());
            return _list;
        }
    }, // end of Occupation options list key

    RACE_OPTIONS(null) {
        /** Each key value contains its own copy of _list */
        private List<String> _list;

        /** Get the list of Races for the user to choose from */
        public List<String> getDefault()
        {
            _list = Race.getRaceTypes();
            String defName = (String) RACENAME.getDefault();
            _list.remove(defName);
            _list.add(0, defName);
            return _list;
        }

        /** Race options are output data, so are always true */
        public boolean isValid(Object obj)
        {
            return true;
        }
    }; // end of Race options list key

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR,
     * FIELDS, AND ABSTRACT METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Constructor to assign default value for key */
    private NewHeroFields(Object obj) {
        _value = obj;
    }

    /**
     * Verify if the default value is correct or not
     * 
     * @return true if value is valid
     */
    abstract public boolean isValid(Object obj);

    /**
     * Ask the keys to check their own validity
     * 
     * @param ws
     *            data shuttle containing the keys to check for validity
     * @return true if shuttle data is valid, else set shuttle's error flags
     */
    static public DataShuttle<NewHeroFields> isValid(
            DataShuttle<NewHeroFields> ws)
    {
        // Load a value into the shuttle for each slot requested; ignore others
        for (NewHeroFields key : ws.getKeys()) {
            Object obj = ws.getField(key);
            // Null is only valid for a few special cases
            if (obj == null) {
                if ((key == HAIR_COLOR_OPTIONS) || (key == RACE_OPTIONS)
                        || (key == OCCUPATION_OPTIONS)) {
                    break;
                } else {
                    ws.setErrorType(ErrorType.NULL_FIELD);
                    ws.setErrorMessage(key.name());
                    ws.setErrorSource(key);
                    break;
                }
            }
            if (key.isValid(obj) == false) {
                // Every key requested must have data to go with it, else error
                ws.setErrorType(ErrorType.FIELD_INVALID);
                ws.setErrorMessage(key.name());
                ws.setErrorSource(key);
                break;
            }
        }
        return ws;
    }

    /**
     * Retrieve default value from key
     * 
     * @return default value set in the key's constructor
     */
    public Object getDefault()
    {
        return _value;
    }

    /** Default value associated with this enum */
    private Object _value;

};

// end of NewHeroFields enum class

