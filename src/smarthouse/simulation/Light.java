package smarthouse.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Light extends JComponent implements MouseListener {
	private boolean on = false;
	private Room room;

	public Light(Room room, int size) {
		super();
		this.room = room;
		setSize(size, size);
		addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (on) {
			g.setColor(Color.YELLOW);
		} else {
			g.setColor(Color.DARK_GRAY);
		}

		g.fillOval(0, 0, getWidth(), getHeight());
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public boolean isOn() {
		return on;
	}

	public void mouseClicked(MouseEvent e) {
		on = !on;
		room.recalculateLightLevel();
		room.repaint();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
