package Tabu;


/**
 *
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: TabuTruck is a stripped-down version of the Zeus Truck
 * Each truck number reflects the same truck in Zeus and stores proposed ops
 * which are currently tabu and which involve customers in that Truck's mainVisitNodes </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuTruck {
    /**
 * Truck number of current TabuTruck matching the Truck number of the corresponding Zeus.Truck.
 */
    int truckNo;

    /**
 * Pointer to the next TabuTruck on the TabuTruckLinkedList.
 */
    TabuTruck next;

    /**
 * Pointer to the previous TabuTruck on the TabuTruckLinkedList.
 */
    TabuTruck prev;

    /**
 * Linked list of operations considered as Tabus.
 */
    TabuOperationLinkedList mainOperations;

    /**
 * Initialize the Tabu memory for this particular truck to match the corresponding Zeus.Truck.
 *
 * @param truck The corresponding Zeus.Truck instance.
 */
    TabuTruck(Zeus.Truck truck) {
        truckNo = truck.getTruckNo();
        mainOperations = new TabuOperationLinkedList();
    }

    /**
 * String representation of this TabuTruck memory structure. Includes information about other levels
 * of Tabu Memory structure hierachy below the Truck level.
 *
 * @return
 */
    public String toString() {
        return ("  --TabuTruckNo: " + truckNo + "\n" +
        mainOperations.toString());
    }

    /**
 * Clears the Tabu memory of all information about operations, both new and expired. Usually done, when there is a
 * change in the state space.
 */
    void clearMemory() {
        mainOperations = new TabuOperationLinkedList();
    }
}
