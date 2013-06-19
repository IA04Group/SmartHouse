package smarthouse.therm;

import com.sun.corba.se.impl.orbutil.closure.Constant;

import Data.Constants;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ThermometerAgent extends Agent{
	String place;
	
	public void setup(){	    
		//récupérer la position de l'agent passé en argument à la création de l'agent (voir smarthouse/Main.java)
		Object[] tab = getArguments();
		place = (String)tab[0];
		
		System.out.println("##############################\nStart Therm Agent: " + this.getLocalName() + " / " + place);
		
		//Enregistrement de l'agent GUI
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.THERMOMETER);
		sd.setName(place);
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {fe.printStackTrace();}
		
		//update toutes les Xms
		addBehaviour(new ThermometerBehaviour(this, Constants.THERMOMETER_TIMER));
	}
}
