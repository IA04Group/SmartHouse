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
	
	private int position = -1; // registered number in autoswitch
	private boolean state = false; // false off, true on


	public void setup(){
		
		//SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register to autoswitch
		//seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
		
		// traitement requêtes capteurs/interactions
		//seqbhv.addSubBehaviour(new AutoSwitchRequestsBehaviour());
		//seqbhv.addSubBehaviour(new SwitchBehaviour());
		
		
		//this.addBehaviour(seqbhv);
		this.addBehaviour(new LightReceiveBehaviour());
		//this.subscribeToSwitch();
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
	        System.out.println(result.length + " results" );
			if(result.length > 0) {
				autoSwitch = result[0].getName();
				message.addReceiver(autoSwitch);
				send(message);
			}
			else {
				System.out.println("et voilà une jolie boucle infinie pour " + this.getName());
				subscribeToSwitch();
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	
	}

	public void changeState() {
		this.state = !this.state;
		
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	

}
