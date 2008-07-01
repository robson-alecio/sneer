package sneer.skin.dashboard.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.mainMenu.MainMenu;
import wheel.io.ui.Action;
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
		contentPanel = new JPanel();
		
		if(isLocked){
			window = jwindow;
			contentPanel = (JPanel) jwindow.getContentPane();
		}else{
			window = jframe;
			contentPanel = (JPanel) jframe.getContentPane();
		}
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(mainMenu.getWidget(), BorderLayout.NORTH);
		createDemoTaskPane();
	}

	private void createDemoTaskPane() {
		JPanel desktop = new JPanel();
		desktop.setLayout(new FlowLayout());
        
		SnappFrame taskPane;
        
        taskPane = new SnappFrame("Teste1");
        desktop.add(taskPane);
        
        taskPane = new SnappFrame("Teste2");
        desktop.add(taskPane);
        
        
        taskPane = new SnappFrame("Teste3");
        JTextArea textArea = new JTextArea(15, 20);
		taskPane.add(new JScrollPane(textArea));
        desktop.add(taskPane);
        
        contentPanel.add(desktop, BorderLayout.CENTER);
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
		
		jwindow.setContentPane(contentPanel);
		jwindow.setBounds(bounds);
		jwindow.setVisible(true);
		window = jwindow;
		isLocked = true;
	}
	
	private void changeJWindowToJFrame() {
		bounds = jwindow.getBounds();
		jwindow.setVisible(false);
		jwindow.setContentPane(new JPanel());
		
		jframe.setContentPane(contentPanel);
		jframe.setBounds(bounds);
		jframe.setVisible(true);
		window = jframe;
		isLocked = false;
	}
	
	private void addLockUnlockAction() {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return (window==jframe)?"Lock":"Unlock";
			}
			@Override
			public void run() {
				if(window==jframe){
					changeJFrameToJWindow();
				}else{
					changeJWindowToJFrame();
				}
			}
		};
		
		mainMenu.getSneerMenu().addAction(cmd);
	}	
	
	private void addBorderTypeAction() {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Change Snapp Border";
			}
			@Override
			public void run() {
				if(SnappFrame.hasDefaultWindowBorder()){
					SnappFrame.setDefaultWindowBorder(null);
				}else{
					SnappFrame.setDefaultWindowBorder(
						new LineBorder(Color.WHITE,2,true));
				}
				refreshLaf();
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
}