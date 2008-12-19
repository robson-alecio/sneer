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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.listentome.gui.ListenToMeGui;
import sneer.pulp.contacts.Contact;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.rooms.ActiveRoomKeeper;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.sound.loopback.LoopbackTester;
import sneer.skin.sound.mic.Mic;
import sneer.skin.sound.speaker.Speaker;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Consumer;
import wheel.lang.Environments;
import wheel.reactive.Signal;
import wheel.reactive.impl.And;
import static wheel.lang.Environments.my;

public class ListenToMeGuiImpl implements ListenToMeGui { //Optimize need a better snapp window support

	private final LoopbackTester _loopback = my(LoopbackTester.class);

	private final InstrumentManager _instrumentManager = my(InstrumentManager.class);

	private final ContactActionManager _actionsManager = my(ContactActionManager.class);

	private final Speaker _speaker = my(Speaker.class);

	private final Mic _mic = my(Mic.class);
	
	JToggleButton _listenToMeButton;
	JToggleButton _loopBackButton;
	
	private final Signal<Boolean> _isMicAndSpeakerRunning;

	private Consumer<Boolean> _consumerToAvoidGc;

	private TextWidget<JTextField> _roomField;

	ListenToMeGuiImpl(){
		_instrumentManager.registerInstrument(this);
		_isMicAndSpeakerRunning = new And(_mic.isRunning(), _speaker.isRunning()).output();
		initConsumer();
	}

	private void initConsumer() {
		_consumerToAvoidGc = new Consumer<Boolean>(){ @Override public void consume(final Boolean value) {
			_listenToMeButton.setSelected(value);
			_roomField.getMainWidget().setEnabled(value);
		}};	
		_isMicAndSpeakerRunning.addReceiver(_consumerToAvoidGc);
	}
	
	private ImageIcon loadIcon(String fileName) {
		try {
			return new ImageIcon(ImageIO.read(this.getClass().getResource(fileName)));
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	@Override
	public void init(InstrumentWindow window) {
		Container container = window.contentPane();
		container.setBackground(Color.WHITE);
		container.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
		
		ActiveRoomKeeper room = Environments.my(ActiveRoomKeeper.class);

		_roomField = Environments.my(ReactiveWidgetFactory.class).newTextField(room.room(), room.setter());
		room.setter().consume("<Room>");

		container.add(_roomField.getMainWidget());
		_roomField.getMainWidget().setPreferredSize(new Dimension(100,36));
		
		_listenToMeButton = createButton(container, "Listen To Me", "listenToMeOn.png", "listenToMeOff.png");
		container.add(space(36, 10));
		_loopBackButton = createButton(container, "Loop Back Test", "loopbackOn.png", "loopbackOff.png");
		
		createListenToMeButtonListener();
		createLoopBackButtonListener();
		addListenContactAction();
	}

	private JPanel space(final int height, final int width) {
		return new JPanel(){{
		setPreferredSize(new Dimension(width,height)); setOpaque(false);}};
	}
	
	@Override
	public int defaultHeight() {
		return 50;
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
					return "Stop listening";
				return "Listen";
			}

			@Override
			public void run() {
				isStarted = !isStarted;
				throw new wheel.lang.exceptions.NotImplementedYet("" + _contact); // Implement
			}});
	}

	private void createListenToMeButtonListener() {
		_listenToMeButton.addMouseListener(new MouseAdapter() {	@Override public void mouseReleased(MouseEvent e) {
			if (_listenToMeButton.isSelected()) listenToMeOn();
			else listenToMeOff();
			_listenToMeButton.setSelected(false);
		}});
	}

	private void createLoopBackButtonListener() {
		_loopBackButton.addMouseListener(new MouseAdapter() {	@Override public void mouseReleased(MouseEvent e) {
			if(_loopBackButton.isSelected()){
				_loopBackButton.setSelected(_loopback.start());
				return;
			}
			_loopback.stop();
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

	private JToggleButton createButton(Container container, String tip, final String onIcon, final String offIcon) {
		final JToggleButton btn = new JToggleButton(){
			Icon ON_ICON = loadIcon(onIcon);
			Icon OFF_ICON = loadIcon(offIcon);
			{setIcon(OFF_ICON);}
			
			@Override
			public void setSelected(boolean isSelected) {
				super.setSelected(isSelected);
				if (isSelected) setIcon(ON_ICON);
				else setIcon(OFF_ICON);
			}
			
			{addMouseListener(new MouseAdapter() {
				@Override public void mouseEntered(MouseEvent e) { setIcon(ON_ICON); }
				@Override public void mouseExited(MouseEvent e) { if(!isSelected()) setIcon(OFF_ICON);	}
			});}
		};
		btn.setPreferredSize(new Dimension(40,40));
		btn.setBorder(new EmptyBorder(2,2,2,2));
		btn.setOpaque(true);
		btn.setBackground(Color.WHITE);
		btn.setToolTipText(tip);
		container.add(btn);
		return btn;
	}
}