package Hermes;

import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.sql.*;

import java.util.*;


/**
 * <p>Title: MasterMessageHandler.java </p>
 * <p>Description: Handles all incoming messages to the master server.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class MasterMessageHandler extends Thread implements MessageTags {
    /**
     * Socket that will be read.
     */
    private Socket incomingCommunication;

    /**
     * Interface to the register shipper and carriers database
     */
    private RegisterShipperCarrierDatabaseInterface regShipCarDB;
    private MasterAgentInterface mai;

    /**
     * Constructor, will read all the messages from the incoming thread and
     * do the appropriate actions to handle them.
     * @param incomingComm socket to the newly connected client
     * @param rscDB register shipper carrier database interface
     * @param mai  instance of MasterAgentInterface for backwards communication
     */
    public MasterMessageHandler(Socket incomingComm,
        RegisterShipperCarrierDatabaseInterface rscDB, MasterAgentInterface mai) {
        //assign the socket
        incomingCommunication = incomingComm;

        //assign the database link
        regShipCarDB = rscDB;

        this.mai = mai;

        //start the thread to handle the message
        start();
    }

    /**
     * This is what the thread executes when started. It will listen for all
     * the different types of messages it knows how to handle.
     */
    public void run() {
        try {
            //create a reader to the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        incomingCommunication.getInputStream()));

            //get the message contained in the socket
            Message clientMsg = new Message(in.readLine());

            mai.logWriter.writeLog("Incoming message: " + clientMsg.toString());

            //get the type of message
            String messageType = clientMsg.getMessageType();

            //now process the message based upon its type
            //shipper is beginning to send the customers
            if (messageType.equals(StartSendPointsTag)) {
                System.out.println("Start Send Points");
            }
            //shipper has finished sending the customers
            else if (messageType.equals(EndSendPointsTag)) {
                System.out.println("End Send Points");
            }
            //tell all carriers to enable their buttons
            else if (messageType.equals(EnableTag)) {
                mai.logWriter.writeLog("Enabling buttons.");

                Message msg = new Message();
                msg.setMessageType(SummaryTag);
                mai.isDoneSched = true;
                mai.enableMasterJButtons();
                clientMsg.addArgument(msg);
                mai.clearTextFields();

                //broadcasts enable button message and get summary to all carriers
                broadcastToCarriers(clientMsg);

                if (mai.hasGUI == false) {
                    mai.runLesserOpts();
                    globalOptimization();
                    mai.printSolution();
                }
            } else if (messageType.equals(SummaryTag)) {
                mai.logWriter.writeLog("Retrieving totals.");

                if (mai.isDoneSched) {
                    getTotals(clientMsg);
                }
            } else if (messageType.equals(GetSummaryTag)) {
                if (mai.isDoneSched) {
                    getSummary();
                }
            } else if (messageType.equals(LocalOptTag)) {
                //if lesser opt button is hit on the master then
                //broadcasts local opt message to all carriers
                mai.logWriter.writeLog(
                    "Sending Lesser Optimization message to All registered Carriers");
                System.out.println(
                    "Sending Lesser Optimization message to All registered Carriers");
                broadcastToCarriers(clientMsg);
                respond();
            }
            //a shipper or carrier is trying to register
            else if (messageType.equals(RegisterTag)) {
                //a shipper or carrier has started and would like to register
                if (clientMsg.getValue(AgentTypeTag).equals(ShipperMessage)) {
                    registerShipper(clientMsg);
                } else if (clientMsg.getValue(AgentTypeTag).equals(CarrierMessage)) {
                    registerCarrier(clientMsg);
                }
            }
            //a shipper or carrier is trying to unregister
            else if (messageType.equals(UnregisterTag)) {
                //a shipper or carrier experience a Port Bind exception with their
                //designated port number, so give them a new one
                //or a shipper or carrier is terminating, so remove them.
                if (clientMsg.getValue(AgentTypeTag).equals(ShipperMessage)) {
                    removeShipper(clientMsg);
                } else if (clientMsg.getValue(AgentTypeTag).equals(CarrierMessage)) {
                    removeCarrier(clientMsg);
                }
            }
            // take a break from the auctioning and optimize the routes
            else if (messageType.equals(AuctionBreakTag)) {
                mai.isLock = true;
                mai.jbOptimize_actionPerformed(new ActionEvent(this, 0, ""));

                if (mai.numCarriers > 1) {
                    globalOptimization();
                }

                respond();
            }
            //broadcast message
            else if (messageType.equals(MessageTags.BroadcastTag)) {
                System.out.println("Broadcast");

                //take this recieved message and broadcast it to the proper set
                //of agents
                if (clientMsg.getValue(DestinationTag).equals(CarrierMessage)) {
                    //get the type of message the embedded message is
                    String embeddedMsgType = clientMsg.getValue(PackagedMessageTag);

                    //get the embedded message
                    Message embMsg = clientMsg.getMessage(embeddedMsgType);

                    //broadcast that message to all the carriers
                    broadcastToCarriers(embMsg);
                } else if (clientMsg.getValue(DestinationTag).equals(ShipperMessage)) {
                    //get the type of message the embedded message is
                    String embeddedMsgType = clientMsg.getValue(PackagedMessageTag);

                    //get the embedded message
                    Message embMsg = clientMsg.getMessage(embeddedMsgType);

                    //broadcast that message to all the shippers
                    broadcastToShippers(embMsg);
                }
            } else if (messageType.equals(MessageTags.RelayTag)) {
                //get the address to send the message to
                Message addr = clientMsg.getMessage(RelayAddressTag);

                //get what type of address is contained
                String msgType = clientMsg.getValue(MessageTags.PackagedMessageTag);
                System.out.println("Relay Message " + msgType);

                //get the contained message
                Message msg = clientMsg.getMessage(msgType);

                //if it is a cost tag... send the number of carriers so the shipper
                //knows when to stop accepting bids
                if (msgType.equals(InsertCostTag)) {
                    msg.addArgument(CarrierCountTag, "" + mai.numCarriers);
                }

                //relay the message
                this.relayMessage(msg, addr);
            } else if (messageType.equals(AckTag)) {
                //do nothing, just an ack
            } else if (messageType.equals(GlobalOptTag)) {
                if (mai.numCarriers > 1) {
                    //handle all global opt functionalities
                    globalOptimization();
                    getSummary();
                }

                respond();
            } else {
                System.err.println("Unknown Tag " + messageType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Respond to a received message over the same socket.
     */
    private synchronized void respond() {
        Message msg = new Message();

        //output writer socket
        PrintWriter out = null;

        try {
            out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                            incomingCommunication.getOutputStream())), true);
        } catch (IOException ex) {
            System.err.println("Error in respond: " + ex);
        }

        msg.setMessageType(AckTag);
        out.println(msg.getMessageString());
    }

    /**
     * this performs all global optimizations
     */
    private synchronized void globalOptimization() {
        if (mai.isChanged) {
            mai.carrierInfoArray = regShipCarDB.getCarrierIPandPort();
            mai.numCarriers = mai.carrierInfoArray.length;
            mai.isChanged = false;
        }

        Socket socket = null;
        Message response = null;
        Message LocalOptMsg = new Message(); //to perform local opts after global
        LocalOptMsg.setMessageType(LocalOptTag);

        if (mai.numCarriers > 1) {
            //cycle through all carriers performing exchanges
            for (int i = 0; i < (HermesGlobals.maxLoop); i++) {
                cycle01(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange01
                cycle10(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange10
                cycle02(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange02
                cycle20(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange20
                cycle11(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange11
                cycle12(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange12
                cycle21(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange21
                cycle22(mai.carrierInfoArray, mai.numCarriers); //cycle through exchange22
            }

            try {
                socket = new Socket(HermesGlobals.masterServerIP,
                        HermesGlobals.masterServerPortNo);

                new ThreadedClient(LocalOptMsg, socket);
                response = mai.getResponse(socket);
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
            while (!response.getMessageType().equals(MessageTags.AckTag))
                ;
        } else {
            System.out.println(
                "ATTENTION: More than ONE carrier NEEDED for global Opts.");
        }

        if (mai.isLock) {
            mai.isLock = false;
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt01
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle01(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt01(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange01 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange01 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt01(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange01 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange01 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt10
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle10(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt10(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange10 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange10 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt10(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange10 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange10 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt11
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle11(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt11(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange11 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange11 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt11(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange11 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange11 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt12
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle12(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt12(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange12 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange12 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt12(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange12 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange12 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt21
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle21(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt21(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange21 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange21 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt21(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange21 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange21 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt02
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle02(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt02(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange02 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange02 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt02(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange02 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange02 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt20
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle20(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt20(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange20 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange20 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt20(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange20 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange20 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * This methods cycles through all the registered carriers performing
     * myGreatOpt22
     * @param carrierInfo array containing all registered carrier info
     * @param NumberOfCarriers number of carriers
     */
    void cycle22(Message[] carrierInfo, int NumberOfCarriers) {
        boolean status;

        for (int i = 0; i <= (NumberOfCarriers - 2); i++) {
            status = myGreatOpt22(carrierInfo[i], carrierInfo[i + 1]);

            if (status) {
                System.out.println(
                    "Exchange22 SUCCESFULLY performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            } else {
                System.out.println("Exchange22 NOT performed between Carrier " +
                    carrierInfo[i].getValue(PortNumberTag) + " and Carrier " +
                    carrierInfo[i + 1].getValue(PortNumberTag));
            }
        }

        //complete cycle
        status = myGreatOpt22(carrierInfo[NumberOfCarriers - 1], carrierInfo[0]);

        if (status) {
            System.out.println(
                "Exchange22 SUCCESFULLY performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        } else {
            System.out.println("Exchange22 NOT performed between Carrier " +
                carrierInfo[NumberOfCarriers - 1].getValue(PortNumberTag) +
                " and Carrier " + carrierInfo[0].getValue(PortNumberTag));
        }
    }

    /**
     * handles 01 optimizations between carier a and B. accept0 remove 1
     * @param carrierA message containing carrier A's contact info
     * @param carrierB message containing carrier B's contact info
     * @return true if opt01  was performed. ele if not
     */
    private boolean myGreatOpt01(Message carrierA, Message carrierB) {
        String ipCarrA;
        int portCarrA;
        String ipCarrB;
        int portCarrB;
        float insertCost = 0;
        float savingsCost = 0;
        Message savingsMsg;
        Message replyMsg;
        Message bestSaveTypeMsg = new Message();
        bestSaveTypeMsg.setMessageType(CalcBestSavingsTag);

        //parse messages and obtain info necessary of carrier might be removing but not accpetion
        ipCarrA = carrierA.getValue(MessageTags.IPAddressTag);
        portCarrA = Integer.parseInt(carrierA.getValue(
                    MessageTags.PortNumberTag));

        //parse messages and obtain info necessary of carrier that will not remove but might be accepting
        ipCarrB = carrierB.getValue(IPAddressTag);
        portCarrB = Integer.parseInt(carrierB.getValue(
                    MessageTags.PortNumberTag));

        //getBestSavings from carrierA
        savingsMsg = getBestSavings(bestSaveTypeMsg, ipCarrA, portCarrA);

        //this means carrier is empty DO NOT CONTINUE
        if (savingsMsg.getValue(FeasibilityTag).equals("0")) {
            System.out.println("Carrier " + portCarrA +
                " IS EMPTY. Nothing can possibly be removed");

            return false; //do not continue simply jump out
        }

        savingsMsg.addArgument(ExchangeTypeTag, "01");

        //getInsertion acceptance of shipment from carrA into carrB
        //savingsMsg forwarded because it has all the shipment ifo
        replyMsg = getOptInsertCost(savingsMsg, ipCarrB, portCarrB);

        if (replyMsg.getMessageType().equals(AcceptTag)) {
            System.out.println(
                "Exchange 01 Feasible...should be performing exchange 01 on " +
                portCarrA);

            Message exchange01Remove = new Message();
            exchange01Remove.setMessageType(ExchangeTag);
            exchange01Remove.addArgument(ExchangeTypeTag, "01A");
            exchange01Remove.addArgument(savingsMsg);

            //remove ship fom carA
            new ThreadedClient(exchange01Remove, ipCarrA, portCarrA);

            //insertship in carrB
            Message exchange01Insert = new Message();
            exchange01Insert.setMessageType(ExchangeTag);
            exchange01Insert.addArgument(ExchangeTypeTag, "01B");
            exchange01Insert.addArgument(savingsMsg);
            exchange01Insert.addArgument(AfterIndexTag,
                replyMsg.getValue(AfterIndexTag));
            exchange01Insert.addArgument(TruckNumberTag,
                replyMsg.getValue(TruckNumberTag));
            new ThreadedClient(exchange01Insert, ipCarrB, portCarrB);
        } else {
            System.out.println("Exchange 01 not performed on Carrier " +
                portCarrA);

            return false;
        }

        return true;
    }

    /**
     * This will perfrorm a 10 optimization accept 1 remove 0 on carriers A
     * @param carrierA the carrier that will probably accepting a shipment
     * @param carrierB the carrier that will probably removing a shipment and send to A
     * @return boolean if exchange was performed or not
     */
    private boolean myGreatOpt10(Message carrierA, Message carrierB) {
        return myGreatOpt01(carrierB, carrierA); //flip order of parameters and the 01 opts becomes 10opts
    }

    /**
     * This method performs a 02 opts on carrier A. accept 0 remove 2
     * @param carrierA carrier to probably remove
     * @param carrierB carrier to probably insert ships removed frrom carrierA
     * @return boolean staus. true if exchange took place
     */
    private boolean myGreatOpt02(Message carrierA, Message carrierB) {
        //model this after opts 01. try and follow good pattern
        //test find 2 savings mathod
        String ipCarrA;
        int portCarrA;
        String ipCarrB;
        int portCarrB;
        float insertCost = 0;
        float savingsCost = 0;
        Message savingsMsg;
        Message replyMsg;
        Message bestSaveTypeMsg = new Message();
        bestSaveTypeMsg.setMessageType(CalcBestTwoSavingsTag);

        //parse messages and obtain info necessary of carrier might be removing 2 ships but not accpetion
        ipCarrA = carrierA.getValue(MessageTags.IPAddressTag);
        portCarrA = Integer.parseInt(carrierA.getValue(
                    MessageTags.PortNumberTag));

        //parse messages and obtain info necessary of carrier that will not remove but might be accepting
        ipCarrB = carrierB.getValue(IPAddressTag);
        portCarrB = Integer.parseInt(carrierB.getValue(
                    MessageTags.PortNumberTag));

        //getBestSavings from carrierA
        savingsMsg = getBestSavings(bestSaveTypeMsg, ipCarrA, portCarrA);

        //this means carrier is empty DO NOT CONTINUE
        if (savingsMsg.getValue(FeasibilityTag).equals("0")) {
            System.out.println("Carrier " + portCarrA +
                " CANNOT remove any pairs of shipments. Exchange not possible");

            return false; //do not continue simply jump out
        }

        savingsMsg.addArgument(ExchangeTypeTag, "02");

        //getInsertion acceptance of shipment from carrA into carrB
        replyMsg = getOptInsertCost(savingsMsg, ipCarrB, portCarrB);

        if (replyMsg.getMessageType().equals(AcceptTag)) {
            System.out.println(
                "Exchange 02 Feasible...should be performing exchange 02 on " +
                portCarrA);

            Message exchange02Remove = new Message();
            exchange02Remove.setMessageType(ExchangeTag);
            exchange02Remove.addArgument(ExchangeTypeTag, "02A");
            exchange02Remove.addArgument(savingsMsg);

            //remove ships fom carA
            new ThreadedClient(exchange02Remove, ipCarrA, portCarrA);

            //insertships in carrB
            Message exchange02Insert = new Message();
            exchange02Insert.setMessageType(ExchangeTag);
            exchange02Insert.addArgument(ExchangeTypeTag, "02B");
            exchange02Insert.addArgument(savingsMsg);
            exchange02Insert.addArgument(AfterIndexTag,
                replyMsg.getValue(AfterIndexTag, 1));
            exchange02Insert.addArgument(AfterIndexTag,
                replyMsg.getValue(AfterIndexTag, 2));
            exchange02Insert.addArgument(TruckNumberTag,
                replyMsg.getValue(TruckNumberTag, 1));
            exchange02Insert.addArgument(TruckNumberTag,
                replyMsg.getValue(TruckNumberTag, 2));
            new ThreadedClient(exchange02Insert, ipCarrB, portCarrB);
        } else {
            System.out.println("Exchange 02 not performed on Carrier " +
                portCarrA);

            return false;
        }

        return true;
    }

    /**
     * This method performs a 20 opts on carrier A. accept 2 remove 0
     * @param carrierA carrier to probably accept 2 removed from B
     * @param carrierB carrier to probably remove 2 ship
     * @return boolean staus. true if exchange took place
     */
    private boolean myGreatOpt20(Message carrierA, Message carrierB) {
        return myGreatOpt02(carrierB, carrierA);
    }

    /**
     * this will perform a 11 excahnge between the carriers. accept 1 remove 1
     * @param carrierA the carrier probably accept 1 and probably remove 1
     * @param carrierB the second carrier probably accept 1 and probably remove 1
     * @return return the
     */
    private boolean myGreatOpt11(Message carrierA, Message carrierB) {
        String ipCarrA;
        int portCarrA;
        String ipCarrB;
        int portCarrB;
        Message savingsMsgCarrA;
        Message savingsMsgCarrB;
        Message replyMsgCarrA;
        Message replyMsgCarrB;
        Message msgA = new Message();
        Message msgB = new Message();
        Message bestSaveTypeMsg = new Message();
        bestSaveTypeMsg.setMessageType(CalcBestSavingsTag);

        //parse messages and obtain info necessary of carrier might be removing but not accpetion
        ipCarrA = carrierA.getValue(MessageTags.IPAddressTag);
        portCarrA = Integer.parseInt(carrierA.getValue(
                    MessageTags.PortNumberTag));

        //parse messages and obtain info necessary of carrier that will not remove but might be accepting
        ipCarrB = carrierB.getValue(IPAddressTag);
        portCarrB = Integer.parseInt(carrierB.getValue(
                    MessageTags.PortNumberTag));

        //getBestSavings from carrierA and carrierB
        savingsMsgCarrA = getBestSavings(bestSaveTypeMsg, ipCarrA, portCarrA);
        savingsMsgCarrB = getBestSavings(bestSaveTypeMsg, ipCarrB, portCarrB);

        //if any of both carriers is empty exit
        if (savingsMsgCarrA.getValue(FeasibilityTag).equals("0") ||
                savingsMsgCarrB.getValue(FeasibilityTag).equals("0")) {
            System.out.println("Carrier " + portCarrA + " or Carrier " +
                portCarrB + " IS EMPTY. Exchange not possible");

            return false; //do not continue simply jump out
        }

        msgA.setMessageType(CalculateExchangeTag);
        msgA.addArgument(ExchangeTypeTag, "11");

        msgB.setMessageType(CalculateExchangeTag);
        msgB.addArgument(ExchangeTypeTag, "11");

        //bundle up messages.
        msgA.addArgument(savingsMsgCarrB); //message with shipment to probably accpet
        msgA.addArgument(savingsMsgCarrA); //message with shipment to probably remove

        msgB.addArgument(savingsMsgCarrA); //message with shipment to probably accpet
        msgB.addArgument(savingsMsgCarrB); //message with shipment to probably remove

        replyMsgCarrA = getOptInsertCost(msgA, ipCarrA, portCarrA);
        replyMsgCarrB = getOptInsertCost(msgB, ipCarrB, portCarrB);

        if (replyMsgCarrA.getMessageType().equals(AcceptTag) &&
                replyMsgCarrB.getMessageType().equals(AcceptTag)) { //if both carriers are gaining
            System.out.println(
                "Exchange 11 Feasible...should be performing exchange 11 on " +
                portCarrA);

            //update msg data
            msgA.setMessageType(ExchangeTag); //udpdate msg Type
            msgB.setMessageType(ExchangeTag);

            msgA.addArgument(AfterIndexTag,
                replyMsgCarrA.getValue(AfterIndexTag));
            msgA.addArgument(TruckNumberTag,
                replyMsgCarrA.getValue(TruckNumberTag));
            msgB.addArgument(AfterIndexTag,
                replyMsgCarrB.getValue(AfterIndexTag));
            msgB.addArgument(TruckNumberTag,
                replyMsgCarrB.getValue(TruckNumberTag));

            //send mesage to perform exchange11
            new ThreadedClient(msgA, ipCarrA, portCarrA);
            new ThreadedClient(msgB, ipCarrB, portCarrB);
        } else {
            System.out.println("Exchange 11 NOT feasable");

            return false;
        }

        //if everything went right
        return true;
    }

    /**
     * This method performs a 12 opts on carrier A. accept 1 remove 2
     * @param carrierA carrier to probably accept 1 and remove 2
     * @param carrierB carrier to probably insert ships removed frrom carrierA
     * @return boolean staus. true if exchange took place
     */
    private boolean myGreatOpt12(Message carrierA, Message carrierB) {
        String ipCarrA;
        int portCarrA;
        String ipCarrB;
        int portCarrB;
        Message savingsMsgCarrA;
        Message savingsMsgCarrB;
        Message replyMsgCarrA;
        Message replyMsgCarrB;
        Message msgA = new Message();
        Message msgB = new Message();
        Message bestSaveTypeMsgCarrA = new Message(); //for 2 to remove from carA
        bestSaveTypeMsgCarrA.setMessageType(CalcBestTwoSavingsTag);

        Message bestSaveTypeMsgCarrB = new Message(); //for 1 to remove from carB
        bestSaveTypeMsgCarrB.setMessageType(CalcBestSavingsTag);

        //parse messages and obtain info necessary of carrier might be removing but not accpetion
        ipCarrA = carrierA.getValue(MessageTags.IPAddressTag);
        portCarrA = Integer.parseInt(carrierA.getValue(
                    MessageTags.PortNumberTag));

        //parse messages and obtain info necessary of carrier that will not remove but might be accepting
        ipCarrB = carrierB.getValue(IPAddressTag);
        portCarrB = Integer.parseInt(carrierB.getValue(
                    MessageTags.PortNumberTag));

        //get savings of removing 2 on carrA
        savingsMsgCarrA = getBestSavings(bestSaveTypeMsgCarrA, ipCarrA,
                portCarrA);

        //get savings of removing 1 on carrB
        savingsMsgCarrB = getBestSavings(bestSaveTypeMsgCarrB, ipCarrB,
                portCarrB);

        //any of these means carrier is empty DO NOT CONTINUE
        if (savingsMsgCarrA.getValue(FeasibilityTag).equals("0")) {
            System.out.println("Carrier " + portCarrA +
                " IS EMPTY or CANNOT remove any pair of shipments.");

            return false; //do not continue simply jump out
        }

        if (savingsMsgCarrB.getValue(FeasibilityTag).equals("0")) {
            System.out.println("Carrier " + portCarrB +
                " IS EMPTY. Exchange not possible");

            return false; //do not continue simply jump out
        }

        msgA.setMessageType(CalculateExchangeTag);
        msgB.setMessageType(CalculateExchangeTag);

        msgA.addArgument(ExchangeTypeTag, "12A");
        msgB.addArgument(ExchangeTypeTag, "12B");

        //bundle up messages.
        msgA.addArgument(savingsMsgCarrB); //message with 1 shipment to probably accpet
        msgA.addArgument(savingsMsgCarrA); //message with 2 shipment to probably remove

        msgB.addArgument(savingsMsgCarrA); //message with 2 shipment to probably accpet
        msgB.addArgument(savingsMsgCarrB); //message with 1 shipment to probably remove

        replyMsgCarrA = getOptInsertCost(msgA, ipCarrA, portCarrA); //has cost,afterindex
        replyMsgCarrB = getOptInsertCost(msgB, ipCarrB, portCarrB); //has cost aftindex1,aftindex2

        if (replyMsgCarrA.getMessageType().equals(AcceptTag) &&
                replyMsgCarrB.getMessageType().equals(AcceptTag)) { //if both carriers are gaining{ //if both carriers are gaining
            System.out.println(
                "Exchange 01 Feasible...should be performing exchange 01 on " +
                portCarrA);

            //update msg info
            msgA.setMessageType(ExchangeTag); //udpdate msg Type
            msgB.setMessageType(ExchangeTag);

            msgA.addArgument(AfterIndexTag,
                replyMsgCarrA.getValue(AfterIndexTag));
            msgA.addArgument(TruckNumberTag,
                replyMsgCarrA.getValue(TruckNumberTag));

            msgB.addArgument(AfterIndexTag,
                replyMsgCarrB.getValue(AfterIndexTag, 1));
            msgB.addArgument(AfterIndexTag,
                replyMsgCarrB.getValue(AfterIndexTag, 2));
            msgB.addArgument(TruckNumberTag,
                replyMsgCarrB.getValue(TruckNumberTag, 1));
            msgB.addArgument(TruckNumberTag,
                replyMsgCarrB.getValue(TruckNumberTag, 2));

            //send mesage to perform exchange12
            new ThreadedClient(msgA, ipCarrA, portCarrA);
            new ThreadedClient(msgB, ipCarrB, portCarrB);
        } else {
            System.out.println("Exchange 12 NOT feasable");

            return false;
        }

        return true;
    }

    /**
     * This method performs a 21 opts on carrier A. accept 2 remove 1
     * @param carrierA carrier to probably remove 1
     * @param carrierB carrier to probably insert ships removed frrom carrierA
     * @return boolean staus. true if exchange took place
     */
    private boolean myGreatOpt21(Message carrierA, Message carrierB) {
        return myGreatOpt12(carrierB, carrierA);
    }

    /**
     * This method performs a 22 opts on carrier A. accept 2 remove 2
     * @param carrierA carrier to probably remove 2 and accept 2
     * @param carrierB carrier to probably insert ships removed frrom carrierA
     * @return boolean staus. true if exchange took place
     */
    private boolean myGreatOpt22(Message carrierA, Message carrierB) {
        String ipCarrA;
        int portCarrA;
        String ipCarrB;
        int portCarrB;
        Message savingsMsgCarrA;
        Message savingsMsgCarrB;
        Message replyMsgCarrA;
        Message replyMsgCarrB;
        Message msgA = new Message();
        Message msgB = new Message();
        Message bestSaveTypeMsg = new Message();
        bestSaveTypeMsg.setMessageType(CalcBestTwoSavingsTag);

        //parse messages and obtain info necessary of carrier might be removing but not accpetion
        ipCarrA = carrierA.getValue(MessageTags.IPAddressTag);
        portCarrA = Integer.parseInt(carrierA.getValue(
                    MessageTags.PortNumberTag));

        //parse messages and obtain info necessary of carrier that will not remove but might be accepting
        ipCarrB = carrierB.getValue(IPAddressTag);
        portCarrB = Integer.parseInt(carrierB.getValue(
                    MessageTags.PortNumberTag));

        //getBestSavings from carrierA and carrierB
        savingsMsgCarrA = getBestSavings(bestSaveTypeMsg, ipCarrA, portCarrA);
        savingsMsgCarrB = getBestSavings(bestSaveTypeMsg, ipCarrB, portCarrB);

        //for debug
        System.out.println("The savings received  message from " + portCarrA +
            " is: \n" + savingsMsgCarrA.getMessageString());

        //for debug
        System.out.println("The savings received  message from " + portCarrB +
            " is: \n" + savingsMsgCarrB.getMessageString());

        //if any of both carriers is empty exit or has less than 2 shipments
        if (savingsMsgCarrA.getValue(FeasibilityTag).equals("0") ||
                savingsMsgCarrB.getValue(FeasibilityTag).equals("0")) {
            System.out.println("Carrier " + portCarrA + " or Carrier " +
                portCarrB +
                " IS EMPTY or CANNOT remove a pair of shipments. Exchange not possible");

            return false; //do not continue simply jump out
        }

        msgA.setMessageType(CalculateExchangeTag);
        msgB.setMessageType(CalculateExchangeTag);

        msgA.addArgument(ExchangeTypeTag, "22");
        msgB.addArgument(ExchangeTypeTag, "22");

        //bundle up messages.
        msgA.addArgument(savingsMsgCarrB); //message with 2 shipment to probably accpet
        msgA.addArgument(savingsMsgCarrA); //message with 2 shipment to probably remove

        msgB.addArgument(savingsMsgCarrA); //message with 2  shipment to probably accpet
        msgB.addArgument(savingsMsgCarrB); //message with 2 shipment to probably remove

        //get insertion decisions
        replyMsgCarrA = getOptInsertCost(msgA, ipCarrA, portCarrA);
        replyMsgCarrB = getOptInsertCost(msgB, ipCarrB, portCarrB);

        if (replyMsgCarrA.getMessageType().equals(AcceptTag) &&
                replyMsgCarrB.getMessageType().equals(AcceptTag)) {
            //do necessary exchanges
            System.out.println(
                "Exchange 22 Feasible...should be performing exchange 22 on Carrier " +
                portCarrA);

            //update msg data
            msgA.setMessageType(ExchangeTag); //udpdate msg Type
            msgB.setMessageType(ExchangeTag);

            msgA.addArgument(AfterIndexTag,
                replyMsgCarrA.getValue(AfterIndexTag, 1));
            msgA.addArgument(AfterIndexTag,
                replyMsgCarrA.getValue(AfterIndexTag, 2));
            msgA.addArgument(TruckNumberTag,
                replyMsgCarrA.getValue(TruckNumberTag, 1));
            msgA.addArgument(TruckNumberTag,
                replyMsgCarrA.getValue(TruckNumberTag, 2));

            msgB.addArgument(AfterIndexTag,
                replyMsgCarrB.getValue(AfterIndexTag, 1));
            msgB.addArgument(AfterIndexTag,
                replyMsgCarrB.getValue(AfterIndexTag, 2));
            msgB.addArgument(TruckNumberTag,
                replyMsgCarrB.getValue(TruckNumberTag, 1));
            msgB.addArgument(TruckNumberTag,
                replyMsgCarrB.getValue(TruckNumberTag, 2));

            //send mesage to perform exchange22
            new ThreadedClient(msgA, ipCarrA, portCarrA);
            new ThreadedClient(msgB, ipCarrB, portCarrB);
        } else {
            System.out.println("Exchange 22 NOT feasable");

            return false;
        }

        return true;
    }

    /**
     * this method will be used to get the cost of a shipement during optmization. MIGHT just use insertCostCar
     * @param m message containing shipment to get insertcost fee for. It also has address of insert carrier
     * @param ip ip address of carrier to probably get insert
     * @param port port number of carrier to probably get insert
     * @return msg containing insertion cost
     */
    private synchronized Message getOptInsertCost(Message m, String ip, int port) {
        Message response;

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in;

        try {
            socket = new Socket(ip, port);
        } catch (IOException ex) {
            System.err.println("Error creating socket ");
        }

        Message Msg = new Message();
        Message responseMsg;
        Msg.setMessageType(OptimizationInsertCostTag);
        Msg.addArgument(m); //bundle shipment info passed
        Msg.addArgument(ExchangeTypeTag, m.getValue(ExchangeTypeTag)); //get exchage type number

        try {
            out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true); //used to send
            in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream())); //used to receive response

            out.println(Msg.getMessageString()); //send message

            response = new Message(in.readLine()); //readfrom socket

            //	    System.out.println("Reply succesful");
            return response;
        } catch (Exception ex) {
            System.err.println(
                "Error while sending/receiving OptsInsertCost message" + ex);

            return null;
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * returns the best savings of a carrier bundled in a message for Global Opts use
     * @param saveTypeMsg tell what type of save calculation to be performed
     * @param carIP ip address of carrier
     * @param carPort port number of carrier
     * @return msg with shipment info
     */
    private synchronized Message getBestSavings(Message saveTypeMsg,
        String carIP, int carPort) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in;

        try {
            socket = new Socket(carIP, carPort);
        } catch (IOException ex) {
            System.err.println("Error creating socket ");
        }

        //use socket to send msg and retrieve response using in/out
        try {
            out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true); //used to send
            in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream())); //used to receive response

            out.println(saveTypeMsg.getMessageString()); //send message

            Message msg = new Message(in.readLine()); //readfrom socket

            //	    System.out.println("Reply succesful");
            System.out.println();

            return msg;
        } catch (Exception ex) {
            System.err.println(
                "Error while sending/receiving getBestSavings message" + ex);

            return null;
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * adds the totals of all carriers to accumulate the grand total of the problem
     * @param msg  total of a carrier to be added to the rest
     */
    private synchronized void getTotals(Message msg) {
        mai.setTextFields(Float.parseFloat(msg.getValue(CapacityTag)),
            Float.parseFloat(msg.getValue(DistanceTag)),
            Float.parseFloat(msg.getValue(TotalTimeTag)),
            Float.parseFloat(msg.getValue(TardinessTag)),
            Float.parseFloat(msg.getValue(ExcessTimeTag)),
            Float.parseFloat(msg.getValue(OverloadTag)),
            Float.parseFloat(msg.getValue(WaitTimeTag)),
            Float.parseFloat(msg.getValue(ServiceTimeTag)),
            Integer.parseInt(msg.getValue(CustomerTag)),
            Integer.parseInt(msg.getValue(TruckNumberTag)));

        if (++mai.updateCount == mai.numCarriers) {
            mai.updating = false;
            mai.updateCount = 0;
        }
    }

    /**
     * Sends a message to the Carrier agents to send their solution info to the
     * Master Server
     */
    private synchronized void getSummary() {
        Message msg = new Message();

        if (!mai.updating) {
            mai.updating = true;
            mai.numCarriers = regShipCarDB.getCarrierIPandPort().length;
            mai.logWriter.writeLog("Getting totals for update.");
            mai.clearTextFields(); // initialize the text fields to zero
            msg.setMessageType(GetSummaryTag);
            broadcastToCarriers(msg);
        }
    }

    /**
     * Register shipper agent with master server
     * @param msg  shipper agent information
     */
    private void registerShipper(Message msg) {
        Message message = new Message();
        Random rNumber = new Random();

        //add shipper info to DB
        //parse message
        String shipperCode = msg.getValue(CodeTag);
        String shipperName = msg.getValue(NameTag);
        Message addressMsg = msg.getMessage(InetAddressTag);
        String shipperIP = addressMsg.getValue(MessageTags.IPAddressTag);

        //randomly generate port no
        int portNo = rNumber.nextInt(HermesGlobals.randomPortBounds) +
            HermesGlobals.randomPortOffset;

        while (portNoUsed(shipperIP, portNo)) { // if port number alread used, generate another
            portNo = rNumber.nextInt(HermesGlobals.randomPortBounds) +
                HermesGlobals.randomPortOffset;
        }

        int shipperPort = portNo;

        mai.logWriter.writeLog("Registering Shipper: " + shipperName + " " +
            shipperCode + " " + shipperIP + ":" + shipperPort);

        //make a new message listener to handle this agent
        java.net.ServerSocket server = null;

        try {
            server = new ServerSocket(0);
        } catch (IOException ex) {
            mai.logWriter.writeLog(
                "Error creating server socket to register shipper: " + ex);
            ex.printStackTrace();
        }

        //add the message listeners to the master agent interface
        mai.messageListeners.add(new MasterMessageListener(server,
                regShipCarDB, mai));

        //rand generate port no
        //put port no into a message
        message.addArgument(PortNumberTag, "" + portNo);
        message.addArgument(MessageTags.MasterPortTag,
            "" + server.getLocalPort());

        //use out to send it back to were it came from
        try {
            PrintWriter out = new PrintWriter(incomingCommunication.getOutputStream(),
                    true);
            out.println(message.getMessageString());

            //add shipper to the database
            regShipCarDB.addShipper(shipperCode, shipperName, shipperIP,
                new Integer(shipperPort).toString());

            //            mai.isChanged = true;
            System.out.println("Registering Shipper @ " + shipperIP + " " +
                shipperPort);
        } catch (IOException ex) {
            mai.logWriter.writeLog("Error returning shipper port number: " +
                ex);
            System.err.println("Error returning shipper port number: " + ex);
        }
    }

    /**
     * Register the carrier with the master server
     * @param msg  carrier info
     */
    private void registerCarrier(Message msg) {
        Message message = new Message();
        Random rNumber = new Random();

        //parse message
        String carrierCode = msg.getValue(CodeTag);
        String carrierName = msg.getValue(NameTag);
        Message addressMsg = msg.getMessage(InetAddressTag);
        String carrierIP = addressMsg.getValue(MessageTags.IPAddressTag);

        //randomly generate port no
        int portNo = rNumber.nextInt(HermesGlobals.randomPortBounds) +
            HermesGlobals.randomPortOffset;

        //        int portNo = 50000;
        // if port number alread used, generate another
        while (portNoUsed(carrierIP, portNo)) {
            portNo = rNumber.nextInt(HermesGlobals.randomPortBounds) +
                HermesGlobals.randomPortOffset;
        }

        int carrierPort = portNo;

        mai.logWriter.writeLog("Registering Shipper: " + carrierName + " " +
            carrierCode + " " + carrierIP + ":" + carrierPort);

        //make a new message listener to handle this agent
        java.net.ServerSocket server = null;

        try {
            server = new ServerSocket(0);
        } catch (IOException ex) {
            mai.logWriter.writeLog(
                "Error creating server socket to register carrier: " + ex);
            ex.printStackTrace();
        }

        //add the message listeners to the master agent interface
        mai.messageListeners.add(new MasterMessageListener(server,
                regShipCarDB, mai));

        //rand generate port no
        //put port no into a message
        message.addArgument(PortNumberTag, "" + portNo);
        message.addArgument(MessageTags.MasterPortTag,
            "" + server.getLocalPort());

        //use out to send it back to were it came from
        try {
            PrintWriter out = new PrintWriter(incomingCommunication.getOutputStream(),
                    true);
            out.println(message.getMessageString());

            //add the carrier to the database
            regShipCarDB.addCarrier(carrierCode, carrierName, carrierIP,
                new Integer(carrierPort).toString());
            mai.numCarriers++;
            mai.isChanged = true;
            System.out.println("Registering Carrier @ " + carrierIP + " " +
                carrierPort);
        } catch (IOException ex) {
            mai.logWriter.writeLog("Error registering a carrier: " + ex);
            System.err.println("Error registering a carrier: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     * Will remove the shipper from the database
     * @param msg message containing carrier information
     */
    private void removeShipper(Message msg) {
        Message addr = msg.getMessage(InetAddressTag);
        String ip = addr.getValue(IPAddressTag);
        String port = addr.getValue(PortNumberTag);
        mai.logWriter.writeLog("Removing shipper: " + ip + ":" + port);
        System.out.println("removing shipper");
        regShipCarDB.removeShipper(ip, port);

        //        mai.isChanged = true;
    }

    /**
     * Will remove the carrier from the database
     * @param msg message containing the carrier information
     */
    private void removeCarrier(Message msg) {
        Message addr = msg.getMessage(InetAddressTag);
        String ip = addr.getValue(IPAddressTag);
        String port = addr.getValue(PortNumberTag);
        mai.logWriter.writeLog("Removing carrier: " + ip + ":" + port);
        System.out.println("removing carrier");
        regShipCarDB.removeCarrier(ip, port);
        mai.numCarriers--;
        mai.isChanged = true;
    }

    /**
     * Determines if this port/ip combination is being used.
     * @param ip     ip address to check
     * @param portNo port number to check
     * @return boolean  true - already used, false - not used
     */
    private boolean portNoUsed(String ip, int portNo) {
        //query the register database for all ports on this ip
        int[] usedPorts = regShipCarDB.getUsedPorts(ip);

        //loop through used ports, if the port is being used, return true
        for (int i = 0; i < usedPorts.length; i++) {
            if (usedPorts[i] == portNo) {
                mai.logWriter.writeLog("Port Used: " + portNo);

                return true;
            }
        }

        //port not being used, return false
        return false;
    }

    /**
     * This method sends a customer to the carrier after it has won the bidding
     * process. This will create an Accept message and will send it to the carrier
     * @param customer customer to send
     * @param address address of the carrier
     */
    private void sendCustomerToCarrierToAccept(Message customer, Message address) {
        String ip = null;
        int port = 0;

        try {
            //get the carrier's IP and port
            ip = address.getValue(MessageTags.IPAddressTag);
            port = Integer.parseInt(address.getValue(MessageTags.PortNumberTag));

            //create a message asking the carrier if it wishes to accept this customer
            Message msg = new Message();
            msg.setMessageType(MessageTags.AcceptTag);
            msg.addArgument(MessageTags.DestinationTag,
                MessageTags.CarrierMessage);
            msg.addArgument(customer);

            //send this message using the threaded client
            new ThreadedClient(msg, ip, port);
        } catch (Exception e) {
            mai.logWriter.writeLog("Error sending shipment to carrier: " + ip +
                ":" + port + " for scheduling." + e);
            e.printStackTrace();
        }
    }

    /**
     * Will take the message and send it to all the carriers
     * @param msg message to broadcast
     */
    private void broadcastToCarriers(Message msg) {
        //query the database for the carrier's addresses
        if (mai.isChanged) {
            mai.carrierInfoArray = regShipCarDB.getCarrierIPandPort();
            mai.numCarriers = mai.carrierInfoArray.length;
            mai.isChanged = false;
        }

        mai.logWriter.writeLog("Broadcasting to carriers from handler: " +
            msg.toString());

        //loop through the carriers
        for (int i = 0; i < mai.numCarriers; i++) {
            //parse the message for the ip and port
            String ip = mai.carrierInfoArray[i].getValue(MessageTags.IPAddressTag);
            int port = Integer.parseInt(mai.carrierInfoArray[i].getValue(
                        MessageTags.PortNumberTag));

            try {
                //use a new ThreadedClient to send the message
                new ThreadedClient(msg, ip, port);

                mai.logWriter.writeLog("Broadcasting to carrier: " + port);

                //print to screen
                System.out.println("Broadcasting to carriers");
            } catch (Exception e) {
                mai.logWriter.writeLog("Error broadcasting to carrier " + port +
                    " from handler " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Will take the message and send it to all the shippers
     * @param msg message to broadcast
     */
    private void broadcastToShippers(Message msg) {
        //query the database for the shipper's addresses
        Message[] shipperAddrs = regShipCarDB.getShipperIPandPort();

        mai.logWriter.writeLog("Broadcasting to shippers from handler: " +
            msg.toString());

        //loop through the shipppers
        for (int i = 0; i < shipperAddrs.length; i++) {
            //parse the message for the ip and port
            String ip = shipperAddrs[i].getValue(MessageTags.IPAddressTag);
            int port = Integer.parseInt(shipperAddrs[i].getValue(
                        MessageTags.PortNumberTag));

            try {
                //use a new ThreadedClient to send the message
                mai.logWriter.writeLog("Broadcasting to shipper: " + port);
                new ThreadedClient(msg, ip, port);
            } catch (Exception e) {
                mai.logWriter.writeLog("Error broadcasting shipper " + port +
                    " from handler " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Will send a message to a specific ip and port number
     * @param msg message to send
     * @param addr address to sent it to
     */
    private void relayMessage(Message msg, Message addr) {
        String ip = addr.getValue(MessageTags.IPAddressTag);
        int port = Integer.parseInt(addr.getValue(MessageTags.PortNumberTag));

        mai.logWriter.writeLog("Relaying message to " + ip + " " + port +
            "---->" + msg);

        //System.out.println("---> to " + ip + " " + port + "---->" + msg);
        try {
            //use a new ThreadedClient to send the message
            new ThreadedClient(msg, ip, port);

            //System.out.println(" ...msg relayed");
        } catch (Exception e) {
            mai.logWriter.writeLog("Error relaying message: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Exit application
     */
    public void myExit() {
        regShipCarDB.myExit();
        System.exit(4);
    }
}
