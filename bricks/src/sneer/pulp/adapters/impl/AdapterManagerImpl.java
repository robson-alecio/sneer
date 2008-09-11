package sneer.pulp.adapters.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.adapters.AdapterManager;
import wheel.lang.Functor;
import wheel.lang.Types;

public class AdapterManagerImpl implements AdapterManager {
	
	private static class Adapter<From, To> {
		public final Class<From> _fromType;
		public final Class<To> _toType;
		public final Functor<From, To> _functor;

		public Adapter(Class<From> fromType, Class<To> toType,
				Functor<From, To> functor) {
			_fromType = fromType;
			_toType = toType;
			_functor = functor;
		}
	}
	
	private final List<Adapter<?, ?>> _adapters = new ArrayList<Adapter<?, ?>>();

	@Override
	public <From, To> To adapterFor(From object, Class<To> toType) {
		if (null == object) throw new IllegalArgumentException();
		if (null == toType) throw new IllegalArgumentException();
		
		final Class<?> fromType = object.getClass();
		for (Adapter<?, ?> a : _adapters) {
			if (a._fromType == fromType && a._toType == toType) {
				final Functor<From, To> functor = Types.cast(a._functor);
				return functor.evaluate(object);
			}
		}
		return null;
	}

	@Override
	public <From, To> void register(Class<From> fromType, Class<To> toType,
			Functor<From, To> functor) {
		_adapters.add(new Adapter<From, To>(fromType, toType, functor));
	}

}
