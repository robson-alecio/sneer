package sneer.bricks.snapps.system.log.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.hardware.io.log.filter.LogFilter;
import sneer.bricks.hardware.io.log.workers.notifier.LogNotifier;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.skin.main.dashboard.Dashboard;
import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.skin.widgets.reactive.ListWidget;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScroll;
import sneer.bricks.skin.windowboundssetter.WindowBoundsSetter;
import sneer.bricks.snapps.system.log.gui.LogConsole;
import sneer.foundation.lang.Consumer;

class LogConsoleImpl extends JFrame implements LogConsole {

	private static final String SNEER_LOG_CONSOLE = "Sneer Log Console";
	private final Synth _synth = my(Synth.class);
	{_synth.notInGuiThreadLoad(this.getClass());}
	
	private final Integer _OFFSET_X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetX");
	private final Integer _OFFSET_Y = (Integer) _synth.getDefaultProperty("LodConsoleImpl.offsetY");
	private final Integer _HEIGHT = (Integer) _synth.getDefaultProperty("LodConsoleImpl.height");
	private final Integer _X = (Integer) _synth.getDefaultProperty("LodConsoleImpl.x");
		
	private final JPopupMenu _popupMenu = new JPopupMenu();
	private final MainMenu _mainMenu = my(MainMenu.class);

	private final JTabbedPane _tab = new JTabbedPane();
	private final JTextArea _txtLog = new JTextArea();
	
	@SuppressWarnings("unused")
	private final WidgetLogger _logger = new WidgetLogger();
	private final JScrollPane _autoScroll = AutoScroll();
	
	LogConsoleImpl(){
		super(SNEER_LOG_CONSOLE);
		my(Dashboard.class);
		addMenuAction();
		my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
			initGui();
		}});
	}

	private void addMenuAction() {
		_mainMenu.addAction("Open Log Console", new Runnable() { @Override public void run() {
			open();
		}});
	}

	private void open() {
		setVisible(true);
	}

	private void initGui() {
		_txtLog.setEditable(false);
		getContentPane().setLayout(new BorderLayout());
		
		my(Synth.class).attach(_tab, "LogConsoleTab");
		
		_tab.addTab("", loadIcon("log.png"), _autoScroll, "Log");
		_tab.addTab("", loadIcon("filter.png"), initFilterGui(), "Filter");
		
		_tab.setTabPlacement(SwingConstants.RIGHT);
		_tab.addChangeListener(new ChangeListener(){ @Override public void stateChanged(ChangeEvent e) {
			if(_tab.getSelectedIndex()==0)
				setTitle(SNEER_LOG_CONSOLE);
			else
				setTitle("Sneer Log Console (Filter)");
		}});
		
		getContentPane().add(_tab, BorderLayout.CENTER);
		
		initClearLogAction();
		
		final WindowBoundsSetter wbSetter = my(WindowBoundsSetter.class);
		wbSetter.runWhenBaseContainerIsReady(new Runnable(){ @Override public void run() {
			Rectangle unused = wbSetter.unusedArea();
			setBounds(_X , unused.height-_HEIGHT-_OFFSET_Y, unused.width-_OFFSET_X, _HEIGHT-_OFFSET_Y);
			setFocusableWindowState(false);
			setVisible(true);
			setFocusableWindowState(true);
		}});
	}

	private ImageIcon loadIcon(String fileName) {
		return new ImageIcon(this.getClass().getResource(fileName));
	}

	private JPanel initFilterGui() {
		JPanel filter = new JPanel();
		_synth.attach(filter, "FilterPanel");
		filter.setLayout(new GridBagLayout());
		
		final ListRegister<String> whiteListEntries = my(LogFilter.class).whiteListEntries();
		final ListWidget<String> includes = my(ReactiveWidgetFactory.class).newList(whiteListEntries.output());
		JScrollPane scroll2 = my(SynthScrolls.class).create();
		scroll2.getViewport().add(includes.getComponent());
		scroll2.setBorder(new TitledBorder("Log Events That Contain:"));
		filter.add(scroll2, new GridBagConstraints(0,0,1,2,1.0,1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,2,0,0), 0,0));
		
		final JTextField newInclude = new JTextField();
		newInclude.setBorder(new TitledBorder(""));
		filter.add(newInclude, new GridBagConstraints(0,2,1,1,1.0,0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,4,2,2), 0,0));
		
		JButton addButton = new JButton();
		JButton delButton = new JButton();

		_synth.attach(addButton,"AddButton");
		_synth.attach(delButton,"DelButton");
		
		filter.add(delButton, new GridBagConstraints(1,0,1,1,0.0,0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
		filter.add(addButton, new GridBagConstraints(1,2,1,1,0.0,0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
		
		initAddFilterAction(whiteListEntries, newInclude, addButton);
		initDeleteFilterAction(whiteListEntries, includes, delButton);
		return filter;
	}

	private void initAddFilterAction( final ListRegister<String> whiteListEntries, final JTextField newInclude, JButton addButton) {
		addButton.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			String value = newInclude.getText();
			newInclude.setText("");
			if(value.length()==0) return;
			whiteListEntries.add(value);
		}});
	}

	private void initDeleteFilterAction(
			final ListRegister<String> whiteListEntries,
			final ListWidget<String> includes, JButton delButton) {
		delButton.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			Object[] values = includes.getMainWidget().getSelectedValues();
			for (Object value : values) 
				whiteListEntries.remove((String)value);
		}});
	}

	private void initClearLogAction() {
		_txtLog.addMouseListener(new MouseAdapter(){ 
			JMenuItem clear = new JMenuItem("Clear Log");{
				_popupMenu.add(clear);
				clear.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent ae) {
					_txtLog.setText("");
				}});			
			}

			@Override 
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger())
					_popupMenu.show(e.getComponent(),e.getX(),e.getY());
			}
		});
	}

	private JScrollPane AutoScroll() {
		JScrollPane scroll = my(AutoScroll.class).create(my(LogNotifier.class).loggedMessages());
		scroll.getViewport().add(_txtLog);
		return scroll;
	}

	private class WidgetLogger {
		@SuppressWarnings("unused") private final Object _referenceToAvoidGc;
		private WidgetLogger(){
			_referenceToAvoidGc = my(Signals.class).receive(my(LogNotifier.class).loggedMessages(), 
				new Consumer<String>(){ @Override public void consume(final String msg) {
					my(GuiThread.class).invokeLater(new Runnable(){ @Override public void run() {
						_txtLog.append(msg);
					}});
				}});
		}
	}
}