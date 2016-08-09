package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import scode.ntxuva.model.USSDTransaction;


public class USSDTransactionDAO extends AbstractDAO {
	
	private final String INSERT_STMT = "insert into ussd_transaction values";
	private final SimpleDateFormat mysqlFormater = 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:s");

	public USSDTransactionDAO() {
		super("ussd_transaction");
	}
	
	public void persist(USSDTransaction transaction) {
		try {			
			insertEntity(INSERT_STMT + " (" 
					+ transaction.getID() + ", " 
					+ transaction.getSessionID() + ", "
					+ transaction.getSender() + ", "
					+ transaction.getReceiver() + ", \""
					+ transaction.getIsContinuation() + "\", \""
					+ transaction.getAction() + "\", \""
					+ transaction.getText() + "\", \""
					+ mysqlFormater.format( transaction.getCreated() ) + "\" )");
		} catch (SQLException sqle) {
			logger.severe("Failed to insert USSD Transaction due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to insert USSD Transaction due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}

	@Override
	protected Object getSingleResult(ResultSet rs) throws SQLException { return null; }

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) { return; }
	

}