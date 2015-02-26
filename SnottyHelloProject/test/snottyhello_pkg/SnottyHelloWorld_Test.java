
package snottyhello_pkg;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

public class SnottyHelloWorld_Test
{

    @Test
    public void testSnottyHelloWorld()
    {
        SnottyHelloWorld imasnot = new SnottyHelloWorld(myArgs);

        assertNotNull(imasnot);
        assertTrue(imasnot instanceof SnottyHelloWorld);
        assertEquals("Mr. Ubersnot sends this message: Fah! I am SOOOOO beyond this exercise!",
                imasnot.snottyMessage);
    }

}
