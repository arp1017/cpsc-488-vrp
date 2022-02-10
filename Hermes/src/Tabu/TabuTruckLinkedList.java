package Tabu;


/**
 *
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: linked list of TabuTrucks</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuTruckLinkedList {
    /**
 * Pointer to the first TabuTruck in the TabuTruckLinkedList.
 */
    TabuTruck first;

    /**
 * Pointer to the last TabuTruck in the TabuTruckLinkedList.
 */
    TabuTruck last;

    /**
 * Number of TabuTrucks in the TabuTruckLinkedList.
 */
    int noTrucks = 0;

    /**
 * Initialize the Tabu memory for this TabuTruckLinkedList to match the corresponding Zeus.TruckLinkedList.
 *
 * @param mainTrucks The corresponding Zeus.TruckLinkedList instance.
 */
    TabuTruckLinkedList(Zeus.TruckLinkedList mainTrucks) {
        first = null;
        last = null;

        Zeus.Truck zeusTruck = mainTrucks.find(0);

        while (zeusTruck != null) {
            TabuTruck tabuTruck = new TabuTruck(zeusTruck);
            insertLast(tabuTruck);
            zeusTruck = zeusTruck.next;
        }
    }

    /**
 * Checks whether the TabuTruckLinkedList is empty.
 *
 * @return true if empty, false otherwise.
 */
    boolean isEmpty() {
        return (first == null);
    }

    /**
 * Inserts the given TabuTruck as the first Truck in the TabuTruckLinkedList.
 *
 * @param thisTruck The TabuTruck to insert.
 */
    void insertFirst(TabuTruck thisTruck) {
        if (isEmpty()) {
            last = thisTruck;
        } else {
            first.prev = thisTruck;
            thisTruck.next = first;
        }

        first = thisTruck;
        noTrucks++;
    }

    /**
 * Inserts the given TabuTruck as the last truck in the TabuTruckLinkedList.
 *
 * @param thisTruck The TabuTruck to insert.
 */
    void insertLast(TabuTruck thisTruck) {
        if (isEmpty()) {
            first = thisTruck;
        } else {
            last.next = thisTruck;
            thisTruck.prev = last;
        }

        last = thisTruck;
        noTrucks++;
    }

    /**
 * Searches for TabuTruck with the given truck number.
 *
 * @param truckNo The truck number to match.
 * @return An instance of the matched truck, if found, otherwise null.
 */
    TabuTruck find(int truckNo) {
        TabuTruck current = first;

        while (current != null) {
            if (current.truckNo == truckNo) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    /**
 * Deletes the TabuTruck with the given truck number.
 *
 * @param truckNo Truck number of the truck to delete.
 * @return An instance of truck that was deleted, if it existed, otherwise null.
 */
    TabuTruck delete(int truckNo) {
        TabuTruck current = first;
        TabuTruck previous;

        while (current != null) {
            if (current.truckNo == truckNo) {
                break;
            }

            current = current.next;
        }

        if (current == null) {
            return null;
        }

        previous = current.prev;

        if (current == first) {
            first = first.next;

            if (first != null) {
                first.prev = null;
            }
        } else {
            previous.next = current.next;

            if (previous.next != null) {
                previous.next.prev = previous;
            } else {
                last = previous;
            }
        }

        current.next = null;
        current.prev = null;
        noTrucks--;

        return current;
    }

    /**
 * Inserts the given truck after the truck with the given key as truck number.
 *
 * @param key Truck number of the truck to insert this truck after.
 * @param thisTruck Truck being inserted
 * @return true if successful, otherwise false.
 */
    boolean insertAfter(int key, TabuTruck thisTruck) {
        TabuTruck current = first;

        while (current != null) {
            if (current.truckNo == key) {
                break;
            }

            current = current.next;
        }

        if (current == null) {
            return false;
        }

        if (current == last) {
            thisTruck.next = null;
            last = thisTruck;
        } else {
            thisTruck.next = current.next;
            current.next.prev = thisTruck;
        }

        thisTruck.prev = current;
        current.next = thisTruck;
        noTrucks++;

        return true;
    }

    /**
 * Renumbers the trucks in the this depot, so the truck numbers start from zero and are in consecutive order.
 */
    void updateTruckNo() {
        TabuTruck current = first;
        int no = 0;

        while (current != null) {
            current.truckNo = no;
            current = current.next;
        }
    }

    /**
 * String representation of this TabuTruckLinkedList memory structure. Includes information about other levels
 * of Tabu Memory structure hierachy below the TruckLinkedList level.
 *
 * @return
 */
    public String toString() {
        TabuTruck current = first;
        String value = "";

        while (current != null) {
            value += current.toString();
            current = current.next;
        }

        return value;
    }

    /**
 * Clears the Tabu memory of all information about operations, both new and expired. Usually done, when there is a
 * change in the state space.
 */
    void clearMemory() {
        TabuTruck current = first;

        while (current != null) {
            current.clearMemory();
            current = current.next;
        }
    }
}
