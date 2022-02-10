package Zeus;

import CostFunctions.*;

import java.io.*; //input-output java package


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: The ProblemInfo class is used to maintain information on the current problem being solved. All
 * aspects of the problem such as the number of depots, number of trucks, number of shipments, the single depot
 * coordinates, maximum capacity, maximum distance, name of the data file are kept in this class. This is a static
 * class and as such does not require to be instantiated thus allowing infomation to be accessed using the name
 * of the class followed by the variable name, e,g ProblemInfo.fileName.
 *
 * This class is also used to define instances of the costfunctions that are to be used in the problme being solved</p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:<p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class ProblemInfo implements java.io.Serializable {
    //This class maintains information on the problem attributes
    public static String fileName; //name of the file being read in
    public static int probType; //problem type
    public static String probName = new String(""); //short name of problem
    public static String probNameLong = new String("Zeus"); //long name of problem
    public static int noOfVehs; //number of vehicles
    public static int noOfShips; //number of shipments
    public static int noOfDays; //number of days (horizon) or number of depots for MDVRP
    public static int noOfDepots; // number of depots for VRPTW
    public static int maxCapacity; //maximum capacity of a vehicle
    public static int maxDistance; //maximum distance of a vehicle
    public static boolean isTrucksLocked = false; //disables the creation of new trucks by Zeus
    public static int noTrucks; //maximum number of trucks for VRP

    /***********************Mike added 11/23/03************************/
    public static int NUMBEROFCASES = 3; // number of derivation cases
    public static float MAX_COST = 1000000; //when insertion or exchange is infeasible,
                                            //cost is set to MAX_COST value;

    // cost metric derivation weights
    public static double alpha; // penalty for wait time
    public static double beta; // penalty for excess time
    public static double mu; // penalty for tardiness
    public static double fi; // penalty for overload

    /***********************Sunil's Added code 9/30/03*************************/
    public static float overAllDemand; //Total demand for all the trucks
    public static float overAllTotalWaitTime; //Total wait time for all the trucks
    public static float overAllTotalTravelDistance; //Total travel time for all the trucks
    public static float overAllServiceTime; //Total Servce time for all the trucks
    public static float overAllTotalDistance; //Total Distance covered for all the trucks
    public static float overAllTotalTardiness; //Total Tardiness for all the trucks, must be 0
    public static float overAllTotalExcessTime; //Total Excess time for all the trucks, must be 0
    public static float overAllTotalOverload; //Total OverLoad time for all the trucks, must be 0

    //for single depots
    public static double depotX; //x coordinate of depot
    public static double depotY; //y coordiante of the depot
    public static boolean isUsingTabu;
    public static int optType;
    public static Tabu.TabuSearch tabuSearch;

    /**
 * cost functions to be used at each level of the Zeus system.
 * if a different cost function is required, then implement the CostFunctions
 * interface, and then set the appropriate level here to point to an instance
 * of that implementation.
 */
    public static CostFunctions vNodesLevelCostF = new BasicVNodesCostFunctions();
    public static CostFunctions truckLevelCostF = new BasicTruckCostFunctions();
    public static CostFunctions truckLLLevelCostF = new BasicTruckLLCostFunctions();
    public static CostFunctions depotLevelCostF = new BasicDepotCostFunctions();
    public static CostFunctions depotLLLevelCostF = new BasicDepotLLCostFunctions();
    public static CostFunctions shipmentLLLevelCostF = new BasicShipmentLLCostFunctions();
    public static CostFunctions shipmentLevelCostF = new BasicShipmentCostFunctions();
}
