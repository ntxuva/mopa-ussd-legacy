package scode.ntxuva.model;

public class Bairro {
	
	private int id;
	private String name;
	
	public Bairro(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID() { return id; }
	public String getName() { return name; }
	
	public String toString() { return getID() + ". " + getName(); }
		
	public boolean equals(Object obj) {
		if (obj == null) { return false; } 
		else {
			Bairro bairro;
			try { bairro = (Bairro) obj; }
	        catch (ClassCastException e) { return false; }
	        if (this.id == bairro.getID()) { return true; }
	        return false;
		}
	}	
	
}