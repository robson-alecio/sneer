package wheel.lang;

public interface Predicate<T> {

	boolean evaluate(T arg);

	Predicate<Object> TRUE = new Predicate<Object>() { @Override public boolean evaluate(Object arg) {
		return true;
	}};

	Predicate<Object> FALSE = new Predicate<Object>() { @Override public boolean evaluate(Object arg) {
		return false;
	}};

}
