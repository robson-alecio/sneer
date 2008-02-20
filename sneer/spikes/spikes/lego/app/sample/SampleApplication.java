package spikes.lego.app.sample;

import javax.swing.JOptionPane;

import spikes.lego.Brick;
import spikes.lego.app.Application;

public class SampleApplication implements Application {

	@Brick
	private NameKeeper keeper;
	
	@Override
	public void run() {
		String sampleName = JOptionPane.showInputDialog(null, "Whats your name?", "no name"); 
		keeper.setName(sampleName);
	}
}
