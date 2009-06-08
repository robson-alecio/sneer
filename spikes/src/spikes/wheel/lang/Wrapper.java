package spikes.wheel.lang;


public class Wrapper<T> {

	public Wrapper(T delegate) {
		if (delegate == null) throw new IllegalArgumentException();
		_delegate = delegate;
	}
	
	public final T _delegate;

	@Override
	public boolean equals(Object other) {
		return _delegate.equals(other);
	}

	@Override
	public int hashCode() {
		return _delegate.hashCode();
	}

}
