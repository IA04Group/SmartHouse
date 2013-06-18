package smarthouse.lightagent;

import java.io.IOException;

import Data.Constants;
import Data.MessageContent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class LightReceiveBehaviour extends CyclicBehaviour{

	@Override
	public void action() {
	
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);		
		ACLMessage message = myAgent.receive(template);
			
		if (message != null ) {
			ACLMessage reply = this.parse(message);
			System.out.println("envoi code reponse");
			myAgent.send(reply);
			System.out.println(message.getSender());
		}else{
			block();
		}
	
		
	}
	
	private ACLMessage parse(ACLMessage message){
		ACLMessage answer;
		// parsing
		ObjectMapper objectMapper = new ObjectMapper();
		MessageContent content = null;
		try {
			content = objectMapper.readValue(message.getContent(), MessageContent.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// test valeur
		if( ( ((LightAgent)myAgent).getLightState() &&  content.getValue()==1) 
				|| ( !((LightAgent)myAgent).getLightState() &&  content.getValue()==0)) {
			// already done
			answer = new ACLMessage(ACLMessage.FAILURE);
		}
		else {
			//state change
			 ((LightAgent)myAgent).changeState();
			 answer = new ACLMessage(ACLMessage.INFORM);
		}

		MessageContent answerContent = new MessageContent(content.getValue(), 
							Constants.LIGHT_AGENT, ((LightAgent)myAgent).getPlace());
		
		String res = "";
		try {
			res = objectMapper.writeValueAsString(answerContent);
			answer.setContent(res);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//retrieve sender
		AID sender = ((LightAgent) myAgent).getAIDFromString(content.getContent().get(0));
		
		answer.addReceiver(sender);
		return answer;
		
		// 1 pour allumer, type de l"agent, lieu ou est la lumiere, et 0 pour l'id de la lumiere
		//MessageContent messageContent = new MessageContent(1, Constants.LIGHT_AGENT, "kitchen", "0");
		/*String json = messageContent.toJSON();
		DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(Constants.SIMULATION);
        sd.setName(Constants.SIMULATION_AGENT);
        template.addServices(sd);
        try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                if (result.length > 0) {
                        ACLMessage request = new ACLMessage(ACLMessage.INFORM);
                        for (DFAgentDescription receiver : result) {
                                if (!receiver.getName().equals(myAgent.getAID())) {
                                        request.addReceiver(receiver.getName());
                                       
                                }
                        }
                        request.setContent(json);
                        myAgent.send(request);
                }
        } catch(FIPAException fe) {
                fe.printStackTrace();
        }
		return message;
		 */
		
	
		
	}
	
	

}
