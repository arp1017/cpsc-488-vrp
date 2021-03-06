package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to Truck Linked List level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicTruckLLCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
 * <p>Stub method returns total distance as cost.
 * Uses getTotalDistance method.</p>
 * @param o  Object to get cost for
 * @return double  cost for the object
 */
    public double getTotalCost(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalCost;
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
 * <p>Returns the total demand for the object</p>
 * @param o Object to get demand for
 * @return double  total demand
 */
    public double getTotalDemand(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalDemand;
    }

    /**
 * <p>Returns the total distance for the object</p>
 * @param o  Object to get distance for
 * @return double  total distance
 */
    public double getTotalDistance(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalDistance;
    }

    /**
 * <p>Returns the total number of non empty nodes of the object</p>
 * @param o  Object to get number of non empty nodes for
 * @return double  number of non empty nodes
 */
    public double getTotalNonEmptyNodes(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        truckLL.calculateTotalNonEmptyTrucks();

        return truckLL.totalNonEmptyTrucks;
    }

    /**
 * <p>Returns the total number of nodes of the object</p>
 * @param o  Object to get number of nodes for
 * @return double  total number of nodes
 */
    public double getTotalNodes(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.noTrucks;
    }

    /**
 * <p>Returns the total travel time of the object</p>
 * @param o  Object to get travel time for
 * @return double  total travel time
 */
    public double getTotalTravelTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalTotalTravelTime;
    }

    /**
 * <p>Returns the total excess time for the object</p>
 * @param o  Object to get excess time for
 * @return double  total excess time
 */
    public double getTotalExcessTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalExcessTime;
    }

    /**
 * <p>Returns the total service time of the object</p>
 * @param o  Object to get service time for
 * @return double  total service time
 */
    public double getTotalServiceTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalServiceTime;
    }

    /**
 * <p>Returns the total tardiness time</p>
 * @param o  Object to get tardiness time for
 * @return double  total tardiness time
 */
    public double getTotalTardinessTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalTardiness;
    }

    /**
 * <p>Returns total wait time of the object</p>
 * @param o  Object to get wait time for
 * @return double  total wait time
 */
    public double getTotalWaitTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalWaitTime;
    }

    /**
 * <p>Returns the total overload of the object</p>
 * @param o  Object to get overload for
 * @return double  total overload
 */
    public double getTotalOverload(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;

        return truckLL.totalOverload;
    }

    /**
 * <p>Stub class to set the total cost of the object</p>
 * @param o  Object that will have its cost set
 */
    public void setTotalCost(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalCost = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalCost(t);
            truckLL.totalCost += ProblemInfo.truckLevelCostF.getTotalCost(t);
            t = t.next;
        }
    }

    /**
 * <p>Sets the total demand for the object</p>
 * @param o  Object that will have its demand set
 */
    public void setTotalDemand(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalDemand = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalDemand(t);
            truckLL.totalDemand += ProblemInfo.truckLevelCostF.getTotalDemand(t);
            t = t.next;
        }
    }

    /**
 * <p>Sets the total distance of the object</p>
 * @param o  Object that will have its distance set
 */
    public void setTotalDistance(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalDistance = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalDistance(t);
            truckLL.totalDistance += ProblemInfo.truckLevelCostF.getTotalDistance(t);
            t = t.next;
        }
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
 * <p>Sets the total travel time for the object</p>
 * @param o  Object that will have its travel time set
 */
    public void setTotalTravelTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalTotalTravelTime = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalTravelTime(t);
            truckLL.totalTotalTravelTime += ProblemInfo.truckLevelCostF.getTotalTravelTime(t);
            t = t.next;
        }
    }

    /**
 * <p>Sets the total excess time for the object</p>
 * @param o  Object that will have its excess time set
 */
    public void setTotalExcessTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalExcessTime = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalExcessTime(t);
            truckLL.totalExcessTime += ProblemInfo.truckLevelCostF.getTotalExcessTime(t);
            t = t.next;
        }
    }

    /**
 * <p>Sets total service time for the object</p>
 * @param o  Object that will have its service time set
 */
    public void setTotalServiceTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalServiceTime = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalServiceTime(t);
            truckLL.totalServiceTime += ProblemInfo.truckLevelCostF.getTotalServiceTime(t);
            t = t.next;
        }
    }

    /**
 * <p>Sets the total tardiness time for the object</p>
 * @param o  Object that will have its tardiness time set
 */
    public void setTotalTardinessTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalTardiness = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalTardinessTime(t);
            truckLL.totalTardiness += ProblemInfo.truckLevelCostF.getTotalTardinessTime(t);
            t = t.next;
        }
    }

    /**
 * <p>Set the total wait time for the object</p>
 * @param o  Object that will have its wait time set
 */
    public void setTotalWaitTime(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalWaitTime = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalWaitTime(t);
            truckLL.totalWaitTime += ProblemInfo.truckLevelCostF.getTotalWaitTime(t);
            t = t.next;
        }
    }

    /**
 * <p>Sets the total overload for the object</p>
 * @param o  Object that will have its overload set
 */
    public void setTotalOverload(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.totalOverload = 0;

        Truck t = truckLL.first;

        while (t != null) {
            ProblemInfo.truckLevelCostF.setTotalOverload(t);
            truckLL.totalOverload += ProblemInfo.truckLevelCostF.getTotalOverload(t);
            t = t.next;
        }
    }

    /**
 * <p>Stub method - not used</p>
 * @param caseNo  not used
 */
    public void setWeights(int caseNo) {
    }

    /**
 * <p>Will swap m nodes from one list object with n nodes from another</p>
 * @param o  Object where list objects exist
 */
    public void swapMNchains(Object o) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        truckLL.swapMNchains(2, 3);
    }

    /**
 * <p>Calculates the relative closeness of two nodes from different list objects.
 * Closeness is relative to distance and time, and must be within a defineable range</p>
 * @param o  Object where list objects exist
 * @param p  First list object
 * @param q  Second list object
 * @param type  type of closeness comparison check
 * @return boolean  true if nodes are considered close to each other, false otherwise
 */
    public boolean calcCloseness(Object o, Object p, Object q, int type) {
        TruckLinkedList truckLL = (TruckLinkedList) o;
        PointCell p1 = (PointCell) p;
        PointCell q1 = (PointCell) q;

        if (type == 1) { // list heads

            // close(first object, second object, distance weight, time weight, range)
            return truckLL.close(p1, q1, .4, .6, 15);
        } else if (type == 2) { // list itself

            // close(first object, second object, distance weight, time weight, range)
            return truckLL.close(p1, q1, 0, 1, 15);
        } else {
            return false;
        }
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
 * <p>Calls all of the set value methods to calculate and set all of the
 * stats of an object</p>
 * @param o  Object to have its total stats calculated
 */
    public void calculateTotalsStats(Object o) {
        setTotalDistance(o);
        setTotalExcessTime(o);
        setTotalServiceTime(o);
        setTotalTardinessTime(o);
        setTotalWaitTime(o);
        setTotalOverload(o);
        setTotalDemand(o);
        setTotalTravelTime(o);
        setTotalCost(o); //Setting the total cost of the schedule
    }
}
