package spikes.priscila.mastermind;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends JFrame implements MouseListener{

	private static final long serialVersionUID = 1L;
	private BufferedImage _bufferImage;
	
	public Main(){
		setSize(300, 600);
		setTitle("Mastermind");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics graphics){
		
		Graphics2D buffer = getBuffer();
		buffer.setColor(Color.white);
		buffer.fillRect(0, 0, 300, 600);
		buffer.setColor(Color.black);
		int cont=0;
		
		for(int i = 0; i < 400; i += 40){
			buffer.drawRect(40,i+40, 40, 40);
			for(int j = 0;j < 160; j+=40){
				buffer.drawRect(j+40, cont+40, 40, 40);
			}
			cont+=40;
		}
		
		buffer.setColor(Color.green);
		buffer.fillOval(250,60, 30, 30);
		
		buffer.setColor(Color.blue);
		buffer.fillOval(250,100, 30, 30);
		
		buffer.setColor(Color.pink);
		buffer.fillOval(250,140, 30, 30);
		
		buffer.setColor(Color.yellow);
		buffer.fillOval(250,180, 30, 30);
		
		buffer.setColor(Color.red);
		buffer.fillOval(250,220, 30, 30);
		
		buffer.setColor(Color.orange);
		buffer.fillOval(250,260, 30, 30);
		
		buffer.setColor(Color.magenta);
		buffer.fillOval(250,300, 30, 30);
		
		graphics.drawImage(_bufferImage, 0, 0, this);
	}
	
	private Graphics2D getBuffer() {
			_bufferImage = (BufferedImage)createImage(300, 600);
	    	return _bufferImage.createGraphics();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() <=280 && e.getX() >= 250){
			Graphics2D buffer = getBuffer();
			buffer.setColor(Color.green);
			buffer.fillOval(250,80, 30, 30);
			System.out.println(e.getX());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		}

	@Override
	public void mousePressed(MouseEvent arg0) {
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	
	}


}
