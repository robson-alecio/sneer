package spikes.wheel.lang;

@Deprecated
public class Types {

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object) {
		return (T)object;
	}

}
