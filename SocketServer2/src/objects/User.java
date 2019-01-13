package objects;
import java.io.Serializable;


public class User implements Serializable  {
	private static final long serialVersionUID = 8453218440993040487L;
	private String name;
	private String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
