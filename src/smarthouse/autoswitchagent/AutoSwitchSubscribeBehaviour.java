package smarthouse.autoswitchagent;

import Data.Constants;
import Data.MessageContent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AutoSwitchSubscribeBehaviour extends Behaviour {
	private int current_lights = 0;

	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);		
		ACLMessage message = myAgent.receive(template);
		
		if (message != null) {
			ACLMessage reponse = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			current_lights = ((AutoSwitchAgent) myAgent).subscribeNewLight(message.getSender());
			
			//TODO décoder les infos (salle, etc)
			
			System.out.println("New light ! : " + message.getSender());
		
			ObjectMapper objectMapper = new ObjectMapper();
			MessageContent data = new MessageContent(current_lights, "autoswitch", "placeRandom");
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
