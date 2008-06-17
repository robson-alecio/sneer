package sneer.skin.mainframe.impl;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import sneer.skin.mainframe.MainFrame;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.io.ui.Action;

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
		try {
			TrayIconImpl tray = new TrayIconImpl(MainFrameImpl.class.getResource("sneer.png"));
			addActionOpenWindow(tray);
			addActionBuy(tray);
			
			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					bounds = window.getBounds();
				}
			});
			
		} catch (SystemTrayNotSupported e1) {
			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					//TODO: fix window close event for system tray not supported
					bounds = window.getBounds();
					WindowEvent we = new WindowEvent(e.getWindow(),WindowEvent.WINDOW_ICONIFIED);
					window.setVisible(true);
					window.dispatchEvent(we);
				}
			});			
		}
		resize();
	}

	private void addActionOpenWindow(TrayIconImpl tray) {
		//Set Visible
		Action cmdOpenWindow = new Action(){
			@Override
			public String caption() {
				return "Open!";
			}
			@Override
			public void run() {
				window.setVisible(true);	
			}
		};
		tray.setDefaultAction(cmdOpenWindow);
	}

	private void addActionBuy(TrayIconImpl tray) {
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
