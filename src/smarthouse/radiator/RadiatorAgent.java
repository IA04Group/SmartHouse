package smarthouse.radiator;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import smarthouse.therm.ThermBehaviour;

public class RadiatorAgent extends Agent{
	String place;
	int id;
	double state;
	
	public void setup(){
		System.out.println("##############################\nStart Radiator Agent: " + this.getLocalName());
	    
		//récupérer la position de l'agent passé en argument à la création de l'agent (voir smarthouse/Main.java)
		Object[] tab = getArguments();
		place = (String)tab[0];
		id = (Integer)tab[1];
		
		//Enregistrement de l'agent GUI
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Radiator");
		sd.setName(place);
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {fe.printStackTrace();}
		
		addBehaviour(new RadiatorBehaviour());
	}
}
