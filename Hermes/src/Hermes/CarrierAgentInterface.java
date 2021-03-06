package Hermes;

import Zeus.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;

import java.sql.*;

/**
 * <p>Title: CarrierAgentInterface.java </p>
 * <p>Description: Handles the user actions with the GUI, includes registering
 *    the agent with the Master Server, optimizing the agent's schedule, and
 *    displaying the schedule graphically.</p>
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
public class CarrierAgentInterface extends JFrame implements MessageTags {

	
	
    public ShipperListAdaptor shipmentList;
    public ZeusAdaptor zAdapt;
    private String carrierName;
    private String carrierCode;
    private String carrierIP;
    private int carrierPort;
    public boolean isLock = false;
    public boolean hasGUI = false;
    public int myMasterPort;
    public LogWriter logWriter = null;
    private CarrierMessageListener messageListener;
    private JLabel CarrierLabel = new JLabel();
    private JLabel jLabelCarName = new JLabel();
    private JTextField jtfCarName = new JTextField();
    private JLabel jLabelCarCode = new JLabel();
    private JButton jbRegister = new JButton();
    private JButton jbOptimize = new JButton();
    private JTextField jtfCarCode = new JTextField();
    private JButton jbDisplayRoute = new JButton();
    private JLabel jlTotalDemand = new JLabel();
    private JTextField jtfTotalDemand = new JTextField();
    private JLabel jlTotalDistance = new JLabel();
    private JTextField jtfTotalDistance = new JTextField();
    private JTextField jtfTotalTravTime = new JTextField();
    private JTextField jtfTotalTard = new JTextField();
    private JTextField jtfTotalExcTime = new JTextField();
    private JTextField jtfTotalOverload = new JTextField();
    private JLabel jlTotalTravTime = new JLabel();
    private JLabel jlTotalTard = new JLabel();
    private JLabel jlExcTime = new JLabel();
    private JLabel jlTotalOverload = new JLabel();
    private JLabel jlTotalWaitTime = new JLabel();
    private JTextField jtfTotalWaitTime = new JTextField();
    private JTextField jtfTotalServTime = new JTextField();
    private JLabel jlTotalServTime = new JLabel();
    private JLabel jlTotalCustomers = new JLabel();
    private JTextField jtfTotalCustomers = new JTextField();
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu jmFile = new JMenu();
    private JMenuItem jmiExit = new JMenuItem();
    private JMenuItem jmiOptions = new JMenuItem();
    private JTextField jtfCarPort = new JTextField();
    private JLabel jLabelCarPort = new JLabel();
    private JLabel jlTotalTrucks = new JLabel();
    private JTextField jtfTotalTrucks = new JTextField();
    private JLabel jlMasterIP = new JLabel();
    private JLabel jlMasterPort = new JLabel();
    private JTextField jtfMasterIP = new JTextField();
    private JTextField jtfMasterPort = new JTextField();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton jbReschedule = new JButton();

    /**
    * constructor creates a socket that listens to incoming messages at a specific
    * port number
    * @param depotFile  file name of depot information
    * @param IP ip address of this carrier
    */
    public CarrierAgentInterface(String depotFile, String IP) {
        carrierIP = IP;
        zAdapt = new ZeusAdaptor(new Zeus());
        shipmentList = new ShipperListAdaptor(zAdapt.getZeus().getRoot()
                                                    .getVRPTW().mainShipments);
        readFile(depotFile);

        try {
            jbInit();
        } catch (Exception ex) {
        }
    }

    /**
     * This will start the interface without the Gui
     * @param depotFile depot file containing depot constraints
     * @param IP ip of carrier
     * @param setTrue port of carrier
     */
    public CarrierAgentInterface(String depotFile, String IP, boolean setTrue) {
    	carrierIP = IP;
        
        zAdapt = new ZeusAdaptor(new Zeus());
        readFile(depotFile);
        carrierPort = registerCarrierWithMasterServer("C", "carrier", IP);

        try {
            Thread.sleep(3000);
            return;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        shipmentList = new ShipperListAdaptor(zAdapt.getZeus().getRoot()
                .getVRPTW().mainShipments);
        
        logWriter = new LogWriter("c" + carrierPort + ".txt");
        messageListener = new CarrierMessageListener(carrierPort, zAdapt, this);
        System.out.println("port no " + carrierPort + " " + myMasterPort);
        
    }
    
    public boolean isHasGUI() {
		return hasGUI;
	}

	public void setHasGUI(boolean hasGUI) {
		this.hasGUI = hasGUI;
		shipmentList = new ShipperListAdaptor(zAdapt.getZeus().getRoot()
                .getVRPTW().mainShipments);
        
        logWriter = new LogWriter("c" + carrierPort + ".txt");
        messageListener = new CarrierMessageListener(carrierPort, zAdapt, this);
        System.out.println("port no " + carrierPort + " " + myMasterPort);
	}

    /**
    * Constructor - not used
    */
    public CarrierAgentInterface() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * this method registers the carrier on the masterserver
    * @return int  port number for the carrier
    * @param code the code for the carrier
    * @param name the name of the carrier
    * @param IP ip address of this carrrier
    */
    public int registerCarrierWithMasterServer(String code, String name,
        String IP) {
        int portNo = 0;

        //register the carrier in the master database
        try {
            Message addressMsg = new Message();
            Message registerMsg = new Message();
            Message response = null;
            Socket socket = null;
            BufferedReader in = null;
            ThreadedClient tc;

            //create a new register message to tell master server that this carrier
            //is now active
            registerMsg.setMessageType(MessageTags.RegisterTag);
            registerMsg.addArgument(MessageTags.AgentTypeTag, CarrierMessage);

            registerMsg.addArgument(CodeTag, code);
            registerMsg.addArgument(NameTag, name);

            //create a message containing the address of this carrier for use by the
            //master server
            addressMsg.setMessageType(InetAddressTag);

            addressMsg.addArgument(MessageTags.IPAddressTag, IP);
            addressMsg.addArgument(MessageTags.PortNumberTag,
                Integer.toString(carrierPort));
            registerMsg.addArgument(addressMsg);

            //the message is to be sent to the master server
            // read message from open socket
            // parse message and return port no.
            try {
                socket = new Socket(HermesGlobals.masterServerIP,
                        HermesGlobals.masterServerPortNo);
                in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                new ThreadedClient(registerMsg, socket);

                Message portMsg = getResponse(socket);
                portNo = Integer.parseInt(portMsg.getValue(PortNumberTag));
                myMasterPort = Integer.parseInt(portMsg.getValue(MasterPortTag));
            } catch (Exception ex) {
                System.err.print("Carrier unable to register: " + ex);
                ex.printStackTrace();
                System.exit(800135);
            } finally {
                try {
                    in.close();
                    socket.close();
                } catch (IOException ex) {
                    System.err.print(
                        "Error closing socket for Carrier registration: " + ex);
                }
            }

            jbRegister.setEnabled(false);

            System.out.println(
                "Registered Carrier with MasterServer, got port: " + portNo);

            return portNo;
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //display output about unable registering
        System.err.println(
            "UNABLE to Registered Carrier with Master Server, got port " +
            portNo);

        return portNo;
    }

    /**
    * Unregister the agent with the Master Server
    */
    public void unregisterWithMasterServer() {
        //register the shipper in the master database
        Message addressMsg = new Message();
        Message unregisterMsg = new Message();

        //create a new register message to tell master server that this carrier
        //is now active
        unregisterMsg.setMessageType(MessageTags.UnregisterTag);
        unregisterMsg.addArgument(MessageTags.AgentTypeTag, CarrierMessage);

        //create a message containing the address of this carrier for use by the
        //master server
        addressMsg.setMessageType(InetAddressTag);

        addressMsg.addArgument(MessageTags.IPAddressTag, carrierIP);
        addressMsg.addArgument(MessageTags.PortNumberTag,
            Integer.toString(carrierPort));
        unregisterMsg.addArgument(addressMsg);

        //the message is to be sent to the master server
        // read message from open socket
        // parse message and return port no.
        try {
            logWriter.writeLog("Unregistering carrier");
            new ThreadedClient(unregisterMsg, HermesGlobals.masterServerIP,
                this.myMasterPort);
        } catch (Exception ex) {
            System.err.print("Carrier unable to unregister: " + ex);
            logWriter.writeLog("Carrier unable to unregister: " + ex);
        }

        jbRegister.setEnabled(true);
    }

    /**
    * Initialize the Carrier Agent frame
    * @throws Exception
    */
    private void jbInit() throws Exception {
        jLabelCarName.setText("Carrier Name");
        this.getContentPane().setLayout(gridBagLayout1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminates carrier when frame is closed only if carriers is not yet registered

        jmFile.setMnemonic('F');
        jmFile.setText("File");
        jmiExit.setEnabled(false); //remains inactive carrier user registers
        jmiExit.setToolTipText("Unregister and terminate the agent.");
        jmiExit.setText("Exit");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jmiExit_actionPerformed(e);
                }
            });

        jmiOptions.setToolTipText("Set constraint multipliers for the agent.");
        jmiOptions.setText("Options");
        jmiOptions.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jmiOptions_actionPerformed(e);
                }
            });

        CarrierLabel.setText("Carrier Name");
        this.setFont(new java.awt.Font("Dialog", 1, 12));
        this.setTitle("Carrier Agent");
        jtfCarName.setText("UPS SRU");
        jtfCarName.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jtfCarName_actionPerformed(e);
                }
            });
        jLabelCarCode.setText("Carrier Code");
        jbRegister.setActionCommand("jbregisterCar");
        jbRegister.setContentAreaFilled(false);
        jbRegister.setMargin(new Insets(2, 5, 2, 5));
        jbRegister.setMnemonic('0');
        jbRegister.setText("Register Agent");
        jbRegister.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbRegister_actionPerformed(e);
                }
            });
        jbOptimize.setEnabled(false);
        jbOptimize.setActionCommand("jbOptimize");
        jbOptimize.setMargin(new Insets(2, 5, 2, 5));
        jbOptimize.setMnemonic('0');
        jbOptimize.setText("Optimize");
        jbOptimize.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbOptimize_actionPerformed(e);
                }
            });
        jtfCarCode.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jtfCarCode_actionPerformed(e);
                }
            });
        jtfCarCode.setText("2020");
        jbDisplayRoute.setEnabled(false);
        jbDisplayRoute.setActionCommand("jbDisplayRoute");
        jbDisplayRoute.setMargin(new Insets(2, 5, 2, 5));
        jbDisplayRoute.setMnemonic('0');
        jbDisplayRoute.setText("Display");
        jbDisplayRoute.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbDisplayRoute_actionPerformed(e);
                }
            });
        jlTotalDemand.setText("Total Demand");
        jlTotalDistance.setText("Total Distance");
        jlTotalTravTime.setText("Total Travel Time");
        jlTotalTard.setText("Total Tardiness");
        jlExcTime.setText("Total Excess Time");
        jlTotalOverload.setText("Total Overload");
        jlTotalWaitTime.setText("Total Wait Time");
        jtfTotalDemand.setEditable(false);
        jtfTotalDistance.setEditable(false);
        jtfTotalTravTime.setEditable(false);
        jtfTotalTard.setEditable(false);
        jtfTotalExcTime.setEditable(false);
        jtfTotalOverload.setEditable(false);
        jtfTotalWaitTime.setEditable(false);
        jtfTotalServTime.setEditable(false);
        jlTotalServTime.setText("Total Service Time");
        jlTotalCustomers.setText("Total Customers");
        jtfTotalCustomers.setEditable(false);

        this.setJMenuBar(jMenuBar1);
        jMenuBar1.setAlignmentY((float) 0.5);

        jtfCarPort.setText("Unassigned");
        jLabelCarPort.setText("Carrier Port");
        jtfCarPort.setEditable(false);

        jlTotalTrucks.setText("Total Trucks");
        jtfTotalTrucks.setEditable(false);
        jlMasterIP.setText(" Master Server IP");
        jlMasterPort.setText("Master Server Port");
        jtfMasterIP.setText(carrierIP);
        jtfMasterIP.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jtfMasterIP_actionPerformed(e);
                }
            });
        jtfMasterPort.setText("" + HermesGlobals.masterServerPortNo);
        jtfMasterPort.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jtfMasterPort_actionPerformed(e);
                }
            });
        jbReschedule.setEnabled(false);
        jbReschedule.setToolTipText("");
        jbReschedule.setMargin(new Insets(2, 5, 2, 5));
        jbReschedule.setMnemonic('0');
        jbReschedule.setText("Reschedule");
        jbReschedule.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbReschedule_actionPerformed(e);
                }
            });
        jMenuBar1.add(jmFile);
        jmFile.add(jmiExit);

        //	jmFile.add(jmiOptions);
        this.getContentPane().add(jtfTotalOverload,
            new GridBagConstraints(2, 13, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 33, 20), 263, -3));
        this.getContentPane().add(jtfTotalExcTime,
            new GridBagConstraints(2, 12, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 0, 0, 20), 263, -3));
        this.getContentPane().add(jtfTotalTard,
            new GridBagConstraints(2, 11, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 0, 20), 263, -3));
        this.getContentPane().add(jtfTotalServTime,
            new GridBagConstraints(2, 10, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 0, 20), 263, -3));
        this.getContentPane().add(jtfTotalWaitTime,
            new GridBagConstraints(2, 9, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 0, 20), 263, -3));
        this.getContentPane().add(jtfTotalDistance,
            new GridBagConstraints(2, 7, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 0, 20), 263, -3));
        this.getContentPane().add(jtfTotalTravTime,
            new GridBagConstraints(2, 8, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(10, 0, 0, 20), 263, -3));
        this.getContentPane().add(jtfTotalDemand,
            new GridBagConstraints(2, 6, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(9, 0, 0, 20), 263, -3));
        this.getContentPane().add(jlTotalDemand,
            new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(9, 7, 0, 10), 27, 1));
        this.getContentPane().add(jlTotalDistance,
            new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(9, 7, 0, 10), 26, 1));
        this.getContentPane().add(jlTotalTravTime,
            new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 7, 0, 10), 11, 1));
        this.getContentPane().add(jlTotalWaitTime,
            new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(9, 7, 0, 10), 20, 1));
        this.getContentPane().add(jlTotalServTime,
            new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(9, 7, 0, 10), 4, 1));
        this.getContentPane().add(jlTotalTard,
            new GridBagConstraints(0, 11, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(9, 7, 0, 10), 19, 1));
        this.getContentPane().add(jlExcTime,
            new GridBagConstraints(0, 12, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 7, 0, 10), 4, 1));
        this.getContentPane().add(jlTotalOverload,
            new GridBagConstraints(0, 13, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(9, 7, 33, 11), 25, 1));
        this.getContentPane().add(jbRegister,
            new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(17, 7, 0, 10), 13, 3));
        this.getContentPane().add(jlTotalCustomers,
            new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(17, 7, 0, 10), 13, 1));
        this.getContentPane().add(jtfTotalCustomers,
            new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(17, 0, 0, 10), 40, -3));
        this.getContentPane().add(jLabelCarPort,
            new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 7, 0, 0), 11, 4));
        this.getContentPane().add(jtfCarName,
            new GridBagConstraints(1, 0, 2, 2, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(39, 13, 15, 11), 38, 0));
        this.getContentPane().add(jLabelCarName,
            new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(39, 7, 15, 0), 4, 4));
        this.getContentPane().add(jtfCarCode,
            new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 13, 14, 11), 64, 0));
        this.getContentPane().add(jLabelCarCode,
            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 7, 14, 8), 0, 4));
        this.getContentPane().add(jtfCarPort,
            new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 13, 0, 11), 24, 0));
        this.getContentPane().add(jlMasterIP,
            new GridBagConstraints(3, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(32, 8, 0, 50), 16, 4));
        this.getContentPane().add(jtfMasterIP,
            new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 32, 0, 20), 92, 0));
        this.getContentPane().add(jtfMasterPort,
            new GridBagConstraints(3, 3, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 32, 0, 20), 92, 0));
        this.getContentPane().add(jtfTotalTrucks,
            new GridBagConstraints(3, 5, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(17, 95, 0, 20), 40, -3));
        this.getContentPane().add(jlTotalTrucks,
            new GridBagConstraints(2, 5, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(17, 75, 0, 0), 12, 1));
        this.getContentPane().add(jlMasterPort,
            new GridBagConstraints(3, 2, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 8, 0, 50), 30, 4));
        this.getContentPane().add(jbReschedule,
            new GridBagConstraints(2, 4, 3, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 75, 0, 115), 0, 4));
        this.getContentPane().add(jbDisplayRoute,
            new GridBagConstraints(3, 4, 2, 1, 1.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(17, 105, 0, 20), 40, 3));
        this.getContentPane().add(jbOptimize,
            new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(16, 0, 0, 0), 15, 3));

        this.setSize(new Dimension(409, 461));
        this.setVisible(true);
    }

    /**
    * this unregisters carrier and exits. its is ONLY enabled if the carrier
    * was previously registered
    * @param e  user action
    */
    void jmiExit_actionPerformed(ActionEvent e) {
        System.out.println("Exiting Carrier Agent ");
        unregisterWithMasterServer(); //unregister when exited
        logWriter.writeLog("Terminating agent.");
        System.exit(0);
    }

    /**
    * this opens a dialog to set the constraint multipliers for the Carrier
    * agent's instance of Zeus
    * @param e  user action
    */
    void jmiOptions_actionPerformed(ActionEvent e) {
        logWriter.writeLog("Setting options.");
    }

    /**
    * lesser opitmization on only this carrier
    * @param e  user event
    */
    void jbOptimize_actionPerformed(ActionEvent e) {
        logWriter.writeLog("Optimizing solution.");

        //prepare local optimization message
        Message localOptMsg = new Message();
        localOptMsg.setMessageType(LocalOptTag);

        //sends message to itself so Carriermessagehandler can execute
        new ThreadedClient(localOptMsg, getIP(), getPort());
    }

    /**
    * update carrier name
    * @param e  user event
    */
    void jtfCarName_actionPerformed(ActionEvent e) {
        carrierName = jtfCarName.getText();
    }

    /**
    * update carrier code
    * @param e  user event
    */
    void jtfCarCode_actionPerformed(ActionEvent e) {
        carrierCode = jtfCarCode.getText();
    }

    /**
    * after schedules completed
    */
    public void EnableCarrierJButtons() {
        jbDisplayRoute.setEnabled(true);
        jbOptimize.setEnabled(true);
        jbReschedule.setEnabled(true);
    }

    /**
    * Register the agent with the Master Server
    * @param e  user event
    */
    void jbRegister_actionPerformed(ActionEvent e) {
        carrierPort = registerCarrierWithMasterServer(jtfCarCode.getText(),
                jtfCarName.getText(), carrierIP);
        logWriter = new LogWriter("c" + carrierPort + ".log");
        logWriter.writeLog("Registered with Master Server: " + carrierIP + ":" +
            carrierPort);
        messageListener = new CarrierMessageListener(carrierPort, zAdapt, this);

        this.setTitle("Carrier Agent at " +
            new Integer(carrierPort).toString()); //changing title of frame
        jtfCarPort.setFont(new java.awt.Font("SansSerif", 1, 14)); //setting fonts for port number to be bold
        jtfCarPort.setText(new Integer(carrierPort).toString()); //setting port number

        jmiExit.setEnabled(true); //now unregister & exit button is active. Default exit is now ainactive
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //only unregister & exit menu item can close carrier
        jbRegister.setEnabled(false);
        jtfCarName.setEditable(false);
        jtfCarCode.setEditable(false);
        jtfMasterIP.setEditable(false);
        jtfMasterPort.setEditable(false);
    }

    /**
    * Return the ip address
    * @return ip address
    */
    public String getIP() {
        return carrierIP;
    }

    /**
    * Return the port number
    * @return port number
    */
    public int getPort() {
        return carrierPort;
    }

    /**
    * <p>Read in the data from the datafile and load it into the Zeus System
    * using the tokenizer.</p>
    * @param fileName String type of the filename consisting of the data
    * @return boolean Return true if all successful, false for failure
    */
    private boolean readFile(String fileName) {
        Message msg = new Message();
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        int depCount = 0;
        int depIndex = 0;
        float xIndex = 0;
        float yIndex = 0;
        int dist = 0;
        int capacity = 0;
        File file = new File(fileName);

        try {
            //open the requested file
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        } catch (Exception e) {
            System.err.println("File is not present " + file);

            return false;
        }

        String line;
        StringTokenizer st = null;

        try {
            br.readLine(); // skip text labels
            line = br.readLine(); // depot constraints
            st = new StringTokenizer(line);

            // skip text labels
            br.readLine();
        } catch (IOException ex) {
            System.err.println("Error reading depot file: " + fileName + ": " +
                ex);
        }

        try {
            depCount = Integer.parseInt(st.nextToken().trim());
            dist = Integer.parseInt(st.nextToken().trim());
            capacity = Integer.parseInt(st.nextToken().trim());
        } catch (NumberFormatException ex) {
            System.err.println("Error processing depot constraint info: " + ex);
        }

        //process depots
        for (int i = 0; i < depCount; i++) {
            try {
                line = br.readLine();
                st = new StringTokenizer(line);
            } catch (IOException ex) {
                System.err.println("Error reading depot file: " + fileName +
                    ": " + ex);
            }

            //read the depot information
            try {
                depIndex = Integer.parseInt(st.nextToken().trim());
                xIndex = Float.parseFloat(st.nextToken().trim());
                yIndex = Float.parseFloat(st.nextToken().trim());
            } catch (NumberFormatException ex) {
                System.err.println("Error processing depot coordinate info: " +
                    ex);
            }

            // build the message
            msg.addArgument(IndexTag, "" + depIndex);
            msg.addArgument(XCoordTag, "" + xIndex);
            msg.addArgument(YCoordTag, "" + yIndex);
        }

        //save max distance and capacity
        if (dist == 0) { //if no max distance, set to a large number...
            dist = 999999999;
        }

        if (capacity == 0) { //if there is no maximum capacity, set it to a very large number
            capacity = 999999999;
        }

        // build the message
        msg.addArgument(NumberOfDepotsTag, "" + depCount);
        msg.addArgument(MaxCapacityTag, "" + capacity);
        msg.addArgument(MaxDistanceTag, "" + dist);
        msg.addArgument(FileNameTag, fileName);

        zAdapt.setProblemConstraints(msg);

        return true;
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
        jtfTotalDemand.setText("" + dem);
        jtfTotalDistance.setText("" + dis);
        jtfTotalTravTime.setText("" + tt);
        jtfTotalTard.setText("" + tar);
        jtfTotalExcTime.setText("" + exc);
        jtfTotalOverload.setText("" + ovl);
        jtfTotalWaitTime.setText("" + wait);
        jtfTotalServTime.setText("" + serv);
        jtfTotalCustomers.setText("" + cust);
        jtfTotalTrucks.setText("" + trucks);
    }

    /**
    * Display the routes for the carrier agent's schedule graphically
    * @param e  user event
    */
    void jbDisplayRoute_actionPerformed(ActionEvent e) {
        logWriter.writeLog("Displaying route");
        JOptionPane.showMessageDialog(this,
            new BorlandIndependantDisplay(zAdapt.getZeus().getRoot().getVRPTW().mainDepots),
            "Display for carrier @ " + carrierIP + ":" + carrierPort,
            JOptionPane.PLAIN_MESSAGE);
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

    /**
     * Purge the schedule and reinsert the shipments for this carrier using the
     * VRPTW insertion heuristic
     * @param e  user event
     */
    void jbReschedule_actionPerformed(ActionEvent e) {
        jbReschedule.setEnabled(false);
        jbDisplayRoute.setEnabled(false);
        jbOptimize.setEnabled(false);

        System.out.println("Running Reschedule Optimization");
        zAdapt.reschedule();
        zAdapt.calcCurrentValues();
        setTextFields(zAdapt.calculateCapacity(), zAdapt.calculateDistTrav(),
            zAdapt.calculateTotalTime(), zAdapt.calcTotalTardiness(),
            zAdapt.calcTotalExcessTime(), zAdapt.calcTotalOverload(),
            zAdapt.calcTotalWaitTime(), zAdapt.calcTotalServiceTime(),
            (zAdapt.getSize() - (2 * zAdapt.getNumTrucks())),
            zAdapt.getNumTrucks());

        jbReschedule.setEnabled(true);
        jbDisplayRoute.setEnabled(true);
        jbOptimize.setEnabled(true);
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
    
}//End of class; EoF
