package smarthouse.RemoteControlAgent;

import jade.core.Agent;



public class RemoteControlAgent extends Agent {



	public void setup(){
		
		addBehaviour(new RemoteControlBehaviour());
	}

}