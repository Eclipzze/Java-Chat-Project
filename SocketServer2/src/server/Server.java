package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import objects.User;



public class Server {
	private int port = 5555;
	private ServerSocket socket = null;
	private final static Logger log = Utils.createLogger(Server.class.getName());
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
			
			server.listen();
			
		} catch (IOException | ClassNotFoundException e) {
			log.severe(e.getMessage());
		}
	}
	
	
	public void start() throws IOException {
		socket = new ServerSocket(this.port);
		log.info("Server erfolgreich gestartet");
	}
	
	public void listen() throws IOException, ClassNotFoundException {
		log.info("Lausche auf Port:" + String.valueOf(this.port));
		while (true) {
			Socket clientSocket = socket.accept();
			log.info("Verbindungsaufbau von " + clientSocket.getInetAddress().toString());

			log.fine("User Authentification");
			ObjectInputStream fromClient  = new ObjectInputStream(clientSocket.getInputStream());

			User user = (User)fromClient.readObject();
			log.info("Verbindung erfolgreich aufgebaut von " + user.getName());
		}
		
		
	}
}
