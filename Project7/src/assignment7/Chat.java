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

	public static boolean hasRecieved = false;
	public static boolean canProceed = false;
	
	public void start(Stage primaryStage) {
		primary = primaryStage;
		logIn = new Stage();
		HBox hb = new HBox();
		VBox vb = new VBox();
		logIn.setTitle("Log into Chat!");
		Scene scene = new Scene(group);
		
		
		Label ID = new Label("Username: ");
		
		TextField username = new TextField();
		username.setPromptText("Enter your username");
		
		Label PW = new Label("Password: ");
		
		TextField password = new TextField();
		password.setPromptText("Enter your password");
		
		Label serverIDL = new Label("Server IP: ");
		
		TextField serverID = new TextField("127.0.0.1");
		serverID.setPromptText("Enter the server address");
		
		Button submit = new Button("Enter Chat!");
		grid.add(ID, 1, 1);
		grid.add(username, 3, 1);
		grid.add(PW, 1, 2);
		grid.add(password, 3, 2);
		grid.add(serverIDL, 1, 3);
		grid.add(serverID, 3, 3);
		grid.add(submit, 1, 4);
		
		Scene sceneLog = new Scene(grid, 300, 150);
		logIn.setScene(sceneLog);
		
		logIn.show();
		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
						
				@SuppressWarnings("resource")
				Socket sock;
				try {
					sock = new Socket(serverID.getText(), 4242);
					InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(streamReader);
					writer = new PrintWriter(sock.getOutputStream());
					System.out.println("networking established");
										
					writer.println("/login " + username.getText() + ":" + password.getText());
					writer.flush();
					
					
					// Thread readerThread = new Thread(new IncomingReader());
					// readerThread.start();
		
					hasRecieved = false;
					canProceed = false;
					
					while(!hasRecieved){
						String message;
						while (!hasRecieved && (message = reader.readLine()) != null) {
							if (message.equals("login success")){
								hasRecieved = true;
								canProceed = true;
							}
							else if (message.equals("wrong username")){
								hasRecieved = true;
								canProceed = false;
							}
							else if (message.equals("wrong password")){
								hasRecieved = true;
								canProceed = false;
							}
						}
					}
					
					if (canProceed){
							
						logIn.close();
						primary.setScene(scene);
						primary.setWidth(600);
						primary.setHeight(350);
						primary.show();
						
						Thread readerThread = new Thread(new IncomingReader());
						readerThread.start();
					}
					
			        
					
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
					
					/*
					if (message.equals("login success")){
						hasRecieved = true;
						canProceed = true;
					}
					else if (message.equals("wrong username")){
						hasRecieved = true;
						canProceed = false;
					}
					else if (message.equals("wrong password")){
						hasRecieved = true;
						canProceed = false;
					}
					else{
						
					}*/
					
					incoming.appendText(message + "\n");
					
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
		
}
