package com.ttr.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbAccess {

	private static Connection conn=null;
	private boolean isMysqlDriverLoaded = false;
	private boolean isDb2DriverLoaded = false;

	/**
	 * Constructor
	 */
	public DbAccess() {
		super();
	}


	/**
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}


	/**
	 * @param conn the conn to set
	 */
	public void setConn(Connection conn) {
		DbAccess.conn = conn;
	}


	/**
	 * @return the isMysqlDriverLoaded
	 */
	public boolean isMysqlDriverLoaded() {
		return isMysqlDriverLoaded;
	}


	/**
	 * @param isMysqlDriverLoaded the isMysqlDriverLoaded to set
	 */
	public void setMysqlDriverLoaded(boolean isMysqlDriverLoaded) {
		this.isMysqlDriverLoaded = isMysqlDriverLoaded;
	}


	/**
	 * @return the isDb2DriverLoaded
	 */
	public boolean isDb2DriverLoaded() {
		return isDb2DriverLoaded;
	}


	/**
	 * @param isDb2DriverLoaded the isDb2DriverLoaded to set
	 */
	public void setDb2DriverLoaded(boolean isDb2DriverLoaded) {
		this.isDb2DriverLoaded = isDb2DriverLoaded;
	}


	/**
	 * getConnection
	 */
	public void getConnection(String sgbdName, String ipAddress, String portNumber, String user, String password, String location) throws MySQLException {

		String url = "";

		try {
			// TODO log
			System.out.println(".................... Connection in progress\n");

			if (sgbdName.equalsIgnoreCase("mysql")) {
				if (!isMysqlDriverLoaded) {
					// TODO log
					System.out.println(".................... Load the mysql driver\n");
					// Load the driver
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					// TODO log
					System.out.println(".................... Driver loaded\n");
					isMysqlDriverLoaded = true;
				}
				url = "jdbc:"+ sgbdName.toLowerCase()+ "://"+ ipAddress + ":" + portNumber + "?user=" + user + "&password=" + password;
			}
			if (sgbdName.equalsIgnoreCase("db2")) {
				if (!isDb2DriverLoaded) {
					// TODO log
					System.out.println(".................... Load the DB2 driver\n");
					// Load the driver
					Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
					// TODO log
					System.out.println(".................... Driver loaded\n");
					isDb2DriverLoaded = true;
				}
				url = "jdbc:db2://"+ ipAddress + ":" + portNumber + "/" + location;
			}

			//conn = DriverManager.getConnection("jdbc:mysql://sen57:3306/arcsys_schema?user=arcsysUser&password=infotel0");
	        //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?user=root&password=root");

			// TODO log
			System.out.println(".................... URL="+url+"\n");

			if (sgbdName.equalsIgnoreCase("mysql")) {
				conn = DriverManager.getConnection(url);
			}

			if (sgbdName.equalsIgnoreCase("db2")) {
	            Properties props = new Properties();
	            props.put("securityMechanism", "3"); 
	            props.put("ssid", "DZAA");
	            props.put("user", user);
	            props.put("password", password);
	            props.put("databaseName", location);
	            props.put("traceLevel","-1");
	            props.put("traceFile",".\\log_PrototypeSimpleSQL.txt");

				conn = DriverManager.getConnection(url,props);
			}
			// TODO log
	        System.out.println(".................... Connection done\n");
		}
		catch (SQLException sqlEx) {
			throw new MySQLException("SQLException:" + sqlEx.getMessage() + "/SQLState:" + sqlEx.getSQLState() + "ErrorCode:" + sqlEx.getErrorCode());
		}
		catch (ClassNotFoundException clEx) {
			throw new MySQLException("Class not found");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new MySQLException("Exception");
		}
	}

	/**
	 * Collect the list of schema
	 */
	// TODO filtrer sur schema
	public List<String> getSchemas(String sgbdName) throws MySQLException {
    	List<String> result;
    	result = new ArrayList<String>();

    	String req="";

    	// TODO log
    	System.out.println(".................... Get schema list in progress\n");

    	if (sgbdName.equalsIgnoreCase("mysql")){
    		req= "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA;";
    	}
    	if (sgbdName.equalsIgnoreCase("db2")){
    		// TODO create the stmt to get the list of schema in DB2
    		req= "SELECT DISTINCT CREATOR FROM SYSIBM.SYSTABLES;";
    	}

    	if (null == this.getConn()) {
    		throw new MySQLException("No connection established");	
    	}

    	try {

    		java.sql.Statement stmt = conn.createStatement ();

    		stmt.executeQuery (req);

    		// TODO log
        	System.out.println(".................... " + req + "\n");

    		ResultSet rs = stmt.getResultSet ();

    		while (rs.next ()) {
    			result.add(rs.getString(1).trim());
    		}

    		rs.close ();
    		stmt.close ();

    		// TODO log
    		System.out.println(".................... Get schema list executed\n");

    	} catch (SQLException e) {
    		throw new MySQLException(e.getMessage());
    	}
    	return result;
	}
	
	
	/**
	 * Collect table description
	 */
	// TODO filtrer sur schema
	public List<String> getTables(String sgbdName, String schema) throws MySQLException {
    	List<String> result;
    	result = new ArrayList<String>();

    	String req="";

    	// TODO log
    	System.out.println(".................... Get table list in progress\n");

    	if (sgbdName.equalsIgnoreCase("mysql")){
    		if (schema.isEmpty()){
    			req= "SELECT TABLE_NAME FROM information_schema.TABLES";
    		}
    		else {
    			req= "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA='" + schema +"';";
    		}
    	}
    		
    	if (sgbdName.equalsIgnoreCase("db2")){
    		if (schema.isEmpty()){
    			// TODO vérifier
    			req= "SELECT NAME FROM SYSIBM.SYSTABLES WHERE CREATOR='" + schema +"';";
    		}
    		else{
    			// TODO vérifier
    			req= "SELECT NAME FROM SYSIBM.SYSTABLES WHERE CREATOR='" + schema +"';";
    		}
    	}

    	if (null == this.getConn()) {
    		throw new MySQLException("No connection established");	
    	}

    	try {

    		java.sql.Statement stmt = conn.createStatement ();

    		stmt.executeQuery (req);

        	// TODO log
        	System.out.println(".................... " + req + "\n");

    		ResultSet rs = stmt.getResultSet ();

    		while (rs.next ()) {
    			result.add(rs.getString(1).trim());
    		}

    		rs.close ();
    		stmt.close ();

    		// TODO log
    		System.out.println(".................... Get table list executed\n");

    	} catch (SQLException e) {
    		throw new MySQLException(e.getMessage());
    	}
    	return result;
	}
	

	/**
	 * Collect columns description
	 */
	// TODO filtrer sur schema
	public List<String> getColumns(String sgbdName, String schema, String table) throws MySQLException {
    	List<String> result;
    	result = new ArrayList<String>();

    	String req;

    	// TODO log
    	System.out.println(".................... Get column list in progress\n");

    	if (sgbdName.equalsIgnoreCase("mysql"))
    		// TODO requête mysql pour récupérer la liste des colonnes
    		req= "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='" + schema + "' AND TABLE_NAME='" + table + "';";
    	else if (sgbdName.equalsIgnoreCase("db2"))
    		req= "SELECT NAME FROM SYSIBM.SYSCOLUMNS WHERE TBCREATOR='" + schema + "' AND TBNAME='" + table + "';";
    	else req="";

    	// TODO log
    	System.out.println(".................... " + req + "\n");

    	if (null == this.getConn()) {
    		throw new MySQLException("No connection established");	
    	}

    	try {

    		java.sql.Statement stmt = conn.createStatement ();

    		stmt.executeQuery (req);

        	// TODO log
        	System.out.println(".................... " + req + "\n");

    		ResultSet rs = stmt.getResultSet ();

    		while (rs.next ()) {
    			result.add(rs.getString(1).trim());
    		}

    		rs.close ();
    		stmt.close ();

    		// TODO log
    		System.out.println(".................... Get column list executed\n");

    	} catch (SQLException e) {
    		throw new MySQLException(e.getMessage());
    	}
    	return result;
	}

	/**
	 * Browse table
	 */
	public List<String[]> browseTable(String schema, String table, int nbCols) throws MySQLException {
    	List<String[]> result;
    	result = new ArrayList<String[]>();
    	String req="SELECT * FROM ";

    	// TODO log
    	System.out.println(".................... Browse table in progress\n");

    	if (null == this.getConn()) {
    		throw new MySQLException("No connection established");	
    	}

    	try {

			// --- "SELECT * FROM schema.table;
			java.sql.Statement stmt = conn.createStatement ();

			if (schema.isEmpty())
				req += table;
			else
				req += schema + "." + table;

			stmt.executeQuery (req);

	    	// TODO log
	    	System.out.println(".................... " + req + "\n");

			ResultSet rs = stmt.getResultSet ();

			// TODO log
			//System.out.println("metadata:" + rs.getMetaData());
			
			//System.out.println(rs.getMetaData());
			ResultSetMetaData rsMD = rs.getMetaData();
			
			for (int i = 1; i <= rsMD.getColumnCount(); i++){
				System.out.print("ColumnName:" + rsMD.getColumnName(i) + "\t");
				System.out.print("ColumnLabel:" + rsMD.getColumnLabel(i) + "\t");
				System.out.print("ColumnSize:" + rsMD.getColumnDisplaySize(i) + "\t");
				System.out.print("ColumnType:" + rsMD.getColumnType(i) + "\t");
				System.out.println("ColumnTypeName:" + rsMD.getColumnTypeName(i) + "\t");
			}


			while (rs.next ()) {

				System.out.println("rs." + rs.toString());
				String[] str = new String[nbCols];

				for (int i = 1; i <= nbCols; i++){
					try {
						System.out.println();
						if (null == rs.getObject(i)){
							str[i-1]= new String("NULL");
							// TODO log
							System.out.println("element:" + i + "/" + nbCols + "is NULL");
						}
						else{
							str[i-1]= new String(rs.getObject(i).toString());
							// TODO log
							System.out.println("element:" + i + "/" + nbCols + ":" + str[i-1].toString());
						}
					} catch (SQLException e) {
						// TODO log
						System.out.println("SQL error" + e.getMessage());
						throw new MySQLException (e.getMessage());
					}
					 catch (Exception ex) {
							// TODO log
							System.out.println("Java error" + ex.getMessage());
							throw new MySQLException (ex.getMessage());
					}
				}

				result.add(str);

			}

			rs.close ();
			stmt.close ();

			// TODO log
			System.out.println(".................... Browse table executed\n");

		} catch (SQLException e) {
			throw new MySQLException(e.getMessage());
		}
		return result;
	}	


	/**
	 * Browse table
	 */
	public List<String[]> getColumnDescription(String schema, String table) throws MySQLException {
    	List<String[]> result;
    	result = new ArrayList<String[]>();

    	String req="SELECT ORDINAL_POSITION, COLUMN_NAME, COLUMN_TYPE, COLUMN_KEY FROM information_schema.COLUMNS where TABLE_NAME = '";
    	//String req="SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.COLUMNS where TABLE_NAME = '";
    	// TODO log
    	System.out.println(".................... Get the column description in progress\n");

    	if (null == this.getConn()) {
    		throw new MySQLException("No connection established");	
    	}

    	try {
    		java.sql.Statement stmt = conn.createStatement ();

  
    		req += table + "';";
    		stmt.executeQuery (req);

    		// TODO log
    		System.out.println(".................... " + req + "\n");

    		ResultSet rs = stmt.getResultSet ();

    		// TODO log
    		//System.out.println("metadata:" + rs.getMetaData());

    		//System.out.println(rs.getMetaData());

    		while (rs.next()){
    			String[] str = new String[5];

     			str[0] = "";
    			str[1] = rs.getString(1);
    			str[2] = rs.getString(2);
    			str[3] = rs.getString(3);
    			str[4] = rs.getString(4);
    			result.add(str);
    		}

    		rs.close ();
    		stmt.close ();

    		// TODO log
    		System.out.println(".................... Browse table executed\n");

    	} catch (SQLException e) {
    		throw new MySQLException(e.getMessage());
    	}
    	return result;
	}


	/**
	 * Close the connection
	 */
	public void closeConnection() throws MySQLException{
		try {
			if (null != this.getConn()){
				this.getConn().close();
				this.setConn(null);
				// TODO log
			    System.out.println(".................... Connection closed\n");
			}
		} catch (SQLException e) {
			throw new MySQLException("Close connection failed");
		}
	}
}