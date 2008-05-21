package sneer.bricks.x;

import java.io.Serializable;

import sneer.lego.Brick;

//sample comment
public interface X<T> extends Comparable<String>, Serializable, Brick {
	
	void method(String arg) throws Exception;
	
	String callZ();
	
	String callY();
}