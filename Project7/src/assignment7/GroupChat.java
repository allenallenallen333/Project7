package assignment7;

import java.util.ArrayList;
import java.util.Observable;

public class GroupChat{
	public Observable observable;
	public ArrayList<String> usernames = new ArrayList<String>();
	
	public GroupChat(Observable observable, ArrayList<String> usernames){
		this.observable = observable;
		this.usernames = usernames;
	}
}
