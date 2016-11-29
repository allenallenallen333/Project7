package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class ServerMain extends Observable {

	public static void main(String[] args) {
		Userlist.readFromDatabase();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run(){
				// Save user database
				System.out.println("SHUT DOWN");
				Userlist.writeToFile();
			}
		}));
		
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());			
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("Someone connected");
		}
	}
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		// Our code
		private Socket sock;
		private User user;
		private int index;
		// Our code end
		
		public ClientHandler(Socket clientSocket) {
			// Our code
			// user = Userlist.getUser(clientSocket);
			// Our code end
			
			setChanged();
			// notifyObservers(user.name + " has joined the chat");
			
			sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private boolean canRun = true;
		
		public void stop(){
			canRun = false;
		}

		public void run() {
			if (canRun){
				
				String message;
				try {
					while ((message = reader.readLine()) != null) {
						// System.out.println("Server read from " + user.clientId + " : " + message);
						
						System.out.println("server read: " + message);
						
						PrintWriter thisWriter = new PrintWriter(sock.getOutputStream());
						
						if (message.startsWith("/login ")){
							
							String str = message.substring(7, message.length());
							String[] sArray = str.split(":");
							String username = sArray[sArray.length - 2];
							String password = sArray[sArray.length - 1];
							
							User u = Userlist.getUser(username);
							
							if (u == null){
								thisWriter.println("wrong username");
								thisWriter.flush();
								System.out.println("Wrong username");
								stop();
							}
							else{
								if (!u.password.equals(password)){
									thisWriter.println("wrong password");
									thisWriter.flush();
									System.out.println("Wrong password");
									stop();
								}
								else{
									int id = Userlist.getIndex(username);
									index = id;
									Userlist.Users.get(id).socket = sock;
									user = Userlist.Users.get(id);
									thisWriter.println("login success");
									thisWriter.flush();
									System.out.println("Successfully logged in");
								}
							}
						}
						else if (message.startsWith("/signup ")){
							
							String str = message.substring(8, message.length());
							String[] sArray = str.split(":");
							String username = sArray[sArray.length - 2];
							String password = sArray[sArray.length - 1];
							
							User u = Userlist.getUser(username);
							
							if (u != null){
								thisWriter.println("username already exists");
								thisWriter.flush();
								System.out.println("username already exists");
								stop();
							}
							else{

								Userlist.addUser(username, password);
								int id = Userlist.getIndex(username);
								index = id;
								Userlist.Users.get(id).socket = sock;
								user = Userlist.Users.get(id);
								thisWriter.println("signup success");
								thisWriter.flush();
								System.out.println("Successfully signed up and logged in");
							}
						}
						else if (message.startsWith("/chat ")){
							
							int charId = 6;
							while(message.charAt(charId) != ' '){
								charId++;
							}
							
							String str = message.substring("/chat ".length(), charId);
							String[] sArray = str.split(":");
							
							String msg = message.substring(charId + 1, message.length());
							
							User sender = Userlist.getUser(sock);
							
							
							
							String toUsers = "";
							for(int i = 0; i < sArray.length; i++){
								if (!sArray[i].isEmpty()){
									toUsers += sArray[i] + ", ";
								}
							}
							toUsers = toUsers.substring(0, toUsers.length() - 2);
							
							ClientObserver w = new ClientObserver(sock.getOutputStream());
							w.println("[PM to " + toUsers + "] " + sender.username + ": " + msg);
							w.flush();
							
							
							for(int i = 0; i < sArray.length; i++){
								if (!sArray[i].isEmpty()){
									User u = Userlist.getUser(sArray[i]);
									
									ClientObserver writer = new ClientObserver(u.socket.getOutputStream());
									writer.println("[PM to " + toUsers + "] " + sender.username + ": " + msg);
									writer.flush();
								}
							}
							
							
						}
						else{
							setChanged();
							notifyObservers(user.username + ": " + message);
						}
						
					}
				} catch (Exception e) {
					Userlist.Users.get(index).socket = null;
					System.out.println(user.username + " has disconnected");
					stop();
				}
			}
			
			
		}
	}
}
