package HermesTest;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: ShipperAgentInterfaceTest.java </p>
 * <p>Description: Tests the ShipperAgentInterface class. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ShipperAgentInterfaceTest extends TestCase {
    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public ShipperAgentInterfaceTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("ShipperAgentInterfaceTest");

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
 * Test to see if the file data is read in properly.
 */
    public void testReadFileData() {
    }

    /**
 * Test to see if the registration process and port number
 * is returned.
 */
    public void testRegisterShipperWithMasterServer() {
        //send socket to itself
    }

    /**
 * tests to see if customers are being properly sent to the master server
 */
    public void testSendCustomersToMasterServer() {
    }
}
