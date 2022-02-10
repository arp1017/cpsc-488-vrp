package Zeus;

import java.io.*;

import java.lang.*;

import java.util.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  This class maintains all the customers that have been allocated to a truck.
 * The truck allocation of customers is a linked list of PointCell's. Each
 * Trucks consists of a MDVRPnode instance of a linked list and the collection
 * of PointCell's pointed to by MDVRPNode is the set of shipments to be
 * visited by a truck. Note: Capacity and Demand are used interchangeably.s
 * The PointCell maintains some information about the shipments but the full
 * information on the shipments has to be accessed from the ShipmentLinkedList.</p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company:</p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class VisitNodesLinkedList implements java.io.Serializable {
    private static long numLocalOpt = 0;
    private static long maxLocalOpt = 10;
    public static float MAX_COST = 1000000;
    private int size = 0; //number of nodes in this route- counts the starting and ending depot;

    //first cell of the list is grounded
    private PointCell head = null; //head of the linked list;
    private PointCell last = null; //last node of the linked list;
    public float currentDistance; //total travel time of all the nodes in the the current route
    public float currentCapacity; //total capacity of all the nodes in the current route
    public float currentDemand; //total demand of all the nodes in the current route
    public float currentTardiness; //total tardiness of all the nodes in the current route
    public float currentOverload; //total overload of all the nodes in the current route
    public float currentExcessTime; //total excess time of all the nodes in the current route
    public float currentWaitTime; //total wait time of all the nodes in the current route
    public float currentTotalTravelTime; //total travel time of all the nodes in the current route
    public float currentServiceTime; //total service time of all the nodes in the current route
    public float currentCost; // total cost of this schedule
    public float maxCapacity = 0; //maximum capacity of the vehicle servicing this route;
    public float maxTime = 0; //maximum time the vehicle is allowed to travel;
    private float lastX = 0; //coordinates of the last customer in the previous constructed
    private float lastY = 0; //route or 0;
    public float depoX = 0; //depot coordinates- X coordinate;
    public float depoY = 0; //Y coordinate
    public int depotNo; //depot no. to which the linked list is attached
    public int truckNo; //truck no. to which the linked list is attached

    //count the total number of opts that took place
    int totalOneOpt; //count of 1-opts executed
    int totalTwoOpt; //count of 2-opts executed
    int totalThreeOpt; //count of 3-opts executed
    int totalKOneOpt; //count of k-one-opts executed
    int totalKTwoOpt; //count of k-two-opts executed
    int totalKThreeOpt; //count of k-three-opts executed

    /**
    * <p>Constructor for the VisitingNodesLinkedList - not used at the current time</p>
    *
    * */
    public VisitNodesLinkedList() {
    }

    /**
    * <p>First node in the list with the depot information for the MDVRP
    * problem. The first node maintains all the information on the
    * depot from which the customers are being serviced. This method
    * creates that node and adds it to the linked list.</p>
    * this method modified by Mike McNamara 9/29/03
    * modification is the initialization of the depot slacktime to reflect that
    * it never closes
    * @param depot     depot number from where the customers are being serviced
    * @param startX    x coordinate of the starting location of the service
    * @param startY    y coordinate of the starting location of the service
    * @param endX      x coordinate of the ending location of the service
    * @param endY      y coordinate of the ending location of the service
    * @param lMaxCap   maximum capacity of the truck
    * @param lMaxTime  maximum duration or travel time of the truck
    * @param truck     truck number servicing the customers
    *
    * */
    public void depotMDVRP(int depot, float startX, float startY, float endX,
        float endY, float lMaxCap, float lMaxTime, int truck) {
        //the starting depot has a has an index value of 0
        //the ending depot has an index value of -1
        boolean isDiagnostic = false;
        maxCapacity = lMaxCap;
        maxTime = lMaxTime;
        head = new PointCell(0, depot, startX, startY, maxCapacity, maxTime,
                null, null);
        last = new PointCell(-1, depot, endX, endY, maxCapacity, maxTime, null,
                null);
        head.next = last;
        head.prev = null; //head.prev = last;
        last.next = null; //head.prev = last;
        last.prev = head;
        this.depotNo = depot;
        this.truckNo = truck;
        head.slackTime = 999999999; // the depot never closes
        last.slackTime = 999999999; // the depot never closes

        ++size; //increment size of the route for 2 nodes;
        ++size;

        if (isDiagnostic) {
            System.out.println(
                "depotMDVRP: Adding customers to the initial trucks :Constructor size = " +
                size + "\n");
        }
    }

    //end depotMDVRP

    /**
    * <p>Increment the total capacity/weight of customers/shipments serviced for the list when
    * a node is added to the list. When a new node is added, the capacity of the list needs to
    * be incremented by the capacity of the added node.</p>
    * @param incWeight capacity/weight by which currentCapacity is to be incremented
    * @return float total capacity/weight of customers/shipments serviced in the list
    * */
    public float incCurrentCapacity(float incWeight) {
        currentCapacity = currentCapacity - incWeight;

        return currentCapacity;
    }

    /**
    * <p>Decrement the total capacity/weight of customers/shipments serviced for the list when
    * a node is added to the list. When a new node is deleted, the capacity of the list needs to
    * be decremented by the capacity of the deleted  node.</p>
    * @param decWeight capacity/weight by which currentCapacity is to be decremented
    * @return float total capacity/weight of customers/shipments serviced in the list
    * */
    public float decCurrentCapacity(float decWeight) {
        currentCapacity = currentCapacity - decWeight;

        return currentCapacity;
    }

    /**
    * <p>Set the total capacity/weight of customers/shipments serviced</p>
    * @param capacity current capacity
    * @return float total capacity/weight of customers/shipments serviced
    * */
    public float setCurrentCapacity(float capacity) {
        currentCapacity = capacity;

        return currentCapacity;
    }

    /**
    * <p>Get the total capacity/weight of customers/shipments serviced</p>
    * @return float total capacity/weight of customers/shipments serviced
    * */
    public float getCurrentCapacity() {
        return currentCapacity;
    }

    /**
    * <p>set total capacity/weight of customers/shipments serviced for the list.</p>
    * @param dist distance to which it should be set
    * @return float total capacity/weight of customers/shipments serviced in the list
    * */
    public float setCurrentDistance(float dist) {
        currentDistance = dist;

        return currentDistance;
    }

    /**
    * <p>Get the sum total of the distance travelled in the VisitingNodes linked list</p>
    * @return sum total of the distance travelled in the VisitingNodes linked list
    *
    * */
    public float getCurrentDistance() {
        return currentDistance;
    }

    /**
    * <p>Return a pointer to the head of the linked list</p>
    * @return pointer to the head of the linked list
    * */
    public synchronized PointCell first() {
        return head;
    }

    /**
    * <p>Return a pointer to the last node of the linked list</p>
    * @return pointer to the last node of the linked list
    * */
    public synchronized PointCell last() {
        //define a last pointer that will always point to the last item
        return last.prev;
    }

    /**
    * <p>Removed all nodes to the linked list except for the first
    * and last nodes</p>
    * */
    public synchronized void emptyList() {
        //PointCell last = last();
        size = 2;
        head.next = last;
        last.prev = head;

        //head.next = last;
    }

    /**
    * <p>Display the linked list of trucks from first to last</p>
    * */
    public void displayForwardList() {
        System.out.println("          List (first to last): ");
        System.out.println("          Total number of cells: " + this.size);

        PointCell current = head;

        while (current != null) {
            current.displayPointCell();
            current = current.next;
        }

        System.out.println("");
    }

    /**
    * <p>Display the linked list of shipments from last to first</p>
    * */
    public void displayBackwardList() {
        System.out.print("List (last to first): ");

        PointCell current = last;

        while (current != null) {
            current.displayPointCell();
            current = current.prev;
        }

        System.out.println("");
    }

    /**
    * <p>Display all the information in the linked list for the MDVRP problem.
    * The information displayed are the duration, capacity and shipment/customer
    * id's in the linked  list.</p>
    * */
    public void displayForwardKeyListMDVRP() {
        PointCell currPtr = head;
        int j = 0; //count no. of values printed per line

        if (currPtr != null) {
            //print the duration and capacity for the current route
            System.out.print(currentDistance + " " + currentCapacity + " ");
        }

        while (currPtr != null) {
            System.out.print(currPtr.index);
            System.out.print(" ");
            currPtr = currPtr.next;
            j++;

            if (j > 10) {
                System.out.println();
                j = 0;
            }
        }

        System.out.println("");
    }

    /**
    * <p>Display all the information in the linked list for the VRP problem.
    * The information displayed are the duration, capacity and shipment/customer
    * id's in the linked  list.</p>
    * This method added 8/30/03 by Mike McNamara
    * */
    public void displayForwardKeyListVRP() {
        PointCell currPtr = head;

        //System.out.println(toString());
        if (currPtr != null) {
            //print the duration and capacity for the current route
            System.out.println("Total distance traveled: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalDistance(this));
            System.out.println("Total capacity filled: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalDemand(this));
            System.out.println("Total travel time: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalTravelTime(this));
            System.out.println("Total service time: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalServiceTime(this));
            System.out.println("Total wait time: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalWaitTime(this));
            System.out.println("Total tardiness: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(this));
            System.out.println("Total excess time: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalExcessTime(this));
            System.out.println("Total overload: " +
                (float) ProblemInfo.vNodesLevelCostF.getTotalOverload(this));
            System.out.println("Number of Local 1-Opts: " + totalKOneOpt);
            System.out.println("Number of Local 2-Opts: " + totalKTwoOpt);
            System.out.println("Number of Local 3-Opts: " + totalKThreeOpt);
        }

        System.out.print("Customers serviced in Route: ");

        while (currPtr != null) {
            System.out.print(currPtr.index);
            System.out.print(" ");
            currPtr = currPtr.next;
        }

        System.out.println("");
    }

    /**
    * <p>Display the id's of shipments from first to last.</p>
    * */
    public void displayForwardKeyList() {
        //System.out.println("List (first to last): ");
        PointCell current = head;
        int j = 0; //count no. of values printed per line

        while (current != null) {
            System.out.print(current.index);
            System.out.print(" ");
            current = current.next;
            j++;

            if (j > 10) {
                System.out.println();
                j = 0;
            }
        }

        System.out.println("");
    }

    /**
    * <p>Display the id's of shipments from last to first.</p>
    * */
    public void displayBackwardKeyList() {
        System.out.println("List (last to first): ");

        PointCell current = last;
        int j = 0; //count no. of values printed per line

        while (current != null) {
            System.out.print(current.index);
            System.out.print(" ");
            current = current.prev;
            j++;

            if (j > 10) {
                System.out.println();
                j = 0;
            }
        }

        System.out.println();
        System.out.println();
    }

    /**
    * <p>Load customer/shipment nodes into an array to be checked for consistency.</p>
    * @param nodesArray array into which the nodes are loaded
    * */
    public void checkNodesMDVRP(int[] nodesArray) {
        PointCell currPtr = head;
        currPtr = currPtr.next; //skip the first depot coordinate

        while (currPtr != last) //loop till the ending depot is found
         {
            nodesArray[currPtr.index] = nodesArray[currPtr.index] + 1;
            currPtr = currPtr.next;
        }
    }

    /**
    * <p>Checks if the linked list is empty</p>
    * @return boolean true if list in empty, false otherwise
    * */
    public boolean ifVisitListEmpty() {
        if ((head == null) || ((head.next) == last)) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * <p>Checks if the list has only one node</p>
    * @return boolean true if list has one node only, false otherwise
    * */
    public synchronized int checkList() {
        if ((head.next) == last) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
    * <p>Calculates the lateness for a VRPTW problem</p>
    * @param  lLatTime   latest pickuptime for the shipment
    * @param  lServTime  service for the shipment location
    * @param  lX         x coordinate of the shipment
    * @param  lY         y coodinate of the shipment
    * @param  successor  successor shipment for calculating lateness
    * @return float      absolute latest arrival time of the customer
    *
    * */
    private synchronized float calculateLt(float lLatTime, float lServTime,
        float lX, float lY, PointCell successor) {
        float lt;

        if (successor != last) {
            //System.out.println(" Lat = "+lLatTime+" SUC.lt = "+sucsessor.lt+ " Dist = "+dist(lX,sucsessor.xCoord,lY,sucsessor.yCoord));
            if ((successor.lt -
                    dist(lX, successor.xCoord, lY, successor.yCoord) -
                    lServTime) < 0) {
                lt = min(lLatTime,
                        (float) (successor.lt -
                        dist(lX, successor.xCoord, lY, successor.yCoord) -
                        lServTime));
            } else {
                lt = min(lLatTime,
                        (float) (successor.lt -
                        dist(lX, successor.xCoord, lY, successor.yCoord) -
                        lServTime));
            }
        } else {
            lt = lLatTime;
        }

        return lt;
    }

    /**
    * <p>Finds the minimum between the two numbers</p>
    * @param num1 first number
    * @param num2 second number
    * @return float maximum of the two numbers
    * */
    private synchronized float min(float num1, float num2) {
        if (num1 < num2) {
            return num1;
        } else {
            return num2;
        }
    }

    /**
    * <p>Finds the maximum between the two numbers</p>
    * @param num1 first number
    * @param num2 second number
    * @return float maximum of the two numbers
    * */
    private synchronized float max(float num1, float num2) {
        if (num1 > num2) {
            return num1;
        } else {
            return num2;
        }
    }

    /**
    * <p>Insert a customer into the linked list for the MDVRP problem</p>
    * @param iIndex index of the customer/shipment
    * @param lX x coordinate of the customer
    * @param lY y coordinate of the customer
    * @param lDemand demand of the customer
    * @return int positive value,  insertion was successful, otherwise -1
    * */
    public int insertMDVRP(int iIndex, float lX, float lY, float lDemand) {
        float totalDist;
        float totalCap;
        float minDistance = 999999999;
        PointCell tempCellPtr = head;
        PointCell insertPointCellPtr = null; //postion for inserting the current cell

        try {
            //create an instance of PointCell with the shipment information
            PointCell newCell = new PointCell(iIndex, lX, lY, lDemand);

            //Try inserting it into each vertex and keep track of the
            //insertion location with the lowest cost
            if (head.next == last) //there are no shipments in the list
             {
                //insert the node between head and last
                insertAfterCellMDVRP(head, newCell);

                //compute the distance
                totalDist = calculateDistance();

                //compute the capacity
                totalCap = calculateCapacity();

                //if the insertion of the customer does not exceed the total
                //capacity and distance then accept the insertion else undo the
                //insertion
                if ((head.maxTravelTime < totalDist) ||
                        (head.maxCapacity < totalCap)) {
                    removeCell(newCell);

                    return -1;
                }

                //accept the insertion of the customer
                //update capacity and travel time
                //currentDistance = totalDist;
                //currentCapacity = totalCap;
                setCurrentDistance(totalDist);
                setCurrentCapacity(totalCap);
                ++size;

                return head.index;
            } else //else there are shipments on the list
             {
                tempCellPtr = head; //start with the first node
                insertAfterCellMDVRP(tempCellPtr, newCell);

                //compute the distance
                totalDist = calculateDistance();

                //compute the capacity
                totalCap = calculateCapacity();

                if ((head.maxTravelTime >= totalDist) &&
                        (head.maxCapacity >= totalCap)) { //feasible

                    //log the distance
                    minDistance = totalDist;

                    //mark the insertion spot
                    insertPointCellPtr = tempCellPtr;
                } else { //not feasible
                    minDistance = 999999999;
                }

                //undo the insertion for trying it at other vertices
                removeCell(newCell);

                //loop through all the insertion points
                tempCellPtr = tempCellPtr.next;

                while (tempCellPtr != last) //do not want to insert nodes after the last node
                 {
                    insertAfterCellMDVRP(tempCellPtr, newCell);

                    //compute the distance
                    totalDist = calculateDistance();

                    //compute the capacity
                    totalCap = calculateCapacity();

                    if ((head.maxTravelTime >= totalDist) &&
                            (head.maxCapacity >= totalCap)) // feasible
                     {
                        //log the distance
                        if (totalDist < minDistance) {
                            minDistance = totalDist;

                            //mark the insertion spot
                            insertPointCellPtr = tempCellPtr;
                        }
                    }

                    //else   //not feasible
                    //minDistance = 999999999;
                    //undo the insertion
                    removeCell(newCell);
                    tempCellPtr = tempCellPtr.next;
                }
            }

            //check if an insertion location has been located.  if true, add the
            //cell else return null
            if (insertPointCellPtr != null) {
                insertAfterCellMDVRP(insertPointCellPtr, newCell);

                //compute the distance after final insertion
                totalDist = calculateDistance();

                //compute the capacity after final insertion
                totalCap = calculateCapacity();

                //currentDistance = totalDist;
                //currentCapacity = totalCap;
                setCurrentDistance(totalDist);
                setCurrentCapacity(totalCap);
                ++size;
            } else //cell could not be inserted
             {
                return -1;
            }
        } catch (Exception ie) {
            System.out.println("Caught in Depot.insertCell " + ie);
        }

        // return afterCell.next;
        if (insertPointCellPtr != null) {
            return insertPointCellPtr.index;
        } else {
            return -1;
        }
    }

    /**
    * <p>VisitNodesLinkedList level insert function for VRPTW. Loops through all possible
    * places to insert shipment and chooses the one with the lowest cost</p>
    * this method was added on 9/12/03 by Sunil Gurung
    * @param index shipment number
    * @param x x-coordinate of shipment
    * @param y y-coordinate of shipment
    * @param demand amount of space in truck the shipment will fill
    * @param earTime earliest time to service shipment
    * @param latTime latest time to service shipment
    * @param servTime time to service shipment
    * @return whether the shipment was inserted or not
    */
    public synchronized boolean insertVRPTW(int index, float x, float y,
        float demand, float earTime, float latTime, float servTime) {
        PointCell headCell = null;
        PointCell nextCell = null;
        boolean status = false;

        headCell = this.first();
        nextCell = headCell.next;

        //check to see if the new cell will place truck over capacity,
        //if so, return that it cannot be inserted
        if ((ProblemInfo.vNodesLevelCostF.getTotalDemand(this) + demand) > ProblemInfo.maxCapacity) {
            return false;
        }

        if (headCell.next == this.last) {
            //no customers in the list
            //insert it after head
            insert(index, x, y, demand, earTime, latTime, servTime,
                headCell.index);

            //	    recalculateSlackTime(); //recalculating the slackTime for the customers
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } else {
            //there are customers in the list
            // insertDist will return a string with the cost and index for the best place
            // to insert the node, extract the cost and index, compare to see if insertion
            // is feasable, insert node if feasable
            // call insertDist and extract the index number to insert customer into the list
            int kIndex = calcPushCost(index, x, y, demand, earTime, latTime,
                    servTime);

            if (kIndex > -1) {
                //insert cell at the position with the lowest cost
                insert(index, x, y, demand, earTime, latTime, servTime, kIndex);
                recalculateSlackTime(); //recalculating the slackTime for the customers

                //calculate new data for linked list
                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
            } else {
                return false; // insertion is infeasable
            }
        }

        return true; // insertion succeeded
    }

    /**
    * <p>Insert a customer into the linked list after the customer with index Index</p>
    * @param iIndex index of the customer/shipment
    * @param lX x coordinate of the customer
    * @param lY y coordinate of the customer
    * @param lDemand demand of the customer
    * @param kIndex customer index after which current customer is to be inserted
    * @return Pointcell kIndex is inserted successfully, null otherwise
    * */
    public synchronized PointCell insert(int iIndex, float lX, float lY,
        float lDemand, int kIndex) {
        boolean isDiagnostic = false;
        PointCell afterCell = head;

        if (kIndex == -1) //cannot insert the node
         {
            System.out.println("VisitNodes: insert - kindex is -1");

            return null;
        }

        try {
            afterCell = getPointCell(kIndex); //get the location of th ecell to be inserted after

            PointCell nextCell = afterCell.next; // the cell after aftercell

            if (isDiagnostic) {
                System.out.println("Inserting after cell = " + afterCell.index);
            }

            PointCell newCell = new PointCell(iIndex, lX, lY, lDemand);

            nextCell.prev = newCell;
            afterCell.next = newCell;
            newCell.prev = afterCell;
            newCell.next = nextCell;

            if (isDiagnostic) {
                System.out.println("FINAL: " + toString());
            }

            ++size;
        } catch (Exception ie) {
            System.out.println("Caught in Depot.insert " + ie);
        }

        return afterCell.next;
    }

    /**
    * <p>Insert a customer into the linked list after the afterCellPtr node for
    * the MDVRP problem</p>
    * @param afterCellPtr node after which current node is to be inserted
    * @param newCell current cell to be inserted
    * @return Pointcell afterCellPtr if insertion is successful, null otherwise
    * */
    public synchronized PointCell insertAfterCellMDVRP(PointCell afterCellPtr,
        PointCell newCell) {
        PointCell prev = null;
        PointCell next = null;

        if (afterCellPtr == null) {
            return null;
        }

        try {
            prev = afterCellPtr;
            next = afterCellPtr.next;
            newCell.next = next;
            newCell.prev = prev;
            prev.next = newCell;
            next.prev = newCell;
        } catch (Exception ie) {
            System.out.println("Error in insertAfterCellMdvrp: Nodes are " +
                afterCellPtr.index + " " + newCell.index);
        }

        return prev;
    }

    /**
    * <p>Insert a customer into the linked list after the afterCellPtr node for
    * the VRPTW problem</p>
    * method added 11/5/03 by Mike McNamara and Sunil Gurung
    * @param afterCellPtr node after which current node is to be inserted
    * @param newCell current cell to be inserted
    * @return Pointcell afterCellPtr if insertion is successful, null otherwise
    * */
    public synchronized PointCell insertAfterCellVRPTW(PointCell afterCellPtr,
        PointCell newCell) {
        PointCell prev = null;
        PointCell next = null;

        if (afterCellPtr == null) {
            return null;
        }

        try {
            prev = afterCellPtr;
            next = afterCellPtr.next;
            newCell.next = next;
            newCell.prev = prev;
            prev.next = newCell;
            next.prev = newCell;
        } catch (Exception ie) {
            System.out.println("Error in insertAfterCellVrptw: Nodes are " +
                afterCellPtr.index + " " + newCell.index);
        }

        //need to recalculate ArrTime - for sucsessors and u, lt - for u and predecessors,
        //MAC and MDC values must be recalculated;
        performRecalculates();
        ++size;
        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);

        return prev;
    }

    /**
    * <p>Insert a shipment,iIndex, node for the VRPTW problem
    * after the shipment index kIndex</p>
    * ///////////////////Used by Mike and Sunil/////////////////////////////////////////
    * @paramv iIndex   index of the shipment
    * @param lX         x coordinate of the shipment
    * @param lY         y coordinate of the shipment
    * @param lDemand    pickup capacity for the shipment
    * @param lEar       earliest pickup time for the shipment
    * @param lLat       latest pickuptime for the shipment
    * @param lServ      service for the shipment location
    * @param kIndex     shipment index kIndex after which new shipment is to be inserted
    * @return           PointCell node that was inserted, null otherwise
    * */
    public synchronized PointCell insert(int iIndex, float lX, float lY,
        float lDemand, float lEar, float lLat, float lServ, int kIndex) {
        PointCell afterCell = head;

        try {
            afterCell = getPointCell(kIndex);

            PointCell nextCell = afterCell.next;
            PointCell newCell = new PointCell(iIndex, lX, lY, lDemand, lEar,
                    lLat, lServ,
                    calculateLt(lLat, lServ, lX, lY, afterCell.next),
                    ProblemInfo.maxCapacity - afterCell.DC,
                    (ProblemInfo.maxCapacity - (afterCell.DC + lDemand)),
                    afterCell.next, afterCell);

            nextCell.prev = newCell;
            afterCell.next = newCell;
            newCell.prev = afterCell;
            newCell.next = nextCell;

            if (afterCell == last.prev) {
                last.prev = newCell;
            }

            //need to recalculate ArrTime - for sucsessors and u, lt - for u and predecessors,
            //MAC and MDC values must be recalculated;
            performRecalculates();
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
            ++size;
        } catch (Exception ie) {
            System.out.println("Caught in VisitNodesLinkedList.insert " + ie);
        }

        return afterCell.next;
    }

    /**
    * <p>Insert a shipment,iIndex, node for the VRPTW problem
    * after the shipment cell afterCell</p>
    * @param iIndex     index of the shipment
    * @param lX        x coordinate of the shipment
    * @param lY        y coordinate of the shipment
    * @param lDemand   pickup capacity for the shipment
    * @param afterCell shipment cell after which new shipment is to be inserted
    * @return          PointCell node that was inserted
    *
    * */
    public synchronized PointCell insert(int iIndex, float lX, float lY,
        float lDemand, PointCell afterCell) {
        if (afterCell == null) {
            System.out.println("Aftercell is null in VisitingNodesLinkedList\n");
        }

        try {
            PointCell nextCell = afterCell.next;
            PointCell newCell = new PointCell(iIndex, lX, lY, lDemand);

            nextCell.prev = newCell;
            afterCell.next = newCell;
            newCell.prev = afterCell;
            newCell.next = nextCell;
            ++size;
        } catch (Exception ie) {
            System.out.println("Caught in Depot.insertCell " + ie);
        }

        return afterCell.next;
    }

    /**
    * <p>Append a shipnment,iIndex, node for the VRPTW problem
    * at the end of the list</p>
    * @param iIndex     index of the shipment
    * @param lX        x coordinate of the shipment
    * @param lY        y coordinate of the shipment
    * @param lDemand   pickup capacity for the shipment
    * @param lEar      earliest pickup time for the shipment
    * @param lLat      latest pickuptime for the shipment
    * @param lServ     service for the shipment location
    *
    * */
    public synchronized void append(int iIndex, float lX, float lY,
        float lDemand, float lEar, float lLat, float lServ) {
        try {
            //System.out.println("1: appending: "+  iIndex);
            //PointCell t = insert(iIndex,lX,lY,lDemand,lEar,lLat,lServ,last.prev);
            //System.out.println("last.prev = " + last.prev);

            /*if( t.arrTime >= t.latTime){
            remove(iIndex);//cost*1000; //punishment for breaking time constraints;
            return false;
            }
            if(calculateCapacity() > maxCapacity){
            remove(iIndex);//cost*2000; //punishment for breaking capacity constraints;
            return false;
            }
            if(calculateDistance() > maxDistance){
            remove(iIndex); //punishment for breaking distance constraints;
            return false;
            }*/

            //System.out.println("2: after INSERTION");
        } catch (Exception err) {
            System.out.println("ERROR. in append");
        }

        //return true;
    }

    /**
    * <p>Remove the node pointed to by removeNodePtr
    * after the shipment cell afterCell</p>
    * @param removeNodePtr   index of the shipment
    * @return Point          node that was removed
    *
    * */
    public PointCell removeCell(PointCell removeNodePtr) {
        PointCell prevCell;
        PointCell nextCell;

        //do not remove if the node is the head node or the last node
        if (removeNodePtr == head) {
            System.out.println("Node removal failed: Head node");

            return null;
        }

        if (removeNodePtr == last) {
            System.out.println("Node removal failed: Last node");

            return null;
        }

        //remove the node and return the removed node
        prevCell = removeNodePtr.prev;
        nextCell = removeNodePtr.next;
        prevCell.next = nextCell;
        nextCell.prev = prevCell;
        removeNodePtr.next = null;
        removeNodePtr.prev = null;

        return removeNodePtr;
    }

    /**g
    * <p>Remove the node with index iIndex from the list</p>
    * @param iIndex  Node to be removed
    * @return int index of shipment scheduled before removed shipment, else negative
    * */
    public synchronized int remove(int iIndex) {
        PointCell p = head;
        int indx = -1;

        try {
            p = getPointCell(iIndex);

            //System.out.print("found:"+p);
            //System.out.print("index:"+p.index);
            if (p != null) {
                if (getSize() != 3) {
                    indx = p.prev.index;
                    (p.prev).next = p.next;

                    //if(p.next!=null)
                    (p.next).prev = p.prev;
                } else {
                    indx = 0;
                    head.next = last;

                    last.prev = head;

                    //last.next = head;
                }

                --size;

                //need to recalculate ArrTime - for sucsessors and u, lt - for u and predecessors,
                //MAC and MDC values must be recalculated;
                performRecalculates();
                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
            } else {
                //                System.err.println("Error in remove: null");
                return -1;
            }
        } catch (Exception xs) {
            System.err.println("Error in remove: " + xs);
            xs.printStackTrace();
        }

        return indx;
    }

    /**
    * <p>Remove the first item from the list
    * after the shipment cell afterCell</p>
    *
    * */
    public synchronized void removeFirstPoint() {
        try {
            if (head.next != last) {
                ((head.next).next).prev = head;
                head.next = (head.next).next;
                recalculate_arrTime();
                recalculate_lt();
                recalculateAC_DC();
                --size;
            }
        } catch (Exception xs) {
            System.out.println("error in removeFirst " + xs);
        }
    }

    /**
    * <p>Transform all attributes of the point into the string</p>
    * //////////////////Sunil Gurung 10/26/03/////////////////////
    * @param iNodeNum index of the shipment
    * @return String string with all the attributes of the shipment
    * */
    public synchronized String pointString(int iNodeNum) {
        PointCell p = head.next;
        String s = "";
        int i = 1;

        //System.out.print("head.next.index = p.index "+p.index+" iNodeNum = "+iNodeNum+ " i = "+i);
        while ((p != last) && (i < iNodeNum)) {
            p = p.next;
            i++;
        }

        s = s + p.index + ';' + p.xCoord + ';' + p.yCoord + ';' + p.demand +
            ';' + p.earTime + ';' + p.latTime + ';' + p.servTime;

        //System.out.println(" Returning s = "+s);
        return s;
    }

    /**
    * <p>Get x coordinate of the shipment with index iNodeNum. The node numbers for
    * the shipments are assumed to be in ascending order.</p>
    * @param iNodeNum index of the shipment
    * @return float X coordinate of the shipment with index iNodeNum
    *
    * */
    public synchronized float pointX(int iNodeNum) {
        PointCell p = head.next;
        p = head.next;

        float val = 0;
        int i;
        i = 1;

        //System.out.print("head.next.index = p.index "+p.index+" iNodeNum = "+iNodeNum+ " i = "+i);
        while ((p != last) && (i < iNodeNum)) {
            p = p.next;
            i++;
        }

        val = p.xCoord;

        return val;
    }

    /**
    * <p>Get y coordinate of the shipment with index iNodeNum. The node numbers for
    * the shipments are assumed to be in ascending order.</p>
    * @param iNodeNum index of the shipment
    * @return float Y coordinate of the shipment with index iNodeNum
    *
    * */
    public synchronized float pointY(int iNodeNum) {
        PointCell p = head.next;
        p = head.next;

        float val = 0;
        int i;
        i = 1;

        //System.out.print("head.next.index = p.index "+p.index+" iNodeNum = "+iNodeNum+ " i = "+i);
        while ((p != last) && (i < iNodeNum)) {
            p = p.next;
            i++;
        }

        val = p.yCoord;

        return val;
    }

    /**
    * <p>Get the PointCell with index iIndex</p>
    * @param iIndex index of the shipment
    * @return PointCell cell with index iIndex, else null
    *
    * */
    public synchronized PointCell getPointCell(int iIndex) {
        PointCell p = head;
        PointCell e = null;

        //System.out.print("Searching for index # = "+iIndex+" ");
        while (p != last) {
            //System.out.println("p.number = "+p.number+"\n");
            if (p.index == iIndex) {
                e = p;

                return e;

                //System.out.print("found ");
            }

            p = p.next;
        }

        return e;
    }

    /**
    * <p>Get the cell index that is the nth cell in the list</p>
    * @param n nth cell in the list
    * @return int index of the nth cell in the list, else negative value
    *
    * */
    public synchronized int getPoint(int n) {
        PointCell p = head;

        for (int i = 0; (i < n) && (p != null); i++)
            p = p.next;

        if (p != null) {
            return p.index;
        } else {
            return -1;
        }
    }

    /**
    * <p>Display the entire linked list</p>
    * @return String display information of the head cell
    *
    * */
    public synchronized String toString() {
        try {
            return (toString(head));
        } catch (Exception e) {
            System.out.println("VisitNodesLinkedLists: toString" + e);
        }

        return (toString(head));
    }

    /**
    * <p>Display the detail attributes of the cell for the VRPTW problem</p>
    * ////////////////Modified by Sunil 10/25/03/////////////////////////
    * @param p PointCell whose attributes are to be displayed
    * @return String String will all the attribute information of the cell
    * */
    public synchronized String toString(PointCell p) {
        String s = new String();

        try {
            //PrintStream ostream = new PrintStream(
            //                             new FileOutputStream("D:/out.txt"));
            s = s +
                
                // "\nindex\tdemand\tearTime\tlatTime\tservTime\tarrTime\tlt\tAC\tDC\twaitTime\tslackTime\n";
                "\nindex\tx\ty\tdemand\tearTime\tlatTime\tservTime\tarrTime\tlt\tAC\tDC\twaitTime\tslackTime\n";

            //System.out.println(s);
            if (head == null) {
                System.out.println("HEAD=NULL");
            }

            //System.out.println("POINTNEXT: "+p.next+" POINTPREV: "+p.prev);
            while (p != last) {
                if (p.demand != -1) {
                    //System.out.print("node "+ p.index);
                    s = s + p.index + '\t' + p.xCoord + '\t' + p.yCoord + '\t' +
                        p.demand + '\t' + p.earTime + '\t' + p.latTime + '\t' +
                        p.servTime + '\t' + p.arrTime + '\t' + p.lt + '\t' +
                        p.AC + '\t' + p.DC + '\t' + p.waitTime + '\t' +
                        p.slackTime + " \n";

                    //pointString(p.index);
                    //System.out.println(" * "+s+" * ");
                }

                //System.out.println("POINTNEXT: "+p.next+" POINTPREV: "+p.prev);
                //System.out.println("S = "+s);
                p = p.next;
            }

            //float tard = calcTotalTardiness();
            s = s + "\nCarrier route capacity = " + calculateCapacity() +
                " time = " + CalculateTotalTime() + " total distance = " +
                calculateDistance() + " tard = " + calcTotalWaitTardiness() +
                " overl = " + calcTotalOverload() + " excesst = " +
                calcTotalExcessTime();

            /* p = last;
            while(p!=head){
            if(p.demand!=-1){
            //System.out.print("node "+ p.index);
            s=s+" "+p.index
            + ' '+p.xCoord+ ' '+p.yCoord +' '+
            p.demand + ' '+p.earTime+' '+p.latTime+' '+p.servTime+
            ' '+p.arrTime+' '+p.lt+' '+p.AC+' '+p.DC+' '+p.waitTime+" \n";
            //pointString(p.index);
            //System.out.println(" * "+s+" * ");
            }
            //System.out.println("POINTNEXT: "+p.next+" POINTPREV: "+p.prev);
            //System.out.println("S = "+s);
            p = p.prev;
            }*/

            //ostream.println(s);
            //ostream.close();
        } catch (Exception e) {
            System.out.println("ERROR in toString!!!");
        }

        return s;
    }

    /**
    * <p>Calculate the Euclidean distance between two points</p>
    * @param x1 x coordinate of first point
    * @param y1 y coordinate of first point
    * @param x2 x coordinate of second point
    * @param y2 y coordinate of second point
    * @return float Euclidean distance between the two points
    *
    * */
    public synchronized float dist(float x1, float x2, float y1, float y2) {
        float d = 0;

        try {
            d = (float) Math.sqrt((double) (((x2 - x1) * (x2 - x1)) +
                    ((y2 - y1) * (y2 - y1))));
        } catch (ArithmeticException e) {
            System.out.println(e);
        }

        return d;
    }

    /**
    * <p>Calculate the total distance for all the points in the list</p>
    * @return float total distance for all the points in the list
    *
    * */
    public synchronized float calculateDistance() {
        float distTravelled = 0;
        PointCell left = head;
        PointCell right = head.next;

        /*if(left.next == null){
        return 0;
        }else{*/
        while (right != last) { //head){
            distTravelled = distTravelled +
                dist(left.xCoord, right.xCoord, left.yCoord, right.yCoord);

            //System.out.println(distTravelled);
            left = right;
            right = right.next;
        }

        //distance back to the depot
        distTravelled = distTravelled +
            dist(left.xCoord, right.xCoord, left.yCoord, right.yCoord);

        return distTravelled;
    }

    /**
    * <p>Calculate the total capacity for all the points in the list</p>
    * @return float total capacity for all the points in the list
    *
    * */
    public synchronized float calculateCapacity() {
        float cap = 0;
        PointCell left = head;
        PointCell right = head.next;

        if (left.next == null) {
            return 0;
        } else {
            while (right != null) {
                cap = cap + left.demand;
                left = right;
                right = right.next;
            }

            cap = cap + left.demand;

            return cap;
        }
    }

    /**
    * <p>Returns the x and y coordinates of the cells to the left and right of the vNum
    * cell in the list</p>
    * @param vNum      cell whose left and right vertices are to be returned
    * @return fString  x and y coordinates of the cells to the left and right of vNum
    *
    * */
    public synchronized String getVertice(int vNum) {
        String xy = "";
        int i = 0;
        PointCell left = head;
        PointCell right = head.next;

        if (right == last) {
            return xy;
        } else {
            while ((right != head) && (i < vNum)) {
                left = left.next;
                right = right.next;
                i++;
            }

            xy = "" + left.xCoord + ";" + left.yCoord + ";" + right.xCoord +
                ";" + right.yCoord;

            return xy;
        }
    }

    /**
    * <p>Calculate the Clarke-Wright savings for inserting cell with xoordinates (x,y) between
    * cells (x1,y1) and (x2,y2)</p>
    * @param x1  x coordinate of the first point
    * @param y1  y coordinate of the first point
    * @param x2  x coordinate of the second point
    * @param y2  y coordinate of the second point
    * @param x   x coordinate of the point to be inserted
    * @param y   y coordinate of the point to be inserted
    * @return    difference in costfor inserting the new point
    *
    * */
    private synchronized float clarkeRightSavings(float x1, float y1, float x2,
        float y2, float x, float y) {
        float c;
        c = ((dist(x1, y1, x, y) + dist(x2, y2, x, y)) - dist(x1, y1, x2, y2));

        return c;
    }

    /**
    * <p>Calculate the polar coordinate angle between the depot and the customer coordinate passed
    * in as the parameter.</p>
    * @param x   x coordinate of the point
    * @param y   y coordinate of the point
    * @return    polar coordinate angle
    *
    * */
    private synchronized float calc_polar_angle(float x, float y) {
        float ret = 0;

        try {
            if ((Math.sqrt((double) (dist(depoX, x, depoY, y))) != 0) &&
                    (Math.sqrt((double) (dist(depoX, lastX, depoY, lastY))) != 0)) {
                float alpha1 = (float) Math.acos(Math.cos(
                            (double) (Math.abs((double) (x - depoX)) / (Math.sqrt(
                                (double) (dist(depoX, x, depoY, y)))))));
                float alpha2 = (float) Math.acos(Math.cos(
                            (double) (Math.abs((double) (lastX - depoX)) / (Math.sqrt(
                                (double) (dist(depoX, lastX, depoY, lastY)))))));
                ret = (float) Math.abs((double) (alpha1 - alpha2));
            }
        } catch (Exception exc) {
            System.out.println("Error in  Depot.calc_polar_angle() " + exc);
        }

        return ret;
    }

    /**
    * <p>Calculate the cost of a seed customer for the VRPTW problem</p>
    * @param iIndex     index of the shipment
    * @param lX        x coordinate of the shipment
    * @param lY        y coordinate of the shipment
    * @param lEarTime      earliest pickup time for the shipment
    * @param lLatTime      latest pickuptime for the shipment
    * @param lDemand   pickup capacity for the shipment
    * @param lServ     service for the shipment location
    * @return    cost of the seed customer
    *
    * */
    public synchronized float seedCustomerCost(int iIndex, float lX, float lY,
        float lEarTime, float lLatTime, float lDemand, float lServ) {
        float cost = 0;

        try {
            PointCell cur = head;
            float uArrTime = dist(cur.xCoord, lX, cur.yCoord, lY);
            float dst = uArrTime; //2*
            float uwaitTime = max((lEarTime - uArrTime), 0);

            //System.out.println("Depo x = "+depoX+" Depo y = "+depoY+" x = "+lX+" y = "+lY+" Dist = "+dist(depoX,lX,depoY,lY));
            //System.out.println("lLat = "+lLatTime);
            //System.out.println("pol_angle*0.1 = "+0.1*calc_polar_angle(lX,lY));
            cost = (float) ((0.7 * dist(depoX, lX, depoY, lY)) +
                (0.2 * lLatTime) + (0.1 * calc_polar_angle(lX, lY)));

            if (uArrTime > lLatTime) {
                cost = cost + MAX_COST; //cost*1000; //punishment for breaking time constraints;
            }

            if (lDemand > ProblemInfo.maxCapacity) {
                cost = cost + MAX_COST; //cost*2000; //punishment for breaking capacity constraints;
            }

            if ((dst + uwaitTime) > ProblemInfo.maxDistance) { //+lServ)> maxTime){
                cost = cost + MAX_COST; //punishment for breaking distance constraints;
            }
        } catch (Exception exc) {
            System.out.println("Error in  Depot.seedCustomerCost() " + exc);
        }

        return cost;
    }

    /**
    * <p>Calculate the insertion cost of a customer for th VRPTW problem.</p>
    * @param iIndex     index of the shipment
    * @param lX        x coordinate of the shipment
    * @param lY        y coordinate of the shipment
    * @param lEar      earliest pickup time for the shipment
    * @param lLat      latest pickuptime for the shipment
    * @param lDemand   pickup capacity for the shipment
    * @param lServ     service for the shipment location
    * @return    insertion cost of the customer
    *
    * */
    public synchronized String insertCost(int iIndex, float lX, float lY,
        float lDemand, float lEar, float lLat, float lServ) {
        PointCell cur = head;
        int kIndex = 0;
        int keepInd = -1;
        float finalCost = 1000000000;
        float cost = 1000000000;
        int flagt = 0;

        while ((cur != null) && (cur != last)) {
            kIndex = cur.index;

            int flagtt = 0;

            /*float dChange = dist(cur.xCoord,lX,cur.yCoord,lY)+dist((cur.next).xCoord,lX,(cur.next).yCoord,lY)-
            dist((cur.next).xCoord,cur.xCoord,(cur.next).yCoord,cur.yCoord);
            //System.out.println("dChange = "+dChange+" dist before = "+calculateDistance());
            float D = calculateDistance()+dChange; //total route distance;
            float fi = D/100;              //weight for the total route time;
            //float ibegTime = max(cur.arrTime,cur.earTime);
            //float iwaitTime = max(ibegTime -cur.arrTime,0);
            float uArrTime = cur.arrTime + cur.waitTime+ cur.servTime+dist(cur.xCoord,lX,cur.yCoord,lY);
            float uwaitTime = max((lEar-uArrTime),0);
            float push = uwaitTime+lServ+dChange;
            float W = D + calcTotalWaitTime(cur,push)+calcTotalServTime()+lServ; //total route time;
            cost = D+fi*W;
            float newArrj = uArrTime+dist(lX,cur.next.xCoord,lY,cur.next.yCoord)+
            lServ+uwaitTime;
            float ltu;
            if(cur.next!=last)
            ltu = min(lLat,(cur.next.lt-lServ-dist((cur.next).xCoord,lX,(cur.next).yCoord,lY)));
            else
            ltu = lLat;
            float lti = min(cur.latTime,(ltu-cur.servTime-dist(cur.xCoord,lX,cur.yCoord,lY)));
            //System.out.println("------\nuArrTime = "+uArrTime+" uwaitTime = "+uwaitTime+" newArrj = "+newArrj+" ltu = "+ltu+" lti = "+lti);
            if( (uArrTime > lLat) || (cur.next!=last && (newArrj > (cur.next).lt ) )|| (cur.arrTime-lti>0)){
            cost = cost+1000000; //punishment for breaking time constraints;
            } else
            if(lti!=cur.lt && cur!=head){
            // System.out.println("cur.lt = "+cur.lt+" lti = "+lti+" Sucsessors lt must be recalculated");
            //cost = cost+1000000;
            PointCell temp = cur.prev;
            float newlt = min(temp.latTime,(temp.lt-(cur.lt-lti)));
            if(newlt<=0 || (temp.arrTime+temp.waitTime>newlt)){
            cost = cost+1000000;
            }else{
            temp = temp.prev;
            while(temp!=head){
            newlt = min(temp.latTime,(newlt-temp.servTime-dist(temp.xCoord,temp.prev.xCoord,temp.yCoord,temp.prev.yCoord)));
            if(newlt<=0 || (temp.arrTime+temp.waitTime>newlt)){
             cost = cost+1000000;
             break;
            }
            temp = temp.prev;
            }
            }
            }
            //System.out.println(" car cap = "+calculateCapacity());
            if(calculateCapacity()+lDemand > maxCapacity){
            cost = cost+1000000;//cost*2000; //punishment for breaking capacity constraints;
            }
            if(W>maxTime){
            cost = cost+1000000;
            }
            */
            PointCell temp = insert(iIndex, lX, lY, lDemand, lEar, lLat, lServ,
                    kIndex);

            float D = calculateDistance();
            float fi = D / 100; //weight for the total route time;

            float uArrTime = temp.arrTime;
            float uwaitTime = temp.waitTime;
            float newArrj = 0;

            //float push = uwaitTime+lServ+dChange;
            float W = CalculateTotalTime(); //total route time;
            cost = D + (fi * W);

            if (temp.next != last) {
                newArrj = temp.next.arrTime;
            }

            float ltu = temp.lt;
            float lti = temp.prev.lt;

            if ((ltu < 0) || (lti < 0) || (temp.next.lt < 0) || (temp.lt < 0)) {
                cost = cost + MAX_COST;
            }

            //System.out.println("------\nuArrTime = "+uArrTime+" uwaitTime = "+uwaitTime+" newArrj = "+newArrj+" ltu = "+ltu+" lti = "+lti);
            if (((uArrTime + uwaitTime) > lLat) ||
                    ((temp.next != last) &&
                    ((newArrj + temp.next.waitTime) > (temp.next).lt))) {
                cost = cost + MAX_COST; //punishment for breaking time constraints;
            }

            //System.out.println(" car cap = "+calculateCapacity());
            if (calculateCapacity() > ProblemInfo.maxCapacity) {
                cost = cost + MAX_COST; //cost*2000; //punishment for breaking capacity constraints;
            }

            if ((calcTotalWaitTardiness() > 0) || (calcTotalExcessTime() > 0)) {
                cost = cost + MAX_COST;
                flagtt = 1;
            }

            //if(temp.next!=last)
            //System.out.println("temp.arrTime = "+temp.arrTime+" temp.waitTime = "+temp.waitTime+" temp.next.arrTime = "+ temp.next.arrTime+" temp.lt = "+
            //temp.lt+" temp.prev.lt = "+temp.prev.lt );
            //else
            //System.out.println("temp.arrTime = "+temp.arrTime+" temp.waitTime = "+temp.waitTime+" temp.lt = "+
            //temp.lt+" temp.prev.lt = "+temp.prev.lt );
            //System.out.print("COST between "+ cur.index+" and "+ cur.next.index+" = "+cost+" ; ");
            remove(iIndex); //remove the point from route (it was inserted only to perform

            cur = cur.next;

            if (cost < finalCost) {
                flagt = flagtt;
                finalCost = cost;
                keepInd = kIndex;
            }
        }

        return finalCost + ";" + keepInd;
    }

    /*public synchronized int CheckTardFeasibility(PointCell cur,int iIndex, float lX, float lY, float lDemand,
    float lEar, float lLat, float lServ){
    int ret = 1;
    try{
    float dChange = dist(cur.xCoord,lX,cur.yCoord,lY)+dist((cur.next).xCoord,lX,(cur.next).yCoord,lY)-
    dist((cur.next).xCoord,cur.xCoord,(cur.next).yCoord,cur.yCoord);
    float D = calculateDistance()+dChange; //total route distance;
    float uArrTime = cur.arrTime + cur.waitTime+ cur.servTime+dist(cur.xCoord,lX,cur.yCoord,lY);
    float uwaitTime = max((lEar-uArrTime),0);
    //float push = uwaitTime+lServ+dChange;
    //float W = D + calcTotalWaitTime(kIndex,push)+calcTotalServTime()+lServ; //total route time;
    float newArrj = uArrTime+dist(lX,cur.next.xCoord,lY,cur.next.yCoord)+lServ+uwaitTime;
    if(cur.next!=last)
      ltu = min(lLat,(cur.next.lt-lServ-dist((cur.next).xCoord,lX,(cur.next).yCoord,lY)));
    else
      ltu = lLat;
    float lti = min(cur.latTime,(ltu-cur.servTime-dist(cur.xCoord,lX,cur.yCoord,lY)));
    //System.out.println("------\nuArrTime = "+uArrTime+" uwaitTime = "+uwaitTime+" newArrj = "+newArrj+" ltu = "+ltu+" lti = "+lti);
    if( (uArrTime > lLat) || (cur.next!=last && (newArrj > (cur.next).lt ) )|| (cur.arrTime-lti>0)){
     cost = cost+1000000; //punishment for breaking time constraints;
    } else
     if(lti!=cur.lt){
       System.out.println("cur.lt = "+cur.lt+" lti = "+lti+" Sucsessors lt must be recalculated");
       cost = cost+1000000;
     }
    }catch(Exception ex){
     System.out.println(ex);
    }
    return ret;
    }
    */

    /**
    * <p>Calculate the exchange insertion cost for the VRP/MDVRP problem of shipment
    * index iIndex and then remove it.
    *  The insertion method used is insertMDVRP and the exchange cost used is calcExchCostMDVRPOpt().</p>
    * @param iIndex  index of the shipment
    * @param lX      x coordinate of the shipment
    * @param lY      y coordinate of the shipment
    * @param lDemand customer demand
    * @return String exchange insertion cost
    *
    * */
    public synchronized String exchInsertCost(int iIndex, float lX, float lY,
        float lDemand) {
        boolean isDiagnostic = false;
        float cost = 999999999;
        int insertIndex = -1;

        try {
            //insert the node into the current MDVRP linked list
            //When the node is inserted the total distance travelled and capacity
            //are updated.  When removing the cell, run the calculate to update
            //the travel time and capacity
            //The insertMDVRP should take into consideration the distance and
            //the capacity
            //insert the shipment
            insertIndex = insertMDVRP(iIndex, lX, lY, lDemand);

            //if the node could not be inserted, then insertPtr will be null
            if (insertIndex != -1) //insertion did take place
             {
                //calculate the cost
                cost = calcExchCostMDVRP();

                if (isDiagnostic) {
                    System.out.println("After Insertion of " + iIndex +
                        " Distance: " + cost + "\n " + toString());
                }

                //remove the node after the insertion
                remove(iIndex);

                //when the node is removed, the distance and capacity needs to be updated
                //currentDistance = calculateDistance();
                //currentCapacity = calculateCapacity();
                setCurrentDistance(calculateDistance());
                setCurrentCapacity(calculateCapacity());
            } else //insert did no take place
             {
                cost = 999999999;

                if (isDiagnostic) {
                    System.out.println("No Insertion of " + iIndex +
                        " Distance: " + cost + "\n" + toString());
                }
            }
        } catch (Exception ex) {
            System.out.print("Error in exchInsertCost " + ex);
        }

        return cost + ";" + insertIndex;
    }

    /**
    * <p>Calculate the exchange cost for the VRPTW problem</p>
    * /////////////////Modified by Sunil Gurung 10/29/03/////////////////////
    * @param iIndex   fsindex of the shipment
    * @param lX       x coordinate of the shipment
    * @param lY       y coordinate of the shipment
    * @param lDemand  pickup capacity for the shipment
    * @param lEar     earliest pickup time for the shipment
    * @param lLat     latest pickuptime for the shipment
    * @param lServ    service for the shipment location
    * @return         cost of the seed customer
    * */
    public synchronized String exchInsertCost(int iIndex, float lX, float lY,
        float lDemand, float lEar, float lLat, float lServ) {
        boolean isDiagnostic = false;
        PointCell cur = head;

        int kIndex = 0;
        int keepInd = -1;

        float cost = 9999999;
        float Dst; //Variable to hold the distance before the insertion is made

        float newArrTime;

        //Loop to check if the customer from P will fit in any position in Q
        try {
            while ((cur != null) && (cur != last)) {
                kIndex = cur.index;

                float distChange = ((dist(cur.xCoord, lX, cur.yCoord, lY) +
                    dist(lX, cur.next.xCoord, lY, cur.next.yCoord)) -
                    dist(cur.xCoord, cur.next.xCoord, cur.yCoord,
                        cur.next.yCoord));

                //If the customer that is tried (if kIndex == 0) then
                if (cur == head) {
                    //Actual arrival time of the truck for this customer will be only the distance from depot
                    //to that customer
                    newArrTime = dist(cur.xCoord, lX, cur.yCoord, lY);
                } else { //Othewise

                    //Actual arrival time of the truck for this customer is
                    newArrTime = (cur.arrTime + cur.waitTime + cur.servTime) +
                        dist(cur.xCoord, lX, cur.yCoord, lY);
                }

                //Wait time after arrival for the truck to start service at this cutomer
                //(i.e. arrived before the earliest time)
                float waitTime = max((lEar - newArrTime), 0);

                //Calculating how much push will be required to insert this cutomer in this route
                float push = waitTime + lServ + distChange;

                if ((push <= cur.next.slackTime) &&
                        ((ProblemInfo.vNodesLevelCostF.getTotalDemand(this) +
                        lDemand) <= ProblemInfo.maxCapacity) &&
                        ((ProblemInfo.vNodesLevelCostF.getTotalDistance(this) +
                        distChange) <= ProblemInfo.maxDistance)) {
                    //If the comparing node is the last one that means it is the
                    if (cur.next == last) {
                        return cost + ";" + keepInd;
                    } else {
                        if (isDiagnostic) {
                            System.out.println("Index being added " + iIndex);
                            System.out.println(
                                "Route before 01: Travel Time \n" + //Dst + "\n" +
                                toString() + '\n');
                        }

                        keepInd = kIndex; //Save the index

                        //Insert the node at kIndex location in the route
                        insert(iIndex, lX, lY, lDemand, lEar, lLat, lServ,
                            kIndex);

                        //Check if this insertion will produce any tardiness because of the large time window frame
                        //which is cause without this testing
                        float tardiness = (float) ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(this); //Getting the tardiness

                        //If the tardiness is not equal to 0 then this insertion is infeasible even though the push is good
                        if (tardiness != 0) {
                            remove(iIndex); //Remove
                            keepInd = -1; //Put back the invalid index if it is not correct

                            return cost + ";" + keepInd; //Return the infeasible cost 9999999999 ; -1
                        }

                        cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

                        if (isDiagnostic) {
                            System.out.println("Index being added " + iIndex);
                            System.out.println(
                                "Route before 01: Travel Time \n" + toString());
                        }

                        //Remove the node from the list
                        remove(iIndex);

                        if (isDiagnostic) {
                            System.out.println("Index being added " + iIndex);
                            System.out.println(
                                "Route before 01: Travel Time \n" + toString());
                        }

                        return cost + ";" + keepInd;
                    }
                }

                cur = cur.next;
            }

            //while loop
        } catch (Exception ex) {
            System.out.print("Error in exchInsertCost " + ex);
        }

        return cost + ";" + keepInd;
    }

    /**
    * <p>Calculate the exchange cost for the calling instance of a shipment for the
    * MDVRP problem using the cost functions defined in the ProblemInfo class.</p>
    * @return float        cost of theexchange
    *
    * */
    public synchronized float calcExchCostMDVRP() {
        float cost;
        float distance;
        float overDist;
        float capacity;
        float overLoad;

        //calculate the cost of the exchange
        distance = calculateDistance();
        capacity = calculateCapacity();
        overDist = max(0, distance - ProblemInfo.maxDistance);
        overLoad = max(0, capacity - ProblemInfo.maxCapacity);

        //At the current time, infeasible solutions are not allowed.  Therefore if
        //any constrains are broen, a very large value is returned
        if ((overDist > 0) || (overLoad > 0)) { //solution is infeasible
            cost = 999999999;
        } else {
            cost = distance;
        }

        return cost;
    }

    /**
    * <p>calculate insertion cost for the customer for the MDVRP</p>
    * @return exchange cost
    */
    public synchronized float calcExchCostMDVRPOpt() {
        float cost;
        float distance;
        float overDist;
        float capacity;
        float overLoad;

        //calculate the cost of the exchange
        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);

        distance = (float) ProblemInfo.vNodesLevelCostF.getTotalDistance(this);
        capacity = (float) ProblemInfo.vNodesLevelCostF.getTotalDemand(this);
        overDist = max(0, distance - ProblemInfo.maxDistance);
        overLoad = max(0, capacity - ProblemInfo.maxCapacity);

        //At the current time, infeasible solutions are not allowed.  Therefore if
        //any constrains are broen, a very large value is returned
        if ((overDist > 0) || (overLoad > 0)) { //solution is infeasible
            cost = Float.MAX_VALUE;
        } else {
            cost = distance;
        }

        return cost;
    }

    /**
    * <p>Calculate the exchange cost for the calling instance of a shipment for the
    * VRPTW problem.</p>
    * ////////////////////Modified by Sunil Gurung 11/09/03////////////////////
    * @return float        cost of the exchange
    *
    * */
    public synchronized float calcExchCostVRPTW() {
        float cost = 0;

        float D = calculateDistance(); //+dChange; //total route distance;
        float fi = (float) (D * 0.01); //weight for the total route time;

        float W = calcTotalWaitTime(); //+calcTotalServTime(); //total route time;
        float O = max(0, calculateCapacity() - ProblemInfo.maxCapacity); //overload
        float T = calcTotalWaitTardiness(); //total tardiness
        float E = calcTotalExcessTime(); //excess time
        float key = (float) (D * 0.5);
        float eta = (float) (D * 0.55);
        float mu = (float) (D * 0.6);
        cost = D + (fi * W) + (mu * E) + (eta * O) + (key * T);

        cost = D;

        //float newArrj = uArrTime+dist(lX,cur.next.xCoord,lY,cur.next.yCoord)+
        //lServ+uwaitTime;
        //System.out.print("uArrTime = "+uArrTime+" uwaitTime = "+uwaitTime+" push = "+push+" newArrj = "+newArrj);

        /*if( (uArrTime >= lLat) || (cur.next!=last && (newArrj > (cur.next).lt ) )){
        cost = cost+1000000;//cost*1000; //punishment for breaking time constraints;
        }
        //System.out.println(" car cap = "+calculateCapacity());
        if(calculateCapacity()+lDemand > maxCapacity){
        cost = cost+1000000;//cost*2000; //punishment for breaking capacity constraints;
        }
        if(W > maxTime){
        cost = cost+1000000; //punishment for breaking distance constraints;
        } */

        //System.out.print("COST between "+ cur.index+" and "+ cur.next.index+" = "+cost+" ; ");
        //remove(iIndex); //remove the point from route (it was inserted only to perform
        //calculations)
        //System.out.println("after removal of i= "+iIndex+ " "+toString());
        //}
        return cost;
    }

    /**
    * <p>Calculates the added time a node will create in a route if inserted and
    * compares it to the successors slack time to determine feasibility</p>
    * this method added 9/23/03 by Mike McNamara
    * @param iIndex  index of node to be inserted
    * @param lX  x coordinate of node to be inserted
    * @param lY  y coordinate of node to be inserted
    * @param lDemand  demand of node to be inserted
    * @param lEar  earliest time to service node to be inserted
    * @param lLat  latest time to service node to be inserted
    * @param lServ  service time of node to be inserted
    * @return int  index to insert node or -1 for infeasible solution for current route
    */
    public int calcPushCost(int iIndex, float lX, float lY, float lDemand,
        float lEar, float lLat, float lServ) {
        PointCell cur = head;
        int kIndex = 0;
        int keepInd = -1;
        float oldTime = 0;
        float newTime = 0; // time of route after insertion
        float push = 0; // time new node will add to route pushing the arrival times of subsequent nodes back

        ProblemInfo.vNodesLevelCostF.setTotalTravelTime(this); // calculate and set the total route time before insertion
        oldTime = (float) ProblemInfo.vNodesLevelCostF.getTotalTravelTime(this); // get the current total route time before insertion

        while ((cur != null) && (cur != last)) {
            kIndex = cur.index;

            PointCell temp = insert(iIndex, lX, lY, lDemand, lEar, lLat, lServ,
                    kIndex);

            ProblemInfo.vNodesLevelCostF.setTotalTravelTime(this); // calculate and set the total route time after insertion
            newTime = (float) ProblemInfo.vNodesLevelCostF.getTotalTravelTime(this); // get the current total route time after insertion
            push = newTime - oldTime; // difference of the two times is the amount of time new node pushes

            if (push <= temp.next.slackTime) {
                if (temp.prev.arrTime <= temp.prev.lt) {
                    if (ProblemInfo.vNodesLevelCostF.getTotalDistance(this) < ProblemInfo.maxDistance) {
                        if (ProblemInfo.vNodesLevelCostF.getTotalDemand(this) < ProblemInfo.maxCapacity) {
                            if ((ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(
                                        this) <= 0) &&
                                    (ProblemInfo.vNodesLevelCostF.getTotalExcessTime(
                                        this) <= 0)) {
                                remove(temp.index); // remove the node from the list
                                keepInd = kIndex;

                                break;
                            }
                        }
                    }
                }
            }

            remove(temp.index); // remove the node from the list

            if (cur.next.earTime < temp.latTime) {
                cur = cur.next; // iterate to the next possible insertion point if logically feasible
            } else {
                break; // end the loop because it would be impossible to service the customer

                // after this point for this route
            }
        }

        // recalculations are made here in case there was no feasible insertion point and
        // a new truck needs to be used
        if (keepInd == -1) { // if keepInd = -1 then no insertion point was found
            performRecalculates();
        }

        return keepInd;
    }

    /**
    * <p>Calculate cost of inserting a shipment and the location at which it should
    * be inserted for the VRPTW. The details of the shipment are passed as parameters.</p>
    * @param  iIndex   index of the shipment
    * @param  lX       x coordinate of the shipment
    * @param  lY       y coordinate of the shipment
    * @param  lDemand  pickup capacity for the shipment
    * @param  lEar     earliest pickup time for the shipment
    * @param  lLat     latest pickuptime for the shipment
    * @param  lServ    service for the shipment location
    * @return String   change in in cost and location to be inserted
    * */
    public synchronized String calculateInsertionCost(int iIndex, float lX,
        float lY, float lDemand, float lEar, float lLat, float lServ) {
        PointCell cur = head;
        int kIndex = 0;
        int keepInd = -1;
        double finalCost = 99999999;
        double cost = 9999999;

        while ((cur != null) && (cur != last)) {
            kIndex = cur.index;

            //PointCell temp = insert(iIndex,lX,lY,lDemand,lEar,lLat,lServ,kIndex);
            //System.out.println("after insertion after k = "+kIndex+ " "+toString());
            //Calculation to find out the distance change upon the insertion of the new customer in the route
            float distChange = (dist(cur.xCoord, lX, cur.yCoord, lY) +
                dist((cur.next).xCoord, lX, (cur.next).yCoord, lY)) -
                dist((cur.next).xCoord, cur.xCoord, (cur.next).yCoord,
                    cur.yCoord);

            //System.out.println("dChange = "+dChange+" dist before = "+calculateDistance());
            //         float D = calculateDistance() + dChange; //total route distance;
            //Calculating the total route distance including the new customer to be inserted
            double totalRouteDist = ProblemInfo.vNodesLevelCostF.getTotalDistance(this) +
                distChange;

            //System.out.println("dChange = "+distChange+" dist before = "+calculateDistance());
            //            System.out.println("totalChange = "+ totalRouteDist);
            //Actual arrival time for the truck for this customer
            float truckArrivalTime = cur.arrTime + cur.waitTime + cur.servTime +
                dist(cur.xCoord, lX, cur.yCoord, lY);

            //Wait time after arrival for the truck to start service at this cutomer
            //(i.e. arrived before the earliest time)
            float waitTimeAtArrival = max((lEar - truckArrivalTime), 0);

            //Calculating how much push will be required to insert this cutomer in this route
            float push = waitTimeAtArrival + lServ + distChange;

            /******************************************************************************/

            //cur.slackTime = cur.latTime - uArrTime;

            /******************************************************************************/

            //Calculating the total route time
            double totalRouteTime = totalRouteDist +
                calcTotalWaitTime(cur, push) + calcTotalServTime() + lServ;

            //Updating the cost
            cost = totalRouteDist;

            float newArrivalTimeForJ = cur.leaveTime +
                dist(lX, cur.next.xCoord, lY, cur.next.yCoord);

            //            System.out.print("uArrTime = "+ truckArrivalTime +" uwaitTime = " + waitTimeAtArrival +" push = "+push+" newArrj = "+newArrj);

            /******************************************************************************/

            //Check if the slackTime exceeds the limit the truck can slack to find out whether it is feasible
            //to insert the customer or not. If the push to be made is more than the slackTime of the next
            //pointCell then it is not feasible
            if ((newArrivalTimeForJ - cur.next.earTime) > cur.next.slackTime) {
                cost = cost + 1000000;
            }

            /******************************************************************************/
            if ((truckArrivalTime >= lLat) ||
                    ((cur.next != last) &&
                    (newArrivalTimeForJ > (cur.next).lt))) {
                cost = cost + 1000000; //cost*1000; //punishment for breaking time constraints;
            }

            //System.out.println(" car cap = "+calculateCapacity());
            if ((calculateCapacity() + lDemand) > ProblemInfo.maxCapacity) {
                cost = cost + 1000000; //cost*2000; //punishment for breaking capacity constraints;
            }

            if (totalRouteTime > ProblemInfo.maxDistance) {
                cost = cost + 1000000; //punishment for breaking distance constraints;
            }

            //System.out.print("COST between "+ cur.index+" and "+ cur.next.index+" = "+cost+" ; ");
            //remove(iIndex); //remove the point from route (it was inserted only to perform
            //calculations)
            //System.out.println("after removal of i= "+iIndex+ " "+toString());
            cur = cur.next;

            if (cost < finalCost) {
                finalCost = cost;
                keepInd = kIndex;
            }
        }

        return finalCost + ";" + keepInd;
    }

    /**
    * <p>Calculate the total route cost for a VRPTW. The route cost constitues of
    * the distance, waiting time and service time.</p>
    * @return float total route cost
    * */
    public synchronized float CalculateRouteCost() {
        float Dst = (float) calculateDistance();
        float cost = (float) (Dst +
            (0.01 * Dst * (calcTotalWaitTime() + calcTotalServTime() + Dst)));

        return cost;
    }

    /**
    * <p>Calculate the total route time for a VRPTW. The route cost constitues of
    * the distance and waiting time.</p>
    * @return float total time cost
    *
    * */
    public synchronized float CalculateTotalTime() {
        float time = calculateDistance() + calcTotalWaitTime() +
            calcTotalServTime();

        return time;
    }

    /**
    * <p>set the total route time of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param time  total route time of the truck
    */
    public synchronized void setTotalTime(float time) {
        currentTotalTravelTime = time;
    }

    /**
    * <p>Calculate the cost of exchange11 for a VRPTW with the cells st1 and st2</p>
    * @param st1 first customer to be exchanged
    * @param st2 second customer to be exchanged
    * @return String cost of exchange and the exchange location
    *
    * */
    public synchronized String calcVRPTWExchange11(String st1, String st2) {
        float cost_diff = 0;
        int after_index = -1;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float ear1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float serv1 = (Float.valueOf(s1.nextToken())).floatValue();
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float ear2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float serv2 = (Float.valueOf(s2.nextToken())).floatValue();

            //float Dst =  calculateDistance();
            //float before_cost =(float)( Dst+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            float before_cost = calcExchCostVRPTW();
            System.out.print("Before_cost =  " + before_cost);

            int after_index1 = remove(index1);
            String ncost = exchInsertCost(index2, xCoord2, yCoord2, demand2);
            StringTokenizer st = new StringTokenizer(ncost, ";");
            System.out.println(" After_cost =  " + ncost);

            float after_cost = (Float.valueOf(st.nextToken())).floatValue();
            after_index = (Integer.valueOf(st.nextToken())).intValue();
            cost_diff = after_cost - before_cost;
            insert(index1, xCoord1, yCoord1, demand1, after_index1);
        } catch (Exception exc) {
            System.out.println("error in calcExc11 " + exc);
        }

        return "" + cost_diff + ";" + after_index;
    }

    /**
    * <p>Calculate the total tardiness for a VRPTW with wait time.</p>
    * @return float total tardy time with wait time.
    *
    * */
    public synchronized float calcTotalWaitTardiness() {
        float tard = 0;
        PointCell temp = head.next;

        if (temp == null) {
            return 0;
        } else {
            while (temp != last) {
                if (temp.lt > 0) {
                    tard = tard +
                        max(((temp.arrTime + temp.waitTime) - temp.latTime), 0);
                    temp = temp.next;
                } else {
                    tard = tard + 1000000000;
                    temp = temp.next;

                    break;
                }
            }

            return tard;
        }
    }

    /**
    * <p>Calculate the total tardiness for a VRPTW without wait time.</p>
    * @return float total tardy time without wait time
    *
    * */
    public synchronized float calcTotalTardiness() {
        float tard = 0;
        PointCell temp = head.next;

        if (temp == null) {
            return 0;
        } else {
            while ((temp != null) && (temp != last)) {
                tard = tard + max((temp.arrTime - temp.lt), 0);
                temp = temp.next;
            }

            return tard;
        }
    }

    /**
    * <p>set current total tardiness of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param tardiness  current total tardiness of the truck
    */
    public synchronized void setTotalTardiness(float tardiness) {
        currentTardiness = tardiness;
    }

    /**
    * <p>return the current total tardiness of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @return float  current total tardiness of the truck
    */
    public synchronized float getTotalTardiness() {
        return currentTardiness;
    }

    /**
    * <p>Calculate the total travel time beyond the maximum travel time. The maximum
    * time is in the variable maxTime.</p>
    * @return float total excess travel time
    *
    * */
    public synchronized float calcTotalExcessTime() {
        float excess = 0;
        ProblemInfo.vNodesLevelCostF.setTotalTravelTime(this);
        excess = max(0,
                ((float) ProblemInfo.vNodesLevelCostF.getTotalDistance(this) -
                ProblemInfo.maxDistance));

        return excess;
    }

    /**
    * <p>set total excess time of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param excess  total excess time of the truck
    */
    public synchronized void setTotalExcessTime(float excess) {
        currentExcessTime = excess;
    }

    /**
    * <p>return the current excess time of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @return float  current excess time of the truck
    */
    public synchronized float getTotalExcessTime() {
        return currentExcessTime;
    }

    /**
    * <p>Calculate the overload for the set of shipments. The maximum capacity is
    * in the variable maxCapacity.</p>
    * @return float tootal excess load beyond the maximum capacity.
    *
    * */
    public synchronized float calcTotalOverload() {
        return max(0, calculateCapacity() - ProblemInfo.maxCapacity);
    }

    /**
    * <p>set the current total overload of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param overload  current total overload of the truck
    */
    public synchronized void setTotalOverload(float overload) {
        currentOverload = overload;
    }

    /**
    * <p>return the current total overload of the truck</p>
    * method added 10/13/03 by Mike McNamara
    * @return float  current total overload of the truck
    */
    public synchronized float getTotalOverload() {
        return currentOverload;
    }

    /**
    * <p>Get the total service time for this route</p>
    * @return totalServiceTime
    */
    public float gettotalServiceTime() {
        float totalServiceTime = 0; //Variable to hold the total service time

        //Get the first pointCell in the tempVisitList
        PointCell tempPointCell = this.first().next;

        //While there are no more pointCell
        while (tempPointCell.next != null) {
            //Perform the summation of servTime for each pointCell
            totalServiceTime += tempPointCell.servTime;
            tempPointCell = tempPointCell.next; //Next pointCell
        }

        return totalServiceTime;
    }

    /**
    * <p>Set the total waiting time for this route</p>
    * @return totalWaitTime
    */
    public synchronized float calculateTotalWaitTime() {
        float totalWaitTime = 0; //Variable to hold the total time

        //Get the first pointCell in the tempVisitList
        PointCell tempPointCell = this.first().next;

        //While there are no more pointCell
        while (tempPointCell.next != null) {
            //Perform the summation of waitTime for each pointCell
            totalWaitTime += tempPointCell.waitTime;
            tempPointCell = tempPointCell.next; //Next pointCell
        }

        return totalWaitTime;
    }

    /**
    * <p>Calculate the cost of exchange11 for a MDVRP with the cells st1 and st2. The
    * function used to calculate the cost is calcExchCostMDVRP(). Remove shipment st1.
    * Compute the exchange cost of inserting shipment st2 and then reinsert shipment st1.</p>
    * //////////////////Modified by Sunil Gurung 11/07/03/////////////////////
    * @param st1 shipment to be removed
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @return String cost of exchange and the best location to insert st2
    * */
    public synchronized String calcExchange11(String st1, String st2) {
        boolean isDiagnostic = false;
        float cost_diff = 0;
        int after_index = -1;

        try {
            //Retrieving the information of m and n nodes from st1(p) and st2(q) respectively
            //m node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //n node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //cost before exchange takes place
            //this contains the actual cost before making the exchange
            //float before_cost = calcExchCostMDVRP(); //+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            float before_cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            if (isDiagnostic) {
                System.out.println("Remove " + index1 + " " + "Accept " +
                    index2);
            }

            //remove the shipment index1 (m) from p and lock its current location
            int after_index1 = remove(index1);

            if (after_index1 == -1) {
                System.out.println("Can't find  index " + index1 + " in \n" +
                    toString());

                return "" + 999999999 + ";" + after_index;
            }

            if (isDiagnostic) {
                //System.out.println("Index of removed point "+index1);
                //System.out.println(toString());
                displayForwardKeyListMDVRP();
            }

            //Insert n in p where feasible and return the cost
            //insert the point, calculate the cost and remove the point
            //the cost is in ncost and the best location to insert is after_index
            String ncost = exchInsertCost(index2, xCoord2, yCoord2, demand2,
                    earTime2, latTime2, servTime2);
            StringTokenizer st = new StringTokenizer(ncost, ";");
            float after_cost = (Float.valueOf(st.nextToken())).floatValue();

            //cost after the exchange
            after_index = (Integer.valueOf(st.nextToken())).intValue();

            //cost differential
            cost_diff = after_cost - before_cost;

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost + ": " + index2);
            }

            //insert the first shipment back into its original location (m in p again)
            insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                servTime1, after_index1);

            if (isDiagnostic) {
                System.out.println("   After Insertion  " + index1);
                displayForwardKeyList();

                //System.out.println(toString());
            }

            //recompute the distance and capacity after the moves
        } catch (Exception exc) {
            System.out.println("error in calcExc11 " + exc);
        }

        return "" + cost_diff + ";" + after_index;
    }

    /**
    * <p>Perform the permanent exchange11 of shipments st1 and st2. Remove
    * the shipment st1 and and insert shipment st2  in location after_index.</p>
    * //////////////////Modified by Sunil Gurung 11/07/03///////////////////
    * @param st1 shipment to be removed
    * @param st2 shipment to be inserted
    * @param after_index location into which shipment st2 is to be inserted
    * */
    public synchronized void exchange11(String st1, String st2, int after_index) {
        try {
            //Retrieving the informations
            //st1 is to be removed permanently
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();

            //st2 is to be inserted permanently
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //Saving the pointCell where the insertion is to be made to check the validity of the location
            PointCell p = getPointCell(after_index);

            //If the location where the insertion is to be done is not null and after_index is valid
            //then remove the index1 and insert the index2 at after_index location in the route
            if ((after_index != -1) && (p != null)) {
                remove(index1); //remove the shipment
                insert(index2, xCoord2, yCoord2, demand2, earTime2, latTime2,
                    servTime2, after_index); //insert the shipment
            } else {
                System.out.println("trying to insert after " + index2 +
                    " p = " + p);
            }
        } catch (Exception exc) {
            System.out.println("error in Depot Exc11 " + exc);
        }
    }

    /**
    * <p>Calculate the cost of exchange01 for shipment st1. The
    * function used to calculate the cost is calcExchCostMDVRP(); The shipment st1 is
    * removed and the differential cost is returned. St1 is inserted in the same location.
    * /////////////Modified by Sunil 10/25/03////////////////////</p>
    * @param st1 shipment to be removed
    * @return String cost removing shipment st1
    * */
    public synchronized String calcExchange01(String st1) {
        boolean isDiagnostic = false;
        StringTokenizer s1 = new StringTokenizer(st1, ";");
        int index1 = (Integer.valueOf(s1.nextToken())).intValue();
        float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
        float earTime = (Float.valueOf(s1.nextToken())).floatValue();
        float latTime = (Float.valueOf(s1.nextToken())).floatValue();
        float servTime = (Float.valueOf(s1.nextToken())).floatValue();

        //cost before exchange takes place
        //this contains the actual cost before making the exchange
        //float Dst = calcExchCostMDVRP();
        float Dst1 = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

        if (isDiagnostic) {
            System.out.println("Index being removed " + index1);
            System.out.println("Route before 01: Travel Time " + Dst1 + "\n" +
                toString());
        }

        //System.out.println("\nBEFORE  REMOVE" + this.toString());
        // float before_cost = calcExchCost();//+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
        int after_index1 = remove(index1);

        //System.out.println("\nAFTER\n  REMOVE" + this.toString());
        //float Dst2 = calcExchCostMDVRP();
        float Dst2 = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

        if (isDiagnostic) {
            System.out.println("Index being removed " + index1);
            System.out.println("Route after 01: Travel Time " + Dst2 + " " +
                toString());
        }

        //float after_cost = calcExchCost();//+0.01*Dst2*(calcTotalWaitTime()+calcTotalServTime()+Dst2));
        //int after_index =  (Integer.valueOf(st.nextToken())).intValue();
        float cost_diff = Dst2 - Dst1;

        //Method added by Sunil to activate the correct insert
        insert(index1, xCoord1, yCoord1, demand1, earTime, latTime, servTime,
            after_index1);

        //System.out.println("\nAFTER\n  INSERT" + this.toString());
        //System.out.println(toString());
        if (isDiagnostic) {
            System.out.println("Index being added " + index1);
            System.out.println(
                "Route after 01 inserting index again: Travel Time " + Dst1 +
                " " + toString());
        }

        return "" + cost_diff + "";
    }

    /**
    * <p>Perform the permanent exchange01 of shipment st1. Remove
    * shipment st1 from current route.</p>
    * ////////////Modified by Sunil Gurung 10/29/03//////////////////
    * @param st1 shipment to be removed
    * */
    public synchronized void exchange01(String st1) {
        StringTokenizer s1 = new StringTokenizer(st1, ";");
        int index1 = (Integer.valueOf(s1.nextToken())).intValue();

        remove(index1);
    }

    /**
    * <p>Calculate the cost of exchange10 for a MDVRP shipment st1. The
    * function used to calculate the cost is calcExchCostMDVRP(); Shipment
    * st1 is inserted into the current route and the cost difference before
    * and after the insertion and the best location of insertion is returned.</p>
    * ///////////////////Modified by Sunil Gurung 25/10/03/////////////////////
    * @param st1 shipment used for computing the exchange cost (not inserted in this method)
    * @return String cost of exchange and best location to insert st1
    * */
    public synchronized String calcExchange10(String st1) {
        boolean isDiagnostic = false;
        StringTokenizer s1 = new StringTokenizer(st1, ";");
        int index1 = (Integer.valueOf(s1.nextToken())).intValue();
        float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
        float earTime = (Float.valueOf(s1.nextToken())).floatValue();
        float latTime = (Float.valueOf(s1.nextToken())).floatValue();
        float servTime = (Float.valueOf(s1.nextToken())).floatValue();

        //  float Dst =  calculateDistance();
        //float before_cos = calcExchCostMDVRP(); //+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
        //Get the initial cost before doing the addition of the customer from "p" visitNodesLinkedList
        float before_cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

        if (isDiagnostic) {
            System.out.println("Index being added " + index1);
            System.out.println("Route before 10: Travel Time " + before_cost +
                "\n" + toString());
        }

        //insert the node, calculate the cost and then remove the node
        String str1 = exchInsertCost(index1, xCoord1, yCoord1, demand1,
                earTime, latTime, servTime);
        StringTokenizer s2 = new StringTokenizer(str1, ";");
        float after_cost = (Float.valueOf(s2.nextToken())).floatValue();
        int afterIndex = (Integer.valueOf(s2.nextToken())).intValue();

        //Finding the difference between after and before the insertion
        float cost_diff = after_cost - before_cost;

        if (isDiagnostic) {
            System.out.println("Index being added " + index1);
            System.out.println("Route after 10: Travel Time " + after_cost +
                " " + toString());
        }

        return "" + cost_diff + ";" + afterIndex;
    }

    /**
    * <p>Perform the permanent exchange10 of shipments st1. Insert shipment st1
    * into location after_index. Update the currentDuration and currentCapacity.</p>
    * /////////////////Modified by Sunil Gurung 10/29/03/////////////////////
    * @param st1 shipment to be inserted into the route
    * @param after_index best location for inserting st1
    * */
    public synchronized void exchange10(String st1, int after_index) {
        StringTokenizer s1 = new StringTokenizer(st1, ";");
        int index1 = (Integer.valueOf(s1.nextToken())).intValue();
        float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

        /////////////Sunil 10/25/03//////////////////////////////////
        float earTime = (Float.valueOf(s1.nextToken())).floatValue();
        float latTime = (Float.valueOf(s1.nextToken())).floatValue();
        float servTime = (Float.valueOf(s1.nextToken())).floatValue();

        insert(index1, xCoord1, yCoord1, demand1, earTime, latTime, servTime,
            after_index);
    }

    /**
    * <p>Calculate the cost of exchange12 for a MDVRP with the shipments st1, st2 and st3. The
    * function used to calculate the cost is calcExchCostMDVRP(); Remove st2 and st3. Insert st1
    * into the current route. Reinsert shipments st2 and st3 back into the routes.
    * Return the difference in cost and the best location for insertion of st1.</p>
    * ////////////////////Modified by Sunil Gurung 10/29/03////////////////////     *
    * @param st1 shipment used for computing the exchange cost (not inserted in this method)
    * @param st2 shipment to be removed
    * @param st3 shipment to be removed
    * @return String cost of exchange and best location to insert st3
    * */
    public synchronized String calcExchange12(String st1, String st2, String st3) {
        boolean isDiagnostic = false;
        float cost_diff = 0;
        int after_index = -1;

        try {
            //Retrieving the information of q(j), p(i) and p(i+1) nodes from st1, st2 and st3 respectively
            //q(j) node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //p(i) node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //p(i+1)
            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();
            float earTime3 = (Float.valueOf(s3.nextToken())).floatValue();
            float latTime3 = (Float.valueOf(s3.nextToken())).floatValue();
            float servTime3 = (Float.valueOf(s3.nextToken())).floatValue();

            //calcualte the cost before the exchange
            //float before_cost = calcExchCostMDVRP(); //+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            float before_cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            if (isDiagnostic) {
                System.out.println("Accept " + index1 + " " + "Remove " +
                    index2 + " and " + index3);
            }

            //lock the locations of the two nodes being removed
            //Remove the i and (i+1) nodes from the route and save the index
            int after_index2 = remove(index2);
            int after_index3 = remove(index3);

            //insert a node and compute its cost
            String ncost = exchInsertCost(index1, xCoord1, yCoord1, demand1,
                    earTime1, latTime1, servTime1);

            StringTokenizer st = new StringTokenizer(ncost, ";");
            float after_cost = (Float.valueOf(st.nextToken())).floatValue();
            after_index = (Integer.valueOf(st.nextToken())).intValue();

            //cost differential
            cost_diff = after_cost - before_cost;

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost + ": " + index2);
            }

            //Inserting the nodes back to their original position
            ///////////////////MAKE SURE TO INSERT THE LATTER INSERTED NODE FIRST////////////
            insert(index3, xCoord3, yCoord3, demand3, earTime3, latTime3,
                servTime3, after_index3);
            insert(index2, xCoord2, yCoord2, demand2, earTime2, latTime2,
                servTime2, after_index2);
        } catch (Exception exc) {
            System.out.println("error in calcExc11 " + exc);
        }

        return "" + cost_diff + ";" + after_index;
    }

    /**
    * <p>Perform the permanent exchange12 of shipments st1, st2 and st3. Remove
    * shipments st2 and st3 and insert shipment st1 in location after_index.</p>
    * //////////////////Modified by Sunil Gurung 11/09/03/////////////////////
    * @param st1 information on first shipment
    * @param after_index1 location into which shipment st1 is to be inserted
    * @param st2 information on second shipment
    * @param st3 information on third shipment
    * */
    public synchronized void exchange12(String st1, int after_index1,
        String st2, String st3) {
        try {
            //Retrieving the information from st1, st2 and st3 respectively
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();

            //Remove the index2 and index3 from p permanently
            remove(index2);
            remove(index3);

            //Permanently inserting the index1 at after_index1 respectively
            insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                servTime1, after_index1);
        } catch (Exception exc) {
            System.out.println("error in Exc12 " + exc);
        }
    }

    /**
    * <p>Calculate the cost of exchange21 for a MDVRP with the shipments st1, st2 and st3. The
    * function used to calculate the cost is calcExchCostMDVRP(); Remove st3. Calculate
    * the cost of inserting st1 and st2. Shipment st3 is reinserted into the route.
    * Calculate the total cost differential for
    * the whole process then reinsert st3 back into the route.</p>
    * //////////////////Modified by Sunil Gurung 11/05/03/////////////////////
    * @param st1 shipment inserted into route.
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @param st3 shipment to be removed
    * @return String cost of exchange and best locations to insert st1 and st2
    * */
    public synchronized String calcExchange21(String st1, String st2, String st3) {
        boolean isDiagnostic = false;
        float cost_diff = 0;

        //for keepint track of the two locations for insertions
        int after_index1 = -1;
        int after_index2 = -1;

        try {
            //Retrieving the information of p(i), p(i+1) and q(j) nodes from st1, st2 and st3 respectively
            //p(i) node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //p(i+1) node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //q(j)
            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();
            float earTime3 = (Float.valueOf(s3.nextToken())).floatValue();
            float latTime3 = (Float.valueOf(s3.nextToken())).floatValue();
            float servTime3 = (Float.valueOf(s3.nextToken())).floatValue();

            //float Dst =  calculateDistance();
            //float before_cost = calcExchCostMDVRP(); //+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            float before_cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            if (isDiagnostic) {
                System.out.println("Accept " + index1 + " and " + index2 +
                    " Remove " + index3);
            }

            //remove index 3 and save the location
            int after_index3 = remove(index3);

            //insert index1
            String ncost1 = exchInsertCost(index1, xCoord1, yCoord1, demand1,
                    earTime1, latTime1, servTime1);

            StringTokenizer st = new StringTokenizer(ncost1, ";");
            float after_cost1 = (Float.valueOf(st.nextToken())).floatValue();
            after_index1 = (Integer.valueOf(st.nextToken())).intValue();

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost1 + ": " + index1);
            }

            //if insertion was feasible
            if (after_index1 != -1) {
                //Then put the index1 back to the place where it was feasible
                insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                    servTime1, after_index1);

                //And try inserting the index2 where ever it is feasible
                String ncost2 = exchInsertCost(index2, xCoord2, yCoord2,
                        demand2, earTime2, latTime2, servTime2);

                StringTokenizer stt = new StringTokenizer(ncost2, ";");
                float after_cost2 = (Float.valueOf(stt.nextToken())).floatValue();
                after_index2 = (Integer.valueOf(stt.nextToken())).intValue();

                //calculate cost differential
                cost_diff = after_cost2 - before_cost;

                if (isDiagnostic) {
                    System.out.println(
                        "   Cost after insertion/removal of point " +
                        cost_diff + ": " + index2);
                }

                //Take out the index1 from the route
                remove(index1);
            } else {
                cost_diff = 999999999;
                after_index1 = -1;
                after_index2 = -1;
            }

            //reinsert index3 back into the route
            insert(index3, xCoord3, yCoord3, demand3, earTime3, latTime3,
                servTime3, after_index3); //insert the node back
        } catch (Exception exc) {
            System.out.println("error in calcExc21 " + exc);
        }

        return "" + cost_diff + ";" + after_index1 + ";" + after_index2;
    }

    /**
    * <p>Perform the permanent exchange21 of shipments st1, st2 and st3. Remove
    * shipment st3 and insert shipment st1 and st2 in locations after_index1
    * and after_index2.</p>
    * //////////////////Modified by Sunil Gurung 11/09/03/////////////////////
    * @param st1 first shipment to be inserted
    * @param st2 second shipment to be inserted
    * @param after_index1 location into which shipment st1 is to be inserted
    * @param after_index2 location into which shipment st2 is to be inserted
    * @param st3 shipment to be removed
    * */
    public synchronized void exchange21(String st1, int after_index1,
        String st2, int after_index2, String st3) {
        try {
            //Retrieving the information from st1, st2 and st3 respectively
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();

            //Permanently remove index3
            remove(index3);

            //Permanently inserting the index1 and index2 at after_index1 and after_index2 respectively
            insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                servTime1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, earTime2, latTime2,
                servTime2, after_index2);
        } catch (Exception exc) {
            System.out.println("error in Exc21 " + exc);
        }
    }

    /**
    * <p>Calculate the cost of exchange02 for a MDVRP with the shipments st1 and st2. The
    * function used to calculate the cost is calcExchCostMDVRP(); Remove st1 and st2.
    * Calculate and return the cost differential before and after the removal. Insert
    * st1 and st2 into the route.
    * /////////////Modified by Sunil 11/4/03/////////////////////////////</p>
    * @param st1 shipment to be removed
    * @param st2 shipment ro be removed
    * @return String cost of removing st1 and st2
    * */
    public synchronized String calcExchange02(String st1, String st2) {
        boolean isDiagnostic = false;
        float cost_diff = 0;

        try {
            //Retrieving the information of i and i+1 nodes from st1 and st2 respectively
            //i node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //i+1 node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //cost before exchange takes place
            //this contains the actual cost before making the exchange
            //float before_cost = calcExchCostMDVRP(); //+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            float Dst1 = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            if (isDiagnostic) {
                System.out.println("Remove " + index1 + " and " + index2);
            }

            //System.out.println("\nBEFOOOOOOOOOOOORE  REMOVE" );
            //displayForwardKeyList();
            int after_index1 = remove(index1); //Take out index1 from p
            int after_index2 = remove(index2); //Take out index2 from p

            float Dst2 = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            //Calcuting the difference
            cost_diff = Dst2 - Dst1;

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    Dst2 + ": " + index1 + ": " + index2);
            }

            //Inserting the nodes back to their original position
            ///////////////////MAKE SURE TO INSERT THE LATTER INSERTED NODE FIRST////////////
            insert(index2, xCoord2, yCoord2, demand2, earTime2, latTime2,
                servTime2, after_index2);
            insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                servTime1, after_index1);

            //System.out.println("\nAFTEEEEEEEEEEEEER  INSERT");
            //displayForwardKeyList();
            //System.out.println("\n==================\n==================");
        } catch (Exception exc) {
            System.out.println("error in calcExc02 " + exc);
        }

        //        System.out.println(toString());
        return "" + cost_diff + "";
    }

    /**
    * <p>Perform the permanent exchange02 of shipments st1 and st2. Remove
    * shipments st1 and st2.</p>
    * //////////////////Modified by Sunil Gurung 11/05/03/////////////////////
    * @param st1 shipment to be removed
    * @param st2 shipment to be removed
    * */
    public synchronized void exchange02(String st1, String st2) {
        try {
            //Retrieving the information of i and i+1 nodes from st1 and st2 respectively
            //i node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();

            //i+1 node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();

            //Permanently removing the index1 and index2 from p
            remove(index1);
            remove(index2);
        } catch (Exception exc) {
            System.out.println("error in Exc02 " + exc);
        }
    }

    /**
    * <p>Calculate the cost of exchange20 for a MDVRP with the shipments st1 and st2. The
    * function used to calculate the cost is calcExchCostMDVRP(); Insert st1 and st2 and
    * computing the cost of the route. Return the differential in cost and the best
    * locations for inserting shipment st1 and st2. Remove st1 from the route.</p>
    * //////////////////Modified by Sunil Gurung 11/05/03/////////////////////
    * @param st1 shipment to be inserted
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @return String cost of inserting shipments st1 and st2 and best insertion locations
    * */
    public synchronized String calcExchange20(String st1, String st2) {
        boolean isDiagnostic = false;
        float cost_diff = 0;
        int after_index1 = -1;
        int after_index2 = -1;

        try {
            //Retrieving the information of i and i+1 nodes from st1 and st2 respectively
            //i node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //i+1 node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //Get the initial cost before insertions of the customers
            float before_cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            //float before_cost = calcExchCostMDVRP(); //+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            if (isDiagnostic) {
                System.out.println("Add " + index1 + " and " + index2);
            }

            //insert the first (i) node, calculate the cost and then remove the node
            String ncost1 = exchInsertCost(index1, xCoord1, yCoord1, demand1,
                    earTime1, latTime1, servTime1);

            StringTokenizer str1 = new StringTokenizer(ncost1, ";");
            float after_cost1 = (Float.valueOf(str1.nextToken())).floatValue();
            after_index1 = (Integer.valueOf(str1.nextToken())).intValue();

            if (after_index1 != -1) { //insertion was feasible

                //it was feasible so insert it again to check for the second node (i+1) to find the cost
                insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                    servTime1, after_index1);

                //insert the first (i+1) node, calculate the cost and then remove the node
                String ncost2 = exchInsertCost(index2, xCoord2, yCoord2,
                        demand2, earTime2, latTime2, servTime2);

                StringTokenizer str2 = new StringTokenizer(ncost2, ";");
                float after_cost2 = (Float.valueOf(str2.nextToken())).floatValue();
                after_index2 = (Integer.valueOf(str2.nextToken())).intValue();

                //Finding the difference between after and before the insertion
                cost_diff = after_cost2 - before_cost;

                if (isDiagnostic) {
                    System.out.println(
                        "   Cost after insertion/removal of point " +
                        cost_diff + ": " + index2);
                }

                //Take out the index1 node which was inserted againd to find the cost for inserting i+1 node
                remove(index1);
            } else {
                cost_diff = 999999999;
                after_index1 = -1;
                after_index2 = -1;

                if (isDiagnostic) {
                    System.out.println(
                        "   Cost after insertion/removal of point " +
                        cost_diff + ": " + index2);
                }
            }
        } catch (Exception exc) {
            System.out.println("error in calcExc11 " + exc);
        }

        return "" + cost_diff + ";" + after_index1 + ";" + after_index2;
    }

    /**
    * <p>Perform the permanent exchange20 of shipments st1 and st2. Insert
    * shipments st1 and st2 into locations after_index1 and after_index2
    * respectively.</p>
    * //////////////////Modified by Sunil Gurung 11/05/03/////////////////////
    * @param st1 shipment to be inserted
    * @param after_index1  best location for inserting shipment st1
    * @param st2 shipment to be inserted
    * @param after_index2  best location for inserting shipment st2
    * */
    public synchronized void exchange20(String st1, int after_index1,
        String st2, int after_index2) {
        try {
            //Retrieving the information of i and i+1 nodes from st1 and st2 respectively
            //i node
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //i+1 node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //Permanently inserting the index1 and index2 at after_index1 and after_index2 respectively
            insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                servTime1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, earTime2, latTime2,
                servTime2, after_index2);
        } catch (Exception exc) {
            System.out.println("error in Exc20 " + exc);
        }
    }

    /**
    * <p>Calculate the cost of exchange22 for a MDVRP with the shipments st1,st2, st3 and st4. The
    * function used to calculate the cost is calcExchCostMDVRP(); Remove st3 and st4.
    * Insert shipment st1 and calculate the exchange cost of insertion st2. Return
    * the cost differential and the best location for inserting shipments st1 and st2.
    * Remove shipment st1 and reinsert shipment st3 and st4 in original locations. The
    * insertion of two shipments does not guarantee that they will be consecutive. The best
    * locations for inserting them are located.</p>
    * ////////////////////Modified by Sunil Gurung 11/09/03////////////////////
    * @param st1 shipment to be inserted
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @param st3 shipment to be removed
    * @param st4 shipment to be removed
    * @return String cost of inserting removing shipments st3 and st4 and inserting shipments st1 and st2
    * */
    public synchronized String calcExchange22(String st1, String st2,
        String st3, String st4) {
        float cost_diff = 0;
        int after_index1 = -1;
        int after_index2 = -1;

        try {
            //Retrieving the information of p(i), p(i+1), q(i), q(i+1)
            //q(i)
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //q(i+1) node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //p(i)
            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();
            float earTime3 = (Float.valueOf(s3.nextToken())).floatValue();
            float latTime3 = (Float.valueOf(s3.nextToken())).floatValue();
            float servTime3 = (Float.valueOf(s3.nextToken())).floatValue();

            //p(i+1)
            StringTokenizer s4 = new StringTokenizer(st4, ";");
            int index4 = (Integer.valueOf(s4.nextToken())).intValue();
            float xCoord4 = (Float.valueOf(s4.nextToken())).floatValue();
            float yCoord4 = (Float.valueOf(s4.nextToken())).floatValue();
            float demand4 = (Float.valueOf(s4.nextToken())).floatValue();
            float earTime4 = (Float.valueOf(s4.nextToken())).floatValue();
            float latTime4 = (Float.valueOf(s4.nextToken())).floatValue();
            float servTime4 = (Float.valueOf(s4.nextToken())).floatValue();

            //float Dst =  calculateDistance();
            //float before_cost = calcExchCostVRPTW(); //(float)( Dst);//+0.01*Dst*(calcTotalWaitTime()+calcTotalServTime()+Dst));
            float before_cost = (float) ProblemInfo.vNodesLevelCostF.globalCalculateOptimizedCost(this);

            //Removing index3 [p(i)] and index4 from [p(i+1)]
            int after_index3 = remove(index3);
            int after_index4 = remove(index4);

            //insert index1 [q(i)] and compute its cost
            String ncost1 = exchInsertCost(index1, xCoord1, yCoord1, demand1,
                    earTime1, latTime1, servTime1);

            StringTokenizer st = new StringTokenizer(ncost1, ";");
            float after_cost1 = (Float.valueOf(st.nextToken())).floatValue();
            after_index1 = (Integer.valueOf(st.nextToken())).intValue();

            if (after_index1 != -1) { //insertion was feasible

                //it was feasible so insert it again to check for the second node - index2 (q+1) to find the cost
                insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                    servTime1, after_index1);

                //insert the index2 [q(i+1)] node, calculate the cost and then remove the node
                String ncost2 = exchInsertCost(index2, xCoord2, yCoord2,
                        demand2, earTime2, latTime2, servTime2);

                StringTokenizer stt = new StringTokenizer(ncost2, ";");
                float after_cost2 = (Float.valueOf(stt.nextToken())).floatValue();
                after_index2 = (Integer.valueOf(stt.nextToken())).intValue();

                //Finding the difference between after and before the insertion
                cost_diff = after_cost2 - before_cost;

                //Take out the index1 node which was inserted againd to find the cost for inserting i+1 node
                remove(index1);
            }

            if ((after_index3 == -1) || (after_index4 == -1)) {
                System.out.println(
                    "VisitNodes: After_index3 or after_index4 is negative");
            }

            //Inserting the nodes back to their original position
            ///////////////////MAKE SURE TO INSERT THE LATTER INSERTED NODE FIRST////////////
            insert(index4, xCoord4, yCoord4, demand4, earTime4, latTime4,
                servTime4, after_index4);
            insert(index3, xCoord3, yCoord3, demand3, earTime3, latTime3,
                servTime3, after_index3);
        } catch (Exception exc) {
            System.out.println("error in calcExc22 " + exc);
        }

        return "" + cost_diff + ";" + after_index1 + ";" + after_index2;
    }

    /**
    * <p>Perform the permanent exchange22 of shipments st1, st2, st3 and st4.
    * Remove shipments st3 and st4 and insert shipments st1 and st2.</p>
    * //////////////////Modified by Sunil Gurung 11/09/03/////////////////////
    * @param st1 shipment to be inserted
    * @param after_index1 best location for inserting shipment st1
    * @param st2 shipment to be inserted
    * @param after_index2 best location for inserting shipment st2
    * @param st3 shipment to be removed
    * @param st4 shipment to be removed
    * */
    public synchronized void exchange22(String st1, int after_index1,
        String st2, int after_index2, String st3, String st4) {
        try {
            //Retrieving the information of p(i), p(i+1), q(i), q(i+1)
            //q(i)
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float earTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float servTime1 = (Float.valueOf(s1.nextToken())).floatValue();

            //q(i+1) node
            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();
            float earTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float latTime2 = (Float.valueOf(s2.nextToken())).floatValue();
            float servTime2 = (Float.valueOf(s2.nextToken())).floatValue();

            //p(i)
            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();

            //p(i+1)
            StringTokenizer s4 = new StringTokenizer(st4, ";");
            int index4 = (Integer.valueOf(s4.nextToken())).intValue();

            //Removing index3 [p(i)] and index4 from [p(i+1)]
            int after_index3 = remove(index3);
            int after_index4 = remove(index4);

            if ((after_index1 == -1) || (after_index2 == -1)) {
                System.out.println(
                    "VisitNodes: Exchange 22: after_index1 and after_index2 is negative");
            }

            //Inserting the nodes back to their original position
            insert(index1, xCoord1, yCoord1, demand1, earTime1, latTime1,
                servTime1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, earTime2, latTime2,
                servTime2, after_index2);
        } catch (Exception exc) {
            System.out.println("Error:error in Exc22 " + exc);
        }
    }

    /**
    * <p>Locally optimize the route using the 1-opt optimization. The 1-opt method
    * exchanges customers within a route if it results in decreased cost. The
    * current local 1-opt is for the MDVRP problem and uses the methods
    * insertAfterCellMDVRP(insertP,p) to insert a customer and calcExchCostMDVRP()
    * method to calculate the cost. In order to use it for other problems, define
    * the required methods for insertion and exchange and replace it in the current
    * method.</p>
    * @return boolean true if an exchange took place
    * Reference:
    *
    * */
    public synchronized boolean localOneOpt() {
        boolean isDiagnostic = false;
        boolean noChange = false; //no change to place in the local opt
        boolean globalNoChange = true; //value returned by the opt procedure
        float cost_diff = MAX_COST;
        int startPos = 1; //position of the node to be moved relative to the head
        PointCell startP; //pointer to the node to be moved
        PointCell insertP = null; //best location to insert p
        PointCell p = null; //p is the node to be moved, q is the insertion location
        PointCell q = null; //p is the node to be moved, q is the insertion location
        String outp = "";
        String bef = "";

        float Dst1 = 0;
        float Dst2 = 0;
        numLocalOpt = 0;

        if (isDiagnostic) {
            System.out.println("Beginning Local 1-Opt ***");
        }

        try {
            totalOneOpt = 0;

            //there should be at least 2 visit nodes  + the 2 depot nodes to perform a local 1-opt
            if (getSize() < 4) {
                if (isDiagnostic) {
                    System.out.println("Less than 2 nodes in route: return ***");
                }

                return true; //no changes took place
            }

            //the maximum local opt is a class varible - change if required
            startP = head.next;

            // while(!noChange && (numLocalOpt < maxLocalOpt)){
            //starting position does not count the starting and ending depot locations
            while (startPos < (getSize() - 2)) {
                if (isDiagnostic) {
                    System.out.println(" Distance 1 = " + Dst1 +
                        " routeSize = " + (getSize() - 2));
                }

                //check if insertion of p has to be done - this is exceute after a node
                //has been inserted into every possible location in the list and
                //the best location is recorded by insertP
                //If no improvement was made then insertP has the same value as p
                if ((insertP != null) && (p != null)) {
                    removeCell(p); //remove the cell to be inserted
                    insertAfterCellMDVRP(insertP, p); //insert p after insertP pointer
                    ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values

                    if (isDiagnostic) {
                        System.out.println("Switch LocalOpt1 is permanent");
                        System.out.println(toString());
                    }
                }

                if (!noChange) //if change took place, start from the same location
                 {
                    //loop to the position of the node to be moved and then point to it
                    //The loop is required as the pointer to previous or next nodes could
                    //be lost during the exchanges
                    startP = head; //the head node

                    for (int i = 0; (i < startPos) && (startP != last); i++)
                        startP = startP.next; //else start from the same location
                } else {
                    //if no change, advance to the next position
                    startPos++;
                    startP = head;

                    for (int i = 0; (i < startPos) && (startP != last); i++)
                        startP = startP.next;
                }

                Dst1 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this); //calculate cost before insertion
                p = startP; //starting node is in startP
                q = head; //insert from the top of the list

                if (isDiagnostic) {
                    System.out.println("Node to be moved: " + p.index);
                }

                //If no changes took place then put p in it its original position
                insertP = startP.prev;

                //set noChange to true
                noChange = true; //if change takes place, this is set to false

                while (q != last) {
                    if ((p == q) && isDiagnostic) {
                        System.out.println("\n" +
                            "Local one opt: p is the same as q");
                    }

                    removeCell(p); //remove the cell to be inserted
                    insertAfterCellMDVRP(q, p); //insert p after q
                    ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values
                    Dst2 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this); //calculate cost after insertion

                    if (isDiagnostic) {
                        System.out.println("Distance 2 = " + Dst2 +
                            " routeSize = " + (getSize() - 2));
                    }

                    //calculate the difference in cost
                    cost_diff = Dst2 - Dst1;

                    if (isDiagnostic) {
                        outp = outp + "\nAfter Opt1 \n" + toString();
                        System.out.println(outp);
                        outp = "";
                    }

                    if (cost_diff == 0) //insertion was in the same place
                     {
                        if (isDiagnostic) {
                            System.out.println("No Switch: Same set of node");
                        }

                        q = p.next;
                    } else if (cost_diff > 0) { //undo the move
                        removeCell(p); //remove p

                        if (q != head) {
                            q = q.prev; //and insert it before q
                        }

                        insertAfterCellMDVRP(q, p);
                        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values

                        if (p != last) {
                            q = p.next; //then set q to the next location from the original
                        }

                        if (q != last) {
                            q = q.next;
                        }

                        if (isDiagnostic) {
                            System.out.println("Undo Switch");
                            System.out.println(toString());
                        }
                    } //else if
                    else {
                        Dst1 = Dst2; //capture the best new cost
                        insertP = q; //capture the best location for insertion

                        if (isDiagnostic) {
                            System.out.println("Capture best location");
                        }

                        q = p.next; //have q point to the next node from p
                        numLocalOpt++;

                        if (noChange) {
                            noChange = false;
                        }

                        if (globalNoChange) { //value returned by the opt procedure
                            noChange = false;
                        }
                    }

                    //else
                }

                //while
            }

            //while
            if (isDiagnostic) {
                System.out.println("Done Local 1-Opt");
            }
        } //try
        catch (Exception exc) {
            System.out.println("Error occured in Local1-Opt(): " + exc);
        }

        //print the total number of 1-opt moves
        if (isDiagnostic) {
            System.out.println("Total 1-opt moves: " + numLocalOpt);
        }

        totalOneOpt += numLocalOpt;

        //compute the distance after final insertion
        //currentDuration = calculateDistance();
        setCurrentDistance(calculateDistance());

        //compute the capacity after final insertion
        //currentCapacity = calculateCapacity();
        setCurrentCapacity(calculateCapacity());

        return globalNoChange;
    }

    /**
    * <p>Locally optimize the route using the 2-opt optimization. The 2-opt method
    * breaks two edges between four shipments and reconnects them in different
    * permutations. The change is made permanent if there is a decrease in cost.
    * The current local 2-opt is for the MDVRP problem and uses the methods
    * insertAfterCellMDVRP(insertP,p) to insert a customer and calcExchCostMDVRP()
    * method to calculate the cost. In order to use it for other problems, define
    * the required methods for insertion and exchange and replace it in the current
    * method.</p>
    * @return boolean true if an exchange took place
    * Reference:
    *
    * */
    public synchronized boolean localTwoOpt() {
        boolean isDiagnostic = false;
        boolean noChange = false; //no change to place in the local opt
        boolean globalNoChange = true; //value returned by the opt procedure
        int startPos = 0; //initial starting position for the i
        float cost_diff = MAX_COST;
        String outp = "";
        String bef = "";
        ;

        float Dst1 = 0;
        float Dst2 = 0;
        int i = 0;
        int i1; //temporary variable used for i and j values
        int j1; //temporary variable used for i and j values
        numLocalOpt = 0;

        //if there are less than 4 nodes, return out of the loop
        if (isDiagnostic) {
            System.out.println("Beginning Local 2-Opt ***");
        }

        try {
            totalTwoOpt = 0;

            //there should be at least 2 visit nodes  + the 2 depot nodes to perform a local 2-opt
            if (getSize() < 4) {
                if (isDiagnostic) {
                    System.out.println("Less than 4 nodes in route: return ***");
                }

                return true; //no changes took place
            }

            //for(i=1; (i <= getSize()-2) && (numLocalOpt < maxLocalOpt); i++){
            //for(i=1; (i <= getSize()-2); i++){
            while ((startPos + 1) <= (getSize() - 2)) {
                if (noChange) //no changes took place move to the next node
                 {
                    startPos++;

                    if (isDiagnostic) {
                        System.out.println("Position moved to: " + startPos);
                    }
                }

                noChange = true;
                i = startPos; //i = 0;

                int j = 1;

                if (isDiagnostic) {
                    System.out.println("i  = " + i + " dst1 = " + Dst1 +
                        " routeSize = " + (getSize() - 2));
                }

                while (j <= (getSize() - 2)) {
                    if ((i != j) && ((i + 1) != j) && (i != (j + 1)) &&
                            (i != (getSize() - 2)) && (j != (getSize() - 1))) {
                        noChange = true;
                        cost_diff = MAX_COST;

                        //calculate the exchange cost
                        Dst1 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);

                        //PointCell p = head.next; //initially p and q pointing to the same node
                        //PointCell q = head.next;
                        PointCell p = head; //initially p and q pointing to the same node
                        PointCell q = head;

                        //The following set of if then clauses guarantee that p is always in
                        //front of q
                        if (i < j) { //i is in front of j
                            i1 = i;
                            j1 = j;
                        } else { //j is in front of i
                            i1 = j;
                            j1 = i;
                        }

                        //locate the ith node for p
                        int x = 0;

                        while ((p != last) && (x < i1)) {
                            p = p.next;
                            x++;
                        }

                        //while loop
                        //locate the jth node for q
                        int y = 0;

                        while ((q != last) && (y < j1)) {
                            q = q.next;
                            y++;
                        }

                        //while loop
                        //Exchange is to be performed between p,p2 and q and q2
                        PointCell p2 = p.next;
                        PointCell q2 = q.next;

                        if (p == q) {
                            System.out.println("\n" +
                                "Local two opt: p is the same as q");
                        }

                        if (p2 == q2) {
                            System.out.println("\n" +
                                "Local two opt: p2 is the same as q2");
                        }

                        bef = toString(); //capture the string before the change

                        if (isDiagnostic) {
                            System.out.println("p, p+1, q, q+1 " + p.index +
                                " " + p2.index + " " + q.index + " " +
                                q2.index);
                        }

                        if (isDiagnostic) {
                            System.out.println("Value of i and j are: " + i +
                                " " + j);
                            System.out.println("Nodes p and q are: " + p.index +
                                " " + q.index);
                        }

                        //2-opt optimization
                        //when i points to j and then i+1 points to j+1, all the nodes from
                        //j until i are reversed
                        //Start from the p2 node and reverse the direction of all the nodes
                        //till q2
                        PointCell h; //h follows r
                        PointCell r = p2; //start at p2 to locate q2

                        while (r != q2) {
                            h = r.next;
                            r.next = r.prev;
                            r.prev = h;
                            r = r.prev;
                        }

                        p.next = q;
                        q.prev = p;
                        p2.next = q2;
                        q2.prev = p2;

                        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values

                        //calculate the exchange cost
                        Dst2 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);

                        if (isDiagnostic) {
                            System.out.println("j  = " + j + " dst2 = " + Dst2 +
                                " routeSize = " + (getSize() - 2));
                        }

                        cost_diff = Dst2 - Dst1;

                        if (isDiagnostic) {
                            outp = outp + "\nAfter 2-opt \n" + toString();
                            System.out.println(outp);
                        }

                        //System.out.println("Route before switch: "+bef);
                        //System.out.println("Route after switch: "+toString());
                        if (cost_diff >= 0) { //no change, switch back to original location
                            r = p2;

                            while (r != p) {
                                h = r.next;
                                r.next = r.prev;
                                r.prev = h;
                                r = r.next;
                            }

                            //while loop
                            p.next = p2;
                            p2.prev = p;
                            q.next = q2;
                            q2.prev = q;
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values

                            if (!bef.equals(toString())) {
                                System.out.println(
                                    "SOMETHING CHANGED: Switch Back in error in Locap 2-Opt\n");
                                System.out.println("Route before switch: " +
                                    bef);
                                System.out.println("Route after switch: " +
                                    toString());
                            }

                            // inner if
                        } //if
                        else {
                            Dst1 = Dst2;

                            if (noChange) {
                                noChange = false;
                            }

                            if (globalNoChange) { //value returned by the opt procedure
                                globalNoChange = false;
                            }

                            if (isDiagnostic) {
                                System.out.println(
                                    "Swith Local 2-Opt is permanent");
                            }

                            //if
                            //System.out.println("Total 2-opt moves: "+ numLocalOpt);
                            //totalTwoOpt += numLocalOpt;
                            numLocalOpt++;
                        }

                        //else
                    }

                    //if
                    j++; //increment to the next node
                }

                //while loop 1
            }

            //while loop 2
            //System.out.println("Done optimizing.");
        } //try
        catch (Exception exc) {
            System.out.println("Error occured in Local 2-opt(): " + exc);
        }

        //print the total number of 2-opt moves
        if (isDiagnostic) {
            System.out.println("Total 2-opt moves: " + numLocalOpt);
        }

        totalTwoOpt += numLocalOpt;

        //compute the distance after final insertion
        //currentDuration = calculateDistance();
        setCurrentDistance(calculateDistance());

        //compute the capacity after final insertion
        //currentCapacity = calculateCapacity();
        setCurrentCapacity(calculateCapacity());

        return globalNoChange;
    }

    /**
    * <p>Locally optimize the route using the 3-opt optimization. The 3-opt method
    * breaks three edges between six shipments and reconnects them in different
    * permutations. The change is made permanent if there is a decrease in cost.
    * The current local 3-opt is for the MDVRP problem and uses the methods
    * insertAfterCellMDVRP(insertP,p) to insert a customer and calcExchCostMDVRP()
    * method to calculate the cost. In order to use it for other problems, define
    * the required methods for insertion and exchange and replace it in the current
    * method.</p>
    * @param optType type of exchange to be done with values between 1 and 5
    * @return true if an exchange took place
    * Reference:
    *
    * */
    public synchronized boolean localThreeOpt(int optType) {
        //There are four different cases for the 3opt with the first case being the one
        //that does not reverse any of the nodes.  In order to allow any combination of the cases
        //to be tested, the type is used to indicate the case type that is to be run.  The OptTypes are
        //as follows:
        // 1 - Case 1
        // 2 - Case 2
        // 3 - Case 3
        // 4 - Case 4
        // 5 - All cases
        boolean isDiagnostic = false;
        boolean noChange = false; //no change to place in the local opt
        boolean globalNoChange = true; //value returned by the opt procedure
        int startPosI = 0; //initial starting position for the i and j
        int startPosJ = 0; //initial starting position for the i and j
        float cost_diff = MAX_COST;
        String outp = "";
        String bef = "";
        float Dst1 = 0;
        float Dst2 = 0;
        int i = 0;
        int j = 0;
        int i1; //temporary variable used for i and j values
        int j1; //temporary variable used for i and j values
        int k1; //temporary variable used for i and j values
        numLocalOpt = 0;

        int startOpt; //used for looping the differnt optimizations
        int maxOpt; //used for looping the differnt optimizations

        //if there are less than 6 nodes, return out of the loop
        if ((optType < 1) || (optType > 5)) {
            System.out.println("Error: Local 3-Opt has a value of " + optType);
        }

        if (isDiagnostic) {
            System.out.println("Beginning Local 3-Opt ***");
        }

        try {
            if (getSize() < 6) {
                if (isDiagnostic) {
                    System.out.println("Less than 6 nodes in route: return ***");
                }

                return true;
            }

            totalThreeOpt = 0;

            if (optType < 5) {
                startOpt = optType;
                maxOpt = optType + 1;
            } else {
                startOpt = 1;
                maxOpt = optType + 1;
            }

            for (int optExec = startOpt; optExec < maxOpt; optExec++) //check the moves on the four different cases
             {
                startPosI = 0;

                if (isDiagnostic) {
                    System.out.println("Case being executed: " + optExec);
                }

                while ((startPosI + 1) <= (getSize() - 2)) {
                    if (noChange) { //no changes took place move to the next node
                        startPosI++;

                        if (isDiagnostic) {
                            System.out.println("Position i moved to: " +
                                startPosI);
                        }
                    }

                    i = startPosI; //i = 0;

                    if (isDiagnostic) {
                        System.out.println("i  = " + i + " dst1 = " + Dst1 +
                            " routeSize = " + (getSize() - 2));
                    }

                    startPosJ = 0;

                    while ((startPosJ + 1) <= (getSize() - 2)) {
                        if (noChange) { //no changes took place move to the next node
                            startPosJ++;

                            if (isDiagnostic) {
                                System.out.println("Position j moved to: " +
                                    startPosJ);
                            }
                        }

                        j = startPosJ; //i = 0;

                        int k = 0;
                        noChange = true;

                        while (k <= (getSize() - 2)) {
                            if (isDiagnostic) {
                                System.out.println("i, j and k = " + i + " " +
                                    j + " " + k + " dst1 = " + Dst1 +
                                    " routeSize = " + (getSize() - 2));
                            }

                            if ((i != j) && ((i + 1) != j) && (i != (j + 1)) &&
                                    (i != (getSize() - 3)) &&
                                    (j != (getSize() - 2)) &&
                                    (j != (getSize() - 1)) && (i != k) &&
                                    ((i + 1) != k) && (i != (k + 1)) &&
                                    (j != k) && ((j + 1) != k) &&
                                    ((j + 1) != (k + 1))) {
                                cost_diff = MAX_COST;

                                //calculate the exchange cost
                                Dst1 = calcExchCostMDVRP();

                                PointCell p = head; //initially p,q and l pointing to the same node
                                PointCell q = head;
                                PointCell l = head;

                                //The following set of if then clauses guarantee that p is always in
                                //front of q and q is always in front of k
                                if ((i < j) && (j < k)) { //i is in front of j and j is in front of k
                                    i1 = i;
                                    j1 = j;
                                    k1 = k;
                                } else if ((i < k) && (k < j)) { //i is in front of k and k is in front of j
                                    i1 = i;
                                    j1 = k;
                                    k1 = j;
                                } else if ((j < i) && (i < k)) { //j is in front of i and i is in front of k
                                    i1 = j;
                                    j1 = i;
                                    k1 = k;
                                } else if ((j < k) && (k < i)) { //j is in front of k and k is in front of i
                                    i1 = j;
                                    j1 = k;
                                    k1 = i;
                                } else if ((k < i) && (i < j)) { //k is in front of i and i is in front of
                                    i1 = k;
                                    j1 = i;
                                    k1 = j;
                                } else { // (k < j < i){  //k is in front of j and k is in front of i
                                    i1 = k;
                                    j1 = j;
                                    k1 = i;
                                }

                                //locate the ith node for p
                                int x = 0;

                                while ((p != last) && (x < i1)) {
                                    p = p.next;
                                    x++;
                                }

                                //while loop
                                //locate the jth node for q
                                int y = 0;

                                while ((q != last) && (y < j1)) {
                                    q = q.next;
                                    y++;
                                }

                                //locate the kth node for l
                                int z = 0;

                                while ((l != last) && (z < k1)) {
                                    l = l.next;
                                    z++;
                                }

                                //while loop
                                //Exchange is to be performed between p,p2,q,q2and l, l2
                                PointCell p2 = p.next;
                                PointCell q2 = q.next;
                                PointCell l2 = l.next;

                                if ((p == q) || (p == l)) {
                                    System.out.println("\n" +
                                        "Local three opt: p is the same as q or l");
                                }

                                if ((p2 == q2) || (p2 == l2)) {
                                    System.out.println("\n" +
                                        "Local three opt: p2 is the same as q2 or l2");
                                }

                                bef = toString(); //capture the string before the change

                                if (isDiagnostic) {
                                    System.out.println("i, j and k = i1 j1 k1" +
                                        i + " " + j + " " + k + " " + i1 + " " +
                                        j1 + " " + k1);
                                    System.out.println(
                                        "p, p+1, q, q+1, l and l+1 " + p.index +
                                        " " + p2.index + " " + q.index + " " +
                                        q2.index + " " + l.index + " " +
                                        l2.index);
                                }

                                if (isDiagnostic) {
                                    System.out.println(
                                        "Value of i, j and k are: " + i + " " +
                                        j + " " + k);
                                    System.out.println("Nodes p, q and l are: " +
                                        p.index + " " + q.index + " " +
                                        l.index);
                                }

                                //3-opt optimization
                                //PointCell h;        //h follows r
                                //PointCell r = p2;   //start at p2 to locate q2
                                //Loop through the different cases of the local 3-opt
                                //optExec - is the optimization being executed
                                if (isDiagnostic) {
                                    System.out.println("Local Opt is " +
                                        optExec);
                                }

                                switch (optExec) {
                                case 1:
                                    local3OptCase1(p, q, l, p2, q2, l2);

                                    break;

                                case 2:
                                    local3OptCase2(p, q, l, p2, q2, l2);

                                    break;

                                case 3:
                                    local3OptCase3(p, q, l, p2, q2, l2);

                                    break;

                                case 4:
                                    local3OptCase4(p, q, l, p2, q2, l2);

                                    break;
                                }

                                //calculate the exchange cost
                                Dst2 = calcExchCostMDVRP();

                                if (isDiagnostic) {
                                    System.out.println("j  = " + j +
                                        " dst2 = " + Dst2 + " routeSize = " +
                                        (getSize() - 2));
                                }

                                cost_diff = Dst2 - Dst1;

                                if (isDiagnostic) {
                                    System.out.println("\nBefore  3-opt \n" +
                                        bef);
                                    System.out.println("\nAfter 3-opt \n" +
                                        toString());
                                }

                                //System.out.println("Route before switch: "+bef);
                                //System.out.println("Route after switch: "+toString());
                                if (cost_diff >= 0) { //no change, switch back to original location

                                    switch (optExec) {
                                    case 1:
                                        local3OptCase1Undo(p, q, l, p2, q2, l2);

                                        break;

                                    case 2:
                                        local3OptCase2Undo(p, q, l, p2, q2, l2);

                                        break;

                                    case 3:
                                        local3OptCase3Undo(p, q, l, p2, q2, l2);

                                        break;

                                    case 4:
                                        local3OptCase4Undo(p, q, l, p2, q2, l2);

                                        break;
                                    }

                                    if (!bef.equals(toString())) {
                                        System.out.println(
                                            "SOMETHING CHANGED: Switch Back in error in Local 3-Opt\n");
                                        System.out.println(
                                            "p, p+1, q, q+1, l and l+1 " +
                                            p.index + " " + p2.index + " " +
                                            q.index + " " + q2.index + " " +
                                            l.index + " " + l2.index);
                                        System.out.println(
                                            "Route before switch: " + bef);
                                        System.out.println(
                                            "Route after switch: " +
                                            toString());
                                    }

                                    // inner if
                                } //if
                                else {
                                    Dst1 = Dst2;

                                    if (noChange) {
                                        noChange = false;
                                    }

                                    if (globalNoChange) { //value returned by the opt procedure
                                        globalNoChange = false;
                                    }

                                    if (isDiagnostic) {
                                        System.out.println(
                                            "Switch Local 3-Opt is permanent");
                                    }

                                    //if
                                    numLocalOpt++;

                                    //totalThreeOpt += numLocalOpt;
                                }

                                //else
                            }

                            //if
                            k++;
                        }

                        //while loop 1
                        j++; //increment to the next node
                    }

                    //while loop 2
                }

                //while loop
            }

            //for loop for the differnt cases
        } //try
        catch (Exception exc) {
            System.out.println("Error occured in Local 3-opt(): " + exc);
        }

        //print the total number of 2-opt moves
        if (isDiagnostic) {
            System.out.println("Total 3-opt moves: " + numLocalOpt);
        }

        totalThreeOpt += numLocalOpt;

        //compute the distance after final insertion
        //currentDuration = calculateDistance();
        setCurrentDistance(calculateDistance());

        //compute the capacity after final insertion
        //currentCapacity = calculateCapacity();
        setCurrentCapacity(calculateCapacity());

        return globalNoChange;
    }

    /**
    *  <p>There are four cases for the local 3-opt. Case1: arcs (i, j+1), (k, i+1), (j, k+1)
    *  are switched and direction is not reversed. These switches are permanent and
    *  needs to be undone by the local3OptCase1Undo method.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    *
    * */
    private synchronized void local3OptCase1(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        boolean isDiagnostic = false;

        try {
            //make the changes
            p.next = q2;
            p2.prev = l;
            q.next = l2;
            q2.prev = p;
            l.next = p2;
            l2.prev = p2;
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase1");
        }
    }

    /**
    *  <p>Undo the switches made for Case1: arcs (i, j+1), (k, i+1), (j, k+1)for
    *  local 3-opt.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    *
    * */
    private synchronized void local3OptCase1Undo(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        boolean isDiagnostic = false;

        try {
            //make the changes
            p.next = p2;
            p2.prev = p;
            q.next = q2;
            q2.prev = q;
            l.next = l2;
            l2.prev = l;
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase1Undo");
        }
    }

    /**
    *  <p>There are four cases for the local 3-opt. Case 2: arcs (i, k), (j+1, i+1), (j, k+1)
    *  are switched and direction reversed from k to j+1. These switches are permanent and
    *  needs to be undone by the local3OptCase2Undo method.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    *
    * */
    private synchronized void local3OptCase2(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        boolean isDiagnostic = false;
        PointCell h;
        PointCell r;

        try {
            //switch direction of all nodes from q to p2
            if (isDiagnostic) {
                System.out.println("Route before all switches\n" + toString());
                System.out.println("p, p+1, q, q+1, l and l+1 " + p.index +
                    " " + p2.index + " " + q.index + " " + q2.index + " " +
                    l.index + " " + l2.index);
            }

            reverseDoubleLinkArcs(q2, l2); //reverse all arcs from q2 to node before l2
            p.next = l;
            l.prev = p;
            q2.next = p2;
            p2.prev = q2;
            q.next = l2;
            l2.prev = q;

            if (isDiagnostic) {
                System.out.println("Route after all switches\n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase2");
        }
    }

    /**
    *  <p>Undo the switches made for Case1: arcs (i, k), (j+1, i+1), (j, k+1) for
    *  local 3-opt.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    *
    * */
    private synchronized void local3OptCase2Undo(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        boolean isDiagnostic = false;
        PointCell h;
        PointCell r;

        try {
            //switch direction of all nodes from q to p2
            if (isDiagnostic) {
                System.out.println("Route before all switches\n" + toString());
                System.out.println("p, p+1, q, q+1, l and l+1 " + p.index +
                    " " + p2.index + " " + q.index + " " + q2.index + " " +
                    l.index + " " + l2.index);
            }

            reverseDoubleLinkArcs(l, p2); //reverse all arcs from l to node before p2
            p.next = p2;
            p2.prev = p;
            q.next = q2;
            q2.prev = q;
            l.next = l2;
            l2.prev = l;

            if (isDiagnostic) {
                System.out.println("Route after all switches\n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase2Undo");
        }
    }

    /**
    *  <p>There are four cases for the local 3-opt. Case 3 arcs (i, j), (i+1, k), (j+1, k+1)
    *  are switched and directions reversed from  j to i+1 and k to j+1. These switches
    *  are permanent and needs to be undone by the local3OptCase3Undo method.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    *
    * */
    private synchronized void local3OptCase3(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        try {
            boolean isDiagnostic = false;
            PointCell h;
            PointCell r;

            //switch direction of all nodes from q to p2
            if (isDiagnostic) {
                System.out.println("Route before all switches\n" + toString());
            }

            reverseDoubleLinkArcs(p2, q2); //reverse all arcs from p2 to node before q2
            reverseDoubleLinkArcs(q2, l2); //reverse all arcs from q2 to node before l2
            p.next = q;
            q.prev = p;
            p2.next = l;
            q2.next = l2;
            l.prev = p2;
            l2.prev = q2;

            if (isDiagnostic) {
                System.out.println("Route after all switches\n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase3");
        }
    }

    /**
    *  <p>Undo the switches made for Case 3: arcs (i, j), (i+1, k), (j+1, k+1) for
    *  local 3-opt.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    * */
    private synchronized void local3OptCase3Undo(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        boolean isDiagnostic = false;
        PointCell temp = head;
        PointCell temp2 = q;
        PointCell h;
        PointCell r;

        try {
            //switch direction of all nodes from q to p2
            if (isDiagnostic) {
                System.out.println("Route before all switches\n" + toString());
            }

            reverseDoubleLinkArcs(q, l); //reverse all arcs from q to node before l
            p2.prev = p;
            p.next = p2;
            reverseDoubleLinkArcs(l, l2); //reverse all arcs from l to node before l
            q.next = q2;
            q2.prev = q;
            l.next = l2;
            l2.prev = l;

            if (isDiagnostic) {
                System.out.println("Route after all switches\n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase3Undo");
        }
    }

    /**
    *  <p>There are four cases for the local 3-opt. Case 4 arcs (i, j+1), (k,j), (i+1, k+1)
    *  are switched and direction reversed from  j to i+1. These switches
    *  are permanent and needs to be undone by the local3OptCase4Undo method.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    *
    * */
    private synchronized void local3OptCase4(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        try {
            boolean isDiagnostic = false;
            PointCell h;
            PointCell r;

            if (isDiagnostic) {
                System.out.println("Route before all switches\n" + toString());
                System.out.println("p, p+1, q, q+1, l and l+1 " + p.index +
                    " " + p2.index + " " + q.index + " " + q2.index + " " +
                    l.index + " " + l2.index);
            }

            reverseDoubleLinkArcs(p2, q2); //reverse all arcs from p2 to node before q2
            p.next = q2;
            q2.prev = p;
            p2.next = l2;
            l2.prev = p2;
            l.next = q;
            q.prev = l;

            if (isDiagnostic) {
                System.out.println("Route after all switches\n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase4");
        }
    }

    /**
    *  <p>Undo the switches made for Case 4: arcs (i, j+1), (k,j), (i+1, k+1) for
    *  local 3-opt.</p>
    *  @param p node i
    *  @param q node j
    *  @param l node k
    *  @param p2 node i+1
    *  @param q2 node j+1
    *  @param l2 node k+1
    * */
    private synchronized void local3OptCase4Undo(PointCell p, PointCell q,
        PointCell l, PointCell p2, PointCell q2, PointCell l2) {
        try {
            boolean isDiagnostic = false;
            PointCell h;
            PointCell r;

            if (isDiagnostic) {
                System.out.println("Route before all switches\n" + toString());
            }

            reverseDoubleLinkArcs(q, l2); //reverse all arcs from q to node before l2
            p.next = p2;
            p2.prev = p;
            q.next = q2;
            q2.prev = q;
            l.next = l2;
            l2.prev = l;

            if (isDiagnostic) {
                System.out.println("Route after all switches\n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in Local3OptCase4Undo");
        }
    }

    /**
    * <p>Reverse all the arcs starting from p till the node before q
    * This will result in a route pointing in the direction of p to q-1
    * changing direction and pointing in the direction of q-1 to p</p>
    * @param p starting location of reverse
    * @param q ending location of reverse
    *
    * */
    private synchronized void reverseDoubleLinkArcs(PointCell p, PointCell q) {
        try {
            boolean isDiagnostic = false;
            PointCell h;
            PointCell r;

            if (isDiagnostic) {
                System.out.println("Route before reversal\n" + toString());
            }

            r = p; //start at p and reverse till q is located

            while (r != q) {
                h = r.next;
                r.next = r.prev;
                r.prev = h;
                r = r.prev;
            }

            if (isDiagnostic) {
                System.out.println("Route after reversal \n" + toString());

                //print the list forward and backward to make sure the exchange were
                //done right
                displayForwardKeyList();
                displayBackwardKeyList();
            }
        } catch (Exception e) {
            System.out.println("Error in reverseDoubleLinkArcs");
        }
    }

    /**
    * <p>Locally optimize the route using the k-interchange k-opt-1 optimization. In the
    * k-interchange optimization switches are performed without reversing the direction
    * of the route. The k-opt-1 is the same as the 1-opt method.</p>
    * Modified by Mike McNamara
    * modifications are associated with the VRPTW problem, such as calculating the
    * push of a node into the list
    *@return boolean  true if exchanges were executed
    * */
    public synchronized boolean kInterChange1() {
        boolean isDiagnostic = false;
        boolean noChange = true;
        boolean globalNoChange = true; //value returned by the opt procedure
        int startPos = 0;
        float cost_diff = MAX_COST;
        float Dst1 = 0;
        float Dst2 = 0;
        float push = 0;
        float distChange = 0;
        float newArrTime = 0;
        float waitTime = 0;
        int i = 1;
        numLocalOpt = 0;

        try {
            //System.out.println("Beginning Local Optimization ***");
            //System.out.println("Route before :\n"+toString());
            i = 0;

            //for(i=1; (i <= getSize()-2)&&(numLocalOpt < maxLocalOpt); i++)
            while ((startPos + 1) <= (getSize() - 2)) {
                if (noChange) { //if no change, move to the next position
                    startPos++;
                }

                i = startPos;

                int j = 1;

                // System.out.println("i  = "+i+" dst1 = "+Dst1+ " routeSize = "+(getSize()-2) );
                //for (j=1; (j<=getSize()-2) && (numLocalOpt<maxLocalOpt); j++)
                while (j <= (getSize() - 2)) {
                    if (((i < j) &&
                            ((i != j) && (i != (j + 1)) && (i != (j + 2)) &&
                            (j != (i + 1)) && (i != (getSize() - 2)) &&
                            (j != (getSize() - 2)) && ((getSize() - 2) >= 5) &&
                            ((i + 1) <= (getSize() - 2)) &&
                            ((j + 1) <= (getSize() - 2)))) ||
                            ((i > j) &&
                            ((i != j) && (j != (i + 1)) && (j != (i + 2)) &&
                            (i != (j + 1)) && (i != (getSize() - 2)) &&
                            (j != (getSize() - 2)) && ((getSize() - 2) >= 5) &&
                            ((i + 1) <= (getSize() - 2)) &&
                            ((j + 1) <= (getSize() - 2))))) {
                        //System.out.println("Inside of if for K1");
                        cost_diff = MAX_COST;
                        Dst1 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);

                        if (isDiagnostic) {
                            System.out.println("-------------BEFORE K1 inter " +
                                i + " and " + j + "\n" + toString());
                        }

                        String bef = toString();
                        PointCell p = head.next;
                        PointCell q = head.next;

                        if (i < j) {
                            int k = 1;

                            while ((p != last) && (k < i)) {
                                p = p.next;
                                k++;
                            }

                            int m = 1;

                            while ((q != last) && (m < j)) {
                                q = q.next;
                                m++;
                            }
                        } else {
                            int a = 1;

                            while ((p != last) && (a < j)) {
                                p = p.next;
                                a++;
                            }

                            int b = 1;

                            while ((q != last) && (b < i)) {
                                q = q.next;
                                b++;
                            }
                        }

                        //check if the same cells are being exchanged
                        if (p == q) {
                            System.out.println("\n" +
                                "Interchanged K1 inter: p is the same as q");
                        }

                        PointCell p1 = p.prev;
                        PointCell p2 = p.next;
                        PointCell q2 = q.next;

                        remove(p2.index);

                        //Calculation to find out the distance change upon the insertion of the new customer in the route
                        distChange = (dist(p1.xCoord, p2.xCoord, p1.yCoord,
                                p2.yCoord) +
                            dist((p1.next).xCoord, p2.xCoord, (p1.next).yCoord,
                                p2.yCoord)) -
                            dist((p1.next).xCoord, p1.xCoord, (p1.next).yCoord,
                                p1.yCoord);

                        //Actual arrival time of the truck for this customer
                        newArrTime = p1.arrTime + p1.waitTime + p1.servTime +
                            dist(p1.xCoord, p2.xCoord, p1.yCoord, p2.yCoord);

                        //Wait time after arrival for the truck to start service at this cutomer
                        //(i.e. arrived before the earliest time)
                        waitTime = max((p2.earTime - newArrTime), 0);

                        //Calculating how much push will be required to insert this cutomer in this route
                        push = waitTime + p2.servTime + distChange;

                        //System.out.println(push + " " + p1.next.slackTime);
                        if (push <= p1.next.slackTime) {
                            insertAfterCellVRPTW(p1, p2);

                            Dst2 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);

                            //System.out.println("j  = "+j+" dst2 = "+Dst2+ " routeSize = "+(getSize()-2) );
                            cost_diff = Dst2 - Dst1;

                            if (isDiagnostic) {
                                System.out.println("\n" +
                                    "Interchanged K1 inter " + i + " and " + j +
                                    " Cost1 = " + Dst1 + " Cost2 = " + Dst2 +
                                    " \n" + toString());
                            }

                            if (cost_diff >= 0) {
                                //return everything as it was before:
                                remove(p2.index);
                                insertAfterCellVRPTW(p, p2);

                                if (isDiagnostic) {
                                    System.out.println("\nSwitching back " +
                                        toString());
                                }

                                if (!bef.equals(toString())) {
                                    System.out.println(
                                        " SOMETHING CHANGED!!!\n" + toString() +
                                        bef);

                                    if (noChange) {
                                        noChange = false;
                                    }

                                    if (globalNoChange) { //value returned by the opt procedure
                                        globalNoChange = false;
                                    }
                                }
                            } else {
                                //Dst1 = Dst2;
                                if (isDiagnostic) {
                                    System.out.println("K1 Swith is permanent." +
                                        toString());
                                }

                                numLocalOpt++;
                            }

                            //else
                        }
                        // if push
                        else {
                            insertAfterCellVRPTW(p, p2);
                        }
                    }

                    //if
                    j++;
                }

                //while loop 1
            }

            //while loop 2
            //System.out.println("Done optimizing.");
        } //try
        catch (Exception exc) {
            System.out.println("Error occured in KInterChange1(): " + exc);
        }

        //print the total number of 1-opt moves
        if (isDiagnostic) {
            System.out.println("Total k-1opt moves: " + numLocalOpt);
        }

        totalKOneOpt += numLocalOpt;

        return globalNoChange;
    }

    /**
    * <p>Locally optimize the route using the k-interchange k-opt-2 optimization. In the
    * k-interchange optimization switches are performed without reversing the direction
    * of the route.</p>
    * Modified by Mike McNamara
    * modifications are associated with the VRPTW problem, such as calculating the
    * push of a node into the list
    * @return boolean true if exchanges were executed
    * */
    public synchronized boolean kInterChange2() {
        boolean isDiagnostic = false;
        boolean noChange = false;
        boolean globalNoChange = true; //value returned by the opt procedure
        int startPos = 0;
        float cost_diff = 0;
        String before;
        float Dst1 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);
        float Dst2 = 0;
        float push = 0;
        float distChange = 0;
        float newArrTime = 0;
        float waitTime = 0;
        int i = 1;
        numLocalOpt = 0;

        //if there are less than 4 nodes, return out of the loop
        if (isDiagnostic) {
            System.out.println("Beginning Local k-2Opt ***");
        }

        try {
            //there should be at least 3 visit nodes  + the 2 depot nodes to perform a local 2-opt
            if (getSize() < 5) {
                if (isDiagnostic) {
                    System.out.println("Less than 4 nodes in route: return ***");
                }

                return true;
            }

            totalKTwoOpt = 0;

            //System.out.println("Beginning Local Optimization ***");
            //System.out.println("Route before :\n"+toString());
            startPos = 1;

            //for(i=1; (i<=getSize()-2)&&(numLocalOpt<maxLocalOpt); i++)
            //there should be at least two nodes in front of startPos
            while (startPos <= (getSize() - 3)) {
                if (noChange) { //no changes took place move to the next node
                    startPos++;

                    if (isDiagnostic) {
                        System.out.println("Position moved to: " + startPos);
                    }
                }

                //if
                noChange = true;
                i = startPos; //i = 0;

                int j = 1;
                noChange = true;

                // System.out.println("i  = "+i+" dst1 = "+Dst1+ " routeSize = "+(getSize()-2) );
                while (j <= (getSize() - 2)) {
                    if ((Math.abs(i - j) >= 2) && ((i + 2) <= (getSize() - 1))) {
                        PointCell p = head.next;
                        PointCell q = head.next;

                        if ((p == q) && isDiagnostic) {
                            System.out.println("\n" +
                                "Local K2 opt: p is the same as q");
                        }

                        before = toString(); //capture the string before change

                        if (i < j) {
                            int k = 1;

                            while ((p != last) && (k < i)) {
                                p = p.next;
                                k++;
                            }

                            //while
                            int m = 1;

                            while ((q != last) && (m < j)) {
                                q = q.next;
                                m++;
                            }

                            //while
                        } //if
                        else {
                            int a = 1;

                            while ((p != last) && (a < j)) {
                                p = p.next;
                                a++;
                            }

                            //while
                            int b = 1;

                            while ((q != last) && (b < i)) {
                                q = q.next;
                                b++;
                            }

                            //while
                        }

                        //else
                        //p3 points to the same location as q
                        PointCell p1 = p.prev;
                        PointCell p2 = p.next;
                        PointCell p3 = p2.next;
                        PointCell q2 = q.next;

                        if (p == q) {
                            System.out.println("\n" +
                                "Interchanged K2 inter: p is the same as q");
                        }

                        if (p2 == q2) {
                            System.out.println("\n" +
                                "Interchanged K2 inter: p2 is the same as q2");
                        }

                        if (p3 == last) {
                            System.out.println("\n" +
                                "Interchanged K2 inter: p3 is the end of the list");
                            j++;

                            continue;
                        }

                        if (isDiagnostic) {
                            System.out.println(
                                "The cells p, p2, q, q2 to be exchanged are: " +
                                p.index + " " + p2.index + " " + q.index + " " +
                                q2.index);
                        }

                        remove(p3.index);

                        //Calculation to find out the distance change upon the insertion of the new customer in the route
                        distChange = (dist(p1.xCoord, p3.xCoord, p1.yCoord,
                                p3.yCoord) +
                            dist((p1.next).xCoord, p3.xCoord, (p1.next).yCoord,
                                p3.yCoord)) -
                            dist((p1.next).xCoord, p1.xCoord, (p1.next).yCoord,
                                p1.yCoord);

                        //Actual arrival time of the truck for this customer
                        newArrTime = p1.arrTime + p1.waitTime + p1.servTime +
                            dist(p1.xCoord, p3.xCoord, p1.yCoord, p3.yCoord);

                        //Wait time after arrival for the truck to start service at this cutomer
                        //(i.e. arrived before the earliest time)
                        waitTime = max((p3.earTime - newArrTime), 0);

                        //Calculating how much push will be required to insert this cutomer in this route
                        push = waitTime + p3.servTime + distChange;

                        if (push <= p1.next.slackTime) {
                            insertAfterCellVRPTW(p1, p3);

                            /*
                            p1.next = p3;
                            p3.prev = p1;
                            q2.prev = p2; //q2 next remains the same
                            p2.next = q2;
                            p.prev = q;
                            q.next = p;
                            */
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values
                            Dst2 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);

                            if (isDiagnostic) {
                                System.out.println("j  = " + j + " dst2 = " +
                                    Dst2 + " routeSize = " + (getSize() - 2));
                                System.out.println("Before opt:\n" + before);
                                System.out.println("After opt:\n" + toString());
                            }

                            cost_diff = Dst2 - Dst1;

                            PointCell f = head.next;
                            int flag = 0;

                            if (cost_diff >= 0) {
                                //return everything as it was before:
                                remove(p3.index);
                                insertAfterCellVRPTW(p2, p3);

                                /*                            p1.next = p;
                                p.prev = p1;
                                p2.next = p3;
                                p3.prev = p2;
                                q.next = q2;
                                q2.prev = q;
                                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);  // update values
                                */
                                if (isDiagnostic) {
                                    System.out.println("Switching back ");
                                }

                                if (!before.equals(toString())) { // the switch back did not work
                                    System.out.println(
                                        "Something wrong - switchback did not work");
                                    System.out.println(before);
                                    System.out.println(toString());
                                }
                            } //if
                            else {
                                Dst1 = Dst2;

                                if (isDiagnostic) {
                                    System.out.println(
                                        "K-2opt is permanent  = " + toString());
                                }

                                if (noChange) {
                                    noChange = false;
                                }

                                if (globalNoChange) { //value returned by the opt procedure
                                    globalNoChange = false;
                                }

                                numLocalOpt++;
                            }

                            //else
                        } else {
                            insertAfterCellVRPTW(p2, p3);

                            //                            displayForwardKeyList();
                        }
                    }

                    //inner for loop
                    j = j + 1;
                }

                //if
            }

            //outer for loop
            //System.out.println("Done optimizing.");
        } catch (Exception exc) {
            System.out.println("Error occured in KInterChange2(): " + exc);
        }

        //print the total number of 1-opt moves
        if (isDiagnostic) {
            System.out.println("Total k-2opt moves: " + numLocalOpt);
        }

        totalKTwoOpt += numLocalOpt;

        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);

        return globalNoChange;
    }

    /**
    * <p>Locally optimize the route using the k-interchange k-opt-3 optimization. In the
    * k-interchange optimization switches are performed without reversing the direction
    * of the route.</p>
    * Modified by Mike McNamara
    * modifications are associated with the VRPTW problem, such as calculating the
    * push of a node into the list
    * @return boolean true if exchanges were executed
    * */
    public synchronized boolean kInterChange3() {
        boolean isDiagnostic = false;
        boolean noChange = false;
        boolean globalNoChange = true; //value returned by the opt procedure
        int startPos = 0;
        float cost_diff = 0;
        String before;
        float Dst1 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);
        float Dst2 = 0;
        float push = 0;
        float distChange = 0;
        float newArrTime = 0;
        float waitTime = 0;
        int i = 1;
        numLocalOpt = 0;

        //if there are less than 4 nodes, return out of the loop
        if (isDiagnostic) {
            System.out.println("Beginning Local k-3Opt ***");
        }

        try {
            //there should be at least 3 visit nodes  + the 2 depot nodes to perform a local 2-opt
            if (getSize() < 6) {
                if (isDiagnostic) {
                    System.out.println("Less than 4 nodes in route: return ***");
                }

                return true;
            }

            totalKThreeOpt = 0;

            //System.out.println("Beginning Local Optimization ***");
            //System.out.println("Route before :\n"+toString());
            startPos = 1;

            //for(i=1; (i<=getSize()-2)&&(numLocalOpt<maxLocalOpt); i++)
            //there should be at least two nodes in front of startPos
            while (startPos <= (getSize() - 4)) {
                if (noChange) { //no changes took place move to the next node
                    startPos++;

                    if (isDiagnostic) {
                        System.out.println("Position moved to: " + startPos);
                    }
                }

                //if
                noChange = true;
                i = startPos; //i = 0;

                int j = 1;
                noChange = true;

                // System.out.println("i  = "+i+" dst1 = "+Dst1+ " routeSize = "+(getSize()-2) );
                while (j <= (getSize() - 2)) {
                    if ((Math.abs(i - j) >= 3) && ((i + 3) <= (getSize() - 1))) {
                        PointCell p = head.next;
                        PointCell q = head.next;
                        before = toString(); //capture the string before change

                        if (i < j) {
                            int k = 1;

                            while ((p != last) && (k < i)) {
                                p = p.next;
                                k++;
                            }

                            //while
                            int m = 1;

                            while ((q != last) && (m < j)) {
                                q = q.next;
                                m++;
                            }

                            //while
                        } //if
                        else {
                            int a = 1;

                            while ((p != last) && (a < j)) {
                                p = p.next;
                                a++;
                            }

                            //while
                            int b = 1;

                            while ((q != last) && (b < i)) {
                                q = q.next;
                                b++;
                            }

                            //while
                        }

                        //else
                        //p4 points to the same location as q
                        PointCell p1 = p.prev;
                        PointCell p2 = p.next;
                        PointCell p3 = p2.next;
                        PointCell p4 = p3.next;
                        PointCell q2 = q.next;

                        if ((p == q) || (p == p2)) {
                            System.out.println("\n" +
                                "Interchanged K3 inter: p is the same as q or p2");
                        }

                        if ((p1 == p3) || (p1 == q2)) {
                            System.out.println("\n" +
                                "Interchanged K3 inter: p1 is the same as p3,q2");
                        }

                        if (p4 == last) {
                            System.out.println("\n" +
                                "Interchanged K3 inter: p3 is the end of the list");
                            j++;

                            continue;
                        }

                        if (isDiagnostic) {
                            System.out.println("Before opt:\n" + before);
                            System.out.println(
                                "The cells p, p1, p2, p3, q, q2 to be exchanged are: " +
                                p.index + " " + p1.index + " " + p2.index +
                                " " + p3.index + " " + p4.index + " " +
                                q.index + " " + q2.index);
                        }

                        remove(p4.index);

                        //Calculation to find out the distance change upon the insertion of the new customer in the route
                        distChange = (dist(p1.xCoord, p4.xCoord, p1.yCoord,
                                p4.yCoord) +
                            dist((p1.next).xCoord, p4.xCoord, (p1.next).yCoord,
                                p4.yCoord)) -
                            dist((p1.next).xCoord, p1.xCoord, (p1.next).yCoord,
                                p1.yCoord);

                        //Actual arrival time of the truck for this customer
                        newArrTime = p1.arrTime + p1.waitTime + p1.servTime +
                            dist(p1.xCoord, p4.xCoord, p1.yCoord, p4.yCoord);

                        //Wait time after arrival for the truck to start service at this cutomer
                        //(i.e. arrived before the earliest time)
                        waitTime = max((p4.earTime - newArrTime), 0);

                        //Calculating how much push will be required to insert this cutomer in this route
                        push = waitTime + p4.servTime + distChange;

                        if (push <= p1.next.slackTime) {
                            insertAfterCellVRPTW(p1, p4);

                            /*
                            p1.next = p4;
                            p4.prev = p1;
                            q2.prev = p3; //q2 next remains the same
                            p3.next = q2;
                            p.prev = q;
                            q.next = p;
                            */
                            if (isDiagnostic) {
                                System.out.println("After opt:\n" + toString());
                                System.out.println(
                                    "The cells p, p1, p2, p3, q, q2 to be exchanged are: " +
                                    p.index + " " + p1.index + " " + p2.index +
                                    " " + p3.index + " " + p4.index + " " +
                                    q.index + " " + q2.index);
                            }

                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this); // update values
                            Dst2 = (float) ProblemInfo.vNodesLevelCostF.calculateOptimizedCost(this);

                            if (isDiagnostic) {
                                System.out.println("j  = " + j + " dst2 = " +
                                    Dst2 + " routeSize = " + (getSize() - 2));
                                System.out.println("Before opt:\n" + before);
                                System.out.println("After opt:\n" + toString());
                            }

                            cost_diff = Dst2 - Dst1;

                            PointCell f = head.next;
                            int flag = 0;

                            if (cost_diff >= 0) {
                                //return everything as it was before:
                                remove(p4.index);
                                insertAfterCellVRPTW(p3, p4);

                                /*
                                p1.next = p;
                                p.prev = p1;
                                p3.next = p4;
                                p4.prev = p3;
                                q.next = q2;
                                q2.prev = q;
                                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);  // update values
                                */
                                if (isDiagnostic) {
                                    System.out.println("After switch:\n" +
                                        toString());
                                    System.out.println(
                                        "The cells p, p1, p2, p3, q, q2 to be exchanged are: " +
                                        p.index + " " + p1.index + " " +
                                        p2.index + " " + p3.index + " " +
                                        p4.index + " " + q.index + " " +
                                        q2.index);
                                }

                                if (isDiagnostic) {
                                    System.out.println("Switching back ");
                                }

                                if (!before.equals(toString())) { // the switch back did not work
                                    System.out.println(
                                        "Something wrong - switchback did not work " +
                                        before + " " + toString());
                                }
                            } //if
                            else {
                                Dst1 = Dst2;

                                if (isDiagnostic) {
                                    System.out.println(
                                        "K-3opt is permanent  = " + toString());
                                }

                                if (noChange) {
                                    noChange = false;
                                }

                                if (globalNoChange) { //value returned by the opt procedure
                                    globalNoChange = false;
                                }

                                numLocalOpt++;
                                totalKThreeOpt += numLocalOpt;
                            }

                            // else
                        } else {
                            insertAfterCellVRPTW(p3, p4);
                        }
                    }

                    //inner for loop
                    j = j + 1;
                }

                //if
            }

            //outer for loop
            //System.out.println("Done optimizing.");
        } catch (Exception exc) {
            System.out.println("Error occured in KInterChange3(): " + exc);
        }

        //print the total number of 1-opt moves
        if (isDiagnostic) {
            System.out.println("Total k-3opt moves: " + numLocalOpt);
        }

        totalKThreeOpt += numLocalOpt;

        //compute the distance after final insertion
        //currentDuration = calculateDistance();
        setCurrentDistance(calculateDistance());

        //compute the capacity after final insertion
        //currentCapacity = calculateCapacity();
        setCurrentCapacity(calculateCapacity());

        return globalNoChange;
    }

    /**
    * <p>Get the total number of 1-opts executed for the route.</p>
    * @return int total number of 1-opts
    * */
    public synchronized int getTotalOneOpt() {
        return totalOneOpt;
    }

    /**
    * <p>Get the total number of 2-opts executed for the route</p>
    * @return int total number of 2-opts
    * */
    public synchronized int getTotalTwoOpt() {
        return totalTwoOpt;
    }

    /**
    * <p>Get the total number of 3-opts executed for the route</p>
    * @return int total number of 3-opts
    * */
    public synchronized int getTotalThreeOpt() {
        return totalThreeOpt;
    }

    /**
    * <p>Get the total number of k-2-opts executed for the route.</p>
    * @return int total number of k-2-opts
    * */
    public synchronized int getTotalKOneOpt() {
        return totalKOneOpt;
    }

    /**
    * <p>Get the total number of k-2-opts executed for the route.</p>
    * @return int total number of k-2-opts
    * */
    public synchronized int getTotalKTwoOpt() {
        return totalKTwoOpt;
    }

    /**
    * <p>Get the total number of k-3-opts executed for the route.</p>
    * @return int total number of k-3-opts
    * */
    public synchronized int getTotalKThreeOpt() {
        return totalKThreeOpt;
    }

    /**
    * <p>Calculate the total service time for the VRPTW problem.</p>
    * @return float total service time
    * */
    private synchronized float calcTotalServTime() {
        float serv = 0;
        PointCell temp = head.next;

        if (temp == null) {
            return 0;
        } else {
            while ((temp != null) && (temp != last)) {
                serv = serv + temp.servTime;
                temp = temp.next;
            }

            return serv;
        }
    }

    /**
    * <p>Calculate the total waiting time for the VRPTW problems.</p>
    * @return float total waiting time
    * */
    private synchronized float calcTotalWaitTime() {
        float wait = 0;
        PointCell temp = head.next;

        if (temp == null) {
            return 0;
        } else {
            while ((temp != head) && (temp != last)) {
                float waitTime = (float) temp.waitTime;
                wait = wait + waitTime;
                temp = temp.next;
            }

            return wait;
        }
    }

    /**
    * <p>Calculate the total service time for the VRPTW for the
    * second inserted point.</p>
    * @param cur point to be inserted
    * @param push total push
    * @return float total wait time
    * */
    private synchronized float calcTotalWaitTime(PointCell cur, float push) {
        float wait = 0;
        PointCell temp = cur.next;

        while ((temp != last) && (temp != null)) {
            float begTime = max(temp.arrTime, temp.earTime);
            float waitTime = 0;
            waitTime = max(temp.earTime - temp.arrTime - push, 0);
            wait = wait + waitTime;
            temp = temp.next;
        }

        return wait;
    }

    /**
    * <p>Get the size of the instace of the VisitingNodesLinkedLists.</p>
    * @return int size of linked listfs
    * */
    public synchronized int getSize() {
        //System.out.println("returning size = " + size);
        return size;
    }

    /**
    * <p>adjust the size of the list by the offset value</p>
    * @param offset  integer value to either increase or decrease the size of the list
    */
    public void offsetSize(int offset) {
        size += offset;
    }

    /**
    * <p>Recalculate lt for all points for the VRPTW problem.</p>
    * */
    private synchronized void recalculate_lt() {
        try {
            PointCell t = last.prev;
            PointCell sucs = t.next;
            t.lt = t.latTime;
            t = t.prev;

            if (t != null) {
                sucs = t.next;

                while ((t != head) && (t != last)) {
                    t.lt = calculateLt(t.latTime, t.servTime, t.xCoord,
                            t.yCoord, sucs);
                    t = t.prev;
                    sucs = t.next;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in recalculate lt : " + ex);
        }
    }

    /**
    * <p>Recalculate lt for all points until the upToCell for the VRPTW problem.</p>
    * @param upToCell ending cell for lt calculation
    * */
    private synchronized void recalculate_lt(PointCell upToCell) {
        PointCell t = last.prev;
        PointCell sucs = t.next;

        t.lt = t.latTime;
        t = t.prev;
        sucs = sucs.prev;

        while (t != upToCell.next) {
            t.lt = calculateLt(t.latTime, t.servTime, t.xCoord, t.yCoord, sucs);
            t = t.prev;
            sucs = t.prev;
        }
    }

    /**
    * <p>Recalculate AC and DC values for all points for the VRPTW problem.</p>
    * */
    private synchronized void recalculateAC_DC() {
        float subt = 0;
        PointCell p = head.next;

        while (p != last) {
            p.AC = ProblemInfo.maxCapacity - subt;
            subt = subt + p.demand;
            p.DC = ProblemInfo.maxCapacity - subt;
            p = p.next;
        }
    }

    /**
    * <p>Recalculate AC and DC values for all points from the FromCell for the VRPTW problem.</p>
    * @param fromCell starting location for recalculation
    * */
    private synchronized void recalculateAC_DC(PointCell fromCell) {
        float subt = fromCell.AC;
        PointCell p = fromCell.next;

        while (p != last) {
            p.AC = ProblemInfo.maxCapacity - subt;
            subt = subt + p.demand;
            p.DC = ProblemInfo.maxCapacity - subt;
            p = p.next;
        }
    }

    /**
    * <p>method to group the recalculation methods
    * such as: recalculate_arrTime(), recalculate_lt(),
    * recalculateAC_DC(), recalculateSlackTime()</p>
    * method added 11/21/03 by Mike McNamara and Sunil Gurung
    */
    public void performRecalculates() {
        //need to recalculate ArrTime - for sucsessors and u, lt - for u and predecessors,
        //MAC and MDC values must be recalculated as well as slacktime
        recalculate_arrTime(); //recalculating the arrival time for the customers
        recalculate_lt(); //recalculating the latest possible arrival time for the customers
        recalculateAC_DC(); //recalculating the arriving and departing capacity for the customers
        recalculateSlackTime(); //recalculating the slackTime for the customers
    }

    /**
    * <p>Recalculate arrival time for all for all points for the VRPTW problem.</p>
    * */
    private synchronized void recalculate_arrTime() {
        PointCell p = head.next;
        p.arrTime = dist(head.xCoord, p.xCoord, head.yCoord, p.yCoord);
        p.waitTime = max((p.earTime - p.arrTime), 0);
        p = p.next;

        if (p != null) {
            while (p != last) {
                p.arrTime = p.prev.arrTime + p.prev.servTime + p.prev.waitTime +
                    dist(p.prev.xCoord, p.xCoord, p.prev.yCoord, p.yCoord);

                p.waitTime = max((p.earTime - p.arrTime), 0);
                p = p.next;
            }
        }
    }

    /**
    * <p>Recalculate arrival time for all for all points from the fromCell for the VRPTW problem.</p>
    * @param fromCell starting location for recalculation
    * */
    private synchronized void recalculate_arrTime(PointCell fromCell) {
        PointCell p = fromCell.next;

        if (p == head.next) {
            p.arrTime = dist(head.xCoord, p.xCoord, head.yCoord, p.yCoord);
            p.waitTime = max((p.earTime - p.arrTime), 0);
            p = p.next;
        }

        while (p != last) {
            p.arrTime = p.prev.arrTime + p.prev.waitTime + p.prev.servTime +
                dist(p.prev.xCoord, p.xCoord, p.prev.yCoord, p.yCoord);

            //distTo(p.index) + servTimeTo(p.index) + waitTimeTo(p.index);
            p.waitTime = max((p.earTime - p.arrTime), 0);
            p = p.next;
        }
    }

    /**
    * <p>Recalculating the slackTime for each customer from the currently inserted customer to the end</p>
    * Added 9/27/03 by Sunil Gurung
    */
    private synchronized void recalculateSlackTime() {
        PointCell temp = head.next; //Get the first index

        try {
            while (temp != last) { //Till the end of the route
                temp.slackTime = temp.lt - temp.arrTime; //slacTime = latTime - arrTime

                //                System.out.println("TruckNo: " + temp.index + "     slackTime: " + temp.slackTime);
                temp = temp.next; //Go to next pointCell
            }

            //          System.out.println("\n\n\n");
        } catch (Exception ie) {
            System.out.println("Exception in recalculateSlackTime " + ie);
        }
    }

    /**
    * Updating the minimum slacktime for the whole route
    * @param None
    *
    private synchronized void updateOverallSlackTime() {
    PointCell temp = last.prev; //Start from the beginning
    float minimum = 0; //To store the minimim slack time

    try {
        while (temp != head) { //Till the first customer in the route

            if (temp == last.prev) { //If the customer is the last one in the route excluding the depot
                minimum = temp.slackTime; //Then the minimum slacktime is it's one slackTime
                temp.slackTime = minimum;

                // System.out.println("TruckNo: " + temp.index + "     MinslackTime: " + temp.minSlackTime);
            } else {
                minimum = min(minimum, temp.slackTime); //Find the minimum slackTime
                temp.slackTime = minimum;

                //System.out.println("TruckNo: " + temp.index + "     MinslackTime: " + temp.minSlackTime);
            }

            temp = temp.prev; //Go to the previous one
        }

        //System.out.println("\n\n\n");
    } catch (Exception ie) {
        System.out.println("Exception in updateOverallSlackTime " + ie);
    }
    }
    */
    /**
    * <p>Calculate distance travelled from depot to a specific point with index iIndex
    * for the VRPTW problem.</p>
    * @param iIndex total distance from depot to shipment iIndex
    * @return float total distance
    * */
    private synchronized float distTo(int iIndex) {
        PointCell p = head;
        float dis = 0;

        while ((p != last) && (p.index != iIndex)) {
            dis = dis +
                (float) dist(p.xCoord, (p.next).xCoord, p.yCoord,
                    (p.next).yCoord);
            p = p.next;
        }

        return dis;
    }

    /**
    * <p>Calculate service time from depot to a specific point with index iIndex
    * for the VRPTW problem.</p>
    * @param iIndex total service time from depot to shipment iIndex
    * @return float total service time
    * */
    private synchronized float servTimeTo(int iIndex) {
        float serv = 0;
        PointCell p = head;

        while ((p != last) && (p.index != iIndex)) {
            serv = serv + p.servTime;
            p = p.next;
        }

        return serv;
    }

    /**
    * <p>Calculate waiting time from depot to a specific point with index iIndex
    * for the VRPTW problem.</p>
    * @param iIndex total waiting time from depot to shipment iIndex
    * @return float total waiting time
    * */
    private synchronized float waitTimeTo(int iIndex) {
        float wait = 0;
        PointCell p = head;

        while ((p != last) && (p.index != iIndex)) {
            wait = (wait + max(p.arrTime, p.earTime)) - p.arrTime;
            p = p.next;
        }

        return wait;
    }

    /**
    * <p>Calculate total cost  for the VRPTW problem.(shipper agent uses it)</p>
    * @param sJob job whose cost is to be calculated
    * @return String index and the cost
    * */
    public synchronized String calculateCost(String sJob) {
        String msg = "";

        try {
            float cost = 999999999;

            StringTokenizer st = new StringTokenizer(sJob, ";");
            int index = (Integer.valueOf(st.nextToken())).intValue();
            float xCoord = (Float.valueOf(st.nextToken())).floatValue();
            float yCoord = (Float.valueOf(st.nextToken())).floatValue();
            float demand = (Float.valueOf(st.nextToken())).floatValue();
            float ear = (Float.valueOf(st.nextToken())).floatValue();
            float latTime = (Float.valueOf(st.nextToken())).floatValue();
            float serv = (Float.valueOf(st.nextToken())).floatValue();

            //System.out.println("Done parsing "+index+";"+xCoord+";"+ yCoord+";"+demand+";"+latTime+";"+serv);
            //System.out.println("Route = "+Route);
            //System.out.println("Size = "+ Route.getSize());
            if (getSize() == 2) {
                //System.out.println("seedCustomerCost");
                cost = seedCustomerCost(index, xCoord, yCoord, ear, latTime,
                        demand, serv);
                msg = msg + index + ";" + cost + ";0";

                //System.out.println("After before Route.seedCustomerCost");
            } else {
                //System.out.println("insertCost");
                msg = index + ";" +
                    insertCost(index, xCoord, yCoord, demand, ear, latTime, serv);

                //System.out.println("calc "+Route.toString());
            }

            //post it to the IntransDB
            //System.out.println("COST;INDEX = "+msg);
        } catch (Exception exc) {
            System.out.println("Error in Customer.CalculateCost " + exc);
        }

        return msg;
    }

    /**
    * <p>Insert customer into the linked list for the VRPTW problem.(shipper agent uses it)</p>
    * @param sJob job whose cost is to be calculated
    * */
    public synchronized void InsertCustomer(String sJob) {
        try {
            StringTokenizer st = new StringTokenizer(sJob, ";");

            //System.out.println("inserting");
            int index = (Integer.valueOf(st.nextToken())).intValue();
            float xCoord = (Float.valueOf(st.nextToken())).floatValue();
            float yCoord = (Float.valueOf(st.nextToken())).floatValue();
            float demand = (Float.valueOf(st.nextToken())).floatValue();
            float ear = (Float.valueOf(st.nextToken())).floatValue();
            float latTime = (Float.valueOf(st.nextToken())).floatValue();
            float serv = (Float.valueOf(st.nextToken())).floatValue();
            int after = (Integer.valueOf(st.nextToken())).intValue();

            //System.out.println("Route size = "+getSize());
            insert(index, xCoord, yCoord, demand, ear, latTime, serv, after);

            //Route.append(index,xCoord,yCoord,demand,ear,latTime,serv);
            //System.out.println(toString());
            //iniDB =   new CarrierTransactor();
            //post it to the IntransDB
        } catch (Exception e1) {
            System.out.println("error inserting customer into carrier route.");
        }
    }

    /**
    * <p>Write out the short form of the cell information for all shipments in
    * the list into the solOutFile file.</p>
    * @param solOutFile name of the output file
    * */
    public void writeShortNodesSol(PrintWriter solOutFile) {
        solOutFile.print(" " + this.size + " ");

        PointCell current = head;

        while (current != null) {
            current.writeShortPointCell(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
    * <p>Write out the detail form of the cell information for all shipments in
    * the list into the solOutFile file.</p>
    * Modified by Mike McNamara and Sunil Gurung
    * modifications involve the output of the total number of local
    * optimizations of each level
    * @param solOutFile name of the output file
    * */
    public void writeDetailNodesSol(PrintWriter solOutFile) {
        solOutFile.println("     Total local 1-Opts " + totalKOneOpt);
        solOutFile.println("     Total local 2-Opts " + totalKTwoOpt);
        solOutFile.println("     Total local 3-Opts " + totalKThreeOpt);
        solOutFile.println("          Total number of cells: " + this.size);

        PointCell current = head;

        while (current != null) {
            current.writePointCell(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    //============================================================================================================
    //======================= START (TABU) FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =========================
    //============================================================================================================

    /**
    * <p>Calculate the cost of exchange01 (accept(0)- remove(1)) for shipment st1 using the
    * cost function in ProblemInfo.
    * The function used to calculate the cost is calcExchCostMDVRPOpt(). The shipment st1 is
    * removed and the differential cost is returned. St1 is inserted in the same location.</p>
    * @param st1 shipment to be removed
    * @return String cost removing shipment st1
    *
    * */
    public synchronized String calcExchange01Opt(String st1) {
        boolean isDiagnostic = false;
        StringTokenizer s1 = new StringTokenizer(st1, ";");
        int index1 = (Integer.valueOf(s1.nextToken())).intValue();
        float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

        float before_cost = calcExchCostMDVRPOpt();

        if (isDiagnostic) {
            System.out.println("Index being removed " + index1);
            System.out.println("Route before 01: Travel Time " + before_cost +
                "\n" + toString());
        }

        int after_index1 = remove(index1);
        float after_cost = calcExchCostMDVRPOpt();

        if (ProblemInfo.isUsingTabu && (after_cost < Float.MAX_VALUE)) {
            after_cost = (1 - ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost;
        }

        if (isDiagnostic) {
            System.out.println("Index being removed " + index1);
            System.out.println("Route after 01: Travel Time " + after_cost +
                " " + toString());
        }

        float cost_diff = after_cost - before_cost;
        insert(index1, xCoord1, yCoord1, demand1, after_index1);

        //recompute the distance and capacity after the moves
        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);

        return "" + cost_diff + "";
    }

    /**
    * <p>Calculate the cost of exchange10 (accept(1)- remove(0)) for a MDVRP shipment st1 using
    * the cost function in ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRPOpt(); Shipment
    * st1 is inserted into the current route and the cost difference before
    * and after the insertion and the best location of insertion is returned.</p>
    * @param st1 shipment used for computing the exchange cost (not inserted in this method)
    * @return String cost of exchange and best location to insert st1
    *
    * */
    public synchronized String calcExchange10Opt(String st1) {
        boolean isDiagnostic = false;
        StringTokenizer s1 = new StringTokenizer(st1, ";");
        String str1;
        int index1 = (Integer.valueOf(s1.nextToken())).intValue();
        float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
        float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

        float before_cost = calcExchCostMDVRPOpt();

        if (isDiagnostic) {
            System.out.println("Index being added " + index1);
            System.out.println("Route before 10: Travel Time " + before_cost +
                "\n" + toString());
        }

        //insert the node, calculate the cost and then remove the node
        str1 = exchInsertCostOpt(index1, xCoord1, yCoord1, demand1);

        StringTokenizer s2 = new StringTokenizer(str1, ";");
        float after_cost = (Float.valueOf(s2.nextToken())).floatValue();
        int afterIndex = (Integer.valueOf(s2.nextToken())).intValue();

        if (ProblemInfo.isUsingTabu && (after_cost < Float.MAX_VALUE)) {
            after_cost = (1 - ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost;
        }

        if (isDiagnostic) {
            System.out.println("Index being added " + index1);
            System.out.println("Route after 10: Travel Time " + after_cost +
                " " + toString());
        }

        float cost_diff = after_cost - before_cost;

        //recompute the distance and capacity after the moves
        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);

        return "" + cost_diff + ";" + afterIndex;
    }

    /**
    * <p>Calculate the cost of exchange02 (accept(0)- remove(2)) for a MDVRP with the
    * shipments st1 and st2 using the cost funtion on ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRPOpt(); Remove st1 and st2.
    * Calculate and return the cost differential before and after the removal. Insert
    * st1 and st2 into the route.</p>
    * @param st1 shipment to be removed
    * @param st2 shipment ro be removed
    * @return String cost of removing st1 and st2
    *
    * */
    public synchronized float calcExchange02Opt(String st1, String st2) {
        boolean isDiagnostic = false;
        float cost_diff = 0;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            float before_cost = calcExchCostMDVRPOpt();

            if (isDiagnostic) {
                System.out.println("Remove " + index1 + " and " + index2);
            }

            int after_index1 = remove(index1);
            int after_index2 = index1;
            remove(index2);

            float after_cost = calcExchCostMDVRPOpt();

            if (ProblemInfo.isUsingTabu && (after_cost < Float.MAX_VALUE)) {
                after_cost = (1 - ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost;
            }

            cost_diff = after_cost - before_cost;

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost + ": " + index2);
            }

            insert(index1, xCoord1, yCoord1, demand1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, after_index2);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception exc) {
            System.out.println("ERROR: calcExchange02Opt: " + exc);
        }

        return cost_diff;
    }

    /**
    * <p>Calculate the cost of exchange11  (accept(1)- remove(1))for a MDVRP with the cells st1 and st2
    * using the cost function in ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRP()Opt. Remove shipment st1.
    * Compute the exchange cost of inserting shipment st2 and then reinsert shipment st1.</p>
    * @param st1 shipment to be removed
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @return String cost of exchange and the best location to insert st2
    *
    * */
    public synchronized String calcExchange11Opt(String st1, String st2) {
        boolean isDiagnostic = false;
        float cost_diff = 0;
        int after_index = -1;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            float before_cost = calcExchCostMDVRPOpt();

            if (isDiagnostic) {
                System.out.println("Remove " + index1 + " " + "Accept " +
                    index2);
            }

            int after_index1 = remove(index1);

            if (after_index1 == -1) {
                System.out.println("Can't find  index " + index1 + " in \n" +
                    toString());

                return "" + Float.MAX_VALUE + ";" + after_index;
            }

            if (isDiagnostic) {
                System.out.println("Index of removed point " + index1);
            }

            //insert the point, calculate the cost and remove the point
            String ncost = exchInsertCostOpt(index2, xCoord2, yCoord2, demand2);
            StringTokenizer st = new StringTokenizer(ncost, ";");
            float after_cost = (Float.valueOf(st.nextToken())).floatValue();
            after_index = (Integer.valueOf(st.nextToken())).intValue();

            if (ProblemInfo.isUsingTabu && (after_cost < Float.MAX_VALUE)) {
                after_cost = (1 - ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost;
            }

            cost_diff = after_cost - before_cost;

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost + ": " + index2);
            }

            insert(index1, xCoord1, yCoord1, demand1, after_index1);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception exc) {
            System.out.println("ERROR: calcExchange11Opt: " + exc);
        }

        return "" + cost_diff + ";" + after_index;
    }

    /**
    * <p>Calculate the cost of exchange12 (accept(1)- remove(2)) for a MDVRP with the
    * shipments st1, st2 and st3 using the cost function in ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRPOpt(); Remove st2 and st3. Insert st1
    * into the current route. Reinsert shipments st2 and st3 back into the routes.
    * Return the difference in cost and the best location for insertion of st1.</p>
    * @param st1 shipment used for computing the exchange cost (not inserted in this method)
    * @param st2 shipment to be removed
    * @param st3 shipment to be removed
    * @return String cost of exchange and best location to insert st3
    *
    * */
    public synchronized String calcExchange12Opt(String st1, String st2,
        String st3) {
        boolean isDiagnostic = false;
        float cost_diff = 0;
        int after_index = -1;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();

            float before_cost = calcExchCostMDVRPOpt();

            if (isDiagnostic) {
                System.out.println("Accept " + index1 + " " + "Remove " +
                    index2 + " and " + index3);
            }

            int after_index2 = remove(index2);
            int after_index3 = index2;
            remove(index3);

            String ncost = exchInsertCostOpt(index1, xCoord1, yCoord1, demand1);
            StringTokenizer st = new StringTokenizer(ncost, ";");
            float after_cost = (Float.valueOf(st.nextToken())).floatValue();
            after_index = (Integer.valueOf(st.nextToken())).intValue();

            if (ProblemInfo.isUsingTabu && (after_cost < Float.MAX_VALUE)) {
                after_cost = (1 - ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost;
            }

            cost_diff = after_cost - before_cost;

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost + ": " + index2);
            }

            insert(index2, xCoord2, yCoord2, demand2, after_index2);
            insert(index3, xCoord3, yCoord3, demand3, after_index3);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception exc) {
            System.out.println("ERROR: calcExchange12Opt: " + exc);
        }

        return "" + cost_diff + ";" + after_index;
    }

    /**
    * <p>Calculate the cost of exchange20 (accept(2)- remove(2)) for a MDVRP with the shipments st1 and st2 using
    * the cost function in ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRPOpt(); Insert st1 and st2 and
    * computing the cost of the route. Return the differential in cost and the best
    * locations for inserting shipment st1 and st2. Remove st1 from the route.</p>
    * @param st1 shipment to be inserted
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @return String cost of inserting shipments st1 and st2 and best insertion locations
    *
    * */
    public synchronized String calcExchange20Opt(String st1, String st2) {
        boolean isDiagnostic = false;
        float cost_diff = 0;
        int after_index1 = -1;
        int after_index2 = -1;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            float before_cost = calcExchCostMDVRPOpt();

            if (isDiagnostic) {
                System.out.println("Add " + index1 + " and " + index2);
            }

            String ncost1 = exchInsertCostOpt(index1, xCoord1, yCoord1, demand1);
            StringTokenizer st = new StringTokenizer(ncost1, ";");
            float after_cost1 = (Float.valueOf(st.nextToken())).floatValue();
            after_index1 = (Integer.valueOf(st.nextToken())).intValue();

            if (after_index1 != -1) {
                insert(index1, xCoord1, yCoord1, demand1, after_index1);

                String ncost2 = exchInsertCostOpt(index2, xCoord2, yCoord2,
                        demand2);
                StringTokenizer stt = new StringTokenizer(ncost2, ";");
                float after_cost2 = (Float.valueOf(stt.nextToken())).floatValue();
                after_index2 = (Integer.valueOf(stt.nextToken())).intValue();

                if (ProblemInfo.isUsingTabu && (after_cost2 < Float.MAX_VALUE)) {
                    after_cost2 = (1 -
                        ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost2;
                }

                cost_diff = after_cost2 - before_cost;

                if (isDiagnostic) {
                    System.out.println(
                        "   Cost after insertion/removal of point " +
                        cost_diff + ": " + index2);
                }

                remove(index1);
            } else {
                cost_diff = Float.MAX_VALUE;
                after_index1 = -1;
                after_index2 = -1;

                if (isDiagnostic) {
                    System.out.println(
                        "   Cost after insertion/removal of point " +
                        cost_diff + ": " + index2);
                }
            }

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception exc) {
            System.out.println("ERROR: calcExchange20Opt: " + exc);
        }

        return "" + cost_diff + ";" + after_index1 + ";" + after_index2;
    }

    /**
    * <p>Calculate the cost of exchange21 (accept(2)- remove(1) for a MDVRP with the shipments
    * st1, st2 and st3 using the cost function in ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRPOpt(); Remove st3. Calculate
    * the cost of inserting st1 and st2. Shipment st3 is reinserted into the route.
    * Calculate the total cost differential for
    * the whole process then reinsert st3 back into the route.</p>
    * @param st1 shipment inserted into route.
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @param st3 shipment to be removed
    * @return String cost of exchange and best locations to insert st1 and st2
    *
    * */
    public synchronized String calcExchange21Opt(String st1, String st2,
        String st3) {
        boolean isDiagnostic = false;
        float cost_diff = 0;

        //for keepint track of the two locations for insertions
        int after_index1 = -1;
        int after_index2 = -1;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();

            float before_cost = calcExchCostMDVRPOpt();

            if (isDiagnostic) {
                System.out.println("Accept " + index1 + " and " + index2 +
                    " Remove " + index3);
            }

            int after_index3 = remove(index3);
            String ncost1 = exchInsertCostOpt(index1, xCoord1, yCoord1, demand1);
            StringTokenizer st = new StringTokenizer(ncost1, ";");
            float after_cost1 = (Float.valueOf(st.nextToken())).floatValue();
            after_index1 = (Integer.valueOf(st.nextToken())).intValue();

            if (isDiagnostic) {
                System.out.println("   Cost after insertion/removal of point " +
                    after_cost1 + ": " + index1);
            }

            if (after_index1 != -1) {
                insert(index1, xCoord1, yCoord1, demand1, after_index1);

                String ncost2 = exchInsertCostOpt(index2, xCoord2, yCoord2,
                        demand2);
                StringTokenizer stt = new StringTokenizer(ncost2, ";");
                float after_cost2 = (Float.valueOf(stt.nextToken())).floatValue();
                after_index2 = (Integer.valueOf(stt.nextToken())).intValue();

                if (ProblemInfo.isUsingTabu && (after_cost2 < Float.MAX_VALUE)) {
                    after_cost2 = (1 -
                        ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost2;
                }

                cost_diff = after_cost2 - before_cost;

                if (isDiagnostic) {
                    System.out.println(
                        "   Cost after insertion/removal of point " +
                        cost_diff + ": " + index2);
                }

                remove(index1);
            } else {
                cost_diff = Float.MAX_VALUE;
                after_index1 = -1;
                after_index2 = -1;
            }

            insert(index3, xCoord3, yCoord3, demand3, after_index3); //insert the node back

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception exc) {
            System.out.println("ERROR: calcExchange21Opt: " + exc);
        }

        return "" + cost_diff + ";" + after_index1 + ";" + after_index2;
    }

    /**
    * <p>Calculate the cost of exchange22 for a MDVRP with the shipments st1,st2, st3 and st4
    * using the cost function in ProblemInfo. The
    * function used to calculate the cost is calcExchCostMDVRP()Opt; Remove st3 and st4.
    * Insert shipment st1 and calculate the exchange cost of insertion st2. Return
    * the cost differential and the best location for inserting shipments st1 and st2.
    * Remove shipment st1 and reinsert shipment st3 and st4 in original locations. The
    * insertion of two shipments does not guarantee that they will be consecutive. The best
    * locations for inserting them are located.</p>
    * @param st1 shipment to be inserted
    * @param st2 shipment used for computing the exchange cost (not inserted in this method)
    * @param st3 shipment to be removed
    * @param st4 shipment to be removed
    * @return String cost of inserting removing shipments st3 and st4 and inserting shipments st1 and st2
    *
    * */
    public synchronized String calcExchange22Opt(String st1, String st2,
        String st3, String st4) {
        float cost_diff = 0;
        int after_index1 = -1;
        int after_index2 = -1;

        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();

            StringTokenizer s4 = new StringTokenizer(st4, ";");
            int index4 = (Integer.valueOf(s4.nextToken())).intValue();
            float xCoord4 = (Float.valueOf(s4.nextToken())).floatValue();
            float yCoord4 = (Float.valueOf(s4.nextToken())).floatValue();
            float demand4 = (Float.valueOf(s4.nextToken())).floatValue();

            float before_cost = calcExchCostMDVRPOpt();

            int after_index3 = remove(index3);
            int after_index4 = index3;
            remove(index4);

            String ncost1 = exchInsertCostOpt(index1, xCoord1, yCoord1, demand1);
            StringTokenizer st = new StringTokenizer(ncost1, ";");
            float after_cost1 = (Float.valueOf(st.nextToken())).floatValue();
            after_index1 = (Integer.valueOf(st.nextToken())).intValue();

            if (after_index1 != -1) {
                insert(index1, xCoord1, yCoord1, demand1, after_index1);

                String ncost2 = exchInsertCostOpt(index2, xCoord2, yCoord2,
                        demand2);
                StringTokenizer stt = new StringTokenizer(ncost2, ";");
                float after_cost2 = (Float.valueOf(stt.nextToken())).floatValue();
                after_index2 = (Integer.valueOf(stt.nextToken())).intValue();

                if (ProblemInfo.isUsingTabu && (after_cost2 < Float.MAX_VALUE)) {
                    after_cost2 = (1 -
                        ProblemInfo.tabuSearch.getTabuThreshold()) * after_cost2;
                }

                cost_diff = after_cost2 - before_cost;
                remove(index1);
            }

            if ((after_index3 == -1) || (after_index4 == -1)) {
                System.out.println(
                    "VisitNodes: After_index3 or after_index4 is negative");
            }

            insert(index3, xCoord3, yCoord3, demand3, after_index3);
            insert(index4, xCoord4, yCoord4, demand4, after_index4);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception exc) {
            System.out.println("ERROR: calcExchange22Opt: " + exc);
        }

        return "" + cost_diff + ";" + after_index1 + ";" + after_index2;
    }

    /**
    * <p>Calculate the exchange insertion cost for the VRP/MDVRP problem of shipment
    * index iIndex and then remove it using the cost function in ProblemInfo. The
    * insertion method used is insertMDVRP and the exchange cost used is calcExchCostMDVRPOpt().</p>
    * @param iIndex  index of the shipment
    * @param lX      x coordinate of the shipment
    * @param lY      y coordinate of the shipment
    * @param lDemand customer demand
    * @return String exchange insertion cost
    *
    * */
    public synchronized String exchInsertCostOpt(int iIndex, float lX,
        float lY, float lDemand) {
        boolean isDiagnostic = false;
        float cost = Float.MAX_VALUE;
        int insertIndex = -1;

        try {
            //insert the node into the current MDVRP linked list
            //When the node is inserted the total distance travelled and capacity
            //are updated.  When removing the cell, run the calculate to update
            //the travel time and capacity
            //The insertMDVRp should take into consideration the distance and
            //the capacity
            //insert the shipment
            insertIndex = insertMDVRP(iIndex, lX, lY, lDemand);

            //if the node could not be inserted, then insertPtr will be null
            if (insertIndex != -1) { //insertion did take place
                cost = calcExchCostMDVRPOpt(); //using cost function defined in ProblemInfo

                if (isDiagnostic) {
                    System.out.println("After Insertion of " + iIndex +
                        " Distance: " + cost + "\n " + toString());
                }

                //remove the node after the insertion
                remove(iIndex);

                //when the node is removed, the distance and capacity needs to be updated
                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
            } else { //insert did no take place
                cost = Float.MAX_VALUE;

                if (isDiagnostic) {
                    System.out.println("No Insertion of " + iIndex +
                        " Distance: " + cost + "\n" + toString());
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: exchInsertCostOpt: " + ex);
        }

        return cost + ";" + insertIndex;
    }

    /**
    * <p>Perform the permanent exchange01 (accept(0)- remove(1)) of shipment st1
    * using the cost function in ProblemInfo.
    * Remove shipment st1 from current route.</p>
    * @param st1 shipment to be removed
    *
    * */
    public synchronized void exchange01Opt(String st1) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();
            float ear1 = (Float.valueOf(s1.nextToken())).floatValue();
            float latTime1 = (Float.valueOf(s1.nextToken())).floatValue();
            float serv1 = (Float.valueOf(s1.nextToken())).floatValue();

            remove(index1);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);

            //insert(inndex1, xCoord1, yCoord1, demand1,ear1, latTime1,serv1,after_index);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange01Opt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange02 (accept(0)- remove(2)) of shipments st1 and st2. Remove
    * shipments st1 and st2 using the cost function in ProblemInfo.</p>
    * @param st1 shipment to be removed
    * @param st2 shipment to be removed
    *
    * */
    public synchronized void exchange02Opt(String st1, String st2) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            int after_index1 = remove(index1);
            int after_index2 = remove(index2);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange02Opt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange10 of shipments st1 ( accept(1)- remove(0))
    * using the cost function in ProblemInfo. sInsert shipment st1
    * into location after_index. Update the currentDuration and currentCapacity.</p>
    * @param st1 shipment to be inserted into the route
    * @param after_index best location for inserting st1
    *
    * */
    public synchronized void exchange10Opt(String st1, int after_index) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            insert(index1, xCoord1, yCoord1, demand1, after_index);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange10Opt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange11(accept(1)- remove(1)) of shipments st1 and st2
    * using the cost function in ProblemInfo. Remove
    * the shipment st1 and and insert shipment st2  in location after_index.</p>
    * @param st1 shipment to be removed
    * @param st2 shipment to be inserted
    * @param after_index location into which shipment st2 is to be inserted
    *
    * */
    public synchronized void exchange11Opt(String st1, String st2,
        int after_index) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            PointCell p = getPointCell(after_index);

            if ((after_index != -1) && (p != null)) {
                remove(index1);
                insert(index2, xCoord2, yCoord2, demand2, after_index);

                //recompute the distance and capacity after the moves
                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
            } else {
                System.out.println("trying to insert after " + index2 +
                    " p = " + p);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: exchange11Opt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange12 (accept(1)- remove(2)) of shipments st1, st2 and st3
    * using the cost function in ProblemInfo. Remove
    * shipments st2 and st3 and insert shipment st1 in location after_index.</p>
    * @param st1 information on first shipment
    * @param after_index1 location into which shipment st1 is to be inserted
    * @param st2 information on second shipment
    * @param st3 information on third shipment
    *
    * */
    public synchronized void exchange12Opt(String st1, int after_index1,
        String st2, String st3) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();

            int after_index2 = remove(index2);
            int after_index3 = remove(index3);
            insert(index1, xCoord1, yCoord1, demand1, after_index1);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange12pt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange20 (accept(2)- remove(0)) of shipments st1 and st2. Insert
    * shipments st1 and st2 into locations after_index1 and after_index2
    * respectively using the cost function in ProblemInfo.</p>
    * @param st1 shipment to be inserted
    * @param after_index1  best location for inserting shipment st1
    * @param st2 shipment to be inserted
    * @param after_index2  best location for inserting shipment st2
    *
    * */
    public synchronized void exchange20Opt(String st1, int after_index1,
        String st2, int after_index2) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            insert(index1, xCoord1, yCoord1, demand1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, after_index2);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange20Opt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange21 (accept(2)- remove(1)) of shipments st1,
    * st2 and st3 using the cost function in ProblemInfo.
    * Remove shipment st3 and insert shipment st1 and st2 in
    * locations after_index1 and after_index2.</p>
    * @param st1 first shipment to be inserted
    * @param st2 second shipment to be inserted
    * @param after_index1 location into which shipment st1 is to be inserted
    * @param after_index2 location into which shipment st2 is to be inserted
    * @param st3 shipment to be removed
    *
    * */
    public synchronized void exchange21Opt(String st1, int after_index1,
        String st2, int after_index2, String st3) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();

            int after_index3 = remove(index3);
            insert(index1, xCoord1, yCoord1, demand1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, after_index2);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange21Opt: " + ex);
        }
    }

    /**
    * <p>Perform the permanent exchange22 ( accept(2)- remove(2)) of shipments st1,
    * st2, st3 and st4 using the cost function in ProblemInfo.
    * Remove shipments st3 and st4 and insert shipments st1 and st2.</p>
    * @param st1 shipment to be inserted
    * @param after_index1 best location for inserting shipment st1
    * @param st2 shipment to be inserted
    * @param after_index2 best location for inserting shipment st2
    * @param st3 shipment to be removed
    * @param st4 shipment to be removed
    *
    * */
    public synchronized void exchange22Opt(String st1, int after_index1,
        String st2, int after_index2, String st3, String st4) {
        try {
            StringTokenizer s1 = new StringTokenizer(st1, ";");
            int index1 = (Integer.valueOf(s1.nextToken())).intValue();
            float xCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float yCoord1 = (Float.valueOf(s1.nextToken())).floatValue();
            float demand1 = (Float.valueOf(s1.nextToken())).floatValue();

            StringTokenizer s2 = new StringTokenizer(st2, ";");
            int index2 = (Integer.valueOf(s2.nextToken())).intValue();
            float xCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float yCoord2 = (Float.valueOf(s2.nextToken())).floatValue();
            float demand2 = (Float.valueOf(s2.nextToken())).floatValue();

            StringTokenizer s3 = new StringTokenizer(st3, ";");
            int index3 = (Integer.valueOf(s3.nextToken())).intValue();
            float xCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float yCoord3 = (Float.valueOf(s3.nextToken())).floatValue();
            float demand3 = (Float.valueOf(s3.nextToken())).floatValue();

            StringTokenizer s4 = new StringTokenizer(st4, ";");
            int index4 = (Integer.valueOf(s4.nextToken())).intValue();
            float xCoord4 = (Float.valueOf(s4.nextToken())).floatValue();
            float yCoord4 = (Float.valueOf(s4.nextToken())).floatValue();
            float demand4 = (Float.valueOf(s4.nextToken())).floatValue();

            int after_index3 = remove(index3);
            int after_index4 = remove(index4);

            if ((after_index1 == -1) || (after_index2 == -1)) {
                System.out.println(
                    "VisitNodes: Exchange 22: after_index1 and after_index2 is negative");
            }

            insert(index1, xCoord1, yCoord1, demand1, after_index1);
            insert(index2, xCoord2, yCoord2, demand2, after_index2);

            //recompute the distance and capacity after the moves
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(this);
        } catch (Exception ex) {
            System.out.println("ERROR: exchange22Opt: " + ex);
        }
    }
}


//============================================================================================================
//============================ END FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =============================
//============================================================================================================
//end VisitNodesLinkedList
