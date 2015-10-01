/**
 * Adventurer.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;


/** Display messages to the IOPanel */

public interface UserMsg
{
    /**
     * Display an error message in a different font color than normal messages
     * 
     * @param msg the error message to display
     */
    public void errorOut(String msg);


    /**
     * Close down the application
     * 
     * @param msg text to display
     */
    public void msgOut(String msg);
}
