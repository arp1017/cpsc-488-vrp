package Zeus;

import java.io.*; //input-output java package

import java.util.*; //for string tokenizer


public class VRP {
    int truckCount = 0; //number of trucks
    int customerCount = 0; //number of customers
    int numDepots = 0; // number of depots, will be 1 for VRP problem
    int t = 0; //travel time of trucks
    int D = 0; //maximum distance of vehicle
    int Q = 0; //maximum capacity of vehicle

    /** Customers read in from the data file are loaded into the ShipmentLinkedList
 *  instance named mainShipments. The mainShipments instance holds all of the
 *  shipments that are avaialble for routing. This list should not be used
 *  for manipulating the problem being solved. The list can be updated if a
 *  customer is added, deleted or edited.
 */
    ShipmentLinkedList mainShipments = new ShipmentLinkedList();

    /** The DepotLinkedList with instance mainDepots forms the root node that keeps
 *  track of the solution that has been obtained for the problem. From the mainDepots
 *  one can obtain the list of trucks and for each truck the list of customers for
 *  the problem.
 */
    DepotLinkedList mainDepots = new DepotLinkedList();

    /**
 * Constructor for the VRP problem. Reads in the data from the file.
 * Creates the depot.
 * Allocates the customers to the trucks.
 * @param fileName Name of the file to be used for the problem
 */
    public VRP(String fileName) {
        boolean isDiagnostic = false;
        Shipment tempShip;
        Depot thisDepot;
        int type;
        int depotNo;
        int countAssignLoop;
        boolean status;
        String outputFileName;

        // read in the data from the file given as tokens
        readVRPTokenFileData(fileName);

        /* The customers and depots are read into the mainShipments
   shipment linked list. The createDepots method in the
   mainDepots instance creates a linked list of
   depots using the depot information in the mainShipments linked
   list. The linked list is accessible through the mainDepots instance.
   The VRP problem only has one depot so the linked list will contain
   only one node.
 */
        mainDepots.createDepots(mainShipments);

        /* Get the shipment that is closest to a depot with respect to the
   criteria
   The method for assinging customers to the depots is as follows.
   Iterate throught the customer list inserting the customer to the truck,
   as long as it does not violate the constraints.  If it does, create
   another truck and insert the customer there.  Customers will be inserted
   into the trucks in a position that yields the lowest cost until all
   the customers have been serviced.
 */
        //loop while all shipments have not been assigned to the depot
        countAssignLoop = 0;

        // get depot number to assign customers to, will always be 1 for VRP problem
        thisDepot = mainDepots.getFirst();
        depotNo = thisDepot.getDepotNo();

        // start iteration of list at the beginning
        tempShip = mainShipments.first;

        while ((countAssignLoop < customerCount) && (tempShip != null)) {
            status = mainDepots.insertShipmentVRP(tempShip, depotNo); //add the shipment to the depot

            if ((status != true) && isDiagnostic) {
                System.out.println("VRP: Shipment could not be inserted");
            }

            if (isDiagnostic) {
                System.out.println("The depot  and shipment is      : " +
                    depotNo + " " + tempShip.shipNo);
            }

            /* it is assume that everytime the insert shipment is called, a shipment is
   assigned.  This is to prevent the infinite looping
 */
            countAssignLoop += 1;

            tempShip = tempShip.next; // service the next customer
        }

        //end while
        //At this point all shipments have been assigned
        mainDepots.displayForwardKeyListVRPTW();
        mainDepots.calculateTotalNonEmptyTrucksVRP();
        System.out.println("Total number of trucks:    " +
            mainDepots.getTotalNonEmptyTrucksVRP());
        mainDepots.calculateTotalDemandVRP();
        System.out.println("Total demand for trucks:   " +
            mainDepots.getTotalDemandVRP());
        mainDepots.calculateTotalDistanceVRP();
        System.out.println("Total distance for trucks: " +
            mainDepots.getTotalDistanceVRP());

        //Use the fileprefix of the filename to write out the output
        String filePrefix = fileName.substring(fileName.lastIndexOf("/") + 1);
        filePrefix = filePrefix.substring(0, filePrefix.lastIndexOf("."))
                               .toLowerCase();

        //Write the solution out to the solution file
        outputFileName = filePrefix + "_detailSolution.txt";
        writeVRPDetailSol(outputFileName);

        //Write out the short solution to the solution file
        outputFileName = filePrefix + "_shortSolution.txt";
        writeVRPShortSol(outputFileName);
    }

    /**
 * Read in the data from the datafile and load it into the mainShipments
 * ShipmentLinkedList using the tokenizer.
 * @param VRPfileName String type of the filename consisting of the data
 * @return int Return 1 if all successful, 0 for failure
 */
    public int readVRPTokenFileData(String VRPfileName) {
        /* read in the VRP data from the listed file and load the information
   into the availShipments linked list
*/
        int index = 0;
        int garbage; // to collect garbage values in data file

        //open the requested file
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;

        try {
            fis = new FileInputStream(VRPfileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        } catch (Exception e) {
            System.out.println("File is not present");

            return 0;
        }

        //String to hold the entire line read in
        String readLn;

        //StringTokenizer to obtain data in tokens
        StringTokenizer st;

        /* This reads in the first line that is used to determine the total
   number of customers, max distance trucks travel and max capacity
 */
        try {
            //read in the first line
            readLn = br.readLine();

            //tokenize the first line
            st = new StringTokenizer(readLn);

            while (st.hasMoreTokens()) { //while there are more tokens

                switch (index) {
                case 0:
                    numDepots = Integer.parseInt(st.nextToken());

                    break;

                case 1:
                    customerCount = Integer.parseInt(st.nextToken());

                    break;

                case 2:
                    D = Integer.parseInt(st.nextToken());

                    break;

                case 3:
                    Q = Integer.parseInt(st.nextToken());

                    break;

                case 4:
                    garbage = Integer.parseInt(st.nextToken());

                    break;
                } //end switch

                index += 1;
            }

            //end while
        } catch (Exception e) {
            System.out.println("Line could not be read in");
        }

        //Put the problem information into the ProblemInfo class. This is used
        //to identify the file type and the information that was read in from
        //the input file. When printing the output this information can be
        //used to associate with the date file that was read in.
        ProblemInfo.fileName = VRPfileName; //name of the file being read in
        ProblemInfo.noOfShips = customerCount; //number of shipments

        if (Q == 0) { //if there is no maximum capacity, set it to a very large number
            Q = 999999999;
        }

        if (D == 0) { //if there is no maximum distance, set it to a very large number
            D = 999999999;
        }

        ProblemInfo.maxCapacity = Q; //maximum capacity of a vehicle
        ProblemInfo.maxDistance = D; //maximum distance of a vehicle

        //place the number of depots and number of shipments in the linked list instance
        mainShipments.noShipments = customerCount;
        mainShipments.noDepots = numDepots;
        mainShipments.maxCapacity = Q;
        mainShipments.maxDuration = D;

        // This section will get the customer/depot information.
        float x = 0; //x coordinate
        float y = 0; //y coordinate
        int i = 0; // customer number
        int q = 0; // customer demand
        int runTimes; //total number of lines to read

        // this section will get the depot coordinates
        try {
            readLn = br.readLine();
            st = new StringTokenizer(readLn);
            x = Integer.parseInt(st.nextToken()); // x coordinate
            y = Integer.parseInt(st.nextToken()); // y coordinate

            mainShipments.insertDepotPosition((customerCount + numDepots), x, y);
        } catch (Exception e) {
            System.out.println("Reading the line");
        }

        runTimes = customerCount;

        //This section will get the customers and related information
        try {
            readLn = br.readLine();

            /* The first for loop runtimes dependent upon how many lines are to be read
   The next for loop reads the line into s.  Then the entire string in s
   is processesd until the the entire line is processed and there are no
   more characters are to be processed.
*/

            //runTimes includes the total number of customers
            for (int k = 0; k < runTimes; k++) {
                index = 0;
                st = new StringTokenizer(readLn);

                while (st.hasMoreElements()) {
                    switch (index) {
                    case 0:
                        i = Integer.parseInt(st.nextToken());

                        break;

                    case 1:
                        x = Integer.parseInt(st.nextToken());

                        break;

                    case 2:
                        y = Integer.parseInt(st.nextToken());

                        break;

                    case 3:
                        q = Integer.parseInt(st.nextToken());

                        break;

                    default:
                        break;
                    } //end switch

                    index += 1;
                }

                //end while
                // insert the shipment information in the the linked list
                mainShipments.insertShipment(i, x, y, q);

                //read the next line from the file
                try {
                    readLn = br.readLine();
                } //try
                catch (Exception e) {
                    System.out.println("Reading in the next line");
                }
            }

            //end for - runTimes
        } catch (Exception e) {
            System.out.println("Reading the line");
        }

        //print out the shipment numbers on command line
        //         mainShipments.printShipNos();
        return 1;
    }

    /**
 * Write out the solution to the file - short version
 * @param outFileName A String type of the file into which the output is to be
 *        written
 * This method added 8/30/03 by Mike McNamara
 */
    public void writeVRPShortSol(String outFileName) {
        double maxDistance = 0;
        PrintWriter solOutFile = null;
        Shipment curr = mainShipments.first;
        int j = 0;

        try {
            solOutFile = new PrintWriter(new FileWriter(outFileName));

            //print out the problem information to the file
            if (ProblemInfo.maxDistance >= 999999999) {
                maxDistance = 0;
            } else {
                maxDistance = ProblemInfo.maxDistance;
            }

            solOutFile.println(ProblemInfo.fileName + " " +
                ProblemInfo.noOfShips + " " + numDepots + " " +
                ProblemInfo.maxCapacity + " " + maxDistance);

            //write out the route information
            mainDepots.writeVRPShortDepotsSol(solOutFile);
        } catch (IOException ioe) {
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                System.out.println("Detail solution file not open.");
            }
        }

        //end finally
    }

    // end writeVRPShortSol()

    /**
 * Write out the solution to the file - longer version.
 * @param outFileName A String type of the file into which the output is to be
 *        written
 * This method added 8/30/03 by Mike McNamara
 */
    public void writeVRPDetailSol(String outFileName) {
        PrintWriter solOutFile = null;
        Shipment curr = mainShipments.first;
        int j = 0;

        try {
            solOutFile = new PrintWriter(new FileWriter(outFileName));

            //print out the problem information to the file
            solOutFile.println("VRP File         : " + ProblemInfo.fileName);
            solOutFile.println("No. of Shipments : " + ProblemInfo.noOfShips);
            solOutFile.println("No. of Depots    : " + numDepots);
            solOutFile.println("Maximum capcity  : " + ProblemInfo.maxCapacity);
            solOutFile.println("Maximum Distance : " + ProblemInfo.maxDistance);

            if (ProblemInfo.tabuSearch != null) {
                solOutFile.println(ProblemInfo.tabuSearch.getStatistics());
            }

            //write out the route information
            mainDepots.writeVRPDetailDepotsSol(solOutFile);
        } catch (IOException ioe) {
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutFile != null) {
                solOutFile.close();
            } else {
                System.out.println("Solution file not open.");
            }
        }

        //end finally
    }

    // end writeVRPDetailSol()
}
