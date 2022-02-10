package Tabu;

import java.util.*;


/**
 *
 * <p>Title: TabuSearch - a Meta-Heuristic for the Zeus System</p>
 * <p>Description: Represents one tabu operation. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class TabuOperation {
    /**
 * Represents the time/iteration this move/operation was proposed.
 */
    int createIteration;

    /**
 * Represents the time/iteration when this move/operation is no longer considered tabu.
 */
    int expireIteration;

    /**
 * The previous TabuOperation in the TabuOperationLinkedList.
 */
    TabuOperation prev;

    /**
 * The next TabuOperation in the TabuOperationLinkedList.
 */
    TabuOperation next;

    /**
 * The shipment(or index) number of node just before nodes being used in the operation.
 */
    int beforeNode;

    /**
 * The shipment(or index) number of node just after nodes being used in the operation.
 */
    int afterNode;

    /**
 * The shipment(or index) numbers of nodes being used in the operation. The must be inserted
 * into the vector in order as instances of java.lang.Integer.
 */
    Vector nodesExchanged;

    /**
 * The truck number of the truck this operation originates from (i.e. where these nodes originally
 * belonged to).
 */
    int truckNo;

    /**
 * The depot number of the depot this operation originates from (i.e. where these nodes originally
 * belonged to).
 */
    int depotNo;

    /**
 * Initialize the Tabu Operation.
 *
 * @param befNode Before node (shipment number) of the nodes just before the nodes used in the operation.
 * @param afNode After node (shipment number) of the nodes just before the nodes used in the operation.
 * @param nodesEx The vector of nodes being used in the operation (vector should have instances of java.lang.Integer with each instance having the index or shipment number of the node(s) in the operation.)
 * @param truck The truck number where this operation originates from. (Where the nodes originally were).
 * @param depot The depot number where this operation originates from. (Where the ndoes originally were).
 */
    public TabuOperation(int befNode, int afNode, Vector nodesEx, int truck,
        int depot) {
        prev = next = null;
        beforeNode = befNode;
        afterNode = afNode;
        nodesExchanged = nodesEx;
        truckNo = truck;
        depotNo = depot;
    }

    /**
 * Compares this TabuOperation with another TabuOperation for equality.
 *
 * @param o The second TabuOperation used in the comparison.
 * @return True, if equal, false otherwise.
 */
    public boolean equals(Object o) {
        TabuOperation op2 = (TabuOperation) o;
        boolean isEqual = false;

        isEqual = equalVectors(this.nodesExchanged, op2.nodesExchanged);

        if (isEqual && (beforeNode == op2.beforeNode) &&
                (afterNode == op2.afterNode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
 * String representation of this TabuOperation memory structure.
 *
 * @return
 */
    public String toString() {
        return ("    --TabuOperation: \n" + "      --created: " +
        createIteration + "\texpires: " + expireIteration + "\n" +
        "      --depotNo: " + depotNo + "\ttruckNo: " + truckNo + "\n" +
        "      --beforeNode: " + beforeNode + "\tafterNode: " + afterNode +
        "\n" + "      --nodesExchanged: " + nodesExchanged + "\n");
    }

    /**
 * used by equals() for comparing vector equivalency
 * @param v1
 * @param v2
 * @return
 */
    private boolean equalVectors(Vector v1, Vector v2) {
        if (v1.size() != v2.size()) {
            return false;
        }

        for (int i = 0; i < v1.size(); i++) {
            Integer a = (Integer) v1.elementAt(i);
            Integer b = (Integer) v2.elementAt(i);

            if (a.intValue() != b.intValue()) {
                return false;
            }
        }

        return true;
    }
}
