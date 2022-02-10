package HermesTest;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: MasterAgentInterfaceTest.java </p>
 * <p>Description: Tests the MasterAgentInterface class. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class MasterAgentInterfaceTest extends TestCase {
    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public MasterAgentInterfaceTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("MasterAgentInterfaceTest");

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
 * Tests to see if lesser optimization message is being generated properly.
 * And if the text fields are being updated properly.
 */
    public void testLesserOptimization() {
    }

    /**
 * Tests to see if greater optimization message is being generated properly
 * and if the text fields are being updated properly.
 */
    public void testGreaterOptimization() {
    }

    /**
 * Test to see if the short solution, long solution, and std out files are
 * being generated correctly
 */
    public void testMasterSchedule() {
    }
}
