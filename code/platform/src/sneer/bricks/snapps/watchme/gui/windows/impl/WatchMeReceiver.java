package sneer.bricks.snapps.watchme.gui.windows.impl;

import static sneer.foundation.environments.Environments.my;

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

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.bricks.skin.widgets.reactive.Widget;
import sneer.bricks.snapps.watchme.WatchMe;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.lang.Consumer;

class WatchMeReceiver{

	private final WatchMe _watchMe = my(WatchMe.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	private final ReactiveWidgetFactory _factory = my(ReactiveWidgetFactory.class);
	private final Contact _contact;

	private Widget<JFrame> _windowWidget;
	private JLabel _imageLabel = new JLabel();

	@SuppressWarnings("unused") private final Object _refToAvoidGc;
	private Reception _screensReception;

	WatchMeReceiver(Contact contact) {
		_contact = contact;
		_refToAvoidGc = my(Signals.class).receive(_keyManager.keyChanges(), new Consumer<Contact>() {@Override public void consume(Contact contactWithNewKey) {
			if(contactWithNewKey != _contact) return;
			startWindowPaint(_keyManager.keyGiven(_contact));
		}});
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
		if (_screensReception != null) _screensReception.dispose();
		
		final EventSource<BufferedImage> screens = _watchMe.screenStreamFor(key);
		_screensReception = my(Signals.class).receive(screens, new Consumer<Image>() { @Override public void consume(Image img) {
			if (_windowWidget == null) initGui();
			
			JFrame frm = _windowWidget.getMainWidget();
			if (!frm.isVisible()) frm.setVisible(true);
			
			ImageIcon icon = new ImageIcon(img);
			_imageLabel.setIcon(icon);
			_imageLabel.repaint();
		}});
	}

	void dispose() {
		if(_windowWidget==null) return;
		
		JFrame frm = _windowWidget.getMainWidget();
		if (frm != null) 
			frm.dispose();
		_windowWidget = null;
	}
}