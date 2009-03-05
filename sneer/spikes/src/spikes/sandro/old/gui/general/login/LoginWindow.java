package spikes.sandro.old.gui.general.login;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import spikes.sandro.old.swing.layout.StackLayout;
import spikes.sandro.old.swing.panel.ImagePanel;

public class LoginWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	private JPanel avatarLayer00 = new JPanel();
	private JPanel avatarLayer01 = new JPanel();
	private JPanel avatarLayer02 = new JPanel();
	private JPanel avatarLayer03 = new JPanel();
	private JPanel avatarLayer04 = new JPanel();
	
	private JPanel composit = new JPanel();

	private ImagePanel avatar00 = new ImagePanel("logo64.png");
	private ImagePanel avatar01 = new ImagePanel("logo64.png");
	private ImagePanel avatar02 = new ImagePanel("logo64.png");
	private ImagePanel avatar03 = new ImagePanel("logo64.png");
	private ImagePanel avatar04 = new ImagePanel("logo64.png");
	
	public LoginWindow(){
		this.setContentPane(composit);
		composit.setLayout(new StackLayout());
				
		addAvatars(avatarLayer00, avatar00, 1);
		addAvatars(avatarLayer01, avatar01, 2);
		addAvatars(avatarLayer02, avatar02, 3);
		addAvatars(avatarLayer03, avatar03, 4);
		addAvatars(avatarLayer04, avatar04, 5);
		

	}
	
	protected void initLayer(JPanel layer){
		layer.setOpaque(false);
		layer.setLayout(new GridBagLayout());
		composit.add(layer, StackLayout.BOTTOM);		
	}
	
	protected void addAvatars(JPanel layer, ImagePanel avatar, double index){
		initLayer(layer);
		double alpha = 1/(index);
		avatar.setAlpha((float) alpha);
		avatar.setBorder(BorderFactory.createEtchedBorder());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridx = 0;
		
		int x = (int)(avatar.getIcon().getIconWidth()/3*(index-1));
		int y = (int)(avatar.getIcon().getIconHeight()/2*(index-1));
		
		c.insets = new Insets(0,x,y,0);
		layer.add(avatar,c);
				
	}
	
	public static void main(String[] args) {
		LoginWindow frame = new LoginWindow();
		frame.setBounds(10, 10, 200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

	}
	
}
