package objects;

import java.io.Serializable;

public class Raum implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2944724487243227728L;
	private int id;
	private String name;
	private String typ;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	@Override
	public String toString() {
		return "Raum [id=" + id + ", name=" + name + ", typ=" + typ + "]";
	}
}
