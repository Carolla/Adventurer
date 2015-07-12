
package civ;



public interface ChronosLogger
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
