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

	// lights
	public ArrayList<LightData> lights = new ArrayList<LightData>();
	
	
	/*
	 * 0 : outdoor
	 * 1 : bedroom
	 * 2 : livingroom
	 * 3 : kitchen
	 * 4 : random
	 * 
	 * 
	 */
	private ArrayList<Integer> lightsPerRoom = new ArrayList<Integer>(Constants.NBR_PLACES);
	
	public void setup(){
		
		// setup arraylist
		this.lightsPerRoom.add(0);
		this.lightsPerRoom.add(0);
		this.lightsPerRoom.add(0);
		this.lightsPerRoom.add(0);
		this.lightsPerRoom.add(0);
		
		
		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register lights
		seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
		System.out.println("Premier subbhv registered");
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
		int n = findLightNumber(place);
		lights.add(new LightData(sender, place, n));
		return lights.size();
	}

	private int findLightNumber(String place) {
		int i = 0;
		
		if(place.equals(Constants.PLACE_OUTDOOR)) {
			i = lightsPerRoom.get(0);
			if(Integer.valueOf(i) != null)
				lightsPerRoom.set(0, i+1);
			else
				lightsPerRoom.set(0, 1);				
			return lightsPerRoom.get(0);
		}
		else if(place.equals(Constants.PLACE_BEDROOM)) {
			i = lightsPerRoom.get(1);
			if(Integer.valueOf(i) != null)
				lightsPerRoom.set(1, i+1);
			else
				lightsPerRoom.set(1, 1);
			return lightsPerRoom.get(1);
		}
		else if(place.equals(Constants.PLACE_LIVINGROOM)) {
			i = lightsPerRoom.get(2);
			if(Integer.valueOf(i) != null)
				lightsPerRoom.set(2, i+1);
			else
				lightsPerRoom.set(2, 1);
			return lightsPerRoom.get(2);
		}
		else if(place.equals(Constants.PLACE_KITCHEN)){
			i = lightsPerRoom.get(3);
			if(Integer.valueOf(i) != null)
				lightsPerRoom.set(3, i+1);
			else
				lightsPerRoom.set(3, 1);
			return lightsPerRoom.get(3);
		}
		else  { // random
			i = lightsPerRoom.get(4);
			if(Integer.valueOf(i) != null)
				lightsPerRoom.set(4, i+1);
			else
				lightsPerRoom.set(4, 1);
			return lightsPerRoom.get(4);
		}
	}

	public ArrayList<Integer> getLightsPerRoom() {
		return lightsPerRoom;
	}

	public void setLightsPerRoom(ArrayList<Integer> lightsPerRoom) {
		this.lightsPerRoom = lightsPerRoom;
	}

	public LightData retrieveLight(String place, int lightN) {
		for (LightData ld : this.lights) {
			if(ld.getPlace().equals(place)) {
				if(ld.getPosition() == lightN) {
					return ld;
				}
			}
		}
		return null;
	}
}
