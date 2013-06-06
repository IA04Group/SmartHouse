package smarthouse.autoswitchagent;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AutoSwitchSubscribeBehaviour extends Behaviour {
	private static int MAX_LIGHTS = 2;
	private int current_lights = 0;

	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);		
		ACLMessage message = myAgent.receive(template);
		
		if (message != null) {
			ACLMessage reponse = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			current_lights++;
		
			ObjectMapper objectMapper = new ObjectMapper();
			//TODO light classes..
			
			// TODO réponse
		}
		else{
			block();
		}
	}

	@Override
	public boolean done() {
		return MAX_LIGHTS < current_lights;
	}

}
