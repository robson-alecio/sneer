package sneer.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sneer.skin.image.ImageFactory;
import sneer.widgets.reactive.ImageWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RImageImpl extends JPanel implements ImageWidget{

	private final Signal<Image> _image;
	private Image _lastImage;
	private Omnivore<Image> _listener;
	private static final long serialVersionUID = 1L;
	private final ImageFactory _imageFactory;
	
	RImageImpl(Signal<Image> image, ImageFactory imageFactory){
		_image = image;
		_imageFactory = imageFactory;
		addReceivers();
	}

	private void addReceivers() {
		_image.addReceiver(imageReceiver());
	}

	private Omnivore<Image> imageReceiver() {
		if(_listener==null)
			_listener = createImageReceiver();
		return _listener;
	}

	private Omnivore<Image> createImageReceiver() {
		return new Omnivore<Image>() {
			public void consume(final Image image) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						_lastImage = image;
						validate();
						repaint();
					}
				});
			}
		};
	}

	@Override
	public Dimension getMaximumSize() {
		Dimension size;
		if(_lastImage==null)
			size = new Dimension(32, 32);
		else
			size = new Dimension(_lastImage.getWidth(null), _lastImage.getHeight(null));
		return size;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getMaximumSize();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getMaximumSize();
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
	public JComponent getMainWidget() {
		return this;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(_lastImage==null){
			_lastImage = _imageFactory.getIcon(this.getClass(), "sneer48.png").getImage();
		}

        Graphics2D g2 = (Graphics2D)g;
        int newW = _lastImage.getWidth(null);
        int newH = _lastImage.getHeight(null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(_lastImage, 0, 0, newW, newH, null);
	}
}