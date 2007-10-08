package wheel.lang;

import java.util.Date;

public class FrozenTime {
	
	private static final ThreadLocal<Long> FROZEN_MILLIS = new ThreadLocal<Long>();
	
	public static Date frozenDate() {
		return new Date(FROZEN_MILLIS.get());
	}
	
	public static long frozenTimeMillis() {
		return FROZEN_MILLIS.get();
	}

	public static void freezeForCurrentThread(long millis) {
		FROZEN_MILLIS.set(millis);
	}

}
