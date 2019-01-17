package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import objects.User;



public class Server {
	private int port = 5555;
	private ServerSocket socket = null;
	
	private final static Logger log = Utils.createLogger(Server.class.getSimpleName());
	static Vector<ClientHandler> clients = new Vector<>(); 
	
	public static void main(String[] args) {
		try {
			Datenbank.loadConfig();
			Datenbank.connect();
			//Datenbank.resetUserStatus()
			
			Server server = new Server(args);
			server.start();
			server.listen();
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			//log.severe(e.getMessage());
		}
	}
	
	public Server(String[] params) {
		if (params.length > 0) {
			 this.port = Integer.parseInt(params[0]);
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

			ObjectOutputStream toClient = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream fromClient = new ObjectInputStream(clientSocket.getInputStream()); 
            
			log.fine("Erstelle neuen Handler für den Client....");
			ClientHandler handler = new ClientHandler(clientSocket, fromClient, toClient);
			
			
			Thread thread = new Thread(handler); 
			clients.add(handler);
			
			thread.start();
		}
	}
}
