package smarthouse.lightagent;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Data.Constants;
import Data.MessageContent;
import smarthouse.autoswitchagent.AutoSwitchSubscribeBehaviour;
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
	
	private int position = -1; // registered number in autoswitch
	private boolean state = false; // false off, true on
	private String place = "";


	public void setup(){
		try {
			String[] args = (String[]) getArguments();
			place = args[0];
		}
		catch(Exception e) {
			place = Constants.PLACE_RANDOM;
		}
		
		SequentialBehaviour seqbhv = new SequentialBehaviour();
		
		// register to autoswitch
		seqbhv.addSubBehaviour(new AutoSwitchSubscribeBehaviour());
				
		//this.addBehaviour(seqbhv);
		seqbhv.addSubBehaviour(new LightReceiveBehaviour());
		
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
	        System.out.println(result.length + " results" );
			if(result.length > 0) {
				autoSwitch = result[0].getName();
				message.addReceiver(autoSwitch);
				
				MessageContent d = new MessageContent(0, Constants.LIGHT_AGENT, this.place);
				ObjectMapper objectMapper = new ObjectMapper();
				
				String answer = "";
				try {
					answer = objectMapper.writeValueAsString(d);
					System.out.println("LIGHT on send : " + answer);
					message.setContent(answer);
					
					send(message);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("et voilà une jolie boucle infinie pour " + this.getName());
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
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	public boolean getLightState() {
		return this.state;
	}
	
	public AID getAIDFromString(String sender) {
		AID id = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			id = objectMapper.readValue(sender, AID.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return id;
	}

}
