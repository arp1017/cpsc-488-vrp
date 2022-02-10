package Hermes;

import Zeus.Shipment;

import Zeus.ShipmentLinkedList;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.StringTokenizer;

import javax.swing.*;

import java.sql.*;

/**
 * <p>Title: ShipperAgentInterface.java </p>
 * <p>Description: Handles the user actions with the GUI, includes registering
 *    the agent with the Master Server, reading problem files, and sending
 *    shipments for bidding.</p>
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
public class ShipperAgentInterface extends JFrame implements MessageTags {
    /**
    * Server socket that will listen for incoming communication to the shipper.
    */
    ShipperMessageListener messageListener;
    private String shipperCode;
    private String shipperName;
    private String shipperIP;
    private String fileName; // name of problem file
    private int shipperPort;
    public boolean hasGUI = false;
    public LogWriter logWriter = null;
    public int myMasterPort;
    public int confirmCount; // number of shipments confirmed as scheduled
    private ShipmentLinkedList mainShipments;
    private ShipperListAdaptor shipListAdaptor;
    private Container shipContainer = new Container();
    private JMenuBar jMenuBar = new JMenuBar();
    private JMenu jmFile = new JMenu();
    private JMenuItem jmiOpen = new JMenuItem();
    private JMenuItem jmiExit = new JMenuItem();
    private JLabel jlShipperCode = new JLabel();
    private JTextField jtfShipperCode = new JTextField();
    private JLabel jlShipperName = new JLabel();
    private JTextField jtfShipperName = new JTextField();
    private JButton jbSendShips = new JButton();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JButton jbRegister = new JButton();
    private JTextField jtfMasterPort = new JTextField();
    private JLabel jlMasterIP = new JLabel();
    private JTextField jtfMasterIP = new JTextField();
    private JLabel jlMasterPort = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**
    * Constructor. Will create a new message listener on the specified port no
    * @param IP  the IP address of the agent
    */
    public ShipperAgentInterface(String IP) {
        mainShipments = new ShipmentLinkedList();
        shipListAdaptor = new ShipperListAdaptor(mainShipments);
        shipperIP = IP;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Constructor. Will create the agent interface without gui's. Will
    * automatically register with the master server, then read in data file
    * and finally send the shipments to the master server to be routed.
    * @param IP ip address of this shipper
    * @param dataFile file to read in
    */
    public ShipperAgentInterface(String IP, String dataFile) {
    	fileName = dataFile;
        mainShipments = new ShipmentLinkedList();
        shipListAdaptor = new ShipperListAdaptor(mainShipments);
        shipperIP = IP;
        shipperPort = registerShipperWithMasterServer("1", "shipper", shipperIP);
        try {
            Thread.sleep(3000);
            return;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

       
    }
    
    public boolean getHasGUI() {
    	sendCustomersToMasterServer("1");
    	return hasGUI;
    }
    
    public void setHasGUI(boolean hasGUI) {
 		this.hasGUI = hasGUI;
 		messageListener = new ShipperMessageListener(shipperPort, this,
                shipListAdaptor);
        logWriter = new LogWriter("s" + shipperPort + ".txt");
        readFileData(fileName);
 	}

    /**
    * get name of problem file
    * @return String  name of problem file
    */
    public String getFileName() {
        return fileName;
    }

    /**
    * get maximum capacity of each truck for problem file
    * @return float  maximum distance of each truck for problem file
    */
    public float getMaxCap() {
        return mainShipments.getMaxCap();
    }

    /**
    * get maximum distance of each truck for problem file
    * @return flaot  maximum distance of each truck for problem file
    */
    public float getMaxDist() {
        return mainShipments.getMaxDist();
    }

    /**
    * Opens a socket and sends a message to the Master Server for registration.
    * Once the server is registered, the Master Server will send the registered
    * port number back to the server.
    * @param code  Identifying code for the server
    * @param name  Name of the shipper
    * @param IP  IP address of the server
    * @return int  port number the server is registered under
    */
    private int registerShipperWithMasterServer(String code, String name,
        String IP) {
        int portNo = 0;
        Socket socket = null;
        BufferedReader in = null;
        ThreadedClient tc;
        Message addressMsg = new Message();
        Message registerMsg = new Message();
        Message portMsg;

        //create a new register message to tell master server that this shipper
        //is now active
        registerMsg.setMessageType(RegisterTag);
        registerMsg.addArgument(AgentTypeTag, ShipperMessage);
        registerMsg.addArgument(CodeTag, code);
        registerMsg.addArgument(NameTag, name);

        //create a message containing the address of this shipper for use by the
        //master server
        addressMsg.setMessageType(InetAddressTag);
        addressMsg.addArgument(IPAddressTag, IP);
        registerMsg.addArgument(addressMsg);

        //register the shipper in the master database
        //the message is to be sent to the master server
        //create a client to send the message to the master server
        try {
            socket = new Socket(HermesGlobals.masterServerIP,
                    HermesGlobals.masterServerPortNo);
            in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
            new ThreadedClient(registerMsg, socket);
            portMsg = new Message(in.readLine());
            portNo = Integer.parseInt(portMsg.getValue(PortNumberTag));
            myMasterPort = Integer.parseInt(portMsg.getValue(MasterPortTag));
        } catch (Exception ex) {
            System.err.print("Shipper unable to register: " + ex);
        } finally {
            try {
                socket.close();
                in.close();
            } catch (IOException ex) {
                System.err.print(
                    "Error closing socket for Shipper registration: " + ex);
            }
        }

        jbRegister.setEnabled(false);

        //print output about registering
        System.out.println("Registered Shipper with Master Server, got port " +
            portNo);

        return portNo;
    }

    /**
    * Will send the customers to the master server. This has been updated to
    * work with the new broadcast tag.
    * @param shipperCode the unique identifier for this shipper
    */
    public void sendCustomersToMasterServer(String shipperCode) {
        Socket socket = null;
        BufferedReader in = null;
        int noOfCustomers = mainShipments.getNoShipments();
        int broadcastCount = 0;
        Message sLine;
        Message msg;
        Shipment ship;

        //make a new message for the InetAddress of this shipper
        Message addr = new Message();
        addr.setMessageType(MessageTags.InetAddressTag);
        addr.addArgument(MessageTags.IPAddressTag, this.shipperIP);
        addr.addArgument(MessageTags.PortNumberTag, "" + this.shipperPort);

        logWriter.writeLog("Sending shipments from " + this.shipperIP + ":" +
            this.shipperPort);

        try {
            //create new start and end messages
            Message startPointsMessage = new Message();
            Message endPointsMessage = new Message();

            //set the start and end points messages
            startPointsMessage.setMessageType(StartSendPointsTag);
            endPointsMessage.setMessageType(EndSendPointsTag);

            //send the start points message
            new ThreadedClient(startPointsMessage,
                HermesGlobals.masterServerIP, myMasterPort);

            //loop through the customers, sending each to the master server
            ship = mainShipments.getFirst();

            for (int i = 0; i < noOfCustomers; i++) {
                msg = new Message();

                sLine = new Message();

                sLine.setMessageType(CalculateTag);
                sLine.addArgument(IndexTag, "" + ship.getShipNo());
                sLine.addArgument(XCoordTag, "" + ship.getX());
                sLine.addArgument(YCoordTag, "" + ship.getY());
                sLine.addArgument(DemandTag, "" + ship.getDemand());
                sLine.addArgument(EarlyTimeTag, "" + ship.getEarliestTime());
                sLine.addArgument(LateTimeTag, "" + ship.getLatestTime());
                sLine.addArgument(ServiceTimeTag, "" + ship.getServeTime());

                //add the address of this shipper
                sLine.addArgument(addr);

                //make a message to broadcast to the carriers
                msg.setMessageType(BroadcastTag);
                msg.addArgument(DestinationTag, CarrierMessage);

                //set the type of tag the embedded message is
                msg.addArgument(MessageTags.PackagedMessageTag,
                    MessageTags.CalculateTag);

                //set the shipments' attributes to the message
                msg.addArgument(sLine);

                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    logWriter.writeLog(
                        "Error putting thread to sleep in sending shipments: " +
                        ex);
                    ex.printStackTrace();
                }

                //send the message to the master server
                new ThreadedClient(msg, HermesGlobals.masterServerIP,
                    myMasterPort);
                logWriter.writeLog("Sending shipment " + (i + 1) +
                    " to master server");
                System.out.println("Sending shipment " + (i + 1) +
                    " to master server");

                broadcastCount++;

                if ((broadcastCount % (noOfCustomers / HermesGlobals.auctionBreak)) == 0) {
                    // wait till the shipments have been scheduled
                    while (confirmCount != broadcastCount) {
                        Thread.sleep(1500);
                    }

                    msg.setMessageType(AuctionBreakTag);

                    //the message is to be sent to the master server
                    //create a client to send the message to the master server
                    try {
                        Message endBreakMsg;
                        socket = new Socket(HermesGlobals.masterServerIP,
                                HermesGlobals.masterServerPortNo);
                        in = new BufferedReader(new InputStreamReader(
                                    socket.getInputStream()));
                        new ThreadedClient(msg, socket);
                        endBreakMsg = new Message(in.readLine());

                        while (!endBreakMsg.getMessageType().equals(AckTag)) {
                            endBreakMsg = new Message(in.readLine());
                        }
                    } catch (IOException ex) {
                        System.err.print(
                            "Shipper unable to send auction break message: " +
                            ex);
                    } finally {
                        try {
                            socket.close();
                            in.close();
                        } catch (IOException ex) {
                            System.err.print(
                                "Error closing socket for auction break: " +
                                ex);
                        }
                    }
                }

                ship = ship.next;
            }

            //create a timeout to wait so that the shipment threads can process
            //first
            Thread.sleep(1000);

            //send the end points message
            new ThreadedClient(endPointsMessage, HermesGlobals.masterServerIP,
                myMasterPort);
        } catch (Exception e1) {
            logWriter.writeLog("Error sending shipments: " + e1);
            e1.printStackTrace();
        }
    }

    /**
    * <p>Read in the data from the datafile and load it into the mainShipments
    * ShipmentLinkedList using the tokenizer.</p>
    * @param fileName String type of the filename consisting of the data
    * @return boolean Return true if all successful, false for failure
    */
    private boolean readFileData(String fileName) {
        //open the requested file
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        float x = 0;
        float y = 0;
        int demand = 0;
        int id = 0;
        int eT = 0;
        int lT = 0;
        int sT = 0;

        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        } catch (Exception e) {
            System.err.println("File is not present");

            return false;
        }

        String line;
        StringTokenizer st;

        int custCount = 0;

        try {
            //skip the first line
            br.readLine();

            //read the first line of the file
            line = br.readLine();
            st = new StringTokenizer(line);

            //read and save information on first line
            float xDist = Float.parseFloat(st.nextToken().trim());
            float yDist = Float.parseFloat(st.nextToken().trim());
            custCount = Integer.parseInt(st.nextToken().trim());
            st.nextToken(); // garbage

            int cap = Integer.parseInt(st.nextToken().trim());
            int dist = Integer.parseInt(st.nextToken().trim());

            //save max distance and capacity
            if (dist == 0) { //if no max distance, set to a large number...
                dist = Integer.MAX_VALUE;
            }

            if (cap == 0) { //if there is no maximum capacity, set it to a very large number
                cap = Integer.MAX_VALUE;
            }

            //place the number of shipments in the linked list instance
            mainShipments.noShipments = custCount;
            mainShipments.setMaxCapacity(cap);
            mainShipments.setMaxDuration(dist);

            //skip the header line for the customers
            br.readLine();

            //process Customers
            for (int i = 0; i < custCount; i++) {
                line = br.readLine();
                st = new StringTokenizer(line);

                //read customer information
                x = Float.parseFloat(st.nextToken().trim());
                y = Float.parseFloat(st.nextToken().trim());
                demand = Integer.parseInt(st.nextToken().trim());
                id = Integer.parseInt(st.nextToken().trim());
                eT = Integer.parseInt(st.nextToken().trim());
                lT = Integer.parseInt(st.nextToken().trim());
                sT = Integer.parseInt(st.nextToken().trim());

                //save customer in shipment linked list
                mainShipments.insertShipment(x, y, demand, id, eT, lT, sT);
            }
        } catch (NumberFormatException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }

        System.out.println("Read in problem file " + fileName + " with " +
            custCount + " customers.");

        Shipment s = mainShipments.first;

        while (s != null) {
            System.out.println("Shipment " + s.getShipNo() + " (" + s.vertexX +
                "," + s.vertexY + ")");
            s = s.next;
        }

        return true;
    }

    /**
    * Will read from a buffered reader until a line that isnt commented out
    * is encountered. Will return that line
    * @param br  read from this
    * @return String  the uncommented line
    */
    private String readLine(BufferedReader br) {
        String line = null;

        try {
            line = br.readLine();

            while ((line.charAt(0) == '#') && br.ready()) {
                line = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return line;
    }

    /**
    * Frame initialization method
    * @throws Exception  frame unable to initialize
    */
    private void jbInit() throws Exception {
        this.setTitle("Shipper Agent");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(gridBagLayout1);
        jmFile.setMnemonic('F');
        jmFile.setText("File");
        jmiOpen.setText("Open");
        jmiOpen.setEnabled(false);
        jmiOpen.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jmiOpen_actionPerformed(e);
                }
            });
        jmiExit.setText("Exit");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jmiExit_actionPerformed(e);
                }
            });
        jlShipperCode.setText("Shipper Code");
        jtfShipperCode.setToolTipText("ID code associated with Shipper");
        jtfShipperCode.setText("amd");
        jtfShipperCode.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jtfShipperCode_actionPerformed(e);
                }
            });
        jlShipperName.setText("Shipper Name");
        jtfShipperName.setText("American Micro Devices");
        jtfShipperName.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jtfShipperName_actionPerformed(e);
                }
            });
        jbSendShips.setEnabled(false);
        jbSendShips.setText("Send Shipments");
        jbSendShips.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbSendShips_actionPerformed(e);
                }
            });
        this.setJMenuBar(jMenuBar);
        jbRegister.setActionCommand("");
        jbRegister.setText("Register");
        jbRegister.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbRegister_actionPerformed(e);
                }
            });
        jlMasterIP.setText(" Master Server IP");
        jtfMasterIP.setText(shipperIP);
        jlMasterPort.setText("Master Server Port");
        jtfMasterPort.setText("" + HermesGlobals.masterServerPortNo);
        jMenuBar.add(jmFile);
        jmFile.add(jmiOpen);
        jmFile.add(jmiExit);
        this.getContentPane().add(jlMasterIP,
            new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(36, 11, 0, 19), 16, 4));
        this.getContentPane().add(jlShipperCode,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(36, 15, 0, 0), 12, 0));
        this.getContentPane().add(jlShipperName,
            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 15, 0, 0), 8, 0));
        this.getContentPane().add(jtfShipperName,
            new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 15, 0, 0), -25, 0));
        this.getContentPane().add(jtfShipperCode,
            new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 15, 0, 0), 80, 0));
        this.getContentPane().add(jtfMasterIP,
            new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 23, 0, 19), 92, 0));
        this.getContentPane().add(jtfMasterPort,
            new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 23, 0, 19), 92, 0));
        this.getContentPane().add(jlMasterPort,
            new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 11, 0, 19), 8, 4));
        this.getContentPane().add(jbRegister,
            new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(16, 15, 29, 0), 3, 0));
        this.getContentPane().add(jbSendShips,
            new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(16, 14, 29, 19), 0, 0));
        this.setSize(270, 200);
        this.setVisible(true);
    }

    /**
    * Open the problem file through a file chooser dialog
    * @param e  user event
    */
    void jmiOpen_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(".");

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileName = fileChooser.getSelectedFile().getPath();

            if (readFileData(fileName)) {
                jbSendShips.setEnabled(true);
            }
        }
    }

    /**
    * Terminate the agent
    * @param e  user event
    */
    void jmiExit_actionPerformed(ActionEvent e) {
        System.out.println("Exiting Shipper Agent.");
        System.exit(0);
    }

    /**
    * Send shipments to the Master Server for bidding
    * @param e  user event
    */
    void jbSendShips_actionPerformed(ActionEvent e) {
        jbSendShips.setEnabled(false);
        sendCustomersToMasterServer(shipperCode);
    }

    /**
    * update shipper code
    * @param e user event
    */
    void jtfShipperCode_actionPerformed(ActionEvent e) {
        shipperCode = jtfShipperCode.getText();
    }

    /**
    * update shipper name
    * @param e user event
    */
    void jtfShipperName_actionPerformed(ActionEvent e) {
        shipperName = jtfShipperName.getText();
    }

    /**
    * Register the agent with the Master Server
    * @param e  user event
    */
    void jbRegister_actionPerformed(ActionEvent e) {
        jtfShipperCode.setEditable(false);
        jtfShipperName.setEditable(false);
        jtfMasterIP.setEditable(false);
        jtfMasterPort.setEditable(false);
        shipperPort = registerShipperWithMasterServer(shipperCode, shipperName,
                shipperIP);
        logWriter = new LogWriter("s" + shipperPort + ".log");
        messageListener = new ShipperMessageListener(shipperPort, this,
                shipListAdaptor);
        jmiOpen.setEnabled(true);
    }

    /**
    * Return the shipper ip address
    * @return String  the shipper ip address
    */
    public String getIP() {
        return shipperIP;
    }

    /**
    * Return the shipper port number
    * @return int  the shipper port number
    */
    public int getPort() {
        return shipperPort;
    }

    /**
    * Set the IP address of the Master Server through the GUI
    * @param e  user event
    */
    void jtfMasterIP_actionPerformed(ActionEvent e) {
        HermesGlobals.masterServerIP = jtfMasterIP.getText();
    }

    /**
    * Set the port number of the Master Server through the GUI
    * @param e  user event
    */
    void jtfMasterPort_actionPerformed(ActionEvent e) {
        HermesGlobals.masterServerPortNo = Integer.parseInt(jtfMasterPort.getText());
    }
}
