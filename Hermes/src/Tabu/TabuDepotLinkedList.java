package Tabu;


/**
 *
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: TabuDepotLinkedList mirrors the DepotLinkedList in Zeus </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuDepotLinkedList {
    /**
 * Pointer to the first depot in the TabuDepotLinkedList.
 */
    TabuDepot first;

    /**
 * Pointer to the last depot in the TabuDepotLinkedList.
 */
    TabuDepot last;

    /**
 * Number of depots in the TabuDepotLinkedList.
 */
    int noDepots = 0;

    /**
 * Initialize the Tabu memory with the information from the given Zeus.DepotLinkedList.
 *
 * @param mainDepots Zeus.DepotLinkedList representing the current solution in Zeus.
 */
    TabuDepotLinkedList(Zeus.DepotLinkedList mainDepots) {
        first = null;
        last = null;

        Zeus.Depot zeusDepot = mainDepots.find(1);

        /*assign a TabuDepot for every Zeus Depot*/
        while (zeusDepot != null) {
            TabuDepot tabuDepot = new TabuDepot(zeusDepot);
            insertLast(tabuDepot);
            zeusDepot = zeusDepot.next;
        }
    }

    /**
 * Checks whether the TabuDepotLinkedList is empty.
 * @return true if empty, false otherwise.
 */
    boolean isEmpty() {
        return (first == null);
    }

    /**
 * Inserts the given TabuDepot as the first item on the list.
 *
 * @param thisDepot The TabuDepot to insert.
 */
    void insertFirst(TabuDepot thisDepot) {
        if (isEmpty()) {
            last = thisDepot;
        } else {
            first.prev = thisDepot;
            thisDepot.next = first;
        }

        first = thisDepot;
        noDepots++;
    }

    /**
 * Inserts the given TabuDepot as the last item on the list.
 *
 * @param thisDepot The depot to insert.
 */
    void insertLast(TabuDepot thisDepot) {
        if (isEmpty()) {
            first = thisDepot;
        } else {
            last.next = thisDepot;
            thisDepot.prev = last;
        }

        last = thisDepot;
        noDepots++;
    }

    /**
 * Searches for a TabuDepot with the given depot number.
 * @param depotNo Depot number of the depot to match.
 * @return Returns an instance if the depot was found, otherwise returns null.
 */
    TabuDepot find(int depotNo) {
        TabuDepot current = first;

        while (current != null) {
            if (current.tabuDepotNo == depotNo) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    /**
 * Deletes the depot with the given depot number from the list.
 *
 * @param depotNo Depot number of the depot to delete.
 * @return Returns an instance of the deleted depot if it existed, otherwise returns null.
 */
    TabuDepot delete(int depotNo) {
        TabuDepot current = first;
        TabuDepot previous;

        while (current != null) {
            if (current.tabuDepotNo == depotNo) {
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
        noDepots--;

        return current;
    }

    /**
 * Inserts the given depot after the depot with the given depot number (key).
 *
 * @param key The depot number of the depot to insert after.
 * @param thisDepot The depot being inserted.
 * @return true if insertion was successful, false otherwise.
 */
    boolean insertAfter(int key, TabuDepot thisDepot) {
        TabuDepot current = first;

        while (current != null) {
            if (current.tabuDepotNo == key) {
                break;
            }

            current = current.next;
        }

        if (current == null) {
            return false;
        }

        if (current == last) {
            thisDepot.next = null;
            last = thisDepot;
        } else {
            thisDepot.next = current.next;
            current.next.prev = thisDepot;
        }

        thisDepot.prev = current;
        current.next = thisDepot;
        noDepots++;

        return true;
    }

    /**
 * String representation of this TabuDepotLinkedList memory structure. Includes information about other levels
 * of Tabu Memory structure hierachy below the DepotLinkedList level.
 *
 * @return
 */
    public String toString() {
        TabuDepot current = first;
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
        TabuDepot current = first;

        while (current != null) {
            current.clearMemory();
            current = current.next;
        }
    }

    /**
 * Renumbers the depots, so the depot numbers start from one and are in consecutive order.
 */
    void updateDepotNo() {
        TabuDepot current = first;
        int no = 1;

        while (current != null) {
            current.tabuDepotNo = no;
            current = current.next;
        }
    }
}
