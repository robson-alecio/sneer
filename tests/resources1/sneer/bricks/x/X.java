package sneer.bricks.x;

import java.io.Serializable;

import sneer.kernel.container.Brick;

//sample comment
public interface X<T> extends Comparable<String>, Serializable, Brick {
	
	void method(String arg) throws Exception;
	
	String callZ();
	
	String callY();
}