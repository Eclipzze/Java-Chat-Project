package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import objects.Message;
import objects.Raum;
import objects.User;


public class ClientHandler implements Runnable {
	private Socket socket;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private User user;
	private final static Logger log = Utils.createLogger(ClientHandler.class.getSimpleName());
	
	public ClientHandler(Socket socket, ObjectInputStream fromClient,  ObjectOutputStream toClient) {
		this.socket = socket;
		this.fromClient = fromClient;
		this.toClient = toClient;
	}
	
	
	@Override
	public void run() {
		while (true) { 
			try {
				Object obj = fromClient.readObject();
				log.fine("Neues Object empfangen");
				if (obj instanceof User){
					user = (User) obj;
					if (!user.getLoggedIn()){
						log.info("Authentifiziere " + user.getUsername());
						Datenbank.login(user);
						
						//Sende Antwort
						toClient.writeObject(user);
						
						if (user.getLoggedIn()) {
							log.info(user.getUsername() + " erfolgreich authentifiziert");
							
							
							List<User> users = Datenbank.getOtherUsers(user);
							List<Raum> inRooms = Datenbank.getInRooms(user);
							List<Raum> notInRooms = Datenbank.getNotInRooms(user);
							

							//toClient.writeObject(users);
						}else {
							log.info(user.getUsername() + " konnte nicht authentifiziert werden");
						}
					}			
				}
				else if(obj instanceof Message) {
					log.info(obj.toString());
				}
				else {
					log.info(obj.getClass().getName());
				}
			} catch (SocketException e) {
				log.fine("Verbindung verloren");
				break;
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				log.info(e.getMessage());
				
			}
		}		
	}
}
