package scode.ntxuva.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public abstract class AbstractDAO {

	protected static Logger logger = Logger.getLogger(AbstractDAO.class.getName());
	private DataSource datasource;

    // private final String DRIVER_CLASS, DB_URL, USERNAME, PASSWORD, TABLE;
	private final String TABLE;
	private final String SELECT_ALL_STMT = "select * from ";
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    
    public AbstractDAO(String table) {
    	try {
    		InitialContext ctx = new InitialContext();
    		datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/ntxuvaDB");
    	} catch (NamingException e) {
    		logger.severe("Exiting due to NamingException when creating datasource");
			e.printStackTrace();
			// System.exit(1);
    	}
    	TABLE = table;
    }
    
    abstract protected void addEntityToListFromResultSet(ResultSet rs) throws SQLException;
    // Valter: changed from return type void to Object
    abstract protected Object getSingleResult(ResultSet rs) throws SQLException;

    //TODO: populate a specific number of rows only (instead of entire table)
    protected void populateList() throws SQLException, ClassNotFoundException {
    	try {
    		conn = getConnection();
        	stmt = conn.createStatement();
    		rs = stmt.executeQuery(SELECT_ALL_STMT + TABLE);
    		logger.info("Executed: " + SELECT_ALL_STMT + TABLE);
    		while (rs.next()) {
    			addEntityToListFromResultSet(rs);
    		}
    	} finally {
    		closeConnection(rs, stmt, conn);
    	}
    }
    
    // Valter: original version returned "void" rather than 
    protected Object executeSingleQuery(String statement) 
    		throws SQLException, ClassNotFoundException {
    	Object obj;
    	try {
    		conn = getConnection();
        	stmt = conn.createStatement();
    		logger.info("Executing " + statement);
    		rs = stmt.executeQuery(statement);
    		// rs.next();
    		// getSingleResult(rs);
    		obj = getSingleResult(rs);
    	} finally {
    		closeConnection(rs, stmt, conn);
    	}
    	return obj;
    } 
    
    protected void insertEntity(String statement) throws SQLException, ClassNotFoundException {
    	try {
    		conn = getConnection();
        	stmt = conn.createStatement();
    		logger.info("Executing: " + statement);
    		stmt.executeUpdate(statement);
    	} finally {
    		closeConnection(stmt, conn);
    	}
    } 
    
    protected String getTable() { return TABLE; }
    
    private Connection getConnection() throws SQLException {
    	return datasource.getConnection();
    }
    
    private void closeConnection(ResultSet rs, Statement stmt, Connection conn) throws SQLException {
    	rs.close();
    	stmt.close();
    	conn.close();
    	rs = null;
    	stmt = null;
    	conn = null;
    }	
    
    private void closeConnection(Statement stmt, Connection conn) throws SQLException {
    	stmt.close();
    	conn.close();
    	stmt = null;
    	conn = null;
    }	

	
}