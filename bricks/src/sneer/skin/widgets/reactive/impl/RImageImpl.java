package sneer.skin.widgets.reactive.impl;

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
import sneer.skin.widgets.reactive.ImageWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class RImageImpl extends JPanel implements ImageWidget{

	private Signal<Image> _source;
	private Image _lastImage;
	private Omnivore<Image> _listener;
	private static final long serialVersionUID = 1L;
	private ImageFactory _imageFactory;
	private Dimension _dimension;
	private Omnivore<Image> _setter;
	
	RImageImpl(ImageFactory imageFactory, Signal<Image> source, Omnivore<Image> setter){
		setOpaque(false);
		_source = source;
		_imageFactory = imageFactory;
		_setter = setter;
		
		_lastImage = _source.currentValue();
		if(_lastImage==null){
			_dimension = new Dimension(48, 48);
		}else{
			_dimension = new Dimension(_lastImage.getWidth(null), _lastImage.getHeight(null));			
		}
		addReceivers();
	}

	RImageImpl(ImageFactory imageFactory, Signal<Image> source) {
		this(imageFactory, source, null);
	}

	private void addReceivers() {
		_source.addReceiver(imageReceiver());
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
		if(_lastImage!=null)
			_dimension = new Dimension(_lastImage.getWidth(null), _lastImage.getHeight(null));
		return _dimension;
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

	@Override
	public Signal<Image> output() {
		return _source;
	}

	@Override
	public Omnivore<Image> setter() {
		if(_setter!=null) return _setter;
		throw new RuntimeException("The widget is readonly.");
	}
}