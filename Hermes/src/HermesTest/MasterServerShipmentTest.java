package HermesTest;

import Hermes.MasterServerShipment;
import Hermes.Message;
import Hermes.MessageTags;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: MasterServerShipmentTest.java </p>
 * <p>Description: Tests the MasterServerShipment class.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class MasterServerShipmentTest extends TestCase implements MessageTags {
    Message msg;

    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public MasterServerShipmentTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("MasterServerShipmentTest");
        suite.addTest(new MasterServerShipmentTest("testShipment"));

        return suite;
    }

    /**
 * runs before every test is perfomed.
 */
    protected void setUp() {
        System.out.println("Entering setUp");

        msg = new Message();
        msg.setMessageType(CustomerTag);
        msg.addArgument(IndexTag, "" + 0);
        msg.addArgument(XCoordTag, "" + 10);
        msg.addArgument(YCoordTag, "" + 10);
        msg.addArgument(DemandTag, "" + 20);
        msg.addArgument(EarlyTimeTag, "" + 0);
        msg.addArgument(LateTimeTag, "" + 100);
        msg.addArgument(ServiceTimeTag, "" + 30);

        System.out.println("Exiting setUp");
    }

    /**
 * executes automatically after each test. Close socket connections
 */
    protected void tearDown() {
        System.out.println("Entering tearDown");

        System.out.println("Exiting tearDown");
    }

    /**
 * Test if the shipment information is loaded correctly.
 */
    public void testShipment() {
        System.out.println("Entering testShipment");

        MasterServerShipment ship = new MasterServerShipment(msg);

        assertTrue(ship.index == 0);
        assertTrue(ship.demand == 20);
        assertTrue(ship.xCoord == 10);
        assertTrue(ship.yCoord == 10);
        assertTrue(ship.earTime == 0);
        assertTrue(ship.latTime == 100);
        assertTrue(ship.servTime == 30);

        System.out.println("Exiting testShipment");
    }
}
