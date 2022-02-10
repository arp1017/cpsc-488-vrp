package Tabu;


/**
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: Global user-defined parameters for use in tabu search.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuProblemInfo {
    /**
 * Maximum number of iterations permitted by Tabu Search.
 * After this number of iterations has exceeded, any more calls to isTabu(..) method
 * of TabuSearch will return true, even if called with a valid operation. Also after this
 * number of iterations have been exceeded, the following parameter 'STOP' will be set to
 * true.
 */
    public static int maxTabuIterations = 10000;

    /**
 * Maximum number of iterations that will be allowed where no change occurs to current solution.
 * Change is defined as any difference in total cost of the current solution in Zeus. Change could be
 * for better or worse. When exceeded, any more calls to isTabu(..) in TabuSearch will return true,
 * and the following STOP parameter will be set to true.
 */
    public static int maxTabuIterationsWithNoChange = 500;

    /**
 * An optimization should check when to stop by checking the value of 'STOP'.
 */
    public static boolean STOP = false;

    /**
 * Maximum number of operations remembered by one truck.
 */
    public static int maxOperationsInMemoryPerTruck = 70;

    /**
 * Life of a tabu operation specified as a number of iterations.
 * The operations are only considered tabu for this many iterations. Having too
 * short a lifetime, may cause the optimization technique to go into a loop, as the
 * operations are not considered tabu for long enough.
 */
    public static int lifetime = 70;

    /**
 * The percentage by which a given solution may deviate from the current solution (in Zeus)
 * in terms of cost or other constaints, if necessary.
 */
    public static float tabuThreshold = 0.11F;

    /**
 * Indicates whether the Tabu Threshold is deviated (as determined necessary by Tabu Search)
 * or whether it is kept constant through the whole search.
 */
    public static boolean useDeviatedThreshold = true;

    /**
 * Folder for creating temporary files. IT MUST EXIST AND USER MUST HAVE WRITE ACCESS!!!
 */
    public static String tempFileLocation = "h:/";
}
