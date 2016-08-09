package scode.ntxuva.model;

import java.util.Date;

import scode.ntxuva.dao.BairroDAO;
import scode.ntxuva.dao.ContainerDAO;
import scode.ntxuva.model.USSDSession;

public class Complaint {
	
	private long id, MSISDN, sessionID;
	private int bairroID, complaintTypeID, containerID, blockID;
	private String comment;
	private Date created;
	private BairroDAO bairroDAO;
	private ContainerDAO containerDAO;
	
	public Complaint
		(long id, long MSISDN, long sessionID, int bairroID, int complaintTypeID, 
			int containerID, int blockID, String text, Date created) {
		this(MSISDN, sessionID, bairroID, complaintTypeID, containerID, blockID, text, created);
		this.id = id;
	}
	
	public Complaint(long MSISDN, USSDSession session) {
		this(MSISDN, session.getID(), session.getBairro(), session.getComplaintType(), 
				0, 0, session.getComment(), session.getEnded());
		
		bairroDAO = new BairroDAO();
		containerDAO = new ContainerDAO();
		
		// convert session answers into container ID, quarteirao ID, 
		// and descriptive comment
		String extraInfo = "";
		if (session.getContainer() != 0 ) {
			int containerNbr = (10 * session.getBairro()) + session.getContainer();
			setContainerID(containerNbr);
			extraInfo = "Contentor: " + containerDAO.findItemName(containerNbr);
		}
		if (session.getBlock() != 0 ) {
			int blockNbr = (1000 * session.getBairro()) + session.getBlock();
			setBlockID(blockNbr);
			// extraInfo = "Quarteirao " + blockNbr;
			extraInfo = "Quarteirao " + session.getBlock();
		}
		if ( session.getComment() == null || session.getComment().trim().isEmpty() ) {
			setComment("Criado por USSD. Bairro: " 
					+ bairroDAO.findItemName(session.getBairro()) + ". " + extraInfo);
		}
		
	}
	
	public Complaint
		(long MSISDN, long sessionID, int bairroID, int complaintTypeID,
			int containerID, int blockID, String text, Date created) {
		this.MSISDN = MSISDN;
		this.sessionID = sessionID;
		this.bairroID = bairroID;
		this.complaintTypeID = complaintTypeID;
		this.containerID = containerID;
		this.blockID = blockID;
		this.comment = text;
		this.created = created;
	}
	
	public long getID() { return id; }
	public long getMSISDN() { return MSISDN; }
	public long getSessionID() { return sessionID; }
	public int getBairroID() { return bairroID; }
	public int getComplaintTypeID() { return complaintTypeID; }
	public int getContainerID() { return containerID; }
	public int getBlockID() { return blockID; }
	public String getComment() { return comment; }
	public Date getCreated() { return created; }
	
	private void setContainerID(int id) { containerID = id; }
	private void setBlockID(int id) { blockID = id; }
	private void setComment(String text) { comment = text; }
	
	// TODO: include complaint & container name
	public String toString() { 
		return "Complaint " + getID() + " with session " + getSessionID()
				+ " from " + getMSISDN() + ": " +  "\"" +
				getComment() + "\" @" + getCreated(); 
	}
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			Complaint complaint;
			try { complaint = (Complaint) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == complaint.getID()) { return true; }
	        return false;
		}
	}
	
}