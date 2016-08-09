package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import scode.ntxuva.model.Block;


public class BlockDAO extends AbstractDAO {
	
	private ArrayList<Block> blockList;

	public BlockDAO() {
		super("quarteirao");
		blockList = new ArrayList<Block>();
		try {
			populateList();
		} catch (SQLException sqle) {
			logger.severe("Failed to populateList of quarteirao from resultset due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to populateList of quarteirao from resultset due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}

	@Override
	protected Object getSingleResult(ResultSet rs) throws SQLException { return null; }

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) {
		Block block = null;
		try {
			block = new Block(rs.getInt("id"), rs.getDouble("latitude"), 
					rs.getDouble("longitude"), rs.getInt("bairro"));
		} catch (SQLException sqle) {
			logger.severe("Failed to get quarteirao from resultset due to SQLException");
			sqle.printStackTrace();
		}
		blockList.add(block);
		logger.fine("Added to list, quarteirao " + block);
	}
	

// 	public ArrayList<Container> getList() { return containerList; }
	
	
	// TODO: put these methods in AbstractDAO
	
	public int getListSize() { return blockList.size(); }
	
	public Block getListItem(int index) { 
		if ( (index >= 0) && (index <= getListSize()) )
			return blockList.get(index); 
		return null;
	}
	
	public Block getBlock(int ID) {
		for (int i = 0; i < blockList.size(); i++)
			if ( blockList.get(i).getID() == ID )
				return blockList.get(i);
		return null;
	}
	
	public Block getBlockFromBairro(int bairroID) {
		for (int i = 0; i < blockList.size(); i++)
			if ( blockList.get(i).getBairroID() == bairroID )
				return blockList.get(i);
		return null;
	}
	
	public String printList(String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < blockList.size(); i++)
			sb.append(blockList.get(i).toString() + delimiter);
		return sb.toString();
	}
	
	public String printBairroList(int bairroID, String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < blockList.size(); i++) {
			if ( blockList.get(i).getBairroID() == bairroID )
				sb.append(blockList.get(i).toString() + delimiter);
		}
		return sb.toString();
	}
	
	public boolean isValid(int bairroID, String answer) {
		for (int i = 0; i < blockList.size(); i++)
			if ( blockList.get(i).getBairroID() == bairroID )
				if ( answer.equals("" + (blockList.get(i).getID()) % 1000) )
					return true;
		return false;
	}
	
}