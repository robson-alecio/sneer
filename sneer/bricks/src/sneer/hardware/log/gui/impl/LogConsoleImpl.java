package sneer.hardware.log.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Dimension;

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
import sneer.skin.main.synth.scroll.SynthScrolls;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class LogConsoleImpl extends JFrame implements LogConsole {

	private final Synth _synth = my(Synth.class);
	{_synth.load(this.getClass());}
	
	private final MainMenu _mainMenu = my(MainMenu.class);	

	private boolean _isInitialized = false;

	LogConsoleImpl(){
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
		if(!_isInitialized ) initGUI();
		setVisible(true);
	}

	private void initGUI() {
		JScrollPane scroll = my(SynthScrolls.class).create();
		LogNotifier notifier = my(LogNotifier.class);

		final JTextArea txtLog = new JTextArea();
		_synth.attach(txtLog, "logTextArea");
		my(Signals.class).receive(this, new Consumer<String>() { @Override public void consume(String value) {
			txtLog.append(value);
		}}, notifier.loggedMessages());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scroll, BorderLayout.CENTER);
		scroll.getViewport().add(txtLog);
		setSize(new Dimension(400,300));
		my(WindowBoundsSetter.class).setBestBounds(this);
	}
}