package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Logger;


import objects.Message;
import objects.User;

public class Client {
	private String serverIP = "localhost";
	private int serverPort = 5555;
	private Socket socket = null;
	private User user = null;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Queue<Object> sendQueue = new LinkedList<>(); 
	
	private final static Logger log = Utils.createLogger(Client.class.getName());

	
	public static void main(String[] args) {
		try {
			Client client = new Client("hans");
			client.connectToServer();
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
	}
	
	public Client(String name) {
		user = new User();
		user.setUsername(name);
		user.setLoggedIn(false);
		
		
	}
	
	public void connectToServer() throws IOException {
		log.info("Verbinde zu " + this.serverIP);
		socket = new Socket(this.serverIP, this.serverPort);

		toServer= new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream()); 
		

		Thread sendMessage = new Thread(new Runnable() { 
			@Override
			public void run() {
				try {
					//Authentifiziere den Client
					toServer.writeObject(user);
					
					//Warte auf eine Anwort
					synchronized (Thread.currentThread()) {
						Thread.currentThread().wait();
					}				

					/*
					while (sendQueue.peek() != null) {
						toServer.wrt
						
						
					}
					*/
				
					//Thread.currentThread().wait();
					

					
					Message msg = new Message();
					msg.setText("helloe");
					msg.setAuthor(user);
					
					toServer.writeObject(msg);
					

				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					log.severe(e1.getMessage());
				}
				
				
				/*
				while (true) { 
					Game msg = new Game();
					msg.setStatus("start");
					
					try {
						toServer.writeObject(msg);
					} catch (IOException e) {
						log.severe(e.getMessage());
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					
					break;
				} 
				*/
			} 
		}); 
		  
		// readMessage thread 
		Thread readMessage = new Thread(new Runnable()  
		{ 
			@Override
			public void run() { 
				while (true) { 
					try { 
						Object obj = fromServer.readObject();
						if (obj instanceof User) {
							user = (User) obj;
							if (user.getLoggedIn()) {
								log.info("Erfolgreich eingeloggt");
							}

							synchronized (sendMessage) {
								sendMessage.notify();
							}
						}
						else if(obj instanceof Message) {
							log.info("msg");
						}	   
						
					} catch (SocketException e) {
						log.info("Verbindung verloren");
						break;
						
						
					} catch (IOException | ClassNotFoundException e) { 
  
						e.printStackTrace(); 
					} 
				} 
			} 
		}); 
  
		sendMessage.start(); 
		readMessage.start(); 
		
	}
		

}
