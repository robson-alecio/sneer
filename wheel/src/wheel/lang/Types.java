package wheel.lang;

public class Types {

	@SuppressWarnings("unchecked")
	public static <T> T uncheckedGenericCast(Object object) {
		return (T)object;
	}

	static public boolean instanceOf(Object candidate, Class<?> type) {
		return type.isAssignableFrom(candidate.getClass());
	}

}
