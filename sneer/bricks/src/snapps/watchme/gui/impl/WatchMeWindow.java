package snapps.watchme.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
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
import wheel.reactive.impl.Receiver;
import wheel.reactive.impl.RegisterImpl;

public class WatchMeWindow extends JFrame {
	
	@Inject
	private static WatchMe _watchMe;
	
	@Inject
	private static ReactiveWidgetFactory _factory;
	
	private Register<Image> _imageRegister = new RegisterImpl<Image>(null);

	@SuppressWarnings("unused")
	private Omnivore<Image> _receiverToAvoidGc;

	public WatchMeWindow(PublicKey key) {
		createReceiver(key);
		initGui();
	}

	private void initGui() {
		GuiThread.invokeAndWait(new Runnable(){	@Override public void run() {
			ImageWidget imageWidget = _factory.newImage(_imageRegister.output());
				
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

	private void createReceiver(PublicKey key) {
		final EventSource<BufferedImage> screens = _watchMe.screenStreamFor(key);
	
		_receiverToAvoidGc = new Receiver<Image>(screens){ @Override public void consume(Image img) {
			System.out.println("WatchMe image: " + img);
			if (!isVisible()) setVisible(true);
			_imageRegister.setter().consume(img);
		}};
	}

}
