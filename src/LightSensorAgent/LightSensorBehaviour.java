package LightSensorAgent;

import jade.core.behaviours.Behaviour;
import jade.domain.introspection.ACLMessage;


class LightSensorBehaviour extends Behaviour{

	@Override
	public void action() {
		// TODO Auto-generated method stub
		jade.lang.acl.ACLMessage msg=myAgent.receive();
		if(msg!=null){
			System.out.println("AppAgent receive : "+ msg.getContent());
			readValue();
		}
		else block();
		
	}

	private void readValue() {
		System.out.println("toto");
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
