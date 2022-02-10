package Tabu;

import java.io.*;

import java.util.*;


/**
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: The main hub of the Tabu Search implementation responsible
 * for maintaining the Tabu memory structure and used in optimization routines
 * to check for the 'Tabu'ness of proposed operations prior to implementation to
 * break out of local optima. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuSearch {
    private boolean isDiagnostic = false;

    /**
 * Tabu memory structure
 */
    private TabuDepotLinkedList tabuMainDepots;

    /**
 * iterative data (used in tabu search)
 */
    private int currentIteration;
    private int iterationsWithoutChange;
    private boolean lastOperationPermitted;
    private float currentThreshold;

    /**
 * statistical data (only used for statistical purposes, not for decision making)
 */
    private int numOfTimesSolutionGotWorse;
    private int numOfTimesSolutionGotWorseBeforeBest;
    private int numberOfPreventedOps;
    private float initialSolutionCost;
    private double startTime;
    private double endTime;

    /**
 * Always keep a pointer to the current solution in Zeus.
 */
    final private Zeus.DepotLinkedList mainDepotsHandle;

    /**
 * best solution store
 */
    private Zeus.DepotLinkedList bestSolutionDepotLL;
    private float bestSolutionCost;
    private int numOfTimesBestSolutionUpdated;
    private int bestSolutionFoundIteration;

    /**
 * Initialize the Tabu memory structure to match the current solution in Zeus.
 *
 * @param mainDepots DepotLinkedList of the current solution from Zeus
 */
    public TabuSearch(Zeus.DepotLinkedList mainDepots) {
        // initialize to default values
        startTime = (double) System.currentTimeMillis();
        currentIteration = 0;
        numberOfPreventedOps = 0;
        iterationsWithoutChange = 0;
        numOfTimesSolutionGotWorse = 0;
        numOfTimesSolutionGotWorseBeforeBest = 0;
        numOfTimesBestSolutionUpdated = -1;
        lastOperationPermitted = false;
        TabuProblemInfo.STOP = false;
        bestSolutionCost = Float.POSITIVE_INFINITY;
        currentThreshold = TabuProblemInfo.tabuThreshold;

        // get a pointer to the current Zeus solution
        mainDepotsHandle = mainDepots;
        Zeus.ProblemInfo.depotLLLevelCostF.calculateTotalsStats(mainDepotsHandle);
        initialSolutionCost = (float) Zeus.ProblemInfo.depotLLLevelCostF.getTotalCost(mainDepotsHandle);

        // initialize the tabu memory structure to match the current Zeus solution structure
        tabuMainDepots = new TabuDepotLinkedList(mainDepots);

        // set up the current solution as the best solution
        updateBestSolution();
    }

    /**
 * Checks whether a proposed operation is considered a tabu or not.
 *
 * @param op An implementation of the Operation interface containing information about the proposed move.
 * @return true if the move is not allowed, false if allowed.
 */
    public boolean isTabu(Zeus.Operation op) {
        TabuDepot tabuDepot1 = null;
        TabuDepot tabuDepot2 = null;
        TabuTruck tabuTruck1 = null;
        TabuTruck tabuTruck2 = null;
        TabuOperation tabuOp1 = null;
        TabuOperation tabuOp2 = null;
        boolean op1Found = false;
        boolean op2Found = false;

        // display diagnostic data 2.5% of the time, if in diagnostic mode
        if (isDiagnostic &&
                ((currentIteration % (TabuProblemInfo.maxTabuIterations * .025)) == 0)) {
            System.out.println("## Iteration " + currentIteration +
                ", Iterations Without Change " + iterationsWithoutChange +
                " ##");
        }

        // check whether the maximum allowed iterations or maximum allowed iteration with no change has exceeded
        if ((currentIteration >= TabuProblemInfo.maxTabuIterations) ||
                (iterationsWithoutChange >= TabuProblemInfo.maxTabuIterationsWithNoChange)) {
            TabuProblemInfo.STOP = true;
            lastOperationPermitted = false;
            iterationsWithoutChange++;

            if (isDiagnostic) {
                System.out.println(((currentIteration >= TabuProblemInfo.maxTabuIterations)
                    ? "##### MAX ITERATIONS REACHED #####"
                    : "##### MAX ITERATIONS WITHOUT CHANGE REACHED #####"));
            }

            updateTime();

            return true;
        }

        /* explictly call the garbage collector if the currentIteration is a multiple
 * of 10% of maxTabuIterations */
        if ((currentIteration % (TabuProblemInfo.maxTabuIterations * .10)) == 0) {
            System.gc();
        }

        currentIteration++;

        // get the proposed operations details
        tabuOp1 = op.getFirstTabuOperation();
        tabuOp2 = op.getSecondTabuOperation();

        // check whether the proposed operations exist in Tabu memory
        if (tabuOp1 != null) {
            tabuOp1.createIteration = currentIteration;
            tabuOp1.expireIteration = currentIteration +
                TabuProblemInfo.lifetime;
            tabuTruck1 = tabuMainDepots.find(tabuOp1.depotNo).mainTabuTrucks.find(tabuOp1.truckNo);
            op1Found = tabuTruck1.mainOperations.find(tabuOp1);
        }

        if (tabuOp2 != null) {
            tabuOp2.createIteration = currentIteration;
            tabuOp2.expireIteration = currentIteration +
                TabuProblemInfo.lifetime;
            tabuTruck2 = tabuMainDepots.find(tabuOp2.depotNo).mainTabuTrucks.find(tabuOp2.truckNo);
            op2Found = tabuTruck2.mainOperations.find(tabuOp2);
        }

        // if the proposed operations exist, then they are considered Tabu, then the move is not allowed
        if (op1Found || op2Found) {
            System.out.println("+++++ FORBIDDEN: Operation TABU. (Iteration " +
                currentIteration + ") +++++");
            numberOfPreventedOps++;
            lastOperationPermitted = false;
            iterationsWithoutChange++;
            updateTime();

            return true;
        }

        // if the proposed operations do not exist, the move is allowed
        if (isDiagnostic) {
            System.out.println("+++++ PERMITTED: Operation OK. (Iteration " +
                currentIteration + ") +++++");
            System.out.println("   " + tabuOp1);
            System.out.println("   " + tabuOp2);
            System.out.println("++++++++++");
        }

        // update the Tabu memory to include this allowed operation
        if (tabuOp1 != null) {
            tabuTruck1.mainOperations.insertTabuOperation(tabuOp1);
        }

        if (tabuOp2 != null) {
            tabuTruck2.mainOperations.insertTabuOperation(tabuOp2);
        }

        lastOperationPermitted = true;
        iterationsWithoutChange = 0;
        updateTime();

        return false;
    }

    /**
 * Used to find how further from the current solution the operation will be allowed to deviate
 * by tabu search.
 *
 * @return the percentage allowed to deviate (e.g. 0.10).
 */
    public float getTabuThreshold() {
        // if a deviated threshold is being used, then the threshold decreases down to zero as the
        // number of iterations increase, otherwise it stays constant.
        if (TabuProblemInfo.useDeviatedThreshold) {
            currentThreshold = TabuProblemInfo.tabuThreshold * (1 -
                (((float) currentIteration) / ((float) TabuProblemInfo.maxTabuIterations)));
        }

        if (isDiagnostic &&
                ((currentIteration % (TabuProblemInfo.maxTabuIterations * .025)) == 0)) {
            String number = Long.toString((long) (currentThreshold * 100));
            System.out.println("##### Current Threshold = 0." +
                ((number.length() > 1) ? number : ("0" + number)) + " #####");
        }

        return currentThreshold;
    }

    /**
 * String representation of the complete Tabu memory structure. Includes information about all levels
 * of Tabu Memory structure hierachy. Warning: The output may be really large if the number of
 * maxOperationsInMemoryPerTruck is high.
 *
 * @return
 */
    public String toString() {
        return ("\n\n>>>>>>>>>>>>>>>>> TABU SEARCH MEMORY STRUCTURE <<<<<<<<<<<<<<<\n" +
        tabuMainDepots +
        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n\n");
    }

    /**
 * Updates the best solution. Solution is updated only if the total cost of the
 * current solution in Zeus is less than the best solution known to tabu search.
 */
    public void updateBestSolution() {
        if (bestSolutionCost <= getSolutionCost(mainDepotsHandle)) {
            numOfTimesSolutionGotWorse++;

            return;
        }

        numOfTimesSolutionGotWorseBeforeBest += numOfTimesSolutionGotWorse;
        numOfTimesSolutionGotWorse = 0;
        bestSolutionDepotLL = (Zeus.DepotLinkedList) Zeus.GeneralZeusTools.object_Clone(mainDepotsHandle,
                TabuProblemInfo.tempFileLocation);
        bestSolutionCost = getSolutionCost(bestSolutionDepotLL);
        bestSolutionFoundIteration = currentIteration;
        numOfTimesBestSolutionUpdated++;
    }

    /**
 * Retrieves statistics on the performed search.
 *
 * @return A string containing the information that can printed to the STDOUT or a file.
 */
    public String getStatistics() {
        return ("\r\n\r\n" +
        "----------------------------- TABU SEARCH STATISTICS --------------------------\r\n" +
        "Went through " + currentIteration + " iterations in " +
        ((getRunTime() / 1000) / 60) + " minutes.\r\n" +
        "Best solution was found on " + bestSolutionFoundIteration +
        " (th) iteration.\r\n" + "Best solution was updated " +
        numOfTimesBestSolutionUpdated + " times.\r\n" +
        "Solution was made worse " + numOfTimesSolutionGotWorseBeforeBest +
        " times before the best solution was found.\r\n" +
        "Initial solution found before using tabu search had a total cost of " +
        initialSolutionCost + ".\r\n" +
        "Best solution found by tabu search has a total cost of " +
        bestSolutionCost + ".\r\n" + "Prevented " + numberOfPreventedOps +
        " tabu operations from being performed.\r\n" +
        "Maximum tabu iterations (" + TabuProblemInfo.maxTabuIterations +
        ") was " +
        ((currentIteration >= TabuProblemInfo.maxTabuIterations)
        ? "exceeded.\r\n" : "not exceeded.\r\n") +
        "Maximum tabu iterations without change (" +
        TabuProblemInfo.maxTabuIterationsWithNoChange + ") was " +
        ((iterationsWithoutChange >= TabuProblemInfo.maxTabuIterationsWithNoChange)
        ? "exceeded.\r\n" : "not exceeded.\r\n") +
        "-------------------------------------------------------------------------------\r\n\r\n");
    }

    /**
 * Returns the best solution known to Tabu Search.
 *
 * @return Best known solution in the form of a Zeus.DepotLinkedList instance
 */
    public Zeus.DepotLinkedList getBestSolution() {
        return bestSolutionDepotLL;
    }

    /**
 * Method internal to tabu search used to find the total cost of a particular solution.
 *
 * @param depotLL The Zeus.DepotLinkedList instance of the current solution of which the cost is required
 * @return The cost of the given solution as determined by Tabu Search.
 */
    private float getSolutionCost(Zeus.DepotLinkedList depotLL) {
        Zeus.ProblemInfo.depotLLLevelCostF.calculateTotalsStats(depotLL);

        return (float) Zeus.ProblemInfo.depotLLLevelCostF.getTotalCost(depotLL);
    }

    /**
 * Retrieves the current solution from Zeus.
 * @return Current solution as an instance of Zeus.DepotLinkedList.
 */
    public Zeus.DepotLinkedList getCurrentSolution() {
        return mainDepotsHandle;
    }

    /**
 * Updates the last accessed time on Tabu Search.
 */
    public void updateTime() {
        endTime = (double) System.currentTimeMillis();
    }

    /**
 * Retrieves the time taken by Tabu Search.
 *
 * @return The time as a double in milliseconds.
 */
    public double getRunTime() {
        return (endTime - startTime);
    }

    /**
 * Delete a truck from the Tabu Memory structure. Should call updateTruckNo(..) after all required
 * trucks have been deleted, so that the trucks are renumbered.
 *
 * @param depotNo Depot number of depot that this truck belongs to.
 * @param truckNo Truck number of the truck to delete.
 */
    public void deleteTruck(int depotNo, int truckNo) {
        TabuDepot tDepot = tabuMainDepots.find(depotNo);

        if (tDepot != null) {
            TabuTruck tTruck = tDepot.mainTabuTrucks.delete(truckNo);
        }

        clearMemory();
    }

    /**
 * Delete a depot from the Tabu Memory Structure.
 *
 * @param depotNo Depot number of the depot being deleted.
 */
    public void deleteDepot(int depotNo) {
        TabuDepot tDepot = tabuMainDepots.delete(depotNo);
        clearMemory();
    }

    /**
 * Renumbers the trucks in the given the depot, so the numbers start from zero and are in consecutive order.
 *
 * @param depotNo Depot number of the depot in which the trucks need to be renumbered.
 */
    public void updateTruckNo(int depotNo) {
        TabuDepot tDepot = tabuMainDepots.find(depotNo);
        tDepot.mainTabuTrucks.updateTruckNo();
    }

    /**
 * Renumbers the depots in the tabu memory, so the numbers start from one and are in consecutive order.
 */
    public void updateDepotNo() {
        tabuMainDepots.updateDepotNo();
    }

    /**
 * Clears the Tabu memory of all information about operations, both new and expired. Usually done, when there is a
 * change in the state space.
 */
    public void clearMemory() {
        tabuMainDepots.clearMemory();
    }
}


//end of TabuSearch
