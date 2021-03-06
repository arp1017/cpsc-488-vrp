package Zeus;

import java.io.*; //input-output java package


/**
 *
 * <p>Title: Zeus - A Unified Object Oriented Model for Routing and Scheduling Problems</p>
 * <p>Description: The root class is the root node for accessing all the classes in the Zeus
 *    system. The root creates an instance class of the problem to be solved. If the problem
 *    to be solved is the multi-depot vehicle routing problem, a MDVRP class is created serves
 *    as the interface for solving the MDVRP problem. If the problem to be solved is the Tour
 *    Orientering Problem then a TOP class is created. The root class in only involved in
 *    creating an instance of the problem class to be solved, such as MDVRP, and then delegates
 *    the MDVRP class to take over the problem solving </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 * @author Sam R. Thangiah
 */
public class Root {
    private VRPTW theVRPTW;

    /**
    * <p>Root constructor - Create an instance of the problem to be solved</p>
    */
    public Root() {
        //	long startCPUTime;
        //	long endCPUTime;
        //	long totalCPUTime;

        /**
        * <p>Create an instance of the Vehicle Routing Problem with Time Windows</p>
        */

        //System.out.println("Starting CPU time"+System.currentTimeMillis());
        //	startCPUTime = System.currentTimeMillis();
        //	theVRPTW = new VRPTW("problems/c_50.txt");
        theVRPTW = new VRPTW();

        //End timer here
        //System.out.println("Ending CPU time"+System.currentTimeMillis());
        //	endCPUTime = System.currentTimeMillis();
        //	totalCPUTime = endCPUTime - startCPUTime;
        //	System.out.println("TotalCPU time " + (totalCPUTime / 1000D) +
        //	    " seconds");
    }

    /**
    * return the instance of VRPTW
    * @return VRPTW  the instance of VRPTW
    */
    public VRPTW getVRPTW() {
        return theVRPTW;
    }
}
