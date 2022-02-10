package Zeus;

import java.io.*;


/**
 * <p>Title: Zeus - Unified Object Oriented Model for Routeing and Schdeduling Problems
 * <p>Description:  The VehTypesLinkedList class is to keep track of a list of vehicles used for
 * a problem. This VehicleTypesLinkedList keeps track of heterogenous vehicles used in a problem.
 * The VehTypesLinkedList class is a wrapper for the VehTypes class and allows
 * instances of the VehTypes to be maintained by means of a linked list. NOTE: This class needs to be
 * expanded with methods for manipulating the linked list. </p>
 * <p>Copyright:(c) 2001-2003<p>
 * <p>Company:</p>
 * @author Sam R. Thangiah
 * @version 1.0
 */
public class VehTypesLinkedList {
    VehTypes first; //head of the linked list
    VehTypes last; //tail of the linked list
    int countTypes; //number of vehicle types in the list

    public VehTypesLinkedList() {
        countTypes = 0;

        first = null;
        last = null;
    }

    /**
 * Check if the linked list is empty.
 * @return boolean  true if list is empty
 */
    public boolean isEmpty() {
        return (first == null);
    }

    /**
 * Insert a vehicle typ into the linked list
 * @param thisType instance of VehType with the required information
 * @return pointer to to the inserted vehicle type
 */
    public VehTypes insertFirst(VehTypes thisType) {
        boolean isDiagnostic = false;
        VehTypes theType = thisType; //copy attributes of current vehyType

        if (isEmpty()) { //if empty list
            last = theType; //theType-> last
        } else {
            first.prev = theType; //theType <- old first
        }

        thisType.next = first; //theType -> old first
        first = theType; //first -> theType
        countTypes++; //increment number of vehTypes

        //Diagnostic
        if (isDiagnostic) {
            System.out.println("inserted a vehicle into vehicle linked list");
        }

        return first; //return the pointer to the added node
    }
}
