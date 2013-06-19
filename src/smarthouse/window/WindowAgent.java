package smarthouse.window;

import Data.Constants;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import smarthouse.therm.ThermometerBehaviour;

public class WindowAgent extends Agent{
	String place;
	int id;
	boolean state;
	
	public void setup(){	    
		//récupérer la position de l'agent passé en argument à la création de l'agent (voir smarthouse/Main.java)
		Object[] tab = getArguments();
		place = (String)tab[0];
		id = (Integer)tab[1];
		
		System.out.println("##############################\nStart Window Agent: " + this.getLocalName() + " / " + place);
		
		//Enregistrement de l'agent GUI
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.WINDOW);
		sd.setName(place);
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {fe.printStackTrace();}
		
		addBehaviour(new WindowBehaviour());
	}
}
