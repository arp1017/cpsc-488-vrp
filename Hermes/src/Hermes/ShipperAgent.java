package Hermes;

import java.net.*;
import java.net.InetAddress;


/**
 * <p>Title: ShipperAgent.java </p>
 * <p>Description: Driver Program for the Hermes Shipper Agent.  Starts the interface.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ShipperAgent {
    public static boolean isVerbose = false; // for debugging later
    private ShipperAgentInterface sai;
    private static ShipperAgentInterface asai;

    /**
 * Costructor. Will begin the Shipper Agent
 * @param IP shipper IP address
 */
    public ShipperAgent(String IP) {
        isVerbose = true;
        sai = new ShipperAgentInterface(IP);
    }

    /**
 * Constructor. Will start the Shipper without a GUI
 * @param IP ip address of the carrier
 * @param inputFile data file to process
 */
    public ShipperAgent(String IP, String inputFile) {
        asai = new ShipperAgentInterface(IP, inputFile);
    }
    
    public static ShipperAgentInterface getAsai() {
		return asai;
	}

    /**
 * Driver method for the shipper agent.
 * @param args  command line arguments - none used
 */
    public static void main(String[] args) {
        /**
 * This section of code will:
 * 1) create an instance of a shipper agent
 * 2) find the IP address of the machine the shipper is created on
 */
        try {
            new ShipperAgent(InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost()
                                                                                        .toString()
                                                                                        .indexOf("/") +
                    1));
        } catch (UnknownHostException ex) {
            System.err.println("Unable to find IP address for Shipper: " + ex);
        }
    }
}
