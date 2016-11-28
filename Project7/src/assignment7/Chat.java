package assignment7;


import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument.Content;

import assignment7.Chat.IncomingReader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chat extends Application{
	
	private VBox chat = new VBox();
	private GridPane grid = new GridPane();
	private TextArea incoming;
	private TextField outgoing;
	
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	
	public void run(String[] hello) throws Exception {
		launch(hello);
		setUpNetworking();
	}
	
	public void start(Stage primaryStage) {
		
		Scene scene = new Scene(grid);
		primaryStage.setScene(scene);
		primaryStage.show();
		incoming.setEditable(false);
		ScrollPane scrollbar = new ScrollPane(incoming);
		outgoing = new TextField();
		
		
	
		Button send = new Button("Send");
		
		
		/*
		 * 1. find out how to keep printing messages line by line
		 * 2. figure out how to connect the thread and shit so that user name prints out with message
		 * 		ex: user193 : hello!
		 * 3. on the right hand side have a list of users that are currently in the chat room 
		 * 4. create log-in window
		 * 
		 */
		send.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String message = outgoing.getText();
				incoming.setText(message);
			}
		});
		
		grid.add(incoming, 1, 1);
		grid.add(outgoing, 1, 2);
		grid.add(send, 2, 2);
	
	}
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		IncomingReader a = new IncomingReader();
		Thread readerThread = new Thread(a);
		readerThread.start();
	}

	/*class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}*/

	public static void main(String[] args) {
		try {
			new Chat().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
						incoming.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
		
}
