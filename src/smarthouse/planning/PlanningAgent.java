package smarthouse.planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import Data.Constants;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class PlanningAgent extends Agent {
	private Model model;

	protected void setup() {
		super.setup();

		DFAgentDescription df = new DFAgentDescription();
		df.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.PLANNING);
		sd.setName(Constants.PLANNING_AGENT);
		df.addServices(sd);
		try {
			DFService.register(this, df);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		} 

		try {
			model = ModelFactory.createDefaultModel();
			model.read("file:./ontologies/time.n3", null, "N3");
			model.read("file:./ontologies/events.n3", null, "N3");
		} catch(Exception ex) { 
			ex.printStackTrace();
		}
		addBehaviour(new ReceiveInformBehaviour());
	}
	
	public List<HashMap<String, RDFNode>> runExecQuery(String mess) {
		Query query = QueryFactory.create(mess);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet results = qexec.execSelect();
		List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<HashMap<String, RDFNode>> out = new ArrayList<HashMap<String, RDFNode>>();
		for (QuerySolution row : list) {
			HashMap<String, RDFNode> map = new HashMap<String, RDFNode>();
			Iterator<String> vars = row.varNames();
			while (vars.hasNext()) {
				String var = vars.next();
				map.put(var, row.get(var));
			}
			out.add(map);
		}
		qexec.close();
		return out;
	}

	public Model getModel(){
		return model;
	}
}