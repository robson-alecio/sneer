package sneer.skin.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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

import sneer.kernel.container.Inject;
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
import wheel.io.ui.action.Action;
import wheel.io.ui.graphics.Images;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.impl.SimpleListReceiver;

//Implement Persist window size and position
class DashboardImpl implements Dashboard, Runnable {
	
	private static final int TIMEOUT_FOR_GUI_EVENTS = 30 * 1000;
	
	private Dimension _screenSize;
	private Rectangle _bounds;
	
	private transient JFrame _jframe;
	private transient JPanel _rootPanel;
	private transient JPanel _contentPanel;
	
	private static transient final int WIDTH = 250;
	private static transient final int H_OFFSET = 30;
	
	@Inject
	static private ThreadPool _threadPool;
	
	@Inject
	static private ImageFactory _imageFactory;
	
	@Inject
	static private MainMenu _mainMenu;
	
	@Inject
	static private OwnNameKeeper _ownNameKeeper;
		
	@Inject
	static private InstrumentManager _instrumentManager;

	@Inject
	static private BlinkingLights _blinkingLights;
	
	@SuppressWarnings("unused")
	private final Receiver<String> _ownNameReceiver = new Receiver<String>(_ownNameKeeper.name()) { @Override public void consume(String value) {
		updateTitle();
	}};
		
	@SuppressWarnings("unused")
	private SimpleListReceiver<Instrument> _instrumentsReceiver;
	
	DashboardImpl() {
		TimeboxedEventQueue.startQueueing(TIMEOUT_FOR_GUI_EVENTS);
		_threadPool.registerActor(this);
	}

	private void initialize() {
		initWindows();	
		resizeWindow();
		initTrayIconIfPossible();
		addInstrumentsReceiver();
		open();
	}

	private void initTrayIconIfPossible() {
		TrayIconImpl tray = null;
		try {
			tray = new TrayIconImpl(logoIconURL());
		} catch (SystemTrayNotSupported e1) {
			_blinkingLights.turnOn(LightType.INFO, e1.getMessage() + " When closing the Sneer window, it will be minimized instead of closed.");
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
			protected void elementToBeRemoved(Instrument element) {
				throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
			}

			@Override
			public void elementInserted(int index, Instrument value) {
				throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
			}};
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
		_rootPanel.add(_mainMenu.getWidget(), BorderLayout.NORTH);
		_contentPanel = new ContentPane();
		_rootPanel.add(_contentPanel, BorderLayout.CENTER);
		_contentPanel.setLayout(new FlowLayout());
	}

	private InstrumentWindow install(final Instrument instrument) {
		final InstrumentWindowImpl sf = new InstrumentWindowImpl();
		GuiThread.strictInvokeAndWait(new Runnable(){	@Override public void run() {
			_contentPanel.add(sf);
			instrument.init(sf);
			if(instrument.defaultHeight()>Instrument.ANY_HEIGHT)
				resizeContainer(instrument, sf);
			sf.revalidate();
		}});
        return sf;
	}

	private void resizeContainer(final Instrument instrument,	final InstrumentWindowImpl sf) {
		int width = sf.getSize().width;
		Dimension size = new Dimension(width, instrument.defaultHeight());
		sf.setMinimumSize(size);
		sf.setPreferredSize(size);
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
	
	private void addOpenWindowAction(TrayIconImpl tray) {
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
	
	private void addExitAction(TrayIconImpl tray) {
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
		initialize();
	}
	
	@Override
	public Container getContentPanel() {
		return _contentPanel;
	}
	
	@Override
	public Container getRootPanel() {
		return _rootPanel;
	}
	
	
	@Override
	public void moveInstrument(int index, InstrumentWindow frame) {
		_contentPanel.remove(frame.getContent());
		_contentPanel.add(frame.getContent(), index);
	}

	@Override
	public void moveInstrumentDown(InstrumentWindow frame) {
		_contentPanel.remove(frame.getContent());
		_contentPanel.add(frame.getContent(), 0);
	}

	@Override
	public void moveInstrumentUp(InstrumentWindow frame) {
		_contentPanel.remove(frame.getContent());
		_contentPanel.add(frame.getContent());
	}
}

class ContentPane extends JPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0, 0, 0, 50));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}