package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import objects.Message;
import objects.User;

public class ClientHandler implements Runnable {
	private Socket socket;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private final static Logger log = Utils.createLogger(ClientHandler.class.getName());
	
	public ClientHandler(Socket socket, ObjectInputStream fromClient,  ObjectOutputStream toClient) {
		this.socket = socket;
		this.fromClient = fromClient;
		this.toClient = toClient;
	}
	
	
	@Override
	public void run() {
		while (true)  
        { 
			try {
				Object obj = fromClient.readObject();
				log.info("Neue erhalten");
				if (obj instanceof User){
					log.info(obj.toString());
				}
				else if(obj instanceof Message) {
					log.info(obj.toString());
				}
				
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			log.info("Thread run");
        }
		// TODO Auto-generated method stub
		
	}

}
