
package snottyhello_pkg;

import java.util.ArrayList;

public class SnottyHelloWorld
{
    // variables
    private final String SNOTTY_PERSON = "Mr. Uber Snot";
    private final String SNOTTY_MESSAGE =
            "would like to pass along the following command-line arguments";
    private final String NO_ARGS_MSG =
            "You waste my time with no arguments!?\nFah! I bid you adieu!";
    
    private String[] _myArgs;

    // private final String snottyMessage = "Fah! I am SOOOOO beyond this exercise!";
    public SnottyHelloWorld(String[] theArgs)
    {
        _myArgs = theArgs;
        System.out.println(SNOTTY_PERSON + " " + SNOTTY_MESSAGE);

        if (_myArgs.length > 0) {
            for (String arg : _myArgs) {
                System.out.println(arg);
            }
        } else {
            System.out.println(NO_ARGS_MSG);
        }

    }
    
    public String[] getArgs() {
        String[] sendArgs = new String[_myArgs.length];
        for (int i = 0; i < _myArgs.length; i++) {
            sendArgs[i] = _myArgs[i];
        }
        return sendArgs;
    }

}
