package spikes.wheel.lang;

import java.io.Serializable;

public class Counter implements Serializable{

	private int _next = 0;

	public synchronized long next() {
		return _next++;
	};

	private static final long serialVersionUID = 1L;
}
