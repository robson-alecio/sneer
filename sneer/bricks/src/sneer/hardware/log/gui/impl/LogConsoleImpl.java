package sneer.hardware.log.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.gui.Action;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.hardware.log.gui.LogConsole;
import sneer.pulp.log.filter.LogFilter;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.skin.main.menu.MainMenu;
import sneer.skin.main.synth.Synth;
import sneer.skin.main.synth.scroll.SynthScrolls;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.autoscroll.AutoScrolls;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class LogConsoleImpl extends JFrame implements LogConsole {

	private final Synth _synth = my(Synth.class);
	
	{_synth.load(this.getClass());}
	private final double _SPLIT_LOCATION = (((Integer) _synth.getDefaultProperty("LodConsoleImpl.splitLocationPercent")).doubleValue())/100;
	private final Integer _OFFSET_X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetX");
	private final Integer _OFFSET_Y = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetY");
	private final Integer _HEIGHT = (Integer) _synth.getDefaultProperty("LodConsoleImpl.height");
	private final Integer _X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.x");
		
	private final MainMenu _mainMenu = my(MainMenu.class);	

	LogConsoleImpl(){
		super("Sneer Log Console");
		addMenuAction();
		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			initGUI();
		}});
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
		_synth.attach(txtLog, "LogTextArea");
		my(Signals.class).receive(this, new Consumer<String>() { @Override public void consume(String value) {
			txtLog.append(value);
		}}, my(LogNotifier.class).loggedMessages());

		JScrollPane scroll = newAutoScroll();
		getContentPane().setLayout(new BorderLayout());
		scroll.getViewport().add(txtLog);
		Rectangle unused = my(WindowBoundsSetter.class).unusedArea();
		setBounds(_X , unused.height-_HEIGHT-_OFFSET_Y, unused.width-_OFFSET_X, _HEIGHT-_OFFSET_Y);

		JPanel filter = new JPanel();
		_synth.attach(filter, "FilterPanel");
		filter.setLayout(new GridBagLayout());
		
		final ListRegister<String> whiteListEntries = my(LogFilter.class).whiteListEntries();
		final ListWidget<String> includes = my(ReactiveWidgetFactory.class).newList(whiteListEntries.output());
		JScrollPane scroll2 = my(SynthScrolls.class).create();
		scroll2.getViewport().add(includes.getComponent());
		scroll2.setBorder(new TitledBorder("Includes:"));
		
		final JTextField newInclude = new JTextField();
		newInclude.setBorder(new TitledBorder("New Filter:"));
		filter.add(newInclude, new GridBagConstraints(0,0,1,1,1.0,0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
		
		filter.add(scroll2, new GridBagConstraints(0,1,1,2,1.0,1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));
		JButton addButton = new JButton();
		JButton delButton = new JButton();

		_synth.attach(addButton,"AddButton");
		_synth.attach(delButton,"DelButton");
		
		filter.add(addButton, new GridBagConstraints(1,0,1,1,0.0,0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
		filter.add(delButton, new GridBagConstraints(1,1,1,1,0.0,0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
		
		JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, filter);
		main.setOneTouchExpandable(true);
		getContentPane().add(main, BorderLayout.CENTER);
		
		addButton.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			String value = newInclude.getText();
			newInclude.setText("");
			if(value.length()==0) return;
			whiteListEntries.add(value);
		}});
		
		delButton.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			Object[] values = includes.getMainWidget().getSelectedValues();
			for (Object value : values) 
				whiteListEntries.remove((String)value);
		}});
		
		setVisible(true);
		main.setDividerLocation(_SPLIT_LOCATION);
	}

	private JScrollPane newAutoScroll() {
		return my(AutoScrolls.class).create(my(LogNotifier.class).loggedMessages());
	}
}