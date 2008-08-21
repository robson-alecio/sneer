package spikes.legobricks;

import javax.swing.JOptionPane;

import sneer.kernel.container.Inject;
import sneer.kernel.container.Startable;
import sneer.pulp.ownName.OwnNameKeeper;
import sneer.pulp.threadpool.ThreadPool;
import spikes.legobricks.security.Sorry;
import wheel.io.ui.User;

public class NameGui implements Startable {

	@Inject
	private OwnNameKeeper _keeper;
	
	@Inject
	private ThreadPool _pool;
	
	@Inject
	private User user;
	
	@Override
	public void start() {
		_pool.registerActor(new Runnable() { @Override public void run() {
			keepAskingForName();
		}});
	}

	private void keepAskingForName() {
		while (true) {
			String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", _keeper.name().currentValue());
			if (sampleName == null) break;
			try {
				_keeper.nameSetter().consume(sampleName);
			} catch(Sorry sorry) {
				user.acknowledgeNotification("Can not set name: "+sampleName);
			}
		}
	}
}
