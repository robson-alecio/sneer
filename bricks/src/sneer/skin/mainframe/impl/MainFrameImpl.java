package sneer.skin.mainframe.impl;

import javax.swing.JFrame;

import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.lego.Startable;
import sneer.skin.mainframe.MainFrame;
import sneer.skin.viewmanager.PartyView;
import sneer.skin.viewmanager.ViewManager;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class MainFrameImpl implements MainFrame, Startable {
	
	@Inject
	static private ViewManager _viewManager;

	@Inject
	static private Me _me;
	
	@Override
	public void start() {
		JFrame container = new JFrame();
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container.setSize(200, 300);
		container.setVisible(true);
		
		PartyView view = _viewManager.getOnlyOnePartyViewForNow();
		
		Signal<Party> partySignal = new RegisterImpl<Party>(_me).output();
		view.init(container, partySignal);
	}

}
