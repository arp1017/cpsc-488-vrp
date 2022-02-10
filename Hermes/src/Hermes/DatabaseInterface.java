package Hermes;

import java.sql.*;


/**
 * <p>Title: DatabaseInterface.java </p>
 * <p>Description: Provides a default communication platform for database
                   interaction.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
* <p>Log: Completely removed all refrences to the old Access DBs, now utilizing SQL. Introduced dynamic user credentials.
*/
public class DatabaseInterface {
    /*DUMMIED OUT: No longer using Access DBs.
    
    names of database links
    public static final String INTRACARRIER = "E-IntraCarrier"; //IntraCarrier.mdb
    public static final String INTRASHIPS = "E-IntraShips"; //IntraShip.mdb
    public static final String REGISTERSHIPCAR = "E-RegisterShipCarr"; //RegisterShipperCarrier.mdb
    public static final String MAINDATABASE = "E-TransMainDB"; //MainDatabase.mdb
    public static final String TRANSSHIPS = "E-TransShips"; //Shipper.mdb
    public static final String TRANSTRUCKS = "E-TransTrucks"; //Carrier.mdb
    */
	
    public static String username;
    public static String password;
    Connection connDrv;
    
    /*
     * Revised method utilizes the JDBC driver to establish connection to the SQL Server.
     * Log in credentials may be hard  coded below, or via the new Credentials pop-up.
     */
    public DatabaseInterface() {
        try {
            //Establish database connection
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Establish a connection using the jdbc odbc bridge via user credentials (must be defined in SQL).
//            connDrv = DriverManager.getConnection("jdbc:mysql://10.1.40.232/hermes",
//                    username, password);
            connDrv = DriverManager.getConnection("jdbc:mysql://localhost/hermes",
                    username, password);
        } catch (Exception e) {
        	//Most common checkpoint for any error that occurs with SQL. Start here.
            e.printStackTrace();
        }
    }
    
    //Added this setter to retrieve new user credentials instead of having them hardcoded in.
    public static void setUnP(String u, String p){
    	username = u;
		password = p;
    }

    /**
    * Will execute an SQL statement on the connected database, will return
    * a result set if one is present.
    * @param sql SQL statement
    * @return ResultSet  result or null if none
    */
    public synchronized ResultSet executeSQL(String sql) {
        ResultSet result = null;
        Statement stmt = null;

        try {
            stmt = connDrv.createStatement();

            boolean hasResult = false;

            try {
                hasResult = stmt.execute(sql);
            } catch (SQLException ex) {
                System.err.println("Error executing a statement: ");
                ex.printStackTrace();
            }

            if (hasResult) {
                result = stmt.getResultSet();
            }
        } catch (SQLException sqle) {
            System.err.println("Error creating a statement: ");
            sqle.printStackTrace();
        }

        return result;
    }
}
