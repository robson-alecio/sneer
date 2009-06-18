package sneer.foundation.lang;

public interface Functor<A, B> {

	public static final Functor<Object, Object> IDENTITY = new Functor<Object, Object>() { @Override public Object evaluate(Object obj) {
		return obj;
	}};

	B evaluate(A value);
	
	public static final Functor<Object, Object> SINGLETON_FUNCTOR = new Functor<Object, Object>() { @Override public Object evaluate(Object value) {
		return this; //Any single object would do.
	}};

}
