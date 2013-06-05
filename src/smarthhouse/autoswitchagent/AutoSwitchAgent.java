package smarthhouse.autoswitchagent;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

@SuppressWarnings("serial")
public class AutoSwitchAgent extends Agent {
	
	public void setup(){

		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// enregistre les lights
		seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
		
		// traitement requêtes capteurs/interactions
		seqbhv.addSubBehaviour(new AutoSwitchRequestsBehaviour());
	}
}
