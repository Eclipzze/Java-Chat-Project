package objects;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable  {
	private static final long serialVersionUID = 8453218440993040487L;
	private String name;
	private String username;
	private String password;
	private String status;
	private Date lastLogin;
	private BufferedImage profilImage;
	private Boolean loggedIn;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public BufferedImage getProfilImage() {
		return profilImage;
	}
	
	public void setProfilImage(BufferedImage profilImage) {
		this.profilImage = profilImage;
	}
	
	public Boolean getLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
