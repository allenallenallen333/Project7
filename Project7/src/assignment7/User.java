package assignment7;

import java.net.Socket;

public class User {
	public Socket socket;
	public String username;
	public String password;

	public User(String username, String password){
		this.username = username;
		this.password = password;
		this.socket = null;
	}
	
}