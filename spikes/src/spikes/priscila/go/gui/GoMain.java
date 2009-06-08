package spikes.priscila.go.gui;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.Brickness;
import sneer.commons.environments.Environments;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signals;
import spikes.priscila.go.Move;
import spikes.priscila.go.GoBoard.StoneColor;

public class GoMain {
	
	
	public GoMain() {
		Environments.runWith(Brickness.newBrickContainer(), new Runnable() { @Override public void run() {
			init();
		}});
	}
	
	private void init() {
		Register<Move> moveRegister = my(Signals.class).newRegister(null);
		new GoFrame(moveRegister, StoneColor.BLACK, 0);
		new GoFrame(moveRegister, StoneColor.WHITE, 500);
	}

	public static void main(String[] args){
		my(GuiThread.class).invokeAndWait(new Runnable(){@Override public void run() {
			new GoMain();
		}});
	}
	
}
