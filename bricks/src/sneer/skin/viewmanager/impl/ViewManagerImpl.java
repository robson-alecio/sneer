package sneer.skin.viewmanager.impl;

import javax.swing.JFrame;

import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.skin.viewmanager.View;
import sneer.skin.viewmanager.ViewManager;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ViewManagerImpl implements ViewManager {

	@Inject
	private Me _me;

	@Override
	public void register(View view) {
		JFrame container = new JFrame();
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container.setSize(200, 300);
		container.setVisible(true);
		
		Signal<Party> partySignal = new RegisterImpl<Party>(_me).output();
		view.init(container, partySignal);
	}

}
