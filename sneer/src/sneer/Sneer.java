package sneer;

import static sneer.strap.SneerDirectories.validNumber;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import sneer.strap.SneerDirectories;
import sneer.strap.VersionUpdateAttempt;
import wheelexperiments.Log;


public class Sneer {
	
	private TrayIcon trayIcon;

	public Sneer() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void sysTray() throws AWTException{
		if (!SystemTray.isSupported())
			return;
		
	    SystemTray tray = SystemTray.getSystemTray();
		PopupMenu popup = new PopupMenu();
	    MenuItem defaultItem = new MenuItem("Exit");
	    defaultItem.addActionListener(
	    	new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {	        	
	    			System.out.println("Exiting...");
	    			System.exit(0);
	    		}
	    	}
	    );
		
	    Image image = Toolkit.getDefaultToolkit().getImage(Sneer.class.getResource("logo16.png"));
	    trayIcon = new TrayIcon(image, "Sneer", popup);	            
	    trayIcon.setImageAutoSize(false);
//	    trayIcon.addMouseListener(mouseListener);
	    tray.add(trayIcon);		
	}
	

	private void tryToRun() throws Exception {		
		int TODO_Optimization_DoInParallelThread;
		tryToDownloadNextVersion();

		new NameAcquisition();
	}


	private void tryToDownloadNextVersion() throws Exception {
		sysTray();
		if(trayIcon!=null){
			trayIcon.displayMessage("Sneer", "Searching for updates...", MessageType.INFO);		
		}
		File mainApp = SneerDirectories.findNewestMainApp();
		int currentVersion = validNumber(mainApp.getName());
		new VersionUpdateAttempt(currentVersion + 1);
		
		Thread.sleep(5000);
	}
}
