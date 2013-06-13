package smarthouse.lightagent;


/*import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;*/
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import Data.MessageContent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


// teste avec l'arduino marche niquel !!
class SwitchBehaviour extends CyclicBehaviour /*implements SerialPortEventListener */{
	

	@Override
	public void action() {
		
	
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);		
		jade.lang.acl.ACLMessage message =myAgent.receive(template);
		
		
		if (message != null) {
			
			/*ObjectMapper objectMapper = new ObjectMapper();
			MessageContent mc = null;
			try {
				mc = objectMapper.readValue(message.getContent(), MessageContent.class);
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			((LightAgent) myAgent).changeState();		
			this.toGUIAgent();
			
		}else{
			block();
		}
	
		
	
	}
	
	private void toGUIAgent(){
		
		ACLMessage reponse = new ACLMessage(ACLMessage.INFORM);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Data.MessageContent messageContent  = new MessageContent(((LightAgent) myAgent).getState(), "Cuisine", "1");
		String toGUI;
	
		try {
			toGUI = objectMapper.writeValueAsString(messageContent);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			toGUI= "";
			e.printStackTrace();
		}
		
		
		reponse.setContent(toGUI);
		//reponse.addReceiver();
		System.out.println(reponse);
		myAgent.send(reponse);
		
	}



	
	
}
