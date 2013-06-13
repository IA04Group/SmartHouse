package smarthouse;

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
		p.setParameter("host", "localhost");
				
		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 1099)
		ContainerController cc = rt.createMainContainer(p);

		// Create a new agent
		// and pass it a reference to an Object
		// AgentController lightSensorAgent = cc.createNewAgent("lightSensorAgent","smarthouse.lightsensoragent.LightSensorAgent", null);
		// Fire up the agent
		// lightSensorAgent.start();
		
		// Create a new agent
		// and pass it a reference to an Object
		//AgentController lightAgentCuisine = cc.createNewAgent("lightAgentCuisine","smarthouse.LightAgent.LightAgent", null);
		// Fire up the agent
		//lightAgentCuisine.start();
		
		// Create a new agent
		// and pass it a reference to an Object
		//AgentController remoteControlAgent = cc.createNewAgent("remoteControlAgent","smarthouse.RemoteControlAgent.RemoteControlAgent", null);
		// Fire up the agent
		//remoteControlAgent.start();
		
		/*
		 * NICO's stuff
		 * 
		 */
		// agents autoswitch and lights
		AgentController autoSwitchAgent = cc.createNewAgent("autoSwitchAgent",
				"smarthouse.autoswitchagent.AutoSwitchAgent", null);
		// Fire up the agent
		autoSwitchAgent.start();
				
		AgentController light1Agent = cc.createNewAgent("lightAgent1",
				"smarthouse.lightagent.LightAgent", null);
		// Fire up the agent
		light1Agent.start();
		
		AgentController light2Agent = cc.createNewAgent("lightAgent2",
				"smarthouse.lightagent.LightAgent", null);
		// Fire up the agent
		light2Agent.start();
		
		AgentController light3Agent = cc.createNewAgent("lightAgent3",
				"smarthouse.lightagent.LightAgent", null);
		// Fire up the agent
		light3Agent.start();
		
		
		// AgentController simulation = cc.createNewAgent("simulation","smarthouse.simulation.SimulationAgent", null);
		// Fire up the agent
		// simulation.start();
		
		

		// Pour la creation du second agent sur un conteneur non principal
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
