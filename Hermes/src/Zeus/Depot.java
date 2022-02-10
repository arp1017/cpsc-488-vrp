package Zeus;

import java.io.*;

import java.util.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: This class implements the Depot class. The Depot class maintains
 * information on the depots from which trucks service customers. The Depot class
 * is used by the DepotLinkedList class. </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class Depot implements java.io.Serializable {
    //From
    public static int maxSize = 100;
    public static VisitNodesLinkedList[] VisitNodesArray = new VisitNodesLinkedList[maxSize]; //Commented 6/29/2001
    public static int carrNum = 0;
    public static int response = 0;

    //this is the first node that was used to maintain the depot information in's code
    //public  static VisitNodes VisitNodeList= new VisitNodes(0,0,0,0,0,0,0,200,1236,0,0,40,50); //Commented 6/29/2001
    public static VisitNodesLinkedList VisitNodeList = new VisitNodesLinkedList(); //Commented 6/29/2001
    public static float M_COST = 1000000;
    public static float MAX_COST = 1000000000;
    public float x; //coordinates of the depot
    public float y; //coordinates of the depot
    public int depotNo; //the unique number for the depot
    float maxDuration; //max duration of route
    float maxCapacity; //max load of truck
    int maxTrucks; //PVRP predetermined, PTSP=1,MDVRP add as needed
    int currNoTrucks; //current number of trucks in the depot
    public TruckLinkedList mainTrucks; //linked list of trucks associated with the depot
    double[][] distanceMatrix; //array of costs(dist between all custs and depots
    int p; //Np neighborhood size
    int[] custTruckLog; //index by cust#, contains truck#
    int depotIndex;

    //for computation
    private int totalTrucks; //total trucks used by the depot
    public float totalDistance; //total distance of all trucks travelled
    public float totalDemand; //totaldemand of all trucks serviced

    /***************Mike McNamara  10/13/03 ******************/
    public float totalTravelTime; // total travel time of all trucks
    public float totalWaitTime; // total wait time of all trucks
    public float totalServTime; // total service time of all trucks
    public float totalTardiness; // total tardiness time of all trucks
    public float totalExcessTime; // total excess time of all trucks
    public float totalOverload; // total overload of all trucks
    public float totalCost; // total cost of the schedule

    /***********************************************************/
    public Depot next;

    /**
    * <p>Depot constructor - not being used at the current time.</p>
    */
    public Depot() {
    }

    /**
    * <p>Depot constructor -
    * The current instance of the depot has the x and y
    * value set to the parameters being passed. In addition
    * it creates an instance of a truckLinkedList named
    * mainTrucks for each of the depots.</p>
    * @param vertexX  x coordinate of the depot
    * @param vertexY  y coordinate of the depot
    */
    public Depot(float vertexX, float vertexY) {
        x = vertexX;
        y = vertexY;

        //get an instance of the TruckLinkedList
        mainTrucks = new TruckLinkedList();
    }

    /**
    * <p>Get x coordinate of the depot</p>
    * @return x  x coordinate of the depot
    */
    public double getX() {
        return x;
    }

    /**
    * <p>Get y coordinate of the depot</p>
    * @return y  y coordinate of the depot
    */
    public double getY() {
        return y;
    }

    /**
    * <p>Set the x coordinate of the depot</p>
    * @param xCoord x coordinate of the depot
    */
    public void setX(float xCoord) {
        x = xCoord;
    }

    /**
    * <p>Set the y coordinate of the depot</p>
    * @param yCoord  y coordinate of the depot
    */
    public void setY(float yCoord) {
        y = yCoord;
    }

    /**
    * <p>Set the total number of trucks for the depot</p>
    * @param num number of trucks for the depot
    * @return int  total number of trucks in the depot
    */
    public int setTotalTrucks(int num) {
        return totalTrucks = num;
    }

    /**
    * <p>return the total number of trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return int  total number of trucks
    */
    public int getTotalTrucks() {
        return totalTrucks;
    }

    /**
    * <p>calculate and set the total number of trucks</p>
    * method added 10/13/03 by Mike McNamara
    */
    public void calcTotalTrucks() {
        int totalTrucks;

        //get the required information from the TruckLinkedList
        totalTrucks = (int) ProblemInfo.truckLLLevelCostF.getTotalNodes(mainTrucks);
        setTotalTrucks(totalTrucks);
    }

    /**
    * <p>Set the total number of non-empty trucks for the depot</p>
    * @param num number of non-empty trucks for the depot
    * @return int  total number of non-empty trucks in the depot
    */
    public int setTotalNonEmptyTrucks(int num) {
        return totalTrucks = num;
    }

    /**
    * <p>return the total number of non-empty trucks for the depot</p>
    * method added 10/13/03 by Mike McNamara
    * @return  total number of non-empty trucks for the depot
    */
    public int getTotalNonEmptyTrucks() {
        return totalTrucks;
    }

    /**
    * <p>calculate and set the total number of non-empty trucks for the depot</p>
    * method added 10/13/03 by Mike McNamara
    */
    public void calcTotalNonEmptyTrucks() {
        int totalTrucks;

        //get the required information from the TruckLinkedList
        totalTrucks = (int) ProblemInfo.truckLLLevelCostF.getTotalNonEmptyNodes(mainTrucks);
        setTotalNonEmptyTrucks(totalTrucks);
    }

    /**
    * <p>Set the total demand for the current truck</p>
    * @param num demand for the current truck
    * @return float demand for the current truck
    */
    public float setTotalDemand(float num) {
        return totalDemand = num;
    }

    /**
    * <p>Get the total demand for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float demand for the current truck
    */
    public float getTotalDemand() {
        return totalDemand;
    }

    /**
    * <p>calculate and set total demand for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total demand
    */
    public float calcTotalDemand() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalDemand(mainTrucks);
    }

    /**
    * <p>Set the total duration for depot</p>
    * @param num duration for depot
    * @return float duration for depot
    */
    public float setTotalDuration(float num) {
        return totalDistance = num;
    }

    /**
    * <p>Get the total duration for depot</p>
    * method added 10/13/03 by Mike McNamara
    * @return float duration for depot
    */
    public float getTotalDuration() {
        return totalDistance;
    }

    /**
    * <p>calculate and set total distance for depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total distance
    */
    public float calcTotalDuration() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalDistance(mainTrucks);
    }

    /**
    * <p>Set the total wait time for the current truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param num wait time for the current truck
    * @return float wait time for the current truck
    */
    public float setTotalWaitTime(float num) {
        return totalWaitTime = num;
    }

    /**
    * <p>Get the total wait time for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float wait time for the current truck
    */
    public float getTotalWaitTime() {
        return totalWaitTime;
    }

    /**
    * <p>calculate and set total wait time for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total wait time
    */
    public float calcTotalWaitTime() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalWaitTime(mainTrucks);
    }

    /**
    * <p>Set the total travel time for the current truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param num travel time for the current truck
    * @return float travel time for the current truck
    */
    public float setTotalTravelTime(float num) {
        return totalTravelTime = num;
    }

    /**
    * <p>Get the total travel time for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float travel time for the current truck
    */
    public float getTotalTravelTime() {
        return totalTravelTime;
    }

    /**
    * <p>calculate and set total travel time for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total travel time
    */
    public float calcTotalTravelTime() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalTravelTime(mainTrucks);
    }

    /**
    * <p>Set the total tardiness for the current truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param num tardiness for the current truck
    * @return float tardiness for the current truck
    */
    public float setTotalTardiness(float num) {
        return totalTardiness = num;
    }

    /**
    * <p>Get the total tardiness for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float tardiness for the current truck
    */
    public float getTotalTardiness() {
        return totalTardiness;
    }

    /**
    * <p>calculate and set total tardiness for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total total tardiness
    */
    public float calcTotalTardiness() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalTardinessTime(mainTrucks);
    }

    /**
    * <p>Set the total overload for the current truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param num overload for the current truck
    * @return float overload for the current truck
    */
    public float setTotalOverload(float num) {
        return totalOverload = num;
    }

    /**
    * <p>Get the total overload for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float overload for the current truck
    */
    public float getTotalOverload() {
        return totalOverload;
    }

    /**
    * <p>calculate and set total overload for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total overload
    */
    public float calcTotalOverload() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalOverload(mainTrucks);
    }

    /**
    * <p>Set the total excess time for the current truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param num excess time for the current truck
    * @return float excess time for the current truck
    */
    public float setTotalExcessTime(float num) {
        return totalExcessTime = num;
    }

    /**
    * <p>Get the total excess time for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float excess time for the current truck
    */
    public float getTotalExcessTime() {
        return totalExcessTime;
    }

    /**
    * <p>calculate and set total excess time for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total excess time
    */
    public float calcTotalExcessTime() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalExcessTime(mainTrucks);
    }

    /**
    * <p>Set the total service time for the current truck</p>
    * method added 10/13/03 by Mike McNamara
    * @param num service time for the current truck
    * @return float service time for the current truck
    */
    public float setTotalServTime(float num) {
        return totalServTime = num;
    }

    /**
    * <p>Get the total service time for all the trucks</p>
    * method added 10/13/03 by Mike McNamara
    * @return float service time for the current truck
    */
    public float getTotalServTime() {
        return totalServTime;
    }

    /**
    * <p>calculate and set total service time for all trucks in depot</p>
    * method added 10/20/03 by Mike McNamara
    * @return float  total service time
    */
    public float calcTotalServTime() {
        return (float) ProblemInfo.truckLLLevelCostF.getTotalServiceTime(mainTrucks);
    }

    /**
    * <p>Set the depot number to a unique value.</p>
    * @param num unique number for the depot.
    * @return int unique number of the depot.
    */
    public int setDepotNo(int num) {
        return depotNo = num;
    }

    /**
    * <p>Get the unique number for the depot.</p>
    * @return int unique number of the depot.
    */
    public int getDepotNo() {
        return depotNo;
    }

    /**
    * <p>Set the maximum duration for a depot.</p>
    * @param num maximum duration for depot.
    * @return float maximum duration of the depot.
    */
    public float setMaxDuration(float num) {
        return maxDuration = num;
    }

    /**
    * <p>Get maximum duration for a depot.</p>
    * @return int maximum duration of the depot.
    */
    public float getMaxDuration() {
        return maxDuration;
    }

    /**
    * <p>Set maximum capacity for a depot.</p>
    * @param num maximum capacity  for depot.
    * @return float maximum capacity  of the depot.
    */
    public float setMaxCapacity(float num) {
        return maxCapacity = num;
    }

    /**
    * <p>Get maximum capacity for a depot.</p>
    * @return float maximum capacity  of the depot.
    */
    public float getMaxCapacity() {
        return maxCapacity;
    }

    /**
    * <p>Write out the depot information in short form to a file</p>
    * @param solOutFile name of the output file
    */
    public void writeShortDepot(PrintWriter solOutFile) {
        int i;
        solOutFile.print(depotNo + " " + x + " " + y + " ");
    }

    /**
    * <p>Write out the depot information in detail form to a file</p>
    * @param solOutFile name of the output file
    */
    public void writeDetailDepot(PrintWriter solOutFile) {
        solOutFile.println("Depot No. and vertices are:  " + depotNo + " " + x +
            " " + y);
    }

    /**
    * <p>Display the depot information for an MDVRP problem to a console. This method
    * will display the number of non-empty trucks, duration of trucks and total demand.</p>
    */
    public void displayDepotMDVRP() {
        int i;

        //load the required information into the depot node
        getTotalNonEmptyTrucks();
        getTotalDuration();
        getTotalDemand();
        System.out.println("Depot Information:  " + depotNo + " " + x + " " +
            y + " " + maxDuration + " " + maxCapacity);
        System.out.println("Total trucks, distance and demand are:  " +
            totalTrucks + " " + totalDistance + " " + totalDemand);
    }

    /**
    * <p>Display the depot information for a VRPTW problem to a console. This method
    * will display the number of non-empty trucks, duration of trucks and total demand.</p>
    * This method added 9/15/03 by Mike McNamara
    */
    public void displayDepotVRPTW() {
        // calculate values to be displayed
        calcTotalNonEmptyTrucks();

        System.out.println("Depot Information:  " + depotNo + " " + x + " " +
            y + " " + maxDuration + " " + maxCapacity);
        System.out.println("Total trucks, distance and demand are:  " +
            getTotalNonEmptyTrucks() + " " + totalDistance + " " + totalDemand);

        System.out.println("Total travel time: " + totalTravelTime);
        System.out.println("Total service time: " + totalServTime);
        System.out.println("Total wait time: " + totalWaitTime);
        System.out.println("Total tardiness: " + totalTardiness);
        System.out.println("Total excess time: " + totalExcessTime);
        System.out.println("Total overload: " + totalOverload);
        System.out.println("Total Local 1-Opts: " + mainTrucks.totalKOneOpt);
        System.out.println("Total Local 2-Opts: " + mainTrucks.totalKTwoOpt);
        System.out.println("Total Local 3-Opts: " + mainTrucks.totalKThreeOpt);
    }

    /**
    * <p>Display the depot information to a console - is not being used at the current time</p>
    */
    public void displayDepot() {
        int i;

        System.out.println("Depot No. and vertices are:  " + depotNo + " " + x +
            " " + y);

        /*System.out.println("  Duration is  " + duration);
        System.out.println("  Demand is    " + demand);
        System.out.println("  Frequency is " + frequency);
        System.out.println("  Number of Combinations is " + noComb);
        if (noComb > 0)
        {
        System.out.println("  The combinations are: ");
        for (i = 1; i < (noComb + 1); i++)
        {
         System.out.println("  " + visitComb[i]);
        }
        }
        if(noComb > 0 && currentComb!=null)
        {
        System.out.println("  Current visit Comb: ");
        for(int z = 0; z < currentComb.length; z++)
        { System.out.print(currentComb[z]);}       //index starts at 0
        System.out.println("");
        } */
    }

    /*public void DepotDay(int D, int Q, int m, int num, double[][]distMatrix,int Np,int nDepotDay)
    {
     maxDuration = D;
     maxCapacity = Q;
     maxTrucks = m;
     currNumTrucks = 0;
     totalNumCusts = num;
     depotDay = nDepotDay;
     numCustomers = 0;   //will contain num customers assigned to the depot
     notYetOnRoute = new Customer[num+1];  //do not use [0]
     theTruckList = new TruckList();
     onRoute = 1;
     distanceMatrix = distMatrix;
     custTruckLog = new int[totalNumCusts+1];
     p = Np;
    } //end constructor
    public void makeTruck()
    {
      int[]not_yet_on_route = convertToInts();
      Truck tempTruck = new Truck(maxDuration,maxCapacity, ++currNumTrucks,
        distanceMatrix, not_yet_on_route,totalNumCusts,p,numCustomers);
      theTruckList.insertFirst(tempTruck);
      initRoute();
    }    //end makeTruck()
    //convert notYetOnRoute array of customers to array of cust numbers
    public int[]convertToInts()
    {
      int j;
      int[]integerArray = new int [numCustomers +1];
      for(j=1; j<=numCustomers; j++)
      {
        integerArray[j] = notYetOnRoute[j].getCustNum();
      }
      return integerArray;
    }  //end convertToInts()
    //depotIndex = 0 for PVRP and PTSP, n+1 to n+t for MDVRP
    public void initRoute()
    {
      theTruckList.first.initRoute(onRoute,depotIndex,notYetOnRoute[onRoute],
        notYetOnRoute[onRoute+1]);     //call truck method
      custTruckLog[notYetOnRoute[onRoute].custNum] = currNumTrucks;
      custTruckLog[notYetOnRoute[onRoute+1].custNum] = currNumTrucks;
      onRoute+=2;                      //init route with one depot and 2 cust
    }   //end initRoute
    //------------------------------------------------------------------
    //cycle through all vertices and place on a route on a truck
    //checks if call to truck insertCust returns false create new truck
    public void completeRoute(int type)
    {
      int j;
      int maxCap;
      double maxDur = 0;
      int infinity = 65000;
      while(onRoute <= numCustomers)
      {
        System.out.println("Next Cust " + onRoute + "   ");
        if (type == 0) {
          maxCap = maxCapacity;
          maxDur = maxDuration;
        } else {
          if (type == 1) {
            maxCap = infinity;
            maxCap = infinity;
          } else {
            if (currNumTrucks == maxTrucks) {
              maxCap = infinity;
              maxDur = infinity;
            } else {
              maxCap = maxCapacity;
              maxDur = maxDuration;
            }
          }
        }
        if (!theTruckList.first.insertCust(onRoute, notYetOnRoute[onRoute], maxDur, maxCap )) {
          if (onRoute < numCustomers) {
            System.out.println("Route is full... create a new truck");
            makeTruck();
          } else {
              theTruckList.first.insertCust(onRoute, notYetOnRoute[onRoute], infinity, infinity);
              onRoute++;
          }
        } else {
          onRoute++;
        }
    }
    }
    //Truck method header: public insertCust(int onRoute, Customer insertCust,
    //double maxDur,int maxDemand)
    //call truck to insert single cust
    public boolean insertCust(int onRoute, Customer insertCust)
    {
      //**  REMEMBER DO IF STMT TO DETERMINE IF INFINITY SHOULD BE PASSED FOR MAX
      //    WHEN ON LAST PVRP TRUCK
      return(theTruckList.first.insertCust(onRoute, insertCust, maxDuration, maxCapacity));
    } */

    /**
    * <p>This method calls the first-best exchange procedures with routes of all
    * possible combinations and keeps the changes permanent untill no more
    * exchanges can be done based on a cost function.
    * Not being used at the current time</p>
    */
    public void useFirstBest() {
        int i;
        int j;
        int pass = 1;
        int exchange_flag = 0;
        int p_flag = 1;
        int ex01 = 1; /* 1 - use procedure exchange01 */
        int ex02 = 1; /* 1 - use procedure exchange02 */
        int ex11 = 1; /* 1 - use procedure CustArray */
        int ex12 = 1; /* 1 - use procedure exchange12 */
        int ex22 = 1; /* 1 - use procedure exchange22 */
        int num_routes = carrNum;
        int break_flag = 0;

        //ostream.println("METHOD FIRST BEST");
        try {
            do {
                exchange_flag = 0; /* exchange flag - no changes in route list */

                if (p_flag == 1) {
                    System.out.println("\nPass: (" + pass +
                        ")\tMethod: First Best\n");
                }

                //flags ex11, ex01, ex02, ex12, ex22 are initialized in the
                //declaration section to control the use of exchange algorithms
                //and also can be dynamically assigned while running in the
                //interactive mode.
                if (ex11 == 1) {
                    do {
                        break_flag = 0;

                        //System.out.println("num = "+num_routes);
                        for (i = 0; i <= num_routes; i++)
                            for (j = 0; j <= num_routes; j++)
                                if (i != j) { /* skip itself */

                                    //System.out.print("--------");
                                    exchange11(VisitNodesArray[i],
                                        VisitNodesArray[j]);

                                    //System.out.print("----DONE---");
                                }
                    } while (break_flag == 1); /* repeat until no more exchanges */
                }

                if (ex01 == 1) {
                    do {
                        break_flag = 0;

                        for (i = 1; i <= num_routes; i++)
                            for (j = 1; j <= num_routes; j++)
                                if (i != j) { /* skip itself */
                                    exchange01(VisitNodesArray[i],
                                        VisitNodesArray[j]);
                                }
                    } while (break_flag == 1); /* repeat until no more exchanges */
                }

                if (ex12 == 1) {
                    do {
                        break_flag = 0;

                        for (i = 1; i <= num_routes; i++)
                            for (j = 1; j <= num_routes; j++)
                                if (i != j) { /* skip itself */
                                    exchange12(VisitNodesArray[i],
                                        VisitNodesArray[j]);
                                }
                    } while (break_flag == 1); /* repeat until no more exchanges */
                }

                if (ex02 == 1) {
                    do {
                        break_flag = 0;

                        for (i = 1; i <= num_routes; i++)
                            for (j = 1; j <= num_routes; j++)
                                if (i != j) { /* skip itself */
                                    exchange02(VisitNodesArray[i],
                                        VisitNodesArray[j]);
                                }
                    } while (break_flag == 1); /* repeat until no more exchanges */
                }

                if (ex22 == 1) {
                    do {
                        //System.out.println("exc22");
                        break_flag = 0;

                        for (i = 1; i <= num_routes; i++)
                            for (j = 1; j <= num_routes; j++)
                                if (i != j) { /* skip itself */
                                    exchange22(VisitNodesArray[i],
                                        VisitNodesArray[j]);
                                }
                    } while (break_flag == 1); /* repeat until no more exchanges */
                }

                pass++;
            } while (exchange_flag == 1); /* repeat until no more exchanges */
        } catch (Exception e) {
            System.out.println("Caught in use first best: " + e);
        }
    }

    /* useFirstBest() */

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
    * @param p Visitnodes linked list from truck p
    * @param q Visitnodes linked list from truck q
    */
    public void exchange11(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        try {
            int m = 1;
            int n = 1;
            String curr_str = "11";

            /* if (p_flag) System.out.println ("\nExchange11: route (%2d, %2d) -> ", p, q); */
            m = 1;

            // System.out.println("p= "+p+" q = "+q+"route_ptr[p].num_of_nodes = "+route_ptr[p].num_of_nodes+
            // "\nroute_ptr[q].num_of_nodes = "+route_ptr[q].num_of_nodes);
            // MasterServer.consoleFrame.printConsole(p.toString()+" "+q.toString()+" "+m+"-"+n);
            for (m = 1; m <= (p.getSize() - 2); m++) {
                for (n = 1; n <= (q.getSize() - 2); n++) {
                    //      if (m <= p.getSize()-2)
                    //		{
                    String s1 = "";
                    s1 = p.calcExchange11(p.pointString(m), q.pointString(n));

                    StringTokenizer st1;
                    st1 = new StringTokenizer(s1, ";");

                    float cost1;
                    cost1 = (Float.valueOf(st1.nextToken())).floatValue();

                    int index1;
                    index1 = (Integer.valueOf(st1.nextToken())).intValue();

                    String s2;
                    s2 = q.calcExchange11(q.pointString(n), p.pointString(m));

                    StringTokenizer st2;
                    st2 = new StringTokenizer(s2, ";");

                    float cost2;
                    cost2 = (Float.valueOf(st2.nextToken())).floatValue();

                    int index2;
                    index2 = (Integer.valueOf(st2.nextToken())).intValue();

                    float cost_diff = cost1 + cost2;

                    if (cost_diff < 0) {
                        String p1 = p.pointString(m);
                        String q1 = q.pointString(n);
                        p.exchange11(p1, q1, index1);
                        q.exchange11(q1, p1, index2);
                        p.localTwoOpt();
                        q.localTwoOpt();
                    }

                    //  }
                    //best_flag = 0;
                }

                /* inner for */
            }

            /* outer for */
        } catch (Exception e) {
            System.err.println("Caught in Depot Exchange11: " + e);
        }
    }

    /* exchange11() */

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
    * @param p Visitnodes linked list from truck p
    * @param q Visitnodes linked list from truck q
    */
    public void exchange01(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        try {
            int i;
            int j;
            String curr_str = "01";

            /*    if (p_flag) System.out.println ("\nExchange01: route (%2d, %2d) -> ", p, q);   */
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                if (q.getSize() > 2) {
                    //if (i <= p.getSize()-2)
                    //{
                    String s1 = p.calcExchange01(p.pointString(i));
                    StringTokenizer st1 = new StringTokenizer(s1, ";");
                    float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                    String s2 = q.calcExchange10(p.pointString(i));
                    StringTokenizer st2 = new StringTokenizer(s2, ";");
                    float cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                    int index2 = (Integer.valueOf(st2.nextToken())).intValue();
                    float cost_diff = cost1 + cost2;

                    if ((cost_diff < 0) ||
                            ((cost_diff < 1000000) && (p.getSize() == 3))) { //cost2<1000000) //

                        String p1 = p.pointString(i);
                        p.exchange01(p1);
                        q.exchange10(p1, index2);

                        //System.out.println("Exchanging01 p.i and nothing "+i);
                        p.localTwoOpt();
                        q.localTwoOpt();
                    }

                    //if
                }

                /* if */
                i++;
            }

            /* while */
        } catch (Exception e) {
            System.err.println("Caught in exchange01: " + e);
        }
    }

    /* exchange01() */

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
    * @param p Visitnodes linked list from truck p
    * @param q Visitnodes linked list from truck q
    */
    public void exchange12(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        try {
            int i;
            int j;
            String curr_str = "12";

            /* if (p_flag) System.out.println ("\nExchange12: route (%2d, %2d) -> ", p, q); */
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                while (j <= (q.getSize() - 2)) {
                    if ((i <= (p.getSize() - 2)) &&
                            ((i + 1) <= (p.getSize() - 2))) {
                        String s1 = p.calcExchange12(q.pointString(j),
                                p.pointString(i), p.pointString(i + 1));
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index1 = (Integer.valueOf(st1.nextToken())).intValue();
                        String s2 = q.calcExchange21(p.pointString(i),
                                p.pointString(i + 1), q.pointString(j));
                        StringTokenizer st2 = new StringTokenizer(s2, ";");
                        float cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                        int index21 = (Integer.valueOf(st2.nextToken())).intValue();
                        int index22 = (Integer.valueOf(st2.nextToken())).intValue();
                        float cost_diff = cost1 + cost2;

                        if (cost_diff < 0) { //cost_diff<1000000)

                            String p1 = p.pointString(i);
                            String p2 = p.pointString(i + 1);
                            String q1 = q.pointString(j);
                            p.exchange12(q1, index1, p1, p2);
                            q.exchange21(p1, index21, p2, index22, q1);

                            //System.out.println("Exchanging12 p.i and q.j " +p+"."+i+" "+p+"."+(i+1)+
                            // " and " +q+"."+j);
                            p.localTwoOpt();
                            q.localTwoOpt();
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
    * @param p Visitnodes linked list from truck p
    * @param q Visitnodes linked list from truck q
    */
    public void exchange02(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        try {
            int i;
            int j;
            String curr_str = "02";

            /* if (p_flag) System.out.println ("\nExchange12: route (%2d, %2d) -> ", p, q); */
            i = 1;

            while (i <= (p.getSize() - 2)) {
                j = 1;

                if (q.getSize() > 2) {
                    if ((i <= (p.getSize() - 2)) &&
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

                        /*//float cost1 = p.calcExchange02(p.pointString(i), p.pointString(i + 1));
                        String s2 = q.calcExchange20(p.pointString(i),
                        p.pointString(i + 1));
                        StringTokenizer st2 = new StringTokenizer(s2, ";");
                        //float cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                        int index21 = (Integer.valueOf(st2.nextToken())).intValue();
                        int index22 = (Integer.valueOf(st2.nextToken())).intValue();
                        float cost_diff = cost1 + cost2;*/
                        if ((cost_diff < 0) ||
                                ((cost_diff < 1000000) && (p.getSize() == 4))) { //cost_diff<1000000)

                            String p1 = p.pointString(i);
                            String p2 = p.pointString(i + 1);
                            p.exchange02(p1, p2);
                            q.exchange20(p1, index21, p2, index22);

                            //System.out.println("Exchanging12 p.i and q.j " +p+"."+i+" "+p+"."+(i+1)+
                            // " and " +q+"."+j);
                            p.localTwoOpt();
                            q.localTwoOpt();
                        }

                        //if
                    }

                    //if
                }

                //if
                i++;
            }

            //while
        } catch (Exception e) {
            System.err.println("Caught in exchange02: " + e);
        }
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
    * @param p Visitnodes linked list from truck p
    * @param q Visitnodes linked list from truck q
    */
    public void exchange22(VisitNodesLinkedList p, VisitNodesLinkedList q) {
        try {
            int i;
            int j;
            String curr_str = "22";

            /* if (p_flag) System.out.println ("\nExchange12: route (%2d, %2d) -> ", p, q); */
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
                        String s1 = p.calcExchange22(q1, q2, p1, p2);
                        StringTokenizer st1 = new StringTokenizer(s1, ";");
                        float cost1 = (Float.valueOf(st1.nextToken())).floatValue();
                        int index11 = (Integer.valueOf(st1.nextToken())).intValue();
                        int index12 = (Integer.valueOf(st1.nextToken())).intValue();
                        String s2 = q.calcExchange22(p1, p2, q1, q2);
                        StringTokenizer st2 = new StringTokenizer(s2, ";");
                        float cost2 = (Float.valueOf(st2.nextToken())).floatValue();
                        int index21 = (Integer.valueOf(st2.nextToken())).intValue();
                        int index22 = (Integer.valueOf(st2.nextToken())).intValue();
                        float cost_diff = cost1 + cost2;

                        if (cost_diff < 0) { // || cost_diff<1000000 && p.getSize()==4)

                            try {
                                p.exchange22(q1, index11, q2, index12, p1, p2);
                                q.exchange22(p1, index21, p2, index22, q1, q2);
                                p.localTwoOpt();
                                q.localTwoOpt();

                                //System.out.println("Exchanging12 p.i and q.j " +p+"."+i+" "+p+"."+(i+1)+
                                // " and " +q+"."+j);
                            } catch (Exception xp) {
                                System.out.println("ERRRRROR:" + xp);
                            }
                        }

                        //if
                    }

                    //if
                    //best_flag = 0;
                    j++;
                }

                //inner while
                i++;
            }

            /* outer while */
        } catch (Exception e) {
            System.out.println("Caught in exchange22: " + e);
        }
    }

    /* exchange22() */

    //**********Integrate Thompson's Cyclic transfer algorithms here **********************/
    //============================================================================================================
    //========================== START FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =============================
    //============================================================================================================

    /**
    * <p>return trucks in this depot.</p>
    * @return TruckLinkedList pointer to the mainTrucks
    */
    public TruckLinkedList getMainTrucks() {
        return this.mainTrucks;
    }

    /**
    * <p>Delete empty trucks in this depot.</p>
    */
    public void clearEmptyTrucks() {
        // delete empty trucks
        Truck t = this.mainTrucks.find(0);
        Vector delVector = new Vector();
        boolean deleted = false;
        int numToKeep = 0;

        while (t != null) {
            /* add truck numbers of empty trucks to a vector, and delete them after
            * the end of traversal through the list of trucks, you do not want to
            * delete while traversing.
            */
            if (t.mainVisitNodes.getSize() < 3) {
                delVector.add(new Integer(t.truckNo));
            }

            t = t.next;
        }

        if (delVector.size() == this.mainTrucks.getNoTrucks()) {
            numToKeep = 1; // must keep at least one truck in list
        }

        while (delVector.size() > numToKeep) {
            int truckNo = ((Integer) delVector.elementAt(0)).intValue();
            this.mainTrucks.delete(truckNo);

            /* if using tabu search, delete them from the tabu memory structure as well */
            if (ProblemInfo.isUsingTabu) {
                ProblemInfo.tabuSearch.deleteTruck(this.depotNo, truckNo);
            }

            /* remove the truck number of the deleted truck from the vector */
            delVector.removeElementAt(0);
            deleted = true;
        }

        if (deleted) {
            /* if any trucks were deleted, update the truck numbers of the remaining trucks,
            * so that they are numbered sequential order. */
            this.mainTrucks.updateTruckNo();

            /* if using tabu search, do the same for the trucks in tabu memory structure */
            if (ProblemInfo.isUsingTabu) {
                ProblemInfo.tabuSearch.updateTruckNo(this.depotNo);
            }
        }
    }
}


//============================================================================================================
//============================ END FIRST FIRST, FIRST BEST & BEST BEST EXCHANGES =============================
//============================================================================================================
//End of Depot class
