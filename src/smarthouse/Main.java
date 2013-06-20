package smarthouse;

import Data.Constants;
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

		String place = null;

		AgentController simulation = null;
		AgentController planning = null;
		AgentController therm = null;
		AgentController rad = null;
		AgentController vol = null;
		AgentController win = null;

		// Create a new agent
		// and pass it a reference to an Object
		AgentController lightSensorAgent = cc.createNewAgent("lightSensorAgent","smarthouse.lightsensoragent.LightSensorAgent", null);
		// Fire up the agent
		lightSensorAgent.start();
		
		// Create a new agent
		// and pass it a reference to an Object
		//AgentController lightAgentCuisine = cc.createNewAgent("lightAgentCuisine","smarthouse.LightAgent.LightAgent", null);
		// Fire up the agent
		//lightAgentCuisine.start();
		
		// Create a new agent
		// and pass it a reference to an Object
		AgentController remoteControlAgent = cc.createNewAgent("remoteControlAgent","smarthouse.RemoteControlAgent.RemoteControlAgent", null);
		// Fire up the agent
		remoteControlAgent.start();
		
		/*
		 * NICO's stuff
		 * 
		 */
		// agents autoswitch and lights
		AgentController autoSwitchAgent = cc.createNewAgent(Constants.AUTO_SWITCH_AGENT,
				"smarthouse.autoswitchagent.AutoSwitchAgent", null);
		// Fire up the agent
		autoSwitchAgent.start();

		String[] param = new String[1];
		
		param[0] = Constants.PLACE_BEDROOM;
		AgentController light1Agent = cc.createNewAgent("light_bedroom1",
				"smarthouse.lightagent.LightAgent", param);
		// Fire up the agent
		light1Agent.start();
		
		param[0] = Constants.PLACE_BEDROOM;
		AgentController light2Agent = cc.createNewAgent("light_bedroom2",
				"smarthouse.lightagent.LightAgent", param);
		// Fire up the agent
		light2Agent.start();
		
		param[0] = Constants.PLACE_LIVINGROOM;
		AgentController light3Agent = cc.createNewAgent("light_livingroom1",
				"smarthouse.lightagent.LightAgent", param);
		// Fire up the agent
		light3Agent.start();
		
		param[0] = Constants.PLACE_KITCHEN;
		AgentController light4Agent = cc.createNewAgent("light_kitchen1",
				"smarthouse.lightagent.LightAgent", param);
		// Fire up the agent
		light4Agent.start();
		
		/*
		 * 
		 * END NICO's stuff
		 */
		
		/* === No Place === */
		simulation = cc.createNewAgent("simulation","smarthouse.simulation.SimulationAgent", null);
		simulation.start();

		planning = cc.createNewAgent("planning","smarthouse.planning.PlanningAgent", null);
		planning.start();
		
		/* === Living Room === */
		place = Constants.PLACE_LIVINGROOM;
		therm = cc.createNewAgent(Constants.THERMOMETER_AGENT + " " + place,"smarthouse.therm.ThermometerAgent", new Object[]{Constants.PLACE_LIVINGROOM});
		therm.start();
		
		win = cc.createNewAgent("Window 1 " + place,"smarthouse.window.WindowAgent", new Object[]{place, 0});
		win.start();
		win = cc.createNewAgent("Window 2 " + place,"smarthouse.window.WindowAgent", new Object[]{place, 1});
		win.start();
		
		rad = cc.createNewAgent("Heater 1 " + place,"smarthouse.heater.HeaterAgent", new Object[]{place, 0});
		rad.start();
		rad = cc.createNewAgent("Heater 2 " + place,"smarthouse.heater.HeaterAgent", new Object[]{place, 1});
		rad.start();

		vol = cc.createNewAgent("Shutter 1 " + place,"smarthouse.shutter.ShutterAgent", new Object[]{place, 0});
		vol.start();
		vol = cc.createNewAgent("Shutter 2 " + place,"smarthouse.shutter.ShutterAgent", new Object[]{place, 1});
		vol.start();
		
		/* === Kitchen === */
		place = Constants.PLACE_KITCHEN;
		therm = cc.createNewAgent(Constants.THERMOMETER_AGENT + " " + place,"smarthouse.therm.ThermometerAgent", new Object[]{place});
		therm.start();

		win = cc.createNewAgent("Window 1 " + place,"smarthouse.window.WindowAgent", new Object[]{place, 0});
		win.start();

		rad = cc.createNewAgent("Heater 1 " + place,"smarthouse.heater.HeaterAgent", new Object[]{place, 0});
		rad.start();

		vol = cc.createNewAgent("Shutter 1 " + place,"smarthouse.shutter.ShutterAgent", new Object[]{place, 0});
		vol.start();
		
		/* === Bedroom === */
		place = Constants.PLACE_BEDROOM;
		therm = cc.createNewAgent(Constants.THERMOMETER_AGENT + " " + place,"smarthouse.therm.ThermometerAgent", new Object[]{place});
		therm.start();

		win = cc.createNewAgent("Window 1 " + place,"smarthouse.window.WindowAgent", new Object[]{place, 0});
		win.start();

		rad = cc.createNewAgent("Heater 1 " + place,"smarthouse.heater.HeaterAgent", new Object[]{place, 0});
		rad.start();

		vol = cc.createNewAgent("Shutter 1 " + place,"smarthouse.shutter.ShutterAgent", new Object[]{place, 0});
		vol.start();

		/*
		// Pour la creation du second agent sur un conteneur non principal
		// utiliser cette fonction sinon de/commenter
		// en reseau exactemment la meme chose en remplacant l'IP
		// par le localhost de la machine distante.
		// createNotmain();
		*/
	}
	
	public static void createNotmain() throws StaleProxyException{
		Runtime rt = Runtime.instance();

		Profile p = new ProfileImpl("localhost", -1, null, false);
		/*ContainerController cc =*/ rt.createAgentContainer(p);
		//AgentController queryAgent = cc.createNewAgent("QueryAgent2","td5.agent.QueryAgent", null);
		// Fire up the agent
		//queryAgent.start();

	}

}
