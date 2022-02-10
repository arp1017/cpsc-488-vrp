package Tabu;

import java.io.*;

import java.util.*;


/**
 *
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: TabuDepot mirrors the Depot in the Zeus System.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuDepot {
    /**
 * Depot number of the corresponding depot in Zeus.
 */
    int tabuDepotNo;

    /**
 * Tabu mirror of TruckLinkedList of Zeus.Depot.
 */
    TabuTruckLinkedList mainTabuTrucks;

    /**
 * Pointer to the previous depot in the Tabu Depot Linked List.
 */
    TabuDepot prev;

    /**
 * Pointer to the next depot in the Tabu Depot Linked List.
 */
    TabuDepot next;

    /**
 * Initialize the Tabu memory for this particular depot to match the corresponding Zeus.Depot.
 *
 * @param depot The corresponding Zeus.Depot.
 */
    TabuDepot(Zeus.Depot depot) {
        tabuDepotNo = depot.getDepotNo();
        mainTabuTrucks = new TabuTruckLinkedList(depot.getMainTrucks());
    }

    /**
 * String representation of this TabuDepot memory structure. Includes information about other levels
 * of Tabu Memory structure hierachy below the Depot level.
 *
 * @return
 */
    public String toString() {
        return ("--TabuDepotNo: " + tabuDepotNo + "\n" +
        mainTabuTrucks.toString());
    }

    /**
 * Clears the Tabu memory of all operations, both new and expired. Usually done, when there is a
 * change in the state space.
 */
    void clearMemory() {
        mainTabuTrucks.clearMemory();
    }
}
