package Hermes;

import java.net.InetAddress;


/**
 * <p>Title: CarrierAgent.java </p>
 * <p>Description: Main Carrier Agent class.  This class recieves the depot
 *                 file from command line and starts the interface</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* All revisions made will be color coded green. 
*/
public class CarrierAgent {
    private CarrierAgentInterface CarInterface;
    private static CarrierAgentInterface acar;

    /**
* Constructor sstarts a Shipper Agent and puts an instance of zeus into it
* @param depotFile  name of depot information file
* @param IP ip address of string
*/
    public CarrierAgent(String depotFile, String IP) {
        System.out.println("Depot File name " + depotFile + " started @ IP " +
            IP);
        CarInterface = new CarrierAgentInterface(depotFile, IP);
    }

    /**
 * This will run the carrier with out the graphical user interfaces.
 * @param depotFile file with the depot information
 * @param IP ip address of carrier
 * @param setTrue flag to mark no gui
 */
    public CarrierAgent(String depotFile, String IP, boolean setTrue) {
        System.out.println("Depot File name " + depotFile + " started @ IP " +
            IP);
        acar = new CarrierAgentInterface(depotFile, IP, true);
    }
    
    public static CarrierAgentInterface getAcar() {
		return acar;
	}

    /**
 * Main class for carrier agent
 * @param args  command line arguments, first argument must be depot file name
 */
    public static void main(String[] args) {
        try {
            //if the user has not used the proper amount of cmd line params throw
            //an exception
            if (args.length != 1) {
                throw new Exception();
            }

            //depot info is entered through the commandline by the user but the IP
            //is automatically generated
            new CarrierAgent(args[0],
                InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost()
                                                                           .toString()
                                                                           .indexOf("/") +
                    1));
        } catch (Exception nfe) {
            //user made a mistake with the command line params. Output a helpful msg
            System.err.println("args.length= " + args.length);
            System.err.println("Error -Usage- java CarrierAgent <Depot File>");
        }
    }
}
