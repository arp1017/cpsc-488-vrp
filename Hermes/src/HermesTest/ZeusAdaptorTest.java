package HermesTest;

import Hermes.Message;
import Hermes.MessageTags;
import Hermes.ZeusAdaptor;

import Zeus.*;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: ZeusAdaptorTest.java</p>
 * <p>Description: Testing class for the ZeusAdaptor class</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ZeusAdaptorTest extends TestCase {
    private ZeusAdaptor za = null;
    private Depot tempDepot = null;
    private Truck tempTruck = null;
    private Message msg = new Message();
    private int noOfDepots = 1;
    private int noOfCust = 3;
    private String fileName = "fileName";
    private int capacity = 200;
    private int distance = 230;
    private int indx = 1;
    private float depotX = 25;
    private float depotY = 30;
    private float xCoord = 26;
    private float yCoord = 31;
    private float demand = 1;
    private int earTime = 1;
    private int latTime = 30;
    private int servTime = 1;

    /**
    * Constructor used with the suite
    * @param name  name of the test
    */
    public ZeusAdaptorTest(String name) {
        super(name); //pass it to parent class
    }

    /**
    * Sets order tests will be run in
    * @return Test  testing order
    */
    public static Test suite() {
        TestSuite suite = new TestSuite("ZeusAdaptorTest");
        suite.addTest(new ZeusAdaptorTest("testSetProblemConstraints"));
        suite.addTest(new ZeusAdaptorTest("testInsertMethods"));
        suite.addTest(new ZeusAdaptorTest("testFindRemove"));
        suite.addTest(new ZeusAdaptorTest("testIsEmpty"));
        suite.addTest(new ZeusAdaptorTest("testGetIndexPoint"));
        suite.addTest(new ZeusAdaptorTest("testGetSize"));
        suite.addTest(new ZeusAdaptorTest("testCalcExchangeMethods"));
        suite.addTest(new ZeusAdaptorTest("testExchangeMethods"));
        suite.addTest(new ZeusAdaptorTest("testGetDepotCoords"));
        suite.addTest(new ZeusAdaptorTest("testCalcTotalMethods"));
        suite.addTest(new ZeusAdaptorTest("testPointString"));
        suite.addTest(new ZeusAdaptorTest("testGetAllVertices"));

        return suite;
    }

    /**
    * Executed once for each of the tests
    */
    protected void setUp() {
        System.out.println("Entering setUp");

        // set up the problem constraints message for the test case
        msg.addArgument(MessageTags.NumberOfDepotsTag, "" + noOfDepots); // 1 depot
        msg.addArgument(MessageTags.FileNameTag, fileName); // arbitrary name
        msg.addArgument(MessageTags.MaxCapacityTag, "" + capacity); // 200 capacity
        msg.addArgument(MessageTags.MaxDistanceTag, "" + distance); // 230 distance
        msg.addArgument(MessageTags.IndexTag, "" + indx); // index of depot
        msg.addArgument(MessageTags.XCoordTag, "" + depotX); // x-coord of depot
        msg.addArgument(MessageTags.YCoordTag, "" + depotY); // y-coord of depot
        msg.addArgument(MessageTags.NumberOfCustomersTag, "" + noOfCust); // num of customers

        za = new ZeusAdaptor(new Zeus()); // create an instance of class to test
        za.setProblemConstraints(msg); // set up problem constraints to test
        za.insert(indx, xCoord, yCoord, demand, earTime, latTime, servTime); // default shipment
        msg = new Message();
        msg.addArgument(MessageTags.IndexTag, "" + indx); // index of shipment
        msg.addArgument(MessageTags.XCoordTag, "" + xCoord); // x-coord of shipment
        msg.addArgument(MessageTags.YCoordTag, "" + yCoord); // y-coord of shipment
        msg.addArgument(MessageTags.DemandTag, "" + demand);
        msg.addArgument(MessageTags.EarlyTimeTag, "" + earTime);
        msg.addArgument(MessageTags.LateTimeTag, "" + latTime);
        msg.addArgument(MessageTags.ServiceTimeTag, "" + servTime);
        System.out.println("Exiting setUp");
    }

    /**
     * Tests each of the 'calculating the cost of exchange' methods.
     * Methods should find the cost to insert a method into the schedule
     * without leaving the shipment in the schedule
     */
    public void testCalcExchangeMethods() {
        System.out.println("Entering testCalcExchangeMethods");

        Message msg2 = new Message();
        Message msg3 = new Message();
        Message msg4 = new Message();
        Message response = new Message();
        float cost = 0;
        float initialOne = za.calculateCost();
        float initialTwo;
        float initialThree;
        float initialFour;
        float costOfOneTwo;
        float costOfTwoThree;
        float costOfThreeFour;

        // info for the second shipment
        int indx2 = 2;
        float xCoord2 = 30;
        float yCoord2 = 40;
        float demand2 = 100;
        int earTime2 = 30;
        int latTime2 = 40;
        int servTime2 = 1;

        // info for the third shipment
        int indx3 = 3;
        float xCoord3 = 35;
        float yCoord3 = 45;
        float demand3 = 10;
        int earTime3 = 40;
        int latTime3 = 50;
        int servTime3 = 1;

        // info for the fourth shipment
        int indx4 = 4;
        float xCoord4 = 30;
        float yCoord4 = 45;
        float demand4 = 20;
        int earTime4 = 50;
        int latTime4 = 60;
        int servTime4 = 1;

        msg2.addArgument(MessageTags.IndexTag, "" + indx2); // index of shipment
        msg2.addArgument(MessageTags.XCoordTag, "" + xCoord2); // x-coord of shipment
        msg2.addArgument(MessageTags.YCoordTag, "" + yCoord2); // y-coord of shipment
        msg2.addArgument(MessageTags.DemandTag, "" + demand2);
        msg2.addArgument(MessageTags.EarlyTimeTag, "" + earTime2);
        msg2.addArgument(MessageTags.LateTimeTag, "" + latTime2);
        msg2.addArgument(MessageTags.ServiceTimeTag, "" + servTime2);

        msg3.addArgument(MessageTags.IndexTag, "" + indx3); // index of shipment
        msg3.addArgument(MessageTags.XCoordTag, "" + xCoord3); // x-coord of shipment
        msg3.addArgument(MessageTags.YCoordTag, "" + yCoord3); // y-coord of shipment
        msg3.addArgument(MessageTags.DemandTag, "" + demand3);
        msg3.addArgument(MessageTags.EarlyTimeTag, "" + earTime3);
        msg3.addArgument(MessageTags.LateTimeTag, "" + latTime3);
        msg3.addArgument(MessageTags.ServiceTimeTag, "" + servTime3);

        msg4.addArgument(MessageTags.IndexTag, "" + indx4); // index of shipment
        msg4.addArgument(MessageTags.XCoordTag, "" + xCoord4); // x-coord of shipment
        msg4.addArgument(MessageTags.YCoordTag, "" + yCoord4); // y-coord of shipment
        msg4.addArgument(MessageTags.DemandTag, "" + demand4);
        msg4.addArgument(MessageTags.EarlyTimeTag, "" + earTime4);
        msg4.addArgument(MessageTags.LateTimeTag, "" + latTime4);
        msg4.addArgument(MessageTags.ServiceTimeTag, "" + servTime4);

        // calculate some benchmarks
        za.remove(indx);

        za.insert(indx2, xCoord2, yCoord2, demand2, earTime2, latTime2,
            servTime2);
        initialTwo = za.calculateCost();
        za.remove(indx2);

        za.insert(indx3, xCoord3, yCoord3, demand3, earTime3, latTime3,
            servTime3);
        initialThree = za.calculateCost();

        za.insert(indx2, xCoord2, yCoord2, demand2, earTime2, latTime2,
            servTime2);
        costOfTwoThree = za.calculateCost();
        za.remove(indx2);

        za.insert(indx4, xCoord4, yCoord4, demand4, earTime4, latTime4,
            servTime4);
        costOfThreeFour = za.calculateCost();
        za.remove(indx3);

        initialFour = za.calculateCost();
        za.remove(indx4);

        za.insert(indx, xCoord, yCoord, demand, earTime, latTime, servTime);

        // continue testing
        cost = za.calcExchange01(msg);
        assertTrue(cost == (-1 * initialOne));

        za.insert(indx2, xCoord2, yCoord2, demand2, earTime2, latTime2,
            servTime2);
        costOfOneTwo = za.calculateCost();
        cost = za.calcExchange02(msg, msg2);
        assertTrue(cost == (-1 * costOfOneTwo));
        za.remove(indx2); // remove all shipments for next test
        za.remove(indx);

        response = za.calcExchange10(msg); // add and remove default shipment
        cost = Float.parseFloat(response.getValue(MessageTags.CostTag));
        assertTrue(initialOne == cost);

        response = za.calcExchange20(msg, msg2);
        cost = Float.parseFloat(response.getValue(MessageTags.CostTag));
        assertTrue(cost == costOfOneTwo);

        za.insert(indx3, xCoord3, yCoord3, demand3, earTime3, latTime3,
            servTime3);
        response = za.calcExchange11(msg3, msg);
        cost = Float.parseFloat(response.getValue(MessageTags.CostTag));
        assertTrue(cost == (initialOne - initialThree));

        // at this point shipment 3 should still be scheduled
        response = za.calcExchange21(msg, msg2, msg3);
        cost = Float.parseFloat(response.getValue(MessageTags.CostTag));
        assertTrue(cost == (costOfOneTwo - initialThree));

        // need to insert shipment 2
        za.insert(indx2, xCoord2, yCoord2, demand2, earTime2, latTime2,
            servTime2);
        response = za.calcExchange12(msg, msg2, msg3);
        cost = Float.parseFloat(response.getValue(MessageTags.CostTag));
        assertTrue(cost == (initialOne - costOfTwoThree));
        za.remove(indx2);

        // need to insert shipment 4
        za.insert(indx4, xCoord4, yCoord4, demand4, earTime4, latTime4,
            servTime4);
        response = za.calcExchange22(msg, msg2, msg3, msg4);
        cost = Float.parseFloat(response.getValue(MessageTags.CostTag));
        assertTrue(cost == (costOfOneTwo - costOfThreeFour));

        System.out.println("Exiting testCalcExchangeMethods");
    }

    /**
    * Tests each of the 'retrieve total cost variable' methods.
    * Methods should return the value for the appropriate method
    */
    public void testCalcTotalMethods() {
        System.out.println("Entering testCalcTotalMethods");

        float actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalCost(za.getZeus()
                                                                                .getRoot()
                                                                                .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        float value = za.calculateCost();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalExcessTime(za.getZeus()
                                                                                .getRoot()
                                                                                .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calcTotalExcessTime();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalDemand(za.getZeus()
                                                                            .getRoot()
                                                                            .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calculateCapacity();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalDistance(za.getZeus()
                                                                              .getRoot()
                                                                              .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calculateDistTrav();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalOverload(za.getZeus()
                                                                              .getRoot()
                                                                              .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calcTotalOverload();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalTardinessTime(za.getZeus()
                                                                                   .getRoot()
                                                                                   .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calcTotalTardiness();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalWaitTime(za.getZeus()
                                                                              .getRoot()
                                                                              .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calcTotalWaitTime();
        assertTrue(value == actualValue);

        actualValue = (float) ProblemInfo.vNodesLevelCostF.getTotalTravelTime(za.getZeus()
                                                                                .getRoot()
                                                                                .getVRPTW().mainDepots.getFirst().mainTrucks.getFirst().mainVisitNodes);
        value = za.calculateTotalTime();
        assertTrue(value == actualValue);

        System.out.println("Exiting testCalcTotalMethods");
    }

    /**
     * Tests each of the 'exchange shipment' methods.
     * Methods should perform shipment swaps between the shipments passed into
     * the method.
     */
    public void testExchangeMethods() {
        System.out.println("Entering testExchangeMethods");

        Message msg2 = new Message();
        Message msg3 = new Message();
        Message msg4 = new Message();
        Message response = new Message();

        // info for the second shipment
        int indx2 = 2;
        float xCoord2 = 30;
        float yCoord2 = 40;
        float demand2 = 100;
        int earTime2 = 30;
        int latTime2 = 40;
        int servTime2 = 1;

        // info for the third shipment
        int indx3 = 3;
        float xCoord3 = 35;
        float yCoord3 = 45;
        float demand3 = 10;
        int earTime3 = 40;
        int latTime3 = 50;
        int servTime3 = 1;

        // info for the fourth shipment
        int indx4 = 4;
        float xCoord4 = 30;
        float yCoord4 = 45;
        float demand4 = 20;
        int earTime4 = 50;
        int latTime4 = 60;
        int servTime4 = 1;

        msg2.addArgument(MessageTags.IndexTag, "" + indx2); // index of shipment
        msg2.addArgument(MessageTags.XCoordTag, "" + xCoord2); // x-coord of shipment
        msg2.addArgument(MessageTags.YCoordTag, "" + yCoord2); // y-coord of shipment
        msg2.addArgument(MessageTags.DemandTag, "" + demand2);
        msg2.addArgument(MessageTags.EarlyTimeTag, "" + earTime2);
        msg2.addArgument(MessageTags.LateTimeTag, "" + latTime2);
        msg2.addArgument(MessageTags.ServiceTimeTag, "" + servTime2);

        msg3.addArgument(MessageTags.IndexTag, "" + indx3); // index of shipment
        msg3.addArgument(MessageTags.XCoordTag, "" + xCoord3); // x-coord of shipment
        msg3.addArgument(MessageTags.YCoordTag, "" + yCoord3); // y-coord of shipment
        msg3.addArgument(MessageTags.DemandTag, "" + demand3);
        msg3.addArgument(MessageTags.EarlyTimeTag, "" + earTime3);
        msg3.addArgument(MessageTags.LateTimeTag, "" + latTime3);
        msg3.addArgument(MessageTags.ServiceTimeTag, "" + servTime3);

        msg4.addArgument(MessageTags.IndexTag, "" + indx4); // index of shipment
        msg4.addArgument(MessageTags.XCoordTag, "" + xCoord4); // x-coord of shipment
        msg4.addArgument(MessageTags.YCoordTag, "" + yCoord4); // y-coord of shipment
        msg4.addArgument(MessageTags.DemandTag, "" + demand4);
        msg4.addArgument(MessageTags.EarlyTimeTag, "" + earTime4);
        msg4.addArgument(MessageTags.LateTimeTag, "" + latTime4);
        msg4.addArgument(MessageTags.ServiceTimeTag, "" + servTime4);

        // continue testing
        za.exchange01(msg);
        assertTrue(za.calculateCost() == 0);
        assertTrue(za.getSize() == 0);

        za.insert(indx, xCoord, yCoord, demand, earTime, latTime, servTime);
        za.insert(indx2, xCoord2, yCoord2, demand2, earTime2, latTime2,
            servTime2);
        za.exchange02(msg, msg2);
        assertTrue(za.calculateCost() == 0);
        assertTrue(za.getSize() == 0);

        za.exchange10(msg, 0);
        assertTrue(za.getSize() == 3);
        za.remove(indx);

        za.exchange20(msg, 0, msg2, 1);
        assertTrue(za.getSize() == 4);
        za.remove(indx);
        za.remove(indx2);

        za.insert(indx3, xCoord3, yCoord3, demand3, earTime3, latTime3,
            servTime3);
        za.exchange11(msg3, msg, 0);
        assertTrue(za.getSize() == 3);
        za.remove(indx);

        za.insert(indx3, xCoord3, yCoord3, demand3, earTime3, latTime3,
            servTime3);
        za.exchange21(msg, 0, msg2, 1, msg3);
        assertTrue(za.getSize() == 4);
        za.remove(indx);
        za.remove(indx2);

        // need to insert shipment 2 & 3
        za.insert(indx2, xCoord2, yCoord2, demand2, earTime2, latTime2,
            servTime2);
        za.insert(indx3, xCoord3, yCoord3, demand3, earTime3, latTime3,
            servTime3);
        za.exchange12(msg, 0, msg2, msg3);
        assertTrue(za.getSize() == 3);
        za.remove(indx);

        // need to insert shipment 3 & 4
        za.insert(indx3, xCoord3, yCoord3, demand3, earTime3, latTime3,
            servTime3);
        za.insert(indx4, xCoord4, yCoord4, demand4, earTime4, latTime4,
            servTime4);
        za.exchange22(msg, 0, msg2, 1, msg3, msg4);
        assertTrue(za.getSize() == 4);

        System.out.println("Exiting testExchangeMethods");
    }

    /**
     * Tests the find shipment and remove shipment methods.
     * Methods should find the shipment with a given index and then:
     * find) returns the list the shipment belongs
     * remove) removes the shipment from the list, returning the cell scheduled
     * before it.
     */
    public void testFindRemove() {
        System.out.println("Entering testFindRemove");

        VisitNodesLinkedList v = za.find(indx);
        PointCell p = null;
        assertTrue(v != null);
        assertFalse(v.ifVisitListEmpty());
        assertTrue(v.getSize() == 3);
        assertTrue(v.getCurrentCapacity() == demand);
        assertTrue(v.getCurrentDistance() < distance);
        assertTrue(v.gettotalServiceTime() == servTime);
        p = v.getPointCell(indx);
        assertTrue(p != null);
        assertTrue(p.getDemand() == demand);
        assertTrue(p.getEarTime() == earTime);
        assertTrue(p.getLatTime() == latTime);
        assertTrue(p.getServTime() == servTime);
        assertTrue(p.getXCoord() == xCoord);
        assertTrue(p.getYCoord() == yCoord);
        System.out.println("Exiting testFindRemove");
    }

    /**
     * Tests the getAllVertices method.
    * Method converts all scheduled shipments ID's into a single string
    */
    public void testGetAllVertices() {
        System.out.println("Entering testGetAllVertices");
        System.out.println("Exiting testGetAllVertices");
    }

    /**
     * Tests the getDepotX and getDepotY methods.
    * Methods should return the x-coordinate and the y-coordinate of the
    * depot, respectively
    */
    public void testGetDepotCoords() {
        System.out.println("Entering testGetDepotCoords");
        System.out.println("Exiting testGetDepotCoords");
    }

    /**
     * Tests the getIndexPoint method.
    * Method should return the position of shipment in the schedule
    */
    public void testGetIndexPoint() {
        System.out.println("Entering testGetIndexPoint");

        int position = za.getIndexPoint(indx);
        assertTrue(position == 2); // depot == 1, default shipment == 2, depot == 3
        System.out.println("Exiting testGetIndexPoint");
    }

    /**
     * Tests the getSize method.
    * Method should retrieve the size of the schedule
    */
    public void testGetSize() {
        System.out.println("Entering testGetSize");

        int size = za.getSize();
        assertTrue(size == 3); // depot, default shipment, depot
        System.out.println("Exiting testGetSize");
    }

    /**
     * Tests the shipment insertion methods.
    * Methods should insert the shipment into the schedule
    */
    public void testInsertMethods() {
        System.out.println("Entering testInsertMethods");

        PointCell p = null;
        PointCell last = null;
        float diff;
        int indx = 2;

        // insert to calculate cost
        msg = za.insertCostCar(indx, xCoord, yCoord, demand, earTime, latTime,
                servTime);
        diff = (Float.parseFloat(msg.getValue(MessageTags.CostTag)) -
            (float) ProblemInfo.depotLLLevelCostF.getTotalCost(za.getZeus()
                                                                 .getRoot()
                                                                 .getVRPTW().mainDepots));
        assertTrue((diff > -0.001) && (diff < 0.001));
        assertEquals(Integer.parseInt(msg.getValue(MessageTags.AfterIndexTag)),
            0);

        // check if shipment is removed
        tempDepot = za.getZeus().getRoot().getVRPTW().mainDepots.getFirst();

        // find the first truck in depot
        tempTruck = tempDepot.getMainTrucks().first;

        assertTrue(tempTruck.mainVisitNodes.getSize() == 3); // depot, default shipment, depot

        // find the last node (depot) in truck
        p = tempTruck.mainVisitNodes.first().next.next;
        last = tempTruck.mainVisitNodes.last();
        assertFalse(p.equals(last));

        // insert infeasible shipment
        msg = za.insertCostCar(indx, xCoord, yCoord, Float.MAX_VALUE, earTime,
                latTime, servTime);
        diff = (Float.parseFloat(msg.getValue(MessageTags.CostTag)) -
            ProblemInfo.MAX_COST);
        assertTrue((diff > -0.001) && (diff < 0.001));
        assertEquals(Integer.parseInt(msg.getValue(MessageTags.AfterIndexTag)),
            -1);

        // insert to schedule
        msg = za.insert(indx, xCoord, yCoord, demand, earTime, latTime, servTime);
        assertEquals(MessageTags.ConfirmTag, msg.getMessageType());

        // check for shipment
        tempDepot = za.getZeus().getRoot().getVRPTW().mainDepots.getFirst();

        // find the first truck in depot
        tempTruck = tempDepot.getMainTrucks().first;

        assertTrue(tempTruck.mainVisitNodes.getSize() == 4);

        // find the inserted shipment in truck (should be right after depot)
        p = tempTruck.mainVisitNodes.first().next;
        last = tempTruck.mainVisitNodes.last();
        assertTrue(p != null);
        assertTrue(p.equals(last.prev));
        assertTrue(p.getCellIndex() == indx);
        assertTrue(p.getDemand() == demand);
        assertTrue(p.getEarTime() == earTime);
        assertTrue(p.getLatTime() == latTime);
        assertTrue(p.getServTime() == servTime);
        assertTrue(p.getXCoord() == xCoord);
        assertTrue(p.getYCoord() == yCoord);

        // insert infeasible shipment
        msg = za.insert(indx, xCoord, yCoord, Float.MAX_VALUE, earTime,
                latTime, servTime);
        assertEquals(MessageTags.RefuseTag, msg.getMessageType());
        System.out.println("Exiting testInsertMethods");
    }

    /**
     * Tests the isEmpty method.
    * Method should return true if the schedule is empty, false otherwise
    */
    public void testIsEmpty() {
        System.out.println("Entering testIsEmpty");
        assertFalse(za.isEmpty());
        za.remove(indx);
        assertTrue(za.isEmpty());
        System.out.println("Exiting testIsEmpty");
    }

    /**
     * Tests the opts method.
    * Method should run the full gamot of optimizations within a
    * Carrier's schedule
    */
    public void testOpts() {
        System.out.println("Entering testOpts");
        System.out.println("Exiting testOpts");
    }

    /**
     * Tests the pointString method.
    * Method should convert the information about a shipment into a
    * Message type
    */
    public void testPointString() {
        System.out.println("Entering testPointString");
        System.out.println("Exiting testPointString");
    }

    /**
     * Tests the setProblemConstraints method.
    * Method should set the constraint value for the problem as defined
    * within the problem input file
    */
    public void testSetProblemConstraints() {
        System.out.println("Entering testSetProblemConstraints");

        // only one instance of a depot created
        Depot depot = za.getZeus().getRoot().getVRPTW().mainDepots.getFirst();

        // Global constants must match
        assertEquals(ProblemInfo.noOfDepots, noOfDepots);
        assertEquals(ProblemInfo.noOfShips, noOfCust);
        assertEquals(ProblemInfo.fileName, fileName);
        assertEquals(ProblemInfo.maxCapacity, capacity);
        assertEquals(ProblemInfo.maxDistance, distance);
        assertTrue(ProblemInfo.depotX == depotX);
        assertTrue(ProblemInfo.depotY == depotY);

        // this instance of depot should be the only one in the list
        assertTrue(depot.next == null);
        assertTrue(depot.getDepotNo() == indx); // index must match
        assertTrue(depot.getX() == depotX); // coordinates must match
        assertTrue(depot.getY() == depotY);
        System.out.println("Exiting testSetProblemConstraints");
    }

    /**
    * opposite of setup. Executed once for each of the tests
    */
    protected void tearDown() {
        System.out.println("Entering tearDown");
        System.out.println("Exiting tearDown");
    }
}
