package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import objects.User;

public class Client {
	private String serverIP = "localhost";
	private int serverPort = 5555;
	private Socket socket = null;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	
	private final static Logger log = Utils.createLogger(Client.class.getName());

	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.connectToServer();
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		
		
	}
	
	public void connectToServer() throws IOException {
		log.info("Verbinde zu " + this.serverIP);
		socket = new Socket(this.serverIP, this.serverPort);

		User user = new User();
		user.setName("Hans");
		
        toServer = new ObjectOutputStream(socket.getOutputStream());
        toServer.writeObject(user);
        log.info("Verbindung hat geklapp");
        socket.close();
	}
		

}
