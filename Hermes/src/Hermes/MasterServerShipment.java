package Hermes;


/**
 * <p>Title: MasterServerShipment.java </p>
 * <p>Description: This is the data structure for a shipment when it is being
 * held on the master server. This should be modified for each different kind of
 * problem.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class MasterServerShipment {
    public int index;
    public float xCoord;
    public float yCoord;
    public float demand;
    public float earTime;
    public float latTime;
    public float servTime;

    /**
 * Costructor. Will parse the customer information from a customer type
 * message.
 * @param cust The customer message.
 */
    public MasterServerShipment(Message cust) {
        index = Integer.parseInt(cust.getValue(MessageTags.IndexTag));
        xCoord = Float.parseFloat(cust.getValue(MessageTags.XCoordTag));
        yCoord = Float.parseFloat(cust.getValue(MessageTags.YCoordTag));
        demand = Float.parseFloat(cust.getValue(MessageTags.DemandTag));
        earTime = Float.parseFloat(cust.getValue(MessageTags.EarlyTimeTag));
        latTime = Float.parseFloat(cust.getValue(MessageTags.LateTimeTag));
        servTime = Float.parseFloat(cust.getValue(MessageTags.ServiceTimeTag));
    }
}
