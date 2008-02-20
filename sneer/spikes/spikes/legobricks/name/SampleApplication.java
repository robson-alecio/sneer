package spikes.legobricks.name;

import javax.swing.JOptionPane;

import spikes.lego.Brick;
import spikes.lego.app.Toy;

public class SampleApplication implements Toy {

	@Brick
	private NameKeeper keeper;
	
	@Override
	public void run() {
		String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", "no name"); 
		keeper.setName(sampleName);
	}
}
