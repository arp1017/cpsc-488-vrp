package HermesTest;

import java.net.ServerSocket;


/**
 * <p>Title: PortOccupier.java </p>
 * <p>Description: This will occupy a port on the computer to create a bind exception. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class PortOccupier {
    private int port = 50000;
    private String ip = "10.1.74.74";

    /**
 * Constructor - creates a server socket to occupy a port
 */
    public PortOccupier() {
        try {
            ServerSocket server = new ServerSocket(port);

            while (true) {
                server.accept();
            }
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
 * Driver for the PortOccupier application.
 * @param args command line arguments - none used
 */
    public static void main(String[] args) {
        new PortOccupier();
    }
}
