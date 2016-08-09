package scode.ntxuva.model;

public class ComplaintType {
	
	private int id;
	private String name;
	
	public ComplaintType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID() { return id; }
	public String getName() { return name; }
	
	public String toString() { return getID() + ". " + getName(); }
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			ComplaintType complaintType;
			try { complaintType = (ComplaintType) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == complaintType.getID()) { return true; }
	        return false;
		}
	}
	
}