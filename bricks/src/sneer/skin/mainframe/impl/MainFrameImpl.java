package sneer.skin.mainframe.impl;

import javax.swing.JFrame;

import sneer.lego.Startable;
import sneer.skin.mainframe.MainFrame;

public class MainFrameImpl implements MainFrame, Startable {
	
//	@Inject
//	static private Me _me;

	@Override
	public void start() {
		JFrame container = new JFrame();
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container.setSize(200, 300);
		container.setVisible(true);
		
//		Signal<Party> partySignal = new RegisterImpl<Party>(_me).output();
//		view.init(container, partySignal);
	}

}
