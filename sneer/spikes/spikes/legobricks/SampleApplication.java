package spikes.legobricks;

import javax.swing.JOptionPane;

import spikes.lego.Toy;
import spikes.lego.Brick;
import spikes.legobricks.name.NameKeeper;

public class SampleApplication implements Toy {

	@Brick
	private NameKeeper keeper;
	
	@Override
	public void run() {
		String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", "no name"); 
		keeper.setName(sampleName);
	}
}
