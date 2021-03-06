package Zeus;

import java.io.*; //input-output java package

import java.util.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  The TruckLinkedList class has all the trucks available for scheduling. An
 * instance of a TruckLinkedList is always part of a Depot. That is, an instance of a Depot is
 * used to instantiate an instance of a TruckLinkedList.</p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:</p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class TruckLinkedList implements java.io.Serializable {
    public Truck first;
    public Truck last;
    public int noTrucks = 0; //total number of trucks in the linked list   made public 10/13/03 by Mike McNamara
    public int totalNonEmptyTrucks = 0; //total number of non-empty trucks   made public 10/13/03 by Mike McNamara
    public float totalDistance = 0; //total distance of all trucks
    public float totalDemand = 0; //total demand of all trucks

    /********************/
    public float totalTardiness; //total tardiness of all the nodes in the depots
    public float totalOverload; //total overload of all the nodes in the depots
    public float totalExcessTime; //total excess time of all the nodes in the depots
    public float totalWaitTime; //total wait time of all the nodes in the depots
    public float totalTotalTravelTime; //total travel time of all the nodes in the depots
    public float totalServiceTime; //total service time of all the nodes in the depots
    public float totalCost; // total cost of the schedule
    public int[] truckSwap = new int[2]; // will contain the truck indexes for the cyclic exchange
    public int cycle = 0; // hold the current cycle that the cyclic exchange is operating on
    public boolean swap = false; // flag to determine if trucks should be swapped in a cyclic exchange
    int assignHeadTruck; // head truck in a variable cyclic or acyclic exchange; from MDVRPTW problem

    //total number of local opts that took place
    int totalOneOpt;

    //total number of local opts that took place
    int totalTwoOpt;

    //total number of local opts that took place
    int totalThreeOpt;

    //total number of local opts that took place *added 10/24/03 Mike McNamara*
    int totalKOneOpt;

    //total number of local opts that took place
    int totalKTwoOpt;

    //total number of local opts that took place
    int totalKThreeOpt;

    //total number of exchanges and relocations that took place
    int total01;

    //total number of exchanges and relocations that took place
    int total11;

    //total number of exchanges and relocations that took place
    int total02;

    //total number of exchanges and relocations that took place
    int total12;

    //total number of exchanges and relocations that took place
    int total22;

    /**
 * <p>Constructor for the TruckLinkedList. The first and last nodes are set to NULL
 * and the number of trucks is set to 0.</p>
 *
 * */
    public TruckLinkedList() {
        first = null;
        last = null;
        setNoTrucks(0);
        truckSwap[0] = truckSwap[1] = -1; // set flags
    }

    /**
 * <p>Load the nodes into the array to check for consistency. All
 * the shipment numbers are checked including duplicate shipment
 * numbers.</p>
 * @param nodesArray the array consisting of the shipment nodes
 *
 * */
    public void checkNodesMDVRP(int[] nodesArray) {
        //System.out.print("     List (first to last): ");
        Truck current = first;

        while (current != null) {
            //nodesArray[current.truckNo] = nodesArray[current.truckNo]+1;
            current.mainVisitNodes.checkNodesMDVRP(nodesArray);
            current = current.next;
        }
    }

    /**
 * <p>Set the total number of trucks present in the linked list</p>
 * @param value total number of trucks
 * @return int total number of trucks in linked list
 *
 * */
    public int setNoTrucks(int value) {
        noTrucks = value;

        return noTrucks;
    }

    /**
 * <p>Get the total number of trucks present in the linked list</p>
 * @return int get the total number of trucks in linked list
 *
 * */
    public int getNoTrucks() {
        return noTrucks;
    }

    /**
 * <p>Returns a pointer to the first truck in the linked list</p>
 * @return first truck in linked list
 */
    public Truck getFirst() {
        return first;
    }

    /**
 * <p>Returns a pointer to the first truck in the linked list</p>
 * @return Truck  last node in the list
 */
    public Truck getLast() {
        return last;
    }

    /**
 * <p>After performing 1-opt for trucks, set the number of moves done.</p>
 * @param value total 1-opts for the trucks
 * @return value total 1-opts for the trucks
 *
 * */
    public int setTotalOneOpt(int value) {
        totalOneOpt = value;

        return totalOneOpt;
    }

    /**
 * <p>Get the total number of 1-opts for the trucks</p>
 * @return value total 1-opts for the trucks
 *
 * */
    public int getTotalOneOpt() {
        return totalOneOpt;
    }

    /**
 * <p>After performing 2-opt for trucks, set the number of moves done.</p>
 * @param value total 2-opts for the trucks
 * @return value total 2-opts for the trucks
 *
 * */
    public int setTotalTwoOpt(int value) {
        totalTwoOpt = value;

        return totalTwoOpt;
    }

    /**
 * <p>Get the total number of 2-opts for the trucks</p>
 * @return value total 2-opts for the trucks
 *
 * */
    public int getTotalTwoOpt() {
        return totalTwoOpt;
    }

    /**
 * <p>After performing 3-opt for trucks, set the number of moves done.</p>
 * @param value total 3-opts for the trucks
 * @return value total 3-opts for the trucks
 *
 * */
    public int setTotalThreeOpt(int value) {
        totalThreeOpt = value;

        return totalThreeOpt;
    }

    /**
 * <p>Get the total number of 3-opts for the trucks</p>
 * @return value total 3-opts for the trucks
 *
 * */
    public int getTotalThreeOpt() {
        return totalThreeOpt;
    }

    /**
 * <p>After performing k-2-opt for trucks, set the number of moves done.</p>
 * @param value total k-2-opts for the trucks
 * @return value total k-2-opts for the trucks
 *
 * */
    public int setTotalKTwoOpt(int value) {
        totalKTwoOpt = value;

        return totalKTwoOpt;
    }

    /**
 * <p>Get the total number of k-2-opts for the trucks</p>
 * @return value total k-2-opts for the trucks
 *
 * */
    public int getTotalKTwoOpt() {
        return totalKTwoOpt;
    }

    /**
 * <p>After performing k-3-opt for trucks, set the number of moves done.</p>
 * @param value total k-3-opts for the trucks
 * @return value total k-3-opts for the trucks
 *
 * */
    public int setTotalKThreeOpt(int value) {
        totalKThreeOpt = value;

        return totalKThreeOpt;
    }

    /**
 * <p>Get the total number of k-3-opts for the trucks</p>
 * @return value total k-3-opts for the trucks
 *
 * */
    public int getTotalKThreeOpt() {
        return totalKThreeOpt;
    }

    /**
 * <p>After performing 01 exchanges for trucks, set the number of moves done.</p>
 * @param value total 01 exchanges for the trucks
 * @return value total 01 exchanges for the trucks
 *
 * */
    public int setTotal01(int value) {
        total01 = value;

        return total01;
    }

    /**
 * <p>Get the total number of 01 exchanges for the trucks</p>
 * @return value total 01 exchanges for the trucks
 *
 * */
    public int getTotal01() {
        return total01;
    }

    /**
 * <p>After performing 11 exchanges for trucks, set the number of moves done.</p>
 * @param value total 11 exchanges for the trucks
 * @return value total 11 exchanges for the trucks
 *
 * */
    public int setTotal11(int value) {
        total11 = value;

        return total11;
    }

    /**
 * <p>Get the total number of 11 exchanges for the trucks</p>
 * @return value total 11 exchanges for the trucks
 *
 * */
    public int getTotal11() {
        return total11;
    }

    /**
 * <p>After performing 02 exchanges for trucks, set the number of moves done.</p>
 * @param value total 02 exchanges for the trucks
 * @return value total 02 exchanges for the trucks
 *
 * */
    public int setTotal02(int value) {
        total02 = value;

        return total02;
    }

    /**
 * <p>Get the total number of 02 exchanges for the trucks</p>
 * @return value total 02 exchanges for the trucks
 *
 * */
    public int getTotal02() {
        return total02;
    }

    /**
 * <p>After performing 12 exchanges for trucks, set the number of moves done.</p>
 * @param value total 12 exchanges for the trucks
 * @return value total 12 exchanges for the trucks
 *
 * */
    public int setTotal12(int value) {
        total12 = value;

        return total12;
    }

    /**
 * <p>Get the total number of 12 exchanges for the trucks</p>
 * @return value total 12 exchanges for the trucks
 *
 * */
    public int getTotal12() {
        return total12;
    }

    /**
 * <p>After performing 22 exchanges for trucks, set the number of moves done.</p>
 * @param value total 22 exchanges for the trucks
 * @return value total 22 exchanges for the trucks
 *
 * */
    public int setTotal22(int value) {
        total22 = value;

        return total22;
    }

    /**
 * <p>Get the total number of 22 exchanges for the trucks</p>
 * @return value total 22 exchanges for the trucks
 *
 * */
    public int getTotal22() {
        return total22;
    }

    /**
 * <p>Set the total number of empty trucks to value</p>
 * @param value total number of non-empty trucks
 * @return value total number of non-empty trucks
 *
 * */
    public int setTotalNonEmptyOfTrucks(int value) {
        totalNonEmptyTrucks = value;

        return totalNonEmptyTrucks;
    }

    /**
 * <p>Get the total number of empty trucks</p>
 * @return value total number of non-empty trucks
 *
 * */
    public int getTotalNonEmptyTrucks() {
        //calculateTotalNonEmptyTrucks();
        return totalNonEmptyTrucks;
    }

    /**
 * <p>Increment the total capacity/weight of the truck serviced for the list when
 * a node is added to the list. When a new node is added to a truck, the total capacity
 * of the trucks needs to be incremented by the capacity of the added node.</p>
 * @param incWeight capacity/weight by which totalDemand is to be incremented
 * @return float total capacity/weight of the truck
 * */
    public float incTotalCapacity(float incWeight) {
        totalDemand = totalDemand + incWeight;

        return totalDemand;
    }

    /**
 * <p>Decrement the total capacity/weight of customers/shipments serviced for the list when
 * a node is added to the list. When a new node is deleted, the capacity of the trucks needs to
 * be decremented by the capacity of the deleted  node.</p>
 * @param decWeight capacity/weight by which totalDemand is to be decremented
 * @return float total capacity/weight of the truck
 * */
    public float decCurrentCapacity(float decWeight) {
        totalDemand = totalDemand - decWeight;

        return totalDemand;
    }

    /**
 * <p>Set the total demand for all trucks to value</p>
 * @param value total demand of all the trucks
 * @return value total demand of all trucks
 *
 * */
    public float setTotalDemandOfTrucks(float value) {
        totalDemand = value;

        return totalDemand;
    }

    /**
 * <p>Get the total demand for all trucks</p>
 * @return value total demand of all trucks
 *
 * */
    public float getTotalDemandOfTrucks() {
        //calculateTotalDemandOfTrucks();
        return totalDemand;
    }

    /**
 * <p>Set the total distance/duration for all trucks to value</p>
 * @param value value for the total distance
 * @return distance total demand of all trucks
 *
 * */
    public float setTotalDistance(float value) {
        totalDistance = value;

        return totalDistance;
    }

    /**
 * <p>Get the total distance for all trucks</p>
 * @return distance total demand of all trucks
 *
 * */
    public float getTotalDistanceOfTrucks() {
        return totalDistance;
    }

    /**
 * <p>Compute the total number of non-empty trucks in the list. The computed
 * put into a variable location using setTotalNonEmptyTrucks method.</p>
 *
 * */
    public void calculateTotalNonEmptyTrucks() {
        Truck currPtr = first;
        int noTrucks = 0;

        while (currPtr != null) {
            //if the list is not empty, increment no of trucks
            if (!currPtr.mainVisitNodes.ifVisitListEmpty()) {
                noTrucks++;
            }

            currPtr = currPtr.next;
        }

        //set the total number of non-empty trucks in the list
        setTotalNonEmptyOfTrucks(noTrucks);
    }

    /**
 * <p>calculate the total travel time for the depot</p>
 * @return float  total travel time for the depot
 */
    public float calcTotalTravelTime() {
        float travelTime = 0;
        Truck current = first;

        while (current != null) {
            travelTime += (float) ProblemInfo.truckLevelCostF.getTotalTravelTime(current);
            current = current.next;
        }

        return travelTime;
    }

    /**
 * <p>Compute the distance, demand and trucks required to solve the MDVRP
 * problem.</p>
 *
 * */
    public void calculateTotalStatsForMDVRP() {
        calculateTotalDistanceOfTrucks(); //total distance traveled
        calculateTotalDemandOfTrucks(); //total demand
        calculateTotalNonEmptyTrucks(); //total non-empty trucks
    }

    /**
 * <p>Compute the total distance traveled by all the trucks in the list</p>
 * altered method 10/10/03 to use cost function interface
 * Mike McNamara
 * */
    public void calculateTotalDistanceOfTrucks() {
        ProblemInfo.truckLLLevelCostF.setTotalDistance(this);
    }

    /**
 * <p>Compute the total demand for all the trucks in the list</p>
 * altered method 10/10/03 to use cost function interface
 * Mike McNamara
 * */
    public void calculateTotalDemandOfTrucks() {
        ProblemInfo.truckLLLevelCostF.setTotalDemand(this);
    }

    /**
 * <p>Check if the list is empty</p>
 * @return boolean true if empty, false otherwist
 *
 * */
    public boolean isEmpty() {
        return (first == null);
    }

    /**
 * <p>Perform the Exchange01 for all the trucks in the list</p>
 *
 * */
    public void exchangeOneDepot01() {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal01(0);

        while ((tempTruck != null) && (tempTruck.next != null)) { //loop through all the trucks
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        //while (!noChange && countLoop < maxLoop) {
        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange01(p, q);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot01 */

    /**
 * <p>Perform the Exchange10 for all the trucks in the list.
 * Exchange 10 is the same as 01 except that the order of truck routes p and q are switched</p>
 *
 * */
    public void exchangeOneDepot10() {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal01(0);

        while ((tempTruck != null) && (tempTruck.next != null)) //loop through all the trucks
         {
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange01(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        //while (!noChange && countLoop < maxLoop) {
        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange01(q, p);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot01 */

    /**
 * <p>Perform the Exchange11 for all the trucks in the list.</p>
 *
 * */
    public void exchangeOneDepot11() {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal11(0);

        while ((tempTruck != null) && (tempTruck.next != null)) //loop through all the trucks
         {
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange11(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange11(p, q);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot11() */

    /**
 * <p>Perform the Exchange12 for all the trucks in the list.</p>
 *
 * */
    public void exchangeOneDepot12() {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal12(0);

        while ((tempTruck != null) && (tempTruck.next != null)) //loop through all the trucks
         {
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange12(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //inner while loop
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //outer while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange12(p, q);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot12() */

    /**
 * <p>Perform the Exchange21 for all the trucks in the list.</p>
 *
 * */
    public void exchangeOneDepot21()
    //Exchange nodes between truck routes in one single depot
     {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal12(0);

        while ((tempTruck != null) && (tempTruck.next != null)) { //loop through all the trucks
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange12(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        //while (!noChange && countLoop < maxLoop) {
        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange12(q, p);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot21() */

    /**
 * <p>Perform the Exchange02 for all the trucks in the list.</p>
 *
 * */
    public void exchangeOneDepot02() {
        boolean isDiagnostic = false;
        boolean noChange = false; //No change took place
        boolean status = true;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal02(0);

        while ((tempTruck != null) && (tempTruck.next != null)) { //loop through all the trucks
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange02(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        //while (!noChange && countLoop < maxLoop) {
        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange02(p, q);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot02() */

    /**
 * <p>Perform the Exchange20 for all the trucks in the list.</p>
 *
 * */
    public void exchangeOneDepot20() {
        boolean isDiagnostic = false;
        boolean noChange = false; //No change took place
        boolean status = true;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal02(0);

        while ((tempTruck != null) && (tempTruck.next != null)) //loop through all the trucks
         {
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange02(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        //while (!noChange && countLoop < maxLoop) {
        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange02(q, p);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot02() */

    /**
 * <p>Perform the Exchange22 for all the trucks in the list.</p>
 *
 * */
    public void exchangeOneDepot22()
    //Exchange nodes between truck routes in one single depot
     {
        boolean isDiagnostic = false;
        boolean noChange = false; //No change took place
        boolean status = true;
        int truckNo;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal22(0);

        while ((tempTruck != null) && (tempTruck.next != null)) //loop through all the trucks
         {
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange22(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }

        //while loop
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        //while (!noChange && countLoop < maxLoop) {
        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            status = exchange22(p, q);

            if (noChange) { //check to see if any changes took place
                noChange = status;
            }

            calculateTotalStatsForMDVRP();
        }

        //if
    }

    /* exchangeOneDepot02() */

    /**
 * <p>Use local optimization for the trucks in the list. This method invokes 1-opt,
 * 2-opt, 3-opt, k-2-opt and k-3-opt.</p>
 *
 * */
    public void localOpt() {
        boolean isDiagnostic = false;
        boolean noChange = false; //No change took place
        boolean status = true;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;

        while (tempTruck != null) //loop through all the trucks
         {
            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                if (isDiagnostic) {
                    System.out.println("Executing Local 1-opt");
                }

                noChange = true;
                status = tempTruck.mainVisitNodes.localOneOpt();
                totalOneOpt += tempTruck.mainVisitNodes.getTotalOneOpt();

                //System.out.println("Total one-opt "+totalOneOpt);
                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                if (isDiagnostic) {
                    System.out.println("Executing Local 2-opt");
                }

                status = tempTruck.mainVisitNodes.localTwoOpt();
                totalTwoOpt += tempTruck.mainVisitNodes.getTotalTwoOpt();

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                if (isDiagnostic) {
                    System.out.println("Executing K Interchange 2-opt");
                }

                status = tempTruck.mainVisitNodes.kInterChange2();
                totalKTwoOpt += tempTruck.mainVisitNodes.getTotalKTwoOpt();

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                if (isDiagnostic) {
                    System.out.println("Executing K Interchange 3-opt");
                }

                status = tempTruck.mainVisitNodes.kInterChange3();
                totalKThreeOpt += tempTruck.mainVisitNodes.getTotalKThreeOpt();

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                if (isDiagnostic) {
                    System.out.println("Executing Local 3-opt");
                }

                status = tempTruck.mainVisitNodes.localThreeOpt(5);
                totalThreeOpt += tempTruck.mainVisitNodes.getTotalThreeOpt();

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                //System.out.println("No change took place "+noChange);
                countLoop++;
            }

            //tempTruck.mainVisitNodes.LocalThreeOpt(2);
            // tempTruck.mainVisitNodes.LocalThreeOpt(3);
            //tempTruck.mainVisitNodes.LocalThreeOpt(4);
            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            calculateTotalStatsForMDVRP();
        }
    }

    /* localOpt() */

    /**
 * <p>Use local optimization for the trucks in the list. This method invokes k-1-opt,
 * k-2-opt and k-3-opt.</p>
 * Added 10/22/03 by Mike McNamara
 * */
    public void localOptVRPTW() {
        boolean isDiagnostic = false;
        boolean noChange = false; //No change took place
        boolean status = true;
        int maxLoop = 1;
        int countLoop;
        Truck tempTruck = first;

        while (tempTruck != null) //loop through all the trucks
         {
            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                if (isDiagnostic) {
                    System.out.println("Executing K Interchange 1-opt");
                }

                noChange = true;
                status = tempTruck.mainVisitNodes.kInterChange1();
                totalKOneOpt += tempTruck.mainVisitNodes.getTotalKOneOpt();

                //System.out.println("Total one-opt "+totalOneOpt);
                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                if (isDiagnostic) {
                    System.out.println("Executing K Interchange 2-opt");
                }

                status = tempTruck.mainVisitNodes.kInterChange2();
                totalKTwoOpt += tempTruck.mainVisitNodes.getTotalKTwoOpt();

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                if (isDiagnostic) {
                    System.out.println("Executing K Interchange 3-opt");
                }

                status = tempTruck.mainVisitNodes.kInterChange3();
                totalKThreeOpt += tempTruck.mainVisitNodes.getTotalKThreeOpt();

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                //System.out.println("No change took place "+noChange);
                countLoop++;
            }

            tempTruck = tempTruck.next;

            //recompute the cost after the local optimization for each truck
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }
    }

    /**
 * <p>Insert a truck into the list. An instace of a truck is created using the truck number,
 * depot number, starting and ending coordinates, the maximum duration or travel time and
 * the maximum capacity or weight. This information is kept at the head node of the linked list
 * so that the shipments or customers being added to the truck can reference this information. The
 * shipments and customers are added to the truck using the mainVisitNodes instance from the
 * VisitNodesLinkedList class.</p>
 * @param depotNo id for the current depot
 * @param startX starting x coordinate of the truck
 * @param startY starting y coordinate of the truck
 * @param endX ending x coordinate of the truck
 * @param endY ending y coordinate of the truck
 * @param maxTravelTime maximum travel time
 * @param maxCapacity maximum capacity
 *
 * */
    public void insertTruck(int depotNo, float startX, float startY,
        float endX, float endY, float maxTravelTime, float maxCapacity) {
        boolean isDiagnostic = false;
        int truckNum = getNoTrucks(); //get total number of trucks in list

        //create an instance of the truck
        Truck thisTruck = new Truck(truckNum, depotNo, startX, startY, endX,
                endY, maxTravelTime, maxCapacity);

        //add the instance to the linked list - in this case it iis added at the end of the list
        insertLast(thisTruck);

        //increase the total number of trucks inserted into the list
        truckNum++; //increment it by one
        setNoTrucks(truckNum);

        if (isDiagnostic) {
            System.out.println("Truck number inserted is " + truckNum);
        }
    }

    /**
 * <p>Insert truck into the first location of the linked list</p>
 * @param thisTruck truck to be inserted
 * @return Truck pointer to the inserted truck
 *
 * */
    public Truck insertFirst(Truck thisTruck) {
        boolean isDiagnostic = false;
        Truck theTruck = thisTruck; //copy attributes of current truck

        if (isEmpty()) { //if empty list
            last = theTruck; //theTruck-> last
        } else {
            first.prev = theTruck; //theTruck <- old first
        }

        theTruck.next = first; //theTruck -> old first
        first = theTruck; //first -> theTruck
        noTrucks++; //increment number of trucks

        //Diagnostic
        if (isDiagnostic) {
            System.out.println("inserted " + theTruck.getTruckNo());
        }

        return first; //return the pointer to the added node
    }

    /**
 * <p>Insert truck into the last location of the linked list</p>
 * @param thisTruck truck to be inserted
 * @return Truck pointer to the inserted truck
 *
 * */
    public Truck insertLast(Truck thisTruck) {
        boolean isDiagnostic = false;
        Truck theTruck = thisTruck;

        if (isEmpty()) {
            first = theTruck; //first -> theShip
        } else {
            last.next = thisTruck; //old last -> theShip
            theTruck.prev = last; //old last <- theShip
        }

        last = thisTruck; //theShip <- last
        noTrucks++; //increment number of shipments

        //Diagnostic
        if (isDiagnostic) {
            System.out.println("inserted " + theTruck.getTruckNo());
        }

        return last; //return the pointer to the added node
    }

    /**
 * <p>Delete the first truck in the lined list</p>
 * @return Truck pointer to the deleted truck
 *
 * */
    public Truck deleteFirst() {
        boolean isDiagnostic = false;
        Truck temp = first;

        //check for empty list
        if (isEmpty()) {
            return null; //return null, if empty
        }

        if (first.next == null) { //if only one item in linked list
            last = null; //null <- last
        } else {
            first.next.prev = null; //null <- old next
        }

        first = first.next; //first -> old next
        temp.next = null; //ground pointer
        noTrucks--; //decrement number of shipments

        if (isDiagnostic) {
            System.out.println("deleted " + temp.getTruckNo());
        }

        return temp; //return deleted node
    }

    //end deleteFirst()

    /**
 * <p>Delete the lst truck in the lined list</p>
 * @return Truck pointer to the deleted truck
 *
 * */
    public Truck deleteLast() {
        boolean isDiagnostic = false;
        Truck temp = last;

        //check for empty list
        if (isEmpty()) {
            return null; //if empty, return null
        }

        if (first.next == null) { //if only one item in linked list
            first = null; //null <- first
        } else {
            last.prev.next = null; //old prev -> null
        }

        last = last.prev; //old prev -> last
        temp.next = null; //ground pointer
        noTrucks--; //decrement number of shipments

        if (isDiagnostic) {
            System.out.println("deleted " + temp.getTruckNo());
        }

        return temp; //return deleted node
    }

    /**
 * <p>Find the truck with id key</p>
 * @param key id of the truck to be located
 * @return Truck pointer to the located truck, null if not located
 *
 * */
    public Truck find(int key) {
        Truck current = first;

        while (current != null) {
            if (current.truckNo == key) {
                break;
            }

            current = current.next;
        }

        return current;
    }

    //end find()

    /**
 * <p>Finds truck based on index from first element in linked list</p>
 * @param index truck to find
 * @return pointer to truck found
 */
    public Truck findByTraversal(int index) {
        Truck temp = this.getFirst();

        for (int i = 0; i < index; i++) {
            if (temp.next == null) {
                return null;
            } else {
                temp = temp.next;
            }
        }

        return temp;
    }

    /**
 * <p>Delete the truck with id key</p>
 * @param key id of the truck to be deleted
 * @return Truck pointer to the deleted truck
 *
 * */
    public Truck delete(int key) {
        Truck current = first;
        Truck previous = null;

        while (current != null) {
            if (current.truckNo == key) {
                break;
            }

            current = current.next;
        }

        if ((current == null) || (current.truckNo != key)) {
            return null;
        }

        previous = current.prev;

        if (current == first) {
            first = first.next;

            if (first != null) {
                first.prev = null;
            } else {
                last = null;
            }
        } else if (current == last) {
            previous.next = null;
            last = previous;
        } else {
            previous.next = current.next;
            current.next.prev = previous;
        }

        current.next = null;
        current.prev = null;
        noTrucks--;

        return current;
    }

    //end delete()

    /**
 * <p>Insert a truck after the truck node with key value</p>
 * @param key is the unique address of the truck
 * @param thisTruck truck to be inserted
 * @return thisTruck Pointer to the inserted truck, null if shipment was not found
 */
    public boolean insertAfter(int key, Truck thisTruck) {
        boolean isDiagnostic = false;
        Truck current = first;

        while (current.truckNo != key) //until match is found
         {
            current = current.next; //move to next node

            if (current == null) { //no more nodes

                return false; //return false;
            }
        }

        //key was found
        Truck newTruck = thisTruck;

        if (current == last) //if last link
         {
            newTruck.next = null; //newTruck -> null
            last = newTruck; //newTruck -> last
        } else //not last link
         {
            newTruck.next = current.next; //newTruck -> old next
            current.next.prev = newTruck;
        }

        newTruck.prev = current; //old current <- newTruck
        current.next = newTruck; //old current -> newTruck

        noTrucks++; //increment number of trucks

        if (isDiagnostic) {
            System.out.println("inserted " + thisTruck.getTruckNo() + " after" +
                key);
        }

        return true; //insertion successful
    }

    /**
 * <p>Display the information on the trucks to the console. This method uses the displayTruck
 * method in the Truck class to display all the information on the truck.</p>
 */
    public void displayForwardList() {
        System.out.print("List (first to last): ");

        Truck current = first;

        while (current != null) {
            current.displayTruck();
            current = current.next;
        }

        System.out.println("");
    }

    /**
 * <p>Display the information on the trucks to the console. This method uses the displayTruck
 * method in the Truck class to display all the information on the truck. In additon, it uses
 * the displayForwardList method to display other information the trucks.</p>
 */
    public void displayAllForwardList() {
        //System.out.print("     List (first to last): ");
        Truck current = first;

        while (current != null) {
            current.displayTruck();
            current.mainVisitNodes.displayForwardList();
            current = current.next;
        }

        System.out.println("");
    }

    /**
 * <p>Display information for the trucks in the MDVRP problem using the
 * displayForwardKeyListMDVRP method in the VisitingNodesLinkedList class. This
 * displays specialised information for the MDVRP type of shipments.</p>
 */
    public void displayForwardKeyListMDVRP() {
        //System.out.print("     List (first to last): ");
        Truck current = first;

        while (current != null) {
            System.out.print(current.truckNo + " ");
            current.mainVisitNodes.displayForwardKeyListMDVRP();
            current = current.next;
        }

        System.out.println("");
    }

    /**
 * <p>Display information for the trucks in the VRP problem using the
 * displayForwardKeyListVRP method in the VisitingNodesLinkedList class. This
 * displays specialised information for the VRP type of shipments.</p>
 * This method added 8/30/03 by Mike McNamara
 */
    public void displayForwardKeyListVRP() {
        Truck current = first;

        while (current != null) {
            System.out.println();
            System.out.println("Route/Truck No.: " + current.truckNo + " ");
            current.mainVisitNodes.displayForwardKeyListVRP();
            current = current.next;
        }

        System.out.println("");
    }

    /**
 * <p>Display the information on the trucks to the console from the last truck to the first
 * truck in the list. This method uses the displayTruck method in the Truck class to display
 * all the information on the truck. In additon, it uses
 * the displayForwardList method to display other information the trucks.</p>
 */
    public void displayBackwardList() {
        System.out.print("List (last to first): ");

        Truck current = last;

        while (current != null) {
            current.displayTruck();
            current = current.prev;
        }

        System.out.println("");
    }

    /**
 * <p>Display the information on the trucks to the console from the first truck to the last
 * truck in the list.</p>
 */
    public void displayForwardKeyList() {
        System.out.println("List (first to last): ");

        Truck current = first;
        int j = 0; //count no. of values printed per line

        while (current != null) {
            System.out.print(current.truckNo);
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
 * <p>Display the information on the trucks to the console from the first truck to the last
 * truck in the list.</p>
 */
    public void displayBackwardKeyList() {
        System.out.println("List (last to first): ");

        Truck current = last;
        int j = 0; //count no. of values printed per line

        while (current != null) {
            System.out.print(current.truckNo);
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
 * <p>Write the short form of information for all all trucks in the depot including
 * the depot number to which the trucks are linked to a file.</p>
 * @param depotNo depot number to which the list is linked
 * @param solOutFile output file name
 *
 * */
    public void writeShortTrucksSol(int depotNo, PrintWriter solOutFile) {
        Truck current = first;

        while (current != null) {
            current.writeShortTruck(depotNo, solOutFile);
            current.mainVisitNodes.writeShortNodesSol(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
 * <p>Write the short form of information for all all trucks in the depot including
 * the depot number to which the trucks are linked to a file.</p>
 * @param depotNo depot number to which the list is linked
 * @param solOutFile output file name
 *
 * */
    public void writeVRPShortTrucksSol(int depotNo, PrintWriter solOutFile) {
        Truck current = first;

        while (current != null) {
            current.writeVRPShortTruck(depotNo, solOutFile);
            current.mainVisitNodes.writeShortNodesSol(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
 * <p>Write the detail form of information for all all trucks to a file.</p>
 * @param solOutFile output file name
 *
 * */
    public void writeDetailTrucksSol(PrintWriter solOutFile) {
        //System.out.print("     List (first to last): ");
        Truck current = first;

        while (current != null) {
            current.writeVRPTWDetailTruck(solOutFile);
            current.mainVisitNodes.writeDetailNodesSol(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
 * <p>Write the detail form of information for all all trucks to a file.</p>
 * This method added 8/30/03 by Mike McNamara
 * @param solOutFile output file name
 * */
    public void writeVRPDetailTrucksSol(PrintWriter solOutFile) {
        Truck current = first;

        while (current != null) {
            current.writeVRPDetailTruck(solOutFile);
            current.mainVisitNodes.writeDetailNodesSol(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
 * <p>Display on the console the unique id's of all the shipments associated with
 * the trucks in the list.</p>
 *
 * */
    public void printShipNos() {
        int noCount = 0;
        Truck current = first;

        while (current != null) {
            System.out.print(current.getTruckNo() + " ");
            noCount++;

            if ((noCount % 10) == 0) {
                System.out.println("");
            }

            current = current.next;
        }

        System.out.println();
        System.out.println();
    }

    /**
 * <p>insert a shipment into the truck linked list.  In the case of the MDVRP
 * any number of trucks can be made available from the depot.  Therefore it
 * is the responsibility of the TruckLinkedList to add the shipment into the
 * linked list.  If there is no trucks, create a truck and insert it in.  If
 * a constraint in the current truck is broken, create another truck and add
 * the shipment into the truck.</p>
 * @param thisShip shipment to be inserted
 * @return boolean truck if inserted, otherwise false
 *  */
    public boolean insertShipMDVRP(Shipment thisShip) {
        Truck current = first;
        boolean status;
        int insertStatus; //if -1, no insertion

        status = false;

        if (current == null) //list has no trucks
         {
            System.out.println("Error: No trucks found in truck linked list");

            return status;
        }

        //insert the shipment into the mainVisitNode linkedlist for the current truck
        //The current truck starts with the first truck.  If the shipment cannot be inserted into the
        //first truck then the next truck in the linked list is tried and the next
        //If there are no more trucks in the linked list to which the shipment can be added, then
        //a status of false is returned so that a new truck can be entered into the linked list
        //and the shipment be inserted into that.  It is a bit inefficient as the new truck that is
        //created will not be accessed until the shipment goes through the insertion procedure for
        //all the trucks before it. The advantage being that when a shipment is to be added all the
        //trucks are tried and not just the new one.
        while (current != null) //if current hits a null, then status is false and a new truck is needed
         {
            insertStatus = current.mainVisitNodes.insertMDVRP(thisShip.shipNo,
                    thisShip.vertexX, thisShip.vertexY, thisShip.demand);

            //load the current demand and capacities of the shipments into the Truck node
            current.currentDemand = current.mainVisitNodes.getCurrentCapacity();
            current.currentDistance = current.mainVisitNodes.getCurrentDistance();

            if (insertStatus == -1) { //shipment could not be inserted
                current = current.next;
                status = false;
            } else { //shipment was inserted ans status is true
                thisShip.setTruckNo(current.getTruckNo());
                current = null;
                status = true;
            }
        }

        //calculate the number of non-empty trucks, distance and capacity on the link list
        calculateTotalStatsForMDVRP();

        return status;
    }

    /**
 * <p>TruckLinkedList level insert function for the VRP</p>
 * @param theShip shipment to be inserted
 * @return whether it was inserted or not
 */
    public boolean insertShipVRPTW(Shipment theShip) {
        Truck tempTruck;
        boolean status = false;

        //get first truck in linked list
        tempTruck = this.getFirst();

        //loop through all the trucks
        while (tempTruck != null) {
            //attempt to insert
            status = tempTruck.mainVisitNodes.insertVRPTW(theShip.getShipNo(),
                    theShip.getX(), theShip.getY(), theShip.getDemand(),
                    theShip.getEarliestTime(), theShip.getLatestTime(),
                    theShip.getServeTime());

            if (status == false) {
                // shipment could not be inserted
                tempTruck = tempTruck.next;
            } else {
                //shipment was inserted
                theShip.setTruckNo(tempTruck.getTruckNo());
                tempTruck = null;
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
            }
        }

        return status;
    }

    /**
 * <p>Find a chain of m nodes in list p and swap with n nodes in list q.  This
 * should reduce the number of route crossovers which will reduce the total
 * distance for each route.  Swaps will only be performed if the constraints
 * are upheld.</p>
 * method added 11/03/03 by Mike McNamara
 * @param m  number of nodes to swap
 * @param n  number of nodes to swap
 * @return boolean  true if swap is successful
 */
    public boolean swapMNchains(int m, int n) {
        boolean isDiagnostic = false;
        boolean status;
        boolean continueSwap = true;
        VisitNodesLinkedList p = null;
        VisitNodesLinkedList q = null;
        VisitNodesLinkedList[] pQ = new VisitNodesLinkedList[2];
        PointCell ptr1 = null; //pointers used in
                               //the swap

        PointCell ptr2 = null; //pointers used in
                               //the swap

        PointCell ptr3 = null; //pointers used in
                               //the swap

        PointCell ptr4 = null; //pointers used in
                               //the swap

        int segHeads = 1; // type of closeness calculation dealing with the heads of the swap segments
        int listSeg = 2; // type of closeness calculation dealing with the swap segment total cost before the swap
        int caseNum = 3; // case of swap, case 0: m = 0, case 1: n = 0, case 2: m = n = 0, case 3: all others

        if (getNoTrucks() < 2) { // not enough trucks to swap with

            return false;
        }

        pQ = cyclicPQchooser(); // calculate the lists to be swapped

        while ((pQ[0] != null) && (pQ[1] != null)) {
            p = pQ[0];
            q = pQ[1];

            ProblemInfo.vNodesLevelCostF.setTotalCost(p);
            ProblemInfo.vNodesLevelCostF.setTotalCost(q);

            double preCost = ProblemInfo.vNodesLevelCostF.getTotalCost(p) +
                ProblemInfo.vNodesLevelCostF.getTotalCost(q);

            if (isDiagnostic) {
                System.out.println("Forward Key List Before");
                p.displayForwardKeyList();
                q.displayForwardKeyList();
                System.out.println("");
            }

            ptr1 = p.first().next; //set pointer ptr1 to the first node of p
            ptr2 = q.first().next; //set pointer ptr2 to the first node of q

            continueSwap = true;

            while (continueSwap) {
                if ((ptr1 == null) || (ptr1.next == null) ||
                        (ptr1.next.index == -1)) { //if the next node is null, there are not enough customers

                    //in the route to perform the swap with m
                    if (isDiagnostic) {
                        System.out.println("Cannot swap " + m +
                            " nodes from route p.  Route p does not have " + m +
                            " nodes.");
                    }

                    continueSwap = false;
                }

                //end if
                if ((ptr2 == null) || (ptr2.next == null) ||
                        (ptr2.next.index == -1)) { //if the next node is null, htere are not enough customers

                    //in the route to perform the swap with n
                    if (isDiagnostic) {
                        System.out.println("Cannot swap " + n +
                            " nodes from route q.  Route q does not have " + n +
                            " nodes.");
                    }

                    continueSwap = false;
                }

                if (!continueSwap) {
                    break;
                }

                //end if
                if (ProblemInfo.truckLLLevelCostF.calcCloseness(this, ptr1,
                            ptr2, segHeads)) {
                    ptr3 = ptr1;
                    ptr4 = ptr2;

                    if (m > 1) {
                        //loop through m times to determine how large the list segment in p is
                        for (int i = 1; i < m; i++) {
                            if ((ptr3.next == null) || (ptr3.next.index == -1)) { //if the next node is null, there are not enough customers

                                //in the route to perform the swap with m
                                if (isDiagnostic) {
                                    System.out.println("Cannot swap " + m +
                                        " nodes from route p.  Route p does not have " +
                                        m + " nodes left to swap.");
                                }

                                continueSwap = false;

                                break;
                            }

                            //end if
                            ptr3 = ptr3.next; //cycle through the nodes of p
                        }

                        //end for
                    }

                    if (n > 1) {
                        //loop through n times to determine how large the list segment in q is
                        for (int j = 1; j < n; j++) {
                            if ((ptr4.next == null) || (ptr4.next.index == -1)) { //if the next node is null, there are not enough customers

                                //in the route to perform the swap with n
                                if (isDiagnostic) {
                                    System.out.println("Cannot swap " + n +
                                        " nodes from route q.  Route q does not have " +
                                        n + " nodes left to swap.");
                                }

                                continueSwap = false;

                                break;
                            }

                            //end if
                            ptr4 = ptr4.next; // cycle through the nodes of q
                        }

                        //end for
                    }

                    if (!continueSwap) {
                        break;
                    }

                    if ((m == 0) && (n > 0)) {
                        caseNum = 0;
                    } else if ((m > 0) && (n == 0)) {
                        caseNum = 1;
                    } else if ((m == 0) && (n == 0)) {
                        caseNum = 2;
                    } else {
                        caseNum = 3;
                    }

                    switch (caseNum) {
                    case 0: { // swap zero nodes with n nodes

                        PointCell a = ptr2.prev;
                        PointCell x = ptr3.next;
                        PointCell y = ptr4.next;

                        // perform swap
                        ptr1.next = ptr2;
                        ptr2.prev = ptr1;
                        ptr4.next = x;
                        x.prev = ptr4;
                        a.next = y;
                        y.prev = a;

                        // update values form p and q lists
                        p.performRecalculates();
                        q.performRecalculates();
                        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(p);
                        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(q);

                        /**
 * calls method to calculate the cost of the swap
 * and whether or not the swap can be done.  A
 * boolean, status, is returned. A true value
 * means the swap was feasible, a false means
 * not feasible.
 */
                        status = calcSwapMNChain(p, q);

                        ProblemInfo.vNodesLevelCostF.setTotalCost(p);
                        ProblemInfo.vNodesLevelCostF.setTotalCost(q);

                        double postCost = ProblemInfo.vNodesLevelCostF.getTotalCost(p) +
                            ProblemInfo.vNodesLevelCostF.getTotalCost(q);

                        if ((status == true) &&
                                ((postCost < preCost) ||
                                (q.ifVisitListEmpty() &&
                                (ProblemInfo.vNodesLevelCostF.getTotalDistance(
                                    q) < ProblemInfo.maxDistance)))) {
                            if (isDiagnostic) {
                                System.out.println(
                                    "Swap successfully performed.");
                                System.out.println("");
                                System.out.println("Forward Key list after");
                                p.displayForwardKeyList();
                                q.displayForwardKeyList();
                            }

                            // adjust the size of the lists
                            p.offsetSize(n - m); // list p will gain n nodes while giving m nodes away
                            q.offsetSize(m - n); // list q will gain m nodes while giving n nodes away

                            if (q.getSize() < 3) {
                                delete(q.truckNo); // delete the empty truck
                                updateTruckNo(); // update their numbers
                                calculateTotalNonEmptyTrucks(); // recalculate number of trucks
                                System.out.println("Truck Reduced!!");
                                continueSwap = false;

                                break; // stop this iteration of the loop, move on to next
                            }

                            /** after a swap, ptr2 is now in the opposite list,
* this section moves ptr2 back into its original list
* to continue iterating
*/
                            ptr2 = a.next;
                        } else {
                            // remove swap
                            ptr1.next = x;
                            x.prev = ptr1;
                            a.next = ptr2;
                            ptr2.prev = a;
                            ptr4.next = y;
                            y.prev = ptr4;

                            // update values form p and q lists
                            p.performRecalculates();
                            q.performRecalculates();
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(p);
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(q);

                            /**
* move only one pointer down list, since the pointers seek to
* be close in order to perform a swap, the other will follow
*/
                            ptr1 = ptr1.next;
                        }
                    }

                    break;

                    case 1: { // swap m nodes with zero nodes

                        PointCell a = ptr1.prev;
                        PointCell x = ptr3.next;
                        PointCell y = ptr4.next;

                        // perform swap
                        a.next = x;
                        x.prev = a;
                        ptr2.next = ptr1;
                        ptr1.prev = ptr2;
                        ptr3.next = y;
                        y.prev = ptr3;

                        // update values form p and q lists
                        p.performRecalculates();
                        q.performRecalculates();
                        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(p);
                        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(q);

                        /**
 * calls method to calculate the cost of the swap
 * and whether or not the swap can be done.  A
 * boolean, status, is returned. A true value
 * means the swap was feasible, a false means
 * not feasible.
 */
                        status = calcSwapMNChain(p, q);

                        ProblemInfo.vNodesLevelCostF.setTotalCost(p);
                        ProblemInfo.vNodesLevelCostF.setTotalCost(q);

                        double postCost = ProblemInfo.vNodesLevelCostF.getTotalCost(p) +
                            ProblemInfo.vNodesLevelCostF.getTotalCost(q);

                        if ((status == true) &&
                                ((postCost < preCost) ||
                                (p.ifVisitListEmpty() &&
                                (ProblemInfo.vNodesLevelCostF.getTotalDistance(
                                    p) < ProblemInfo.maxDistance)))) {
                            if (isDiagnostic) {
                                System.out.println(
                                    "Swap successfully performed.");
                                System.out.println("");
                                System.out.println("Forward Key list after");
                                p.displayForwardKeyList();
                                q.displayForwardKeyList();
                            }

                            // adjust the size of the lists
                            p.offsetSize(n - m); // list p will gain n nodes while giving m nodes away
                            q.offsetSize(m - n); // list q will gain m nodes while giving n nodes away

                            if (p.getSize() < 3) {
                                delete(p.truckNo); // delete the empty truck
                                updateTruckNo(); // update their numbers
                                calculateTotalNonEmptyTrucks(); // recalculate number of trucks
                                System.out.println("Truck Reduced!!");

                                break; // stop this iteration of the loop, move on to next
                            }

                            /** after a swap, ptr1 are now in the opposite list,
* this section moves ptr1 back into its original list
* to continue iterating
*/
                            ptr1 = a.next;
                        } else {
                            // remove swap
                            a.next = ptr1;
                            ptr1.prev = a;
                            ptr3.next = x;
                            x.prev = ptr3;
                            ptr2.next = y;
                            y.prev = ptr2;

                            // update values form p and q lists
                            p.performRecalculates();
                            q.performRecalculates();
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(p);
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(q);

                            /**
* move only one pointer down list, since the pointers seek to
* be close in order to perform a swap, the other will follow
*/
                            ptr1 = ptr1.next;
                        }
                    }

                    break;

                    case 2: // swap zero nodes with zero nodes
                        return true; // if both lists will swap zero nodes with each other, then the
                                     // swap is successful by default

                    default: // swap m nodes with n nodes

                        // this checks to see if the list segments can fit in each others space
                        if (ProblemInfo.truckLLLevelCostF.calcCloseness(this,
                                    ptr3, ptr4, listSeg)) {
                            PointCell a = ptr1.prev;
                            PointCell b = ptr2.prev;
                            PointCell x = ptr3.next;
                            PointCell y = ptr4.next;

                            // perform swap
                            a.next = ptr2;
                            ptr2.prev = a;
                            b.next = ptr1;
                            ptr1.prev = b;
                            ptr3.next = y;
                            y.prev = ptr3;
                            ptr4.next = x;
                            x.prev = ptr4;

                            // update values form p and q lists
                            p.performRecalculates();
                            q.performRecalculates();
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(p);
                            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(q);

                            /**
 * calls method to calculate the cost of the swap
 * and whether or not the swap can be done.  A
 * boolean, status, is returned. A true value
 * means the swap was feasible, a false means
 * not feasible.
 */
                            status = calcSwapMNChain(p, q);

                            ProblemInfo.vNodesLevelCostF.setTotalCost(p);
                            ProblemInfo.vNodesLevelCostF.setTotalCost(q);

                            double postCost = ProblemInfo.vNodesLevelCostF.getTotalCost(p) +
                                ProblemInfo.vNodesLevelCostF.getTotalCost(q);

                            if ((status == true) && (postCost < preCost)) {
                                if (isDiagnostic) {
                                    System.out.println(
                                        "Swap successfully performed.");
                                    System.out.println("");
                                    System.out.println("Forward Key list after");
                                    p.displayForwardKeyList();
                                    q.displayForwardKeyList();
                                }

                                // adjust the size of the lists
                                p.offsetSize(n - m); // list p will gain n nodes while giving m nodes away
                                q.offsetSize(m - n); // list q will gain m nodes while giving n nodes away

                                /** after a swap, the ptr's are now in the opposite lists,
* this section moves the ptr's back into their original lists
* to continue iterating
*/
                                PointCell temp = ptr1;
                                ptr1 = ptr2;
                                ptr2 = temp;
                            } else {
                                // remove swap
                                a.next = ptr1;
                                ptr1.prev = a;
                                b.next = ptr2;
                                ptr2.prev = b;
                                ptr3.next = x;
                                x.prev = ptr3;
                                ptr4.next = y;
                                y.prev = ptr4;

                                // update values form p and q lists
                                p.performRecalculates();
                                q.performRecalculates();
                                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(p);
                                ProblemInfo.vNodesLevelCostF.calculateTotalsStats(q);

                                /**
* move only one pointer down list, since the pointers seek to
* be close in order to perform a swap, the other will follow
*/
                                ptr1 = ptr1.next;
                            }
                        } else { // ptr3 and ptr4 are not close

                            /**
 * move only one pointer down list, since the pointers seek to
 * be close in order to perform a swap, the other will follow
 */
                            ptr1 = ptr1.next;
                        }

                        break;
                    }
                } else { // ptr1 and ptr2 are not close

                    if (ptr1.lt > ptr2.lt) {
                        while ((ptr2 != null) && (ptr2.lt <= ptr1.lt)) {
                            if (!ProblemInfo.truckLLLevelCostF.calcCloseness(
                                        this, ptr1, ptr2, segHeads)) {
                                ptr2 = ptr2.next;
                            } else {
                                break;
                            }
                        }
                    } else { // ptr1.lt < ptr2.lt

                        while ((ptr1 != null) && (ptr1.lt <= ptr2.lt)) {
                            if (!ProblemInfo.truckLLLevelCostF.calcCloseness(
                                        this, ptr1, ptr2, segHeads)) {
                                ptr1 = ptr1.next;
                                ptr2 = q.first().next; //set pointer ptr2 to the first node of q
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            // end while (continueSwap)
            pQ = cyclicPQchooser(); // calculate the lists to be swapped
        }

        // end while (pQ != null)
        return true;
    }

    /**
 * <p>calculates how closely related two nodes are from different lists</p>
 * method added 11/15/03 by Mike McNamara
 * @param p  a node from a list
 * @param q  a node from a different list that is to be compared to p
 * @param distWeight  percentage that distance will determine the nodes closeness
 * @param timeWeight  percentage that time will determine the nodes closeness
 * @param range  range that determines how close the nodes should be
 * @return boolean  true if nodes are close
 */
    public boolean close(PointCell p, PointCell q, double distWeight,
        double timeWeight, double range) {
        double x = 0;
        x = (dist(p.xCoord, q.xCoord, p.yCoord, q.yCoord) * distWeight) + // distance
            (Math.abs(p.lt - q.lt) * timeWeight); // time

        if (x < range) {
            return true;
        }

        //else
        return false;
    }

    /**
 * <p>checks for any violation of constraints after a swap of m nodes from one list
 * with n nodes from another</p>
 * added and modified 11/5/03 by Mike McNamara, originally written by Jennifer Davis and Ethan Fry
 * @param p a list of nodes that recieved n nodes from q and gave m nodes to q
 * @param q a list of nodes that recieved m nodes from p and gave n nodes to p
 * @return boolean true if no constraints are broken
 */
    public boolean calcSwapMNChain(VisitNodesLinkedList p,
        VisitNodesLinkedList q) {
        boolean isDiagnostic = false;

        if ((ProblemInfo.vNodesLevelCostF.getTotalDemand(p) * (1 -
                ProblemInfo.fi)) > p.maxCapacity) {
            if (isDiagnostic) {
                System.out.println(
                    "Capacity in first route exceeded.  Swap not executed.");
            }

            return false;
        } else if (ProblemInfo.vNodesLevelCostF.getTotalDistance(p) > p.maxTime) {
            if (isDiagnostic) {
                System.out.println(
                    "Travel Time in first route exceeded.  Swap not executed.");
            }

            return false;
        } else if ((ProblemInfo.vNodesLevelCostF.getTotalDemand(q) * (1 -
                ProblemInfo.fi)) > q.maxCapacity) {
            if (isDiagnostic) {
                System.out.println(
                    "Capacity in second route exceeded.  Swap not executed.");
            }

            return false;
        } else if (ProblemInfo.vNodesLevelCostF.getTotalDistance(q) > q.maxTime) {
            if (isDiagnostic) {
                System.out.println(
                    "Travel Time in Second route exceeded.  Swap not executed.");
            }

            return false;
        } else if (ProblemInfo.vNodesLevelCostF.getTotalExcessTime(p) > (ProblemInfo.vNodesLevelCostF.getTotalDistance(
                    p) * (1 - ProblemInfo.beta))) {
            if (isDiagnostic) {
                System.out.println(
                    "Excess Time in First route exists.  Swap not executed.");
            }

            return false;
        } else if (ProblemInfo.vNodesLevelCostF.getTotalExcessTime(q) > (ProblemInfo.vNodesLevelCostF.getTotalDistance(
                    q) * (1 - ProblemInfo.beta))) {
            if (isDiagnostic) {
                System.out.println(
                    "Excess Time in Second route exists.  Swap not executed.");
            }

            return false;
        } else if (ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(p) > (ProblemInfo.vNodesLevelCostF.getTotalDistance(
                    p) * (1 - ProblemInfo.mu))) {
            if (isDiagnostic) {
                System.out.println(
                    "Tardiness Time in First route exists.  Swap not executed.");
            }

            return false;
        } else if (ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(q) > (ProblemInfo.vNodesLevelCostF.getTotalDistance(
                    q) * (1 - ProblemInfo.mu))) {
            if (isDiagnostic) {
                System.out.println(
                    "Tardiness Time in Second route exists.  Swap not executed.");
            }

            return false;
        } else {
            return true;
        }
    }

    /**
 * <p>Iterates through the list of trucks in a cyclic manner to find two lists
 * of customers to be swapped.  The list of one truck will be the first list
 * and will have m nodes from it swapped with n nodes from the list of the second truck
 * (secondList)</p>
 * method added 11/15/03 by Mike McNamara
 * @return VisitNodesLinkedList[]  array of VisitNodesLinkedList types
 */
    public VisitNodesLinkedList[] cyclicPQchooser() {
        VisitNodesLinkedList[] pQ = new VisitNodesLinkedList[2];

        if ((truckSwap[0] == -1) || (truckSwap[1] == -1)) {
            /**
 * there will be four cycles of exchanges, the first is between a
 * truck and the next truck, the second will be the same trucks
 * swapped, third is the first truck and the truck after next, then
 * the fourth is those same trucks swapped.  Multiply this with the
 * number of trucks, minus this current cycle, and this will be the
 * total number of remaining cycles the cyclicPQchooser must perform.
 */
            cycle = (getNoTrucks() * 4) - 1;
            swap = true;
            truckSwap[0] = getFirst().truckNo;
            truckSwap[1] = getFirst().next.truckNo;
            pQ[0] = getFirst().mainVisitNodes;
            pQ[1] = getFirst().next.mainVisitNodes;
        } else if ((cycle > 0) && swap) {
            cycle--;
            swap = false;

            int temp = truckSwap[0];
            truckSwap[0] = truckSwap[1];
            truckSwap[1] = temp;

            if ((find(truckSwap[0]) != null) || (find(truckSwap[1]) != null)) {
                pQ[0] = find(truckSwap[0]).mainVisitNodes;
                pQ[1] = find(truckSwap[1]).mainVisitNodes;
            } else {
                pQ[0] = null;
                pQ[1] = null;
            }
        } else if ((cycle > 0) && !swap) {
            cycle--;
            swap = true;
            truckSwap[0] = (truckSwap[0] + 1) % getNoTrucks();

            if ((find(truckSwap[0]) != null) || (find(truckSwap[1]) != null)) {
                pQ[0] = find(truckSwap[0]).mainVisitNodes;
                pQ[1] = find(truckSwap[1]).mainVisitNodes;
            } else {
                pQ[0] = null;
                pQ[1] = null;
            }
        } else { // reinitialize the values for any subsequent swapMN exchanges
            truckSwap[0] = truckSwap[1] = -1;
            pQ[0] = pQ[1] = null;
        }

        return pQ;
    }

    /**
 * <pre>                2C1T, 2 cycles, 2 transfer exchange
 *
 * ________First Iteration____________|_____________Second Iteration_______________
 *             2 customers            |
 *        R1(p) ------------> R2(q)   |   R1             R2(p)
 *               then back            |                    *  2 customers
 *                                    |                    *   then back
 *        R4                  R3      |   R4             R3(q)
 * _______________________________________________________________________________
 * This is just of the first, and second times.  It keeps exchanging between
 * two customers.  This would happen in a complete circle. Exchanges 2 customers
 * with 2 routes. </pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclic2C2T(int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;

        //int maxLoop = 4;                        //declared and passed from MDVRPTW
        int countLoop;
        int counter = 0;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal01(0);

        while ((tempTruck != null) && (tempTruck.next != null)) { //loop through all the trucks
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 02");
                status = exchange02(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 02");
                status = exchange02(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization for each truck
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;
        }

        //while loop
        //Move nodes between the first truck and the last truck.
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 02");
                status = exchange02(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 02");
                status = exchange02(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        //if
    }

    /* cyclic2C2T */

    /**
 * <pre>      vC2T, v cycles, 2 transfers if cost see's fit to exchange the customers
 * ____________First Iteration_________________*________Second Iteration___________
 * headnode ---> R1 p     R1       |-> R1 q    *               R1      R1      R1
 *                                 |           *
 *               R2 q     R2 p     |   R2      * headnode ---> R2  p   R2   -> R2 q
 *                                 |           *                            |
 *               R3       R3 q     |   R3 p    *               R3  q   R3 p |  R3
 *                                 |______|    *                            |
  *               R4       R4           R4      *               R4      R4 q |  R4 p
 *               *                            |_____|
 * ____________________________________________*___________________________________
 * This is an example of VC2T. The v is decided in the MDVRPTW. This diagram
 * is NOT showing the rest of the cycles. Just an example on how this actually
 * takes place for the first 2 iterations.  If the headnode would continue, a null
 * pointer would be encountered there are not enough routes past R2 to keep doing a
 * v cyclic transfer.  A check for this is perfomed and will return out of the
 * program.  This tries to transfer two customers into the next route instead of one.</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param v  variable depth
 * @param whichTruck  number of trucks at the depot
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclicVC2T(int v, int whichTruck, int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int count = 0;
        int truckNo;

        // int maxLoop = 2;                //no need for this passsing as parameter
        int countLoop = 0;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;
        setTotal01(0); //set the number of exchanges to 0
        p = null;

        //check to make sure that there are at least v trucks before the exchanges begin
        if (getNoTrucks() < v) {
            return;
        }

        //assign the first truck route to p
        p = first.mainVisitNodes; //always start at first truck

        /*Need to go to the node in the linked list that is going to be the first truck
  so you must scroll to it
  (starts at one cause you already assigned first node above this comment)   */
        for (assignHeadTruck = 1; assignHeadTruck <= whichTruck;
                assignHeadTruck++) {
            if (assignHeadTruck != 1) {
                tempTruck = tempTruck.next;
            }
        }

        /*Now that you assign/reassign head of node you must make sure there are enough
  routes to do a v cyclic transfer, meaning - while cycling through the linked
  list there will never be a null pointer encouter                              */
        if ((assignHeadTruck + (v - 1)) > getNoTrucks()) {
            //            System.out.println(
            //                "outta range: Null pointer coming not enough routes to do a " +
            //                v + " cyclic transfer");
            return;
        }
        //Do the cyclic transfers!!
        else {
            /* Must reassign p just incase this is not the first run through and the truck
   pointer has been changed if not then just being redundant
   Also must save p, because that will be the last transfer (q, firstTruck)
   or the current head node                                                   */
            p = tempTruck.mainVisitNodes;
            firstTruck = p;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("P before exchange is  " + p.toString());
                System.out.println("Q before exchange is  " + q.toString());
            }

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Execute Exchanging 02");
                status = exchange02(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            /*increment the counter because you have exchanged already once
  and increment the tempTruck pointer so you can make Q the next route        */
            count++;
            tempTruck = tempTruck.next;

            while ((count < v) && (tempTruck != null) &&
                    (tempTruck.next != null)) {
                p = q;
                q = tempTruck.next.mainVisitNodes;

                if (isDiagnostic) {
                    System.out.println("P is  " + p.toString());
                    System.out.println("Q is  " + q.toString());
                }

                /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
                noChange = false;
                countLoop = 0;

                //if routes are the same don't exchange
                if (p == q) {
                    System.out.println("same");
                } else {
                    while (!noChange && (countLoop < maxLoop)) {
                        noChange = true;

                        //              System.out.println("Executing Exchanging 02");
                        status = exchange02(p, q);

                        if (noChange) { //check to see if any changes took place
                            noChange = status;
                        }

                        countLoop++;
                    }
                }

                //else
                //increment the truck pointer
                tempTruck = tempTruck.next;

                //recompute the cost after the local optimization for each truck
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

                //go to next route and increment count
                tempTruck = tempTruck.next;
                count++;
            }

            //while
            //do this to finish back to the first node to make cyclical move ;)
            if (isDiagnostic) {
                System.out.println("***********DOING LAST EXCHANGE***********");
                System.out.println("FirstTruck p is: " + firstTruck.toString());
                System.out.println("q is: " + q.toString());
            }

            noChange = false;
            countLoop = 0;

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Executing Exchanging 02");
                status = exchange02(q, firstTruck);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Executing Exchanging 02");
                status = exchange02(firstTruck, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        //else
    }

    /* cyclicVC2T */

    /**
 * <pre>                    2C1T, 2 cycles, 1 transfer exchange
 *
 *    ________First Iteration____________|_____________Second Iteration_______________
 *                (p,q)  (q,p)           |
 *           R1(p) ------------> R2(q)   |   R1             R2(p)
 *                                       |                    *  (p,q)
 *                                       |                    *  (q,p)
 *           R4                  R3      |   R4             R3(q)
 *    _______________________________________________________________________________
 *    This is just of the first, and second times.  It keeps exchanging between
 *    two customers.  This would happen in a complete circle.</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclic2C1T(int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;

        //int maxLoop = 4;                        //declared and passed from MDVRPTW
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal01(0);

        while ((tempTruck != null) && (tempTruck.next != null)) { //loop through all the trucks
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 01");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange01(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization for each truck
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;
        }

        //while loop
        //Move nodes between the first truck and the last truck.
        if (isDiagnostic) {
            System.out.println("Moving to execute the first and last trucks");
        }

        noChange = false;
        countLoop = 0;

        if ((first != null) && (tempTruck != null) && (tempTruck.next == null)) {
            noChange = true;
            p = first.mainVisitNodes; //first truck
            q = tempTruck.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 01");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 01");
                status = exchange01(q, p);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        //if
    }

    /* cyclic2C1T */

    /**
 * <pre>         3C1T, 3 cycles, 1 transfer if cost see's fit to exchange the customers
 *                          R1 p     R1       |-> R1 q
 *                                            |
 *                          R2 q     R2 p     |   R2
 *                                            |
 *                          R3       R3 q     |   R3 p
 *                                            |______|
 *    ________________________________________________________________________________
 *    Next time through R2 would become the next head node.  If there were an R4
 *    then you could advance down and then make R2 the new head node and then start
 *    cycling through.  There must be at least 3 truck routes in order to perform this
 *    operation otherwise you will recieve a null pointer, because you are trying to
 *    exchange with a route that does not exist.
 *    That's why a check is perfromed for this, if there is not enough routes, then
 *    it returns.</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param whichTruck  number of trucks in the depot
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclic3C1T(int whichTruck, int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int count = 0;
        int truckNo;

        // int maxLoop = 2;                //no need for this passsing as parameter
        int v = 3; //setting the depth at 3 for 3C1T
        int countLoop = 0;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;
        setTotal01(0); //set the number of exchanges to 0
        p = null;

        //check to make sure that there are at least v trucks before the exchanges begin
        if (getNoTrucks() < v) {
            return;
        }

        //assign the first truck route to p
        p = first.mainVisitNodes; //always start at first truck

        /*Need to go to the node in the linked list that is going to be the first truck
  so you must scroll to it
  (starts at one cause you already assigned first node above this comment)   */
        for (assignHeadTruck = 1; assignHeadTruck <= whichTruck;
                assignHeadTruck++) {
            if (assignHeadTruck != 1) {
                tempTruck = tempTruck.next;
            }
        }

        /*Now that you assign/reassign head of node you must make sure there are enough
  routes to do a 3 cyclic transfer, meaning - while cycling through the linked
  list there will never be a null pointer encouter                              */
        if ((assignHeadTruck + (v - 1)) > getNoTrucks()) {
            //            System.out.println(
            //                "outta range: Null pointer coming not enough routes to do a " +
            //                v + " cyclic transfer");
            return;
        }
        //Do the cyclic transfers!!
        else {
            /* Must reassign p just incase this is not the first run through and the truck
   pointer has been changed if not then just being redundant
   Also must save p, because that will be the last transfer (q, firstTruck)
   or the current head node                                                   */
            p = tempTruck.mainVisitNodes;
            firstTruck = p;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("P before exchange is  " + p.toString());
                System.out.println("Q before exchange is  " + q.toString());
            }

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            noChange = false;
            countLoop = 0;

            /*while (!noChange && countLoop < maxLoop){
   noChange = true;
   System.out.println("Execute Exchanging 01");
   status = exchange01(p,q);
   if (noChange)     //check to see if any changes took place
     noChange = status;
   countLoop++;
  }*/
            //System.out.println("P after exchange is  "+ p.toString());
            //System.out.println("Q after exchange is  "+ q.toString());
            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            /*increment the counter because you have exchanged already once
  and increment the tempTruck pointer so you can make Q the next route        */
            count++;
            tempTruck = tempTruck.next;

            while ((count < v) && (tempTruck != null) &&
                    (tempTruck.next != null)) {
                p = q;
                q = tempTruck.next.mainVisitNodes;

                if (isDiagnostic) {
                    System.out.println("P is  " + p.toString());
                    System.out.println("Q is  " + q.toString());
                }

                /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
                noChange = false;
                countLoop = 0;

                //if routes are the same don't exchange
                if (p == q) {
                    System.out.println("same");
                } else {
                    /* while (!noChange && countLoop < maxLoop){
    noChange = true;
    System.out.println("Executing Exchanging 01");
    status = exchange01(p,q);
    if (noChange)     //check to see if any changes took place
      noChange = status;
    countLoop++;
}*/
                }

                //recompute the cost after the local optimization
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

                //increment the truck pointer
                tempTruck = tempTruck.next;
                count++;

                //do this to finish back to the first node to make cyclical move ;)
            }

            //while
            if (isDiagnostic) {
                System.out.println("***********DOING LAST EXCHANGE***********");
                System.out.println("FirstTruck p is: " + firstTruck.toString());
                System.out.println("q is: " + q.toString());
            }

            noChange = false;
            countLoop = 0;

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            /*while (!noChange && countLoop < maxLoop){
      noChange = true;
      System.out.println("Executing Exchanging 01");
      status = exchange01(q, firstTruck);
      if (noChange)     //check to see if any changes took place
        noChange = status;
      countLoop++;
 }*/
            noChange = false;
            countLoop = 0;

            /*while (!noChange && countLoop < maxLoop){
      noChange = true;
      System.out.println("Executing Exchanging 01");
      status = exchange01(firstTruck, q);
      if (noChange)     //check to see if any changes took place
        noChange = status;
      countLoop++;
 }*/
            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        //else
    }

    /* cyclic3C1T */

    /**
 * <pre>         vC1T, v cycles, 1 transfer if cost see's fit to exchange the customers
 *    ____________First Iteration_________________*________Second Iteration___________
 *    headnode ---> R1 p     R1       |-> R1 q    *               R1      R1      R1
 *                                    |           *
 *                  R2 q     R2 p     |   R2      * headnode ---> R2  p   R2   -> R2 q
 *                                    |           *                            |
 *                  R3       R3 q     |   R3 p    *               R3  q   R3 p |  R3
 *                                    |______|    *                            |
 *                  R4       R4           R4      *               R4      R4 q |  R4 p
 *                  *                            |_____|
 *    ____________________________________________*___________________________________
 *    This is an example of 3C1T. The v is decided in the MDVRPTW. This diagram
 *    is NOT showing the rest of the cycles. Just an example on how this actually
 *    takes place for the first 2 iterations.  If the headnode would continue, a null
 *    pointer would be encountered there are not enough routes past R2 to keep doing a
 *    3 cyclic transfer.  A check for this is perfomed and will return out of the
 *    program.</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param v  variable depth
 * @param whichTruck  number of trucks at the depot
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclicVC1T(int v, int whichTruck, int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int count = 0;
        int truckNo;

        // int maxLoop = 2;                //no need for this passsing as parameter
        int countLoop = 0;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;
        setTotal01(0); //set the number of exchanges to 0
        p = null;

        //check to make sure that there are at least v trucks before the exchanges begin
        if (getNoTrucks() < v) {
            return;
        }

        //assign the first truck route to p
        p = first.mainVisitNodes; //always start at first truck

        /*Need to go to the node in the linked list that is going to be the first truck
  so you must scroll to it
   (starts at one cause you already assigned first node above this comment)   */
        assignHeadTruck = 0;

        for (assignHeadTruck = 0; assignHeadTruck <= whichTruck;
                assignHeadTruck++) {
            if (assignHeadTruck != 0) {
                tempTruck = tempTruck.next;
            }
        }

        /*Now that you assign/reassign head of node you must make sure there are enough
  routes to do a v cyclic transfer, meaning - while cycling through the linked
  list there will never be a null pointer encouter                              */
        if ((assignHeadTruck + (v - 1)) > getNoTrucks()) {
            //            System.out.println(
            //                "outta range: Null pointer coming not enough routes to do a " +
            //                v + " cyclic transfer");
            return;
        }
        //Do the cyclic transfers!!
        else {
            /* Must reassign p just incase this is not the first run through and the truck
   pointer has been changed if not then just being redundant
   Also must save p, because that will be the last transfer (q, firstTruck)
   or the current head node    */
            p = tempTruck.mainVisitNodes;
            firstTruck = p;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("P before exchange is  " + p.toString());
                System.out.println("Q before exchange is  " + q.toString());
            }

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Execute Exchanging 01");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            /*increment the counter because you have exchanged already once
  and increment the tempTruck pointer so you can make Q the next route        */
            count++;
            tempTruck = tempTruck.next;

            while ((count < v) && (tempTruck != null) &&
                    (tempTruck.next != null) && (v != 2)) {
                p = q;
                q = tempTruck.next.mainVisitNodes;

                if (isDiagnostic) {
                    System.out.println("P is  " + p.toString());
                    System.out.println("Q is  " + q.toString());
                }

                /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
                noChange = false;
                countLoop = 0;

                //if routes are the same don't exchange
                if (p == q) {
                    System.out.println("same");
                } else {
                    while (!noChange && (countLoop < maxLoop)) {
                        noChange = true;

                        //              System.out.println("Executing Exchanging 01");
                        status = exchange01(p, q);

                        if (noChange) { //check to see if any changes took place
                            noChange = status;
                        }

                        countLoop++;
                    }
                }

                //else
                //increment the truck pointer
                tempTruck = tempTruck.next;

                //recompute the cost after the local optimization for each truck
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

                //go to next route and increment count
                tempTruck = tempTruck.next;
                count++;
            }

            //while
            //do this to finish back to the first node to make cyclical move ;)
            if (isDiagnostic) {
                System.out.println("***********DOING LAST EXCHANGE***********");
                System.out.println("FirstTruck p is: " + firstTruck.toString());
                System.out.println("q is: " + q.toString());
            }

            noChange = false;
            countLoop = 0;

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Executing Exchanging 01");
                status = exchange01(q, firstTruck);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Executing Exchanging 01");
                status = exchange01(firstTruck, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        //else
    }

    /* cyclicVC1T */

    /**
 * <pre>                    2AC1T, 2 a-cyclic, 1 transfer exchange
 *
 *    ________First Iteration____________|______Second Iteration______________________
 *                     (p,q)             |
 *           R1(p) ------------> R2(q)   |   R1             R2(p)
 *                                       |                    *  (p,q)
 *                                       |                    *
 *           R4                  R3      |   R4             R3(q)
 *    _______________________________________________________________________________
 *    This is just of the first, and second times.  It keeps exchanging between
 *    two customers.  This would happen in a complete circle.
 *    ** NOTE: p exchanges with q, but not back from q -> p. **</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclic2AC1T(int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int truckNo;

        //int maxLoop = 4;                        //declared and passed from MDVRPTW
        int countLoop;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        setTotal01(0);

        while ((tempTruck != null) && (tempTruck.next != null)) { //loop through all the trucks
            p = tempTruck.mainVisitNodes;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("The p and q routes are " + p.toString() +
                    q.toString());
            }

            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //System.out.println("Executing Exchanging 1");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            noChange = false;
            countLoop = 0;
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            //if (noChange)    //if no change took place then move to the next node
            tempTruck = tempTruck.next;
        }

        //while loop
        //not needed a-cyclic
        /*if (isDiagnostic)
   System.out.println("Moving to execute the first and last trucks");
 noChange = false;
 countLoop = 0;
 if (first != null && tempTruck != null && tempTruck.next == null)
 {
     noChange = true;
     p = first.mainVisitNodes;                  //first truck
     q = tempTruck.mainVisitNodes;
     if (isDiagnostic)
         System.out.println("The p and q routes are "+p.toString()+q.toString());
     while (!noChange && countLoop < maxLoop){
       noChange = true;
//System.out.println("Executing Exchanging 1");
       status = exchange01(p,q);
       if (noChange)     //check to see if any changes took place
         noChange = status;
       countLoop++;
  }
 }*/
        ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
    }

    /* cyclic2AC1T */

    /**
 * <pre>         3AC1T, 3 a-cyclic, 1 transfer if cost see's fit to exchange the customers
 *                                     R1 p     R1
 *
 *                                     R2 q     R2 p
 *
 *                                     R3       R3 q
 *    ________________________________________________________________________________
 *    Next time through R2 would become the next head node.  If there were an R4
 *    then you could advance down and then make R2 the new head node and then start
 *    cycling through.  There must be at least 3 truck routes in order to perform this
 *    otherwise you will recieve a null pointer, because you are trying to exchange
 *    with a route that does not exist.
 *    That's why a check is perfromed for this, if there is not enough routes, then
 *    it returns.
 *    This does not cycle back up from R3 - R1 because this is a A-cyclic transfer</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param whichTruck  number of trucks at the depot
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclic3AC1T(int whichTruck, int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int count = 0;
        int truckNo;

        // int maxLoop = 2;                //no need for this passsing as parameter
        int v = 3; //setting the depth at 3 for 3C1T
        int countLoop = 0;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;
        setTotal01(0); //set the number of exchanges to 0
        p = null;

        //check to make sure that there are at least v trucks before the exchanges begin
        if (getNoTrucks() < v) {
            return;
        }

        //assign the first truck route to p
        p = first.mainVisitNodes; //always start at first truck

        /*Need to go to the node in the linked list that is going to be the first truck
 so you must scroll to it
  (starts at one cause you already assigned first node above this comment)   */
        for (assignHeadTruck = 1; assignHeadTruck <= whichTruck;
                assignHeadTruck++) {
            if (assignHeadTruck != 1) {
                tempTruck = tempTruck.next;
            }
        }

        /*Now that you assign/reassign head of node you must make sure there are enough
 routes to do a 3 cyclic transfer, meaning - while cycling through the linked
 list there will never be a null pointer encouter                              */
        if ((assignHeadTruck + (v - 1)) > getNoTrucks()) {
            //            System.out.println(
            //                "outta range: Null pointer coming not enough routes to do a " +
            //                v + " cyclic transfer");
            return;
        }
        //Do the cyclic transfers!!
        else {
            /* Must reassign p just incase this is not the first run through and the truck
  pointer has been changed if not then just being redundant
  Also must save p, because that will be the last transfer (q, firstTruck)
  or the current head node                                                   */
            p = tempTruck.mainVisitNodes;
            firstTruck = p;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("P before exchange is  " + p.toString());
                System.out.println("Q before exchange is  " + q.toString());
            }

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //         System.out.println("Execute Exchanging 01");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            /*increment the counter because you have exchanged already once
 and increment the tempTruck pointer so you can make Q the next route        */
            count++;
            tempTruck = tempTruck.next;

            while ((count < v) && (tempTruck != null) &&
                    (tempTruck.next != null)) {
                p = q;
                q = tempTruck.next.mainVisitNodes;

                if (isDiagnostic) {
                    System.out.println("P is  " + p.toString());
                    System.out.println("Q is  " + q.toString());
                }

                /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
                noChange = false;
                countLoop = 0;

                //if routes are the same don't exchange
                if (p == q) {
                    System.out.println("same");
                } else {
                    while (!noChange && (countLoop < maxLoop)) {
                        noChange = true;

                        //             System.out.println("Executing Exchanging 01");
                        status = exchange01(p, q);

                        if (noChange) { //check to see if any changes took place
                            noChange = status;
                        }

                        countLoop++;
                    }
                }

                //recompute the cost after the local optimization
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

                //increment the truck pointer
                tempTruck = tempTruck.next;
                count++;
            }

            //recompute the cost after the local optimization one more time
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            //else
        }
    }

    /* cyclic3AC1T */

    /**
 * <pre>         vAC1T, v cycles, 1 transfer if cost see's fit to exchange the customers
 *    ____________First Iteration____________*_____________Second Iteration___________
 *    headnode ---> R1 p     R1              *               R1      R1
 *    *
 *                  R2 q     R2 p            * headnode ---> R2  p   R2
 *                  *
 *                  R3       R3 q            *               R3  q   R3 p
 *                  *
 *                  R4       R4              *               R4      R4 q
 *
 *    This is an example of 3AC1T. The v is decided in the MDVRPTW. This diagram
 *    is NOT showing the rest of the cycles. Just an example on how this actually
 *    takes place for the first 2 iterations.  If the headnode would continue, a null
 *    pointer would be encountered there are not enough routes past R2 to keep doing a
 *    3 cyclic transfer.  A check for this is perfomed and will return out of the
 *    method.
 *    **NOTE: There is no exchange from the first/head node to the last truck. **</pre>
 * This method was modified from code taken from the MDVRPTW problem
 * @param v  variable depth
 * @param whichTruck  number of trucks at the depot
 * @param maxLoop  maximum number of customer exchange comparison iterations
 */
    public void cyclicVAC1T(int v, int whichTruck, int maxLoop) {
        boolean noChange = false; //No change took place
        boolean status = true;
        boolean isDiagnostic = false;
        int count = 0;
        int truckNo;

        // int maxLoop = 2;                //no need for this passsing as parameter
        int countLoop = 0;
        Truck tempTruck = first;
        VisitNodesLinkedList p;
        VisitNodesLinkedList q;
        VisitNodesLinkedList firstTruck;
        VisitNodesLinkedList lastTruck;
        setTotal01(0); //set the number of exchanges to 0
        p = null;

        //check to make sure that there are at least v trucks before the exchanges begin
        if (getNoTrucks() < v) {
            return;
        }

        //assign the first truck route to p
        p = first.mainVisitNodes; //always start at first truck

        /*Need to go to the node in the linked list that is going to be the first truck
  so you must scroll to it
   (starts at one cause you already assigned first node above this comment)   */
        for (assignHeadTruck = 1; assignHeadTruck <= whichTruck;
                assignHeadTruck++) {
            if (assignHeadTruck != 1) {
                tempTruck = tempTruck.next;
            }
        }

        /*Now that you assign/reassign head of node you must make sure there are enough
  routes to do a v cyclic transfer, meaning - while cycling through the linked
  list there will never be a null pointer encouter                              */
        if ((assignHeadTruck + (v - 1)) > getNoTrucks()) {
            //            System.out.println(
            //                "outta range: Null pointer coming not enough routes to do a " +
            //                v + " cyclic transfer");
            return;
        }
        //Do the cyclic transfers!!
        else {
            /* Must reassign p just incase this is not the first run through and the truck
   pointer has been changed if not then just being redundant
   Also must save p, because that will be the last transfer (q, firstTruck)
   or the current head node                                                   */
            p = tempTruck.mainVisitNodes;
            firstTruck = p;
            q = tempTruck.next.mainVisitNodes;

            if (isDiagnostic) {
                System.out.println("P before exchange is  " + p.toString());
                System.out.println("Q before exchange is  " + q.toString());
            }

            /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
            noChange = false;
            countLoop = 0;

            while (!noChange && (countLoop < maxLoop)) {
                noChange = true;

                //          System.out.println("Execute Exchanging 01");
                status = exchange01(p, q);

                if (noChange) { //check to see if any changes took place
                    noChange = status;
                }

                countLoop++;
            }

            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

            /*increment the counter because you have exchanged already once
  and increment the tempTruck pointer so you can make Q the next route        */
            count++;
            tempTruck = tempTruck.next;

            while ((count < v) && (tempTruck != null) &&
                    (tempTruck.next != null)) {
                p = q;
                q = tempTruck.next.mainVisitNodes;

                if (isDiagnostic) {
                    System.out.println("P is  " + p.toString());
                    System.out.println("Q is  " + q.toString());
                }

                /*RUN THROUGH ALL P'S CUSTOMERS AND Q'S CUSTOMERS TO SEE IF AN EXCHANGE WORKS*/
                noChange = false;
                countLoop = 0;

                //if routes are the same don't exchange
                if (p == q) {
                    System.out.println("same");
                } else {
                    while (!noChange && (countLoop < maxLoop)) {
                        noChange = true;

                        //              System.out.println("Executing Exchanging 01");
                        status = exchange01(p, q);

                        if (noChange) { //check to see if any changes took place
                            noChange = status;
                        }

                        countLoop++;
                    }
                }

                //else
                //increment the truck pointer
                tempTruck = tempTruck.next;

                //recompute the cost after the local optimization for each truck
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);

                //go to next route
                tempTruck = tempTruck.next;
                count++;
            }

            //while
            //recompute the cost after the local optimization
            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        //else
    }

    /* cyclicVAC1T */

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
 * <p>Move one customer from route p to route q.</p>
 * <p>Initially:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1> 1 -9 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c1</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c2</p>
 * <p>After first exchange:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1>-9 -9 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c3</p>
 * <p>Route(q): {<0> 0  1 -9 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c4</p>
 * <p>Excahnge is performed if (c1+c2) > (c3+c4)</p>
 * <p>The calcExchange methods in the VisitnodesLinked list are called to do the
 * actual exchanges.</p>
 * //////////////////Modified by Sunil Gurung 10/20/03/////////////////////
 * @param p Visitnodes linked list from truck p
 * @param q Visitnodes linked list from truck q
 * @return boolean truck if exchanges occured
 */
    public boolean exchange01(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        boolean isDiagnostic = false;
        boolean noChange = true; //no exchanges took place
        int noExs = 0;

        //check to make sure that there are at least 2 trucks before the exchanges begins
        if (getNoTrucks() < 2) {
            return noChange;
        }

        try {
            int i;
            int j;
            String curr_str = "01";

            if (isDiagnostic) {
                try {
                    System.out.println("Exchange01: routes " + p.toString() +
                        " " + q.toString());
                } catch (Exception ex) {
                }
            }

            i = 1;

            //Loop till the number of nodes in the list
            while (i <= (p.getSize() - 2)) {
                j = 1;

                if (q.getSize() > 2) //there is at least one node - if empty, no exchanges
                 {
                    //calculate exchange checks to see if it is feasible to
                    //exhange the nodes. Send the cell information as a string
                    String str = p.calcExchange01(p.pointString(i)); //remove i from p

                    StringTokenizer s = new StringTokenizer(str, ";");
                    float cost1 = (Float.valueOf(s.nextToken())).floatValue();

                    //returns the cost of insertion of p into Q plus the location of the insertion
                    //Here the p is added to q and the sum plus the location where it was tested is
                    //is returned
                    String str1 = q.calcExchange10(p.pointString(i)); //add p to q
                    StringTokenizer s1 = new StringTokenizer(str1, ";");
                    float cost2 = (Float.valueOf(s1.nextToken())).floatValue();
                    int index2 = (Integer.valueOf(s1.nextToken())).intValue();
                    float cost_diff = cost1 + cost2;

                    //if it is feasible then the actual exchange is made
                    if (cost_diff < 0) {
                        //do the actual exchange
                        //Getting out the index permanently from p
                        String p1 = p.pointString(i);
                        p.exchange01(p1);

                        //Inserting the index from P to Q at locaiton index2 permanently
                        q.exchange10(p1, index2);

                        noChange = false;

                        //                        System.out.println("global optimization done");
                        if (isDiagnostic) {
                            System.out.println(" *** An exchange took place **");
                        }

                        localOptVRPTW(); //Calling of local optimization
                        noExs++; //increment number of exchanges
                    }
                }

                /* inner while */
                i++;
            }

            /* outer while */
        } catch (Exception e) {
            System.err.println("Caught in exchange01: " + e);

            return false;
        }

        //set the number of exchanges
        setTotal01(getTotal01() + noExs);

        return noChange;
    }

    /* exchange01() */

    /**
 * <p>Exchange one customer between two trucks.</p>
 * <p>Initially:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1> 1 -9 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c1</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c2</p>
 * <p>After first exchange:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1>-9  2 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c3</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1>-9  1 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c4</p>
 * <p>Excahnge is performed if (c1+c2) > (c3+c4)</p>
 * <p>The calcExchange methods in the VisitnodesLinked list are called to do the
 * actual exchanges.</p>
 * //////////////////Modified by Sunil Gurung 11/07/03/////////////////////
 * @param p Visitnodes linked list from truck p
 * @param q Visitnodes linked list from truck q
 * @return boolean truck if exchanges occured
 */
    public boolean exchange11(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        boolean isDiagnostic = false;
        boolean noChange = true; //no exchanges took place
        int noExs = 0; //Counter to count the number of exchanges

        try {
            int m = 1;
            int n = 1;
            String curr_str = "11";

            //if (isDiagnostic)
            //System.out.println("p= "+p+" q = "+q);
            // MasterServer.consoleFrame.printConsole(p.toString()+" "+q.toString()+" "+m+"-"+n);
            if (isDiagnostic) {
                System.out.println(
                    "Routes being considered by exchange 11 are:");
                System.out.println("Route 1: ");
                p.displayForwardKeyListMDVRP();
                System.out.println("Route 2: ");
                q.displayForwardKeyListMDVRP();
            }

            //int exch = 0;   //Counter to count the number of exchanges
            //Loop number of customers in p route
            for (m = 1; m < (p.getSize() - 2); m++) { //Outer loop

                //Loop number of customers in q route
                for (n = 1; n < (q.getSize() - 2); n++) { //Inner loop

                    String s1 = p.calcExchange11(p.pointString(m),
                            q.pointString(n));

                    //Tokenizing the string and retreiving the contents
                    StringTokenizer st1 = new StringTokenizer(s1, ";");
                    float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                    int index1 = (Integer.valueOf(st1.nextToken())).intValue();

                    //For evaluating the effect of exchange11 in q route
                    float cost2 = 000000000;
                    int index2 = -1;

                    //if insertion was feasible
                    if (index1 != -1) {
                        //then try evaluating the cost of inserting m in q and taking
                        //out n in q
                        String s2 = q.calcExchange11(q.pointString(n),
                                p.pointString(m));

                        //Tokenizing the string and retreiving the contents
                        StringTokenizer st2 = new StringTokenizer(s2, ";");
                        cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                        index2 = (Integer.valueOf(st2.nextToken())).intValue();
                    } else {
                        cost2 = 999999999; //insertion was not feasible

                        if (isDiagnostic) {
                            System.out.println(
                                "Previous Insertion was infeasible, skipping next 11 move");
                        }
                    }

                    float cost_diff;
                    cost_diff = cost1 + cost2;

                    //If the difference is less than 0 then the permanent echange02() can be performed
                    //ie (c3+c4) < (c1+c2)
                    if (cost_diff < 0) {
                        //Get the cutomer informations those are going to be exchanged permanently
                        String p1 = p.pointString(m);
                        String q1 = q.pointString(n);

                        //Do the permanent exchanges
                        p.exchange11(p1, q1, index1);
                        q.exchange11(q1, p1, index2);

                        if (isDiagnostic) {
                            System.out.println(
                                "permanent change in exchange 11");
                            System.out.println("AFTER EXCHANGE calc11\n" +
                                p.toString() + "\n" + q.toString());
                        }

                        // m = 1;
                        //n = 1;
                        //exch++;
                        noExs++;

                        if (noChange) {
                            noChange = false;
                        }

                        localOptVRPTW(); //Calling of local optimization
                    }
                }

                //Inner loop
            }

            //Outer loop
        } catch (Exception e) {
            System.err.println("Caught in exchange11: " + e);
        }

        //set the number of exchanges to the previous moves
        setTotal11(getTotal11() + noExs);

        return noChange;
    }

    /* exchange11() */

    /**
 * <p>Move two customers from route p to route q and one customer from route q to p</p>
 * <p>Initially:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1> 1 -9 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c1</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c2</p>
 * <p>After first exchange:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1>-9  2 -9 | <2>-9 -9 -9 | <3> 0 -9 -9} = c3</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1>-9  1  3 | <2> 4 -9 -9 | <3> 0 -9 -9} = c4</p>
 * <p>Excahnge is performed if (c1+c2) > (c3+c4)</p>
 * <p>The calcExchange methods in the VisitnodesLinked list are called to do the
 * actual exchanges.</p>
 * //////////////////Modified by Sunil Gurung 11/07/03/////////////////////
 * @param p Visitnodes linked list from truck p
 * @param q Visitnodes linked list from truck q
 * @return boolean truck if exchanges occured
 */
    public boolean exchange12(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        boolean isDiagnostic = false;
        boolean noChange = true; //no exchanges took place
        int noExs = 0;

        try {
            int i;
            int j;
            String curr_str = "12";

            if (isDiagnostic) {
                System.out.println(
                    "Routes being considered by exchange 12 are:");
                System.out.println("Route 1: ");
                p.displayForwardKeyListMDVRP();
                System.out.println("Route 2: ");
                q.displayForwardKeyListMDVRP();
            }

            i = 1; //Start at 1

            //Loop till the number of nodes in the p list
            while (i < (p.getSize() - 2)) { //Outer while loop
                j = 1;

                //Loop till the number of nodes in the q list
                while (j < (q.getSize() - 2)) {
                    //Check to see if the nodes to be considered are within the route or not
                    if ((i <= (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2))) {
                        String s1 = p.calcExchange12(q.pointString(j),
                                p.pointString(i), p.pointString(i + 1));

                        //Tokenizing the string and retreiving the contents
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index1 = (Integer.valueOf(st1.nextToken())).intValue();

                        //For q route evaluation
                        float cost2 = 999999999;
                        int index21 = -1;
                        int index22 = -1;

                        //If the index1 was inserted then try inserting another node
                        if (index1 != -1) {
                            String s2 = q.calcExchange21(p.pointString(i),
                                    p.pointString(i + 1), q.pointString(j));

                            //Tokenizing the string and retreiving the contents
                            StringTokenizer st2 = new StringTokenizer(s2, ";");
                            cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                            index21 = (Integer.valueOf(st2.nextToken())).intValue();
                            index22 = (Integer.valueOf(st2.nextToken())).intValue();
                        } else {
                            cost2 = 999999999; //insertion was not feasible

                            if (isDiagnostic) {
                                System.out.println(
                                    "Previous Insertion was infeasible, skipping next 11 move");
                            }
                        }

                        float cost_diff = cost1 + cost2;

                        //if the cost_diff is less than 0 that means it is feasible and profitable to do
                        //the permanent exchange
                        if (cost_diff < 0) { //cost_diff<1000000)

                            String p1 = p.pointString(i);
                            String p2 = p.pointString(i + 1);
                            String q1 = q.pointString(j);

                            //Do the permanent exchange
                            p.exchange12(q1, index1, p1, p2);
                            q.exchange21(p1, index21, p2, index22, q1);

                            if (isDiagnostic) {
                                System.out.println(
                                    "permanent change in exchange 12");
                                System.out.println("AFTER EXCHANGE calc12\n" +
                                    p.toString() + "\n" + q.toString());
                            }

                            noExs++;

                            if (noChange) {
                                noChange = false;
                            }

                            localOptVRPTW(); //Calling of local optimization
                        }

                        //if
                    }

                    //if
                    //best_flag = 0;
                    j++;
                }

                /* inner while */
                i++;
            }

            /* outer while */
        } catch (Exception e) {
            System.err.println("Caught in exchange12: " + e);
        }

        //set the number of exchanges to the previous moves
        setTotal12(getTotal12() + noExs);

        return noChange;
    }

    /* exchange12() */

    /**
 * <p>Move two customers from route p to route q</p>
 * <p>Initially:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1> 1 -9 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c1</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c2</p>
 * <p>After first exchange:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1>-9 -9 -9 | <2>-9 -9 -9 | <3> 0 -9 -9} = c3</p>
 * <p>Route(q): {<0> 0  1  3 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c4</p>
 * <p>Excahnge is performed if (c1+c2) > (c3+c4)</p>
 * <p>The calcExchange methods in the VisitnodesLinked list are called to do the
 * actual exchanges.</p>
 * ////////////////////Modified by Sunil Gurung 11/04/03////////////////////
 * @param p Visitnodes linked list from truck p
 * @param q Visitnodes linked list from truck q
 * @return boolean truck if exchanges occured
 */
    public boolean exchange02(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        boolean isDiagnostic = false;
        boolean noChange = true; //no exchanges took place
        int noExs = 0;

        //check to make sure that there are at least 2 trucks before the exchanges begins
        if (getNoTrucks() < 2) {
            return noChange;
        }

        try {
            int i;
            int j;
            String curr_str = "02";

            /* if (p_flag) System.out.println ("\nExchange12: route (%2d, %2d) -> ", p, q); */
            if (isDiagnostic) {
                /*System.out.println("Routes being considered by exchange 02 are:");
System.out.println("Route 1: ");
p.displayForwardKeyListMDVRP();
System.out.println("Route 2: ");
q.displayForwardKeyListMDVRP();*/
                try {
                    System.out.println("Exchange02: routes " + p.toString() +
                        " " + q.toString());
                } catch (Exception ex) {
                }
            }

            i = 1; //First customer of p

            while (i < (p.getSize() - 2)) {
                j = 1; //First customer of q

                if (q.getSize() > 2) { //there is at least one node - if empty, no exchanges

                    if ((i < (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2))) {
                        //calculate exchange checks to see if it is feasible to
                        //exhange the nodes. Send the cell information as a string
                        //send i and i + 1 as string and figure out the difference of cost without
                        //these nodes in p
                        String str1 = p.calcExchange02(p.pointString(i),
                                p.pointString(i + 1));
                        StringTokenizer st1 = new StringTokenizer(str1, ";");

                        //Retrieving the cost1 from the string
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();

                        //calculate exchange20 to see if it is feasible to insert the nodes in q
                        //and send the cost as string along with the indexes of two insertions
                        //Send the cell information as a string
                        //send i and i + 1 as string and figure out the difference of cost without
                        //these nodes in p
                        String str2 = q.calcExchange20(p.pointString(i),
                                p.pointString(i + 1));

                        StringTokenizer st2 = new StringTokenizer(str2, ";");
                        float cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                        int index21 = (Integer.valueOf(st2.nextToken())).intValue();
                        int index22 = (Integer.valueOf(st2.nextToken())).intValue();

                        //Finding out the difference between two nodes being moved from p to q
                        float cost_diff = cost1 + cost2;

                        //If the difference is less than 0 then the permanent echange02() can be performed
                        if (cost_diff < 0) {
                            String p1 = p.pointString(i);
                            String p2 = p.pointString(i + 1);

                            //System.out.println("BEFORE EXCHANGE calc02\n"+p.toString()+"\n"+q.toString());
                            p.exchange02(p1, p2);
                            q.exchange20(p1, index21, p2, index22);

                            //                            System.out.println("global optimization done");
                            //  if (isDiagnostic) {
                            //   System.out.println("permanent change in exchange 02");
                            //  System.out.println("AFTER EXCHANGE calc02\n"+p.toString()+"\n"+q.toString());
                            //}
                            noExs++;

                            if (noChange) {
                                noChange = false;
                            }

                            localOptVRPTW(); //Calling of local optimization
                        }

                        //if
                    }

                    //if
                }

                /* inner while */
                i++;
            }

            /* outer while */
        } catch (Exception e) {
            System.err.println("Caught in exchange02: " + e);
        }

        //set the number of exchanges to the previous moves
        setTotal02(getTotal02() + noExs);

        return noChange;
    }

    /* exchange02() */

    /**
 * <p>Move two customers from route p to route q and two customer from route q to p</p>
 * <p>Initially:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1> 1 -9 -9 | <2> 3 -9 -9 | <3> 0 -9 -9} = c1</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1> 2 -9 -9 | <2> 4 -9 -9 | <3> 0 -9 -9} = c2</p>
 * <p>After first exchange:</p>
 * <p>Route(p): {<0> 0 -9 -9 | <1>-9  2  4 | <2>-9 -9 -9 | <3> 0 -9 -9} = c3</p>
 * <p>Route(q): {<0> 0 -9 -9 | <1>-9  1  3 | <2>-9 -9 -9 | <3> 0 -9 -9} = c4</p>
 * <p>Excahnge is performed if (c1+c2) > (c3+c4)</p>
 * <p>The calcExchange methods in the VisitnodesLinked list are called to do the
 * actual exchanges.</p>
 * ////////////////////Modified by Sunil Gurung 11/09/03////////////////////
 * @param p Visitnodes linked list from truck p
 * @param q Visitnodes linked list from truck q
 * @return boolean truck if exchanges occured
 */
    public boolean exchange22(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        boolean isDiagnostic = false;
        boolean noChange = true;
        int noExs = 0;

        //check to make sure that there are at least 2 trucks before the exchanges begins
        if (getNoTrucks() < 2) {
            return noChange;
        }

        try {
            int i;
            int j;
            String curr_str = "22";

            i = 1; //First customer of p

            while (i < (p.getSize() - 2)) {
                j = 1; //First customer of q

                //Loop till the number of nodes in the q list
                while (j < (q.getSize() - 2)) {
                    //Check to see if the nodes to be considered are within the route or not for all
                    //i.e. i, (i+1) , j and (j+1)
                    if ((i <= (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2)) &&
                            (j <= (q.getSize() - 2)) &&
                            ((j + 1) <= (q.getSize() - 2))) {
                        //Saving the i and i+1 of p, and j and j+1 of q as strings in p1, p2, q1, and q2 respectivley
                        String p1 = p.pointString(i);
                        String p2 = p.pointString(i + 1);
                        String q1 = q.pointString(j);
                        String q2 = q.pointString(j + 1);

                        //Calculating the cost in p
                        String s1 = p.calcExchange22(q1, q2, p1, p2);

                        //Tokenizing the string and retreiving the contents
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index11 = (Integer.valueOf(st1.nextToken())).intValue();
                        int index12 = (Integer.valueOf(st1.nextToken())).intValue();

                        //For q route evaluation
                        float cost_diff = 999999999;
                        float cost2 = 999999999;
                        int index21 = -1;
                        int index22 = -2;

                        //Earlier insertions were successful in p, so try in q now
                        if ((index11 != -1) && (index12 != -1)) { //insertion took place for 2 nodes

                            //Calculating the cost in p
                            String s2 = q.calcExchange22(p1, p2, q1, q2);

                            //Tokenizing the string and retreiving the contents
                            StringTokenizer st2 = new StringTokenizer(s2, ";");
                            cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                            index21 = (Integer.valueOf(st2.nextToken())).intValue();
                            index22 = (Integer.valueOf(st2.nextToken())).intValue();

                            if ((index21 != -1) && (index22 != -1)) { //insertion took place for 2 nodes
                                cost_diff = cost1 + cost2;
                            }
                        } else {
                            cost_diff = 999999999;
                        }

                        if (cost_diff < 0) // || cost_diff<1000000 && p.getSize()==4)
                         {
                            try {
                                //Do the exchanges permanently
                                p.exchange22(q1, index11, q2, index12, p1, p2);
                                q.exchange22(p1, index21, p2, index22, q1, q2);

                                if (isDiagnostic) {
                                    System.out.println(
                                        "permanent change in exchange 22");
                                    System.out.println(
                                        "AFTER EXCHANGE calc22\n" +
                                        p.toString() + "\n" + q.toString());
                                }

                                noExs++;

                                if (noChange) {
                                    noChange = false;
                                }

                                localOptVRPTW(); //Calling of local optimization
                            } catch (Exception xp) {
                                System.out.println("TrunkLinkList:ERROR:" + xp);
                            }

                            //if
                        }

                        //if
                    }

                    //while
                    //best_flag = 0;
                    j++;
                }

                /* inner while */
                i++;
            }

            /* outer while */
        } catch (Exception e) {
            System.out.println("Caught in exchange22: " + e);
        }

        //set the number of exchanges to the previous moves
        setTotal22(getTotal22() + noExs);

        return noChange;
    }

    //============================================================================================================
    //======================= START (TABU) FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =========================
    //============================================================================================================

    /* exchange22() */

    /**
 * <p>Tabu version of exchange01 between two trucks using the cost functions defined
 * in ProblemInfo. The method uses calcExchange01Opt.</p>
 * @param  truck1   first truck used in exchange
 * @param  truck2   second truck used in exchange
 * @param  depot1No depot number of first truck
 * @param  depot2No depot number of second truck
 * @return ExchangesOperation best operation performed, else null
 */
    public ExchangesOperation checkImplementExchange01Opt(Truck truck1,
        Truck truck2, int depot1No, int depot2No) {
        VisitNodesLinkedList p = truck1.mainVisitNodes;
        VisitNodesLinkedList q = truck2.mainVisitNodes;
        int beforeNode1;
        int beforeNode2;
        int afterNode1;
        int afterNode2;
        boolean isDiagnostic = false;
        boolean noChange = true; //no exchanges took place
        int noExs = 0;
        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;

        //check to make sure that there are at least 2 trucks in each depot before the exchanges begins
        if ((truck1 == null) || (truck2 == null)) {
            return null;
        }

        try {
            int i;
            int j;
            String curr_str = "01";
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                if (q.getSize() > 2) {
                    //calculate exchange checks to see if it is feasible to
                    //exhange the nodes. Send the cell information as a string
                    String str = p.calcExchange01Opt(p.pointString(i)); //remove p from p
                    StringTokenizer s = new StringTokenizer(str, ";");
                    float cost1 = (Float.valueOf(s.nextToken())).floatValue();

                    //returns the cost of insertion plus the location of the insertion
                    String str1 = q.calcExchange10Opt(p.pointString(i)); //add p to q
                    StringTokenizer s1 = new StringTokenizer(str1, ";");
                    float cost2 = (Float.valueOf(s1.nextToken())).floatValue();
                    int index2 = (Integer.valueOf(s1.nextToken())).intValue();
                    float cost_diff = cost1 + cost2;
                    beforeNode1 = p.getPoint(i - 1);
                    afterNode1 = p.getPoint(i + 1);

                    currOp = new ExchangesOperation();
                    currOp.setOperation01(truck1, truck2, depot1No, depot2No,
                        cost1, cost2, index2, p.pointString(i), beforeNode1,
                        afterNode1);

                    //if it is feasible then the actual exchange is made
                    if (cost_diff < 0) {
                        if (ProblemInfo.optType == ZeusConstants.FIRST_FIRST) {
                            if (ProblemInfo.isUsingTabu) {
                                if (!ProblemInfo.tabuSearch.isTabu(currOp)) {
                                    executeExchangeOpt(currOp);
                                    ProblemInfo.tabuSearch.updateBestSolution();
                                    noExs++;
                                }
                            } else {
                                executeExchangeOpt(currOp);
                                noExs++;
                            }
                        } else {
                            if (bestOp == null) {
                                bestOp = currOp;
                            } else if (currOp.changeInCost < bestOp.changeInCost) {
                                bestOp = currOp;
                            }
                        }
                    }
                }

                /* inner while */
                i++;
            }

            /* outer while */
            switch (ProblemInfo.optType) {
            case ZeusConstants.FIRST_FIRST:
                setTotal01(getTotal01() + noExs);

                return null;

            case ZeusConstants.FIRST_BEST:

                if (bestOp == null) {
                    return null;
                }

                if (bestOp.changeInCost < 0) {
                    if (ProblemInfo.isUsingTabu) {
                        if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                            executeExchangeOpt(bestOp);
                            setTotal01(getTotal01() + 1); // only 1 exchange was performed, the FIRST_BEST
                            ProblemInfo.tabuSearch.updateBestSolution();
                        }
                    } else {
                        executeExchangeOpt(bestOp);
                        setTotal01(getTotal01() + 1); // only 1 exchange was performed, the FIRST_BEST
                    }
                }

                return null;

            case ZeusConstants.BEST_BEST:
                return bestOp;
            }
        } catch (Exception e) {
            System.err.println("ERROR: CheckImplementexchange01Opt: " + e);
        }

        return null;
    }

    /**
 * <p>Tabu version of exchange02 between two trucks using the cost functions defined
 * in ProblemInfo. The method uses calcExchange02Opt and calcExchange20Opt.</p>
 * @param  truck1   first truck used in exchange
 * @param  truck2   second truck used in exchange
 * @param  depot1No depot number of first truck
 * @param  depot2No depot number of second truck
 * @return ExchangesOperation best operation performed, else null
 */
    public ExchangesOperation checkImplementExchange02Opt(Truck truck1,
        Truck truck2, int depot1No, int depot2No) {
        boolean isDiagnostic = false;
        boolean noChange = true;
        int noExs = 0;
        int beforeNode1;
        int afterNode1;
        VisitNodesLinkedList p = truck1.mainVisitNodes;
        VisitNodesLinkedList q = truck2.mainVisitNodes;
        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;

        //check to make sure that there are at least 2 trucks in each depot before the exchanges begins
        if ((truck1 == null) || (truck2 == null)) {
            return null;
        }

        try {
            int i;
            int j;
            String curr_str = "02";
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                if (q.getSize() > 2) {
                    if ((i <= (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2))) {
                        float cost1 = p.calcExchange02Opt(p.pointString(i),
                                p.pointString(i + 1));
                        String s2 = q.calcExchange20Opt(p.pointString(i),
                                p.pointString(i + 1));
                        StringTokenizer st2 = new StringTokenizer(s2, ";");
                        float cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                        int index21 = (Integer.valueOf(st2.nextToken())).intValue();
                        int index22 = (Integer.valueOf(st2.nextToken())).intValue();
                        float cost_diff = cost1 + cost2;
                        String p1 = p.pointString(i);
                        String p2 = p.pointString(i + 1);
                        beforeNode1 = p.getPoint(i - 1);
                        afterNode1 = p.getPoint(i + 2);
                        currOp = new ExchangesOperation();
                        currOp.setOperation02(truck1, truck2, depot1No,
                            depot2No, cost1, cost2, index21, index22, p1, p2,
                            beforeNode1, afterNode1);

                        if (cost_diff < 0) { //cost_diff<1000000)

                            if (ProblemInfo.optType == ZeusConstants.FIRST_FIRST) {
                                // if FIRST_FIRST, execute right away
                                if (ProblemInfo.isUsingTabu) {
                                    if (!ProblemInfo.tabuSearch.isTabu(currOp)) {
                                        executeExchangeOpt(currOp);
                                        ProblemInfo.tabuSearch.updateBestSolution();
                                        noExs++;
                                    }
                                } else {
                                    executeExchangeOpt(currOp);
                                    noExs++;
                                }
                            } else {
                                // if FIRST_BEST or BEST_BEST, find the best exchange out of these two
                                // trucks, and save it in bestOp
                                if (bestOp == null) {
                                    bestOp = currOp;
                                } else if (currOp.changeInCost < bestOp.changeInCost) {
                                    bestOp = currOp;
                                }
                            }
                        }

                        //if cost_diff < 0
                    }

                    //if
                    j++;
                }

                /* inner while */
                i++;
            }

            /* outer while */
            switch (ProblemInfo.optType) {
            case ZeusConstants.FIRST_FIRST:
                setTotal02(getTotal02() + noExs);

                return null;

            case ZeusConstants.FIRST_BEST:

                if (bestOp == null) {
                    return null;
                }

                if (bestOp.changeInCost < 0) {
                    if (ProblemInfo.isUsingTabu) {
                        if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                            executeExchangeOpt(bestOp);
                            setTotal02(getTotal02() + 1); // only 1 exchange was performed, the FIRST_BEST
                            ProblemInfo.tabuSearch.updateBestSolution();
                        }
                    } else {
                        executeExchangeOpt(bestOp);
                        setTotal02(getTotal02() + 1); // only 1 exchange was performed, the FIRST_BEST
                    }
                }

                return null;

            case ZeusConstants.BEST_BEST:
                return bestOp;
            }
        } catch (Exception e) {
            System.err.println("ERROR: CheckImplementexchange02Opt: " + e);
        }

        return null;
    }

    /**
 * <p>Tabu version of exchange11 between two trucks using the cost functions defined
 * in ProblemInfo. The method uses calcExchange11Opt.</p>
 * @param  truck1   first truck used in exchange
 * @param  truck2   second truck used in exchange
 * @param  depot1No depot number of first truck
 * @param  depot2No depot number of second truck
 * @return ExchangesOperation best operation performed, else null
 */
    public ExchangesOperation checkImplementExchange11Opt(Truck truck1,
        Truck truck2, int depot1No, int depot2No) {
        boolean isDiagnostic = false;
        int beforeNode1;
        int beforeNode2;
        int afterNode1;
        int afterNode2;
        VisitNodesLinkedList p = truck1.mainVisitNodes;
        VisitNodesLinkedList q = truck2.mainVisitNodes;
        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;

        //check to make sure that there are at least 2 trucks in each depot before the exchanges begins
        if ((truck1 == null) || (truck2 == null)) {
            return null;
        }

        int noExs = 0;

        try {
            int i;
            int j;
            String curr_str = "11";
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                while (j <= (q.getSize() - 2)) {
                    if (i <= (p.getSize() - 2)) {
                        String s1 = p.calcExchange11Opt(p.pointString(i),
                                q.pointString(j));
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index1 = (Integer.valueOf(st1.nextToken())).intValue();
                        float cost2 = Float.MAX_VALUE;
                        int index2 = -1;

                        if (index1 != -1) {
                            String s2 = q.calcExchange11Opt(q.pointString(j),
                                    p.pointString(i));
                            StringTokenizer st2 = new StringTokenizer(s2, ";");
                            cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                            index2 = (Integer.valueOf(st2.nextToken())).intValue();
                        } else {
                            cost2 = Float.MAX_VALUE; // insertion not feasable
                        }

                        float cost_diff = cost1 + cost2;
                        String p1 = p.pointString(i);
                        String q1 = q.pointString(j);
                        beforeNode1 = p.getPoint(i - 1);
                        beforeNode2 = q.getPoint(j - 1);
                        afterNode1 = p.getPoint(i + 1);
                        afterNode2 = q.getPoint(j + 1);
                        currOp = new ExchangesOperation();
                        currOp.setOperation11(truck1, truck2, depot1No,
                            depot2No, cost1, cost2, index1, index2, p1, q1,
                            beforeNode1, beforeNode2, afterNode1, afterNode2);

                        if (cost_diff < 0) {
                            if (ProblemInfo.optType == ZeusConstants.FIRST_FIRST) {
                                // if FIRST_FIRST, execute right away
                                if (ProblemInfo.isUsingTabu) {
                                    if (!ProblemInfo.tabuSearch.isTabu(currOp)) {
                                        executeExchangeOpt(currOp);
                                        ProblemInfo.tabuSearch.updateBestSolution();
                                        noExs++;
                                    }
                                } else {
                                    executeExchangeOpt(currOp);
                                    noExs++;
                                }
                            } else {
                                // if FIRST_BEST or BEST_BEST, find the best exchange out of these two
                                // trucks, and save it in bestOp
                                if (bestOp == null) {
                                    bestOp = currOp;
                                } else if (currOp.changeInCost < bestOp.changeInCost) {
                                    bestOp = currOp;
                                }
                            }
                        }

                        //if cost_diff < 0
                    }

                    //else
                    j++;
                }

                //inner while
                i++;
            }

            // outer while
            switch (ProblemInfo.optType) {
            case ZeusConstants.FIRST_FIRST:
                setTotal11(getTotal11() + noExs);

                return null;

            case ZeusConstants.FIRST_BEST:

                if (bestOp == null) {
                    return null;
                }

                if (bestOp.changeInCost < 0) {
                    if (ProblemInfo.isUsingTabu) {
                        if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                            executeExchangeOpt(bestOp);
                            setTotal02(getTotal11() + 1); // only 1 exchange was performed, the FIRST_BEST
                            ProblemInfo.tabuSearch.updateBestSolution();
                        }
                    } else {
                        executeExchangeOpt(bestOp);
                        setTotal02(getTotal11() + 1); // only 1 exchange was performed, the FIRST_BEST
                    }
                }

                return null;

            case ZeusConstants.BEST_BEST:
                return bestOp;
            } // switch
        } catch (Exception e) {
            System.err.println("ERROR: checkImplementExchange11Opt: " + e);
        }

        return null;
    }

    /**
 * <p>Tabu version of exchange12 between two trucks using the cost functions defined
 * in ProblemInfo. The method uses calcExchange12Opt and calcExchange21Opt.</p>
 * @param  truck1   first truck used in exchange
 * @param  truck2   second truck used in exchange
 * @param  depot1No depot number of first truck
 * @param  depot2No depot number of second truck
 * @return ExchangesOperation best operation performed, else null
 */
    public ExchangesOperation checkImplementExchange12Opt(Truck truck1,
        Truck truck2, int depot1No, int depot2No) {
        VisitNodesLinkedList p = truck1.mainVisitNodes;
        VisitNodesLinkedList q = truck2.mainVisitNodes;
        int beforeNode1;
        int beforeNode2;
        int afterNode1;
        int afterNode2;
        int noExs = 0;

        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;

        //check to make sure that there are at least 2 trucks in each depot before the exchanges begins
        if ((truck1 == null) || (truck2 == null)) {
            return null;
        }

        try {
            int i;
            int j;
            String curr_str = "12";
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                while (j <= (q.getSize() - 2)) {
                    if ((i <= (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2))) {
                        String s1 = p.calcExchange12Opt(q.pointString(j),
                                p.pointString(i), p.pointString(i + 1));
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index1 = (Integer.valueOf(st1.nextToken())).intValue();
                        float cost2 = Float.MAX_VALUE;
                        int index21 = -1;
                        int index22 = -1;

                        if (index1 != -1) {
                            String s2 = q.calcExchange21Opt(p.pointString(i),
                                    p.pointString(i + 1), q.pointString(j));
                            StringTokenizer st2 = new StringTokenizer(s2, ";");
                            cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                            index21 = (Integer.valueOf(st2.nextToken())).intValue();
                            index22 = (Integer.valueOf(st2.nextToken())).intValue();
                        } else {
                            cost2 = Float.MAX_VALUE; //insertion was not feasible
                        }

                        float cost_diff = cost1 + cost2;
                        String p1 = p.pointString(i);
                        String p2 = p.pointString(i + 1);
                        String q1 = q.pointString(j);
                        beforeNode1 = p.getPoint(i - 1);
                        beforeNode2 = q.getPoint(j - 1);
                        afterNode1 = p.getPoint(i + 2);
                        afterNode2 = q.getPoint(j + 1);
                        currOp = new ExchangesOperation();
                        currOp.setOperation12(truck1, truck2, depot1No,
                            depot2No, cost1, cost2, index1, index21, index22,
                            p1, p2, q1, beforeNode1, beforeNode2, afterNode1,
                            afterNode2);

                        if (cost_diff < 0) {
                            if (ProblemInfo.optType == ZeusConstants.FIRST_FIRST) {
                                // if FIRST_FIRST, execute right away
                                if (ProblemInfo.isUsingTabu) {
                                    if (!ProblemInfo.tabuSearch.isTabu(currOp)) {
                                        executeExchangeOpt(currOp);
                                        ProblemInfo.tabuSearch.updateBestSolution();
                                        noExs++;
                                    }
                                } else {
                                    executeExchangeOpt(currOp);
                                    noExs++;
                                }
                            } else {
                                // if FIRST_BEST or BEST_BEST, find the best exchange out of these two
                                // trucks, and save it in bestOp
                                if (bestOp == null) {
                                    bestOp = currOp;
                                } else if (currOp.changeInCost < bestOp.changeInCost) {
                                    bestOp = currOp;
                                }
                            }
                        }

                        //if cost_diff < 0
                    }

                    //if
                    j++;
                }

                /* inner while */
                i++;
            }

            /* outer while */
            switch (ProblemInfo.optType) {
            case ZeusConstants.FIRST_FIRST:
                setTotal12(getTotal12() + noExs);

                return null;

            case ZeusConstants.FIRST_BEST:

                if (bestOp == null) {
                    return null;
                }

                if (bestOp.changeInCost < 0) {
                    if (ProblemInfo.isUsingTabu) {
                        if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                            executeExchangeOpt(bestOp);
                            setTotal12(getTotal12() + 1); // only 1 exchange was performed, the FIRST_BEST
                            ProblemInfo.tabuSearch.updateBestSolution();
                        }
                    } else {
                        executeExchangeOpt(bestOp);
                        setTotal12(getTotal12() + 1); // only 1 exchange was performed, the FIRST_BEST
                    }
                }

                return null;

            case ZeusConstants.BEST_BEST:
                return bestOp;
            }
        } catch (Exception e) {
            System.err.println("ERROR: checkImplementExchange12Opt: " + e);
        }

        return null;
    }

    /**
 * <p>Tabu version of exchange22 between two trucks using the cost functions defined
 * in ProblemInfo. The method uses calcExchange22Opt.</p>
 * @param  truck1   first truck used in exchange
 * @param  truck2   second truck used in exchange
 * @param  depot1No depot number of first truck
 * @param  depot2No depot number of second truck
 * @return ExchangesOperation best operation performed, else null
 */
    public ExchangesOperation checkImplementExchange22Opt(Truck truck1,
        Truck truck2, int depot1No, int depot2No) {
        boolean isDiagnostic = false;
        int beforeNode1;
        int beforeNode2;
        int afterNode1;
        int afterNode2;
        int noExs = 0;

        VisitNodesLinkedList p = truck1.mainVisitNodes;
        VisitNodesLinkedList q = truck2.mainVisitNodes;

        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;

        //check to make sure that there are at least 2 trucks in each depot before the exchanges begins
        if ((truck1 == null) || (truck2 == null)) {
            return null;
        }

        try {
            int i;
            int j;
            String curr_str = "22";

            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                while (j <= (q.getSize() - 2)) {
                    if ((i <= (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2)) &&
                            (j <= (q.getSize() - 2)) &&
                            ((j + 1) <= (q.getSize() - 2))) {
                        String p1 = p.pointString(i);
                        String p2 = p.pointString(i + 1);
                        String q1 = q.pointString(j);
                        String q2 = q.pointString(j + 1);
                        String s1 = p.calcExchange22Opt(q1, q2, p1, p2);
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index11 = (Integer.valueOf(st1.nextToken())).intValue();
                        int index12 = (Integer.valueOf(st1.nextToken())).intValue();

                        if (isDiagnostic) {
                            System.out.println("\n cost1:  " + cost1 +
                                "  index11:  " + index11 + "   index12:   " +
                                index12 + "\n");
                        }

                        float cost_diff = Float.MAX_VALUE;
                        float cost2 = Float.MAX_VALUE;
                        int index21 = -1;
                        int index22 = -1;

                        if ((index11 != -1) && (index12 != -1)) { //insertion took place for 2 nodes

                            String s2 = q.calcExchange22Opt(p1, p2, q1, q2);
                            StringTokenizer st2 = new StringTokenizer(s2, ";");
                            cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                            index21 = (Integer.valueOf(st2.nextToken())).intValue();
                            index22 = (Integer.valueOf(st2.nextToken())).intValue();

                            if (isDiagnostic) {
                                System.out.println("\n cost2:  " + cost2 +
                                    "  index21:  " + index21 +
                                    "   index22:   " + index22 + "\n");
                            }

                            if ((index21 != -1) && (index22 != -1)) { //insertion took place for 2 nodes
                                cost_diff = cost1 + cost2;
                            }
                        } else {
                            cost_diff = Float.MAX_VALUE;
                        }

                        currOp = new ExchangesOperation();
                        beforeNode1 = p.getPoint(i - 1);
                        beforeNode2 = q.getPoint(j - 1);
                        afterNode1 = p.getPoint(i + 2);
                        afterNode2 = q.getPoint(j + 2);
                        currOp.setOperation22(truck1, truck2, depot1No,
                            depot2No, cost1, cost2, index11, index12, index21,
                            index22, p1, p2, q1, q2, beforeNode1, beforeNode2,
                            afterNode1, afterNode2);

                        if (cost_diff < 0) {
                            if (ProblemInfo.optType == ZeusConstants.FIRST_FIRST) {
                                // if FIRST_FIRST, execute right away
                                if (ProblemInfo.isUsingTabu) {
                                    if (!ProblemInfo.tabuSearch.isTabu(currOp)) {
                                        executeExchangeOpt(currOp);
                                        ProblemInfo.tabuSearch.updateBestSolution();
                                        noExs++;
                                    }
                                } else {
                                    executeExchangeOpt(currOp);
                                    noExs++;
                                }
                            } else {
                                // if FIRST_BEST or BEST_BEST, find the best exchange out of these two
                                // trucks, and save it in bestOp
                                if (bestOp == null) {
                                    bestOp = currOp;
                                } else if (currOp.changeInCost < bestOp.changeInCost) {
                                    bestOp = currOp;
                                }
                            }

                            //else
                        }

                        //cost_diff<0
                    }

                    //if (i <= p.getSize()-2 && i+1 <= p.getSize()-2 && j <= q.getSize()-2 && j+1 <= q.getSize()-2)
                    j++;
                }

                // inner while
                i++;
            }

            // outer while
            switch (ProblemInfo.optType) {
            case ZeusConstants.FIRST_FIRST:
                setTotal22(getTotal22() + noExs);

                return null;

            case ZeusConstants.FIRST_BEST:

                if (bestOp == null) {
                    return null;
                }

                if (bestOp.changeInCost < 0) {
                    if (ProblemInfo.isUsingTabu) {
                        if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                            executeExchangeOpt(bestOp);
                            setTotal22(getTotal22() + 1); // only 1 exchange was performed, the FIRST_BEST
                            ProblemInfo.tabuSearch.updateBestSolution();
                        }
                    } else {
                        executeExchangeOpt(bestOp);
                        setTotal22(getTotal22() + 1); // only 1 exchange was performed, the FIRST_BEST
                    }
                }

                return null;

            case ZeusConstants.BEST_BEST:
                return bestOp;
            }
        } catch (Exception e) {
            System.err.println("ERROR: checkImplementExchange22Opt: " + e);
        }

        return null;
    }

    /**
 * <p>Exchange nodes between truck routes in one single depot using the cost function
 * in ProblemInfo.</p>
 * @param  ExchangeType type of exchange to be performed
 * @param  depot depot with the trucks for exchange
 *
 */
    public void exchangeOneDepotOpt(int ExchangeType, Depot depot) {
        SelectExchange selectExchange = new SelectExchange();

        int depot1No;
        int depot2No;
        depot1No = depot2No = depot.depotNo;

        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;
        Truck truck1;
        Truck truck2;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if (getNoTrucks() < 2) {
            return;
        }

        //set the number of exchanges to 0
        selectExchange.setTotals(ExchangeType, 0, true);
        truck1 = this.first;

        while ((truck1 != null) && (truck1.next != null)) {
            truck2 = truck1.next;
            currOp = selectExchange.callExchange(ExchangeType, truck1, truck2,
                    depot1No, depot2No);

            if ((currOp != null) &&
                    (ProblemInfo.optType == ZeusConstants.BEST_BEST)) {
                // if BEST_BEST, keep track of best operation to perform at the end of
                // the two while loops, if it's FIRST_FIRST or FIRST_BEST, it will be
                // performed inside checkExchange12().
                if (bestOp == null) {
                    bestOp = currOp;
                } else if (currOp.changeInCost < bestOp.changeInCost) {
                    bestOp = currOp;
                }
            }

            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
            truck1 = truck1.next;
        }

        //if cyclical move is required, move nodes between the first truck and the last
        //truck.
        if ((first != null) && (truck1 != null) && (truck1.next == null)) {
            truck2 = truck1;
            truck1 = first;
            currOp = selectExchange.callExchange(ExchangeType, truck1, truck2,
                    depot1No, depot2No);

            if ((currOp != null) &&
                    (ProblemInfo.optType == ZeusConstants.BEST_BEST)) {
                // if BEST_BEST, keep track of best operation to perform at the end of
                // the two while loops, if it's FIRST_FIRST or FIRST_BEST, it will be
                // performed inside checkExchange12().
                if (bestOp == null) {
                    bestOp = currOp;
                } else if (currOp.changeInCost < bestOp.changeInCost) {
                    bestOp = currOp;
                }
            }

            ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
        }

        // perform BEST_BEST if necessary
        if ((bestOp != null) &&
                (ProblemInfo.optType == ZeusConstants.BEST_BEST)) {
            if (ProblemInfo.isUsingTabu) {
                if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                    executeExchangeOpt(bestOp);
                    selectExchange.setTotals(ExchangeType, 1, false); // one more exchange performed here (i.e. BEST_BEST)
                    ProblemInfo.tabuSearch.updateBestSolution();
                }
            } else {
                executeExchangeOpt(bestOp);
                selectExchange.setTotals(ExchangeType, 1, false); // one more exchange performed here (i.e. BEST_BEST)
            }
        }

        ProblemInfo.truckLLLevelCostF.calculateTotalsStats(this);
    }

    /**
 * <p>Exchange nodes between truck routes in multiple depots using the cost function
 * in ProblemInfo.</p>
 * @param  ExchangeType type of exchange to be performed
 * @param  depot1 first  depot with the trucks for exchange
 * @param  depot2 second depot with the trucks for exchange
 *
 */
    public void exchangeMultDepotOpt(int ExchangeType, Depot depot1,
        Depot depot2) {
        SelectExchange selectExchange = new SelectExchange();
        int depot1No;
        int depot2No;
        depot1No = depot1.depotNo;
        depot2No = depot2.depotNo;

        ExchangesOperation currOp = null;
        ExchangesOperation bestOp = null;
        Truck truck1;
        Truck truck2;

        //check to make sure that there are at least 2 trucks before the exchanges begin
        if ((depot1.mainTrucks.getNoTrucks() + depot2.mainTrucks.getNoTrucks()) < 2) {
            return;
        }

        //set the number of exchanges to 0
        selectExchange.setTotals(ExchangeType, 0, true);

        truck1 = depot1.mainTrucks.first;

        while (truck1 != null) {
            truck2 = depot2.mainTrucks.first;

            while (truck2 != null) {
                currOp = selectExchange.callExchange(ExchangeType, truck1,
                        truck2, depot1No, depot2No);

                if ((currOp != null) &&
                        (ProblemInfo.optType == ZeusConstants.BEST_BEST)) {
                    // if BEST_BEST, keep track of best operation to perform at the end of
                    // the two while loops, if it's FIRST_FIRST or FIRST_BEST, it will be
                    // performed inside checkExchange12().
                    if (bestOp == null) {
                        bestOp = currOp;
                    } else if (currOp.changeInCost < bestOp.changeInCost) {
                        bestOp = currOp;
                    }
                }

                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(depot1.mainTrucks);
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(depot2.mainTrucks);
                truck2 = truck2.next;
            }

            truck1 = truck1.next;
        }

        // perform BEST_BEST if necessary
        if ((bestOp != null) &&
                (ProblemInfo.optType == ZeusConstants.BEST_BEST)) {
            if (ProblemInfo.isUsingTabu) {
                if (!ProblemInfo.tabuSearch.isTabu(bestOp)) {
                    executeExchangeOpt(bestOp);
                    selectExchange.setTotals(ExchangeType, 1, false); // one more exchange performed here (i.e. BEST_BEST)
                    ProblemInfo.tabuSearch.updateBestSolution();
                }
            } else {
                executeExchangeOpt(bestOp);
                selectExchange.setTotals(ExchangeType, 1, false); // one more exchange performed here (i.e. BEST_BEST)
            }
        }

        ProblemInfo.truckLLLevelCostF.calculateTotalsStats(depot1.mainTrucks);
        ProblemInfo.truckLLLevelCostF.calculateTotalsStats(depot2.mainTrucks);
    }

    /**
 * <p>Central hub to choose specific exchange using the cost function
 * in ProblemInfo.</p>
 * @param  op type of operation to be executed
 *
 */
    public void executeExchangeOpt(ExchangesOperation op) {
        switch (op.exchangeType) {
        case ZeusConstants.EXCHANGE_01:
            executeExchange01Opt(op.p, op.q, op.index2, op.p1);

            break;

        case ZeusConstants.EXCHANGE_02:
            executeExchange02Opt(op.p, op.q, op.index21, op.index22, op.p1,
                op.p2);

            break;

        case ZeusConstants.EXCHANGE_11:
            executeExchange11Opt(op.p, op.q, op.p1, op.q1, op.index1, op.index2);

            break;

        case ZeusConstants.EXCHANGE_12:
            executeExchange12Opt(op.p, op.q, op.p1, op.p2, op.q1, op.index1,
                op.index21, op.index22);

            break;

        case ZeusConstants.EXCHANGE_22:
            executeExchange22Opt(op.p, op.q, op.p1, op.p2, op.q1, op.q2,
                op.index11, op.index12, op.index21, op.index22);

            break;
        }
    }

    /**
 * <p>Tabu version of executeExchange01 between two trucks p and q using the cost functions defined
 * in ProblemInfo. The method uses exchange01Opt and exchange10Opt and local optimizations
 * oneOpt and twoOpt.</p>
 * @param  p  first truck used in exchange
 * @param  q  second truck used in exchange
 * @param  p1 shipment to be removed from p
 * @param  index2 location shipment p1 is to be inserted in q
 */
    public void executeExchange01Opt(VisitNodesLinkedList p,
        VisitNodesLinkedList q, int index2, String p1) {
        p.exchange01Opt(p1);
        q.exchange10Opt(p1, index2);
        p.localOneOpt();
        p.localTwoOpt();
        q.localOneOpt();
        q.localTwoOpt();
    }

    /**
 * <p>Tabu version of executeExchange02 between two trucks p and q using the cost functions defined
 * in ProblemInfo. The method uses exchange02Opt and exchange20Opt and local optimizations
 * oneOpt and twoOpt.</p>
 * @param  p       first truck used in exchange
 * @param  q       second truck used in exchange
 * @param  index21 best location to insert shipment p1 in q
 * @param  index22 best location to insert shipment p2 in q
 * @param  p1      shipment to be removed from p
 * @param  p2      shipment to be removed from p
 */
    public void executeExchange02Opt(VisitNodesLinkedList p,
        VisitNodesLinkedList q, int index21, int index22, String p1, String p2) {
        p.exchange02Opt(p1, p2);
        q.exchange20Opt(p1, index21, p2, index22);
        p.localOneOpt();
        p.localTwoOpt();
        q.localOneOpt();
        q.localTwoOpt();
    }

    /**
 * <p>Tabu version of executeExchange11 between two trucks p and q using the cost functions defined
 * in ProblemInfo. The method uses exchange11Opt and local optimizations
 * oneOpt and twoOpt.</p>
 * @param  p       first truck used in exchange
 * @param  q       second truck used in exchange
 * @param  p1      shipment to be removed from p
 * @param  q1      shipment to be removed from q
 * @param  index1  best location to insert shipment q1 in p
 * @param  index2  best location to insert shipment p1 in q
 *
 */
    public void executeExchange11Opt(VisitNodesLinkedList p,
        VisitNodesLinkedList q, String p1, String q1, int index1, int index2) {
        p.exchange11Opt(p1, q1, index1);
        q.exchange11Opt(q1, p1, index2);
        p.localOneOpt();
        p.localTwoOpt();
        q.localOneOpt();
        q.localTwoOpt();
    }

    /**
 * <p>Tabu version of executeExchange12 between two trucks p and q using the cost functions defined
 * in ProblemInfo. The method uses exchange12Opt and exchange21Opt and local optimizations
 * oneOpt and twoOpt.</p>
 * @param  p       first truck used in exchange
 * @param  q       second truck used in exchange
 * @param  p1      shipment to be removed from p
 * @param  p2      shipment to be removed from p
 * @param  q1      shipment to be removed from q
 * @param  index1  best location to insert shipment q1 in p
 * @param  index21  best location to insert shipment p1 in q
 * @param  index22 best location to insert shipment p2 in q
 *
 */
    public void executeExchange12Opt(VisitNodesLinkedList p,
        VisitNodesLinkedList q, String p1, String p2, String q1, int index1,
        int index21, int index22) {
        p.exchange12Opt(q1, index1, p1, p2);
        q.exchange21Opt(p1, index21, p2, index22, q1);
        p.localOneOpt();
        p.localTwoOpt();
        q.localOneOpt();
        q.localTwoOpt();
    }

    /**
 * <p>Tabu version of executeExchange22 between two trucks p and q using the cost functions defined
 * in ProblemInfo. The method uses exchange22Opt and local optimizations
 * oneOpt and twoOpt.</p>
 * @param  p       first truck used in exchange
 * @param  q       second truck used in exchange
 * @param  p1      shipment to be removed from p
 * @param  p2      shipment to be removed from p
 * @param  q1      shipment to be removed from q
 * @param  q2      shipment to be removed from q
 * @param  index11 best location to insert shipment q1 in p
 * @param  index12 best location to insert shipment q2 in p
 * @param  index21 best location to insert shipment p1 in q
 * @param  index22 best location to insert shipment p2 in q
 *
 */
    public void executeExchange22Opt(VisitNodesLinkedList p,
        VisitNodesLinkedList q, String p1, String p2, String q1, String q2,
        int index11, int index12, int index21, int index22) {
        p.exchange22Opt(q1, index11, q2, index12, p1, p2);
        q.exchange22Opt(p1, index21, p2, index22, q1, q2);
        p.localOneOpt();
        p.localTwoOpt();
        q.localOneOpt();
        q.localTwoOpt();
    }

    /**
 * <p>Updates truck numbers after deletion of a truck</p>
 */
    public void updateTruckNo() {
        Truck current = first;
        int no = 0;

        while (current != null) {
            current.truckNo = no++;
            current = current.next;
        }
    }

    /**
 * <p>Central hub to select specific checkImplementExchangeOpt</p>
 */
    class SelectExchange {
        public ExchangesOperation callExchange(int ExchangeType, Truck truck1,
            Truck truck2, int depot1No, int depot2No) {
            ExchangesOperation newOp = null;

            switch (ExchangeType) {
            case ZeusConstants.EXCHANGE_01:
                newOp = checkImplementExchange01Opt(truck1, truck2, depot1No,
                        depot2No);

                break;

            case ZeusConstants.EXCHANGE_02:
                newOp = checkImplementExchange02Opt(truck1, truck2, depot1No,
                        depot2No);

                break;

            case ZeusConstants.EXCHANGE_10:
                newOp = checkImplementExchange01Opt(truck2, truck1, depot2No,
                        depot1No);

                break;

            case ZeusConstants.EXCHANGE_11:
                newOp = checkImplementExchange11Opt(truck1, truck2, depot1No,
                        depot2No);

                break;

            case ZeusConstants.EXCHANGE_12:
                newOp = checkImplementExchange12Opt(truck1, truck2, depot1No,
                        depot2No);

                break;

            case ZeusConstants.EXCHANGE_20:
                newOp = checkImplementExchange02Opt(truck2, truck1, depot2No,
                        depot1No);

                break;

            case ZeusConstants.EXCHANGE_21:
                newOp = checkImplementExchange12Opt(truck2, truck1, depot2No,
                        depot1No);

                break;

            case ZeusConstants.EXCHANGE_22:
                newOp = checkImplementExchange22Opt(truck1, truck2, depot1No,
                        depot2No);

                break;
            }

            return newOp;
        }

        /**
 * <p>Single method to update setTotal for specific exchange - use in tandem
 * with checkImplementExchange methods</p>
 * @param ExchangeType type of exchange
 * @param numExchanges total number of exchanges
 * @param reset flag to reset
 */
        public void setTotals(int ExchangeType, int numExchanges, boolean reset) {
            switch (ExchangeType) {
            case ZeusConstants.EXCHANGE_01:
            case ZeusConstants.EXCHANGE_10:

                if (reset) {
                    setTotal01(0);
                } else {
                    setTotal01(getTotal01() + numExchanges);
                }

                break;

            case ZeusConstants.EXCHANGE_02:
            case ZeusConstants.EXCHANGE_20:

                if (reset) {
                    setTotal02(0);
                } else {
                    setTotal02(getTotal02() + numExchanges);
                }

                break;

            case ZeusConstants.EXCHANGE_11:

                if (reset) {
                    setTotal11(0);
                } else {
                    setTotal11(getTotal11() + numExchanges);
                }

                break;

            case ZeusConstants.EXCHANGE_12:
            case ZeusConstants.EXCHANGE_21:

                if (reset) {
                    setTotal12(0);
                } else {
                    setTotal12(getTotal12() + numExchanges);
                }

                break;

            case ZeusConstants.EXCHANGE_22:

                if (reset) {
                    setTotal22(0);
                } else {
                    setTotal22(getTotal22() + numExchanges);
                }

                break;
            }
        }
    }

    //============================================================================================================
    //========================= END (TABU) FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =========================
    //============================================================================================================
}


//TruckLinkedList class
