package scode.ntxuva.model;

public class Container {
	
	private int id, bairroID;
	private String name;
	private Double latitude, longitude;
	
	public Container(int id, String name, Double latitude, 
			Double longitude, int bairroID) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.bairroID = bairroID;
	}
	
	public int getID() { return id; }
	public String getName() { return name; }
	public Double getLatitude() { return latitude; }
	public Double getLongitude() { return longitude; }
	public int getBairroID() { return bairroID; }
	
	public String toString() { return ( getID() % 10 ) + ". " + getName(); }
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			Container container;
			try { container = (Container) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == container.getID()) { return true; }
	        return false;
		}
	}
	
}