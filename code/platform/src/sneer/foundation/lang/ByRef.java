package sneer.foundation.lang;

public class ByRef<T> {
	
	public static <T> ByRef<T> newInstance() {
		return new ByRef<T>();
	}

	public static <T> ByRef<T> newInstance(T value_) {
		ByRef<T> result = newInstance();
		result.value = value_;
		return result;
	}
	
	private ByRef() {
	}
	
	volatile public T value;

}
