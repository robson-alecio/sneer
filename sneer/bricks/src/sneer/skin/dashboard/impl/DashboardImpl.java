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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.dashboard.InstrumentWindow;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.main_Menu.MainMenu;
import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import wheel.graphics.Images;
import wheel.io.ui.action.Action;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.impl.SimpleListReceiver;

class DashboardImpl implements Dashboard, Runnable {
	
	private static transient final int _WIDTH = 250;
	private static transient final int _HOFFSET = 30;
	
	@Inject
	static private ThreadPool threadPool;
	
	@Inject
	static private ImageFactory imageFactory;
	
	@Inject
	static private MainMenu mainMenu;
	
	@Inject
	static private OwnNameKeeper _ownNameKeeper;
		
	@Inject
	static private InstrumentManager _instrumentManager;

	@Inject
	private BlinkingLights _blinkingLights;
	
	@SuppressWarnings("unused")
	private final Receiver<String> _ownNameReceiver = new Receiver<String>(_ownNameKeeper.name()) { @Override public void consume(String value) {
		updateTitle();
	}};
		
	private Dimension screenSize;
	private Rectangle bounds;
	
	private transient JFrame jframe;
	private transient JPanel rootPanel;
	private transient JPanel contentPanel;
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Instrument> _instrumentsReceiver;
	
	public DashboardImpl() {
		threadPool.registerActor(this);
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
			}};
	}

	private URL logoIconURL() {
		return imageFactory.getImageUrl(DefaultIcons.logo16x16);
	}

	private void initWindows() {
		initFrame();
		initRootPanel();
	}

	private void initRootPanel() {
		rootPanel = new JPanel();
		
		rootPanel = (JPanel) jframe.getContentPane();
		
		rootPanel.setLayout(new BorderLayout());
		rootPanel.add(mainMenu.getWidget(), BorderLayout.NORTH);
		contentPanel = new ContentPane();
		rootPanel.add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout());
	}

	private void initFrame() {
		jframe = new JFrame();
		jframe.setIconImage(Images.getImage(logoIconURL()));

		updateTitle();
	}

	private InstrumentWindow install(final Instrument instrument) {
		final InstrumentWindowImpl sf = new InstrumentWindowImpl();
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					contentPanel.add(sf);
					instrument.init(sf);
					sf.revalidate();
				}
			}
		);
        return sf;
	}
	
	private void resizeWindow() {
		Dimension newSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if(bounds==null || screenSize==null || !screenSize.equals(newSize)){
			screenSize  = newSize;
			bounds = new Rectangle((int) screenSize.getWidth() - _WIDTH, 0, _WIDTH,	
								   (int) screenSize.getHeight() - _HOFFSET);
		}
		jframe.setBounds(bounds);
	}
	
	private void persistWindowsProperties() {
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = jframe.getBounds();
			}
		});
	}

	private void changeWindowCloseEventToMinimizeEvent() {
		jframe.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = jframe.getBounds();
				jframe.setState(Frame.ICONIFIED);
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
				SwingUtilities.invokeLater( new Runnable(){ public void run() {
					open();						
				}});
			}
		};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
	}
	
	private void open() {
		jframe.setState(Frame.NORMAL);
		jframe.setVisible(true);
		jframe.requestFocusInWindow();
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
		mainMenu.getSneerMenu().addSeparator();
		mainMenu.getSneerMenu().addAction(cmd);
	}

	@Override
	public void run() {
		initialize();
	}
	
	@Override
	public Container getContentPanel() {
		return contentPanel;
	}
	
	@Override
	public Container getRootPanel() {
		return rootPanel;
	}
	
	
	@Override
	public void moveInstrument(int index, InstrumentWindow frame) {
		contentPanel.remove(frame.getContent());
		contentPanel.add(frame.getContent(), index);
	}

	@Override
	public void moveInstrumentDown(InstrumentWindow frame) {
		contentPanel.remove(frame.getContent());
		contentPanel.add(frame.getContent(), 0);
	}

	@Override
	public void moveInstrumentUp(InstrumentWindow frame) {
		contentPanel.remove(frame.getContent());
		contentPanel.add(frame.getContent());
	}

	private void updateTitle() {
		if (jframe == null) return;
		
		jframe.setTitle("Sneer - " + _ownNameKeeper.name().currentValue());
	}	
}

class ContentPane extends JPanel{
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		//TODO: add a gradient user configuration
		super.paintComponent(g);
		g.setColor(new Color(0, 0, 0, 50));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	
	}
}