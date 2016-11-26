package assignment7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
	
	static int port = 8000; 
	public static boolean closed = false;
	public static Socket clientSocket = null;
	public static DataInputStream inS = null;
	public static PrintStream outS = null;
	public static DataInputStream inputLine = null;
	
	public static void main(String[] args) {

	   

	    
	    try {
	      clientSocket = new Socket("host", port);
	      outS = new PrintStream(clientSocket.getOutputStream());
	      inS = new DataInputStream(clientSocket.getInputStream());
	      inputLine = new DataInputStream(new BufferedInputStream(System.in));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }

	   
	    if (clientSocket != null && outS != null && inS != null) {
	      try {

	       
	    	  new Thread(new Client()).start();
	          while (!closed) {
	            outS.println(inputLine.readLine().trim());
	          }

	        outS.close();
	        inS.close();
	        clientSocket.close();
	      } catch (IOException e) {
	    	 e.printStackTrace();
	      }
	    }
	  }


	@Override
	public void run() {
		String response;
		System.out.print("Type 'Exit' to leave chat");
		try {
			while ((response = inS.readLine()) != null) {
				System.out.println(response);
		        if (response.indexOf("Exit") != -1)
		          break;
			}
		    closed = true;
		}
		catch (IOException e) {
			System.err.println("IOException:  " + e);
			}
		
	}
	
	
	
	

}
