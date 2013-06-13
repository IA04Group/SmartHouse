package smarthouse.RemoteControlAgent;


/*import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;*/
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import smarthouse.lightagent.LightAgent;
import Data.MessageContent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


// teste avec l'arduino marche niquel !!
class RemoteControlBehaviour extends CyclicBehaviour /*implements SerialPortEventListener */ implements SerialPortEventListener{
	
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
		
	
	RemoteControlBehaviour(){
		
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

	@Override
	public void action() {
		
		// TODO Auto-generated method stub
		System.out.println(inputLine);
		jade.lang.acl.ACLMessage msg=myAgent.receive();
		if(msg!=null){
			System.out.println(inputLine);
		}else{
			block();
		}
		
	
		
	
	}


	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				inputLine=input.readLine();
				System.out.println(inputLine);
				this.sendSignal();
			} catch (Exception e) {
				//System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	private void sendSignal(){
		
		ACLMessage reponse = new ACLMessage(ACLMessage.INFORM);
		ObjectMapper objectMapper = new ObjectMapper();
		Data.MessageContent messageContent  = new MessageContent(0, "User remote", "");
		String toGUI;
	
		try {
			toGUI = objectMapper.writeValueAsString(messageContent);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			toGUI= "";
			e.printStackTrace();
		}
		
		
		reponse.setContent(toGUI);
		// provisore faire page blanche
		reponse.addReceiver(new AID("lightAgentCuisine"));
		System.out.println(reponse);
		myAgent.send(reponse);
		
	}
		
	

	
	
}
