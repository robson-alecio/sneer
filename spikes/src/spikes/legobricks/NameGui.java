package spikes.legobricks;

import javax.swing.JOptionPane;

import sneer.lego.Brick;
import sneer.lego.Startable;
import spikes.legobricks.name.NameKeeper;
import spikes.legobricks.security.Sorry;
import spikes.legobricks.threadpool.ThreadPool;
import wheel.io.ui.User;

public class NameGui implements Startable {

	@Brick
	private NameKeeper _keeper;
	
	@Brick
	private ThreadPool _pool;
	
	@Brick
	private User user;
	
	@Override
	public void start() {
		_pool.run(new Runnable() { @Override public void run() {
			keepAskingForName();
		}});
	}

	private void keepAskingForName() {
		while (true) {
			String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", _keeper.getName());
			if (sampleName == null) break;
			try {
				_keeper.setName(sampleName);
			} catch(Sorry sorry) {
				user.acknowledgeNotification("Can not set name: "+sampleName);
			}
		}
	}
}
