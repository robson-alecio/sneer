package spikes.priscila.go.gui;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environments;
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
		my(GuiThread.class).invokeAndWaitForWussies(new Runnable(){@Override public void run() {
			new GoMain();
		}});
	}
	
}
