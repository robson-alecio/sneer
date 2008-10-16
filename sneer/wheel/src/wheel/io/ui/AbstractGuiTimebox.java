package wheel.io.ui;

import wheel.lang.Timebox;;

public abstract class AbstractGuiTimebox extends Timebox {
	
	private static int TIMEOUT = 7000;

	public AbstractGuiTimebox() {
		super(TIMEOUT, false);
	}
	
}
