package sneer.skin.main.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import sneer.commons.lang.Functor;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.hardware.gui.timebox.TimeboxedEventQueue;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.impl.SimpleListReceiver;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.main.dashboard.Dashboard;
import sneer.skin.main.instrumentregistry.Instrument;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.main.menu.MainMenu;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.WindowWidget;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class DashboardImpl implements Dashboard {

	private static final int WIDTH = 280;
	private static final int H_OFFSET = 30;
	private static final int TIMEOUT_FOR_GUI_EVENTS = 10 * 1000;
	
	private final DashboardPanel _dashboardPanel = new DashboardPanel();
	private JPanel _rootPanel = new JPanel();

	private Dimension _screenSize;
	private Rectangle _bounds;
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Instrument> _instrumentsReceiver = new SimpleListReceiver<Instrument>(my(InstrumentRegistry.class).installedInstruments()){
		@Override protected void elementAdded(Instrument instrument) { 	_dashboardPanel.install(instrument); }
		@Override protected void elementPresent(Instrument instrument) { _dashboardPanel.install(instrument); }
		@Override protected void elementRemoved(Instrument element) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
		}};
	
	DashboardImpl() {
		my(ThreadPool.class).registerActor(new Runnable(){@Override public void run() {
			initGuiTimebox();
			initGui();
		}});
		waitUntilTheGuiThreadStarts();
	}
		
	private void initGuiTimebox() {
		my(TimeboxedEventQueue.class).startQueueing(TIMEOUT_FOR_GUI_EVENTS);
	}
	
	private void initGui() {
		WindowSupport windowSupport = new WindowSupport();
		windowSupport.open();
		new TrayIconSupport(windowSupport);
	}
	
	private void waitUntilTheGuiThreadStarts() {
		my(GuiThread.class).strictInvokeAndWait(new Runnable(){@Override public void run() {}});
	}

   class WindowSupport{
		private WindowWidget<JFrame> _rwindow;
		private JFrame _frame;

		WindowSupport() {
			initWindow();
			initRootPanel();		
			resizeWindow();
		}
		
		private void initWindow() {
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				_rwindow = my(ReactiveWidgetFactory.class).newFrame(reactiveTitle());
			}});
			_frame = _rwindow.getMainWidget();
			_frame.setIconImage(IconUtil.getLogo());
			my(WindowBoundsSetter.class).defaultContainer(_rootPanel);
			
			_frame.addWindowListener(new WindowAdapter(){
				@Override public void windowDeactivated(WindowEvent e) {
					_dashboardPanel.hideAllToolbars();
				}});
		}

		private void initRootPanel() {
			MainMenu mainMenu = my(MainMenu.class);
			mainMenu.getWidget().setBorder(new EmptyBorder(0,0,0,0));
			
			_rootPanel.setLayout(new BorderLayout());
			_rootPanel.add(mainMenu.getWidget(), BorderLayout.NORTH);
			_rootPanel.add(_dashboardPanel, BorderLayout.CENTER);
			
			addListenerToHideToolbarsWhenMouseExited();
			
//			RunMe.logTree(_dashboardPanel);
		}

		private void addListenerToHideToolbarsWhenMouseExited() {
			//Fix: this method is a hack, consider to use a glasspane mouse listener
			JPanel content = (JPanel) _frame.getContentPane();
			content.setLayout(new BorderLayout());
			content.setBorder(new EmptyBorder(0,2,0,2));
			content.add(_rootPanel, BorderLayout.CENTER);
			content.addMouseListener(new MouseAdapter(){ @Override public void mouseEntered(MouseEvent e) {
				_dashboardPanel.hideAllToolbars();
			}});
		}	
		
		private void resizeWindow() {
			Dimension newSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			if(_bounds==null || _screenSize==null || !_screenSize.equals(newSize)){
				_screenSize  = newSize;
				_bounds = new Rectangle((int) _screenSize.getWidth() - WIDTH, 0, WIDTH,	
									   				  (int) _screenSize.getHeight() - H_OFFSET);
			}
			_rwindow.getMainWidget().setBounds(_bounds);
		}

		void changeWindowCloseEventToMinimizeEvent() {
			_frame.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
			_frame.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {
				_bounds = _frame.getBounds();
				_frame.setState(Frame.ICONIFIED);
			}});
		}		
		
		void open() {
			_frame.setState(Frame.NORMAL);
			_frame.setVisible(true);
			_frame.requestFocusInWindow();
		}

		private Signal<String> reactiveTitle() {
			Signal<String> title = my(Signals.class).adapt(
				my(OwnNameKeeper.class).name(), 
				new Functor<String, String>(){	@Override public String evaluate(String ownName) {
					return "Sneer - " + ownName;
				}});
			return title;
		}
	}
}