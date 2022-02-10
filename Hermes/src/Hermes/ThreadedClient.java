package Hermes;

import java.io.*;

import java.net.*;


/**
 * <p>Title: ThreadedClient.java </p>
 * <p>Description: Threaded class that will send messages to the other agents.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ThreadedClient extends Thread {
    Message message;
    Socket socket = null;

    /**
    * Constructor
    */
    public ThreadedClient() {
        start();
    }

    /**
    * Constructor - Will create a socket on the designated ip and port and send
    * that message over that socket, the socket will then be closed
    * @param m message
    * @param ip ip address
    * @param port port number
    */
    public ThreadedClient(Message m, String ip, int port) {
        message = m;

        try {
            socket = new Socket(ip, port);

            start();

            //wait until thread finishes
            while (this.isAlive()) {
            }

            //close the socket
            socket.close();
        } catch (Exception ex) {
            System.err.println("Error in sending message (1): " +
                ex.getMessage());
            System.err.println("Resending after 2 seconds.");

            try {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException iex) {
                        System.err.println("Error closing socket: " + iex);
                    }
                }

                this.sleep(2000);
                new ThreadedClient(message, ip, port, 2);
            } catch (InterruptedException iex) {
                System.err.println("Error putting thread to sleep: " + iex);
            }
        }
    }

    /**
    * Constructor - Will create a socket on the designated ip and port and send
    * that message over that socket, the socket will then be closed
    * @param m message
    * @param ip ip address
    * @param port port number
    * @param count  number of attempts to send message
    */
    public ThreadedClient(Message m, String ip, int port, int count) {
        message = m;

        try {
            socket = new Socket(ip, port);

            start();

            //wait until thread finishes
            while (this.isAlive()) {
            }

            //close the socket
            socket.close();
        } catch (Exception ex) {
            if (count < 4) {
                System.err.println("Error in sending message (" + count +
                    "): " + ex.getMessage());
                System.err.println("Resending after " + (count * 2) +
                    " seconds.");

                try {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException iex) {
                            System.err.println("Error closing socket: " + iex);
                        }
                    }

                    this.sleep(2000 * count);
                    new ThreadedClient(message, ip, port, ++count);
                } catch (InterruptedException iex) {
                    System.err.println("Error putting thread to sleep: " + iex);
                }
            } else {
                new ThreadedClient(message, ip, port); // only backup plan we have
            }
        }
    }

    /**
    * Constructor - sets the message to send and will broadcast this message to
    * all the clients who's sockets are passed. Will spawn a new thread to handle
    * each client. When the user passes the socket, they are responsible for
    * closing that socket.
    * @param m message to send
    * @param s sockets to send to
    */
    public ThreadedClient(Message m, Socket s) {
        message = m;
        socket = s;

        start();
    }

    /**
    * Will run the thread. This will send the message to the desired client
    */
    public void run() {
        PrintWriter out = null;

        try {
            //send the message
            out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);

            out.println(message.getMessageString());
        } catch (Exception e) {
            if (socket != null) {
                System.err.println("Error in sending message: " +
                    e.getMessage());
                System.err.println("Resending after 2 seconds.");

                try {
                    this.sleep(2000);

                    ThreadedClient tc = new ThreadedClient(message, socket);

                    //wait until thread finishes
                    while (tc.isAlive()) {
                    }

                    //close the socket
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        System.err.println("Error closing socket: " + ex);
                    }
                } catch (InterruptedException ex) {
                    System.err.println("Error putting thread to sleep: " + ex);
                }
            }
        }
    }
}
