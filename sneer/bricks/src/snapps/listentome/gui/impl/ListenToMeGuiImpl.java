package snapps.listentome.gui.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.listentome.gui.ListenToMeGui;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.sound.mic.Mic;
import sneer.skin.sound.speaker.Speaker;

public class ListenToMeGuiImpl implements ListenToMeGui { //Optimize need a better snapp window support

	@Inject
	static private InstrumentManager _instrumentManager;

	@Inject
	static private ContactActionManager _actionsManager;

	@Inject
	static private Speaker _speaker;
	
	@Inject
	static private Mic _mic;
	
	JToggleButton _listenToMeButton;

	ListenToMeGuiImpl(){
		_instrumentManager.registerInstrument(this);
	}
	
	private ImageIcon loadIcon(String fileName) {
		try {
			return new ImageIcon(ImageIO.read(this.getClass().getResource(fileName)));
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	@Override
	public void init(Container container) {
		container.setBackground(Color.WHITE);
		container.setLayout(new FlowLayout());
		_listenToMeButton = createButton(container, "Listen To Me!");
		
		createListenToMeButtonListener();
		addListenContactAction();
	}

	private void addListenContactAction() {
		_actionsManager.addContactAction(new ContactAction(){

			boolean isStarted = false;
			private Contact _contact;

			@Override
			public boolean isEnabled() {
				return true;  //Fix return true only when remote microphone is shared.
			}

			@Override
			public boolean isVisible() {
				return true;  //Fix return true only when remote microphone is shared.
			}

			@Override
			public void setActive(Contact contact) {
				_contact = contact;
			}

			@Override
			public String caption() {
				if(isStarted)
					return "stop listen";
				return "start listen";
			}

			@Override
			public void run() {
				isStarted = !isStarted;
				System.out.println(_contact);
				throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
			}});
	}

	private void createListenToMeButtonListener() {
		_listenToMeButton.addMouseListener(new MouseAdapter() {	@Override public void mouseReleased(MouseEvent e) {
			if (_listenToMeButton.isSelected()) listenToMeOn();
			else listenToMeOff();
		}});
	}

	protected void listenToMeOff() {
		_mic.close();
		_speaker.close();
	}

	protected void listenToMeOn() {
		_mic.open();
		_speaker.open();
	}

	private JToggleButton createButton(Container container, String tip) {
		final JToggleButton btn = new JToggleButton();
		btn.setPreferredSize(new Dimension(40,40));
		btn.setBorder(new EmptyBorder(2,2,2,2));
		btn.setOpaque(true);
		btn.setBackground(Color.WHITE);
		btn.setToolTipText(tip);
		addMouseListener(btn);
		container.add(btn);
		return btn;
	}

	private void addMouseListener(final JToggleButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			Icon LISTEN_TO_ME_ON = loadIcon("listenToMeOn.png");
			Icon LISTEN_TO_ME_OFF = loadIcon("listenToMeOff.png");
			{btn.setIcon(LISTEN_TO_ME_OFF);}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setIcon(LISTEN_TO_ME_ON);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseReleased(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (btn.isSelected())
					btn.setIcon(LISTEN_TO_ME_ON);
				else
					btn.setIcon(LISTEN_TO_ME_OFF);
			}
		});
	}
}