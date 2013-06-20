package smarthouse.shutter;

import smarthouse.heater.HeaterAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.LinkedList;
import jade.util.leap.List;
import Data.Constants;
import Data.MessageContent;

public class ShutterBehaviour extends CyclicBehaviour{

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		ACLMessage msg = myAgent.receive(mt);
		
		if(msg != null){
			received(msg);
		} else {
			block();
		}
	}

	void received(ACLMessage msg){
		switch(msg.getPerformative()){
		case ACLMessage.INFORM:
			changeState(msg);
			break;
		case ACLMessage.REQUEST:
			sendState(msg);
			break;
		}
	}
	
	void changeState(ACLMessage msg){
		MessageContent ct = new MessageContent(msg);	
		((ShutterAgent)myAgent).state = (ct.getValue() != 0);
		
		//Envoi un message de changement d'�tat � l'agent GUI
		ACLMessage chSt = new ACLMessage(ACLMessage.INFORM);
		
		chSt.addReceiver((AID)getReceiver(Constants.SIMULATION, Constants.SIMULATION_AGENT).get(0));
		chSt.setContent(getMsgContent().toJSON());
		
		myAgent.send(chSt);
	}
	
	void sendState(ACLMessage msg){
		ACLMessage st = msg.createReply();
		st.setPerformative(ACLMessage.INFORM);
		st.setContent(getMsgContent().toJSON());
		
		//System.out.println(myAgent.getLocalName() + ": reply to " + msg.getSender().getLocalName() + "\n\t with: " + st.getContent());
		
		myAgent.send(st);
	}
	
	MessageContent getMsgContent(){
		return new MessageContent((((ShutterAgent)myAgent).state) ? 1.0 : 0.0, Constants.SHUTTER, ((ShutterAgent)myAgent).place, "" + ((ShutterAgent)myAgent).id);
	}
	
	public List getReceiver(String type, String name) {
		List receivers = new LinkedList();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		sd.setType(type);
		sd.setName(name);
		template.addServices(sd);
		
		try {
			//System.out.println("recherche d'agent (" + sd.getType() + ", " + sd.getName() + ") ...");
			DFAgentDescription[] result = DFService.search(myAgent, template);
			
			for(int i = 0; i < result.length; i++)
				receivers.add(result[i].getName());
		}
		catch(FIPAException fe) {fe.printStackTrace();}
		
		return receivers;
	}
}