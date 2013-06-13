package smarthouse.autoswitchagent;

import java.io.IOException;

import Data.MessageContent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AutoSwitchRequestsBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		System.out.println("ACTION from requestBHV");
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);		
		ACLMessage message = myAgent.receive(template);
		
		if (message != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			MessageContent d = null;
			try {
				d = objectMapper.readValue(message.getContent(), MessageContent.class);
			} catch (JsonParseException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// TODO switch sur les differents request possibles
			
		}else{
			block();
		}
	}
	
	/*
	 *  send order to Light number lightId
	 *  lightId : assuming existing in the lights arraylist
	 */
	public void sendLightRequest(int lightId, int order) {
		String s = "" + order;
		MessageContent d = new MessageContent(lightId, "autoswitch", s);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String answer;
		
		try {
			answer = objectMapper.writeValueAsString(d);
		} catch (JsonProcessingException e) {
			answer = "";
			e.printStackTrace();
		}
		System.out.println("fin action Subscribe : " + answer);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent(answer);
		msg.addReceiver(((AutoSwitchAgent)myAgent).lights.get(lightId));
		myAgent.send(msg);
	}
	
	/*
	 * if the requested light does not exist or is already in the demanded state
	 * 
	 */
	public void sendBadRequest(AID sender, String message) {
		MessageContent d = new MessageContent(0, "autoswitch", message);
		

		ObjectMapper objectMapper = new ObjectMapper();
		String answer;
		
		try {
			answer = objectMapper.writeValueAsString(d);
		} catch (JsonProcessingException e) {
			answer = "";
			e.printStackTrace();
		}

		ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
		msg.setContent(answer);
		msg.addReceiver(sender);
		myAgent.send(msg);
	}
}
