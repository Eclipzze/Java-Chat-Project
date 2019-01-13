package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import objects.Message;
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

		toServer= new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream()); 
		
		
		Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
                	Message msg = new Message();
                	msg.setText("hello");
                	
                	try {
						toServer.writeObject(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                	break;
                } 
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
                    		log.info("user");
                    	}
            			else if(obj instanceof Message) {
            				log.info("msg");
            			}
                    	log.info(obj.getClass().getName());
                    } catch (IOException | ClassNotFoundException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
        
        
        
		
        /*
		
		User user = new User();
		user.setName("Hans");
		
        //toServer = new ObjectOutputStream(socket.getOutputStream());
        toServer.writeObject(user);
        log.info("Verbindung hat geklapp");
        */
		/*
        fromServer.close();
		toServer.close();
		
		socket.close();
		*/
	}
		

}
