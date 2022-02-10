package Hermes;

import Zeus.*;

import java.io.*;

import java.util.StringTokenizer;


/**
 * <p>Title: ZeusAdaptor.java</p>
 * <p>Description: This is the adaptor class to interface the Carrier agent with
 *                 the Zeus System. Contains tools to calculate inter-carrier
 *                 exchanges, perform the inter-carrier exchanges, calculate
 *                 problem specific values (like total travel time), print
 *                 detailed and short solutions, and handle shipment and depot
 *                 information</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ZeusAdaptor implements MessageTags {
    public static float MAX_COST = ProblemInfo.MAX_COST;
    public ShipperListAdaptor shipListAdaptor;
    private float maxCap;
    private float maxDist;
    private float depotX;
    private float depotY;
    private Zeus theZeus;

    //these variables are for the frequency at which the local opts run whenever
    //inserting customers during the auctioning process
    private int oneOptFrequency = 100;
    private int twoOptFrequency = 100;
    private int threeOptFrequency = 100;

    //this is the type of heuristic to run for the VRPTW
    //type four checks angle, distance, and time for the insertion
    //for more types, see VRPTW...
    private int heuristicType = 4;

    /**
     * Constructor (only allows one depot per Carrier at the moment)
     *             (perhaps pass an array of coordinates later?)
     * @param z  instance of Zues
     */
    public ZeusAdaptor(Zeus z) {
        theZeus = z;
        shipListAdaptor = new ShipperListAdaptor(theZeus.getRoot().getVRPTW().mainShipments);
    }

    /**
     * retrieve the instance of Zeus - Used primarily for testing
     * @return Zeus  the instance of the Zeus schedule
     */
    public Zeus getZeus() {
        return theZeus;
    }

    /**
     * returns the x-coordinate of the depot
     * @return float  the x-coordinate of the depot
     */
    public float getDepotX() {
        return (float) ProblemInfo.depotX;
    }

    /**
     * returns the y-coordinate of the depot
     * @return float  the y-coordinate of the depot
     */
    public float getDepotY() {
        return (float) ProblemInfo.depotY;
    }

    /**
     * Appends a detailed solution of this schedule to the file given
     * @param fileName  the file given
     */
    public void detailedSolution(String fileName) {
        PrintWriter solOutFile = null;

        try {
            solOutFile = new PrintWriter(new FileWriter(fileName, true));
            theZeus.getRoot().getVRPTW().mainDepots.writeDetailDepotsSol(solOutFile);
        } catch (IOException ex) {
            System.err.println(
                "Error opening detailed solution file to append: " + ex);
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                System.out.println("Detail solution file not open.");
            }
        }
    }

    /**
     * Appends a short solution of this schedule to the file given
     * @param fileName  the file given
     */
    public void shortSolution(String fileName) {
        PrintWriter solOutFile = null;

        try {
            solOutFile = new PrintWriter(new FileWriter(fileName, true));
            theZeus.getRoot().getVRPTW().mainDepots.writeShortDepotsSol(solOutFile);
        } catch (IOException ex) {
            System.err.println(
                "Error opening detailed solution file to append: " + ex);
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                System.out.println("Detail solution file not open.");
            }
        }
    }

    /**
     * Checks to see if schedule is empty
     * @return boolean  true if schedule is empty, false otherwise
     */
    public boolean isEmpty() {
        boolean empty = false;

        // list of depots for this carrier
        Depot tempDepot = theZeus.getRoot().getVRPTW().mainDepots.getFirst();
        Truck tempTruck = tempDepot.getMainTrucks().getFirst();
        VisitNodesLinkedList v = tempTruck.mainVisitNodes;

        while (tempDepot != null) {
            while (tempTruck != null) {
                // if there are no shipments scheduled for the trucks
                if (v.ifVisitListEmpty()) {
                    empty = true; // then the schedule is empty

                    break;
                }

                tempTruck = tempTruck.next;
            }

            if (empty) {
                break;
            }

            tempDepot = tempDepot.next;
        }

        return empty;
    }

    /**
     * Get the number of trucks in the schedule
     * @return int  the number of trucks in the schedule
     */
    public int getNumTrucks() {
        ProblemInfo.depotLLLevelCostF.setTotalNonEmptyNodes(theZeus.getRoot()
                                                                   .getVRPTW().mainDepots);

        return (int) ProblemInfo.depotLLLevelCostF.getTotalNonEmptyNodes(theZeus.getRoot()
                                                                                .getVRPTW().mainDepots);
    }

    /**
     * get size of schedule
     * @return int  size of schedule
     */
    public int getSize() {
        int size = 0;

        // list of depots for the carrier
        DepotLinkedList tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
        Depot tempDepot;
        Truck tempTruck;

        if (!isEmpty()) { // check if schedule is empty
            tempDepot = tempDepotList.getFirst(); // find first depot in list

            // for the number of depots in the list...
            for (int i = 0; i < tempDepotList.getNoDepots(); i++) {
                tempTruck = tempDepot.getMainTrucks().first; // find first truck in list

                // for the number of trucks in the list...
                for (int j = 0; j < tempDepot.getTotalNonEmptyTrucks(); j++) {
                    size += tempTruck.getNoNodes(); // add up the size of schedule
                    tempTruck = tempTruck.next; // move to next truck
                }

                tempDepot = tempDepot.next; // move to next depot
            }
        }

        // else size == 0
        return size;
    }

    /**
     * returns the total distance traveled in the schedule
     * @return float  total distance traveled in the schedule
     */
    public float calculateDistTrav() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalDistance(theZeus.getRoot()
                                                                                 .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total time traveled in the schedule
     * @return float  total time traveled in the schedule
     */
    public float calculateTotalTime() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalTravelTime(theZeus.getRoot()
                                                                                   .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total capacity hauled in the schedule
     * @return float  total capacity hauled in the schedule
     */
    public float calculateCapacity() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalDemand(theZeus.getRoot()
                                                                               .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total waiting time in the schedule
     * @return float  total waiting time in the schedule
     */
    public float calcTotalWaitTime() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalWaitTime(theZeus.getRoot()
                                                                                 .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total tardiness in the schedule
     * @return float  total tardiness in the schedule
     */
    public float calcTotalTardiness() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalTardinessTime(theZeus.getRoot()
                                                                                      .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total overload in the schedule
     * @return float  total overload in the schedule
     */
    public float calcTotalOverload() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalOverload(theZeus.getRoot()
                                                                                 .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total excess time in the schedule
     * @return float  total excess time in the schedule
     */
    public float calcTotalExcessTime() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalExcessTime(theZeus.getRoot()
                                                                                   .getVRPTW().mainDepots);
        }

        // else
        return 0;
    }

    /**
     * returns the total servcie time in the schedule
     * @return float  total service time int the schedule
     */
    public float calcTotalServiceTime() {
        if (!isEmpty()) { // check if schedule is empty

            return (float) ProblemInfo.depotLLLevelCostF.getTotalServiceTime(theZeus.getRoot()
                                                                                    .getVRPTW().mainDepots);
        }

        //else
        return 0;
    }

    /**
     * calculates the current statistics for this schedule
     */
    public void calcCurrentValues() {
        theZeus.getRoot().getVRPTW().upDateCostFunctions();
    }

    /**
     * get all shipment ID's in schedule
     * @return String  all Shipment ID's in schedule
     */
    public synchronized String getAllVertices() {
        String xy = "";
        PointCell left;
        PointCell right;
        PointCell last;

        // list of depots for the Carrier
        DepotLinkedList tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
        Depot tempDepot;
        Truck tempTruck;

        if (!isEmpty()) { // check if schedule is empty
            tempDepot = tempDepotList.getFirst(); // find first depot in list

            // for the number of depots in the list...
            for (int i = 0; i < tempDepotList.getNoDepots(); i++) {
                // find the first truck in list
                tempTruck = tempDepot.getMainTrucks().first;

                // find the first shipment scheduled for that truck
                left = tempTruck.mainVisitNodes.first();
                last = tempTruck.mainVisitNodes.last();
                right = left.next; // the next scheduled shipment

                // depot the truck belongs to
                xy += ("DD" + tempDepot.getDepotNo() + "DD");

                // for the number of trucks in the list...
                for (int j = 0; j < tempDepot.getTotalNonEmptyTrucks(); j++) {
                    // truck the shipment belongs to
                    xy += ("^" + tempTruck.getTruckNo() + "^");

                    while (right != last) {
                        /**
                         * build string with coordinates of starting point to a
                         * destination point
                         * i.e. startX;startY>destX;destY&
                         * the destX;destY should replace the startX;startY for the
                         * next string build iteration
                         */
                        xy = xy + left.getXCoord() + ";" + left.getYCoord() +
                            ">" + right.getXCoord() + ";" + right.getYCoord() +
                            "&";
                        left = left.next;
                        right = right.next;
                        xy = xy + left.getXCoord() + ";" + left.getYCoord() +
                            ">" + right.getXCoord() + ";" + right.getYCoord() +
                            "&";
                    }

                    tempTruck = tempTruck.next; // move to next truck
                }

                tempDepot = tempDepot.next; // move to next depot
            }

            return xy;
        } else { // if there are no scheduled shipments, then the string is "empty"

            return "empty";
        }
    }

    /**
     * returns the vertice coordinates specified by the number vNum
     * @param vNum  vertice number
     * @return String  vertice
     */
    public synchronized String getVertice(int vNum) {
        String xy = "";
        PointCell left;
        PointCell right;
        PointCell last;

        // list of depots for the Carrier
        DepotLinkedList tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
        Depot tempDepot;
        Truck tempTruck;

        if (!isEmpty()) { // check if schedule is empty
            tempDepot = tempDepotList.getFirst(); // find first depot in list

            // for the number of depots in the list...
            for (int i = 0; i < tempDepotList.getNoDepots(); i++) {
                // find the first truck in list
                tempTruck = tempDepot.getMainTrucks().first;

                while ((tempTruck.mainVisitNodes.getSize() < vNum) &&
                        (tempTruck.next != null)) {
                    tempTruck = tempTruck.next;
                    vNum -= (tempTruck.mainVisitNodes.getSize() - 2); // subtract this list from position desired
                }

                // find the first shipment scheduled for that truck
                left = tempTruck.mainVisitNodes.first();
                last = tempTruck.mainVisitNodes.last();
                right = left.next; // the next scheduled shipment

                if (right == last) {
                    return xy;
                } else {
                    for (int j = 0; ((right != left) && (j < vNum)); j++) {
                        left = left.next;
                        right = right.next;
                    }

                    xy = "" + left.getXCoord() + ";" + left.getYCoord() + ";" +
                        right.getXCoord() + ";" + right.getYCoord();

                    return xy;
                }
            }
        }

        return null;
    }

    /**
     * calculate initial insertion cost for the customer while building routes
     * @param iIndex  cust index
     * @param lX  x coord
     * @param lY  y coord
     * @param lDemand  cust demand
     * @param lEar  earliest serv time
     * @param lLat  latest serv time
     * @param lServ  length of time needed
     * @return Message  string message containing the cost
     */
    public Message insertCostCar(int iIndex, float lX, float lY, float lDemand,
        float lEar, float lLat, float lServ) {
        Message msg = new Message();
        Shipment costShip = new Shipment(lX, lY, lDemand, iIndex, lEar, lLat,
                lServ);
        String costStr = theZeus.getRoot().getVRPTW().calculateCost(costShip);
        StringTokenizer st = new StringTokenizer(costStr);
        msg.setMessageType(InsertCostTag);
        msg.addArgument(CostTag, st.nextToken()); // float cost value
        msg.addArgument(AfterIndexTag, st.nextToken()); // index to insert after
        msg.addArgument(TruckNumberTag, st.nextToken()); // truck index for shipment

        //added 3/28
        msg.addArgument(MessageTags.IndexTag, "" + iIndex);

        return msg;
    }

    /**
     * insert shipment
     * @param iIndex  cust index
     * @param lX  x coord
     * @param lY  y coord
     * @param lDemand  cust demand
     * @param lEar  earliest serv time
     * @param lLat  latest serv time
     * @param lServ  length of time needed
     * @return Message  inserted shipment succeeded message
     */
    public Message insert(int iIndex, float lX, float lY, float lDemand,
        float lEar, float lLat, float lServ) {
        boolean status = false;
        Message msg = new Message();
        Shipment newShip = new Shipment(lX, lY, lDemand, iIndex, lEar, lLat,
                lServ);

        // insert the shipment using the Zeus system
        status = theZeus.getRoot().getVRPTW().scheduleShipment(newShip);

        //create a message based upon the value returned from Zeus
        if (status) {
            shipListAdaptor.insertShipment(newShip);
            ProblemInfo.noOfShips++; // increment the number of shipments in schedule

            //decide whether to run a local opt now or not
            if ((ProblemInfo.noOfShips % oneOptFrequency) == 0) {
                theZeus.getRoot().getVRPTW().runLocalOneOpts();
                System.out.println("Performed a 1-Opt, total: " +
                    theZeus.getRoot().getVRPTW().totalOneOpts);
            }

            if ((ProblemInfo.noOfShips % twoOptFrequency) == 0) {
                theZeus.getRoot().getVRPTW().runLocalTwoOpts();
                System.out.println("Performed a 2-Opt, total: " +
                    theZeus.getRoot().getVRPTW().totalTwoOpts);
            }

            if ((ProblemInfo.noOfShips % threeOptFrequency) == 0) {
                theZeus.getRoot().getVRPTW().runLocalThreeOpts();
                System.out.println("Performed a 3-Opt, total: " +
                    theZeus.getRoot().getVRPTW().totalThreeOpts);
            }

            msg.setMessageType(ConfirmTag);
            msg.addArgument(IndexTag, "" + iIndex);
        } else {
            msg.setMessageType(RefuseTag);
            msg.addArgument(IndexTag, "" + iIndex);
        }

        return msg;
    }

    /**
     * insert shipment after shipment with index kIndex
     * @param iIndex  cust index
     * @param lX  x coord
     * @param lY  y coord
     * @param lDemand  cust demand
     * @param lEar  earliest serv time
     * @param lLat  latest serv time
     * @param lServ  length of time needed
     * @param kIndex  shipment index to insert current shipment after
     * @param truck  truck number the shipment is to be inserted into
     */
    private void insert(int iIndex, float lX, float lY, float lDemand,
        int lEar, int lLat, int lServ, int kIndex, int truck) {
        Message msg = new Message();

        if (truck < getNumTrucks()) {
            VisitNodesLinkedList v = null;
            PointCell newShip = new PointCell(iIndex, lX, lY, lDemand, lEar,
                    lLat, lServ);
            PointCell afterCell = null;
            DepotLinkedList tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
            Depot tempDepot;
            Truck tempTruck;

            tempDepot = tempDepotList.getFirst();

            // iterate through the schedule to find the right list and afterCell
            while (tempDepot != null) {
                tempTruck = tempDepot.getMainTrucks().getFirst();

                while ((tempTruck != null) &&
                        (tempTruck.getTruckNo() != truck)) {
                    tempTruck = tempTruck.next;
                }

                if (tempTruck != null) {
                    afterCell = tempTruck.mainVisitNodes.getPointCell(kIndex);
                }

                if (afterCell != null) { // cell is found
                    v = tempTruck.mainVisitNodes; // capture list reference

                    break;
                }

                tempDepot = tempDepot.next;
            }

            // insert new cell into list, if unsuccessful, it will return null
            if (v.insertAfterCellVRPTW(afterCell, newShip) != null) {
                msg.setMessageType(ConfirmTag); // successful insertion
            } else {
                msg.setMessageType(RefuseTag); // unsuccessful insertion
            }
        } else {
            msg = insert(iIndex, lX, lY, lDemand, lEar, lLat, lServ);
        }
    }

    /**
     * Builds a string message containing the shipment information
     * @param iNodeNum  position of shipment in schedule
     * @return Message  string message containing the shipment information
     */
    public Message pointString(int iNodeNum) {
        Message msg = new Message();
        int i = 1;
        DepotLinkedList tempDepotList;
        Depot tempDepot;
        Truck tempTruck;
        PointCell p = null;
        PointCell last = null;

        if (!isEmpty()) { // check if schedule is empty
            tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
            tempDepot = tempDepotList.getFirst(); // find first depot in list

            // for the number of depots in the list...
            for (int k = 0; k < tempDepotList.getNoDepots(); k++) {
                // find the first truck in depot
                tempTruck = tempDepot.getMainTrucks().first;

                // for the number of trucks in the list...
                for (int j = 0; j < tempDepot.getTotalNonEmptyTrucks(); j++) {
                    // find the first shipment in truck
                    p = tempTruck.mainVisitNodes.first();
                    last = tempTruck.mainVisitNodes.last();

                    // keep a running count of shipments until the desired one is found
                    while ((p != last) && (i < iNodeNum)) {
                        p = p.next;
                        i++;
                    }
                }
            }

            msg.setMessageType(CustomerTag);
            msg.addArgument(IndexTag, Integer.toString(p.getCellIndex()));
            msg.addArgument(XCoordTag, Float.toString(p.getXCoord()));
            msg.addArgument(YCoordTag, Float.toString(p.getYCoord()));
            msg.addArgument(DemandTag, Float.toString(p.getDemand()));
            msg.addArgument(EarlyTimeTag, Float.toString(p.getEarTime()));
            msg.addArgument(LateTimeTag, Float.toString(p.getLatTime()));
            msg.addArgument(ServiceTimeTag, Float.toString(p.getServTime()));
        }

        return msg;
    }

    /**
     * calculate cost for exchange accept(0)- remove(1) between carriers
     * @param msg  message string containing the shipment information to be removed
     * @return float  cost of schedule after exchange
     */
    public float calcExchange01(Message msg) {
        VisitNodesLinkedList v;
        float cost_diff = 0;
        int index1 = Integer.parseInt(msg.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(msg.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(msg.getValue(YCoordTag));
        float demand1 = Float.parseFloat(msg.getValue(DemandTag));
        int ear1 = Integer.parseInt(msg.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(msg.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(msg.getValue(ServiceTimeTag));
        String shipment = "" + index1 + ";" + xCoord1 + ";" + yCoord1 + ";" +
            demand1 + ";" + ear1 + ";" + latTime1 + ";" + serv1;
        v = find(index1); // find list with given shipment

        // find difference in cost
        cost_diff = Float.valueOf(v.calcExchange01(shipment)).floatValue();

        /**
         * the difference returned is a backwards, i.e. afterCost - beforeCost
         * we need a normal value, i.e. beforeCost - afterCost
         * by negating the backwards value, we get the normal value
         * i.e. (taking opposite of) afterCost - beforeCost
         */
        cost_diff *= -1;

        return cost_diff;
    }

    /**
     * perform exchange accept(0)- remove(1) between carriers
     * @param msg  message string containing the shipment information to be removed
     */
    public void exchange01(Message msg) {
        int index1 = Integer.parseInt(msg.getValue(IndexTag));

        remove(index1);
    }

    /**
     * calculate cost for exchange accept(0)- remove(2) between carriers
     * @param st1  message string containing the shipment information to be removed
     * @param st2  message string containing the shipment information to be removed
     * @return float  cost of schedule after exchange
     */
    public float calcExchange02(Message st1, Message st2) {
        VisitNodesLinkedList v;
        float cost_diff = 0;

        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
        String ship1 = "" + index1 + ";" + xCoord1 + ";" + yCoord1 + ";" +
            demand1 + ";" + ear1 + ";" + latTime1 + ";" + serv1;

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));
        String ship2 = "" + index2 + ";" + xCoord2 + ";" + yCoord2 + ";" +
            demand2 + ";" + ear2 + ";" + latTime2 + ";" + serv2;

        v = find(index1); // find list with given shipment

        // find difference in cost
        cost_diff = Float.valueOf(v.calcExchange02(ship1, ship2)).floatValue();

        /**
         * the difference returned is a backwards, i.e. afterCost - beforeCost
         * we need a normal value, i.e. beforeCost - afterCost
         * by negating the backwards value, we get the normal value
         * i.e. (taking opposite of) afterCost - beforeCost
         */
        cost_diff *= -1;

        return cost_diff;
    }

    /**
     * perform exchange accept(0)- remove(2) between carriers
     * @param st1  message string containing the shipment information to be removed
     * @param st2  message string containing the shipment information to be removed
     */
    public void exchange02(Message st1, Message st2) {
        try {
            int index1 = Integer.parseInt(st1.getValue(IndexTag));
            int index2 = Integer.parseInt(st2.getValue(IndexTag));

            remove(index1);
            remove(index2);
        } catch (Exception exc) {
        }
    }

    /**
     * calculate cost for exchange accept(1)- remove(0) between carriers
     * @param msg  message string containing the shipment information to be accepted
     * @return Message  message string containing the cost of the exchange
     */
    public Message calcExchange10(Message msg) {
        int index1 = Integer.parseInt(msg.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(msg.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(msg.getValue(YCoordTag));
        float demand1 = Float.parseFloat(msg.getValue(DemandTag));
        int ear1 = Integer.parseInt(msg.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(msg.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(msg.getValue(ServiceTimeTag));

        float before_cost = calculateCost();

        // find cost to insert shipment
        Message ncost = insertCostCar(index1, xCoord1, yCoord1, demand1, ear1,
                latTime1, serv1);
        float after_cost = Float.parseFloat(ncost.getValue(CostTag));
        int after_index = Integer.parseInt(ncost.getValue(AfterIndexTag));
        int feasibility = 0; // false
        float cost_diff = 0;

        if (after_cost < MAX_COST) {
            cost_diff = after_cost - before_cost;
            feasibility = 1; // true
        } else {
            cost_diff = MAX_COST;
        }

        Message exchangeCost = new Message();
        exchangeCost.setMessageType(ExchangeCostTag);
        exchangeCost.addArgument(CostTag, Float.toString(cost_diff));
        exchangeCost.addArgument(AfterIndexTag, Integer.toString(after_index));
        exchangeCost.addArgument(TruckNumberTag, ncost.getValue(TruckNumberTag));
        exchangeCost.addArgument(FeasibilityTag, Integer.toString(feasibility));

        return exchangeCost;
    }

    /**
     * perform exchange accept(1)- remove(0) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param after_index  shipment index to insert current shipment after
     */
    public void exchange10(Message st1, int after_index) {
        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
        int truck = Integer.parseInt(st1.getValue(TruckNumberTag, 2));

        insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
            after_index, truck);
    }

    /**
     * calculate cost for exchange accept(2)- remove(0) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param st2  message string containing the shipment information to be accepted
     * @return Message  message string containing the cost of the exchange
     */
    public Message calcExchange20(Message st1, Message st2) {
        float cost_diff = 0;
        int truck2 = -1;
        int after_index1 = -1;
        int after_index2 = -1;
        int feasibility = 0;
        Message ret = new Message();

        ret.setMessageType(ExchangeCostTag);

        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));

        float before_cost = calculateCost();
        Message ncost1 = insertCostCar(index1, xCoord1, yCoord1, demand1, ear1,
                latTime1, serv1);
        float after_cost1 = Float.parseFloat(ncost1.getValue(CostTag));
        after_index1 = Integer.parseInt(ncost1.getValue(AfterIndexTag));

        if (after_cost1 < MAX_COST) {
            insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
                after_index1, Integer.parseInt(ncost1.getValue(TruckNumberTag)));

            Message ncost2 = insertCostCar(index2, xCoord2, yCoord2, demand2,
                    ear2, latTime2, serv2);
            float after_cost2 = Float.parseFloat(ncost2.getValue(CostTag));
            truck2 = Integer.parseInt(ncost2.getValue(TruckNumberTag));
            after_index2 = Integer.parseInt(ncost2.getValue(AfterIndexTag));

            if (after_cost2 < MAX_COST) {
                cost_diff = after_cost2 - before_cost;
                feasibility = 1; // true
            } else {
                cost_diff = MAX_COST;
            }

            remove(index1);
        } else {
            cost_diff = MAX_COST;
        }

        ret.addArgument(CostTag, Float.toString(cost_diff));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index1));
        ret.addArgument(TruckNumberTag, ncost1.getValue(TruckNumberTag));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index2));
        ret.addArgument(TruckNumberTag, "" + truck2);
        ret.addArgument(FeasibilityTag, Integer.toString(feasibility));

        return ret;
    }

    /**
     * perform exchange accept(2)- remove(0) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param after_index1  shipment index to insert shipment st1 after
     * @param st2  message string containing the shipment information to be accepted
     * @param after_index2  shipment index to insert shipment st2 after
     */
    public void exchange20(Message st1, int after_index1, Message st2,
        int after_index2) {
        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
        int truck1 = Integer.parseInt(st1.getValue(TruckNumberTag, 2));

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));
        int truck2 = Integer.parseInt(st2.getValue(TruckNumberTag, 2));

        insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
            after_index1, truck1);
        insert(index2, xCoord2, yCoord2, demand2, ear2, latTime2, serv2,
            after_index2, truck2);
    }

    /**
     * calculate cost for exchange accept(1)- remove(1) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param st2  message string containing the shipment information to be removed
     * @return Message  message string containing the cost of the exchange
     * MODIFIED parameter order by OLA LALEYE on 4/14/04
     */
    public Message calcExchange11(Message st1, Message st2) {
        float cost_diff = 0;
        int after_index = -1;
        Message ret = new Message();
        float before_cost = 0;
        float after_cost = 0;
        int feasibility = 0;

        ret.setMessageType(ExchangeCostTag);

        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));
        int truck2 = Integer.parseInt(st2.getValue(TruckNumberTag));

        before_cost = calculateCost();

        int after_index2 = remove(index2);

        System.out.println("the afterIndex2 is " + after_index2); //DEBUG

        if (after_index2 == -1) {
            ret.addArgument(CostTag, Float.toString(MAX_COST));
            ret.addArgument(AfterIndexTag, "-1");
            ret.addArgument(FeasibilityTag, "-1");

            return ret;
        }

        Message ncost = insertCostCar(index1, xCoord1, yCoord1, demand1, ear1,
                latTime1, serv1);
        after_cost = Float.parseFloat(ncost.getValue(CostTag));
        after_index = Integer.parseInt(ncost.getValue(AfterIndexTag));

        System.out.println("the other afterIndex is " + after_index); //BEGUG

        if (after_cost < MAX_COST) {
            if (after_index == -1) {
                after_cost = MAX_COST;
                feasibility = 0;
            }

            cost_diff = after_cost - before_cost;
            feasibility = 1;
        } else {
            cost_diff = MAX_COST;
        }

        insert(index2, xCoord2, yCoord2, demand2, ear2, latTime2, serv2,
            after_index2, truck2);

        ret.addArgument(FeasibilityTag, "" + feasibility);
        ret.addArgument(CostTag, Float.toString(cost_diff));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index));
        ret.addArgument(TruckNumberTag, ncost.getValue(TruckNumberTag));

        //	System.out.println("the transaction before cost for carIndex is " +
        //	    before_cost);
        //	System.out.println("the transaction after cost for carIndex is " +
        //	    after_cost);
        System.out.println("the transaction cost for carIndex is " + cost_diff);

        return ret;
    }

    /**
     * perform exchange accept(1)- remove(1) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param st2  message string containing the shipment information to be removed
     * @param after_index  shipment index to insert shipment st1 after
     * ORDER OF PARAMETERS REVERSED BY OLA
     */
    public void exchange11(Message st1, Message st2, int after_index) {
        try {
            int index2 = Integer.parseInt(st2.getValue(IndexTag));

            int index1 = Integer.parseInt(st1.getValue(IndexTag));
            float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
            float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
            float demand1 = Float.parseFloat(st1.getValue(DemandTag));
            int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
            int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
            int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
            int truck1 = Integer.parseInt(st1.getValue(TruckNumberTag, 2));

            DepotLinkedList tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
            Depot tempDepot;
            Truck tempTruck;
            PointCell p = null;

            tempDepot = tempDepotList.getFirst();

            while (tempDepot != null) {
                tempTruck = tempDepot.getMainTrucks().getFirst();

                while (tempTruck != null) {
                    p = tempTruck.mainVisitNodes.getPointCell(after_index);

                    if (p != null) {
                        break;
                    }

                    tempTruck = tempTruck.next;
                }

                if (p != null) {
                    break;
                }

                tempDepot = tempDepot.next;
            }

            if ((after_index != -1) && (p != null)) {
                remove(index2);
                insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1,
                    serv1, after_index, truck1);
            } else {
                System.err.println("Err doing the 11 exchange. Afetr index is " +
                    after_index + " or p is null");
            }
        } catch (Exception exc) {
        }
    }

    /**
     * calculate cost for exchange accept(1)- remove(2) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param st2  message string containing the shipment information to be removed
     * @param st3  message string containing the shipment information to be removed
     * @return Message  message string containing the cost of the exchange
     */
    public Message calcExchange12(Message st1, Message st2, Message st3) {
        float cost_diff = 0;
        int after_index = -1;
        int feasibility = 0;
        Message ret = new Message();

        ret.setMessageType(ExchangeCostTag);

        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));
        int truck2 = Integer.parseInt(st2.getValue(TruckNumberTag));

        int index3 = Integer.parseInt(st3.getValue(IndexTag));
        float xCoord3 = Float.parseFloat(st3.getValue(XCoordTag));
        float yCoord3 = Float.parseFloat(st3.getValue(YCoordTag));
        float demand3 = Float.parseFloat(st3.getValue(DemandTag));
        int ear3 = Integer.parseInt(st3.getValue(EarlyTimeTag));
        int latTime3 = Integer.parseInt(st3.getValue(LateTimeTag));
        int serv3 = Integer.parseInt(st3.getValue(ServiceTimeTag));
        int truck3 = Integer.parseInt(st3.getValue(TruckNumberTag));

        float before_cost = calculateCost();
        int after_index2 = remove(index2);
        int after_index3 = remove(index3);

        Message ncost = insertCostCar(index1, xCoord1, yCoord1, demand1, ear1,
                latTime1, serv1);
        float after_cost = Float.parseFloat(ncost.getValue(CostTag));
        after_index = Integer.parseInt(ncost.getValue(AfterIndexTag));

        if (after_cost < MAX_COST) {
            cost_diff = after_cost - before_cost;
            feasibility = 1;
        } else {
            cost_diff = MAX_COST;
            feasibility = 0;
        }

        insert(index3, xCoord3, yCoord3, demand3, ear3, latTime3, serv3,
            after_index3, truck3);
        insert(index2, xCoord2, yCoord2, demand2, ear2, latTime2, serv2,
            after_index2, truck2);
        ret.addArgument(FeasibilityTag, "" + feasibility);
        ret.addArgument(CostTag, Float.toString(cost_diff));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index));
        ret.addArgument(TruckNumberTag, ncost.getValue(TruckNumberTag));

        return ret;
    }

    /**
     * perform exchange accept(1)- remove(2) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param after_index1  shipment index to insert shipment st1 after
     * @param st2  message string containing the shipment information to be removed
     * @param st3  message string containing the shipment information to be removed
     */
    public void exchange12(Message st1, int after_index1, Message st2,
        Message st3) {
        try {
            int index1 = Integer.parseInt(st1.getValue(IndexTag));
            float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
            float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
            float demand1 = Float.parseFloat(st1.getValue(DemandTag));
            int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
            int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
            int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
            int truck1 = Integer.parseInt(st1.getValue(TruckNumberTag, 2));

            int index2 = Integer.parseInt(st2.getValue(IndexTag));

            int index3 = Integer.parseInt(st3.getValue(IndexTag));

            remove(index2);
            remove(index3);

            insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
                after_index1, truck1);
        } catch (Exception exc) {
        }
    }

    /**
     * calculate cost for exchange accept(2)- remove(1) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param st2  message string containing the shipment information to be accepted
     * @param st3  message string containing the shipment information to be removed
     * @return Message  message string containing the cost of the exchange
     */
    public Message calcExchange21(Message st1, Message st2, Message st3) {
        float cost_diff = 0;
        int truck2 = -1;
        int after_index1 = -1;
        int after_index2 = -1;
        int feasibility = 0;
        Message ret = new Message();

        ret.setMessageType(ExchangeCostTag);

        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));

        int index3 = Integer.parseInt(st3.getValue(IndexTag));
        float xCoord3 = Float.parseFloat(st3.getValue(XCoordTag));
        float yCoord3 = Float.parseFloat(st3.getValue(YCoordTag));
        float demand3 = Float.parseFloat(st3.getValue(DemandTag));
        int ear3 = Integer.parseInt(st3.getValue(EarlyTimeTag));
        int latTime3 = Integer.parseInt(st3.getValue(LateTimeTag));
        int serv3 = Integer.parseInt(st3.getValue(ServiceTimeTag));
        int truck3 = Integer.parseInt(st3.getValue(TruckNumberTag));

        float before_cost = calculateCost();
        int after_index3 = remove(index3);

        Message ncost1 = insertCostCar(index1, xCoord1, yCoord1, demand1, ear1,
                latTime1, serv1);
        float after_cost1 = Float.parseFloat(ncost1.getValue(CostTag));
        after_index1 = Integer.parseInt(ncost1.getValue(AfterIndexTag));

        if (after_cost1 < MAX_COST) {
            insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
                after_index1, Integer.parseInt(ncost1.getValue(TruckNumberTag)));

            Message ncost2 = insertCostCar(index2, xCoord2, yCoord2, demand2,
                    ear2, latTime2, serv2);
            float after_cost2 = Float.parseFloat(ncost2.getValue(CostTag));
            truck2 = Integer.parseInt(ncost2.getValue(TruckNumberTag));
            after_index2 = Integer.parseInt(ncost2.getValue(AfterIndexTag));

            if (after_cost2 < MAX_COST) {
                cost_diff = after_cost2 - before_cost;
                feasibility = 1;
            } else {
                cost_diff = MAX_COST;
            }

            remove(index1);
        } else {
            cost_diff = MAX_COST;
        }

        insert(index3, xCoord3, yCoord3, demand3, ear3, latTime3, serv3,
            after_index3, truck3);
        ret.addArgument(FeasibilityTag, Integer.toString(feasibility));
        ret.addArgument(CostTag, Float.toString(cost_diff));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index1));
        ret.addArgument(TruckNumberTag, ncost1.getValue(TruckNumberTag));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index2));
        ret.addArgument(TruckNumberTag, "" + truck2);

        return ret;
    }

    /**
     * perform exchange accept(2)- remove(1) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param after_index1  shipment index to insert shipment st1 after
     * @param st2  message string containing the shipment information to be accepted
     * @param after_index2  shipment index to insert shipment st2 after
     * @param st3  message string containing the shipment information to be removed
     */
    public void exchange21(Message st1, int after_index1, Message st2,
        int after_index2, Message st3) {
        try {
            int index1 = Integer.parseInt(st1.getValue(IndexTag));
            float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
            float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
            float demand1 = Float.parseFloat(st1.getValue(DemandTag));
            int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
            int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
            int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
            int truck1 = Integer.parseInt(st1.getValue(TruckNumberTag, 2));

            int index2 = Integer.parseInt(st2.getValue(IndexTag));
            float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
            float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
            float demand2 = Float.parseFloat(st2.getValue(DemandTag));
            int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
            int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
            int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));
            int truck2 = Integer.parseInt(st2.getValue(TruckNumberTag, 2));

            int index3 = Integer.parseInt(st3.getValue(IndexTag));

            remove(index3);

            insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
                after_index1, truck1);
            insert(index2, xCoord2, yCoord2, demand2, ear2, latTime2, serv2,
                after_index2, truck2);
        } catch (Exception exc) {
        }
    }

    /**
     * calculate cost for exchange accept(2)- remove(2) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param st2  message string containing the shipment information to be accepted
     * @param st3  message string containing the shipment information to be removed
     * @param st4  message string containing the shipment information to be removed
     * @return Message  message string containing the cost of the exchange
     */
    public Message calcExchange22(Message st1, Message st2, Message st3,
        Message st4) {
        float cost_diff = 0;
        int truck2 = -1;
        int after_index1 = -1;
        int after_index2 = -1;
        int feasibility = 0;
        Message ret = new Message();

        ret.setMessageType(ExchangeCostTag);

        int index1 = Integer.parseInt(st1.getValue(IndexTag));
        float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
        float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
        float demand1 = Float.parseFloat(st1.getValue(DemandTag));
        int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));

        int index2 = Integer.parseInt(st2.getValue(IndexTag));
        float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
        float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
        float demand2 = Float.parseFloat(st2.getValue(DemandTag));
        int ear2 = Integer.parseInt(st1.getValue(EarlyTimeTag));
        int latTime2 = Integer.parseInt(st1.getValue(LateTimeTag));
        int serv2 = Integer.parseInt(st1.getValue(ServiceTimeTag));

        int index3 = Integer.parseInt(st3.getValue(IndexTag));
        float xCoord3 = Float.parseFloat(st3.getValue(XCoordTag));
        float yCoord3 = Float.parseFloat(st3.getValue(YCoordTag));
        float demand3 = Float.parseFloat(st3.getValue(DemandTag));
        int ear3 = Integer.parseInt(st3.getValue(EarlyTimeTag));
        int latTime3 = Integer.parseInt(st3.getValue(LateTimeTag));
        int serv3 = Integer.parseInt(st3.getValue(ServiceTimeTag));
        int truck3 = Integer.parseInt(st3.getValue(TruckNumberTag));

        int index4 = Integer.parseInt(st4.getValue(IndexTag));
        float xCoord4 = Float.parseFloat(st4.getValue(XCoordTag));
        float yCoord4 = Float.parseFloat(st4.getValue(YCoordTag));
        float demand4 = Float.parseFloat(st4.getValue(DemandTag));
        int ear4 = Integer.parseInt(st4.getValue(EarlyTimeTag));
        int latTime4 = Integer.parseInt(st4.getValue(LateTimeTag));
        int serv4 = Integer.parseInt(st4.getValue(ServiceTimeTag));
        int truck4 = Integer.parseInt(st4.getValue(TruckNumberTag));

        float before_cost = calculateCost();
        int after_index3 = remove(index3);
        int after_index4 = remove(index4);

        Message ncost1 = insertCostCar(index1, xCoord1, yCoord1, demand1, ear1,
                latTime1, serv1);
        float after_cost1 = Float.parseFloat(ncost1.getValue(CostTag));
        after_index1 = Integer.parseInt(ncost1.getValue(AfterIndexTag));

        if (after_cost1 < MAX_COST) {
            insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
                after_index1, Integer.parseInt(ncost1.getValue(TruckNumberTag)));

            Message ncost2 = insertCostCar(index2, xCoord2, yCoord2, demand2,
                    ear2, latTime2, serv2);
            float after_cost2 = Float.parseFloat(ncost2.getValue(CostTag));
            truck2 = Integer.parseInt(ncost2.getValue(TruckNumberTag));
            after_index2 = Integer.parseInt(ncost2.getValue(AfterIndexTag));

            if (after_cost2 < MAX_COST) {
                cost_diff = after_cost2 - before_cost;
                feasibility = 1;
            } else {
                cost_diff = MAX_COST;
            }

            remove(index1);
        } else {
            cost_diff = MAX_COST;
        }

        insert(index4, xCoord4, yCoord4, demand4, ear4, latTime4, serv4,
            after_index4, truck4);
        insert(index3, xCoord3, yCoord3, demand3, ear3, latTime3, serv3,
            after_index3, truck3);

        ret.addArgument(CostTag, Float.toString(cost_diff));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index1));
        ret.addArgument(TruckNumberTag, ncost1.getValue(TruckNumberTag));
        ret.addArgument(AfterIndexTag, Integer.toString(after_index2));
        ret.addArgument(TruckNumberTag, "" + truck2);
        ret.addArgument(FeasibilityTag, "" + feasibility);

        return ret;
    }

    /**
     * perform exchange accept(2)- remove(2) between carriers
     * @param st1  message string containing the shipment information to be accepted
     * @param after_index1  shipment index to insert shipment st1 after
     * @param st2  message string containing the shipment information to be accepted
     * @param after_index2  shipment index to insert shipment st2 after
     * @param st3  message string containing the shipment information to be removed
     * @param st4  message string containing the shipment information to be removed
     */
    public void exchange22(Message st1, int after_index1, Message st2,
        int after_index2, Message st3, Message st4) {
        //DEBUG
        System.out.println("OFFICIALLY INSDE EXH-22");
        System.out.println("ship to accpet1 message is " +
            st1.getMessageString());
        System.out.println("ship to accept2 message is " +
            st2.getMessageString());
        System.out.println("ship to remove1 message is " +
            st3.getMessageString());
        System.out.println("ship to remove2 message is " +
            st4.getMessageString());

        try {
            int index1 = Integer.parseInt(st1.getValue(IndexTag));
            float xCoord1 = Float.parseFloat(st1.getValue(XCoordTag));
            float yCoord1 = Float.parseFloat(st1.getValue(YCoordTag));
            float demand1 = Float.parseFloat(st1.getValue(DemandTag));
            int ear1 = Integer.parseInt(st1.getValue(EarlyTimeTag));
            int latTime1 = Integer.parseInt(st1.getValue(LateTimeTag));
            int serv1 = Integer.parseInt(st1.getValue(ServiceTimeTag));
            int truck1 = Integer.parseInt(st1.getValue(TruckNumberTag, 2));

            int index2 = Integer.parseInt(st2.getValue(IndexTag));
            float xCoord2 = Float.parseFloat(st2.getValue(XCoordTag));
            float yCoord2 = Float.parseFloat(st2.getValue(YCoordTag));
            float demand2 = Float.parseFloat(st2.getValue(DemandTag));
            int ear2 = Integer.parseInt(st2.getValue(EarlyTimeTag));
            int latTime2 = Integer.parseInt(st2.getValue(LateTimeTag));
            int serv2 = Integer.parseInt(st2.getValue(ServiceTimeTag));
            int truck2 = Integer.parseInt(st2.getValue(TruckNumberTag, 2));

            int index3 = Integer.parseInt(st3.getValue(IndexTag));

            int index4 = Integer.parseInt(st4.getValue(IndexTag));

            remove(index3);
            remove(index4);
            insert(index1, xCoord1, yCoord1, demand1, ear1, latTime1, serv1,
                after_index1, truck1);
            insert(index2, xCoord2, yCoord2, demand2, ear2, latTime2, serv2,
                after_index2, truck2);
        } catch (Exception exc) {
        }
    }

    /**
     * Calculates the cost of the schedule
     * @return float  cost of the schedule
     */
    public float calculateCost() {
        ProblemInfo.depotLLLevelCostF.setTotalCost(theZeus.getRoot().getVRPTW().mainDepots);

        return (float) ProblemInfo.depotLLLevelCostF.getTotalCost(theZeus.getRoot()
                                                                         .getVRPTW().mainDepots);
    }

    /**
     * Perform local optimizations on the schedule
     */
    public synchronized void opts() {
        theZeus.getRoot().getVRPTW().runOptimize();
    }

    public synchronized void reschedule() {
        //get the shipment linked list, ok b/c method is synchronized
        //unassign all the shipments
        shipListAdaptor.shipLL.setNoShipmentsAssigned(0);

        Shipment ship = shipListAdaptor.shipLL.getFirst();

        while (ship != null) {
            ship.setAssigned(false);
            ship = ship.next;
        }

        shipListAdaptor.shipLL.printShipNos();

        //clear out Zeus
        Depot depot = theZeus.getRoot().getVRPTW().mainDepots.getFirst();

        while (depot != null) {
            Truck truck = depot.mainTrucks.getFirst();

            while (truck != null) {
                truck.mainVisitNodes.emptyList();
                truck = truck.next;
            }

            depot = depot.next;
        }

        calcCurrentValues();

        //run the push insertion algorithm
        boolean isDiagnostic = false;

        if (isDiagnostic) {
            System.out.println("Running reschedule with " +
                shipListAdaptor.shipLL.getCountShipments() + " shipments");
        }

        /* Get the shipment that is closest to a depot with respect to the
        criteria
        The method for assinging customers to the depots is as follows.
        Iterate throught the customer list inserting the customer to the truck,
        as long as it does not violate the constraints.  If it does, create
        another truck and insert the customer there.  Customers will be inserted
        into the trucks in a position that yields the lowest cost until all
        the customers have been serviced.
        */

        // get depot number to assign customers to, will always be 1 for VRPTW problem
        Depot thisDepot = theZeus.getRoot().getVRPTW().mainDepots.getFirst();
        int depotNo = thisDepot.getDepotNo();

        // start iteration of list at the beginning
        Shipment tempShip = shipListAdaptor.shipLL.first;
        int countShips = shipListAdaptor.shipLL.getCountShipments();

        //loop while all shipments have not been assigned to the depot
        for (int countAssignLoop = 0;
                (countAssignLoop < countShips) && (tempShip != null);
                countAssignLoop++) {
            //Getting the closest shipment for the depot using the heuristic search
            tempShip = theZeus.getRoot().getVRPTW().getClosestShipmentVRPTW(heuristicType);

            boolean status = theZeus.getRoot().getVRPTW().mainDepots.insertShipmentVRPTW(tempShip,
                    depotNo); //add the shipment to the depot

            if (status == true) {
                tempShip.assigned = true;
            }

            if ((status != true) && isDiagnostic) {
                System.out.println("VRPTW: Shipment could not be inserted");
            }

            //           System.out.println(countAssignLoop);
            if (isDiagnostic) {
                System.out.println("The depot  and shipment is      : " +
                    depotNo + " " + tempShip);
            }

            if ((countAssignLoop % (countShips / HermesGlobals.auctionBreak)) == 0) {
                calcCurrentValues();
                opts();
            }
        }

        //Update all the status in the CostFunctions interface
        calcCurrentValues();

        //optimize solution
        opts();
        calcCurrentValues();
    }

    /**
     * Remove the shipment from the schedule
     * @param iIndex  index of the shipment to be removed
     * @return int  index of shipment the removed shipment was scheduled after
     */
    public int remove(int iIndex) {
        int indx = -1;
        Depot tempDepot = theZeus.getRoot().getVRPTW().mainDepots.getFirst();
        Truck tempTruck;

        while (tempDepot != null) {
            tempTruck = tempDepot.getMainTrucks().getFirst();

            while (tempTruck != null) {
                indx = tempTruck.mainVisitNodes.remove(iIndex);

                if (indx > -1) {
                    return indx;
                }

                tempTruck = tempTruck.next;
            }

            tempDepot = tempDepot.next;
        }

        return indx;
    }

    /**
     * Find the list the shipment from the schedule belongs to
     * @param iIndex  index of the shipment to be found
     * @return VisitNodesLinkedList  list containing the shipment
     */
    public VisitNodesLinkedList find(int iIndex) {
        VisitNodesLinkedList v = null;
        Depot tempDepot = theZeus.getRoot().getVRPTW().mainDepots.getFirst();
        Truck tempTruck;

        while (tempDepot != null) {
            tempTruck = tempDepot.getMainTrucks().getFirst();

            while (tempTruck != null) {
                if (tempTruck.mainVisitNodes.getPointCell(iIndex) != null) {
                    v = tempTruck.mainVisitNodes;

                    break;
                }

                tempTruck = tempTruck.next;
            }

            tempDepot = tempDepot.next;
        }

        return v;
    }

    /**
     * Get the x-coord of the shipment with given index
     * @param index  index of shipment
     * @return float  x-coord of shipment
     */
    public float getX(int index) {
        VisitNodesLinkedList v = find(index);
        v.getPointCell(index);

        return v.getPointCell(index).getXCoord();
    }

    /**
     * Get the y-coord of the shipment with given index
     * @param index  index of shipment
     * @return float  y-coord of shipment
     */
    public float getY(int index) {
        VisitNodesLinkedList v = find(index);
        v.getPointCell(index);

        return v.getPointCell(index).getYCoord();
    }

    /**
     * Finds the position of the shipment in the schedule
     * @param n  index of shipment to be found in the schedule
     * @return int  position of shipment in the schedule
     */
    public synchronized int getIndexPoint(int n) {
        int i = 1;
        DepotLinkedList tempDepotList;
        Depot tempDepot;
        Truck tempTruck;

        if (!isEmpty()) { // check if schedule is empty
            tempDepotList = theZeus.getRoot().getVRPTW().mainDepots;
            tempDepot = tempDepotList.getFirst(); // find first depot in list

            // for the number of depots in the list...
            for (int k = 0; k < tempDepotList.getNoDepots(); k++) {
                // find the first truck in depot
                tempTruck = tempDepot.getMainTrucks().first;

                // for the number of trucks in the list...
                for (int j = 0; j < tempDepot.getTotalNonEmptyTrucks(); j++) {
                    // find the first shipment in truck
                    PointCell p = tempTruck.mainVisitNodes.first();
                    PointCell last = tempTruck.mainVisitNodes.last();

                    // keep a running count of shipments until the desired one is found
                    for (i = i; (p.index != n) && (p != last); i++) {
                        p = p.next;
                    }

                    tempTruck = tempTruck.next;
                }

                tempDepot = tempDepot.next;
            }
        }

        return i;
    }

    /**
     * set the constraints for the scheduling problem
     * @param msg  constraints for the scheduling problem
     */
    public void setProblemConstraints(Message msg) {
        int indx = 0;
        float depotX = 0;
        float depotY = 0;
        int NumOfDepots = Integer.parseInt(msg.getValue(NumberOfDepotsTag));
        ProblemInfo.noOfDepots = NumOfDepots;
        ProblemInfo.fileName = msg.getValue(FileNameTag);
        ProblemInfo.maxCapacity = Integer.parseInt(msg.getValue(MaxCapacityTag));
        ProblemInfo.maxDistance = Integer.parseInt(msg.getValue(MaxDistanceTag));
        theZeus.getRoot().getVRPTW().mainShipments.setNoDepots(NumOfDepots);

        if (NumOfDepots > 1) {
            for (int i = 1; i == NumOfDepots; i++) {
                indx = Integer.parseInt(msg.getValue(IndexTag, i));
                depotX = Float.parseFloat(msg.getValue(XCoordTag, i));
                depotY = Float.parseFloat(msg.getValue(YCoordTag, i));
                theZeus.getRoot().getVRPTW().mainShipments.insertDepotPosition(indx,
                    depotX, depotY);
            }
        } else {
            indx = Integer.parseInt(msg.getValue(IndexTag));
            ProblemInfo.depotX = Double.parseDouble(msg.getValue(XCoordTag));
            ProblemInfo.depotY = Double.parseDouble(msg.getValue(YCoordTag));
            theZeus.getRoot().getVRPTW().mainShipments.insertDepotPosition(indx,
                (float) ProblemInfo.depotX, (float) ProblemInfo.depotY);
        }

        theZeus.getRoot().getVRPTW().mainDepots.createDepots(theZeus.getRoot()
                                                                    .getVRPTW().mainShipments);
    }

    /**
     * this method finds amongst the shipments of this carrier, the best savings if shipment taken out.
     * That is the most expensive shipment to carry. It returns shipment info as message...
     * @return message containing info on best savings on the schedule
     * ADDED BY OLA on 04/11/04
     */
    public Message findBestSavings() {
        //all shipment info
        int iIndex = 0;
        float lX = 0;
        float lY = 0;
        float lDemand = 0;
        int lEar = 0;
        int lLat = 0;
        int lServ = 0;
        int truck = 0;
        float pointcell_Savings = 0; //cost saved by taking out shipment
        float Highest_Cost = -1; //may need non primitive Float
        float curr_Cost = 0;
        int Max_index; //maybe not necessary
        int feasibility = 1;
        Message msg = new Message();
        Truck currTruck;
        PointCell currPointcell;
        int nodesOnTruck;
        Depot myDepot = theZeus.getRoot().getVRPTW().mainDepots.getFirst(); //get first depot

        while (myDepot != null) {
            //myDepot.clearEmptyTrucks();
            currTruck = myDepot.getMainTrucks().getFirst(); //get first truck in list

            while (currTruck != null) {
                nodesOnTruck = currTruck.getNoNodes();
                currPointcell = currTruck.mainVisitNodes.first().next;

                for (int i = 0;
                        (i < nodesOnTruck) &&
                        (currPointcell != currTruck.mainVisitNodes.last().next);
                        i++) {
                    curr_Cost = calcSavings(currPointcell); //obtain cost of pointcell

                    if (Highest_Cost < curr_Cost) {
                        Highest_Cost = curr_Cost;

                        //update necessary pointcell info
                        iIndex = currPointcell.getCellIndex();
                        lX = currPointcell.getXCoord();
                        lY = currPointcell.getYCoord();
                        lDemand = currPointcell.getDemand();
                        lEar = (int) currPointcell.getEarTime();
                        lLat = (int) currPointcell.getLatTime();
                        lServ = (int) currPointcell.getServTime();
                        truck = currTruck.getTruckNo();
                        pointcell_Savings = curr_Cost; //update savings
                    }

                    currPointcell = currPointcell.getNext();
                }

                currTruck = currTruck.next; //get next truck
            }

            myDepot = myDepot.next; //get next depot for multi depots
        }

        //Now, bundle pointcell info into a message and return
        msg = new Message();
        msg.setMessageType(BestSavingsTag);

        //add depot & truck info to msg
        //msg.addArgument(DepotNumberTag, Integer.toString(theDepotNo));
        //msg.addArgument(TruckNumberTag, Integer.toString(theTruckNo));
        //add pointcell info to msg
        msg.addArgument(FeasibilityTag, Integer.toString(feasibility));
        msg.addArgument(IndexTag, Integer.toString(iIndex));
        msg.addArgument(XCoordTag, Float.toString(lX));
        msg.addArgument(YCoordTag, Float.toString(lY));
        msg.addArgument(DemandTag, Float.toString(lDemand));
        msg.addArgument(EarlyTimeTag, Integer.toString(lEar));
        msg.addArgument(LateTimeTag, Integer.toString(lLat));
        msg.addArgument(ServiceTimeTag, Integer.toString(lServ));
        msg.addArgument(TruckNumberTag, Integer.toString(truck));
        msg.addArgument(PointCellSavingsTag, Float.toString(pointcell_Savings));

        return msg;
    }

    /**
     * this method finds the 2 best savings in a schedule, bundle them in a message
     * and returns the message.
     * @return message with both shipment's info
     */
    public Message findTwoBestSavings() {
        //all shipment info
        int iIndex1 = 0;
        int iIndex2 = 0;
        float lX1 = 0;
        float lX2 = 0;
        float lY1 = 0;
        float lY2 = 0;
        float lDemand1 = 0;
        float lDemand2 = 0;
        int lEar1 = 0;
        int lEar2 = 0;
        int lLat1 = 0;
        int lLat2 = 0;
        int lServ1 = 0;
        int lServ2 = 0;
        int truck = 0;
        float MaxPointcell_Savings = -100; //cost saved by taking out shipment
        float total_Savings = 0; //cost saved by taking out both shipment
        float curr_Cost = 0;
        PointCell Max_Pointcell = new PointCell();
        int feasibility = 0;
        int Max_index = -1;
        VisitNodesLinkedList Max_V; //keeps track on LL on which max savings is
        Message msg = new Message();
        Truck currTruck;
        PointCell currPointcell;
        int nodesOnTruck;
        Depot myDepot = theZeus.getRoot().getVRPTW().mainDepots.getFirst(); //get first depot

        while (myDepot != null) {
            //myDepot.clearEmptyTrucks();
            currTruck = myDepot.getMainTrucks().getFirst(); //get first truck in list

            while (currTruck != null) {
                nodesOnTruck = currTruck.getNoNodes();
                currPointcell = currTruck.mainVisitNodes.first().next;

                for (int i = 0;
                        (i < nodesOnTruck) &&
                        (currPointcell != currTruck.mainVisitNodes.last().next);
                        i++) {
                    curr_Cost = calcSavings(currPointcell); //obtain cost of pointcell

                    if (MaxPointcell_Savings < curr_Cost) {
                        //keep track of necessarypointcell info
                        Max_Pointcell = currPointcell;
                        Max_index = currPointcell.getCellIndex();
                        MaxPointcell_Savings = curr_Cost; //update savings
                        truck = currTruck.getTruckNo();
                    }

                    currPointcell = currPointcell.getNext();
                }

                currTruck = currTruck.next; //get next truck
            }

            myDepot = myDepot.next; //get next depot for multi depots
        }

        //now, determine second 1st and second ships
        Max_V = find(Max_index); //determine visitednodesLL

        if ((Max_Pointcell.next != null) &&
                (Max_Pointcell.next.getCellIndex() != -1)) {
            //setup first shipinfo
            feasibility = 1;
            iIndex1 = Max_Pointcell.getCellIndex();
            lX1 = Max_Pointcell.getXCoord();
            lY1 = Max_Pointcell.getYCoord();
            lDemand1 = Max_Pointcell.getDemand();
            lEar1 = (int) Max_Pointcell.getEarTime();
            lLat1 = (int) Max_Pointcell.getLatTime();
            lServ1 = (int) Max_Pointcell.getServTime();

            //setup second ship info
            iIndex2 = Max_Pointcell.next.getCellIndex();
            lX2 = Max_Pointcell.next.getXCoord();
            lY2 = Max_Pointcell.next.getYCoord();
            lDemand2 = Max_Pointcell.next.getDemand();
            lEar2 = (int) Max_Pointcell.next.getEarTime();
            lLat2 = (int) Max_Pointcell.next.getLatTime();
            lServ2 = (int) Max_Pointcell.next.getServTime();
        } else if ((Max_Pointcell.next.getCellIndex() == -1) &&
                (Max_Pointcell.prev.getCellIndex() != 0)) {
            //setup first shipinfo
            feasibility = 1;
            iIndex1 = Max_Pointcell.prev.getCellIndex();
            lX1 = Max_Pointcell.prev.getXCoord();
            lY1 = Max_Pointcell.prev.getYCoord();
            lDemand1 = Max_Pointcell.prev.getDemand();
            lEar1 = (int) Max_Pointcell.prev.getEarTime();
            lLat1 = (int) Max_Pointcell.prev.getLatTime();
            lServ1 = (int) Max_Pointcell.prev.getServTime();

            //setup second ship info
            iIndex2 = Max_Pointcell.getCellIndex();
            lX2 = Max_Pointcell.getXCoord();
            lY2 = Max_Pointcell.getYCoord();
            lDemand2 = Max_Pointcell.getDemand();
            lEar2 = (int) Max_Pointcell.getEarTime();
            lLat2 = (int) Max_Pointcell.getLatTime();
            lServ2 = (int) Max_Pointcell.getServTime();
        } else if ((Max_Pointcell.next.getCellIndex() != -1) &&
                (Max_Pointcell.prev.getCellIndex() != 0)) { //there is only one ship in schedule
            feasibility = 0;
        }

        //Now, bundle pointcells info into a message and return
        msg = new Message();
        msg.setMessageType(BestTwoSavingsTag);
        msg.addArgument(FeasibilityTag, Integer.toString(feasibility));

        if (feasibility != 0) {
            //determine total savings
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(Max_V); // calculate and set the total route time before removing

            float oldCost = (float) ProblemInfo.vNodesLevelCostF.getTotalCost(Max_V); // get the current total route time before removing
            int kIndex1 = remove(iIndex1); //remove both ships in order
            int kIndex2 = remove(iIndex2);
            ProblemInfo.vNodesLevelCostF.calculateTotalsStats(Max_V); // calculate and set the total route time after removing

            float newCost = (float) ProblemInfo.vNodesLevelCostF.getTotalCost(Max_V); // get the current total route time after removing
            total_Savings = oldCost - newCost;
            insert(iIndex2, lX2, lY2, lDemand2, lEar2, lLat2, lServ2, kIndex2,
                truck);
            insert(iIndex1, lX1, lY1, lDemand1, lEar1, lLat1, lServ1, kIndex1,
                truck); //reinsert ships

            //add necessary pointcell info to msg if necessary
            Message saveMsgA = new Message();
            Message saveMsgB = new Message();
            saveMsgA.setMessageType(BestSavingsTag);
            saveMsgB.setMessageType(BestSavingsTag);

            //bundle first ship info
            saveMsgA.addArgument(IndexTag, Integer.toString(iIndex1));
            saveMsgA.addArgument(XCoordTag, Float.toString(lX1));
            saveMsgA.addArgument(YCoordTag, Float.toString(lY1));
            saveMsgA.addArgument(DemandTag, Float.toString(lDemand1));
            saveMsgA.addArgument(EarlyTimeTag, Integer.toString(lEar1));
            saveMsgA.addArgument(LateTimeTag, Integer.toString(lLat1));
            saveMsgA.addArgument(ServiceTimeTag, Integer.toString(lServ1));
            saveMsgA.addArgument(TruckNumberTag, Integer.toString(truck));

            //bundle up second ship
            saveMsgB.addArgument(IndexTag, Integer.toString(iIndex2));
            saveMsgB.addArgument(XCoordTag, Float.toString(lX2));
            saveMsgB.addArgument(YCoordTag, Float.toString(lY2));
            saveMsgB.addArgument(DemandTag, Float.toString(lDemand2));
            saveMsgB.addArgument(EarlyTimeTag, Integer.toString(lEar2));
            saveMsgB.addArgument(LateTimeTag, Integer.toString(lLat2));
            saveMsgB.addArgument(ServiceTimeTag, Integer.toString(lServ2));
            saveMsgB.addArgument(TruckNumberTag, Integer.toString(truck));

            msg.addArgument(saveMsgA);
            msg.addArgument(saveMsgB);
            msg.addArgument(TotalSavingsTag, Float.toString(total_Savings));
            msg.addArgument(TruckNumberTag, Integer.toString(truck)); //add in truck number
        }

        return msg;
    }

    /**
     * this method will calculate and the cost of a single pointcell on the schedule
     * ADDED BY OLA LALEYE for Hermes on 04/10/04 based on calcPushCost in VisitNodeLinkedList class
     * @param currPointcell pointcell on visitnodeLL
     * @return cost that will be saved by taking out pointcell
     */
    public synchronized float calcSavings(PointCell currPointcell) {
        int iIndex = 0;
        float lX = 0;
        float lY = 0;
        float lDemand = 0;
        int lEar = 0;
        int lLat = 0;
        int lServ = 0;
        int truck = 0;

        //PointCell phead,plast;//=new PointCell();
        VisitNodesLinkedList v = find(currPointcell.getCellIndex());

        //phead=v.getHead();
        //plast=v.getLast();
        int kIndex = 0;
        float oldTime = 0;
        float newTime = 0; // time of route after insertion
        float push = 0; // time new node will add to route pushing the arrival times of subsequent nodes back

        //	currPointcell.displayPointCell(); //FOR DEBUG
        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(v); // calculate and set the total route time before removing
        oldTime = (float) ProblemInfo.vNodesLevelCostF.getTotalCost(v); // get the current total route time before removing

        //back up cell info
        iIndex = currPointcell.getCellIndex();
        lX = currPointcell.getXCoord();
        lY = currPointcell.getYCoord();
        lDemand = currPointcell.getDemand();
        lEar = (int) currPointcell.getEarTime();
        lLat = (int) currPointcell.getLatTime();
        lServ = (int) currPointcell.getServTime();
        truck = v.truckNo;

        //remove cell
        kIndex = remove(currPointcell.getCellIndex());

        ProblemInfo.vNodesLevelCostF.calculateTotalsStats(v); // calculate and set the total route time after removing
        newTime = (float) ProblemInfo.vNodesLevelCostF.getTotalCost(v); // get the current total route time after removing

        push = oldTime - newTime;
        System.out.println("the savings cost for carIndex " + iIndex + " is " +
            push); //FOR DEBUG

        insert(iIndex, lX, lY, lDemand, lEar, lLat, lServ, kIndex, truck);

        //if(!(msg.getMessageType().equals(ConfirmTag)))
        //System.err.println("Error reinserting shipment inside calcSavings method");
        return push;
    }
}
