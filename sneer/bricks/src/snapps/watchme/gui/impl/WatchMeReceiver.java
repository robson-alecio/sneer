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

public class WatchMeReceiver{
	
	@Inject
	private static WatchMe _watchMe;
	
	@Inject
	private static KeyManager _keyManager;
	
	@SuppressWarnings("unused")
	private Omnivore<Image> _imageReceiverToAvoidGc;
	
	private JLabel _imageLabel = new JLabel();

	@SuppressWarnings("unused")
	private Receiver<Contact> _keyChangeReceiverToAvoidGc;

	private final Contact _contact;
	private JFrame _window;
	
	public WatchMeReceiver(Contact contact) {
		_contact = contact;
		createReceiver(contact);
	}

	private void initGui() {
		GuiThread.invokeAndWait(new Runnable(){	@Override public void run() {
			String nick = _contact.nickname().currentValue();
			System.out.println("Watchin '" + nick + "'");
			_window = new JFrame(nick);
			_window.setBounds(0,0,1024,768);
			Container contentPane = _window.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(_imageLabel, BorderLayout.CENTER);
			initWindowListener();
		}});
	}

	private void initWindowListener() {
		_window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
		final EventSource<BufferedImage> screens = _watchMe.screenStreamFor(key);
		_imageReceiverToAvoidGc = new Receiver<Image>(screens){ @Override public void consume(Image img) {
			if(_window==null) initGui();
			if (!_window.isVisible()) _window.setVisible(true);
			ImageIcon icon = new ImageIcon(img);
			_imageLabel.setIcon(icon);
			_imageLabel.repaint();
		}};
	}
	
	void dispose(){
		if(_window==null) 
			_window.dispose();
		_window = null;
	}
}
