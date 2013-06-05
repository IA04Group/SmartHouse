package smarthhouse.autoswitchagent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class AutoSwitchRequestsBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		ACLMessage reponse = myAgent.receive();
		if (reponse != null) {
			//TODO templates en fonction des cas
		}else{
			block();
		}
	}

}
