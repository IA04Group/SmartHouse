package smarthouse.therm;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import Data.MessageContent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.LinkedList;
import jade.util.leap.List;

public class ThermBehaviour extends CyclicBehaviour{

	AID receiver;
	int temp = 20;
	int threshold = 20;
	int nb_el = 0;
	int nb_rad_on = 0;
	int nb_rad_off = 0;
	int nb_win_o = 0;
	int nb_win_c = 0;
	boolean day = true;
	
	@Override public void action()
	{
		// TODO Auto-generated method stub
		//r�cup�rer la temp�rature d'un interface graphique
		//ordonne le fermeture de fenêtre(s) ou l'ouverture de radiateur(s) et/ou fen�tre(s) en fontion du besoin
		
		//si on a re�us tout les r�sultats de nos requ�tes pr�c�dente ou init
		if(nb_el == 0){
			//Fait une requête auprès des agents fenêtres (OPEN / CLOSE), emploi du temps (DAY / NIGHT) et radiateur (ON/OFF)
			ThermAskWindow();
			ThermAskRadiator();
			//r�cup�re la temp�rature et le seuil
			ThermAskGUI();
		} else{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg != null){
				received(msg);
			} else {
				block();
			}
		}
	}
	
	private void received(ACLMessage message){
		MessageContent ct = new MessageContent(message);
		
		if(ct.getType() == "Window"){
			//Réponse d'un fenêtre
			if(ct.getValue() == 1)
				nb_win_o ++;
			else
				nb_win_c ++;
			
		} else if (ct.getType() == "Radiator"){
			//Réponse d'un Radiateur
			if(ct.getValue() > 0)
				nb_rad_on ++;
			else
				nb_rad_off ++;
		} else if (ct.getType() == ""){
			//r�cup�rer la temp�rature et le seuil
		}
		
		//si on à reçus toutes les réponses on transmet la réponse
		if((--nb_el) == 0){
			//g�n�ration des ordres et envoie
			
			order(tempControl(temp, threshold));
			
			nb_rad_on = 0;
			nb_rad_off = 0;
			nb_win_o = 0;
			nb_win_c = 0;
		}		
	}
	
	private ArrayList<String> tempControl(int temp, int threshold){
		//En fonction de la temp�rature, du seuil et des �tats des �l�ments de la pi�ce, retourner une decision
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		
		try
		{
			map = mapper.readValue("{}", Map.class);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Exemple de d�cision:
		 [
		 *	{
		 *		value=0
		 *		place=place
		 *		type
		 *		content="radiator"
		 *	},{
		 *		value=0
		 *		place=place
		 *		content="window"
		 *	},{
		 *		value=1
		 *		place=place
		 *		content="window"
		 *	}
		 ]
		 **/
		int val = 0;
		ArrayList<String> content = new ArrayList();
		
		if(temp < threshold){
			if(nb_win_o > 0){
				//fen�tres ouvertes ou en partie => on les ferme
				val = 1;
				
				for(int i = 0; i < nb_win_o; i++){
					MessageContent subCt = new MessageContent(0, "Window", ((ThermAgent)myAgent).place);
					
					content.add(subCt.toJSON());
				}	
			} else if(nb_rad_off > 0) {
				//fen�tres ferm�es && radiateurs ferm� ou en partie => on les ouvres tous
				val = 1;
				for(int i = 0; i < nb_rad_off; i++){
					MessageContent subCt = new MessageContent(1, "Radiator", ((ThermAgent)myAgent).place);
					
					content.add(subCt.toJSON());
				}	
			}
		} else if (temp > threshold){
			if(nb_rad_on > 0){
				//radiateurs ouverts ou en partie => on les fermes
				val = 1;
				for(int i = 0; i < nb_rad_on; i++){
					MessageContent subCt = new MessageContent(0, "Radiator", ((ThermAgent)myAgent).place);
					
					content.add(subCt.toJSON());
				}	
			} else if(nb_win_c > 0){
				//radiateurs ferm�s && fen�tres ferm�es ou en partie => on les ouvres toutes
				val = 1;
				for(int i = 0; i < nb_win_c; i++){
					MessageContent subCt = new MessageContent(1, "Window", ((ThermAgent)myAgent).place);
					
					content.add(subCt.toJSON());
				}	
			}
		}
		
		//si temp == threshold => on ne fait rien (valeurs par d�faut)
		
		//ajoute l'�tat du Thermometre
		content.add(String.valueOf(val));
		
		return content;
	}
	
	private void ThermAskRadiator(){
		//Envoie d'une demande des valeurs des radiateurs
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		List receivers = getReceiver("Radiator", ((ThermAgent)myAgent).place);
		nb_el += receivers.size(); 
		for(int i = 0; i < receivers.size(); i++)
			message.addReceiver((AID)receivers.get(i));
		
		MessageContent msgCt = new MessageContent("Thermometre", ((ThermAgent)myAgent).place);
		
		message.setContent(msgCt.toJSON());
		
		myAgent.send(message);
	}
	
	private void ThermAskWindow(){
		//Envoie une demande de l'état à tout les agent window de la pièce
		
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		List receivers = getReceiver("Window", ((ThermAgent)myAgent).place);
		nb_el += receivers.size(); 
		for(int i = 0; i < receivers.size(); i++)
			message.addReceiver((AID)receivers.get(i));
		
		MessageContent msgCt = new MessageContent("Thermometre", ((ThermAgent)myAgent).place);
		
		message.setContent(msgCt.toJSON());
		
		myAgent.send(message);
	}
	
	private void ThermAskGUI(){
		//Envoie d'une requête à l'agent Schedule
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
//Mettre les caract�ristiques de l'agent GUI
		message.addReceiver((AID)getReceiver("", "").get(0));
		nb_el ++;
		
		MessageContent msgCt = new MessageContent("Thermometre", ((ThermAgent)myAgent).place);
		
		message.setContent(msgCt.toJSON());
		
		myAgent.send(message);
	}
	
	private void order(ArrayList<String> orders){
// A finir: envoyer les ordres aux agents Radiator et Window
		int val = Integer.valueOf(orders.get(orders.size() - 1));
		for(int i = 0; i < orders.size() - 1; i++){
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			//msg.addReceiver()
			MessageContent ct = new MessageContent(val, "Thermometre", ((ThermAgent)myAgent).place, orders.get(i));
			myAgent.send(msg);
		}
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
