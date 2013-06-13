package smarthouse.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Heater extends JComponent implements MouseListener {
	private boolean on = false;
	private boolean vertical = false;
	private Room room;

	public Heater(Room room, int size, boolean vertical) {
		super();
		this.room = room;
		if (vertical) {
			setSize(11, size);
		} else {
			setSize(size, 11);
		}
		this.vertical = vertical;
		addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (on) {
			g.setColor(Color.ORANGE);
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		if (vertical) {
			g.fillRect(2, 2, 1, getHeight() - 4);
			g.fillRect(4, 2, 1, getHeight() - 4);
			g.fillRect(6, 2, 1, getHeight() - 4);
			g.fillRect(8, 2, 1, getHeight() - 4);
		} else {
			g.fillRect(2, 2, getWidth() - 4, 1);
			g.fillRect(2, 4, getWidth() - 4, 1);
			g.fillRect(2, 6, getWidth() - 4, 1);
			g.fillRect(2, 8, getWidth() - 4, 1);
		}
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public boolean isOn() {
		return on;
	}

	public void mouseClicked(MouseEvent e) {
		on = !on;
		room.recalculateTempPerMin();
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
