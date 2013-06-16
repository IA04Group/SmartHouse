package smarthouse.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class Room extends JComponent {
	private Color color;
	private List<Light> lights = new ArrayList<Light>();
	private List<Shutter> shutters = new ArrayList<Shutter>();
	private List<Heater> heaters = new ArrayList<Heater>();
	private List<Window> windows = new ArrayList<Window>();
	private int lightlevel = 0;
	private boolean dayTime = false;
	private JLabel thermometer = null;
	private double temp = 18;
	private double outTemp = 10;
	private double tempPerMin = 0;

	public Room(int width, int height, Color color) {
		super();
		setSize(width, height);
		this.color = color;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// TODO: use background image
		Color c = color;
		for (int i = 0; i < 2 - lightlevel; ++i) {
			c = c.darker();
		}
		g.setColor(c);
		g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
	}

	public void tick() {
		recalculateTempPerMin();
		temp += tempPerMin;
		if (thermometer != null) {
			thermometer.setText(String.format("%.1f°C", temp));
		}
	}

	public void recalculateLightLevel() {
		lightlevel = 0;
		for (Light l : lights) {
			if (l.isOn()) {
				lightlevel += 1;
			}
		}
		if (dayTime) {
			for (Shutter s : shutters) {
				if (s.isOpen()) {
					lightlevel += 1;
				}
			}
		}
		if (lightlevel > 2) {
			lightlevel = 2;
		}
	}

	public void recalculateTempPerMin() {
		tempPerMin = 0;
		for (Heater h : heaters) {
			if (h.isOn()) {
				tempPerMin += 0.1;
			}
		}
		for (Window w : windows) {
			if (w.isOpen()) {
				double deltaT = outTemp - temp;
				tempPerMin +=  Math.abs(deltaT) * deltaT * 0.02;
			}
		}
		if (temp > 37.5 && tempPerMin > 0) {
			tempPerMin -= Math.pow(temp - 37.5, 2) * 0.01;
		}
	}

	public double getTemperature() {
		return temp;
	}

	public double getLightLevel() {
		return lightlevel;
	}

	public void setDay(boolean day) {
		if (day != dayTime) {
			dayTime = day;
			if (dayTime) {
				outTemp = 25;
			} else {
				outTemp = 10;
			}
			recalculateLightLevel();
			recalculateTempPerMin();
			repaint();
		}
	}

	public void addThermometer(JLabel t) {
		thermometer = t;
	}

	public void addLight(int x, int y, int size) {
		Light l = new Light(this, size);
		l.setLocation(x, y);
		add(l);
		lights.add(l);
		recalculateLightLevel();
	}

	public void setLight(int id, boolean on) {
		if (lights.size() < id) {
			System.out.println("Light " + id + " not found");
			return;
		}
		Light l = lights.get(id);
		if (l.isOn() != on) {
			lights.get(id).setOn(on);
			recalculateLightLevel();
			repaint();
		}
	}

	public double getLightStatus(int id) {
		if (lights.size() < id) {
			System.out.println("Light " + id + " not found");
			return 0;
		}
		return lights.get(id).isOn() ? 1 : 0;
	}

	public void addShutter(int x, int y, int size, boolean vertical) {
		Shutter s = new Shutter(this, size, vertical);
		s.setLocation(x, y);
		add(s);
		shutters.add(s);
		recalculateLightLevel();
	}

	public void setShutter(int id, boolean open) {
		if (shutters.size() < id) {
			System.out.println("Shutter " + id + " not found");
			return;
		}
		Shutter s = shutters.get(id);
		if (s.isOpen() != open) {
			s.setOpen(open);
			recalculateLightLevel();
			repaint();
		}
	}

	public double getShutterStatus(int id) {
		if (shutters.size() < id) {
			System.out.println("Shutter " + id + " not found");
			return 0;
		}
		return shutters.get(id).isOpen() ? 1 : 0;
	}

	public void addHeater(int x, int y, int size, boolean vertical) {
		Heater h = new Heater(this, size, vertical);
		h.setLocation(x, y);
		add(h);
		heaters.add(h);
		recalculateTempPerMin();
	}

	public void setHeater(int id, boolean on) {
		if (heaters.size() < id) {
			System.out.println("Heater " + id + " not found");
			return;
		}
		Heater h = heaters.get(id);
		if (h.isOn() != on) {
			h.setOn(on);
			recalculateTempPerMin();
			repaint();
		}
	}

	public double getHeaterStatus(int id) {
		if (heaters.size() < id) {
			System.out.println("Heater " + id + " not found");
			return 0;
		}
		return heaters.get(id).isOn() ? 1 : 0;
	}

	public void addWindow(int x, int y, int size, boolean vertical) {
		Window w = new Window(this, size, vertical);
		w.setLocation(x, y);
		add(w);
		windows.add(w);
		recalculateTempPerMin();
	}

	public void setWindow(int id, boolean open) {
		if (windows.size() < id) {
			System.out.println("Window " + id + " not found");
			return;
		}
		Window w = windows.get(id);
		if (w.isOpen() != open) {
			w.setOpen(open);
			recalculateTempPerMin();
			repaint();
		}
	}

	public double getWindowStatus(int id) {
		if (shutters.size() < id) {
			System.out.println("Window " + id + " not found");
			return 0;
		}
		return windows.get(id).isOpen() ? 1 : 0;
	}
}
