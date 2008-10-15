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
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import wheel.io.ui.GuiThread;
import wheel.lang.Omnivore;
import wheel.reactive.EventSource;
import wheel.reactive.impl.Receiver;

public class WatchMeReceiver extends JFrame {
	
	@Inject
	private static WatchMe _watchMe;
	
	@Inject
	private static KeyManager _keyManager;
	
	@SuppressWarnings("unused")
	private Omnivore<Image> _imageReceiverToAvoidGc;
	
	private JLabel _imageLabel = new JLabel();

	@SuppressWarnings("unused")
	private Receiver<Contact> _keyChangeReceiverToAvoidGc;

	public WatchMeReceiver(Contact contact) {
		createReceiver(contact);
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

	private void createReceiver(final Contact contact) {
		PublicKey key = _keyManager.keyGiven(contact);
		if(key==null){
			_keyChangeReceiverToAvoidGc = new Receiver<Contact>(_keyManager.keyChanges()){@Override public void consume(Contact value) {
				if(contact!=value) return;
				startWindowPaint(_keyManager.keyGiven(contact));
				_keyChangeReceiverToAvoidGc = null;
			}};
			return;
		}
		startWindowPaint(key);
	}

	private void startWindowPaint(PublicKey key) {
		initGui();
		final EventSource<BufferedImage> screens = _watchMe.screenStreamFor(key);
		_imageReceiverToAvoidGc = new Receiver<Image>(screens){ @Override public void consume(Image img) {
			if (!isVisible()) setVisible(true);
			ImageIcon icon = new ImageIcon(img);
			_imageLabel.setIcon(icon);
			_imageLabel.repaint();
		}};
	}
}
