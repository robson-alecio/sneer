package sneer.commons.environments;

public interface Environment {
	<T> T provide(Class<T> intrface);
}