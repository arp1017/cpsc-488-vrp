package Zeus;

import java.io.*; //input-output java package


/**
 * Title:        Zeus - A Unified Object Oriented Model for VRP's
 * Description:
 * Copyright:    Copyright (c) 2001-2003
 * Company:
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class PTSP {
    //This program loads in MDVRP, PTSP and PVRP files based on the flags
    //that are set up on the files.
    //customers read in from a file or database that are available
    ShipmentLinkedList availShipments = new ShipmentLinkedList();
    DepotLinkedList availDepots = new DepotLinkedList();

    //constructor for the class
    public PTSP(String fileName) {
        //read in the MDVRP data
        readPTSPDataFromFile(fileName);

        //run a couple of checks on the doubly-linked list
        availShipments.deleteFirst();
        availShipments.displayBackwardList();
        availShipments.deleteLast();
        availShipments.displayForwardList();
    }

    //read in the data from the requested file
    public int readPTSPDataFromFile(String PTSPFileName) {
        // read in the PTSP data from the listed file and load the information
        // into the availShipments linked list
        //type = 0 (MDVRP)
        //     = 1 (PTSP)
        //     = 2 (PVRP)
        char ch;
        String temp = "";
        int index = 0; //maximum load of vehicle
        int j = 0; //maximum load of vehicle
        int type = 0; //maximum load of vehicle
        int m = 0; //maximum load of vehicle
        int n = 0; //maximum load of vehicle
        int t = 0; //maximum load of vehicle
        int D = 0; //maximum load of vehicle
        int Q = 0; //maximum load of vehicle
        int p = 3; //Np neighborhood size
        int depotIndex;

        //Open the requested file
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;

        try {
            fis = new FileInputStream(PTSPFileName);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
        } catch (Exception e) {
            System.out.println("File is not present");

            return 0;
        }

        //InitData             currentProb = new InitData();    //Class for initializing data
        //This section will get the initial information from the data file
        //Read in the first line from the file
        String s;
        s = "";

        try {
            s = br.readLine();
        } catch (Exception e) {
            System.out.println("Line could not be read in");
        }

        //print out the line that was read
        //System.out.println("This is s:" + s);
        //This reads in the first line that is used to determine the total
        //numer of customers and type of problem
        //typePtsp is + type);
        //numVeh is         + m;
        //numCust is        + n;
        //Days is            + t; //Horizon
        //Depot duration is + D;
        //capacity is       + Q;
        for (j = 0; j < s.length(); j++) {
            ch = s.charAt(j);

            if ((ch != ' ') && (j != (s.length() - 1))) {
                temp += ch;
            } else {
                if (j == (s.length() - 1)) {
                    temp += ch;
                }

                switch (index) {
                case 0:
                    type = Integer.parseInt(temp);

                    break;

                case 1:
                    m = Integer.parseInt(temp);

                    break;

                case 2:
                    n = Integer.parseInt(temp);

                    break;

                case 3:
                    t = Integer.parseInt(temp);

                    break;

                case 4:
                    D = Integer.parseInt(temp);

                    break;

                case 5:
                    Q = Integer.parseInt(temp);

                    break;
                }

                temp = "";
                index += 1;
            }
        }

        //Put the problem information into the ProblemInfo class
        ProblemInfo.fileName = PTSPFileName; //name of the file being read in
        ProblemInfo.probType = type; //problem type
        ProblemInfo.noOfVehs = m; //number of vehicles
        ProblemInfo.noOfShips = n; //number of shipments
        ProblemInfo.noOfDays = t; //number of days (horizon)
        ProblemInfo.maxCapacity = Q; //maximum capacityof a vehicle
        ProblemInfo.maxDistance = D; //maximum distance of a vehicle

        if (type != 1) //then it is not an PTSP problem
         {
            System.out.println("Problem is not an PTSP problem");

            return 0;
        }

        //This section will get the depot x and y for the PVRP and the PTSP.
        float x = 0; //y coordinate

        //This section will get the depot x and y for the PVRP and the PTSP.
        float y = 0; //y coordinate
        int i = 0;
        int d = 0;
        int q = 0;
        int f = 0;
        int a = 0;
        int vIndex = 1;
        int custCnt = 0;
        int runTimes;

        //Use 1 less the maximum as the 0 index is not used
        //declare the total number of combinations
        int[] list = new int[ZeusConstants.MaxCombinations];

        //array of 0'1 and 1's for the combinations
        int[][] currentComb = new int[ZeusConstants.MaxHorizon][ZeusConstants.MaxCombinations];

        //Number of lines to be read in is depot + number of customers
        runTimes = n + 1;

        //This section will get the customers/depots and related information
        try {
            s = br.readLine();
        } catch (Exception e) {
            System.out.println("Reading the line");
        }

        //print out the line that was read in
        //System.out.println("This is s:" + s);
        //The first for loop runtimes dependent upon how many lines are to be read
        //in
        //The next for loop reads the line into s.  Then the entire string in s
        //is processesd until the the entire line is processed and there are no
        //more characters are to be processed. There is a case for each index
        //except for the combinations.  The combinations are processed
        //until the last character in s is processed
        for (int k = 0; k < runTimes; k++) {
            index = 0;
            temp = "";
            vIndex = 0;
            custCnt++;

            for (j = 0; j < s.length(); j++) {
                ch = s.charAt(j);

                if ((ch != ' ') && (j != (s.length() - 1))) {
                    temp += ch;
                } else {
                    if (j == (s.length() - 1)) {
                        temp += ch;
                    }

                    if (temp != "") {
                        switch (index) {
                        case 0:
                            i = Integer.parseInt(temp);

                            //System.out.println("custNum is " + custNum);
                            break;

                        case 1: //x = Double.parseDouble(temp);
                            x = Float.parseFloat(temp);

                            //System.out.println("x is " + vertexX);
                            break;

                        case 2:
                            y = Float.parseFloat(temp);

                            //y = Double.parseDouble(temp);
                            //System.out.println("y is " + vertexY);
                            break;

                        case 3:
                            d = Integer.parseInt(temp);

                            //System.out.println("duration is " + duration);
                            break;

                        case 4:
                            q = Integer.parseInt(temp);

                            //System.out.println("demand is " + demand);
                            break;

                        case 5:
                            f = Integer.parseInt(temp);

                            //System.out.println("frequency is " + frequency);
                            break;

                        case 6:
                            a = Integer.parseInt(temp);

                            //System.out.println("number of comb is " + numComb);
                            break;

                        default:
                            list[vIndex] = Integer.parseInt(temp);

                            //System.out.println("visitComb[" + vIndex +"] is " + visitComb[vIndex]);
                            vIndex++;

                            break;
                        } //end switch

                        temp = "";
                        index += 1;
                    }

                    //end if
                }

                //end else
            }

            //end for
            for (int l = 0; l < a; l++)
                currentComb[l] = availShipments.getCurrentComb(list, l, t); // current visit comb

            if (k == 0) { //the first shipment line has the depot information
                ProblemInfo.depotX = x; //name of the file being read in
                ProblemInfo.depotY = y; //problem type
            } else {
                //insert the customer data into the linked list
                availShipments.insertShipment(i, x, y, d, q, f, a, list,
                    currentComb);
            }

            //read the next line from the file
            try {
                s = br.readLine();
            } catch (Exception e) {
                System.out.println("Reading in the next line");
            }

            //System.out.println("This is s:" + s);
        }

        //end for
        //print out the shipment numbers on command line
        availShipments.printShipNos();

        //call method to send the data to file
        try {
            outputPTSPShipData(type, t, PTSPFileName, "outCust.txt"); //problem type, #days or depots
        } catch (Exception e) {
            System.out.println(
                "Shipment informatin could not be sent to the file");
        }

        return 1;
    }

    //-------------------------------------------------------------
    //Method to output Customer data to a file
    //nElms member of CustomerDataArray is the size of the array
    //start member of CustomerDataArray is the 1st index, MDVRP starts with
    //one, depots are n+1 to n+t, PTSP and PVRP start with depot at 0
    //type = problem type, 0=MDVRP, 1=PTSP, 2=PVRP
    //noDepots = number of depots or number of days in planning horizon
    public void outputPTSPShipData(int type, int noDepots, String PTSPFileName,
        String fileName) throws IOException {
        PrintWriter out = null;
        Shipment curr = availShipments.first;
        int j = 0;

        try {
            //check to make sure that it is an MDVRP problem
            if (type != 1) {
                System.out.println(
                    "ShipmentLinkedList: Problem file is not an PTSP problem");

                return;
            }

            out = new PrintWriter(new FileWriter(fileName));

            //print out the problem information to the file
            out.println("PTSP File        : " + ProblemInfo.fileName);
            out.println("Type             : " + ProblemInfo.probType);
            out.println("No. of Vehs      : " + ProblemInfo.noOfVehs);
            out.println("No. of Shipments : " + ProblemInfo.noOfShips);
            out.println("No. of Days      : " + ProblemInfo.noOfDays);
            out.println("Maximum capacity : " + ProblemInfo.maxCapacity);
            out.println("Maximum Distance : " + ProblemInfo.maxDistance);
            out.println("Depot X          : " + ProblemInfo.depotX);
            out.println("Depot Y          : " + ProblemInfo.depotY);

            out.println("");

            //start processing the data in the data structure
            while (curr != null) {
                String s = Integer.toString(curr.getShipNo());
                out.print(s);
                out.print("  ");
                s = Double.toString(curr.getX());
                out.print(s);
                out.print("  ");
                s = Double.toString(curr.getY());
                out.print(s);
                out.print("  ");
                s = Integer.toString(Math.round(curr.getDuration()));
                out.print(s);
                out.print("  ");
                s = Integer.toString(Math.round(curr.getDemand()));
                out.print(s);
                out.print("  ");
                s = Integer.toString(curr.getFrequency());
                out.print(s);
                out.print("  ");

                //check if the shipment has been assigned
                //out.print(curr.isAssigned());
                //out.print("  ");
                //print out the combination of the visits to the depots
                s = Integer.toString(curr.getNoComb());
                out.print(s);
                out.print("  ");

                int numberOfComb = curr.getNoComb();
                int[] tempComb = curr.getVisitComb();

                for (int c = 0; c < numberOfComb; c++) {
                    s = Integer.toString(tempComb[c]);
                    out.print(s);
                    out.print(" ");
                }

                out.println("");

                //these are combinations that have been assigned to the depots
                //The 0 locations are not used
                int[][] curComb = curr.getCurrentComb();

                //for each of the depots print out the combinations
                for (int h = 0; h < numberOfComb; h++) {
                    for (int k = 0; k < curComb[h].length; k++) {
                        s = Integer.toString(curComb[h][k]);
                        out.print(s);
                    }

                    out.println("");
                }

                out.println("");

                j++;

                //go to the next link
                curr = curr.next;
            }

            //end for
            out.close();
        } //end try
        catch (IOException ioe) {
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (out != null) {
                System.out.println("closing file");
                out.close();
            } else {
                System.out.println("File not open.");
            }
        }

        //end finally
    }

    // end outputPTSPShipData
}


//End of PTSP
