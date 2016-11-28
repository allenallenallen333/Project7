package assignment7;

<<<<<<< HEAD
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	int port = 8000;
	String host = "localhost";
	DataInputStream in;
	DataOutputStream out;
	Socket socket;
	
	
	void runme () {
		Scanner sc = new Scanner(System.in);
		
		try {
			// Define client socket, and initialize in and out streams.
			socket = new Socket(host, port);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			Double msg;
			while (true) {
				try {
					// ask user to enter a double
					System.out.print("Enter a number to send to server: ");
					msg = sc.nextDouble();
				} catch (Exception e) {
					System.out.println("Try again.");
					continue;
				}
				
				// send the double to the server
				out.writeDouble(msg);
				out.flush();
				
				// read the server's response, and print it out.
				System.out.println("Client: The server says the square is: " + 
						in.readDouble());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
=======
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
	
	
	
	

>>>>>>> b641b8d02c8248813c5ad1ca73d8caa40769a0c6
}
