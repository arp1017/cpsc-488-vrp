package Zeus;

import java.io.*;

import java.lang.*;

import java.util.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description:  The PointCell class is used to hold information on the customer or
 * package that is to be delivered by a truck. An instance of the PointCell class has a number
 * of different variables. The variables to be used depends on the problem to be solved. Use only
 * those variables that are required for the problem and leave the others as is (do not delete them).
 * In addition, new variables locations can be defined if required.
 * A truck can consisit of one or more instances of PointCells.
 * The PointCell class is used by the VisitingNodesLinkedList class.</p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:<p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class PointCell implements java.io.Serializable {
    //default values for the cell
    public int index = 0; //shipment index to the ShipLinkedList class
    float xCoord = 0; //x coordinate
    float yCoord = 0; //y coordinate;
    float demand = 0; //demand at this customer;
    int depotNo; //depot number associated with the point
    float maxTravelTime; //maximum travel time
    float maxCapacity; //maximum capacity
    float currentCapacity; //current capacity
    float currentDuration; //current duration or travel time

    //These are for vehicle routing problems with time windows, VRPTW
    float earTime = 0; //earliest time for the beginning of service;
    float latTime = 0; //latest time for the end of service;
    float servTime = 0; //service time for the customer;

    //These are for vehicle routing problems with time windows
    float arrTime = 0; //arrival time at the customer; (recalculated)
    float leaveTime = 0; // time that truck leaves customer (recalculated)
    float lt = 0; //latest time that customer can be serviced; (recalculated)
    float AC = 0; //free capacity of the vehicle on arrival at current customer; (recalculated)
    float DC = 0; //free capacity of the vehicle on departure from current customer; (recalculated)
    float waitTime = 0; //waiting time at the customer;

    /****************************************************************************/
    float slackTime; //The time that the truck can slack at each location so as
                     //to be able to insert customer in between the customer in a route

    //    float minSlackTime; //Minimum slackTime that can be applied for a route. Upadated each
    boolean PickupFlag = false; //pickup/delivery point;
    public PointCell next = null; //pointer to a next node;
    public PointCell prev = null; //pointer to a previous node;

    /**
 * <p>Default constructor for the PointCell class. Not used at the curren time.</p>
 */
    public PointCell() {
    }

    /**
 * <p>Constructor for initializing values</p>
 * @param iIndex      index of the shipment
 * @param depotNumber depot number associated with the PointCell
 * @param lX          x coordinate of the shipment
 * @param lY          y coordinate of the shipment
 * @param lDemand      customer demand
 * @param lDuration    durantion/travel time
 * @param nextCell    next cell
 * @param prevCell    previous cell
 */
    public PointCell(int iIndex, int depotNumber, float lX, float lY,
        float lDemand, float lDuration, PointCell nextCell, PointCell prevCell) {
        index = iIndex; //for the starting depot it is 0, for ending it is -1
        xCoord = lX;
        yCoord = lY;
        maxCapacity = (float) lDemand; //maximum capacity for the depot
        maxTravelTime = (float) lDuration; //maximum travel time for the depot
        currentCapacity = 0;
        currentDuration = 0;
        depotNo = depotNumber; //the actual number of the depot
        next = nextCell;
        prev = prevCell;
    }

    /**
 * <p>Constructor for initializing values for the MDVRP</p>
 * @param iIndex      index of the shipment
 * @param lX          x coordinate of the shipment
 * @param lY          y coordinate of the shipment
 * @param lDemand      customer demand
 */
    public PointCell(int iIndex, float lX, float lY, float lDemand) {
        index = iIndex; //for a shipment, it is the number of the shipment
        xCoord = lX;
        yCoord = lY;
        demand = (float) lDemand; //pickup capacity of the shipment
        next = null;
        prev = null;
    }

    /**
 * <p>Constructor for initializing values for the VRPTW</p>
 * this constructor added by Sunil Gurung 9/12/03
 * @param iIndex      index of the shipment
 * @param lX          x coordinate of the shipment
 * @param lY          y coordinate of the shipment
 * @param lDemand      customer demand
 * @param eTime       earliest Time for the shipment
 * @param lTime       latestTime for the shipment
 * @param sTime       service Time for the shipment
 */
    public PointCell(int iIndex, float lX, float lY, float lDemand, int eTime, //MODIFIED BY OLA
        int lTime, int sTime) {
        index = iIndex; //for a shipment, it is the number of the shipment
        xCoord = lX;
        yCoord = lY;
        demand = (float) lDemand; //pickup capacity of the shipment
        earTime = eTime;
        latTime = lTime;
        servTime = sTime;
        slackTime = 0;

        //        minSlackTime = 0;
        next = null;
        prev = null;
    }

    /**
 * <p>Constructor for initializing values in a depot node cell</p>
 * @param iIndex      index of the shipment
 * @param lX          x coordinate of the shipment
 * @param lY          y coordinate of the shipment
 * @param lDemand     customer demand
 * @param lEar        earliest time
 * @param lLat        latest delivery time
 * @param lServ       service time
 * @param lLt         total latest time
 * @param lAC         total AC value
 * @param lDC         total DC value
 * @param nextCell    next cell
 * @param prevCell    previous cell
 */
    public PointCell(int iIndex, float lX, float lY, float lDemand, float lEar,
        float lLat, float lServ, float lLt, float lAC, float lDC,
        PointCell nextCell, PointCell prevCell) {
        //++number;
        index = iIndex;
        xCoord = lX;
        yCoord = lY;
        demand = lDemand;
        earTime = lEar;
        latTime = lLat;
        servTime = lServ;

        //arrTime = lArr;
        lt = lLt;
        AC = lAC;
        DC = lDC;

        //PickupFlag = bPickupFlag;
        next = nextCell;
        prev = prevCell;

        //System.out.println("next = "+next+"\n");
        // System.out.println("POINTCELL: "+index+","+ xCoord+","+yCoord+","+demand+","+
        //earTime+","+latTime+","+servTime+","+lt+","+AC+","+DC+","+next+","+prev);
    }

    /**
 * <p>Constructor for initializing values for a VRPTW</p>
 * @param iIndex      index of the shipment
 * @param lX          x coordinate of the shipment
 * @param lY          y coordinate of the shipment
 * @param lDemand     customer demand
 * @param lEar        earliest time
 * @param lLat        latest delivery time
 * @param lServ       service time
 * @param lArr        total arrival time
 * @param lLt        total latest time
 * @param lAC         total AC value
 * @param lDC         total DC value
 * @param nextCell    next cell
 * @param prevCell    previous cell
 */
    public PointCell(int iIndex, float lX, float lY, float lDemand, float lEar,
        float lLat, float lServ, float lArr, float lLt, float lAC, float lDC,
        PointCell nextCell, PointCell prevCell) {
        //++number;
        index = iIndex;
        xCoord = lX;
        yCoord = lY;
        demand = lDemand;
        earTime = lEar;
        latTime = lLat;
        servTime = lServ;
        arrTime = lArr;
        lt = lLt;
        AC = lAC;
        DC = lDC;

        //PickupFlag = bPickupFlag;
        next = nextCell;
        prev = prevCell;

        //System.out.println("next = "+next+"\n");
    }

    /**
 * <p>Write out the short form of the cell information into the solOutFile file.</p>
 * @param solOutFile  name of the output file
 * */
    public void writeShortPointCell(PrintWriter solOutFile) {
        solOutFile.print(index + " ");
    }

    /**
 * <p>Write out the detailed form of the cell information into the solOutFile file.</p>
 * Modified 10/3/03 by Sunil Gurung
 * Modifications include added detail corresponding to the VRPTW problem
 * @param solOutFile  name of the output file
 * */
    public void writePointCell(PrintWriter solOutFile) {
        if ((index == 0) || (index == -1)) { //depots
            solOutFile.println("          Cell Index    :           " + index); //truck number
            solOutFile.println("          Depot Number  :           " +
                depotNo); //depot number
            solOutFile.println("          X coordinate  :           " + xCoord); //x coordinate
            solOutFile.println("          Y coordinate  :           " + yCoord); //y coordinate
            solOutFile.println("          Max distance  :           " +
                ProblemInfo.maxDistance); //maximum duration of truck
            solOutFile.println("          Max capacity  :           " +
                ProblemInfo.maxCapacity); //maximum capacity of truck
            solOutFile.println("");
        } else { //not a depot
            solOutFile.println("           "); //truck number
            solOutFile.println("           "); //truck number
            solOutFile.println("          Cell Index    :           " + index); //truck number
            solOutFile.println("          X coordinate  :           " + xCoord); //x coordinate
            solOutFile.println("          Y coordinate  :           " + yCoord); //y coordinate

            //            solOutFile.println("************************************"); //y coordinate
            solOutFile.println("          Earliest Time :           " +
                earTime); //y coordinate

            //            solOutFile.println("************************************"); //y coordinate
            solOutFile.println("          Latest Time   :           " +
                latTime); //Latest time
            solOutFile.println("          Wait Time     :           " +
                waitTime); //Wait time
            solOutFile.println("          Arrival Time  :           " +
                arrTime); //arrival time
            solOutFile.println("          Latest Arrival:           " + lt); //latest possible time to arrive
            solOutFile.println("          Service Time  :           " +
                servTime); //Service time
            solOutFile.println("          Distance to next:         " +
                dist(this.xCoord, this.next.xCoord, this.yCoord,
                    this.next.yCoord)); //Service time
            solOutFile.println("          demand        :           " + demand); //demand of cell
            solOutFile.println("          AC            :           " + AC); // arriving demand of cell
            solOutFile.println("          DC            :           " + DC); // leaving demand of cell
            solOutFile.println("");
        }
    }

    /**
 * <p>Calculate the Euclidean distance between two points</p>
 * Sunil Added 10/2/03
 * @param x1  x coordinate of first point
 * @param y1  y coordinate of first point
 * @param x2  x coordinate of second point
 * @param y2  y coordinate of second point
 * @return float  Euclidean distance between the two points
 *
 * */
    public synchronized float dist(float x1, float x2, float y1, float y2) {
        float d = 0;

        try {
            d = (float) Math.sqrt((double) (((x2 - x1) * (x2 - x1)) +
                    ((y2 - y1) * (y2 - y1))));
        } catch (ArithmeticException e) {
            System.out.println(e);
        }

        return d;
    }

    /**
 * Display the information on an instance of PointCell
 * */
    public void displayPointCell() {
        if ((index == 0) || (index == -1)) { //depots
            System.out.println("          Cell Index    :           " + index); //cell number in list
            System.out.println("          Depot Number  :           " +
                depotNo); //depot number
            System.out.println("          X coordinate  :           " + xCoord); //x coordinatenumber
            System.out.println("          Y coordinate  :           " + yCoord); //y coordinate number
            System.out.println("          Maximum duration:         " +
                maxTravelTime); //maximum duration of truck
            System.out.println("          Maximum capacity:         " +
                maxCapacity); //maximum capacity of truck
            System.out.println("");
        } else //not a depot
         {
            System.out.println("          Cell Index    :           " + index); //cell number
            System.out.println("          X coordinate  :           " + xCoord); //x coordinate
            System.out.println("          Y coordinate  :           " + yCoord); //y coordinate
            System.out.println("          demand:                   " + demand); //demand of truck
            System.out.println("");
        }
    }

    /**
 * <p>Returns pointer to next cell in linked list</p>
 * @return PointCell  pointer to next cell
 */
    public PointCell getNext() {
        return next;
    }

    /**
 * <p>Returns the Cell Index</p>
 * @return int  Cell Index
 */
    public int getCellIndex() {
        return index;
    }

    /**
 * <p>Returns the X coordinate of the point cell</p>
 * @return float  X coordinate
 */
    public float getXCoord() {
        return xCoord;
    }

    /**
 * <p>Returns the Y coordinate of the point cell</p>
 * @return float  Y Coordinate
 */
    public float getYCoord() {
        return yCoord;
    }

    /**
 * <p>Returns the demand of the point cell</p>
 * @return float  demand
 */
    public float getDemand() {
        return demand;
    }

    /**
 * <p>Returns the earliest time to service the point cell</p>
 * @return float  earliest time
 */
    public float getEarTime() {
        return earTime;
    }

    /**
 * <p>Returns the lastest time to service the point cell</p>
 * @return float  latest time to service the
 */
    public float getLatTime() {
        return latTime;
    }

    /**
 * <p>Returns the time to service the point cell</p>
 * @return float  time to service the
 */
    public float getServTime() {
        return servTime;
    }

    /**
 * <p>Returns the slack time for a point cell</p>
 * Added 9/28/03 by Sunil Gurung
 * @return float  slack time
 */
    public float getSlackTime() {
        return slackTime;
    }
}


//end PointCell class
