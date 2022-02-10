package CostFunctions;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: interface class provided for user to define for a specific problem</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @version 1.0
 */
public interface CostFunctions {
    /**
 * <p>must return the total cost associated with the given object</p>
 *
 * @param o  given object
 * @return double
 */
    public double getTotalCost(Object o);

    /**
 * <p>must return the median time associated with a node in the given object</p>
 *
 * @param o  given object
 * @return double
 */
    public double getMedianTime(Object o);

    /**
 * <p>must return the angle associated with a node in the given object</p>
 *
 * @param o  given object
 * @return double
 */
    public double getAngle(Object o);

    /**
 * <p>must return the total number of non empty nodes of the given object</p>
 * Method added 10/10/03 by Mike McNamara
 *
 * @param o given object
 * @return double
 */
    public double getTotalNonEmptyNodes(Object o);

    /**
 * <p>must return the total number of nodes of the given object</p>
 * Method added 10/13/03 by Mike McNamara
 *
 * @param o given object
 * @return double
 */
    public double getTotalNodes(Object o);

    /**
 * <p>must return the total demand of the given object</p>
 *
 * @param o given object
 * @return double
 */
    public double getTotalDemand(Object o);

    /**
 * <p>must return the total distance travelled by the given object</p>
 *
 * @param o  given object
 * @return double
 */
    public double getTotalDistance(Object o);

    /**
 * <p>must return the total travel time of the given object</p>
 *
 * @param o given object
 * @return double
 */
    public double getTotalTravelTime(Object o);

    /**
 * <p>must return the total excess time of the given object</p>
 * Method added 10/10/03 by Mike McNamara
 *
 * @param o given object
 * @return double
 **/
    public double getTotalExcessTime(Object o);

    /**
 * <p>must return the total wait time of the given object</p>
 * Method added 10/10/03 by Mike McNamara
 *
 * @param o given object
 * @return double
 */
    public double getTotalWaitTime(Object o);

    /**
 * <p>must return the total service time of the given object</p>
 * Method added 10/10/03 by Mike McNamara
 *
 * @param o  given object
 * @return double
 */
    public double getTotalServiceTime(Object o);

    /**
 * <p>must return the total tardiness time of the given object</p>
 * Method added 10/10/03 by Mike McNamara
 *
 * @param o  given object
 * @return double
 */
    public double getTotalTardinessTime(Object o);

    /**
 * <p>must return the total overload of the given object</p>
 * Method added 10/10/03 by Mike McNamara
 *
 * @param o  given object
 * @return double
 */
    public double getTotalOverload(Object o);

    /**
 * <p>sets the total cost associated with the object. This method may need to make
 * use of other methods to retrieve the required cost values.</p>
 *
 * @param o  given object
 */
    public void setTotalCost(Object o);

    /**
 * <p>sets the total number of non Empty nodes associated with the object. This
 * method may need to make use of other methods to retrieve the required
 * number of nodes.</p>
 *
 * @param o  given object
 */
    public void setTotalNonEmptyNodes(Object o);

    /**
 * <p>sets the total demand associated with the object. This method may need to make
 * use of other methods to retrieve the required demand values.</p>
 *
 * @param o  given object
 */
    public void setTotalDemand(Object o);

    /**
 * <p>sets the total distance associated with the object. This method may need to make
 * use of other methods to retrieve the required distance values.</p>
 *
 * @param o  given object
 */
    public void setTotalDistance(Object o);

    /**
 * <p>sets the total travel time associated with the object. This method may need to make
 * use of other methods to retrieve the required travel time values.</p>
 *
 * @param o  given object
 */
    public void setTotalTravelTime(Object o);

    /**
 * <p>sets the total excess time associated with the object. This method may need to make
 * use of other methods to retrieve the required excess time values.</p>
 *
 * @param o  given object
 */
    public void setTotalExcessTime(Object o);

    /**
 * <p>sets the total service time associated with the object. This method may need to make
 * use of other methods to retrieve the required service time values.</p>
 *
 * @param o  given object
 */
    public void setTotalServiceTime(Object o);

    /**
 * <p>sets the total wait time associated with the object. This method may need to make
 * use of other methods to retrieve the required wait time values.</p>
 *
 * @param o  given object
 */
    public void setTotalWaitTime(Object o);

    /**
 * <p>sets the total tardiness time associated with the object. This method may need to make
 * use of other methods to retrieve the required tardiness time values.</p>
 *
 * @param o  given object
 */
    public void setTotalTardinessTime(Object o);

    /**
 * <p>sets the total overload time associated with the object. This method may need to make
 * use of other methods to retrieve the required overload time values.</p>
 *
 * @param o  given object
 */
    public void setTotalOverload(Object o);

    /**
 * <p>Sets the cost metric weight variables according to the case number</p>
 *
 * @param caseNo  determines what the cost metric weights will be
 */
    public void setWeights(int caseNo);

    /**
 * <p>swaps m nodes from a list with n nodes from a different list</p>
 *
 * @param o object where lists exist
 */
    public void swapMNchains(Object o);

    /**
 * <p>calculates the closeness of two objects relative to certain constraints, such
 * as time windows, distance, and capacity</p>
 * method added 11/03/03 by Mike McNamara
 *
 * @param o  object in which the objects that are being compared exist
 * @param p  an object
 * @param q  another object
 * @param x  type of closeness calculation
 * @return boolean true if objects are close
 */
    public boolean calcCloseness(Object o, Object p, Object q, int x);

    /**
 * <p>calculate the cost of a local exchange for the given object.</p>
 * Method added 10/20/03 by Mike McNamara and Sunil Gurung
 *
 * @param o  given object
 * @return double
 */
    public double calculateOptimizedCost(Object o);

    /**
 * <p>calculate the cost of a global exchange for the given object.</p>
 * /////////////Sunil Gurung 10/27/03///////////////////
 *
 * @param o  given object
 * @return double
 */
    public double globalCalculateOptimizedCost(Object o);

    /**
 * <p>calculate all the statistics for the given object.</p>
 *
 * @param o  given object
 */
    public void calculateTotalsStats(Object o);
}
