package HermesTest;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: CarrierMessageHandlerTest.java </p>
 * <p>Description: Tests the carrier agent message handler class. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class CarrierMessageHandlerTest extends TestCase {
    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public CarrierMessageHandlerTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("CarrierMessageHandlerTest");
        suite.addTest(new CarrierAgentInterfaceTest("testProcessMessage"));
        suite.addTest(new CarrierAgentInterfaceTest("getCostFromZeus"));
        suite.addTest(new CarrierAgentInterfaceTest("getAcceptFromZeus"));

        return suite;
    }

    /**
 * runs before every test is perfomed.
 */
    protected void setUp() {
        System.out.println("Entering setUp");

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
 * tests to see if the incoming message is being parsed properly.
 */
    public void testProcessMessage() {
    }

    /**
 * tests to see if zeus is giving us the cost properly
 */
    public void getCostFromZeus() {
    }

    /**
 * tests to see if this shipment can be inserted into Zeus.
 */
    public void getAcceptFromZeus() {
    }
}
