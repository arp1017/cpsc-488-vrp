package CostFunctions;

import Zeus.*;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to Depot LinkedList level</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class BasicDepotLLCostFunctions implements CostFunctions,
    java.io.Serializable {
    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    * @return double
    */
    public double getTotalCost(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.totalCost;
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
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.totalDemand;
    }

    /**
    * <p>Returns the total distance for the object</p>
    * @param o  Object to get distance for
    * @return double  total distance
    */
    public double getTotalDistance(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.totalDistance;
    }

    /**
    * <p>Returns the total number of non empty nodes for the object</p>
    * @param o  Object to get number of non empty nodes for
    * @return double  total number of non empty nodes
    */
    public double getTotalNonEmptyNodes(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.totalNonEmptyTrucks;
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
    * <p>Returns the total travel time for the object</p>
    * @param o  Object to get travel time for
    * @return double  total travel time
    */
    public double getTotalTravelTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.totalTotalTravelTime;
    }

    /**
    * <p>Returns the total excess time for the object</p>
    * @param o  Object to get excess time for
    * @return double  total excess time
    */
    public double getTotalExcessTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.getTotalExcessTime();
    }

    /**
    * <p>Returns the total service time for the object</p>
    * @param o  Object to get service time for
    * @return double  total service time
    */
    public double getTotalServiceTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.getTotalServiceTime();
    }

    /**
    * <p>Returns the total tardiness time for the object</p>
    * @param o  Object to get tardiness time for
    * @return double  total tardiness time
    */
    public double getTotalTardinessTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.getTotalTardiness();
    }

    /**
    * <p>Returns the total wait time for the object</p>
    * @param o  Object to get wait time for
    * @return double  total wait time
    */
    public double getTotalWaitTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.getTotalWaitTime();
    }

    /**
    * <p>Returns the total overload for the object</p>
    * @param o  Object to get overload for
    * @return double  total overload
    */
    public double getTotalOverload(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        return depotLL.getTotalOverload();
    }

    /**
    * <p>Stub method - not used</p>
    * @param o  not used
    */
    public void setTotalCost(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalCost = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalCost(d);
            depotLL.totalCost += (float) ProblemInfo.depotLevelCostF.getTotalCost(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total demand for the object</p>
    * @param o  Object that will have its demand set
    */
    public void setTotalDemand(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalDemand = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalDemand(d);
            depotLL.totalDemand += (float) ProblemInfo.depotLevelCostF.getTotalDemand(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total distance for the object</p>
    * @param o  Object that will have its distance set
    */
    public void setTotalDistance(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalDistance = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalDistance(d);
            depotLL.totalDistance += (float) ProblemInfo.depotLevelCostF.getTotalDistance(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total number of non empty nodes for the object</p>
    * @param o  Object that will have its number of non empty nodes set
    */
    public void setTotalNonEmptyNodes(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;
        depotLL.calculateTotalNonEmptyTrucks();
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
        DepotLinkedList depotLL = (DepotLinkedList) o;
        depotLL.totalTotalTravelTime = (float) (getTotalWaitTime(o) +
            getTotalServiceTime(o) + getTotalDistance(o)); // calculates and returns total excess time
    }

    /**
    * <p>Sets the total excess time for the object</p>
    * @param o  Object that will have its excess time set
    */
    public void setTotalExcessTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalExcessTime = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalExcessTime(d);
            depotLL.totalExcessTime += (float) ProblemInfo.depotLevelCostF.getTotalExcessTime(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total service time for the object</p>
    * @param o  Object that will have its service time set
    */
    public void setTotalServiceTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalServiceTime = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalServiceTime(d);
            depotLL.totalServiceTime += (float) ProblemInfo.depotLevelCostF.getTotalServiceTime(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total tardiness time for the object</p>
    * @param o  Object that will have its tardiness time set
    */
    public void setTotalTardinessTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalTardiness = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalTardinessTime(d);
            depotLL.totalTardiness += (float) ProblemInfo.depotLevelCostF.getTotalTardinessTime(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total wait time for the object</p>
    * @param o  Object that will have its wait time set
    */
    public void setTotalWaitTime(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalWaitTime = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalWaitTime(d);
            depotLL.totalWaitTime += (float) ProblemInfo.depotLevelCostF.getTotalWaitTime(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the total overload for the object</p>
    * @param o  Object that will have its overload set
    */
    public void setTotalOverload(Object o) {
        DepotLinkedList depotLL = (DepotLinkedList) o;

        depotLL.totalOverload = 0;

        Depot d = depotLL.getFirst();

        while (d != null) {
            ProblemInfo.depotLevelCostF.setTotalOverload(d);
            depotLL.totalOverload += (float) ProblemInfo.depotLevelCostF.getTotalOverload(d);
            d = d.next;
        }
    }

    /**
    * <p>Sets the cost metric weight variables according to the case number</p>
    *
    * @param caseNo  determines what the cost metric weights will be
    */
    public void setWeights(int caseNo) {
        switch (caseNo) {
        case 0: // no infeasiblity
            ProblemInfo.alpha = 1; // 1% penalty for wait time
            ProblemInfo.beta = 10; // 1000% penalty for excess time
            ProblemInfo.mu = 10; // 1000% penalty for tardiness
            ProblemInfo.fi = 10; // 1000% penalty for overload

            break;

        case 1: // loosen up the solution by allowing infeasiblity
            ProblemInfo.alpha = 0; // penalty for wait time
            ProblemInfo.beta = 0.03; // penalty for excess time
            ProblemInfo.mu = 0.05; // penalty for tardiness
            ProblemInfo.fi = 0.15; // penalty for overload

            break;

        case 2: // tighten it slightly by allowing less infeasiblity
            ProblemInfo.alpha = 0; // penalty for wait time
            ProblemInfo.beta = .25; // penalty for excess time
            ProblemInfo.mu = .35; // penalty for tardiness
            ProblemInfo.fi = .45; // penalty for overload

            break;

        default: // tighten it back up to no infeasibility
            setWeights(0); // reset weights to the initial settings

            break;
        }
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
        setTotalNonEmptyNodes(o); //Setting the total number of non empty nodes for each depot
        setTotalCost(o); //Setting the total cost of the schedule
    }
}
