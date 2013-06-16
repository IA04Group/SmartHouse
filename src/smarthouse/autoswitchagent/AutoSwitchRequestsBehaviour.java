package smarthouse.autoswitchagent;

import java.io.IOException;

import Data.Constants;
import Data.LightData;
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
			/*
			 * algo:
			 *  - which room
			 *  - which light
			 *  - is day ?
			 *  - YES : send error
			 *  - No : send light up
			 *  
			 *  STIL TODO
			 *  - return YES : return yes
			 *  - return NO : return no
			 * 
			 */
			LightData ld = null;
			int lightN = 0;
			if(d.getPlace().equals(Constants.PLACE_OUTDOOR)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(0);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_OUTDOOR, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue());
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else if(d.getPlace().equals(Constants.PLACE_BEDROOM)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(0);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_BEDROOM, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue());
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else if(d.getPlace().equals(Constants.PLACE_LIVINGROOM)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(0);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_LIVINGROOM, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue());
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(0);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_RANDOM, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue());
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			
		}else{
			block();
		}
	}
	
	/*
	 *  send order to Light number lightId
	 *  lightId : an existing lightagent
	 */
	public void sendLightRequest(AID lightId, int order) {
		MessageContent d = new MessageContent(order, Constants.AUTO_SWITCH, "");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String answer;
		
		try {
			answer = objectMapper.writeValueAsString(d);
		} catch (JsonProcessingException e) {
			answer = "";
			e.printStackTrace();
		}
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent(answer);
		msg.addReceiver(lightId);
		myAgent.send(msg);
	}
	
	/*
	 * if the requested light does not exist or is already in the demanded state
	 * 
	 */
	public void sendBadRequest(AID sender, String message) {
		MessageContent d = new MessageContent(0, Constants.AUTO_SWITCH, message);
		

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
