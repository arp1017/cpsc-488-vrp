package Hermes;

import Zeus.*;

import java.io.*;

import java.net.*;


/**
 * <p>Title: CarrierMessageHandler.java </p>
 * <p>Description: This class receives a message over a socket and proceeses
 *    it accordingly. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class CarrierMessageHandler extends Thread implements MessageTags {
    /**
    * socket to be read
    */
    private Socket incomingCommunication;
    private ZeusAdaptor zAdapt;
    private CarrierAgentInterface cai;

    /**
    * constructor reads message from incomming socket thread and processses it accordingdly
    * @param inComm  socket created after connection was made
    * @param zeus  instance of zeus for this carrier
    * @param cai  instance of CarrierAgentInterface for backwards communication
    */
    public CarrierMessageHandler(Socket inComm, ZeusAdaptor zeus,
        CarrierAgentInterface cai) {
        //assigns the socket
        incomingCommunication = inComm;
        zAdapt = zeus;
        this.cai = cai;

        //start the thread to handle the message
        start();
    }

    /**
    * this run will read and process the messages received over the socket accordingly
    * when thread is start
    */
    public void run() {
        try {
            //create a reader to the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        incomingCommunication.getInputStream()));

            //output writer socket
            PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                            incomingCommunication.getOutputStream())), true);

            //now process the message using socket reader.
            processMessageToCarrier(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * this method, processes the message gotten over a socket accordingly
    * @param in  buffered input stream of socket
    * @param out  output stream of socket
    * @throws IOException
    */
    public void processMessageToCarrier(BufferedReader in, PrintWriter out)
        throws IOException {
        boolean closeSocket = true;

        try {
            Message ackMessage = new Message();
            Message clientMsg;
            Message msg = null;
            String messageType;
            ackMessage.setMessageType(AckTag);
            clientMsg = new Message(in.readLine());
            messageType = clientMsg.getMessageType();

            cai.logWriter.writeLog("Incoming message: " + clientMsg.toString());

            if (messageType.equals(AckTag)) {
            } else if (messageType.equals(BroadcastOneOptTag)) {
                //start running the broadcast one opt
            } else if (messageType.equals(InsertCostTag)) {
                setCostSendPoint(clientMsg);
            } else if (messageType.equals(ConfirmTag)) {
                // remove shipment from list
                int index = Integer.parseInt(clientMsg.getValue(IndexTag));
                cai.shipmentList.setAssigned(index);
                System.out.println("Confirm Shipment " + index);
            } else if (messageType.equals(RefuseTag)) {
                System.out.println("Refuse Tag");
                rebroadcastShipment(clientMsg);
            } else if (messageType.equals(StartBroadcastTag)) {
                out.println(ackMessage.getMessageString());
            } else if (messageType.equals(EndBroadcastTag)) {
                out.println(ackMessage.getMessageString());
            } else if (messageType.equals(CalculateTag)) {
                System.out.println("Calculate Tag");
                calculateBidCost(clientMsg);
            } else if (messageType.equals(AcceptTag)) {
                System.out.println("Accept Tag");
                insertCustomerToRoute(clientMsg);
            } else if (messageType.equals(TerminateTag)) {
                cai.logWriter.writeLog("Terminating agent by Master Server.");
                System.out.println("Terminating Carrier Agent.");
                System.exit(0);
            } else if (messageType.equals(EnableTag)) {
                cai.logWriter.writeLog(
                    "Enabling buttons and sending totals to Master Server.");

                //enable all buttons on carrier
                cai.EnableCarrierJButtons();
                sendTotals();
            } else if (messageType.equals(DetailSolutionTag)) {
                cai.logWriter.writeLog("Printing Detailed solution.");
                zAdapt.detailedSolution(clientMsg.getValue(FileNameTag));
            } else if (messageType.equals(ShortSolutionTag)) {
                cai.logWriter.writeLog("Printing Short Solution.");
                zAdapt.shortSolution(clientMsg.getValue(FileNameTag));
            } else if (messageType.equals(GetSummaryTag)) {
                sendTotals();
            } else if (messageType.equals(LocalOptTag)) {
                cai.isLock = true;
                runLocalOpts();
            } else if (messageType.equals(CalcBestSavingsTag)) {
                //                System.out.println(
                //                    "Received the CalcBestSavings message...Getting best savings");
                Message bestSave;

                if (!(zAdapt.isEmpty())) {
                    bestSave = zAdapt.findBestSavings(); //find bestSave and pass to message

                    Message addr = new Message(); //prepare carrier addres message
                    addr.setMessageType(InetAddressTag);
                    addr.addArgument(IPAddressTag, cai.getIP());
                    addr.addArgument(PortNumberTag,
                        Integer.toString(cai.getPort()));

                    bestSave.addArgument(addr); //add address mesage to best save

                    out.println(bestSave.getMessageString()); //send message through incoming port
                } else {
                    bestSave = new Message();
                    bestSave.addArgument(FeasibilityTag, "0");

                    Message addr = new Message(); //prepare carrier addres message
                    addr.setMessageType(InetAddressTag);
                    addr.addArgument(IPAddressTag, cai.getIP());
                    addr.addArgument(PortNumberTag,
                        Integer.toString(cai.getPort()));
                    out.println(bestSave.getMessageString()); //send message through incoming port
                }

                System.out.println("Reply Sent");
            } else if (messageType.equals(CalcBestTwoSavingsTag)) {
                //                System.out.println(
                //                    "Received the CalcBestTwoSavings message...Getting best savings");
                Message bestSave;

                if (zAdapt.getSize() >= 4) { //because depot also counted
                    bestSave = zAdapt.findTwoBestSavings(); //find 2 bestSave and pass to message

                    Message addr = new Message(); //prepare carrier address message
                    addr.setMessageType(InetAddressTag);
                    addr.addArgument(IPAddressTag, cai.getIP());
                    addr.addArgument(PortNumberTag,
                        Integer.toString(cai.getPort()));

                    bestSave.addArgument(addr); //add address mesage to best save

                    out.println(bestSave.getMessageString()); //send responsemessage through incoming port
                } else {
                    System.out.println(
                        "Less than TWO shipments on the Schedule CANNOT get best 2 Savings.");
                    bestSave = new Message();
                    bestSave.addArgument(FeasibilityTag, "0");
                    out.println(bestSave.getMessageString());
                }
            } else if (messageType.equals(OptimizationInsertCostTag)) {
                //                System.out.println(
                //                    "Received the OptimizationInsertCost message...Getting insert cost");
                Message InsertCostMsg;
                Message decisionMsg = new Message();
                double savingsCost;
                double insertCost;

                if (clientMsg.getValue(ExchangeTypeTag).equals("01")) {
                    //                    System.out.println(
                    //                        "meassage  inside OptInsertionCost received is \n" +
                    //                        clientMsg.getMessageString()); //DEBUG
                    Message shipInfo = clientMsg.getMessage(BestSavingsTag); //retrieve msg with shipment info
                    savingsCost = Double.parseDouble(shipInfo.getValue(
                                PointCellSavingsTag));

                    InsertCostMsg = zAdapt.calcExchange10(shipInfo); //calculate the cost of inserting ship into this car

                    //calcExchnge10 does this perfectly
                    insertCost = Double.parseDouble(InsertCostMsg.getValue(
                                CostTag));

                    if ((insertCost < savingsCost) &&
                            InsertCostMsg.getValue(FeasibilityTag).equals("1")) {
                        decisionMsg.setMessageType(AcceptTag);
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag));
                    } else {
                        decisionMsg.setMessageType(RefuseTag);
                    }

                    out.println(decisionMsg.getMessageString()); //send message through incoming port
                    System.out.println("InsertCost Reply Sent");
                } else if (clientMsg.getValue(ExchangeTypeTag).equals("11")) {
                    //System.out.println("meassage  received is \n"+clientMsg.getMessageString());//DEBUG
                    Message CaclExcMsg = clientMsg.getMessage(CalculateExchangeTag); //retrive calcExcMsg transition message

                    Message ShipToAccept = CaclExcMsg.getMessage(BestSavingsTag,
                            1);
                    savingsCost = Double.parseDouble(ShipToAccept.getValue(
                                PointCellSavingsTag));

                    //System.out.println("ship to accept is \n"+ShipToAccept.getMessageString());//DEBUG
                    Message ShipToRemove = CaclExcMsg.getMessage(BestSavingsTag,
                            2);

                    //System.out.println("ship to remove is \n"+ShipToRemove.getMessageString());//DEBUG
                    InsertCostMsg = zAdapt.calcExchange11(ShipToAccept,
                            ShipToRemove);
                    insertCost = Double.parseDouble(InsertCostMsg.getValue(
                                CostTag));

                    if ((insertCost < savingsCost) &&
                            InsertCostMsg.getValue(FeasibilityTag).equals("1")) {
                        decisionMsg.setMessageType(AcceptTag);
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag));
                    } else {
                        decisionMsg.setMessageType(RefuseTag);
                    }

                    out.println(decisionMsg.getMessageString()); //send message through incoming port
                    System.out.println("InsertCost Reply Sent");
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("02"))) {
                    //retrieve messaage with ship infos
                    Message Msg = clientMsg.getMessage(BestTwoSavingsTag);
                    savingsCost = Double.parseDouble(Msg.getValue(
                                TotalSavingsTag));

                    //put each info into a seperate message
                    Message shipInfo1 = Msg.getMessage(BestSavingsTag, 1); //retrieve msg with shipment info
                    Message shipInfo2 = Msg.getMessage(BestSavingsTag, 2); //retrieve msg with shipment info
                    Message response;
                    InsertCostMsg = zAdapt.calcExchange20(shipInfo1, shipInfo2);
                    insertCost = Double.parseDouble(InsertCostMsg.getValue(
                                CostTag));

                    if ((insertCost < savingsCost) &&
                            InsertCostMsg.getValue(FeasibilityTag).equals("1")) {
                        decisionMsg.setMessageType(AcceptTag);
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag, 1)); //afterindex1 and 2
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag, 2));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag, 1));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag, 2));
                    } else {
                        decisionMsg.setMessageType(RefuseTag);
                    }

                    out.println(decisionMsg.getMessageString()); //send message through incoming port
                    System.out.println("InsertCost Reply Sent");
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("12A"))) { //calc accpet1 remove2

                    //System.out.println("meassage  received is \n"+clientMsg.getMessageString());//DEBUG
                    Message CaclExcMsg = clientMsg.getMessage(CalculateExchangeTag); //retrive calcExcMsg transition message

                    Message ShipToAccept = CaclExcMsg.getMessage(BestSavingsTag);

                    //System.out.println("ship to accept is \n" +
                    //ShipToAccept.getMessageString()); //DEBUG
                    Message ShipToRemoveBundle = CaclExcMsg.getMessage(BestTwoSavingsTag);

                    //System.out.println("shipToremoveBundle to remove is \n" +
                    //ShipToRemoveBundle.getMessageString()); //DEBUG
                    Message ShipToRemove1 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            1);
                    Message ShipToRemove2 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            2);
                    savingsCost = Double.parseDouble(ShipToRemoveBundle.getValue(
                                TotalSavingsTag));

                    InsertCostMsg = zAdapt.calcExchange12(ShipToAccept,
                            ShipToRemove1, ShipToRemove2);

                    insertCost = Double.parseDouble(InsertCostMsg.getValue(
                                CostTag));

                    if ((insertCost < savingsCost) &&
                            InsertCostMsg.getValue(FeasibilityTag).equals("1")) {
                        decisionMsg.setMessageType(AcceptTag);
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag));
                    } else {
                        decisionMsg.setMessageType(RefuseTag);
                    }

                    out.println(decisionMsg.getMessageString()); //send response
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("12B"))) { //calc accpet2 remove1

                    //System.out.println("meassage  received is \n"+clientMsg.getMessageString());//DEBUG
                    Message CaclExcMsg = clientMsg.getMessage(CalculateExchangeTag); //retrive calcExcMsg transition message

                    Message ShipToAcceptBundle = CaclExcMsg.getMessage(BestTwoSavingsTag);

                    Message ShipToAccept1 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            1);
                    Message ShipToAccept2 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            2);

                    Message ShipToRemove = CaclExcMsg.getMessage(BestSavingsTag);
                    savingsCost = Double.parseDouble(ShipToRemove.getValue(
                                PointCellSavingsTag));

                    InsertCostMsg = zAdapt.calcExchange21(ShipToAccept1,
                            ShipToAccept2, ShipToRemove);

                    insertCost = Double.parseDouble(InsertCostMsg.getValue(
                                CostTag));

                    if ((insertCost < savingsCost) &&
                            InsertCostMsg.getValue(FeasibilityTag).equals("1")) {
                        decisionMsg.setMessageType(AcceptTag);
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag, 1)); //afterindex1 and 2
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag, 2));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag, 1)); //truck 1 and 2
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag, 2));
                    } else {
                        decisionMsg.setMessageType(RefuseTag);
                    }

                    out.println(decisionMsg.getMessageString()); //send response
                } else if (clientMsg.getValue(ExchangeTypeTag).equals("22")) {
                    //System.out.println("meassage  received is \n"+clientMsg.getMessageString());//DEBUG
                    Message CalcExcMsg = clientMsg.getMessage(CalculateExchangeTag); //retrive calcExcMsg transition message
                    Message ShipToAcceptBundle = CalcExcMsg.getMessage(BestTwoSavingsTag,
                            1);
                    Message ShipToRemoveBundle = CalcExcMsg.getMessage(BestTwoSavingsTag,
                            2);

                    //retrieve 2 message to be probably accpted
                    Message ShipToAccept1 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            1);
                    Message ShipToAccept2 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            2);

                    //retieve 2 message with ships to be removed
                    Message ShipToRemove1 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            1);
                    Message ShipToRemove2 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            2);
                    savingsCost = Double.parseDouble(ShipToRemoveBundle.getValue(
                                TotalSavingsTag));

                    InsertCostMsg = zAdapt.calcExchange22(ShipToAccept1,
                            ShipToAccept2, ShipToRemove1, ShipToRemove2);
                    insertCost = Double.parseDouble(InsertCostMsg.getValue(
                                CostTag));

                    if ((insertCost < savingsCost) &&
                            InsertCostMsg.getValue(FeasibilityTag).equals("1")) {
                        decisionMsg.setMessageType(AcceptTag);
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag, 1)); //afterindex1 and 2
                        decisionMsg.addArgument(AfterIndexTag,
                            InsertCostMsg.getValue(AfterIndexTag, 2));
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag, 1)); //truck 1 and 2
                        decisionMsg.addArgument(TruckNumberTag,
                            InsertCostMsg.getValue(TruckNumberTag, 2));
                    } else {
                        decisionMsg.setMessageType(RefuseTag);
                    }

                    out.println(decisionMsg.getMessageString()); //send response
                }
            } else if (messageType.equals(ExchangeTag)) {
                if ((clientMsg.getValue(ExchangeTypeTag).equals("01A"))) {
                    Message shipInfo;
                    shipInfo = clientMsg.getMessage(BestSavingsTag); //retrieve msg with shipment info

                    try {
                        zAdapt.exchange01(shipInfo);
                        System.out.println(
                            "Removing ship SUCCESFUL for opts from carrier " +
                            cai.getPort());
                        updateGui();
                    } catch (Exception ex) {
                        System.out.println(
                            "Error REMOVING ship from old local on carrier " +
                            cai.getPort() + " during exchange01");
                    }
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("01B"))) {
                    System.out.println("meassage  received is \n" +
                        clientMsg.getMessageString()); //DEBUG

                    Message shipInfo;
                    shipInfo = clientMsg.getMessage(BestSavingsTag); //retrieve msg with shipment info
                    shipInfo.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag));

                    int kIndex = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag));

                    try { //inserting ship to new locale
                        zAdapt.exchange10(shipInfo, kIndex);
                        System.out.println("Opt insert sucessful into carrier " +
                            cai.getPort());

                        // updateGui();
                    } catch (Exception ex) {
                        System.err.println(
                            "Error INSERTING ship into new local on carrier " +
                            cai.getPort() + " during exchange01");
                        ex.printStackTrace();
                    }
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("02A"))) {
                    Message Msg = clientMsg.getMessage(BestTwoSavingsTag);
                    Message shipInfo1;
                    Message shipInfo2;
                    shipInfo1 = Msg.getMessage(BestSavingsTag, 1); //retrieve msg with shipment info
                    shipInfo2 = Msg.getMessage(BestSavingsTag, 2);

                    try {
                        zAdapt.exchange02(shipInfo1, shipInfo2);

                        //updateGui();
                        System.out.println(
                            "Removing ship SUCCESFUL for opts02 from carrier " +
                            cai.getPort());
                    } catch (Exception ex) {
                        System.err.println(
                            "Error REMOVING ship from old local on carrier " +
                            cai.getPort() + " during exchange01");
                    }
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("02B"))) {
                    Message Msg = clientMsg.getMessage(BestTwoSavingsTag);
                    Message shipInfo1;
                    Message shipInfo2;
                    shipInfo1 = Msg.getMessage(BestSavingsTag, 1); //retrieve msg with shipment info
                    shipInfo2 = Msg.getMessage(BestSavingsTag, 2);
                    shipInfo1.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag, 1));
                    shipInfo2.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag, 2));

                    int afterIndex1 = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag, 1));
                    int afterIndex2 = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag, 2));

                    try {
                        zAdapt.exchange20(shipInfo1, afterIndex1, shipInfo2,
                            afterIndex2);
                        updateGui();
                        System.out.println(
                            "Insert ship SUCCESFUL for opts02 into carrier " +
                            cai.getPort());
                    } catch (Exception ex) {
                        System.err.println(
                            "Error INSERTING ship from old local on carrier " +
                            cai.getPort() + " during exchange01");
                    }
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("11"))) {
                    Message ShipToAccept = clientMsg.getMessage(BestSavingsTag,
                            1);
                    int afterIndex = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag));
                    ShipToAccept.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag));

                    Message ShipToRemove = clientMsg.getMessage(BestSavingsTag,
                            2);
                    zAdapt.exchange11(ShipToAccept, ShipToRemove, afterIndex); //perform exchange11

                    //updateGui();
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("12A"))) { // 1 accpet 2 remove

                    Message ShipToAccept = clientMsg.getMessage(BestSavingsTag);
                    int afterIndex = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag));
                    ShipToAccept.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag));

                    Message ShipToRemoveBundle = clientMsg.getMessage(BestTwoSavingsTag);
                    Message ShipToRemove1 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            1);
                    Message ShipToRemove2 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            2);

                    zAdapt.exchange12(ShipToAccept, afterIndex, ShipToRemove1,
                        ShipToRemove2);
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("12B"))) { //2 accept 1 remove

                    Message ShipToAcceptBundle = clientMsg.getMessage(BestTwoSavingsTag);
                    Message ShipToAccept1 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            1);
                    ShipToAccept1.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag, 1));

                    Message ShipToAccept2 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            2);
                    ShipToAccept2.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag, 2));

                    int afterIndex1 = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag, 1));
                    int afterIndex2 = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag, 2));

                    Message ShipToRemove = clientMsg.getMessage(BestSavingsTag);

                    zAdapt.exchange21(ShipToAccept1, afterIndex1,
                        ShipToAccept2, afterIndex2, ShipToRemove);

                    //updateGui();
                } else if ((clientMsg.getValue(ExchangeTypeTag).equals("22"))) {
                    Message ShipToAcceptBundle = clientMsg.getMessage(BestTwoSavingsTag,
                            1);
                    Message ShipToRemoveBundle = clientMsg.getMessage(BestTwoSavingsTag,
                            2);
                    int afterIndex1 = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag, 1));
                    int afterIndex2 = Integer.parseInt(clientMsg.getValue(
                                AfterIndexTag, 2));

                    //retrieve 2 ships to be inserted accpted
                    Message ShipToAccept1 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            1);
                    ShipToAccept1.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag, 1));

                    Message ShipToAccept2 = ShipToAcceptBundle.getMessage(BestSavingsTag,
                            2);
                    ShipToAccept2.addArgument(TruckNumberTag,
                        clientMsg.getValue(TruckNumberTag, 2));

                    //retieve 2 message with ships to be removed
                    Message ShipToRemove1 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            1);
                    Message ShipToRemove2 = ShipToRemoveBundle.getMessage(BestSavingsTag,
                            2);

                    //perform exchange
                    zAdapt.exchange22(ShipToAccept1, afterIndex1,
                        ShipToAccept2, afterIndex2, ShipToRemove1, ShipToRemove2);
                }

                updateGui();
            }

            msg = new Message();
            msg.setMessageType(AckTag);
            out.println(msg.getMessageString());
        } catch (Exception e) {
            cai.logWriter.writeLog("Error parsing message: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (closeSocket) {
                    in.close();
                }
            } catch (IOException e) {
                cai.logWriter.writeLog(
                    "Error closing socket input stream for message processing: " +
                    e);
            }

            if (closeSocket) {
                out.close();
            }
        }
    }

    /**
    * Update the interface with current values
    */
    private synchronized void updateGui() {
        zAdapt.calcCurrentValues(); // be sure the values being set are the current values

        //update text fields in gui
        cai.setTextFields(zAdapt.calculateCapacity(),
            zAdapt.calculateDistTrav(), zAdapt.calculateTotalTime(),
            zAdapt.calcTotalTardiness(), zAdapt.calcTotalExcessTime(),
            zAdapt.calcTotalOverload(), zAdapt.calcTotalWaitTime(),
            zAdapt.calcTotalServiceTime(),
            (zAdapt.getSize() - (2 * zAdapt.getNumTrucks())), // subtract the depot nodes
            zAdapt.getNumTrucks());
    }

    /**
    * Update the Master Server interface with current values
    */
    private synchronized void updateMaster() {
        System.out.println("Updating Master Server GUI");

        Message summaryMsg = new Message();

        summaryMsg.setMessageType(GetSummaryTag);

        // send a message to the master server telling it to update its values
        new ThreadedClient(summaryMsg, HermesGlobals.masterServerIP,
            cai.myMasterPort);
    }

    /**
    * sends the summation of each value to the Master Server;
    * values such as demand, travel time, distance, wait time...
    */
    private synchronized void sendTotals() {
        Message msg = new Message();

        msg.setMessageType(SummaryTag);
        msg.addArgument(CapacityTag, "" + zAdapt.calculateCapacity());
        msg.addArgument(DistanceTag, "" + zAdapt.calculateDistTrav());
        msg.addArgument(TotalTimeTag, "" + zAdapt.calculateTotalTime());
        msg.addArgument(TardinessTag, "" + zAdapt.calcTotalTardiness());
        msg.addArgument(ExcessTimeTag, "" + zAdapt.calcTotalExcessTime());
        msg.addArgument(OverloadTag, "" + zAdapt.calcTotalOverload());
        msg.addArgument(WaitTimeTag, "" + zAdapt.calcTotalWaitTime());
        msg.addArgument(ServiceTimeTag, "" + zAdapt.calcTotalServiceTime());
        msg.addArgument(CustomerTag,
            "" + (zAdapt.getSize() - (2 * zAdapt.getNumTrucks())));
        msg.addArgument(TruckNumberTag, "" + zAdapt.getNumTrucks());

        //send message
        new ThreadedClient(msg, HermesGlobals.masterServerIP, cai.myMasterPort);
    }

    /**
    * sends a tag about the status of the accepted shipment, either confirm
    * or reject
    * @param msg  status message
    */
    public void sendStatusToShipperViaMaster(Message msg) {
        new ThreadedClient(msg, HermesGlobals.masterServerIP, cai.myMasterPort); //send messge
    }

    /**
     * Calculates the bid cost of a shipment
     * @param clientMsg message containing the shipment to bid upon
     */
    public void calculateBidCost(Message clientMsg) {
        try {
            Message costMsg;
            Message addr = clientMsg.getMessage(MessageTags.InetAddressTag);

            //ZEUS & ZEUS_ADAPTOR FONCTIONALITIES WILL BE HERE
            int iIndex = Integer.parseInt(clientMsg.getValue(IndexTag));
            float lX = Float.parseFloat(clientMsg.getValue(XCoordTag));
            float lY = Float.parseFloat(clientMsg.getValue(YCoordTag));
            float lDemand = Float.parseFloat(clientMsg.getValue(DemandTag));
            float lEar = Float.parseFloat(clientMsg.getValue(EarlyTimeTag));
            float lLat = Float.parseFloat(clientMsg.getValue(LateTimeTag));
            float lServ = Float.parseFloat(clientMsg.getValue(ServiceTimeTag));
            costMsg = zAdapt.insertCostCar(iIndex, lX, lY, lDemand, lEar, lLat,
                    lServ);

            cai.logWriter.writeLog("Cost for " + iIndex + ": " +
                costMsg.toString());

            //make a message for the address of this carrier
            Message carrierAddress = new Message();
            carrierAddress.setMessageType(MessageTags.InetAddressTag);
            carrierAddress.addArgument(MessageTags.IPAddressTag, cai.getIP());
            carrierAddress.addArgument(MessageTags.PortNumberTag,
                "" + cai.getPort());

            //add the carrier's address to the cost message
            costMsg.addArgument(carrierAddress);

            //wrap the cost message to be forwarded to the shipper
            Message msg = new Message();
            msg.setMessageType(MessageTags.RelayTag);
            msg.addArgument(MessageTags.PackagedMessageTag, InsertCostTag);
            addr.setMessageType(RelayAddressTag);
            msg.addArgument(addr);
            msg.addArgument(costMsg);

            //System.out.println("Sending cost tag to Zeus " + msg);
            new ThreadedClient(msg, HermesGlobals.masterServerIP,
                cai.myMasterPort); //send messge
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Inserts a customer into the route. Will send back a message that will
     * either confirm or reject the customer.
     * @param clientMsg customer to insert.
     */
    private void insertCustomerToRoute(Message clientMsg) {
        try {
            Message status = new Message();
            Message addr = clientMsg.getMessage(MessageTags.InetAddressTag);

            // INSERT CUSTOMER INTO SCHEDULE
            int iIndex = Integer.parseInt(clientMsg.getValue(IndexTag));
            float lX = Float.parseFloat(clientMsg.getValue(XCoordTag));
            float lY = Float.parseFloat(clientMsg.getValue(YCoordTag));
            float lDemand = Float.parseFloat(clientMsg.getValue(DemandTag));
            float lEar = Float.parseFloat(clientMsg.getValue(EarlyTimeTag));
            float lLat = Float.parseFloat(clientMsg.getValue(LateTimeTag));
            float lServ = Float.parseFloat(clientMsg.getValue(ServiceTimeTag));
            status = zAdapt.insert(iIndex, lX, lY, lDemand, lEar, lLat, lServ);

            cai.logWriter.writeLog("Accepting " + iIndex + ": Status: " +
                status);

            //wrap the status message to be forwarded to the shipper
            Message msg = new Message();
            msg.setMessageType(MessageTags.RelayTag);
            msg.addArgument(MessageTags.PackagedMessageTag,
                status.getMessageType());
            addr.setMessageType(RelayAddressTag);
            msg.addArgument(addr);
            msg.addArgument(status);

            //System.out.println(msg);
            sendStatusToShipperViaMaster(msg);

            updateGui();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
    * This function takes a bid message and compares it to the current best
    * bid found in the ShipmentLinkedList data structure. If this bid is better than
    * the currently saved one, it will be saved. If a bid is received from all
    * the carriers, the carrier with the best bid will be offered to the
    * winning carrier.
    * @param costMessage message containing the cost
    * @return an Ack message if there are still outstanding bids. If all bids
    * have been recorded, the message will be an Accept message.
    */
    private Message setCostSendPoint(Message costMessage) {
        //declaring message to send back
        Message ret = null;

        try {
            //get the address of the sender
            Message addressMessage = costMessage.getMessage(InetAddressTag);

            //pull out the attributes of the customer
            int after = Integer.parseInt(costMessage.getValue(AfterIndexTag));
            int index = Integer.parseInt(costMessage.getValue(IndexTag));
            float cost = Float.parseFloat(costMessage.getValue(CostTag));
            int numCar = Integer.parseInt(costMessage.getValue(
                        MessageTags.CarrierCountTag));

            //get the IP and Port of who send the message
            String cName = addressMessage.getValue(IPAddressTag);
            String cPort = addressMessage.getValue(PortNumberTag);
            String shipperCode = "";

            if (!(cai.shipmentList.carrierSched(numCar, cName,
                        Integer.parseInt(cPort)))) {
                cai.logWriter.writeLog("Random cost generated for " + cName +
                    ":" + cPort + "; cost before: " + cost);
                cost += (Math.random() * .0001);
                cai.logWriter.writeLog("Random cost generated for " + cName +
                    ":" + cPort + "; cost after: " + cost);
            }

            cai.logWriter.writeLog("Bid for customer  " + index + " by " +
                cName + ":" + cPort + " is $" + cost);
            System.out.println("Bid for customer  " + index + " by " + cName +
                ":" + cPort + " is $" + cost);

            //update the cost for this shipment, will return false when all
            //carriers have made their bids
            boolean status = cai.shipmentList.setShipmentCost(index, cost,
                    cName, Integer.parseInt(cPort), numCar);

            //once all carriers have made their bids, offer to the lowest one
            if (status == true) {
                sendShipmentToBeAccepted(index);
            }
        } catch (Exception e1) {
            cai.logWriter.writeLog("Error analyzing bid for shipment: " + e1);
            e1.printStackTrace();
        }

        //if the return message has not been allocated yet (the shipment is still
        //waiting for bids) just return an ack tag
        if (ret == null) {
            ret = new Message();
            ret.setMessageType(AckTag);
        }

        return ret;
    }

    /**
    * Once a message has been rejected, the shipper will clear the cost info
    * out of the shipment and send the shipment again
    * @param msg reject message
    */
    private void rebroadcastShipment(Message msg) {
        //reset the bid info of the rejected shipment
        int index = Integer.parseInt(msg.getValue(IndexTag));
        cai.shipmentList.resetShipment(index);

        cai.logWriter.writeLog("Shipment " + index +
            " is being rebroadcasted.");

        //create the message containting the shipment information
        Shipment ship = cai.shipmentList.getShipment(index);
        Message shipMsg = new Message();
        shipMsg.setMessageType(CalculateTag);
        shipMsg.addArgument(IndexTag, "" + ship.getShipNo());
        shipMsg.addArgument(XCoordTag, "" + ship.getX());
        shipMsg.addArgument(YCoordTag, "" + ship.getY());
        shipMsg.addArgument(DemandTag, "" + ship.getDemand());
        shipMsg.addArgument(EarlyTimeTag, "" + ship.getEarliestTime());
        shipMsg.addArgument(LateTimeTag, "" + ship.getLatestTime());
        shipMsg.addArgument(ServiceTimeTag, "" + ship.getServeTime());

        //create a message of the address of this shipper and attach it to the
        //shipment message, so the carrier's know who to send their costs to
        Message shipAddr = new Message();
        shipAddr.setMessageType(InetAddressTag);
        shipAddr.addArgument(IPAddressTag, cai.getIP());
        shipAddr.addArgument(PortNumberTag, "" + cai.getPort());
        shipMsg.addArgument(shipAddr);

        //wrap this message with a broadcast message so the master server knows
        //what to do with it
        Message bmsg = new Message();
        bmsg.setMessageType(BroadcastTag);
        bmsg.addArgument(DestinationTag, CarrierMessage);
        bmsg.addArgument(shipMsg);

        //send the message to the master server
        new ThreadedClient(bmsg, HermesGlobals.masterServerIP, cai.myMasterPort);
    }

    /**
    * Will send an accept message to the customer with the best cost
    * @param i index of the shipment to send
    */
    private void sendShipmentToBeAccepted(int i) {
        Shipment ship = cai.shipmentList.getShipment(i);
        Message acceptMsg = new Message();
        Message addr = new Message();

        //create the message for the carrier
        acceptMsg.setMessageType(AcceptTag);
        acceptMsg.addArgument(IndexTag, "" + ship.getShipNo());
        acceptMsg.addArgument(XCoordTag, "" + ship.getX());
        acceptMsg.addArgument(YCoordTag, "" + ship.getY());
        acceptMsg.addArgument(DemandTag, "" + ship.getDemand());
        acceptMsg.addArgument(EarlyTimeTag, "" + ship.getEarliestTime());
        acceptMsg.addArgument(LateTimeTag, "" + ship.getLatestTime());
        acceptMsg.addArgument(ServiceTimeTag, "" + ship.getServeTime());

        //set the address to forward to
        addr.setMessageType(RelayAddressTag);
        addr.addArgument(IPAddressTag, ship.IP);
        addr.addArgument(PortNumberTag, "" + ship.port);

        cai.logWriter.writeLog("Shipment " + i + " was accepted " +
            "for carrier " + ship.IP + ":" + ship.port);

        //add the address of this shipper so the carrier can reply to it
        Message addrMsg = new Message();
        addrMsg.setMessageType(InetAddressTag);
        addrMsg.addArgument(IPAddressTag, cai.getIP());
        addrMsg.addArgument(PortNumberTag, "" + cai.getPort());
        acceptMsg.addArgument(addrMsg);

        System.out.println("Winner: " + addr.getValue(IPAddressTag) + " " +
            addr.getValue(PortNumberTag));

        //make a message to send to the Master Server wrapping the carrier's msg
        Message msg = new Message();
        msg.setMessageType(RelayTag);
        msg.addArgument(addr);

        //set the type of tag the embedded message is
        msg.addArgument(MessageTags.PackagedMessageTag, AcceptTag);

        //set the shipments' attributes to the message
        msg.addArgument(acceptMsg);

        //send the message off to the master server
        new ThreadedClient(msg, HermesGlobals.masterServerIP, cai.myMasterPort);
    }

    /**
     * Runs the local opts on this carrier.
     */
    private void runLocalOpts() {
        //message comes in if lesser opt button is hit on master
        //or if optimize is hit on carrier
        //It run local Opts on carrier using Zeus via zeusAdaptor
        if (!(zAdapt.isEmpty())) {
            cai.logWriter.writeLog(
                "Runnning Lesser Optimization on this Carrier");
            System.out.println("Runnning Lesser Optimization on this Carrier");
            zAdapt.opts();
            System.out.println("Finished Lesser Optimization on this Carrier");

            // update the interface values
            updateGui();
            updateMaster();
        } else {
            cai.logWriter.writeLog(
                "There are NO ASSIGNED shipments this Carrier can Optimize");
            System.out.println(
                "There are NO ASSIGNED shipments this Carrier can Optimize");
        }

        cai.isLock = false;
    }

    /**
     * sends out the most costly customer in the route for the other carrier
     * agents to bid upon
     * @param theShip a message containing the shipment to broadcast.
     *
     * How to do this:
     * Treat as a normal shipment
     * Zues has a new Shipment linked list and ShipListAdaptor to contain the
     * shipments that will be send out to bid.
     */
    public void broadcastOneOpt(Message theShip) {
        Message addressMessage = new Message();
        addressMessage.setMessageType(MessageTags.InetAddressTag);
        addressMessage.addArgument(IPAddressTag, cai.getIP());
        addressMessage.addArgument(PortNumberTag, "" + cai.getPort());
        theShip.addArgument(addressMessage);

        Message broadcastMessage = new Message();
        broadcastMessage.setMessageType(BroadcastTag);
        broadcastMessage.addArgument(MessageTags.PackagedMessageTag,
            broadcastMessage.getMessageType());
        broadcastMessage.addArgument(theShip);

        new ThreadedClient(broadcastMessage, HermesGlobals.masterServerIP,
            HermesGlobals.masterServerPortNo);
    }
}
