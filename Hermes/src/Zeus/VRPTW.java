package Zeus;

import java.io.*; //input-output java package

import java.util.*; //for string tokenizer


/**
 * <pre>Name: Mike McNamara and Sunil Gurung
 * Status: Complete
 * "This program was done entirely by me and my partner and no part of this program was
 *  plagiarized, intentionally or unintentionally, from anybody.  I would be
 *  held accountable and penalized if any part of this program was plagiarized."
 *
 * Classes that were added to Zeus:
 *    VRPTW class
 *      It's methods are:
 *         VRPTW
 *            constructor that runs the VRPTW class.
 *            calls readVRPTWTokenFileData to read in the input
 *            data, and assigns all shipments to trucks using
 *            mainDepots.insertShipmentVRPTW, and writes all
 *            solutions to output files.
 *         readVRPTWTokenFileData
 *            read the data from input file and inserts it into
 *            the ShipmentLinkedList.
 *         writeVRPTWDetailSol
 *            writes a detailed solution to a file.
 *         writeVRPTWShortSol
 *            writes a shorter version of the solution to a file.
 *         calculateTotalOpts
 *            calculates the number of optimizations performed to display
 *            it as output
 *         getClosestShipmentVRPTW
 *            Find the closest customer to the depot to be inserted
 *            into a truck
 *         runOptimize
 *            runs the optimization methods for local and global
 *            optimization
 *         upDateCostFunctions
 *            updates all the set interfaces in the CostFunctions
 *
 * New variables that were added to existing classes:
 *    ProblemInfo:
 *       public static float overAllDemand; //Total demand for all the trucks
 *       public static float overAllTotalWaitTime; //Total wait time for all the trucks
 *       public static float overAllTotalTravelDistance; //Total travel time for all the trucks
 *       public static float overAllServiceTime; //Total Servce time for all the trucks
 *       public static float overAllTotalDistance; //Total Distance covered for all the trucks
 *       public static float overAllTotalTardiness; //Total Tardiness for all the trucks, must be 0
 *       public static float overAllTotalExcessTime; //Total Excess time for all the trucks, must be 0
 *       public static float overAllTotalOverload; //Total OverLoad time for all the trucks, must be 0
 *       public static int NUMBEROFCASES;  // number of derivation cases
 *       public static double alpha;  // penalty for wait time
 *       public static double beta;  // penalty for excess time
 *       public static double mu;  // penalty for tardiness
 *       public static double fi;  // penalty for overload
 *       public static CostFunctions shipmentLLLevelCostF; // for the ShipmentLinkedList level cost functions
 *       public static CostFunctions shipmentLevelCostF; // for the Shipment level cost functions
 *    ShipmentLinkedList:
 *       public double angle; // angle of customer in relation to the depot
 *       public double distance; // distance of customer from depot
 *       public double time; // median time of a customer
 *       public double cost; // cost for choosing customer
 *    Shipment:
 *       int earliestTime; // earliest time to service this shipment
 *       int latestTime; // latest time to service this shipment
 *       int servTime; // time to service this shipment
 *    DepotLinkedList:
 *       public float totalTardiness; //total tardiness of all the nodes in the depots
 *       public float totalOverload; //total overload of all the nodes in the depots
 *       public float totalExcessTime; //total excess time of all the nodes in the depots
 *       public float totalWaitTime; //total wait time of all the nodes in the depots
 *       public float totalTotalTravelTime; //total travel time of all the nodes in the depots
 *       public float totalServiceTime; //total service time of all the nodes in the depots
 *       int totalvC1T; //total number of exchanges and relocations that took place
 *       int total2C1T; //total number of exchanges and relocations that took place
 *       int total3C1T; //total number of exchanges and relocations that took place
 *       int total2AC1T; //total number of exchanges and relocations that took place
 *       int total3AC1T; //total number of exchanges and relocations that took place
 *       int totalvAC1T; //total number of exchanges and relocations that took place
 *       int total2C2T; //total number of exchanges and relocations that took place
 *       int totalvC2T; //total number of exchanges and relocations that took place
 *    Depot:
 *       public float totalTravelTime; // total travel time of all trucks
 *       public float totalWaitTime; // total wait time of all trucks
 *       public float totalServTime; // total service time of all trucks
 *       public float totalTardiness; // total tardiness time of all trucks
 *       public float totalExcessTime; // total excess time of all trucks
 *       public float totalOverload; // total overload of all trucks
 *    TruckLinkedList:
 *       public float totalTardiness; //total tardiness of all the nodes in the depots
 *       public float totalOverload; //total overload of all the nodes in the depots
 *       public float totalExcessTime; //total excess time of all the nodes in the depots
 *       public float totalWaitTime; //total wait time of all the nodes in the depots
 *       public float totalTotalTravelTime; //total travel time of all the nodes in the depots
 *       public float totalServiceTime; //total service time of all the nodes in the depots
 *       public int[] truckSwap = new int[2]; // will contain the truck indexes for the cyclic exchange
 *       public int cycle = 0; // hold the current cycle that the cyclic exchange is operating on
 *       public boolean swap = false; // flag to determine if trucks should be swapped in a cyclic exchange
 *       int assignHeadTruck; // head truck in a variable cyclic or acyclic exchange; from MDVRPTW problem
 *    Truck:
 *       None.
 *    VisitNodesLinkedList:
 *       public float currentDemand; //total demand of all the nodes in the current route
 *       public float currentTardiness; //total tardiness of all the nodes in the current route
 *       public float currentOverload; //total overload of all the nodes in the current route
 *       public float currentExcessTime; //total excess time of all the nodes in the current route
 *       public float currentWaitTime; //total wait time of all the nodes in the current route
 *       public float currentTotalTravelTime; //total travel time of all the nodes in the current route
 *       public float currentServiceTime; //total service time of all the nodes in the current route
 *    PointCell:
 *       float slackTime; //The time that the truck can slack at each location so as
 *                        //to be able to insert customer in between the customer in a route
 *
 * New methods that were added to or changed in existing classes:
 *   ShipmentLinkedList:
 *      double calcMedianTime(Shipment customer)
 *         calculates the median time of the customer
 *      Shipment getClosestShipmentVRPTW(double x, double y, int type)
 *         Find the shipment that is closest to the given x,y coordinate
 *         using type.  Type allows one to compute closeness with respect to
 *         different criteria
 *      void insertDepotPosition(int num, float x, float y)
 *         Insert the depot coordinates at the end of the linked list to be read
 *         by the DepotLinkedList class. A new instance of the
 *         shipment is created, using the parameter values, and is inserted at the end
 *         of the linked list.
 *      void insertShipment(int num, float x, float y, int q)
 *         Insert the shipment at the end of the linked list. A new instance of the
 *         shipment is created, using the parameter values, and is inserted at the end
 *         of the linked list.
 *
 *   Shipment:
 *      Shipment(float x, float y, int q, int no, int eTime, int lTime, int sTime)
 *         Create an instance of the shipment with the required information for the VRPTW problem
 *      Shipment(int no, float x, float y)
 *         Create an instance of the shipment with the required information
 *         for the VRP problem and VRPTW problem depot coordinate information.
 *      int getEarliestTime()
 *         Get the earliest time of the shipment.
 *      int getLatestTime()
 *         Get the latest time of the shipment.
 *      int getServeTime()
 *         Get the service time of the shipment.
 *
 *   DepotLinkedList:
 *      public void cyclic2AC1T(int maxLoop)
 *         Exchange 01 nodes between the 2 different routes then reassign the head pointer
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic2C1T(int maxLoop)
 *         Exchange 01 nodes between the 2 different routes then cycle back to that node
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic2C2T(int maxLoop)
 *         Exchange 02 nodes between the 2 different routes then cycle back to that node
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic3AC1T(int maxLoop)
 *         Exchange 01 nodes between the 3 different routes then reassign the head pointer
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic3C1T(int maxLoop)
 *         Exchange 01 nodes between the 3 different routes then cycle back to the head node
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclicVAC1T(int maxLoop)
 *         Exchange 01 nodes between the v different routes then reassign the head pointer
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclicVC1T(int maxLoop)
 *         Exchange 01 nodes between the v different routes then cycle back to that node
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclicVC2T(int maxLoop)
 *         Exchange 02 nodes between the n different routes then cycle back to the head node
 *         This method was modified from code taken from the MDVRPTW problem
 *      void displayForwardKeyListVRPTW()
 *         Display all the information in the the linked list of Depots for the VRPTW
 *         problem. Each depot is called with the displayDepotVRPTW method followed by
 *         each the instance of the TruckLinkedList mainTrucks being called with the
 *         displayForwardKeyListVRPTW method.
 *      float getTotalExcessTime()
 *         Calculate the total excess time for all the trucks
 *      float getTotalOverload()
 *         Calculate the total overload for all the trucks
 *      float getTotalServiceTime()
 *         Calculates the overall total service time for all the trucks
 *      float getTotalTardiness()
 *         Calculate the overall tardiness for all the trucks
 *      float getTotalTravelTime()
 *         Calculates the overall total travel time for all the trucks
 *      float getTotalWaitTime()
 *         Calculates the overall total wait time for all the trucks
 *      boolean insertShipmentVRPTW(Shipment theShip, int depotNo)
 *         Depot level insert function for the VRPTW modified to utilize the
 *         cost function interface
 *
 *   Depot:
 *      float calcTotalDemand()
 *         calculate and set total demand for all trucks in depot
 *      float calcTotalDuration()
 *         calculate and set total distance for depot
 *      float calcTotalExcessTime()
 *         calculate and set total excess time for all trucks in depot
 *      void calcTotalNonEmptyTrucks()
 *         calculate and set the total number of non-empty trucks for the depot
 *      float calcTotalOverload()
 *         calculate and set total overload for all trucks in depot
 *      float calcTotalServTime()
 *         calculate and set total service time for all trucks in depot
 *      float calcTotalTardiness()
 *         calculate and set total tardiness for all trucks in depot
 *      float calcTotalTravelTime()
 *         calculate and set total travel time for all trucks in depot
 *      void calcTotalTrucks()
 *         calculate and set the total number of trucks
 *      float calcTotalWaitTime()
 *         calculate and set total wait time for all trucks in depot
 *      void displayDepotVRPTW()
 *         Display the depot information for a VRPTW problem to a console. This method
 *         will display the number of non-empty trucks, duration of trucks and total demand.
 *      float getTotalDemand()
 *         Get the total demand for all the trucks
 *      float getTotalDuration()
 *         Get the total distance for all the trucks
 *      float getTotalExcessTime()
 *         Get the total excess time for all the trucks
 *      int getTotalNonEmptyTrucks()
 *         return the total number of non-empty trucks for the depot
 *      float getTotalOverload()
 *         Get the total overload for all the trucks
 *      float getTotalServTime()
 *         Get the total service time for all the trucks
 *      float getTotalTardiness()
 *         Get the total tardiness time for all the trucks
 *      float getTotalTravelTime()
 *         Get the total travel time for all the trucks
 *      int getTotalTrucks()
 *         return the total number of trucks
 *      float getTotalWaitTime()
 *         Get the total wait time for all the trucks
 *      float setTotalExcessTime(float num)
 *         Set the total excess time for the current truck
 *      float setTotalOverload(float num)
 *         Set the total overload for the current truck
 *      float setTotalServTime(float num)
 *         Set the total service time for the current truck
 *      float setTotalTardiness(float num)
 *         Set the total tardiness time for the current truck
 *      float setTotalTravelTime(float num)
 *         Set the total travel time for the current truck
 *      float setTotalWaitTime(float num)
 *         Set the total wait time for the current truck
 *
 *   TruckLinkedList:
 *      boolean calcSwapMNChain(VisitNodesLinkedList p, VisitNodesLinkedList q)
 *         checks for any violation of constraints after a swap of m nodes from one list
 *         with n nodes from another, modified to deal with tardiness and excess time
 *         from the original version written by Jennifer Davis and Ethan Fry
 *      void calculateTotalDemandOfTrucks()
 *         Compute the total demand for all the trucks in the list
 *      void calculateTotalDistanceOfTrucks()
 *         Compute the total distance traveled by all the trucks in the list
 *      boolean close(PointCell p, PointCell q, double distWeight, double timeWeight, double range)
 *         calculates how closely related two nodes are from different lists
 *      public void cyclic2AC1T(int maxLoop)
 *         2 a-cyclic, 1 transfer exchange; p exchanges with q, but not back from q -> p.
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic2C1T(int maxLoop)
 *         2 cycles, 1 transfer exchange; exchanging between
 *         two customers in a complete circle.
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic2C2T(int maxLoop)
 *         2 cycles, 2 transfer exchange; exchanging between
 *         two customers in a complete circle. Exchanges 2 customers with 2 routes.
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic3AC1T(int maxLoop)
 *         3 a-cyclic, 1 transfer; A node from route one will be inserted into route 2
 *         while a node from route 2 will be inserted into route 3. In the next iteration,
 *         the exchanges will start from route 2, and so on...
 *         There must be at least 3 truck routes in order to perform this
 *         otherwise you will recieve a null pointer, because you are trying to exchange
 *         with a route that does not exist.
 *         If there is not enough routes, then it returns.
 *         This does not cycle back up from route 3 - route 1 because this is a A-cyclic transfer
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclic3C1T(int maxLoop)
 *         3 cycles, 1 transfer; A node from route one will be inserted into route 2
 *         while a node from route 2 will be inserted into route 3. In the next iteration,
 *         the exchanges will start from route 2, and so on...
 *         There must be at least 3 truck routes in order to perform this
 *         otherwise you will recieve a null pointer, because you are trying to exchange
 *         with a route that does not exist.
 *         If there is not enough routes, then it returns.
 *         This method was modified from code taken from the MDVRPTW problem
 *      VisitNodesLinkedList[] cyclicPQchooser()
 *         Iterates through the list of trucks in a cyclic manner to find two lists
 *         of customers to be swapped.  The list of one truck will be the first list
 *         and will have m nodes from it swapped with n nodes from the list of the second truck
 *      public void cyclicVAC1T(int maxLoop)
 *         v a-cyclic, 1 transfer; A node from route one will be inserted into route 2
 *         while a node from route 2 will be inserted into route 3, a node from route 3
 *         will be inserted...a node from route v - 1 will be inserted into route v.
 *         In the next iteration, the exchanges will start from route 2, and so on...
 *         This does not cycle back up from route v - route 1 because this is a A-cyclic transfer
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclicVC1T(int maxLoop)
 *         v cycles, 1 transfer; A node from route one will be inserted into route 2
 *         while a node from route 2 will be inserted into route 3, a node from route 3
 *         will be inserted...a node from route v - 1 will be inserted into route v.
 *         In the next iteration, the exchanges will start from route 2, and so on...
 *         This method was modified from code taken from the MDVRPTW problem
 *      public void cyclicVC2T(int maxLoop)
 *         v cycles, 2 transfer; two nodes from route one will be inserted into route 2
 *         while two nodes from route 2 will be inserted into route 3, two nodes from route 3
 *         will be inserted...two nodes from route v - 1 will be inserted into route v.
 *         In the next iteration, the exchanges will start from route 2, and so on...
 *         This method was modified from code taken from the MDVRPTW problem
 *      boolean exchange01(VisitNodesLinkedList p, VisitNodesLinkedList q)
 *         Move one customer from route p to route q.  Modified from original to handle
 *         time window constraints.
 *      boolean exchange02(VisitNodesLinkedList p, VisitNodesLinkedList q)
 *         Move two customers from route p to route q.  Modified from original to handle
 *         time window constraints.
 *      boolean exchange11(VisitNodesLinkedList p, VisitNodesLinkedList q)
 *         Exchange one customer between two trucks.  Modified from original to handle
 *         time window constraints.
 *      boolean exchange12(VisitNodesLinkedList p, VisitNodesLinkedList q)
 *         Move two customers from route p to route q and one customer from route q to p.
 *         Modified from original to handle time window constraints.
 *      boolean exchange22(VisitNodesLinkedList p, VisitNodesLinkedList q)
 *         Move two customers from route p to route q and two customer from route q to p.
 *         Modified from original to handle time window constraints.
 *      void localOptVRPTW()
 *         Use local optimization for the trucks in the list. This method invokes k-1-opt,
 *         k-2-opt and k-3-opt.  This is just a modified version of the localOpt method.
 *      boolean swapMNchains(int m, int n)
 *         Find a chain of m nodes in list p and swap with n nodes in list q.  This
 *         should reduce the number of route crossovers which will reduce the total
 *         distance for each route.  Swaps will only be performed if the constraints
 *         are upheld.  Some of the code for this method came from a version of this
 *         method written by Jennifer Davis and Ethan Fry.
 *
 *   Truck:
 *      float getDemand()
 *         Returns the current demand for the truck
 *      float getDistance()
 *         Returns the current distance traveled by the truck
 *      void writeVRPTWDetailTruck(PrintWriter solOutFile)
 *         Write in detail form the information on the truck number and details of
 *         the truck to an output file.  This is a modified version of writeDetailTruck
 *         method to handle time window information.
 *      void writeVRPTWShortTruck(int depotNo, PrintWriter solOutFile)
 *         Write in short form the information on the depot number truck number and details of
 *         the truck to an output file.  This is a modified version of writeDetailTruck
 *         method to handle time window information.
 *
 *   VisitNodesLinkedList:
 *      public synchronized String calcExchange01(String st1)
 *         Calculate the cost of exchange01 for shipment st1. The
 *         function used to calculate the cost is calcExchCostMDVRP(); The shipment st1 is
 *         removed and the differential cost is returned. St1 is inserted in the same location.
 *      public synchronized String calcExchange02(String st1, String st2)
 *         Calculate the cost of exchange02 for a MDVRP with the shipments st1 and st2. The
 *         function used to calculate the cost is calcExchCostMDVRP(); Remove st1 and st2.
 *         Calculate and return the cost differential before and after the removal. Insert
 *         st1 and st2 into the route.
 *      public synchronized String calcExchange10(String st1)
 *         Calculate the cost of exchange10 for a MDVRP shipment st1. The
 *         function used to calculate the cost is calcExchCostMDVRP(); Shipment
 *         st1 is inserted into the current route and the cost difference before
 *         and after the insertion and the best location of insertion is returned.
 *      public synchronized String calcExchange11(String st1, String st2)
 *         Calculate the cost of exchange11 for a MDVRP with the cells st1 and st2. The
 *         function used to calculate the cost is calcExchCostMDVRP(). Remove shipment st1.
 *         Compute the exchange cost of inserting shipment st2 and then reinsert shipment st1.
 *      public synchronized String calcExchange12(String st1, String st2, String st3)
 *         Calculate the cost of exchange12 for a MDVRP with the shipments st1, st2 and st3. The
 *         function used to calculate the cost is calcExchCostMDVRP(); Remove st2 and st3. Insert st1
 *         into the current route. Reinsert shipments st2 and st3 back into the routes.
 *         Return the difference in cost and the best location for insertion of st1.
 *      public synchronized String calcExchange20(String st1, String st2)
 *         Calculate the cost of exchange20 for a MDVRP with the shipments st1 and st2. The
 *         function used to calculate the cost is calcExchCostMDVRP(); Insert st1 and st2 and
 *         computing the cost of the route. Return the differential in cost and the best
 *         locations for inserting shipment st1 and st2. Remove st1 from the route.
 *      public synchronized String calcExchange21(String st1, String st2, String st3)
 *         Calculate the cost of exchange21 for a MDVRP with the shipments st1, st2 and st3. The
 *         function used to calculate the cost is calcExchCostMDVRP(); Remove st3. Calculate
 *         the cost of inserting st1 and st2. Shipment st3 is reinserted into the route.
 *         Calculate the total cost differential for
 *         the whole process then reinsert st3 back into the route.
 *      public synchronized String calcExchange22(String st1, String st2, String st3, String st4)
 *         Calculate the cost of exchange22 for a MDVRP with the shipments st1,st2, st3 and st4. The
 *         function used to calculate the cost is calcExchCostMDVRP(); Remove st3 and st4.
 *         Insert shipment st1 and calculate the exchange cost of insertion st2. Return
 *         the cost differential and the best location for inserting shipments st1 and st2.
 *         Remove shipment st1 and reinsert shipment st3 and st4 in original locations. The
 *         insertion of two shipments does not guarantee that they will be consecutive. The best
 *         locations for inserting them are located.
 *      public int calcPushCost(int iIndex, float lX, float lY, float lDemand, float lEar, float lLat, float lServ)
 *         Calculates the added time a node will create in a route if inserted and
 *         compares it to the successors slack time to determine feasibility
 *      public void displayForwardKeyListVRP()
 *         Display all the information in the linked list for the VRP problem.
 *         The information displayed are the duration, capacity and shipment/
 *         id's in the linked  list.
 *      public synchronized void exchange01(String st1)
 *         Perform the permanent exchange01 of shipment st1. Remove
 *         shipment st1 from current route.
 *      public synchronized void exchange02(String st1, String st2)
 *         Perform the permanent exchange02 of shipments st1 and st2. Remove
 *         shipments st1 and st2.
 *      public synchronized void exchange10(String st1, int after_index)
 *         Perform the permanent exchange10 of shipments st1. Insert shipment st1
 *         into location after_index. Update the currentDuration and currentCapacity.
 *      public synchronized void exchange11(String st1, String st2, int after_index)
 *         Perform the permanent exchange11 of shipments st1 and st2. Remove
 *         the shipment st1 and and insert shipment st2  in location after_index.
 *      public synchronized void exchange12(String st1, int after_index1, String st2, String st3)
 *         Perform the permanent exchange12 of shipments st1, st2 and st3. Remove
 *         shipments st2 and st3 and insert shipment st1 in location after_index.
 *      public synchronized void exchange20(String st1, int after_index1, String st2, int after_index2)
 *         Perform the permanent exchange20 of shipments st1 and st2. Insert
 *         shipments st1 and st2 into locations after_index1 and after_index2
 *         respectively.
 *      public synchronized void exchange21(String st1, int after_index1, String st2, int after_index2, String st3)
 *         Perform the permanent exchange21 of shipments st1, st2 and st3. Remove
 *         shipment st3 and insert shipment st1 and st2 in locations after_index1
 *         and after_index2.
 *      public synchronized void exchange22(String st1, int after_index1, String st2, int after_index2, String st3, String st4)
 *         Perform the permanent exchange22 of shipments st1, st2, st3 and st4.
 *         Remove shipments st3 and st4 and insert shipments st1 and st2.
 *      public synchronized String exchInsertCost(int iIndex, float lX, float lY, float lDemand, float lEar, float lLat, float lServ)
 *         Calculate the exchange cost for the VRPTW problem
 *      public synchronized float getTotalExcessTime()
 *         return the current excess time of the truck
 *      public synchronized float getTotalOverload()
 *         return the current total overload of the truck
 *      public synchronized float getTotalTardiness()
 *         return the current total tardiness of the truck
 *      public synchronized PointCell insert(int iIndex, float lX, float lY, float lDemand, float lEar, float lLat, float lServ, int kIndex)
 *         Insert a shipment,iIndex, node for the VRPTW problem
 *         after the shipment index kIndex
 *      public synchronized PointCell insertAfterCellVRPTW(PointCell afterCellPtr, PointCell newCell)
 *         Insert a customer into the linked list after the afterCellPtr node for
 *         the VRPTW problem
 *      public boolean insertVRPTW(int index, float x, float y, float demand, int earTime, int latTime, int servTime)
 *         VisitNodesLinkedList level insert function for VRPTW. Loops through all possible
 *         places to insert shipment and chooses the one with the lowest cost
 *      public synchronized boolean kInterChange1()
 *         Locally optimize the route using the k-intercahnge k-opt-1 optimization. In the
 *         k-interchange optimization switches are performed without reversing the direction
 *         of the route. The k-opt-1 is the same as the 1-opt method.
 *      public synchronized boolean kInterChange2()
 *         Locally optimize the route using the k-intercahnge k-opt-2 optimization. In the
 *         k-interchange optimization switches are performed without reversing the direction
 *         of the route.
 *      public synchronized boolean kInterChange3()
 *         Locally optimize the route using the k-intercahnge k-opt-3 optimization. In the
 *         k-interchange optimization switches are performed without reversing the direction
 *         of the route.
 *      public void performRecalculates()
 *         method to group the recalculation methods
 *         such as: recalculate_arrTime(), recalculate_lt(),
 *         recalculateAC_DC(), recalculateSlackTime()
 *      public synchronized String pointString(int iNodeNum)
 *         Transform all attributes of the point into the string
 *      public synchronized void setTotalExcessTime(float excess)
 *         set total excess time of the truck
 *      public synchronized void setTotalOverload(float overload)
 *         set the current total overload of the truck
 *      public synchronized void setTotalTardiness(float tardiness)
 *         set current total tardiness of the truck
 *      public synchronized void setTotalTime(float time)
 *         set the total route time of the truck
 *      public synchronized String toString(PointCell p)
 *         Display the detail attributes of the cell for the VRPTW problem
 *      public void writeDetailNodesSol(PrintWriter solOutFile)
 *         Write out the detail form of the cell information for all shipments in
 *         the list into the solOutFile file.
 *      private synchronized void recalculateSlackTime()
 *         Recalculating the slackTime for each customer from the currently
 *         inserted customer to the end
 *
 *   PointCell:
 *      PointCell(int iIndex, float lX, float lY, float lDemand, int eTime, int lTime, int sTime)
 *         Constructor for initializing values for the VRPTW
 *      float dist(float x1, float x2, float y1, float y2)
 *         Calculate the Euclidean distance between two points, copied from an existing
 *         method from another class.
 *      float getSlackTime()
 *         Returns the slack time for a point cell
 *      void writePointCell(PrintWriter solOutFile)
 *         Write out the detailed form of the cell information into the solOutFile file.
 *         Modifications include added detail corresponding to the VRPTW problem.
 *
 * New methods that were added to or changed in the CostFunctions package:
 *   CostFunctions:
 *      public boolean calcCloseness(Object o, Object p, Object q, int x)
 *         calculates the closeness of two objects relative to certain constraints, such
 *         as time windows, distance, and capacity
 *      public double calculateOptimizedCost(Object o)
 *         calculate the cost for the given object.
 *      public double getAngle(Object o)
 *         must return the angle associated with a node in the given object
 *      public double getMedianTime(Object o)
 *         must return the median time associated with a node in the given object
 *      public double getTotalExcessTime(Object o)
 *         must return the total excess time of the given object
 *      public double getTotalNodes(Object o)
 *         must return the total number of nodes of the given object
 *      public double getTotalNonEmptyNodes(Object o)
 *         must return the total number of non empty nodes of the given object
 *      public double getTotalOverload(Object o)
 *         must return the total overload of the given object
 *      public double getTotalServiceTime(Object o)
 *         must return the total service time of the given object
 *      public double getTotalTardinessTime(Object o)
 *         must return the total tardiness time of the given object
 *      public double getTotalWaitTime(Object o)
 *         must return the total wait time of the given object
 *      public double globalCalculateOptimizedCost(Object o)
 *         calculate the cost of a global exchange for the given object.
 *      public void setTotalExcessTime(Object o)
 *         sets the total excess time associated with the object. This method may need to make
 *         use of other methods to retrieve the required excess time values.
 *      public void setTotalNonEmptyNodes(Object o)
 *         sets the total number of non Empty nodes associated with the object. This
 *         method may need to make use of other methods to retrieve the required
 *         number of nodes.
 *      public void setTotalOverload(Object o)
 *         sets the total overload time associated with the object. This method may need to make
 *         use of other methods to retrieve the required overload time values.
 *      public void setTotalServiceTime(Object o)
 *         sets the total service time associated with the object. This method may need to make
 *         use of other methods to retrieve the required service time values.
 *      public void setTotalTardinessTime(Object o)
 *         sets the total tardiness time associated with the object. This method may need to make
 *         use of other methods to retrieve the required tardiness time values.
 *      public void setTotalWaitTime(Object o)
 *         sets the total wait time associated with the object. This method may need to make
 *         use of other methods to retrieve the required wait time values.
 *      public void setWeights(int caseNo)
 *         Sets the cost metric weight variables according to the case number
 *      public void swapMNchains(Object o)
 *         swaps m nodes from a list with n nodes from a different list
 *
 * New classes added or classes modified to the CostFunctions package and methods used:
 *    BasicShipmentLLCostFunctions (added):
 *       public double getAngle(Object o)
 *          Returns the angle for a node in relation to the depot in the object
 *       public double getMedianTime(Object o)
 *          Returns the median time for a node in the object
 *       public double getTotalCost(Object o)
 *          Returns the total cost of inserting a node in the depot linked list
 *       public double getTotalDistance(Object o)
 *          Returns the distance between two nodes in the object
 *       public void setTotalCost(Object o)
 *          Sets the cost associated with inserting a node into a depot linked list
 *
 *    BasicShipmentCostFunctions (added):
 *       None.
 *
 *    BasicDepotLLCostFunctions:
 *       public void calculateTotalsStats(Object o)
 *          Calls all of the set value methods to calculate and set all of the
 *          stats of an object
 *       public double getTotalDemand(Object o)
 *          Returns the total demand for the object
 *       public double getTotalDistance(Object o)
 *          Returns the total distance for the object
 *       public double getTotalExcessTime(Object o)
 *          Returns the total excess time for the object
 *       public double getTotalNonEmptyNodes(Object o)
 *          Returns the total number of non empty nodes for the object
 *       public double getTotalOverload(Object o)
 *          Returns the total overload for the object
 *       public double getTotalServiceTime(Object o)
 *          Returns the total service time for the object
 *       public double getTotalTardinessTime(Object o)
 *         Returns the total tardiness time for the object
 *       public double getTotalTravelTime(Object o)
 *          Returns the total travel time for the object
 *       public double getTotalWaitTime(Object o)
 *          Returns the total wait time for the object
 *       public void setTotalDemand(Object o)
 *          Sets the total demand for the object
 *       public void setTotalDistance(Object o)
 *          Sets the total distance for the object
 *       public void setTotalExcessTime(Object o)
 *          Sets the total excess time for the object
 *       public void setTotalNonEmptyNodes(Object o)
 *          Sets the total number of non empty nodes for the object
 *       public void setTotalOverload(Object o)
 *          Sets the total overload for the object
 *       public void setTotalServiceTime(Object o)
 *          Sets the total service time for the object
 *       public void setTotalTardinessTime(Object o)
 *          Sets the total tardiness time for the object
 *       public void setTotalTravelTime(Object o)
 *          Sets the total travel time for the object
 *       public void setTotalWaitTime(Object o)
 *          Sets the total wait time for the object
 *       public void setWeights(int caseNo)
 *          Sets the cost metric weight variables according to the case number
 *
 *    BasicDepotCostFunctions:
 *       public void calculateTotalsStats(Object o)
 *          Calls all of the set value methods to calculate and set all of the
 *          stats of an object
 *       public double getTotalDemand(Object o)
 *          Returns the total demand for the object
 *       public double getTotalDistance(Object o)
 *          Returns the total distance for the object
 *       public double getTotalExcessTime(Object o)
 *          Returns the total excess time for the object
 *       public double getTotalOverload(Object o)
 *          Returns the total overload for the object
 *       public double getTotalServiceTime(Object o)
 *          Returns the total service time for the object
 *       public double getTotalTardinessTime(Object o)
 *         Returns the total tardiness time for the object
 *       public double getTotalTravelTime(Object o)
 *          Returns the total travel time for the object
 *       public double getTotalWaitTime(Object o)
 *          Returns the total wait time for the object
 *       public void setTotalDemand(Object o)
 *          Sets the total demand for the object
 *       public void setTotalDistance(Object o)
 *          Sets the total distance for the object
 *       public void setTotalExcessTime(Object o)
 *          Sets the total excess time for the object
 *       public void setTotalOverload(Object o)
 *          Sets the total overload for the object
 *       public void setTotalServiceTime(Object o)
 *          Sets the total service time for the object
 *       public void setTotalTardinessTime(Object o)
 *          Sets the total tardiness time for the object
 *       public void setTotalTravelTime(Object o)
 *          Sets the total travel time for the object
 *       public void setTotalWaitTime(Object o)
 *          Sets the total wait time for the object
 *
 *
 *    BasicTruckLLCostFunctions:
 *       public boolean calcCloseness(Object o, Object p, Object q, int type)
 *          Calculates the relative closeness of two nodes from different list objects.
 *          Closeness is relative to distance and time, and must be within a defineable range
 *       public void calculateTotalsStats(Object o)
 *          Calls all of the set value methods to calculate and set all of the
 *          stats of an object
 *       public double getTotalCost(Object o)
 *          Returns the total cost for the object
 *       public double getTotalDemand(Object o)
 *          Returns the total demand for the object
 *       public double getTotalDistance(Object o)
 *          Returns the total distance for the object
 *       public double getTotalExcessTime(Object o)
 *          Returns the total excess time for the object
 *       public double getTotalNodes(Object o)
 *          Returns the total number of nodes for the object
 *       public double getTotalNonEmptyNodes(Object o)
 *          Returns the total number of non empty nodes for the object
 *       public double getTotalOverload(Object o)
 *          Returns the total overload for the object
 *       public double getTotalServiceTime(Object o)
 *          Returns the total service time for the object
 *       public double getTotalTardinessTime(Object o)
 *         Returns the total tardiness time for the object
 *       public double getTotalTravelTime(Object o)
 *          Returns the total travel time for the object
 *       public double getTotalWaitTime(Object o)
 *          Returns the total wait time for the object
 *       public void setTotalCost(Object o)
 *          Sets the total cost for the object
 *       public void setTotalDemand(Object o)
 *          Sets the total demand for the object
 *       public void setTotalDistance(Object o)
 *          Sets the total distance for the object
 *       public void setTotalExcessTime(Object o)
 *          Sets the total excess time for the object
 *       public void setTotalNonEmptyNodes(Object o)
 *          Sets the total number of non empty nodes for the object
 *       public void setTotalOverload(Object o)
 *          Sets the total overload for the object
 *       public void setTotalServiceTime(Object o)
 *          Sets the total service time for the object
 *       public void setTotalTardinessTime(Object o)
 *          Sets the total tardiness time for the object
 *       public void setTotalTravelTime(Object o)
 *          Sets the total travel time for the object
 *       public void setTotalWaitTime(Object o)
 *          Sets the total wait time for the object
 *       public void swapMNchains(Object o)
 *          Will swap m nodes from one list object with n nodes from another
 *
 *    BasicTruckCostFunctions:
 *       public void calculateTotalsStats(Object o)
 *          Calls all of the set value methods to calculate and set all of the
 *          stats of an object
 *       public double getTotalDemand(Object o)
 *          Returns the total demand for the object
 *       public double getTotalDistance(Object o)
 *          Returns the total distance for the object
 *       public double getTotalExcessTime(Object o)
 *          Returns the total excess time for the object
 *       public double getTotalOverload(Object o)
 *          Returns the total overload for the object
 *       public double getTotalServiceTime(Object o)
 *          Returns the total service time for the object
 *       public double getTotalTardinessTime(Object o)
 *         Returns the total tardiness time for the object
 *       public double getTotalTravelTime(Object o)
 *          Returns the total travel time for the object
 *       public double getTotalWaitTime(Object o)
 *          Returns the total wait time for the object
 *       public void setTotalDemand(Object o)
 *          Sets the total demand for the object
 *       public void setTotalDistance(Object o)
 *          Sets the total distance for the object
 *       public void setTotalExcessTime(Object o)
 *          Sets the total excess time for the object
 *       public void setTotalOverload(Object o)
 *          Sets the total overload for the object
 *       public void setTotalServiceTime(Object o)
 *          Sets the total service time for the object
 *       public void setTotalTardinessTime(Object o)
 *          Sets the total tardiness time for the object
 *       public void setTotalTravelTime(Object o)
 *          Sets the total travel time for the object
 *       public void setTotalWaitTime(Object o)
 *          Sets the total wait time for the object
 *
 *
 *    BasicVNodesCostFunctions:
 *       public double calculateOptimizedCost(Object o)
 *          Calculates the cost to perform a local optimization.
 *          Uses getTotalDistance to aid in the calculation.
 *       public void calculateTotalsStats(Object o)
 *          Calls all of the set value methods to calculate and set all of the
 *          stats of an object
 *       public double getTotalCost(Object o)
 *          Returns total distance as cost.
 *          It uses getTotalDistance method.
 *       public double getTotalDemand(Object o)
 *          Returns the total demand for the object
 *       public double getTotalDistance(Object o)
 *          Returns the total distance for the object
 *       public double getTotalExcessTime(Object o)
 *          Returns the total excess time for the object
 *       public double getTotalNodes(Object o)
 *          Returns the total number of nodes for the object
 *       public double getTotalNonEmptyNodes(Object o)
 *          Returns the total number of non empty nodes for the object
 *       public double getTotalOverload(Object o)
 *          Returns the total overload for the object
 *       public double getTotalServiceTime(Object o)
 *          Returns the total service time for the object
 *       public double getTotalTardinessTime(Object o)
 *         Returns the total tardiness time for the object
 *       public double getTotalTravelTime(Object o)
 *          Returns the total travel time for the object
 *       public double getTotalWaitTime(Object o)
 *          Returns the total wait time for the object
 *       public double globalCalculateOptimizedCost(Object o)
 *          Calculates the cost to perform a global optimization.
 *          Uses getTotalDistance to aid in the calculation.
 *       public void setTotalCost(Object o)
 *          Sets the total cost for the object
 *       public void setTotalDemand(Object o)
 *          Sets the total demand for the object
 *       public void setTotalDistance(Object o)
 *          Sets the total distance for the object
 *       public void setTotalExcessTime(Object o)
 *          Sets the total excess time for the object
 *       public void setTotalNonEmptyNodes(Object o)
 *          Sets the total number of non empty nodes for the object
 *       public void setTotalOverload(Object o)
 *          Sets the total overload for the object
 *       public void setTotalServiceTime(Object o)
 *          Sets the total service time for the object
 *       public void setTotalTardinessTime(Object o)
 *          Sets the total tardiness time for the object
 *       public void setTotalTravelTime(Object o)
 *          Sets the total travel time for the object
 *       public void setTotalWaitTime(Object o)
 *          Sets the total wait time for the object
 *</pre>
 *
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description:  The VRPTW, Vehicle Routing Problem with Time Windows, consists of one
 *    depot used in solving a routing problem. A set of customers are to be serviced from
 *    the depot using trucks. The objective of the problem
 *    is to service the customers by first minimizing the total number of trucks
 *    and second minimizing the total distance traveled by the trucks. The constraints
 *    is not to exceed the total capacity of the truck and to service each customer
 *    within a their given time window. </p>
 * <p> This class added 9/4/03 by Mike McNamara
 *     Modified by Sunil Gurung 9/12/03</p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:<p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class VRPTW {
    int truckCount = 0; //number of trucks
    int customerCount = 0; //number of customers
    int numDepots = 0; // number of depots, will be 1 for VRPTW problem
    int t = 0; //travel time of trucks
    int D = 0; //maximum distance of vehicle
    int Q = 0; //maximum capacity of vehicle
    int heuristicType = 4;
    public int totalOneOpts = 0; // total number of one node optimizations performed
    public int totalTwoOpts = 0; // total number of two node optimizations performed
    public int totalThreeOpts = 0; // total number of three node optimizations performed
    boolean isDiagnostic = false;
    Shipment tempShip;
    int depotNo;
    int countAssignLoop;
    boolean status;
    String outputFileName;

    // these next two variables are from the MDVRPTW problem
    int v = 2; //variable depth for vC1T
    int maxLoop = 2; /*variable to control how many times the while
    loop at the TruckLinkList level will try to
    control the how many customers are compared to
    the customer trying to fit into the new route*/

    /** Customers read in from the data file are loaded into the ShipmentLinkedList
    *  instance named mainShipments. The mainShipments instance holds all of the
    *  shipments that are avaialble for routing. This list should not be used
    *  for manipulating the problem being solved. The list can be updated if a
    *  customer is added, deleted or edited.
    */
    public ShipmentLinkedList mainShipments = new ShipmentLinkedList();

    /** The DepotLinkedList with instance mainDepots forms the root node that keeps
    *  track of the solution that has been obtained for the problem. From the mainDepots
    *  one can obtain the list of trucks and for each truck the list of customers for
    *  the problem.
    */
    public DepotLinkedList mainDepots = new DepotLinkedList();

    /**
    * Default constructor
    */
    public VRPTW() {
        ProblemInfo.depotLLLevelCostF.setWeights(0); // set the default weights
    }

    /**
    * <p>Constructor for the VRPTW problem. Reads in the data from the file.
    * Creates the depot.
    * Allocates the customers to the trucks.</p>
    * @param fileName Name of the file to be used for the problem
    */
    public VRPTW(String fileName) {
        Depot thisDepot;

        // read in the data from the file given as tokens
        readVRPTWTokenFileData(fileName);

        /* The customers and depots are read into the mainShipments
        shipment linked list. The createDepots method in the
        mainDepots instance creates a linked list of
        depots using the depot information in the mainShipments linked
        list. The linked list is accessible through the mainDepots instance.
        The VRPTW problem only has one depot so the linked list will contain
        only one node.
        */
        mainDepots.createDepots(mainShipments);

        //      System.out.print(mainDepots.getNoDepots());
        /* Get the shipment that is closest to a depot with respect to the
        criteria
        The method for assinging customers to the depots is as follows.
        Iterate throught the customer list inserting the customer to the truck,
        as long as it does not violate the constraints.  If it does, create
        another truck and insert the customer there.  Customers will be inserted
        into the trucks in a position that yields the lowest cost until all
        the customers have been serviced.
        */
        // get depot number to assign customers to, will always be 1 for VRPTW problem
        thisDepot = mainDepots.getFirst();
        depotNo = thisDepot.getDepotNo();

        // start iteration of list at the beginning
        tempShip = mainShipments.first;

        //loop while all shipments have not been assigned to the depot
        for (countAssignLoop = 0;
                (countAssignLoop < customerCount) && (tempShip != null);
                countAssignLoop++) {
            //Getting the closest shipment for the depot using the heuristic search
            tempShip = getClosestShipmentVRPTW(heuristicType);
            status = mainDepots.insertShipmentVRPTW(tempShip, depotNo); //add the shipment to the depot

            if (status == true) {
                tempShip.assigned = true;
            }

            if ((status != true) && isDiagnostic) {
                System.out.println("VRPTW: Shipment could not be inserted");
            }

            if (isDiagnostic) {
                System.out.println("The depot  and shipment is      : " +
                    depotNo + " " + tempShip.shipNo);
            }

            if ((countAssignLoop % 5) == 0) {
                runOptimize();
            }
        }

        //end for
        //Update all the status in the CostFunctions interface
        upDateCostFunctions();

        //optimize solution
        runOptimize();
        upDateCostFunctions();

        //At this point all shipments have been assigned
        //        mainDepots.displayForwardKeyListVRPTW();
        //Calculating the number of non empty trucks
        ProblemInfo.depotLLLevelCostF.setTotalNonEmptyNodes(mainDepots);

        //        System.out.println("Total number of trucks:    " +
        //            (int) ProblemInfo.depotLLLevelCostF.getTotalNonEmptyNodes(
        //                mainDepots));
        //Calculation the total capacity
        ProblemInfo.depotLLLevelCostF.setTotalDemand(mainDepots);

        //        System.out.println("Total demand for trucks:   " +
        //            ProblemInfo.depotLLLevelCostF.getTotalDemand(mainDepots));
        //
        //Calculating the total distance
        ProblemInfo.depotLLLevelCostF.setTotalDistance(mainDepots);

        /****************Sunil 10/1/03*****************************************/
        ProblemInfo.overAllDemand = (float) ProblemInfo.depotLLLevelCostF.getTotalDemand(mainDepots);
        ProblemInfo.overAllTotalWaitTime = (float) ProblemInfo.depotLLLevelCostF.getTotalWaitTime(mainDepots);
        ProblemInfo.overAllTotalTravelDistance = (float) ProblemInfo.depotLLLevelCostF.getTotalTravelTime(mainDepots);
        ProblemInfo.overAllServiceTime = (float) ProblemInfo.depotLLLevelCostF.getTotalServiceTime(mainDepots);
        ProblemInfo.overAllTotalDistance = (float) ProblemInfo.depotLLLevelCostF.getTotalDistance(mainDepots);
        ProblemInfo.overAllTotalTardiness = (float) ProblemInfo.depotLLLevelCostF.getTotalTardinessTime(mainDepots);
        ProblemInfo.overAllTotalExcessTime = (float) ProblemInfo.depotLLLevelCostF.getTotalExcessTime(mainDepots);
        ProblemInfo.overAllTotalOverload = (float) ProblemInfo.depotLLLevelCostF.getTotalOverload(mainDepots);

        System.out.println("Over all totalWaitTime =========== " +
            ProblemInfo.overAllTotalWaitTime);
        System.out.println("Over all totalTravelTime ========= " +
            ProblemInfo.overAllTotalTravelDistance);
        System.out.println("Over all totalServiceTime ======== " +
            ProblemInfo.overAllServiceTime);
        System.out.println("Over all totalDistance covered === " +
            ProblemInfo.overAllTotalDistance);
        System.out.println("Over all totalTardinessTime ====== " +
            ProblemInfo.overAllTotalTardiness);
        System.out.println("Over all totalExcessTime ========= " +
            ProblemInfo.overAllTotalExcessTime);
        System.out.println("Over all totalOverload =========== " +
            ProblemInfo.overAllTotalOverload);

        //Use the fileprefix of the filename to write out the output
        String filePrefix = fileName.substring(fileName.lastIndexOf("/") + 1);
        filePrefix = filePrefix.substring(0, filePrefix.lastIndexOf("."))
                               .toLowerCase();

        System.out.println(filePrefix);

        //Write the solution out to the solution file
        outputFileName = filePrefix + "_detailSolution.txt";
        writeVRPTWDetailSol(outputFileName);

        //Write out the short solution to the solution file
        outputFileName = filePrefix + "_shortSolution.txt";
        writeVRPTWShortSol(outputFileName);
    }

    // end Constructor

    /**
    * Calculate the cost of inserting a shipment into the schedule
    * @param tempShip  shipment to find cost for insertion
    * @return float  cost to insert shipment
    */
    public synchronized String calculateCost(Shipment tempShip) {
        Depot thisDepot;
        Truck tempTruck;
        float cost = 0;
        float finalCost = ProblemInfo.MAX_COST;
        int keepInd = -1;
        int ind = 0;
        int truckNo = -1;
        int keepTruckNo = -1;

        /*  The method for assinging customers to the depots is as follows.
        Insert the customer to the truck, as long as it does not violate
        the constraints.  If it does, create another truck and insert the
        customer there.  Customers will be inserted into the trucks in a
        position that yields the lowest cost.
        */
        // get depot number to assign customers to, will always be 1 for VRPTW problem
        thisDepot = mainDepots.getFirst();

        for (int i = 0; i < ProblemInfo.noOfDepots; i++) {
            depotNo = thisDepot.getDepotNo();

            status = mainDepots.insertShipmentVRPTW(tempShip, depotNo); //add the shipment to the depot

            if ((status != true) && isDiagnostic) {
                System.out.println("VRPTW: Shipment could not be inserted");
                cost = ProblemInfo.MAX_COST;
            } else if (!status) {
                cost = ProblemInfo.MAX_COST;
            } else {
                if (isDiagnostic) {
                    System.err.println("The depot  and shipment is      : " +
                        depotNo + " " + tempShip.shipNo);
                }

                //Update all the status in the CostFunctions interface
                upDateCostFunctions();

                //optimize solution
                //runLocalOpts();
                // extract cost
                cost = (float) ProblemInfo.depotLLLevelCostF.getTotalCost(mainDepots);

                // remove shipment from schedule
                tempTruck = thisDepot.getMainTrucks().getFirst();

                while (tempTruck != null) {
                    ind = tempTruck.mainVisitNodes.remove(tempShip.getShipNo());

                    if (ind != -1) {
                        truckNo = tempTruck.getTruckNo();

                        break;
                    }

                    tempTruck = tempTruck.next;
                }
            }

            if (thisDepot.next != null) {
                thisDepot = thisDepot.next;
            }

            if (cost < finalCost) {
                finalCost = cost;
                keepInd = ind;
                keepTruckNo = truckNo;
            }
        }

        //Update all the status in the CostFunctions interface
        upDateCostFunctions();

        //optimize solution
        //runLocalOpts();
        return finalCost + " " + keepInd + " " + keepTruckNo;
    }

    /**
    * Schudule the shipment
    * @param tempShip  shipment to schedule
    * @return boolean  status of the success of the scheduled shipment
    */
    public synchronized boolean scheduleShipment(Shipment tempShip) {
        Depot thisDepot;
        boolean status = false;

        /**
        * The method for assinging customers to the depots is as follows.
        *  Insert the customer to the truck, as long as it does not violate
        *  the constraints.  If it does, create another truck and insert the
        *  customer there.  Customers will be inserted into the trucks in a
        *  position that yields the lowest cost.
        */
        // get depot number to assign customers to, will always be 1 for VRPTW problem
        thisDepot = mainDepots.getFirst(); // THIS ALWAYS SCHEDULES TO THE FIRST DEPOT ONLY

        for (int i = 0; i < ProblemInfo.noOfDepots; i++) {
            depotNo = thisDepot.getDepotNo();

            status = mainDepots.insertShipmentVRPTW(tempShip, depotNo); //add the shipment to the depot

            if (!status && isDiagnostic) {
                System.err.println(
                    "VRPTW: Shipment could not be inserted into depot: " +
                    depotNo);
            } else if (status) {
                if (thisDepot.next != null) {
                    thisDepot = thisDepot.next;
                }

                break;
            }

            if (thisDepot.next != null) {
                thisDepot = thisDepot.next;
            }
        }

        if (isDiagnostic) {
            System.out.println("The depot  and shipment is: " + depotNo + " " +
                tempShip.shipNo);
        }

        //Update all the status in the CostFunctions interface
        upDateCostFunctions();

        //optimize solution
        //runLocalOpts();
        return status;
    }

    /**
    * <p>Read in the data from the datafile and load it into the mainShipments
    * ShipmentLinkedList using the tokenizer.</p>
    * @param VRPTWfileName String type of the filename consisting of the data
    * @return int Return 1 if all successful, 0 for failure
    */
    public int readVRPTWTokenFileData(String VRPTWfileName) {
        /* read in the VRPTW data from the listed file and load the information
        into the availShipments linked list
        */
        float x = 0; //x coordinate
        float y = 0; //y coordinate
        int earliestTime = 0; // earliest time to visit customer
        int latestTime = 0; // latest time to visit customer
        int servTime = 0; // time to service customer
        int index = 0; // index of customer
        int garbage; // to collect garbage values in data file

        //open the requested file
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;

        try {
            fis = new FileInputStream(VRPTWfileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        } catch (Exception e) {
            System.out.println("File is not present");

            return 0;
        }

        //String to hold the entire line read in
        String readLn;

        //StringTokenizer to obtain data in tokens
        StringTokenizer st;

        /* This reads in the first line that is used to determine the total
        number of customers, max distance trucks travel, max capacity
        and coordinates of the depot
        */
        try {
            //read in the first line
            readLn = br.readLine();

            //tokenize the first line
            st = new StringTokenizer(readLn);

            // first line read are labels, read in second line to get values
            if (readLn.substring(0, 2).trim().equalsIgnoreCase("x") ||
                    readLn.substring(0, 2).trim().equalsIgnoreCase("#x")) {
                readLn = br.readLine();
                st = new StringTokenizer(readLn);
            }

            //************************************************************************
            while (st.hasMoreTokens()) { //while there are more tokens

                switch (index) {
                case 0:
                    x = Float.parseFloat(st.nextToken());

                    break;

                case 1:
                    y = Float.parseFloat(st.nextToken());

                    break;

                case 2:
                    customerCount = Integer.parseInt(st.nextToken());

                    break;

                case 3:
                    numDepots = Integer.parseInt(st.nextToken());

                    break;

                case 4:
                    Q = Integer.parseInt(st.nextToken());

                    break;

                case 5:
                    D = Integer.parseInt(st.nextToken());

                    break;
                } //end switch

                index += 1;
            }

            //end while
        } catch (Exception e) {
            System.out.println("Line could not be read in");
        }

        //*************************************************************************
        //Put the problem information into the ProblemInfo class. This is used
        //to identify the file type and the information that was read in from
        //the input file. When printing the output this information can be
        //used to associate with the date file that was read in.
        ProblemInfo.fileName = VRPTWfileName; //name of the file being read in
        ProblemInfo.noOfShips = customerCount; //number of shipments
        ProblemInfo.depotX = x; // x coordinate of the depot
        ProblemInfo.depotY = y; // y coordinate of the depot

        if (Q == 0) { //if there is no maximum capacity, set it to a very large number
            Q = 999999999;
        }

        if (D == 0) { //if there is no maximum distance, set it to a very large number
            D = 999999999;
        }

        ProblemInfo.maxCapacity = Q; //maximum capacity of a vehicle
        ProblemInfo.maxDistance = D; //maximum distance of a vehicle

        //place the number of depots and number of shipments in the linked list instance
        mainShipments.noShipments = customerCount;
        mainShipments.noDepots = numDepots;
        mainShipments.maxCapacity = Q;
        mainShipments.maxDuration = D;

        // This section will get the customer information.
        int i = 0; // customer number
        int q = 0; // customer demand
        int runTimes; //total number of lines to read

        runTimes = customerCount;

        //This section will get the customers and related information
        try {
            readLn = br.readLine();

            /* The first for loop runtimes dependent upon how many lines are to be read
            The next for loop reads the line into s.  Then the entire string in s
            is processesd until the the entire line is processed and there are no
            more characters are to be processed.
            */

            //runTimes includes the total number of customers
            for (int k = 0; k < runTimes; k++) {
                index = 0;
                st = new StringTokenizer(readLn);

                if (readLn.substring(0, 2).trim().equalsIgnoreCase("x") ||
                        readLn.substring(0, 2).trim().equalsIgnoreCase("#x")) {
                    readLn = br.readLine();
                    st = new StringTokenizer(readLn);
                }

                while (st.hasMoreElements()) {
                    switch (index) {
                    case 0:
                        x = Float.parseFloat(st.nextToken()); // x coordinate

                        break;

                    case 1:
                        y = Float.parseFloat(st.nextToken()); // y coordinate

                        break;

                    case 2:
                        q = Integer.parseInt(st.nextToken()); // demand of customer

                        break;

                    case 3:
                        i = Integer.parseInt(st.nextToken()); // index of customer

                        break;

                    case 4:
                        earliestTime = Integer.parseInt(st.nextToken()); // earliest time to service customer

                        break;

                    case 5:
                        latestTime = Integer.parseInt(st.nextToken()); // latest time to service customer

                        break;

                    case 6:
                        servTime = Integer.parseInt(st.nextToken()); // time to service customer

                        break;

                    default:
                        break;
                    } //end switch

                    index += 1;
                }

                // end while
                // insert the shipment information in the the linked list
                mainShipments.insertShipment(x, y, q, i, earliestTime,
                    latestTime, servTime);

                // read the next line from the file
                try {
                    if (i < runTimes) { // i is the index of the shipment, runTimes is the total number of shipements
                        readLn = br.readLine();
                    }
                } // try
                catch (Exception e) {
                    System.out.println("Reading in the next line");
                }

                // catch
            }

            //end for - runTimes
        } catch (Exception e) {
            System.out.println("Reading the line");
        }

        try {
            //read in depot info
            readLn = br.readLine();

            //tokenize the line
            st = new StringTokenizer(readLn);

            index = 0; // reinitialize index

            while (st.hasMoreElements()) {
                switch (index) {
                case 0:
                    x = Float.parseFloat(st.nextToken()); // x coordinat of depot

                    break;

                case 1:
                    y = Float.parseFloat(st.nextToken()); // y coordinat of depot

                    break;

                case 2:
                    garbage = Integer.parseInt(st.nextToken()); // always zero

                    break;

                case 3:
                    i = Integer.parseInt(st.nextToken()); // index of depot: always one for VRPTW

                    break;

                default:
                    break;
                }

                index++; // increment index to extract next value
            }

            // insert the depot information in the the linked list
            mainShipments.insertDepotPosition(i, x, y);
        } catch (Exception e) {
            System.out.println("Reading the line");
        }

        return 1;
    }

    /**
    * <p>Write out the solution to the file - short version</p>
    * @param outFileName A String type of the file into which the output is to be
    *        written
    * This method added 9/4/03 by Mike McNamara
    */
    public void writeVRPTWShortSol(String outFileName) {
        double maxDistance = 0;
        PrintWriter solOutFile = null;
        Shipment curr = mainShipments.first;
        int j = 0;

        try {
            solOutFile = new PrintWriter(new FileWriter(outFileName));

            //print out the problem information to the file
            if (ProblemInfo.maxDistance >= 999999999) {
                maxDistance = 0;
            } else {
                maxDistance = ProblemInfo.maxDistance;
            }

            solOutFile.println(ProblemInfo.fileName + " " +
                ProblemInfo.noOfShips + " " + numDepots + " " +
                ProblemInfo.maxCapacity + " " + maxDistance + " " +
                ProblemInfo.overAllDemand + " " +
                ProblemInfo.overAllTotalWaitTime + " " +
                ProblemInfo.overAllTotalTravelDistance + " " +
                ProblemInfo.overAllServiceTime + " " +
                ProblemInfo.overAllTotalDistance + " " +
                ProblemInfo.overAllTotalTardiness + " " +
                ProblemInfo.overAllTotalExcessTime + " " +
                ProblemInfo.overAllTotalOverload);

            //write out the route information
            mainDepots.writeShortDepotsSol(solOutFile);
        } catch (IOException ioe) {
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                System.out.println("Detail solution file not open.");
            }
        }

        //end finally
    }

    // end writeVRPTWShortSol()

    /**
    * <p>Write out the solution to the file - longer version.</p>
    * @param outFileName A String type of the file into which the output is to be
    *        written
    * This method added 9/4/03 by Mike McNamara
    */
    public void writeVRPTWDetailSol(String outFileName) {
        PrintWriter solOutFile = null;
        Shipment curr = mainShipments.first;
        int j = 0;

        try {
            solOutFile = new PrintWriter(new FileWriter(outFileName));

            //print out the problem information to the file
            solOutFile.println("VRPTW File       : " + ProblemInfo.fileName);
            solOutFile.println("No. of Shipments : " + ProblemInfo.noOfShips);
            solOutFile.println("No. of Depots    : " + numDepots);
            solOutFile.println("Maximum capcity  : " + ProblemInfo.maxCapacity);
            solOutFile.println("Maximum Distance : " + ProblemInfo.maxDistance);

            /***************Sunil 10/1/03**********************************/
            solOutFile.println("********************************************");
            solOutFile.println("Over all totalWaitTime =========== " +
                ProblemInfo.overAllTotalWaitTime);
            solOutFile.println("Over all totalTravelTime ========= " +
                ProblemInfo.overAllTotalTravelDistance);
            solOutFile.println("Over all totalServiceTime ======== " +
                ProblemInfo.overAllServiceTime);
            solOutFile.println("Over all totalDistance covered === " +
                ProblemInfo.overAllTotalDistance);
            solOutFile.println("Over all totalTardinessTime ====== " +
                ProblemInfo.overAllTotalTardiness);
            solOutFile.println("Over all totalExcessTime ========= " +
                ProblemInfo.overAllTotalExcessTime);
            solOutFile.println("Over all totalOverload =========== " +
                ProblemInfo.overAllTotalOverload);
            solOutFile.println("Total Local 1-Opts: " + totalOneOpts);
            solOutFile.println("Total Local 2-Opts: " + totalTwoOpts);
            solOutFile.println("Total Local 3-Opts: " + totalThreeOpts);
            solOutFile.println("********************************************");

            if (ProblemInfo.tabuSearch != null) {
                solOutFile.println(ProblemInfo.tabuSearch.getStatistics());
            }

            //write out the route information
            mainDepots.writeDetailDepotsSol(solOutFile);
        } catch (IOException ioe) {
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                System.out.println("Solution file not open.");
            }
        }

        //end finally
    }

    // end writeVRPTWDetailSol()

    /**
    * <p> Find the closest customer to the depot to be inserted into a truck </p>
    * @param type Type of heuristic to be used to obtain the customer closest to the shipment
    * @return Shipment The closest shipment to the depot based on the type of heuristic
    */
    public Shipment getClosestShipmentVRPTW(int type) {
        Depot tempDepot;
        Shipment tempShip = null;

        //find the node for depotNo in the linked list
        tempDepot = mainDepots.getFirst();

        //While there are no more depot
        while (tempDepot != null) {
            //call on shipmentLinked list to find the polar and distance values
            tempShip = mainShipments.getClosestShipmentVRPTW(tempDepot.x,
                    tempDepot.y, type);
            tempDepot = tempDepot.next; //Next depot
        }

        return tempShip;
    }

    /**
    * <p> This method updates all the set interfaces in the CostFunctions. </p>
    */
    public void upDateCostFunctions() {
        ProblemInfo.depotLLLevelCostF.calculateTotalsStats(mainDepots);
        mainDepots.clearEmptyTrucks();
    }

    /**
    * <p>runs the optimization methods for local and global optimization</p>
    */
    public void runOptimize() {
        for (int caseNo = 0; caseNo <= ProblemInfo.NUMBEROFCASES; caseNo++) {
            ProblemInfo.depotLLLevelCostF.setWeights(caseNo);

            Depot tempDepot = mainDepots.getFirst();

            while (tempDepot != null) {
                tempDepot.mainTrucks.localOptVRPTW();
                ProblemInfo.truckLLLevelCostF.swapMNchains(tempDepot.mainTrucks);
                tempDepot.mainTrucks.localOptVRPTW();
                tempDepot = tempDepot.next;
            }

            //////////////////////Sunil Gurung 10/17/03//////////////////////
            //Get the mainTrucks in from the mainDepot
            Truck truck = mainDepots.getFirst().mainTrucks.first.next;
            TruckLinkedList mainTruck = mainDepots.getFirst().mainTrucks;

            //int one = 1;  //number of depot
            while ((truck != null) && (truck.next != null)) {
                mainTruck.exchange01(truck.mainVisitNodes,
                    truck.next.mainVisitNodes);
                mainTruck.exchange11(truck.mainVisitNodes,
                    truck.next.mainVisitNodes);
                mainTruck.exchange12(truck.mainVisitNodes,
                    truck.next.mainVisitNodes);
                mainTruck.exchange02(truck.mainVisitNodes,
                    truck.next.mainVisitNodes);

                mainTruck.exchange22(truck.mainVisitNodes,
                    truck.next.mainVisitNodes);

                //System.out.print("================================================\n");
                truck = truck.next;
            }

            // this section of code is from the MDVRPTW problem, it has been
            // modified to work with the VRPTW problem code
            //do the exchange optimization in the linked list
            //first do a display for checking
            //            mainDepots.displayForwardKeyListVRPTW();
            //******************************************************************************
            //                       2C1T                                                 //
            //                     Two cyclic one transfer                                //
            //         Parameters are: maxLoop                                            //
            //         ~maxLoop is how many times to attempt to do the transfer           //
            //          between  p and q                                                  //
            //            (declaration found on top of this VRPTW file)                 //
            //******************************************************************************
            //            System.out.println("Excecuting 2C1T...");
            mainDepots.cyclic2C1T(maxLoop);

            //******************************************************************************
            //                       3C1T                                                 //
            //                Three cyclic one transfer                                   //
            //         Parameters are: maxLoop                                            //
            //         ~maxLoop is how many times to attempt to do the transfer           //
            //          between  p and q                                                  //
            //            (declaration found on top of this VRPTW file)                 //
            //******************************************************************************
            //            System.out.println("Excecuting 3C1T...");
            mainDepots.cyclic3C1T(maxLoop);

            //******************************************************************************
            //                       VC1T                                                 //
            //         Parameters are: v, countloop                                       //
            //         ~v is variable depth for cyclic transfer starts at depth 3         //
            //            (declaration found on top of this VRPTW file)                 //
            //         ~maxLoop is how many times to do the exchange between  p and q     //
            //            (declaration found on top of this VRPTW file)                 //
            //******************************************************************************
            //            System.out.println("Excecuting vC1T...");
            mainDepots.cyclicVC1T(v, maxLoop);

            //******************************************************************************
            //******************************************************************************
            //                       A-Cyclic Transfers                                   //
            //        These are transfers that don't go back unto the head node           //
            //         ~v is variable depth for cyclic transfer starts at depth 3         //
            //            (declaration found on top of this VRPTW file)                 //
            //         ~maxLoop is how many times to do the exchange between  p and q     //
            //            (declaration found on top of this VRPTW file)                 //
            //******************************************************************************
            //Two A-Cyclic one transfer
            mainDepots.cyclic2AC1T(maxLoop);

            //Three A-Cylic one transfer
            mainDepots.cyclic3AC1T(maxLoop);

            //v A-Cylic one transfer
            mainDepots.cyclicVAC1T(v, maxLoop);

            //******************************************************************************
            //                       2C2T/vC2T                                            //
            //These act exactly like 2C1T or vC1T but they try to transfer two customers  //
            //         ~v is variable depth for cyclic transfer starts at depth 3         //
            //            (declaration found on top of this VRPTW file)                   //
            //         ~maxLoop is how many times to do the exchange between  p and q     //
            //            (declaration found on top of this VRPTW file)                   //
            //******************************************************************************
            //Two Cyclic two transfer
            mainDepots.cyclic2C2T(maxLoop);

            //v Cyclic two transfer
            mainDepots.cyclicVC2T(v, maxLoop);

            //            mainDepots.displayForwardKeyListVRPTW();
            tempDepot = mainDepots.getFirst();

            while (tempDepot != null) {
                tempDepot.mainTrucks.localOptVRPTW();
                ProblemInfo.truckLLLevelCostF.swapMNchains(tempDepot.mainTrucks);
                tempDepot.mainTrucks.localOptVRPTW();
                tempDepot = tempDepot.next;
            }

            calculateTotalOpts();
            upDateCostFunctions();
        }
    }

    /**
    * <p> Calculates the number of local optimizations done by each class to be
    * displayed to standard output and detailed solution file.</p>
    */
    public void calculateTotalOpts() {
        Depot tempDepot = mainDepots.getFirst();

        while (tempDepot != null) {
            totalOneOpts += tempDepot.mainTrucks.totalKOneOpt;
            totalTwoOpts += tempDepot.mainTrucks.totalKTwoOpt;
            totalThreeOpts += tempDepot.mainTrucks.totalKThreeOpt;
            tempDepot = tempDepot.next;
        }
    }

    /**
    * Run all local optimizations only
    */
    public void runLocalOpts() {
        Depot tempDepot = mainDepots.getFirst();

        while (tempDepot != null) {
            tempDepot.mainTrucks.localOptVRPTW();
            tempDepot = tempDepot.next;
        }

        calculateTotalOpts();
    }

    /**
    * Run local one optimizations only
    */
    public void runLocalOneOpts() {
        Depot tempDepot = mainDepots.getFirst();
        boolean noChange = false; //No change took place
        boolean status = true;
        int maxLoop = 1;
        int countLoop;

        while (tempDepot != null) {
            Truck tempTruck = tempDepot.getMainTrucks().getFirst();

            while (tempTruck != null) //loop through all the trucks
             {
                noChange = false;
                countLoop = 0;

                while (!noChange && (countLoop < maxLoop)) {
                    noChange = true;
                    status = tempTruck.mainVisitNodes.kInterChange1();

                    //System.out.println("Total one-opt "+totalOneOpt);
                    if (noChange) { //check to see if any changes took place
                        noChange = status;
                    }

                    //System.out.println("No change took place "+noChange);
                    countLoop++;
                }

                tempTruck = tempTruck.next;

                //recompute the cost after the local optimization for each truck
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(tempDepot.mainTrucks);
            }

            tempDepot = tempDepot.next;
        }

        calculateTotalOpts();
    }

    /**
    * Run local two optimizations only
    */
    public void runLocalTwoOpts() {
        Depot tempDepot = mainDepots.getFirst();
        boolean noChange = false; //No change took place
        boolean status = true;
        int maxLoop = 1;
        int countLoop;

        while (tempDepot != null) {
            Truck tempTruck = tempDepot.getMainTrucks().getFirst();

            while (tempTruck != null) //loop through all the trucks
             {
                noChange = false;
                countLoop = 0;

                while (!noChange && (countLoop < maxLoop)) {
                    noChange = true;
                    status = tempTruck.mainVisitNodes.kInterChange2();

                    //System.out.println("Total one-opt "+totalOneOpt);
                    if (noChange) { //check to see if any changes took place
                        noChange = status;
                    }

                    //System.out.println("No change took place "+noChange);
                    countLoop++;
                }

                tempTruck = tempTruck.next;

                //recompute the cost after the local optimization for each truck
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(tempDepot.mainTrucks);
            }

            tempDepot = tempDepot.next;
        }

        calculateTotalOpts();
    }

    /**
    * Run local three optimizations only
    */
    public void runLocalThreeOpts() {
        Depot tempDepot = mainDepots.getFirst();
        boolean noChange = false; //No change took place
        boolean status = true;
        int maxLoop = 1;
        int countLoop;

        while (tempDepot != null) {
            Truck tempTruck = tempDepot.getMainTrucks().getFirst();

            while (tempTruck != null) //loop through all the trucks
             {
                noChange = false;
                countLoop = 0;

                while (!noChange && (countLoop < maxLoop)) {
                    noChange = true;
                    status = tempTruck.mainVisitNodes.kInterChange3();

                    //System.out.println("Total one-opt "+totalOneOpt);
                    if (noChange) { //check to see if any changes took place
                        noChange = status;
                    }

                    //System.out.println("No change took place "+noChange);
                    countLoop++;
                }

                tempTruck = tempTruck.next;

                //recompute the cost after the local optimization for each truck
                ProblemInfo.truckLLLevelCostF.calculateTotalsStats(tempDepot.mainTrucks);
            }

            tempDepot = tempDepot.next;
        }

        calculateTotalOpts();
    }
}
