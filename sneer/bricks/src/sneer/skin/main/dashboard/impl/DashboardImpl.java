package sneer.skin.main.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
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
import sneer.skin.colors.Colors;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.main.dashboard.Dashboard;
import sneer.skin.main.dashboard.InstrumentWindow;
import sneer.skin.main.instrumentregistry.Instrument;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.skin.main.menu.MainMenu;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.WindowWidget;
import sneer.skin.windowboundssetter.WindowBoundsSetter;
import wheel.io.ui.action.Action;

class DashboardImpl implements Dashboard {
	private static final int TIMEOUT_FOR_GUI_EVENTS = 10 * 1000;
	
	private Dimension _screenSize;
	private Rectangle _bounds;
	
	private final WindowWidget<JFrame> _rwindow;

	private JPanel _rootPanel;
	private JPanel _contentPanel;
	
	private static final int WIDTH = 280;
	private static final int H_OFFSET = 30;
	
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Instrument> _instrumentsReceiver;
	
	DashboardImpl() {
		_rwindow =my(ReactiveWidgetFactory.class).newFrame(reactiveTitle());
		my(ThreadPool.class).registerActor(new Runnable(){@Override public void run() {
			init();
		}});
		waitUntilTheGuiThreadStarts();
	}
		
	private void init() {
		my(TimeboxedEventQueue.class).startQueueing(TIMEOUT_FOR_GUI_EVENTS);
		initGui();
	}
	
	private void initGui() {
		initWindow();
		initRootPanel();		
		resizeWindow();
		initTrayIconIfPossible();
		addInstrumentsReceiver();
		open();
	}

	private void waitUntilTheGuiThreadStarts() {
		my(GuiThread.class).strictInvokeAndWait(new Runnable(){@Override public void run() {}});
	}

	private void initTrayIconIfPossible() {
		TrayIcon trayIcon = null;
		try {
			trayIcon = my(TrayIcons.class).newTrayIcon(logoIconURL());
		} catch (SystemTrayNotSupported e1) {
			my(BlinkingLights.class).turnOn(LightType.INFO, "Minimizing Sneer Window", 
														  e1.getMessage() + " When closing the Sneer window, it will be minimized instead of closed.");
			changeWindowCloseEventToMinimizeEvent();
			return;
		}
		
		addOpenWindowAction(trayIcon);
		addExitAction(trayIcon);
	}

	private void addInstrumentsReceiver() {
		_instrumentsReceiver = new SimpleListReceiver<Instrument>(my(InstrumentRegistry.class).installedInstruments()){
			@Override protected void elementAdded(Instrument newElement) {
				install(newElement);
			}

			@Override protected void elementPresent(Instrument element) {
				install(element);
			}

			@Override protected void elementRemoved(Instrument element) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
			}
		};
	}

	private URL logoIconURL() {
		return my(ImageFactory.class).getImageUrl(DefaultIcons.logo16x16);
	}


	private void initWindow() {
		_rootPanel = (JPanel) _rwindow.getMainWidget().getContentPane();
		my(WindowBoundsSetter.class).defaultContainer(_rootPanel);
		_rwindow.getMainWidget().setIconImage(my(Images.class).getImage(logoIconURL()));
	}

	private void initRootPanel() {
		_rootPanel.setLayout(new BorderLayout());
		MainMenu mainMenu = my(MainMenu.class);
		mainMenu.getWidget().setBorder(new EmptyBorder(0,0,0,0));
		_rootPanel.add(mainMenu.getWidget(), BorderLayout.NORTH);
		_contentPanel = new ContentPane();
		_contentPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 1));
		_rootPanel.add(_contentPanel, BorderLayout.CENTER);
	}

	private InstrumentWindow install(final Instrument instrument) {
		final InstrumentWindowImpl sf = new InstrumentWindowImpl(instrument.title());
		my(GuiThread.class).strictInvokeAndWait(new Runnable(){	@Override public void run() {
			_contentPanel.add(sf);
			instrument.init(sf);
			resizeContainer(instrument, sf);
			sf.revalidate();
			sf.resizeContents();
		}});
        return sf;
	}

	private void resizeContainer(final Instrument instrument,	final InstrumentWindowImpl sf) {
		int width = sf.getSize().width;
		Dimension size = new Dimension(width, instrument.defaultHeight());
		sf.setMinimumSize(size);
		sf.setPreferredSize(size);
		sf.setSize(size);
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
		final JFrame frm = _rwindow.getMainWidget();
		frm.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
		frm.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				_bounds = frm.getBounds();
				frm.setState(Frame.ICONIFIED);
			}
		});
	}
	
	private void addOpenWindowAction(TrayIcon tray) {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Open";
			}
			@Override
			public void run() {
				my(GuiThread.class).assertInGuiThread();
				open();						
			}
		};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
	}
	
	private void open() {
		JFrame frm = _rwindow.getMainWidget();
		frm .setState(Frame.NORMAL);
		frm.setVisible(true);
		frm.requestFocusInWindow();
	}
	
	private void addExitAction(TrayIcon trayIcon) {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Exit";
			}
			@Override
			public void run() {
				System.exit(0);
			}
		};
		trayIcon.addAction(cmd);
		my(MainMenu.class).getSneerMenu().addAction(cmd);
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

class ContentPane extends JPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(my(Colors.class).lowContrast());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
