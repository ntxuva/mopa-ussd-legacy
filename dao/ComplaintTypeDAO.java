package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import scode.ntxuva.model.ComplaintType;


public class ComplaintTypeDAO extends AbstractDAO {
	
	private ArrayList<ComplaintType> complaintTypeList;

	public ComplaintTypeDAO() {
		super("reclamacao_type");
		complaintTypeList = new ArrayList<ComplaintType>();
		try {
			populateList();
		} catch (SQLException sqle) {
			logger.severe("Failed to populateList of complaintType from resultset due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to populateList of complaintType from resultset due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}

	@Override
	protected Object getSingleResult(ResultSet rs) throws SQLException { return null; }

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) {
		ComplaintType complaintType = null;
		try {
			complaintType = new ComplaintType(rs.getInt("id"), rs.getString("name"));
		} catch (SQLException sqle) {
			logger.severe("Failed to get complaintType from resultset due to SQLException");
			sqle.printStackTrace();
		}
		complaintTypeList.add(complaintType);
		logger.fine("Added to list, complaintType " + complaintType);
	}
	
//	public ArrayList<ComplaintType> getList() { return complaintTypeList; }
	
	// TODO: put these methods in AbstractDAO
	
		public int getListSize() { return complaintTypeList.size(); }
		
		public ComplaintType getListItem(int index) { 
			if ( (index >= 0) && (index <= getListSize()) )
				return complaintTypeList.get(index); 
			return null;
		}
		
		public String findItemName(int id) { 
			for (int i = 0; i < complaintTypeList.size(); i++)
				if ( complaintTypeList.get(i).getID() == id ) 
					return complaintTypeList.get(i).getName();
			return "";
		}
		
		public String printList(String delimiter) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < complaintTypeList.size(); i++)
				sb.append(complaintTypeList.get(i).toString() + delimiter);
			return sb.toString();
		}

		
		public boolean isValid(String answer) {
			for (int i = 0; i < complaintTypeList.size(); i++) 
				if ( answer.equals( "" + complaintTypeList.get(i).getID() ) )
					return true; 
			return false;
		}
		
	
	
}