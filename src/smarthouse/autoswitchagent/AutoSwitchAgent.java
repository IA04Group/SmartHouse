package smarthouse.autoswitchagent;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class AutoSwitchAgent extends Agent {
	
	public ArrayList<AID> lights = new ArrayList<AID>();
	
	public void setup(){
		
		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register lights
		seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
		
		// answer requests
		seqbhv.addSubBehaviour(new AutoSwitchRequestsBehaviour());
		
		this.addBehaviour(seqbhv);
		
		
		// registration
		DFAgentDescription dfd = new  DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("AutoSwitch");
		sd.setName("AutoSwitchAgent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	public int subscribeNewLight(AID sender) {
			lights.add(sender);
			return lights.size();
	}
}
