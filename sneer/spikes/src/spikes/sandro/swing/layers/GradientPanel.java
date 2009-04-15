package spikes.sandro.swing.layers;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {

	private final Color _start;
	private final Color _end;
	
	public GradientPanel(Color start, Color end) {
		_start = start;
		_end = end;
	}

	@Override
	protected void paintComponent(Graphics g) {

		int w = getWidth();
		int h = getHeight();

		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp = new GradientPaint(0, 0, _start, 0, h, _end);

		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);

		setOpaque(false);
		super.paintComponent(g);
		setOpaque(true);
	}
}