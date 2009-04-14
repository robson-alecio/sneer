package sneer.commons.lang;

import java.io.Serializable;

import org.apache.commons.collections15.Transformer;

public class Pair<A, B> implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_a == null) ? 0 : _a.hashCode());
		result = prime * result + ((_b == null) ? 0 : _b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (_a == null) {
			if (other._a != null)
				return false;
		} else if (!_a.equals(other._a))
			return false;
		if (_b == null) {
			if (other._b != null)
				return false;
		} else if (!_b.equals(other._b))
			return false;
		return true;
	}

	public final A _a;
	public final B _b;

	public Pair(A a, B b) {
		_a = a;
		_b = b;
	}

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return "Pair[" + _a + ", " + _b + "]";
	}

	public static <A, B> Pair<A, B> pair(A a, B b) {
		return new Pair<A, B>(a, b);
	}

	public static <A, B> Transformer<Pair<A, B>, A> first() {
		return new Transformer<Pair<A, B>, A>() {
			@Override public A transform(Pair<A, B> pair){
				return pair._a;
			}
		};
	}
	
	public static <A, B> Transformer<Pair<A, B>, B> second() {
		return new Transformer<Pair<A, B>, B>() {
			@Override public B transform(Pair<A, B> pair){
				return pair._b;
			}
		};
	}
}
