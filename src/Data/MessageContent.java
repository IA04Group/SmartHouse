package Data;

import java.util.ArrayList;

/***
 * MESSAGE FORMAT
 * {
 *		"value":<double>		"place":<string> // "entrance" / "room1" / "kitchen"
 *		"content":<list/array>
 *	}
 *
 */

public class MessageContent {
	
	private double value;
	private String place;
	private ArrayList<String> content = new ArrayList<String>();
	
	public MessageContent(double value, String place, String... content){
		
		this.value = value;
		this.place = place;
		for (String s : content) {
			this.content.add(s);
	    }
	
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public ArrayList<String> getContent() {
		return content;
	}
	
	public void setContent(ArrayList<String> content) {
		this.content = content;
	}
		
}
