package smarthouse.simulation;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

import Data.MessageContent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ReceiveInformBehaviour extends CyclicBehaviour {
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			parseContent(msg);
		} else {
			block();
		}
	}

	private void parseContent(ACLMessage msg) {
		ObjectMapper mapper = new ObjectMapper();
		MessageContent json;
		try {
			json = mapper.readValue(msg.getContent(), MessageContent.class);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Room room = ((SimulationAgent) myAgent).getWindow().getRoom(json.getPlace());
		int id = Integer.parseInt(json.getContent().get(0));
		//TODO marche pas sous jre6
		/*
		switch (json.getType()) {
			case "light":
				room.setLight(id, json.getValue() > 0);
				break;
			case "shutter":
				room.setShutter(id, json.getValue() > 0);
				break;
			case "heater":
				room.setHeater(id, json.getValue() > 0);
				break;
			case "window":
				room.setWindow(id, json.getValue() > 0);
				break;
		}*/
		String s = json.getType();
		if(s.equals("light")) {
			room.setLight(id, json.getValue() > 0);
		}
		else if(s.equals("shutter")) {
			room.setShutter(id, json.getValue() > 0);
		}
		else if(s.equals("heater")) {
			room.setHeater(id, json.getValue() > 0);
		}
		else if(s.equals("window")) {
			room.setWindow(id, json.getValue() > 0);
		}
	}
}
