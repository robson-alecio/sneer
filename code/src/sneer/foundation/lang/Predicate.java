package sneer.foundation.lang;

public interface Predicate<T> {

	boolean evaluate(T arg);

	Predicate<Object> TRUE = new Predicate<Object>() { @Override public boolean evaluate(Object ignored) { return true; }};
	Predicate<Object> FALSE = new Predicate<Object>() { @Override public boolean evaluate(Object ignored) { return false; }};

}
