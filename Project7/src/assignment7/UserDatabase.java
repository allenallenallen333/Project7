package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserDatabase {
	
	public static ArrayList<User> Users = new ArrayList<>();
	
	class User{
		String username;
		String password;
		
		public User(String username, String password){
			this.username = username;
			this.password = password;
		}
	}

	/**
	 * Reads the user database text file and add the users into an ArrayList
	 */
	public void readFromDatabase(){
		String path = "./db.txt";
		File dbFile = new File(path);
		
		try {
			if (!dbFile.createNewFile()){
				// Did not successfully created blank file / Already has file
				BufferedReader br = new BufferedReader(new FileReader(path));
				
				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					String[] arr = sCurrentLine.split(",");
					
					User user = new User(arr[arr.length - 2], arr[arr.length - 1]);
					
					Users.add(user);
				}
			}
		} catch (IOException e) {
		}
	}
	
	/**
	 * Adds a single user to the text file
	 * @param username
	 * @param password
	 */
	public void writeToDatabase(String username, String password){
		String path = "./db.txt";
		
		BufferedWriter out = null;
		
		try  
		{
		    FileWriter fstream = new FileWriter(path, true); //true tells to append data.
		    out = new BufferedWriter(fstream);
		    out.write("\n" + username + ":" + password);
		    out.close();
		}
		catch (IOException e)
		{
		    System.err.println("Error: " + e.getMessage());
		}

	}

}
