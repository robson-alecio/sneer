package org.sneer.gui.general.start;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.sneer.image.IconFactory;

public class Start{

	private static final long serialVersionUID = 1L;
	private SelectServiceWindow win = new SelectServiceWindow();

	public Start() throws AWTException {
		initialize();
	}
	
	public void initialize() throws AWTException{
		
		win.addMouseListener(new MouseListener(){
	        public void mouseEntered(MouseEvent e) {
	        	win.requestFocus();
	        }
	        public void mouseExited(MouseEvent e) {
	        	Point p = e.getPoint();
	        	if(!win.contains(p))        	
	        		win.setVisible(false);
	        }
			public void mouseClicked(MouseEvent arg0) {}
	        public void mousePressed(MouseEvent e) {}
	        public void mouseReleased(MouseEvent e) {}
			
		});

		TrayIcon trayIcon;

		if (SystemTray.isSupported()) {

		    SystemTray tray = SystemTray.getSystemTray();   
		    Image image = IconFactory.getIcon("logo16.png").getImage();

		    MouseListener mouseListener = new MouseListener() {
		                
		        public void mouseClicked(MouseEvent e) {
		        	if(e.getButton()==MouseEvent.BUTTON1){
			            Point p = e.getPoint();
			    		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
			            if(p.x<dim.width/2){
				            if(p.y<dim.height/2){
				            	win.setBounds(p.x, p.y, 500, 130);
				            }else{
				            	win.setBounds(p.x, p.y-150, 500, 130);
				            }
			            }else{
				            if(p.y<dim.height/2){
				            	win.setBounds(dim.width-500, p.y, 500, 130); //1o Quadrante
				            }else{
				            	win.setBounds(dim.width-500, p.y-150, 500, 130); //4o Quadrante
				            }	            	
			            }
			            win.updateBackground();
			            win.setVisible(true);	
			            win.requestFocus();
		        	}
		        }
		        public void mouseEntered(MouseEvent e) {}
		        public void mouseExited(MouseEvent e) {}
		        public void mousePressed(MouseEvent e) {}
		        public void mouseReleased(MouseEvent e) {}
		    };
		            
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
		    popup.add(defaultItem);

		    trayIcon = new TrayIcon(image, "Sneer", popup);	            
		    trayIcon.setImageAutoSize(false);
		    trayIcon.addMouseListener(mouseListener);
		    tray.add(trayIcon);

		}
	}

	public static void main(String[] args) throws AWTException {
		new Start();
	}
}
