package smarthouse.therm;

import jade.core.Agent;

public class ThermAgent extends Agent{
	public void setup(){
		addBehaviour(new ThermBehaviour());
	}
}
