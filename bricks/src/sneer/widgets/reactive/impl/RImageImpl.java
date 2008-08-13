package sneer.widgets.reactive.impl;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sneer.widgets.reactive.ImageWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RImageImpl extends JPanel implements ImageWidget{

	protected JButton _btnImage = new JButton();
	private final Signal<Image> _image;
	private Omnivore<Image> listener;
	private static final long serialVersionUID = 1L;
	
	RImageImpl(Signal<Image> image){
		_image = image;
		initComponents();
		addReceivers();
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		c = new GridBagConstraints(0,0,1,1,1.0,1.0,
					GridBagConstraints.EAST, 
					GridBagConstraints.BOTH,
					new Insets(0,0,0,0),0,0);
		setOpaque(false);
		Image img = _image.currentValue();
		if(img!=null)
			_btnImage.setIcon(new ImageIcon(img));
		add(_btnImage, c);
	}

	private void addReceivers() {
		_image.addReceiver(imageReceiver());
	}

	private Omnivore<Image> imageReceiver() {
		if(listener==null)
			listener = createImageReceiver();
		return listener;
	}

	private Omnivore<Image> createImageReceiver() {
		return new Omnivore<Image>() { public void consume(final Image image) {
			SwingUtilities.invokeLater(new Runnable() { public void run() {
				if(image==null)
					return; //Implement remove image from button
				_btnImage.setIcon(new ImageIcon(image));
			}});
		}};
	}

	@Override
	public Image getImage() {
		return _image.currentValue();
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public JButton getMainWidget() {
		return _btnImage;
	}
}
