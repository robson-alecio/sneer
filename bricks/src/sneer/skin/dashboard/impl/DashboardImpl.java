package sneer.skin.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.dashboard.SnappFrame;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.mainMenu.MainMenu;
import sneer.skin.viewmanager.Snapp;
import wheel.io.ui.action.Action;
import wheel.io.ui.action.SelectableAction;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;

public class DashboardImpl implements Dashboard, Runnable {
	
	private static transient final int _WIDTH = 250;
	private static transient final int _HOFFSET = 30;
	
	@Inject
	static private ThreadPool threadPool;
	
	@Inject
	static private ImageFactory imageFactory;
	
	@Inject
	static private MainMenu mainMenu;
		
	private Dimension screenSize;
	private Rectangle bounds;
	private boolean isLocked;
	
	private transient Window window;
	private transient JFrame jframe;
	private transient JWindow jwindow;
	private transient JPanel rootPanel;
	private transient JPanel contentPanel;
	
	public DashboardImpl() {
		threadPool.registerActor(this);
		isLocked = false;
	}

	private void initialize() {
		
		initWindows();	
		resizeWindow();

		TrayIconImpl tray = null;
		try {
			tray = new TrayIconImpl(imageFactory.getImageUrl(DefaultIcons.logo16x16));
		} catch (SystemTrayNotSupported e1) {
			changeWindowCloseEventToMinimizeEvent();
			return;
		}
		
		addOpenWindowAction(tray);
		addLockUnlockAction();
		addExitAction(tray);
		addBorderTypeAction();
	}

	public void refreshLaf() {
		SwingUtilities.updateComponentTreeUI(window);
	}

	private void initWindows() {
		jframe = new JFrame();
		jwindow = new JWindow();
		rootPanel = new JPanel();
		
		if(isLocked){
			window = jwindow;
			rootPanel = (JPanel) jwindow.getContentPane();
		}else{
			window = jframe;
			rootPanel = (JPanel) jframe.getContentPane();
		}
		
		rootPanel.setLayout(new BorderLayout());
		rootPanel.add(mainMenu.getWidget(), BorderLayout.NORTH);
		contentPanel = new JPanel();
		rootPanel.add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout());
	}
	
	@Override
	public SnappFrame installSnapp(final Snapp snapp) {
		final SnappFrameImpl sf = new SnappFrameImpl(snapp.getName());
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					contentPanel.add(sf);
					snapp.init(sf.getContentPane());
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
		window.setBounds(bounds);
	}
	
	private void persistWindowsProperties() {
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = window.getBounds();
				isLocked = (window instanceof JWindow);
			}
		});
	}

	private void changeWindowCloseEventToMinimizeEvent() {
		jframe.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = window.getBounds();
				jframe.setState(Frame.ICONIFIED);
			}
		});
	}
	
	private void changeJFrameToJWindow() {
		bounds = jframe.getBounds();
		jframe.setVisible(false);
		jframe.setContentPane(new JPanel());
		
		jwindow.setContentPane(rootPanel);
		jwindow.setBounds(bounds);
		jwindow.setVisible(true);
		window = jwindow;
		isLocked = true;
	}
	
	private void changeJWindowToJFrame() {
		bounds = jwindow.getBounds();
		jwindow.setVisible(false);
		jwindow.setContentPane(new JPanel());
		
		jframe.setContentPane(rootPanel);
		jframe.setBounds(bounds);
		jframe.setVisible(true);
		window = jframe;
		isLocked = false;
	}
	
	private void addLockUnlockAction() {
		SelectableAction cmd = new SelectableAction(){
			@Override
			public String caption() {
				return "Lock";
			}
			@Override
			public void run() {
				if(window==jframe){
					changeJFrameToJWindow();
				}else{
					changeJWindowToJFrame();
				}
			}
			@Override
			public boolean isSelected() {
				return window==jwindow;
			}
		};
		
		mainMenu.getSneerMenu().addAction(cmd);
	}	
	
	private void addBorderTypeAction() {
		SelectableAction cmd = new SelectableAction(){
			boolean deafaulBorder = true;
			@Override
			public String caption() {
				return "Default Snapp Border";
			}
			@Override
			public void run() {
				if(SnappFrameImpl.hasDefaultWindowBorder()){
					SnappFrameImpl.setDefaultWindowBorder(null);
				}else{
					SnappFrameImpl.setDefaultWindowBorder(
						new LineBorder(Color.WHITE,2,true));
				}
				refreshLaf();
				deafaulBorder=!deafaulBorder;
			}
			@Override
			public boolean isSelected() {
				return deafaulBorder;
			}
		};
		
		mainMenu.getPreferencesMenu().addAction(cmd);
	}
	
	private void addOpenWindowAction(TrayIconImpl tray) {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Open";
			}
			@Override
			public void run() {
				SwingUtilities.invokeLater(
					new Runnable(){
						public void run() {
							if(window==jframe){
								jframe.setState(Frame.NORMAL);
							}
							window.setVisible(true);
							window.requestFocusInWindow();						}
					}
				);
			}
		};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
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
}