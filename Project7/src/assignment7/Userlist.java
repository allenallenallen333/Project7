package assignment7;

import java.net.Socket;
import java.util.ArrayList;


public class Userlist {

	public static ArrayList<User> Users = new ArrayList<>();
	public static int ids = 0;
	
	public static User getUser(Socket sock){
		int i = 0;
		while(!Users.get(i).socket.equals(sock)){
			i++;
		}
		
		return Users.get(i);
	}
}
