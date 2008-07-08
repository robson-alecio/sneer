package sneer.skin.imageSelector.impl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.LabelUI;

import sneer.skin.image.ImageFactory;

public class ImageDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private ImageFactory _imageFactory;

	private JLabel _image;
	private AvatarPreview _avatarPreview;
	private File _file;

	public ImageDialog(File file, ImageFactory imageFactory) {
		_avatarPreview = new AvatarPreview(this);
		_image = new MyImage(_avatarPreview);
		_imageFactory = imageFactory;
		_file = file;
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Dimension desktopSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int preferredHeight = (int) (desktopSize.height*0.8);
		int preferredWidth = (int) (desktopSize.width*0.8);

		setBounds((desktopSize.width-preferredWidth)/2,
				(desktopSize.height-preferredHeight)/2,
				preferredWidth,preferredHeight);
		
		ImageIcon icon = getIcon(_file, preferredHeight, preferredWidth);		
		_image.setIcon(icon);
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));
		getContentPane().add(_image);
		
		setSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
		addSizeListener();
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					setVisible(true);
				}
			}
		);		
	}

	private void addSizeListener() {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				_image.setVisible(false);
				SwingUtilities.invokeLater(
					new Runnable() {
						@Override
						public void run() {
							_image.setIcon(getIcon(_file, getHeight(), getWidth()));
							_image.setVisible(true);
							_avatarPreview.resizeAvatarPreview();
							_avatarPreview.setVisible(true);
						}
					}
				);
			}
			
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				_avatarPreview.resizeAvatarPreview();
			}
		});
	}
	
	private ImageIcon getIcon(final File file, int height, int width) {
		ImageIcon icon = _imageFactory.getIcon(file, false);
		if (icon.getIconWidth() > width) {
			icon = new ImageIcon(
				icon.getImage().getScaledInstance(
					width, -1, 
					Image.SCALE_DEFAULT));
			
			if (icon.getIconHeight() > height) {
				icon = new ImageIcon(
					icon.getImage().getScaledInstance(-1, 
						height, 
						Image.SCALE_DEFAULT));
			}
		}
		return icon;
	}
}

class MyImage extends JLabel{

	private static final long serialVersionUID = 1L;
	private final AvatarPreview _avatarPreview;
	
	public MyImage(AvatarPreview avatarPreview) {
		_avatarPreview = avatarPreview;
		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				repaint();
			}
		};
		_avatarPreview.top.getModel().addChangeListener(changeListener);
		_avatarPreview.botton.getModel().addChangeListener(changeListener);
		_avatarPreview.left.getModel().addChangeListener(changeListener);
		_avatarPreview.right.getModel().addChangeListener(changeListener);
		_avatarPreview.cropCheck.getModel().addChangeListener(changeListener);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getIcon().getIconWidth(),getIcon().getIconHeight());
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}	
	
	@Override
	public void setUI(final LabelUI old) {
		LabelUI wrapper = new LabelUI(){

			@Override
			public void paint(Graphics g, JComponent c) {
				old.paint(g, c);
		        g.setColor(new Color(50, 50, 50, 150));
		        
		        
		        int top2 = (int) (c.getHeight()*getTop());
		        int botton2 = (int) (c.getHeight()*(1-getBotton()));

		        g.fillRect(0, 0, c.getWidth(), top2);
				g.fillRect(0, botton2, c.getWidth(), c.getHeight());
				
		        g.fillRect(0, top2, (int) (c.getWidth()*getLeft()), botton2-top2);
				g.fillRect((int) (c.getWidth()*(1-getRight())), top2, c.getWidth(), botton2-top2);
		        
		    }

			private double getRight() {
				return getDoubleValue(_avatarPreview.right.getValue());
			}

			private double getLeft() {
				return getDoubleValue(_avatarPreview.left.getValue());
			}

			private double getBotton() {
				return getDoubleValue(_avatarPreview.botton.getValue());
			}

			private double getTop() {
				return getDoubleValue(_avatarPreview.top.getValue());
			}
			
			private double getDoubleValue(double value) {
				if(_avatarPreview.cropCheck.isSelected())
					return (value/10000);
				return 0;
			}
		};
		super.setUI(wrapper);
	}
}