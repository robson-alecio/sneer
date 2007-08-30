package spikes.priscila;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics gr) {
		Graphics2D gr2D = (Graphics2D) gr;

		gr2D.setBackground(Color.white);
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		gr2D.setRenderingHints(renderHints);
		gr2D.setColor(Color.PINK);
		gr2D.fillRect(0, 0, 80, 80);

		gr2D.setColor(Color.green);
		gr2D.fillOval(0, 0, 70, 70);

		gr2D.setColor(new Color(220,220,100));
		gr2D.fillOval(0, 0, 60, 60);

		Line2D line = new Line2D.Double(10, 10, 40, 40);
		gr2D.setColor(Color.black);
		gr2D.draw(line);
		Rectangle2D rect = new Rectangle2D.Double(20, 20, 100, 100);
		gr2D.draw(rect);
		gr2D.setPaint(new GradientPaint(0, 0, new Color(220,220,100), 50, 25, Color.white,
				true));
		gr2D.fill(rect);

	}

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(0, 0, 200, 200);

		// JPanel panel= new JPanel();

		// setContentPane(panel);
		setVisible(true);

	}

	public static void main(String[] args) {
		new Main();

	}

}
