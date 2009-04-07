package sneer.hardware.logging.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sneer.hardware.logging.gui.LogConsole;
import sneer.pulp.logging.Logger;
import sneer.skin.main_Menu.MainMenu;
import wheel.io.ui.action.Action;
import wheel.reactive.impl.EventReceiver;

class LogConsoleImpl extends JFrame implements LogConsole {
	
	private final MainMenu _mainMenu = my(MainMenu.class);	
	
	@SuppressWarnings("unused")
	private EventReceiver<String> _toAvoidGC;
	
	private boolean _isInitialized = false;
	
	LogConsoleImpl(){
		addMenuAction();
	}

	private void addMenuAction() {
		Action cmd = new Action(){
			@Override public String caption() {
				return "Open Log Console";
			}
			@Override public void run() {
				open();
			}
		};
		_mainMenu.getSneerMenu().addAction(cmd);
	}
	
	private void open() {
		if(!_isInitialized ) initGUI();
		setVisible(true);
	}

	private void initGUI() {
		JScrollPane scroll = new JScrollPane();
		Logger logger = my(Logger.class);
		
		final JTextArea txtLog = new JTextArea();
		_toAvoidGC = new EventReceiver<String>(logger.loggedMessages()) { @Override public void consume(String value) {
			txtLog.append(value);
		}};
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scroll, BorderLayout.CENTER);
		scroll.getViewport().add(txtLog);
		
		setBounds(10,10,400,300);
	}
}