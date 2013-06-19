package smarthouse.therm;

import Data.Constants;
import Data.MessageContent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.LinkedList;
import jade.util.leap.List;

public class ThermometerBehaviour extends TickerBehaviour{
      
	AID receiver;
	double temp = 0.0;
	double threshold = 0.0;
	double threshold_d = 0.25;
	int nb_el = 0;
	List nb_rad_ona = new LinkedList();
	List nb_rad_offa = new LinkedList();
	List nb_win_oa = new LinkedList();
	List nb_win_ca = new LinkedList();
	boolean day = true;
	
    public ThermometerBehaviour(Agent a, long period)
    {
        super(a, period);
        // TODO Auto-generated constructor stub
    }

    @Override protected void onTick()
    {
		// TODO Auto-generated method stub
		//r�cup�rer la temp�rature d'un interface graphique
		//ordonne le fermeture de fenêtre(s) ou l'ouverture de radiateur(s) et/ou fen�tre(s) en fonction du seuil +- l'acceptation
		
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
		
		if(ct.getType().equals(Constants.WINDOW)){
			//Réponse d'un fenêtre
			if(ct.getValue() == 1)
				nb_win_oa.add(message.getSender());
			else
				nb_win_ca.add(message.getSender());
			
		} else if (ct.getType().equals(Constants.HEATER)){
			//Réponse d'un Radiateur
			if(ct.getValue() > 0)
				nb_rad_ona.add(message.getSender());
			else
				nb_rad_offa.add(message.getSender());
		} else if (ct.getType().equals(Constants.SIMULATION)){
			//r�cup�re la temp�rature et le seuil
			temp = ct.getValue();
			//en valeur 0 il y a la demande retournée pour savoir à quoi l'agent à répondu ici: "temperature"
			threshold = Double.valueOf(ct.getContent().get(1));
		}
		
		//si on à reçus toutes les réponses on transmet la réponse
		if((--nb_el) == 0){
			//g�n�ration des ordres et envoie
			
			System.out.println("\nTemp init: " + temp + "°\nSeuil: " + threshold + "°");
			System.out.println("\nStatus:\n\tnb_win_o: " + nb_win_oa.size() + "\n\tnb_win_c: " + nb_win_ca.size() + "\n\tnb_rad_on: " + nb_rad_ona.size() + "\n\tnb_rad_off: " + nb_rad_offa.size());
			
			tempControl(temp, threshold);
			
			nb_rad_ona.clear();
			nb_rad_offa.clear();
			nb_win_oa.clear();
			nb_win_ca.clear();
		}		
	}
	
	@SuppressWarnings("null")
	private void tempControl(double temp, double threshold){
		//En fonction de la temp�rature, du seuil et des �tats des �l�ments de la pi�ce, retourner une decision
		
		/*Exemple de d�cision:
		 [
		 *	{
		 *		value=0
		 *		place=place
		 *		type
		 *		content=Constants.HEATER
		 *	}
		 *
		 *	{
		 *		value=0
		 *		place=place
		 *		content=Constants.WINDOW
		 *	}
		 *
		 *	{
		 *		value=1
		 *		place=place
		 *		content=Constants.WINDOW
		 *	}
		 **/
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		MessageContent Ct = null;
		
		if(temp < threshold + threshold_d && temp > threshold - threshold_d){
			//on est entre les bornes du seuil => on ferme les fenêtres ouvertes et les radiateurs allumés
			
			if(nb_win_oa.size() > 0){
				//fen�tres ouvertes ou en partie => on les ferme				
				Ct = new MessageContent(0, Constants.WINDOW, ((ThermometerAgent)myAgent).place);

				//on ajoute N fenettre à fermer
				for(int i = 0; i < nb_win_oa.size(); i++)
					msg.addReceiver((AID)nb_win_oa.get(i));
				
			} else if (nb_rad_ona.size() > 0){
				//radiateurs ouverts ou en partie => on les fermes
				Ct = new MessageContent(0, Constants.HEATER, ((ThermometerAgent)myAgent).place);
				
				for(int i = 0; i < nb_rad_ona.size(); i++)
					msg.addReceiver((AID)nb_rad_ona.get(i));

			}
			
		} else if(temp < threshold - threshold_d){
			if(nb_win_oa.size() > 0){
				//fen�tres ouvertes ou en partie => on les ferme				
				Ct = new MessageContent(0, Constants.WINDOW, ((ThermometerAgent)myAgent).place);

				//on ajoute N fenettre à fermer
				for(int i = 0; i < nb_win_oa.size(); i++)
					msg.addReceiver((AID)nb_win_oa.get(i));
				
			} else if(nb_rad_offa.size() > 0) {
				//fen�tres ferm�es && radiateurs ferm� ou en partie => on les ouvres tous
				Ct = new MessageContent(1, Constants.HEATER, ((ThermometerAgent)myAgent).place);
				
				for(int i = 0; i < nb_rad_offa.size(); i++)
					msg.addReceiver((AID)nb_rad_offa.get(i));
			
			}
		} else if (temp > threshold + threshold_d){
			if(nb_rad_ona.size() > 0){
				//radiateurs ouverts ou en partie => on les fermes
				Ct = new MessageContent(0, Constants.HEATER, ((ThermometerAgent)myAgent).place);
				
				for(int i = 0; i < nb_rad_ona.size(); i++)
					msg.addReceiver((AID)nb_rad_ona.get(i));

			} else if(nb_win_ca.size() > 0){
				//radiateurs ferm�s && fen�tres ferm�es ou en partie => on les ouvres toutes
				Ct = new MessageContent(1, Constants.WINDOW, ((ThermometerAgent)myAgent).place);
				
				for(int i = 0; i < nb_win_ca.size(); i++)
					msg.addReceiver((AID)nb_win_ca.get(i));

			}
		}
		
		if(Ct != null){
			msg.setContent(Ct.toJSON());
			System.out.println("Ordres: " + msg.getContent());
			myAgent.send(msg);
		}
	}
	
	private void ThermAskRadiator(){
		//Envoie d'une demande des valeurs des radiateurs
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		List receivers = getReceiver(Constants.HEATER, ((ThermometerAgent)myAgent).place);
		nb_el += receivers.size(); 
		
		for(int i = 0; i < receivers.size(); i++){
			message.addReceiver((AID)receivers.get(i));
			System.out.println(myAgent.getLocalName() + ": ask to " + ((AID)receivers.get(i)).getLocalName());
		}
		
		MessageContent msgCt = new MessageContent(Constants.THERMOMETER, ((ThermometerAgent)myAgent).place);
		
		message.setContent(msgCt.toJSON());
		
		myAgent.send(message);
	}
	
	private void ThermAskWindow(){
		//Envoie une demande de l'état à tout les agent window de la pièce
		
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		List receivers = getReceiver(Constants.WINDOW, ((ThermometerAgent)myAgent).place);
		nb_el += receivers.size(); 
		for(int i = 0; i < receivers.size(); i++){
			message.addReceiver((AID)receivers.get(i));
			System.out.println(myAgent.getLocalName() + ": ask to " + ((AID)receivers.get(i)).getLocalName());
		}
		
		MessageContent msgCt = new MessageContent(Constants.THERMOMETER, ((ThermometerAgent)myAgent).place);
		
		message.setContent(msgCt.toJSON());
		
		myAgent.send(message);
	}
	
	@SuppressWarnings("unused")
	private void ThermAskGUI(){
		//Envoie d'une requête à l'agent Simulation
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		List rcv = getReceiver(Constants.SIMULATION, Constants.SIMULATION_AGENT);
		
		if(rcv.size() == 0){
			System.out.println(myAgent.getLocalName() + ": Can't find " + Constants.SIMULATION_AGENT);
			return;
		}
			
		message.addReceiver((AID)rcv.get(0));
		nb_el ++;
		System.out.println(myAgent.getLocalName() + ": ask to " + ((AID)rcv.get(0)).getLocalName());
		MessageContent msgCt = new MessageContent(Constants.THERMOMETER, ((ThermometerAgent)myAgent).place, "temperature");
		
		message.setContent(msgCt.toJSON());
		
		myAgent.send(message);
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
