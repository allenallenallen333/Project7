package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class ChatServer extends Observable {
	public static void main(String[] args) {
		try {
			new ChatServer().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			// Our code
			Userlist.Users.add(new User(clientSocket));
			// Our code end
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
		private User user;
		// Our code end
		
		public ClientHandler(Socket clientSocket) {
			// Our code
			user = Userlist.getUser(clientSocket);
			// Our code end
			
			Socket sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("Server read from " + user.clientId + " : " + message);
					
					if (message.startsWith("/name ")){
						user.setName(message.substring(6, message.length()));
					}
					else{
						setChanged();
						notifyObservers(user.name + ": " + message);
					}
					
					
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
