
package snottyhello_pkg;

public class SnottyHelloWorld
{
    // variables
    private final String snottyPerson = "Mr. Uber Snot";

    // private final String snottyMessage = "Fah! I am SOOOOO beyond this exercise!";
    public SnottyHelloWorld(String[] theArgs)
    {
        System.out.println("I am " + this.snottyPerson
                + " and I approve the following command-line arguments: ");

        if (theArgs.length > 0) {
            for (String arg : theArgs) {
                System.out.println(arg);
            }
        } else {
            System.out.println("No arguments provided.");
            System.out.println("Thus, I bid you adieu!");
        }

    }

}
