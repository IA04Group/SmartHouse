package smarthouse.autoswitchagent;

import java.io.IOException;

import Data.Constants;
import Data.MessageContent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AutoSwitchSubscribeBehaviour extends Behaviour {
	private int current_lights = 0;

	@Override
	public void action() {
		System.out.println("ON DEMARRE");
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);		
		ACLMessage message = myAgent.receive(template);
		
		if (message != null) {
			ACLMessage reponse = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			
			//retrieving the data
			ObjectMapper objectMapper = new ObjectMapper();
			MessageContent content = null;
			try {
				content = objectMapper.readValue(reponse.getContent(), MessageContent.class);
				
				current_lights = ((AutoSwitchAgent) myAgent).subscribeNewLight(message.getSender(),
						content.getPlace());
				System.out.println("New light ! : " + message.getSender());
				System.out.println("Suite : New light ! : " + 
							((AutoSwitchAgent) myAgent).lights.get(current_lights-1).toString());
			
			} catch (JsonParseException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			MessageContent data = new MessageContent(current_lights, Constants.AUTO_SWITCH, "");
			String answer;
			
			try {
				answer = objectMapper.writeValueAsString(data);
			} catch (JsonProcessingException e) {
				answer = "";
				e.printStackTrace();
			}
			System.out.println("fin action Subscribe : " + answer);
			reponse.setContent(answer);
			reponse.addReceiver(message.getSender());
			myAgent.send(reponse);
		}
		else{
			block();
		}
	}

	@Override
	public boolean done() {
		return Constants.NBR_MAX_LIGHTS <= current_lights;
	}

}
