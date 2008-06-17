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

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bounds = window.getBounds();
			}
		});
		resize();
		window.setVisible(true);	
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
