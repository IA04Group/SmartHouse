package smarthouse.lightsensoragent;


/*import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;*/
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import Data.Constants;
import Data.MessageContent;


// teste avec l'arduino marche niquel !!
class LightSensorBehaviour extends CyclicBehaviour implements SerialPortEventListener {
	
	SerialPort serialPort;
	    /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM5", // Windows
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	private String inputLine;
		
	
	LightSensorBehaviour(){
		
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}




	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				inputLine=input.readLine();
				// System.out.println(inputLine);
			} catch (Exception e) {
				//System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	
	@Override
	public void action() {
		
		//System.out.println(inputLine);
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);		
		ACLMessage message = myAgent.receive(template);
			
		if (message != null ) {
			ACLMessage reply = this.parse(message);
			System.out.println("light sensor ready to send");
			myAgent.send(reply);
		
		}else{
			block();
		}
		
	
	}
	

	
	private ACLMessage parse(ACLMessage message){
		
		Double value;
		if(inputLine!=null){
			 value = Double.parseDouble(inputLine);//inputLine
		}else{
			value = 0.0;
		}
		System.out.println("############################################" + value);
		// 1 pouur allumer, type de l"agent, lieu ou est la lumiere, et 0 pour l'id de la lumiere
		MessageContent messageContent = new MessageContent(value, Constants.LIGHT_SENSOR_AGENT, Constants.PLACE_OUTDOOR, "toto");
		System.out.println(messageContent);
		String json = messageContent.toJSON();
		/*DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(Constants.AUTO_SWITCH);
        sd.setName(Constants.AUTO_SWITCH_AGENT);
        template.addServices(sd);
        try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                if (result.length > 0) {
                        ACLMessage request = new ACLMessage(ACLMessage.INFORM);
                        for (DFAgentDescription receiver : result) {
                                if (!receiver.getName().equals(myAgent.getAID())) {
                                        request.addReceiver(receiver.getName());
                                       
                                }
                        }
                        request.setContent(json);
                        myAgent.send(request);
                }
        } catch(FIPAException fe) {
                fe.printStackTrace();
        }
        */
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.REQUEST);
		reply.setContent(json);

		
		return reply;
	}
	
		
		
	

	
	
}
