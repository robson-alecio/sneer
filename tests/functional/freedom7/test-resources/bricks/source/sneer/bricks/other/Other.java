package sneer.bricks.other;

import java.io.Serializable;

//sample comment
public interface Other<T> extends Comparable<String>, Serializable {
	void method(String arg) throws Exception;
}