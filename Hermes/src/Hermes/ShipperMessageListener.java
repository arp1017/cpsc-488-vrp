package Hermes;

import java.io.*;

import java.net.*;


/**
 * <p>Title: ShipperMessageListener.java </p>
 * <p>Description: Thread that will listen for connections on the shipper's port, and spawn
 * other threads to handle the messages.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ShipperMessageListener extends Thread {
    /**
 * socket that will listen for connections and spawn new sockets once new
 * connections are made
 */
    private ServerSocket server;
    private ShipperListAdaptor shipListAdaptor;
    private ShipperAgentInterface agentInterface;

    /**
 * Constructor, will make a server socket and run the thread to listen on it.
 * @param portNo port to listen to.
 * @param sai point to the agent interface for the shiper
 * @param sla point to the adaptor for the shipment linked list
 */
    public ShipperMessageListener(int portNo, ShipperAgentInterface sai,
        ShipperListAdaptor sla) {
        try {
            agentInterface = sai;
            shipListAdaptor = sla;
            server = new ServerSocket(portNo);
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
 * The message listener thread's run method. Will listen for connections and
 * spawn new threads once the connections are made.
 */
    public void run() {
        try {
            while (true) {
                Socket incoming;

                //once a new connection is made assign the socket to incoming
                incoming = server.accept();

                new ShipperMessageHandler(incoming, agentInterface,
                    shipListAdaptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
