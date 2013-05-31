package SmartHouse;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {

	public static void main(String[] args) throws StaleProxyException {
		// Get a hold on JADE runtime
		Runtime rt = Runtime.instance();
		// Create a default profile
		Profile p = new ProfileImpl();
		p.setParameter("main","true");
		p.setParameter("gui", "true");
		// p.setParameter("host", "localhost");
				
		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 1099)
		ContainerController cc = rt.createMainContainer(p);

		// Create a new agent
		// and pass it a reference to an Object
		AgentController lightAgent = cc.createNewAgent("lightSensor","LightSensorAgent.LightSensorAgent", null);
		// Fire up the agent
		lightAgent.start();
		
		// Pour la création du second agent sur un conteneur non principal
		// utiliser cette fonction sinon de/commenter
		// en reseau exactemment la meme chose en remplacant l'IP
		// par le localhost de la machine distante.
		// createNotmain();
		

		
	}
	
	public static void createNotmain() throws StaleProxyException{
		Runtime rt = Runtime.instance();

		Profile p = new ProfileImpl("localhost", -1, null, false);
		ContainerController cc = rt.createAgentContainer(p);
		//AgentController queryAgent = cc.createNewAgent("QueryAgent2","td5.agent.QueryAgent", null);
		// Fire up the agent
		//queryAgent.start();

	}

}
