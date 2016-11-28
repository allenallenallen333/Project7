package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	
	

	/**
	 * Reads the user database text file and add the users into an ArrayList
	 */
	public static void readFromDatabase(){
		String path = "./db.txt";
		File dbFile = new File(path);
		
		try {
			if (!dbFile.createNewFile()){
				// Did not successfully created blank file / Already has file
				BufferedReader br = new BufferedReader(new FileReader(path));
				
				String sCurrentLine = br.readLine();
				
				while (sCurrentLine != null) {
					
					if (!sCurrentLine.isEmpty()){
						String[] arr = sCurrentLine.split(":");
						
						User user = new User(arr[arr.length - 2], arr[arr.length - 1]);
						
						Users.add(user);
					}
					sCurrentLine = br.readLine();
				}
			}
		} catch (IOException e) {
		}
	}
	
	
	/**
	 * Save all current users to text file
	 */
	public static void writeToFile(){
		String path = "./db.txt";

		try  
		{
			PrintWriter w = new PrintWriter(path);
			
		    
		    for (User u : Users){
		    	w.println(u.username + ":" + u.password);
		    }
		    
		    w.close();
		}
		catch (IOException e)
		{
		    System.err.println("Error: " + e.getMessage());
		}

	}
}
