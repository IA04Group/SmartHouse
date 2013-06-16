package smarthouse.lightagent;

import java.io.IOException;

import Data.Constants;
import Data.MessageContent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class LightReceiveBehaviour extends CyclicBehaviour{

	@Override
	public void action() {
	
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);		
		ACLMessage message = myAgent.receive(template);
			
		if (message != null ) {
			ACLMessage reply = this.parse(message);
			System.out.println("test");
			myAgent.send(reply);
			System.out.println(message.getSender());
		}else{
			block();
		}
	
		
	}
	
	private ACLMessage parse(ACLMessage message){
		// 1 pour allumer, type de l"agent, lieu ou est la lumiere, et 0 pour l'id de la lumiere
		MessageContent messageContent = new MessageContent(1, Constants.LIGHT_AGENT, "kitchen", "0");
		String json = messageContent.toJSON();
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

		
	
		
	}
	
	

}
