package Hermes;

import Zeus.*;

import java.io.*;

import java.net.*;


/**
 * <p>Title: CarrierMessageListener.java </p>
 * <p>Description: this threads listen for conncetions and spawns a new
 *    thread(a socket) when a new connection comes in.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class CarrierMessageListener extends Thread {
    /**
    * socket that will listen for connections and spawn new sockets once new
    * connections are made
    */
    private ServerSocket server;
    private ZeusAdaptor z;
    private CarrierAgentInterface cai;

    /**
    * Constructor, will make a server socket and run the thread to listen on it.
    * @param portNo port to listen to.
    * @param zeus  instance of zeus scheduling system for this carrier
    * @param cai pointer to the carrier agent interface
    */
    public CarrierMessageListener(int portNo, ZeusAdaptor zeus,
        CarrierAgentInterface cai) {
        try {
            z = zeus;
            this.cai = cai;
            server = new ServerSocket(portNo);
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
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

                while (cai.isLock) {
                    System.out.println("Waiting on carrier");
                    Thread.sleep(1500);
                }

                new CarrierMessageHandler(incoming, z, cai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
