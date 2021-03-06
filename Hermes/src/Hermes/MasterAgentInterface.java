package Hermes;

import java.awt.*;

import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;

import java.sql.*;

/**
 * <p>Title: MasterAgentInterface.java </p>
 * <p>Description: Interface for communication with all other agents. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
* <p>Log: Entirety of the Master Interface was redone using GridBagLayout. Provides a much cleaner,
* user-friendly design that functions near identically to the old JUnit format. For the sake of 
* simplicity and sheer number of changes, all change notes for this file will be listed here.
* 	++Each "frame" is created with JFrame, and likewise for JButton and JLabel / JTextField.
* 	++Anchor points defined below are in relation to each button / field / asset on the frame.
* 	++Function calls remain largely the same as with the old JUnit implementation.
* </p>
*/

public class MasterAgentInterface extends JFrame {
    public boolean hasGUI = false;
    public LogWriter logWriter = new LogWriter("m" +
            HermesGlobals.masterServerPortNo + ".log");
    public boolean isDoneSched = false;
    public Vector messageListeners = new Vector();
    public int numCarriers = 0;
    public int updateCount = 0;
    public boolean updating = false;
    public boolean isLock = false;
    public boolean isChanged = false;
    MasterMessageListener messageListener;
    RegisterShipperCarrierDatabaseInterface shipcarDB;
    Message[] carrierInfoArray = null;
    private JButton jbMetaGlobal = new JButton();
    private JButton jbOptimize = new JButton();
    private JButton jbTerminate = new JButton();
    private JButton jbMasterSched = new JButton();
    private JLabel jlTotalDemand = new JLabel();
    private JLabel jlTotalDistance = new JLabel();
    private JLabel jlTotalTravel = new JLabel();
    private JLabel jlTotalTard = new JLabel();
    private JLabel jlTotalExcess = new JLabel();
    private JLabel jlTotalOverload = new JLabel();
    private JLabel jlTotalWait = new JLabel();
    private JTextField jtfTotalDemand = new JTextField();
    private JTextField jtfTotalDistance = new JTextField();
    private JTextField jtfTravelTime = new JTextField();
    private JTextField jtfTotalTardiness = new JTextField();
    private JTextField jtfTotalExcess = new JTextField();
    private JTextField jtfTotalWait = new JTextField();
    private JTextField jtfTotalOver = new JTextField();
    private JLabel jlTotalServ = new JLabel();
    private JTextField jtfTotalServ = new JTextField();
    private JLabel jlTotalCustomers = new JLabel();
    private JTextField jtfTotalCust = new JTextField();
    private JTextField jtfTotalTrucks = new JTextField();
    private JLabel jlTotalTrucks = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**
    * Constructor, starts the message listener
    */
    public MasterAgentInterface() {
        shipcarDB = new RegisterShipperCarrierDatabaseInterface();
        messageListener = new MasterMessageListener(HermesGlobals.masterServerPortNo,
                shipcarDB, this);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Constructor, starts the auctioning system without a GUI
    * @param setTrue  place holder, does nothing but create a unique signature
    */
    public MasterAgentInterface(boolean setTrue) {
        shipcarDB = new RegisterShipperCarrierDatabaseInterface();
        messageListener = new MasterMessageListener(HermesGlobals.masterServerPortNo,
                shipcarDB, this);
        if (isDoneSched) {
        	printSolution();
        }
    }

    /**
    * Get the total demand of the problem
    * @return float  total demand
    */
    public float getDemand() {
        return Float.parseFloat(jtfTotalDemand.getText());
    }

    /**
    * Get the total time of the problem
    * @return float  total time
    */
    public float getTotalTime() {
        return Float.parseFloat(jtfTravelTime.getText());
    }

    /**
    * Get the total distance of the problem
    * @return float  total distance
    */
    public float getDistance() {
        return Float.parseFloat(jtfTotalDistance.getText());
    }

    /**
    * Get the total serv of the problem
    * @return float  total serv
    */
    public float getServ() {
        return Float.parseFloat(jtfTotalServ.getText());
    }

    /**
    * Get the total wait of the problem
    * @return float  total wait
    */
    public float getWait() {
        return Float.parseFloat(jtfTotalWait.getText());
    }

    /**
    * Get the total excess of the problem
    * @return float  total excess
    */
    public float getExcess() {
        return Float.parseFloat(jtfTotalExcess.getText());
    }

    /**
    * Get the total overload of the problem
    * @return float  total overload
    */
    public float getOver() {
        return Float.parseFloat(jtfTotalOver.getText());
    }

    /**
    * Get the total tardiness of the problem
    * @return float  total tardiness
    */
    public float getTardiness() {
        return Float.parseFloat(jtfTotalTardiness.getText());
    }

    /**
    * Initializes the Master Server frame
    * @throws Exception
    */
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(gridBagLayout1);
        this.setTitle("Master Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jbMetaGlobal.setEnabled(false);
        jbMetaGlobal.setToolTipText("Communicate between Carriers to optimize");
        jbMetaGlobal.setText("Greater Optimize");
        jbMetaGlobal.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbMetaGlobal_actionPerformed(e);
                }
            });
        jbOptimize.setEnabled(false);
        jbOptimize.setToolTipText(
            "Have each Carrier perform optimizations on their own schedules");
        jbOptimize.setText("Lesser Optimize");
        jbOptimize.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbOptimize_actionPerformed(e);
                }
            });
        jbTerminate.setToolTipText("Close all servers");
        jbTerminate.setText("Terminate");
        jbTerminate.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbTerminate_actionPerformed(e);
                }
            });
        jbMasterSched.setEnabled(false);
        jbMasterSched.setToolTipText(
            "Prints the full schedule from each Carrier");
        jbMasterSched.setText("Master Schedule");
        jbMasterSched.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbMasterSched_actionPerformed(e);
                }
            });
        jlTotalDemand.setText("Total Demand");
        jlTotalDistance.setText("Total Distance");
        jlTotalTravel.setText("Total Travel Time");
        jlTotalTard.setText("Total Tardiness");
        jlTotalExcess.setText("Total Excess Time");
        jlTotalOverload.setText("Total Overload");
        jlTotalWait.setText("Total Wait Time");
        jlTotalServ.setText("Total Service Time");
        jtfTotalCust.setEditable(false);
        jtfTravelTime.setEditable(false);
        jtfTotalTardiness.setEditable(false);
        jtfTotalExcess.setEditable(false);
        jtfTotalWait.setEditable(false);
        jtfTotalOver.setEditable(false);
        jtfTotalDistance.setEditable(false);
        jtfTotalDemand.setEditable(false);
        jtfTotalServ.setEditable(false);
        jtfTravelTime.setText("0.0");
        jtfTotalTardiness.setText("0.0");
        jtfTotalExcess.setText("0.0");
        jtfTotalWait.setText("0.0");
        jtfTotalOver.setText("0.0");
        jtfTotalDistance.setText("0.0");
        jtfTotalDemand.setText("0.0");
        jtfTotalServ.setText("0.0");
        jlTotalCustomers.setText("Total Customers");
        jtfTotalCust.setText("0");
        jtfTotalTrucks.setText("0");
        jtfTotalTrucks.setEditable(false);
        jlTotalTrucks.setText("Total Trucks");
        this.getContentPane().add(jlTotalCustomers,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(22, 25, 0, 20), 3, 0));
        this.getContentPane().add(jtfTotalCust,
            new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(22, 0, 0, 0), 35, 0));
        this.getContentPane().add(jlTotalTrucks,
            new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(22, 11, 0, 0), 8, 0));
        this.getContentPane().add(jtfTotalTrucks,
            new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(22, 9, 0, 25), 33, 0));
        this.getContentPane().add(jtfTotalWait,
            new GridBagConstraints(1, 5, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(7, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTotalServ,
            new GridBagConstraints(1, 4, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(7, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTotalTardiness,
            new GridBagConstraints(1, 6, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(8, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTotalOver,
            new GridBagConstraints(1, 7, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(7, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTotalDemand,
            new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTravelTime,
            new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(7, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTotalExcess,
            new GridBagConstraints(1, 8, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(7, 0, 0, 25), 164, 0));
        this.getContentPane().add(jtfTotalDistance,
            new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 0, 25), 164, 0));
        this.getContentPane().add(jlTotalDistance,
            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(12, 25, 0, 20), 16, 0));
        this.getContentPane().add(jlTotalDemand,
            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(15, 25, 0, 20), 17, 0));
        this.getContentPane().add(jlTotalTravel,
            new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 25, 0, 20), 1, 0));
        this.getContentPane().add(jlTotalOverload,
            new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 25, 0, 20), 16, 0));
        this.getContentPane().add(jlTotalTard,
            new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(11, 25, 0, 20), 9, 0));
        this.getContentPane().add(jlTotalServ,
            new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 25, 0, 0), 14, 0));
        this.getContentPane().add(jlTotalExcess,
            new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 25, 0, 0), 14, 0));
        this.getContentPane().add(jlTotalWait,
            new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 25, 0, 20), 10, 0));
        this.getContentPane().add(jbTerminate,
            new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(9, 25, 0, 33), 37, 0));
        this.getContentPane().add(jbMetaGlobal,
            new GridBagConstraints(2, 9, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(8, 0, 0, 36), 1, 0));
        this.getContentPane().add(jbMasterSched,
            new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 25, 16, 33), 1, 0));
        this.getContentPane().add(jbOptimize,
            new GridBagConstraints(2, 10, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 16, 36), 3, 0));

        hasGUI = true;
        this.setSize(new Dimension(368, 366));
        this.setVisible(true);
    }

    /**
    * Perform inter-carrier optimizations
    * @param e  user event
    */
    void jbMetaGlobal_actionPerformed(ActionEvent e) {
        jbOptimize.setEnabled(false);
        jbMasterSched.setEnabled(false);
        jbMetaGlobal.setEnabled(false);

        Message gOptMsg = new Message();
        gOptMsg.setMessageType(MessageTags.GlobalOptTag);

        Message responseMsg = null;
        Socket socket = null;

        try {
            socket = new Socket(HermesGlobals.masterServerIP,
                    HermesGlobals.masterServerPortNo);

            gOptMsg.setMessageType(MessageTags.GlobalOptTag);
            logWriter.writeLog("Optimizing all Carriers.");
            new ThreadedClient(gOptMsg, socket);

            responseMsg = getResponse(socket);
        } catch (IOException ex) {
            System.err.println("Error in optimize message sending: " + ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("Error closing socket in lesser opts: " +
                    ex);
            }
        }

        //wait till thread is out before reactivating buttons
        while (!responseMsg.getMessageType().equals(MessageTags.AckTag))
            ;

        enableMasterJButtons();
    }

    /**
    * performs optimizations on all the registered carriers
    * @param e  user event
    */
    void jbOptimize_actionPerformed(ActionEvent e) {
        jbMetaGlobal.setEnabled(false);
        jbMasterSched.setEnabled(false);

        //prepare & send a local opt message to itself.
        //Inside message handler, msg is then forwarded to all registered carrier
        Message LocalOptMsg = new Message();
        Message responseMsg = null;
        Socket socket = null;

        try {
            socket = new Socket(HermesGlobals.masterServerIP,
                    HermesGlobals.masterServerPortNo);

            LocalOptMsg.setMessageType(MessageTags.LocalOptTag);
            logWriter.writeLog("Optimizing all Carriers.");
            new ThreadedClient(LocalOptMsg, socket);

            responseMsg = getResponse(socket);
        } catch (IOException ex) {
            System.err.println("Error in optimize message sending: " + ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("Error closing socket in lesser opts: " +
                    ex);
            }
        }

        //wait till thread is out before reactivating buttons
        while (!responseMsg.getMessageType().equals(MessageTags.AckTag))
            ;

        enableMasterJButtons();
    }

    /**
     * Receive a response from a sent message over a socket.
     * @param s  socket the message was sent over.
     * @return Message  response to sent message
     */
    public synchronized Message getResponse(Socket s) {
        BufferedReader in = null;
        Message msg = null;

        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            msg = new Message(in.readLine());
        } catch (IOException ex) {
            System.err.println("Error in receiving response: " + ex);
        } catch (Exception e) {
            System.err.println("Error reading message from socket: " + e);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                System.err.println("Error closing input stream in response: " +
                    ex);
            }
        }

        return msg;
    }

    /**
    * Terminate all servers
    * @param e user event
    */
    void jbTerminate_actionPerformed(ActionEvent e) {
        Message msg = new Message();

        msg.setMessageType(MessageTags.TerminateTag);
        broadcastToShippers(msg); // broadcast message to all servers
        broadcastToCarriers(msg);
        logWriter.writeLog("Terminating all agents.");
        System.out.println("Terminating application.");
        System.exit(0);
    }

    /**
    * Print the master schedule to standard out,
    * short solution and detailed solution files.
    * @param e  user event
    */
    void jbMasterSched_actionPerformed(ActionEvent e) {
        // retreive info from carriers, then print it
        printSolution();
    }

    /**
    * this destroys the interface object.
    */
    public void myExit() {
        messageListener.myExit(); //call my exit for listener
        System.exit(2); //exit agent interface
    }

    /**
    * after master receives enable buttons message
    */
    public void enableMasterJButtons() {
        if (isDoneSched) {
            jbOptimize.setEnabled(true);
            jbMasterSched.setEnabled(true);
            jbMetaGlobal.setEnabled(true);
        }
    }

    /**
    * Will take the message and send it to all the carriers
    * @param msg message to broadcast
    */
    private void broadcastToCarriers(Message msg) {
        //query the database for the carrier's addresses
        if (isChanged) {
            carrierInfoArray = shipcarDB.getCarrierIPandPort();
            numCarriers = carrierInfoArray.length;
            isChanged = false;
        }

        logWriter.writeLog("Broadcasting to carriers from interface: " +
            msg.toString());

        //loop through the carriers
        for (int i = 0; i < numCarriers; i++) {
            //parse the message for the ip and port
            String ip = carrierInfoArray[i].getValue(MessageTags.IPAddressTag);
            int port = Integer.parseInt(carrierInfoArray[i].getValue(
                        MessageTags.PortNumberTag));

            try {
                //use a new ThreadedClient to send the message
                new ThreadedClient(msg, ip, port);

                logWriter.writeLog("Broadcasting to carrier: " + port);

                //print to screen
                System.out.println("Broadcasting to carriers");
            } catch (Exception e) {
                logWriter.writeLog("Error broadcasting to carrier " + port +
                    " from interface " + e);
                e.printStackTrace();
            }
        }
    }

    /**
    * Will take the message and send it to all the carriers waiting for each
    * carrier to finish processing the message before sending it to the next
    * @param msg message to broadcast
    */
    private void broadcastToCarriersWithBlocking(Message msg) {
        //query the database for the carrier's addresses
        if (isChanged) {
            carrierInfoArray = shipcarDB.getCarrierIPandPort();
            numCarriers = carrierInfoArray.length;
            isChanged = false;
        }

        Socket socket = null;
        Message response = null;
        logWriter.writeLog(
            "Broadcasting to carriers with blocking from interface: " +
            msg.toString());

        //loop through the carriers
        for (int i = 0; i < numCarriers; i++) {
            //parse the message for the ip and port
            String ip = carrierInfoArray[i].getValue(MessageTags.IPAddressTag);
            int port = Integer.parseInt(carrierInfoArray[i].getValue(
                        MessageTags.PortNumberTag));

            try {
                //use a new ThreadedClient to send the message
                socket = new Socket(ip, port);
                new ThreadedClient(msg, socket);
                logWriter.writeLog("Broadcasting to carrier with blocking: " +
                    port);
                response = getResponse(socket);

                while (!response.getMessageType().equals(MessageTags.AckTag))
                    ;
            } catch (Exception e) {
                logWriter.writeLog(
                    "Error broadcasting with blocking to carrier " + port +
                    " from interface " + e);
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing socket in lesser opts: " +
                        ex);
                }
            }
        }
    }

    /**
    * Will take the message and send it to all the shippers
    * @param msg message to broadcast
    */
    private void broadcastToShippers(Message msg) {
        //query the database for the shipper's addresses
        Message[] shipperAddrs = shipcarDB.getShipperIPandPort();

        logWriter.writeLog("Broadcasting to shippers from interface: " +
            msg.toString());

        //loop through the shipppers
        for (int i = 0; i < shipperAddrs.length; i++) {
            //parse the message for the ip and port
            String ip = shipperAddrs[i].getValue(MessageTags.IPAddressTag);
            int port = Integer.parseInt(shipperAddrs[i].getValue(
                        MessageTags.PortNumberTag));

            try {
                //use a new ThreadedClient to send the message
                logWriter.writeLog("Broadcasting to shipper: " + port);
                new ThreadedClient(msg, ip, port);
            } catch (Exception e) {
                logWriter.writeLog("Error broadcasting shipper " + port +
                    " from interface " + e);
                e.printStackTrace();
            }
        }
    }

    /**
    * Print the solution to standard out, Detailed solution file, and short
    * solution files.
    */
    public void printSolution() {
        Socket socket = null;
        BufferedReader in = null;
        String shipInfo = null;
        String fileName = null;
        String filePrefix = null;
        String outputFileName = null;
        float maxCapacity = 0;
        float maxDistance = 0;
        float totalWaitTime = 0;
        float totalTravelDist = 0;
        float totalService = 0;
        float totalDist = 0;
        float totalTardiness = 0;
        float totalExcess = 0;
        float totalOverload = 0;

        String shipperIP = null;
        int shipperPort = 0;
        Message msg = new Message();
        Message[] response;

        logWriter.writeLog("Printing solution files.");
        msg.setMessageType(MessageTags.FileNameTag);

        response = shipcarDB.getShipperIPandPort();
        shipperIP = response[0].getValue(MessageTags.IPAddressTag);
        shipperPort = Integer.parseInt(response[0].getValue(
                    MessageTags.PortNumberTag));

        try {
            socket = new Socket(shipperIP, shipperPort);
            in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
            new ThreadedClient(msg, socket);
            shipInfo = in.readLine();
        } catch (Exception ex) {
            logWriter.writeLog("Unable to retrieve fileName: " + ex);
            System.err.print("Unable to retrieve fileName: " + ex);
        } finally {
            try {
                in.close();
                socket.close();
            } catch (IOException ex) {
                logWriter.writeLog("Error closing socket for retrieving file: " +
                    ex);
                System.err.print("Error closing socket for retrieving file: " +
                    ex);
            }
        }

        try {
            msg.setMessage(shipInfo);
        } catch (Exception ex) {
            logWriter.writeLog("Error setting shipment info to message: " + ex);
            System.err.println("Error setting shipment info to message: " + ex);
        }

        fileName = msg.getValue(MessageTags.FileNameTag);
        maxCapacity = Float.parseFloat(msg.getValue(MessageTags.CapacityTag));
        maxDistance = Float.parseFloat(msg.getValue(MessageTags.DistanceTag));

        logWriter.writeLog("File Name: " + fileName + "\n Max Capacity: " +
            maxCapacity + "\n Max Distance: " + maxDistance);

        //Use the fileprefix of the filename to write out the output
        if (hasGUI == true) {
        	filePrefix = fileName.substring(fileName.lastIndexOf("/") + 1);
        	filePrefix = filePrefix.substring(0, filePrefix.lastIndexOf("."))
                    .toLowerCase();
        	}
        	
        else if (hasGUI == false) {
        		filePrefix = "problems/";
        		filePrefix = filePrefix + fileName.substring(fileName.lastIndexOf("/") + 1);
        		filePrefix = filePrefix.substring(0, filePrefix.lastIndexOf("."))
                        .toLowerCase();
        		
        	}

        System.out.println("\n" + "Summary for problem " + filePrefix);
        System.out.println("Over all Travel Time ============= " +
            jtfTravelTime.getText());
        System.out.println("Maximum Capacity for each truck == " + maxCapacity);
        System.out.println("Over all Demand for trucks ======= " +
            jtfTotalDemand.getText());
        System.out.println("Maximum Distance for each truck == " + maxDistance);
        System.out.println("Over all Distance covered ======== " +
            jtfTotalDistance.getText());
        System.out.println("Over all Service Time ============ " +
            jtfTotalServ.getText());
        System.out.println("Over all Wait Time =============== " +
            jtfTotalWait.getText());
        System.out.println("Over all Tardiness Time ========== " +
            jtfTotalTardiness.getText());
        System.out.println("Over all Excess Time ============= " +
            jtfTotalExcess.getText());
        System.out.println("Over all Overload ================ " +
            jtfTotalOver.getText());

        //Write the solution out to the solution file
        writeDetailSol(filePrefix, maxCapacity, maxDistance);

        //Write out the short solution to the solution file
        writeShortSol(filePrefix, maxCapacity, maxDistance);

        System.out.println("Finished printing solutions.");
    }

    /**
    * Writes the detailed solution to a file.
    * @param filePrefix  file name of detailed solution
    * @param maxCap  maximum capacity of a truck
    * @param maxDist  maximum distance of a truck
    */
    private void writeDetailSol(String filePrefix, float maxCap, float maxDist) {
        String outputFileName = null;
        PrintWriter solOutFile = null;
        Message msg = new Message();

        /*
        * this returns a array of information where each entry pertains to one
        * agent, the number of any agent is the length of the array
        */
        int numOfShippers = shipcarDB.getShipperIPandPort().length;
        int numOfCarrs = shipcarDB.getCarrierIPandPort().length;

        try {
            outputFileName = filePrefix + "_detailSolution.txt";
            solOutFile = new PrintWriter(new FileWriter(outputFileName));

            //print out the problem information to the file
            solOutFile.println("VRPTW File       : " + filePrefix + ".txt");
            solOutFile.println("No. of Shippers  : " + numOfShippers);
            solOutFile.println("No. of Shipments : " + jtfTotalCust.getText());
            solOutFile.println("No. of Carriers  : " + numOfCarrs);
            solOutFile.println("Maximum Capacity : " + maxCap);
            solOutFile.println("Maximum Distance : " + maxDist);

            /***************Sunil 10/1/03**********************************/
            solOutFile.println("********************************************");
            solOutFile.println("Over all Travel Time ========= " +
                jtfTravelTime.getText());
            solOutFile.println("Over all Distance covered === " +
                jtfTotalDistance.getText());
            solOutFile.println("Over all Service Time ======== " +
                jtfTotalServ.getText());
            solOutFile.println("Over all Wait Time =========== " +
                jtfTotalWait.getText());
            solOutFile.println("Over all Tardiness Time ====== " +
                jtfTotalTardiness.getText());
            solOutFile.println("Over all  Excess Time ========= " +
                jtfTotalExcess.getText());
            solOutFile.println("Over all  Overload =========== " +
                jtfTotalOver.getText());
            solOutFile.println("********************************************");
        } catch (IOException ioe) {
            logWriter.writeLog("IO error writing detailed solution: " + ioe);
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                logWriter.writeLog("Detailed solution file not open.");
                System.out.println("Solution file not open.");
            }
        }

        //end finally
        //write out the route information
        msg.setMessageType(MessageTags.DetailSolutionTag);
        msg.addArgument(MessageTags.FileNameTag, outputFileName);
        broadcastToCarriersWithBlocking(msg);
    }

    /**
    * Writes the short solution to a file
    * @param filePrefix  file name of short solution
    * @param maxCap  maximum capacity of a truck
    * @param maxDist  maximum distance of a truck
    */
    private void writeShortSol(String filePrefix, float maxCap, float maxDist) {
        String outputFileName = null;
        PrintWriter solOutFile = null;
        Message msg = new Message();

        /*
        * this returns a array of information where each entry pertains to one
        * agent, the number of any agent is the length of the array
        */
        int numOfShippers = shipcarDB.getShipperIPandPort().length;
        int numOfCarrs = shipcarDB.getCarrierIPandPort().length;

        try {
            outputFileName = filePrefix + "_shortSolution.txt";
            solOutFile = new PrintWriter(new FileWriter(outputFileName));

            solOutFile.println(filePrefix + ".txt" + " " + numOfShippers + " " +
                jtfTotalCust.getText() + " " + numOfCarrs + " " + maxCap + " " +
                maxDist + " " + jtfTotalDemand.getText() + " " +
                jtfTotalWait.getText() + " " + jtfTravelTime.getText() + " " +
                jtfTotalServ.getText() + " " + jtfTotalDistance.getText() +
                " " + jtfTotalTardiness.getText() + " " +
                jtfTotalExcess.getText() + " " + jtfTotalOver.getText());
        } catch (IOException ioe) {
            logWriter.writeLog("IO error writing short solution: " + ioe);
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                logWriter.writeLog("Detail solution file not open.");
                System.out.println("Detail solution file not open.");
            }
        }

        //end finally
        //write out the route information
        msg.setMessageType(MessageTags.ShortSolutionTag);
        msg.addArgument(MessageTags.FileNameTag, outputFileName);
        broadcastToCarriersWithBlocking(msg);
    }

    /**
    * Will update the text fields
    * @param dem demand
    * @param dis distance
    * @param tt travel time
    * @param tar tardyness
    * @param exc excess time
    * @param ovl overload
    * @param wait  wait time
    * @param serv  service time
    * @param cust  number of customers
    * @param trucks  number of trucks
    */
    public synchronized void setTextFields(float dem, float dis, float tt,
        float tar, float exc, float ovl, float wait, float serv, int cust,
        int trucks) {
        dem += Float.parseFloat(jtfTotalDemand.getText());
        dis += Float.parseFloat(jtfTotalDistance.getText());
        tt += Float.parseFloat(jtfTravelTime.getText());
        tar += Float.parseFloat(jtfTotalTardiness.getText());
        exc += Float.parseFloat(jtfTotalExcess.getText());
        ovl += Float.parseFloat(jtfTotalOver.getText());
        wait += Float.parseFloat(jtfTotalWait.getText());
        serv += Float.parseFloat(jtfTotalServ.getText());
        cust += Integer.parseInt(jtfTotalCust.getText());
        trucks += Integer.parseInt(jtfTotalTrucks.getText());

        jtfTotalDemand.setText("" + dem);
        jtfTotalDistance.setText("" + dis);
        jtfTravelTime.setText("" + tt);
        jtfTotalTardiness.setText("" + tar);
        jtfTotalExcess.setText("" + exc);
        jtfTotalOver.setText("" + ovl);
        jtfTotalWait.setText("" + wait);
        jtfTotalServ.setText("" + serv);
        jtfTotalCust.setText("" + cust);
        jtfTotalTrucks.setText("" + trucks);
    }

    /**
    * This will clear the text fields, setting them all to zero.
    */
    public void clearTextFields() {
        jtfTotalDemand.setText("" + 0);
        jtfTotalDistance.setText("" + 0);
        jtfTravelTime.setText("" + 0);
        jtfTotalTardiness.setText("" + 0);
        jtfTotalExcess.setText("" + 0);
        jtfTotalOver.setText("" + 0);
        jtfTotalWait.setText("" + 0);
        jtfTotalServ.setText("" + 0);
        jtfTotalCust.setText("" + 0);
        jtfTotalTrucks.setText("" + 0);
    }

    /**
    * Run the lesser optimizations without interacting with the GUI
    */
    public void runLesserOpts() {
        //prepare & send a local opt message to itself.
        //Inside message handler, msg is then forwarded to all registered carrier
        Socket socket = null;
        Message response = null;
        Message LocalOptMsg = new Message();
        LocalOptMsg.setMessageType(MessageTags.LocalOptTag);
        logWriter.writeLog("Optimizing all Carriers.");

        try {
            socket = new Socket(HermesGlobals.masterServerIP,
                    HermesGlobals.masterServerPortNo);
            new ThreadedClient(LocalOptMsg, socket);
        } catch (IOException ex) {
            System.err.println("Error sending message in lesser opts: " + ex);
        }
        response = getResponse(socket);

        while (!response.getMessageType().equals(MessageTags.AckTag))
            ;
        
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("Error closing socket in lesser opts: " +
                ex);
        }
    }
}
