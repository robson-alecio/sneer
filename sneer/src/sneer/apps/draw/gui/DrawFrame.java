package sneer.apps.draw.gui;

import static wheel.i18n.Language.translate;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import sneer.apps.draw.DrawPacket;
import sneer.apps.draw.packet.BrushPacket;
import sneer.apps.draw.packet.ClearPacket;
import sneer.apps.draw.packet.ColorPacket;
import sneer.apps.draw.packet.StrokePacket;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class DrawFrame extends JFrame {

	private static final int IMAGE_HEIGHT = 400;
	private static final int IMAGE_WIDTH = 500;
	private static final int BUTTON_WIDTH = 75;

	public DrawFrame(Signal<String> otherGuysNick, final Signal<DrawPacket> drawInput, Omnivore<DrawPacket> drawOutput) {
		_otherGuysNick = otherGuysNick;
		_drawOutput = drawOutput;

		initComponents();

		_otherGuysNick.addReceiver(new Omnivore<String>() { @Override
			public void consume(String nick) {
				setTitle(nick);
			}
		});
		
		drawInput.addReceiver(new Omnivore<DrawPacket>() { @Override
			public void consume(DrawPacket drawPacket) {
				switch(drawPacket.type()){
					case DrawPacket.BRUSH:
						BrushPacket brushPacket = (BrushPacket)drawPacket;
						drawLine(brushPacket._beginX,brushPacket._beginY,brushPacket._endX,brushPacket._endY);
						break;
					case DrawPacket.CLEAR:
						clearDrawingArea();
						break;
					case DrawPacket.COLOR:
						setColor(((ColorPacket)drawPacket)._color);
						break;
					case DrawPacket.STROKE:
						setStrokeSize(((StrokePacket)drawPacket)._size);
						break;
				}
			}
		});
		setVisible(true);
	}

	private final Signal<String> _otherGuysNick;
	private final Omnivore<DrawPacket> _drawOutput;
	
	private final DrawingArea area = new DrawingArea();
	
	private BufferedImage _bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private Graphics2D _g2d;
	private Stroke _stroke = new BasicStroke(5, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	private Color _color = Color.black;
	
	private int _lastX;
	private int _lastY;
	 
	private void initComponents() {
		clearDrawingArea();
		setLayout(new BorderLayout());
		setSize(IMAGE_WIDTH + BUTTON_WIDTH, IMAGE_HEIGHT);
		
        JButton clearButton = new JButton("Clear");
        prepareComponent(clearButton);
        clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clearAndConsume();
			}
        });
        
        JButton saveButton = new JButton("Save");
        prepareComponent(saveButton);
        saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(); //Refactor: Move to User.
				fc.setDialogTitle(translate("Saving file"));
				fc.setApproveButtonText(translate("Save"));
				int value = fc.showSaveDialog(null);
				if (value != JFileChooser.APPROVE_OPTION) return;
				File file = fc.getSelectedFile();
				try {
		            ImageIO.write(_bufferedImage, "jpg", file);
		        } catch (IOException ex) {
		        }
			}
        });
        
        JComboBox sizeBox = sizeBox();
        sizeBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				setStrokeSizeAndConsume(Integer.parseInt(e.getItem().toString()));
			}
        });
        
        final JButton colorButton = new JButton("Color");
        colorButton.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2),new LineBorder(Color.white)));
        prepareComponent(colorButton);
        colorButton.setBackground(Color.black);
        colorButton.setForeground(Color.white);
        colorButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		 Color color= JColorChooser.showDialog(null,"Choose Color",_color);
        		 if (color != null){
        		        setColorAndConsume(color);
        		        colorButton.setBackground(color);
        		 }
			}
        });
  
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.black);
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));
        topPanel.add(clearButton);
        topPanel.add(sizeBox);
        topPanel.add(colorButton);
        topPanel.add(saveButton);
        
        add(topPanel,BorderLayout.WEST);
        add(area, BorderLayout.CENTER);
        
        area.addMouseListener(new MouseAdapter() {
            @Override
			public void mousePressed(MouseEvent e) {
            	_lastX = e.getX();
            	_lastY = e.getY();
            	drawAndConsume(_lastX,_lastY,_lastX,_lastY);
            }
        });
        
        area.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
			public void mouseDragged(MouseEvent e) {
            	int currentX = e.getX();
            	int currentY = e.getY();
            	drawAndConsume(_lastX,_lastY,currentX, currentY);
                _lastX = currentX;
            	_lastY = currentY;
            }
        });
		
	}
	
	private void drawAndConsume(int beginX, int beginY, int endX, int endY) {
		drawLine(beginX,beginY,endX,endY);
		BrushPacket brush = new BrushPacket(beginX,beginY,endX,endY);
        _drawOutput.consume(brush);
	}
	
	private void clearAndConsume() {
		clearDrawingArea();
		ClearPacket clearPacket = new ClearPacket();
		_drawOutput.consume(clearPacket);
	}
	
	private void setStrokeSizeAndConsume(int size) {
		setStrokeSize(size);
		StrokePacket strokePacket = new StrokePacket(size);
		_drawOutput.consume(strokePacket);
	}
	
	private void setColorAndConsume(Color color) {
		setColor(color);
		ColorPacket colorPacket = new ColorPacket(color);
		_drawOutput.consume(colorPacket);
	}
	
	
	private void clearDrawingArea() {
		_g2d = _bufferedImage.createGraphics();
		_g2d.setColor(Color.white);
		_g2d.fillRect(0,0,IMAGE_WIDTH,IMAGE_HEIGHT);
		_g2d.setColor(_color);
		area.repaint();
	}
	
	private void setStrokeSize(int size){
		_stroke = new BasicStroke(size, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	}
	
	private void setColor(Color color) {
		_color = color;
	}
	
	public void close() {
		dispose();
	}
	
	public class DrawingArea extends JPanel {
		public DrawingArea(){
			setDoubleBuffered(true);
            setBackground(Color.white);
        }
        @Override
		public void paintComponent(Graphics g) {
        	super.paintComponent(g);
            g.drawImage(_bufferedImage,0,0,null);
        }
        private static final long serialVersionUID = 1L;  
    }
	
	private void drawLine(int beginX, int beginY, int endX, int endY) {
    	_g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	_g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    	_g2d.setStroke(_stroke);
    	_g2d.setColor(_color);
    	_g2d.drawLine(beginX,beginY,endX,endY);
    	area.repaint();
	}
	
	public JComboBox sizeBox() {
		Object[] sizes = new Object[] { "5", "10", "25", "50" };
		JComboBox sizeBox = new JComboBox(sizes);
		prepareComponent(sizeBox);
		return sizeBox;
	}

	public void prepareComponent(JComponent component) {
		component.setMaximumSize(new Dimension(BUTTON_WIDTH, 20));
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	private static final long serialVersionUID = 1L;
}
