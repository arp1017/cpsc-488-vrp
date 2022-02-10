package Zeus;

import java.io.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems</p>
 * <p>Description: This class implements the GENIUS class. The GENIUS class is used to provide
 * local optimization for single vehicles. </p>
 * <p>Referece: Gendreau, Laporte and </p>
 * <p>Copyright:(c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
class Genius {
    int n; //number of vertices,edges ,total number of customers for the problem
    int p; //size of neigborhood
    double[][] arcs; //2-dimensional array containing costs
    int[][] Np; //2-dimensional array [n][p] - neigborhood
    int[][] next; //route [2][n] (double linked)
    int[][] next1; //route [2][n] (double linked)
    int on_route; //number of vertices already on route
    double bigcost; //distance of not connected vertexes
    int[] not_yet_on_route;
    int notYetOnRouteNumCust; //number of customers in not_yet_on_route
    double EPS; //minimum accepted improvement
    double currentDuration; //current sum of cust service times + route duration
    int currentDemand; //current sum of cust demands
    int numGeniCust; //number customers on this route
    double z; //US current, best found and initial cost.
    double z_star; //US current, best found and initial cost.
    double z_init; //US current, best found and initial cost.
    int[] t_star;
    int[] x_star;
    double routeDuration; //total route duration w/o service time

    public Genius(int neighborhoodSize, int num, int[] notYetOnRte,
        double[][] distMatrix, int notYetOnRouteNum) { //constructor
                                                       //neighborhoodSize = p(neighborhoodSize) from main, num = total number of customers
                                                       //notYetOnRte = array of customers that are not on the route, distMatrix = distance
                                                       //matrix of customers and depots, notYetOnRouteNum = number of customers not yet on
                                                       //route
        numGeniCust = 0;
        n = num;
        p = neighborhoodSize;
        Np = new int[n + 1][p + 1];
        next = new int[n + 1][n + 1];
        not_yet_on_route = notYetOnRte;
        notYetOnRouteNumCust = notYetOnRouteNum;
        arcs = distMatrix;
        bigcost = arcs[1][1];
        on_route = 1;
        EPS = 0.0001;
        currentDuration = 0;
        currentDemand = 0;
        routeDuration = 0;

        for (int I = 0; I <= n; I++)
            for (int J = 0; J <= p; J++)
                Np[I][J] = -1;
    }

    //end constructor
    //------------------------------------------------------------------------------
    public void link(int a, int direction, int b) { //make double-link from a to b in a given direction
        next[direction][a] = b;
        next[1 - direction][b] = a;
    }

    //end link()
    //------------------------------------------------------------------------------
    public void set(int a, int direction, int b) { //make a single link from a to b in a given direction
        next[direction][a] = b;
    }

    //end set()
    //------------------------------------------------------------------------------
    public void reverse(int a, int direction, int b) { //reverse directions of all nodes between a and b to a given direction

        int tmp; //used for swap

        while (a != b) {
            tmp = next[direction][a];
            next[direction][a] = next[1 - direction][a];
            next[1 - direction][a] = tmp;
            a = next[direction][a];
        }
    }

    //end reverse()
    //------------------------------------------------------------------------------
    public int[][] updateTempNp(int[] tempT_star) { //updates nieghborhood for tabu search
                                                    //tempT_star =

        int[][] tempNp = new int[n + 1][p + 1];
        int I;
        int J;

        for (I = 0; I <= n; I++)
            for (J = 0; J <= p; J++)
                tempNp[I][J] = -1;

        int i;
        int j;
        int k;
        int tmp;
        int v;
        int w;
        int vi;
        int vj;
        int r;
        double d;
        int nStart = 1;

        for (J = 0; J <= numGeniCust; J++) {
            v = tempT_star[J];

            for (i = nStart; i <= (numGeniCust + 1); i++) {
                w = tempT_star[i];

                if (v != w) {
                    j = 0;
                    d = arcs[v][w];

                    while ((j < p) && (tempNp[w][j] != -1) &&
                            (arcs[tempNp[w][j]][w] <= d))
                        j++;

                    if (j < p) {
                        vi = v;

                        for (k = j; k < p; k++) {
                            tmp = tempNp[w][k];
                            tempNp[w][k] = vi;
                            vi = tmp;
                        }

                        //end for k
                    }

                    //end if(j<p)
                }

                //end if (v!=w)
            }

            //end for(i = nStart)
        }

        //end for J
        return tempNp;
    }

    //end updateTempNp
    //------------------------------------------------------------------------------
    public boolean tempBetween(int v, int a, int b, int direction,
        int[][] tempNext) { //returns 1 if vertex v is between vertices a and b in the given direction

        while (a != b) {
            if (v == a) {
                return true;
            }

            a = tempNext[direction][a];
        }

        if (v == a) {
            return true;
        }

        return false;
    }

    //------------------------------------------------------------------------------
    public boolean between(int v, int a, int b, int direction) { //returns 1 if vertex v is between vertices a and b in the given direction
                                                                 //v = vertex in between?, a = vertex, b = vertex, direction = right or left

        while (a != b) {
            if (v == a) {
                return true;
            }

            a = next[direction][a];
        }

        if (v == a) {
            return true;
        }

        return false;
    }

    //end between()
    double tempCost_type_I(int v, int vi, int vj, int vk, int direction,
        int[][] tempNext) { //

        int vip1 = tempNext[direction][vi];
        int vjp1 = tempNext[direction][vj];
        int vkp1 = tempNext[direction][vk];

        return (arcs[vi][v] + arcs[v][vj] + arcs[vip1][vk] + arcs[vjp1][vkp1]) -
        arcs[vi][vip1] - arcs[vj][vjp1] - arcs[vk][vkp1];
    }

    //------------------------------------------------------------------------------
    double tempCost_type_II(int v, int vi, int vj, int vk, int vl,
        int direction, int[][] tempNext) { //

        int vip1 = tempNext[direction][vi];
        int vjp1 = tempNext[direction][vj];
        int vkm1 = tempNext[1 - direction][vk];
        int vlm1 = tempNext[1 - direction][vl];

        return (arcs[vi][v] + arcs[v][vj] + arcs[vl][vjp1] + arcs[vkm1][vlm1] +
        arcs[vip1][vk]) - arcs[vi][vip1] - arcs[vlm1][vl] - arcs[vj][vjp1] -
        arcs[vkm1][vk];
    }

    //------------------------------------------------------------------------------
    double cost_type_I(int v, int vi, int vj, int vk, int direction) { //calculates cost of inserting v
                                                                       //v, vi, vj, vk = vertices; direction = right or left

        int vip1 = next[direction][vi];
        int vjp1 = next[direction][vj];
        int vkp1 = next[direction][vk];

        return (arcs[vi][v] + arcs[v][vj] + arcs[vip1][vk] + arcs[vjp1][vkp1]) -
        arcs[vi][vip1] - arcs[vj][vjp1] - arcs[vk][vkp1];
    }

    //end cost_type_I()
    //------------------------------------------------------------------------------
    double cost_type_II(int v, int vi, int vj, int vk, int vl, int direction) { //calculates cost of inserting v in between vi, vj and vk, vl
                                                                                //v, vi, vj, vk, vl = vertices; direction = right or left

        int vip1 = next[direction][vi];
        int vjp1 = next[direction][vj];
        int vkm1 = next[1 - direction][vk];
        int vlm1 = next[1 - direction][vl];

        return (arcs[vi][v] + arcs[v][vj] + arcs[vl][vjp1] + arcs[vkm1][vlm1] +
        arcs[vip1][vk]) - arcs[vi][vip1] - arcs[vlm1][vl] - arcs[vj][vjp1] -
        arcs[vkm1][vk];
    }

    //end cost_type_II()
    void insert_type_0(int v, int vi, int direction) { //

        int vip1 = next[direction][vi];

        link(vi, direction, v);
        link(v, direction, vip1);
    }

    //end insert_type_0
    //------------------------------------------------------------------------------
    void insert_type_I(int v, int vi, int vj, int vk, int direction) { //

        int vip1 = next[direction][vi];
        int vjp1 = next[direction][vj];
        int vkp1 = next[direction][vk];

        link(vi, direction, v);
        set(vj, direction, next[1 - direction][vj]);
        link(v, direction, vj);
        link(vip1, 1 - direction, vk);

        if (vk != vjp1) {
            set(vjp1, 1 - direction, next[direction][vjp1]);
            link(vjp1, direction, vkp1);
            reverse(next[direction][vj], direction, vjp1);
        } else {
            reverse(next[direction][vj], direction, vk);
            link(vk, direction, vkp1);
            set(vk, 1 - direction, vip1);
        }
    }

    //end insert_type_0()
    //------------------------------------------------------------------------------
    void insert_type_II(int v, int vi, int vj, int vk, int vl, int direction) { //

        int vip1 = next[direction][vi];
        int vjp1 = next[direction][vj];
        int vkm1 = next[1 - direction][vk];
        int vlm1 = next[1 - direction][vl];

        link(vi, direction, v);

        if (vl != vj) {
            set(vj, direction, next[1 - direction][vj]);
        }

        link(v, direction, vj);

        if (vl != vj) {
            set(vl, 1 - direction, next[direction][vl]);
            link(vl, direction, vjp1);
            reverse(next[direction][vj], direction, vl);
        }

        if (vip1 != vlm1) {
            set(vlm1, direction, next[1 - direction][vlm1]);
            set(vip1, 1 - direction, next[direction][vip1]);
            reverse(next[direction][vlm1], direction, vip1);
        }

        link(vkm1, direction, vlm1);
        link(vip1, direction, vk);
    }

    //end insert_type_II()
    //------------------------------------------------------------------------------
    void update_Np(int v, int nOn_route) { //

        int i;
        int j;
        int k;
        int tmp;
        int w;
        int vi;
        double d;

        for (i = nOn_route; i <= notYetOnRouteNumCust; i++) {
            w = not_yet_on_route[i];

            if (v != w) {
                j = 0;
                d = arcs[v][w];

                while ((j < p) && (Np[w][j] != -1) && (arcs[Np[w][j]][w] <= d))
                    j++;

                if (j < p) {
                    vi = v;

                    for (k = j; k < p; k++) {
                        tmp = Np[w][k];
                        Np[w][k] = vi;
                        vi = tmp;
                    }
                }
            }
        }
    }

    //end update_Np()
    //-----------------------------------------------------------------------------
    public double removeCust(int deleteCust, double duration, int demand) { /*Remove a specified customer from the route*/

        int i; /*counters, from, to*/
        int j; /*counters, from, to*/
        int k; /*counters, from, to*/
        int l; /*counters, from, to*/
        int direction; /*counters, from, to*/
        int v; /*vertices...*/
        int vi; /*vertices...*/
        int vj; /*vertices...*/
        int vk; /*vertices...*/
        int vl; /*vertices...*/
        int vip1; /*vertices...*/
        int vjp1;
        int vkp1;
        int vlp1;
        int vkm1;
        int vlm1;
        int vim1;
        int vjm1;
        int t;
        int r; /*index to insert into Np*/
        int round; /*round counter (0,1)*/
        double cost; /*cost of unstringing/stringing*/
        int tmp; /*temporary for variable exchange*/
        double bigdouble; /*best cost found so far and its solution */
        double best_cost; /*best cost found so far and its solution */
        int best_vi;
        int best_vj;
        int best_vk;
        int best_vl;
        int best_method;
        int best_direction;
        long t0us; /* for logging purpose */
        long t1us; /* for logging purpose */
        long t2us; /* for logging purpose */
        long t0s; /* for logging purpose */
        long t1s; /* for logging purpose */
        long t2s; /* for logging purpose */
        long nus; /* for logging purpose */
        long ns; /* for logging purpose */

        bigdouble = bigcost;
        t0us = t1us = t2us = t0s = t1s = t2s = nus = ns = 0;
        best_vi = best_vj = best_vk = best_vl = best_method = best_direction = -1;

        vi = deleteCust;
        vip1 = next[0][vi];
        vim1 = next[1][vi];

        /* compute cost of unstringing type 0 */
        cost = arcs[vim1][vip1] - arcs[vi][vip1] - arcs[vi][vim1];
        best_method = 0;
        best_direction = 0;
        best_cost = cost;

        /* for all vj from v(i+1)'s neigborhood */
        for (j = 0; (j < p) && ((vj = Np[vip1][j]) != -1); j++) {
            if (vi != vj) {
                /* for both orientations */
                for (direction = 0; direction < 2; direction++)
                    if (vj != vim1) {
                        vip1 = next[direction][vi];
                        vim1 = next[1 - direction][vi];
                        vjm1 = next[1 - direction][vj];
                        vjp1 = next[direction][vj];

                        /* for all vk from v(i-1)'s neigborhood */
                        for (k = 0; (k < p) && ((vk = Np[vim1][k]) != -1);
                                k++) {
                            /* compute cost of unstringing type I */
                            if (between(vk, vip1, vjm1, direction)) {
                                cost = cost_us_type_I(vi, vj, vk, direction);

                                if ((best_cost - cost) > EPS) {
                                    best_method = 1;
                                    best_cost = cost;
                                    best_direction = direction;
                                    best_vj = vj;
                                    best_vk = vk;
                                }
                            }

                            vkp1 = next[direction][vk];
                            vkm1 = next[1 - direction][vk];

                            if (between(vk, vj, vim1, direction) && (vk != vj) &&
                                    (vk != vim1)) {
                                /* for all vl from v(k+1)'s neigborhood */
                                for (l = 0;
                                        (l < p) && ((vl = Np[vkp1][l]) != -1);
                                        l++) {
                                    /* compute cost of unstringing type II */
                                    if (between(vl, vj, vkm1, direction)) {
                                        cost = cost_us_type_II(vi, vj, vk, vl,
                                                direction);

                                        if ((best_cost - cost) > EPS) {
                                            best_method = 2;
                                            best_cost = cost;
                                            best_direction = direction;
                                            best_vj = vj;
                                            best_vk = vk;
                                            best_vl = vl;
                                        }
                                    }
                                }

                                //end for l
                            }
                        }

                        //end for k
                    }

                //end for direction
            }
        }

        //end for j

        /* best unstringing method found, perform it */
        switch (best_method) {
        case 0:
            us_type_0(vi, best_direction);
            t0us++;
            nus++;

            break;

        case 1:
            us_type_I(vi, best_vj, best_vk, best_direction);
            t1us++;
            nus++;

            break;

        case 2:
            us_type_II(vi, best_vj, best_vk, best_vl, best_direction);
            t2us++;
            nus++;

            break;

        default:
            System.out.println("Internal Error in unstringing");
        }

        /* update cost after unstringing */
        z += best_cost;

        //Create new t_star without deleteCust
        numGeniCust--; //one less customer
        vi = 0;

        for (i = 0; i <= numGeniCust; i++) {
            t_star[i] = vi;
            vi = next[0][vi];
        }

        //update Np based on removal of customer
        for (i = 0; i <= n; i++)
            for (j = 0; j <= p; j++)
                Np[i][j] = -1;

        for (i = 0; i <= numGeniCust; i++)
            us_update_Np(t_star[i], 1);

        //update based on customers service time (duration) and the dec in cost
        //due to unstringing
        currentDuration = (currentDuration + best_cost) - duration;

        //update based on customers demand being removed from route
        currentDemand = currentDemand - demand;
        routeDuration += best_cost;

        return best_cost; //decrease in duration, excluding service time
    }

    // end method removeCust()
    //------------------------------------------------------------------------------
    public double tabuInsertCust(int insertCust, double demand, double duration) { //inserting customer from another route, demand = cust demand, duration =
                                                                                   //cust service time
                                                                                   //method used during tabu search

        int i; /* counters, from, to */
        int j; /* counters, from, to */
        int k; /* counters, from, to */
        int l; /* counters, from, to */
        int direction; /* counters, from, to */
        int v; /* vertices... */
        int vi; /* vertices... */
        int vj; /* vertices... */
        int vk; /* vertices... */
        int vl; /* vertices... */
        int vip1; /* vertices... */
        int vjp1;
        int vkp1;
        int vlp1;
        int vkm1;
        int vlm1;
        long li;
        double cost; /* cost */
        double best_cost; /* best cost found so far and its solution */
        int best_vi;
        int best_vj;
        int best_vk;
        int best_vl;
        int best_method;
        int best_direction;
        long attempt; /* number of random generator trials */
        int skipped; /* counter of unsuccessful insertions */

        //**REMEMBER TO UPDATE ON_ROUTE OF GENI
        skipped = 0;
        best_cost = bigcost * bigcost;
        best_vi = -1;
        best_vj = -1;
        best_vk = -1;
        best_vl = -1;
        best_direction = -1;
        best_method = -1;

        //first time called becomes 3
        int[] tempT_star = new int[numGeniCust + 2]; //current t_star has numGenicust + 1

        for (i = 0; i <= numGeniCust; i++)
            tempT_star[i] = t_star[i];

        tempT_star[i] = insertCust; //placed at end of array
        t_star = new int[numGeniCust + 2];

        for (i = 0; i <= (numGeniCust + 1); i++)
            t_star[i] = tempT_star[i]; //t_star has new cust

        //update Np neighborhood , first reset to -1
        for (i = 0; i <= n; i++)
            for (j = 0; j <= p; j++)
                Np[i][j] = -1;

        numGeniCust++; //adding a cust to geni

        for (i = 0; i < numGeniCust; i++) //update all t_star Np's with vertices

            us_update_Np(t_star[i], 1); //currently on the route

        v = insertCust; //v = cust to insert

        /* for all vi from v's neighborhood */
        for (i = 0; (i < p) && ((vi = Np[v][i]) != -1); i++) {
            /* for both directions */
            for (direction = 0; direction < 2; direction++) {
                vip1 = next[direction][vi];

                /* standard insertion immidiately after vi */
                cost = (arcs[vi][v] + arcs[v][vip1]) - arcs[vi][vip1];

                if (cost < (best_cost - EPS)) {
                    best_cost = cost;
                    best_method = 0;
                    best_vi = vi;
                    best_direction = direction;
                }

                /* for all vj from v's neighborhood */
                for (j = 0; (j < p) && ((vj = Np[v][j]) != -1); j++)
                    if (i != j) {
                        vjp1 = next[direction][vj];

                        /* and for all vk from v(i+1)'s neighborhood */
                        for (k = 0; (k < p) && ((vk = Np[vip1][k]) != -1);
                                k++) {
                            if ((vk != vj) && (vk != vi) && (vip1 != vj)) {
                                if (!between(vk, vi, vj, direction)) {
                                    vkp1 = next[direction][vk];
                                    vkm1 = next[1 - direction][vk];

                                    /* type I insertion */
                                    cost = cost_type_I(v, vi, vj, vk, direction);

                                    if (cost < (best_cost - EPS)) {
                                        best_method = 1;
                                        best_cost = cost;
                                        best_direction = direction;
                                        best_vi = vi;
                                        best_vj = vj;
                                        best_vk = vk;
                                    }

                                    /* for all vl from v(j+1)'s neighborhood */
                                    for (l = 0;
                                            (l < p) &&
                                            ((vl = Np[vjp1][l]) != -1); l++) {
                                        if ((vk != vjp1) && (vl != vi) &&
                                                (vl != vip1)) {
                                            if (between(vl, vi, vj, direction)) {
                                                vlm1 = next[1 - direction][vl];

                                                /* type II insertion */
                                                cost = cost_type_II(v, vi, vj,
                                                        vk, vl, direction);

                                                if (cost < (best_cost - EPS)) {
                                                    best_method = 2;
                                                    best_cost = cost;
                                                    best_direction = direction;
                                                    best_vi = vi;
                                                    best_vj = vj;
                                                    best_vk = vk;
                                                    best_vl = vl;
                                                }
                                            }
                                        }
                                    }

                                    //end for l
                                }
                            }
                        }

                        /* for k */
                    }

                /*if after for j*/
            }

            /* for direction */
        }

        /* for i */
        /* best insertion found, perform it */
        if ((skipped < n) || (best_cost < (bigcost - EPS))) {
            /* if it uses only legal edges or no vertex can be inserted legally */
            switch (best_method) {
            case 0:
                insert_type_0(v, best_vi, best_direction);
                skipped = 0;

                break;

            case 1:
                insert_type_I(v, best_vi, best_vj, best_vk, best_direction);
                skipped = 0;

                break;

            case 2:
                insert_type_II(v, best_vi, best_vj, best_vk, best_vl,
                    best_direction);
                skipped = 0;

                break;

            default:
                System.out.println("internal error.\n");
                System.exit(-1);
            } //end of switch
        } else {
            /* GENI did not found the correct way to insert v, skip it */
            skipped++;

            for (i = on_route; i < notYetOnRouteNumCust; i++)
                not_yet_on_route[i] = not_yet_on_route[i + 1];

            not_yet_on_route[notYetOnRouteNumCust] = v;
            System.out.println("Should not get here, tabuInsertCust");
            System.exit(-1);
        }

        /* and update neigborhood for all points */
        if (skipped == 0) {
            update_Np(v, 1);
            currentDuration += (duration + best_cost);
            currentDemand += demand;
            routeDuration += best_cost;

            //update t_star
            vi = 0;

            for (i = 0; i <= numGeniCust; i++) {
                t_star[i] = vi;
                vi = next[0][vi];
            }

            System.out.println("Tabu Customer " + insertCust + "inserted.");

            return best_cost;
        }

        return best_cost;
    }

    // end tabuInsertCust
    //------------------------------------------------------------------------------
    public void us_update_Np(int v, int nStart) { //update the Np neighborhood for unstringing phase
                                                  //v = new vertex on route, updating all customers with respect to v
                                                  //nStart begin update at nStart

        int i;
        int j;
        int k;
        int tmp;
        int w;
        int vi;
        int vj;
        int r;
        double d;

        for (i = nStart; i <= numGeniCust; i++) {
            w = t_star[i];

            if (v != w) {
                j = 0;
                d = arcs[v][w];

                while ((j < p) && (Np[w][j] != -1) && (arcs[Np[w][j]][w] <= d))
                    j++;

                if (j < p) {
                    vi = v;

                    for (k = j; k < p; k++) {
                        tmp = Np[w][k];
                        Np[w][k] = vi;
                        vi = tmp;
                    }
                }
            }
        }
    }

    //end us_update_Np()
    //------------------------------------------------------------------------------
    void init_Stars() { //initializes variables

        int vi;
        int i;

        z_star = currentDuration;
        z_init = z_star;
        z = z_init;
        vi = 0;
        t_star = new int[numGeniCust + 1];

        for (i = 0; i <= numGeniCust; i++) {
            t_star[i] = vi;
            vi = next[0][vi];
        }

        x_star = new int[numGeniCust + 1];

        for (i = 0; i <= numGeniCust; i++)
            x_star[i] = t_star[i];
    }

    //end init_Stars()
    //------------------------------------------------------------------------------
    void init_US(int depotIndex) { //

        int vi;
        int i;
        int j;

        init_Stars();

        for (i = 0; i <= n; i++)
            for (j = 0; j <= p; j++)
                Np[i][j] = -1;

        for (i = 1; i <= numGeniCust; i++)
            us_update_Np(t_star[i], 1);

        us_update_Np(depotIndex, 1);
    }

    //end init_US()
    //------------------------------------------------------------------------------
    public double costBestStringing(int insertCust) { //calculates the cost of inserting a customer without actually inserting it for the tabu search
                                                      //insertCust = customer to be inserted

        int[][] tempNext = new int[n + 1][n + 1];
        int[][] tempNp = new int[n + 1][p + 1];
        int[] tempT_star = new int[numGeniCust + 2];
        int I;
        int J;

        int i; //counters, from, to
        int j; //counters, from, to
        int k; //counters, from, to
        int l; //counters, from, to
        int direction; //counters, from, to
        int v; //vertices..
        int vi; //vertices..
        int vj; //vertices..
        int vk; //vertices..
        int vl; //vertices..
        int vip1; //vertices..
        int vjp1;
        int vkp1;
        int vlp1;
        int vkm1;
        int vlm1;
        int vim1;
        int vjm1;
        int t;
        int r; //index to insert into Np
        int round; //round counter (0,1)
        double cost; //cost of unstringing/stringing
        int tmp; //temporary for variable exchange
        double bigdouble; //best cost found so far and its solution
        double best_cost = 0; //best cost found so far and its solution
        int best_vi;
        int best_vj;
        int best_vk;
        int best_vl;
        int best_method;
        int best_direction;
        long t0us; //for logging purpose
        long t1us; //for logging purpose
        long t2us; //for logging purpose
        long t0s; //for logging purpose
        long t1s; //for logging purpose
        long t2s; //for logging purpose
        long nus; //for logging purpose
        long ns; //for logging purpose

        for (I = 0; I <= n; I++) {
            for (J = 0; J <= n; J++) {
                tempNext[I][J] = next[I][J];
            }
        }

        for (I = 0; I <= numGeniCust; I++) {
            tempT_star[I] = t_star[I];
        }

        tempT_star[numGeniCust + 1] = insertCust;

        //update tempNp neighborhoods
        tempNp = updateTempNp(tempT_star);

        bigdouble = bigcost;
        t0us = t1us = t2us = t0s = t1s = t2s = nus = ns = 0;
        best_vi = best_vj = best_vk = best_vl = best_method = best_direction = -1;

        /***************** STEP 2: STRINGING *****************/
        best_cost = bigdouble * bigdouble;
        best_method = -1;
        v = tempT_star[numGeniCust + 1]; //we will insert removed vertex

        /***for all vi from v's neighborhood***/
        for (i = 0; (i < p) && ((vi = tempNp[v][i]) != -1); i++) {
            /***for both directions***/
            for (direction = 0; direction < 2; direction++) {
                vip1 = tempNext[direction][vi];

                /***standard insertion immediately after vi***/
                cost = (arcs[vi][v] + arcs[v][vip1]) - arcs[vi][vip1];

                if ((best_cost - cost) > EPS) {
                    best_cost = cost;
                    best_method = 0;
                    best_vi = vi;
                    best_direction = direction;
                }

                /***for all vj from v's neighborhood***/
                for (j = 0; (j < p) && ((vj = tempNp[v][j]) != -1); j++) {
                    if (vi != vj) {
                        vjp1 = tempNext[direction][vj];

                        /***and for all vk from v(i+1)'s neighborhood***/
                        for (k = 0; (k < p) && ((vk = tempNp[vip1][k]) != -1);
                                k++) {
                            if ((vk != v) && (vk != vj) && (vk != vi) &&
                                    (vip1 != vj)) {
                                if (!(tempBetween(vk, vi, vj, direction,
                                            tempNext))) {
                                    vkp1 = tempNext[direction][vk];
                                    vkm1 = tempNext[1 - direction][vk];

                                    /***type I insertion***/
                                    cost = tempCost_type_I(v, vi, vj, vk,
                                            direction, tempNext);

                                    if ((best_cost - cost) > EPS) {
                                        best_method = 1;
                                        best_cost = cost;
                                        best_direction = direction;
                                        best_vi = vi;
                                        best_vj = vj;
                                        best_vk = vk;
                                    }

                                    /***for all vl from v(j+1)'s neighborhood***/
                                    for (l = 0;
                                            (l < p) &&
                                            ((vl = tempNp[vjp1][l]) != -1);
                                            l++) {
                                        if ((vl != v) && (vk != vjp1) &&
                                                (vl != vi) && (vl != vip1)) {
                                            if (tempBetween(vl, vi, vj,
                                                        direction, tempNext)) {
                                                vlm1 = tempNext[1 - direction][vl];

                                                /***type II insertion***/
                                                cost = tempCost_type_II(v, vi,
                                                        vj, vk, vl, direction,
                                                        tempNext);

                                                if ((best_cost - cost) > EPS) {
                                                    best_method = 2;
                                                    best_cost = cost;
                                                    best_direction = direction;
                                                    best_vi = vi;
                                                    best_vj = vj;
                                                    best_vk = vk;
                                                    best_vl = vl;
                                                }
                                            }
                                        }
                                    }

                                    //end for l
                                }
                            }
                        }

                        //end for k
                    }
                }
            }

            //end for direction
        }

        //end for i
        return best_cost;
    }

    //end costBestStringing
    //------------------------------------------------------------------------------
    public double costBest_US(int delCust) { //calculates the cost of removing one customer without actually removing it
                                             //delCust - customer number that is going to be deleted

        int i; //counters, from, to
        int j; //counters, from, to
        int k; //counters, from, to
        int l; //counters, from, to
        int direction; //counters, from, to
        int v; //vertices...
        int vi; //vertices...
        int vj; //vertices...
        int vk; //vertices...
        int vl; //vertices...
        int vip1; //vertices...
        int vjp1;
        int vkp1;
        int vlp1;
        int vkm1;
        int vlm1;
        int vim1;
        int vjm1;
        int t;
        int r; //index to insert into Np
        int round; //round counter (0,1)
        double cost; //cost of unstringing/stringing
        int tmp; //temporary for variable exchange
        double bigdouble; //best cost found so far and its solution
        double best_cost; //best cost found so far and its solution
        int best_vi;
        int best_vj;
        int best_vk;
        int best_vl;
        int best_method;
        int best_direction;
        long t0us; //for logging purpose
        long t1us; //for logging purpose
        long t2us; //for logging purpose
        long t0s; //for logging purpose
        long t1s; //for logging purpose
        long t2s; //for logging purpose
        long nus; //for logging purpose
        long ns; //for logging purpose

        bigdouble = bigcost;
        t0us = t1us = t2us = t0s = t1s = t2s = nus = ns = 0;
        best_vi = best_vj = best_vk = best_vl = best_method = best_direction = -1;

        /***** STEP1: UNSTRINGING *****/
        vi = delCust;
        vip1 = next[0][vi];
        vim1 = next[1][vi];

        /***compute cost of unstringing type 0***/
        cost = arcs[vim1][vip1] - arcs[vi][vip1] - arcs[vi][vim1];
        best_method = 0;
        best_direction = 0;
        best_cost = cost;

        /***for all vj from v(i+1)'s neigborhood***/
        for (j = 0; (j < p) && ((vj = Np[vip1][j]) != -1); j++) {
            if (vi != vj) {
                /***for both orientations***/
                for (direction = 0; direction < 2; direction++) {
                    if (vj != vim1) {
                        vip1 = next[direction][vi];
                        vim1 = next[1 - direction][vi];
                        vjm1 = next[1 - direction][vj];
                        vjp1 = next[direction][vj];

                        /***for all vk from v(i-1)'s neigborhood***/
                        for (k = 0; (k < p) && ((vk = Np[vim1][k]) != -1);
                                k++) {
                            /***compute cost of unstringing type I***/
                            if (between(vk, vip1, vjm1, direction)) {
                                cost = cost_us_type_I(vi, vj, vk, direction);

                                if ((best_cost - cost) > EPS) {
                                    best_method = 1;
                                    best_cost = cost;
                                    best_direction = direction;
                                    best_vj = vj;
                                    best_vk = vk;
                                }
                            }

                            vkp1 = next[direction][vk];
                            vkm1 = next[1 - direction][vk];

                            if (between(vk, vj, vim1, direction) && (vk != vj) &&
                                    (vk != vim1)) {
                                /***for all vl from v(k+1)'s neigborhood***/
                                for (l = 0;
                                        (l < p) && ((vl = Np[vkp1][l]) != -1);
                                        l++) {
                                    /***compute cost of unstringing type II***/
                                    if (between(vl, vj, vkm1, direction)) {
                                        cost = cost_us_type_II(vi, vj, vk, vl,
                                                direction);

                                        if ((best_cost - cost) > EPS) {
                                            best_method = 2;
                                            best_cost = cost;
                                            best_direction = direction;
                                            best_vj = vj;
                                            best_vk = vk;
                                            best_vl = vl;
                                        }
                                    }
                                }

                                //end for l
                            }

                            //end for k
                        }

                        //end for direction
                    }

                    //end for j
                }
            }
        }

        return best_cost;
    }

    //end costBest_Us
    //---------------------------------------------------------------------------
    void exec_US(int depotIndex) { //executes unstringing and stringing to optimize the current route
                                   //depotIndex - index of current depot in distance matrix

        int i; //counters, from, to
        int j; //counters, from, to
        int k; //counters, from, to
        int l; //counters, from, to
        int direction; //counters, from, to
        int v; //vertices...
        int vi; //vertices...
        int vj; //vertices...
        int vk; //vertices...
        int vl; //vertices...
        int vip1; //vertices...
        int vjp1;
        int vkp1;
        int vlp1;
        int vkm1;
        int vlm1;
        int vim1;
        int vjm1;
        int t;
        int r; //index to insert into Np
        int round; //round counter (0,1)
        double cost; //cost of unstringing/stringing
        int tmp; //temporary for variable exchange
        double bigdouble; //best cost found so far and its solution
        double best_cost; //best cost found so far and its solution
        int best_vi;
        int best_vj;
        int best_vk;
        int best_vl;
        int best_method;
        int best_direction;
        long t0us; //for logging purpose
        long t1us; //for logging purpose
        long t2us; //for logging purpose
        long t0s; //for logging purpose
        long t1s; //for logging purpose
        long t2s; //for logging purpose
        long nus; //for logging purpose
        long ns; //for logging purpose

        bigdouble = bigcost;
        t0us = t1us = t2us = t0s = t1s = t2s = nus = ns = 0;
        best_vi = best_vj = best_vk = best_vl = best_method = best_direction = -1;

        init_US(depotIndex);

        next1 = new int[n + 1][n + 1];

        for (i = 0; i <= n; i++)
            for (j = 0; j <= n; j++)
                next1[i][j] = next[i][j];

        /***main loop***/
        t = 1; //t is the index of vertex to perform US on

        for (round = 0; round < 1; round++) //2 rounds - 1st: only legal unstringing


            while (t < (numGeniCust + 1)) //repeat until no improvements are possible
             {
                /***** STEP1: UNSTRINGING *****/
                vi = x_star[t];
                vip1 = next[0][vi];
                vim1 = next[1][vi];

                /* compute cost of unstringing type 0 */
                cost = arcs[vim1][vip1] - arcs[vi][vip1] - arcs[vi][vim1];
                best_method = 0;
                best_direction = 0;
                best_cost = cost;

                /* for all vj from v(i+1)'s neigborhood */
                for (j = 0; (j < p) && ((vj = Np[vip1][j]) != -1); j++) {
                    if (vi != vj) {
                        /* for both orientations */
                        for (direction = 0; direction < 2; direction++)
                            if (vj != vim1) {
                                vip1 = next[direction][vi];
                                vim1 = next[1 - direction][vi];
                                vjm1 = next[1 - direction][vj];
                                vjp1 = next[direction][vj];

                                /* for all vk from v(i-1)'s neigborhood */
                                for (k = 0;
                                        (k < p) && ((vk = Np[vim1][k]) != -1);
                                        k++) {
                                    /* compute cost of unstringing type I */
                                    if (between(vk, vip1, vjm1, direction)) {
                                        cost = cost_us_type_I(vi, vj, vk,
                                                direction);

                                        if ((best_cost - cost) > EPS) {
                                            best_method = 1;
                                            best_cost = cost;
                                            best_direction = direction;
                                            best_vj = vj;
                                            best_vk = vk;
                                        }
                                    }

                                    vkp1 = next[direction][vk];
                                    vkm1 = next[1 - direction][vk];

                                    if (between(vk, vj, vim1, direction) &&
                                            (vk != vj) && (vk != vim1)) {
                                        /* for all vl from v(k+1)'s neigborhood */
                                        for (l = 0;
                                                (l < p) &&
                                                ((vl = Np[vkp1][l]) != -1);
                                                l++) {
                                            /* compute cost of unstringing type II */
                                            if (between(vl, vj, vkm1, direction)) {
                                                cost = cost_us_type_II(vi, vj,
                                                        vk, vl, direction);

                                                if ((best_cost - cost) > EPS) {
                                                    best_method = 2;
                                                    best_cost = cost;
                                                    best_direction = direction;
                                                    best_vj = vj;
                                                    best_vk = vk;
                                                    best_vl = vl;
                                                }
                                            }
                                        }

                                        //end for l
                                    }
                                }

                                //end for k
                            }

                        //end for direction
                    }
                }

                //end for j

                /* best unstringing method found, perform it */
                if (round == 0) {
                    if (best_cost > (bigdouble - EPS)) //if unstringing uses non-existing edges
                     {
                        t++;

                        continue;
                    }
                }

                switch (best_method) {
                case 0:
                    us_type_0(vi, best_direction);
                    t0us++;
                    nus++;

                    break;

                case 1:
                    us_type_I(vi, best_vj, best_vk, best_direction);
                    t1us++;
                    nus++;

                    break;

                case 2:
                    us_type_II(vi, best_vj, best_vk, best_vl, best_direction);
                    t2us++;
                    nus++;

                    break;

                default:
                    System.out.println("Internal Error in unstringing");
                }

                /* update cost after unstringing */
                z += best_cost;
                routeDuration += best_cost;

                /***************** STEP 2: STRINGING *****************/
                best_cost = bigdouble * bigdouble;
                best_method = -1;
                v = x_star[t]; /* we will insert removed vertex */

                /* for all vi from v's neighborhood */
                for (i = 0; (i < p) && ((vi = Np[v][i]) != -1); i++) {
                    /* for both directions */
                    for (direction = 0; direction < 2; direction++) {
                        vip1 = next[direction][vi];

                        /* standard insertion immediately after vi */
                        cost = (arcs[vi][v] + arcs[v][vip1]) - arcs[vi][vip1];

                        if ((best_cost - cost) > EPS) {
                            best_cost = cost;
                            best_method = 0;
                            best_vi = vi;
                            best_direction = direction;
                        }

                        /* for all vj from v's neighborhood */
                        for (j = 0; (j < p) && ((vj = Np[v][j]) != -1); j++)
                            if (vi != vj) {
                                vjp1 = next[direction][vj];

                                /* and for all vk from v(i+1)'s neighborhood */
                                for (k = 0;
                                        (k < p) && ((vk = Np[vip1][k]) != -1);
                                        k++) {
                                    if ((vk != v) && (vk != vj) && (vk != vi) &&
                                            (vip1 != vj)) {
                                        if (!(between(vk, vi, vj, direction))) {
                                            vkp1 = next[direction][vk];
                                            vkm1 = next[1 - direction][vk];

                                            /* type I insertion */
                                            cost = cost_type_I(v, vi, vj, vk,
                                                    direction);

                                            if ((best_cost - cost) > EPS) {
                                                best_method = 1;
                                                best_cost = cost;
                                                best_direction = direction;
                                                best_vi = vi;
                                                best_vj = vj;
                                                best_vk = vk;
                                            }

                                            /* for all vl from v(j+1)'s neighborhood */
                                            for (l = 0;
                                                    (l < p) &&
                                                    ((vl = Np[vjp1][l]) != -1);
                                                    l++) {
                                                if ((vl != v) && (vk != vjp1) &&
                                                        (vl != vi) &&
                                                        (vl != vip1)) {
                                                    if (between(vl, vi, vj,
                                                                direction)) {
                                                        vlm1 = next[1 -
                                                            direction][vl];

                                                        /* type II insertion */
                                                        cost = cost_type_II(v,
                                                                vi, vj, vk, vl,
                                                                direction);

                                                        if ((best_cost - cost) > EPS) {
                                                            best_method = 2;
                                                            best_cost = cost;
                                                            best_direction = direction;
                                                            best_vi = vi;
                                                            best_vj = vj;
                                                            best_vk = vk;
                                                            best_vl = vl;
                                                        }
                                                    }
                                                }
                                            }

                                            //end for l
                                        }
                                    }
                                }

                                //end for k
                            }
                    }

                    //end for direction
                }

                //end for i

                /* best insertion found, perform it */
                switch (best_method) {
                case 0:
                    insert_type_0(v, best_vi, best_direction);
                    t0s++;
                    ns++;

                    break;

                case 1:
                    insert_type_I(v, best_vi, best_vj, best_vk, best_direction);
                    t1s++;
                    ns++;

                    break;

                case 2:
                    insert_type_II(v, best_vi, best_vj, best_vk, best_vl,
                        best_direction);
                    t2s++;
                    ns++;

                    break;

                default:
                    System.out.println("Internal Error in stringing");
                }

                /* update cost after stringing */
                z += best_cost;
                routeDuration += best_cost;

                /* save the route, if it is the best found so far */
                if ((z_star - z) > EPS) {
                    vi = 0;

                    for (i = 0; i <= numGeniCust; i++) {
                        t_star[i] = vi;
                        vi = next[0][vi];
                    }

                    z_star = z;
                    t = 1; // start unstringing from the 1st vertex again
                } else {
                    t++; //if length of the route did not improve, try next vertex
                }
            }

        //end while (t<n+1)
    }

    //end exec_US()
    //------------------------------------------------------------------------------
    public void saveSolution(String cFilename, String probId) //throws IOException
     {
        int i;
        PrintWriter solOutput = null;

        try {
            solOutput = new PrintWriter(new FileWriter(cFilename));

            solOutput.print("Problem Id :" + probId + "\r\n");
            solOutput.print("Number of Customers :" + numGeniCust + "\r\n");
            solOutput.print("Processing Time :" + "\r\n");

            for (i = 0; i <= numGeniCust; i++)
                solOutput.print("#" + i + " Cust Id :" + t_star[i] + "\r\n");

            solOutput.print("Current demand : " + currentDemand + "\r\n");
            solOutput.print("Duration of route with service time: : " +
                currentDuration + "\r\n");
            solOutput.print("Duration of route without service time: " +
                routeDuration + "\r\n");

            double checkRouteDuration = 0;
            int vi = t_star[0];

            for (i = 1; i <= numGeniCust; i++) {
                checkRouteDuration += arcs[vi][t_star[i]];
                vi = t_star[i];
            }

            checkRouteDuration += arcs[t_star[numGeniCust]][t_star[0]];
            solOutput.print("Check Route Duration: " + checkRouteDuration +
                "\r\n");
            solOutput.close();
        } //end try

        catch (IOException ioe) {
            System.out.println("IO error " + ioe.getMessage());
        } finally {
            if (solOutput != null) {
                solOutput.close();
            } else {
                System.out.println("File not open.");
            }
        }

        //end finally
    }

    // end saveSolution()
    //------------------------------------------------------------------------------
    double cost_us_type_I(int vi, int vj, int vk, int direction) { //calculates the cost of unstringing of a type I
                                                                   //vi, vj, vk = vertices; direction = right or left

        int vip1 = next[direction][vi];
        int vim1 = next[1 - direction][vi];
        int vjp1 = next[direction][vj];
        int vkp1 = next[direction][vk];

        return (arcs[vim1][vk] + arcs[vip1][vj] + arcs[vkp1][vjp1]) -
        arcs[vim1][vi] - arcs[vi][vip1] - arcs[vk][vkp1] - arcs[vj][vjp1];
    }

    //end cost_us_type_I()
    //------------------------------------------------------------------------------
    double cost_us_type_II(int vi, int vj, int vk, int vl, int direction) { //calculates the cost of unstringing of a type II
                                                                            //vi, vj, vk, vl = vertices; direction = right or left

        int vip1 = next[direction][vi];
        int vim1 = next[1 - direction][vi];
        int vjm1 = next[1 - direction][vj];
        int vkp1 = next[direction][vk];
        int vlp1 = next[direction][vl];

        return (arcs[vip1][vj] + arcs[vl][vkp1] + arcs[vim1][vk] +
        arcs[vlp1][vjm1]) - arcs[vi][vip1] - arcs[vjm1][vj] - arcs[vl][vlp1] -
        arcs[vk][vkp1] - arcs[vim1][vi];
    }

    //end cost_us_type_II
    void us_type_0(int vi, int direction) {
        int i;
        link(next[1 - direction][vi], direction, next[direction][vi]);
    }

    //end us_type_0()
    //------------------------------------------------------------------------------
    void us_type_I(int vi, int vj, int vk, int direction) {
        int vip1 = next[direction][vi];
        int vim1 = next[1 - direction][vi];
        int vjp1 = next[direction][vj];
        int vjm1 = next[1 - direction][vj];
        int vkp1 = next[direction][vk];
        int vkm1 = next[1 - direction][vk];
        int i;

        set(vk, direction, vkm1);
        link(vim1, direction, vk);
        reverse(vkm1, direction, vi);
        link(vip1, direction, vj);
        set(vj, direction, vjm1);
        reverse(vjm1, direction, vk);
        link(vkp1, direction, vjp1);
    }

    //end us_type_I()
    //------------------------------------------------------------------------------
    void us_type_II(int vi, int vj, int vk, int vl, int direction) {
        int vip1 = next[direction][vi];
        int vip2 = next[direction][vip1];
        int vim1 = next[1 - direction][vi];
        int vjm1 = next[1 - direction][vj];
        int vjm2 = next[1 - direction][vjm1];
        int vkm1 = next[1 - direction][vk];
        int vkp1 = next[direction][vk];
        int vlp1 = next[direction][vl];
        int i;

        set(vip1, 1 - direction, vip2);
        link(vip1, direction, vj);
        link(vl, direction, vkp1);
        link(vim1, direction, vk);
        set(vk, direction, vkm1);
        reverse(vkm1, direction, vl);
        link(vlp1, direction, vjm1);

        if (vjm1 != vip1) {
            set(vjm1, direction, vjm2);
            reverse(vjm2, direction, vip1);
        }
    }

    //end us_type_II()
    //------------------------------------------------------------------------------
    public void initRoute(int onRoute, Shipment one, Shipment two) { //init route with depot and first two customers
                                                                     //if onroute = 0, use not_yet_on_route[1] and [2]
                                                                     //update next matrix with first three members of not_yet_on_route
                                                                     //update Np neighborhoods
        on_route = onRoute;

        /* put first 3 vertices on the route */
        //individually init the route
        next[0][0] = not_yet_on_route[on_route];
        next[1][0] = not_yet_on_route[on_route + 1];
        next[0][not_yet_on_route[on_route]] = not_yet_on_route[on_route + 1];
        next[1][not_yet_on_route[on_route]] = not_yet_on_route[0];
        next[0][not_yet_on_route[on_route + 1]] = not_yet_on_route[0];
        next[1][not_yet_on_route[on_route + 1]] = not_yet_on_route[on_route];

        // initialize Np
        update_Np(0, on_route); //send depot index in arcs matrix
        update_Np(not_yet_on_route[on_route], on_route);
        update_Np(not_yet_on_route[on_route + 1], on_route);
        on_route += 2; //we now have first two custs on route
        numGeniCust += 2;

        routeDuration = arcs[0][one.shipNo] + arcs[one.shipNo][two.shipNo] +
            arcs[two.shipNo][0];
        currentDuration += (one.duration + two.duration + routeDuration);
        currentDemand += (one.demand + two.demand);
    }

    //end initRoute()
    //------------------------------------------------------------------------------
    public boolean insertCust(int onRoute, Shipment insertCust,
        double maxDuration, int maxDemand) {
        //onRoute = index of notYetOnRoute array
        int i; /* counters, from, to */

        //onRoute = index of notYetOnRoute array
        int j; /* counters, from, to */

        //onRoute = index of notYetOnRoute array
        int k; /* counters, from, to */

        //onRoute = index of notYetOnRoute array
        int l; /* counters, from, to */

        //onRoute = index of notYetOnRoute array
        int direction; /* counters, from, to */
        int v; /* vertices... */
        int vi; /* vertices... */
        int vj; /* vertices... */
        int vk; /* vertices... */
        int vl; /* vertices... */
        int vip1; /* vertices... */
        int vjp1;
        int vkp1;
        int vlp1;
        int vkm1;
        int vlm1;
        long li;
        double cost; /* cost */
        double best_cost; /* best cost found so far and its solution */
        int best_vi;
        int best_vj;
        int best_vk;
        int best_vl;
        int best_method;
        int best_direction;
        long attempt; /* number of random generator trials */
        int skipped; /* counter of unsuccessful insertions */

        //**REMEMBER TO UPDATE ON_ROUTE OF GENI
        skipped = 0;
        best_cost = bigcost * bigcost;
        best_vi = -1;
        best_vj = -1;
        best_vk = -1;
        best_vl = -1;
        best_direction = -1;
        best_method = -1;
        on_route++; //first time called becomes 3

        //Troubleshooting
        if (insertCust.shipNo != not_yet_on_route[onRoute]) {
            System.out.println(" indices do not match in insert method.");
            System.exit(-1);
        }

        if (((currentDemand + insertCust.demand) > maxDemand) ||
                ((currentDuration + insertCust.duration) > maxDuration)) {
            return false;
        }

        v = not_yet_on_route[onRoute]; //next v not on the route */

        /* for all vi from v's neighborhood */
        for (i = 0; (i < p) && ((vi = Np[v][i]) != -1); i++) {
            /* for both directions */
            for (direction = 0; direction < 2; direction++) {
                vip1 = next[direction][vi];

                /* standard insertion immidiately after vi */
                cost = (arcs[vi][v] + arcs[v][vip1]) - arcs[vi][vip1];

                if (cost < (best_cost - EPS)) {
                    best_cost = cost;
                    best_method = 0;
                    best_vi = vi;
                    best_direction = direction;
                }

                /* for all vj from v's neighborhood */
                for (j = 0; (j < p) && ((vj = Np[v][j]) != -1); j++)
                    if (i != j) {
                        vjp1 = next[direction][vj];

                        /* and for all vk from v(i+1)'s neighborhood */
                        for (k = 0; (k < p) && ((vk = Np[vip1][k]) != -1);
                                k++) {
                            if ((vk != vj) && (vk != vi) && (vip1 != vj)) {
                                if (!between(vk, vi, vj, direction)) {
                                    vkp1 = next[direction][vk];
                                    vkm1 = next[1 - direction][vk];

                                    /* type I insertion */
                                    cost = cost_type_I(v, vi, vj, vk, direction);

                                    if (cost < (best_cost - EPS)) {
                                        best_method = 1;
                                        best_cost = cost;
                                        best_direction = direction;
                                        best_vi = vi;
                                        best_vj = vj;
                                        best_vk = vk;
                                    }

                                    /* for all vl from v(j+1)'s neighborhood */
                                    for (l = 0;
                                            (l < p) &&
                                            ((vl = Np[vjp1][l]) != -1); l++) {
                                        if ((vk != vjp1) && (vl != vi) &&
                                                (vl != vip1)) {
                                            if (between(vl, vi, vj, direction)) {
                                                vlm1 = next[1 - direction][vl];

                                                /* type II insertion */
                                                cost = cost_type_II(v, vi, vj,
                                                        vk, vl, direction);

                                                if (cost < (best_cost - EPS)) {
                                                    best_method = 2;
                                                    best_cost = cost;
                                                    best_direction = direction;
                                                    best_vi = vi;
                                                    best_vj = vj;
                                                    best_vk = vk;
                                                    best_vl = vl;
                                                }
                                            }
                                        }
                                    }

                                    //end for l
                                }
                            }
                        }

                        /* for k */
                    }

                /*if after for j*/
            }

            /* for direction */
        }

        /* for i */
        if ((currentDuration + insertCust.duration + best_cost) > maxDuration) {
            on_route--;

            return false; //CANNOT INSERT
        } else {
            /* best insertion found, perform it */
            if ((skipped < n) || (best_cost < (bigcost - EPS))) {
                /* if it uses only legal edges or no vertex can be inserted legally */
                switch (best_method) {
                case 0:
                    insert_type_0(v, best_vi, best_direction);
                    skipped = 0;

                    break;

                case 1:
                    insert_type_I(v, best_vi, best_vj, best_vk, best_direction);
                    skipped = 0;

                    break;

                case 2:
                    insert_type_II(v, best_vi, best_vj, best_vk, best_vl,
                        best_direction);
                    skipped = 0;

                    break;

                default:
                    System.out.println("internal error.\n");
                    System.exit(-1);
                } //end of switch
            } else {
                /* GENI did not found the correct way to insert v, skip it */
                skipped++;

                for (i = on_route; i < notYetOnRouteNumCust; i++)
                    not_yet_on_route[i] = not_yet_on_route[i + 1];

                not_yet_on_route[notYetOnRouteNumCust] = v;
                on_route--;

                return false;
            }

            /* and update neigborhood for all points not on the route */
            if (skipped == 0) {
                update_Np(v, on_route);
                currentDuration += (insertCust.duration + best_cost);
                currentDemand += insertCust.demand;
                routeDuration += best_cost;
                numGeniCust++;

                return true;
            }
        }

        //end of else after if >maxDuration
        return false;
    }

    //end  insertCust()
    //------------------------------------------------------------------------------
    public void printNext() { //prints out the arcs of the Geni

        int I;
        int v = 0;

        for (I = 0; I < 3; I++) {
            System.out.println("arcs[" + v + "][" + next[0][v] + "]:" +
                arcs[v][next[0][v]]);
            v = next[0][v];
        }
    }

    //end printNext()
    //------------------------------------------------------------------------------
}


//end class Geni
