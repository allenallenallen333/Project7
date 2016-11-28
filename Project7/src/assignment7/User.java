package assignment7;

import java.net.Socket;

public class User {
	public Socket socket;
	public int clientId;
	public String name = "";
	public String username;
	public String password;

	public User(String username, String password){
		this.username = username;
		this.password = password;
		this.socket = null;
	}
	
	
	public User(Socket sock) {
		this.socket = sock;
		this.clientId = Userlist.ids;
		Userlist.ids++;
	}
	
	public void setName(String name){
		this.name = name;
	}
}