package org.sneer.swing.window.transparent;

import java.awt.Dimension;

@SuppressWarnings("unchecked")
public class JFrame<CORE extends Core> extends javax.swing.JFrame {
	
	protected static final long serialVersionUID = 1L;
	protected CORE core;
	
	public JFrame() {
		core = (CORE) new Core();
		init();
	}
	
	public JFrame(String title) {
		super(title);
		core = (CORE) new Core();
		init();
	}
	
	protected JFrame(CORE core) {
		this.core = core;
		init();
	}

	protected void init() {
		this.setSize(new Dimension(300, 300));
		this.setContentPane(core.getTransparentPane());
		this.setGlassPane(core.newDefaultGlassPane());
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.getGlassPane().setVisible(visible);
	}

	public CORE getCore() {
		return core;
	}
	
	public static void main(String[] args) {
		JFrame win = new JFrame("Transparent Window");
		win.setBounds(10, 10, 400, 500);		
		win.setVisible(true);		
	}		
}
