package smarthouse.lightagent;

import java.io.IOException;

import Data.MessageContent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class LightSubscribeBehaviour extends Behaviour {
	boolean end = false;
	
	@Override
	public void action() {
		System.out.println("on attend l'accept");
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);		
		ACLMessage answer = myAgent.receive(template);
		System.out.println("reception d'un accept");
		if (answer != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			MessageContent d = null;
			try {
				d = objectMapper.readValue(answer.getContent(), MessageContent.class);
			} catch (JsonParseException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			System.out.println("I am registered : " + d.getValue());
			((LightAgent)myAgent).setPosition((int) d.getValue());
			
			end = true;
		}
		else {
			block();
		}
	}

	@Override
	public boolean done() {
		return end;
	}
}
