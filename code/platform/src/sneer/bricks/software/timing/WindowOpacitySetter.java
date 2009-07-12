package sneer.bricks.software.timing;

import java.awt.Window;

public interface WindowOpacitySetter {
	
	<T> Animator animator(int duration,  Window window, float start, float end);
}

