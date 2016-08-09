package scode.ntxuva.model;

import java.util.Date;

public class USSDSession {
	
	private long id, MSISDN;
	private int mainMenu, complaintType, bairro, block, container;
	private String comment;
	private Date created, ended;
	
	public USSDSession(long id, long MSISDN) {
		this.id = id;
		this.MSISDN = MSISDN;
		created = new Date();
	}
	
	public USSDSession(long id, long MSISDN, int mainMenu, int complaintType,
			int bairro, int block, int container, String comment,
			Date created, Date ended) {
		this.id = id;
		this.MSISDN = MSISDN;
		this.mainMenu = mainMenu;
		this.complaintType = complaintType;
		this.bairro = bairro;
		this.block = block;
		this.container = container;
		this.comment = comment;
		this.created = created;
		this.ended = ended;
	}
	
	public long getID() { return id; }
	public long getMSISDN() { return MSISDN; }
	public Date getCreated() { return created; }
	
	public int getMainMenu() { return mainMenu; }
	public void setMainMenu(int mainMenu) { this.mainMenu = mainMenu; }
	
	public int getComplaintType() { return complaintType; }
	public void setComplaintType(int complaintType) { this.complaintType = complaintType; }
	
	public int getBairro() { return bairro; }
	public void setBairro(int bairro) { this.bairro = bairro; }
	
	public int getBlock() { return block; }
	public void setBlock(int block) { this.block = block; }
	
	public int getContainer() { return container; }
	public void setContainer(int container) { this.container = container; }
	
	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }
	
	public Date getEnded() { return ended; }
	public void setEnded(Date ended) { this.ended = ended; }
	
	
	public String toString() { 
		return "USSD session " + getID() + ": " + getMSISDN()
				+ " created @" + getCreated(); 
	}
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			USSDSession session;
			try { session = (USSDSession) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == session.getID()) { return true; }
	        return false;
		}
	}
	
}