package smarthouse.autoswitchagent;

import Data.Constants;
import Data.MessageContent;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AutoSwitchSubscribeBehaviour extends Behaviour {
	private int current_lights = 0;
	private int nbr_lights = 0;

	@Override
	public void action() {
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);		
		ACLMessage message = myAgent.receive(template);
		
		if (message != null) {
			ACLMessage reponse = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			
			//retrieving the data
			MessageContent content = new MessageContent(message);
			current_lights = ((AutoSwitchAgent) myAgent).subscribeNewLight(message.getSender(),
						content.getPlace());
			System.out.println("New light ! : " + message.getSender());
			nbr_lights++;
			MessageContent data = new MessageContent(current_lights, Constants.AUTO_SWITCH, "");
			String answer = data.toJSON();

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
		return Constants.NBR_MAX_LIGHTS <= nbr_lights;
	}

}
