package sneer.lego;

public interface Binder {
	
	Binder bind(Class<?> clazz);
	
	Binder to(Class<?> clazz);

	String lookup(Class<?> clazz);
}