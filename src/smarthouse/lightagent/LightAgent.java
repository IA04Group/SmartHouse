package smarthouse.lightagent;

import Data.Constants;
import Data.LightData;
import Data.MessageContent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class LightAgent extends Agent {
	
	private boolean state = false; // false off, true on
	private LightData myData = new LightData(this.getAID());

	public void setup(){
		try {
			String[] args = (String[]) getArguments();
			myData.setPlace(args[0]);
		}
		catch(Exception e) {
			myData.setPlace(Constants.PLACE_RANDOM);
		}
		
		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register to autoswitch
		seqbhv.addSubBehaviour(new LightSubscribeBehaviour());
				
		//this.addBehaviour(seqbhv);
		seqbhv.addSubBehaviour(new LightReceiveBehaviour());
		
		this.addBehaviour(seqbhv);
		
		// and there we're starting
		this.subscribeToSwitch();
	}
	
	public void subscribeToSwitch() {
		AID autoSwitch = null;
		ACLMessage message = new ACLMessage(ACLMessage.SUBSCRIBE);
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.AUTO_SWITCH);
		template.addServices(sd);
		
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(this, template);
			if(result.length > 0) {
				autoSwitch = result[0].getName();
				message.addReceiver(autoSwitch);
				System.out.println("LIGHT GET PLACE : " + this.myData.getPlace());
				MessageContent d = new MessageContent(0, Constants.LIGHT_AGENT, 
						this.myData.getPlace(), "");
				
				String answer = d.toJSON();
				System.out.println("LIGHT on send : " + answer);
				message.setContent(answer);	
				send(message);
			}
			else {
				System.out.println("voilà une jolie boucle infinie pour " + this.getName());
				subscribeToSwitch();
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	
	}

	public void changeState() {
		this.state = !this.state;
		
	}

	public int getPosition() {
		return this.myData.getPosition();
	}

	public void setPosition(int position) {
		this.myData.setPosition(position);
	}

	public String getPlace() {
		return myData.getPlace();
	}

	public void setPlace(String place) {
		this.myData.setPlace(place);
	}
	
	public boolean getLightState() {
		return this.state;
	}
}
