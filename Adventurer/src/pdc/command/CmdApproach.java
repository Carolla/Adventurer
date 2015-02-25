/**
 * CmdApproach.java
 * 
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package pdc.command;

import java.util.List;

/**
 * Moves the Hero .... EXIT has no effect if called from Town View;
 * it will ask if the user meant QUIT instead, to leave the program.
 * <P>
 * Format: EXIT <br>
 * 
 * @author Alan Cline
 * @version 1.0 Mar 19 2014 // original <br>
 * @see Command
 */
public class CmdApproach extends Command
{

    @Override
    public boolean init(List<String> args)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean exec()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
