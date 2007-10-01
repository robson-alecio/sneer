package sneer.kernel.communication.impl;

import java.util.Hashtable;
import java.util.Map;

import wheel.lang.Omnivore;


public class Router<U> implements Omnivore<U>{
	
	private Map<Class<?>, Omnivore<U>> _clazzToCallbacks = new Hashtable<Class<?>, Omnivore<U>>();
	private Omnivore<U> _unrecognized;
	
	public Router(Omnivore<U> unrecognized){
		_unrecognized = unrecognized;
	}
	
	public void register(Class<?> clazz, Omnivore<U> callback){
		_clazzToCallbacks.put(clazz, callback);
	}
	
	public void consume(U object){
		boolean unrecognized = true;
		if (object != null)
			for(Class<?> clazz:_clazzToCallbacks.keySet())
				if (accept(clazz,object)){
					_clazzToCallbacks.get(clazz).consume(object);
					unrecognized = false;
				}
		if (unrecognized)
			if (_unrecognized != null)
				_unrecognized.consume(object);
	}
	
	//override this make different kind of routers
	public boolean accept(Class<?> clazz, U object){
		return clazz.isInstance(object);
	}
	
}
