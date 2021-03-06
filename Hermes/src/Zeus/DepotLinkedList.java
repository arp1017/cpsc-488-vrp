package Zeus;

import java.io.*; //input-output java package


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: This class implements the DepotLinkedList class. The Root class creates
 * an instance of a problem, like MDVRP, class. The problem, like MDVRP, class creates an instance
 * of the DepotLinkedList class. If the problem has only one depot then the DepotLinkedList class
 * will have only one instance of the Depot class, else it will have a linked list of multple
 * instances of the Depot class. </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class DepotLinkedList implements java.io.Serializable {
    private Depot first; //head of the linked list
    private Depot last; //tail of the linked list
    public int noDepots; //total number of depots in the problem
    public int totalNonEmptyTrucks; //total number of non-empty trucks in the depots
    public float totalCost; // total cost of the schedule
    public float totalDemand; //total demand of all trucks in the depots
    public float totalDistance; //total duration of all trucks in the depots

    /********************/
    public float totalTardiness; //total tardiness of all the nodes in the depots
    public float totalOverload; //total overload of all the nodes in the depots
    public float totalExcessTime; //total excess time of all the nodes in the depots
    public float totalWaitTime; //total wait time of all the nodes in the depots
    public float totalTotalTravelTime; //total travel time of all the nodes in the depots
    public float totalServiceTime; //total service time of all the nodes in the depots

    //total number of exchanges and relocations that took place
    int totalvC1T;

    //total number of exchanges and relocations that took place
    int total2C1T;

    //total number of exchanges and relocations that took place
    int total3C1T;

    //total number of exchanges and relocations that took place
    int total2AC1T;

    //total number of exchanges and relocations that took place
    int total3AC1T;

    //total number of exchanges and relocations that took place
    int totalvAC1T;

    //total number of exchanges and relocations that took place
    int total2C2T;

    //total number of exchanges and relocations that took place
    int totalvC2T;

    //total number of local opts executed
    int totalOneOpt;

    //total number of local opts executed
    int totalTwoOpt;

    //total number of local opts executed
    int totalThreeOpt;

    //total number of local opts executed
    int totalKTwoOpt;

    //total number of local opts executed
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
    * Constructor for depot linked list. Sets the first and last
    * pointer to null.
    */
    public DepotLinkedList() {
        //initialize the depot linked list's first and
        //last pointers
        first = null;
        last = null;
    }

    /**
    * Check if the linked list is empty
    * @return boolean true if empty, false otherwise
    */
    public boolean isEmpty() {
        return (first == null);
    }

    /**
    * Insert a Depot into the first location of the linked list.
    * @param thisDepot depot to be inserted into the linked list
    * @return Depot pointer to the linked list
    */
    public Depot insertFirst(Depot thisDepot) {
        boolean isDiagnostic = false;
        Depot theDepot = thisDepot;

        if (isEmpty()) {
            last = theDepot;
        }

        theDepot.next = first;
        first = theDepot;

        if (isDiagnostic) {
            System.out.println("inserted " + theDepot.getDepotNo());
        }

        return first; //return the pointer to the added node
    }

    /**
    * Insert the Depot into the last location of the linked list.
    * @param thisDepot depot to be inserted into the linked list
    * @return Depot pointer to the linked list
    */
    public Depot insertLast(Depot thisDepot) {
        boolean isDiagnostic = false;
        Depot theDepot = thisDepot;

        if (isEmpty()) {
            first = theDepot;
        } else {
            last.next = theDepot;
        }

        last = theDepot;

        //Diagnostic
        if (isDiagnostic) {
            System.out.println("inserted " + theDepot.getDepotNo());
        }

        return last; //return the pointer to the added node
    }

    /**
    * Delete the first Depot from the linked list
    * @return Depot pointer to the linked list
    */
    public Depot deleteFirst() {
        Depot temp = first;

        if (first == null) { // there are no depots

            return null;
        } else if (first.next == null) { //if there is only one depot
            last = null;
        } else {
            first = first.next;
            temp.next = null; //ground pointer
        }

        return temp;
    }

    /**
    * find a Depot in the linked list with Depot number key
    * @param key unique id of the depot
    * @return Depot pointer to the depot with the id, else null
    */
    public Depot find(int key) {
        Depot current = first;

        while (current.depotNo != key) {
            if (current.next == null) {
                return null;
            } else {
                current = current.next;
            }
        }

        return current;
    }

    /**
    * Finds depot based on the index from the first element in the linked list
    * @param index depot to find
    * @return pointer to the depot found
    */
    public Depot findByTraversal(int index) {
        Depot temp = this.getFirst();

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
    * Delete a Depot from the linked list with Depot number key
    * @param key unique id of the depot
    * @return Depot pointer to the deleted depot with the id, else null
    */
    public Depot delete(int key) {
        Depot current = first;
        Depot previous = first;

        if (isEmpty()) {
            return null;
        } else {
            while (current.depotNo != key) {
                if (current.next == null) {
                    return null; //can't find Depot
                } else {
                    previous = current;
                    current = current.next;
                }
            }

            //end while
            if (current == first) {
                first = current.next;
            } else {
                previous.next = current.next;
            }

            if (current == last) {
                if (first == null) { //only one node, will be deleted
                    last = null;
                } else {
                    last = previous;
                }
            }

            current.next = null;

            return current;
        }

        //end outer else
    }

    //end delete

    /**
    * Display the linked list of Depots. Each depot is called with the displayDepot
    * method.
    */
    public void displayList() {
        System.out.println("List (first to last): ");

        Depot current = first;

        while (current != null) {
            current.displayDepot();
            current = current.next;
        }

        System.out.println("");
    }

    /**
    * Display all the information in the the linked list of Depots. Each depot is
    * called with the displayDepot method followed by each the instance of the
    * TruckLinkedList mainTrucks being called with the displayAllForwardList method.
    */
    public void displayAllList() {
        System.out.println("List (first to last): ");

        Depot current = first;

        while (current != null) {
            current.displayDepot();
            current.mainTrucks.displayAllForwardList();
            current = current.next;
        }

        System.out.println("");
    }

    /**
    * Display all the information in the the linked list of Depots for the MDVRP
    * problem. Each depot is called with the displayDepotMDVRP method followed by
    * each the instance of the TruckLinkedList mainTrucks being called with the
    * displayForwardKeyListMDVRP method.
    */
    public void displayForwardKeyListMDVRP() {
        //System.out.println("List (first to last): ");
        Depot current = first;

        while (current != null) {
            current.displayDepotMDVRP();
            current.mainTrucks.displayForwardKeyListMDVRP();
            current = current.next;
        }

        System.out.println("");
    }

    /**
    * Display all the information in the the linked list of Depots for the VRPTW
    * problem. Each depot is called with the displayDepotVRPTW method followed by
    * each the instance of the TruckLinkedList mainTrucks being called with the
    * displayForwardKeyListVRPTW method.
    * This method added 9/15/03 by Mike McNamara
    */
    public void displayForwardKeyListVRPTW() {
        Depot current = first;

        while (current != null) {
            current.displayDepotVRPTW();
            current.mainTrucks.displayForwardKeyListVRP();
            current = current.next;
        }

        System.out.println("");
    }

    /**
    * Display the uniqe id's of all the depots in the linked list
    */
    public void printDepotNos() {
        int noCount = 0;
        Depot current = first;

        while (current != null) {
            System.out.print(current.getDepotNo() + " ");
            noCount++;

            if ((noCount % 10) == 0) {
                System.out.println("");
            }

            current = current.next;
        }

        System.out.println("");
    }

    /**
    * Locally optimize each of the routes in the depot. This method takes the
    * depot and applied OneOpt, twoOpt, ThreeOpt, KTwoOpt and KThreeOpt to each
    * of the trucks in the depot.
    */
    public void localOpt() {
        Depot tempDepot = first;

        //initialize the opt values
        totalOneOpt = 0;
        totalTwoOpt = 0;
        totalThreeOpt = 0;
        totalKTwoOpt = 0;
        totalKThreeOpt = 0;
        tempDepot.mainTrucks.setTotalOneOpt(0);
        tempDepot.mainTrucks.setTotalTwoOpt(0);
        tempDepot.mainTrucks.setTotalThreeOpt(0);
        tempDepot.mainTrucks.setTotalKTwoOpt(0);
        tempDepot.mainTrucks.setTotalKThreeOpt(0);

        while (tempDepot != null) //loop through all the depots
         {
            //perform the local optimization
            tempDepot.mainTrucks.localOpt();

            //get total number of moves executed
            totalOneOpt += tempDepot.mainTrucks.getTotalOneOpt();
            totalTwoOpt += tempDepot.mainTrucks.getTotalTwoOpt();
            totalThreeOpt += tempDepot.mainTrucks.getTotalThreeOpt();
            totalKTwoOpt += tempDepot.mainTrucks.getTotalKTwoOpt();
            totalKThreeOpt += tempDepot.mainTrucks.getTotalKThreeOpt();

            //move to the next depot
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
        }
    }

    /* localOpt() */

    /**
    * Exchange 01 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot01() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot01();

            //compute the number of exchanges that took place
            total01 += tempDepot.mainTrucks.getTotal01();
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
        }

        System.out.println("Total number of 01 exchanges is " + total01);
    }

    /* exchangeOneDepot01() */

    /**
    * Exchange 10 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot10() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot01();

            //compute the number of exchanges that took place - same as 01
            total01 += tempDepot.mainTrucks.getTotal01();
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
        }

        System.out.println("Total number of 10 exchanges is " + total01);
    }

    /* exchangeOneDepot01() */

    /**
    * Exchange 11 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot11() {
        Depot tempDepot = first;
        total11 = 0;

        while (tempDepot != null) //loop through all the depots
         {
            //execute the 11 exchange
            tempDepot.mainTrucks.exchangeOneDepot11();

            //total total number of exchanges for the depot
            total11 += tempDepot.mainTrucks.getTotal11();
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();

            //compute the number of exchanges that took place
        }

        System.out.println("Total number of 11 exchanges is " + total11);
    }

    /* exchangeOneDepot11() */

    /**
    * Exchange 12 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot12() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot12();

            //compute the number of exchanges that took place
            total12 += tempDepot.mainTrucks.getTotal12();

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
            tempDepot = tempDepot.next;
        }

        System.out.println("Total number of 12 exchanges is " + total12);
    }

    /* exchangeOneDepot12() */

    /**
    * Exchange 21 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot21() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot21();

            //compute the number of exchanges that took place
            total12 += tempDepot.mainTrucks.getTotal12();

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
            tempDepot = tempDepot.next;
        }

        System.out.println("Total number of 21 exchanges is " + total12);
    }

    /* exchangeOneDepot21() */

    /**
    * Exchange 02 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot02() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot02();

            //compute the number of exchanges that took place
            total02 += tempDepot.mainTrucks.getTotal02();
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
        }

        System.out.println("Total number of 02 exchanges is " + total02);
    }

    /* exchangeOneDepot02() */

    /**
    * Exchange 20 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot20() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot20();

            //compute the number of exchanges that took place
            total02 += tempDepot.mainTrucks.getTotal02();
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
        }

        System.out.println("Total number of 20 exchanges is " + total02);
    }

    /* exchangeOneDepot21() */

    /**
    * Exchange 22 nodes between the different trucks in a single depot
    */
    public void exchangeOneDepot22() {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.exchangeOneDepot22();

            //compute the number of exchanges that took place
            total22 += tempDepot.mainTrucks.getTotal22();
            tempDepot = tempDepot.next;

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            calculateTotalCapacityMDVRP();
            calculateTotalDistanceMDVRP();
            calculateTotalNonEmptyTrucksMDVRP();
        }

        System.out.println("Total number of 22 exchanges is " + total22);
    }

    /* exchangeOneDepot22() */

    /**
    * MDVRP -Add a shipment for the MDVRP problem. The instance of the depot
    * is identified by the depotNo and the shipment to be added is thisShip. The
    * shipment is added to the mainTrucks instance of the depot
    * @param thisShip shipment to be added
    * @param depotNo id for the depot to which the shipment is added
    * @return boolean true if shipment was successfully added, false otherwise
    * */
    public boolean insertShipmentMDVRP(Shipment thisShip, int depotNo) {
        boolean isDiagnostic = false;
        Depot tempDepot;
        boolean status;

        //For the MDVRP the shipment is added to the first avaliable truck in the
        //depot.  If the truck cannot accept the shipment, then a new truck is added
        //to the depot and the shipment is added to the new truck
        tempDepot = find(depotNo); //find the depot number with key
        status = tempDepot.mainTrucks.insertShipMDVRP(thisShip);

        if (status == false) //shipment could not be inserted
         {
            //create a truck and insert the shipment
            tempDepot = find(depotNo); //find the depot number with keyDepot.depotNo,thisDepot.maxDuration,thisDepot.maxCapacity);
            insertTruck(depotNo, tempDepot.maxDuration, tempDepot.maxCapacity);
            status = tempDepot.mainTrucks.insertShipMDVRP(thisShip); //find the depot number with tempDepot.mainTrucks.insertShipMDVRP(thisShip);

            //if status is false, something is wrong
            if (status == false) {
                if (isDiagnostic) {
                    System.out.println(
                        "DepotLinkedList: InsertShipmentMDVRP - shipment could not in inserted");
                }
            }
        }

        //compute the noTrucks, distance and capacity for the current depot
        calculateTotalCapacityMDVRP();
        calculateTotalDistanceMDVRP();
        calculateTotalNonEmptyTrucksMDVRP();

        return status;
    }

    /**
    * Depot level insert function for the VRP
    * @param theShip shipment to insert
    * @param depotNo depot to insert the shipment in
    * @return true-shipment inserted false-shipment not inserted
    */
    public boolean insertShipmentVRP(Shipment theShip, int depotNo) {
        boolean isDiagnostic = false;
        Depot tempDepot;
        boolean status;

        //get the depot
        tempDepot = this.find(depotNo);

        if (tempDepot != null) {
            status = tempDepot.mainTrucks.insertShipVRPTW(theShip);

            if (status == false) {
                //could not be inserted, create new truck
                // create truck and insert
                this.insertTruck(depotNo, tempDepot.getMaxDuration(),
                    tempDepot.getMaxCapacity());
                status = tempDepot.mainTrucks.insertShipVRPTW(theShip);

                //still could not be inserted, print error
                if (status == false) {
                    System.err.println("Shipment could not be inserted");

                    return false;
                }
            }

            //calculate information for the depot
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        } else {
            System.err.println("Depot No. " + depotNo +
                " not found, shipment not inserted");

            return false;
        }

        return status;
    }

    /**
    * Depot level insert function for the VRPTW
    * @param theShip shipment to insert
    * @param depotNo depot to insert the shipment in
    * @return true-shipment inserted false-shipment not inserted
    */
    public boolean insertShipmentVRPTW(Shipment theShip, int depotNo) {
        boolean isDiagnostic = true;
        Depot tempDepot;
        boolean status;

        //get the depot
        tempDepot = this.find(depotNo);

        if (tempDepot != null) {
            status = tempDepot.mainTrucks.insertShipVRPTW(theShip);

            if (status == false) {
                //could not be inserted, create new truck
                // create truck and insert
                this.insertTruck(depotNo, tempDepot.getMaxDuration(),
                    tempDepot.getMaxCapacity());
                status = tempDepot.mainTrucks.insertShipVRPTW(theShip);

                //still could not be inserted, print error
                if (status == false) {
                    System.err.println("Shipment could not be inserted");

                    return false;
                }
            }

            //calculate information for the depot
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        } else {
            System.err.println("Depot No. " + depotNo +
                " not found, shipment not inserted");

            return false;
        }

        return status;
    }

    /**
    * Insert an empty truck into the depot identified in depotNo
    * @param depotNo id for the depot to which the shipment is added
    * @param maxTravelTime maximum duration or travel time for the truck
    * @param maxCapacity maximum capacity of the truck
    * */
    public void insertTruck(int depotNo, float maxTravelTime, float maxCapacity) {
        boolean isDiagnostic = false;
        boolean status;
        float startX; //starting and ending depot coordinates
        float startY; //starting and ending depot coordinates
        float endX; //starting and ending depot coordinates
        float endY; //starting and ending depot coordinates
        Depot tempDepot;
        tempDepot = find(depotNo); //find the depot number with key

        if (isDiagnostic) {
            System.out.println("Inserting a truck into depot number " +
                depotNo);
        }

        //when inserting a new truck into the linked list the depotNo, staring and
        //ending location of the depot, the maximum capacity and the maximum travel time
        //have to be considered at least for an MDVRP
        startX = tempDepot.x;
        startY = tempDepot.y;
        endX = tempDepot.x;
        endY = tempDepot.y;
        tempDepot.mainTrucks.insertTruck(depotNo, startX, startY, endX, endY,
            maxTravelTime, maxCapacity);
    }

    /**
    * Create depots from the ShipmentLinked list given the number of
    * depots.  The shipment list maintains the total number of depots,
    * shipments, max capacity and duration for a homogeneous problem. That
    * is, the characteristics of all the depots are uniform.
    * @param shipList Linked list containing information on the depots
    * */
    public void createDepots(ShipmentLinkedList shipList) {
        Depot tempDepot;

        for (int i = 1; i <= shipList.getShipDepots(); i++) {
            //pass depot number, shipment linked list and the total number of customers
            tempDepot = insertLast(createADepot(i, shipList));
            tempDepot.setMaxDuration(shipList.maxDuration);
            tempDepot.setMaxCapacity(shipList.maxCapacity);

            //for each of the depot with depot number i
            //add an empty truck for the MDVRP problem
            //All trucks are assumed to be homogeneous
            //call with the depot number, duration of truck and capacity of truck
            insertTruck(tempDepot.getDepotNo(), shipList.maxDuration,
                shipList.maxCapacity);
        }
    }

    /**
    * Get the total number of non-empty trucks for the depot
    * @return int number of non-empty trucks for the depot
    * */
    public int getTotalNonEmptyTrucksMDVRP() {
        //calculate the total trucks for each depot
        //calculateTotalNonEmptyTrucksMDVRP();
        return totalNonEmptyTrucks;
    }

    /**
    * Get the total number of non-empty trucks for the depot
    * This method added 8/30/03 by Mike McNamara
    * Not used for VRPTW problem
    * @return int number of non-empty trucks for the depot
    * */
    public int getTotalNonEmptyTrucksVRP() {
        return totalNonEmptyTrucks;
    }

    /**
    * Set the total number of non-empty trucks for the depot
    * @param trucks number of non-empty trucks for the depot
    * @return int number of non-empty trucks for the depot
    * */
    public int setTotalNonEmptyTrucksMDVRP(int trucks) {
        //calculate the total non-empty trucks for each depot
        totalNonEmptyTrucks = trucks;

        return totalNonEmptyTrucks;
    }

    /**
    * Set the total number of non-empty trucks for the depot
    * This method added 8/30/03 by Mike McNamara
    * Not used for the VRPTW problem
    * @param trucks number of non-empty trucks for the depot
    * @return int number of non-empty trucks for the depot
    * */
    public int setTotalNonEmptyTrucksVRP(int trucks) {
        totalNonEmptyTrucks = trucks;

        return totalNonEmptyTrucks;
    }

    /**
    * Calculate the total number of trucks in the depot for the MDVRP problem. When trucks are added
    * to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * */
    public void calculateTotalNonEmptyTrucksMDVRP() {
        Depot current = first;
        int totalTrucks = 0;

        while (current != null) {
            current.mainTrucks.calculateTotalNonEmptyTrucks();
            totalTrucks += current.mainTrucks.getTotalNonEmptyTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalTrucks(totalTrucks);
            current = current.next;
        }

        setTotalNonEmptyTrucksMDVRP(totalTrucks);
    }

    /**
    * Calculate the total number of trucks in the depot. When trucks are added
    * to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * */
    public void calculateTotalNonEmptyTrucks() {
        Depot current = first;
        int totalTrucks = 0;

        while (current != null) {
            current.mainTrucks.calculateTotalNonEmptyTrucks();
            totalTrucks += current.mainTrucks.getTotalNonEmptyTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalTrucks(totalTrucks);
            current = current.next;
        }

        setTotalNonEmptyTrucksMDVRP(totalTrucks);
    }

    /**
    * Calculate the total number of trucks in the depot for the VRP problem. When trucks are added
    * to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * This method added 8/30/03 by Mike McNamara
    * Not used for the VRPTW problem
    */
    public void calculateTotalNonEmptyTrucksVRP() {
        Depot current = first;
        int totalTrucks = 0;

        while (current != null) {
            current.mainTrucks.calculateTotalNonEmptyTrucks();
            totalTrucks += current.mainTrucks.getTotalNonEmptyTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalTrucks(totalTrucks);
            current = current.next;
        }

        setTotalNonEmptyTrucksVRP(totalTrucks);
    }

    /**
    * Increment the total capacity/weight of the depot, consisting of trucks,
    * serviced when a shipment is added a truck in the depot. When a new shipment is added to a truck,
    * the total capacity of the depot needs to be incremented by the capacity of the added truck.
    * @param incWeight capacity/weight by which totalDemand is to be incremented.
    * @return float total capacity/weight of the depot
    * */
    public float incTotalCapacity(float incWeight) {
        totalDemand = totalDemand + incWeight;

        return totalDemand;
    }

    /**
    * Decrement the total capacity/weight of the depot, consisting of trucks,
    * serviced when a shipment is added to a truck in the depot. When a new shipment is added to a truck,
    * the total capacity of the depot needs to be incremented by the capacity of the added shipment.
    * @param decWeight capacity/weight by which totalDemand is to be decremented
    * @return float total capacity/weight of the truck
    * */
    public float decCurrentCapacity(float decWeight) {
        totalDemand = totalDemand - decWeight;

        return totalDemand;
    }

    /**
    * Get the total demand or weight of all the trucks for current depot
    * @return float total demand or weight of all trucks in current depot
    * */
    public float getTotalDemandMDVRP() {
        //calculate the total trucks for each depot
        //calculateTotalDemandMDVRP();
        //sum up the demand of trucks in each depot
        return totalDemand;
    }

    /**
    * Get the total demand or weight of all the trucks for depot
    * @return float total demand or weight of all trucks in depot
    * */
    public float getTotalDemandVRP() {
        return totalDemand;
    }

    /**
    * Set the total demand or weight of all the trucks for current depot
    * @param demand the total demand or weight of all trucks
    * @return total demand or weight of trucks
    * */
    public float setTotalDemandOfMDVRP(float demand) {
        totalDemand = demand;

        //sum up the demand of trucks in each depot
        return totalDemand;
    }

    /**
    * Set the total demand or weight of all the trucks for current depot
    * This method added 8/30/03 by Mike McNamara
    * Not used for the VRPTW problem
    * @param demand the total demand or weight of all trucks
    * @return total demand or weight of trucks
    */
    public float setTotalDemandOfVRP(float demand) {
        totalDemand = demand;

        //sum up the demand of trucks in each depot
        return totalDemand;
    }

    /**
    * Returns a pointer to the first depot in the linked list
    * @return first depot
    */
    public Depot getFirst() {
        return first;
    }

    /**
    * Returns a pointer to the last depot in the linked list
    * @return last depot
    */
    public Depot getLast() {
        return last;
    }

    /**
    * Returns the number of depots in the linked list
    * @return number of depots in linked list
    */
    public int getNoDepots() {
        int count = 0;
        Depot temp = first;

        while (temp != null) {
            temp = temp.next;
            count++;
        }

        return count;
    }

    /**
    * Returns the depot closest to the (x,y) coordinates given by a particular
    * search type denoted by an integer.
    *
    * Types:
    * 1 = Euclidean distance
    * 2 = Polar Coordinate Angle
    *
    * @param x X coordinate
    * @param y Y coodinate
    * @param type criteria of search
    * @return pointer to the closest depot
    */
    public Depot getClosestDepot(double x, double y, int type) {
        boolean isDiagnostic = false;
        Depot temp = first; //point to the first shipment
        Depot foundDepot = null; //the shipment found with the criteria
        double angle;
        double foundAngle = 360; //initial value
        double distance;
        double foundDistance = 200; //initial distance

        while (temp != null) {
            if (isDiagnostic) {
                System.out.print("Shipment " + temp.depotNo + " ");

                if (((temp.x - x) >= 0) && ((temp.y - y) >= 0)) {
                    System.out.print("Quadrant I ");
                } else if (((temp.x - x) <= 0) && ((temp.y - y) >= 0)) {
                    System.out.print("Quadrant II ");
                } else if (((temp.x) <= (0 - x)) && ((temp.y - y) <= 0)) {
                    System.out.print("Quadrant III ");
                } else if (((temp.x - x) >= 0) && ((temp.y - y) <= 0)) {
                    System.out.print("Quadrant VI ");
                } else {
                    System.out.print("No Quadrant");
                }
            }

            //if not assigned, check it
            switch (type) {
            //find the customer closest to the depot in Euclidean distance
            case 1:
                distance = Utility.calcDist(x, temp.x, y, temp.y);

                if (isDiagnostic) {
                    System.out.println("  " + distance);
                }

                //check if this shipment should be tracked
                if (foundDepot == null) //this is the first shipment being checked
                 {
                    foundDepot = temp;
                    foundDistance = distance;
                } else {
                    if (distance < foundDistance) //found an angle that is less
                     {
                        foundDepot = temp;
                        foundDistance = distance;
                    }
                }

                break;

            //find the customer with the lowest polar coordinate angle
            case 2:
                angle = Utility.calcPolarAngle(x, y, temp.getX(), temp.getY());

                if (isDiagnostic) {
                    System.out.println("  " + angle);
                }

                //check if this shipment should be tracked
                if (foundDepot == null) //this is the first shipment being checked
                 {
                    foundDepot = temp;
                    foundAngle = angle;
                } else {
                    if (angle < foundAngle) //found an angle that is less
                     {
                        foundDepot = temp;
                        foundAngle = angle;
                    }
                }

                break;
            }

            temp = temp.next;
        }

        return foundDepot; //stub
    }

    /**
    * gets the distance of all depots in the DepotLinkedList relative to the
    * (x,y) coordinates of another object using a particular search type denoted
    * by an integer
    * Types:
    * 1 = Euclidean distance
    * 2 = Polar Coordinate Angle
    * @param x X Coordinate of the object to compare
    * @param y Y Coordinate of the object to compare
    * @param type criteria of search
    * @return array of depots sorted from closest to farthest
    */
    public Depot[] getDistanceForAllDepots(double x, double y, int type) {
        boolean isDiagnostic = false;
        Depot temp = first; //point to the first depot

        /**
        * Class used for sorting depots by a value, implements the comparable
        * interface so that an array can be easily sorted using
        * java.util.Array.sort(d_SortArray)
        * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
        * <p>Description: </p>
        * <p>Copyright: Copyright (c) 2001</p>
        * <p>Company: </p>
        * @author Anthony Pitluga
        * @version 1.0
        */
        class D_Sort implements Comparable {
            Depot thisDepot;
            double value; // value to be compared (distace, angle, etc)

            public D_Sort(Depot d) {
                thisDepot = d;
            }

            public int compareTo(Object o) {
                D_Sort k = (D_Sort) o;

                if (value != k.value) {
                    return new Double(value - k.value).intValue();
                } else {
                    return 0;
                }
            }
        }

        Depot[] foundDepots = new Depot[this.getNoDepots()]; // array to return
        D_Sort[] depots = new D_Sort[this.getNoDepots()];

        int count = 0;

        while (temp != null) {
            depots[count] = new D_Sort(temp);

            //depots[count].thisDepot.displayDepot();
            if (isDiagnostic) {
                System.out.print("Depot " + temp.depotNo + " ");

                if (((temp.x - x) >= 0) && ((temp.y - y) >= 0)) {
                    System.out.print("Quadrant I ");
                } else if (((temp.x - x) <= 0) && ((temp.y - y) >= 0)) {
                    System.out.print("Quadrant II ");
                } else if (((temp.x) <= (0 - x)) && ((temp.y - y) <= 0)) {
                    System.out.print("Quadrant III ");
                } else if (((temp.x - x) >= 0) && ((temp.y - y) <= 0)) {
                    System.out.print("Quadrant VI ");
                } else {
                    System.out.print("No Quadrant");
                }
            }

            //if not assigned, check it
            switch (type) {
            //find the customer closest to the depot in Euclidean distance
            case 1:
                depots[count].value = Utility.calcDist(x, temp.x, y, temp.y);

                if (isDiagnostic) {
                    System.out.println("  " + depots[count].value);
                }

                break;

            //find the customer with the lowest polar coordinate angle
            case 2:
                depots[count].value = Utility.calcPolarAngle(x, y, temp.getX(),
                        temp.getY());

                if (isDiagnostic) {
                    System.out.println("  " + depots[count].value);
                }

                break;
            }

            count++;
            temp = temp.next;
        }

        java.util.Arrays.sort(depots);

        for (int i = 0; i < this.getNoDepots(); i++) {
            foundDepots[i] = depots[i].thisDepot;
        }

        return foundDepots; //stub
    }

    /**
    * Get total number of 1-opt's performed for all the trucks in the current
    * depot.
    *  @return float number of 1-opt's performed
    * */
    public float getTotalOneOpt() {
        return totalOneOpt;
    }

    /**
    * Get total number of 2-opt's performed for all the trucks in the current
    * depot.
    *  @return float number of 2-opt's performed
    * */
    public float getTotalTwoOpt() {
        return totalTwoOpt;
    }

    /**
    * Get total number of 3-opt's performed for all the trucks in the current
    * depot.
    *  @return float number of 3-opt's performed
    * */
    public float getTotalThreeOpt() {
        return totalThreeOpt;
    }

    /**
    * Get total number of k-two-opt's performed for all the trucks in the current
    * depot.
    * @return float number of k-two-opt's performed
    * */
    public float getTotalKTwoOpt() {
        return totalKTwoOpt;
    }

    /**
    * Get total number of k-three-opt's performed for all the trucks in the current
    * depot.
    * @return float number of k-three-opts performed
    * */
    public float getTotalKThreeOpt() {
        return totalKThreeOpt;
    }

    /**
    * Calculate the total demand or weight of trucks in the depot for the MDVRP. When trucks are added
    * to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * */
    public void calculateTotalCapacityMDVRP() {
        Depot current = first;
        float totalDemand = 0;

        while (current != null) {
            totalDemand += current.mainTrucks.getTotalDemandOfTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalDemand(totalDemand);
            current = current.next;
        }

        setTotalDemandOfMDVRP(totalDemand);
    }

    /**
    * Calculate the total demand or weight of trucks in the depot for the VRP.
    * When trucks are added to a depot, this method needs to be called to update
    * the variables maintaining information on trucks at the Depot level.
    * This method added 8/30/03 by Mike McNamara
    * Not used for VRPTW problem
    */
    public void calculateTotalDemandVRP() {
        Depot current = first;
        float totalDemand = 0;

        while (current != null) {
            totalDemand += current.mainTrucks.getTotalDemandOfTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalDemand(totalDemand);
            current = current.next;
        }

        setTotalDemandOfVRP(totalDemand);
    }

    /**
    * Calculate the total demand or weight of trucks in the depot. When trucks are added
    * to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * */
    public void calculateTotalCapacity() {
        Depot current = first;
        float totalDemand = 0;

        while (current != null) {
            totalDemand += current.mainTrucks.getTotalDemandOfTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalDemand(totalDemand);
            current = current.next;
        }

        setTotalDemandOfMDVRP(totalDemand);
    }

    /**
    * Get the total distance or duration of all the trucks for the current depot
    * @return total distance or duration
    * */
    public float getTotalDistanceMDVRP() {
        //calculate the total distance for each depot
        //calculateTotalDistanceMDVRP();
        //sum up the distance traveled by trucks in each depot
        return totalDistance;
    }

    /**
    * Get the total distance or duration of all the trucks for the depot
    * @return total distance or duration
    * */
    public float getTotalDistanceVRP() {
        return totalDistance;
    }

    /**
    * Set the total distance traveled by all the trucks for this depot
    * @param distance total distance or duration
    * @return float    total distance or duration
    * */
    public float setTotalDistanceOfMDVRP(float distance) {
        totalDistance = distance;

        //sum up the demand of trucks in each depot
        return totalDistance;
    }

    /**
    * Set the total distance traveled by all the trucks for the depot
    * This method added 8/30/03 by Mike McNamara
    * Not used for the VRPTW problem
    * @param distance total distance or duration
    * @return float    total distance or duration
    */
    public float setTotalDistanceOfVRP(float distance) {
        totalDistance = distance;

        //sum of the demand of trucks in each depot
        return totalDistance;
    }

    /**
    * Calculate the total demand or weight of trucks in the depot.
    * When trucks are added to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * */
    public void calculateTotalDistance() {
        Depot current = first;
        float totalDistance = 0;

        while (current != null) {
            totalDistance += current.mainTrucks.getTotalDistanceOfTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalDuration(totalDistance);
            current = current.next;
        }

        setTotalDistanceOfMDVRP(totalDistance);
    }

    /**
    * Calculate the total demand or weight of trucks in the depot for the MDVRP problem.
    * When trucks are added to a depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * */
    public void calculateTotalDistanceMDVRP() {
        Depot current = first;
        float totalDistance = 0;

        while (current != null) {
            totalDistance += current.mainTrucks.getTotalDistanceOfTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalDuration(totalDistance);
            current = current.next;
        }

        setTotalDistanceOfMDVRP(totalDistance);
    }

    /**
    * Calculate the total demand or weight of trucks in the depot for the VRP problem.
    * When trucks are added to the depot, this method needs to be called to update the variables
    * maintaining information on trucks at the Depot level.
    * This method add 8/30/03 by Mike McNamara
    * Not used for the VRPTW problem
    */
    public void calculateTotalDistanceVRP() {
        Depot current = first;
        float totalDistance = 0;

        while (current != null) {
            totalDistance += current.mainTrucks.getTotalDistanceOfTrucks();

            //each depot maintains the total number of trucks for the
            //TruckLInked list
            current.setTotalDuration(totalDistance);
            current = current.next;
        }

        setTotalDistanceOfVRP(totalDistance);
    }

    /**
    * A diagnostic method to check if all the shipments have been routed and
    * no shipments are duplicated for the MDVRP problem
    * @param noNodes number of shipments in the problem
    * @return boolean true if all checked out else false
    * */
    public boolean checkIfAllNodesRoutedMDVRP(int noNodes) {
        boolean isDiagnostic = false;
        boolean status = true;
        Depot current = first;
        int[] nodeArray;

        nodeArray = new int[noNodes + 1]; //0 is not used

        //initialize the array
        for (int i = 0; i < (noNodes + 1); i++)
            nodeArray[i] = 0; //set the initial values to 0

        while (current != null) {
            //load the nodes from the trucks into the array
            //For every node that is present, the location is
            //incremented.  If all the nodes have 1 then it
            //it worked, else it didn't
            current.mainTrucks.checkNodesMDVRP(nodeArray);
            current = current.next;
        }

        //check if all the shipments are present and none are duplicated
        for (int i = 1; i < (noNodes + 1); i++)
            if ((nodeArray[i] != 1) && (status == true)) {
                status = false;
            }

        if (isDiagnostic) {
            System.out.println("");

            for (int i = 1; i < noNodes; i++)
                System.out.println(i + " " + nodeArray[i]);
        }

        return status;
    }

    /**
    * Create a depot with a unique id using the information from the shipList
    * instance of the ShipmentLinkedList.
    * @param  depotNo  number of the depot
    * @param  shipList shipment list
    * @return Depot    depot with the shipment information
    *
    * */
    private Depot createADepot(int depotNo, ShipmentLinkedList shipList) {
        boolean isDiagnostic = false;
        Shipment tempShip;
        Depot newDepot;

        //The information on the depots are available after the
        //total number of customers. The depot information of
        //depotNo is located at n (customer number) + depotNo
        //find the depot in the mainShipment list
        //noShipments - has all the shipments from the file, including the depots
        //noDepots - indicates the total number of depots out of the shipments
        tempShip = shipList.find(shipList.noShipments + depotNo);

        if (isDiagnostic) {
            System.out.println("The depot to be added is " +
                (shipList.noShipments + depotNo));
        }

        if (tempShip != null) {
            newDepot = new Depot(tempShip.getX(), tempShip.getY());

            //set the depot number
            newDepot.setDepotNo(tempShip.getShipNo());

            if (isDiagnostic) {
                System.out.println("Depot coordinates are " + tempShip.getX() +
                    " " + tempShip.getY());
            }

            //once the depot is created, remove the node from mainShipments
            tempShip.shipNo = shipList.noShipments + depotNo;
            shipList.deleteKey(shipList.noShipments + depotNo);
        } else {
            System.out.println("Depot was not found in mainShipments");
            newDepot = null;
        }

        return newDepot;
    }

    /**
    * <p>Exchange 01 nodes between the v different routes then cycle back to that node</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param v  variable depth
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclicVC1T(int v, int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            //sending in the variable depth and number of trucks at the depot
            int whichTrucks = tempDepot.mainTrucks.getNoTrucks();

            for (int i = 0; i < whichTrucks; i++) {
                tempDepot.mainTrucks.cyclicVC1T(v, i, maxLoop);

                //compute the number of exchanges that took place
                totalvC1T += tempDepot.mainTrucks.getTotal01();
            }

            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of vC1T exchanges are " + totalvC1T);
    }

    /* cyclicVC1T */

    /**
    * <p>Exchange 01 nodes between the 2 different routes then cycle back to that node</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclic2C1T(int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.cyclic2C1T(maxLoop);

            //compute the number of exchanges that took place
            total2C1T += tempDepot.mainTrucks.getTotal01();
            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of 2C1T exchanges are " + total2C1T);
    }

    /* cyclic2C1T */

    /**
    * <p>Exchange 01 nodes between the 3 different routes then cycle back to the head node</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclic3C1T(int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            //for loop advances the pointer in the trucklinked list
            int whichTrucks = tempDepot.mainTrucks.getNoTrucks();

            for (int i = 0; i < whichTrucks; i++) {
                tempDepot.mainTrucks.cyclic3C1T(i, maxLoop);

                //compute the number of exchanges that took place
                total3C1T += tempDepot.mainTrucks.getTotal01();
            }

            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of 3C1T exchanges are " + total3C1T);
    }

    /* cyclic3C1T */

    /**
    * <p>Exchange 01 nodes between the 2 different routes then reassign the head pointer</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclic2AC1T(int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.cyclic2AC1T(maxLoop);

            //compute the number of exchanges that took place
            total2AC1T += tempDepot.mainTrucks.getTotal01();
            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of 2AC1T exchanges are " + total2AC1T);
    }

    /* cyclic2AC1T */

    /**
    * <p>Exchange 01 nodes between the 3 different routes then reassign the head pointer</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclic3AC1T(int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            //for loop advances the pointer in the trucklinked list
            int whichTrucks = tempDepot.mainTrucks.getNoTrucks();

            for (int i = 0; i < whichTrucks; i++) {
                tempDepot.mainTrucks.cyclic3AC1T(i, maxLoop);

                //compute the number of exchanges that took place
                total3AC1T += tempDepot.mainTrucks.getTotal01();
            }

            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of 3AC1T exchanges are " + total3AC1T);
    }

    /* cyclic3AC1T */

    /**
    * <p>Exchange 01 nodes between the v different routes then reassign the head pointer</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param v  variable depth
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclicVAC1T(int v, int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            //sending in the variable depth and number of trucks at the depot
            int whichTrucks = tempDepot.mainTrucks.getNoTrucks();

            for (int i = 0; i < whichTrucks; i++) {
                tempDepot.mainTrucks.cyclicVAC1T(v, i, maxLoop);

                //compute the number of exchanges that took place
                totalvAC1T += tempDepot.mainTrucks.getTotal01();
            }

            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of vAC1T exchanges are " + totalvAC1T);
    }

    /* cyclicVAC1T */

    /**
    * <p>Exchange 02 nodes between the 2 different routes then cycle back to that node</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclic2C2T(int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            tempDepot.mainTrucks.cyclic2C2T(maxLoop);

            //compute the number of exchanges that took place
            total2C2T += tempDepot.mainTrucks.getTotal02();
            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of 2C2T exchanges are " + total2C2T);
    }

    /* cyclic2C2T */

    /**
    * <p>Exchange 02 nodes between the n different routes then cycle back to the head node</p>
    * This method was modified from code taken from the MDVRPTW problem
    * @param v  variable depth
    * @param maxLoop  maximum number of customer exchange comparison iterations
    */
    public void cyclicVC2T(int v, int maxLoop) {
        Depot tempDepot = first;

        while (tempDepot != null) //loop through all the depots
         {
            //for loop advances the pointer in the trucklinked list
            int whichTrucks = tempDepot.mainTrucks.getNoTrucks();

            for (int i = 0; i < whichTrucks; i++) {
                tempDepot.mainTrucks.cyclicVC2T(v, i, maxLoop);

                //compute the number of exchanges that took place
                total3C1T += tempDepot.mainTrucks.getTotal02();
            }

            tempDepot = tempDepot.next;

            // update values after exchanges
            ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        }

        //        System.out.println("Total number of vC2T exchanges are " + totalvC2T);
    }

    /* cyclicVC2T */

    /**
    * Write the short form of information for all the depots and trucks to a file.
    * @param solOutFile output file name
    *
    * */
    public void writeShortDepotsSol(PrintWriter solOutFile) {
        int depotNo;
        Depot current = first;

        while (current != null) {
            //current.writeShortDepot(solOutFile); //write depot coordinates
            depotNo = current.depotNo;
            current.mainTrucks.writeShortTrucksSol(depotNo, solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
    * <p>Write the short form of information for all the depots and trucks to a file.</p>
    * This method added 8/30/03 by Mike McNamara
    * @param solOutFile output file name
    * */
    public void writeVRPShortDepotsSol(PrintWriter solOutFile) {
        int depotNo;
        Depot current = first;

        while (current != null) {
            //current.writeShortDepot(solOutFile); //write depot coordinates
            depotNo = current.depotNo;
            current.mainTrucks.writeVRPShortTrucksSol(depotNo, solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
    * <p>Write the detail form of information for all the depots and trucks to a file.</p>
    * @param solOutFile output file name
    *
    * */
    public void writeDetailDepotsSol(PrintWriter solOutFile) {
        //solOutFile.println("List (first to last): ");
        Depot current = first;

        while (current != null) {
            current.writeDetailDepot(solOutFile);
            current.mainTrucks.writeDetailTrucksSol(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    /**
    * <p>Write the detail form of information for all the depots and trucks to a file.</p>
    * This method added 8/30/03 by Mike McNamara
    * @param solOutFile output file name
    * */
    public void writeVRPDetailDepotsSol(PrintWriter solOutFile) {
        Depot current = first;

        while (current != null) {
            current.writeDetailDepot(solOutFile);
            current.mainTrucks.writeVRPDetailTrucksSol(solOutFile);
            current = current.next;
        }

        solOutFile.println("");
    }

    //============================================================================================================
    //========================== START FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =============================
    //============================================================================================================

    /**
    * <p>Exchange nodes between the different trucks in a single depot</p>
    * @param exchangeType type of exchange
    */
    public void exchangeOneDepotOpt(int exchangeType) {
        SelectExchange selectExchange = new SelectExchange();
        Depot tempDepot = first;
        selectExchange.setTotals(exchangeType, null, true);

        if (ProblemInfo.isUsingTabu) {
            if (Tabu.TabuProblemInfo.STOP) {
                return;
            }
        }

        while (tempDepot != null) {
            tempDepot.mainTrucks.exchangeOneDepotOpt(exchangeType, tempDepot);

            //compute the number of exchanges that took place
            selectExchange.setTotals(exchangeType, tempDepot, false);

            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            ProblemInfo.depotLevelCostF.calculateTotalsStats(tempDepot);
            calculateTotalNonEmptyTrucksMDVRP();
            tempDepot = tempDepot.next;
        }

        ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        clearEmptyTrucks();
        System.out.println("IntraDepot " +
            ZeusConstants.getExchangeName(exchangeType) + " exchanges is " +
            selectExchange.getTotalExchanges());
    }

    /* exchangeOneDepotOpt() */

    /**
    * <p>Exchange nodes between the trucks in different depots. Depots are selected
    * in a cyclical manner.</p>
    * @param exchangeType type of exchange
    */
    public void exchangeMultDepotOpt1(int exchangeType) {
        SelectExchange selectExchange = new SelectExchange();
        Depot depot1;
        Depot depot2;
        selectExchange.setTotals(exchangeType, null, true);

        if (ProblemInfo.isUsingTabu) {
            if (Tabu.TabuProblemInfo.STOP) {
                return;
            }
        }

        depot1 = first;

        while ((depot1 != null) && (depot1.next != null)) {
            depot2 = depot1.next;
            depot1.mainTrucks.exchangeMultDepotOpt(exchangeType, depot1, depot2);

            //compute the number of exchanges that took place
            selectExchange.setTotals(exchangeType, depot1, false);

            //IMPORTANT: DO NOT setTotals for depot2 because the number of exchanges will be counted twice!!!!!
            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            ProblemInfo.depotLevelCostF.calculateTotalsStats(depot1);
            ProblemInfo.depotLevelCostF.calculateTotalsStats(depot2);
            calculateTotalNonEmptyTrucksMDVRP();
            depot1 = depot1.next;
        }

        // make it cyclical by exchanging between last and first depots
        if (depot1 != null) {
            depot2 = first;
            depot1.mainTrucks.exchangeMultDepotOpt(exchangeType, depot1, depot2);

            //compute the number of exchanges that took place
            selectExchange.setTotals(exchangeType, depot1, false);

            //IMPORTANT: DO NOT setTotals for depot2 because the number of exchanges will be counted twice!!!!!
            //update the change in demand and distance after localopt
            //compute the noTrucks, distance and capacity for the current depot
            ProblemInfo.depotLevelCostF.calculateTotalsStats(depot1);
            ProblemInfo.depotLevelCostF.calculateTotalsStats(depot2);
            calculateTotalNonEmptyTrucksMDVRP();
        }

        ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        clearEmptyTrucks();
        System.out.println("InterDepot " +
            ZeusConstants.getExchangeName(exchangeType) + " exchanges is " +
            selectExchange.getTotalExchanges());
    }

    /* exchangeMultDepotOpt1() */

    /**
    * <p>Exchange nodes between the trucks in different depots. All possible combinations of
    * depots are tried.</p>
    * @param exchangeType type of exchange
    */
    public void exchangeMultDepotOpt2(int exchangeType) {
        SelectExchange selectExchange = new SelectExchange();
        Depot depot1;
        Depot depot2;
        selectExchange.setTotals(exchangeType, null, true);

        if (ProblemInfo.isUsingTabu) {
            if (Tabu.TabuProblemInfo.STOP) {
                return;
            }
        }

        depot1 = first;

        while (depot1 != null) {
            depot2 = depot1.next;

            while (depot2 != null) {
                depot1.mainTrucks.exchangeMultDepotOpt(exchangeType, depot1,
                    depot2);

                //compute the number of exchanges that took place
                selectExchange.setTotals(exchangeType, depot1, false);

                //IMPORTANT: DO NOT setTotals for depot2 because the number of exchanges will be counted twice!!!!!
                //update the change in demand and distance after localopt
                //compute the noTrucks, distance and capacity for the current depot
                ProblemInfo.depotLevelCostF.calculateTotalsStats(depot1);
                ProblemInfo.depotLevelCostF.calculateTotalsStats(depot2);
                calculateTotalNonEmptyTrucksMDVRP();
                depot2 = depot2.next;
            }

            depot1 = depot1.next;
        }

        ProblemInfo.depotLLLevelCostF.calculateTotalsStats(this);
        clearEmptyTrucks();
        System.out.println("InterDepot " +
            ZeusConstants.getExchangeName(exchangeType) + " exchanges is " +
            selectExchange.getTotalExchanges());
    }

    /* exchangeMultDepotOpt2() */

    /**
    * <p>Renumber the depot numbers of all depots, so that they are in sequential
    * order, starting with 1.</p>
    */
    public void updateDepotNo() {
        Depot current = first;
        int no = 1;

        while (current != null) {
            current.depotNo = no;
            current = current.next;
        }
    }

    /**
    * <p>Clear all empty trucks in all depots.</p>
    */
    public void clearEmptyTrucks() {
        Depot depot = this.first;

        while (depot != null) {
            depot.clearEmptyTrucks();
            depot = depot.next;
        }
    }

    /**
    * <p>Get the total wait time for all the trucks in the depot</p>
    * Sunil 28/9/03
    * @return float the totalwaitTime
    */
    public float getTotalWaitTime() {
        //Temporary location to hold the summation of waitTime for each truck
        float totalWaitTime = 0;

        //Get the mainTruckLinkedList
        TruckLinkedList tempTruckList = this.first.getMainTrucks();

        //Get the first truck in the TruckLinkedList
        Truck tempTruck = tempTruckList.first;

        //While there are no more trucks
        while (tempTruck != null) {
            //While there are no more trucks
            VisitNodesLinkedList tempVisitList = tempTruck.mainVisitNodes;

            //Get the first pointCell in the tempVisitList
            PointCell tempPointCell = tempVisitList.first().next;

            //While there are no more pointCell
            while (tempPointCell.next != null) {
                //Perform the summation of waitTime for each pointCell
                totalWaitTime += tempPointCell.waitTime;
                tempPointCell = tempPointCell.next; //Next pointCell
            }

            tempTruck = tempTruck.next; //Next truck
        }

        return totalWaitTime;
    }

    /**
    * <p>Calculates the overall total travel time for all the trucks</p>
    * Sunil 28/9/03
    * @return float totalWaitTime
    */
    public float getTotalTravelTime() {
        //Temporary location to hold the summation of waitTime for each truck
        float totalTravelTime = 0;

        //Get the mainTruckLinkedList
        TruckLinkedList tempTruckList = this.first.getMainTrucks();

        //Get the first truck in the TruckLinkedList
        Truck tempTruck = tempTruckList.first;

        //While there are no more trucks
        while (tempTruck != null) {
            //Get the mainVisitNodes for this truck
            VisitNodesLinkedList tempVisitList = tempTruck.mainVisitNodes;

            // calculate and set the total travel time
            ProblemInfo.vNodesLevelCostF.setTotalTravelTime(tempVisitList);

            //Add the totalTravelTime for this truck to the summation
            totalTravelTime += ProblemInfo.vNodesLevelCostF.getTotalTravelTime(tempVisitList);
            tempTruck = tempTruck.next; //Get the next truck
        }

        return totalTravelTime;
    }

    /**
    * <p>Calculates the overall total service time for all the trucks</p>
    * Sunil 29/9/03
    * @return totalServicetime
    */
    public float getTotalServiceTime() {
        //Temporary location to hold the summation of serviceTime for each truck
        float totalServiceTime = 0;

        //Get the mainTruckLinkedList
        TruckLinkedList tempTruckList = this.first.getMainTrucks();

        //Get the first truck in the TruckLinkedList
        Truck tempTruck = tempTruckList.first;

        //While there are no more trucks
        while (tempTruck != null) {
            //While there are no more trucks
            VisitNodesLinkedList tempVisitList = tempTruck.mainVisitNodes;

            //Get the first pointCell in the tempVisitList
            PointCell tempPointCell = tempVisitList.first().next;

            //While there are no more pointCell
            while (tempPointCell.next != null) {
                //Perform the summation of waitTime for each pointCell
                totalServiceTime += tempPointCell.servTime;
                tempPointCell = tempPointCell.next; //Next pointCell
            }

            tempTruck = tempTruck.next; //Next truck
        }

        return totalServiceTime;
    }

    /**
    * <p>Calculate the overall tardiness for all the trucks</p>
    * Sunil 9/28/03
    * @return overAllTardiness
    */
    public float getTotalTardiness() {
        //Temporary location to hold the summation of tardinessTime for each truck
        float totalTardinessTime = 0;

        //Get the mainTruckLinkedList
        TruckLinkedList tempTruckList = this.first.getMainTrucks();

        //Get the first truck in the TruckLinkedList
        Truck tempTruck = tempTruckList.first;

        //While there are no more trucks
        while (tempTruck != null) {
            //Get the mainVisitNodes in this truck and get the tardiness for this truck and add
            VisitNodesLinkedList tempVisitList = tempTruck.mainVisitNodes;
            totalTardinessTime += tempVisitList.calcTotalTardiness();
            tempTruck = tempTruck.next; //Next truck
        }

        return totalTardinessTime;
    }

    /**
    * <p>Calculate the total excess time for all the trucks</p>
    * Sunil 9/28/03
    * @return float  total excess time for all the trucks
    */
    public float getTotalExcessTime() {
        //Temporary location to hold the summation of excessTime for each truck
        float totalExcessTime = 0;

        //Get the mainTruckLinkedList
        TruckLinkedList tempTruckList = this.first.getMainTrucks();

        //Get the first truck in the TruckLinkedList
        Truck tempTruck = tempTruckList.first;

        //While there are no more trucks
        while (tempTruck != null) {
            //Get the mainVisitNodes in this truck and get the excessTime for this truck and add
            VisitNodesLinkedList tempVisitList = tempTruck.mainVisitNodes;
            totalExcessTime += ProblemInfo.vNodesLevelCostF.getTotalExcessTime(tempVisitList); //tempVisitList.calcTotalExcessTime();
            tempTruck = tempTruck.next; //Next truck
        }

        return totalExcessTime;
    }

    /**
    * <p>Calculate the total overload for all the trucks</p>
    * Sunil 9/29/03
    * @return totalOverload
    */
    public float getTotalOverload() {
        //Temporary location to hold the summation of overload for each truck
        float totalOverload = 0;

        //Get the mainTruckLinkedList
        TruckLinkedList tempTruckList = this.first.getMainTrucks();

        //Get the first truck in the TruckLinkedList
        Truck tempTruck = tempTruckList.first;

        //While there are no more trucks
        while (tempTruck != null) {
            //Get the mainVisitNodes in this truck and get the overload for this truck and add
            VisitNodesLinkedList tempVisitList = tempTruck.mainVisitNodes;
            totalOverload += tempVisitList.calcTotalOverload();
            tempTruck = tempTruck.next; //Next truck
        }

        return totalOverload;
    }

    /**
    * <p>calculates total exchanges specific to exchange used</p>
    */
    class SelectExchange {
        private int totalExchanges = 0;

        public void setTotals(int ExchangeType, Depot dp, boolean reset) {
            switch (ExchangeType) {
            case ZeusConstants.EXCHANGE_01:
            case ZeusConstants.EXCHANGE_10:

                if (reset) {
                    total01 = 0;
                } else {
                    total01 += dp.mainTrucks.getTotal01();
                    totalExchanges += dp.mainTrucks.getTotal01();
                }

                break;

            case ZeusConstants.EXCHANGE_02:
            case ZeusConstants.EXCHANGE_20:

                if (reset) {
                    total02 = 0;
                } else {
                    total02 += dp.mainTrucks.getTotal02();
                    totalExchanges += dp.mainTrucks.getTotal02();
                }

                break;

            case ZeusConstants.EXCHANGE_11:

                if (reset) {
                    total11 = 0;
                } else {
                    total11 += dp.mainTrucks.getTotal11();
                    totalExchanges += dp.mainTrucks.getTotal11();
                }

                break;

            case ZeusConstants.EXCHANGE_12:
            case ZeusConstants.EXCHANGE_21:

                if (reset) {
                    total12 = 0;
                } else {
                    total12 += dp.mainTrucks.getTotal12();
                    totalExchanges += dp.mainTrucks.getTotal12();
                }

                break;

            case ZeusConstants.EXCHANGE_22:

                if (reset) {
                    total22 = 0;
                } else {
                    total22 += dp.mainTrucks.getTotal22();
                    totalExchanges += dp.mainTrucks.getTotal22();
                }

                break;
            }
        }

        public int getTotalExchanges() {
            return totalExchanges;
        }
    }
}


//============================================================================================================
//============================ END FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =============================
//============================================================================================================
//DepotLinkedList class
