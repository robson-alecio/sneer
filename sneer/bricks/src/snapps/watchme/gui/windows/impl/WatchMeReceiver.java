package snapps.watchme.gui.windows.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import snapps.watchme.WatchMe;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.WindowWidget;
import wheel.io.ui.GuiThread;
import wheel.lang.Consumer;
import wheel.reactive.EventSource;
import wheel.reactive.impl.Receiver;
import static wheel.lang.Environments.my;

class WatchMeReceiver{
	
	private WatchMe _watchMe = my(WatchMe.class);
	
	private KeyManager _keyManager = my(KeyManager.class);
	
	private ReactiveWidgetFactory _factory = my(ReactiveWidgetFactory.class);
	
	@SuppressWarnings("unused")
	private Consumer<Image> _imageReceiverToAvoidGc;
	

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
			_windowWidget = _factory.newFrame(_contact.nickname());
			JFrame frm = _windowWidget.getMainWidget();
			frm.setBounds(0,0,1024,768);
			initFrame(frm);
		}});
	}
	
	private void initFrame(JFrame frm) {
		Container contentPane = frm.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(_imageLabel, BorderLayout.CENTER);
		initFrameListener();
	}
	
	private void initFrameListener() {
		final JFrame frame = _windowWidget.getMainWidget();
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){ @Override public void windowClosing(WindowEvent e) {
			frame.setVisible(false);
		}});
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