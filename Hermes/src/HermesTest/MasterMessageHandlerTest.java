package HermesTest;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: MasterMessageHandlerTest.java </p>
 * <p>Description: Tests the MasterMessageHandler class. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class MasterMessageHandlerTest extends TestCase {
    /**
    * Constructor that passes class name to parent class
    * @param name test class name
    */
    public MasterMessageHandlerTest(String name) {
        super(name);
    }

    /**
    * Suite that runs all the test in order wanted.
    * @return Test  test suite configuration
    */
    public static Test suite() {
        TestSuite suite = new TestSuite("MasterMessageHandlerTest");

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
     * tests to see if function determining if the port is used is working
     * correctly
     */
    public void testPortNoUsed() {
    }

    /**
    * tests to see if shipments are being posted to main linked list properly
    */
    public void testPostShipmentsToMainLinkedList() {
    }

    /**
    * tests to see if carriers are being registered and port no being generated
    * properly
    */
    public void testRegisterCarrier() {
    }

    /**
    * tests to see if shippers are being registered and port no being generated
    * properly
    */
    public void testRegisterShipper() {
    }

    /**
    * checks to see if the accept messages are being generated properly and
    * is being sent to the carrier.
    */
    public void testSendCustomerToCarrierToAccept() {
    }
}
