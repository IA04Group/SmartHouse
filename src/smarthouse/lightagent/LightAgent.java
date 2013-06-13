package smarthouse.lightagent;

import smarthouse.autoswitchagent.AutoSwitchRequestsBehaviour;
import smarthouse.autoswitchagent.AutoSwitchSubscribeBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class LightAgent extends Agent {
	
	private boolean state = false; // false off, true on


	public void setup(){
		
		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register to autoswitch
		seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
		
		// traitement requêtes capteurs/interactions
		seqbhv.addSubBehaviour(new AutoSwitchRequestsBehaviour());
		//seqbhv.addSubBehaviour(new SwitchBehaviour());
		
		this.addBehaviour(seqbhv);
		this.subscribeToSwitch();
	}
	
	public void subscribeToSwitch() {
		AID autoSwitch = null;
		ACLMessage message = new ACLMessage(ACLMessage.SUBSCRIBE);
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("AutoSwitch");
		template.addServices(sd);
		
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(this, template);
			if(result != null && result[0] != null) 
				autoSwitch = result[0].getName();
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		message.addReceiver(autoSwitch);
		send(message);
	
	}

	public void changeState() {
		this.state = !this.state;
		
	}
	
	

}