package sneer.bricks.software.timing;

import java.awt.Window;

public interface WindowOpacitySetter {
	
	<T> Animator animator(Window window, float start, float end, int duration);
	<T> Animator animator(Window window, float start, float end, int forwardDuration, int backwardDuration );

}

