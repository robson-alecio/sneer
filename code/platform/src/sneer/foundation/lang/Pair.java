package sneer.foundation.lang;

import java.io.Serializable;

public class Pair<A, B> implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
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
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	public final A a;
	public final B b;

	public Pair(A a_, B b_) {
		a = a_;
		b = b_;
	}

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return "Pair[" + a + ", " + b + "]";
	}

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<A, B>(a, b);
	}

	public static <A, B> Functor<Pair<A, B>, A> first() {
		return new Functor<Pair<A, B>, A>() {
			@Override public A evaluate(Pair<A, B> pair){
				return pair.a;
			}
		};
	}
	
	public static <A, B> Functor<Pair<A, B>, B> second() {
		return new Functor<Pair<A, B>, B>() {
			@Override public B evaluate(Pair<A, B> pair){
				return pair.b;
			}
		};
	}
}
