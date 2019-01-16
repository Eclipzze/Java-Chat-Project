package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Logger;


import objects.Message;
import objects.User;

public class Client  implements Runnable {
	private String serverIP = "localhost";
	private int serverPort = 5555;
	private Socket socket = null;
	private User user = null;
	
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Queue<Message> sendQueue = new LinkedList<>(); 
	
	
	private final static Logger log = Utils.createLogger(Client.class.getName());
	
	
	public static void main(String[] args) {
		try {
			User user = new User();
			user.setUsername("hans");
			user.setPassword("1234");
			user.setLoggedIn(false);
			
			Client client = new Client(user);
			client.connect();
			user = client.authenticateUser(user);
			
			if (user.getLoggedIn()) {
				log.info("Erfolgreich Authentifiziert");
			}
			else{
				log.info("Authentifizierung fehlgeschlagen");
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			log.severe(e.getMessage());
		}
	}
	
	public void addMessage(Message msg) {
		sendQueue.add(msg);
	}
	
	public Client(User user) {
		this.user = user;
		log.info("Construktot " + Thread.currentThread().getName());
	}
	
	public void connect() throws UnknownHostException, IOException {
		log.info("Verbinde zu " + this.serverIP);
		socket = new Socket(this.serverIP, this.serverPort);

		toServer= new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream()); 
	}	
	
	public User authenticateUser(User user) throws IOException, ClassNotFoundException{
		log.info("Authentifiziere " + user.getUsername());
		toServer.writeObject(user);
		return (User) fromServer.readObject();
	}


	public void startSendingThread() {
		Thread sendMessage = new Thread(new Runnable() { 
			@Override
			public void run() {
				log.info("Sending Thread: " + Thread.currentThread().getName());
				while (true) {
					try {
						while (sendQueue.peek() != null) {
							toServer.writeObject(sendQueue.poll());
						}

						//Lassen den Thread wieder schlafen
						synchronized (Thread.currentThread()) {
							Thread.currentThread().wait();
						}				
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						log.severe(e.getMessage());
					}				
				}
			}
		});
	}
	
	public Thread startReadingThread() {
		Thread readMessage = new Thread(new Runnable() { 
			@Override
			public void run() { 
				log.info("Reading Thread: " + Thread.currentThread().getName());
				while (true) { 
					try { 
						Object obj = fromServer.readObject();
						if (obj instanceof User) {
							user = (User) obj;
							if (user.getLoggedIn()) {
								log.info("Erfolgreich eingeloggt");
							}
							/*
							synchronized (sendMessage) {
								sendMessage.notify();
							}
							*/
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
		readMessage.start();
		return readMessage;
	}

	@Override
	public void run() {
		try {
			connect();
			log.info("run " + Thread.currentThread().getName());
			authenticateUser(user);
			if (user.getLoggedIn()) {
				Thread a =  startReadingThread();
				while (true) {
					try {
						while (sendQueue.peek() != null) {
							toServer.writeObject(sendQueue.poll());
						}

						//Lassen den Thread wieder schlafen
						synchronized (Thread.currentThread()) {
							Thread.currentThread().wait();
						}				
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						log.severe(e.getMessage());
					}				
				} 
			}	
			else {
				log.info(user.getUsername() + " konnte nicht authentifiziert werden");
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
