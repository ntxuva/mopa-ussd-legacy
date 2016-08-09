package scode.ntxuva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import scode.ntxuva.model.USSDSession;


public class USSDSessionDAO extends AbstractDAO {
	
	private final String INSERT_STMT = "insert into ussd_session " +
			"(id, MSISDN, main_menu, reclamacao_type, bairro, quarteirao, " +
			"contentor, comment, created) values";
	private final String UPDATE_STMT = "update ussd_session set ";
	private final String SELECT_STMT = "select * from ussd_session where id = ";
	
	private final SimpleDateFormat iso8601Formater = 
			new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
	private final SimpleDateFormat mysqlFormater = 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:s");

	
	public USSDSessionDAO() {
		super("ussd_session");
	}
	
	public void persist(USSDSession session) {
		USSDSession dbSess = findSession(session);
		if (dbSess == null) { 
			insert(session); 
		}
		else { update(session); }
	}
	
	public USSDSession findSession(USSDSession session) { 
		USSDSession foundSession = null;
		try {		
			// this statement should use "getSingleResult" to create
			// ussdSess object
			logger.info("searching for existing session");
			foundSession = (USSDSession) executeSingleQuery(SELECT_STMT
					+ session.getID() + " and MSISDN = " + session.getMSISDN());
		} catch (SQLException sqle) {
			logger.severe("Failed to select USSD Session due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to select USSD Session due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
		return foundSession;
	}

	@Override
	protected USSDSession getSingleResult(ResultSet rs) throws SQLException { 
		USSDSession ussdSess = null;
		if (rs.next()) {
			logger.info("getSingleResult - USSD Session found");
			ussdSess = new USSDSession(rs.getLong("id"), rs.getLong("MSISDN"), 
				rs.getInt("main_menu"), rs.getInt("reclamacao_type"), 
				rs.getInt("bairro"), rs.getInt("quarteirao"), 
				rs.getInt("contentor"), rs.getString("comment"), 
				rs.getDate("created"), rs.getDate("ended") );
			
			/* ussdSess = new USSDSession(rs.getLong("id"), rs.getLong("MSISDN"), 
					rs.getInt("main_menu"), rs.getInt("reclamacao_type"), 
					rs.getInt("bairro"), rs.getInt("quarteirao"), 
					rs.getInt("contentor"), rs.getString("comment"), 
					mysqlFormater.parse(rs.getString("created")),
					mysqlFormater.parse(rs.getString("ended")) );
			*/
		}
		logger.info("getSingleResult - returning USSD Session");
		return ussdSess;
	}

	@Override
	protected void addEntityToListFromResultSet(ResultSet rs) { return; }
	
	
	private void insert(USSDSession session) { 
		try {			
			insertEntity(INSERT_STMT + " (" 
					+ session.getID() + ", " 
					+ session.getMSISDN() + ", "
					+ session.getMainMenu() + ", "
					+ session.getComplaintType() + ", "
					+ session.getBairro() + ", "
					+ session.getBlock() + ", "
					+ session.getContainer() + ", \""
					+ session.getComment() + "\", \""
					+ mysqlFormater.format( session.getCreated() ) + "\" )");
		} catch (SQLException sqle) {
			logger.severe("Failed to insert USSD Session due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to insert USSD Session due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}
	
	private void update(USSDSession session) { 
		try {			
			insertEntity(UPDATE_STMT  
					+ "main_menu = " + session.getMainMenu() 
					+ ", reclamacao_type = " + session.getComplaintType() 
					+ ", bairro = " + session.getBairro() 
					+ ", quarteirao = " + session.getBlock() 
					+ ", contentor = " + session.getContainer() 
					+ ", comment = \"" + session.getComment() 
					+ "\", created = \""
					+ mysqlFormater.format( session.getCreated() ) 
					+ "\" where id = " + session.getID() 
					+ " and MSISDN = " + session.getMSISDN() );
		} catch (SQLException sqle) {
			logger.severe("Failed to update USSD Session due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to update USSD Session due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}
	
	public void close(USSDSession session) { 
		session.setEnded(new Date());
		try {			
			insertEntity(UPDATE_STMT  
					+ "main_menu = " + session.getMainMenu() 
					+ ", reclamacao_type = " + session.getComplaintType() 
					+ ", bairro = " + session.getBairro() 
					+ ", quarteirao = " + session.getBlock() 
					+ ", contentor = " + session.getContainer() 
					+ ", comment = \"" + session.getComment() 
					+ "\", created = \""
					+ mysqlFormater.format( session.getCreated() ) 
					+ "\", ended = \""
					+ mysqlFormater.format( session.getEnded() ) 
					+ "\" where id = " + session.getID() 
					+ " and MSISDN = " + session.getMSISDN() );
		} catch (SQLException sqle) {
			logger.severe("Failed to close (update) USSD Session due to SQLException");
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			logger.severe("Failed to close (update) USSD Session due to ClassNotFoundException");
			cnfe.printStackTrace();
		}
	}
	

}