package spikes.legobricks;

import javax.swing.JOptionPane;

import spikes.lego.Brick;
import spikes.lego.Toy;
import spikes.legobricks.name.NameKeeper;

public class NameGui implements Toy {

	@Brick
	private NameKeeper keeper;
	
	@Override
	public void run() {
		while (true) {
			String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", keeper.sayMyNameBeach());
			if (sampleName == null) break;
			keeper.setName(sampleName);
		}
	}

}
