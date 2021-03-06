package Hermes;

import java.lang.*;

import java.util.*;


/**
 * <p>Title: QueueCell.java</p>
 * <p>Description: Node of the log queue.  Holds a string that is a message to
 *                 be written to the log file.</p>
 * @author Matlack
 * @version 1.0
 */
class QueueCell {
    String logMsg = "";
    QueueCell next = null;

    QueueCell(String msg) {
        logMsg = msg;
        next = null;
    }
}


/**
 * <p>Title: LogQueue.java </p>
 * <p>Description: LogQueue Class is a queue for log messages. </p>
 * @author Matlack, McNamara
 * @version 2.3
 */
public class LogQueue extends java.lang.Object {
    private QueueCell head = null; //head of the queue;
    private QueueCell last = null; //last message in the queue;

    /** Constructor Creates new LogQueue */
    public LogQueue() {
    }

    /**
* append message to log queue.
*
* @param msg - String msg to write to a log file
*/
    public synchronized void append(String msg) {
        if (head == null) {
            head = new QueueCell(msg);
            last = head;
        } else {
            last.next = new QueueCell(msg);
            last = last.next;
        }
    }

    /**
* get next message.
*
* @return msg - string next message in the queue
*/
    public synchronized String getNextMsg() {
        String msg = "";

        if (head == null) {
            return msg;
        } else {
            QueueCell temp = head;
            head = head.next;
            msg = temp.logMsg;
            temp = null;

            return msg;
        }
    }

    /**
* isAvailable.
*
* @return boolean - is there any messages in the queue (true/false)
*/
    public boolean isAvailable() {
        if (head == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
* toString.
*
* @return String - all the messages in the queue
*/
    public String toString() {
        String ret = "";
        QueueCell temp = head;

        while (temp != null) {
            ret = ret + "\n" + temp.logMsg;
            temp = temp.next;
        }

        return ret;
    }
}
