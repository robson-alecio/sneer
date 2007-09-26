package sneer.apps.asker;

import java.util.ArrayList;
import java.util.List;

import wheel.lang.Omnivore;

public class Router<U> implements Omnivore<U>{
	
	private List<Class<?>> _clazzes = new ArrayList<Class<?>>();
	private List<Omnivore<U>> _callbacks = new ArrayList<Omnivore<U>>();
	private Omnivore<U> _unrecognized;
	
	public Router(Omnivore<U> unrecognized){
		_unrecognized = unrecognized;
	}
	
	public void register(Class<?> clazz, Omnivore<U> callback){
		_clazzes.add(clazz);
		_callbacks.add(callback);
	}
	
	public void consume(U object){
		boolean unrecognized = true;
		if (object != null)
			for(int t=0;t<_clazzes.size();t++)
				if (accept(_clazzes.get(t),object)){
					_callbacks.get(t).consume(object);
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
