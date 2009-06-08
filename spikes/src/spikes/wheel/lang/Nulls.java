package spikes.wheel.lang;

import java.util.Iterator;

public class Nulls {

	public static <T> T nvl(T obj1, T obj2) {
		if (obj1 == null) return obj2;
		
		return obj1;
	}
	
	public static <T> T firstOrNull(Iterable<T> iterable) {
		Iterator<T> it = iterable.iterator();
		return it.hasNext()
			? it.next()
			: null;
	}

}
