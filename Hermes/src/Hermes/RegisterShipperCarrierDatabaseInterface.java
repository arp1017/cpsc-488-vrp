package Hermes;

import java.sql.*;
import java.util.*;


/**
 * <p>Title: RegisterShipperCarrierDatabaseInterface.java </p>
 * <p>Description: Provides an interface to the Registered shipper and carriers
 * database.</p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
/* ---REVISED AS OF 8/16---
* @author Matthew Krowitz, John Olenic
* @version 3.0
* Note: All revisions made will be color coded green.
* <p>Log: Old Access commands have been replaced to function with the SQL server.</p>
*/
public class RegisterShipperCarrierDatabaseInterface extends DatabaseInterface {
    /**
    * Constructor, will create a JDBC:ODBC connection to the RegisterShipCarr.mdb
    * database.
    */
    public RegisterShipperCarrierDatabaseInterface() {
        /*DUMMIED OUT: Incompatable SQL commands for the new implementation sans-Access.
        
        super(DatabaseInterface.REGISTERSHIPCAR);
        delete all the lingering carriers and shippers that may be in the database
        String sql = "Delete * FROM RegCarriers;";
        super.executeSQL(sql);
        sql = "Delete * From RegShippers;";
        super.executeSQL(sql);*/
    	
    	//New commands and properly formatted.
        try{
        	PreparedStatement stmtR1 = connDrv.prepareStatement("DELETE FROM registershippercarrierregcarriers");
        	stmtR1.execute();
        	PreparedStatement stmtR2 = connDrv.prepareStatement("DELETE FROM registershippercarrierregshippers");
        	stmtR2.execute();
        }catch (Exception e) { 
        	e.printStackTrace(); 
        }
    }

    /**
    * Will add a carrier to the database
    * @param code carrier code
    * @param name carrier name
    * @param ip   carrier ip
    * @param port carrier port
    */
    
    //New commands and properly formatted. Note several SQL commands may be ordered such that they execute in one statement.
    public void addCarrier(String code, String name, String ip, String port) {
        String query = "INSERT INTO registershippercarrierregcarriers (CODE,NAME,IPAddress,PortNo) " +
            "VALUES ('" + code + "', '" + name + "', '" + ip + "', '" + port +
            "');";
        super.executeSQL(query);
    }

    /**
    * Will add a shipper to the database
    * @param code shipper code
    * @param name shipper name
    * @param ip   shipper ip
    * @param port shipper port
    */
    
  //New commands and properly formatted. Note several SQL commands may be ordered such that they execute in one statement.
    public void addShipper(String code, String name, String ip, String port) {
        String query = "INSERT INTO registershippercarrierregshippers (CODE,NAME,IPAddress,PortNo) " +
            "VALUES ('" + code + "', '" + name + "', '" + ip + "', '" + port +
            "');";
        super.executeSQL(query);
    }

    /**
    * Will remove a carrier from the database
    * @param ip   carrier ip
    * @param port carrier port
    */
    
  //New commands and properly formatted. Note several SQL commands may be ordered such that they execute in one statement.
    public void removeCarrier(String ip, String port) {
        super.executeSQL("DELETE FROM registershippercarrierregcarriers WHERE IPAddress=" + ip +
            " AND PortNo=" + port + ";");
    }

    /**
    * Will remove a shipper from the database
    * @param ip   shipper ip
    * @param port shipper port
    */
    
  //New commands and properly formatted. Note several SQL commands may be ordered such that they execute in one statement.
    public void removeShipper(String ip, String port) {
        super.executeSQL("DELETE FROM registershippercarrierregshippers WHERE IPAddress=\"" + ip +
            "\" AND PortNo=\"" + port + "\";");
    }

    /**
    * Will get all registered ports on an ip address
    * @param ip ip address to query
    * @return array of ports used
    */
    
  //New commands and properly formatted.
    public int[] getUsedPorts(String ip) {
        ResultSet res1 = null;
        ResultSet res2 = null;
        String q1 = "SELECT PortNo FROM registershippercarrierregshippers " + "WHERE IPAddress = '" +
            ip + "';";
        res1 = super.executeSQL(q1);

        String q2 = "SELECT PortNo FROM registershippercarrierregcarriers " + "WHERE IPAddress = '" +
            ip + "';";
        res2 = super.executeSQL(q2);
        
        /*DUMMIED OUT: Below was used only for testing purposes.
        System.out.println(q1);
        System.out.println(q2);
        */
        Vector v = new Vector();

        try {
            //if there is a set returned
            if (res1 != null) {
                //get the next b/c ResultSet initially points before first row
                while (res1.next()) {
                    //parse the res set
                    v.add(res1.getString("PortNo"));
                }
            }

            //if there is a set returned
            if (res2 != null) {
                while (res2.next()) {
                    //parse the res set
                    v.add(res2.getString("PortNo"));
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                res1.close();
                res2.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        //change the vector into an int array
        int[] ports = new int[v.size()];

        for (int i = 0; i < v.size(); i++) {
            String s = (String) v.elementAt(i);
            ports[i] = Integer.parseInt(s);
        }

        //return the array
        return ports;
    }

    /**
    * Will query the database for all the carriers returning messages containing
    * their IPs and port numbers.
    * @return message array of ips and ports
    */
    public synchronized Message[] getCarrierIPandPort() {
        Message[] addrs;
        Vector v = new Vector();
        
        //Updated call from Access to SQL.
        String sql = "SELECT * FROM registershippercarrierregcarriers";
        ResultSet res = super.executeSQL(sql);

        try {
            while (res.next()) {
                Message msg = new Message();
                msg.setMessageType(MessageTags.InetAddressTag);
                msg.addArgument(MessageTags.IPAddressTag,
                    res.getString("IPAddress"));
                msg.addArgument(MessageTags.PortNumberTag,
                    res.getString("PortNo"));
                v.add(msg);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                res.close();
                System.gc();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        addrs = new Message[v.size()];
        v.toArray(addrs);

        return addrs;
    }

    /**
    * Will query the database for all the shippers returning messages containing
    * their IPs and port numbers
    * @return message array of ips and ports
    */
    public synchronized Message[] getShipperIPandPort() {
        Message[] addrs;
        Vector v = new Vector();
        
      //Updated call from Access to SQL.
        String sql = "SELECT * FROM registershippercarrierregshippers";
        ResultSet res = super.executeSQL(sql);

        try {
            while (res.next()) {
                Message msg = new Message();
                msg.setMessageType(MessageTags.InetAddressTag);
                msg.addArgument(MessageTags.IPAddressTag,
                    res.getString("IPAddress"));
                msg.addArgument(MessageTags.PortNumberTag,
                    res.getString("PortNo"));
                v.add(msg);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        addrs = new Message[v.size()];
        v.toArray(addrs);

        return addrs;
    }

    /**
    * Exit the application.
    */
    public void myExit() {
        System.exit(5);
    }
}
