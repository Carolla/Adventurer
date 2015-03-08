
package snottyhello_pkg;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

public class SnottyHelloWorld_Test
{

    @Test
    public void testSnottyHelloWorld()
    {
        String[] someTestArgs =
                {"I've got a boogey on my finger!", "I can't get the boogey off my finger!",
                        "Just lick it off!"};
        SnottyHelloWorld imasnot = new SnottyHelloWorld(someTestArgs);

        // SnottyHelloWorld object has been properly created
        assertNotNull(imasnot);
        assertTrue(imasnot instanceof SnottyHelloWorld);
        
        // Same number of members in each array
        assertEquals(someTestArgs.length, imasnot.getArgs().length);
        
        // Array members contain the same data
        assertTrue(someTestArgs[0].equals(imasnot.getArgs()[0]));
        assertTrue(someTestArgs[1].equals(imasnot.getArgs()[1]));
        assertTrue(someTestArgs[2].equals(imasnot.getArgs()[2]));
    }

}
