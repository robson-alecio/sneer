package sneer.bricks.snapps.whisper.gui.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.gates.logic.LogicGates;
import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.rooms.ActiveRoomKeeper;
import sneer.bricks.skin.sound.loopback.LoopbackTester;
import sneer.bricks.skin.sound.mic.Mic;
import sneer.bricks.skin.sound.speaker.Speaker;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.bricks.skin.widgets.reactive.TextWidget;
import sneer.bricks.snapps.whisper.gui.WhisperGui;
import sneer.foundation.commons.environments.Environments;

class WhisperGuiImpl implements WhisperGui {

	private final Synth _synth = my(Synth.class);
	private final LoopbackTester _loopback = my(LoopbackTester.class);
	private final InstrumentRegistry _instrumentManager = my(InstrumentRegistry.class);
	private final Speaker _speaker = my(Speaker.class);
	private final Mic _mic = my(Mic.class);

	private final JToggleButton _whisperButton = new JToggleButton();
	private final JToggleButton _loopBackButton = new JToggleButton();

	private TextWidget<JTextField> _roomField;

	@SuppressWarnings("unused") private Object _referenceToAvoidGc;

	WhisperGuiImpl(){
		_synth.load(this);
		initSynth();
		_instrumentManager.registerInstrument(this);
	}

	private void initSynth() {
		_whisperButton.setName("WhisperButton");
		_synth.attach(_whisperButton);
		_loopBackButton.setName("LoopbackButton");
		_synth.attach(_loopBackButton);
	}
	
	private Signal<Boolean> isRunning() {
		return my(LogicGates.class).and(_mic.isRunning(), _speaker.isRunning());
	}

	@Override
	public void init(InstrumentPanel window) {
		Container container = window.contentPane();
		_synth.attach((JComponent)container);
		container.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		ActiveRoomKeeper room = Environments.my(ActiveRoomKeeper.class);

		_roomField = Environments.my(ReactiveWidgetFactory.class).newTextField(room.room(), room.setter());
		room.setter().consume("<Room>");

		container.add(_roomField.getMainWidget());
		_roomField.getMainWidget().setPreferredSize(new Dimension(100,36)); //Fix: change this to synth
		
		container.add(_whisperButton);
		container.add(_loopBackButton);
		
		createWhisperButtonListener();
		createLoopBackButtonListener();
		
		_referenceToAvoidGc = my(Signals.class).receive(isRunning(), new Consumer<Boolean>() { @Override public void consume(Boolean isRunning) {
			_whisperButton.setSelected(isRunning);
			_roomField.getMainWidget().setEnabled(isRunning);
		}});

	}
	
	@Override
	public int defaultHeight() {
		return 50;
	}

	private void createWhisperButtonListener() {
		_whisperButton.addMouseListener(new MouseAdapter() {	@Override public void mouseReleased(MouseEvent e) {
			if (_whisperButton.isSelected()) whisperOn();
			else whisperOff();
			_whisperButton.setSelected(false);
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

	protected void whisperOff() {
		_mic.close();
		_speaker.close();
	}

	protected void whisperOn() {
		_mic.open();
		_speaker.open();
	}

	@Override
	public String title() {
		return "Whisper";
	}
}