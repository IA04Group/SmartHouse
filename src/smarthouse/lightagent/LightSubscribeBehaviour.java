package smarthouse.lightagent;

import Data.MessageContent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class LightSubscribeBehaviour extends Behaviour {
	boolean end = false;
	
	@Override
	public void action() {
		System.out.println("SUBSCRIBE LIGHT");
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);		
		ACLMessage answer = myAgent.receive(template);
		System.out.println("reception d'un accept");
		if (answer != null) {
			MessageContent d = new MessageContent(answer);
			
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
