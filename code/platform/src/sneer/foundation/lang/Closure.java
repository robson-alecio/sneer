package sneer.foundation.lang;

public interface Closure<X extends Throwable> {
	
	void run() throws X;

}
