package Hermes;

import Zeus.*;


/**
 * <p>Title: ShipperListAdaptor.java </p>
 * <p>Description: Adaptor class to interface with the ShipmentLinkedList.  This
 *   class controls tools to set the status of shipments, get the shipment info,
 *   find the number of carriers that have scheduled, and reset the shipment status.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ShipperListAdaptor {
    ShipmentLinkedList shipLL;
    boolean isSched = false;

    /**
    * Constructor - initializes the ShipmentLinkedList instance
    * @param sLL  the ShipmentLinkedList instance
    */
    public ShipperListAdaptor(ShipmentLinkedList sLL) {
        shipLL = sLL;
    }

    /**
    * Returns the shipment at the given index
    * @param index index of the shipment
    * @return Shipment  the shipment at the given index
    */
    public Shipment getShipment(int index) {
        return shipLL.find(index);
    }

    /**
    * Will update the cost of the shipment indicated by the index passed. If the
    * cost that is passed is lower than the available cost, then that cost is
    * saved as the best cost and the carrier information is updated. Once all
    * shippers have bid upon the shipment, the method will return true.
    * @param index index of the shipment to update
    * @param cost cost to update
    * @param ip ip of the carrier that gave the cost
    * @param port port number of the carrier that gave the cost
    * @param numCarriers the total number of carriers currently active
    * @return boolean  true if all carriers have bid upon the shipment
    */
    public synchronized boolean setShipmentCost(int index, float cost,
        String ip, int port, int numCarriers) {
        Shipment ship = getShipment(index);

        //update the cost if the new cost is lower
        if (cost < ship.lowestCost) {
            ship.lowestCost = cost;
            ship.IP = ip;
            ship.port = port;
        }

        //increment the number of bids
        ship.numBids++;

        //check to see if all the carriers have bid on this shipment
        if (ship.numBids == numCarriers) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * This method will clear the bidding information of the shipment. This would
    * be called when the shipment has been offered to the carrier that won the
    * bidding process, but after it recieved the shipment, it was infeasible to
    * insert it.
    * @param index index of the shipment
    */
    public synchronized void resetShipment(int index) {
        Shipment ship = getShipment(index);

        //clear Hermes arguments from shipment
        ship.IP = "";
        ship.port = -1;
        ship.lowestCost = Float.MAX_VALUE;
        ship.numBids = 0;
    }

    /**
    * Set the shipment to be assigned to the person with the lowest cost
    * @param index index of the shipment to set
    */
    public synchronized void setAssigned(int index) {
        Shipment ship = getShipment(index);

        shipLL.setAssigned(ship);
    }

    /**
     * Inserts a shipment into the shipment linked list
     * @param ship shipment to insert
     */
    public synchronized void insertShipment(Shipment ship) {
        shipLL.insertLast(ship);
    }

    /**
    * Checks if all shipments have been scheduled
    * @return boolean  true if all shipments scheduled
    */
    public synchronized boolean allAssigned() {
        if (shipLL.getNoShipments() == shipLL.getNoShipsAssigned()) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * checks if more than one carrier still hasn't scheduled a shipment
    * @param numCarriers  number of carriers registered
    * @param cIP  carrier agent IP
    * @param cPort  carrier agent port number
    * @return boolean  true if more than one carrier still needs a shipment
    */
    public boolean carrierSched(int numCarriers, String cIP, int cPort) {
        if (!isSched) {
            int carr = 0;
            int size = shipLL.getNoShipments();
            boolean used = false;
            CarrierInfo[] carriers = new CarrierInfo[numCarriers];
            Shipment ship = shipLL.getFirst();

            for (int i = 0; i < size; i++) {
                if ((carr == 0) && ship.getAssigned()) {
                    // this carrier already has a shipment so its cost is different
                    if (ship.IP.equals(cIP) && (ship.port == cPort)) {
                        return true;
                    } else { // if not, then keep looking for carriers
                        carriers[carr] = new CarrierInfo(ship.IP, ship.port);
                        carr++;
                    }
                } else if (ship.getAssigned()) {
                    // this carrier already has a shipment so its cost is different
                    if (ship.IP.equals(cIP) && (ship.port == cPort)) {
                        return true;
                    } else { // if not, then keep looking for carriers

                        for (int j = 0; j < numCarriers; j++) {
                            if (carriers[j].getIP().equals(ship.IP)) {
                                if (carriers[j].getPort() == ship.port) {
                                    used = true;

                                    break;
                                }
                            }
                        }

                        if (!used) {
                            carriers[carr] = new CarrierInfo(ship.IP, ship.port);
                            carr++;
                        }
                    }
                }
            }

            if (carr < (numCarriers - 1)) {
                return false;
            }

            // else
            isSched = true;
        }

        return true;
    }

    /**
    * <p>Title: CarrierInfo.java </p>
    * <p>Description: This class acts as a struct to hold carrier information</p>
    * @author Ola Laleye, Mike McNamara, Anthony Pitluga
    * @version 2.3
    */
    private class CarrierInfo {
        String ip = null;
        int port = 0;

        /**
        * Constructor
        */
        public CarrierInfo() {
        }

        /**
        * Constructor that initializes the values
        * @param ip  ip address of carrier
        * @param port  port number of the carrier
        */
        public CarrierInfo(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        /**
        * Sets the ip address of the carrier
        * @param ip  ip address of the carrier
        */
        public void setIP(String ip) {
            this.ip = ip;
        }

        /**
        * Sets the port number of the carrier
        * @param port  port number of the carrier
        */
        public void setPort(int port) {
            this.port = port;
        }

        /**
        * Gets the ip address of the carrier
        * @return String  ip address of the carrier
        */
        public String getIP() {
            return ip;
        }

        /**
        * Gets the port number of the carrier
        * @return int  port number of the carrier
        */
        public int getPort() {
            return port;
        }
    }
}
