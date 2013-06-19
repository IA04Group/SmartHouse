package smarthouse.autoswitchagent;

import Data.Constants;
import Data.LightData;
import Data.MessageContent;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AutoSwitchRequestsBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		System.out.println("ACTION from requestBHV");
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);		
		ACLMessage message = myAgent.receive(template);
		
		if (message != null) {
			MessageContent d = new MessageContent(message);

			
			/*
			 * algo:
			 *  - which room
			 *  - which light
			 *  - is day ?
			 *  - YES : send error
			 *  - No : send lightagent msg
			 *  
			 * 
			 */
			
			// is day ?
			boolean isDay = false;
			
			try {
				sendDetectorRequest(d.getPlace());
			}
			catch(FIPAException fe) {
				return;
			}
			
			
			if(d.getType().equals(Constants.LIGHT_SENSOR_AGENT)) {
				if(d.getValue() == 0) {
					isDay = false;
				}
				else {
					isDay = true;
				}
			}
			
			
			
			if(isDay) {
				sendBadSimRequest("Il fait deja jour");
				return;
			}
			// light processing
			
			LightData ld = null;
			AID sender = message.getSender();
			
			int lightN = 0;
			if(d.getPlace().equals(Constants.PLACE_OUTDOOR)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(0);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_OUTDOOR, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue(), sender);
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else if(d.getPlace().equals(Constants.PLACE_BEDROOM)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(1);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_BEDROOM, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue(), sender);
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else if(d.getPlace().equals(Constants.PLACE_LIVINGROOM)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(2);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_LIVINGROOM, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue(), sender);
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else if(d.getPlace().equals(Constants.PLACE_KITCHEN)) {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(3);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_KITCHEN, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue(), sender);
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			else {
				lightN = ((AutoSwitchAgent)myAgent).getLightsPerRoom().get(4);
				ld = ((AutoSwitchAgent)myAgent).retrieveLight(Constants.PLACE_RANDOM, lightN);
				if(ld != null) {
					sendLightRequest(ld.getLightAgentID(), (int) d.getValue(), sender);
				}
				else {
					sendBadRequest(message.getSender(), "Lumière introuvable");
				}
			}
			
		}else{
			block();
		}
	}
	
	private void sendBadSimRequest(String string) {
		ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
		
		// parsing
		MessageContent content = new MessageContent(0, Constants.AUTO_SWITCH_AGENT, "", string);
		

		String res = content.toJSON();
		
		//retrieve sender
		AID sender = null;
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.SIMULATION);
		template.addServices(sd);
		
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(myAgent, template);
			if(result.length > 0) {
				sender = result[0].getName();
				answer.addReceiver(sender);
				answer.setContent(res);
				myAgent.send(answer);
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}		
	}

	/*
	 * ask LightDetector if there is already light
	 */
	private void sendDetectorRequest(String place) throws FIPAException {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		AID receiver =null;
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.LIGHT_SENSOR_AGENT);
		template.addServices(sd);
		
		DFAgentDescription[] result = null;
		result = DFService.search(myAgent, template);
        System.out.println(result.length + " light detector(s)" );
		if(result.length > 0) {
			receiver = result[0].getName();
			message.addReceiver(receiver);
							
			myAgent.send(message);
		}
	}

	/*
	 *  send order to Light number lightId
	 *  lightId : an existing lightagent
	 */
	public void sendLightRequest(AID lightId, int order, AID sender) {
		MessageContent d = new MessageContent(order, Constants.AUTO_SWITCH, "");
				String answer = d.toJSON();
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent(answer);
		msg.addReceiver(lightId);
		myAgent.send(msg);
	}
	
	/*
	 * if the requested light does not exist or is already in the demanded state
	 * 
	 */
	public void sendBadRequest(AID sender, String message) {
		MessageContent d = new MessageContent(0, Constants.AUTO_SWITCH, message);
		
		String answer = d.toJSON();

		ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
		msg.setContent(answer);
		msg.addReceiver(sender);
		myAgent.send(msg);
	}
}
