package Hermes;

import Zeus.*;

import java.io.*;

import java.net.*;


/**
 * <p>Title: ShipperMessageHandler.java </p>
 * <p>Description: This class receives a message over a socket and proceeses
 *    it accordingly.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ShipperMessageHandler extends Thread implements MessageTags {
    /**
    * Socket that will be read.
    */
    private Socket incomingCommunication;

    /**
    * Adaptor for the shipment linked list
    */
    private ShipperListAdaptor shipListAdaptor;
    private ShipperAgentInterface agentInterface;

    /**
    * Constructor, will read all the messages from the incoming thread and
    * do the appropriate actions to handle them.
    * @param incomingComm socket to the newly connected client
    * @param sai pointer to shipper's agent interface
    * @param sla pointer to the list adaptor
    */
    public ShipperMessageHandler(Socket incomingComm,
        ShipperAgentInterface sai, ShipperListAdaptor sla) {
        //assign the socket
        incomingCommunication = incomingComm;

        //save the agent interface
        agentInterface = sai;

        //save the shipper list adaptor
        shipListAdaptor = sla;

        //start the thread to handle the message
        start();
    }

    /**
    * Method that will process the message(s) coming in over the socket.
    */
    public void run() {
        try {
            //create a reader to the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        incomingCommunication.getInputStream()));

            //get the message contained in the socket
            Message clientMsg = new Message(in.readLine());

            agentInterface.logWriter.writeLog("Processing message: " +
                clientMsg.toString());

            //get the type of message
            String messageType = clientMsg.getMessageType();

            //now process the message based upon its type
            //message is a calculate message so we will find the best cost
            if (messageType.equals(InsertCostTag)) {
                setCostSendPoint(clientMsg);
            } else if (messageType.equals(ConfirmTag)) {
                agentInterface.confirmCount++;

                // remove shipment from list
                int index = Integer.parseInt(clientMsg.getValue(IndexTag));
                shipListAdaptor.setAssigned(index);
                System.out.println("Confirm Shipment " + index);

                if (shipListAdaptor.allAssigned()) {
                    enableButtonsMsg();
                }
            } else if (messageType.equals(RefuseTag)) {
                System.out.println("Refuse Tag");
                rebroadcastShipment(clientMsg);
            } else if (messageType.equals(TerminateTag)) {
                agentInterface.logWriter.writeLog("Terminating Shipper Agent.");
                System.out.println("Terminating Shipper Agent.");
                System.exit(0);
            } else if (messageType.equals(FileNameTag)) {
                PrintWriter out = null;
                Message msg = new Message();

                msg.addArgument(FileNameTag, agentInterface.getFileName());
                msg.addArgument(CapacityTag, "" + agentInterface.getMaxCap());
                msg.addArgument(DistanceTag, "" + agentInterface.getMaxDist());

                //use out to send it back to were it came from
                try {
                    out = new PrintWriter(incomingCommunication.getOutputStream(),
                            true);
                    out.println(msg.getMessageString());
                } catch (IOException ex) {
                    agentInterface.logWriter.writeLog(
                        "Error returning file name, " +
                        "max capacity, and max distance: " + ex);
                    System.err.println("Error returning file name, " +
                        "max capacity, and max distance: " + ex);
                } finally {
                    out.close();
                }
            }
        } catch (Exception e) {
            agentInterface.logWriter.writeLog("Error processing message: " + e);
            e.printStackTrace();
        }
    }

    /**
    * Sends the message to enable interface buttons to the Master Server after
    * all shipments have been scheduled
    */
    private void enableButtonsMsg() {
        Message msg = new Message();

        msg.setMessageType(EnableTag);

        //send the message off to the master server
        new ThreadedClient(msg, HermesGlobals.masterServerIP,
            agentInterface.myMasterPort);
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

            if (!(shipListAdaptor.carrierSched(numCar, cName,
                        Integer.parseInt(cPort)))) {
                agentInterface.logWriter.writeLog("Random cost generated for " +
                    cName + ":" + cPort + "; cost before: " + cost);
                cost += (Math.random() * .0001);
                agentInterface.logWriter.writeLog("Random cost generated for " +
                    cName + ":" + cPort + "; cost after: " + cost);
            }

            agentInterface.logWriter.writeLog("Bid for customer  " + index +
                " by " + cName + ":" + cPort + " is $" + cost);
            System.out.println("Bid for customer  " + index + " by " + cName +
                ":" + cPort + " is $" + cost);

            //update the cost for this shipment, will return false when all
            //carriers have made their bids
            boolean status = shipListAdaptor.setShipmentCost(index, cost,
                    cName, Integer.parseInt(cPort), numCar);

            //once all carriers have made their bids, offer to the lowest one
            if (status == true) {
                sendShipmentToBeAccepted(index);
            }
        } catch (Exception e1) {
            agentInterface.logWriter.writeLog(
                "Error analyzing bid for shipment: " + e1);
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
    * Will send an accept message to the customer with the best cost
    * @param i index of the shipment to send
    */
    private void sendShipmentToBeAccepted(int i) {
        Shipment ship = shipListAdaptor.getShipment(i);
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

        agentInterface.logWriter.writeLog("Shipment " + i + " was accepted " +
            "for carrier " + ship.IP + ":" + ship.port);

        //add the address of this shipper so the carrier can reply to it
        Message addrMsg = new Message();
        addrMsg.setMessageType(InetAddressTag);
        addrMsg.addArgument(IPAddressTag, agentInterface.getIP());
        addrMsg.addArgument(PortNumberTag, "" + agentInterface.getPort());
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
        new ThreadedClient(msg, HermesGlobals.masterServerIP,
            agentInterface.myMasterPort);
    }

    /**
    * Once a message has been rejected, the shipper will clear the cost info
    * out of the shipment and send the shipment again
    * @param msg reject message
    */
    private void rebroadcastShipment(Message msg) {
        //reset the bid info of the rejected shipment
        int index = Integer.parseInt(msg.getValue(IndexTag));
        shipListAdaptor.resetShipment(index);

        agentInterface.logWriter.writeLog("Shipment " + index +
            " is being rebroadcasted.");

        //create the message containting the shipment information
        Shipment ship = shipListAdaptor.getShipment(index);
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
        shipAddr.addArgument(IPAddressTag, agentInterface.getIP());
        shipAddr.addArgument(PortNumberTag, "" + agentInterface.getPort());
        shipMsg.addArgument(shipAddr);

        //wrap this message with a broadcast message so the master server knows
        //what to do with it
        Message bmsg = new Message();
        bmsg.setMessageType(BroadcastTag);
        bmsg.addArgument(DestinationTag, CarrierMessage);
        bmsg.addArgument(shipMsg);

        //send the message to the master server
        new ThreadedClient(bmsg, HermesGlobals.masterServerIP,
            agentInterface.myMasterPort);
    }
}
