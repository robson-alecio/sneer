package spikes.priscila.go.gui;

import sneer.commons.environments.Environments;
import sneer.kernel.container.Containers;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.impl.RegisterImpl;
import spikes.priscila.go.Move;
import spikes.priscila.go.GoBoard.StoneColor;
import wheel.io.ui.GuiThread;

public class GoMain {
	
	
	public GoMain() {
		Environments.runWith(Containers.newContainer(), new Runnable() { @Override public void run() {
			init();
		}});
	}
	
	private void init() {
		Register<Move> moveRegister = new RegisterImpl<Move>(null);
		new GoFrame(moveRegister, StoneColor.BLACK, 0);
		new GoFrame(moveRegister, StoneColor.WHITE, 500);
	}

	public static void main(String[] args){
		GuiThread.invokeAndWait(new Runnable(){@Override public void run() {
			new GoMain();
		}});
	}
	
}
