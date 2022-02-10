package Zeus;

import java.util.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: This class implements the Operation class and also
 * provides a data structure in which to store proposed operations. Exchanges are
 * performed between two trucks.  Each exchange is represented
 * by two operations.  firstTruckTabuOpt will contain information of the customers
 * being taken out of the first truck's linked list of customers and the same idea
 * follows for secondTruckTabuOpt</p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class ExchangesOperation implements Operation, java.io.Serializable {
    /*Exchanges are performed between two trucks.  Each exchange is represented
   by two operations.  firstTruckTabuOpt will contain information of the customers
   being taken out of the first truck's linked list of customers and the same idea
   follows for secondTruckTabuOpt
 */
    public Tabu.TabuOperation firstTruckTabuOpt;
    public Tabu.TabuOperation secondTruckTabuOpt;
    public float cost1 = 0;
    public float cost2 = 0;
    public int index1 = 0;
    public int index2 = 0;
    public int index11 = 0;
    public int index12 = 0;
    public int index21 = 0;
    public int index22 = 0;
    public String p1 = null;
    public String p2 = null;
    public String q1 = null;
    public String q2 = null;
    public float changeInCost = 0;
    public VisitNodesLinkedList p = null;
    public VisitNodesLinkedList q = null;
    public int exchangeType;

    /**
   Default constructor - not used at the current time.
 */
    public ExchangesOperation() {
    }

    /**
   Get the first Tabu operation
   @return Tabu.TabuOperation return the Tabu operation
 */
    public Tabu.TabuOperation getFirstTabuOperation() {
        return firstTruckTabuOpt;
    }

    /**
   Get the second Tabu operation
   @return Tabu.TabuOperation return the Tabu operation
 */
    public Tabu.TabuOperation getSecondTabuOperation() {
        return secondTruckTabuOpt;
    }

    /**
   Set the first  Tabu operation
   @param firstTabu the Tabu operation
 */
    public void setFirstTabuOperation(Tabu.TabuOperation firstTabu) {
        firstTruckTabuOpt = firstTabu;
    }

    /**
   Set the second Tabu operation
   @param secondTabu the Tabu operation
 */
    public void setSecondTabuOperation(Tabu.TabuOperation secondTabu) {
        secondTruckTabuOpt = secondTabu;
    }

    /**
 * store the operation performed in Zeus checkImplementExchange01Opt.
 * checkImplementExchange01Opt is the Tabu version of exchange01 between two trucks
 * using the cost functions defined in ProblemInfo.
 * @param truck1       first truck
 * @param truck2       second truck
 * @param depot1No     first depot
 * @param depot2No     second depot
 * @param c1           cost of first truck
 * @param c2           cost of second truck
 * @param index2       location for the insertion of the shipment
 * @param ps1          shipment to be removed (all info. in string form)
 * @param beforeNode1  node before the shipment - lock in on the location of the shipment
 * @param afterNode1   node after the shipment
 */
    public void setOperation01(Truck truck1, Truck truck2, int depot1No,
        int depot2No, float c1, float c2, int index2, String ps1,
        int beforeNode1, int afterNode1) {
        p = truck1.mainVisitNodes;
        q = truck2.mainVisitNodes;
        cost1 = c1;
        cost2 = c2;
        p1 = ps1;
        this.index2 = index2;
        changeInCost = c1 + c2;
        exchangeType = ZeusConstants.EXCHANGE_01;

        Vector v = new Vector();
        v.add(new Integer(getShipmentNo(p1)));

        firstTruckTabuOpt = new Tabu.TabuOperation(beforeNode1, afterNode1, v,
                truck1.truckNo, depot1No);
        secondTruckTabuOpt = null;
    }

    /**
 * store the operation performed in Zeus checkImplementExchange02Opt.
 * checkImplementExchange02Opt is the Tabu version of exchange01 between two trucks
 * using the cost functions defined in ProblemInfo.
 * @param truck1       first truck
 * @param truck2       second truck
 * @param depot1No     first depot
 * @param depot2No     second depot
 * @param c1           cost of first truck
 * @param c2           cost of second truck
 * @param index21      location for of the insertion of the first shipment
 * @param index22      location for of the insertion of the second shipment
 * @param ps1          first shipment to be removed (all info. in string form)
 * @param ps2          second shipment to be removed (all info. in string form)
 * @param beforeNode1  node before the shipment - lock in on the location of the shipment
 * @param afterNode1   node after the shipment
 */
    public void setOperation02(Truck truck1, Truck truck2, int depot1No,
        int depot2No, float c1, float c2, int index21, int index22, String ps1,
        String ps2, int beforeNode1, int afterNode1) {
        p = truck1.mainVisitNodes;
        q = truck2.mainVisitNodes;
        cost1 = c1;
        cost2 = c2;
        this.index21 = index21;
        this.index22 = index22;
        p1 = ps1;
        p2 = ps2;
        changeInCost = c1 + c2;
        exchangeType = ZeusConstants.EXCHANGE_02;

        Vector v = new Vector();
        v.add(new Integer(getShipmentNo(p1)));
        v.add(new Integer(getShipmentNo(p2)));
        firstTruckTabuOpt = new Tabu.TabuOperation(beforeNode1, afterNode1, v,
                truck1.truckNo, depot1No);
        secondTruckTabuOpt = null;
    }

    /**
 * store the operation performed in Zeus checkImplementExchange11Opt
 * checkImplementExchange11Opt is the Tabu version of exchange01 between two trucks
 * using the cost functions defined in ProblemInfo.
 * @param truck1       first truck
 * @param truck2       second truck
 * @param depot1No     first depot
 * @param depot2No     second depot
 * @param c1           cost of first truck
 * @param c2           cost of second truck
 * @param i1           location to insert first shipment from first truck
 * @param i2           location to insert first shipment from first truck
 * @param ps1          first shipment to be removed from first truck (all info. in string form)
 * @param qs1          first shipment  shipment to be removed from second truck(all info. in string form)
 * @param beforeNode1  node before the first shipment from first truck
 * @param beforeNode2  node before the second shipment from first truck
 * @param afterNode1   node after the first shipment
 * @param afterNode2   node after the second shipment
 */
    public void setOperation11(Truck truck1, Truck truck2, int depot1No,
        int depot2No, float c1, float c2, int i1, int i2, String ps1,
        String qs1, int beforeNode1, int beforeNode2, int afterNode1,
        int afterNode2) {
        p = truck1.mainVisitNodes;
        q = truck2.mainVisitNodes;
        cost1 = c1;
        cost2 = c2;
        index1 = i1;
        index2 = i2;
        p1 = ps1;
        q1 = qs1;
        changeInCost = c1 + c2;
        exchangeType = ZeusConstants.EXCHANGE_11;

        Vector v = new Vector();
        v.add(new Integer(getShipmentNo(p1)));
        firstTruckTabuOpt = new Tabu.TabuOperation(beforeNode1, afterNode1, v,
                truck1.truckNo, depot1No);

        v = new Vector();
        v.add(new Integer(getShipmentNo(q1)));
        secondTruckTabuOpt = new Tabu.TabuOperation(beforeNode2, afterNode2, v,
                truck2.truckNo, depot2No);
    }

    /**
 * store the operation performed in Zeus checkImplementExchange12Opt
 * checkImplementExchange12Opt is the Tabu version of exchange01 between two trucks
 * using the cost functions defined in ProblemInfo.
 * @param truck1       first truck
 * @param truck2       second truck
 * @param depot1No     first depot
 * @param depot2No     second depot
 * @param c1           cost of first truck
 * @param c2           cost of second truck
 * @param i1           location to insert first shipment from first truck
 * @param i21          location to insert first shipment from second truck
 * @param i22          location to insert second shipment from second truck
 * @param ps1          first shipment to be removed from first truck
 * @param ps2          second shipment to be removed from first truck (all info. in string form)
 * @param qs1          first shipment to be removed from second truck
 * @param beforeNode1  node before the first shipment
 * @param beforeNode2  node before the second shipment
 * @param afterNode1   node after the first shipment
 * @param afterNode2   node after the second shipment
 */
    public void setOperation12(Truck truck1, Truck truck2, int depot1No,
        int depot2No, float c1, float c2, int i1, int i21, int i22, String ps1,
        String ps2, String qs1, int beforeNode1, int beforeNode2,
        int afterNode1, int afterNode2) {
        p = truck1.mainVisitNodes;
        q = truck2.mainVisitNodes;
        cost1 = c1;
        cost2 = c2;
        index1 = i1;
        index21 = i21;
        index22 = i22;
        p1 = ps1;
        p2 = ps2;
        q1 = qs1;
        changeInCost = c1 + c2;
        exchangeType = ZeusConstants.EXCHANGE_12;

        Vector v = new Vector();
        v.add(new Integer(getShipmentNo(p1)));
        v.add(new Integer(getShipmentNo(p2)));
        firstTruckTabuOpt = new Tabu.TabuOperation(beforeNode1, afterNode1, v,
                truck1.truckNo, depot1No);

        v = new Vector();
        v.add(new Integer(getShipmentNo(q1)));
        secondTruckTabuOpt = new Tabu.TabuOperation(beforeNode2, afterNode2, v,
                truck2.truckNo, depot2No);
    }

    /**
 * store the operation performed in Zeus checkImplementExchange22Opt
 * checkImplementExchange22Opt is the Tabu version of exchange01 between two trucks
 * using the cost functions defined in ProblemInfo.
 * @param truck1       first truck
 * @param truck2       second truck
 * @param depot1No     first depot
 * @param depot2No     second depot
 * @param c1           cost of first truck
 * @param c2           cost of second truck
 * @param i11          location to insert first shipment from first truck
 * @param i12          location to insert second  shipment from first truck
 * @param i21          location to insert first shipment from second truck
 * @param i22          location to insert first shipment from second truck
 * @param ps1          first shipment to be removed from first truck
 * @param ps2          second shipment to be removed from first truck
 * @param qs1          first shipment to be removed from second truck
 * @param qs2          second shipment to be removed from first truck
 * @param beforeNode1  node before the first shipment
 * @param beforeNode2  node before the second shipment
 * @param afterNode1   node after the first shipment
 * @param afterNode2   node after the second shipment
 */
    public void setOperation22(Truck truck1, Truck truck2, int depot1No,
        int depot2No, float c1, float c2, int i11, int i12, int i21, int i22,
        String ps1, String ps2, String qs1, String qs2, int beforeNode1,
        int beforeNode2, int afterNode1, int afterNode2) {
        p = truck1.mainVisitNodes;
        q = truck2.mainVisitNodes;
        cost1 = c1;
        cost2 = c2;
        p1 = ps1;
        p2 = ps2;
        q1 = qs1;
        q2 = qs2;
        index11 = i11;
        index12 = i12;
        index21 = i21;
        index22 = i22;

        changeInCost = c1 + c2;
        exchangeType = ZeusConstants.EXCHANGE_22;

        Vector v = new Vector();
        v.add(new Integer(getShipmentNo(p1)));
        v.add(new Integer(getShipmentNo(p2)));
        firstTruckTabuOpt = new Tabu.TabuOperation(beforeNode1, afterNode1, v,
                truck1.truckNo, depot1No);

        v = new Vector();
        v.add(new Integer(getShipmentNo(q1)));
        secondTruckTabuOpt = new Tabu.TabuOperation(beforeNode2, afterNode2, v,
                truck2.truckNo, depot2No);
    }

    private int getShipmentNo(String s) {
        StringTokenizer st = new StringTokenizer(s, ";");

        return Integer.valueOf(st.nextToken()).intValue();
    }
}
