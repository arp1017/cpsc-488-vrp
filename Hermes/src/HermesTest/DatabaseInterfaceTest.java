package HermesTest;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: DatabaseInterfaceTest.java </p>
 * <p>Description: Tests the DatabaseInterface class. </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class DatabaseInterfaceTest extends TestCase {
    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public DatabaseInterfaceTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("DatabaseInterfaceTest");

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
}
