package wheel.lang;

public interface Functor<A, B> {

	B evaluate(A value);
	
	public static final Functor<Object, Object> SINGLETON_FUNCTOR = new Functor<Object, Object>() { @Override public Object evaluate(Object value) {
		return this; //Any single object would do.
	}};

}
