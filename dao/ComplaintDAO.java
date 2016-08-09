package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import scode.ntxuva.model.Complaint;


public class ComplaintDAO extends AbstractDAO {
	
	private final String INSERT_STMT = "insert into reclamacao values";
	private final SimpleDateFormat iso8601Formater = 
			new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
	private final SimpleDateFormat mysqlFormater = 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:s");

	public ComplaintDAO() {
		super("reclamacao");
	}
	
	public void persist(Complaint complaint) {
		try {			
			insertEntity(INSERT_STMT + " (" 
					+ complaint.getID() + ", " 
					+ complaint.getMSISDN() + ", "
					+ complaint.getSessionID() + ", "
					+ complaint.getComplaintTypeID() + ", "
					+ complaint.getContainerID() + ", \""
					+ complaint.getComment() + "\", \""
					+ mysqlFormater.format( complaint.getCreated() ) + "\" )");
		} catch (SQLException sqle) {
			logger.severe("Failed to insert Complaint due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to insert Complaint due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}

	@Override
	protected Object getSingleResult(ResultSet rs) throws SQLException { return null; }

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) { return; }
	

}