package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import scode.ntxuva.model.Bairro;


public class BairroDAO extends AbstractDAO {
	
	private ArrayList<Bairro> bairroList;

	public BairroDAO() {
		super("bairro");
		bairroList = new ArrayList<Bairro>();
		try {
			populateList();
		} catch (SQLException sqle) {
			logger.severe("Failed to populateList of bairro from resultset due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to populateList of bairro from resultset due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}

	@Override
	protected Object getSingleResult(ResultSet rs) throws SQLException { return null; }

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) {
		Bairro bairro = null;
		try {
			bairro = new Bairro(rs.getInt("id"), rs.getString("name"));
		} catch (SQLException sqle) {
			logger.severe("Failed to get bairro from resultset due to SQLException");
			sqle.printStackTrace();
		}
		bairroList.add(bairro);
		logger.fine("Added to list, bairro " + bairro);
	}
	
//	public ArrayList<Bairro> getList() { return bairroList;	}
	
	// TODO: put these methods in AbstractDAO
	
	public int getListSize() { return bairroList.size(); }
	
	public Bairro getListItem(int index) { 
		if ( (index >= 0) && (index <= getListSize()) )
			return bairroList.get(index); 
		return null;
	}
	
	public String findItemName(int id) { 
		for (int i = 0; i < bairroList.size(); i++)
			if ( bairroList.get(i).getID() == id ) 
				return bairroList.get(i).getName();
		return "";
	}
	
	public String printList(String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bairroList.size(); i++)
			sb.append(bairroList.get(i).toString() + delimiter);
		return sb.toString();
	}
	
	public boolean isValid(String answer) {
		for (int i = 0; i < bairroList.size(); i++)
			if ( answer.equals( "" + bairroList.get(i).getID() ) )
				return true;
		return false;
	}
	
}