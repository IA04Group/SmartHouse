package smarthouse.simulation;

import Data.Constants;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class SimulationAgent extends GuiAgent {
	private SimulationFrame window;

	public SimulationAgent() {
		super();
	}

	protected void setup(){
		window = new SimulationFrame(this);

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.SIMULATION);
		sd.setName(Constants.SIMULATION_AGENT);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new ReceiveInformBehaviour());
		addBehaviour(new ReceiveRequestBehaviour());
	}

	public SimulationFrame getWindow() {
		return window;
	}

	protected void onGuiEvent(GuiEvent event) {
		//addBehaviour(new SendMessageBehaviour((String) event.getParameter(0)));
	}
}
