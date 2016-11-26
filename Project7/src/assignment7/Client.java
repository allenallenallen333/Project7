package assignment7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
	
	static int port = 8100; 

	
	public static void main(String[] args) {

	    Socket clientSocket = null;
	    DataInputStream inS = null;
	    PrintStream outS = null;
	    DataInputStream inputLine = null;

	    
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

	       
	        System.out.println("Type 'Exit' to leave.");
	        String responseLine;
	        outS.println(inputLine.readLine());
	        while ((responseLine = inS.readLine()) != null) {
	          System.out.println(responseLine);
	          if (responseLine.indexOf("Exit") != -1) {
	            break;
	          }
	          outS.println(inputLine.readLine());
	        }

	        outS.close();
	        inS.close();
	        clientSocket.close();
	      } catch (IOException e) {
	    	 e.printStackTrace();
	      }
	    }
	  }
	

}
