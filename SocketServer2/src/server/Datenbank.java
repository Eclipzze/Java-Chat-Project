package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;


import objects.Raum;
import objects.User;

public class Datenbank {
	private static String ip;
	private static String dbname;
	private static String user;
	private static String pw;
	
	private static Connection con;
	private static Statement myStatement;
	private static ResultSet myRS;
	
	private final static Logger log = Utils.createLogger(Datenbank.class.getSimpleName());

	
	public static void loadConfig() throws IOException{
		log.info("Load Config");
		Properties prop = new Properties();
		InputStream inputStream = new FileInputStream("config.txt");
		prop.load(inputStream);
		
		ip = prop.getProperty("ip");
		dbname = prop.getProperty("dbname");
		user = prop.getProperty("dbusername");
		pw = prop.getProperty("dbpasswort");
		inputStream.close();
	}
	
	public static void connect() throws SQLException {
		log.info("Verbindung herstellen");
		String path = "jdbc:mysql://"+ ip + "/" + dbname + "?serverTimezone=UTC";
		con = DriverManager.getConnection(path, user, pw);
		log.info("Verbindung erfolgreich hergestellt");
	}
	
	public static void login(User user) {
		try {
			PreparedStatement myStatement = con.prepareStatement("SELECT idBenutzer From benutzer WHERE username=? and password=?"); //password=SHA2(?, 256)");
			myStatement.setString(1, user.getUsername());
			myStatement.setString(2, user.getPassword());
			myRS=myStatement.executeQuery();
			if (myRS.next()) {
				user.setId(myRS.getInt(1));
				user.setLoggedIn(true);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public static List<Raum> getInRooms(User user) {
		List<Raum> raume= new ArrayList<Raum>();
		try {
			PreparedStatement myStatement = con.prepareStatement("SELECT idRaum, raum.name, typ FROM benutzer_in_raum JOIN raum ON raum_idRaum=raum.idRaum JOIN benutzer ON benutzer_idBenutzer=benutzer.idBenutzer WHERE benutzer_idBenutzer=?");
			myStatement.setInt(1, user.getId());
			myRS=myStatement.executeQuery();
			
			while(myRS.next()) {
				Raum r = new Raum();
				r.setId(myRS.getInt(1));
				r.setName(myRS.getString(2));
				r.setTyp(myRS.getString(3));
				
				raume.add(r);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return raume;
	}
	
	
	public static List<User> getOtherUsers(User user) {
		List<User> users = new ArrayList<User>();
		try {
			PreparedStatement myStatement = con.prepareStatement("SELECT idBenutzer, username, status, lastonline FROM benutzer WHERE idBenutzer !=?");
			myStatement.setInt(1, user.getId());
			myRS=myStatement.executeQuery();
			
			while(myRS.next()) {
				User u = new User();
				u.setId(myRS.getInt(1));
				u.setUsername(myRS.getString(2));
				u.setStatus(myRS.getString(3));
				u.setLastLogin(myRS.getDate(4));
				users.add(u);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return users;
	}
	
	public static List<Raum> getNotInRooms(User user){
		List<Raum> raume = new ArrayList<Raum>();
		try {
			PreparedStatement myStatement = con.prepareStatement("SELECT idRaum, raum.name, typ FROM raum WHERE typ=\"öffentlich\" AND idRaum NOT IN (SELECT raum_idRaum FROM benutzer_in_raum WHERE benutzer_idBenutzer=?)");
			myStatement.setInt(1, user.getId());
			myRS=myStatement.executeQuery();
			
			while(myRS.next()) {
				Raum r = new Raum();
				r.setId(myRS.getInt(1));
				r.setName(myRS.getString(2));
				r.setTyp(myRS.getString(3));
				
				raume.add(r);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return raume;
	}
}
