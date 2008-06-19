package sneer.skin.mainframe.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import sneer.skin.mainframe.MainFrame;
import wheel.io.ui.Action;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;

public class MainFrameImpl implements MainFrame, Runnable {
	
	private static transient final int _WIDTH = 250;
	private static transient final int _HOFFSET = 30;
	
	@Inject
	static private ThreadPool threadPool;
	
	private Dimension screenSize;
	private Rectangle bounds;
	private boolean isLocked;
	
	private transient Window window;
	private transient JFrame jframe;
	private transient JWindow jwindow;
	

	public MainFrameImpl() {
		threadPool.registerActor(this);
		isLocked = false;
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(new NapkinLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			//ignore: using default L&F
		}
		
		initWindows();	
		resizeWindow();

		TrayIconImpl tray = null;
		try {
			tray = new TrayIconImpl(MainFrameImpl.class.getResource("logo16x16.png"));
		} catch (SystemTrayNotSupported e1) {
			changeWindowCloseEventToMinimizeEvent();
			return;
		}
		
		addOpenWindowAction(tray);
//		addLockUnlockAction(tray);
		addExitAction(tray);
	}

	private void initWindows() {
		jframe = new JFrame();
		jwindow = new JWindow();
		
		if(isLocked){
			window = jwindow;
		}else{
			window = jframe;
		}
		
		Container pane = jframe.getContentPane();
		pane.setLayout(new FlowLayout());
		pane.add(new JLabel("Sneer!", IconFactory.getIcon("sneer16x16.png"), SwingConstants.LEFT));
		
		
		changeWindowMaximizeEvent();
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

	private void changeWindowMaximizeEvent() {
		jframe.addWindowStateListener(new WindowStateListener(){
			@Override
			public void windowStateChanged(WindowEvent e) {
				if(e.getNewState()==Frame.MAXIMIZED_BOTH){
					jframe.setState(e.getOldState());
					changeJFrameToJWindow();
				}
			}
		});
	}
	
	private void changeJFrameToJWindow() {
		bounds = jframe.getBounds();
		jframe.setVisible(false);
		Container tmp = jframe.getContentPane();
		tmp.getParent().remove(tmp);
		jframe.dispose();
		jframe = new JFrame();
		jwindow.setContentPane(tmp);
		jwindow.setBounds(bounds);
		jwindow.setVisible(true);
		window = jwindow;
		isLocked = true;
	}
	
	private void changeJWindowToJFrame() {
		bounds = jwindow.getBounds();
		jwindow.setVisible(false);
		Container tmp = jwindow.getContentPane();
		tmp.getParent().remove(tmp);
		jwindow.dispose();
		jwindow = new JWindow();
		jframe.setContentPane(tmp);
		jframe.setBounds(bounds);
		jframe.setVisible(true);
		window = jframe;
		isLocked = false;
	}
	
//	private void addLockUnlockAction(TrayIconImpl tray) {
//		Action cmd = new Action(){
//			@Override
//			public String caption() {
//				return (window==jframe)?"Lock!":"Unlock!";
//			}
//			@Override
//			public void run() {
//				if(window==jframe){
//					changeJFrameToJWindow();
//				}else{
//					changeJWindowToJFrame();
//				}
//			}
//		};
//		tray.addAction(cmd);
//	}
	
	private void addOpenWindowAction(TrayIconImpl tray) {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Open!";
			}
			@Override
			public void run() {
				window.setVisible(true);
				if(window==jframe){
					jframe.setState(Frame.NORMAL);
				}
				window.requestFocus();
			}
		};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
	}

	private void addExitAction(TrayIconImpl tray) {
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Exit...";
			}
			@Override
			public void run() {
				persistWindowsProperties();
				System.exit(0);
			}
		};
		tray.addAction(cmd);
	}

	@Override
	public void run() {
		initialize();
	}
}