package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to Shipment level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicShipmentCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
 * <p>Stub method - not used</p>
 * @param o  not used
 * @return double
 */
    public double getTotalCost(Object o) {
        return getTotalDistance(o);
    }

    /**
 * <p>Stub method - not used</p>
 * @param o  not used
 * @return double
 */
    public double getAngle(Object o) {
        return getTotalDistance(o);
    }

    /**
 * <p>Stub method - not used</p>
 * @param o  not used
 * @return double
 */
    public double getMedianTime(Object o) {
        return getTotalDistance(o);
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
 * <p>Stub method - not used</p>
 * @param o  not used
 * @return double
 */
    public double getTotalDistance(Object o) {
        return getTotalDistance(o);
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
 * <p>Stub method - not used</p>
 * @param o  not used
 */
    public void setTotalCost(Object o) {
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
