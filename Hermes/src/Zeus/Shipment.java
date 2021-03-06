package Zeus;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  An instance of a Shipment class is used to maintain information on a
 * shipment that is avaialble for scheduling. The Shipment class is used by the ShipmentLinkedList
 * class to maintain information on all the shipments that are avaialble for the problem.</p>
 *
 * <p>As there are a large number of problems, not all variables defined in the class are used
 *  for all the problems. The variables used are dependent on the problem being solveed. </p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:<p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class Shipment implements java.io.Serializable {
    //Most VRP's have to have the following data
    public float vertexX; //vertex X and vertex Y
    public float vertexY;
    public int shipNo; //unique number for the shipment
    public float duration; //maximum travel time
    public float demand; //maximum demand

    //MDVRP, PTSP, PVRP, VRP and VRPTW
    int depotNo; // depot number servicing this shipment, -1 if unassigned
    int truckNo; // truck number servicing this shipment, -1 if unassigned
    int frequency; //frequency of the trip
    int noComb; //number of combinations
    float earliestTime; // earliest time to service this shipment
    float latestTime; // latest time to service this shipment
    float servTime; // time to service this shipment
    public int[] visitComb; //the visit combination for the depots
    public int[][] currentComb; //the binary visit combinations
    public boolean assigned; //false means not assigned
    public Shipment next; //next shipment in the linked list
    public Shipment prev; //previous shipment in the linked list

    //these variables are for Hermes distributed routing system
    public float lowestCost = Float.MAX_VALUE;
    public String IP;
    public int port;
    public int numBids = 0;

    /**
    * <p>Create an instance of the shipment with the required information.
    * The default constructor is not used at the current time.</p>
    *
    */
    public Shipment() {
    }

    /**
    * <p>Create an instance of the shipment with the required information.
    * Depending on the problem, a new Shipment constructor will have to be
    * defined for each problem with the relevant parameters.</p>
    * @param no unique number for the shipment
    * @param x  x coordinate of the shipment
    * @param y  y coordinate of the shipment
    * @param d  duration
    * @param q  demand or weight
    * @param e  freqquency
    * @param comb number of combinations
    * @param vComb visit combinations
    * @param cuComb current combinations
    */
    public Shipment(int no, float x, float y, int d, int q, int e, int comb,
        int[] vComb, int[][] cuComb) {
        int i;

        shipNo = no;
        vertexX = x;
        vertexY = y;
        duration = d;
        demand = q;
        frequency = e;
        noComb = comb;
        visitComb = vComb;
        currentComb = cuComb;
        assigned = false;
        next = null;
        truckNo = -1;
        depotNo = -1;

        //the combinations to be created should not exceed the maximum allowable
        //combination
        for (i = 0; i < noComb; i++) {
            visitComb[i] = vComb[i];
        }
    }

    /**
    * <p>Create an instance of the shipment with the required information.</p>
    * this method added 8/30/03 by Mike McNamara
    * @param no unique number for the shipment
    * @param x  x coordinate of the shipment
    * @param y  y coordinate of the shipment
    * @param q  demand or weight
    */
    public Shipment(int no, float x, float y, int q) {
        shipNo = no;
        vertexX = x;
        vertexY = y;
        demand = q;
        assigned = false;
        next = null;
        truckNo = -1;
        depotNo = -1;
    }

    /**
    * <p>Create an instance of the shipment with the required information for the VRPTW problem</p>
    * this method added 9/12/03 by Sunil Gurung
    * @param x  x coordinate of the shipment
    * @param y  y coordinate of the shipment
    * @param no  unique number for the shipment
    * @param q  demand for the shipment
    * @param eTime  earliest time to service this shipment
    * @param lTime  latest time to service this shipment
    * @param sTime  time to service this shipment
    */
    public Shipment(float x, float y, float q, int no, float eTime,
        float lTime, float sTime) {
        shipNo = no;
        vertexX = x;
        vertexY = y;
        demand = q;
        earliestTime = eTime;
        latestTime = lTime;
        servTime = sTime;
        assigned = false;
        next = null;
        truckNo = -1;
        depotNo = -1;
    }

    /**
    * <p>Create an instance of the shipment with the required information
    * for the VRP problem and VRPTW problem depot coordinate information.</p>
    * this method added 8/30/03 by Mike McNamara
    * @param no no is the key used to find the depot information in the list
    * @param x  x coordinate of the shipment
    * @param y  y coordinate of the shipment
    */
    public Shipment(int no, float x, float y) {
        shipNo = no;
        vertexX = x;
        vertexY = y;
        assigned = false;
        next = null;
        truckNo = -1;
        depotNo = -1;
    }

    /**
    * <p>Get the x coordinate of the shipment.</p>
    * @return x coordinate of the shipment.
    */
    public float getX() {
        return vertexX;
    }

    /**
    * <p>Get the y coordinate of the shipment.</p>
    * @return y coordinate of the shipment.
    */
    public float getY() {
        return vertexY;
    }

    /**
    * <p>Get the earliest time of the shipment.</p>
    * this method added 9/12/03 by Sunil Gurung
    * @return the earlierst time of the shipment.
    */
    public float getEarliestTime() {
        return earliestTime;
    }

    /**
    * <p>Get the latest time of the shipment.</p>
    * this method added 9/12/03 by Sunil Gurung
    * @return the latest time of the shipment.
    */
    public float getLatestTime() {
        return latestTime;
    }

    /**
    * <p>Get the service time of the shipment.</p>
    * this method added 9/12/03 by Sunil Gurung
    * @return the service time of the shipment.
    */
    public float getServeTime() {
        return servTime;
    }

    /**
    * <p>Get the unique shipment number.</p>
    * @return Unique shipment number.
    */
    public int getShipNo() {
        return shipNo;
    }

    /**
    * <p>Get the duration of the shipment.</p>
    * @return Duration of the shipment.
    */
    public float getDuration() {
        return duration;
    }

    /**
    * <p>Get the demand or weight of the shipment.</p>
    * @return Demand or weight of the shipment.
    */
    public float getDemand() {
        return demand;
    }

    /**
    * <p>Get the frequency of the shipment.</p>
    * @return Frequency of the shipment.
    */
    public int getFrequency() {
        return frequency;
    }

    /**
    * <p>Get the number of combinations for the shipment.</p>
    * @return Number of combinations for the shipment.
    */
    public int getNoComb() {
        return noComb;
    }

    /**
    * <p>Get the visit combinations for the shipment.</p>
    * @return visit of combinations for the shipment.
    */
    public int[] getVisitComb() {
        return visitComb;
    }

    /**
    * <p>Get the current decoded visit combinations for the shipment.</p>
    * @return Current decoded visit combinations for the shipment.
    */
    public int[][] getCurrentComb() {
        return currentComb;
    }

    /**
    * <p>Check if the shipment has been assigned.</p>
    * @return True if the shipment has been assigned, false otherwise.
    */
    public boolean getAssigned() {
        return assigned;
    }

    /**
    * <p>Returns the depot number servicing the shipment
    * -1 if unassigned</p>
    * @return depot number
    */
    public int getDepotNo() {
        return depotNo;
    }

    /**
    * <p>Returns the truck number servicing the shipment
    * -1 if unassigned</p>
    * @return truck number
    */
    public int getTruckNo() {
        return truckNo;
    }

    /**
    * <p>Set the x Coordinate for the shipment</p>
    * @param x X coordinate
    */
    public void setX(float x) {
        vertexX = x;
    }

    /**
    * <p>Set the Y coordinate for the shipment</p>
    * @param y Y coordinate
    */
    public void setY(float y) {
        vertexY = y;
    }

    /**
    * <p>Set the duration for the Shipment</p>
    * @param d duration
    */
    public void setDuration(int d) {
        duration = d;
    }

    /**
    * <p>Set the demand for the shipment</p>
    * @param d demand
    */
    public void setDemand(int d) {
        demand = d;
    }

    /**
    * <p>Sets the frequency for the shipment</p>
    * @param f frequency
    */
    public void setFrequency(int f) {
        frequency = f;
    }

    /**
    * <p>Set the number of combinations for the shipment</p>
    * @param n number of combinations
    */
    public void setNoComb(int n) {
        noComb = n;
    }

    /**
    * <p>Set the shipment as assigned.</p>
    * @param value value to be assigned
    */
    public void setAssigned(boolean value) {
        assigned = value;
    }

    /**
    * <p>Set the current combinations for the shipment.</p>
    * @param combination matrix for the combination
    */
    public void setCurrentComb(int[][] combination) { //set the current visit comb
        currentComb = combination;
    }

    /**
    * <p>Sets the depot number servicing this shipment</p>
    * @param dNo depot number
    */
    public void setDepotNo(int dNo) {
        depotNo = dNo;
    }

    /**
    * <p>Sets truck number servicing this shipment</p>
    * @param tNo truck number
    */
    public void setTruckNo(int tNo) {
        truckNo = tNo;
    }

    /**
    * <p>Display on the console the shipment information.</p>
    */
    public void displayShipment() {
        int i;
        String s;

        System.out.println("Shipment number is " + shipNo);
        System.out.println("  Vertex x is  " + vertexX);
        System.out.println("  Vertex Y is  " + vertexY);
        System.out.println("  Duration is  " + duration);
        System.out.println("  Demand is    " + demand);
        System.out.println("  Frequency is " + frequency);
        System.out.println("  Number of Combinations is " + noComb);

        if (noComb > 0) {
            System.out.println("  The combinations are: ");

            for (i = 0; i < noComb; i++) {
                System.out.println("  " + visitComb[i]);
            }
        }

        if ((noComb > 0) && (currentComb != null)) {
            System.out.println("  Current visit Comb: ");

            for (int h = 0; h < noComb; h++) {
                System.out.print("  ");

                for (int k = 0; k < currentComb[h].length; k++) {
                    s = Integer.toString(currentComb[h][k]);
                    System.out.print(s);
                }

                System.out.println(" ");
            }

            System.out.println("");
        }
    }

    public String toString() {
        return "" + shipNo;
    }
}


//end of Shipment class
