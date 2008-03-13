package sneer.lego;

public interface Binder {
	
	Binder bind(Class<?> intrface);
	
	Binder to(Class<?> implementation);

	Binder toInstance(Object instance);

	String implementationFor(Class<?> intrface);

	Object instanceFor(Class<?> intrface);
}