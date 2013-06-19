package Data;

public class Constants {
	/*
	 * Simulation Settings
	 */
	public static final double DAY_LIGHT_POWER = 1;
	public static final double LIGHT_POWER = 1;
	public static final double MAX_LIGHT_LEVEL = 2;
	public static final double HEATER_POWER = 0.1;
	public static final double TEMPERATURE_TRANSFER_RATE = 0.02;
	public static final double TEMPRATE_THRESHOLD = 37.5;
	public static final double DISMINUSHING_TEMPRATE = 0.02;
	public static final double INITIAL_TEMP = 18;
	public static final double NIGHT_OUTDOOR_TEMPERATURE = 10;
	public static final double DAY_OUTDOOR_TEMPERATURE = 25;


	/*
	 * Number of lights that can subscribe on AutoSwitch
	 */
	public static final int NBR_MAX_LIGHTS = 3;


	/*
	 * agents
	 */
	public static final String AUTO_SWITCH_AGENT = "AutoSwitchAgent";
	public static final String AUTO_SWITCH = "AutoSwitch";
	public static final String LIGHT_SENSOR_AGENT = "LightSensorAgent";
	public static final String LIGHT_SENSOR = "LightSensor";
	public static final String REMOTE_CONTROL = "RemoteControl";
	public static final String REMOTE_CONTROL_AGENT = "RemoteControlAgent";
	public static final String LIGHT_AGENT = "LightAgent";
	public static final String SIMULATION_AGENT = "SimulationAgent";
	public static final String SIMULATION = "Simulation";
	public static final String PLANNING_AGENT = "PlanningAgent";
	public static final String PLANNING = "planning";
	
	
	/* 
	 * places
	 */
	public static final int NBR_PLACES = 5;
	public static final String PLACE_OUTDOOR = "outdoor";
	public static final String PLACE_BEDROOM = "bedroom";
	public static final String PLACE_LIVINGROOM = "livingroom";
	public static final String PLACE_KITCHEN = "kitchen";
	public static final String PLACE_RANDOM = "random";

	  
	/*
	 * furniture
	 */
	public static final String WINDOW = "window";
	public static final String SHUTTER = "shutter";
	public static final String HEATER = "heater";
	public static final String LIGHT = "light";
	
	
	/*
	 * REMOTE CONTROL CODE
	 */
	public static final String BUTTON_1_ON = "4128960";
	public static final String BUTTON_1_OFF = "4128768";
	public static final String BUTTON_2_ON = "983232";
	public static final String BUTTON_2_OFF = "983040";
	public static final String BUTTON_3_ON = "3342528";
	public static final String BUTTON_3_OFF = "3342336";
	public static final String BUTTON_4_ON = "196800";
	public static final String BUTTON_4_OFF = "196608";
	public static final String BUTTON_5_ON = "3932352";
	public static final String BUTTON_5_OFF = "3932160";
	
	
	/*
	 * ACTIONS
	 */
	public static final String ACTION_LOG_APPEND = "logAppend";


	/*
	 * Gui Event
	 */
	public static final int GUI_EVENT_TIME = 0;


	/*
	 * Time
	 */
	public static final String[] dayOfWeek = {
		"Monday",
		"Tuesday",
		"Wednesday",
		"Thursday",
		"Friday",
		"Saturday",
		"Sunday"};


}
