package Data;

import jade.core.AID;

public class LightData {

	private String place;
	private int position;
	private AID lightAgentID;
	
	public LightData(AID id) {
		setLightAgentID(id);
		position = -1;
		place = "";
	}
	
	public LightData(AID id, String p, int pos) {
		setLightAgentID(id);
		setPlace(p);
		setPosition(pos);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public AID getLightAgentID() {
		return lightAgentID;
	}

	public void setLightAgentID(AID lightAgentID) {
		this.lightAgentID = lightAgentID;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	@Override
	public String toString() {
		return "light : " + lightAgentID.getName()
				+ " is in " + place 
				+ " at position " + position;
	}
}
