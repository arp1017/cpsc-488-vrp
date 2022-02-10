package Zeus;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  The VehTypes class is to keep track of different types of vehicles that can be
 * present in a problem. This class would be used by the VehicleTypesLinkedList to keep track
 * of heterogenous vehicles used in a problem.</p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:</p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class VehTypes {
    int vehicleNo; //unique id for the vehicle
    String vehicleType; //type of the vehicle
    float vehicleCapacity; //capacity of the vehicle
    float vehicleDuration; //maximum travel time for the vehicle
    float vehicleFixedCost; //fixed cost of the vehicle
    float vehicleVariableCost; //variable cost of the vehicle
    VehTypes next = null;
    VehTypes prev = null;

    /**
 * Constructor for VehType.
 * @param vehNo id for the vehicle type
 * @param type  vehicle type
 * @param capacity maximum capacity of the vehicle
 * @param duration maximum duration of the vehicle
 * @param fCost fixed cost of the vehicle
 * @param vCost variable cost of the vehicle
 * */
    public VehTypes(int vehNo, String type, float capacity, float duration,
        float fCost, float vCost) {
        vehicleType = type;
        vehicleCapacity = capacity;
        vehicleDuration = duration;
        vehicleFixedCost = fCost;
        vehicleVariableCost = vCost;
        next = null;
        prev = null;
    }
}
