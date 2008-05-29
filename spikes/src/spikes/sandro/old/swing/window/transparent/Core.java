package spikes.sandro.old.swing.window.transparent;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;


import spikes.sandro.old.swing.panel.TransparentPanel;
import spikes.sandro.old.swing.panel.decorators.TitleBarDecorator;

public class Core {

	protected TransparentPanel transparentTitle;
	protected TransparentPanel transparentPane;
	protected JPanel compositPane;
	
	private GridBagConstraints c0 = new GridBagConstraints();
		
	protected Core(){}
	
	public Core(int titleH) {
		c0.ipady = titleH;
	}	

	public Component newDefaultGlassPane() {
		JComponent glassPane = new JComponent(){
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics g) {
				super.paint(g);
			}
		};
		return glassPane;
	}

	public TransparentPanel getTransparentPane() {
		if (transparentPane == null) {		
			transparentPane = new TransparentPanel();
			transparentPane.setLayout(new GridBagLayout());
		}
		return transparentPane;
	}

	public TransparentPanel getTransparentTitle(String title) {
		if (transparentTitle == null) {
			transparentTitle = new TransparentPanel();
			transparentTitle.setLayout(new GridBagLayout());
			transparentTitle.setAutoRefresh(false);
			transparentTitle.setBlur(0);
			transparentTitle.setBrightness(0);
			transparentTitle.getDecorators().add(new TitleBarDecorator(transparentTitle,title));
		}
		
		return transparentTitle;
	}

	public JPanel getCompositPane(String title) {
		if (compositPane == null) {			
			compositPane = new JPanel();
			compositPane.setLayout(new GridBagLayout());

			c0.insets = new Insets(0, 0, 0, 0);
			c0.weightx = 1.0;
			c0.weighty = 0.0;
			c0.fill = GridBagConstraints.HORIZONTAL;
			compositPane.add(getTransparentTitle(title), c0);
			
			GridBagConstraints c1 = new GridBagConstraints();
			c1.gridy = 1;
			c1.insets = new Insets(0, 0, 0, 0);
			c1.fill = GridBagConstraints.BOTH;
			c1.weightx = 1.0;
			c1.weighty = 1.0;	
			compositPane.add(getTransparentPane(), c1);
		}
		return compositPane;
	}

	public float getBlur() {
		return getTransparentPane().getBlur();
	}

	public void setBlur(float blur) {
		getTransparentPane().setBlur(blur);
	}

	public boolean isAutoRefresh() {
		return getTransparentPane().isAutoRefresh();
	}

	public void setAutoRefresh(boolean autoRefresh) {
		getTransparentPane().setAutoRefresh(autoRefresh);
		if(transparentTitle!=null)
			transparentTitle.setAutoRefresh(autoRefresh);
	}
	
	public int getBrightness() {
		return getTransparentPane().getBrightness();
	}

	public void setBrightness(int brightness) {
		getTransparentPane().setBrightness(brightness);
	}	
	
	public int getRefreshFactor() {
		return getTransparentPane().getRefreshFactor();
	}

	public void setRefreshFactor(int refreshFactor) {
		getTransparentPane().setRefreshFactor(refreshFactor);
		if(transparentTitle!=null)
			transparentTitle.setRefreshFactor(refreshFactor);
	}

	public int getRefreshSleep() {
		return getTransparentPane().getRefreshSleep();
	}

	public void setRefreshSleep(int refreshSleep) {
		getTransparentPane().setRefreshSleep(refreshSleep);
		if(transparentTitle!=null)
			transparentTitle.setRefreshSleep(refreshSleep);
	}
	
	public int getTitleHeight(){
		return getTransparentTitle("").getHeight();
	}
	public  void setTitleHeight(int h){
		c0.ipady = h;
	}	
}