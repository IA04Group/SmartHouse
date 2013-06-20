package smarthouse.autoswitchagent;

import java.util.ArrayList;

import Data.Constants;
import Data.LightData;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class AutoSwitchAgent extends Agent {
	
	// TODO
	//lights
	public ArrayList<LightData> bedroomLights = new ArrayList<LightData>();
	public ArrayList<LightData> livingroomLights = new ArrayList<LightData>();
	public ArrayList<LightData> outdoorLights = new ArrayList<LightData>();
	public ArrayList<LightData> randomLights = new ArrayList<LightData>();
	public ArrayList<LightData> kitchenLights = new ArrayList<LightData>();

	// lights
	//public ArrayList<LightData> lights = new ArrayList<LightData>();
	

	//private ArrayList<Integer> lightsPerRoom = new ArrayList<Integer>(Constants.NBR_PLACES);
	
	public void setup(){

		
		
		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register lights
		seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
		// answer requests
		seqbhv.addSubBehaviour(new AutoSwitchRequestsBehaviour());
		
		this.addBehaviour(seqbhv);
		
		
		// registration
		DFAgentDescription dfd = new  DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.AUTO_SWITCH);
		sd.setName(Constants.AUTO_SWITCH_AGENT);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	public int subscribeNewLight(AID sender, String place) {
		if(place == null) {
			place = Constants.PLACE_RANDOM;
		}
		int pos = 0;
		
		if(place.equals(Constants.PLACE_BEDROOM)) {
			pos = this.findLightNumber(this.bedroomLights);
			this.bedroomLights.add(new LightData(sender, place, pos));
		}
		else if(place.equals(Constants.PLACE_KITCHEN)) {
			pos = this.findLightNumber(this.kitchenLights);
			this.kitchenLights.add(new LightData(sender, place, pos));
		}
		else if(place.equals(Constants.PLACE_LIVINGROOM)) {
			pos = this.findLightNumber(this.livingroomLights);
			this.livingroomLights.add(new LightData(sender, place, pos));
		}
		else if(place.equals(Constants.PLACE_OUTDOOR)) {
			pos = this.findLightNumber(this.outdoorLights);
			this.outdoorLights.add(new LightData(sender, place, pos));
		}
		else if(place.equals(Constants.PLACE_RANDOM)) {
			pos = this.findLightNumber(this.randomLights);
			this.randomLights.add(new LightData(sender, place, pos));
		}
		
		return pos;
	}

	private int findLightNumber(ArrayList<LightData> lights) {
		if(lights.size() == 0)
			return 0;
		else
			return lights.size();
	}


	public LightData retrieveLight(String place, int lightN) {
		if(place.equals(Constants.PLACE_BEDROOM)) {
			return bedroomLights.get(lightN);
		}
		else if(place.equals(Constants.PLACE_KITCHEN)) {
			return kitchenLights.get(lightN);
		}
		else if(place.equals(Constants.PLACE_LIVINGROOM)) {
			return livingroomLights.get(lightN);
		}
		else if(place.equals(Constants.PLACE_OUTDOOR)) {
			return outdoorLights.get(lightN);
		}
		else if(place.equals(Constants.PLACE_RANDOM)) {
			return randomLights.get(lightN);
		}
		else 
			return null;
	}
}
