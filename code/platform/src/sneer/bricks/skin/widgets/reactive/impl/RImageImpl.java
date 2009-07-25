package sneer.bricks.skin.widgets.reactive.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JPanel;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.skin.widgets.reactive.ImageWidget;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.PickyConsumer;

class RImageImpl extends JPanel implements ImageWidget{

	private final ImageFactory _imageFactory = my(ImageFactory.class);

	protected final Register<Image> _image;
	protected final PickyConsumer<Image> _setter;

	@SuppressWarnings("unused") private Object _referenceToAvoidGc;

	RImageImpl(Signal<Image> source) {
		this(source, null);
	}
	
	RImageImpl(Signal<Image> source, PickyConsumer<Image> setter){
		setOpaque(false);
		_image = my(Signals.class).newRegister(null);
		_setter = setter;

		imageReceiverFor(source);
	}

	private void imageReceiverFor(Signal<Image> signal) {
		_referenceToAvoidGc = signal.addReceiver(new Consumer<Image>() { @Override public void consume(final Image image) {
			_image.setter().consume(image);
		
			my(GuiThread.class).invokeAndWait(new Runnable() { @Override public void run() {
				revalidate();
				repaint();
			}});
		}});
	}

	@Override
	public Dimension getMaximumSize() {
		Image current = currentImage();
		if(current != null)
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
	public PickyConsumer<Image> setter() {
		if(_setter!=null) return _setter;
		throw new RuntimeException("The widget is readonly.");
	}
}