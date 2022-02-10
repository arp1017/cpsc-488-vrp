package Tabu;

import java.util.*;


/**
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description:The TabuOperationLinkedList class keeps track of all tabu operations for a truck </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuOperationLinkedList {
    /**
 * Pointer to first TabuOperation on the TabuOperationLinkedList.
 */
    TabuOperation first;

    /**
 * Pointer to last TabuOperation on the TabuOperationLinkedList.
 */
    TabuOperation last;

    /**
 * Number of TabuOperations on TabuOperationLinkedList.
 */
    int operationCount;

    /**
 * Diagnostic mode on/off.
 */
    boolean isDiagnostic = false;

    /**
 * Initialize the Tabu Operation Linked List.
 */
    TabuOperationLinkedList() {
        operationCount = 0;
    }

    /**
 * Checks whether the TabuOperationLinkedList is empty.
 *
 * @return true if empty, false otherwise.
 */
    boolean isEmpty() {
        return (first == null);
    }

    /**
 * Inserts the given TabuOperation into the TabuOperationLinkedList.
 *
 * @param tOp The TabuOperation to insert.
 */
    void insertTabuOperation(TabuOperation tOp) {
        if (first == null) {
            first = tOp;
        } else {
            last.next = tOp;
            tOp.prev = last;
        }

        last = tOp;
        operationCount++;

        // it makes sense to delete expired & older operations in memory, when they exceed the
        // maximum allowed size, but doing seems to lead to not so good results with the best
        // set of parameters currently known. 05/05/03.

        /*if (operationCount > TabuProblemInfo.maxOperationsInMemoryPerTruck) {
   deleteExpiredTabuOperations(tOp.createIteration);
   if (operationCount > TabuProblemInfo.maxOperationsInMemoryPerTruck) delete(first);
   }*/
    }

    /**
 * Deletes the given TabuOperation to delete.
 *
 * @param tOp The TabuOperation being deleted.
 * @return The TabuOperation that was deleted, if deletion was successful, null otherwise.
 */
    TabuOperation delete(TabuOperation tOp) {
        TabuOperation current = first;

        while (current != null) {
            if (current.equals(tOp)) {
                break;
            }

            current = current.next;
        }

        if (current == null) {
            return null;
        }

        if (current == first) {
            first = current.next;

            if (first != null) {
                first.prev = null;
            }
        } else if (current == last) {
            last = last.prev;
            last.next = null;
        } else {
            TabuOperation prev = current.prev;
            TabuOperation next = current.next;
            prev.next = next;
            next.prev = prev;
        }

        current.prev = null;
        current.next = null;
        operationCount--;

        return current;
    }

    /**
 * Searches for existence of non-expired TabuOperation matching the given TabuOperation.
 *
 * @param tOp The TabuOperation to match.
 * @return true if such an operation was found, false otherwise.
 */
    boolean find(TabuOperation tOp) {
        TabuOperation current = first;

        //delete expired operations
        if (operationCount > TabuProblemInfo.maxOperationsInMemoryPerTruck) {
            deleteExpiredTabuOperations(tOp.createIteration);
        }

        //look for this operation
        while (current != null) {
            if (current.equals(tOp)) {
                if (current.expireIteration > tOp.createIteration) {
                    return true;
                }
            }

            current = current.next;
        }

        return false;
    }

    /**
 * Deletes expired TabuOperation from the TabuOperationLinkedList.
 *
 * @param currentIteration The current Iteration/time on Tabu Search.
 */
    void deleteExpiredTabuOperations(int currentIteration) {
        TabuOperation current = first;
        Vector expiredOperations = new Vector();
        int beforeOpCount = operationCount;

        //cycle through all operations, add expired ops to delete vector
        while (current != null) {
            if (current.expireIteration <= currentIteration) {
                expiredOperations.add(current);
            }

            current = current.next;
        }

        //cycle through delete vector and delete members
        for (int i = 0; i < expiredOperations.size(); i++) {
            TabuOperation t = (TabuOperation) expiredOperations.elementAt(i);
            delete(t);
        }

        if (isDiagnostic) {
            if (expiredOperations.size() > 0) {
                System.out.println("############# " + expiredOperations.size() +
                    " expired TabuOperations deleted because operationCount exceeded. " +
                    "(Before= " + beforeOpCount + ", After= " + operationCount +
                    ") #############");
            }
        }
    }

    /**
 * String representation of this TabuOperationLinkedList memory structure. Includes information about other levels
 * of Tabu Memory structure hierachy below the OperationLinkedList level.
 *
 * @return
 */
    public String toString() {
        TabuOperation current = first;
        String value = "";

        while (current != null) {
            value += current.toString();
            current = current.next;
        }

        return value;
    }
}
