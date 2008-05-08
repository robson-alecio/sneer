package sneer.bricks.other;

import java.io.Serializable;

import sneer.lego.Brick;

//sample comment
public interface Other<T> extends Comparable<String>, Serializable, Brick {
	
	void method(String arg) throws Exception;
	
	String callSample();
	
	void callA();
}