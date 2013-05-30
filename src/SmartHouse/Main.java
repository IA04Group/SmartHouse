package SmartHouse;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			p = new ProfileImpl("config");
			ContainerController cc = rt.createMainContainer(p);
			System.out.println(rt);
			if(cc != null) {
				AgentController	agent = cc.createNewAgent("Light",
						"Light", null);
				agent.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}