package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to Depot level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicDepotCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalCost(Object o) {
        Depot depot = (Depot) o;

        return depot.totalCost;
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
    * <p>Returns the total demand for the object</p>
    * @param o  Object to get demand for
    * @return double  total demand
    */
    public double getTotalDemand(Object o) {
        Depot depot = (Depot) o;

        return depot.totalDemand;
    }

    /**
    * <p>Returns the total distance for the object</p>
    * @param o  Object to get distance for
    * @return double  total distance
    */
    public double getTotalDistance(Object o) {
        Depot depot = (Depot) o;

        return depot.totalDistance;
    }

    /**
    * <p>Returns the total travel time for the object</p>
    * @param o  Object to get travel time for
    * @return double  total travel time
    */
    public double getTotalTravelTime(Object o) {
        Depot depot = (Depot) o;

        return depot.totalTravelTime;
    }

    /**
    * <p>Returns the total excess time for the object</p>
    * @param o  Object to get excess time for
    * @return double  total excess time
    */
    public double getTotalExcessTime(Object o) {
        Depot depot = (Depot) o;

        return depot.totalExcessTime;
    }

    /**
    * <p>Returns the total service time for the object</p>
    * @param o  Object to get service time for
    * @return double  total service time
    */
    public double getTotalServiceTime(Object o) {
        Depot depot = (Depot) o;

        return depot.totalServTime;
    }

    /**
    * <p>Returns the total tardiness for the object</p>
    * @param o  Object to get tardiness for
    * @return double  total tardiness
    */
    public double getTotalTardinessTime(Object o) {
        Depot depot = (Depot) o;

        return depot.totalTardiness;
    }

    /**
    * <p>Returns the total wait time for the object</p>
    * @param o  Object to get wait time for
    * @return double  total wait time
    */
    public double getTotalWaitTime(Object o) {
        Depot depot = (Depot) o;

        return depot.totalWaitTime;
    }

    /**
    * <p>Returns the total overload for the object</p>
    * @param o  Object to get overload for
    * @return double  total overload
    */
    public double getTotalOverload(Object o) {
        Depot depot = (Depot) o;

        return depot.totalOverload;
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalCost(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalCost(depot.mainTrucks);
        depot.totalCost = (float) ProblemInfo.truckLLLevelCostF.getTotalCost(depot.mainTrucks);
    }

    /**
    * <p>Sets the total demand for the object</p>
    * @param o  Object that will have its demand set
    */
    public void setTotalDemand(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalDemand(depot.mainTrucks);
        depot.totalDemand = (float) ProblemInfo.truckLLLevelCostF.getTotalDemand(depot.mainTrucks);
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
    * <p>Sets the total distance for the object</p>
    * @param o  Object that will have its distance set
    */
    public void setTotalDistance(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalDistance(depot.mainTrucks);
        depot.totalDistance = (float) ProblemInfo.truckLLLevelCostF.getTotalDistance(depot.mainTrucks);
    }

    /**
    * <p>Sets the total travel time for the object</p>
    * @param o  Object that will have its travel time set
    */
    public void setTotalTravelTime(Object o) {
        Depot depot = (Depot) o;
        depot.totalTravelTime = (float) (getTotalServiceTime(depot) +
            getTotalWaitTime(depot) + getTotalDistance(depot));
    }

    /**
    * <p>Sets the total excess time for the object</p>
    * @param o  Object that will have its excess time set
    */
    public void setTotalExcessTime(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalExcessTime(depot.mainTrucks);
        depot.totalExcessTime = (float) ProblemInfo.truckLLLevelCostF.getTotalExcessTime(depot.mainTrucks);
    }

    /**
    * <p>Sets the total service time for the object</p>
    * @param o  Object that will have its service time set
    */
    public void setTotalServiceTime(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalServiceTime(depot.mainTrucks);
        depot.totalServTime = (float) ProblemInfo.truckLLLevelCostF.getTotalServiceTime(depot.mainTrucks);
    }

    /**
    * <p>Sets the total tardiness time for the object</p>
    * @param o  Object that will have its tardiness time set
    */
    public void setTotalTardinessTime(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalTardinessTime(depot.mainTrucks);
        depot.totalTardiness = (float) ProblemInfo.truckLLLevelCostF.getTotalTardinessTime(depot.mainTrucks);
    }

    /**
    * <p>Sets the total wait time for the object</p>
    * @param o  Object that will have its wait time set
    */
    public void setTotalWaitTime(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalWaitTime(depot.mainTrucks);
        depot.totalWaitTime = (float) ProblemInfo.truckLLLevelCostF.getTotalWaitTime(depot.mainTrucks);
    }

    /**
    * <p>Sets the total overload for the object</p>
    * @param o  Object that will have its overload set
    */
    public void setTotalOverload(Object o) {
        Depot depot = (Depot) o;
        ProblemInfo.truckLLLevelCostF.setTotalOverload(depot.mainTrucks);
        depot.totalOverload = (float) ProblemInfo.truckLLLevelCostF.getTotalOverload(depot.mainTrucks);
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
        setTotalDistance(o); //Setting the total distance for each depot
        setTotalDemand(o); //Setting the total demand for each depot
        setTotalWaitTime(o); //Setting the total wait time for each depot
        setTotalServiceTime(o); //Setting the total Service time for each depot
        setTotalTardinessTime(o); //Setting the total tardiness for each depot
        setTotalExcessTime(o); //Setting the total excess time for each depot
        setTotalOverload(o); //Setting the total excess time for each depot
        setTotalTravelTime(o); //Setting the total travel time for each depot
        setTotalCost(o); //Setting the total cost of the schedule
    }
}
