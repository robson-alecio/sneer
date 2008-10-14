package snapps.watchme.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import snapps.watchme.WatchMe;
import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.PublicKey;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.io.ui.GuiThread;
import wheel.lang.Omnivore;
import wheel.reactive.EventSource;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class WatchMeWindow extends JFrame {
	
	@Inject
	private static WatchMe _watchMe;
	
	@Inject
	private static ReactiveWidgetFactory _factory;
	
	private Register<Image> imageResgiter = new RegisterImpl<Image>(null);

	public WatchMeWindow(PublicKey key) throws HeadlessException {
		final EventSource<BufferedImage> screen = createReceiver(key);
		initGui();
		openWindowForFirstRecivedImage(screen);
	}

	private void initGui() {
		GuiThread.invokeAndWait(new Runnable(){	@Override public void run() {
			ImageWidget imageWidget = _factory.newImage(imageResgiter.output());
				
			setBounds(0,0,1024,768);
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(imageWidget.getComponent(), BorderLayout.CENTER);
				
			initWindowListener();
		}});
	}

	private void initWindowListener() {
		addWindowListener(new WindowAdapter(){ @Override public void windowClosed(WindowEvent e) {
			dispose();
		}});
	}

	private EventSource<BufferedImage> createReceiver(PublicKey key) {
		final EventSource<BufferedImage> screen = _watchMe.screenStreamFor(key);
		screen.addReceiver(new Omnivore<Image>(){ @Override public void consume(Image img) {
			imageResgiter.setter().consume(img);
		}});
		return screen;
	}

	private void openWindowForFirstRecivedImage(final EventSource<BufferedImage> screen) {
		screen.addReceiver(new Omnivore<Image>(){ @Override public void consume(Image img) {
			setVisible(true);
			screen.removeReceiver(this);
		}});
	}
}
