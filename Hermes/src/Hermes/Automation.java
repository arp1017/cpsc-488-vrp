package Hermes;

import java.net.*;


/**
 * <p>Title: Automation.java </p>
 * <p>Description: Runs the auctioning system without user dependent events.
 *                 The user will set up which problem is to be solved with
 *                 the number of carriers and their respective depots.  Then the
 *                 entire auctioning process is run and completed without using
 *                 GUI's or waiting for the user to click a button. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
* <p>Log: Automation did not have a properly working setup. Changes to the code of each agent had to be made to
* 		accommodate for the lack of user input. Specifically, the shipper and carrier agents needed to be entered
* 		and exited multiple times in order to ensure that shipments were being properly sent out and bidded on.
* 		Automation currently only functions with one shipper agent and one carrier agent.</p>
*/
public class Automation {
    /** 
 * Default constructor - not used
 */
	String thisIP = null;
    int test = 0;
	ShipperAgent box;
	CarrierAgent rect;

    public Automation() {
    }

    /**
 * Constructor
 * @param dataFile  problem file name to be solved by the system
 * @param numCarr  number of carriers to be used
 * @param depotFile  depot information file name to be used by the carriers
 * @todo  make this compatible with mulitple depot files
 */
    public Automation(String dataFile, int numCarr, String depotFile) {
        //String thisIP = null;
        //int test = 0;

        try { // capture the IP address of this machine
            thisIP = InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost()
                                                                                .toString()
                                                                                .indexOf("/") +
                    1);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            System.exit(8008135);
        }

  
        
        System.out.println("Starting Master Server");
        //new MasterCredentials(); // start master server without a GUI


     // start the shipping agent with the problem file
        System.out.println("Starting shipper");
        box = new ShipperAgent(thisIP, dataFile);
        
     // create the specified number of carriers
        for (int i = 0; i < numCarr; i++) {
            System.out.println("Starting carrier");
            rect = new CarrierAgent(depotFile, thisIP, true);
        }
        /*
         * These calls ensure that the shipper and carrier agents are carrying out tasks in the proper order.
         */
        doSomething(ShipperAgent.getAsai());
        doAThing(CarrierAgent.getAcar());
        helpMe(ShipperAgent.getAsai());

        
    }
    public void doSomething(ShipperAgentInterface attempt) {
    	attempt.setHasGUI(true);
    }
    
    public void helpMe(ShipperAgentInterface attempt) {
    	attempt.getHasGUI();
    }

    public void doAThing(CarrierAgentInterface attempt) {
    	attempt.setHasGUI(true);
    }

    /**
 * Driver method for the Automation class - retrieves the problem file name,
 * number of carriers, and depot information file from the command line
 * @param args  command line arguments <problem file> <number of carriers>
 *                                       <depot information file>
 */
    public static void main(String[] args) {
        if ((args.length > 0) && (args.length < 4)) {
            String dataFile = args[0];
            int numCarriers = Integer.parseInt(args[1]);
            String depotFile = args[2];
            new Automation(dataFile, numCarriers, depotFile);
        } else if (args.length == 0) {
            //run interface
        }
    }
}
