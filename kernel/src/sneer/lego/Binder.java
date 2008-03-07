package sneer.lego;

public interface Binder {
	
	Binder bind(Class<?> intrface);
	
	Binder to(Class<?> implementation);

	String implementationFor(Class<?> intrface);
}