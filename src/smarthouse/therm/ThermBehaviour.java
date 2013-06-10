package smarthouse.therm;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ThermBehaviour extends CyclicBehaviour{

	@Override public void action()
	{
		// TODO Auto-generated method stub
		//récupérer les demandes de température
		//retourner la température régit par une loi suivant l'heure de la journée et la température du radiateur, l'état de la fenêtre (ouverte/fermée) etc.
	
	
	
	}

	public class ThermBehaviourAskWindow extends OneShotBehaviour{

		@Override public void action()
		{
			// TODO Auto-generated method stub
			//demander l'état de la fenêtre
		}
	}

	public class ThermBehaviourAskRadiator extends OneShotBehaviour{

		@Override public void action()
		{
			// TODO Auto-generated method stub
			//demander l'état du radiateur
		}
	}
}
