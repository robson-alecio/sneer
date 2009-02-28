package spikes.priscila.go.gui;

import sneer.brickness.environments.Environments;
import sneer.kernel.container.Containers;
import spikes.priscila.go.Move;
import spikes.priscila.go.GoBoard.StoneColor;
import wheel.io.ui.GuiThread;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

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
