package assignment7;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import assignment7.ChatClient.IncomingReader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chat extends Application{
	
	private Group group = new Group();
	private TextArea incoming;
	private TextField outgoing;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	
	public void run(String[] hello) throws Exception {
		setUpNetworking();
		launch();  		
	}
	
	public void start(Stage primaryStage) {
		HBox hb = new HBox();
		VBox vb = new VBox();
		
		Scene scene = new Scene(group);
		
		incoming = new TextArea();
		incoming.setEditable(false);
		incoming.setWrapText(true);
		
		ScrollPane scrollbar = new ScrollPane(incoming);
		outgoing = new TextField();
	    outgoing.setPrefWidth(500);
		outgoing.setPromptText("Enter Your Message Here");
		
		/*
		 * 1. find out how to keep printing messages line by line
		 * 2. figure out how to connect the thread and shit so that user name prints out with message
		 * 		ex: user193 : hello!
		 * 3. on the right hand side have a list of users that are currently in the chat room 
		 * 4. create log-in window
		 * 
		 */
		Button send = new Button("Send");
		send.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {	
				
				if (outgoing.getText() != null && !outgoing.getText().isEmpty()){
					writer.println(outgoing.getText());
					writer.flush();
					outgoing.setText("");
					outgoing.requestFocus();
				}
			}
		});
		
		
		hb.setSpacing(5);
	    hb.setPadding(new Insets(10, 0, 0, 10));
	    hb.getChildren().addAll(outgoing, send);

		
	    vb.setSpacing(5);
	    vb.setPadding(new Insets(10, 0, 0, 10));
	    vb.getChildren().addAll(incoming, hb);
		
		group.getChildren().addAll(vb);
	    
		primaryStage.setScene(scene);
		primaryStage.setWidth(600);
		primaryStage.setHeight(350);
		primaryStage.show();
	
	}
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("Connected to server");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

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
