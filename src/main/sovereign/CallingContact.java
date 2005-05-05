package sovereign;

public class CallingContact extends ThreadLocal {

	public synchronized LifeView life() {
		return (LifeView)get();
	}

	public synchronized void life(LifeView life) {
		set(life);
	}
	
}
