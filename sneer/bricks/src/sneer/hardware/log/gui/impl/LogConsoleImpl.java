package sneer.hardware.log.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.gui.Action;
import sneer.hardware.log.gui.LogConsole;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;
import sneer.skin.main.menu.MainMenu;
import sneer.skin.main.synth.Synth;
import sneer.skin.widgets.reactive.autoscroll.AutoScrolls;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class LogConsoleImpl extends JFrame implements LogConsole {

	private final Synth _synth = my(Synth.class);
	
	{_synth.load(this.getClass());}
	private final Integer _OFFSET_X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetX");
	private final Integer _OFFSET_Y = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetY");
	private final Integer _HEIGHT = (Integer) _synth.getDefaultProperty("LodConsoleImpl.height");
	private final Integer _X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.x");
		
	private final MainMenu _mainMenu = my(MainMenu.class);	

	LogConsoleImpl(){
		super("Sneer Log Console");
		initGUI();
		open();
		addMenuAction();
	}

	private void addMenuAction() {
		Action cmd = new Action(){
			@Override public String caption() {	return "Open Log Console"; }
			@Override public void run() { open(); }
		};
		_mainMenu.getSneerMenu().addAction(cmd);
	}

	private void open() {
		setVisible(true);
	}

	private void initGUI() {
		final JTextArea txtLog = new JTextArea();
		_synth.attach(txtLog, "logTextArea");
		my(Signals.class).receive(this, new Consumer<String>() { @Override public void consume(String value) {
			txtLog.append(value);
		}}, my(LogNotifier.class).loggedMessages());

		JScrollPane scroll = newAutoScroll();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scroll, BorderLayout.CENTER);
		scroll.getViewport().add(txtLog);
		Rectangle unused = my(WindowBoundsSetter.class).unusedArea();
		setBounds(_X , unused.height-_HEIGHT-_OFFSET_Y, unused.width-_OFFSET_X, _HEIGHT-_OFFSET_Y);
		
	}

	private JScrollPane newAutoScroll() {
		return my(AutoScrolls.class).create(my(LogNotifier.class).loggedMessages());
	}
}