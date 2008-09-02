package sneer.skin.widgets.reactive.impl;

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
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class RImageImpl extends JPanel implements ImageWidget{

	private final Register<Image> _image = new RegisterImpl<Image>(null);
	private Omnivore<Image> _listener;
	private static final long serialVersionUID = 1L;
	private ImageFactory _imageFactory;
	private Omnivore<Image> _setter;
	
	RImageImpl(ImageFactory imageFactory, Signal<Image> source, Omnivore<Image> setter){
		setOpaque(false);
		_imageFactory = imageFactory;
		_setter = setter;
		
		//_image.setter().consume(source.currentValue());
		
		source.addReceiver(imageReceiver());
	}

	RImageImpl(ImageFactory imageFactory, Signal<Image> source) {
		this(imageFactory, source, null);
	}


	private Omnivore<Image> imageReceiver() {
		if(_listener==null)
			_listener = createImageReceiver();
		return _listener;
	}

	private Omnivore<Image> createImageReceiver() {
		return new Omnivore<Image>() {
			public void consume(final Image image) {
				_image.setter().consume(image);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						revalidate();
						repaint();
					}
				});
			}
		};
	}

	@Override
	public Dimension getMaximumSize() {
		Image current = currentImage();
		if(current!=null)
			return new Dimension(current.getWidth(null), current.getHeight(null));
		
		return new Dimension(48, 48); //Fix This should be 0,0. See Fix below.;
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
	public JComponent getComponent() {
		return this;
	}

	@Override
	public JPanel getMainWidget() {
		return this;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(currentImage() == null){
			_image.setter().consume(_imageFactory.getIcon(this.getClass(), "sneer48.png").getImage()); //Fix No image should be presented. See fix above.
		}
		
		Image current = currentImage();

        Graphics2D g2 = (Graphics2D)g;
        int newW = current.getWidth(null);
        int newH = current.getHeight(null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(current, 0, 0, newW, newH, null);
	}

	private Image currentImage() {
		return _image.output().currentValue();
	}

	@Override
	public Signal<Image> output() {
		return _image.output();
	}

	@Override
	public Omnivore<Image> setter() {
		if(_setter!=null) return _setter;
		throw new RuntimeException("The widget is readonly.");
	}
}