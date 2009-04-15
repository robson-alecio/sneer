package sneer.skin.main.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import sneer.commons.lang.Functor;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.hardware.gui.images.Images;
import sneer.hardware.gui.timebox.TimeboxedEventQueue;
import sneer.hardware.gui.trayicon.SystemTrayNotSupported;
import sneer.hardware.gui.trayicon.TrayIcon;
import sneer.hardware.gui.trayicon.TrayIcons;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.impl.SimpleListReceiver;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.main.dashboard.Dashboard;
import sneer.skin.main.instrumentregistry.Instrument;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.main.menu.MainMenu;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.WindowWidget;
import sneer.skin.windowboundssetter.WindowBoundsSetter;
import wheel.io.ui.action.Action;

class DashboardImpl implements Dashboard {

	private static final int WIDTH = 280;
	private static final int H_OFFSET = 30;
	private static final int TIMEOUT_FOR_GUI_EVENTS = 10 * 1000;
	
	private final DashboardPane _dashboardPane = new DashboardPane();

	private Dimension _screenSize;
	private Rectangle _bounds;
	private WindowSupport _windowSupport;
	private JPanel _rootPanel;
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Instrument> _instrumentsReceiver = new SimpleListReceiver<Instrument>(my(InstrumentRegistry.class).installedInstruments()){
		@Override protected void elementAdded(Instrument newElement) { 	_dashboardPane.install(newElement); }
		@Override protected void elementPresent(Instrument element) { 		_dashboardPane.install(element); }
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
		_windowSupport = new WindowSupport();
		new TrayIconSupport();
		_windowSupport.open();
	}
	
	private void waitUntilTheGuiThreadStarts() {
		my(GuiThread.class).strictInvokeAndWait(new Runnable(){@Override public void run() {}});
	}

	private URL logoIconURL() {
		return my(ImageFactory.class).getImageUrl(DefaultIcons.logo16x16);
	}
	
	class TrayIconSupport {
		
		TrayIconSupport(){
			TrayIcon trayIcon = null;
			try {
				trayIcon = my(TrayIcons.class).newTrayIcon(logoIconURL());
				addOpenWindowAction(trayIcon);
				addExitAction(trayIcon);
			} catch (SystemTrayNotSupported e1) {
				my(BlinkingLights.class).turnOn(LightType.INFO, "Minimizing Sneer Window", 
															  e1.getMessage() + " When closing the Sneer window, it will be minimized instead of closed.");
				_windowSupport.changeWindowCloseEventToMinimizeEvent();
			}
		}
		
		private void addOpenWindowAction(TrayIcon tray) {
			Action cmd = new Action(){
				@Override public String caption() { return "Open"; }
				@Override public void run() {
					my(GuiThread.class).assertInGuiThread();
					_windowSupport.open();						
				}};
			tray.setDefaultAction(cmd);
			tray.addAction(cmd);
		}
		
		private void addExitAction(TrayIcon trayIcon) {
			Action cmd = new Action(){
				@Override public String caption() { return "Exit"; }
				@Override public void run() {
					System.exit(0);
				}};
			trayIcon.addAction(cmd);
			my(MainMenu.class).getSneerMenu().addAction(cmd);
		}
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
			_frame.setIconImage(my(Images.class).getImage(logoIconURL()));
			my(WindowBoundsSetter.class).defaultContainer(_rootPanel);
		}

		private void initRootPanel() {
			MainMenu mainMenu = my(MainMenu.class);
			mainMenu.getWidget().setBorder(new EmptyBorder(0,0,0,0));
			_frame.setContentPane(_dashboardPane);
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

		private void changeWindowCloseEventToMinimizeEvent() {
			_frame.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
			_frame.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {
				_bounds = _frame.getBounds();
				_frame.setState(Frame.ICONIFIED);
			}});
		}		
		
		private void open() {
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