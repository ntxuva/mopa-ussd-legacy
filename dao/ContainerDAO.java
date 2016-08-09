package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import scode.ntxuva.model.Container;


public class ContainerDAO extends AbstractDAO {
	
	private ArrayList<Container> containerList;

	public ContainerDAO() {
		super("contentor");
		containerList = new ArrayList<Container>();
		try {
			populateList();
		} catch (SQLException sqle) {
			logger.severe("Failed to populateList of container from resultset due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to populateList of container from resultset due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}

	@Override
	protected Object getSingleResult(ResultSet rs) throws SQLException { return null; }

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) {
		Container container = null;
		try {
			container = new Container(rs.getInt("id"), rs.getString("name"), 
					rs.getDouble("latitude"), rs.getDouble("longitude"), 
					rs.getInt("bairro"));
		} catch (SQLException sqle) {
			logger.severe("Failed to get bairro from resultset due to SQLException");
			sqle.printStackTrace();
		}
		containerList.add(container);
		logger.fine("Added to list, container " + container);
	}
	

// 	public ArrayList<Container> getList() { return containerList; }
	
	
	// TODO: put these methods in AbstractDAO
	
	public int getListSize() { return containerList.size(); }
	
	public Container getListItem(int index) { 
		if ( (index >= 0) && (index <= getListSize()) )
			return containerList.get(index); 
		return null;
	}
	
	public Container getContainer(int ID) {
		for (int i = 0; i < containerList.size(); i++)
			if ( containerList.get(i).getID() == ID )
				return containerList.get(i);
		return null;
	}
	
	public Container getContainerFromBairro(int bairroID) {
		for (int i = 0; i < containerList.size(); i++)
			if ( containerList.get(i).getBairroID() == bairroID )
				return containerList.get(i);
		return null;
	}
	
	public String findItemName(int id) { 
		for (int i = 0; i < containerList.size(); i++)
			if ( containerList.get(i).getID() == id ) 
				return containerList.get(i).getName();
		return "";
	}
	
	public String printList(String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < containerList.size(); i++)
			sb.append(containerList.get(i).toString() + delimiter);
		return sb.toString();
	}
	
	public String printBairroList(int bairroID, String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < containerList.size(); i++) {
			if ( containerList.get(i).getBairroID() == bairroID )
				sb.append(containerList.get(i).toString() + delimiter);
		}
		return sb.toString();
	}
	
	public boolean isValid(int bairroID, String answer) {
		for (int i = 0; i < containerList.size(); i++)
			if ( containerList.get(i).getBairroID() == bairroID )
				if ( answer.equals("" + (containerList.get(i).getID()) % 10) )
					return true;
		return false;
	}
	
}