package Zeus;

import java.io.*; //input-output java package


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  An instance of a Truck class is used to maintain information on a
 * truck that is avaialble for scheduling. The Truck class is used by the TruckLinkedList
 * class to maintain information on all the trucks that are avaialble for the problem </p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:<p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class Truck implements java.io.Serializable {
    int truckNo; //truck number
    float maxDuration; //max duration/travel time of route
    float maxCapacity; //max truck capacity
    public float currentDemand; //sum of demands on route
    public float currentDistance; //sum all durations on route
    public float currentCost; // cost of the schedule for this truck
    int currentNodeNos; //current # of nodes on route
    Genius theGeni; //for the genius
    public VisitNodesLinkedList mainVisitNodes; //nodes in the linked list
    public Truck next; //for linked list
    public Truck prev; //for linked list

    /**
 * <p>Constructor for the trucks - not used at the current time</p>
 *
 * */
    public Truck() {
    }

    /**
 * <p>Constructor for the truck. An instace of a truck is created using the truck number,
 * depot number, starting and ending coordinates, the maximum duration or travel time and
 * the maximum capacity or weight. This information is kept at the head node of the linked list
 * so that the shipments or customers being added to the truck can reference this information. The
 * shipments and customers are added to the truck using the mainVisitNodes instance from the
 * VisitNodesLinkedList class.</p>
 * @param currTruckNo id for the truck
 * @param depotNo id for the current depot
 * @param startX starting x coordinate of the truck
 * @param startY starting y coordinate of the truck
 * @param endX ending x coordinate of the truck
 * @param endY ending y coordinate of the truck
 * @param D maximum distance
 * @param Q maximum capacity
 *
 * */
    public Truck(int currTruckNo, int depotNo, float startX, float startY,
        float endX, float endY, float D, float Q) {
        boolean isDiagnostic = false;
        truckNo = currTruckNo; //truck number
        maxDuration = D; //same as costs in Geni
        maxCapacity = Q; //same as demand
        currentDemand = 0; //total current demand
        currentDistance = 0; //total traveled so far
        currentNodeNos = 0; //total number of nodes in route
        next = null;

        //The first and last node in the truck linked list are the depots for the problem
        mainVisitNodes = new VisitNodesLinkedList(); //create an instance

        mainVisitNodes.depotMDVRP(depotNo, startX, startY, endX, endY, Q, D,
            truckNo);

        if (isDiagnostic) {
            System.out.println("Inserted starting and ending nodes in truck");
        }
    }

    //end constructor

    /**
 * <p>Initiate route building procedures using GENI - Not used at the current time</p>
 * @param onRoute route
 * @param depotIndex depot
 * @param one first shipment
 * @param two second shipment
 * */
    public void initRoute(int onRoute, int depotIndex, Shipment one,
        Shipment two) {
        //theGeni.initRoute(onRoute, depotIndex,one,two); //commented 6/29/2001
        theGeni.initRoute(onRoute, one, two);

        //custList.insertFirst(one);
        //custList.insertFirst(two);
    }

    //end initRoute
    //-------------------------------------------------------------------
    //public boolean insertCust(int onRoute, double currDuration, double maxDuration
    //onRoute = index of notYetOnRoute array to be added,
    //insertCust = Customer to try to insert
    //maxDur and maxDemand determined by depot, for PVRP-last truck may exceed
    //maxDuration and maxCapacity, so send in Infinity

    /**
 * <p>Insert customer into the route using GENI. Not used at the current time</p>
 * @param onRoute insertion route
 * @param insertCust insertion customer
 * @param maxDur maximum travel time
 * @param maxDemand maximum demand
 * @return booolen true if inserted, else false
 *
 * */
    public boolean insertCust(int onRoute, Shipment insertCust, double maxDur,
        int maxDemand) {
        //if((currentDemand + insertCust.demand) > maximumDemand ||
        //  (currentDuration + insertCust.duration) > maxDur)
        return (theGeni.insertCust(onRoute, insertCust, maxDur, maxDemand));
    }

    //end insertCust

    /**
 * <p>Get the unique id of the truck</p>
 * @return int unique id of the truck
 *
 * */
    public int getTruckNo() {
        return truckNo;
    }

    /**
 * <p>Returns the current demand for the truck</p>
 * altered method 10/10/03 to use cost function interface
 * Mike McNamara
 * @return current demand for the truck
 */
    public float getDemand() {
        return (float) ProblemInfo.truckLevelCostF.getTotalDemand(this);
    }

    /**
 * <p>Returns the current distance traveled by the truck</p>
 * altered method 10/10/03 to use cost function interface
 * Mike McNamara
 * @return current distance traveled by truck
 */
    public float getDistance() {
        return (float) ProblemInfo.truckLevelCostF.getTotalDistance(this);
    }

    /**
 * <p>Returns the current number of nodes visited by the truck</p>
 * @return number of nodes visited
 */
    public int getNoNodes() {
        currentNodeNos = (int) ProblemInfo.vNodesLevelCostF.getTotalNodes(mainVisitNodes);

        return currentNodeNos;
    }

    /**
 * <p>Returns the maximum capacity of the truck</p>
 * @return maximum capacity
 */
    public float getMaxCapacity() {
        return maxCapacity;
    }

    /**
 * <p>Sets the maximum capacity of the truck</p>
 * @param maxC maximum capacity
 */
    public void setMaxCapicity(float maxC) {
        maxCapacity = maxC;
    }

    /**
 * <p>Returns the maximum duration of the truck</p>
 * @return maximum duration
 */
    public float getMaxDuration() {
        return maxDuration;
    }

    /**
 * <p>Sets the maximum duration of the truck</p>
 * @param maxD maximum duration
 */
    public void setMaxDuration(float maxD) {
        maxDuration = maxD;
    }

    /**
 * <p>Display all the information about the truck on the console</p>
 *
 * */
    public void displayTruck() {
        currentNodeNos = (int) ProblemInfo.vNodesLevelCostF.getTotalNodes(mainVisitNodes);
        System.out.println("     Truck number:             " + truckNo); //truck number
        System.out.println("     Maximum duration:         " + maxDuration); //maximum duration of truck
        System.out.println("     Maximum capacity:         " + maxCapacity); //maximum capacity of truck
        System.out.print("     Current Demand:           " + currentDemand); //current demand on truck
        System.out.println("     Current Distance:         " + currentDistance); //current duration of truck
        System.out.println("     Current No. of Shipments: " + currentNodeNos); //current number of nodes on the truck
    }

    //end displayTruck

    /**
 * <p>Write in short form the information on the depot number truck number and details of
 * the truck to an output file</p>
 * @param depotNo unique number of the depot in which the truck is present
 * @param solOutFile output file for the method
 *
 * */
    public void writeShortTruck(int depotNo, PrintWriter solOutFile) {
        solOutFile.print(depotNo + " " + truckNo + " " + currentDemand + " " +
            currentDistance);
    }

    /**
 * <p>Write in short form the information on the depot number truck number and details of
 * the truck to an output file</p>
 * Added 10/3/03 by Sunil Gurung
 * @param depotNo unique number of the depot in which the truck is present
 * @param solOutFile output file for the method
 * */
    public void writeVRPTWShortTruck(int depotNo, PrintWriter solOutFile) {
        solOutFile.print(depotNo + " " + truckNo + " " + currentDemand + " " +
            currentDistance + " " + currentNodeNos // + " " +
            );
    }

    /**
 * <p>Write in short form the information on the depot number truck number and details of
 * the truck to an output file</p>
 * @param depotNo unique number of the depot in which the truck is present
 * @param solOutFile output file for the method
 *
 * */
    public void writeVRPShortTruck(int depotNo, PrintWriter solOutFile) {
        solOutFile.print(depotNo + " " + truckNo + " " + currentDemand + " " +
            currentDistance + " " + " ");
    }

    //end writeShortTruck

    /**
 * <p>Write in detail form the information on the truck number and details of
 * the truck to an output file</p>
 * @param solOutFile output file for the method
 *
 * */
    public void writeDetailTruck(PrintWriter solOutFile) {
        solOutFile.println("     Truck number:             " + truckNo); //truck number
        solOutFile.println("     Maximum duration:         " + maxDuration); //maximum duration of truck
        solOutFile.println("     Maximum capacity:         " + maxCapacity); //maximum capacity of truck
        solOutFile.print("     Current Demand:           " + currentDemand); //current demand on truck
        solOutFile.println("     Current Duration:         " + currentDistance); //current duration of truck
        solOutFile.println("     Current No. of Shipments: " + currentNodeNos); //current number of nodes on the truck
    }

    /**
 * <p>Write in detail form the information on the truck number and details of
 * the truck to an output file</p>
 * Added 10/3/03 by Sunil Gurung
 * @param solOutFile output file for the method
 * */
    public void writeVRPTWDetailTruck(PrintWriter solOutFile) {
        currentNodeNos = (int) ProblemInfo.vNodesLevelCostF.getTotalNodes(mainVisitNodes);
        solOutFile.println("     Truck number:             " + truckNo); //truck number
        solOutFile.println("     Maximum duration:         " +
            ProblemInfo.maxDistance); //maximum duration of truck
        solOutFile.println("     Maximum capacity:         " +
            ProblemInfo.maxCapacity); //maximum capacity of truck
        solOutFile.print("     Current Demand:           " + currentDemand); //current demand on truck
        solOutFile.println("     Current Duration:         " + currentDistance); //current duration of truck
        solOutFile.println("     Current No. of Shipments: " +
            (currentNodeNos - 2)); //current number of nodes on the truck

        /************************Sunil added 10/10/03*********************/
        solOutFile.println("     totalWaitTime             " +
            ProblemInfo.truckLevelCostF.getTotalWaitTime(this));
        solOutFile.println("     TravelTime                " +
            ProblemInfo.truckLevelCostF.getTotalTravelTime(this));
        solOutFile.println("     totalServiceTime          " +
            ProblemInfo.truckLevelCostF.getTotalServiceTime(this));
        solOutFile.println("     totalDistance covered     " +
            ProblemInfo.truckLevelCostF.getTotalDistance(this));
        solOutFile.println("     totalTardinessTime        " +
            ProblemInfo.truckLevelCostF.getTotalTardinessTime(this));
        solOutFile.println("     totalExcessTime           " +
            ProblemInfo.truckLevelCostF.getTotalExcessTime(this));
        solOutFile.println("     totalOverload             " +
            ProblemInfo.truckLevelCostF.getTotalOverload(this));
    }

    /**
 * <p>Write in detail form the information on the truck number and details of
 * the truck to an output file</p>
 * This method added 8/30/03 by Mike McNamara
 * @param solOutFile output file for the method
 * */
    public void writeVRPDetailTruck(PrintWriter solOutFile) {
        solOutFile.println("     Truck number:             " + truckNo); //truck number
        solOutFile.println("     Maximum duration:         " + maxDuration); //maximum duration of truck
        solOutFile.println("     Maximum capacity:         " + maxCapacity); //maximum capacity of truck
        solOutFile.println("     Current Demand:           " + currentDemand); //current demand on truck
        solOutFile.println("     Current Duration:         " + currentDistance); //current duration of truck
    }

    //end displayTruck
}


//end of Truck class
