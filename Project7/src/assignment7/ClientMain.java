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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ClientMain extends Application{
	
	private Group group = new Group();
	private GridPane grid = new GridPane();
	private TextArea incoming;
	private TextField outgoing;

	public Stage primary;
	public Stage logIn;
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	public void start(Stage primaryStage) {
		primary = primaryStage;
		logIn = new Stage();
		HBox hb = new HBox();
		VBox vb = new VBox();
		HBox hb1 = new HBox();
		HBox hb2 = new HBox();
		logIn.setTitle("Log into Chat!");
		Scene scene = new Scene(group);
		
		
		Label ID = new Label("Username: ");
		
		TextField username = new TextField("Allen");
		username.setPromptText("Enter your username");
		
		Label PW = new Label("Password: ");
		
		TextField password = new TextField("1234");
		password.setPromptText("Enter your password");
		
		Label serverIDL = new Label("Server IP: ");
		
		TextField serverID = new TextField("127.0.0.1");
		serverID.setPromptText("Enter the server address");
		
		Button submit = new Button("Enter Chat!");
		Button new_user = new Button("Sign up!");
		
		Label warning = new Label();
		warning.setTextFill(Color.RED);
		
	
		
		grid.add(ID, 1, 1);
		grid.add(username, 3, 1);
		grid.add(PW, 1, 2);
		grid.add(password, 3, 2);
		grid.add(serverIDL, 1, 3);
		grid.add(serverID, 3, 3);
		grid.add(submit, 1, 5);
		grid.add(new_user, 1, 7);
		grid.add(warning, 3, 9);
		
		Scene sceneLog = new Scene(grid, 320, 200);
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
		
					boolean hasRecieved = false;
					boolean canProceed = false;
					
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
								warning.setText("No such username");
							}
							else if (message.equals("wrong password")){
								hasRecieved = true;
								canProceed = false;
								warning.setText("Incorrect password");
							}
						}
					}
					
					if (canProceed){
							
						logIn.close();
						primary.setTitle("Chat Room: Signed in as " + username.getText());
						primary.setScene(scene);
						primary.setWidth(800);
						primary.setHeight(400);
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
		
		new_user.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
						
				@SuppressWarnings("resource")
				Socket sock;
				try {
					sock = new Socket(serverID.getText(), 4242);
					InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
					reader = new BufferedReader(streamReader);
					writer = new PrintWriter(sock.getOutputStream());
					System.out.println("networking established");
										
					writer.println("/signup " + username.getText() + ":" + password.getText());
					writer.flush();
					
					
					// Thread readerThread = new Thread(new IncomingReader());
					// readerThread.start();
		
					boolean hasRecieved = false;
					boolean canProceed = false;
					
					while(!hasRecieved){
						String message;
						while (!hasRecieved && (message = reader.readLine()) != null) {
							if (message.equals("signup success")){
								hasRecieved = true;
								canProceed = true;
							}
							else if (message.equals("username already exists")){
								hasRecieved = true;
								canProceed = false;
								warning.setText("Username already exists");
							}
						}
					}
					
					if (canProceed){
							
						logIn.close();
						primary.setTitle("Chat Room: Signed in as " + username.getText());
						primary.setScene(scene);
						primary.setWidth(775);
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
		
		
		Label chat = new Label("Chat:");
		chat.setFont(Font.font("Cambria", 20));
		chat.setTextFill(Color.AQUAMARINE);
		Label userLabel = new Label("Users Online:");
		userLabel.setFont(Font.font("Cambria", 20));
		userLabel.setTextFill(Color.AQUAMARINE);
		hb2.setSpacing(490);
		hb2.getChildren().addAll(chat, userLabel);
		
		
		
		
		ListView<String> online = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList();
		Userlist a = new Userlist();
		a.readFromDatabase();
		for (User b: a.Users) {
			items.add(b.username);
		}
		online.setItems(items);
		online.setPrefWidth(185);
		online.setPrefHeight(70);
	
		
		
		incoming = new TextArea();
		incoming.setEditable(false);
		incoming.setWrapText(true);
		incoming.setPrefWidth(500);
		ScrollPane scrollbar = new ScrollPane(incoming);
		outgoing = new TextField();
	    outgoing.setPrefWidth(500);
		outgoing.setPromptText("Enter Your Message Here");
		
		GridPane groupchat = new GridPane();
		TextField groupText = new TextField();
		Label label1 = new Label("Enter who you want to talk to!");
		Label message = new Label("Write your message:");
		TextField groupmessage = new TextField();
		Button send2 = new Button("Send message!");
		groupchat.add(label1, 1, 1);
		groupchat.add(groupText, 1, 2);
		groupchat.add(message, 1, 3);
		groupchat.add(groupmessage, 1, 4);
		groupchat.add(send2, 1, 5);
		Scene g = new Scene(groupchat);
		Stage groupM = new Stage();
		groupM.setScene(g);
		
		
		
		
		Button groupB = new Button("Private Message");
		
		groupB.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				groupM.show();	
			}
		});
		
		send2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					if (groupText.getText() != null && !groupText.getText().isEmpty()){
						
						writer.println("/chat " + groupText.getText() + " " + groupmessage.getText());
						writer.flush();
						groupText.setText("");
						groupText.requestFocus();

					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				groupM.close();
			}
		});
		

		Button send = new Button("Send All");
		
		
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
	    hb.getChildren().addAll(outgoing, send, groupB);
	    hb1.setSpacing(5);
	    hb1.setPadding(new Insets(10, 0, 0, 10));
	    hb1.getChildren().addAll(incoming, online);
	    
	    vb.setSpacing(5);
	    vb.setPadding(new Insets(10, 0, 0, 10));
	    vb.getChildren().addAll(hb2, hb1, hb);
		
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
