package assignment7;


import javax.swing.text.AbstractDocument.Content;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chat extends Application{
	
	private VBox chat = new VBox();
	private GridPane grid = new GridPane();
	final TextArea messages = new TextArea();

	
	
	public void start(Stage primaryStage) {
		
		Scene scene = new Scene(grid);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		TextField textField = new TextField ();
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
				String message = textField.getText();
				messages.setText(message);
			}
		});
		
		messages.autosize();
		grid.add(messages, 1, 1);
		grid.add(textField, 1, 2);
		grid.add(send, 2, 2);
	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
		
}
