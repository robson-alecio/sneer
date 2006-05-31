package spikes.sandro.gui.general.start;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JWindow;


import spikes.sandro.image.IconFactory;
import spikes.sandro.swing.panel.AutoZoomImage;
import spikes.sandro.swing.panel.TransparentPanel;

public class SelectServiceWindow extends JWindow {

	private static final long serialVersionUID = 1L;

	JPanel body = new JPanel();
	TransparentPanel composit;
	
	@SuppressWarnings("unchecked")
	public SelectServiceWindow() {
		composit = new TransparentPanel();
//		composit.setBlur(3);
		composit.setBrightness(50);
		composit.setRefreshFactor(60000);
		composit.setRefreshSleep(1000);
		composit.setLayout(new GridBagLayout());
		setContentPane(composit);	
		addBody();
		addServices();
	}

	private void addServices() {
		int pos = 0;
		addService("perfil.png", pos++);
		addService("contacts.png", pos++);
		addService("community.png", pos++);
		addService("cpu.png", pos++);
		addService("find.png", pos++);
	}

	private void addService(String iconName, int pos){
		ImageIcon small = IconFactory.getIcon("small/"+iconName, true);
		ImageIcon big = IconFactory.getIcon("big/"+iconName, true);
		AutoZoomImage img = new AutoZoomImage(small,big);		
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridy = 0;
		c.gridx = pos;
		body.add(img,c);
	}
	
	private void addBody() {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.insets = new Insets(20, 20, 20, 20);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.gridy = 1;
		composit.add(body,c);		
		body.setOpaque(false);
		body.setLayout(new GridBagLayout());
		body.setBorder(BorderFactory.createEtchedBorder());
	}
	
	public void updateBackground(){
		composit.updateBackground();
	}
	
	
	public static void main(String[] args) {
		SelectServiceWindow dlg = new SelectServiceWindow();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dlg.setBounds(dim.width-550, 0, 550, 130);
		dlg.setVisible(true);
		dlg.requestFocus();	
	}
}
