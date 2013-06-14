package smarthouse.lightsensoragent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;



public class LightSensorAgent extends Agent {



	public void setup(){
		
		// registration
		DFAgentDescription dfd = new  DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.Constants.LightAgent);
		sd.setName(Constants.Constants.LightSensorAgent);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		
		addBehaviour(new LightSensorBehaviour());
	}

}