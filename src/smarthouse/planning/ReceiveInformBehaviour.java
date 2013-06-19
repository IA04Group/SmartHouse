package smarthouse.planning;

import java.util.HashMap;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import Data.Constants;
import Data.MessageContent;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ReceiveInformBehaviour extends CyclicBehaviour {
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage message = myAgent.receive(mt);
		if ( message != null ) {
			parse(message);
		} else {
			block();
		}
	}
	
	private void parse(ACLMessage mess){
		MessageContent json = new MessageContent(mess);
		String type = json.getContent().get(0);
		if (type == null) {
			return;
		}
		if (type.equals("time") && json.getContent().size() >= 4) {
			int day =Integer.parseInt(json.getContent().get(1));
			int hour = Integer.parseInt(json.getContent().get(2));
			int min = Integer.parseInt(json.getContent().get(3));
			List<HashMap<String, RDFNode>> starting = ((PlanningAgent) myAgent).runExecQuery(
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
"PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n" +
"PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
"PREFIX sh:   <http://utc.fr/smarthouse#>\n" +

"SELECT DISTINCT ?event ?name\n" +
"WHERE {\n" +
"	?a\n" +
"		a sh:Event;\n" +
"		sh:summary ?event;\n" +
"		sh:owner ?owner;\n" +
"		sh:starts ?startingdate.\n" +

"	?startingdate\n" +
"		a sh:Datetime;\n" +
"		sh:day \"" + Constants.dayOfWeek[day] + "\";\n" +
"		sh:hour " + hour + ";\n" +
"		sh:minute " + min + " .\n" +

"	?owner\n" +
"		a sh:Person;\n" +
"		sh:name ?name.\n" +
"}");
			for (HashMap<String, RDFNode> map : starting) {
				sendEventLog("start",
						map.get("event").asLiteral().getString(),
						map.get("name").asLiteral().getString());
			}
			List<HashMap<String, RDFNode>> ending = ((PlanningAgent) myAgent).runExecQuery(
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
"PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n" +
"PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
"PREFIX sh:   <http://utc.fr/smarthouse#>\n" +

"SELECT DISTINCT ?event ?name\n" +
"WHERE {\n" +
"	?a\n" +
"		a sh:Event;\n" +
"		sh:summary ?event;\n" +
"		sh:owner ?owner;\n" +
"		sh:ends ?endingdate.\n" +

"	?endingdate\n" +
"		a sh:Datetime;\n" +
"		sh:day \"" + Constants.dayOfWeek[day] + "\";\n" +
"		sh:hour " + hour + ";\n" +
"		sh:minute " + min + " .\n" +

"	?owner\n" +
"		a sh:Person;\n" +
"		sh:name ?name.\n" +
"}");
			for (HashMap<String, RDFNode> map : ending) {
				sendEventLog("end",
						map.get("event").asLiteral().getString(),
						map.get("name").asLiteral().getString());
			}
		}
	}
	
	private void sendEventLog(String verb, String event, String owner) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		MessageContent json = new MessageContent(Constants.PLANNING_AGENT,
				Constants.PLACE_OUTDOOR,
				Constants.ACTION_LOG_APPEND,
				"the event " + event + " for " + owner + " has " + verb + "ed.");
		msg.setContent(json.toJSON());
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.SIMULATION);
		sd.setName(Constants.SIMULATION_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			if (result.length > 0) {
				for (DFAgentDescription receiver : result) {
					msg.addReceiver(receiver.getName());
				}
				myAgent.send(msg);
			}
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}
}
