package snapps.watchme.gui.windows.impl;

import static sneer.commons.environments.Environments.my;

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
import sneer.brickness.PublicKey;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.contacts.Contact;
import sneer.pulp.events.EventSource;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.reactive.Signals;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.Widget;

class WatchMeReceiver{

	private final WatchMe _watchMe = my(WatchMe.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	private final ReactiveWidgetFactory _factory = my(ReactiveWidgetFactory.class);
	private final Contact _contact;

	private Widget<JFrame> _windowWidget;
	private JLabel _imageLabel = new JLabel();
	
	WatchMeReceiver(Contact contact) {
		_contact = contact;
		
		my(Signals.class).receive(this, new Consumer<Contact>() {@Override public void consume(Contact contactWithNewKey) {
			if(contactWithNewKey != _contact) return;
			startWindowPaint(_keyManager.keyGiven(_contact));
		}}, _keyManager.keyChanges());
	}

	private void initGui() {
		my(GuiThread.class).invokeAndWait(new Runnable(){	@Override public void run() {
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


	private void startWindowPaint(PublicKey key) {
		if (key == null) return;
		
		final EventSource<BufferedImage> screens = _watchMe.screenStreamFor(key);
		my(Signals.class).receive(this, new Consumer<Image>() { @Override public void consume(Image img) {
			if (_windowWidget == null) initGui();
			
			JFrame frm = _windowWidget.getMainWidget();
			if (!frm.isVisible()) frm.setVisible(true);
			
			ImageIcon icon = new ImageIcon(img);
			_imageLabel.setIcon(icon);
			_imageLabel.repaint();
		}}, screens);
	}
	
	void dispose() {
		JFrame frm = _windowWidget.getMainWidget();
		if (frm != null) 
			frm.dispose();
		_windowWidget = null;
	}
}