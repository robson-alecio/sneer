package spikes.legobricks;

import javax.swing.JOptionPane;

import spikes.lego.Brick;
import spikes.lego.Startable;
import spikes.legobricks.name.NameKeeper;
import spikes.legobricks.threadpool.ThreadPool;

public class NameGui implements Startable {

	@Brick
	private NameKeeper _keeper;
	
	@Brick
	private ThreadPool _pool;
	
	@Override
	public void start() 	{
		_pool.run(new Runnable() { @Override public void run() {
			keepAskingForName();
		}});
	}

	private void keepAskingForName() {
		while (true) {
			String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", _keeper.sayMyNameBeach());
			if (sampleName == null) break;
			_keeper.setName(sampleName);
		}
	}
	
}
