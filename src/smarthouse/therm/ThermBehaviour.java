package smarthouse.therm;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

public class ThermBehaviour extends CyclicBehaviour{

	@Override public void action()
	{
		// TODO Auto-generated method stub
		//r�cup�rer les demandes de temp�rature
		//retourner la temp�rature r�git par une loi suivant l'heure de la journ�e et la temp�rature du radiateur, l'�tat de la fen�tre (ouverte/ferm�e) etc.
	
	
	
	}

	public class ThermBehaviourAskWindow extends OneShotBehaviour{

		@Override public void action()
		{
			// TODO Auto-generated method stub
			//demander l'�tat de la fen�tre
		}
	}

	public class ThermBehaviourAskRadiator extends OneShotBehaviour{

		@Override public void action()
		{
			// TODO Auto-generated method stub
			//demander l'�tat du radiateur
		}
	}
}
