package sneer.skin.dashboard.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
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

import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.main_Menu.MainMenu;
import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import wheel.io.ui.GuiThread;
import wheel.io.ui.TimeboxedEventQueue;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.action.Action;
import wheel.io.ui.graphics.Images;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.reactive.impl.EventReceiver;
import wheel.reactive.lists.impl.SimpleListReceiver;

//Implement Persist window size and position
class DashboardImpl implements Dashboard, Runnable {
	
	private static final int TIMEOUT_FOR_GUI_EVENTS = 10 * 1000;
	
	private Dimension _screenSize;
	private Rectangle _bounds;
	
	private transient JFrame _jframe;

	private transient JPanel _rootPanel;
	private transient JPanel _contentPanel;
	
	private static transient final int WIDTH = 280;
	private static transient final int H_OFFSET = 30;
	
	private final ThreadPool _threadPool = my(ThreadPool.class);
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);
	
	private final MainMenu _mainMenu = my(MainMenu.class);
	
	private final OwnNameKeeper _ownNameKeeper = my(OwnNameKeeper.class);
		
	private final InstrumentManager _instrumentManager = my(InstrumentManager.class);

	private final BlinkingLights _blinkingLights = my(BlinkingLights.class);
	
	@SuppressWarnings("unused")
	private final EventReceiver<String> _ownNameReceiver = new EventReceiver<String>(_ownNameKeeper.name()) { @Override public void consume(String value) {
		updateTitle();
	}};
		
	@SuppressWarnings("unused")
	private SimpleListReceiver<Instrument> _instrumentsReceiver;
	
	private void initialize() {
		initWindows();	
		resizeWindow();
		initTrayIconIfPossible();
		addInstrumentsReceiver();
		open();
	}

	private void waitUntilTheGuiThreadStarts() {
		GuiThread.strictInvokeAndWait(new Runnable(){@Override public void run() {}});
	}

	private void initTrayIconIfPossible() {
		TrayIcon tray = null;
		try {
			tray = new TrayIconImpl(logoIconURL());
		} catch (SystemTrayNotSupported e1) {
			_blinkingLights.turnOn(LightType.INFO, "Minimizing Sneer Window", e1.getMessage() + " When closing the Sneer window, it will be minimized instead of closed.");
			changeWindowCloseEventToMinimizeEvent();
			return;
		}
		
		addOpenWindowAction(tray);
		addExitAction(tray);
	}

	private void addInstrumentsReceiver() {
		_instrumentsReceiver = new SimpleListReceiver<Instrument>(_instrumentManager.installedInstruments()){

			@Override
			protected void elementAdded(Instrument newElement) {
				install(newElement);
			}

			@Override
			protected void elementPresent(Instrument element) {
				install(element);
			}

			@Override
			protected void elementRemoved(Instrument element) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
			}
		};
	}

	private URL logoIconURL() {
		return _imageFactory.getImageUrl(DefaultIcons.logo16x16);
	}

	private void initWindows() {
		initFrame();
		initRootPanel();
	}

	private void initFrame() {
		_jframe = new JFrame();
		_jframe.setIconImage(Images.getImage(logoIconURL()));
		updateTitle();
	}
	
	private void updateTitle() {
		if (_jframe == null) return;
		_jframe.setTitle("Sneer - " + _ownNameKeeper.name().currentValue());
	}	
	
	private void initRootPanel() {
		_rootPanel = new JPanel();
		_rootPanel = (JPanel) _jframe.getContentPane();
		_rootPanel.setLayout(new BorderLayout());
		_mainMenu.getWidget().setBorder(new EmptyBorder(0,0,0,0));
		_rootPanel.add(_mainMenu.getWidget(), BorderLayout.NORTH);
		_contentPanel = new ContentPane();
		_contentPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 1));
		_rootPanel.add(_contentPanel, BorderLayout.CENTER);
	}

	private InstrumentWindow install(final Instrument instrument) {
		final InstrumentWindowImpl sf = new InstrumentWindowImpl(instrument.title());
		GuiThread.strictInvokeAndWait(new Runnable(){	@Override public void run() {
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
		_jframe.setBounds(_bounds);
	}
	
	private void persistWindowsProperties() {
		_jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				_bounds = _jframe.getBounds();
			}
		});
	}

	private void changeWindowCloseEventToMinimizeEvent() {
		_jframe.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
		_jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				_bounds = _jframe.getBounds();
				_jframe.setState(Frame.ICONIFIED);
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
				GuiThread.assertInGuiThread();
				open();						
			}
		};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
	}
	
	private void open() {
		_jframe.setState(Frame.NORMAL);
		_jframe.setVisible(true);
		_jframe.requestFocusInWindow();
	}
	
	private void addExitAction(TrayIcon tray) {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Exit";
			}
			@Override
			public void run() {
				persistWindowsProperties();
				System.exit(0);
			}
		};
		tray.addAction(cmd);
		_mainMenu.getSneerMenu().addAction(cmd);
	}

	@Override
	public void run() {
		TimeboxedEventQueue.startQueueing(TIMEOUT_FOR_GUI_EVENTS);
		initialize();
		waitUntilTheGuiThreadStarts();
	}
	
	
//	private void moveInstrument(int index, InstrumentWindow frame) {
//		_contentPanel.remove(frame.contentPane());
//		_contentPanel.add(frame.contentPane(), index);
//	}
//
//	private void moveInstrumentDown(InstrumentWindow frame) {
//		_contentPanel.remove(frame.contentPane());
//		_contentPanel.add(frame.contentPane(), 0);
//	}
//
//	private void moveInstrumentUp(InstrumentWindow frame) {
//		_contentPanel.remove(frame.contentPane());
//		_contentPanel.add(frame.contentPane());
//	}

	@Override
	public void show() {
		_threadPool.registerActor(this);
	}
}

class ContentPane extends JPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(190, 190, 190));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}