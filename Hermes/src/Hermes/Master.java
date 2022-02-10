package Hermes;

import java.util.Vector;


/**
 * <p>Title: Master.java </p>
 * <p>Description: The main class for the master server. This will start the server.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
* <p>Log: No noteworthy changes have been made to this file, as it remains perfectly functional
* with the new implementation.</p>
*/
public class Master {
    private MasterAgentInterface agentInterface;

    /**
 * Constructor, starts the master agent interface
 */
    public Master() {
        agentInterface = new MasterAgentInterface();
    }

    /**
 * Constructor, will start without GUI's
 * @param setTrue placeholder.. just pass true or false, doesnt matter
 */
    public Master(boolean setTrue) {
        agentInterface = new MasterAgentInterface(true);
    }

    /**
 * Driver class for master server
 * @param args command line arguments - none used
 */
    /*public static void main(String[] args) {
        new Master();
    }*/
}
