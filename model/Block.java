package scode.ntxuva.model;

public class Block {
	
	private int id, bairroID;
	private Double latitude, longitude;
	
	public Block(int id, Double latitude, Double longitude, int bairroID) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.bairroID = bairroID;
	}
	
	public int getID() { return id; }
	public Double getLatitude() { return latitude; }
	public Double getLongitude() { return longitude; }
	public int getBairroID() { return bairroID; }
	
	public String toString() { return ( "Quarteirao " + getID() % 1000 ) ; }
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			Block block;
			try { block = (Block) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == block.getID()) { return true; }
	        return false;
		}
	}
	
}