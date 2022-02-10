package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to Shipment Linked List level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicShipmentLLCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
    * <p>Returns the total cost of inserting a node in the depot linked list</p>
    * @param o  Object that contains the node to be inserted
    * @return double  cost to insert node
    */
    public double getTotalCost(Object o) {
        ShipmentLinkedList shipmentLL = (ShipmentLinkedList) o;

        return shipmentLL.cost;
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalNonEmptyNodes(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalNodes(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalDemand(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Returns the distance between two nodes in the object</p>
    * @param o  Object that contains the nodes to get distance for
    * @return double  distance
    */
    public double getTotalDistance(Object o) {
        ShipmentLinkedList shipmentLL = (ShipmentLinkedList) o;

        return shipmentLL.distance;
    }

    /**
    * <p>Returns the median time for a node in the object</p>
    * @param o  Object that contains the node to get the median time for
    * @return double  median time
    */
    public double getMedianTime(Object o) {
        ShipmentLinkedList shipmentLL = (ShipmentLinkedList) o;

        return shipmentLL.time;
    }

    /**
    * <p>Returns the angle for a node in relation to the depot in the object</p>
    * @param o  Object that contains the node to get the angle for
    * @return double  angle
    */
    public double getAngle(Object o) {
        ShipmentLinkedList shipmentLL = (ShipmentLinkedList) o;

        return shipmentLL.angle;
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalTravelTime(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalExcessTime(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalServiceTime(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalTardinessTime(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalWaitTime(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalOverload(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Sets the cost associated with inserting a node into a depot linked list</p>
    * @param o  Object that contains the node to be insterted
    */
    public void setTotalCost(Object o) {
        ShipmentLinkedList shipmentLL = (ShipmentLinkedList) o;

        /* by emphasizing distance the customers will be clustered
        including the time and angle weights will help find more
        efficient customers to be assigned, this help in randomized problems
        */
        shipmentLL.cost = (.15 * (getMedianTime(shipmentLL))) +
            (.15 * (getTotalDistance(shipmentLL))) +
            (.7 * (getAngle(shipmentLL)));
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalDemand(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalNonEmptyNodes(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalNodes(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalDistance(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalTravelTime(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalExcessTime(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalServiceTime(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalTardinessTime(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalWaitTime(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalOverload(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param caseNo  not used
    */
    public void setWeights(int caseNo) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void swapMNchains(Object o) {
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @param p  not used
    * @param q  not used
    * @param type  not used
    * @return boolean
    */
    public boolean calcCloseness(Object o, Object p, Object q, int type) {
        return false;
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double calculateOptimizedCost(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double globalCalculateOptimizedCost(Object o) {
        return getTotalDistance(o);
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void calculateTotalsStats(Object o) {
    }
}
