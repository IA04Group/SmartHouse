package smarthouse.simulation;

import Data.Constants;
import Data.MessageContent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SendTimeBehaviour extends OneShotBehaviour {
	private String day, hour, min;
	public SendTimeBehaviour(int day, int hour, int min) {
		super();
		this.day = String.valueOf(day);
		this.hour = String.valueOf(hour);
		this.min = String.valueOf(min);
	}

	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		MessageContent json = new MessageContent(Constants.SIMULATION_AGENT, Constants.PLACE_OUTDOOR, "time", day, hour, min);
		msg.setContent(json.toJSON());
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.PLANNING);
		sd.setName(Constants.PLANNING_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			if (result.length > 0) {
				for (DFAgentDescription receiver : result) {
					msg.addReceiver(receiver.getName());
				}
				myAgent.send(msg);
			}
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}
}