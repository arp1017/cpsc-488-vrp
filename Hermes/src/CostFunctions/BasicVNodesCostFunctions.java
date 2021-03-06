package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to VisitNodes level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicVNodesCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
 * <p>Returns total distance as cost.
 * It uses getTotalDistance method.</p>
 * @param o  Object to get cost for
 * @return double  total cost
 */
    public double getTotalCost(Object o) {
        return getTotalDistance(o) +
        ((getTotalDistance(o) * ProblemInfo.alpha) * getTotalWaitTime(o)) +
        ((getTotalDistance(o) * ProblemInfo.beta) * getTotalExcessTime(o)) +
        ((getTotalDistance(o) * ProblemInfo.fi) * getTotalOverload(o)) +
        ((getTotalDistance(o) * ProblemInfo.mu) * getTotalTardinessTime(o));
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
 * <p>Returns current capacity of VisitNodesLinkedList object</p>
 * @param o  Object to get capacity for
 * @return double  total capacity
 */
    public double getTotalDemand(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentCapacity;
    }

    /**
 * <p>Returns total distance of the VisitNodesLinkedList object</p>
 * @param o  Object to get distance for
 * @return double  total distance
 */
    public double getTotalDistance(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentDistance;
    }

    /**
 * <p>Returns total travel time of the VisitNodesLinkedList object</p>
 * @param o  Object to get travel time for
 * @return double  total travel time
 */
    public double getTotalTravelTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentTotalTravelTime;
    }

    /**
 * <p>Returns total number of nodes in the VisitNodesLinkedList object</p>
 * @param o  Object to get number of nodes for
 * @return double  total number of nodes
 */
    public double getTotalNodes(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.getSize();
    }

    /**
 * <p>Returns total number of non empty nodes of the VisitNodesLinkedList object.
 * It uses getTotalNodes method.</p>
 * @param o  Object to get number of non empty nodes for
 * @return double  total number of non empty nodes
 */
    public double getTotalNonEmptyNodes(Object o) {
        return getTotalNodes(o);
    }

    /**
 * <p>return the total wait time for the object</p>
 * @param o Object ot get the wait time for
 * @return double  total wait time
 */
    public double getTotalWaitTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentWaitTime;
    }

    /**
 * <p>return the total service time for the object</p>
 * @param o  Object to get the service time for
 * @return double  total service time
 */
    public double getTotalServiceTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentServiceTime;
    }

    /**
 * <p>return the total overload for the object</p>
 * @param o  Object to get the overload for
 * @return double  total overload
 */
    public double getTotalOverload(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentOverload;
    }

    /**
 * <p>return the total tardiness time for the object</p>
 * @param o  Object to get the tardiness time for
 * @return double  total tardiness time
 */
    public double getTotalTardinessTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentTardiness;
    }

    /**
 * <p>return the total excess time for the object</p>
 * @param o  Object to get the excess time for
 * @return double  total excess time
 */
    public double getTotalExcessTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;

        return vNodes.currentExcessTime;
    }

    /**
 * <p>set the total excess time for the object</p>
 * @param o  Object that will have its excess time set
 */
    public void setTotalExcessTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentExcessTime = vNodes.calcTotalExcessTime();
    }

    /**
 * <p>set the total tardiness time for the object</p>
 * @param o  Object that will have its tardiness time set
 */
    public void setTotalTardinessTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentTardiness = vNodes.calcTotalTardiness();
    }

    /**
 * <p>set the total overload for the object</p>
 * @param o  Object that will have its overload set
 */
    public void setTotalOverload(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentOverload = vNodes.calcTotalOverload();
    }

    /**
 * <p>set the total service time for the object</p>
 * @param o  Object that will have its service time set
 */
    public void setTotalServiceTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentServiceTime = vNodes.gettotalServiceTime();
    }

    /**
 * <p>Set the total wait time for the object</p>
 * @param o  Object that will have its wait time set
 */
    public void setTotalWaitTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentWaitTime = vNodes.calculateTotalWaitTime();
    }

    /**
 * <p>Set the total travel time for the object</p>
 * @param o  Object that will have its travel time set
 */
    public void setTotalTravelTime(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentTotalTravelTime = (float) (getTotalWaitTime(o) +
            getTotalServiceTime(o) + getTotalDistance(o));
    }

    /**
 * <p>Stub method - not used</p>
 * @param o  not used
 */
    public void setTotalNonEmptyNodes(Object o) {
    }

    /**
 * <p>Stub method to set the total cost of the object.
 * Uses setTotalDistance method.</p>
 * @param o  Object that will have its cost set
 */
    public void setTotalCost(Object o) {
        setTotalDistance(o);
    }

    /**
 * <p>Set the total demand of the object</p>
 * @param o  Object that will have its demand set

 */
    public void setTotalDemand(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentCapacity = vNodes.calculateCapacity();
    }

    /**
 * <p>set the total distance of the object</p>
 * @param o  Object that will have its distance set
 */
    public void setTotalDistance(Object o) {
        VisitNodesLinkedList vNodes = (VisitNodesLinkedList) o;
        vNodes.currentDistance = vNodes.calculateDistance();
    }

    /**
 * <p>Calculates the cost to perform a global optimization.
 * Uses getTotalDistance to aid in the calculation.</p>
 * @param o  Object to calculate cost for
 * @return double  cost to perform the calculation
 */
    public double globalCalculateOptimizedCost(Object o) {
        boolean returnType = true;

        //If we want to use the weighing of all the measurements then we return
        //according to the percentage of each
        if (returnType) {
            return getTotalDistance(o) +
            ((getTotalDistance(o) * ProblemInfo.alpha) * getTotalWaitTime(o)) +
            ((getTotalDistance(o) * ProblemInfo.beta) * getTotalExcessTime(o)) +
            ((getTotalDistance(o) * ProblemInfo.fi) * getTotalOverload(o)) +
            ((getTotalDistance(o) * ProblemInfo.mu) * getTotalTardinessTime(o));
        }
        //Otherwise we simply return the total distance
        else {
            return getTotalDistance(o);
        }
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
 * <p>Calculates the cost to perform a local optimization.
 * Uses getTotalDistance to aid in the calculation.</p>
 * @param o  Object to calculate the cost for
 * @return double  cost to perform the optimization
 */
    public double calculateOptimizedCost(Object o) {
        return getTotalDistance(o) +
        ((getTotalDistance(o) * ProblemInfo.alpha) * getTotalWaitTime(o)) +
        ((getTotalDistance(o) * ProblemInfo.beta) * getTotalExcessTime(o)) +
        ((getTotalDistance(o) * ProblemInfo.fi) * getTotalOverload(o)) +
        ((getTotalDistance(o) * ProblemInfo.mu) * getTotalTardinessTime(o));
    }

    /**
 * <p>Calls all of the set value methods to calculate and set all of the
 * stats of an object</p>
 * @param o  Object to have its total stats calculated
 */
    public void calculateTotalsStats(Object o) {
        setTotalDistance(o); //Setting the total distance
        setTotalDemand(o); //Setting the total demand
        setTotalWaitTime(o); //Setting the total wait time
        setTotalServiceTime(o); //Setting the total service time
        setTotalTardinessTime(o); //Setting the total tardiness
        setTotalExcessTime(o); //Setting the total excess time
        setTotalOverload(o); //Setting the total excess time
        setTotalTravelTime(o); //Setting the total travel time
    }
}
