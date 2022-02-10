package Zeus;


/**
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public abstract class Problem {
    protected DepotLinkedList mainDepots = null;
    protected ShipmentLinkedList mainShipments = null;

    public Problem() {
    }

    abstract void readDataFromFile(String file);

    abstract void insertShipments();
}
