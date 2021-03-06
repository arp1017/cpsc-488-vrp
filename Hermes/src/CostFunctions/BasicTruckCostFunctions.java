package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to Truck level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicTruckCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
 * <p>Stub method - not used</p>
 * @param o  not used
 * @return double
 */
    public double getTotalCost(Object o) {
        Truck truck = (Truck) o;

        return truck.currentCost;
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
 * @param o  Object to get demand for
 * @return double  total demand
 */
    public double getTotalDemand(Object o) {
        Truck truck = (Truck) o;

        return truck.currentDemand;
    }

    /**
 * <p>Returns the total distance for the object</p>
 * @param o  Object to get distance for
 * @return double  total distance
 */
    public double getTotalDistance(Object o) {
        Truck truck = (Truck) o;

        return truck.currentDistance;
    }

    /**
 * <p>return the total travel time for the object</p>
 * @param o  Object to get distance for
 * @return double  total travel time
 */
    public double getTotalTravelTime(Object o) {
        Truck truck = (Truck) o;

        return (float) ProblemInfo.vNodesLevelCostF.getTotalTravelTime(truck.mainVisitNodes);
    }

    /**
 * <p>return the total wait time for the object</p>
 * @param o  Object to get wait time for
 * @return double  total wait time
 */
    public double getTotalWaitTime(Object o) {
        Truck truck = (Truck) o;

        return (float) ProblemInfo.vNodesLevelCostF.getTotalWaitTime(truck.mainVisitNodes);
    }

    /**
 * <p>return the total service time for the object</p>
 * @param o  Object to get service time for
 * @return double  total service time
 */
    public double getTotalServiceTime(Object o) {
        Truck truck = (Truck) o;

        return (float) ProblemInfo.vNodesLevelCostF.getTotalServiceTime(truck.mainVisitNodes);
    }

    /**
 * <p>return the total tardiness time for the object</p>
 * @param o  Object to get tardiness time for
 * @return double  total tardiness time
 */
    public double getTotalTardinessTime(Object o) {
        Truck truck = (Truck) o;

        return (float) ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(truck.mainVisitNodes);
    }

    /**
 * <p>return the total excess time for the object</p>
 * @param o  Object to get excess time for
 * @return double  total excess time
 */
    public double getTotalExcessTime(Object o) {
        Truck truck = (Truck) o;

        return (float) ProblemInfo.vNodesLevelCostF.getTotalExcessTime(truck.mainVisitNodes);
    }

    /**
 * <p>return the total overload for the object</p>
 * @param o  Object to get overload for
 * @return double  total overload
 */
    public double getTotalOverload(Object o) {
        Truck truck = (Truck) o;

        return (float) ProblemInfo.vNodesLevelCostF.getTotalOverload(truck.mainVisitNodes);
    }

    /**
 * <p>set the total excess time for the object</p>
 * @param o  Object that will have its excess time set
 */
    public void setTotalExcessTime(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalExcessTime(truck.mainVisitNodes);
    }

    /**
 * <p>set the total tardiness time for the object</p>
 * @param o  Object that will have its tardiness time set
 */
    public void setTotalTardinessTime(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalTardinessTime(truck.mainVisitNodes);
    }

    /**
 * <p>set the total overload time for the object</p>
 * @param o  Object that will have its overload set
 */
    public void setTotalOverload(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalOverload(truck.mainVisitNodes);
    }

    /**
 * <p>set the total service time for the object</p>
 * @param o  Object that will have its service time set
 */
    public void setTotalServiceTime(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalServiceTime(truck.mainVisitNodes);
    }

    /**
 * <p>set the total wait time for the object</p>
 * @param o  Object that will have its wait time set
 */
    public void setTotalWaitTime(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalWaitTime(truck.mainVisitNodes);
    }

    /**
 * <p>set the total travel time for the object</p>
 * @param o  Object that will have its travel time set
 */
    public void setTotalTravelTime(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalTravelTime(truck.mainVisitNodes);
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
 */
    public void setTotalCost(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalCost(truck.mainVisitNodes);
        truck.currentCost = (float) ProblemInfo.vNodesLevelCostF.getTotalCost(truck.mainVisitNodes);
    }

    /**
 * <p>Sets the total demand for the object</p>
 * @param o  Object that will have its demand set
 */
    public void setTotalDemand(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalDemand(truck.mainVisitNodes);
        truck.currentDemand = (float) ProblemInfo.vNodesLevelCostF.getTotalDemand(truck.mainVisitNodes);
    }

    /**
 * <p>Sets the total distance for the object</p>
 * @param o  Object that will have its excess time set
 */
    public void setTotalDistance(Object o) {
        Truck truck = (Truck) o;
        ProblemInfo.vNodesLevelCostF.setTotalDistance(truck.mainVisitNodes);
        truck.currentDistance = (float) ProblemInfo.vNodesLevelCostF.getTotalDistance(truck.mainVisitNodes);
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
 * <p>Calls all of the set value methods to calculate and set all of the
 * stats of an object</p>
 * @param o  Object to have its total stats calculated
 */
    public void calculateTotalsStats(Object o) {
        setTotalDistance(o); //Setting the total distance for each truck
        setTotalDemand(o); //Setting the total demand for each truck
        setTotalWaitTime(o); //Setting the total wait time for each truck
        setTotalServiceTime(o); //Setting the total Service time for each truck
        setTotalTardinessTime(o); //Setting the total tardiness for each truck
        setTotalExcessTime(o); //Setting the total excess time for each truck
        setTotalOverload(o); //Setting the total excess time for each truck
        setTotalTravelTime(o); //Setting the total travel time for each truck
        setTotalCost(o); //Setting the total cost of the schedule
    }
}
