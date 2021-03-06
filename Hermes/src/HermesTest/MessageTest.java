package HermesTest;

import Hermes.Message;
import Hermes.MessageTags;

import junit.framework.*;

import junit.runner.BaseTestRunner;
import java.sql.*;

/**
 * <p>Title: MessageTest.java </p>
 * <p>Description: Tests the Message class.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public class MessageTest extends TestCase {
    /**
 * Constructor that passes class name to parent class
 * @param name test class name
 */
    public MessageTest(String name) {
        super(name);
    }

    /**
 * Suite that runs all the test in order wanted.
 * @return Test  test suite configuration
 */
    public static Test suite() {
        TestSuite suite = new TestSuite("MessageTest");

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
 * tests to see if an argument is added properly
 */
    public void testAddArgument() {
    }

    /**
 * test to see if the arguments are cleared properly
 */
    public void testClearArguments() {
    }

    /**
 * test to see if you can get a message based upon its tag
 */
    public void testGetMessage() {
    }

    /**
 * test to see if two messages are equal is right
 */
    public void testEquals() {
        Message Ola = new Message();
        Ola.setMessageType(MessageTags.AckTag);
        Ola.addArgument(MessageTags.AckTag, "Gotten");

        Message Mike = new Message();
        Mike.setMessageType(MessageTags.AckTag);
        Mike.addArgument(MessageTags.AckTag, "Gotten");

        assertTrue(Ola.equals(Mike));
        Mike.addArgument(MessageTags.AckTag, "GottenXY");
        assertFalse(Ola.equals(Mike));
    }

    /**
 * test to see if you can get a value contained within a tag
 */
    public void testGetValue() {
    }

    /**
 * test to see if you can set messages properly
 */
    public void testSetMessage() {
    }
}
