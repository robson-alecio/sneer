package sneer.skin.mainframe.impl;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
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
	
	private transient JFrame window = new JFrame();

	public MainFrameImpl() {
		threadPool.registerActor(this);
	}

	private void initWindow() {
		try {
			UIManager.setLookAndFeel(new NapkinLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e);
		}
		
		resize();

		TrayIconImpl tray = null;
		try {
			tray = new TrayIconImpl(MainFrameImpl.class.getResource("sneer.png"));
		} catch (SystemTrayNotSupported e1) {
			setWindowCloseToMinimize();
			return;
		}
		
		addActionOpenWindow(tray);
		addActionBye(tray);
		persistWindowsProperties();
	}

	private void persistWindowsProperties() {
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = window.getBounds();
			}
		});
	}

	private void setWindowCloseToMinimize() {
		window.setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = window.getBounds();
				window.setState(Frame.ICONIFIED);
			}
		});
	}

	private void addActionOpenWindow(TrayIconImpl tray) {
		//Set Visible
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Open!";
			}
			@Override
			public void run() {
				window.setVisible(true);
				window.setState(Frame.NORMAL);
			}
		};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
	}

	private void addActionBye(TrayIconImpl tray) {
		//Set Visible
		Action cmd = new Action(){
			@Override
			public String caption() {
				return "Bye!";
			}
			@Override
			public void run() {
				System.exit(0);
			}
		};
		tray.addAction(cmd);
	}

	private void resize() {
		Dimension newSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if(screenSize==null || !screenSize.equals(newSize)){
			//set a new size
			screenSize  = newSize;
			bounds = new Rectangle((int) screenSize.getWidth() - _WIDTH, 0, _WIDTH,	
								   (int) screenSize.getHeight() - _HOFFSET);
		}
		window.setBounds(bounds);
	}

	@Override
	public void run() {
		initWindow();
	}
}
