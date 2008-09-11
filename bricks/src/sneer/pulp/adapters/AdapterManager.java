package sneer.pulp.adapters;

import wheel.lang.Functor;

public interface AdapterManager {

	<From, To> void register(Class<From> fromType, Class<To> toType, Functor<From, To> functor);

	<From, To> To adapterFor(From object, Class<To> toType);

}
