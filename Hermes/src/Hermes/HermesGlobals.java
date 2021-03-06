package Hermes;

import java.net.*;
import java.net.InetAddress;


/**
 * <p>Title: HermesGlobals.java </p>
 * <p>Description: Holds global variables that are used by the entire system.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class HermesGlobals {
    //the IP and port number for the master server (all agents need this)
    public static String masterServerIP; //= "10.1.43.229";
    //public static int masterServerPortNo = 49152;
    public static int masterServerPortNo = 3006;

    // for generating random port numbers
    public static int randomPortBounds = 16383; // (possible 65535 ports) - (offset)
    //public static int randomPortOffset = 49152; // offset of dynamic ports
    public static int randomPortOffset = 3006;
    
    // maximum iterations to perform cyclic inter-carrier optimizations
    public static int maxLoop = 2;

    /**
    * length of time in minutes that a logfile will be saved in the directory
    * before being marked for deletion the next time the agent is run
    */
    public static int logLife = 1;

    /**
     * approximate number of optimizations that will be performed on
     * the carrier routes
     */
    public static int auctionBreak = 100;

    {
        try {
            masterServerIP = InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost()
                                                                                        .toString()
                                                                                        .indexOf("/") +
                    1);
        } catch (UnknownHostException ex) {
            System.err.print("Error setting Master Server IP: " + ex);
        }
    }
}
