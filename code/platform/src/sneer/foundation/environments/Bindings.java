package sneer.foundation.environments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Bindings {

	public Bindings(Object... bindings) {
		bind(bindings);
	}
	
	private final List<Object> _bindings = Collections.synchronizedList(new ArrayList<Object>());
	
	public Environment environment() {
		return new Environment() { @Override public synchronized <T> T provide(Class<T> intrface) {
			for (Object candidate : _bindings.toArray())
				if (intrface.isInstance(candidate))
					return (T)candidate;
			return null;
		}};
	}

	public synchronized void bind(Object... bindings) {
		for (Object binding : bindings)
			_bindings.add(binding);
	}

}
