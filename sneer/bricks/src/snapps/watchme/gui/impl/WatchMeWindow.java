package snapps.watchme.gui.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

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
	
	@SuppressWarnings("unused")
	private Omnivore<Image> _receiverToAvoidGc;
	
	private JLabel _imageLabel = new JLabel();

	public WatchMeWindow(PublicKey key) {
		createReceiver(key);
		initGui();
	}

	private void initGui() {
		GuiThread.invokeAndWait(new Runnable(){	@Override public void run() {
			
			setBounds(0,0,1024,768);
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(_imageLabel, BorderLayout.CENTER);
			initWindowListener();
		}});
	}

	private void initWindowListener() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	private void createReceiver(PublicKey key) {
		final EventSource<BufferedImage> screens = _watchMe.screenStreamFor(key);
	
		_receiverToAvoidGc = new Receiver<Image>(screens){ @Override public void consume(Image img) {
			if (!isVisible() && isEnabled()) setVisible(true);
			ImageIcon icon = new ImageIcon(img);
			_imageLabel.setIcon(icon);
			_imageLabel.repaint();
		}};
	}
}
