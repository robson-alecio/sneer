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
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.WindowWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.Omnivore;
import wheel.reactive.EventSource;
import wheel.reactive.impl.Receiver;

class WatchMeReceiver{
	
	@Inject
	private static WatchMe _watchMe;
	
	@Inject
	private static KeyManager _keyManager;
	
	@Inject
	private static ReactiveWidgetFactory _factory;
	
	@SuppressWarnings("unused")
	private Omnivore<Image> _imageReceiverToAvoidGc;
	

	@SuppressWarnings("unused")
	private Receiver<Contact> _keyChangeReceiverToAvoidGc;

	private final Contact _contact;
	private WindowWidget<JFrame> _windowWidget;
	private JLabel _imageLabel = new JLabel();
	
	WatchMeReceiver(Contact contact) {
		_contact = contact;
		createReceiver(contact);
	}

	private void initGui() {
		GuiThread.invokeAndWait(new Runnable(){	@Override public void run() {
			System.out.println("Watchin '" + _contact.nickname().currentValue() + "'");
			_windowWidget = _factory.newFrame(_contact.nickname());
			JFrame frm = _windowWidget.getMainWidget();
			frm.setBounds(0,0,1024,768);
			Container contentPane = frm.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(_imageLabel, BorderLayout.CENTER);
			initWindowListener();
		}});
	}

	private void initWindowListener() {
		_windowWidget.getMainWidget().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
			if(_windowWidget==null) initGui();
			
			JFrame frm = _windowWidget.getMainWidget();
			if (!frm.isVisible()) frm.setVisible(true);
			
			ImageIcon icon = new ImageIcon(img);
			_imageLabel.setIcon(icon);
			_imageLabel.repaint();
		}};
	}
	
	void dispose() {
		JFrame frm = _windowWidget.getMainWidget();
		if (frm != null) 
			frm.dispose();
		_windowWidget = null;
	}
}
