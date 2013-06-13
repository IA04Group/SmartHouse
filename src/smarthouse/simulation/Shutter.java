package smarthouse.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Shutter extends JComponent implements MouseListener {
	private boolean open = false;
	private boolean vertical = false;
	private Room room;

	public Shutter(Room room, int size, boolean vertical) {
		super();
		this.room = room;
		if (vertical) {
			setSize(5, size);
		} else {
			setSize(size, 5);
		}
		this.vertical = vertical;
		addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.GRAY);

		if (open) {
			if (vertical) {
				int h = getHeight() / 10;
				g.fillRect(0, 0, getWidth(), h);
				g.fillRect(0, getHeight() - h, getWidth(), h);
			} else {
				int w = getWidth() / 10;
				g.fillRect(0, 0, w, getHeight());
				g.fillRect(getWidth() - w, 0, w, getHeight());
			}
		} else {
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
	}

	public void mouseClicked(MouseEvent e) {
		open = !open;
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
