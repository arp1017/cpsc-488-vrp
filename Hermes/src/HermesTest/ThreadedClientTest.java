package HermesTest;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: ThreadedClientTest.java </p>
 * <p>Description: Tests the ThreadedClient class. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class ThreadedClientTest extends TestCase {
    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public ThreadedClientTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("ThreadedClientTest");

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
 * test to see if you are making the client properly
 */
    public void testMakeThreadedClient() {
    }
}
