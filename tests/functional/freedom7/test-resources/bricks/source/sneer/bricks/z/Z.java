package sneer.bricks.z;

import sneer.kernel.container.Brick;

public interface Z extends Brick {
	
	Helper helper();
	
	String doSomething();
	
	ClassLoader libClassLoader();
	
}