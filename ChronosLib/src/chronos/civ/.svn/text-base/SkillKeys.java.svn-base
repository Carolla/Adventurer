/**
 * SkillKeys.java
 * Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.civ;

import mylib.MsgCtrl;

//TODO: Move this to DungeonWizard. It is not part of Adventurer

/** Contains the WidgetKeys for a DataShuttle MVP Stack.
 * These two key sets, and the DataShuttle class, must be in the Civ package because both the HIC 
 * and PDC packages must access them. 
 * Each key has its own validation routine so there is one and only one place for validation.
 *
 * @author Timothy Armstrong
 * @version <DL>
 * <DT> Build 1.0       Sept 5, 2011   // original <DD>
 * </DL>
 */
public enum SkillKeys 
{    
	NAME ("")
    {
	    /** Length limits of Skill name */
	    private final int MAX_NAME_LEN = 35;

	    public boolean isValid (Object obj)
		{
			String str = (String) obj;
			str.trim();
			if (str == null || str == "")
			{
				return false;
			}
		    else
		    {
		        for (int i = 0; i < str.length(); i++)
		        {
		            Character check = str.charAt(i);
		            if (check == '\t' || check == '\n' || check == '\b')
		            {
		                MsgCtrl.errMsgln("Invalid whitespace in skill name");
		                return false;
		            }
		        }
		    }
			return true;
		}
	
	/** Maximum length of Name field */
	public final int maxLength() { return MAX_NAME_LEN; }

	},
	
	DESC ("")
	{
	    /** Length limits of Skill description */
	    private final int MAX_DESC_LEN = 70;
	    
		public boolean isValid(Object obj)
	    {
			String str = (String) obj;
			str.trim();
			if (str == null || str == "")
			{
				return false;
			}
			return true;
	    }
		
		/** Maximum length of Description field */
		public final int maxLength() { return MAX_DESC_LEN; }
	},
	
	ACTION ("Not yet implemented")
	{
		public boolean isValid(Object obj) { return true; }
		
		/** Maximum length of Action field */
		public final int maxLength() { return 35; }

	},
	
	RACE ("All")
	{
		public boolean isValid(Object obj) { return true; }
		
		/** Maximum length of Race field */
		public final int maxLength() { return 20; }
	},
	
	KLASS ("All")
	{
		public boolean isValid(Object obj) { return true; }
		
		/** Maximum length of Description field */
		public final int maxLength() { return 35; }
	},
	
	RACELIST (null)  
	{
		public boolean isValid(Object obj) { return true; }
		
		/** Maximum length of Racelist field */
		public final int maxLength() { return 0; }
	},
	
	KLASSLIST (null)
	{
		public boolean isValid(Object obj) { return true; }
		
		/** Maximum length of Klasslist field */
		public final int maxLength() { return 0; }
	},
	
	SKILLLIST (null)
	{
		public boolean isValid(Object obj) { return true; }
		
		/** Maximum length of Skilllist field */
		public final int maxLength() { return 0; }
	};     
	
	private String _val;
	/** Skill key constructor */
	private SkillKeys(String str) {_val = str;};
	
	/** Get default value method */
	public String getDefault()
	{
		return _val;
	}
	
	abstract public boolean isValid (Object obj);
	abstract public int maxLength();
	
	

} // end of SkillKeys class

