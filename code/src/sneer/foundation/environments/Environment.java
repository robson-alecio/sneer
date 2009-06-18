package sneer.foundation.environments;

public interface Environment {
	<T> T provide(Class<T> intrface);
}