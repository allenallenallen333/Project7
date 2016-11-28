package assignment7;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
	private GridPane grid = new GridPane();
	private TextArea incoming;
	private TextField outgoing;

	public Stage primary;
	public Stage logIn;
	private static BufferedReader reader;
	private static PrintWriter writer;
	private String server;
	private String user;
	private DataOutputStream output = null;
	private DataInputStream input = null;
	
	public void start(Stage primaryStage) {
		primary = primaryStage;
		logIn = new Stage();
		HBox hb = new HBox();
		VBox vb = new VBox();
		logIn.setTitle("Log into Chat!");
		Scene scene = new Scene(group);
		
		
		
		TextField username = new TextField();
		Label ID = new Label("UserName: ");
		TextField serverID = new TextField();
		Label serverIDL = new Label("Server IP: ");
		username.setPromptText("Type in a username");
		Button submit = new Button("Enter Chat!");
		grid.add(ID, 1, 1);
		grid.add(username, 3, 1);
		grid.add(serverIDL, 1, 2);
		grid.add(serverID, 3, 2);
		grid.add(submit, 1, 3);
		
		Scene sceneLog = new Scene(grid, 300, 100);
		logIn.setScene(sceneLog);
		
		logIn.show();
		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				//CHECK USERNAME
				user = username.getText();
				server = serverID.getText();
				//IF USERNAME EXISTS THEN DO THIS:
				
				
				logIn.close();
				primary.setScene(scene);
				primary.setWidth(600);
				primary.setHeight(350);
				primary.show();
			
				
				@SuppressWarnings("resource")
				Socket sock;
				try {
					sock = new Socket(server, 4242);
					InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(streamReader);
					writer = new PrintWriter(sock.getOutputStream());
					System.out.println("networking established");
					
					input = new DataInputStream(sock.getInputStream());
			        output = new DataOutputStream(sock.getOutputStream());
			        Thread readerThread = new Thread(new IncomingReader());
					readerThread.start();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		
		
		
		
		
		
	
		
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
				try {

					if (outgoing.getText() != null && !outgoing.getText().isEmpty()){
						
						writer.println(outgoing.getText());
						writer.flush();
						outgoing.setText("");
						outgoing.requestFocus();
					}

				} catch (Exception e1) {
					e1.printStackTrace();
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
	    
	}
	




	public static void main(String[] args) {
		launch(args);
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
