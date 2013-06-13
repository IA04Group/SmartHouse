package smarthouse.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Data.MessageContent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveRequestBehaviour extends CyclicBehaviour {
	private ObjectMapper mapper = new ObjectMapper();

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			parseContent(msg);
		} else {
			block();
		}
	}

	private void parseContent(ACLMessage msg) {
		MessageContent json;
		try {
			json = mapper.readValue(msg.getContent(), MessageContent.class);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Room room = ((SimulationAgent) myAgent).getWindow().getRoom(json.getPlace());
		MessageContent replyJson = new MessageContent();
		replyJson.setPlace(json.getPlace());
		replyJson.setType(json.getType());
		double value = 0;
		ArrayList<String> content = new ArrayList<String>();
		content.add(json.getContent().get(0));
		switch (json.getContent().get(0)) {
			case "day":
				value = ((SimulationAgent) myAgent).getWindow().isDay() ? 1 : 0;
				break;
			case "temperature":
				value = room.getTemperature();
				break;
			case "light":
				value = room.getLightLevel();
				break;
			case "time":
				int[] datetime = ((SimulationAgent) myAgent).getWindow().getTime();
				content.add(String.valueOf(datetime[0]));
				content.add(String.valueOf(datetime[1]));
				content.add(String.valueOf(datetime[1]));
				break;
			case "isOn":
			case "isOpen":
				int id = Integer.parseInt(json.getContent().get(1));
				content.add(String.valueOf(id));
				switch (json.getType()) {
					case "light":
						value = room.getLightStatus(id);
						break;
					case "shutter":
						value = room.getShutterStatus(id);
						break;
					case "heater":
						value = room.getHeaterStatus(id);
						break;
					case "window":
						value = room.getWindowStatus(id);
						break;
				}
				break;
		}
		replyJson.setContent(content);
		replyJson.setValue(value);

		sendReply(msg, replyJson);
	}

	private void sendReply(ACLMessage msg, MessageContent json) {
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		try {
			reply.setContent(mapper.writeValueAsString(json));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}
		myAgent.send(reply);
	}
}
