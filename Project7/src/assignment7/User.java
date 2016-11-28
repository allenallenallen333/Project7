package assignment7;

import java.net.Socket;

public class User {
	public Socket socket;
	public int clientId;
	public String name = "Anon";

	public User(Socket sock) {
		this.socket = sock;
		this.clientId = Userlist.ids;
		Userlist.ids++;
	}
	
	public void setName(String name){
		this.name = name;
	}
}