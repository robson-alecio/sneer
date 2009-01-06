package spikes.priscila.go.gui;

import spikes.priscila.go.Move;
import spikes.priscila.go.GoBoard.StoneColor;
import wheel.io.ui.GuiThread;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class Main {
	
	
	public Main() {
		Register<Move> moveRegister = new RegisterImpl<Move>(null);
		new GoFrame(moveRegister, StoneColor.BLACK);
		new GoFrame(moveRegister, StoneColor.WHITE);
	}
	
	public static void main(String[] args){
		GuiThread.invokeAndWait(new Runnable(){@Override public void run() {
			new Main();
		}});
	}
	
}
