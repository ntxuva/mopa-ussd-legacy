package scode.ntxuva.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class USSDTransaction {
	
	private long id, sessionID, sender, receiver;
	private String isContinuation, action, text;
	private Date created;	
	private SimpleDateFormat iso8601Formater;
	
	
	private USSDTransaction(long id, long sessionID, long sender, long receiver,
			String isContinuation, String action, String text) {
		this.id = id;
		this.sessionID = sessionID;
		this.sender = sender;
		this.receiver = receiver;
		this.isContinuation = isContinuation;
		this.action = action;
		this.text = text;
		iso8601Formater = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
	}

	// constructor for "created" as a Date (ususally for USSD Response)
	public USSDTransaction(long id, long sessionID, long sender, long receiver,
			String isContinuation, String action, String text, Date created) {
		this(id, sessionID, sender, receiver, isContinuation, action, text);
		setCreated(created);
	}

	// constructor for "created" as a Date (ususally for USSD Request)
	public USSDTransaction(long id, long sessionID, long sender, long receiver,
			String isContinuation, String action, String text, String created) {
		this(id, sessionID, sender, receiver, isContinuation, action, text);
		Date now = new Date();
		try {
			now = iso8601Formater.parse(created);
		} catch (ParseException pe) {
			//logger.severe("Failed to populateList of bairro from resultset due to SQLException");
			pe.printStackTrace();
		} 
		setCreated(now);
	}
	
	// constructor for USSD Request
	public USSDTransaction(long id, long sessionID, long sender, long receiver,
			String isContinuation, String text, Date created) {
		this(id, sessionID, sender, receiver, isContinuation, "", text, created);
	}
	
	// constructor for USSD Response
	public USSDTransaction(long id, long sessionID, long sender, long receiver,
			String action, String text) {
		this(id, sessionID, sender, receiver, "true", action, text, new Date());
	}
	
	public long getID() { return id; }
	public long getSessionID() { return sessionID; }
	public long getSender() { return sender; }
	public long getReceiver() { return receiver; }
	public String getIsContinuation() { return isContinuation; }
	public String getAction() { return action; }
	public String getText() { return text; }
	public Date getCreated() { return created; }
	public String getCreatedStr() { return iso8601Formater.format(created); }
	
	// hack to get SimpleDateFormater working
	private void setCreated(Date created) { this.created = created; }
	
	public String toString() { 
		return "USSD transaction " + getID() + " in session " + getSessionID()
				+ ": " + getSender() + " -> " + getReceiver() + ": \"" +
				getText() + "\" @" + getCreated(); 
	}
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			USSDTransaction transaction;
			try { transaction = (USSDTransaction) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == transaction.getID()) { return true; }
	        return false;
		}
	}
	
}