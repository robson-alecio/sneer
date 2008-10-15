package wheel.lang;

public class ByRef<T> {
	
	public static <T> ByRef<T> newInstance() {
		return new ByRef<T>();
	}
	
	private ByRef() {
	}
	
	volatile public T value;

}
