package smarthouse.LightAgent;

import jade.core.Agent;



public class LightAgent extends Agent {
	
	private boolean state = false; // false off, true on


	public void setup(){
		
		addBehaviour(new SwitchBehaviour());
	}


	public void changeState() {
		this.state = !this.state;
		
	}
	
	

}