package Zeus;

import java.io.*; //input-output java package

import java.lang.Math; // math function java package


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  The ShipmentLinkedList class maintains information on all the shipments
 * that are avaiaible for the problem. The ShipmentLinkedList class is a collection of
 * instances form the Shipment class. For a problem, all the shipment information is read
 * in and placed in the ShipmentLinkedList class. The presence of a shipment in the
 * ShipmentLinkedList class indicates that it is routed in the problem. This class should not
 * be used for any other purpose than for maintaining shipment information. That is, routing shipments and
 * deleting it from this class is prohibited. Deleting a shipment form this class indicates that the
 * shipment does not exist. All shipments being routing in the problem have to be present in this class
 * all the time. </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company:<p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class ShipmentLinkedList implements java.io.Serializable {
    //pointers that keep track of the shipment
    public Shipment first; //head of the linked list
    public Shipment last; //tail of the linked list
    int countShipments; //count shipments is all the shipments including the depots
    public int noShipments; //total number of shipments
    public int noDepots; //total number of depots
    int maxCapacity; //maximum capacity
    float maxDuration; //max duration
    int noShipsAssigned; //total number of shipments that have been assigned so far
    public double angle; // angle of customer in relation to the depot
    public double distance; // distance of customer from depot
    public double time; // median time of a customer
    public double cost; // cost for choosing customer

    /**
    * <p>Constructor for the shipment linked list
    * Sets the first and last pointers to numm and sets the number of shipment and
    * the no of shipments assigned to 0.</p>
    */
    public ShipmentLinkedList() {
        //initialize the shipment linked list's first and
        //last pointers
        first = null;
        last = null;
        setNoShipments(0); //initialize the no of shipments to 0
        setNoShipmentsAssigned(0); //total number of shipments assigned set to 0
    }

    /**
    * <p>Get the number of depots</p>
    * @return int number of depots
    */
    public int getShipDepots() {
        return noDepots;
    }

    /**
    * <p>Set the count for the number of shipments that have been assigned</p>
    * @param countShips count of ships
    * @return int  number of assigned shipments
    */
    public int setCountShipments(int countShips) {
        countShipments = countShips;

        return countShipments;
    }

    /**
    * <p>get the count of shipments that have been assigned</p>
    * @return int return the count of shipments assigned
    */
    public int getCountShipments() {
        return countShipments;
    }

    /**
    * <p>Sets the number of depots in the shipment linked list.
    * Used when first reading the shipments in from a file.</p>
    * @param noDeps number of depots
    */
    public void setNoDepots(int noDeps) {
        noDepots = noDeps;
    }

    /**
    * <p>Set the total number of shipments. [Need to check the difference between
    * this and the setCountShipments method]</p>
    * @param noShips number of shipments
    * @return int number of shipments
    */
    public int setNoShipments(int noShips) {
        noShipments = noShips;

        return noShipments;
    }

    /**
    * <p>Set the total number of shipments that have been assigned</p>
    * @param noShipsAssign number of shipments being assigned
    * @return int number of shipments that have been assigned
    */
    public int setNoShipmentsAssigned(int noShipsAssign) {
        noShipsAssigned = noShipsAssign;

        return noShipsAssigned;
    }

    /**
    * <p>Set the shipment as being assigned</p>
    * @param tempShip the shipment being set to assigned
    * @return int return the number of shipments that have been assigned
    */
    public int setAssigned(Shipment tempShip) {
        //set the assigned flag of the shipment
        tempShip.setAssigned(true);

        //increment the number of shipments assigned
        setNoShipmentsAssigned(getNoShipsAssigned() + 1);

        return noShipsAssigned;
    }

    /**
    * <p>Sets the Max Capacity for the shipment Linked list</p>
    * @param maxQ max capacity
    */
    public void setMaxCapacity(int maxQ) {
        maxCapacity = maxQ;
    }

    /**
    * <p>Sets the max duration for the shipment linked list</p>
    * @param maxD max duration
    */
    public void setMaxDuration(float maxD) {
        maxDuration = maxD;
    }

    /**
    * <p>Get the number of shipments</p>
    * @return int number of shipments
    */
    public int getNoShipments() {
        return noShipments;
    }

    /**
    * <p>Get the total number of shipments that have been assigned</p>
    * @return int total number of shipments that have been assigned
    */
    public int getNoShipsAssigned() {
        return noShipsAssigned;
    }

    /**
    * <p>Returns the first shipment in the linked list</p>
    * @return first shipment
    */
    public Shipment getFirst() {
        return first;
    }

    /**
    * <p>Returns the last shipment in the linked list</p>
    * @return last shipment
    */
    public Shipment getLast() {
        return last;
    }

    /**
    * <p>Check if all the shipments have been assigned. The countShipments variable has
    * the count of all the shipments that have been assigned. If the difference between
    * countShipments and noShipsAssigned is 0, returns true else false</p>
    * @return boolean true if the count matches, else false
    */
    public boolean ifAllShipmentsAssigned() {
        if ((countShipments - noShipsAssigned) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * <p>Insert the shipment at the end of the linked list. A new instance of the
    * shipment is created, using the parameter values, and is inserted at the end
    * of the linked list.
    * Depending on the problem, a new Shipment constructor will have to be
    * defined for each problem with the relevant parameters.</p>
    * @param num number of shipments
    * @param x  x coordinate of the shipment
    * @param y  y coordinate of the shipment
    * @param d  duration
    * @param q  demand or weight
    * @param e  freqquency
    * @param comb number of combinations
    * @param vComb visit combinations
    * @param cuComb current combinations
    */
    public void insertShipment(int num, float x, float y, int d, int q, int e,
        int comb, int[] vComb, int[][] cuComb) {
        if (vComb.length <= ZeusConstants.MaxCombinations) {
            //create an instance of the Shipment
            Shipment thisShip = new Shipment(num, x, y, d, q, e, comb, vComb,
                    cuComb);

            //add the instance to the linked list - in this case it iis added at the end of the list
            //the total number of shipments is incremented in the insert
            insertLast(thisShip);
        } else {
            System.out.println(
                "ShipmentLinkedList: Maximum number of combinations exceeded");
        }
    }

    /**
    * <p>Insert the shipment at the end of the linked list. A new instance of the
    * shipment is created, using the parameter values, and is inserted at the end
    * of the linked list.</p>
    * this method added 8/29/03 by Mike McNamara
    * @param num number of shipments
    * @param x  x coordinate of the shipment
    * @param y  y coordinate of the shipment
    * @param q  demand or weight
    */
    public void insertShipment(int num, float x, float y, int q) {
        //create an instance of the Shipment
        Shipment thisShip = new Shipment(num, x, y, q);

        //add the instance to the linked list - in this case it is added at the end of the list
        //the total number of shipments is incremented in the insert
        insertLast(thisShip);
    }

    /**
    * <p>Insert the shipment at the end of the linked list. A new instance of the
    * shipment is created, using the parameter values, and is inserted at the end
    * of the linked list.</p>
    * @param x  x coordinate of shipment
    * @param y  y coordinate of shipment
    * @param q  demand of shipment
    * @param num  unique identification of shipment
    * @param eTime  earliest time to service this shipment
    * @param lTime  latest time to service this shipment
    * @param sTime  time to service this shipment
    */
    public void insertShipment(float x, float y, int q, int num, int eTime,
        int lTime, int sTime) {
        //create an instance of the Shipment
        Shipment thisShip = new Shipment(x, y, q, num, eTime, lTime, sTime);

        //add the instance to the linked list - in this case it is added at the end of the list
        //the total number of shipments is incremented in the insert
        insertLast(thisShip);
    }

    /**
    * <p>Insert the depot coordinates at the end of the linked list to be read
    * by the DepotLinkedList class. A new instance of the
    * shipment is created, using the parameter values, and is inserted at the end
    * of the linked list.</p>
    * this method added 8/29/03 by Mike McNamara
    * @param num num is the key used to find the depot information in the list
    * @param x  x coordinate of the depot
    * @param y  y coordinate of the depot
    */
    public void insertDepotPosition(int num, float x, float y) {
        //create an instance of the Shipment
        Shipment thisShip = new Shipment(num, x, y);

        //add the instance to the linked list - in this case it is added at the end of the list
        //the total number of shipments is incremented in the insert
        insertLast(thisShip);
    }

    /**
    * <p>Check if if the linked list is empty</p>
    * @return boolean true if the list is empty, false otherwise
    */
    public boolean isEmpty() {
        return (first == null);
    }

    /**
    * <p>Insert the  shipment into the first location of the linked list</p>
    * @return boolean true if the list is empty, false otherwise
    * @param thisShip shipment to be inserted
    */
    public Shipment insertFirst(Shipment thisShip) {
        boolean isDiagnostic = false;
        Shipment theShip = thisShip; //copy attributes of current shipment

        if (isEmpty()) { //if empty list
            last = theShip; //theShip-> last
        } else {
            first.prev = theShip; //theShip <- old first
        }

        thisShip.next = first; //theShip -> old first
        first = theShip; //first -> theShip
        countShipments++; //increment number of shipments

        //Diagnostic
        if (isDiagnostic) {
            System.out.println("inserted " + theShip.getShipNo());
        }

        return first; //return the pointer to the added node
    }

    /**
    * <p>Insert a shipment into the first location of the linked list</p>
    * @return boolean true if the list is empty, false otherwise
    * @param thisShip shipment to be inserted
    */
    public Shipment insertLast(Shipment thisShip) {
        boolean isDiagnostic = false;
        Shipment theShip = thisShip;

        if (isEmpty()) {
            first = theShip; //first -> theShip
        } else {
            last.next = theShip; //old last -> theShip
            theShip.prev = last; //old last <- theShip
        }

        last = theShip; //theShip <- last
        countShipments++; //increment number of shipments

        //Diagnostic
        if (isDiagnostic) {
            System.out.println("inserted " + theShip.getShipNo());
        }

        return last; //return the pointer to the added node
    }

    /**
    * <p>Delete the first shipment from the linked list</p>
    * @return Shipment Instance node of Shipment deleted
    */
    public Shipment deleteFirst() {
        boolean isDiagnostic = false;
        Shipment temp = first;

        //check for empty list
        if (isEmpty()) {
            return null; //return null, if empty
        }

        if (first.next == null) { //if only one item in linked list
            last = null; //null <- last
        } else {
            first.next.prev = null; //null <- old next
        }

        first = first.next; //first -> old next
        temp.next = null; //ground pointer
        countShipments--; //decrement number of shipments

        if (isDiagnostic) {
            System.out.println("deleted " + temp.getShipNo());
        }

        return temp; //return deleted node
    }

    /**
    * <p>Delete the last shipment from the linked list</p>
    * @return Shipment Instance node of Shipment deleted
    */
    public Shipment deleteLast() {
        boolean isDiagnostic = false;
        Shipment temp = last;

        //check for empty list
        if (isEmpty()) {
            return null; //if empty, return null
        }

        if (first.next == null) { //if only one item in linked list
            first = null; //null <- first
        } else {
            last.prev.next = null; //old prev -> null
        }

        last = last.prev; //old prev -> last
        temp.next = null; //ground pointer
        countShipments--; //decrement number of shipments

        if (isDiagnostic) {
            System.out.println("deleted " + temp.getShipNo());
        }

        return temp; //return deleted node
    }

    /**
    * <p>find a shipment in the linked list with shipment number as key</p>
    * @param key is the unique address of the shipment
    * @return Shipment Pointer to the located shipment, null if shipment was not found
    */
    public Shipment find(int key) {
        boolean isDiagnostic = false;
        Shipment current = first;

        while (current.next != null) {
            if (current.shipNo == key) {
                break;
            }

            current = current.next;
        }

        if (isDiagnostic) {
            System.out.println("found " + current.getShipNo());
        }

        return current;
    }

    /**
    * <p>Finds shipment based on index from the first element in the linked list</p>
    * @param index shipment to find
    * @return pointer to shipment found
    */
    public Shipment findByTraversal(int index) {
        Shipment temp = this.getFirst();

        for (int i = 0; i < index; i++) {
            if (temp.next == null) {
                return null;
            } else {
                temp = temp.next;
            }
        }

        return temp;
    }

    /**
    * <p>Delete a shipment in the linked list with shipment number as key</p>
    * @param key is the unique address of the shipment
    * @return Shipment Pointer to the deleted shipment, null if shipment was not found
    */
    public Shipment deleteKey(int key) {
        boolean isDiagnostic = false;
        Shipment current = first; //set pointer to the beginning

        if (isEmpty()) {
            return null; //return null if empty
        }

        while (current.shipNo != key) //while not found
         {
            if (current.next == null) //if next shipment is null
             {
                if (isDiagnostic) {
                    System.out.println("ShiplinkedList: Key not deleted " +
                        key);
                }

                return null; //can't find shipment
            } else {
                current = current.next; //go to next node
            }
        }

        //end while
        if (current == first) { //key in first node
            first = current.next; //first -> old next
        } else { //not first
            current.prev.next = current.next; //old prev <- old next
        }

        if (current == last) { //last item on linked list
            last = current.prev; //old prev <- last
        } else { //not last
            current.next.prev = current.prev; //old prev <- old next
        }

        current.next = null; //ground current
        countShipments--; //decrement number of shipments

        if (isDiagnostic) {
            System.out.println("ShipLinkedList: deleted " +
                current.getShipNo());
        }

        return current; //return current
    }

    //end deleteKey

    /**
    * <p>Insert a shipment after the shipment node with Key value</p>
    * @param key is the unique address of the shipment
    * @param thisShip shipment to be inserted
    * @return thisShip Pointer to the inserted shipment, null if shipment was not found
    */
    public boolean insertAfter(int key, Shipment thisShip) {
        boolean isDiagnostic = false;
        Shipment current = first;

        while (current.shipNo != key) //until match is found
         {
            current = current.next; //move to next node

            if (current == null) { //no more nodes

                return false; //return false;
            }
        }

        //key was found
        Shipment newShip = thisShip;

        if (current == last) //if last link
         {
            newShip.next = null; //newShip -> null
            last = newShip; //newShip -> last
        } else //not last link
         {
            newShip.next = current.next; //newShip -> old next
            current.next.prev = newShip;
        }

        newShip.prev = current; //old current <- newShip
        current.next = newShip; //old current -> newShip

        countShipments++; //increment number of shipments

        if (isDiagnostic) {
            System.out.println("inserted " + thisShip.getShipNo() + " after" +
                key);
        }

        return true; //insertion successful
    }

    /**
    * <p>Display the linked list of shipments from first to last</p>
    */
    public void displayForwardList() {
        System.out.print("List (first to last): ");

        Shipment current = first;

        while (current != null) {
            current.displayShipment();
            current = current.next;
        }

        System.out.println("");
    }

    /**
    * <p>Display the linked list of shipments from last to first.</p>
    */
    public void displayBackwardList() {
        System.out.print("List (last to first): ");

        Shipment current = last;

        while (current != null) {
            current.displayShipment();
            current = current.prev;
        }

        System.out.println("");
    }

    /**
    * <p>Display the linked list of shipments from first to last
    * Breaks the shipments into 10 per line</p>
    */
    public void displayForwardKeyList() {
        System.out.println("List (first to last): ");

        Shipment current = first;
        int j = 0; //count no. of values printed per line

        while (current != null) {
            System.out.print(current.shipNo);
            System.out.print(" ");
            current = current.next;
            j++;

            if (j > 10) {
                System.out.println();
                j = 0;
            }
        }

        System.out.println("");
    }

    /**
    * <p>Display the linked list of shipments from last to first
    * Breaks the shipments into 10 per line</p>
    */
    public void displayBackwardKeyList() {
        System.out.println("List (last to first): ");

        Shipment current = last;
        int j = 0; //count no. of values printed per line

        while (current != null) {
            System.out.print(current.shipNo);
            System.out.print(" ");
            current = current.prev;
            j++;

            if (j > 10) {
                System.out.println();
                j = 0;
            }
        }

        System.out.println();
        System.out.println();
    }

    /**
    * <p>Display the unique shipment id's of the shipments in the linked list</p>
    */
    public void printShipNos() {
        int noCount = 0;
        Shipment current = first;

        while (current != null) {
            System.out.print(current.getShipNo() + " ");
            noCount++;

            if ((noCount % 10) == 0) {
                System.out.println("");
            }

            current = current.next;
        }

        System.out.println();
        System.out.println();
    }

    /**
    * <p>Retrieves the visit combination for a
    * Period Traveling Salesman Problem (PTSP)</p>
    * @param combList  combination list
    * @param combIndex index of combination
    * @param t         total number of combinations
    * @return int[]
    */
    public int[] getCurrentComb(int[] combList, int combIndex, int t) {
        //int combIndex;                   //index of chosen visit comb.
        int combCode;
        int[] decodedComb = new int[t];

        //combIndex = (int)((java.lang.Math.random()*a) + 1);
        //System.out.println("combIndex: " + combIndex);
        combCode = combList[combIndex];
        decodedComb = decodeTheComb(combCode, t);

        return decodedComb;
    }

    /**
    * <p>Decodes the visit combination from an int to array of 1's and 0's rep days
    * equivalent to bit pattern of code ex: 15 = 1111</p>
    * @param code   code in integer format
    * @param t      t length of the code
    * @return int[] decrypted code in an array
    */
    public int[] decodeTheComb(int code, int t) {
        int num = code;
        int quot = -1; //initialize quotient to enter while
        int rem; //remainder
                 //replace num with code

        int[] tempArray = new int[t];
        int[] decode = new int[t];
        int count = 0;
        int numDays = t; //#days in planning horizon

        while (quot != 0) {
            quot = num / 2;
            rem = num % 2;

            //System.out.println("quot "+quot);
            //System.out.println("rem "+rem);
            tempArray[count] = rem;
            count++;
            num = quot;
        }

        //reverse temp and store in decode
        for (int b = 0; b < t; b++)
            decode[b] = tempArray[--numDays];

        //print array
        //for(int b=0; b<t; b++)
        //System.out.print(b + " : " +decode[b]+"  ");
        //System.out.println("");
        return decode;
    }

    /**
    * <p>Insert the combination into the last shipment that was
    * inserted into the linked list</p>
    * @param combination The combination matrix
    * @return  the set combination
    */
    public int[][] setCurrentComb(int[][] combination) {
        //insert the combination into the last shipment that was
        //inserted into the linked list
        last.currentComb = combination;

        return last.currentComb;
    }

    /**
    * get maximum capacity of each truck for problem file
    * @return float  maximum distance of each truck for problem file
    */
    public float getMaxCap() {
        return maxCapacity;
    }

    /**
    * get maximum distance of each truck for problem file
    * @return flaot  maximum distance of each truck for problem file
    */
    public float getMaxDist() {
        return maxDuration;
    }

    /**
    * <p>Find the shipment that is closest to the given x,y coordinate
    * using type.  Type allows one to compute closeness with respect to
    * different criteria</p>
    * @param x x coordinate of the shipment
    * @param y y coordinate of the shipment
    * @param type type of closest method to be applied
    * @return Shipment closest shipment
    */
    public Shipment getClosestShipmentMDVRP(double x, double y, int type) {
        boolean isDiagnostic = false;
        Shipment temp = first; //point to the first shipment
        Shipment foundShipment = null; //the shipment found with the criteria
        double angle;
        double foundAngle = 360; //initial value
        double distance;
        double foundDistance = 200; //initial distance

        while (temp != null) {
            if (isDiagnostic) {
                System.out.print("Shipment " + temp.shipNo + " ");

                if (((temp.vertexX - x) >= 0) && ((temp.vertexY - y) >= 0)) {
                    System.out.print("Quadrant I ");
                } else if (((temp.vertexX - x) <= 0) &&
                        ((temp.vertexY - y) >= 0)) {
                    System.out.print("Quadrant II ");
                } else if (((temp.vertexX) <= (0 - x)) &&
                        ((temp.vertexY - y) <= 0)) {
                    System.out.print("Quadrant III ");
                } else if (((temp.vertexX - x) >= 0) &&
                        ((temp.vertexY - y) <= 0)) {
                    System.out.print("Quadrant VI ");
                } else {
                    System.out.print("No Quadrant");
                }
            }

            //if the shipment is assigned, skip it
            if (temp.getAssigned()) {
                if (isDiagnostic) {
                    System.out.println("has been assigned");
                }

                temp = temp.next;

                continue;
            }

            //if not assigned, check it
            switch (type) {
            //find the customer closest to the depot in Euclidean distance
            case 1:
                distance = calcDist(x, temp.vertexX, y, temp.vertexY);

                if (isDiagnostic) {
                    System.out.println("  " + distance);
                }

                //check if this shipment should be tracked
                if (foundShipment == null) //this is the first shipment being checked
                 {
                    foundShipment = temp;
                    foundDistance = distance;
                } else {
                    if (distance < foundDistance) //found an angle that is less
                     {
                        foundShipment = temp;
                        foundDistance = distance;
                    }
                }

                break;

            //find the customer with the lowest polar coordinate angle
            case 2:
                angle = calcPolarAngle(x, y, temp.vertexX, temp.vertexY);

                if (isDiagnostic) {
                    System.out.println("  " + angle);
                }

                //check if this shipment should be tracked
                if (foundShipment == null) //this is the first shipment being checked
                 {
                    foundShipment = temp;
                    foundAngle = angle;
                } else {
                    if (angle < foundAngle) //found an angle that is less
                     {
                        foundShipment = temp;
                        foundAngle = angle;
                    }
                }

                break;
            }

            temp = temp.next;
        }

        return foundShipment; //stub
    }

    /**
    * <p>Find the shipment that is closest to the given x,y coordinate
    * using type.  Type allows one to compute closeness with respect to
    * different criteria</p>
    * This method added 10/05/03 by Mike McNamara and Sunil Gurung
    * @param x x coordinate of the shipment
    * @param y y coordinate of the shipment
    * @param type type of closest method to be applied
    * @return Shipment closest shipment
    */
    public Shipment getClosestShipmentVRPTW(double x, double y, int type) {
        boolean isDiagnostic = false;
        Shipment temp = first; //point to the first shipment
        Shipment foundShipment = null; //the shipment found with the criteria
        double foundAngle = 360; //initial value
        double foundDistance = 200; //initial distance
        double foundTime = 9999; // initial median time
        double foundCost = 9999; // initial cost

        //while (temp != null) {
        for (int i = 0; i <= (this.getCountShipments() - 1); i++) {
            if (isDiagnostic) {
                System.out.print("Shipment " + temp.shipNo + " ");

                if (((temp.vertexX - x) >= 0) && ((temp.vertexY - y) >= 0)) {
                    System.out.print("Quadrant I ");
                } else if (((temp.vertexX - x) <= 0) &&
                        ((temp.vertexY - y) >= 0)) {
                    System.out.print("Quadrant II ");
                } else if (((temp.vertexX) <= (0 - x)) &&
                        ((temp.vertexY - y) <= 0)) {
                    System.out.print("Quadrant III ");
                } else if (((temp.vertexX - x) >= 0) &&
                        ((temp.vertexY - y) <= 0)) {
                    System.out.print("Quadrant VI ");
                } else {
                    System.out.print("No Quadrant");
                }
            }

            //if the shipment is assigned, skip it
            if (temp.getAssigned()) {
                if (isDiagnostic) {
                    System.out.println("has been assigned");
                }

                temp = temp.next;

                continue;
            }

            //if not assigned, check it
            switch (type) {
            //find the customer closest to the depot in Euclidean distance
            case 1:
                distance = calcDist(x, temp.vertexX, y, temp.vertexY);

                if (isDiagnostic) {
                    System.out.println("  " + distance);
                }

                //check if this shipment should be tracked
                if (foundShipment == null) //this is the first shipment being checked
                 {
                    foundShipment = temp;
                    foundDistance = distance;
                } else {
                    if (distance < foundDistance) //found an angle that is less
                     {
                        foundShipment = temp;
                        foundDistance = distance;
                    }
                }

                break;

            //find the customer with the lowest polar coordinate angle
            case 2:
                angle = calcPolarAngle(x, y, temp.vertexX, temp.vertexY);

                if (isDiagnostic) {
                    System.out.println("  " + angle);
                }

                //check if this shipment should be tracked
                if (foundShipment == null) //this is the first shipment being checked
                 {
                    foundShipment = temp;
                    foundAngle = angle;
                } else {
                    if (angle < foundAngle) //found an angle that is less
                     {
                        foundShipment = temp;
                        foundAngle = angle;
                    }
                }

                break;

            case 3:
                distance = calcDist(x, temp.vertexX, y, temp.vertexY);
                angle = calcPolarAngle(x, y, temp.vertexX, temp.vertexY);
                time = calcMedianTime(temp);
                ProblemInfo.shipmentLLLevelCostF.setTotalCost(this);
                cost = ProblemInfo.shipmentLLLevelCostF.getTotalCost(this);

                if (isDiagnostic) {
                    System.out.println("  " + distance);
                }

                //check if this shipment should be tracked
                if (foundShipment == null) //this is the first shipment being checked
                 {
                    foundShipment = temp;
                    foundDistance = distance;
                    foundAngle = angle;
                    foundTime = time;
                    foundCost = cost;
                } else {
                    if (cost < foundCost) {
                        foundShipment = temp;
                        foundDistance = distance;
                        foundAngle = angle;
                        foundTime = time;
                        foundCost = cost;
                    }
                }

                break;

            case 4:
                distance = calcDist(x, temp.vertexX, y, temp.vertexY);
                angle = calcPolarAngle(x, y, temp.vertexX, temp.vertexY);
                time = calcMedianTime(temp);
                ProblemInfo.shipmentLLLevelCostF.setTotalCost(this);
                cost = ProblemInfo.shipmentLLLevelCostF.getTotalCost(this);

                if (isDiagnostic) {
                    System.out.println("  " + distance);
                }

                //check if this shipment should be tracked
                if (foundShipment == null) //this is the first shipment being checked
                 {
                    foundShipment = temp;
                    foundDistance = distance;
                    foundAngle = angle;
                    foundTime = time;
                    foundCost = cost;
                } else {
                    if (cost < foundCost) {
                        foundShipment = temp;
                        foundDistance = distance;
                        foundAngle = angle;
                        foundTime = time;
                        foundCost = cost;
                    }
                }

                break;
            }

            temp = temp.next;
        }

        return foundShipment;
    }

    /**
    * <p>Calculate the polar coordinate angle between two points</p>
    * @param x1 x coordinate of first point
    * @param y1 y coordinate of first point
    * @param x x coordinate of second point
    * @param y y coordinate of second point
    * @return double the polar coordinate angle
    */
    private double calcPolarAngle(double x1, double y1, double x, double y) {
        //find the polar coordinate angle between (x1,y1) and (x,y)
        double radian = 57.29578;
        double slope = 0;
        double xrun;
        double yrise;
        double angle;

        xrun = x - x1;
        yrise = y - y1;

        if (xrun > 0) {
            slope = yrise / xrun;

            if (yrise >= 0) {
                angle = Math.atan(slope) * radian;
            } else {
                angle = 360 + (Math.atan(slope) * radian);
            }
        } else if (xrun == 0) {
            if (yrise >= 0) {
                angle = 90.0;
            } else {
                angle = 270.0;
            }
        } else {
            slope = yrise / xrun;
            angle = 180 + (Math.atan(slope) * radian);
        }

        return angle;
    }

    /**
    * <p>Calculate the eucledian distance between two points</p>
    * @param x1 x coordinate of first point
    * @param y1 y coordinate of first point
    * @param x2 x coordinate of second point
    * @param y2 y coordinate of second point
    * @return double the euclidean distance
    */
    public double calcDist(double x1, double x2, double y1, double y2) {
        double d = 0;

        try {
            d = (double) Math.sqrt((double) (((x2 - x1) * (x2 - x1)) +
                    ((y2 - y1) * (y2 - y1))));
        } catch (ArithmeticException e) {
            System.out.println(e);
        }

        return d;
    }

    /**
    * <p>calculates the median time of the customer</p>
    * This method added 10/05/03 by Mike McNamara and Sunil Gurung
    * @param customer  customer whose median time is being calculated
    * @return double  the median time of the customer
    */
    public double calcMedianTime(Shipment customer) {
        return ((customer.latestTime - customer.earliestTime) / 2.0) +
        customer.earliestTime;
    }
}


//end ShipmentLinkedList
