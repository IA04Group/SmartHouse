package Data;

import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * MESSAGE FORMAT
 * {
 *		"value":<double>		
 *		"type":<string>
 *		"place":<string> // "entrance" / "room1" / "kitchen"
 *		"content":<list/array>
 *	}
 *
 */

public class MessageContent {
	
	private double value = 0;
	private String type;
	private String place;
	private ArrayList<String> content = new ArrayList<String>();
	
	public MessageContent() {}

	public MessageContent(double value, String type, String place, String... content){
		
		this.value = value;
		this.type = type;
		this.place = place;
		for (String s : content) {
			this.content.add(s);
	    }
	}
	
	public MessageContent(double value, String type, String place){
		
		this.value = value;
		this.type = type;
		this.place = place;
	}
	
	public MessageContent(String type, String place){
		this.type = type;
		this.place = place;
	}
	
	public MessageContent(String type, String place, String... content){
		
		this.type = type;
		this.place = place;
		for (String s : content) {
			this.content.add(s);
	    }
	}
	
	public MessageContent(double value, String type, String place, ArrayList<String> content){
		
		this.value = value;
		this.type = type;
		this.place = place;
		this.content = content;
	}
	
	public MessageContent(String type, String place, ArrayList<String> content){
		
		this.type = type;
		this.place = place;
		this.content = content;
	}
	
	public MessageContent(ACLMessage msg){
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jrootNode = null;
		
		try
		{
			jrootNode = mapper.readValue(msg.getContent(), JsonNode.class);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.value = jrootNode.path("value").asDouble();
		this.type = jrootNode.path("type").asText();
		this.place = jrootNode.path("place").asText();
		if(jrootNode.path("content").isArray()){
			for (int i = 0; i < jrootNode.path("content").size(); i++) {
				this.content.add(jrootNode.path("content").path(i).asText());
		    }
		}
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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
	
	@SuppressWarnings("unchecked")
	public String toJSON(){
		//construction du contenu du message en type JSON
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String, Object> map = null;
		try
		{
			map = mapper.readValue("{}", Map.class);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map.put("value", value);
		map.put("type", type);
		map.put("place", place);
		map.put("content", content);
		
		StringWriter sw = new StringWriter();
		try
		{
			mapper.writeValue(sw, map);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sw.toString();
	}
}
