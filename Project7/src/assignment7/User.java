package assignment7;

import java.net.Socket;

public class User {
	public Socket socket;
	public int clientId;

	public User(Socket sock) {
		this.socket = sock;
		this.clientId = Userlist.ids;
		Userlist.ids++;
	}
}