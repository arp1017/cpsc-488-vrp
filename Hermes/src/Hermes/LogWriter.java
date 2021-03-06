package Hermes;

import java.io.*;

import java.lang.*;

import java.text.*;

import java.util.*;


/**
 * <p>Title: LogWriter.java </p>
 * <p>Description: LogWriter is the thread that writes into the log file.</p>
 * @author  Matlack, McNamara
 * @version 2.3
 */
public class LogWriter extends Thread {
    private PrintWriter out = null;
    private LogQueue queue = new LogQueue();
    private File file = null;

    //date portion of the name of the log file
    private String dateFile = "";

    // name of the log file without appended date
    private String logFileName = "";

    /**
* LogWriter() - constructor.
*/
    public LogWriter() {
    }

    /**
* LogWriter constructor - creates the thread.
*
* @param log - name of the log file to write to
* @exception java.lang.IOException
*/
    public LogWriter(String log) {
        try {
            logFileName = log;

            Calendar now = Calendar.getInstance(Locale.US);

            StringTokenizer st = new StringTokenizer(logFileName, ".");
            String name = st.nextToken();
            String ext = st.nextToken();
            int month = now.get(Calendar.MONTH);
            int date = now.get(Calendar.DAY_OF_MONTH);
            int year = now.get(Calendar.YEAR);
            int hour = now.get(Calendar.HOUR);
            int min = now.get(Calendar.MINUTE);
            String monthS = "";
            String dateS = "";
            String yearS = "" + year;
            String hourS = "";
            String minS = "";

            if (month < 10) {
                monthS = "0" + month;
            } else {
                monthS = "" + month;
            }

            if (date < 10) {
                dateS = "0" + date;
            } else {
                dateS = "" + date;
            }

            if (hour < 10) {
                hourS = "0" + hour;
            } else {
                hourS = "" + hour;
            }

            if (min < 10) {
                minS = "0" + min;
            } else {
                minS = "" + min;
            }

            dateFile = monthS + dateS + yearS + "_" + hourS + minS;
            logHandler(dateFile, name);
            file = new File("Logs/" + name + dateFile + "." + ext);
            out = new PrintWriter(new FileOutputStream(file), true);
            start();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
*writeLog - puts the message into the queue.
*
* @param msg - transaction message
*
*/
    public void writeLog(String msg) {
        queue.append(msg);
    }

    /**
* writeMsg  - writes into the log file.
*
*/
    private synchronized void writeMsg() {
        try {
            String msg = queue.getNextMsg();
            out.println(msg);
            out.flush();
        } catch (Exception io) {
        }
    }

    /**
* run - run method of the thread.
*
*/
    public void run() {
        while (true) {
            if (queue.isAvailable()) {
                writeMsg();
            } else {
                try {
                    sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    /**
 * Manages the log files to keep only the recent logs while deleting the
 * older ones.
 * @param dateStamp  current time
 * @param name  name of agent creating log
 */
    private void logHandler(String dateStamp, String logName) {
        File dir = new File("Logs");

        if (dir.isDirectory()) {
            int monthDateYear = 0;
            int hourMin = 0;
            int logMonthDateYear = 0;
            int logHourMin = 0;
            File[] children = dir.listFiles(); // list the files in this directory
            File fileName = null;
            String temp = "";
            String name = logName;

            if (!name.substring(0, 1).equals("m")) {
                name = name.substring(0, 1);
            }

            temp = dateStamp.substring(0, dateStamp.indexOf("_"));
            monthDateYear = Integer.parseInt(temp);
            temp = dateStamp.substring(dateStamp.indexOf("_") + 1,
                    dateStamp.length());
            hourMin = Integer.parseInt(temp);

            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    fileName = children[i];

                    // seperate file name into values to be compared
                    temp = fileName.getName().substring(logName.length(),
                            fileName.getName().indexOf("_"));
                    logMonthDateYear = Integer.parseInt(temp);
                    temp = fileName.getName().substring(fileName.getName()
                                                                .indexOf("_") +
                            1, fileName.getName().indexOf("."));
                    logHourMin = Integer.parseInt(temp);

                    // if the file is over a day old, or older than specified...
                    if ((logMonthDateYear < monthDateYear) ||
                            (logHourMin < (hourMin + HermesGlobals.logLife))) {
                        if (fileName.getName().substring(0, name.length())
                                        .equals(name)) {
                            fileName.delete(); // delete the file
                        }
                    }
                }
            }
        } else {
            dir.mkdir();
        }
    }
}
