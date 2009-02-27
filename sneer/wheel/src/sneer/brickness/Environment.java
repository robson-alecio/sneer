package sneer.brickness;

public interface Environment {
	<T> T provide(Class<T> intrface);
}