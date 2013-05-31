package LightSensorAgent;

import jade.core.Agent;



public class LightSensorAgent extends Agent {



	public void setup(){
		
		addBehaviour(new LightSensorBehaviour());
	}

}